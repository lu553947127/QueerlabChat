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
 * @Description: app初始化工具
 * @Author: 鹿鸿祥
 * @CreateDate: 5/6/21 1:45 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/6/21 1:45 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class MyApplication extends Application {

    private static MyApplication instance;
    public static boolean isAndroidQ = Build.VERSION.SDK_INT >= 29;
    private static ArrayMap<String, ViewModel> sViewModelCache;
    //网络请求
    public static BaseRequest<ApiService> baseHttpRequest;
    //初始化刷新工具
    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            layout.setPrimaryColorsId(android.R.color.transparent, R.color.color_FFA52C);//全局设置主题颜色
            return new MaterialHeader(context).setColorSchemeResources(R.color.color_FFA52C);//指定为经典Header，默认是贝塞尔雷达Header
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
            //指定为经典Footer，默认是 BallPulseFooter
            return new ClassicsFooter(context).setDrawableSize(20).setAccentColorId(R.color.color_FFA52C);
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        sViewModelCache = new ArrayMap<>();
        /**
         * 必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回
         * 第一个参数：应用程序上下文
         * 第二个参数：如果发现滑动返回后立即触摸界面时应用崩溃，请把该界面里比较特殊的 View 的 class 添加到该集合中，目前在库中已经添加了 WebView 和 SurfaceView
         */
        List<Class<? extends View>> problemViewClassList = new ArrayList<>();
        problemViewClassList.add(CustomMapView.class);
        problemViewClassList.add(MapView.class);
        BGASwipeBackHelper.init(this, problemViewClassList);

        initUtils((Application) getApplicationContext());

        initHttpRequest(BuildConfig.BASE_URL, ApiService.class);

        initTencent();

        //初始化测滑返回
        SwipeBack.getInstance().init(this);
        //初始化测滑方向
//        SwipeBack.getInstance().setSwipeBackDirection(SwipeBackDirection.RIGHT);
        //设置测滑动画效果
//        SwipeBack.getInstance().setSwipeBackTransformer(new ParallaxSwipeBackTransformer());
    }

    /**
     * 初始化Utils工具
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
     * 初始化网络请求
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
     * 初始化腾讯云
     */
    private void initTencent() {
        TUIKitConfigs configs = TUIKit.getConfigs();
        configs.setSdkConfig(new V2TIMSDKConfig());
        configs.setCustomFaceConfig(new CustomFaceConfig());
        configs.setGeneralConfig(new GeneralConfig());
        TUIKit.init(this, TencentUtils.SDKAPPID, configs);

        HeytapPushManager.init(this, true);
        if (BrandUtil.isBrandXiaoMi()) {
            // 小米离线推送
            MiPushClient.registerPush(this, PrivateConstants.XM_PUSH_APPID, PrivateConstants.XM_PUSH_APPKEY);
        } else if (BrandUtil.isBrandHuawei()) {
            // 华为离线推送，设置是否接收Push通知栏消息调用示例
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
            // vivo离线推送
            PushClient.getInstance(getApplicationContext()).initialize();
        } else if (HeytapPushManager.isSupportPush()) {
            // oppo离线推送，因为需要登录成功后向我们后台设置token，所以注册放在MainActivity中做
        }

        registerActivityLifecycleCallbacks(new StatisticActivityLifecycleCallback());
    }

    /**
     * 腾讯云离线推送
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
                // 华为离线推送角标
                HUAWEIHmsMessageService.updateBadge(MyApplication.this, count);
            }
        };

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            LogUtils.i( "onActivityCreated bundle: " + bundle);
            if (bundle != null) { // 若bundle不为空则程序异常结束
                // 重启整个程序
                Intent intent = new Intent(activity, StartUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {
            foregroundActivities++;
            if (foregroundActivities == 1 && !isChangingConfiguration) {
                // 应用切到前台
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
                // 应用切到后台
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
                // 应用退到后台，消息转化为系统通知
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
     * 获取到主线程的handler
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
