package com.queerlab.chat.bean;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.bean
 * @ClassName: ActivityJoinStatusBean
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 2021/11/22 08:44
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/11/22 08:44
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ActivityJoinStatusBean {
    /**
     * selectActivityId : 0
     * selectGroupId : 0
     */

    private int selectActivityId;
    private int selectGroupId;

    public int getSelectActivityId() {
        return selectActivityId;
    }

    public void setSelectActivityId(int selectActivityId) {
        this.selectActivityId = selectActivityId;
    }

    public int getSelectGroupId() {
        return selectGroupId;
    }

    public void setSelectGroupId(int selectGroupId) {
        this.selectGroupId = selectGroupId;
    }
}
