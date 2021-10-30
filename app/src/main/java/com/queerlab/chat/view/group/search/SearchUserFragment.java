package com.queerlab.chat.view.group.search;

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
import com.queerlab.chat.adapter.SearchUserAdapter;
import com.queerlab.chat.base.BaseLazyFragment;
import com.queerlab.chat.base.EmptyViewFactory;
import com.queerlab.chat.bean.LocationUserBean;
import com.queerlab.chat.dialog.AnimationDialog;
import com.queerlab.chat.event.ClapEvent;
import com.queerlab.chat.utils.RefreshUtils;
import com.queerlab.chat.view.group.user.UserInfoActivity;
import com.queerlab.chat.viewmodel.UserViewModel;
import com.queerlab.chat.widget.CustomEditText;
import com.queerlab.chat.widget.DividerItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.view.group.search
 * @ClassName: SearchUserFragment
 * @Description: 搜索用户fragment
 * @Author: 鹿鸿祥
 * @CreateDate: 5/19/21 1:20 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/19/21 1:20 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class SearchUserFragment extends BaseLazyFragment {
    @BindView(R.id.refresh)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    private CustomEditText etSearch;
    private TextWatcher textWatcher;
    private UserViewModel userViewModel;

    @Override
    protected int initLayout() {
        return R.layout.fragment_search_user;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState) {
        etSearch = mActivity.findViewById(R.id.et_search);

        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_15));
        SearchUserAdapter searchUserAdapter = new SearchUserAdapter(R.layout.adapter_search_user, null);
        recyclerView.setAdapter(searchUserAdapter);

        searchUserAdapter.setOnItemClickListener((adapter, view, position) -> {
            LocationUserBean.ListBean listBean = searchUserAdapter.getData().get(position);
            Bundle bundle = new Bundle();
            bundle.putString("userId", String.valueOf(listBean.getUser_id()));
            ActivityUtils.startActivity(bundle, UserInfoActivity.class);
        });

        userViewModel = mActivity.getViewModel(UserViewModel.class);

        //搜索用户成功返回结果
        userViewModel.userSearchLiveData.observe(mActivity, locationUserBean -> {
            searchUserAdapter.setKeyword(etSearch.getTrimmedString());
            if (locationUserBean.getPageNum() == 1) {
                searchUserAdapter.setNewData(locationUserBean.getList());
                searchUserAdapter.setEmptyView(EmptyViewFactory.createEmptyView(mActivity, getString(R.string.not_search)));
            } else {
                searchUserAdapter.addData(searchUserAdapter.getData().size(), locationUserBean.getList());
            }
            RefreshUtils.setNoMore(smartRefreshLayout,locationUserBean.getPageNum(), locationUserBean.getTotal());
        });

        //用户拍一拍成功返回
        userViewModel.updateUserClapLiveData.observe(mActivity, s -> {
            AnimationDialog animationDialog = new AnimationDialog(mActivity);
            animationDialog.showDialog();
        });

        //刷新和加载
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                userViewModel.userSearch(etSearch.getTrimmedString());
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                userViewModel.userSearchMore(etSearch.getTrimmedString());
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
                userViewModel.userSearch(etSearch.getTrimmedString());
            }
        });

        etSearch.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                // 先隐藏键盘
                KeyboardUtils.hideSoftInput(mActivity);
                // 搜索，进行自己要的操作...
                userViewModel.userSearch(etSearch.getTrimmedString());
                return true;
            }
            return false;
        });
    }

    @Override
    public boolean isUseEventBus() {
        return true;
    }

    @Override
    protected void initDataFromService() {
        userViewModel.userSearch(etSearch.getTrimmedString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        etSearch.removeTextChangedListener(textWatcher);
    }

    /**
     * 双击头像拍一拍
     * @param event
     */
    @Subscribe
    public void onEventUpdateClap(ClapEvent event){
        if (event.getStatus().equals("search")){
            userViewModel.updateUserClap(event.getUserId());
        }
    }
}
