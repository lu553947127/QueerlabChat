package com.queerlab.chat.adapter;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.queerlab.chat.R;
import com.queerlab.chat.base.SpConfig;
import com.queerlab.chat.bean.LocationUserBean;
import com.queerlab.chat.utils.PictureUtils;
import com.queerlab.chat.utils.UserUtils;
import com.queerlab.chat.widget.CircleImageView;

import java.util.List;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.adapter
 * @ClassName: GroupMemberAdapter
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 6/8/21 1:46 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 6/8/21 1:46 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class GroupMemberAdapter extends BaseQuickAdapter<LocationUserBean.ListBean, BaseViewHolder> {
    public GroupMemberAdapter(int layoutResId, @Nullable List<LocationUserBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LocationUserBean.ListBean item) {
        helper.setText(R.id.tv_name, item.getUser_name());

        CircleImageView circleImageView = helper.getView(R.id.iv_avatar);
        PictureUtils.setImage(mContext, item.getUser_portrait(), circleImageView);

        if (!SPUtils.getInstance().getString(SpConfig.USER_ID).equals(String.valueOf(item.getUser_id()))){
            UserUtils.setUserClap(circleImageView, String.valueOf(item.getUser_id()), "group");
        }
    }
}
