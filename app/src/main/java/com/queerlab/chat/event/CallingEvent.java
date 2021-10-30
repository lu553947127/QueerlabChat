package com.queerlab.chat.event;

import com.tencent.liteav.login.UserModel;

import java.util.List;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.event
 * @ClassName: CallingEvent
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 6/17/21 1:34 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 6/17/21 1:34 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class CallingEvent {

    private List<UserModel> userIdList;

    public CallingEvent(List<UserModel> userIdList) {
        this.userIdList = userIdList;
    }

    public List<UserModel> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<UserModel> userIdList) {
        this.userIdList = userIdList;
    }
}
