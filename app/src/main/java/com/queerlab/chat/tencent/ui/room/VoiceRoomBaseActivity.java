package com.queerlab.chat.tencent.ui.room;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.app.MyApplication;
import com.queerlab.chat.base.BaseActivity;
import com.queerlab.chat.base.SpConfig;
import com.queerlab.chat.event.ChatEvent;
import com.queerlab.chat.event.PermissionEvent;
import com.queerlab.chat.listener.OnCustomCallBack;
import com.queerlab.chat.tencent.ChatLayoutHelper;
import com.queerlab.chat.tencent.TUIKitUtil;
import com.queerlab.chat.tencent.model.TRTCVoiceRoom;
import com.queerlab.chat.tencent.model.TRTCVoiceRoomCallback;
import com.queerlab.chat.tencent.model.TRTCVoiceRoomDef;
import com.queerlab.chat.tencent.model.TRTCVoiceRoomDelegate;
import com.queerlab.chat.tencent.ui.base.MemberEntity;
import com.queerlab.chat.tencent.ui.base.VoiceRoomSeatEntity;
import com.queerlab.chat.tencent.ui.widget.AudioEffectPanel;
import com.queerlab.chat.tencent.ui.widget.ConfirmDialogFragment;
import com.queerlab.chat.tencent.ui.widget.InputTextMsgDialog;
import com.queerlab.chat.tencent.ui.widget.MoreActionDialog;
import com.queerlab.chat.tencent.ui.widget.SelectMemberView;
import com.queerlab.chat.tencent.ui.widget.msg.AudienceEntity;
import com.queerlab.chat.tencent.ui.widget.msg.MsgEntity;
import com.queerlab.chat.tencent.ui.widget.msg.MsgListAdapter;
import com.queerlab.chat.utils.PermissionUtils;
import com.queerlab.chat.utils.PictureUtils;
import com.queerlab.chat.view.group.group.GroupInfoActivity;
import com.queerlab.chat.view.group.user.UserInfoActivity;
import com.queerlab.chat.viewmodel.GroupViewModel;
import com.squareup.picasso.Picasso;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.ChatLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.input.InputLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.MessageLayout;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfo;
import com.tencent.qcloud.tim.uikit.modules.group.info.StartGroupMemberSelectActivity;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfoUtil;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.trtc.TRTCCloudDef;
import com.zhihu.matisse.Matisse;

import org.greenrobot.eventbus.Subscribe;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.queerlab.chat.tencent.model.TRTCVoiceRoomDef.SeatInfo.STATUS_CLOSE;
import static com.queerlab.chat.tencent.model.TRTCVoiceRoomDef.SeatInfo.STATUS_UNUSED;
import static com.queerlab.chat.tencent.model.TRTCVoiceRoomDef.SeatInfo.STATUS_USED;

/**
 * 群聊页面
 */
public class VoiceRoomBaseActivity extends BaseActivity implements VoiceRoomSeatAdapter.OnItemClickListener
        , TRTCVoiceRoomDelegate, InputTextMsgDialog.OnTextSendListener, MsgListAdapter.OnItemClickListener {

    protected static final String TAG = VoiceRoomBaseActivity.class.getName();

    protected static final int    MAX_SEAT_SIZE           = 9;//当前麦序总个数（包含房主在内）
    protected static final String VOICEROOM_ROOM_ID       = "room_id";//房间id
    protected static final String VOICEROOM_ROOM_NAME     = "room_name";//房间名称
    protected static final String VOICEROOM_USER_NAME     = "user_name";//用户名称
    protected static final String VOICEROOM_USER_ID       = "user_id";//用户id
    protected static final String VOICEROOM_USER_SIG      = "user_sig";//用户签名
    protected static final String VOICEROOM_NEED_REQUEST  = "need_request";//上麦是否需要申请验证
    protected static final String VOICEROOM_SEAT_COUNT    = "seat_count";//麦序数量
    protected static final String VOICEROOM_AUDIO_QUALITY = "audio_quality";//音频音质
    protected static final String VOICEROOM_USER_AVATAR   = "user_avatar";//用户头像
    protected static final String VOICEROOM_ROOM_COVER    = "room_cover";//房间缩略图

    //发送消息用户名颜色集合
    private static final int MESSAGE_USERNAME_COLOR_ARR []       =  {
            R.color.trtcvoiceroom_color_msg_1,
            R.color.trtcvoiceroom_color_msg_2,
            R.color.trtcvoiceroom_color_msg_3,
            R.color.trtcvoiceroom_color_msg_4,
            R.color.trtcvoiceroom_color_msg_5,
            R.color.trtcvoiceroom_color_msg_6,
            R.color.trtcvoiceroom_color_msg_7,
    };

    /**
     *
     */
    protected String        mSelfUserId;     //进房用户ID
    protected int           mCurrentRole;    //用户当前角色
    protected Set<String>   mSeatUserSet; //在座位上的主播集合
    protected TRTCVoiceRoom mTRTCVoiceRoom; //语音直播室音频组件
    private boolean isInitSeat;

    protected List<VoiceRoomSeatEntity> mVoiceRoomSeatEntityList; //麦序坐席列表
    protected Map<String, Boolean>      mSeatUserMuteMap;
    protected VoiceRoomSeatAdapter      mVoiceRoomSeatAdapter; //麦序坐席列表适配器
//    protected AudienceListAdapter       mAudienceListAdapter;
//    protected TextView                  mTvRoomName;
//    protected TextView                  mTvRoomId;
    protected CircleImageView           mImgHead; //主播头像
//    protected CircleImageView           mIvAnchorHead;
    protected ImageView                 mIvManagerMute;//主播麦克风开关icon
    protected ImageView                 mIvManagerTalk;//主播音量icon
    protected TextView                  mTvName;//主播名称
    protected RecyclerView              mRvSeat; //麦序列表rv
//    protected RecyclerView              mRvAudience;
//    protected RecyclerView              mRvImMsg;
//    protected View                      mToolBarView;
//    protected ImageView                 mRootBg;
//    protected AppCompatImageButton mBtnExitRoom;
//    protected AppCompatImageButton      mBtnMsg;
//    protected AppCompatImageButton      mBtnMic;
//    protected AppCompatImageButton      mBtnEffect;
//    protected AppCompatImageButton      mBtnLeaveSeat;
//    protected AppCompatImageButton      mBtnMore;
//    protected ImageView                 mIvAudienceMove;

    protected AudioEffectPanel mAnchorAudioPanel;
    protected SelectMemberView mViewSelectMember;
    protected InputTextMsgDialog        mInputTextMsgDialog;
    protected int                       mRoomId;//房间id
    protected String                    mRoomName;//房间名称
    protected String                    mUserName;//用户名称
    protected String                    mUserAvatar;//用户头像
    protected String                    mRoomCover;//房间缩略图
    protected String                    mMainSeatUserId;
    protected boolean                   mNeedRequest;//上麦是否需要申请验证
    protected int                       mAudioQuality;//音频音质
//    protected List<MsgEntity>           mMsgEntityList;
//    protected LinkedList<AudienceEntity> mAudienceEntityList;
//    protected MsgListAdapter            mMsgListAdapter;
    protected ConfirmDialogFragment mConfirmDialogFragment;
    protected List<MemberEntity>        mMemberEntityList;
    protected Map<String, MemberEntity> mMemberEntityMap;

    private int mMessageColorIndex;
    private Context mContext;
    private int mRvAudienceScrollPosition;
    private boolean mIsMainSeatMute;
    private int mSelfSeatIndex = -1;

    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.iv_more)
    AppCompatImageView ivMore;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.iv_voice)
    AppCompatImageView ivVoice;
    @BindView(R.id.ll_operation)
    LinearLayout llOperation;
    @BindView(R.id.iv_mike)
    AppCompatImageButton ivMike;
    @BindView(R.id.chat_layout)
    ChatLayout mChatLayout;
    @BindView(R.id.cl_voice)
    ConstraintLayout clVoice;
    protected ChatInfo mChatInfo;
    private String userId;
    protected GroupViewModel groupViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        // 应用运行时，保持不锁屏、全屏化
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.trtcvoiceroom_activity_main);
    }

    @Override
    protected int initLayoutRes() {
        return R.layout.trtcvoiceroom_activity_main;
    }

    @Override
    public boolean isUseEventBus() {
        return true;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState) {
//        initStatusBar();
        initView();
        initData();
//        initListener();
//        MsgEntity msgEntity = new MsgEntity();
//        msgEntity.type = MsgEntity.TYPE_WELCOME;
//        msgEntity.content = getString(R.string.trtcvoiceroom_welcome_visit);
//        msgEntity.linkUrl = getString(R.string.trtcvoiceroom_welcome_visit_link);
//        showImMsg(msgEntity);

        BarUtils.setStatusBarColor(fakeStatusBar, getResources().getColor(R.color.color_F4F7FE));
        groupViewModel = getViewModel(GroupViewModel.class);
    }

    @OnClick({R.id.iv_bar_back, R.id.iv_more, R.id.iv_voice, R.id.iv_close, R.id.iv_mike})
    void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()){
            case R.id.iv_bar_back:
                onBackPressed();
                break;
            case R.id.iv_more:
                bundle.putString("groupId", mChatInfo.getId());
                bundle.putString("userId", userId);
                ActivityUtils.startActivity(bundle, GroupInfoActivity.class);
                break;
            case R.id.iv_voice://开启语音通话
                PermissionUtils.openStorageAudio(activity);
                break;
            case R.id.iv_close://结束语音通话
                if (SPUtils.getInstance().getString(SpConfig.USER_ID).equals(userId)){
                    groupViewModel.voiceRoomDelete(mChatInfo.getId());
                }else {
                    groupViewModel.voiceRoomLogout(mChatInfo.getId(), SPUtils.getInstance().getString(SpConfig.USER_ID));
                }
                break;
            case R.id.iv_mike://开/关麦克风
                updateMicButton();
                break;
        }
    }

    /**
     * 初始化
     */
    private void initChat() {
        mChatInfo = (ChatInfo) getIntent().getSerializableExtra("chatInfo");

        if (mChatInfo.getType() == V2TIMConversation.V2TIM_GROUP){
            ivMore.setVisibility(View.VISIBLE);
            ivVoice.setVisibility(View.VISIBLE);
        }else {
            ivVoice.setVisibility(View.GONE);
        }

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
        helper.customizeChatLayout(mChatLayout, true);

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

        //群聊@某个人
//        mChatLayout.getInputLayout().setStartActivityListener(new InputLayout.OnStartActivityListener() {
//            @Override
//            public void onStartGroupMemberSelectActivity() {
//                Intent intent = new Intent(MyApplication.getInstance(), StartGroupMemberSelectActivity.class);
//                GroupInfo groupInfo = new GroupInfo();
//                groupInfo.setId(mChatInfo.getId());
//                groupInfo.setChatName(mChatInfo.getChatName());
//                intent.putExtra(TUIKitConstants.Group.GROUP_INFO, groupInfo);
//                startActivityForResult(intent, 1);
//            }
//        });

        isGroupLeader();
    }

    /**
     * 判断当前用户是否是群主
     */
    private void isGroupLeader(){
        TUIKitUtil.getGroupLeader(mChatInfo.getId(), new OnCustomCallBack() {
            @Override
            public void onSuccess(Object data) {
                userId = String.valueOf(data);
                if (SPUtils.getInstance().getString(SpConfig.USER_ID).equals(userId)){
                    ivVoice.setImageResource(R.drawable.svg_icon_voice);
                }else {
                    ivVoice.setImageResource(R.drawable.icon_voice_join);
                }
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

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
        if (event.getStatus().equals("group")){
            PermissionUtils.openStoragePermission(activity, 3);
        }
    }

    /**
     * 权限开启成功
     *
     * @param event
     */
    @Subscribe
    public void onEventPermission(PermissionEvent event) {
        if (SPUtils.getInstance().getString(SpConfig.USER_ID).equals(userId)){
            groupViewModel.voiceRoomCreate(mChatInfo.getId(), mRoomId, userId);
        }else {
            LogUtils.e("mRoomId: " + mRoomId);
            if (mRoomId == 0){
                ToastUtils.showShort("抱歉，当前群聊还未开启语音聊天哦");
                return;
            }
            groupViewModel.voiceRoomJoin(mChatInfo.getId(), SPUtils.getInstance().getString(SpConfig.USER_ID));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initChat();
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAnchorAudioPanel != null) {
            mAnchorAudioPanel.unInit();
            mAnchorAudioPanel = null;
        }

        if (mChatLayout != null) {
            mChatLayout.exitChat();
        }

        //退出房间后，释放内存
        mTRTCVoiceRoom.setDelegate(null);
    }

    /**
     * 麦克风开启/关闭
     */
    private void updateMicButton() {
        if (checkButtonPermission()) {
//            boolean currentMode = !mBtnMic.isSelected();
            boolean currentMode = !ivMike.isSelected();
            if (currentMode) {
                if (!isSeatMute(mSelfSeatIndex)) {
                    updateMuteStatusView(mSelfUserId, false);
                    mTRTCVoiceRoom.muteLocalAudio(false);
                    ToastUtils.showLong(getString(R.string.trtcvoiceroom_toast_you_have_turned_on_the_microphone));
                } else {
                    ToastUtils.showLong(getString(R.string.trtcvoiceroom_seat_already_mute));
                }
            } else {
                mTRTCVoiceRoom.muteLocalAudio(true);
                updateMuteStatusView(mSelfUserId, true);
                ToastUtils.showLong(getString(R.string.trtcvoiceroom_toast_you_have_turned_off_the_microphone));
            }
        }
    }

    /**
     * 判断当前是否被禁言
     *
     * @param seatIndex
     * @return
     */
    private boolean isSeatMute(int seatIndex) {
        VoiceRoomSeatEntity seatEntity = findSeatEntityFromUserId(seatIndex);
        if (seatEntity != null) {
            return  seatEntity.isSeatMute;
        }
        return false;
    }

//    /**
//     * 点击监听初始化
//     */
//    protected void initListener() {
//        mBtnMic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                updateMicButton();
//            }
//        });
//        mBtnEffect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (checkButtonPermission()) {
//                    if (mAnchorAudioPanel != null) {
//                        mAnchorAudioPanel.show();
//                    }
//                }
//            }
//        });
//        mBtnMsg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showInputMsgDialog();
//            }
//        });
//        mBtnMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MoreActionDialog dialog = new MoreActionDialog(mContext);
//                dialog.show();
//
//            }
//        });
//
//        mRvAudience.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                mRvAudienceScrollPosition = dx;
//            }
//        });
//        mIvAudienceMove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mRvAudienceScrollPosition < 0) {
//                    mRvAudienceScrollPosition = 0;
//                }
//                int position = mRvAudienceScrollPosition + dp2px(mContext, 32);
//                mRvAudience.smoothScrollBy(position, 0);
//            }
//        });
//    }

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * 判断是否为主播，有操作按钮的权限
     *
     * @return 是否有权限
     */
    protected boolean checkButtonPermission() {
        boolean hasPermission = (mCurrentRole == TRTCCloudDef.TRTCRoleAnchor);
        if (!hasPermission) {
            ToastUtils.showLong(getString(R.string.trtcvoiceroom_toast_anchor_can_only_operate_it));
        }
        return hasPermission;
    }

    /**
     * 页面传输数据初始化接收
     */
    protected void initData() {
        Intent intent = getIntent();
        mRoomId = intent.getIntExtra(VOICEROOM_ROOM_ID, 0);
        mRoomName = intent.getStringExtra(VOICEROOM_ROOM_NAME);
        mUserName = intent.getStringExtra(VOICEROOM_USER_NAME);
        mSelfUserId = intent.getStringExtra(VOICEROOM_USER_ID);
        mNeedRequest = intent.getBooleanExtra(VOICEROOM_NEED_REQUEST, false);
        mUserAvatar = intent.getStringExtra(VOICEROOM_USER_AVATAR);
        mRoomCover = intent.getStringExtra(VOICEROOM_ROOM_COVER);
        mAudioQuality = intent.getIntExtra(VOICEROOM_AUDIO_QUALITY, TRTCCloudDef.TRTC_AUDIO_QUALITY_MUSIC);
        //        mSeatCount = intent.getIntExtra(VOICEROOM_SEAT_COUNT);
        mTRTCVoiceRoom = TRTCVoiceRoom.sharedInstance(this);
        mTRTCVoiceRoom.setDelegate(this);
        mAnchorAudioPanel = new AudioEffectPanel(this);
        mAnchorAudioPanel.setAudioEffectManager(mTRTCVoiceRoom.getAudioEffectManager());
//        if (!TextUtils.isEmpty(mRoomCover)) {
//            Picasso.get().load(mRoomCover).into(mRootBg);
//        } else {
//            mRootBg.setBackgroundResource(R.drawable.trtcvoiceroom_scene_bg);
//        }
    }

    /**
     * 控件初始化
     */
    protected void initView() {
//        mRootBg =  (ImageView) findViewById(R.id.root_bg);
//        mTvRoomName = (TextView) findViewById(R.id.tv_room_name);
//        mTvRoomId = (TextView) findViewById(R.id.tv_room_id);
        mImgHead = (CircleImageView) findViewById(R.id.img_head);
        mIvManagerMute = (ImageView) findViewById(R.id.iv_manager_mute);
        mIvManagerTalk = (ImageView) findViewById(R.id.iv_manager_talk);
//        mIvAnchorHead = (CircleImageView) findViewById(R.id.iv_anchor_head);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mRvSeat = (RecyclerView) findViewById(R.id.rv_seat);
//        mRvAudience = (RecyclerView) findViewById(R.id.rv_audience);
//        mRvImMsg = (RecyclerView) findViewById(R.id.rv_im_msg);
//        mToolBarView = findViewById(R.id.tool_bar_view);
//        mBtnExitRoom = (AppCompatImageButton) findViewById(R.id.exit_room);
//        mBtnMsg = (AppCompatImageButton) findViewById(R.id.btn_msg);
//        mBtnMic = (AppCompatImageButton) findViewById(R.id.btn_mic);
//        mBtnEffect = (AppCompatImageButton) findViewById(R.id.btn_effect);
//        mBtnLeaveSeat = (AppCompatImageButton) findViewById(R.id.btn_leave_seat);
//        mBtnMore = (AppCompatImageButton) findViewById(R.id.btn_more);
//        mIvAudienceMove = (ImageView) findViewById(R.id.iv_audience_move);
        mViewSelectMember = new SelectMemberView(this);
        mConfirmDialogFragment = new ConfirmDialogFragment();
        mInputTextMsgDialog = new InputTextMsgDialog(this, R.style.TRTCVoiceRoomInputDialog);
        mInputTextMsgDialog.setmOnTextSendListener(this);
//        mMsgEntityList = new ArrayList<>();
        mMemberEntityList = new ArrayList<>();
        mMemberEntityMap = new HashMap<>();
//        mBtnExitRoom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//        mMsgListAdapter = new MsgListAdapter(this, mMsgEntityList, this);
//        mRvImMsg.setLayoutManager(new LinearLayoutManager(this));
//        mRvImMsg.setAdapter(mMsgListAdapter);
        mSeatUserMuteMap = new HashMap<>();

        initDefaultSeat();

//        mAudienceEntityList = new LinkedList<>();
//        mAudienceListAdapter = new AudienceListAdapter(this, mAudienceEntityList);
//        LinearLayoutManager lm = new LinearLayoutManager(this);
//        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
//        mRvAudience.setLayoutManager(lm);
//        mRvAudience.setAdapter(mAudienceListAdapter);
    }

    /**
     * 默认用户坐席数据初始化
     */
    protected void initDefaultSeat(){
        mVoiceRoomSeatEntityList = new ArrayList<>();
        mVoiceRoomSeatEntityList.clear();
        for (int i = 1; i < MAX_SEAT_SIZE; i++) {
            VoiceRoomSeatEntity seatEntity = new VoiceRoomSeatEntity();
            seatEntity.index = i;
            mVoiceRoomSeatEntityList.add(seatEntity);
        }
        mVoiceRoomSeatAdapter = new VoiceRoomSeatAdapter(this, mVoiceRoomSeatEntityList, this);
        mRvSeat.setLayoutManager(new GridLayoutManager(this, 4));
        mRvSeat.setAdapter(mVoiceRoomSeatAdapter);
    }

    /**
     *     /////////////////////////////////////////////////////////////////////////////////
     *     //
     *     //                      发送文本信息
     *     //
     *     /////////////////////////////////////////////////////////////////////////////////
     */
    /**
     * 发消息弹出框
     */
    private void showInputMsgDialog() {
        WindowManager              windowManager = getWindowManager();
        Display                    display       = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp            = mInputTextMsgDialog.getWindow().getAttributes();
        lp.width = display.getWidth(); //设置宽度
        mInputTextMsgDialog.getWindow().setAttributes(lp);
        mInputTextMsgDialog.setCancelable(true);
        mInputTextMsgDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mInputTextMsgDialog.show();
    }

    /**
     * 发送消息回调
     *
     * @param msg
     */
    @Override
    public void onTextSend(String msg) {
        if (msg.length() == 0) {
            return;
        }
        byte[] byte_num = msg.getBytes(StandardCharsets.UTF_8);
        if (byte_num.length > 160) {
            Toast.makeText(this, getString(R.string.trtcvoiceroom_toast_please_enter_content), Toast.LENGTH_SHORT).show();
            return;
        }

        //消息回显
        MsgEntity entity = new MsgEntity();
        entity.userName = getString(R.string.trtcvoiceroom_me);
        entity.content = msg;
        entity.isChat = true;
        entity.userId = mSelfUserId;
        entity.type = MsgEntity.TYPE_NORMAL;
//        showImMsg(entity);

        mTRTCVoiceRoom.sendRoomTextMsg(msg, new TRTCVoiceRoomCallback.ActionCallback() {
            @Override
            public void onCallback(int code, String msg) {
                if (code == 0) {
                    ToastUtils.showShort(getString(R.string.trtcvoiceroom_toast_sent_successfully));
                } else {
                    ToastUtils.showShort(getString(R.string.trtcvoiceroom_toast_sent_message_failure), code);
                }
            }
        });
    }

    public void resetSeatView() {
        mSeatUserSet.clear();
        for (VoiceRoomSeatEntity entity : mVoiceRoomSeatEntityList) {
            entity.isUsed = false;
        }
        mVoiceRoomSeatAdapter.notifyDataSetChanged();
    }

    /**
     * 座位上点击按钮的反馈
     *
     * @param position
     */
    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onError(int code, String message) {

    }

    @Override
    public void onWarning(int code, String message) {

    }

    @Override
    public void onDebugLog(String message) {

    }

    @Override
    public void onRoomDestroy(String roomId) {

    }

    /**
     * 加载房间信息回调
     *
     * @param roomInfo
     */
    @Override
    public void onRoomInfoChange(TRTCVoiceRoomDef.RoomInfo roomInfo) {
        mNeedRequest = roomInfo.needRequest;
        mRoomName = roomInfo.roomName;
//        mTvRoomName.setText(roomInfo.roomName);
//        mTvRoomId.setText(getString(R.string.trtcvoiceroom_room_id, roomInfo.roomId));
//        String roomCover = roomInfo.coverUrl;
//        if (!TextUtils.isEmpty(roomCover)) {
//            Picasso.get().load(roomCover).into(mRootBg);
//        }
    }

    /**
     * 语音聊天室开启后，加载房间坐席列表回调
     *
     * @param seatInfoList 全量的麦位列表
     */
    @Override
    public void onSeatListChange(final List<TRTCVoiceRoomDef.SeatInfo> seatInfoList) {
        //先刷一遍界面
        final List<String> userids = new ArrayList<>();
        for (int i = 0; i < seatInfoList.size(); i++) {
            TRTCVoiceRoomDef.SeatInfo newSeatInfo = seatInfoList.get(i);
            // 底层返回的第一个座位是主播哦！特殊处理一下
            if (i == 0) {
                if (mMainSeatUserId == null || !mMainSeatUserId.equals(newSeatInfo.userId)) {
                    //主播上线啦
                    mMainSeatUserId = newSeatInfo.userId;
                    userids.add(newSeatInfo.userId);
                    mTvName.setText(getString(R.string.trtcvoiceroom_tv_information_acquisition));
                }
                continue;
            }
            // 接下来是座位区域的列表
            VoiceRoomSeatEntity oldSeatEntity = mVoiceRoomSeatEntityList.get(i - 1);
            if (newSeatInfo.userId != null && !newSeatInfo.userId.equals(oldSeatEntity.userId)) {
                //userId相同，可以不用重新获取信息了
                //但是如果有新的userId进来，那么应该去拿一下主播的详细信息
                userids.add(newSeatInfo.userId);
            }
            oldSeatEntity.userId = newSeatInfo.userId;
            // 座位的状态更新一下
            switch (newSeatInfo.status) {
                case STATUS_UNUSED:
                    oldSeatEntity.isUsed = false;
                    oldSeatEntity.isClose = false;
                    break;
                case STATUS_CLOSE:
                    oldSeatEntity.isUsed = false;
                    oldSeatEntity.isClose = true;
                    break;
                case STATUS_USED:
                    oldSeatEntity.isUsed = true;
                    oldSeatEntity.isClose = false;
                    break;
                default:
                    break;
            }
            oldSeatEntity.isSeatMute = newSeatInfo.mute;
        }
        for (String userId : userids) {
            if (!mSeatUserMuteMap.containsKey(userId)) {
                mSeatUserMuteMap.put(userId, true);
            }
        }
        mVoiceRoomSeatAdapter.notifyDataSetChanged();
        //所有的userId拿到手，开始去搜索详细信息了
        mTRTCVoiceRoom.getUserInfoList(userids, new TRTCVoiceRoomCallback.UserListCallback() {
            @Override
            public void onCallback(int code, String msg, List<TRTCVoiceRoomDef.UserInfo> list) {
                // 解析所有人的userinfo
                Map<String, TRTCVoiceRoomDef.UserInfo> map = new HashMap<>();
                for (TRTCVoiceRoomDef.UserInfo userInfo : list) {
                    map.put(userInfo.userId, userInfo);
                }
                for (int i = 0; i < seatInfoList.size(); i++) {
                    TRTCVoiceRoomDef.SeatInfo newSeatInfo = seatInfoList.get(i);
                    TRTCVoiceRoomDef.UserInfo userInfo    = map.get(newSeatInfo.userId);
                    if (userInfo == null) {
                        continue;
                    }
                    boolean isUserMute = mSeatUserMuteMap.get(userInfo.userId);
                    // 底层返回的第一个座位是房主哦！特殊处理一下
                    if (i == 0) {
                        if (newSeatInfo.status == STATUS_USED) {
                            //主播上线啦
                            if (!TextUtils.isEmpty(userInfo.userAvatar)) {
                                Picasso.get().load(userInfo.userAvatar).into(mImgHead);
//                                Picasso.get().load(userInfo.userAvatar).into(mIvAnchorHead);
                            } else {
                                mImgHead.setImageResource(R.drawable.default_head);
//                                mIvAnchorHead.setImageResource(R.drawable.default_head);
                            }
                            if (TextUtils.isEmpty(userInfo.userName)) {
                                mTvName.setText(userInfo.userId);
                            } else {
                                mTvName.setText(userInfo.userName);
                            }
                            updateMuteStatusView(userInfo.userId, isUserMute);
                        } else {
                            mTvName.setText(getString(R.string.trtcvoiceroom_tv_the_anchor_is_not_online));
                        }
                    } else {
                        // 接下来是座位区域的列表
                        VoiceRoomSeatEntity seatEntity = mVoiceRoomSeatEntityList.get(i - 1);
                        if (userInfo.userId.equals(seatEntity.userId)) {
                            seatEntity.userName = userInfo.userName;
                            seatEntity.userAvatar = userInfo.userAvatar;
                            seatEntity.isUserMute = isUserMute;
                        }
                    }
                }
                mVoiceRoomSeatAdapter.notifyDataSetChanged();
                if (!isInitSeat) {
                    getAudienceList();
                    isInitSeat = true;
                }
            }
        });
    }

    /**
     * 主播/用户上麦提醒
     *
     * @param index 上麦的麦位
     * @param user  用户详细信息
     */
    @Override
    public void onAnchorEnterSeat(int index, TRTCVoiceRoomDef.UserInfo user) {
        if (index != 0) {
            LogUtils.e(TAG, "onAnchorEnterSeat userInfo:"+user);
            MsgEntity msgEntity = new MsgEntity();
            msgEntity.type = MsgEntity.TYPE_NORMAL;
            msgEntity.userName = user.userName;
            msgEntity.content =  getString(R.string.trtcvoiceroom_tv_online_no_name, index);
//            showImMsg(msgEntity);
//            mAudienceListAdapter.removeMember(user.userId);
            if (user.userId.equals(mSelfUserId)) {
                mSelfSeatIndex = index;
            }

            TUIKitUtil.sendGroupNoticeMessage(mChatInfo.getId(), user.userName, getString(R.string.trtcvoiceroom_tv_online_no_name, index));
        }
    }

    /**
     * 主播/用户下麦提醒
     *
     * @param index 下麦的麦位
     * @param user  用户详细信息
     */
    @Override
    public void onAnchorLeaveSeat(int index, TRTCVoiceRoomDef.UserInfo user) {
        if (index != 0) {
            LogUtils.e(TAG, "onAnchorLeaveSeat userInfo:"+user);
            MsgEntity msgEntity = new MsgEntity();
            msgEntity.type = MsgEntity.TYPE_NORMAL;
            msgEntity.userName = user.userName;
            msgEntity.content =  getString(R.string.trtcvoiceroom_tv_offline_no_name, index);
//            showImMsg(msgEntity);
            AudienceEntity entity = new AudienceEntity();
            entity.userId = user.userId;
            entity.userAvatar = user.userAvatar;
//            mAudienceListAdapter.addMember(entity);
            if (user.userId.equals(mSelfUserId)) {
                mSelfSeatIndex = -1;
            }

            TUIKitUtil.sendGroupNoticeMessage(mChatInfo.getId(), user.userName, getString(R.string.trtcvoiceroom_tv_offline_no_name, index));
        }
    }

    /**
     * 用户禁言处理
     *
     * @param index  操作的麦位
     * @param isMute 是否静音
     */
    @Override
    public void onSeatMute(int index, boolean isMute) {
        MsgEntity msgEntity = new MsgEntity();
        msgEntity.type = MsgEntity.TYPE_NORMAL;
        if (isMute) {
            msgEntity.content =  getString(R.string.trtcvoiceroom_tv_the_position_has_muted, index);
            TUIKitUtil.sendGroupNoticeMessage(mChatInfo.getId(), index + "号位", " 被禁言");
        } else {
            msgEntity.content =  getString(R.string.trtcvoiceroom_tv_the_position_has_unmuted, index);
            TUIKitUtil.sendGroupNoticeMessage(mChatInfo.getId(), index + "号位", " 解除禁言");
        }
//        showImMsg(msgEntity);

        VoiceRoomSeatEntity seatEntity = findSeatEntityFromUserId(index);
        if (seatEntity == null) {
            return;
        }
        if (index == mSelfSeatIndex) {
            if (isMute) {
                mTRTCVoiceRoom.muteLocalAudio(true);
                updateMuteStatusView(mSelfUserId, true);
            } else if (!seatEntity.isUserMute) {
                mTRTCVoiceRoom.muteLocalAudio(false);
                updateMuteStatusView(mSelfUserId, false);
            }
        }
    }

    /**
     * 房主锁定坐席
     *
     * @param index  操作的麦位
     * @param isClose 是否封禁麦位
     */
    @Override
    public void onSeatClose(int index, boolean isClose) {
        MsgEntity msgEntity = new MsgEntity();
        msgEntity.type = MsgEntity.TYPE_NORMAL;
        msgEntity.content = isClose ? getString(R.string.trtcvoiceroom_tv_the_owner_ban_this_position, index) :
        getString(R.string.trtcvoiceroom_tv_the_owner_not_ban_this_position, index);
//        showImMsg(msgEntity);
    }

    /**
     * 设置用户是否开启麦克风
     *
     * @param userId 用户id
     * @param mute   是否静音
     */
    @Override
    public void onUserMicrophoneMute(String userId, boolean mute) {
        LogUtils.e(TAG, "onUserMicrophoneMute userId:"+userId + " mute:"+mute);
        mSeatUserMuteMap.put(userId, mute);
        updateMuteStatusView(userId, mute);
    }

    /**
     * 监听当前用户麦克风开关状态 并显示/隐藏
     *
     * @param userId
     * @param mute
     */
    private void updateMuteStatusView(String userId, boolean mute) {
        if (userId == null) {
            return;
        }
        if (userId.equals(mMainSeatUserId)) {
            mIvManagerMute.setVisibility(mute ? View.VISIBLE : View.GONE);
            if (mute) {
                mIvManagerTalk.setVisibility(View.GONE);
            }
            mIsMainSeatMute = mute;
        } else {
            VoiceRoomSeatEntity seatEntity = findSeatEntityFromUserId(userId);
            if (seatEntity != null) {
                if (!seatEntity.isSeatMute && mute != seatEntity.isUserMute) {
                    seatEntity.isUserMute = mute;
                    mVoiceRoomSeatAdapter.notifyDataSetChanged();
                }
            }
        }
        if (userId.equals(mSelfUserId)) {
//            mBtnMic.setSelected(!mute);
            ivMike.setSelected(!mute);
        }
    }

    private VoiceRoomSeatEntity findSeatEntityFromUserId(String userId)  {
        if (mVoiceRoomSeatEntityList != null) {
            for (VoiceRoomSeatEntity seatEntity : mVoiceRoomSeatEntityList) {
                if (userId.equals(seatEntity.userId)) {
                    return seatEntity;
                }
            }
        }
        return null;
    }

    private VoiceRoomSeatEntity findSeatEntityFromUserId(int index)  {
        if (index == -1) {
            return null;
        }
        if (mVoiceRoomSeatEntityList != null) {
            for (VoiceRoomSeatEntity seatEntity : mVoiceRoomSeatEntityList) {
                if (index == seatEntity.index) {
                    return seatEntity;
                }
            }
        }
        return null;
    }

    /**
     * 观众进入房间
     *
     * @param userInfo 观众的详细信息
     */
    @Override
    public void onAudienceEnter(TRTCVoiceRoomDef.UserInfo userInfo) {
        LogUtils.e(TAG, "onAudienceEnter userInfo:"+userInfo);
        MsgEntity msgEntity = new MsgEntity();
        msgEntity.type = MsgEntity.TYPE_NORMAL;
        msgEntity.content = getString(R.string.trtcvoiceroom_tv_enter_room, "");
        msgEntity.userName = userInfo.userName;
//        showImMsg(msgEntity);
        if (userInfo.userId.equals(mSelfUserId)) {
            return;
        }
        AudienceEntity entity = new AudienceEntity();
        entity.userId = userInfo.userId;
        entity.userAvatar = userInfo.userAvatar;
//        mAudienceListAdapter.addMember(entity);
    }

    /**
     * 观众退出房间
     *
     * @param userInfo 观众的详细信息
     */
    @Override
    public void onAudienceExit(TRTCVoiceRoomDef.UserInfo userInfo) {
        LogUtils.e(TAG, "onAudienceExit userInfo:"+userInfo);
        MsgEntity msgEntity = new MsgEntity();
        msgEntity.type = MsgEntity.TYPE_NORMAL;
        msgEntity.userName = userInfo.userName;
        msgEntity.content = getString(R.string.trtcvoiceroom_tv_exit_room, "");
//        showImMsg(msgEntity);
//        mAudienceListAdapter.removeMember(userInfo.userId);
    }

    /**
     * 监听用户音量
     *
     * @param userVolumes 用户列表
     * @param totalVolume 音量大小 0-100
     */
    @Override
    public void onUserVolumeUpdate(List<TRTCCloudDef.TRTCVolumeInfo> userVolumes, int totalVolume) {
        for (TRTCCloudDef.TRTCVolumeInfo info : userVolumes) {
            if (info != null) {
                int volume = info.volume;
                if (info.userId.equals(mMainSeatUserId)) {
                    mIvManagerTalk.setVisibility(mIsMainSeatMute ? View.GONE : volume > 20 ? View.VISIBLE: View.GONE);
                } else {
                    VoiceRoomSeatEntity entity = findSeatEntityFromUserId(info.userId);
                    if (entity != null) {
                        entity.isTalk = volume > 20 ? true : false;
                        mVoiceRoomSeatAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    /**
     * 发送文本
     *
     * @param message 文本消息。
     * @param userInfo 发送者用户信息。
     */
    @Override
    public void onRecvRoomTextMsg(String message, TRTCVoiceRoomDef.UserInfo userInfo) {
        MsgEntity msgEntity = new MsgEntity();
        msgEntity.userId = userInfo.userId;
        msgEntity.userName = userInfo.userName;
        msgEntity.content = message;
        msgEntity.type = MsgEntity.TYPE_NORMAL;
        msgEntity.isChat = true;
//        showImMsg(msgEntity);
    }

    @Override
    public void onRecvRoomCustomMsg(String cmd, String message, TRTCVoiceRoomDef.UserInfo userInfo) {

    }

    @Override
    public void onReceiveNewInvitation(String id, String inviter, String cmd, String content) {

    }

    @Override
    public void onInviteeAccepted(String id, String invitee) {

    }

    @Override
    public void onInviteeRejected(String id, String invitee) {

    }

    @Override
    public void onInvitationCancelled(String id, String invitee) {

    }

    @Override
    public void onAgreeClick(int position) {

    }

    /**
     * 获取当前房间内人员列表
     */
    protected void getAudienceList() {
        mTRTCVoiceRoom.getUserInfoList(null, new TRTCVoiceRoomCallback.UserListCallback() {
            @Override
            public void onCallback(int code, String msg, List<TRTCVoiceRoomDef.UserInfo> list) {
                if (code == 0) {
                    LogUtils.e(TAG, "getAudienceList list size:"+list.size());
                    for (TRTCVoiceRoomDef.UserInfo userInfo : list) {
                        LogUtils.e(TAG, "getAudienceList userInfo:"+userInfo);
                        if (!mSeatUserMuteMap.containsKey(userInfo.userId)) {
                            AudienceEntity audienceEntity = new AudienceEntity();
                            audienceEntity.userAvatar = userInfo.userAvatar;
                            audienceEntity.userId = userInfo.userId;
//                            mAudienceListAdapter.addMember(audienceEntity);
                        }
                        if (userInfo.userId.equals(mSelfUserId)) {
                            continue;
                        }
                        MemberEntity memberEntity = new MemberEntity();
                        memberEntity.userId = userInfo.userId;
                        memberEntity.userAvatar = userInfo.userAvatar;
                        memberEntity.userName = userInfo.userName;
                        memberEntity.type = MemberEntity.TYPE_IDEL;
                        if (!mMemberEntityMap.containsKey(memberEntity.userId)) {
                            mMemberEntityMap.put(memberEntity.userId, memberEntity);
                            mMemberEntityList.add(memberEntity);
                        }
                    }
                }
            }
        });
    }

    /**
     * 座位索引pos+1
     *
     * @param srcSeatIndex
     * @return
     */
    protected int changeSeatIndexToModelIndex(int srcSeatIndex) {
        return srcSeatIndex + 1;
    }

//    /**
//     * 发送消息统一处理
//     *
//     * @param entity
//     */
//    protected void showImMsg(final MsgEntity entity) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (mMsgEntityList.size() > 1000) {
//                    while (mMsgEntityList.size() > 900) {
//                        mMsgEntityList.remove(0);
//                    }
//                }
//                if (!TextUtils.isEmpty(entity.userName)) {
//                    if (mMessageColorIndex >= MESSAGE_USERNAME_COLOR_ARR.length) {
//                        mMessageColorIndex = 0;
//                    }
//                    int color = MESSAGE_USERNAME_COLOR_ARR[mMessageColorIndex];
//                    entity.color = getResources().getColor(color);
//                    mMessageColorIndex ++;
//                }
//                mMsgEntityList.add(entity);
//                mMsgListAdapter.notifyDataSetChanged();
//                mRvImMsg.smoothScrollToPosition(mMsgListAdapter.getItemCount());
//            }
//        });
//    }
}