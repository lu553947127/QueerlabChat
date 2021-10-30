package com.queerlab.chat.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.queerlab.chat.R;
import com.queerlab.chat.base.BaseActivity;
import com.queerlab.chat.listener.OnCustomClickListener;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.dialog
 * @ClassName: PublicTextDialog
 * @Description: 公共文字弹窗
 * @Author: 鹿鸿祥
 * @CreateDate: 5/27/21 1:43 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/27/21 1:43 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@SuppressLint({"StaticFieldLeak","InflateParams","SetTextI18n","CutPasteId"})
public class PublicTextDialog {

    private final BaseActivity activity;
    private final Dialog dialog;
    private OnCustomClickListener onCustomClickListener;
    private String text;

    public PublicTextDialog(BaseActivity activity, String text) {
        this.activity = activity;
        dialog = new Dialog(activity, R.style.custom_dialog);
        this.text = text;
    }

    public void setOnCustomClickListener(OnCustomClickListener onCustomClickListener) {
        this.onCustomClickListener = onCustomClickListener;
    }

    // 显示对话框
    public void showDialog() {
        if (dialog.isShowing()) return;
        dialog.setCancelable(false);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_public_text, null);
        TextView tv_title = view.findViewById(R.id.tv_title);
        TextView tv_agree = view.findViewById(R.id.tv_agree);
        TextView tv_disagree = view.findViewById(R.id.tv_disagree);

        tv_title.setText(text);

        tv_agree.setOnClickListener(v -> {
            onCustomClickListener.onSuccess();
            dialog.dismiss();
        });

        tv_disagree.setOnClickListener(v -> {
            onCustomClickListener.onCancel();
            dialog.dismiss();
        });

        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        dialog.setContentView(view, new ViewGroup.MarginLayoutParams(displayMetrics.widthPixels, ViewGroup.MarginLayoutParams.MATCH_PARENT));
        dialog.show();
    }
}
