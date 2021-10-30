package com.queerlab.chat.view.group.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.base.BaseActivity;
import com.queerlab.chat.event.UserEvent;
import com.queerlab.chat.viewmodel.UserViewModel;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import fj.edittextcount.lib.FJEditTextCount;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.view.group.user
 * @ClassName: UpdateStatusActivity
 * @Description: 更新状态页面
 * @Author: 鹿鸿祥
 * @CreateDate: 5/31/21 10:35 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/31/21 10:35 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class UpdateStatusActivity extends BaseActivity {
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.et_content)
    FJEditTextCount fjEditTextCount;
    private UserViewModel userViewModel;

    @Override
    protected int initLayoutRes() {
        return R.layout.activity_update_status;
    }

    @Override
    public boolean isUseEventBus() {
        return false;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(fakeStatusBar, getResources().getColor(R.color.white));
        fjEditTextCount.setText(getIntent().getStringExtra("status"));
        userViewModel = getViewModel(UserViewModel.class);

        //更新状态成功返回接口
        userViewModel.updateStatusLiveData.observe(activity, s -> {
            ToastUtils.showShort("更新状态成功");
            EventBus.getDefault().post(new UserEvent("status"));
            finish();
        });

        //加载动画返回结果
        userViewModel.pageStateLiveData.observe(activity, s -> {
            showPageState(s);
        });
    }

    @OnClick({R.id.iv_bar_back, R.id.tv_ok})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_bar_back:
                finish();
                break;
            case R.id.tv_ok://保存
                if (TextUtils.isEmpty(fjEditTextCount.getText())){
                    ToastUtils.showShort("输入不能为空");
                    return;
                }

                userViewModel.updateStatus(fjEditTextCount.getText());
                break;
        }
    }
}
