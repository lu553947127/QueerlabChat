package com.queerlab.chat.bean;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.bean
 * @ClassName: UploadFileBean
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 6/2/21 11:17 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 6/2/21 11:17 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class UploadFileBean {

    String userPortraitKey;
    String userId;

    public String getUserPortraitKey() {
        return userPortraitKey;
    }

    public void setUserPortraitKey(String userPortraitKey) {
        this.userPortraitKey = userPortraitKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
