package com.queerlab.chat.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.queerlab.chat.R;
import com.queerlab.chat.bean.LocationUserBean;
import com.queerlab.chat.utils.PictureUtils;
import com.queerlab.chat.widget.CircleImageView;

import java.util.List;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.adapter
 * @ClassName: SelectMemberAdapter
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 6/17/21 9:28 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 6/17/21 9:28 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class SelectMemberAdapter extends BaseQuickAdapter<LocationUserBean.ListBean, BaseViewHolder> {
    public SelectMemberAdapter(int layoutResId, @Nullable List<LocationUserBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LocationUserBean.ListBean item) {
        helper.setText(R.id.tv_name, item.getUser_name())
                .setImageResource(R.id.iv_select, item.isSelect() ? R.drawable.icon_select : R.drawable.progressbar_gray)
                .addOnClickListener(R.id.iv_select);

        CircleImageView circleImageView = helper.getView(R.id.iv_avatar);
        PictureUtils.setImage(mContext, item.getUser_portrait(), circleImageView);
    }
}
