package com.queerlab.chat.view.start;

import android.os.Bundle;
import android.view.View;

import com.queerlab.chat.R;
import com.queerlab.chat.base.BaseFragment;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.view.start
 * @ClassName: ActiveFragment
 * @Description: 活动fragment
 * @Author: 鹿鸿祥
 * @CreateDate: 2021/10/20 09:31
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/10/20 09:31
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ActiveFragment extends BaseFragment {
    @Override
    protected int initLayout() {
        return R.layout.fragment_active;
    }

    @Override
    public boolean isUseEventBus() {
        return false;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState, View view) {

    }

    @Override
    protected void initDataFromService() {

    }
}
