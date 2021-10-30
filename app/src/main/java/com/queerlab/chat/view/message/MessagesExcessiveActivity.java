package com.queerlab.chat.view.message;

import android.os.Bundle;

import com.blankj.utilcode.util.ActivityUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.app.MyApplication;
import com.queerlab.chat.base.BaseActivity;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationLayout;

import butterknife.BindView;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.view.message
 * @ClassName: MessagesExcessiveActivity
 * @Description: 消息加载过度页
 * @Author: 鹿鸿祥
 * @CreateDate: 2021/10/20 11:04
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/10/20 11:04
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class MessagesExcessiveActivity extends BaseActivity {
    @BindView(R.id.conversation_layout)
    ConversationLayout conversationLayout;

    @Override
    protected int initLayoutRes() {
        return R.layout.activity_messages_excessive;
    }

    @Override
    public boolean isUseEventBus() {
        return false;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState) {
        conversationLayout.initDefault();

        showLoading();
        MyApplication.getMainThreadHandler().postDelayed(() -> {
            ActivityUtils.startActivity(MessagesActivity.class);
            finish();
            hideLoading();
        }, 400);
    }
}
