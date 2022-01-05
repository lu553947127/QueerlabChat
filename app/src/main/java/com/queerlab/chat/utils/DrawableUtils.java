package com.queerlab.chat.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.utils
 * @ClassName: DrawableUtils
 * @Description: 文本设置drawable
 * @Author: 鹿鸿祥
 * @CreateDate: 2021/12/2 08:44
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/12/2 08:44
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@SuppressLint("UseCompatLoadingForDrawables")
public class DrawableUtils {

    /**
     * 设置文本左边icon
     *
     * @param context 上下文
     * @param appCompatTextView 设置当前控件
     * @param leftIcon 左侧icon本地路径
     * @param right icon长
     * @param bottom icon宽
     */
    public static void setDrawableLeft(Context context, AppCompatTextView appCompatTextView, int leftIcon, int right, int bottom){
        Drawable drawable = context.getResources().getDrawable(leftIcon);
        drawable.setBounds(0, 0, right, bottom);
        appCompatTextView.setCompoundDrawables(drawable, null, null, null);
    }
}
