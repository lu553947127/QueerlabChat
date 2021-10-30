package com.queerlab.chat.tencent.ui.base;

import com.queerlab.chat.tencent.model.TRTCVoiceRoomDef;

public class MemberEntity extends TRTCVoiceRoomDef.UserInfo {
    public static final int TYPE_IDEL       = 0;
    public static final int TYPE_IN_SEAT    = 1;
    public static final int TYPE_WAIT_AGREE = 2;

    public int type;
}
