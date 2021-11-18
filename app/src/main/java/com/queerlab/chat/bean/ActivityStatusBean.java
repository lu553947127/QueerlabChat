package com.queerlab.chat.bean;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.bean
 * @ClassName: ActivityStatusBean
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 2021/11/17 08:48
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/11/17 08:48
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ActivityStatusBean {
    /**
     * activity_id : 34
     * activity_status : 1
     */

    private String activity_id;
    private int activity_status;

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public int getActivity_status() {
        return activity_status;
    }

    public void setActivity_status(int activity_status) {
        this.activity_status = activity_status;
    }
}
