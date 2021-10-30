package com.queerlab.chat.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.bean
 * @ClassName: GroupListBean
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 6/4/21 9:32 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 6/4/21 9:32 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class GroupListBean {
    /**
     * total : 4
     * list : [{"userNum":2,"liveNum":2,"group_status":1,"group_id":1,"group_name":"森森的群聊","group_type":"biaozhi","group_no":"qw1"},{"userNum":1,"liveNum":0,"group_status":2,"group_id":16,"group_name":"lll","group_type":"fff","group_no":"qw3"},{"userNum":1,"liveNum":0,"group_status":2,"group_id":17,"group_name":"lllw","group_type":"fff","group_no":"qw4"},{"userNum":1,"liveNum":0,"group_status":2,"group_id":2,"group_name":"森森的小群","group_type":"biaozhi","group_no":"qw2"}]
     * pageNum : 1
     * totalPage : 1
     */

    private int total;
    private int pageNum;
    private int totalPage;
    private List<ListBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable {
        /**
         * userNum : 2
         * liveNum : 2
         * group_status : 1
         * group_id : 1
         * group_name : 森森的群聊
         * group_type : biaozhi
         * group_no : qw1
         */

        private int userNum;
        private int liveNum;
        private int group_status;
        private int group_id;
        private String group_name;
        private String group_type;
        private String group_no;
        private int roomId;

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

        public String getGroup_type() {
            return group_type;
        }

        public void setGroup_type(String group_type) {
            this.group_type = group_type;
        }

        public String getGroup_no() {
            return group_no;
        }

        public void setGroup_no(String group_no) {
            this.group_no = group_no;
        }

        public int getRoomId() {
            return roomId;
        }

        public void setRoomId(int roomId) {
            this.roomId = roomId;
        }
    }
}
