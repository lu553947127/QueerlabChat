package com.queerlab.chat.adapter;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.queerlab.chat.R;
import com.queerlab.chat.base.SpConfig;
import com.queerlab.chat.bean.LocationUserBean;
import com.queerlab.chat.utils.DrawableUtils;
import com.queerlab.chat.utils.PictureUtils;
import com.queerlab.chat.utils.UserUtils;
import com.queerlab.chat.widget.CircleImageView;

import java.util.List;


/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.adapter
 * @ClassName: MapNearbyAdapter
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 5/18/21 9:19 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/18/21 9:19 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class MapNearbyAdapter extends BaseQuickAdapter<LocationUserBean.ListBean, BaseViewHolder> {
    public MapNearbyAdapter(int layoutResId, @Nullable List<LocationUserBean.ListBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, LocationUserBean.ListBean item) {
        helper.setText(R.id.tv_name, TextUtils.isEmpty(item.getUser_name()) ? "未知" : item.getUser_name())
                .setText(R.id.tv_status, item.getUser_status())
                .setText(R.id.tv_user_type, item.getUser_type())
                .setGone(R.id.tv_status, !TextUtils.isEmpty(item.getUser_status()))
                .setGone(R.id.tv_user_type, !TextUtils.isEmpty(item.getUser_type()));

        CircleImageView circleImageView = helper.getView(R.id.iv_avatar);
        PictureUtils.setImage(mContext, item.getUser_portrait(), circleImageView);

        if (!SPUtils.getInstance().getString(SpConfig.USER_ID).equals(String.valueOf(item.getUser_id()))){
            UserUtils.setUserClap(circleImageView, String.valueOf(item.getUser_id()), "map");
        }

        DrawableUtils.setDrawableLeft(mContext, helper, R.drawable.icon_type, 35, 35, R.id.tv_user_type);
        DrawableUtils.setDrawableLeft(mContext, helper, R.drawable.icon_activity, 35, 35, R.id.tv_activity);
    }
}
