package com.queerlab.chat.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.bean
 * @ClassName: GroupTypeBean
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 2021/11/5 15:16
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/11/5 15:16
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class GroupTypeBean {
    /**
     * total : 3
     * list : [{"update_time":"2021-11-03 18:23:40","emoName":"🖼️","class_id":31,"class_sn":1,"type":2,"class_name":"电影","add_time":"2021-11-03 18:23:40"}]
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
         * update_time : 2021-11-03 18:23:40
         * emoName : 🖼️
         * class_id : 31
         * class_sn : 1
         * type : 2
         * class_name : 电影
         * add_time : 2021-11-03 18:23:40
         */

        private String update_time;
        private String emoName;
        private String class_id;
        private int class_sn;
        private int type;
        private String class_name;
        private String add_time;

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getEmoName() {
            return emoName;
        }

        public void setEmoName(String emoName) {
            this.emoName = emoName;
        }

        public String getClass_id() {
            return class_id;
        }

        public void setClass_id(String class_id) {
            this.class_id = class_id;
        }

        public int getClass_sn() {
            return class_sn;
        }

        public void setClass_sn(int class_sn) {
            this.class_sn = class_sn;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getClass_name() {
            return class_name;
        }

        public void setClass_name(String class_name) {
            this.class_name = class_name;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }
    }
}
