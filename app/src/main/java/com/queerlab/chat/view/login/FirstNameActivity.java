package com.queerlab.chat.view.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.base.BaseActivity;
import com.queerlab.chat.view.group.group.CreateGroupsActivity;
import com.queerlab.chat.widget.CustomEditText;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.view.login
 * @ClassName: FirstNameActivity
 * @Description: 创建用户名称页面
 * @Author: 鹿鸿祥
 * @CreateDate: 5/10/21 2:44 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/10/21 2:44 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class FirstNameActivity extends BaseActivity {
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.et_name)
    CustomEditText etName;
    @BindView(R.id.tv_next)
    AppCompatTextView tvNext;

    @Override
    protected int initLayoutRes() {
        return R.layout.activity_first_name;
    }

    @Override
    public boolean isUseEventBus() {
        return false;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(fakeStatusBar, getResources().getColor(R.color.white));
    }

    @OnTextChanged(value = R.id.et_name, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void input(Editable editable) {
        String edit = editable.toString().trim();
        tvNext.setEnabled(!edit.isEmpty());
    }

    @OnClick({R.id.iv_bar_back, R.id.tv_next})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_bar_back:
                finish();
                break;
            case R.id.tv_next:
                Bundle bundle = new Bundle();
                bundle.putString("name", etName.getTrimmedString());

                if (TextUtils.isEmpty(getIntent().getStringExtra("type"))){
                    ActivityUtils.startActivity(bundle, FirstHobbyActivity.class);
                    finish();
                }else {
                    ActivityUtils.startActivity(bundle, CreateGroupsActivity.class);
                }
                break;
        }
    }
}
