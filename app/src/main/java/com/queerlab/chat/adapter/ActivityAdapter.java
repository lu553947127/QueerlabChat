package com.queerlab.chat.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.queerlab.chat.R;
import com.queerlab.chat.bean.ActivityListBean;
import com.queerlab.chat.utils.PictureUtils;
import com.queerlab.chat.widget.CornerImageView;

import java.util.List;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.adapter
 * @ClassName: ActivityAdapter
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 2021/10/28 15:27
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/10/28 15:27
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ActivityAdapter extends BaseQuickAdapter<ActivityListBean.ListBean, BaseViewHolder> {
    public ActivityAdapter(int layoutResId, @Nullable List<ActivityListBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActivityListBean.ListBean item) {
        helper.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_address, item.getPlace())
                .setText(R.id.tv_initiator, item.getPromoter());

        CornerImageView cornerImageView = helper.getView(R.id.iv_avatar);
        PictureUtils.setImage(mContext, item.getImage(), cornerImageView);
    }
}
