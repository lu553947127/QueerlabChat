package com.queerlab.chat.tencent;

import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationListLayout;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.tencent
 * @ClassName: ConversationLayoutHelper
 * @Description: 通过API设置ConversataonLayout各种属性的样例
 * @Author: 鹿鸿祥
 * @CreateDate: 6/1/21 11:27 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 6/1/21 11:27 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ConversationLayoutHelper {

    /**
     * 自定义会话列表参数设置
     *
     * @param layout
     */
    public static void customizeConversation(final ConversationLayout layout) {
        ConversationListLayout listLayout = layout.getConversationList();
        listLayout.setItemTopTextSize(16); // 设置adapter item中top文字大小
        listLayout.setItemBottomTextSize(12);// 设置adapter item中bottom文字大小
        listLayout.setItemDateTextSize(10);// 设置adapter item中timeline文字大小
        listLayout.setItemAvatarRadius(180);// 设置adapter item头像圆角大小
        listLayout.disableItemUnreadDot(false);// 设置adapter item是否不显示未读红点，默认显示
    }
}
