package com.queerlab.chat.app;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.ArrayMap;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.heytap.msp.push.HeytapPushManager;
import com.huawei.hms.push.HmsMessaging;
import com.queerlab.chat.R;
import com.queerlab.chat.base.BuildConfig;
import com.queerlab.chat.http.api.ApiService;
import com.queerlab.chat.http.retrofit.BaseRequest;
import com.queerlab.chat.push.BrandUtil;
import com.queerlab.chat.push.HUAWEIHmsMessageService;
import com.queerlab.chat.push.MessageNotification;
import com.queerlab.chat.push.PrivateConstants;
import com.queerlab.chat.tencent.TencentUtils;
import com.queerlab.chat.view.start.StartUpActivity;
import com.queerlab.chat.widget.CustomMapView;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMGroupInfoResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMSDKConfig;
import com.tencent.imsdk.v2.V2TIMSendCallback;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IMEventListener;
import com.tencent.qcloud.tim.uikit.config.CustomFaceConfig;
import com.tencent.qcloud.tim.uikit.config.GeneralConfig;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.vivo.push.PushClient;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import per.goweii.swipeback.SwipeBack;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.app
 * @ClassName: MyApplication
 * @Description: app???????????????
 * @Author: ?????????
 * @CreateDate: 5/6/21 1:45 PM
 * @UpdateUser: ?????????
 * @UpdateDate: 5/6/21 1:45 PM
 * @UpdateRemark: ????????????
 * @Version: 1.0
 */
public class MyApplication extends Application {

    private static MyApplication instance;
    public static boolean isAndroidQ = Build.VERSION.SDK_INT >= 29;
    private static ArrayMap<String, ViewModel> sViewModelCache;
    //????????????
    public static BaseRequest<ApiService> baseHttpRequest;
    //?????????????????????
    //static ?????????????????????????????????
    static {
        //???????????????Header?????????
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            layout.setPrimaryColorsId(android.R.color.transparent, R.color.color_FFA52C);//????????????????????????
            return new MaterialHeader(context).setColorSchemeResources(R.color.color_FFA52C);//???????????????Header???????????????????????????Header
        });
        //???????????????Footer?????????
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
            //???????????????Footer???????????? BallPulseFooter
            return new ClassicsFooter(context).setDrawableSize(20).setAccentColorId(R.color.color_FFA52C);
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        sViewModelCache = new ArrayMap<>();
        /**
         * ????????? Application ??? onCreate ??????????????? BGASwipeBackHelper.init ????????????????????????
         * ???????????????????????????????????????
         * ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????? View ??? class ?????????????????????????????????????????????????????? WebView ??? SurfaceView
         */
        List<Class<? extends View>> problemViewClassList = new ArrayList<>();
        problemViewClassList.add(CustomMapView.class);
        problemViewClassList.add(MapView.class);
        BGASwipeBackHelper.init(this, problemViewClassList);

        initUtils((Application) getApplicationContext());

        initHttpRequest(BuildConfig.BASE_URL, ApiService.class);

        initTencent();

        //?????????????????????
        SwipeBack.getInstance().init(this);
        //?????????????????????
//        SwipeBack.getInstance().setSwipeBackDirection(SwipeBackDirection.RIGHT);
        //????????????????????????
//        SwipeBack.getInstance().setSwipeBackTransformer(new ParallaxSwipeBackTransformer());
    }

    /**
     * ?????????Utils??????
     * @param context
     */
    private static void initUtils(Application context) {
        Utils.init(context);
        LogUtils.getConfig().setGlobalTag(context.getString(R.string.app_name)).setLogSwitch(true);
        ToastUtils.setGravity(Gravity.CENTER, 0, ConvertUtils.dp2px(100));
        ToastUtils.setMsgTextSize(13);
        ToastUtils.setMsgColor(context.getResources().getColor(R.color.white));
        if (isAndroidQ) {
            ToastUtils.setBgResource(R.drawable.shape_bg_toast_q);
        }else {
            ToastUtils.setBgResource(R.drawable.shape_bg_toast);
        }
    }

    /**
     * ?????????????????????
     *
     * @param host
     * @param clazz
     */
    public void initHttpRequest(String host, Class clazz) {
        if (baseHttpRequest == null) {
            if (baseHttpRequest == null) {
                baseHttpRequest = new BaseRequest(host, clazz);
            }
        }
    }

    /**
     * ??????????????????
     */
    private void initTencent() {
        TUIKitConfigs configs = TUIKit.getConfigs();
        configs.setSdkConfig(new V2TIMSDKConfig());
        configs.setCustomFaceConfig(new CustomFaceConfig());
        configs.setGeneralConfig(new GeneralConfig());
        TUIKit.init(this, TencentUtils.SDKAPPID, configs);

        HeytapPushManager.init(this, true);
        if (BrandUtil.isBrandXiaoMi()) {
            // ??????????????????
            MiPushClient.registerPush(this, PrivateConstants.XM_PUSH_APPID, PrivateConstants.XM_PUSH_APPKEY);
        } else if (BrandUtil.isBrandHuawei()) {
            // ???????????????????????????????????????Push???????????????????????????
            HmsMessaging.getInstance(this).turnOnPush().addOnCompleteListener(new com.huawei.hmf.tasks.OnCompleteListener<Void>() {
                @Override
                public void onComplete(com.huawei.hmf.tasks.Task<Void> task) {
                    if (task.isSuccessful()) {
                        LogUtils.i( "huawei turnOnPush Complete");
                    } else {
                        LogUtils.e( "huawei turnOnPush failed: ret=" + task.getException().getMessage());
                    }
                }
            });
        } else if (BrandUtil.isBrandVivo()) {
            // vivo????????????
            PushClient.getInstance(getApplicationContext()).initialize();
        } else if (HeytapPushManager.isSupportPush()) {
            // oppo???????????????????????????????????????????????????????????????token?????????????????????MainActivity??????
        }

        registerActivityLifecycleCallbacks(new StatisticActivityLifecycleCallback());
    }

    /**
     * ?????????????????????
     */
    class StatisticActivityLifecycleCallback implements ActivityLifecycleCallbacks {
        private int foregroundActivities = 0;
        private boolean isChangingConfiguration;
        private IMEventListener mIMEventListener = new IMEventListener() {
            @Override
            public void onNewMessage(V2TIMMessage msg) {
                List<String> groupList = new ArrayList<>(Arrays.asList(msg.getGroupID()));
                V2TIMManager.getGroupManager().getGroupsInfo(groupList, new V2TIMSendCallback<List<V2TIMGroupInfoResult>>() {
                    @Override
                    public void onProgress(int progress) {

                    }

                    @Override
                    public void onError(int code, String desc) {
                        LogUtils.e("getGroupsInfo = "  + code + " , errorInfo =  + " + desc);
                    }

                    @Override
                    public void onSuccess(List<V2TIMGroupInfoResult> v2TIMGroupInfoResults) {
                        for (V2TIMGroupInfoResult result : v2TIMGroupInfoResults) {
                            LogUtils.e("result.getGroupInfo().getGroupName(): " + result.getGroupInfo().getGroupName());
                            MessageNotification notification = MessageNotification.getInstance();
                            notification.notify(msg, result.getGroupInfo().getGroupName());
                        }
                    }
                });

            }
        };

        private ConversationManagerKit.MessageUnreadWatcher mUnreadWatcher = new ConversationManagerKit.MessageUnreadWatcher() {
            @Override
            public void updateUnread(int count) {
                // ????????????????????????
                HUAWEIHmsMessageService.updateBadge(MyApplication.this, count);
            }
        };

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            LogUtils.i( "onActivityCreated bundle: " + bundle);
            if (bundle != null) { // ???bundle??????????????????????????????
                // ??????????????????
                Intent intent = new Intent(activity, StartUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {
            foregroundActivities++;
            if (foregroundActivities == 1 && !isChangingConfiguration) {
                // ??????????????????
                LogUtils.i( "application enter foreground" + activity);
                V2TIMManager.getOfflinePushManager().doForeground(new V2TIMCallback() {
                    @Override
                    public void onError(int code, String desc) {
                        LogUtils.e( "doForeground err = " + code + ", desc = " + desc);
                    }

                    @Override
                    public void onSuccess() {
                        LogUtils.i( "doForeground success");
                    }
                });
                TUIKit.removeIMEventListener(mIMEventListener);
                ConversationManagerKit.getInstance().removeUnreadWatcher(mUnreadWatcher);
                MessageNotification.getInstance().cancelTimeout();
            }
            isChangingConfiguration = false;
        }

        @Override
        public void onActivityResumed(Activity activity) {
            LogUtils.i( "application enter onActivityResumed" + activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {
            LogUtils.i( "application enter onActivityResumed" + activity);
        }

        @Override
        public void onActivityStopped(Activity activity) {
            foregroundActivities--;
            if (foregroundActivities == 0) {
                // ??????????????????
                LogUtils.i( "application enter background onActivityStopped");
                int unReadCount = ConversationManagerKit.getInstance().getUnreadTotal();
                V2TIMManager.getOfflinePushManager().doBackground(unReadCount, new V2TIMCallback() {
                    @Override
                    public void onError(int code, String desc) {
                        LogUtils.e( "doBackground err = " + code + ", desc = " + desc);
                    }

                    @Override
                    public void onSuccess() {
                        LogUtils.i( "doBackground success");
                    }
                });
                // ????????????????????????????????????????????????
                TUIKit.addIMEventListener(mIMEventListener);
                ConversationManagerKit.getInstance().addUnreadWatcher(mUnreadWatcher);
            }
            isChangingConfiguration = activity.isChangingConfigurations();
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            LogUtils.i( "application enter onActivitySaveInstanceState" + activity);
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            LogUtils.i( "application enter onActivitySaveInstanceState" + activity);
        }
    }

    /**
     * ?????????????????????handler
     */
    public static Handler mMainThreadHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            mListener.handlerMessage(msg);
        }
    };

    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    private static HandlerListener mListener;

    public static void setOnHandlerListener(HandlerListener listener) {
        mListener = listener;
    }

    public static HandlerListener getListener() {
        return mListener;
    }

    public interface HandlerListener {
        void handlerMessage(Message msg);
    }


    public static MyApplication getInstance() {
        return instance;
    }

    public static ArrayMap<String, ViewModel> getViewModelCache() {
        return sViewModelCache;
    }
}
