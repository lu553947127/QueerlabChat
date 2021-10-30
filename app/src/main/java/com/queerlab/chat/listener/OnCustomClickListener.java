package com.queerlab.chat.listener;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.listener
 * @ClassName: OnCustomClickListener
 * @Description: 公共点击事件
 * @Author: 鹿鸿祥
 * @CreateDate: 5/27/21 3:04 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/27/21 3:04 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface OnCustomClickListener {

    /**
     * 点击成功回调
     */
    void onSuccess();

    /**
     * 点击失败回调
     */
    void onCancel();
}


