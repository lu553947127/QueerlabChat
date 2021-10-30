package com.queerlab.chat.base;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.queerlab.chat.R;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.base
 * @ClassName: EmptyViewFactory
 * @Description: 全局公共空页面
 * @Author: 鹿鸿祥
 * @CreateDate: 6/1/21 10:16 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 6/1/21 10:16 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@SuppressLint("InflateParams")
public class EmptyViewFactory {

    /**
     * 无数据占位页
     *
     * @param activity 上下文
     * @param empty 占位文字
     * @return
     */
    public static View createEmptyView(BaseActivity activity, String empty){
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_empty, null);
        TextView tvEmpty = view.findViewById(R.id.tv_empty);
        tvEmpty.setText(empty);
        return view;
    }

    /**
     * 无数据占位页(带按钮回掉)
     *
     * @param activity 上下文
     * @param empty 占位文字
     * @param emptyCall 占位按钮文字
     * @param callBack 底部按钮点击回调 没有可传null
     * @return
     */
    public static View createEmptyCallView(BaseActivity activity, String empty, String emptyCall, EmptyViewCallBack callBack){
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_empty_call, null);
        TextView tvEmpty = view.findViewById(R.id.tv_empty);
        TextView tv_empty_call = view.findViewById(R.id.tv_empty_call);
        tvEmpty.setText(empty);
        tv_empty_call.setText(emptyCall);
        //跳转的点击回调
        if (callBack != null) {
            tv_empty_call.setOnClickListener(v -> callBack.onEmptyClick());
        }
        return view;
    }

    /**
     * 无数据点击事件
     */
    public interface EmptyViewCallBack {
        void onEmptyClick();
    }
}
