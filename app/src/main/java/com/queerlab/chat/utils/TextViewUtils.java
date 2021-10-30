package com.queerlab.chat.utils;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ActivityUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.base.BaseActivity;
import com.queerlab.chat.view.message.WebViewActivity;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.utils
 * @ClassName: TextViewUtils
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 6/17/21 1:14 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 6/17/21 1:14 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class TextViewUtils {

    /**
     * 设置TextView中间字体颜色与点击事见
     * start   开始文字的位置 坐标从 0开始
     * end     改变结束的位置 ，并不包括这个位置。
     * 使用BackgroundColorSpan设置背景颜色。
     *
     * @param activity
     * @param textView
     */
    public static void setTextInfo(BaseActivity activity, TextView textView, String content, int userStart, int userEnd, int privacyStart, int privacyEnd) {
        //初始化可以更改内容和标记的文本类
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        //添加需要改变的文本
        spannableStringBuilder.append(content);
        //去掉点击后的背景色
        textView.setHighlightColor(activity.getResources().getColor(android.R.color.transparent));

        //设置用户协议点击
        ClickableSpan firstClickableSpan = new ClickableSpan() {
            @Override
            public void updateDrawState(@NonNull TextPaint textPaint) {
                super.updateDrawState(textPaint);
                textPaint.setUnderlineText(false);//取消点击事见字体的背景颜色
                textPaint.setColor(activity.getResources().getColor(R.color.color_FFA52C));
            }
            //变色字体点击监听设置
            @Override
            public void onClick(@NonNull View widget) {
                Bundle bundle = new Bundle();
                bundle.putString("type", "user");
                ActivityUtils.startActivity(bundle, WebViewActivity.class);
            }
        };

        //设置隐私协议点击
        ClickableSpan secondClickableSpan = new ClickableSpan() {
            @Override
            public void updateDrawState(@NonNull TextPaint textPaint) {
                super.updateDrawState(textPaint);
                textPaint.setUnderlineText(false);//取消点击事见字体的背景颜色
                textPaint.setColor(activity.getResources().getColor(R.color.color_FFA52C));
            }
            //变色字体点击监听设置
            @Override
            public void onClick(@NonNull View widget) {
                Bundle bundle = new Bundle();
                bundle.putString("type", "privacy");
                ActivityUtils.startActivity(bundle, WebViewActivity.class);
            }
        };
        spannableStringBuilder.setSpan(firstClickableSpan, userStart, userEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(secondClickableSpan,privacyStart,privacyEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        //一定要记得设置，不然点击不生效
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        //最好设置文字
        textView.setText(spannableStringBuilder);
    }
}
