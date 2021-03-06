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
import com.queerlab.chat.bean.ActivityDetailBean;
import com.queerlab.chat.bean.ActivityListBean;
import com.queerlab.chat.bean.LocationUserBean;
import com.queerlab.chat.bean.MarkerActivityBean;
import com.queerlab.chat.dialog.AnimationDialog;
import com.queerlab.chat.dialog.PublicTextDialog;
import com.queerlab.chat.event.ClapEvent;
import com.queerlab.chat.event.LocationEvent;
import com.queerlab.chat.listener.OnCustomClickListener;
import com.queerlab.chat.tencent.TUIKitUtil;
import com.queerlab.chat.utils.DrawableUtils;
import com.queerlab.chat.utils.LocationUtils;
import com.queerlab.chat.utils.PermissionUtils;
import com.queerlab.chat.utils.PictureUtils;
import com.queerlab.chat.utils.RefreshUtils;
import com.queerlab.chat.view.activity.ActivityDetailActivity;
import com.queerlab.chat.view.group.user.UserInfoActivity;
import com.queerlab.chat.view.map.MapNearbyActivity;
import com.queerlab.chat.view.search.SearchActivity;
import com.queerlab.chat.viewmodel.ActivityViewModel;
import com.queerlab.chat.viewmodel.MapViewModel;
import com.queerlab.chat.viewmodel.NewGroupViewModel;
import com.queerlab.chat.viewmodel.UserViewModel;
import com.queerlab.chat.widget.CornerImageView;
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
 * @Description: java???????????????
 * @Author: ?????????
 * @CreateDate: 5/6/21 2:17 PM
 * @UpdateUser: ?????????
 * @UpdateDate: 5/6/21 2:17 PM
 * @UpdateRemark: ????????????
 * @Version: 1.0
 */
public class MapFragment extends BaseFragment implements HeatMapTileProvider.OnHeatMapReadyListener {
    @BindView(R.id.tv_switch_user)
    AppCompatTextView tvSwitchUser;
    @BindView(R.id.tv_switch_activity)
    AppCompatTextView tvSwitchActivity;
    @BindView(R.id.ll_list)
    LinearLayout llList;
    @BindView(R.id.map_view)
    MapView mapView;
    @BindView(R.id.ll_activity)
    LinearLayout llActivity;
    @BindView(R.id.tv_map)
    AppCompatTextView tvMap;
    @BindView(R.id.tv_user)
    AppCompatTextView tvUser;
    @BindView(R.id.tv_activity)
    AppCompatTextView tvActivity;
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
    private NewGroupViewModel newGroupViewModel;
    private MapNearbyAdapter mapNearbyAdapter;
    private ActivityAdapter activityAdapter;
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
        newGroupViewModel = mActivity.getViewModel(NewGroupViewModel.class);

        locationEvent = new LocationEvent(0, 0);

        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mapNearbyAdapter = new MapNearbyAdapter(R.layout.adapter_map_nearby, null);
        recyclerView.setAdapter(mapNearbyAdapter);

        recyclerViewActivity.setLayoutManager(new LinearLayoutManager(mActivity));
        activityAdapter = new ActivityAdapter(R.layout.adapter_activity, null);
        recyclerViewActivity.setAdapter(activityAdapter);

        activityAdapter.setOnItemClickListener((adapter, view, position) -> {
            ActivityListBean.ListBean listBean = activityAdapter.getData().get(position);
            Bundle bundle = new Bundle();
            bundle.putString("activityId", String.valueOf(listBean.getId()));
            ActivityUtils.startActivity(bundle, ActivityDetailActivity.class);
        });

        mapNearbyAdapter.setOnItemClickListener((adapter, view, position) -> {
            LocationUserBean.ListBean listBean = mapNearbyAdapter.getData().get(position);
            Bundle bundle = new Bundle();
            bundle.putString("userId", String.valueOf(listBean.getUser_id()));
            ActivityUtils.startActivity(bundle, UserInfoActivity.class);
        });

        //???????????????????????????????????????
        mapViewModel.updateLocationLiveData.observe(mActivity, s -> {
            LogUtils.e("updateLocationLiveData: ??????????????????");
            LocationUtils.getInstance().stopLocalService();
        });

        //???????????????????????????????????????
        mapViewModel.failStateLiveData.observe(mActivity, s -> {
            LogUtils.e("updateLocationLiveData: ??????????????????");
            LocationUtils.getInstance().stopLocalService();
        });

        //???????????????????????????????????????
        mapViewModel.heatMapListLiveData.observe(mActivity, heatMapListBeans -> {
            tileOverlay = TUIKitUtil.getNetworkHeatMapList(heatMapListBeans,this, tencentMap, tileOverlay);
        });

        //????????????????????????????????????????????????
        mapViewModel.locationUserLiveData.observe(mActivity, locationUserBean -> {
            if (locationUserBean.getPageNum() == 1) {
                mapNearbyAdapter.setNewData(locationUserBean.getList());
                mapNearbyAdapter.setEmptyView(EmptyViewFactory.createEmptyView(mActivity, "??????????????????"));
            } else {
                mapNearbyAdapter.addData(mapNearbyAdapter.getData().size(), locationUserBean.getList());
            }
            RefreshUtils.setNoMore(smartRefreshLayout,locationUserBean.getPageNum(), locationUserBean.getTotal());
        });

        //???????????????????????????
        userViewModel.updateUserClapLiveData.observe(mActivity, s -> {
            AnimationDialog animationDialog = new AnimationDialog(mActivity);
            animationDialog.showDialog();
        });

        //????????????????????????????????????????????????
        activityViewModel.locationActivityLiveData.observe(mActivity, activityListBean -> {
            if (activityListBean.getPageNum() == 1) {
                activityAdapter.setNewData(activityListBean.getList());
                activityAdapter.setEmptyView(EmptyViewFactory.createEmptyView(mActivity, "??????????????????"));
            } else {
                activityAdapter.addData(activityAdapter.getData().size(), activityListBean.getList());
            }
            RefreshUtils.setNoMore(smartRefreshLayoutActivity, activityListBean.getPageNum(), activityListBean.getTotal());
        });

        //????????????????????????
        activityViewModel.markerActivityLiveData.observe(mActivity, activityListBean -> {
            setMarker(activityListBean);
        });

        //??????????????????
        newGroupViewModel.activityDetailLiveData.observe(mActivity, activityDetailBean -> {
            showPopWindow(activityDetailBean);
        });

        //???????????????
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

        //???????????????
        smartRefreshLayoutActivity.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (locationEvent.getLongitude() == 0){
                    refreshLayout.finishRefresh();
                    return;
                }
                activityViewModel.locationActivity(String.valueOf(locationEvent.getLongitude()), String.valueOf(locationEvent.getLatitude()));
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (locationEvent.getLongitude() == 0){
                    refreshLayout.finishLoadMore();
                    return;
                }
                activityViewModel.locationActivityMore(String.valueOf(locationEvent.getLongitude()), String.valueOf(locationEvent.getLatitude()));
            }
        });
    }

    @Override
    protected void initDataFromService() {
//        tileOverlay = TUIKitUtil.getLocalHeatMapList(mActivity,this, tencentMap, tileOverlay);
        initMap();
        mapViewModel.heatMapList();
    }

    //???????????????
    private void initMap() {
        //??????????????????
        tencentMap = mapView.getMap();
        //????????????????????????????????????
        tencentMap.getUiSettings().setTiltGesturesEnabled(false);
        //???????????????????????????
        tencentMap.getUiSettings().setMyLocationButtonEnabled(false);
        //??????????????????????????????
//        tencentMap.setMinZoomLevel(11);
        //??????????????????????????????
        tencentMap.setMaxZoomLevel(12);
        //??????3d?????????
        tencentMap.setBuildingEnable(true);
        //??????????????????
        tencentMap.setOnMapClickListener(latLng -> {
            Bundle bundle = new Bundle();
            bundle.putDouble("longitude", latLng.longitude);
            bundle.putDouble("latitude", latLng.latitude);
            ActivityUtils.startActivity(bundle, MapNearbyActivity.class);
        });

        //????????????????????????
        tencentMap.setOnCameraChangeListener(new TencentMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinished(CameraPosition cameraPosition) {
                clearPopupWindow();
                activityViewModel.markerActivity(String.valueOf(cameraPosition.target.longitude), String.valueOf(cameraPosition.target.latitude));
            }
        });

        ///?????????????????????
        tencentMap.setOnMarkerClickListener(marker -> {
            newGroupViewModel.activityDetail(String.valueOf(marker.getTag()));
            return false;
        });
    }

    /**
     * ??????????????????????????????
     */
    private void isLocationPermission(){
        if (PermissionUtils.isCheckPermission(mActivity)){
            LocationUtils.getInstance().startLocalService(tencentMap);
        }else {
            mapNearbyAdapter.setEmptyView(EmptyViewFactory.createEmptyCallView(mActivity,
                    "???????????????????????????", "????????????", new EmptyViewFactory.EmptyViewCallBack() {
                @Override
                public void onEmptyClick() {
                    isLocationPermission();
                }
            }));
            activityAdapter.setEmptyView(EmptyViewFactory.createEmptyCallView(mActivity,
                    "???????????????????????????", "????????????", new EmptyViewFactory.EmptyViewCallBack() {
                        @Override
                        public void onEmptyClick() {
                            isLocationPermission();
                        }
                    }));
            PublicTextDialog publicTextDialog = new PublicTextDialog(mActivity, "????????????????????????");
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

    ///???????????????
    public Marker mCustomMarker;
    private void setMarker(List<MarkerActivityBean> activityList) {
        ArrayList<MarkerOptions> markers = new ArrayList<>();
        markers.clear();

        if (mCustomMarker != null){
            mCustomMarker.remove();
        }
        for (MarkerActivityBean markerActivityBean : activityList) {
            BitmapDescriptor custom = BitmapDescriptorFactory.fromResource(
                    RefreshUtils.getMarkerDrawable(markerActivityBean.getIconName()));
            MarkerOptions markerOptions = new MarkerOptions(new LatLng(markerActivityBean.getLat(), markerActivityBean.getLng()))
                    .icon(custom)//????????????????????????icon
//                    .title(markerActivityBean.getTitle())//?????????????????????title
                    .tag(markerActivityBean.getId())//???????????????
                    .flat(true);//?????????????????????3d
            markers.add(markerOptions);
            mCustomMarker = tencentMap.addMarker(markerOptions);
            mCustomMarker.setClickable(true);
        }
    }

    /**
     * ??????????????????
     */
    CornerImageView cornerImageView;
    AppCompatTextView tvTitle;
    AppCompatTextView tv_address;
    AppCompatTextView tv_initiator;
    private void showPopWindow(ActivityDetailBean activityDetailBean) {
        if (commonPopupWindow == null) {
            commonPopupWindow = new CommonPopupWindow.Builder(mActivity)
                    .setView(R.layout.dialog_custom_marker)
                    .setOutsideTouchable(false)
                    .setBackGroundLevel(1f)
                    .setWidthAndHeight(ConvertUtils.dp2px(320), ViewGroup.LayoutParams.WRAP_CONTENT)
                    .setAnimationStyle(R.style.DialogAnimBottom)
                    .setViewOnclickListener((view, layoutResId) -> {
                        cornerImageView = view.findViewById(R.id.iv_avatar);
                        tvTitle = view.findViewById(R.id.tv_title);
                        tv_address = view.findViewById(R.id.tv_address);
                        tv_initiator = view.findViewById(R.id.tv_initiator);
                        view.setOnClickListener(v -> {
                            Bundle bundle = new Bundle();
                            bundle.putString("activityId", activityDetailBean.getId());
                            ActivityUtils.startActivity(bundle, ActivityDetailActivity.class);
                        });
                    })
                    .create();
        }
        DrawableUtils.setDrawableLeft(mContext, tv_address, R.drawable.icon_address, 35, 35);
        DrawableUtils.setDrawableLeft(mContext, tv_initiator, R.drawable.icon_initiator, 35, 35);
        PictureUtils.setImage(mActivity, activityDetailBean.getImage(), cornerImageView);
        tvTitle.setText(activityDetailBean.getTitle());
        tv_address.setText(activityDetailBean.getPlace());
        tv_initiator.setText(activityDetailBean.getPosition());
        if (!commonPopupWindow.isShowing()) {
            commonPopupWindow.showAtLocation(mActivity.findViewById(android.R.id.content), Gravity.BOTTOM, 0, ConvertUtils.dp2px(65));
        }
    }

    /**
     * ????????????
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

    @OnClick({R.id.tv_search, R.id.tv_switch_user, R.id.tv_switch_activity,  R.id.tv_user, R.id.tv_activity})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_search://??????
                ActivityUtils.startActivity(SearchActivity.class);
                break;
            case R.id.tv_switch_user://????????????
                llActivity.setVisibility(View.VISIBLE);
                llList.setVisibility(View.VISIBLE);
                tvMap.setVisibility(View.INVISIBLE);
                mapView.setVisibility(View.GONE);
                tvSwitchUser.setVisibility(View.GONE);
                tvSwitchActivity.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_switch_activity://????????????
                llActivity.setVisibility(View.INVISIBLE);
                llList.setVisibility(View.GONE);
                tvMap.setVisibility(View.VISIBLE);
                mapView.setVisibility(View.VISIBLE);
                clearPopupWindow();
                tvSwitchUser.setVisibility(View.VISIBLE);
                tvSwitchActivity.setVisibility(View.GONE);
                break;
            case R.id.tv_user://??????
                tvUser.setBackgroundResource(R.drawable.shape_yellow_25);
                tvUser.setTextColor(getResources().getColor(R.color.white));
                tvActivity.setBackgroundResource(R.drawable.shape_gray_25);
                tvActivity.setTextColor(getResources().getColor(R.color.color_D2D2D2));
                smartRefreshLayout.setVisibility(View.VISIBLE);
                smartRefreshLayoutActivity.setVisibility(View.GONE);
                break;
            case R.id.tv_activity://??????
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
     * ??????????????????
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
                    activityViewModel.markerActivity(String.valueOf(locationEvent.getLongitude()), String.valueOf(locationEvent.getLatitude()));
                    activityViewModel.locationActivity(String.valueOf(locationEvent.getLongitude()), String.valueOf(locationEvent.getLatitude()));
                }, 1000);
                break;
            case "refuse":
                tencentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new com.tencent.tencentmap.mapsdk.maps.model.LatLng(39.915119, 116.403963), 4.946870f));
                break;
        }
    }

    /**
     * ?????????????????????
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
