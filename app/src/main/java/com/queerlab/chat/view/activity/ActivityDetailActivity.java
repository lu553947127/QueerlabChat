package com.queerlab.chat.view.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.BarUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.base.BaseActivity;
import com.queerlab.chat.listener.OnCustomCallBack;
import com.queerlab.chat.tencent.TUIKitUtil;
import com.queerlab.chat.utils.PictureUtils;
import com.queerlab.chat.viewmodel.ActivityViewModel;
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
    @BindView(R.id.tv_attend_activity)
    AppCompatTextView tvAttendActivity;
    @BindView(R.id.tv_detail)
    AppCompatTextView tvDetail;
    private ActivityViewModel activityViewModel;

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

        activityViewModel = getViewModel(ActivityViewModel.class);

        //获取活动详情数据
        activityViewModel.activityDetailLiveData.observe(activity, activityDetailBean -> {
            PictureUtils.setImage(activity, activityDetailBean.getImage(), ivImage);
            PictureUtils.setImage(activity, activityDetailBean.getImage(), cornerImageView);
            tvTitle.setText(activityDetailBean.getTitle());
            tvPromoter.setText("发起人：" + activityDetailBean.getPromoter());
            tvStartTime.setText("开始时间：" + activityDetailBean.getStartTime());
            tvEndTime.setText("结束时间：" + activityDetailBean.getEndTime());
            tvPlace.setText("地点：" + activityDetailBean.getPlace());
            tvPoint.setText("点数：" + activityDetailBean.getPlace());
            tvDetail.setText(Html.fromHtml(activityDetailBean.getDetails()));
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
            case R.id.tv_join_group://加入小组
//                TUIKitUtil.enterGroup(activity, listBean, new OnCustomCallBack() {
//                    @Override
//                    public void onSuccess(Object data) {
//                        groupViewModel.groupJoin(listBean.getGroup_no());
//                        TUIKitUtil.startChatActivity(mActivity, true, listBean.getGroup_no(),
//                                listBean.getGroup_type() + listBean.getGroup_name(), listBean.getRoomId());
//                    }
//
//                    @Override
//                    public void onError(String module, int errCode, String errMsg) {
//
//                    }
//                });
                break;
            case R.id.tv_attend_activity://参加活动
                break;
        }
    }
}
