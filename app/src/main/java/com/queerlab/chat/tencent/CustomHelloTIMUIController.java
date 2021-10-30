package com.queerlab.chat.tencent;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.app.MyApplication;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.tencent
 * @ClassName: CustomHelloTIMUIController
 * @Description: 自定义消息布局页面设置
 * @Author: 鹿鸿祥
 * @CreateDate: 7/14/21 2:44 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 7/14/21 2:44 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class CustomHelloTIMUIController {

    private static final String TAG = CustomHelloTIMUIController.class.getSimpleName();

    public static void onDraw(ICustomMessageViewGroup parent, final ChatLayoutHelper.CustomHelloMessage data) {

        // 把自定义消息view添加到TUIKit内部的父容器里
        View view = LayoutInflater.from(MyApplication.getInstance()).inflate(R.layout.test_custom_message_layout, null, false);
        parent.addMessageContentView(view);

        // 自定义消息view的实现，这里仅仅展示文本信息，并且实现超链接跳转
        TextView textView = view.findViewById(R.id.test_custom_message_tv);
        final String text = "不支持的自定义消息";
        if (data == null) {
            textView.setText(text);
        } else {
            textView.setText(data.text);
        }
        view.setClickable(true);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data == null) {
                    LogUtils.e(TAG, "Do what?");
                    ToastUtil.toastShortMessage(text);
                    return;
                }
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(data.link);
                intent.setData(content_url);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApplication.getInstance().startActivity(intent);
            }
        });
    }
}
