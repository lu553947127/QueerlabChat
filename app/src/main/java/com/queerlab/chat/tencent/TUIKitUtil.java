package com.queerlab.chat.tencent;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.queerlab.chat.R;
import com.queerlab.chat.app.MyApplication;
import com.queerlab.chat.base.BaseActivity;
import com.queerlab.chat.base.SpConfig;
import com.queerlab.chat.bean.GroupListBean;
import com.queerlab.chat.bean.HeatMapListBean;
import com.queerlab.chat.event.ChatEvent;
import com.queerlab.chat.event.LoginEvent;
import com.queerlab.chat.listener.OnCustomCallBack;
import com.queerlab.chat.tencent.model.TRTCVoiceRoom;
import com.queerlab.chat.tencent.model.TRTCVoiceRoomCallback;
import com.queerlab.chat.tencent.ui.room.VoiceRoomAnchorActivity;
import com.queerlab.chat.tencent.ui.room.VoiceRoomAudienceActivity;
import com.queerlab.chat.utils.LoginUtils;
import com.queerlab.chat.view.message.ChatActivity;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMGroupInfo;
import com.tencent.imsdk.v2.V2TIMGroupInfoResult;
import com.tencent.imsdk.v2.V2TIMGroupMemberFullInfo;
import com.tencent.imsdk.v2.V2TIMGroupMemberInfoResult;
import com.tencent.imsdk.v2.V2TIMGroupMemberOperationResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMSendCallback;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.map.sdk.utilities.heatmap.Gradient;
import com.tencent.map.sdk.utilities.heatmap.HeatMapTileProvider;
import com.tencent.map.sdk.utilities.heatmap.WeightedLatLng;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageCustom;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfoUtil;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.TileOverlay;
import com.tencent.tencentmap.mapsdk.maps.model.TileOverlayOptions;
import com.tencent.trtc.TRTCCloudDef;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.utils
 * @ClassName: TUIKitUtil
 * @Description: 腾讯云ui库工具类
 * @Author: 鹿鸿祥
 * @CreateDate: 5/24/21 8:53 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/24/21 8:53 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class TUIKitUtil {

    /**
     * 腾讯云登录
     *
     * @param userId 用户ID
     * @param userSig 用户密钥
     */
    public static void getTUIKitLogin(String userId, String userSig){
        TUIKit.login(userId, userSig, new IUIKitCallBack() {
            @Override
            public void onError(String module, final int code, final String desc) {
                ToastUtils.showShort("腾讯云IM登录失败：, errCode = " + code + ", errInfo = " + desc);
                LogUtils.e("imLogin errorCode = "  + code + " , errorInfo =  + " + desc);
//                EventBus.getDefault().post(new LoginEvent("onError"));
                LoginUtils.getExitLogin();
            }

            @Override
            public void onSuccess(Object data) {
                LogUtils.e("腾讯云登录成功");
                EventBus.getDefault().post(new LoginEvent("onSuccess"));
            }
        });
    }

    /**
     * 腾讯云TRTCVoiceRoom组件初始化
     *
     * @param context
     */
    public static void getTRTCVoiceRoomLogin(Context context){
        TRTCVoiceRoom mTRTCVoiceRoom = TRTCVoiceRoom.sharedInstance(context);
        mTRTCVoiceRoom.login(TencentUtils.SDKAPPID
                , SPUtils.getInstance().getString(SpConfig.USER_ID)
                , SPUtils.getInstance().getString(SpConfig.USER_SIG)
                , new TRTCVoiceRoomCallback.ActionCallback() {
                    @Override
                    public void onCallback(int code, String msg) {
                        LogUtils.e("TRTCVoiceRoom = "  + code + " , errorInfo =  + " + msg);
                        if (code == 0){
                            mTRTCVoiceRoom.setSelfProfile(SPUtils.getInstance().getString(SpConfig.USER_NAME)
                                    , SPUtils.getInstance().getString(SpConfig.USER_AVATAR)
                                    , new TRTCVoiceRoomCallback.ActionCallback() {
                                @Override
                                public void onCallback(int code, String msg) {
                                    LogUtils.e("setSelfProfile = "  + code + " , errorInfo =  + " + msg);
                                }
                            });
                        }
                    }
                });
    }

    /**
     * 绘制本地数据热力图
     *
     * @param activity 上下文
     * @param listener 监听
     * @param tencentMap 地图
     * @param tileOverlay
     * @return
     */
    public static TileOverlay getLocalHeatMapList(BaseActivity activity, HeatMapTileProvider.OnHeatMapReadyListener listener, TencentMap tencentMap, TileOverlay tileOverlay){
        BufferedReader br = null;
        try {
            ArrayList<WeightedLatLng> nodes = new ArrayList<>();
            br = new BufferedReader(new InputStreamReader(activity.getResources().getAssets().open("data2k")));
            String line;
            while ((line = br.readLine()) != null) {
                String[] lines = line.split("\t");
                if (lines.length == 3) {
                    double value = Double.parseDouble(lines[2]);
                    LatLng latLng = new LatLng((Double.parseDouble(lines[1])),
                            (Double.parseDouble(lines[0])));
                    nodes.add(new WeightedLatLng(latLng, value));
                }
            }

            HeatMapTileProvider mProvider = new HeatMapTileProvider.Builder()
                    .weightedData(nodes)
                    .gradient(HeatMapTileProvider.DEFAULT_GRADIENT)//热力图渐变方案
                    .opacity(HeatMapTileProvider.DEFAULT_OPACITY)//热力图透明度
                    .radius(HeatMapTileProvider.DEFAULT_RADIUS)//热力图半径
                    .readyListener(listener)
                    .build(tencentMap);
            tileOverlay = tencentMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return tileOverlay;
    }

    /**
     * 绘制接口数据热力图
     *
     * @param heatMapListBeans
     * @param listener
     * @param tencentMap
     * @param tileOverlay
     * @return
     */
    private static final int[] CUSTOM_GRADIENT_COLORS = {
            Color.rgb(103, 49, 152),
            Color.rgb(229, 51, 144),
            Color.rgb(255, 165, 44)
    };

    private static final float[] CUSTOM_GRADIENT_START_POINTS = {
            0.6f, 0.8f, 0.9f
    };
    public static final Gradient CUSTOM_HEATMAP_GRADIENT = new Gradient(CUSTOM_GRADIENT_COLORS,
            CUSTOM_GRADIENT_START_POINTS);
    public static TileOverlay getNetworkHeatMapList(List<HeatMapListBean> heatMapListBeans, HeatMapTileProvider.OnHeatMapReadyListener listener, TencentMap tencentMap, TileOverlay tileOverlay){
        ArrayList<WeightedLatLng> nodes = new ArrayList<>();

        for (HeatMapListBean bean : heatMapListBeans) {
            if (!TextUtils.isEmpty(bean.getLat()) && !TextUtils.isEmpty(bean.getLng())){
                nodes.add(new WeightedLatLng(new LatLng(Double.parseDouble(bean.getLat()), Double.parseDouble(bean.getLng())), Double.parseDouble(bean.getUserNum())));
            }
        }

        HeatMapTileProvider mProvider = new HeatMapTileProvider.Builder()
                .weightedData(nodes)
                .gradient(HeatMapTileProvider.DEFAULT_GRADIENT)//热力图渐变方案
                .opacity(HeatMapTileProvider.DEFAULT_OPACITY)//热力图透明度
                .radius(30)//热力图半径
                .readyListener(listener)
                .build(tencentMap);

        mProvider.setHeatTileGenerator(new HeatMapTileProvider.HeatTileGenerator() {
            @Override
            public double[] generateKernel(int radius) {
                double[] kernel = new double[radius * 2 + 1];
                for (int i = -radius; i <= radius; i++) {
                    kernel[i + radius] = Math.exp(-i * i / (2 * (radius / 2f) * (radius / 2f)));
                }
                return kernel;
            }

            @Override
            public int[] generateColorMap(double opacity) {
                return CUSTOM_HEATMAP_GRADIENT.generateColorMap(opacity);
            }
        });

        tileOverlay = tencentMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
        return tileOverlay;
    }


    /**
     * 设置用户头像
     *
     * @param mIconUrl 头像
     */
    public static void updateProfileAvatar(Context context, String mIconUrl){
        V2TIMUserFullInfo v2TIMUserFullInfo = new V2TIMUserFullInfo();

        // 头像
        if (!TextUtils.isEmpty(mIconUrl)) {
            v2TIMUserFullInfo.setFaceUrl(mIconUrl);
        }

        V2TIMManager.getInstance().setSelfInfo(v2TIMUserFullInfo, new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                LogUtils.e( "updateProfileAvatar err code = " + code + ", desc = " + desc);
                ToastUtil.toastShortMessage("Error code = " + code + ", desc = " + desc);
            }

            @Override
            public void onSuccess() {
                TUIKitConfigs.getConfigs().getGeneralConfig().setUserFaceUrl(mIconUrl);
            }
        });

        TRTCVoiceRoom mTRTCVoiceRoom = TRTCVoiceRoom.sharedInstance(context);
        mTRTCVoiceRoom.setSelfProfile(SPUtils.getInstance().getString(SpConfig.USER_NAME)
                , mIconUrl
                , new TRTCVoiceRoomCallback.ActionCallback() {
                    @Override
                    public void onCallback(int code, String msg) {
                        LogUtils.e("setSelfProfile = "  + code + " , errorInfo =  + " + msg);
                    }
                });
    }

    /**
     * 设置用户昵称
     *
     * @param nickName
     */
    public static void updateProfileName(Context context, String nickName){
        V2TIMUserFullInfo v2TIMUserFullInfo = new V2TIMUserFullInfo();

        // 昵称
        v2TIMUserFullInfo.setNickname(nickName);

        V2TIMManager.getInstance().setSelfInfo(v2TIMUserFullInfo, new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                LogUtils.e( "updateProfileName err code = " + code + ", desc = " + desc);
                ToastUtil.toastShortMessage("Error code = " + code + ", desc = " + desc);
            }

            @Override
            public void onSuccess() {
                TUIKitConfigs.getConfigs().getGeneralConfig().setUserNickname(nickName);
            }
        });

        TRTCVoiceRoom mTRTCVoiceRoom = TRTCVoiceRoom.sharedInstance(context);
        mTRTCVoiceRoom.setSelfProfile(nickName
                , SPUtils.getInstance().getString(SpConfig.USER_AVATAR)
                , new TRTCVoiceRoomCallback.ActionCallback() {
                    @Override
                    public void onCallback(int code, String msg) {
                        LogUtils.e("setSelfProfile = "  + code + " , errorInfo =  + " + msg);
                    }
                });
    }

    /**
     * 设置群聊头像
     *
     * @param groupId
     * @param mIconUrl
     */
    public static void updateGroupAvatar(String groupId, String mIconUrl){
        V2TIMGroupInfo v2TIMGroupInfo = new V2TIMGroupInfo();

        //需要修改的群聊ID
        v2TIMGroupInfo.setGroupID(groupId);

        //头像
        v2TIMGroupInfo.setFaceUrl(mIconUrl);

        V2TIMManager.getGroupManager().setGroupInfo(v2TIMGroupInfo, new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                LogUtils.e( "updateGroupAvatar err code = " + code + ", desc = " + desc);
            }

            @Override
            public void onSuccess() {

            }
        });
    }

    /**
     * 跳转聊天页面
     *
     * @param isGroup 是否单聊false/群聊true
     * @param id 用户ID
     * @param name 用户昵称
     */
    public static void startChatActivity(BaseActivity activity, boolean isGroup, String id, String name, int roomId){
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(isGroup ? V2TIMConversation.V2TIM_GROUP : V2TIMConversation.V2TIM_C2C);
        chatInfo.setId(id);
        chatInfo.setChatName(name);

        //判断是否是群聊
        if (isGroup){
            getGroupLeader(id, new OnCustomCallBack() {
                @Override
                public void onSuccess(Object data) {
                    String userId = String.valueOf(data);
                    if (SPUtils.getInstance().getString(SpConfig.USER_ID).equals(userId)){
                        VoiceRoomAnchorActivity.createRoom(activity, chatInfo.getChatName(), userId, SPUtils.getInstance().getString(SpConfig.USER_NAME), ""
                                , TRTCCloudDef.TRTC_AUDIO_QUALITY_MUSIC, false, chatInfo);
                    }else {
                        VoiceRoomAudienceActivity.enterRoom(activity, roomId, SPUtils.getInstance().getString(SpConfig.USER_ID)
                                , TRTCCloudDef.TRTC_AUDIO_QUALITY_DEFAULT, chatInfo);
                    }
                }

                @Override
                public void onError(String module, int errCode, String errMsg) {
                    LogUtils.e( "getGroupLeader failed, code: " + errCode + "|desc: " + errMsg);
                    if (errCode == 10010){
                        EventBus.getDefault().post(new ChatEvent("message"));
                    }else if (errCode == 10007){
                        ToastUtils.showShort("抱歉，您已不再当前群聊");
                    }else if (errCode == 6014){
                        TUIKit.login(SPUtils.getInstance().getString(SpConfig.USER_ID), SPUtils.getInstance().getString(SpConfig.USER_SIG), new IUIKitCallBack() {
                            @Override
                            public void onError(String module, final int code, final String desc) {
                                LogUtils.e("imLogin errorCode = "  + code + " , errorInfo =  + " + desc);
                                //若登录的userSig失效，则直接退出账号，重新登录
                                if (code == 6206){
                                    LoginUtils.getExitLogin();
                                }else {
                                    ToastUtils.showShort("腾讯云IM登录失败：, errCode = " + code + ", errInfo = " + desc);
                                }
                            }

                            @Override
                            public void onSuccess(Object data) {
                                LogUtils.e("腾讯云登录成功");
                                getGroupLeader(id, null);
                            }
                        });
                    }else {
                        ToastUtils.showShort("进群错误 code： " + errCode + "|desc: " + errMsg);
                    }
                }
            });
        }else {
            Bundle bundle = new Bundle();
            bundle.putSerializable("chatInfo", chatInfo);
            ActivityUtils.startActivity(bundle, ChatActivity.class);
        }
    }

    /**
     * 创建群聊
     *
     * @param groupName 群聊名称
     */
    public static void createGroup(String groupName, OnCustomCallBack callBack){
        V2TIMGroupInfo v2TIMGroupInfo = new V2TIMGroupInfo();
        v2TIMGroupInfo.setGroupType(V2TIMManager.GROUP_TYPE_MEETING);
        v2TIMGroupInfo.setGroupName(groupName);

        V2TIMManager.getGroupManager().createGroup(v2TIMGroupInfo, null, new V2TIMSendCallback<String>() {
            @Override
            public void onProgress(int progress) {

            }

            @Override
            public void onError(int code, String desc) {
                LogUtils.e( "createGroup failed, code: " + code + "|desc: " + desc);
                if (callBack != null) {
                    callBack.onError("onError", code, desc);
                }
            }

            @Override
            public void onSuccess(String groupId) {
                Gson gson = new Gson();
                MessageCustom messageCustom = new MessageCustom();
                messageCustom.version = TUIKitConstants.version;
                messageCustom.businessID = MessageCustom.BUSINESS_ID_GROUP_CREATE;
                messageCustom.opUser = SPUtils.getInstance().getString(SpConfig.USER_NAME);
                messageCustom.content = MyApplication.getInstance().getString(R.string.group_add_success);
                String data = gson.toJson(messageCustom);

                V2TIMMessage createTips = MessageInfoUtil.buildGroupCustomMessage(data);

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                sendMessage(createTips, groupId, new OnCustomCallBack() {
                    @Override
                    public void onSuccess(Object data) {
                        if (callBack != null) {
                            callBack.onSuccess(groupId);
                        }
                    }

                    @Override
                    public void onError(String module, int errCode, String errMsg) {
                        LogUtils.e( "sendTipsMessage failed, code: " + errCode + "|desc: " + errMsg);
                    }
                });
            }
        });
    }

    /**
     * 发送一条群聊文本消息
     *
     * @param groupId
     * @param content
     */
    public static void sendGroupNoticeMessage(String groupId, String name, String content){
        Gson gson = new Gson();
        MessageCustom messageCustom = new MessageCustom();
        messageCustom.version = TUIKitConstants.version;
        messageCustom.businessID = MessageCustom.BUSINESS_ID_GROUP_CREATE;
        messageCustom.opUser = name;
        messageCustom.content = content;
        String data = gson.toJson(messageCustom);

        V2TIMMessage createTips = MessageInfoUtil.buildGroupCustomMessage(data);

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sendMessage(createTips, groupId, new OnCustomCallBack() {
            @Override
            public void onSuccess(Object data) {
                LogUtils.e( "sendGroupNoticeMessage onSuccess");
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                LogUtils.e( "sendGroupNoticeMessage failed, code: " + errCode + "|desc: " + errMsg);
            }
        });
    }

    /**
     * 发送消息
     *
     * @param createTips
     * @param groupId
     */
    public static void sendMessage(V2TIMMessage createTips, String groupId, OnCustomCallBack callBack){
        V2TIMManager.getMessageManager().sendMessage(createTips, null, groupId, V2TIMMessage.V2TIM_PRIORITY_DEFAULT, false, null, new V2TIMSendCallback<V2TIMMessage>() {
            @Override
            public void onProgress(int progress) {

            }

            @Override
            public void onError(int code, String desc) {
                LogUtils.e( "sendMessage fail:" + code + "=" + desc);
            }

            @Override
            public void onSuccess(V2TIMMessage v2TIMMessage) {
                LogUtils.i( "sendTipsMessage onSuccess");
                if (callBack != null) {
                    callBack.onSuccess(v2TIMMessage);
                }
            }
        });
    }

    /**
     * 进入群聊
     *
     * @param listBean
     */
    public static void enterGroup(BaseActivity activity, GroupListBean.ListBean listBean, OnCustomCallBack callBack){
        //加入群聊
        V2TIMManager.getInstance().joinGroup(listBean.getGroup_no(), "大家好", new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                LogUtils.e( "joinGroup fail:" + code + "=" + desc);
                //若当前用户已是群成员，则直接打开群聊
                if (code == 10013){
                    TUIKitUtil.startChatActivity(activity,true, listBean.getGroup_no(), listBean.getGroup_type() + listBean.getGroup_name(), listBean.getRoomId());
                    //登录超时
                }else if (code == 6014){
                    TUIKit.login(SPUtils.getInstance().getString(SpConfig.USER_ID), SPUtils.getInstance().getString(SpConfig.USER_SIG), new IUIKitCallBack() {
                        @Override
                        public void onError(String module, final int code, final String desc) {
                            LogUtils.e("imLogin errorCode = "  + code + " , errorInfo =  + " + desc);
                            //若登录的userSig失效，则直接退出账号，重新登录
                            if (code == 6206){
                                LoginUtils.getExitLogin();
                            }else {
                                ToastUtils.showShort("腾讯云IM登录失败：, errCode = " + code + ", errInfo = " + desc);
                            }
                        }

                        @Override
                        public void onSuccess(Object data) {
                            LogUtils.e("腾讯云登录成功");
                            TUIKitUtil.startChatActivity(activity,true, listBean.getGroup_no(), listBean.getGroup_type() + listBean.getGroup_name(), listBean.getRoomId());
                        }
                    });
                }else if (code == 10037){
                    ToastUtils.showShort("可创建和加入的群组数量超过了限制");
                }else {
                    ToastUtils.showShort("进入群聊异常： " + code + "=" + desc);
                }
            }

            @Override
            public void onSuccess() {
                if (callBack != null) {
                    callBack.onSuccess("OnSuccess");
                }
            }
        });
    }

    /**
     * 获取群主ID
     *
     * @param groupId
     * @param callBack
     */
    public static void getGroupLeader(String groupId, OnCustomCallBack callBack){
        V2TIMManager.getGroupManager().getGroupMemberList(groupId, V2TIMGroupMemberFullInfo.V2TIM_GROUP_MEMBER_FILTER_OWNER, 0, new V2TIMSendCallback<V2TIMGroupMemberInfoResult>() {
            @Override
            public void onProgress(int progress) {

            }

            @Override
            public void onError(int code, String desc) {
                LogUtils.e("getGroupLeader = "  + code + " , errorInfo =  + " + desc);
                if (callBack != null) {
                    callBack.onError("onError", code, desc);
                }
            }

            @Override
            public void onSuccess(V2TIMGroupMemberInfoResult v2TIMGroupMemberInfoResult) {
                for (V2TIMGroupMemberFullInfo v2TIMGroupMemberFullInfo : v2TIMGroupMemberInfoResult.getMemberInfoList()) {
                    LogUtils.e("GroupLeader: " + v2TIMGroupMemberFullInfo.getUserID());
                    if (callBack != null) {
                        callBack.onSuccess(v2TIMGroupMemberFullInfo.getUserID());
                    }
                }
            }
        });
    }

    /**
     * 群主移出用户
     *
     * @param groupId
     * @param userId
     * @param callBack
     */
    public static void getKickGroupMember(String groupId, String userId, OnCustomCallBack callBack){
        List<String> members = new ArrayList<>(Arrays.asList(userId));
        V2TIMManager.getGroupManager().kickGroupMember(groupId, members, "", new V2TIMSendCallback<List<V2TIMGroupMemberOperationResult>>() {
            @Override
            public void onProgress(int progress) {

            }

            @Override
            public void onError(int code, String desc) {
                LogUtils.e( "kickGroupMember failed, code: " + code + "|desc: " + desc);
                ToastUtils.showShort("移出小组异常： " + code + "=" + desc);
            }

            @Override
            public void onSuccess(List<V2TIMGroupMemberOperationResult> v2TIMGroupMemberOperationResults) {
                if (callBack != null) {
                    callBack.onSuccess("");
                }
            }
        });
    }

    /**
     * 获取群聊的免打扰状态
     *
     * @param groupId
     * @param callBack
     */
    public static void getDisturbStatus(String groupId, OnCustomCallBack callBack){
        List<String> groupList = new ArrayList<>(Arrays.asList(groupId));
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
                    LogUtils.e("result.getGroupInfo().getRecvOpt(): " + result.getGroupInfo().getRecvOpt());
                    if (callBack != null) {
                        callBack.onSuccess(String.valueOf(result.getGroupInfo().getRecvOpt()));
                    }
                }
            }
        });
    }

    /**
     * 设置群聊消息免打扰状态
     *
     * @param groupId
     * @param opt
     */
    public static void setDisturbStatus(String groupId, int opt){
        V2TIMManager.getMessageManager().setGroupReceiveMessageOpt(groupId, opt, new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                LogUtils.e("setReceiveMessageOpt = "  + code + " , errorInfo =  + " + desc);
            }

            @Override
            public void onSuccess() {
                ToastUtils.showShort("设置成功");
            }
        });
    }
}
