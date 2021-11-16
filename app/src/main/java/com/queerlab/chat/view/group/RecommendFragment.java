package com.queerlab.chat.view.group;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.queerlab.chat.R;
import com.queerlab.chat.adapter.GroupListAdapter;
import com.queerlab.chat.base.BaseLazyFragment;
import com.queerlab.chat.base.EmptyViewFactory;
import com.queerlab.chat.bean.GroupListBean;
import com.queerlab.chat.event.GroupEvent;
import com.queerlab.chat.listener.OnCustomCallBack;
import com.queerlab.chat.tencent.TUIKitUtil;
import com.queerlab.chat.utils.AnimatorUtils;
import com.queerlab.chat.utils.RefreshUtils;
import com.queerlab.chat.viewmodel.GroupViewModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.view.group
 * @ClassName: RecommendFragment
 * @Description: 小组推荐直播fragment
 * @Author: 鹿鸿祥
 * @CreateDate: 5/11/21 9:16 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/11/21 9:16 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class RecommendFragment extends BaseLazyFragment {
    @BindView(R.id.refresh)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    AppCompatTextView tvAdd;
    private GroupViewModel groupViewModel;

    @Override
    protected int initLayout() {
        return R.layout.fragment_recommend;
    }

    @Override
    public boolean isUseEventBus() {
        return true;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState) {
        tvAdd = mActivity.findViewById(R.id.tv_add);

        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        GroupListAdapter groupListAdapter = new GroupListAdapter(R.layout.adapter_group_list, null);
        recyclerView.setAdapter(groupListAdapter);

        groupListAdapter.setOnItemClickListener((adapter, view, position) -> {
            GroupListBean.ListBean listBean = groupListAdapter.getData().get(position);
            TUIKitUtil.enterGroup(mActivity, listBean, new OnCustomCallBack() {
                @Override
                public void onSuccess(Object data) {
                    groupViewModel.groupJoin(listBean.getGroup_no());
                    TUIKitUtil.startChatActivity(mActivity, true, listBean.getGroup_no(), listBean.getGroup_type() + listBean.getGroup_name(), listBean.getRoomId());
                }

                @Override
                public void onError(String module, int errCode, String errMsg) {

                }
            });
        });

        groupViewModel = mActivity.getViewModel(GroupViewModel.class);

        //获取推荐小组成功返回结果
        groupViewModel.groupListLiveData.observe(mActivity, groupListBean -> {
            isInited = true;
            if (groupListBean.getPageNum() == 1) {
                groupListAdapter.setNewData(groupListBean.getList());
                groupListAdapter.setEmptyView(EmptyViewFactory.createEmptyView(mActivity, getString(R.string.not_empty_group)));
            } else {
                groupListAdapter.addData(groupListAdapter.getData().size(), groupListBean.getList());
            }
            RefreshUtils.setNoMore(smartRefreshLayout,groupListBean.getPageNum(), groupListBean.getTotal());
        });

        //刷新和加载
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                groupViewModel.groupList();
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                groupViewModel.groupListMore();
            }
        });

        //滑动监听
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                AnimatorUtils.listScrollAnimation(tvAdd, dy);
            }
        });
    }

    @Override
    protected void initDataFromService() {
//        groupViewModel.groupList();
    }

    /**
     * 创建小组成功 刷新列表
     *
     * @param event
     */
    @Subscribe
    public void onEventCreate(GroupEvent event){
//        groupViewModel.groupList();
    }
}
