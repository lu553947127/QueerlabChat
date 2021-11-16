package com.queerlab.chat.view;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.adapter.ActivityAdapter;
import com.queerlab.chat.adapter.BannerExampleAdapter;
import com.queerlab.chat.adapter.GroupTypeAdapter;
import com.queerlab.chat.base.BaseFragment;
import com.queerlab.chat.utils.RefreshUtils;
import com.queerlab.chat.view.activity.ActivityDetailActivity;
import com.queerlab.chat.viewmodel.ActivityViewModel;
import com.queerlab.chat.widget.HorizontalItemDecoration;
import com.queerlab.chat.widget.PictureEnlargeUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;

import butterknife.BindView;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.view.start
 * @ClassName: ActivityFragment
 * @Description: 活动fragment
 * @Author: 鹿鸿祥
 * @CreateDate: 2021/10/20 09:31
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/10/20 09:31
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ActivityFragment extends BaseFragment {
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.refresh)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.rv_type)
    RecyclerView recyclerViewType;
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    private ActivityViewModel activityViewModel;

    @Override
    protected int initLayout() {
        return R.layout.fragment_active;
    }

    @Override
    public boolean isUseEventBus() {
        return false;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState, View view) {

        banner.addBannerLifecycleObserver(this)//添加生命周期观察者
                .setAdapter(new BannerExampleAdapter(mActivity, RefreshUtils.getImagesList()))//添加数据
                .setIndicator(new CircleIndicator(mActivity), true)
                .setIndicatorSelectedColor(mActivity.getResources().getColor(R.color.color_FFA52C))
                .setIndicatorNormalColor(mActivity.getResources().getColor(R.color.white))
                .setOnBannerListener((data, position) -> {
                    PictureEnlargeUtils.getPictureEnlargeList(mActivity, RefreshUtils.getImagesList(), position);
                })
                .start();

        recyclerViewType.setLayoutManager(new LinearLayoutManager(mActivity ,LinearLayoutManager.HORIZONTAL,false));
        recyclerViewType.addItemDecoration(new HorizontalItemDecoration(10, mActivity));
        GroupTypeAdapter groupTypeAdapter = new GroupTypeAdapter(R.layout.adapter_group_type, null);
        recyclerViewType.setAdapter(groupTypeAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        ActivityAdapter activityAdapter = new ActivityAdapter(R.layout.adapter_activity, RefreshUtils.getGroupListType());
        recyclerView.setAdapter(activityAdapter);

        activityAdapter.setOnItemClickListener((adapter, view1, position) -> {
            ActivityUtils.startActivity(ActivityDetailActivity.class);
        });

        smartRefreshLayout.setEnableRefresh(false);
        smartRefreshLayout.setEnableLoadMore(false);

        activityViewModel = mActivity.getViewModel(ActivityViewModel.class);

        //获取活动类型列表返回数据
        activityViewModel.activityTypeLiveData.observe(mActivity, groupTypeBean -> {
            groupTypeAdapter.setIsSelect(groupTypeBean.getList().get(0).getClass_name());
            groupTypeAdapter.setNewData(groupTypeBean.getList());
        });
    }

    @Override
    protected void initDataFromService() {
        activityViewModel.getActivityType();
    }
}
