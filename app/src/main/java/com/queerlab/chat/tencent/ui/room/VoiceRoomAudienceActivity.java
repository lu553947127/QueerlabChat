package com.queerlab.chat.tencent.ui.room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.base.SpConfig;
import com.queerlab.chat.tencent.model.TRTCVoiceRoomCallback;
import com.queerlab.chat.tencent.model.TRTCVoiceRoomDef;
import com.queerlab.chat.tencent.ui.base.VoiceRoomSeatEntity;
import com.queerlab.chat.tencent.ui.list.TCConstants;
import com.queerlab.chat.tencent.ui.widget.CommonBottomDialog;
import com.queerlab.chat.tencent.ui.widget.ConfirmDialogFragment;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.trtc.TRTCCloudDef;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 观众界面
 *
 * @author guanyifeng
 */
public class VoiceRoomAudienceActivity extends VoiceRoomBaseActivity {
    //被主播抱上麦序map
    private Map<String, Integer> mInvitationSeatMap;
    //被主播抱上麦邀请的观众id
    private String               mOwnerId;
    //主播麦序是否初始化成功
    private boolean              mIsSeatInitSuccess;
    private int                  mSelfSeatIndex;
    //判断观众是否加入语音聊天室
    private boolean                     mIsEnterRoom;

    /**
     * 加入房间
     *
     * @param context
     * @param roomId
     * @param userId
     * @param audioQuality
     */
    public static void enterRoom(Context context, int roomId, String userId, int audioQuality) {
        Intent starter = new Intent(context, VoiceRoomAudienceActivity.class);
        starter.putExtra(VOICEROOM_ROOM_ID, roomId);
        starter.putExtra(VOICEROOM_USER_ID, userId);
        starter.putExtra(VOICEROOM_AUDIO_QUALITY, audioQuality);
        context.startActivity(starter);
    }

    /**
     * 加入房间
     *
     * @param context
     * @param roomId
     * @param userId
     * @param audioQuality
     * @param chatInfo
     */
    public static void enterRoom(Context context, int roomId, String userId, int audioQuality, ChatInfo chatInfo) {
        Intent starter = new Intent(context, VoiceRoomAudienceActivity.class);
        starter.putExtra(VOICEROOM_ROOM_ID, roomId);
        starter.putExtra(VOICEROOM_USER_ID, userId);
        starter.putExtra(VOICEROOM_AUDIO_QUALITY, audioQuality);
        starter.putExtra("chatInfo", chatInfo);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //用户加入房间成功返回结果
        groupViewModel.voiceRoomJoinLiveData.observe(activity, s -> {
            llOperation.setVisibility(View.VISIBLE);
            clVoice.setVisibility(View.VISIBLE);
            ivVoice.setVisibility(View.GONE);
            initAudience();
        });

        //用户退出房间成功返回结果
        groupViewModel.voiceRoomLogoutLiveData.observe(activity, s -> {
            llOperation.setVisibility(View.GONE);
            clVoice.setVisibility(View.GONE);
            ivVoice.setVisibility(View.VISIBLE);
            mIsEnterRoom = false;
            exitRoom();
        });
    }

    /**
     * 观众初始化
     */
    private void initAudience() {
        mInvitationSeatMap = new HashMap<>();
        mVoiceRoomSeatAdapter.setEmptyText(getString(R.string.trtcvoiceroom_msg_click_to_chat));
        mVoiceRoomSeatAdapter.notifyDataSetChanged();
        // 开始进房哦
        enterRoom();
//        mBtnMsg.setActivated(true);
//        mBtnMsg.setSelected(true);
        refreshView();
//        mBtnLeaveSeat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                leaveSeat();
//            }
//        });
    }

    /**
     * 房间控件显示初始化
     */
    private void refreshView() {
        if (mCurrentRole == TRTCCloudDef.TRTCRoleAnchor) {
//            mBtnMic.setVisibility(View.VISIBLE);
            ivMike.setVisibility(View.VISIBLE);
//            mBtnLeaveSeat.setVisibility(View.VISIBLE);
//            mBtnMic.setActivated(true);
//            mBtnMic.setSelected(true);
            ivMike.setActivated(true);
            ivMike.setSelected(true);
//            mBtnEffect.setVisibility(View.VISIBLE);
            mAnchorAudioPanel.hideManagerView();
        } else {
//            mBtnLeaveSeat.setVisibility(View.GONE);
//            mBtnMic.setVisibility(View.GONE);
            ivMike.setVisibility(View.GONE);
//            mBtnEffect.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (mIsEnterRoom) {
            showExitRoom();
        }else {
            finish();
        }
    }

    /**
     * 退出房间弹窗
     */
    private void showExitRoom() {
        if (mConfirmDialogFragment.isAdded()) {
            mConfirmDialogFragment.dismiss();
        }
        mConfirmDialogFragment.setMessage(getString(R.string.trtcvoiceroom_audience_leave_room));
        mConfirmDialogFragment.setNegativeClickListener(new ConfirmDialogFragment.NegativeClickListener() {
            @Override
            public void onClick() {
                mConfirmDialogFragment.dismiss();
            }
        });
        mConfirmDialogFragment.setPositiveClickListener(new ConfirmDialogFragment.PositiveClickListener() {
            @Override
            public void onClick() {
                mConfirmDialogFragment.dismiss();
                groupViewModel.voiceRoomLogout(mChatInfo.getId(), SPUtils.getInstance().getString(SpConfig.USER_ID));
//                finish();
            }
        });
        mConfirmDialogFragment.show(getFragmentManager(), "confirm_fragment");
    }

    /**
     * 退出房间
     */
    private void exitRoom() {
        mTRTCVoiceRoom.exitRoom(new TRTCVoiceRoomCallback.ActionCallback() {
            @Override
            public void onCallback(int code, String msg) {
                ToastUtils.showShort(R.string.trtcvoiceroom_toast_exit_the_room_successfully);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 开始进入房间
     */
    private void enterRoom() {
        mIsSeatInitSuccess = false;
        mSelfSeatIndex = -1;
        mCurrentRole = TRTCCloudDef.TRTCRoleAudience;
        mTRTCVoiceRoom.setSelfProfile(mUserName, mUserAvatar, null);
        mTRTCVoiceRoom.enterRoom(mRoomId, new TRTCVoiceRoomCallback.ActionCallback() {
            @Override
            public void onCallback(int code, String msg) {
                LogUtils.e( "enterRoom code: " + code + " msg: " + msg);
                if (code == 0) {
                    //进房成功
                    mIsEnterRoom = true;
                    ToastUtils.showShort(R.string.trtcvoiceroom_toast_enter_the_room_successfully);
                    mTRTCVoiceRoom.setAudioQuality(mAudioQuality);
                } else {
                    ToastUtils.showShort(getString(R.string.trtcvoiceroom_toast_enter_the_room_failure, code, msg));
                    finish();
                }
            }
        });
    }

    @Override
    public void onSeatListChange(List<TRTCVoiceRoomDef.SeatInfo> seatInfoList) {
        super.onSeatListChange(seatInfoList);
        mIsSeatInitSuccess = true;
    }

    /**
     * 点击麦位列表观众端的操作
     *
     * @param itemPos
     */
    @Override
    public void onItemClick(final int itemPos) {
        if (!mIsSeatInitSuccess) {
            ToastUtils.showLong(R.string.trtcvoiceroom_toast_list_has_not_been_initialized);
            return;
        }

        //判断当前是否在麦上
        if (mCurrentRole == TRTCCloudDef.TRTCRoleAnchor) {
//            new XPopup.Builder(activity).asConfirm(getString(R.string.reminder), "是否下麦？", () -> {
//                leaveSeat();
//            }).show();

            final CommonBottomDialog dialog = new CommonBottomDialog(this);
            dialog.setButton(new CommonBottomDialog.OnButtonClickListener() {
                @Override
                public void onClick(int position, String text) {
                    if (position == 0) {
                        leaveSeat();
                    }
                    dialog.dismiss();
                }
            }, "点击下麦");
            dialog.show();
            return;
        }

        // 判断座位有没有人
        VoiceRoomSeatEntity entity = mVoiceRoomSeatEntityList.get(itemPos);
        if (entity.isClose) {
            ToastUtils.showShort(R.string.trtcvoiceroom_toast_position_is_locked_cannot_apply_for_chat);
        } else if (!entity.isUsed) {
            // 没人弹出申请上麦
            final CommonBottomDialog dialog = new CommonBottomDialog(this);
            dialog.setButton(new CommonBottomDialog.OnButtonClickListener() {
                @Override
                public void onClick(int position, String text) {
                    if (position == 0) {
                        // 发送请求之前再次判断一下这个座位有没有人
                        VoiceRoomSeatEntity seatEntity = mVoiceRoomSeatEntityList.get(itemPos);
                        if (seatEntity.isUsed) {
                            ToastUtils.showShort(R.string.trtcvoiceroom_toast_position_is_already_occupied);
                            return;
                        }
                        if (seatEntity.isClose) {
                            ToastUtils.showShort(getString(R.string.trtcvoiceroom_seat_closed));
                            return;
                        }
                        startTakeSeat(itemPos);
                    }
                    dialog.dismiss();
                }
            }, "点击上麦");
            dialog.show();
        }
    }

    /**
     * 用户下麦
     */
    private void leaveSeat() {
        mTRTCVoiceRoom.leaveSeat(new TRTCVoiceRoomCallback.ActionCallback() {
            @Override
            public void onCallback(int code, String msg) {
                if (code == 0) {
                    ToastUtils.showShort(R.string.trtcvoiceroom_toast_offline_successfully);
                } else {
                    ToastUtils.showShort(getString(R.string.trtcvoiceroom_toast_offline_failure, msg));
                }
            }
        });
    }

    /**
     * 申请上麦
     *
     * @param itemPos
     */
    private void startTakeSeat(int itemPos) {
        if (mCurrentRole == TRTCCloudDef.TRTCRoleAnchor) {
            ToastUtils.showShort(R.string.trtcvoiceroom_toast_you_are_already_an_anchor);
            return;
        }
        if (mNeedRequest) {
            //需要申请上麦
            if (mOwnerId == null) {
                ToastUtils.showShort(R.string.trtcvoiceroom_toast_the_room_is_not_ready);
                return;
            }
            String inviteId = mTRTCVoiceRoom.sendInvitation(TCConstants.CMD_REQUEST_TAKE_SEAT, mOwnerId, String.valueOf(changeSeatIndexToModelIndex(itemPos)), new TRTCVoiceRoomCallback.ActionCallback() {
                @Override
                public void onCallback(int code, String msg) {
                    if (code == 0) {
                        ToastUtils.showShort(R.string.trtcvoiceroom_toast_application_has_been_sent_please_wait_for_processing);
                    } else {
                        ToastUtils.showShort(getString(R.string.trtcvoiceroom_toast_failed_to_send_application, msg));
                    }
                }
            });
            mInvitationSeatMap.put(inviteId, itemPos);
     } else {
            //不需要的情况下自动上麦
            mTRTCVoiceRoom.enterSeat(changeSeatIndexToModelIndex(itemPos), new TRTCVoiceRoomCallback.ActionCallback() {
                @Override
                public void onCallback(int code, String msg) {
                    LogUtils.e( "enterSeat failed, code: " + code + "|desc: " + msg);
                    if (code == 0) {

                    }
                }
            });
        }
    }

    /**
     * 收到主播抱麦邀请
     *
     * @param id
     * @param cmd
     * @param content
     */
    private void recvPickSeat(final String id, String cmd, final String content) {
        //这里收到了主播抱麦的邀请
        if (mConfirmDialogFragment != null && mConfirmDialogFragment.isAdded()) {
            mConfirmDialogFragment.dismiss();
        }
        mConfirmDialogFragment = new ConfirmDialogFragment();
        int seatIndex = Integer.parseInt(content);
        mConfirmDialogFragment.setMessage(getString(R.string.trtcvoiceroom_msg_invite_you_to_chat, seatIndex));
        mConfirmDialogFragment.setNegativeClickListener(new ConfirmDialogFragment.NegativeClickListener() {
            @Override
            public void onClick() {
                mTRTCVoiceRoom.rejectInvitation(id, new TRTCVoiceRoomCallback.ActionCallback() {
                    @Override
                    public void onCallback(int code, String msg) {
                        LogUtils.e(TAG, "rejectInvitation callback:" + code);
                        ToastUtils.showShort(R.string.trtcvoiceroom_msg_you_refuse_to_chat);
                    }
                });
                mConfirmDialogFragment.dismiss();
            }
        });
        mConfirmDialogFragment.setPositiveClickListener(new ConfirmDialogFragment.PositiveClickListener() {
            @Override
            public void onClick() {
                //同意上麦，回复接受
                mTRTCVoiceRoom.acceptInvitation(id, new TRTCVoiceRoomCallback.ActionCallback() {
                    @Override
                    public void onCallback(int code, String msg) {
                        if (code != 0) {
                            ToastUtils.showShort(getString(R.string.trtcvoiceroom_toast_accept_request_failure, code));
                        }
                        LogUtils.e(TAG, "acceptInvitation callback:" + code);
                    }
                });
                mConfirmDialogFragment.dismiss();
            }
        });
        mConfirmDialogFragment.show(getFragmentManager(), "confirm_fragment" + seatIndex);
    }

    /**
     * 获取房间信息
     *
     * @param roomInfo
     */
    @Override
    public void onRoomInfoChange(TRTCVoiceRoomDef.RoomInfo roomInfo) {
        super.onRoomInfoChange(roomInfo);
        mOwnerId = roomInfo.ownerId;
    }

    @Override
    public void onReceiveNewInvitation(final String id, String inviter, String cmd, final String content) {
        super.onReceiveNewInvitation(id, inviter, cmd, content);
        if (cmd.equals(TCConstants.CMD_PICK_UP_SEAT)) {
            recvPickSeat(id, cmd, content);
        }
    }

    @Override
    public void onInviteeAccepted(String id, String invitee) {
        super.onInviteeAccepted(id, invitee);
        Integer seatIndex = mInvitationSeatMap.remove(id);
        if (seatIndex != null) {
            VoiceRoomSeatEntity entity = mVoiceRoomSeatEntityList.get(seatIndex);
            if (!entity.isUsed) {
                mTRTCVoiceRoom.enterSeat(changeSeatIndexToModelIndex(seatIndex), new TRTCVoiceRoomCallback.ActionCallback() {
                    @Override
                    public void onCallback(int code, String msg) {
                        if (code == 0) {

                        }
                    }
                });
            }
        }
    }

    @Override
    public void onSeatMute(int index, boolean isMute) {
        super.onSeatMute(index, isMute);
    }

    /**
     * 观众上麦回调
     *
     * @param index 上麦的麦位
     * @param user  用户详细信息
     */
    @Override
    public void onAnchorEnterSeat(int index, TRTCVoiceRoomDef.UserInfo user) {
        super.onAnchorEnterSeat(index, user);
        if (user.userId.equals(mSelfUserId)) {
            mCurrentRole = TRTCCloudDef.TRTCRoleAnchor;
            mSelfSeatIndex = index;
            refreshView();
        }
    }

    /**
     * 观众下麦回调
     *
     * @param index 下麦的麦位
     * @param user  用户详细信息
     */
    @Override
    public void onAnchorLeaveSeat(int index, TRTCVoiceRoomDef.UserInfo user) {
        super.onAnchorLeaveSeat(index, user);
        if (user.userId.equals(mSelfUserId)) {
            mCurrentRole = TRTCCloudDef.TRTCRoleAudience;
            mSelfSeatIndex = -1;
            if (mAnchorAudioPanel != null) {
                mAnchorAudioPanel.reset();
            }
            refreshView();
        }
    }

    /**
     * 房主解散房间
     *
     * @param roomId
     */
    @Override
    public void onRoomDestroy(String roomId) {
        super.onRoomDestroy(roomId);
        ToastUtils.showLong(R.string.trtcvoiceroom_msg_close_room);
        mTRTCVoiceRoom.exitRoom(null);
//        finish();
        llOperation.setVisibility(View.GONE);
        clVoice.setVisibility(View.GONE);
        ivVoice.setVisibility(View.VISIBLE);
        mIsEnterRoom = false;
    }
}
