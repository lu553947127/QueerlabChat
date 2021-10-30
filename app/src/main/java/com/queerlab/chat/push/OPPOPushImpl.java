package com.queerlab.chat.push;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.blankj.utilcode.util.LogUtils;
import com.heytap.msp.push.callback.ICallBackResultService;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.push
 * @ClassName: OPPOPushImpl
 * @Description: oppo推送服务
 * @Author: 鹿鸿祥
 * @CreateDate: 6/10/21 1:25 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 6/10/21 1:25 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class OPPOPushImpl implements ICallBackResultService {

    private static final String TAG = OPPOPushImpl.class.getSimpleName();

    @Override
    public void onRegister(int responseCode, String registerID) {
        LogUtils.i(TAG, "onRegister responseCode: " + responseCode + " registerID: " + registerID);
        ThirdPushTokenMgr.getInstance().setThirdPushToken(registerID);
        ThirdPushTokenMgr.getInstance().setPushTokenToTIM();
    }

    @Override
    public void onUnRegister(int responseCode) {
        LogUtils.i(TAG, "onUnRegister responseCode: " + responseCode);
    }

    @Override
    public void onSetPushTime(int responseCode, String s) {
        LogUtils.i(TAG, "onSetPushTime responseCode: " + responseCode + " s: " + s);
    }

    @Override
    public void onGetPushStatus(int responseCode, int status) {
        LogUtils.i(TAG, "onGetPushStatus responseCode: " + responseCode + " status: " + status);
    }

    @Override
    public void onGetNotificationStatus(int responseCode, int status) {
        LogUtils.i(TAG, "onGetNotificationStatus responseCode: " + responseCode + " status: " + status);
    }

    public void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "oppotest";
            String description = "this is opptest";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("tuikit", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
