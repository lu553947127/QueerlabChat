package com.queerlab.chat.utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.queerlab.chat.R;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.utils
 * @ClassName: HtmlUtils
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 6/4/21 2:09 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 6/4/21 2:09 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class HtmlUtils {

    /**
     * 搜索中的文字设置显示高亮
     *
     * @param text
     * @return
     */
    public static SpannableString setSpan(Context context, String text , String keyword) {
        SpannableString sp = new SpannableString(text);
        // 遍历要显示的文字
        for (int i = 0; i < text.length(); i++) {
            // 得到单个文字
            String s1 = text.charAt(i) + "";
            // 判断字符串是否包含高亮显示的文字
            if (keyword.contains(s1)) {
                // 循环查找字符串中所有该文字并高亮显示
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.color_FFA52C));
                sp.setSpan(colorSpan, i, i + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }
        return sp;
    }
}
