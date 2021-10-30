package com.queerlab.chat.view.message;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.lxj.xpopup.XPopup;
import com.queerlab.chat.R;
import com.queerlab.chat.base.BaseActivity;
import com.queerlab.chat.event.ChatEvent;
import com.queerlab.chat.tencent.ConversationLayoutHelper;
import com.queerlab.chat.tencent.TUIKitUtil;
import com.queerlab.chat.viewmodel.GroupViewModel;
import com.queerlab.chat.viewmodel.NoticeViewModel;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationInfo;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.view.message
 * @ClassName: MessagesActivity
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 2021/10/18 10:35
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/10/18 10:35
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class MessagesActivity extends BaseActivity {
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.tv_notice)
    AppCompatTextView tvNotice;
    @BindView(R.id.conversation_layout)
    ConversationLayout conversationLayout;
    private NoticeViewModel noticeViewModel;
    private String url;
    private int position;
    private ConversationInfo conversationInfo;
    private GroupViewModel groupViewModel;

    @Override
    protected int initLayoutRes() {
        return R.layout.activity_messages;
    }

    @Override
    public boolean isUseEventBus() {
        return true;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(fakeStatusBar, getResources().getColor(R.color.white));

        tvNotice.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        noticeViewModel = getViewModel(NoticeViewModel.class);
        groupViewModel = getViewModel(GroupViewModel.class);

        //获取系统通知数据成功返回
        noticeViewModel.noticeLiveData.observe(activity, noticeBean -> {
            tvNotice.setText(noticeBean.getNotice_title());
            url = noticeBean.getNotice_content();
        });

        //获取当前群聊的roomId
        groupViewModel.getGroupRoomIdLiveData.observe(activity, groupRoomIdBean -> {
            if (groupRoomIdBean.getRoomId() != 0){
                TUIKitUtil.startChatActivity(activity, conversationInfo.isGroup(), conversationInfo.getId(), conversationInfo.getTitle(), groupRoomIdBean.getRoomId());
            }else {
                TUIKitUtil.startChatActivity(activity, conversationInfo.isGroup(), conversationInfo.getId(), conversationInfo.getTitle(), 0);
            }
        });

        initConversation();
        noticeViewModel.notice();
    }

    /**
     * 初始化
     */
    private void initConversation() {
        conversationLayout.initDefault();
        // 获取 TitleBarLayout
        TitleBarLayout titleBarLayout = conversationLayout.findViewById(R.id.conversation_title);
        titleBarLayout.setVisibility(View.GONE);

        // 通过API设置ConversataonLayout各种属性的样例
        ConversationLayoutHelper.customizeConversation(conversationLayout);

        //会话列表item点击
        conversationLayout.getConversationList().setOnItemClickListener((view1, position, conversationInfo) -> {
            this.position = position;
            this.conversationInfo = conversationInfo;
            if (conversationInfo.isGroup()){
                groupViewModel.getGroupRoomId(conversationInfo.getId());
            }else {
                TUIKitUtil.startChatActivity(activity, conversationInfo.isGroup(), conversationInfo.getId(), conversationInfo.getTitle(), 0);
            }
        });

        //会话列表item长点击
        conversationLayout.getConversationList().setOnItemLongClickListener((view1, position, conversationInfo) -> {
            new XPopup.Builder(activity)
                    .hasShadowBg(false)
                    .atView(view1)
                    .asAttachList(new String[]{conversationInfo.isTop() ? "取消置顶" : "置顶聊天", "删除聊天"}, null,
                            (position1, text) -> {
                                switch (text){
                                    case "取消置顶":
                                    case "置顶聊天":
                                        conversationLayout.setConversationTop(conversationInfo, new IUIKitCallBack() {
                                            @Override
                                            public void onSuccess(Object data) {

                                            }

                                            @Override
                                            public void onError(String module, int errCode, String errMsg) {

                                            }
                                        });
                                        break;
                                    case "删除聊天":
                                        conversationLayout.deleteConversation(position, conversationInfo);
                                        break;
                                }
                            }).show();
        });
    }

    /**
     * 当群聊异常不存在，自动删除
     *
     * @param event
     */
    @Subscribe
    public void onEventGroup(ChatEvent event) {
        if (event.getStatus().equals("message")){
            conversationLayout.deleteConversation(position, conversationInfo);
        }
    }

    @OnClick({R.id.iv_bar_back, R.id.tv_notice})
    void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.iv_bar_back:
                finish();
                break;
            case R.id.tv_notice://通知页面
                if (TextUtils.isEmpty(url)){
                    return;
                }
                bundle.putString("type", "notice");
                bundle.putString("url", url);
                ActivityUtils.startActivity(bundle, WebViewActivity.class);
                break;
        }
    }
}
