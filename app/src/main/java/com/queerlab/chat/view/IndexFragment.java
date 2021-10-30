 package com.queerlab.chat.view;

 import android.os.Bundle;
 import android.view.View;

 import androidx.annotation.NonNull;
 import androidx.appcompat.widget.AppCompatImageView;
 import androidx.appcompat.widget.AppCompatTextView;
 import androidx.recyclerview.widget.LinearLayoutManager;
 import androidx.recyclerview.widget.RecyclerView;

 import com.blankj.utilcode.util.ActivityUtils;
 import com.queerlab.chat.R;
 import com.queerlab.chat.adapter.GroupListAdapter;
 import com.queerlab.chat.adapter.GroupTypeAdapter;
 import com.queerlab.chat.base.BaseFragment;
 import com.queerlab.chat.base.EmptyViewFactory;
 import com.queerlab.chat.bean.GroupListBean;
 import com.queerlab.chat.event.GroupEvent;
 import com.queerlab.chat.listener.OnCustomCallBack;
 import com.queerlab.chat.push.HUAWEIHmsMessageService;
 import com.queerlab.chat.tencent.TUIKitUtil;
 import com.queerlab.chat.utils.AnimatorUtils;
 import com.queerlab.chat.utils.RefreshUtils;
 import com.queerlab.chat.view.group.search.SearchActivity;
 import com.queerlab.chat.view.login.FirstNameActivity;
 import com.queerlab.chat.view.message.MessagesActivity;
 import com.queerlab.chat.view.message.MessagesExcessiveActivity;
 import com.queerlab.chat.viewmodel.GroupViewModel;
 import com.queerlab.chat.widget.HorizontalItemDecoration;
 import com.scwang.smartrefresh.layout.SmartRefreshLayout;
 import com.scwang.smartrefresh.layout.api.RefreshLayout;
 import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
 import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;

 import org.greenrobot.eventbus.Subscribe;

 import butterknife.BindView;
 import butterknife.OnClick;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.view
 * @ClassName: IndexFragment
 * @Description: 首页fragment
 * @Author: 鹿鸿祥
 * @CreateDate: 5/6/21 2:14 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/6/21 2:14 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class IndexFragment extends BaseFragment implements ConversationManagerKit.MessageUnreadWatcher {
    @BindView(R.id.refresh)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.rv_type)
    RecyclerView recyclerViewType;
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.tv_add)
    AppCompatTextView tvAdd;
    @BindView(R.id.iv_messages_unread)
    AppCompatImageView ivMessagesUnread;
    private GroupViewModel groupViewModel;

    @Override
    protected int initLayout() {
        return R.layout.fragment_index;
    }

    @Override
    public boolean isUseEventBus() {
        return false;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState, View view1) {
        recyclerViewType.setLayoutManager(new LinearLayoutManager(mActivity ,LinearLayoutManager.HORIZONTAL,false));
        recyclerViewType.addItemDecoration(new HorizontalItemDecoration(10, mActivity));
        GroupTypeAdapter groupTypeAdapter = new GroupTypeAdapter(R.layout.adapter_group_type, RefreshUtils.getGroupListType());
        recyclerViewType.setAdapter(groupTypeAdapter);

        groupTypeAdapter.setIsSelect(groupTypeAdapter.getData().get(0).getTitle());

        groupTypeAdapter.setOnItemClickListener((adapter, view, position) -> {
            groupTypeAdapter.setIsSelect(groupTypeAdapter.getData().get(position).getTitle());
        });

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
                groupViewModel.groupList();
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                groupViewModel.groupListMore();
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
        groupViewModel.groupList();
    }

    /**
     * 创建小组成功 刷新列表
     *
     * @param event
     */
    @Subscribe
    public void onEventCreate(GroupEvent event){
        groupViewModel.groupList();
    }

    @OnClick({R.id.tv_search, R.id.iv_messages, R.id.tv_add})
    void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.tv_search://搜索
                ActivityUtils.startActivity(SearchActivity.class);
                break;
            case R.id.iv_messages://消息页面
                ActivityUtils.startActivity(bundle, MessagesActivity.class);
                break;
            case R.id.tv_add://创建小组
                bundle.putString("type", "group");
                ActivityUtils.startActivity(bundle, FirstNameActivity.class);
                break;
        }
    }

    /**
     * 设置底部角标显示状态
     *
     * @param count 未读消息数量
     */
    @Override
    public void updateUnread(int count) {
        if (count < 1) {
            ivMessagesUnread.setVisibility(View.GONE);
        }else {
            ivMessagesUnread.setVisibility(View.VISIBLE);
        }
        HUAWEIHmsMessageService.updateBadge(mActivity, count);
    }

    @Override
    public void onResume() {
        super.onResume();
        TUIKitUtil.getMessageUnreadTip(this, mActivity);
    }
}
