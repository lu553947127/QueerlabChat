package com.queerlab.chat.view.start;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.base.BaseActivity;
import com.queerlab.chat.base.SpConfig;
import com.queerlab.chat.utils.SharedUtils;
import com.queerlab.chat.view.login.LoginActivity;
import com.queerlab.chat.widget.OnDoubleClickListener;

import butterknife.BindView;
import butterknife.OnClick;
import per.goweii.swipeback.SwipeBackDirection;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.view.start
 * @ClassName: GuideActivity
 * @Description: 引导页面
 * @Author: 鹿鸿祥
 * @CreateDate: 5/7/21 2:33 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/7/21 2:33 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@SuppressLint("ClickableViewAccessibility")
public class GuideActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.ll_group)
    LinearLayout llGroup;
    @BindView(R.id.iv_avatar)
    AppCompatImageView ivAvatar;
    @BindView(R.id.tv_next)
    AppCompatTextView tvNext;

    @Override
    protected int initLayoutRes() {
        return R.layout.activity_guide;
    }

    @Override
    public boolean isUseEventBus() {
        return false;
    }

    @NonNull
    @Override
    public SwipeBackDirection swipeBackDirection() {
        return SwipeBackDirection.NONE;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState) {
        BarUtils.setStatusBarLightMode(this,false);
        ivAvatar.setOnTouchListener(new OnDoubleClickListener(new OnDoubleClickListener.OnDoubleClickCallBack() {
            @Override
            public void oneClick() {

            }

            @Override
            public void doubleClick() {
                SharedUtils sharedUtils = new SharedUtils(activity);
                sharedUtils.addShared(SpConfig.GUIDE_APP,"1","guide");
                ActivityUtils.startActivity(LoginActivity.class);
                finish();
            }
        }));
    }

    @OnClick({R.id.ll_group})
    void onClick() {
        tvTitle.setText(getString(R.string.guide_person_title));
        llGroup.setVisibility(View.GONE);
        ivAvatar.setVisibility(View.VISIBLE);
        tvNext.setText(getString(R.string.guide_double_avatar));
    }
}
