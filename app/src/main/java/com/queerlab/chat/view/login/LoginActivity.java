package com.queerlab.chat.view.login;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ActivityUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.base.BaseActivity;

import butterknife.OnClick;
import per.goweii.swipeback.SwipeBackDirection;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.view.login
 * @ClassName: LoginActivity
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 5/7/21 10:44 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/7/21 10:44 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class LoginActivity extends BaseActivity {

    @Override
    protected int initLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    public boolean isUseEventBus() {
        return false;
    }

    @NonNull
    @Override
    public SwipeBackDirection swipeBackDirection() {
        return SwipeBackDirection.NONE;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState) {

    }

    @OnClick({R.id.tv_login})
    void onClick() {
        ActivityUtils.startActivity(LoginPhoneActivity.class);
        finish();
    }
}
