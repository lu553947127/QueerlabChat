package com.queerlab.chat.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.LogUtils;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.heytap.msp.push.HeytapPushManager;
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;
import com.queerlab.chat.R;
import com.queerlab.chat.base.BaseActivity;
import com.queerlab.chat.push.BrandUtil;
import com.queerlab.chat.push.HUAWEIHmsMessageService;
import com.queerlab.chat.push.OPPOPushImpl;
import com.queerlab.chat.push.PrivateConstants;
import com.queerlab.chat.push.ThirdPushTokenMgr;
import com.queerlab.chat.tencent.TUIKitUtil;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationInfo;
import com.vivo.push.IPushActionListener;
import com.vivo.push.PushClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import per.goweii.swipeback.SwipeBackDirection;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.base
 * @ClassName: BaseActivity
 * @Description: 首页
 * @Author: 鹿鸿祥
 * @CreateDate: 5/6/21 11:26 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/6/21 11:26 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class MainActivity extends BaseActivity implements ConversationManagerKit.MessageUnreadWatcher{
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    List<Fragment> mFragments;
    //用于记录上个选择的Fragment
    private int lastFragment;

    @Override
    protected int initLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public boolean isUseEventBus() {
        return false;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(fakeStatusBar, getResources().getColor(R.color.white));
        initFragment();
        getBadgeViewInitView();
        //初始化会话列表，不然一进入app不加载会话列表,操作会话相关的信息报空指针
        ConversationManagerKit.getInstance().loadConversation(0,null);
        prepareThirdPushToken();
        //腾讯云语音聊天室登录初始化
        TUIKitUtil.getTRTCVoiceRoomLogin(activity);
    }

    @NonNull
    @Override
    public SwipeBackDirection swipeBackDirection() {
        return SwipeBackDirection.NONE;
    }

    /**
     * 初始化fragment和fragment数组
     */
    private void initFragment() {
        mFragments = new ArrayList<>();
        mFragments.add(new IndexFragment());
        mFragments.add(new MapFragment());
        mFragments.add(new ActivityFragment());
        mFragments.add(new MessagesFragment());
        mFragments.add(new MineFragment());

        switchFragment(lastFragment,0);lastFragment=0;
        navigation.getMenu().getItem(0).setChecked(true);
        //判断选择的菜单
        navigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_index:
                    BarUtils.setStatusBarColor(fakeStatusBar, getResources().getColor(R.color.white));
                    if(lastFragment!=0)switchFragment(lastFragment,0);lastFragment=0;
                    break;
                case R.id.menu_find:
                    BarUtils.setStatusBarColor(fakeStatusBar, getResources().getColor(R.color.white));
                    if(lastFragment!=1)switchFragment(lastFragment,1);lastFragment=1;
                    break;
                case R.id.menu_activity:
                    BarUtils.setStatusBarColor(fakeStatusBar, getResources().getColor(R.color.white));
                    if(lastFragment!=2)switchFragment(lastFragment,2);lastFragment=2;
                    break;
                case R.id.menu_message:
                    BarUtils.setStatusBarColor(fakeStatusBar, getResources().getColor(R.color.white));
                    if(lastFragment!=3)switchFragment(lastFragment,3);lastFragment=3;
                    break;
                case R.id.menu_mine:
                    BarUtils.setStatusBarColor(fakeStatusBar, getResources().getColor(R.color.color_F4F7FE));
                    if(lastFragment!=4)switchFragment(lastFragment,4);lastFragment=4;
                    break;
                default:
                    break;
            }
            // 这里注意返回true,否则点击失效
            return true;
        });
        clearToast();
    }

    /**
     * 切换Fragment
     *
     * @param lastFragment
     * @param index
     */
    private void switchFragment(int lastFragment,int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(mFragments.get(lastFragment));//隐藏上个Fragment
        if(!mFragments.get(index).isAdded()) {
            transaction.add(R.id.fragment,mFragments.get(index));
        }
        transaction.show(mFragments.get(index)).commitAllowingStateLoss();
    }

    /**
     * 解决BottomNavigationView 长按时出现的吐司
     */
    private void clearToast() {
        List<Integer> ids =new ArrayList<>();
        ids.add(R.id.menu_index);
        ids.add(R.id.menu_find);
        ids.add(R.id.menu_activity);
        ids.add(R.id.menu_message);
        ids.add(R.id.menu_mine);
        ViewGroup bottomNavigationMenuView = (ViewGroup) navigation.getChildAt(0);
        //遍历子View,重写长按点击事件    
        for (int position = 0; position < ids.size(); position++) {
            bottomNavigationMenuView.getChildAt(position).findViewById(ids.get(position)).setOnLongClickListener(view -> true);
        }
    }

    /**
     * 设置底部消息提醒数字布局
     */
    private RelativeLayout relativeLayout;
    private TextView number;
    private void getBadgeViewInitView() {
        //底部标题栏右上角标设置
        //获取整个的NavigationView
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        //这里就是获取所添加的每一个Tab(或者叫menu)，设置在标题栏的位置
        View tab = menuView.getChildAt(3);
        BottomNavigationItemView itemView = (BottomNavigationItemView) tab;
        //加载我们的角标View，新创建的一个布局
        View badge = LayoutInflater.from(this).inflate(R.layout.layout_apply_count, menuView, false);
        //添加到Tab上
        itemView.addView(badge);
        //显示角标数字
        relativeLayout = badge.findViewById(R.id.rl);
        //显示/隐藏整个视图
        number = badge.findViewById(R.id.number);
    }

    /**
     * 消息未读显示
     *
     * @param count
     */
    private void getMessageUnread(int count){
        if (count < 1) {
            relativeLayout.setVisibility(View.GONE);
        } else if (count < 100) {
            relativeLayout.setVisibility(View.VISIBLE);
            number.setTextSize(11);
            number.setText(String.valueOf(count));
        } else {
            relativeLayout.setVisibility(View.VISIBLE);
            number.setTextSize(9);
            number.setText("99+");
        }
        // 华为离线推送角标
        HUAWEIHmsMessageService.updateBadge(this, count);
    }

    /**
     * 设置底部角标显示状态
     *
     * @param count 未读消息数量
     */
    @Override
    public void updateUnread(int count) {
        getMessageUnread(count);
    }

    @Override
    protected void onStop() {
        ConversationManagerKit.getInstance().destroyConversation();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 未读消息监视器
        ConversationManagerKit.getInstance().addUnreadWatcher(this);
        //获取未读消息显示
        V2TIMManager.getConversationManager().getConversationList(0, 100, new V2TIMValueCallback<V2TIMConversationResult>() {
            @Override
            public void onError(int code, String desc) {
                LogUtils.v( "loadConversation getConversationList error, code = " + code + ", desc = " + desc);
            }

            @Override
            public void onSuccess(V2TIMConversationResult v2TIMConversationResult) {
                ArrayList<ConversationInfo> infos = new ArrayList<>();
                List<V2TIMConversation> v2TIMConversationList = v2TIMConversationResult.getConversationList();
                int count = 0;
                for (V2TIMConversation v2TIMConversation : v2TIMConversationList) {
                    count += v2TIMConversation.getUnreadCount();
                }
//                LogUtils.e("getUnreadCount: " + count);
                // 华为离线推送角标
                HUAWEIHmsMessageService.updateBadge(activity, count);
            }
        });
    }

    /**
     * 离线推送
     */
    private void prepareThirdPushToken() {
        ThirdPushTokenMgr.getInstance().setPushTokenToTIM();

        if (BrandUtil.isBrandHuawei()) {
            // 华为离线推送
            new Thread() {
                @Override
                public void run() {
                    try {
                        // read from agconnect-services.json
                        String appId = AGConnectServicesConfig.fromContext(MainActivity.this).getString("client/app_id");
                        String token = HmsInstanceId.getInstance(MainActivity.this).getToken(appId, "HCM");
                        LogUtils.i( "huawei get token:" + token);
                        if(!TextUtils.isEmpty(token)) {
                            ThirdPushTokenMgr.getInstance().setThirdPushToken(token);
                            ThirdPushTokenMgr.getInstance().setPushTokenToTIM();
                        }
                    } catch (ApiException e) {
                        LogUtils.e( "huawei get token failed, " + e);
                    }
                }
            }.start();
        } else if (BrandUtil.isBrandVivo()) {
            // vivo离线推送
            LogUtils.i( "vivo support push: " + PushClient.getInstance(getApplicationContext()).isSupport());
            PushClient.getInstance(getApplicationContext()).turnOnPush(new IPushActionListener() {
                @Override
                public void onStateChanged(int state) {
                    if (state == 0) {
                        String regId = PushClient.getInstance(getApplicationContext()).getRegId();
                        LogUtils.i( "vivopush open vivo push success regId = " + regId);
                        ThirdPushTokenMgr.getInstance().setThirdPushToken(regId);
                        ThirdPushTokenMgr.getInstance().setPushTokenToTIM();
                    } else {
                        // 根据vivo推送文档说明，state = 101 表示该vivo机型或者版本不支持vivo推送，链接：https://dev.vivo.com.cn/documentCenter/doc/156
                        LogUtils.i( "vivopush open vivo push fail state = " + state);
                    }
                }
            });
        } else if (HeytapPushManager.isSupportPush()) {
            // oppo离线推送
            OPPOPushImpl oppo = new OPPOPushImpl();
            oppo.createNotificationChannel(this);
            HeytapPushManager.register(this, PrivateConstants.OPPO_PUSH_APPKEY, PrivateConstants.OPPO_PUSH_APPSECRET, oppo);
        }
    }

    /**
     * 重写返回键
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //实现只在冷启动时显示启动页，类似微信效果
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //实现只在冷启动时显示启动页，即点击返回键与点击HOME键退出效果一致
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}