package com.queerlab.chat.view.login;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.adapter.InterestAdapter;
import com.queerlab.chat.base.BaseActivity;
import com.queerlab.chat.base.SpConfig;
import com.queerlab.chat.event.LoginEvent;
import com.queerlab.chat.event.UserEvent;
import com.queerlab.chat.tencent.TUIKitUtil;
import com.queerlab.chat.view.MainActivity;
import com.queerlab.chat.viewmodel.LoginViewModel;
import com.queerlab.chat.viewmodel.UserViewModel;
import com.queerlab.chat.widget.CustomEditText;
import com.queerlab.chat.widget.HorizontalItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.view.login
 * @ClassName: FirstHobbyActivity
 * @Description: 添加兴趣爱好页面
 * @Author: 鹿鸿祥
 * @CreateDate: 5/10/21 2:57 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/10/21 2:57 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@SuppressLint("NewApi")
public class FirstHobbyActivity extends BaseActivity {
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.tv_skip)
    AppCompatTextView tvSkip;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.et_hobby)
    CustomEditText etHobby;
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.tv_next)
    AppCompatTextView tvNext;
    private InterestAdapter interestAdapter;
    private final List<String> list = new ArrayList<>();
    private LoginViewModel loginViewModel;
    private UserViewModel userViewModel;

    @Override
    protected int initLayoutRes() {
        return R.layout.activity_first_hobby;
    }

    @Override
    public boolean isUseEventBus() {
        return true;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(fakeStatusBar, getResources().getColor(R.color.white));
        //键盘右下健(回车键)点击监听
        etHobby.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                addType();
            }
            return false;
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(activity ,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.addItemDecoration(new HorizontalItemDecoration(4, activity));
        interestAdapter = new InterestAdapter(R.layout.adapter_interest, null, "first");
        recyclerView.setAdapter(interestAdapter);
        interestAdapter.setOnItemClickListener((adapter, view, position) -> {
            interestAdapter.remove(position);
            list.remove(position);
        });

        loginViewModel = getViewModel(LoginViewModel.class);

        //上传用户信息成功返回结果
        loginViewModel.userAddLiveData.observe(activity, loginBean -> {
            SPUtils.getInstance().put(SpConfig.USER_ID, loginBean.getUserId(), true);
            SPUtils.getInstance().put(SpConfig.USER_SIG, loginBean.getUserSig(), true);
            SPUtils.getInstance().put(SpConfig.USER_STATUS, "2", true);
            SPUtils.getInstance().put(SpConfig.USER_NAME, getIntent().getStringExtra("name"), true);
            TUIKitUtil.getTUIKitLogin(loginBean.getUserId(), loginBean.getUserSig());
        });

        //加载动画返回结果
        loginViewModel.pageStateLiveData.observe(activity, s -> {
            showPageState(s);
        });

        userViewModel = getViewModel(UserViewModel.class);

        //编辑兴趣爱好成功返回结果
        userViewModel.updateTypeLiveData.observe(activity, s -> {
            ToastUtils.showShort("编辑兴趣成功");
            EventBus.getDefault().post(new UserEvent("type"));
            finish();
        });

        //加载动画返回结果
        userViewModel.pageStateLiveData.observe(activity, s -> {
            showPageState(s);
        });

        editShowData();
    }

    @OnClick({R.id.iv_bar_back, R.id.tv_skip, R.id.tv_add, R.id.tv_next})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_bar_back:
                finish();
                break;
            case R.id.tv_skip://跳过
                loginViewModel.userAdd(getIntent().getStringExtra("name")
                        , SPUtils.getInstance().getString(SpConfig.COUNTRY_CODE)
                        , SPUtils.getInstance().getString(SpConfig.PHONE)
                        , "");
                break;
            case R.id.tv_add://添加兴趣
                addType();
                break;
            case R.id.tv_next://完成
                if (list.size() == 0){
                    userViewModel.userType = "''";
                }else {
                    userViewModel.userType = String.join(",", list);
                }

                if (!TextUtils.isEmpty(getIntent().getStringExtra("edit"))){
                    userViewModel.updateType();
                }else {
                    loginViewModel.userAdd(getIntent().getStringExtra("name")
                            , SPUtils.getInstance().getString(SpConfig.COUNTRY_CODE)
                            , SPUtils.getInstance().getString(SpConfig.PHONE)
                            , userViewModel.userType);
                }
                break;
        }
    }

    /**
     * 添加兴趣爱好
     */
    private void addType(){
        if (TextUtils.isEmpty(etHobby.getTrimmedString())){
            ToastUtils.showShort("兴趣爱好不能为空");
            return;
        }

        if (etHobby.getTrimmedString().length() > 10){
            ToastUtils.showShort("字数过长");
            return;
        }

        list.add(etHobby.getTrimmedString());
        interestAdapter.replaceData(list);
    }

    /**
     * 编辑兴趣爱好 显示之前数据
     */
    private void editShowData(){
        if (!TextUtils.isEmpty(getIntent().getStringExtra("edit"))){
            tvSkip.setVisibility(View.GONE);
            tvTitle.setText(R.string.login_edit_hobby);
            if (!TextUtils.isEmpty(getIntent().getStringExtra("type"))){
                String type = getIntent().getStringExtra("type");
                List<String> oldList = Arrays.asList(type.split(","));
                list.addAll(oldList);
                interestAdapter.replaceData(list);
            }
        }
    }

    /**
     * 腾讯云IM登录成功返回
     *
     * @param event
     */
    @Subscribe
    public void onEventLogin(LoginEvent event) {
        TUIKitUtil.updateProfileName(activity, getIntent().getStringExtra("name"));
        ActivityUtils.startActivity(MainActivity.class);
        finish();
    }
}
