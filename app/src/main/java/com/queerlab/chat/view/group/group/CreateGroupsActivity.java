package com.queerlab.chat.view.group.group;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.adapter.GroupTagAdapter;
import com.queerlab.chat.base.BaseActivity;
import com.queerlab.chat.base.EmptyViewFactory;
import com.queerlab.chat.bean.GroupEmoBean;
import com.queerlab.chat.event.GroupEvent;
import com.queerlab.chat.listener.OnCustomCallBack;
import com.queerlab.chat.utils.AnimatorUtils;
import com.queerlab.chat.utils.RefreshUtils;
import com.queerlab.chat.tencent.TUIKitUtil;
import com.queerlab.chat.view.login.FirstNameActivity;
import com.queerlab.chat.viewmodel.GroupViewModel;
import com.queerlab.chat.viewmodel.NewGroupViewModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.view.group
 * @ClassName: CreateGroupsActivity
 * @Description: 创建小组页面
 * @Author: 鹿鸿祥
 * @CreateDate: 5/19/21 9:53 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/19/21 9:53 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class CreateGroupsActivity extends BaseActivity {
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.tv_emoji)
    AppCompatTextView tvEmoji;
    @BindView(R.id.refresh)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.tv_next)
    AppCompatTextView tvNext;
    //小组标识
    private String groupType;
    private GroupViewModel groupViewModel;
    private NewGroupViewModel newGroupViewModel;

    @Override
    protected int initLayoutRes() {
        return R.layout.activity_create_groups;
    }

    @Override
    public boolean isUseEventBus() {
        return false;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(fakeStatusBar, getResources().getColor(R.color.white));
        groupType = String.valueOf(RefreshUtils.getEmoji().get(41));
        tvEmoji.setText(groupType);

        GroupTagAdapter groupTagAdapter = new GroupTagAdapter(R.layout.adapter_group_tag, null);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 8, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(groupTagAdapter);

        groupTagAdapter.setOnItemClickListener((adapter, view, position) -> {
            GroupEmoBean.ListBean listBean = groupTagAdapter.getData().get(position);
            groupType = listBean.getCharacter();
            tvEmoji.setText(groupType);
        });

        groupViewModel = getViewModel(GroupViewModel.class);
        newGroupViewModel = getViewModel(NewGroupViewModel.class);

        //创建小组成功返回结果
        groupViewModel.groupAddLiveData.observe(activity, s ->{
            ToastUtils.showShort("创建小组成功");
            EventBus.getDefault().post(new GroupEvent("create"));
            ActivityUtils.finishActivity(FirstNameActivity.class);
            ActivityUtils.finishActivity(CreateGroupTypeActivity.class);
            finish();
        });

        //获取emjio成功返回结果
        newGroupViewModel.groupEmoLiveData.observe(activity, groupEmoBean -> {
            if (groupEmoBean.getPageNum() == 1) {
                groupTagAdapter.setNewData(groupEmoBean.getList());
                groupTagAdapter.setEmptyView(EmptyViewFactory.createEmptyView(activity, getString(R.string.not_empty_emo)));
            } else {
                groupTagAdapter.addData(groupTagAdapter.getData().size(), groupEmoBean.getList());
            }
            RefreshUtils.setNoMore(smartRefreshLayout, groupEmoBean.getPageNum(), groupEmoBean.getTotal());
        });

        //刷新和加载
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                newGroupViewModel.getGroupEmo();
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                newGroupViewModel.getGroupEmoMore();
            }
        });

        //滑动监听
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                AnimatorUtils.listScrollAnimation(tvNext, dy);
            }
        });

        //加载动画返回结果
        groupViewModel.pageStateLiveData.observe(activity, s -> {
            showPageState(s);
        });

        newGroupViewModel.getGroupEmo();
    }

    @OnClick({R.id.iv_bar_back, R.id.tv_next})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_bar_back:
                finish();
                break;
            case R.id.tv_next:
                createGroup();
                break;
        }
    }

    /**
     * 创建小组
     */
    private void createGroup() {
        TUIKitUtil.createGroup(groupType + getIntent().getStringExtra("name"), new OnCustomCallBack() {
            @Override
            public void onSuccess(Object data) {
                groupViewModel.groupAdd(String.valueOf(data), getIntent().getStringExtra("name")
                        , groupType, getIntent().getStringExtra("classId"));
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                if (errCode == 10037){
                    ToastUtils.showShort("可创建和加入的群组数量超过了限制");
                }else {
                    ToastUtils.showShort("创建小组失败：" + errMsg);
                }
            }
        });
    }
}
