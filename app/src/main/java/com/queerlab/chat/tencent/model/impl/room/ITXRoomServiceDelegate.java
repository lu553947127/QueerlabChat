package com.queerlab.chat.tencent.model.impl.room;

import com.queerlab.chat.tencent.model.impl.base.TXRoomInfo;
import com.queerlab.chat.tencent.model.impl.base.TXSeatInfo;
import com.queerlab.chat.tencent.model.impl.base.TXUserInfo;

import java.util.List;

public interface ITXRoomServiceDelegate {
    void onRoomDestroy(String roomId);

    void onRoomRecvRoomTextMsg(String roomId, String message, TXUserInfo userInfo);

    void onRoomRecvRoomCustomMsg(String roomId, String cmd, String message, TXUserInfo userInfo);

    void onRoomInfoChange(TXRoomInfo TXRoomInfo);

    void onSeatInfoListChange(List<TXSeatInfo> TXSeatInfoList);

    void onRoomAudienceEnter(TXUserInfo userInfo);

    void onRoomAudienceLeave(TXUserInfo userInfo);

    void onSeatTake(int index, TXUserInfo userInfo);

    void onSeatClose(int index, boolean isClose);

    void onSeatLeave(int index, TXUserInfo userInfo);

    void onSeatMute(int index, boolean mute);

    void onReceiveNewInvitation(String id, String inviter, String cmd, String content);

    void onInviteeAccepted(String id, String invitee);

    void onInviteeRejected(String id, String invitee);

    void onInvitationCancelled(String id, String inviter);
}
