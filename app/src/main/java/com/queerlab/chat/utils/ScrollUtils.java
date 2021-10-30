package com.queerlab.chat.utils;

import android.view.View;

import androidx.core.widget.NestedScrollView;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.utils
 * @ClassName: 滑动布局工具类
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 5/11/21 9:37 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/11/21 9:37 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ScrollUtils {

    /**
     * NestedScrollView上滑显示 下滑隐藏
     *
     * @param scrollView
     * @param view
     */
    public static void OnScrollChangeListener(NestedScrollView scrollView, View view) {
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            private boolean isShow = true;
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                //上滑 并且 正在显示底部栏
                if (scrollY - oldScrollY > 0 && isShow) {
                    isShow = false;
                    //将Y属性变为底部栏高度  (相当于隐藏了)
                    AnimatorUtils.hideFABAnimation(view);
                } else if (scrollY - oldScrollY < 0 && !isShow) {
                    isShow = true;
                    AnimatorUtils.showFABAnimation(view);
                }
            }
        });
    }
}
