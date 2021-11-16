package com.queerlab.chat.view.group.group;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.queerlab.chat.R;
import com.queerlab.chat.base.BaseActivity;
import com.queerlab.chat.base.SpConfig;
import com.queerlab.chat.event.GroupEvent;
import com.queerlab.chat.listener.OnCustomCallBack;
import com.queerlab.chat.tencent.TUIKitUtil;
import com.queerlab.chat.view.activity.ActivityDetailActivity;
import com.queerlab.chat.view.message.ChatActivity;
import com.queerlab.chat.viewmodel.GroupViewModel;
import com.queerlab.chat.widget.SwitchView;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.view.group
 * @ClassName: GroupInfoActivity
 * @Description: 群聊详情页面
 * @Author: 鹿鸿祥
 * @CreateDate: 6/5/21 2:16 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 6/5/21 2:16 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class GroupInfoActivity extends BaseActivity {
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.tv_see_activity)
    AppCompatTextView tvSeeActivity;
    @BindView(R.id.sv_notice)
    SwitchView svNotice;
    @BindView(R.id.tv_logout)
    AppCompatTextView tvLogout;
    private String userId;
    private GroupViewModel groupViewModel;

    @Override
    protected int initLayoutRes() {
        return R.layout.activity_group_info;
    }

    @Override
    public boolean isUseEventBus() {
        return false;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(fakeStatusBar, getResources().getColor(R.color.white));
        isDisturbStatus();

        userId = getIntent().getStringExtra("userId");

        if (SPUtils.getInstance().getString(SpConfig.USER_ID).equals(userId)){
            tvLogout.setText("\uD83D\uDC4B解散小组");
        }else {
            tvLogout.setText("\uD83D\uDC4B退出小组");
        }

        groupViewModel = getViewModel(GroupViewModel.class);

        //解散小组成功返回结果
        groupViewModel.groupDissolutionLiveData.observe(activity, s -> {
            V2TIMManager.getInstance().dismissGroup(getIntent().getStringExtra("groupId"), new V2TIMCallback() {
                @Override
                public void onError(int code, String desc) {

                }

                @Override
                public void onSuccess() {
                    ToastUtils.showShort("解散成功");
                    EventBus.getDefault().post(new GroupEvent("dissolution"));
                    ActivityUtils.finishActivity(ChatActivity.class);
                    finish();
                }
            });
        });

        //退出小组成功返回结果
        groupViewModel.groupLogoutLiveData.observe(activity, s -> {
            V2TIMManager.getInstance().quitGroup(getIntent().getStringExtra("groupId"), new V2TIMCallback() {
                @Override
                public void onError(int code, String desc) {

                }

                @Override
                public void onSuccess() {
                    ToastUtils.showShort("退出成功");
                    EventBus.getDefault().post(new GroupEvent("logout"));
                    ActivityUtils.finishActivity(ChatActivity.class);
                    finish();
                }
            });
        });

        //加载动画返回结果
        groupViewModel.pageStateLiveData.observe(activity, s -> {
            showPageState(s);
        });

        svNotice.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                svNotice.setOpened(true);
                TUIKitUtil.setDisturbStatus(getIntent().getStringExtra("groupId"), 2);
            }

            @Override
            public void toggleToOff(SwitchView view) {
                svNotice.setOpened(false);
                TUIKitUtil.setDisturbStatus(getIntent().getStringExtra("groupId"), 1);
            }
        });
    }

    @OnClick({R.id.iv_bar_back, R.id.tv_see_members, R.id.tv_see_activity, R.id.tv_logout})
    void onClick(View view){
        switch (view.getId()){
            case R.id.iv_bar_back:
                finish();
                break;
            case R.id.tv_see_members://查看组员
                Bundle bundle = new Bundle();
                bundle.putString("groupId", getIntent().getStringExtra("groupId"));
                bundle.putString("userId", userId);
                ActivityUtils.startActivity(bundle, GroupMemberActivity.class);
                break;
            case R.id.tv_see_activity://查看活动详情
                ActivityUtils.startActivity(ActivityDetailActivity.class);
                break;
            case R.id.tv_logout://解散/退出群组
                groupLogout();
                break;
        }
    }

    /**
     * 解散/退出小组
     */
    private void groupLogout(){
        if (SPUtils.getInstance().getString(SpConfig.USER_ID).equals(userId)){
            new XPopup.Builder(activity).asConfirm(getString(R.string.reminder), "是否要解散群组", () -> {
                groupViewModel.groupDissolution(getIntent().getStringExtra("groupId"));
            }).show();

        }else {
            new XPopup.Builder(activity).asConfirm(getString(R.string.reminder), "是否要退出群组", () -> {
                groupViewModel.groupLogout(getIntent().getStringExtra("groupId"));
            }).show();
        }
    }

    /**
     * 判断当前群聊的免打扰状态
     */
    private void isDisturbStatus(){
        TUIKitUtil.getDisturbStatus(getIntent().getStringExtra("groupId"), new OnCustomCallBack() {
            @Override
            public void onSuccess(Object data) {
                String status = String.valueOf( data);
                switch (status){
                    case "0"://在线正常接受消息，离线时会进行厂商离线推送
                        svNotice.setOpened(true);
                        break;
                    case "1"://不会收到群消息
                        svNotice.setOpened(false);
                        break;
                    case "2"://在线正常接受消息，离线不会有推送通知
                        svNotice.setOpened(true);
                        break;
                    default:
                        svNotice.setOpened(true);
                        break;
                }
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

            }
        });
    }
}
