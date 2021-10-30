package com.queerlab.chat.view.map;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.adapter.MapNearbyAdapter;
import com.queerlab.chat.base.BaseActivity;
import com.queerlab.chat.base.EmptyViewFactory;
import com.queerlab.chat.bean.LocationUserBean;
import com.queerlab.chat.dialog.AnimationDialog;
import com.queerlab.chat.event.ClapEvent;
import com.queerlab.chat.utils.RefreshUtils;
import com.queerlab.chat.view.group.user.UserInfoActivity;
import com.queerlab.chat.viewmodel.MapViewModel;
import com.queerlab.chat.viewmodel.UserViewModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.view.map
 * @ClassName: MapNearbyActivity
 * @Description: 地图附近的人
 * @Author: 鹿鸿祥
 * @CreateDate: 5/13/21 2:04 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/13/21 2:04 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class MapNearbyActivity extends BaseActivity {
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.refresh)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    private MapViewModel mapViewModel;
    private UserViewModel userViewModel;

    @Override
    protected int initLayoutRes() {
        return R.layout.activity_map_nearby;
    }

    @Override
    public boolean isUseEventBus() {
        return true;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(fakeStatusBar, getResources().getColor(R.color.white));
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        MapNearbyAdapter mapNearbyAdapter = new MapNearbyAdapter(R.layout.adapter_map_nearby, null);
        recyclerView.setAdapter(mapNearbyAdapter);

        mapNearbyAdapter.setOnItemClickListener((adapter, view, position) -> {
            LocationUserBean.ListBean listBean = mapNearbyAdapter.getData().get(position);
            Bundle bundle = new Bundle();
            bundle.putString("userId", String.valueOf(listBean.getUser_id()));
            ActivityUtils.startActivity(bundle, UserInfoActivity.class);
        });

        mapViewModel = getViewModel(MapViewModel.class);
        userViewModel = getViewModel(UserViewModel.class);

        //根据定位获取用户列表成功返回结果
        mapViewModel.locationUserLiveData.observe(activity, locationUserBean -> {
            if (locationUserBean.getPageNum() == 1) {
                mapNearbyAdapter.setNewData(locationUserBean.getList());
                mapNearbyAdapter.setEmptyView(EmptyViewFactory.createEmptyView(activity, getString(R.string.not_empty_location)));
            } else {
                mapNearbyAdapter.addData(mapNearbyAdapter.getData().size(), locationUserBean.getList());
            }
            RefreshUtils.setNoMore(smartRefreshLayout,locationUserBean.getPageNum(), locationUserBean.getTotal());
        });

        //用户拍一拍成功返回
        userViewModel.updateUserClapLiveData.observe(activity, s -> {
            AnimationDialog animationDialog = new AnimationDialog(activity);
            animationDialog.showDialog();
        });

        //刷新和加载
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mapViewModel.locationUser(String.valueOf(getIntent().getDoubleExtra("longitude", 0)), String.valueOf(getIntent().getDoubleExtra("latitude", 0)));
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mapViewModel.locationUserMore(String.valueOf(getIntent().getDoubleExtra("longitude", 0)), String.valueOf(getIntent().getDoubleExtra("latitude", 0)));
            }
        });

        mapViewModel.locationUser(String.valueOf(getIntent().getDoubleExtra("longitude", 0)), String.valueOf(getIntent().getDoubleExtra("latitude", 0)));
    }

    /**
     * 双击头像拍一拍
     * @param event
     */
    @Subscribe
    public void onEventUpdateClap(ClapEvent event){
        if (event.getStatus().equals("map")){
            userViewModel.updateUserClap(event.getUserId());
        }
    }

    @OnClick({R.id.iv_bar_back})
    void onClick() {
        finish();
    }
}
