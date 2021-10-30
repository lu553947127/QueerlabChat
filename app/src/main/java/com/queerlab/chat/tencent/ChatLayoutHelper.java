package com.queerlab.chat.tencent;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.queerlab.chat.R;
import com.queerlab.chat.event.ChatEvent;
import com.tencent.imsdk.v2.V2TIMCustomElem;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.qcloud.tim.uikit.modules.chat.ChatLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.input.InputLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.MessageLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.IOnCustomMessageDrawListener;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;

import org.greenrobot.eventbus.EventBus;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.tencent
 * @ClassName: ChatLayoutHelper
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 6/1/21 3:07 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 6/1/21 3:07 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ChatLayoutHelper {

    private static final String TAG = ChatLayoutHelper.class.getSimpleName();

    private Context mContext;

    public ChatLayoutHelper(Context context) {
        mContext = context;
    }

    public void customizeChatLayout(final ChatLayout layout, boolean isGroup) {
        //====== MessageLayout使用范例 ======//
        MessageLayout messageLayout = layout.getMessageLayout();
//        ////// 设置聊天背景 //////
//        messageLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_white_top_25));
//        ////// 设置头像 //////
//        // 设置默认头像，默认与朋友与自己的头像相同
        messageLayout.setAvatar(R.drawable.default_head);
//        // 设置头像圆角
        messageLayout.setAvatarRadius(180);

//        ////// 设置气泡 ///////
//        // 设置自己聊天气泡的背景
        messageLayout.setRightBubble(ContextCompat.getDrawable(mContext, R.drawable.shape_yellow_10));
//        // 设置朋友聊天气泡的背景
        messageLayout.setLeftBubble(ContextCompat.getDrawable(mContext, R.drawable.shape_gray_10));

        // 设置自定义的消息渲染时的回调
//        messageLayout.setOnCustomMessageDrawListener(new CustomMessageDraw());

        //====== InputLayout使用范例 ======//
        final InputLayout inputLayout = layout.getInputLayout();
        // TODO 隐藏音频输入的入口，可以打开下面代码测试
        inputLayout.disableAudioInput(true);
        // TODO 隐藏表情输入的入口，可以打开下面代码测试
        inputLayout.disableEmojiInput(true);
        // TODO 隐藏更多功能的入口，可以打开下面代码测试
//        inputLayout.disableMoreInput(true);
        // TODO 可以用自定义的事件来替换更多功能的入口，可以打开下面代码测试
        inputLayout.replaceMoreInput(v -> {
            EventBus.getDefault().post(new ChatEvent(isGroup ? "group" : "chat"));
        });

        // TODO 自定义加号图标和自定义发送消息
//        InputMoreActionUnit action = new InputMoreActionUnit();
//        action.setIconResId(R.drawable.icon_album);
//        action.setTitleId(R.string.red_envelopes);
//        action.setOnClickListener(v -> {
//            Gson gson = new Gson();
//            CustomHelloMessage customHelloMessage = new CustomHelloMessage();
//            customHelloMessage.version = TUIKitConstants.version;
//            customHelloMessage.text = "我要发送红包";
//            customHelloMessage.link = "https://cloud.tencent.com/document/product/269/3794";
//
//            String data = gson.toJson(customHelloMessage);
//            MessageInfo info = MessageInfoUtil.buildCustomMessage(data);
//            layout.sendMessage(info, false);
//
//        });
//
//        inputLayout.addAction(action);


        // TODO 隐藏相机拍照
        inputLayout.disableCaptureAction(true);
        // TODO 隐藏发送文件
        inputLayout.disableSendFileAction(true);
        // TODO 隐藏相册
        inputLayout.disableSendPhotoAction(true);
        // TODO 隐藏拍摄视频
        inputLayout.disableVideoRecordAction(true);
        // TODO 隐藏语音通话
//        inputLayout.enableAudioCall();
        // TODO 隐藏视频通话
//        inputLayout.enableVideoCall();
    }

    public class CustomHelloMessage {
        String businessID = TUIKitConstants.BUSINESS_ID_CUSTOM_HELLO;
        String text = "欢迎加入云通信IM大家庭";
        String link = "https://cloud.tencent.com/document/product/269/3794";

        int version = TUIKitConstants.JSON_VERSION_UNKNOWN;
    }

    /**
     * 自定义消息
     */
    public class CustomMessageDraw implements IOnCustomMessageDrawListener {

        /**
         * 自定义消息渲染时，会调用该方法，本方法实现了自定义消息的创建，以及交互逻辑
         *
         * @param parent 自定义消息显示的父View，需要把创建的自定义消息view添加到parent里
         * @param info   消息的具体信息
         */
        @Override
        public void onDraw(ICustomMessageViewGroup parent, MessageInfo info, int position) {
            // 获取到自定义消息的json数据
            if (info.getTimMessage().getElemType() != V2TIMMessage.V2TIM_ELEM_TYPE_CUSTOM) {
                return;
            }
            V2TIMCustomElem elem = info.getTimMessage().getCustomElem();
            // 自定义的json数据，需要解析成bean实例
            CustomHelloMessage data = null;
            try {
                data = new Gson().fromJson(new String(elem.getData()), CustomHelloMessage.class);
            } catch (Exception e) {
                LogUtils.e(TAG, "invalid json: " + new String(elem.getData()) + " " + e.getMessage());
            }
            if (data == null) {
                LogUtils.e(TAG, "No Custom Data: " + new String(elem.getData()));
            } else if (data.version == TUIKitConstants.JSON_VERSION_1
                    || (data.version == TUIKitConstants.JSON_VERSION_4 && data.businessID.equals("text_link"))) {
                CustomHelloTIMUIController.onDraw(parent, data);
            } else {
                LogUtils.e(TAG, "unsupported version: " + data);
            }
        }
    }
}
