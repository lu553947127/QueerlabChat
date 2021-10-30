package com.queerlab.chat.view.login;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.base.BaseActivity;
import com.queerlab.chat.utils.NumberUtils;
import com.queerlab.chat.viewmodel.LoginViewModel;
import com.queerlab.chat.widget.CustomEditText;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.view.login
 * @ClassName: LoginPhoneActivity
 * @Description: 登录-输入手机号页面
 * @Author: 鹿鸿祥
 * @CreateDate: 5/8/21 1:26 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/8/21 1:26 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class LoginPhoneActivity extends BaseActivity {
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.ccp_country)
    CountryCodePicker countryCodePicker;
    @BindView(R.id.et_phone)
    CustomEditText etPhone;
    @BindView(R.id.tv_login)
    AppCompatTextView tvLogin;
    //世界各国区号code
    private String code = "+86";
    private LoginViewModel loginViewModel;

    @Override
    protected int initLayoutRes() {
        return R.layout.activity_login_phone;
    }

    @Override
    public boolean isUseEventBus() {
        return false;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(fakeStatusBar, getResources().getColor(R.color.white));
        //国家选择器与输入框绑定
        countryCodePicker.registerPhoneNumberTextView(etPhone);
        //国家选择器选择返回事件
        countryCodePicker.setOnCountryChangeListener(selectedCountry -> {
            code = "+" + selectedCountry.getPhoneCode();
        });

        loginViewModel = getViewModel(LoginViewModel.class);

        //验证码发送成功返回结果
        loginViewModel.sendSmsLiveData.observe(activity, s -> {
            Bundle bundle = new Bundle();
            bundle.putString("code", code);
            bundle.putString("phoneNumber", etPhone.getTrimmedString());
            ActivityUtils.startActivity(bundle, LoginCodeActivity.class);
        });

        //加载动画返回结果
        loginViewModel.pageStateLiveData.observe(activity, s -> {
            showPageState(s);
        });
    }

    @OnTextChanged(value = R.id.et_phone, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void inputPhoneNumber(Editable editable) {
        String phoneNumber = editable.toString().trim();
        tvLogin.setEnabled(NumberUtils.isPhoneNumberValid(activity,code + phoneNumber, code));
    }

    @OnClick({R.id.iv_bar_back, R.id.tv_login})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_bar_back:
                finish();
                break;
            case R.id.tv_login:
                loginViewModel.sendSms(code, etPhone.getTrimmedString());
                break;
        }
    }
}
