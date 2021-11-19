package com.queerlab.chat.view;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.adapter.ActivityAdapter;
import com.queerlab.chat.adapter.BannerExampleAdapter;
import com.queerlab.chat.adapter.GroupTypeAdapter;
import com.queerlab.chat.base.BaseFragment;
import com.queerlab.chat.base.EmptyViewFactory;
import com.queerlab.chat.bean.ActivityListBean;
import com.queerlab.chat.bean.GroupTypeBean;
import com.queerlab.chat.utils.RefreshUtils;
import com.queerlab.chat.view.activity.ActivityDetailActivity;
import com.queerlab.chat.view.login.FirstNameActivity;
import com.queerlab.chat.view.search.SearchActivity;
import com.queerlab.chat.viewmodel.ActivityViewModel;
import com.queerlab.chat.widget.HorizontalItemDecoration;
import com.queerlab.chat.widget.PictureEnlargeUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;

import butterknife.BindView;
import butterknife.OnClick;

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
    private String classId;

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

        recyclerViewType.setLayoutManager(new LinearLayoutManager(mActivity ,LinearLayoutManager.HORIZONTAL,false));
        recyclerViewType.addItemDecoration(new HorizontalItemDecoration(10, mActivity));
        GroupTypeAdapter groupTypeAdapter = new GroupTypeAdapter(R.layout.adapter_group_type, null);
        recyclerViewType.setAdapter(groupTypeAdapter);

        groupTypeAdapter.setOnItemClickListener((adapter, view1, position) -> {
            GroupTypeBean.ListBean listBean = groupTypeAdapter.getData().get(position);
            groupTypeAdapter.setIsSelect(listBean.getClass_name());
            classId = listBean.getClass_id();
            activityViewModel.activityList(classId);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        ActivityAdapter activityAdapter = new ActivityAdapter(R.layout.adapter_activity, null);
        recyclerView.setAdapter(activityAdapter);

        activityAdapter.setOnItemClickListener((adapter, view1, position) -> {
            ActivityListBean.ListBean listBean = activityAdapter.getData().get(position);
            Bundle bundle = new Bundle();
            bundle.putString("activityId", String.valueOf(listBean.getId()));
            ActivityUtils.startActivity(bundle, ActivityDetailActivity.class);
        });

        activityViewModel = mActivity.getViewModel(ActivityViewModel.class);

        //获取活动轮播列表
        activityViewModel.activityBannerLiveData.observe(mActivity, activityBannerBeans -> {
            banner.addBannerLifecycleObserver(this)//添加生命周期观察者
                    .setAdapter(new BannerExampleAdapter(mActivity, activityBannerBeans))//添加数据
                    .setIndicator(new CircleIndicator(mActivity), true)
                    .setIndicatorSelectedColor(mActivity.getResources().getColor(R.color.color_FFA52C))
                    .setIndicatorNormalColor(mActivity.getResources().getColor(R.color.white))
                    .setOnBannerListener((data, position) -> {
                        Bundle bundle = new Bundle();
                        bundle.putString("activityId", String.valueOf(activityBannerBeans.get(position).getId()));
                        ActivityUtils.startActivity(bundle, ActivityDetailActivity.class);
                    })
                    .start();
        });

        //获取活动类型列表返回数据
        activityViewModel.activityTypeLiveData.observe(mActivity, groupTypeBean -> {
            classId = groupTypeBean.getList().get(0).getClass_id();
            groupTypeAdapter.setIsSelect(groupTypeBean.getList().get(0).getClass_name());
            groupTypeAdapter.setNewData(groupTypeBean.getList());
            activityViewModel.activityList(classId);
        });

        //获取活动列表成功返回结果
        activityViewModel.activityListLiveData.observe(mActivity, activityListBean -> {
            isInited = true;
            if (activityListBean.getPageNum() == 1) {
                activityAdapter.setNewData(activityListBean.getList());
                activityAdapter.setEmptyView(EmptyViewFactory.createEmptyView(mActivity, getString(R.string.not_empty_activity)));
            } else {
                activityAdapter.addData(activityAdapter.getData().size(), activityListBean.getList());
            }
            RefreshUtils.setNoMore(smartRefreshLayout, activityListBean.getPageNum(), activityListBean.getTotal());
        });

        //刷新和加载
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                activityViewModel.activityBanner();
                activityViewModel.activityList(classId);
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                activityViewModel.activityListMore(classId);
            }
        });
    }

    @Override
    protected void initDataFromService() {
        activityViewModel.activityBanner();
        activityViewModel.getActivityType();
    }

    @OnClick({R.id.tv_search})
    void onClick() {
        ActivityUtils.startActivity(SearchActivity.class);
    }
}
