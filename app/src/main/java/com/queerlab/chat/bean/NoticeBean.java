package com.queerlab.chat.bean;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.bean
 * @ClassName: NoticeBean
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 6/9/21 10:44 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 6/9/21 10:44 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class NoticeBean {
    /**
     * notice_title : 百度翻译
     * notice_content : https://fanyi.baidu.com/translate?aldtype=16047&query=&keyfrom=baidu&smartresult=dict&lang=auto2zh#auto/zh/
     */

    private String notice_title;
    private String notice_content;

    public String getNotice_title() {
        return notice_title;
    }

    public void setNotice_title(String notice_title) {
        this.notice_title = notice_title;
    }

    public String getNotice_content() {
        return notice_content;
    }

    public void setNotice_content(String notice_content) {
        this.notice_content = notice_content;
    }
}
