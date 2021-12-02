package com.queerlab.chat.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.appcompat.widget.AppCompatTextView;

import com.chad.library.adapter.base.BaseViewHolder;

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
     * @param helper 适配器基类
     * @param leftIcon 左侧icon本地路径
     * @param right icon长
     * @param bottom icon宽
     * @param id 设置当前控件id
     */
    public static void setDrawableLeft(Context context, BaseViewHolder helper,
                                       int leftIcon, int right, int bottom, int id){

        Drawable drawable = context.getResources().getDrawable(leftIcon);
        drawable.setBounds(0, 0, right, bottom);
        AppCompatTextView appCompatTextView = helper.getView(id);
        appCompatTextView.setCompoundDrawables(drawable, null, null, null);
    }
}
