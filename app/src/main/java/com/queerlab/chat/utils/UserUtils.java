package com.queerlab.chat.utils;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.queerlab.chat.event.ClapEvent;
import com.queerlab.chat.view.group.user.UserInfoActivity;
import com.queerlab.chat.widget.OnDoubleClickListener;

import org.greenrobot.eventbus.EventBus;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.utils
 * @ClassName: UserUtils
 * @Description: 用户操作工具
 * @Author: 鹿鸿祥
 * @CreateDate: 6/9/21 10:07 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 6/9/21 10:07 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class UserUtils {

    /**
     * 用户头像触摸事件重写
     *
     * @param view
     */
    public static void setUserClap(View view, String userId, String status){
        view.setOnTouchListener(new OnDoubleClickListener(new OnDoubleClickListener.OnDoubleClickCallBack() {
            @Override
            public void oneClick() {
                if (status.equals("user")){
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);
                ActivityUtils.startActivity(bundle, UserInfoActivity.class);
            }

            @Override
            public void doubleClick() {
                EventBus.getDefault().post(new ClapEvent(userId, status));
            }
        }));
    }
}
