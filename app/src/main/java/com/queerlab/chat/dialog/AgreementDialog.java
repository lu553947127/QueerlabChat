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
import com.queerlab.chat.base.SpConfig;
import com.queerlab.chat.listener.OnCustomClickListener;
import com.queerlab.chat.utils.LoginUtils;
import com.queerlab.chat.utils.SharedUtils;
import com.queerlab.chat.utils.TextViewUtils;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.dialog
 * @ClassName: AgreementDialog
 * @Description: 协议弹窗
 * @Author: 鹿鸿祥
 * @CreateDate: 5/7/21 10:33 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/7/21 10:33 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@SuppressLint({"StaticFieldLeak","InflateParams","SetTextI18n","CutPasteId"})
public class AgreementDialog {
    private final BaseActivity activity;
    private final Dialog dialog;
    private OnCustomClickListener onCustomClickListener;

    public AgreementDialog(BaseActivity activity) {
        this.activity = activity;
        dialog = new Dialog(activity, R.style.custom_dialog);
    }

    public void setOnCustomClickListener(OnCustomClickListener onCustomClickListener) {
        this.onCustomClickListener = onCustomClickListener;
    }

    // 显示对话框
    public void showDialog() {
        if (dialog.isShowing()) return;
        dialog.setCancelable(false);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_agreement, null);
        TextView tv_content = view.findViewById(R.id.tv_content);
        TextView tv_agree = view.findViewById(R.id.tv_agree);
        TextView tv_disagree = view.findViewById(R.id.tv_disagree);

        TextViewUtils.setTextInfo(activity, tv_content, activity.getResources().getString(R.string.agreement_content),100,106,107,113);

        SharedUtils sharedUtils = new SharedUtils(activity);

        tv_agree.setOnClickListener(v -> {
            sharedUtils.addShared(SpConfig.FIRST_APP,"1","first");
            onCustomClickListener.onSuccess();
            dialog.dismiss();
        });

        tv_disagree.setOnClickListener(v -> {
            dialog.dismiss();
            LoginUtils.getExitApp(activity);
        });

        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        dialog.setContentView(view, new ViewGroup.MarginLayoutParams(displayMetrics.widthPixels, ViewGroup.MarginLayoutParams.MATCH_PARENT));
        dialog.show();
    }
}
