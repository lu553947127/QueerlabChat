package com.queerlab.chat.view.group.user;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SPUtils;
import com.lxj.xpopup.XPopup;
import com.queerlab.chat.R;
import com.queerlab.chat.base.BaseActivity;
import com.queerlab.chat.base.SpConfig;
import com.queerlab.chat.bean.UserInfoBean;
import com.queerlab.chat.event.UserEvent;
import com.queerlab.chat.utils.LoginUtils;
import com.queerlab.chat.tencent.TUIKitUtil;
import com.queerlab.chat.view.login.FirstHobbyActivity;
import com.queerlab.chat.view.message.WebViewActivity;
import com.queerlab.chat.viewmodel.UserViewModel;
import com.queerlab.chat.widget.SwitchView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.view.group.user
 * @ClassName: UserSettingActivity
 * @Description: 用户资料设置页面
 * @Author: 鹿鸿祥
 * @CreateDate: 5/19/21 9:25 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/19/21 9:25 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class UserSettingActivity extends BaseActivity {
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.ll_others)
    LinearLayout llOthers;
    @BindView(R.id.ll_own)
    LinearLayout llOwn;
    @BindView(R.id.sv_group)
    SwitchView svGroup;
    @BindView(R.id.sv_activity)
    SwitchView svActivity;
    @BindView(R.id.tv_user_agreement)
    AppCompatTextView tvUserAgreement;
    @BindView(R.id.tv_privacy_agreement)
    AppCompatTextView tvPrivacyAgreement;
    @BindView(R.id.tv_version)
    AppCompatTextView tvVersion;
    private UserInfoBean userInfoBean;
    private UserViewModel userViewModel;

    @Override
    protected int initLayoutRes() {
        return R.layout.activity_user_setting;
    }

    @Override
    public boolean isUseEventBus() {
        return false;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(fakeStatusBar, getResources().getColor(R.color.white));
        tvUserAgreement.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvPrivacyAgreement.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvVersion.setText("版本号: " + AppUtils.getAppVersionName());
        userInfoBean = (UserInfoBean) getIntent().getSerializableExtra("userInfo");

        if (TextUtils.isEmpty(getIntent().getStringExtra("userId"))){
            llOthers.setVisibility(View.GONE);
            llOwn.setVisibility(View.VISIBLE);
        }else {
            if (getIntent().getStringExtra("userId").equals(SPUtils.getInstance().getString(SpConfig.USER_ID))){
                llOthers.setVisibility(View.GONE);
                llOwn.setVisibility(View.VISIBLE);
            }else {
                llOthers.setVisibility(View.VISIBLE);
                llOwn.setVisibility(View.GONE);
            }
        }

        userViewModel = getViewModel(UserViewModel.class);

        //隐藏/显示小组成功返回结果
        userViewModel.updateHideGroupLiveData.observe(activity, s -> {
            EventBus.getDefault().post(new UserEvent("isHideGroup"));
        });

        //隐藏/显示活动成功返回结果
        userViewModel.updateHideActivityLiveData.observe(activity, s -> {
            EventBus.getDefault().post(new UserEvent("isHideActivity"));
        });

        //获取当前小组隐藏状态
        switch (userInfoBean.getIs_hide_group()){
            case 1:
                svGroup.setOpened(true);
                break;
            case 2:
                svGroup.setOpened(false);
                break;
        }

        //获取当前活动隐藏状态
        switch (userInfoBean.getIs_hide_activity()){
            case 1:
                svActivity.setOpened(true);
                break;
            case 2:
                svActivity.setOpened(false);
                break;
        }

        //设置小组隐藏状态
        svGroup.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                userViewModel.updateHideGroup("1");
                svGroup.setOpened(true);
            }

            @Override
            public void toggleToOff(SwitchView view) {
                userViewModel.updateHideGroup("2");
                svGroup.setOpened(false);
            }
        });

        //设置活动隐藏状态
        svActivity.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                userViewModel.updateHideActivity("1");
                svActivity.setOpened(true);
            }

            @Override
            public void toggleToOff(SwitchView view) {
                userViewModel.updateHideActivity("2");
                svActivity.setOpened(false);
            }
        });
    }

    @OnClick({R.id.iv_bar_back, R.id.tv_say_hi, R.id.tv_status, R.id.tv_avatar, R.id.tv_name, R.id.tv_interest, R.id.tv_logout
            , R.id.tv_user_agreement, R.id.tv_privacy_agreement})
    void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()){
            case R.id.iv_bar_back:
                finish();
                break;
            case R.id.tv_say_hi://发送消息
                TUIKitUtil.startChatActivity(activity,false, getIntent().getStringExtra("userId"), userInfoBean.getUser_name(), 0);
                break;
            case R.id.tv_status://更新状态
                bundle.putString("status", userInfoBean.getUser_status());
                ActivityUtils.startActivity(bundle, UpdateStatusActivity.class);
                break;
            case R.id.tv_avatar://编辑头像
                bundle.putString("avatar", userInfoBean.getUser_portrait());
                ActivityUtils.startActivity(bundle, UpdateAvatarActivity.class);
                break;
            case R.id.tv_name://编辑昵称
                bundle.putString("name", userInfoBean.getUser_name());
                ActivityUtils.startActivity(bundle, UpdateNameActivity.class);
                break;
            case R.id.tv_interest://编辑兴趣
                bundle.putString("edit", "edit");
                bundle.putString("type", userInfoBean.getUser_type());
                ActivityUtils.startActivity(bundle, FirstHobbyActivity.class);
                break;
            case R.id.tv_logout://退出登录
                new XPopup.Builder(activity).asConfirm(getString(R.string.reminder), getString(R.string.login_out_is), () -> {
                    LoginUtils.getExitLogin();
                }).show();
                break;
            case R.id.tv_user_agreement://用户协议
                bundle.putString("type", "user");
                ActivityUtils.startActivity(bundle, WebViewActivity.class);
                break;
            case R.id.tv_privacy_agreement://隐私协议
                bundle.putString("type", "privacy");
                ActivityUtils.startActivity(bundle, WebViewActivity.class);
                break;
        }
    }
}
