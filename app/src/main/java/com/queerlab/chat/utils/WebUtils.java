package com.queerlab.chat.utils;

import android.annotation.SuppressLint;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.utils
 * @ClassName: WebUtils
 * @Description: webView工具设置
 * @Author: 鹿鸿祥
 * @CreateDate: 2021/12/2 14:26
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/12/2 14:26
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class WebUtils {

    /**
     * 显示h5标签的文字，也可显示标签带图片的效果
     * @param webView
     */
    @SuppressLint("SetJavaScriptEnabled")
    public static void getShowTextLabel(WebView webView) {
        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);  //设置 缓存模式
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);// 关键点


        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放
        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
         * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        // 扩大比例的缩放
        webSettings.setUseWideViewPort(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setAppCacheEnabled(true); // 设置H5的缓存是否打开，默认关闭。
        webSettings.setDatabaseEnabled(true); // 开启 database storage API 功能
        webSettings.setDomStorageEnabled(true);
        //开启 database storage API 功能
        webSettings.setDatabaseEnabled(true);
        webSettings=webView.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setTextSize(WebSettings.TextSize.LARGEST);
//        SMALLEST(50%), SMALLER(75%),NORMAL(100%),LARGER(150%),LARGEST(200%);
    }
}
