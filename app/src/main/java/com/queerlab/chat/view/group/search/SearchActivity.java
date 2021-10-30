package com.queerlab.chat.view.group.search;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.kcrason.dynamicpagerindicatorlibrary.DynamicPagerIndicator;
import com.queerlab.chat.R;
import com.queerlab.chat.adapter.ViewPagerAdapter;
import com.queerlab.chat.base.BaseActivity;
import com.queerlab.chat.widget.CustomEditText;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.view.group
 * @ClassName: SearchActivity
 * @Description: 搜索页面
 * @Author: 鹿鸿祥
 * @CreateDate: 5/19/21 1:07 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/19/21 1:07 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class SearchActivity extends BaseActivity {
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.et_search)
    CustomEditText etSearch;
    @BindView(R.id.dynamic_pager_indicator)
    DynamicPagerIndicator dynamicPagerIndicator;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @Override
    protected int initLayoutRes() {
        return R.layout.activity_search;
    }

    @Override
    public boolean isUseEventBus() {
        return false;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(fakeStatusBar, getResources().getColor(R.color.white));
        Fragment[] fragments = {
                new SearchGroupFragment(),
                new SearchUserFragment()
        };
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments, getResources().getStringArray(R.array.search_list)));
        dynamicPagerIndicator.setViewPager(viewPager);

        KeyboardUtils.showSoftInput(etSearch);
    }

    @OnClick({R.id.iv_bar_back})
    void onClick() {
        finish();
    }
}
