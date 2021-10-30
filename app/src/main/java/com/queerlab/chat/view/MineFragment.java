package com.queerlab.chat.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.adapter.ActivityAdapter;
import com.queerlab.chat.adapter.GroupListAdapter;
import com.queerlab.chat.adapter.InterestAdapter;
import com.queerlab.chat.base.BaseFragment;
import com.queerlab.chat.base.EmptyViewFactory;
import com.queerlab.chat.base.SpConfig;
import com.queerlab.chat.bean.GroupListBean;
import com.queerlab.chat.bean.UserInfoBean;
import com.queerlab.chat.event.GroupEvent;
import com.queerlab.chat.event.UserEvent;
import com.queerlab.chat.listener.OnCustomCallBack;
import com.queerlab.chat.tencent.TUIKitUtil;
import com.queerlab.chat.utils.PictureUtils;
import com.queerlab.chat.utils.RefreshUtils;
import com.queerlab.chat.view.group.user.UserSettingActivity;
import com.queerlab.chat.viewmodel.GroupViewModel;
import com.queerlab.chat.viewmodel.UserViewModel;
import com.queerlab.chat.widget.CircleImageView;
import com.queerlab.chat.widget.HorizontalItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.view
 * @ClassName: MineFragment
 * @Description: 我的fragment
 * @Author: 鹿鸿祥
 * @CreateDate: 2021/10/15 10:25
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/10/15 10:25
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class MineFragment extends BaseFragment {
    @BindView(R.id.refresh)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_name)
    AppCompatTextView tvName;
    @BindView(R.id.tv_status)
    AppCompatTextView tvStatus;
    @BindView(R.id.tv_clap)
    AppCompatTextView tvClap;
    @BindView(R.id.ll_interest)
    LinearLayout llInterest;
    @BindView(R.id.rv_interest)
    RecyclerView recyclerViewInterest;
    @BindView(R.id.ll_group)
    LinearLayout llGroup;
    @BindView(R.id.tv_user)
    AppCompatTextView tvUser;
    @BindView(R.id.tv_activity)
    AppCompatTextView tvActivity;
    @BindView(R.id.rv_group)
    RecyclerView recyclerViewGroup;
    @BindView(R.id.rv_activity)
    RecyclerView recyclerViewActivity;
    private UserViewModel userViewModel;
    private GroupViewModel groupViewModel;
    private UserInfoBean userInfoBean;

    @Override
    protected int initLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    public boolean isUseEventBus() {
        return true;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState, View view) {
        recyclerViewGroup.setLayoutManager(new LinearLayoutManager(mActivity));
        GroupListAdapter groupListAdapter = new GroupListAdapter(R.layout.adapter_group_list, null);
        recyclerViewGroup.setAdapter(groupListAdapter);

        recyclerViewActivity.setLayoutManager(new LinearLayoutManager(mActivity));
        ActivityAdapter activityAdapter = new ActivityAdapter(R.layout.adapter_activity, RefreshUtils.getGroupListType());
        recyclerViewActivity.setAdapter(activityAdapter);

        recyclerViewInterest.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL,false));
        recyclerViewInterest.addItemDecoration(new HorizontalItemDecoration(4, mActivity));

        groupListAdapter.setOnItemClickListener((adapter, view1, position) -> {
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

        userViewModel = mActivity.getViewModel(UserViewModel.class);
        groupViewModel = mActivity.getViewModel(GroupViewModel.class);

        //获取用户详情成功返回结果
        userViewModel.userInfoLiveData.observe(mActivity, userInfoBean -> {
            this.userInfoBean = userInfoBean;
            PictureUtils.setImage(mActivity, userInfoBean.getUser_portrait(), ivAvatar);
            tvName.setText(TextUtils.isEmpty(userInfoBean.getUser_name()) ? "未知" : userInfoBean.getUser_name());
            tvStatus.setText(userInfoBean.getUser_status());
            tvClap.setVisibility(userInfoBean.getPaipai() == 0 ? View.GONE : View.VISIBLE);
            tvClap.setText("\uD83D\uDC4F " + userInfoBean.getPaipai());

            if (!TextUtils.isEmpty(userInfoBean.getUser_type())){
                llInterest.setVisibility(View.VISIBLE);
                InterestAdapter interestAdapter = new InterestAdapter(R.layout.adapter_interest, Arrays.asList(userInfoBean.getUser_type().split(",")), "user");
                recyclerViewInterest.setAdapter(interestAdapter);
            }else {
                llInterest.setVisibility(View.GONE);
            }

            if (userInfoBean.getIs_hide_group() == 2){
                llGroup.setVisibility(View.VISIBLE);
            }else {
                llGroup.setVisibility(View.GONE);
            }
        });

        //用户最近浏览的小组数据成功返回
        groupViewModel.userLatelyGroupLiveData.observe(mActivity, groupListBean -> {
            if (groupListBean.getPageNum() == 1) {
                groupListAdapter.setNewData(groupListBean.getList());
                groupListAdapter.setEmptyView(EmptyViewFactory.createEmptyView(mActivity, getString(R.string.not_empty_group)));
            } else {
                groupListAdapter.addData(groupListAdapter.getData().size(), groupListBean.getList());
            }
        });

        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            userViewModel.userInfo(SPUtils.getInstance().getString(SpConfig.USER_ID));
            groupViewModel.userLatelyGroupList(SPUtils.getInstance().getString(SpConfig.USER_ID));
            refreshLayout.finishRefresh(1000);
        });
    }

    @Override
    protected void initDataFromService() {
        userViewModel.userInfo(SPUtils.getInstance().getString(SpConfig.USER_ID));
        groupViewModel.userLatelyGroupList(SPUtils.getInstance().getString(SpConfig.USER_ID));
    }

    @OnClick({R.id.iv_more, R.id.tv_user, R.id.tv_activity})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_more:
                Bundle bundle = new Bundle();
                bundle.putString("userId", SPUtils.getInstance().getString(SpConfig.USER_ID));
                bundle.putSerializable("userInfo", userInfoBean);
                ActivityUtils.startActivity(bundle, UserSettingActivity.class);
                break;
            case R.id.tv_user://用户
                tvUser.setBackgroundResource(R.drawable.shape_yellow_25);
                tvUser.setTextColor(getResources().getColor(R.color.white));
                tvActivity.setBackgroundResource(R.drawable.shape_gray_25);
                tvActivity.setTextColor(getResources().getColor(R.color.color_D2D2D2));
                recyclerViewGroup.setVisibility(View.VISIBLE);
                recyclerViewActivity.setVisibility(View.GONE);
                break;
            case R.id.tv_activity://活动
                tvUser.setBackgroundResource(R.drawable.shape_gray_25);
                tvUser.setTextColor(getResources().getColor(R.color.color_D2D2D2));
                tvActivity.setBackgroundResource(R.drawable.shape_yellow_25);
                tvActivity.setTextColor(getResources().getColor(R.color.white));
                recyclerViewGroup.setVisibility(View.GONE);
                recyclerViewActivity.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 修改用户信息并更新
     */
    @Subscribe
    public void onEventUpdate(UserEvent event){
        userViewModel.userInfo(SPUtils.getInstance().getString(SpConfig.USER_ID));
    }

    /**
     * 解散/退出小组 刷新数据
     *
     * @param event
     */
    @Subscribe
    public void onEventCreate(GroupEvent event){
        groupViewModel.userLatelyGroupList(SPUtils.getInstance().getString(SpConfig.USER_ID));
    }
}
