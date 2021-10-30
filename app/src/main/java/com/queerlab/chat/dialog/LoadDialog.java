package com.queerlab.chat.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;

import com.blankj.utilcode.util.ConvertUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.utils.AnimatorUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.dialog
 * @ClassName: LoadDialog
 * @Description: 公共加载动画弹窗
 * @Author: 鹿鸿祥
 * @CreateDate: 5/27/21 11:35 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/27/21 11:35 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class LoadDialog extends AppCompatDialog {

    public Activity mActivity;
    private LoadDialog dialog;
    private Unbinder unbinder;

    public LoadDialog(@NonNull Activity activity) {
        super(activity);
        this.mActivity = activity;
        this.dialog = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        unbinder = ButterKnife.bind(this);

        setWidth(ConvertUtils.dp2px(100));
        setHeight(ConvertUtils.dp2px(100));
        setCancelOutside(false);
        AnimatorUtils.enterCustomAnim(this);
    }

    /**
     * 显示位置
     * @param gravity
     * @return
     */
    public LoadDialog setGravity(int gravity){
        if (getWindow() != null){
            getWindow().setGravity(gravity);
        }
        return this;
    }

    /**
     * 宽度
     * @return
     */
    public LoadDialog setWidth(int width){
        if (getWindow() != null){
            getWindow().getDecorView().setPadding(0,0,0,0);
            getWindow().getDecorView().setBackgroundResource(R.drawable.shape_white_2);
            WindowManager.LayoutParams attributes = getWindow().getAttributes();
            attributes.width = width;
            getWindow().setAttributes(attributes);
        }
        return this;
    }

    /**
     * 高度
     * @return
     */
    public LoadDialog setHeight(int height){
        if (getWindow() != null){
            getWindow().getDecorView().setPadding(0,0,0,0);
            getWindow().getDecorView().setBackgroundResource(R.drawable.shape_white_2);
            WindowManager.LayoutParams attributes = getWindow().getAttributes();
            attributes.height = height;
            getWindow().setAttributes(attributes);
        }
        return this;
    }

    /**
     * 点击外部消失
     * @param isCancel
     * @return
     */
    public LoadDialog setCancelOutside(boolean isCancel){
        dialog.setCanceledOnTouchOutside(isCancel);
        return this;
    }

    /**
     * 显示
     * @return
     */
    public LoadDialog showDialog(){
        dialog.show();
        return this;
    }

    /**
     * 隐藏
     * @return
     */
    public LoadDialog hideDialog(){
        dialog.dismiss();
        return this;
    }

    @Override
    public void dismiss() {
        try {
            unbinder.unbind();
        }catch (Exception e){
            //重复解绑问题
        }
        super.dismiss();
    }
}
