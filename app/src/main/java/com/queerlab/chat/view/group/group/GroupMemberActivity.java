package com.queerlab.chat.view.group.group;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.queerlab.chat.R;
import com.queerlab.chat.adapter.GroupMemberAdapter;
import com.queerlab.chat.base.BaseActivity;
import com.queerlab.chat.base.SpConfig;
import com.queerlab.chat.bean.LocationUserBean;
import com.queerlab.chat.dialog.AnimationDialog;
import com.queerlab.chat.event.ClapEvent;
import com.queerlab.chat.listener.OnCustomCallBack;
import com.queerlab.chat.utils.DensityUtil;
import com.queerlab.chat.tencent.TUIKitUtil;
import com.queerlab.chat.view.group.user.UserInfoActivity;
import com.queerlab.chat.viewmodel.GroupViewModel;
import com.queerlab.chat.viewmodel.UserViewModel;
import com.queerlab.chat.widget.DividerItemDecoration;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.view.group.group
 * @ClassName: GroupMemberActivity
 * @Description: 小组成员列表页面
 * @Author: 鹿鸿祥
 * @CreateDate: 6/8/21 11:48 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 6/8/21 11:48 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class GroupMemberActivity extends BaseActivity {
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.rv)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.tv_tips)
    AppCompatTextView tvTips;
    private GroupViewModel groupViewModel;
    private UserViewModel userViewModel;
    private LocationUserBean.ListBean listBean;
    private int adapterPosition;
    //创建侧滑菜单
    private final SwipeMenuCreator creator = (leftMenu, rightMenu, position) -> {
        SwipeMenuItem deleteItem = new SwipeMenuItem(this)
                .setBackgroundColorResource(R.color.color_FFA52C)
                .setText("移出小组")
                .setTextColor(Color.WHITE)
                .setTextSize(14)
                .setHeight(ViewGroup.LayoutParams.MATCH_PARENT)
                .setWidth(DensityUtil.dp2px(100));
        rightMenu.addMenuItem(deleteItem);
    };

    @Override
    protected int initLayoutRes() {
        return R.layout.activity_group_member;
    }

    @Override
    public boolean isUseEventBus() {
        return true;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(fakeStatusBar, getResources().getColor(R.color.white));
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_15));
        GroupMemberAdapter groupMemberAdapter = new GroupMemberAdapter(R.layout.adapter_search_user, null);

        if (SPUtils.getInstance().getString(SpConfig.USER_ID).equals(getIntent().getStringExtra("userId"))){
            //设置侧滑菜单列表
            recyclerView.setSwipeMenuCreator(creator);
            //侧滑事件监听回调
            recyclerView.setOnItemMenuClickListener((menuBridge, adapterPosition) -> {
                listBean = groupMemberAdapter.getData().get(adapterPosition);
                if (menuBridge.getPosition() == 0) {//删除
                    if (String.valueOf(listBean.getUser_id()).equals(getIntent().getStringExtra("userId"))){
                        ToastUtils.showShort("群主不能删除自己哦");
                        menuBridge.closeMenu();
                        return;
                    }

                    new XPopup.Builder(activity).asConfirm(getString(R.string.reminder), "确认移出小组吗", () -> {
                        this.adapterPosition = adapterPosition;
                        groupViewModel.groupKicking(getIntent().getStringExtra("groupId"), String.valueOf(listBean.getUser_id()));
                        menuBridge.closeMenu();
                    }, () -> {
                        menuBridge.closeMenu();
                    }).show();
                }
            });
            tvTips.setText("左滑移出小组");
        }else {
            tvTips.setText("双击头像有惊喜！");
        }

        recyclerView.setAdapter(groupMemberAdapter);

        groupMemberAdapter.setOnItemClickListener((adapter, view, position) -> {
            LocationUserBean.ListBean listBean = groupMemberAdapter.getData().get(position);
            Bundle bundle = new Bundle();
            bundle.putString("userId", String.valueOf(listBean.getUser_id()));
            ActivityUtils.startActivity(bundle, UserInfoActivity.class);
        });

        groupViewModel = getViewModel(GroupViewModel.class);
        userViewModel = getViewModel(UserViewModel.class);

        //获取推荐小组成功返回结果
        groupViewModel.groupMemberLiveData.observe(activity, listBean -> {
            groupMemberAdapter.setNewData(listBean);
        });

        //群主移出用户成功返回结果
        groupViewModel.groupKickingLiveData.observe(activity, s -> {
            TUIKitUtil.getKickGroupMember(getIntent().getStringExtra("groupId"), String.valueOf(listBean.getUser_id()), new OnCustomCallBack() {
                @Override
                public void onSuccess(Object data) {
                    groupMemberAdapter.remove(adapterPosition);
                }

                @Override
                public void onError(String module, int errCode, String errMsg) {

                }
            });
        });

        //用户拍一拍成功返回
        userViewModel.updateUserClapLiveData.observe(activity, s -> {
            AnimationDialog animationDialog = new AnimationDialog(activity);
            animationDialog.showDialog();
        });

        groupViewModel.groupMemberList(getIntent().getStringExtra("groupId"));
    }

    /**
     * 双击头像拍一拍
     * @param event
     */
    @Subscribe
    public void onEventUpdateClap(ClapEvent event){
        if (event.getStatus().equals("group")){
            userViewModel.updateUserClap(event.getUserId());
        }
    }

    @OnClick(R.id.iv_bar_back)
    void OnClick(){
        finish();
    }
}
