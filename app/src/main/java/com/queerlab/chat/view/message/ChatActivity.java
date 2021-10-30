package com.queerlab.chat.view.message;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.base.BaseActivity;
import com.queerlab.chat.base.SpConfig;
import com.queerlab.chat.event.ChatEvent;
import com.queerlab.chat.tencent.ChatLayoutHelper;
import com.queerlab.chat.utils.PermissionUtils;
import com.queerlab.chat.utils.PictureUtils;
import com.queerlab.chat.view.group.user.UserInfoActivity;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.ChatLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.MessageLayout;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfoUtil;
import com.zhihu.matisse.Matisse;

import org.greenrobot.eventbus.Subscribe;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.view.message
 * @ClassName: ChatActivity
 * @Description: 单聊页面
 * @Author: 鹿鸿祥
 * @CreateDate: 6/1/21 2:17 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 6/1/21 2:17 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ChatActivity extends BaseActivity {
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.chat_layout)
    ChatLayout mChatLayout;
    private ChatInfo mChatInfo;

    @Override
    protected int initLayoutRes() {
        return R.layout.activity_chat;
    }

    @Override
    public boolean isUseEventBus() {
        return true;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(fakeStatusBar, getResources().getColor(R.color.color_F4F7FE));
    }

    @OnClick({R.id.iv_bar_back})
    void onClick() {
        finish();
    }

    /**
     * 初始化
     */
    private void initChat() {
        mChatInfo = (ChatInfo) getIntent().getSerializableExtra("chatInfo");

        tvTitle.setText(mChatInfo.getChatName());

        //单聊组件的默认UI和交互初始化
        mChatLayout.initDefault();

        /*
         * 需要聊天的基本信息
         */
        mChatLayout.setChatInfo(mChatInfo);

        //获取单聊面板的标题栏
        TitleBarLayout mTitleBar = mChatLayout.getTitleBar();
        mTitleBar.setVisibility(View.GONE);

        ChatLayoutHelper helper = new ChatLayoutHelper(activity);
        helper.customizeChatLayout(mChatLayout, false);

        //item点击监听
        mChatLayout.getMessageLayout().setOnItemClickListener(new MessageLayout.OnItemLongClickListener() {
            @Override
            public void onMessageLongClick(View view, int position, MessageInfo messageInfo) {
                //因为adapter中第一条为加载条目，位置需减1
                mChatLayout.getMessageLayout().showItemPopMenu(position - 1, messageInfo, view);
            }

            @Override
            public void onUserIconClick(View view, int position, MessageInfo messageInfo) {
                if (null == messageInfo) {
                    return;
                }

                if (messageInfo.getFromUser().equals(SPUtils.getInstance().getString(SpConfig.USER_ID))){
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putString("userId", messageInfo.getFromUser());
                ActivityUtils.startActivity(bundle, UserInfoActivity.class);
            }
        });
    }

    /**
     * 循环发送图片消息
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureUtils.ALBUM_CODE & resultCode == RESULT_OK) {
            for (int i = 0; i < Matisse.obtainResult(data).size(); i++) {
                LogUtils.e(Matisse.obtainResult(Objects.requireNonNull(data)).get(i));
                MessageInfo info = MessageInfoUtil.buildImageMessage(Matisse.obtainResult(data).get(i), false);
                mChatLayout.sendMessage(info, false);
            }
        }
    }

    /**
     * 发送图片
     *
     * @param event
     */
    @Subscribe
    public void onEventImage(ChatEvent event) {
        if (event.getStatus().equals("chat")){
            PermissionUtils.openStoragePermission(activity, 3);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initChat();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatLayout != null) {
            mChatLayout.exitChat();
        }
    }
}
