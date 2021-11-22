package com.queerlab.chat.bean;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.bean
 * @ClassName: GroupDetailBean
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 2021/11/22 08:48
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/11/22 08:48
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class GroupDetailBean {
    /**
     * userNum : 1
     * liveNum : 0
     * group_status : 3
     * group_id : 166
     * group_name : 踢足球
     * activity_id : 40
     * group_type : 😿
     * activity_status : 1
     * roomId : null
     * group_no : @TGS#3UGSCKTH2
     */

    private int userNum;
    private int liveNum;
    private int group_status;
    private int group_id;
    private String group_name;
    private int activity_id;
    private String group_type;
    private int activity_status;
    private int roomId;
    private String group_no;

    public int getUserNum() {
        return userNum;
    }

    public void setUserNum(int userNum) {
        this.userNum = userNum;
    }

    public int getLiveNum() {
        return liveNum;
    }

    public void setLiveNum(int liveNum) {
        this.liveNum = liveNum;
    }

    public int getGroup_status() {
        return group_status;
    }

    public void setGroup_status(int group_status) {
        this.group_status = group_status;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public int getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(int activity_id) {
        this.activity_id = activity_id;
    }

    public String getGroup_type() {
        return group_type;
    }

    public void setGroup_type(String group_type) {
        this.group_type = group_type;
    }

    public int getActivity_status() {
        return activity_status;
    }

    public void setActivity_status(int activity_status) {
        this.activity_status = activity_status;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getGroup_no() {
        return group_no;
    }

    public void setGroup_no(String group_no) {
        this.group_no = group_no;
    }
}
