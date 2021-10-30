package com.queerlab.chat.event;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.event
 * @ClassName: GroupEvent
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 6/8/21 3:10 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 6/8/21 3:10 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class GroupEvent {

    private String status;

    public GroupEvent(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
