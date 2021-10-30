package com.queerlab.chat.view.activity;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.BarUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.adapter.BannerExampleAdapter;
import com.queerlab.chat.base.BaseActivity;
import com.queerlab.chat.utils.RefreshUtils;
import com.queerlab.chat.widget.PictureEnlargeUtils;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;

import butterknife.BindView;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.view.activity
 * @ClassName: ActivityDetailActivity
 * @Description: 活动详情页面
 * @Author: 鹿鸿祥
 * @CreateDate: 2021/10/30 13:51
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/10/30 13:51
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ActivityDetailActivity extends BaseActivity {
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.banner)
    Banner banner;

    @Override
    protected int initLayoutRes() {
        return R.layout.activity_activity_detail;
    }

    @Override
    public boolean isUseEventBus() {
        return false;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(fakeStatusBar, getResources().getColor(R.color.white));

        banner.addBannerLifecycleObserver(this)//添加生命周期观察者
                .setAdapter(new BannerExampleAdapter(activity, RefreshUtils.getImagesList()))//添加数据
                .setIndicator(new CircleIndicator(activity), true)
                .setIndicatorSelectedColor(activity.getResources().getColor(R.color.color_FFA52C))
                .setIndicatorNormalColor(activity.getResources().getColor(R.color.white))
                .setOnBannerListener((data, position) -> {
                    PictureEnlargeUtils.getPictureEnlargeList(activity, RefreshUtils.getImagesList(), position);
                })
                .start();
    }
}
