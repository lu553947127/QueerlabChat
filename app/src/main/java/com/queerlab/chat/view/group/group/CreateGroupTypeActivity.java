package com.queerlab.chat.view.group.group;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.adapter.CreateGroupTypeAdapter;
import com.queerlab.chat.base.BaseActivity;
import com.queerlab.chat.base.EmptyViewFactory;
import com.queerlab.chat.bean.GroupTypeBean;
import com.queerlab.chat.viewmodel.GroupViewModel;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.view.group.group
 * @ClassName: CreateGroupTypeActivity
 * @Description: 选择小组分类
 * @Author: 鹿鸿祥
 * @CreateDate: 2021/11/5 10:31
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/11/5 10:31
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class CreateGroupTypeActivity extends BaseActivity {
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    private GroupViewModel groupViewModel;
    private String classId;

    @Override
    protected int initLayoutRes() {
        return R.layout.activity_create_group_type;
    }

    @Override
    public boolean isUseEventBus() {
        return false;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(fakeStatusBar, getResources().getColor(R.color.white));

        recyclerView.setLayoutManager(new GridLayoutManager(activity, 3, LinearLayoutManager.VERTICAL, false));
        CreateGroupTypeAdapter createGroupTypeAdapter = new CreateGroupTypeAdapter(R.layout.adapter_create_group_type, null);
        recyclerView.setAdapter(createGroupTypeAdapter);

        createGroupTypeAdapter.setOnItemClickListener((adapter, view, position) -> {
            GroupTypeBean.ListBean listBean = createGroupTypeAdapter.getData().get(position);
            createGroupTypeAdapter.setIsSelect(listBean.getClass_name());
            classId = listBean.getClass_id();
        });

        groupViewModel = getViewModel(GroupViewModel.class);

        //获取活动类型列表返回数据
        groupViewModel.groupTypeLiveData.observe(activity, groupTypeBean -> {
            createGroupTypeAdapter.setNewData(groupTypeBean.getList());
            createGroupTypeAdapter.setEmptyView(EmptyViewFactory.createEmptyView(activity, getString(R.string.not_empty_type)));
        });

        groupViewModel.getGroupType("1");
    }

    @OnClick({R.id.iv_bar_back, R.id.tv_next})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_bar_back:
                finish();
                break;
            case R.id.tv_next:
                if (TextUtils.isEmpty(classId)){
                    ToastUtils.showShort("请选择小组分类");
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putString("name", getIntent().getStringExtra("name"));
                bundle.putString("classId", classId);
                ActivityUtils.startActivity(bundle, CreateGroupsActivity.class);
                break;
        }
    }
}
