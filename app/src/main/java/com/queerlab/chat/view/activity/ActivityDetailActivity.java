package com.queerlab.chat.view.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.BarUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.base.BaseActivity;
import com.queerlab.chat.bean.ActivityDetailBean;
import com.queerlab.chat.bean.GroupListBean;
import com.queerlab.chat.dialog.PublicConfirmDialog;
import com.queerlab.chat.listener.OnCustomCallBack;
import com.queerlab.chat.listener.OnCustomClickListener;
import com.queerlab.chat.tencent.TUIKitUtil;
import com.queerlab.chat.utils.PictureUtils;
import com.queerlab.chat.utils.WebUtils;
import com.queerlab.chat.viewmodel.ActivityViewModel;
import com.queerlab.chat.viewmodel.GroupViewModel;
import com.queerlab.chat.widget.CornerImageView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.view.activity
 * @ClassName: ActivityDetailActivity
 * @Description: 活动详情页面
 * @Author: 鹿鸿祥
 * @CreateDate: 2021/10/30 13:51
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/10/30 13:51
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ActivityDetailActivity extends BaseActivity {
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.iv_image)
    AppCompatImageView ivImage;
    @BindView(R.id.iv_avatar)
    CornerImageView cornerImageView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.tv_promoter)
    AppCompatTextView tvPromoter;
    @BindView(R.id.tv_start_time)
    AppCompatTextView tvStartTime;
    @BindView(R.id.tv_end_time)
    AppCompatTextView tvEndTime;
    @BindView(R.id.tv_place)
    AppCompatTextView tvPlace;
    @BindView(R.id.tv_point)
    AppCompatTextView tvPoint;
    @BindView(R.id.tv_join_group)
    AppCompatTextView tvJoinGroup;
    @BindView(R.id.tv_attend_activity)
    AppCompatTextView tvAttendActivity;
    @BindView(R.id.webview_detail)
    WebView webView;
    private ActivityViewModel activityViewModel;
    private GroupViewModel groupViewModel;
    private ActivityDetailBean activityDetailBean;

    @Override
    protected int initLayoutRes() {
        return R.layout.activity_activity_detail;
    }

    @Override
    public boolean isUseEventBus() {
        return false;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(fakeStatusBar, getResources().getColor(R.color.white));
        WebUtils.getShowTextLabel(webView);

        activityViewModel = getViewModel(ActivityViewModel.class);
        groupViewModel = getViewModel(GroupViewModel.class);

        //获取活动详情数据
        activityViewModel.activityDetailLiveData.observe(activity, activityDetailBean -> {
            this.activityDetailBean = activityDetailBean;
            PictureUtils.setImage(activity, activityDetailBean.getImage(), ivImage);
            PictureUtils.setImage(activity, activityDetailBean.getLogo(), cornerImageView);
            tvTitle.setText(activityDetailBean.getTitle());
            tvPromoter.setText("发起人：" + activityDetailBean.getPromoter());
            tvStartTime.setText("开始时间：" + activityDetailBean.getStartTime());
            tvEndTime.setText("结束时间：" + activityDetailBean.getEndTime());
            tvPlace.setText("地点：" + activityDetailBean.getPlace());
            tvPoint.setText("点数：" + activityDetailBean.getCost());
            webView.loadDataWithBaseURL(null, activityDetailBean.getDetails(),
                    "text/html",  "utf-8", null);
            activityViewModel.activityJoinStatus(activityDetailBean.getId(), activityDetailBean.getGroupNo());
        });

        //查询用户参加活动状态返回结构
        activityViewModel.activityJoinStatusLiveData.observe(activity, activityJoinStatusBean -> {
            if (activityJoinStatusBean.getSelectGroupId() == 0){
                tvJoinGroup.setText("\uD83D\uDCAC加入小组");
            }else {
                tvJoinGroup.setText("\uD83D\uDCAC进入小组");
            }

            if (activityJoinStatusBean.getSelectActivityId() == 0){
                tvAttendActivity.setText("️\uD83D\uDE4B\uD83D\uDE03我要参加");
                tvAttendActivity.setBackgroundResource(R.drawable.shape_yellow_25);
                tvAttendActivity.setClickable(true);
            }else {
                tvAttendActivity.setText("️\uD83D\uDE4B☑️已报名");
                tvAttendActivity.setBackgroundResource(R.drawable.shape_gray2_25);
                tvAttendActivity.setClickable(false);
            }
        });

        //加入活动成功返回结果
        activityViewModel.activityJoinLiveData.observe(activity, s -> {
            PublicConfirmDialog publicConfirmDialog = new PublicConfirmDialog(activity, "\uD83C\uDF89报名成功！", "OK");
            publicConfirmDialog.showDialog();
            publicConfirmDialog.setOnCustomClickListener(new OnCustomClickListener() {
                @Override
                public void onSuccess() {
                    finish();
                }

                @Override
                public void onCancel() {

                }
            });
        });

        //查询耽搁小组详情返回结果
        activityViewModel.groupDetailLiveData.observe(activity, groupDetailBean -> {
            GroupListBean.ListBean listBean = new GroupListBean.ListBean();
            listBean.setGroup_id(groupDetailBean.getGroup_id());
            listBean.setGroup_type(groupDetailBean.getGroup_type());
            listBean.setGroup_name(groupDetailBean.getGroup_name());
            listBean.setGroup_no(groupDetailBean.getGroup_no());
            listBean.setGroup_status(groupDetailBean.getGroup_status());
            listBean.setRoomId(groupDetailBean.getRoomId());
            listBean.setUserNum(groupDetailBean.getUserNum());
            listBean.setLiveNum(groupDetailBean.getLiveNum());

            TUIKitUtil.enterGroup(activity, listBean, new OnCustomCallBack() {
                @Override
                public void onSuccess(Object data) {
                    groupViewModel.groupJoin(listBean.getGroup_no());
                    TUIKitUtil.startChatActivity(activity, true, listBean.getGroup_no(),
                            listBean.getGroup_type() + listBean.getGroup_name(), listBean.getRoomId());
                }

                @Override
                public void onError(String module, int errCode, String errMsg) {

                }
            });
        });

        //加载动画返回结果
        activityViewModel.pageStateLiveData.observe(activity, s -> {
            showPageState(s);
        });

        activityViewModel.activityDetail(getIntent().getStringExtra("activityId"));
    }

    @OnClick({R.id.iv_bar_back, R.id.tv_join_group, R.id.tv_attend_activity})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_bar_back:
                finish();
                break;
            case R.id.tv_join_group://加入/进入小组
                activityViewModel.groupDetail(activityDetailBean.getGroupNo());
                break;
            case R.id.tv_attend_activity://参加活动
                activityViewModel.activityJoin(getIntent().getStringExtra("activityId"));
                break;
        }
    }
}
