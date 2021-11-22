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
 * @ClassName: PublicConfirmDialog
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 2021/11/22 09:03
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/11/22 09:03
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@SuppressLint({"StaticFieldLeak","InflateParams","SetTextI18n","CutPasteId"})
public class PublicConfirmDialog {

    private final BaseActivity activity;
    private final Dialog dialog;
    private OnCustomClickListener onCustomClickListener;
    private String title;
    private String button;

    public PublicConfirmDialog(BaseActivity activity, String title, String button) {
        this.activity = activity;
        dialog = new Dialog(activity, R.style.custom_dialog);
        this.title = title;
        this.button = button;
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

        tv_title.setText(title);
        tv_agree.setText(button);

        tv_agree.setOnClickListener(v -> {
            onCustomClickListener.onSuccess();
            dialog.dismiss();
        });

        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        dialog.setContentView(view, new ViewGroup.MarginLayoutParams(displayMetrics.widthPixels, ViewGroup.MarginLayoutParams.MATCH_PARENT));
        dialog.show();
    }
}
