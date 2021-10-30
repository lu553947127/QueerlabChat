package com.queerlab.chat.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.queerlab.chat.base.BaseActivity;
import com.queerlab.chat.base.SpConfig;
import com.queerlab.chat.view.login.LoginActivity;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;

import java.util.List;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.utils
 * @ClassName: LoginUtils
 * @Description: 登录相关检测工具类
 * @Author: 鹿鸿祥
 * @CreateDate: 5/7/21 10:41 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/7/21 10:41 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class LoginUtils {

    /**
     * 检测是否第一次打开app
     *
     * @return
     */
    public static boolean isFirstApp(BaseActivity activity) {
        SharedUtils sharedUtils = new SharedUtils(activity);
        String first = sharedUtils.getShared(SpConfig.FIRST_APP,"first");
        if (TextUtils.isEmpty(first)) {
            return false;
        }
        return true;
    }

    /**
     * 检测是否打开过引导页面
     *
     * @return
     */
    public static boolean isGuide(BaseActivity activity) {
        SharedUtils sharedUtils = new SharedUtils(activity);
        String first = sharedUtils.getShared(SpConfig.GUIDE_APP,"guide");
        if (TextUtils.isEmpty(first)) {
            return false;
        }
        return true;
    }

    /**
     * 检测是否登录
     *
     * @return
     */
    public static boolean isLogin() {
        String token = SPUtils.getInstance().getString(SpConfig.TOKEN);
        if (StringUtils.isTrimEmpty(token)) {
            return false;
        }
        return true;
    }

    /**
     * 退出到登录页面
     *
     * @return
     */
    public static void getExitLogin() {
        TUIKit.logout(new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                clearData();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                LogUtils.e( "getExitLogin failed, errCode: " + errCode + "|desc: " + errMsg);
                clearData();
                if (errCode == 6014){
                    ToastUtils.showShort("你的账号在另外一台手机登录了wonderland");
                }else {
                    ToastUtils.showShort("logout fail: " + errCode + "=" + errMsg);
                }
            }
        });
    }

    /**
     * 清除用户数据
     */
    private static void clearData(){
        SPUtils.getInstance().clear(true);
        ActivityUtils.startActivity(LoginActivity.class);
        ActivityUtils.finishOtherActivities(LoginActivity.class);
    }

    /**
     * 退出App
     *
     * @return
     */
    public static void getExitApp(BaseActivity activity) {
        // 1. 通过Context获取ActivityManager
        ActivityManager activityManager = (ActivityManager) activity.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        // 2. 通过ActivityManager获取任务栈
        List<ActivityManager.AppTask> appTaskList = activityManager.getAppTasks();
        // 3. 逐个关闭Activity
        for (ActivityManager.AppTask appTask : appTaskList) {
            appTask.finishAndRemoveTask();
        }
    }
}
