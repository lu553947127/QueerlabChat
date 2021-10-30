package com.queerlab.chat.base;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.base
 * @ClassName: IView
 * @Description: 动画加载回调
 * @Author: 鹿鸿祥
 * @CreateDate: 5/22/21 1:38 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/22/21 1:38 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface IView {

    /**
     * 加载中
     */
    void showLoading();

    /**
     * 隐藏
     */
    void hideLoading();
}
