package com.queerlab.chat.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.base
 * @ClassName: BaseLazyFragment
 * @Description: 懒加载公共Fragment
 * @Author: 鹿鸿祥
 * @CreateDate: 5/11/21 9:24 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/11/21 9:24 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public abstract class BaseLazyFragment extends BaseFragment{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isPrepared = true;
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initDataAndEvent(savedInstanceState,view);
        lazyLoad();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            lazyLoad();
        }
    }

    protected void lazyLoad() {
        if (isPrepared && getUserVisibleHint() && !isInited) {
            initDataFromService();
        }
    }

    protected abstract void initDataAndEvent(Bundle savedInstanceState);

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState, View view) {
        initDataAndEvent(savedInstanceState);
    }
}
