package com.queerlab.chat.view;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.adapter.ActivityAdapter;
import com.queerlab.chat.adapter.MapNearbyAdapter;
import com.queerlab.chat.app.MyApplication;
import com.queerlab.chat.base.BaseFragment;
import com.queerlab.chat.base.EmptyViewFactory;
import com.queerlab.chat.bean.ActivityListBean;
import com.queerlab.chat.bean.BaseBean;
import com.queerlab.chat.bean.LocationUserBean;
import com.queerlab.chat.dialog.AnimationDialog;
import com.queerlab.chat.dialog.PublicTextDialog;
import com.queerlab.chat.event.ClapEvent;
import com.queerlab.chat.event.LocationEvent;
import com.queerlab.chat.listener.OnCustomClickListener;
import com.queerlab.chat.tencent.TUIKitUtil;
import com.queerlab.chat.utils.LocationUtils;
import com.queerlab.chat.utils.PermissionUtils;
import com.queerlab.chat.utils.RefreshUtils;
import com.queerlab.chat.view.activity.ActivityDetailActivity;
import com.queerlab.chat.view.group.user.UserInfoActivity;
import com.queerlab.chat.view.map.MapNearbyActivity;
import com.queerlab.chat.viewmodel.ActivityViewModel;
import com.queerlab.chat.viewmodel.MapViewModel;
import com.queerlab.chat.viewmodel.UserViewModel;
import com.queerlab.chat.widget.popup.CommonPopupWindow;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.tencent.map.sdk.utilities.heatmap.HeatMapTileProvider;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptor;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.maps.model.TileOverlay;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.view
 * @ClassName: MapFragment
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 5/6/21 2:17 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/6/21 2:17 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class MapFragment extends BaseFragment implements HeatMapTileProvider.OnHeatMapReadyListener {
    @BindView(R.id.ll_list)
    LinearLayout llList;
    @BindView(R.id.ll_map)
    LinearLayout llMap;
    @BindView(R.id.tv_user)
    AppCompatTextView tvUser;
    @BindView(R.id.tv_activity)
    AppCompatTextView tvActivity;
    @BindView(R.id.map_view)
    MapView mapView;
    @BindView(R.id.refresh)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_activity)
    SmartRefreshLayout smartRefreshLayoutActivity;
    @BindView(R.id.rv_activity)
    RecyclerView recyclerViewActivity;
    TencentMap tencentMap;
    private TileOverlay tileOverlay;
    private MapViewModel mapViewModel;
    private UserViewModel userViewModel;
    private ActivityViewModel activityViewModel;
    private MapNearbyAdapter mapNearbyAdapter;
    private LocationEvent locationEvent;
    private CommonPopupWindow commonPopupWindow;

    @Override
    protected int initLayout() {
        return R.layout.fragment_map;
    }

    @Override
    public boolean isUseEventBus() {
        return true;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState, View view1) {
        initMap();
        mapViewModel = mActivity.getViewModel(MapViewModel.class);
        userViewModel = mActivity.getViewModel(UserViewModel.class);
        activityViewModel = mActivity.getViewModel(ActivityViewModel.class);

        locationEvent = new LocationEvent(0, 0);

        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mapNearbyAdapter = new MapNearbyAdapter(R.layout.adapter_map_nearby, null);
        recyclerView.setAdapter(mapNearbyAdapter);

        recyclerViewActivity.setLayoutManager(new LinearLayoutManager(mActivity));
        ActivityAdapter activityAdapter = new ActivityAdapter(R.layout.adapter_activity, RefreshUtils.getGroupListType());
        recyclerViewActivity.setAdapter(activityAdapter);

        activityAdapter.setOnItemClickListener((adapter, view, position) -> {
            ActivityUtils.startActivity(ActivityDetailActivity.class);
        });

        mapNearbyAdapter.setOnItemClickListener((adapter, view, position) -> {
            LocationUserBean.ListBean listBean = mapNearbyAdapter.getData().get(position);
            Bundle bundle = new Bundle();
            bundle.putString("userId", String.valueOf(listBean.getUser_id()));
            ActivityUtils.startActivity(bundle, UserInfoActivity.class);
        });

        //用户上传经纬度成功返回结果
        mapViewModel.updateLocationLiveData.observe(mActivity, s -> {
            LogUtils.e("updateLocationLiveData: 上传定位成功");
            LocationUtils.getInstance().stopLocalService();
        });

        //用户上传经纬度失败返回结果
        mapViewModel.failStateLiveData.observe(mActivity, s -> {
            LogUtils.e("updateLocationLiveData: 上传定位失败");
            LocationUtils.getInstance().stopLocalService();
        });

        //获取热力图列表数据返回结果
        mapViewModel.heatMapListLiveData.observe(mActivity, heatMapListBeans -> {
            tileOverlay = TUIKitUtil.getNetworkHeatMapList(heatMapListBeans,this, tencentMap, tileOverlay);
        });

        //根据定位获取用户列表成功返回结果
        mapViewModel.locationUserLiveData.observe(mActivity, locationUserBean -> {
            if (locationUserBean.getPageNum() == 1) {
                mapNearbyAdapter.setNewData(locationUserBean.getList());
                mapNearbyAdapter.setEmptyView(EmptyViewFactory.createEmptyView(mActivity, "这里荒无人烟"));
            } else {
                mapNearbyAdapter.addData(mapNearbyAdapter.getData().size(), locationUserBean.getList());
            }
            RefreshUtils.setNoMore(smartRefreshLayout,locationUserBean.getPageNum(), locationUserBean.getTotal());
        });

        //用户拍一拍成功返回
        userViewModel.updateUserClapLiveData.observe(mActivity, s -> {
            AnimationDialog animationDialog = new AnimationDialog(mActivity);
            animationDialog.showDialog();
        });

        //获取经纬度马克点
        activityViewModel.locationActivityLiveData.observe(mActivity, activityListBean -> {
            setMarker(activityListBean.getList());
        });

        //刷新和加载
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (locationEvent.getLongitude() == 0){
                    refreshLayout.finishRefresh();
                    return;
                }
                mapViewModel.locationUser(String.valueOf(locationEvent.getLongitude()), String.valueOf(locationEvent.getLatitude()));
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (locationEvent.getLongitude() == 0){
                    refreshLayout.finishLoadMore();
                    return;
                }
                mapViewModel.locationUserMore(String.valueOf(locationEvent.getLongitude()), String.valueOf(locationEvent.getLatitude()));
            }
        });

        smartRefreshLayoutActivity.setEnableRefresh(false);
        smartRefreshLayoutActivity.setEnableLoadMore(false);
    }

    @Override
    protected void initDataFromService() {
//        tileOverlay = TUIKitUtil.getLocalHeatMapList(mActivity,this, tencentMap, tileOverlay);
        initMap();
        mapViewModel.heatMapList();
    }

    //地图初始化
    private void initMap() {
        //获取地图实例
        tencentMap = mapView.getMap();
        //设置是否开启地图倾斜手势
        tencentMap.getUiSettings().setTiltGesturesEnabled(false);
        //设置显示定位的图标
        tencentMap.getUiSettings().setMyLocationButtonEnabled(false);
        //设置地图最小缩放级别
//        tencentMap.setMinZoomLevel(11);
        //设置地图最大缩放级别
        tencentMap.setMaxZoomLevel(12);
        //支持3d建筑物
        tencentMap.setBuildingEnable(true);
        //点击地图监听
        tencentMap.setOnMapClickListener(latLng -> {
            Bundle bundle = new Bundle();
            bundle.putDouble("longitude", latLng.longitude);
            bundle.putDouble("latitude", latLng.latitude);
            ActivityUtils.startActivity(bundle, MapNearbyActivity.class);
        });

        //地图平滑移动监听
        tencentMap.setOnCameraChangeListener(new TencentMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinished(CameraPosition cameraPosition) {
                clearPopupWindow();
                activityViewModel.locationActivity(String.valueOf(cameraPosition.target.longitude),
                        String.valueOf(cameraPosition.target.latitude), 100);
            }
        });

        ///马克点点击监听
        tencentMap.setOnMarkerClickListener(marker -> {
//            ToastUtils.showShort("马克被电击" + marker.getId() + marker.getTitle() + marker.getTag());
            showPopWindow();
            return false;
        });
    }

    /**
     * 判断定位权限是否开启
     */
    private void isLocationPermission(){
        if (PermissionUtils.isCheckPermission(mActivity)){
            LocationUtils.getInstance().startLocalService(tencentMap);
        }else {
            mapNearbyAdapter.setEmptyView(EmptyViewFactory.createEmptyCallView(mActivity,
                    "您未开启定位权限，", "点击开启", new EmptyViewFactory.EmptyViewCallBack() {
                @Override
                public void onEmptyClick() {
                    isLocationPermission();
                }
            }));
            PublicTextDialog publicTextDialog = new PublicTextDialog(mActivity, "同意获取地理位置");
            publicTextDialog.showDialog();
            publicTextDialog.setOnCustomClickListener(new OnCustomClickListener() {
                @Override
                public void onSuccess() {
                    PermissionUtils.getOpenLocationPermission(mActivity, tencentMap);
                }

                @Override
                public void onCancel() {
                    tencentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new com.tencent.tencentmap.mapsdk.maps.model.LatLng(39.915119, 116.403963), 10.946870f));
                }
            });
        }
    }

    ///设置马克点
    public Marker mCustomMarker;
    private void setMarker(List<ActivityListBean.ListBean> activityList) {
        ArrayList<MarkerOptions> markers = new ArrayList<>();
        for (ActivityListBean.ListBean activityListBean : activityList) {
            BitmapDescriptor custom = BitmapDescriptorFactory.fromResource(RefreshUtils.getMarkerDrawable(activityListBean.getIconName()));
            MarkerOptions markerOptions = new MarkerOptions(new LatLng(activityListBean.getLat(), activityListBean.getLng()))
                    .icon(custom)//添加马克点自定义icon
                    .title(activityListBean.getTitle())//马克点点击显示title
                    .tag(activityListBean.getId())//马克点主键
                    .flat(true);//马克点是否支持3d
            markers.add(markerOptions);
            mCustomMarker = tencentMap.addMarker(markerOptions);
            mCustomMarker.setClickable(true);
            mCustomMarker.hideInfoWindow();
        }
    }

    /**
     * 弹出活动弹窗
     */
    private void showPopWindow() {
        if (commonPopupWindow == null) {
            commonPopupWindow = new CommonPopupWindow.Builder(mActivity)
                    .setView(R.layout.dialog_custom_marker)
                    .setOutsideTouchable(false)
                    .setBackGroundLevel(1f)
                    .setWidthAndHeight(ConvertUtils.dp2px(320), ViewGroup.LayoutParams.WRAP_CONTENT)
                    .setAnimationStyle(R.style.DialogAnimBottom)
                    .setViewOnclickListener((view, layoutResId) -> {
//                        tvTitle = view.findViewById(R.id.tv_title);

                        view.setOnClickListener(v -> {
                            ActivityUtils.startActivity(ActivityDetailActivity.class);
                        });
                    })
                    .create();
        }
//        tvTitle.setText(title);
        if (!commonPopupWindow.isShowing()) {
            commonPopupWindow.showAtLocation(mActivity.findViewById(android.R.id.content), Gravity.BOTTOM, 0, ConvertUtils.dp2px(65));
        }
    }

    /**
     * 清楚弹窗
     */
    private void clearPopupWindow() {
        if (commonPopupWindow != null) {
            if (commonPopupWindow.isShowing()) {
                commonPopupWindow.dismiss();
            }
            commonPopupWindow = null;
        }

        if (mCustomMarker != null && mCustomMarker.isInfoWindowShown()){
            mCustomMarker.hideInfoWindow();
        }
    }

    @OnClick({R.id.tv_screen, R.id.tv_map, R.id.tv_user, R.id.tv_activity})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_screen://列表模式
                llList.setVisibility(View.VISIBLE);
                llMap.setVisibility(View.GONE);
                clearPopupWindow();
                break;
            case R.id.tv_map://探索模式
                llList.setVisibility(View.GONE);
                llMap.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_user://用户
                tvUser.setBackgroundResource(R.drawable.shape_yellow_25);
                tvUser.setTextColor(getResources().getColor(R.color.white));
                tvActivity.setBackgroundResource(R.drawable.shape_gray_25);
                tvActivity.setTextColor(getResources().getColor(R.color.color_D2D2D2));
                smartRefreshLayout.setVisibility(View.VISIBLE);
                smartRefreshLayoutActivity.setVisibility(View.GONE);
                break;
            case R.id.tv_activity://活动
                tvUser.setBackgroundResource(R.drawable.shape_gray_25);
                tvUser.setTextColor(getResources().getColor(R.color.color_D2D2D2));
                tvActivity.setBackgroundResource(R.drawable.shape_yellow_25);
                tvActivity.setTextColor(getResources().getColor(R.color.white));
                smartRefreshLayout.setVisibility(View.GONE);
                smartRefreshLayoutActivity.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null){
            mapView.onResume();
        }

        isLocationPermission();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null){
            mapView.onDestroy();
            mapView = null;
        }

        if (PermissionUtils.isCheckPermission(mActivity)){
            LocationUtils.getInstance().stopLocalService();
        }

        clearPopupWindow();
    }

    /**
     * 定位开启成功
     *
     * @param locationEvent
     */
    @Subscribe
    public void onEventLocation(LocationEvent locationEvent) {
        this.locationEvent = locationEvent;
        switch (locationEvent.getStatus()){
            case "success":
                mapViewModel.updateLocation(String.valueOf(locationEvent.getLongitude()), String.valueOf(locationEvent.getLatitude()));
                MyApplication.getMainThreadHandler().postDelayed(() -> {
                    mapViewModel.locationUser(String.valueOf(locationEvent.getLongitude()), String.valueOf(locationEvent.getLatitude()));
                    activityViewModel.locationActivity(String.valueOf(locationEvent.getLongitude()),
                            String.valueOf(locationEvent.getLatitude()), 100);
                }, 1000);
                break;
            case "refuse":
                tencentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new com.tencent.tencentmap.mapsdk.maps.model.LatLng(39.915119, 116.403963), 4.946870f));
                break;
        }
    }

    /**
     * 双击头像拍一拍
     *
     * @param event
     */
    @Subscribe
    public void onEventUpdateClap(ClapEvent event){
        if (event.getStatus().equals("map")){
            userViewModel.updateUserClap(event.getUserId());
        }
    }

    @Override
    public void onHeatMapReady() {
        tileOverlay.clearTileCache();
        tileOverlay.reload();
    }
}
