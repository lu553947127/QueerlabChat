package com.queerlab.chat.push;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMOfflinePushConfig;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.push
 * @ClassName: ThirdPushTokenMgr
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 6/10/21 11:52 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 6/10/21 11:52 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ThirdPushTokenMgr {
    public static final boolean USER_GOOGLE_FCM = false;
    private static final String TAG = ThirdPushTokenMgr.class.getSimpleName();
    private String mThirdPushToken;

    public static ThirdPushTokenMgr getInstance() {
        return ThirdPushTokenHolder.instance;
    }

    public String getThirdPushToken() {
        return mThirdPushToken;
    }

    public void setThirdPushToken(String mThirdPushToken) {
        this.mThirdPushToken = mThirdPushToken;
    }

    public void setPushTokenToTIM() {
        String token = ThirdPushTokenMgr.getInstance().getThirdPushToken();
        if (TextUtils.isEmpty(token)) {
            LogUtils.i(TAG, "setPushTokenToTIM third token is empty");
            return;
        }
        V2TIMOfflinePushConfig v2TIMOfflinePushConfig = null;
        if (BrandUtil.isBrandXiaoMi()) {
            v2TIMOfflinePushConfig = new V2TIMOfflinePushConfig(PrivateConstants.XM_PUSH_BUZID, token);
        } else if (BrandUtil.isBrandHuawei()) {
            v2TIMOfflinePushConfig = new V2TIMOfflinePushConfig(PrivateConstants.HW_PUSH_BUZID, token);
        } else if (BrandUtil.isBrandMeizu()) {
            v2TIMOfflinePushConfig = new V2TIMOfflinePushConfig(PrivateConstants.MZ_PUSH_BUZID, token);
        } else if (BrandUtil.isBrandOppo()) {
            v2TIMOfflinePushConfig = new V2TIMOfflinePushConfig(PrivateConstants.OPPO_PUSH_BUZID, token);
        } else if (BrandUtil.isBrandVivo()) {
            v2TIMOfflinePushConfig = new V2TIMOfflinePushConfig(PrivateConstants.VIVO_PUSH_BUZID, token);
        } else {
            return;
        }

        V2TIMManager.getOfflinePushManager().setOfflinePushConfig(v2TIMOfflinePushConfig, new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                LogUtils.d(TAG, "setOfflinePushToken err code = " + code);
            }

            @Override
            public void onSuccess() {
                LogUtils.d(TAG, "setOfflinePushToken success");
            }
        });
    }

    private static class ThirdPushTokenHolder {
        private static final ThirdPushTokenMgr instance = new ThirdPushTokenMgr();
    }
}
