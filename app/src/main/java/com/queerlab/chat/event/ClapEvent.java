package com.queerlab.chat.event;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.event
 * @ClassName: ClapEvent
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 6/9/21 10:14 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 6/9/21 10:14 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ClapEvent {

    private String userId;
    private String status;

    public ClapEvent(String userId, String status) {
        this.userId = userId;
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
