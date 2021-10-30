package com.queerlab.chat.view.login;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SPUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.base.BaseActivity;
import com.queerlab.chat.base.SpConfig;
import com.queerlab.chat.event.LoginEvent;
import com.queerlab.chat.tencent.TUIKitUtil;
import com.queerlab.chat.view.MainActivity;
import com.queerlab.chat.viewmodel.LoginViewModel;
import com.queerlab.chat.widget.CustomEditText;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.view.login
 * @ClassName: LoginCodeActivity
 * @Description: 输入手机验证码页面
 * @Author: 鹿鸿祥
 * @CreateDate: 5/10/21 2:17 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/10/21 2:17 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class LoginCodeActivity extends BaseActivity {
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.et_code)
    CustomEditText etCode;
    @BindView(R.id.tv_next)
    AppCompatTextView tvNext;
    @BindView(R.id.tv_code)
    AppCompatTextView tvCode;
    private LoginViewModel loginViewModel;

    @Override
    protected int initLayoutRes() {
        return R.layout.activity_login_code;
    }

    @Override
    public boolean isUseEventBus() {
        return true;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(fakeStatusBar, getResources().getColor(R.color.white));
        loginViewModel = getViewModel(LoginViewModel.class);

        //获取验证码倒计时返回结果
        loginViewModel.timeLiveDataLiveData.observe(activity, aLong -> {
            getCode(aLong);
        });

        //验证码发送成功返回结果
        loginViewModel.sendSmsLiveData.observe(activity, s -> {
            loginViewModel.sendVerificationCode();
        });

        //验证码登录成功返回结果
        loginViewModel.loginLiveData.observe(activity, loginBean -> {
            SPUtils.getInstance().put(SpConfig.TOKEN, loginBean.getToken(), true);
            SPUtils.getInstance().put(SpConfig.USER_STATUS, loginBean.getUserStatus(), true);
            SPUtils.getInstance().put(SpConfig.COUNTRY_CODE, getIntent().getStringExtra("code"), true);
            SPUtils.getInstance().put(SpConfig.PHONE, getIntent().getStringExtra("phoneNumber"), true);
            SPUtils.getInstance().put(SpConfig.USER_NAME, loginBean.getUserName(), true);
            SPUtils.getInstance().put(SpConfig.USER_AVATAR, loginBean.getUserPortrait(), true);
            switch (loginBean.getUserStatus()){
                case "1"://新用户
                    ActivityUtils.startActivity(FirstNameActivity.class);
                    break;
                case "2"://老用户
                    SPUtils.getInstance().put(SpConfig.USER_ID, loginBean.getUserId(), true);
                    SPUtils.getInstance().put(SpConfig.USER_SIG, loginBean.getUserSig(), true);
                    TUIKitUtil.getTUIKitLogin(loginBean.getUserId(), loginBean.getUserSig());
                    break;
            }
        });

        //加载动画返回结果
        loginViewModel.pageStateLiveData.observe(activity, s -> {
            showPageState(s);
        });

        loginViewModel.sendVerificationCode();
    }

    @OnTextChanged(value = R.id.et_code, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void inputPhoneCode(Editable editable) {
        String phoneCode = editable.toString().trim();
        tvNext.setEnabled(phoneCode.length() == 4);
    }

    @OnClick({R.id.iv_bar_back, R.id.tv_code, R.id.tv_next})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_bar_back:
                finish();
                break;
            case R.id.tv_code://获取验证码
                loginViewModel.sendSms(getIntent().getStringExtra("code"), getIntent().getStringExtra("phoneNumber"));
                break;
            case R.id.tv_next://登录
                loginViewModel.login(getIntent().getStringExtra("code"), getIntent().getStringExtra("phoneNumber"), etCode.getTrimmedString());
                break;
        }
    }

    /**
     * 验证码获取成功后操作
     *
     * @param aLong
     */
    private void getCode(Long aLong) {
        if (aLong == -1) {
            //重新获取
            tvCode.setEnabled(true);
            tvCode.setText(getString(R.string.login_code_seconds));
        } else {
            tvCode.setEnabled(false);
            tvCode.setText(String.valueOf(aLong));
        }
    }

    /**
     * 腾讯云IM登录成功返回
     *
     * @param event
     */
    @Subscribe
    public void onEventLogin(LoginEvent event) {
        ActivityUtils.startActivity(MainActivity.class);
        finish();
    }
}
