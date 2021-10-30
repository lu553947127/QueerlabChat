package com.queerlab.chat.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.adapter
 * @ClassName: ViewPagerAdapter
 * @Description: fragment数组的适配器
 * @Author: 鹿鸿祥
 * @CreateDate: 5/11/21 9:15 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/11/21 9:15 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private Fragment[] fragments;
    private String[] titles;

    public ViewPagerAdapter(FragmentManager fm, Fragment[] fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public ViewPagerAdapter(FragmentManager fm, Fragment[] fragments, String[] titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null){
            return titles[position];
        }
        return super.getPageTitle(position);
    }
}
