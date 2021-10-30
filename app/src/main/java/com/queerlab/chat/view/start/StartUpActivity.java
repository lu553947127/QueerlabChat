package com.queerlab.chat.view.start;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.base.BaseActivity;
import com.queerlab.chat.base.SpConfig;
import com.queerlab.chat.dialog.AgreementDialog;
import com.queerlab.chat.event.LoginEvent;
import com.queerlab.chat.listener.OnCustomClickListener;
import com.queerlab.chat.utils.LoginUtils;
import com.queerlab.chat.tencent.TUIKitUtil;
import com.queerlab.chat.view.MainActivity;
import com.queerlab.chat.view.login.FirstNameActivity;
import com.queerlab.chat.view.login.LoginActivity;

import org.greenrobot.eventbus.Subscribe;

import per.goweii.swipeback.SwipeBackDirection;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.view.start
 * @ClassName: StartUpActivity
 * @Description: 启动页
 * @Author: 鹿鸿祥
 * @CreateDate: 5/7/21 10:13 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/7/21 10:13 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class StartUpActivity extends BaseActivity {

    @Override
    protected int initLayoutRes() {
        return R.layout.activity_start_up;
    }

    @Override
    public boolean isUseEventBus() {
        return true;
    }

    @NonNull
    @Override
    public SwipeBackDirection swipeBackDirection() {
        return SwipeBackDirection.NONE;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState) {
        if (LoginUtils.isFirstApp(activity)){
            getIntoActivity();
        }else {
            //协议弹窗
            AgreementDialog agreementDialog = new AgreementDialog(activity);
            agreementDialog.showDialog();
            agreementDialog.setOnCustomClickListener(new OnCustomClickListener() {
                @Override
                public void onSuccess() {
                    getIntoActivity();
                }

                @Override
                public void onCancel() {

                }
            });
        }
    }

    /**
     * 判断跳转
     */
    private void getIntoActivity() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(1500);//休眠1.5秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //判断是否打开过引导页
                if (LoginUtils.isGuide(activity)){
                    //判断是否获取过token
                    if (LoginUtils.isLogin()){
                        switch (SPUtils.getInstance().getString(SpConfig.USER_STATUS)){
                            case "1"://新用户
                                ActivityUtils.startActivity(FirstNameActivity.class);
                                break;
                            case "2"://老用户
                                TUIKitUtil.getTUIKitLogin(SPUtils.getInstance().getString(SpConfig.USER_ID), SPUtils.getInstance().getString(SpConfig.USER_SIG));
                                break;
                        }
                    }else {
                        ActivityUtils.startActivity(LoginActivity.class);
                        finish();
                    }
                }else {
                    ActivityUtils.startActivity(GuideActivity.class);
                    finish();
                }
            }
        }.start();
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
