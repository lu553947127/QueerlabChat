package com.queerlab.chat.view.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.adapter.SearchGroupAdapter;
import com.queerlab.chat.base.BaseLazyFragment;
import com.queerlab.chat.base.EmptyViewFactory;
import com.queerlab.chat.bean.GroupListBean;
import com.queerlab.chat.listener.OnCustomCallBack;
import com.queerlab.chat.tencent.TUIKitUtil;
import com.queerlab.chat.utils.RefreshUtils;
import com.queerlab.chat.viewmodel.GroupViewModel;
import com.queerlab.chat.widget.CustomEditText;
import com.queerlab.chat.widget.DividerItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import butterknife.BindView;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.view.search
 * @ClassName: SearchGroupFragment
 * @Description: 搜索小组fragment
 * @Author: 鹿鸿祥
 * @CreateDate: 5/19/21 1:20 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/19/21 1:20 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class SearchGroupFragment extends BaseLazyFragment {
    @BindView(R.id.refresh)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    private CustomEditText etSearch;
    private TextWatcher textWatcher;
    private GroupViewModel groupViewModel;

    @Override
    protected int initLayout() {
        return R.layout.fragment_search_group;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState) {
        etSearch = mActivity.findViewById(R.id.et_search);

        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_15));
        SearchGroupAdapter searchGroupAdapter = new SearchGroupAdapter(R.layout.adapter_search_group, null);
        recyclerView.setAdapter(searchGroupAdapter);

        searchGroupAdapter.setOnItemClickListener((adapter, view, position) -> {
            GroupListBean.ListBean listBean = searchGroupAdapter.getData().get(position);

            TUIKitUtil.enterGroup(mActivity, listBean, new OnCustomCallBack() {
                @Override
                public void onSuccess(Object data) {
                    groupViewModel.groupJoin(listBean.getGroup_no());
                    TUIKitUtil.startChatActivity(mActivity, true, listBean.getGroup_no(), listBean.getGroup_type() + listBean.getGroup_name(), listBean.getRoomId());
                }

                @Override
                public void onError(String module, int errCode, String errMsg) {

                }
            });
        });

        groupViewModel = mActivity.getViewModel(GroupViewModel.class);

        //搜索用户成功返回结果
        groupViewModel.groupSearchListLiveData.observe(mActivity, groupListBean -> {
            searchGroupAdapter.setKeyword(etSearch.getTrimmedString());
            if (groupListBean.getPageNum() == 1) {
                searchGroupAdapter.setNewData(groupListBean.getList());
                searchGroupAdapter.setEmptyView(EmptyViewFactory.createEmptyView(mActivity, getString(R.string.not_search)));
            } else {
                searchGroupAdapter.addData(searchGroupAdapter.getData().size(), groupListBean.getList());
            }
            RefreshUtils.setNoMore(smartRefreshLayout,groupListBean.getPageNum(), groupListBean.getTotal());
        });

        //刷新和加载
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                groupViewModel.groupSearchList(etSearch.getTrimmedString());
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                groupViewModel.groupSearchListMore(etSearch.getTrimmedString());
            }
        });

        etSearch.addTextChangedListener(textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                groupViewModel.groupSearchList(etSearch.getTrimmedString());
            }
        });

        etSearch.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                // 先隐藏键盘
                KeyboardUtils.hideSoftInput(mActivity);
                // 搜索，进行自己要的操作...
                groupViewModel.groupSearchList(etSearch.getTrimmedString());
                return true;
            }
            return false;
        });
    }

    @Override
    public boolean isUseEventBus() {
        return false;
    }

    @Override
    protected void initDataFromService() {
        groupViewModel.groupSearchList(etSearch.getTrimmedString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        etSearch.removeTextChangedListener(textWatcher);
    }
}
