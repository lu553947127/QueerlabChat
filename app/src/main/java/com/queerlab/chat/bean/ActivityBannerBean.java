package com.queerlab.chat.bean;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.bean
 * @ClassName: ActivityBannerBean
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 2021/11/19 13:35
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/11/19 13:35
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ActivityBannerBean {
    /**
     * image : https://tdc-1303184599.cos.na-ashburn.myqcloud.com/poster/eba31d66-aedf-4f0d-9c85-26d03e08621b.jpg
     * id : 25
     * group_no : @TGS#3MNZ2CSHG
     */

    private String image;
    private int id;
    private String group_no;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroup_no() {
        return group_no;
    }

    public void setGroup_no(String group_no) {
        this.group_no = group_no;
    }
}
