package com.queerlab.chat.view.group.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SPUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.adapter.ActivityAdapter;
import com.queerlab.chat.adapter.GroupListAdapter;
import com.queerlab.chat.adapter.InterestAdapter;
import com.queerlab.chat.base.BaseActivity;
import com.queerlab.chat.base.EmptyViewFactory;
import com.queerlab.chat.base.SpConfig;
import com.queerlab.chat.bean.ActivityListBean;
import com.queerlab.chat.bean.GroupListBean;
import com.queerlab.chat.bean.UserInfoBean;
import com.queerlab.chat.dialog.AnimationDialog;
import com.queerlab.chat.event.ClapEvent;
import com.queerlab.chat.event.GroupEvent;
import com.queerlab.chat.event.UserEvent;
import com.queerlab.chat.listener.OnCustomCallBack;
import com.queerlab.chat.utils.PictureUtils;
import com.queerlab.chat.utils.RefreshUtils;
import com.queerlab.chat.utils.UserUtils;
import com.queerlab.chat.tencent.TUIKitUtil;
import com.queerlab.chat.view.activity.ActivityDetailActivity;
import com.queerlab.chat.viewmodel.ActivityViewModel;
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
 * @Package: com.queerlab.chat.view.group.user
 * @ClassName: UserInfoActivity
 * @Description: 用户详情页面
 * @Author: 鹿鸿祥
 * @CreateDate: 5/18/21 10:22 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/18/21 10:22 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@SuppressLint("ClickableViewAccessibility")
public class UserInfoActivity extends BaseActivity {
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
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
    @BindView(R.id.tv_mine)
    AppCompatTextView tvMine;
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
    private ActivityViewModel activityViewModel;
    private UserInfoBean userInfoBean;
    private String isSelect = "用户";

    @Override
    protected int initLayoutRes() {
        return R.layout.activity_user_info;
    }

    @Override
    public boolean isUseEventBus() {
        return true;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(fakeStatusBar, getResources().getColor(R.color.color_F4F7FE));
        recyclerViewGroup.setLayoutManager(new LinearLayoutManager(activity));
        GroupListAdapter groupListAdapter = new GroupListAdapter(R.layout.adapter_group_list, null);
        recyclerViewGroup.setAdapter(groupListAdapter);

        recyclerViewActivity.setLayoutManager(new LinearLayoutManager(activity));
        ActivityAdapter activityAdapter = new ActivityAdapter(R.layout.adapter_activity, null);
        recyclerViewActivity.setAdapter(activityAdapter);

        groupListAdapter.setOnItemClickListener((adapter, view, position) -> {
            GroupListBean.ListBean listBean = groupListAdapter.getData().get(position);

            TUIKitUtil.enterGroup(activity, listBean, new OnCustomCallBack() {
                @Override
                public void onSuccess(Object data) {
                    groupViewModel.groupJoin(listBean.getGroup_no());
                    TUIKitUtil.startChatActivity(activity, true, listBean.getGroup_no(), listBean.getGroup_type() + listBean.getGroup_name(), listBean.getRoomId());
                }

                @Override
                public void onError(String module, int errCode, String errMsg) {

                }
            });
        });

        activityAdapter.setOnItemClickListener((adapter, view1, position) -> {
            ActivityListBean.ListBean listBean = activityAdapter.getData().get(position);
            Bundle bundle = new Bundle();
            bundle.putString("activityId", String.valueOf(listBean.getId()));
            ActivityUtils.startActivity(bundle, ActivityDetailActivity.class);
        });

        userViewModel = getViewModel(UserViewModel.class);
        groupViewModel = getViewModel(GroupViewModel.class);
        activityViewModel = getViewModel(ActivityViewModel.class);

        //获取用户详情成功返回结果
        userViewModel.userInfoLiveData.observe(activity, userInfoBean -> {
            this.userInfoBean = userInfoBean;
            PictureUtils.setImage(activity, userInfoBean.getUser_portrait(), ivAvatar);
            tvName.setText(TextUtils.isEmpty(userInfoBean.getUser_name()) ? "未知" : userInfoBean.getUser_name());
            tvStatus.setText(userInfoBean.getUser_status());
            tvClap.setVisibility(userInfoBean.getPaipai() == 0 ? View.GONE : View.VISIBLE);
            tvClap.setText("\uD83D\uDC4F " + userInfoBean.getPaipai());

            if (!TextUtils.isEmpty(userInfoBean.getUser_type())){
                llInterest.setVisibility(View.VISIBLE);
                recyclerViewInterest.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,false));
                recyclerViewInterest.addItemDecoration(new HorizontalItemDecoration(4, activity));
                InterestAdapter interestAdapter = new InterestAdapter(R.layout.adapter_interest, Arrays.asList(userInfoBean.getUser_type().split(",")), "user");
                recyclerViewInterest.setAdapter(interestAdapter);
            }else {
                llInterest.setVisibility(View.GONE);
            }

            if (userInfoBean.getIs_hide_group() == 2 && userInfoBean.getIs_hide_activity() != 2){
                isSelect = "用户";
                tvUser.setBackgroundResource(R.drawable.shape_yellow_25);
                tvUser.setTextColor(getResources().getColor(R.color.white));
                tvActivity.setBackgroundResource(R.drawable.shape_gray_25);
                tvActivity.setTextColor(getResources().getColor(R.color.color_D2D2D2));
                tvMine.setVisibility(View.VISIBLE);
                tvUser.setVisibility(View.VISIBLE);
                recyclerViewGroup.setVisibility(View.VISIBLE);
                tvActivity.setVisibility(View.GONE);
                recyclerViewActivity.setVisibility(View.GONE);
            }else if (userInfoBean.getIs_hide_activity() == 2 && userInfoBean.getIs_hide_group() != 2){
                isSelect = "活动";
                tvUser.setBackgroundResource(R.drawable.shape_gray_25);
                tvUser.setTextColor(getResources().getColor(R.color.color_D2D2D2));
                tvActivity.setBackgroundResource(R.drawable.shape_yellow_25);
                tvActivity.setTextColor(getResources().getColor(R.color.white));
                tvMine.setVisibility(View.VISIBLE);
                recyclerViewGroup.setVisibility(View.GONE);
                tvUser.setVisibility(View.GONE);
                tvActivity.setVisibility(View.VISIBLE);
                recyclerViewActivity.setVisibility(View.VISIBLE);
            }else if (userInfoBean.getIs_hide_activity() == 2 && userInfoBean.getIs_hide_group() == 2){
                isSelect = "用户";
                tvUser.setBackgroundResource(R.drawable.shape_yellow_25);
                tvUser.setTextColor(getResources().getColor(R.color.white));
                tvActivity.setBackgroundResource(R.drawable.shape_gray_25);
                tvActivity.setTextColor(getResources().getColor(R.color.color_D2D2D2));
                tvMine.setVisibility(View.VISIBLE);
                tvUser.setVisibility(View.VISIBLE);
                tvActivity.setVisibility(View.VISIBLE);
                if (isSelect.equals("用户")){
                    recyclerViewGroup.setVisibility(View.VISIBLE);
                    recyclerViewActivity.setVisibility(View.GONE);
                }else {
                    recyclerViewActivity.setVisibility(View.VISIBLE);
                    recyclerViewGroup.setVisibility(View.GONE);
                }
            }else {
                tvUser.setVisibility(View.GONE);
                recyclerViewGroup.setVisibility(View.GONE);
                tvActivity.setVisibility(View.GONE);
                recyclerViewActivity.setVisibility(View.GONE);
                tvMine.setVisibility(View.GONE);
            }
        });

        //用户拍一拍成功返回
        userViewModel.updateUserClapLiveData.observe(activity, s -> {
            AnimationDialog animationDialog = new AnimationDialog(activity);
            animationDialog.showDialog();
            userViewModel.userInfo(getIntent().getStringExtra("userId"));
        });

        //用户最近浏览的小组数据成功返回
        groupViewModel.userLatelyGroupLiveData.observe(activity, groupListBean -> {
            if (groupListBean.getPageNum() == 1) {
                groupListAdapter.setNewData(groupListBean.getList());
                groupListAdapter.setEmptyView(EmptyViewFactory.createEmptyView(activity, getString(R.string.not_empty_group)));
            } else {
                groupListAdapter.addData(groupListAdapter.getData().size(), groupListBean.getList());
            }
            RefreshUtils.setNoMore(smartRefreshLayout,groupListBean.getPageNum(), groupListBean.getTotal());
        });

        //用户参加过的活动数据成功返回
        activityViewModel.userJoinActivityLiveData.observe(activity, activityListBean -> {
            if (activityListBean.getPageNum() == 1) {
                activityAdapter.setNewData(activityListBean.getList());
                activityAdapter.setEmptyView(EmptyViewFactory.createEmptyView(activity, getString(R.string.not_empty_activity)));
            } else {
                activityAdapter.addData(activityAdapter.getData().size(), activityListBean.getList());
            }
        });

        if (TextUtils.isEmpty(getIntent().getStringExtra("userId"))){
            userViewModel.userInfo(SPUtils.getInstance().getString(SpConfig.USER_ID));
            groupViewModel.userLatelyGroupList(SPUtils.getInstance().getString(SpConfig.USER_ID));
            activityViewModel.userJoinActivity(SPUtils.getInstance().getString(SpConfig.USER_ID));
        }else {
            userViewModel.userInfo(getIntent().getStringExtra("userId"));
            groupViewModel.userLatelyGroupList(getIntent().getStringExtra("userId"));
            //非本人可双击头像拍一拍
            if (!SPUtils.getInstance().getString(SpConfig.USER_ID).equals(getIntent().getStringExtra("userId"))){
                UserUtils.setUserClap(ivAvatar, getIntent().getStringExtra("userId"), "user");
            }
            activityViewModel.userJoinActivity(getIntent().getStringExtra("userId"));
        }

        //用户最近浏览的小组上拉加载数据
        smartRefreshLayout.setEnableRefresh(false);
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            if (TextUtils.isEmpty(getIntent().getStringExtra("userId"))){
                groupViewModel.userLatelyGroupListMore(SPUtils.getInstance().getString(SpConfig.USER_ID));
            }else {
                groupViewModel.userLatelyGroupListMore(getIntent().getStringExtra("userId"));
            }
            refreshLayout.finishLoadMore(1000);
        });
    }

    @OnClick({R.id.iv_bar_back, R.id.iv_more, R.id.tv_user, R.id.tv_activity})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_bar_back:
                finish();
                break;
            case R.id.iv_more:
                Bundle bundle = new Bundle();
                bundle.putString("userId", getIntent().getStringExtra("userId"));
                bundle.putSerializable("userInfo", userInfoBean);
                ActivityUtils.startActivity(bundle, UserSettingActivity.class);
                break;
            case R.id.tv_user://用户
                isSelect = "用户";
                tvUser.setBackgroundResource(R.drawable.shape_yellow_25);
                tvUser.setTextColor(getResources().getColor(R.color.white));
                tvActivity.setBackgroundResource(R.drawable.shape_gray_25);
                tvActivity.setTextColor(getResources().getColor(R.color.color_D2D2D2));
                recyclerViewGroup.setVisibility(View.VISIBLE);
                recyclerViewActivity.setVisibility(View.GONE);
                break;
            case R.id.tv_activity://活动
                isSelect = "活动";
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
        if (TextUtils.isEmpty(getIntent().getStringExtra("userId"))){
            groupViewModel.userLatelyGroupList(SPUtils.getInstance().getString(SpConfig.USER_ID));
        }else {
            groupViewModel.userLatelyGroupList(getIntent().getStringExtra("userId"));
        }
    }

    /**
     * 双击头像拍一拍
     * @param event
     */
    @Subscribe
    public void onEventUpdateClap(ClapEvent event){
        if (event.getStatus().equals("user")){
            userViewModel.updateUserClap(event.getUserId());
        }
    }
}
