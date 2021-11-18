package com.queerlab.chat.view.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.adapter.SearchActivityAdapter;
import com.queerlab.chat.base.BaseLazyFragment;
import com.queerlab.chat.base.EmptyViewFactory;
import com.queerlab.chat.bean.ActivityListBean;
import com.queerlab.chat.utils.RefreshUtils;
import com.queerlab.chat.view.activity.ActivityDetailActivity;
import com.queerlab.chat.viewmodel.ActivityViewModel;
import com.queerlab.chat.widget.CustomEditText;
import com.queerlab.chat.widget.DividerItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import butterknife.BindView;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.view.search
 * @ClassName: SearchActivityFragment
 * @Description: 搜索活动fragment
 * @Author: 鹿鸿祥
 * @CreateDate: 2021/11/18 09:14
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/11/18 09:14
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class SearchActivityFragment extends BaseLazyFragment {
    @BindView(R.id.refresh)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    private CustomEditText etSearch;
    private TextWatcher textWatcher;
    private ActivityViewModel activityViewModel;

    @Override
    protected int initLayout() {
        return R.layout.fragment_search_activity;
    }

    @Override
    public boolean isUseEventBus() {
        return false;
    }

    @Override
    protected void initDataFromService() {
        activityViewModel.searchActivity(etSearch.getTrimmedString());
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState) {
        etSearch = mActivity.findViewById(R.id.et_search);

        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_15));
        SearchActivityAdapter searchActivityAdapter = new SearchActivityAdapter(R.layout.adapter_search_activity, null);
        recyclerView.setAdapter(searchActivityAdapter);

        searchActivityAdapter.setOnItemClickListener((adapter, view, position) -> {
            ActivityListBean.ListBean listBean = searchActivityAdapter.getData().get(position);
            Bundle bundle = new Bundle();
            bundle.putString("activityId", String.valueOf(listBean.getId()));
            ActivityUtils.startActivity(bundle, ActivityDetailActivity.class);
        });

        activityViewModel = mActivity.getViewModel(ActivityViewModel.class);

        //搜索用户成功返回结果
        activityViewModel.searchActivityLiveData.observe(mActivity, activityListBean -> {
            searchActivityAdapter.setKeyword(etSearch.getTrimmedString());
            if (activityListBean.getPageNum() == 1) {
                searchActivityAdapter.setNewData(activityListBean.getList());
                searchActivityAdapter.setEmptyView(EmptyViewFactory.createEmptyView(mActivity, getString(R.string.not_search)));
            } else {
                searchActivityAdapter.addData(searchActivityAdapter.getData().size(), activityListBean.getList());
            }
            RefreshUtils.setNoMore(smartRefreshLayout,activityListBean.getPageNum(), activityListBean.getTotal());
        });

        //刷新和加载
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                activityViewModel.searchActivity(etSearch.getTrimmedString());
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                activityViewModel.searchActivityMore(etSearch.getTrimmedString());
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
                activityViewModel.searchActivity(etSearch.getTrimmedString());
            }
        });

        etSearch.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                // 先隐藏键盘
                KeyboardUtils.hideSoftInput(mActivity);
                // 搜索，进行自己要的操作...
                activityViewModel.searchActivity(etSearch.getTrimmedString());
                return true;
            }
            return false;
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        etSearch.removeTextChangedListener(textWatcher);
    }
}
