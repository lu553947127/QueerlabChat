package com.queerlab.chat.listener;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.listener
 * @ClassName: OnCustomCallBack
 * @Description: 回调的通用接口类
 * @Author: 鹿鸿祥
 * @CreateDate: 6/5/21 10:31 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 6/5/21 10:31 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface OnCustomCallBack {

    void onSuccess(Object data);

    void onError(String module, int errCode, String errMsg);
}
