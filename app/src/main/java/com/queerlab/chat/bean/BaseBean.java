package com.queerlab.chat.bean;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.bean
 * @ClassName: BaseBean
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 5/11/21 9:27 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/11/21 9:27 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class BaseBean {

    String tag;
    String title;

    public BaseBean(String tag, String title) {
        this.tag = tag;
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
