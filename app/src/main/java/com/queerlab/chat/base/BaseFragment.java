package com.queerlab.chat.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.base
 * @ClassName: BaseFragment
 * @Description: 公共Fragment
 * @Author: 鹿鸿祥
 * @CreateDate: 5/6/21 2:15 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/6/21 2:15 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public abstract class BaseFragment extends Fragment{
    public Context mContext;
    public BaseActivity mActivity;
    public Bundle arguments;
    protected Unbinder unBinder;
    protected boolean isPrepared = false;//页面ui初始化完成
    protected boolean isInited = false;//数据是否已从服务器拉取，拉取成功后设为true

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
        this.mActivity = (BaseActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arguments = getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (initLayout() != 0){
            View view = inflater.inflate(initLayout(), container, false);
            unBinder = ButterKnife.bind(this, view);
            if (isUseEventBus()) {
                EventBus.getDefault().register(this);
            }
            return view;
        }else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDataAndEvent(savedInstanceState, view);
        load();
    }

    private void load() {
        initDataFromService();
    }

    @Override
    public void onDestroyView() {
        unBinder.unbind();
        if (isUseEventBus() && EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroyView();
    }

    /**
     * 初始化布局
     *
     * @return 布局
     */
    protected abstract int initLayout();

    /**
     * EventBus开关
     */
    public abstract boolean isUseEventBus();

    /**
     * 初始化数据
     *
     * @param savedInstanceState 数据状态
     */
    protected abstract void initDataAndEvent(Bundle savedInstanceState, View view);


    /**
     * 从服务器获取数据
     */
    protected abstract void initDataFromService();
}
