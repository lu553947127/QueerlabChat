package com.queerlab.chat.view.group.group;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.adapter.SelectMemberAdapter;
import com.queerlab.chat.base.BaseActivity;
import com.queerlab.chat.bean.LocationUserBean;
import com.queerlab.chat.event.CallingEvent;
import com.queerlab.chat.view.group.user.UserInfoActivity;
import com.queerlab.chat.viewmodel.GroupViewModel;
import com.queerlab.chat.widget.DividerItemDecoration;
import com.tencent.liteav.login.UserModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.view.group.group
 * @ClassName: SelectMemberActivity
 * @Description: 选择群聊人员页面
 * @Author: 鹿鸿祥
 * @CreateDate: 6/16/21 4:11 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 6/16/21 4:11 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class SelectMemberActivity extends BaseActivity {
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    private final Map<String, String> matchMap = new HashMap<>();

    @Override
    protected int initLayoutRes() {
        return R.layout.activity_select_member;
    }

    @Override
    public boolean isUseEventBus() {
        return false;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(fakeStatusBar, getResources().getColor(R.color.white));

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST,R.drawable.divider_15));
        SelectMemberAdapter selectMemberAdapter = new SelectMemberAdapter(R.layout.adapter_select_member, null);
        recyclerView.setAdapter(selectMemberAdapter);

        selectMemberAdapter.setOnItemClickListener((adapter, view, position) -> {
            LocationUserBean.ListBean listBean = selectMemberAdapter.getData().get(position);
            Bundle bundle = new Bundle();
            bundle.putString("userId", String.valueOf(listBean.getUser_id()));
            ActivityUtils.startActivity(bundle, UserInfoActivity.class);
        });

        selectMemberAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            LocationUserBean.ListBean listBean = selectMemberAdapter.getData().get(position);
            if (!listBean.isSelect()){
                listBean.setSelect(true);
                matchMap.put(String.valueOf(listBean.getUser_id()), listBean.getUser_name());
            }else {
                if (getIntent().getStringExtra("userId").equals(String.valueOf(listBean.getUser_id()))){
                    return;
                }
                listBean.setSelect(false);
                matchMap.remove(String.valueOf(listBean.getUser_id()));
            }

            selectMemberAdapter.notifyDataSetChanged();
        });

        GroupViewModel groupViewModel = getViewModel(GroupViewModel.class);

        //获取推荐小组成功返回结果
        groupViewModel.groupMemberLiveData.observe(activity, listBean -> {
            List<LocationUserBean.ListBean> list = new ArrayList<>();
            for (LocationUserBean.ListBean listBean2 : listBean){
                if (getIntent().getStringExtra("userId").equals(String.valueOf(listBean2.getUser_id()))){
                    listBean2.setSelect(true);
//                    matchMap.put(String.valueOf(listBean2.getUser_id()), listBean2.getUser_name());
                }
                list.add(listBean2);
            }
            selectMemberAdapter.setNewData(list);
        });

        groupViewModel.groupMemberList(getIntent().getStringExtra("groupId"));
    }

    @OnClick({R.id.iv_bar_back, R.id.tv_ok})
    void OnClick(View view){
        switch (view.getId()){
            case R.id.iv_bar_back:
                finish();
                break;
            case R.id.tv_ok://选择完成 并开启语音通话
                if (matchMap.size() == 0){
                    ToastUtils.showShort("请至少选择一个用户，开启语音通话哦");
                    return;
                }

                StringBuilder matchIdBuilder = new StringBuilder();
                for (Map.Entry<String, String> vo : matchMap.entrySet()) {
                    matchIdBuilder.append(vo.getKey()).append(",");
                }

                //最后生成用，隔开的字符串
                String ids = matchIdBuilder.substring(0, matchIdBuilder.length() - 1);

                //生成string集合
                List<String> idList = Arrays.asList(ids.split(","));

                LogUtils.e(idList);

                List<UserModel> models = new ArrayList<>();
                for (String userId : idList){
                    UserModel userModel = new UserModel();
                    userModel.userId = userId;
                    models.add(userModel);
                }

                EventBus.getDefault().post(new CallingEvent(models));
                finish();
                break;
        }
    }
}
