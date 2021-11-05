package com.queerlab.chat.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.bean
 * @ClassName: GroupEmoBean
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 2021/11/5 15:50
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/11/5 15:50
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class GroupEmoBean {
    /**
     * total : 2694
     * list : [{"character":"😀","subGroup":"face-smiling","id":1,"slug":"grinning-face","unicodeName":"grinning face","codePoint":"1F600","group":"smileys-emotion"}]
     * pageNum : 1
     * totalPage : 2694
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
         * character : 😀
         * subGroup : face-smiling
         * id : 1
         * slug : grinning-face
         * unicodeName : grinning face
         * codePoint : 1F600
         * group : smileys-emotion
         */

        private String character;
        private String subGroup;
        private int id;
        private String slug;
        private String unicodeName;
        private String codePoint;
        private String group;

        public String getCharacter() {
            return character;
        }

        public void setCharacter(String character) {
            this.character = character;
        }

        public String getSubGroup() {
            return subGroup;
        }

        public void setSubGroup(String subGroup) {
            this.subGroup = subGroup;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public String getUnicodeName() {
            return unicodeName;
        }

        public void setUnicodeName(String unicodeName) {
            this.unicodeName = unicodeName;
        }

        public String getCodePoint() {
            return codePoint;
        }

        public void setCodePoint(String codePoint) {
            this.codePoint = codePoint;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }
    }
}
