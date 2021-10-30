package com.queerlab.chat.push;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;
import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.push
 * @ClassName: HUAWEIHmsMessageService
 * @Description: 华为推送服务
 * @Author: 鹿鸿祥
 * @CreateDate: 6/10/21 11:55 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 6/10/21 11:55 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class HUAWEIHmsMessageService extends HmsMessageService {

    private static final String TAG = HUAWEIHmsMessageService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage message) {
        LogUtils.i(TAG, "onMessageReceived message=" + message);
    }

    @Override
    public void onMessageSent(String msgId) {
        LogUtils.i(TAG, "onMessageSent msgId=" + msgId);
    }

    @Override
    public void onSendError(String msgId, Exception exception) {
        LogUtils.i(TAG, "onSendError msgId=" + msgId);
    }

    @Override
    public void onNewToken(String token) {
        LogUtils.i(TAG, "onNewToken token=" + token);
        ThirdPushTokenMgr.getInstance().setThirdPushToken(token);
        ThirdPushTokenMgr.getInstance().setPushTokenToTIM();
    }

    @Override
    public void onTokenError(Exception exception) {
        LogUtils.i(TAG, "onTokenError exception=" + exception);
    }

    @Override
    public void onMessageDelivered(String msgId, Exception exception) {
        LogUtils.i(TAG, "onMessageDelivered msgId=" + msgId);
    }


    public static void updateBadge(final Context context, final int number) {
        if (!BrandUtil.isBrandHuawei()) {
            return;
        }
        LogUtils.i(TAG, "huawei badge = " + number);
        try {
            Bundle extra = new Bundle();
            extra.putString("package", "com.queerlab.chat");
            extra.putString("class", "com.queerlab.chat.view.start.StartUpActivity");
            extra.putInt("badgenumber", number);
            context.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, extra);
        } catch (Exception e) {
            LogUtils.w(TAG, "huawei badge exception: " + e.getLocalizedMessage());
        }
    }
}
