package com.queerlab.chat.view.message;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.BarUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.base.BaseActivity;
import com.queerlab.chat.base.BuildConfig;
import com.queerlab.chat.widget.CustomWebView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.view.message
 * @ClassName: WebViewActivity
 * @Description: WebView H5页面
 * @Author: 鹿鸿祥
 * @CreateDate: 6/9/21 10:34 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 6/9/21 10:34 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class WebViewActivity extends BaseActivity {
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.web_view)
    CustomWebView webView;

    @Override
    protected int initLayoutRes() {
        return R.layout.activity_web_view;
    }

    @Override
    public boolean isUseEventBus() {
        return false;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(fakeStatusBar, getResources().getColor(R.color.white));
        switch (getIntent().getStringExtra("type")){
            case "user"://用户协议
                webView.loadUrl(BuildConfig.USER_URL);
                break;
            case "privacy"://隐私协议
                webView.loadUrl(BuildConfig.AGREEMENT_URL);
                break;
            case "notice"://系统通知
                webView.loadUrl(getIntent().getStringExtra("url"));
                break;
        }
    }

    @OnClick({R.id.iv_bar_back})
    void onClick() {
        finish();
    }
}
