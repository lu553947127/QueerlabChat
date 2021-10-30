package com.queerlab.chat.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.queerlab.chat.R;
import com.queerlab.chat.app.MyApplication;
import com.queerlab.chat.base.BaseActivity;
import com.queerlab.chat.utils.AnimatorUtils;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.dialog
 * @ClassName: AnimationDialog
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 6/9/21 9:04 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 6/9/21 9:04 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@SuppressLint({"StaticFieldLeak","InflateParams","SetTextI18n","CutPasteId"})
public class AnimationDialog {
    private final BaseActivity activity;
    private final Dialog dialog;

    public AnimationDialog(BaseActivity activity) {
        this.activity = activity;
        dialog = new Dialog(activity, R.style.custom_dialog);
    }

    public void showDialog() {
        if (dialog.isShowing()) return;
        dialog.setCancelable(false);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_animation, null);
        RelativeLayout relativeLayout = view.findViewById(R.id.rl);
        LottieAnimationView lottieAnimationView = view.findViewById(R.id.lottieAnimationView);
        lottieAnimationView.setAnimation("clap.json");

        AnimatorUtils.setShowAnimation(relativeLayout, 2000);

        //3秒后执行Runnable中的run方法
        MyApplication.getMainThreadHandler().postDelayed(() -> AnimatorUtils.setHideAnimation(relativeLayout, 2000, dialog), 2000);

        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        dialog.setContentView(view, new ViewGroup.MarginLayoutParams(displayMetrics.widthPixels, ViewGroup.MarginLayoutParams.MATCH_PARENT));
        dialog.show();
    }
}
