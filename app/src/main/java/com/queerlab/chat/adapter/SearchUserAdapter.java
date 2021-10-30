package com.queerlab.chat.adapter;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.queerlab.chat.R;
import com.queerlab.chat.base.SpConfig;
import com.queerlab.chat.bean.LocationUserBean;
import com.queerlab.chat.utils.HtmlUtils;
import com.queerlab.chat.utils.PictureUtils;
import com.queerlab.chat.utils.UserUtils;
import com.queerlab.chat.widget.CircleImageView;

import java.util.List;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.adapter
 * @ClassName: SearchUserAdapter
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 5/19/21 1:25 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/19/21 1:25 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class SearchUserAdapter extends BaseQuickAdapter<LocationUserBean.ListBean, BaseViewHolder> {
    private String keyword = "";
    public SearchUserAdapter(int layoutResId, @Nullable List<LocationUserBean.ListBean> data) {
        super(layoutResId, data);
    }

    /**
     * 设置关键字
     *
     * @param keyword
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    protected void convert(BaseViewHolder helper, LocationUserBean.ListBean item) {
        helper.setText(R.id.tv_name, HtmlUtils.setSpan(mContext, item.getUser_name(), keyword));

        CircleImageView circleImageView = helper.getView(R.id.iv_avatar);
        PictureUtils.setImage(mContext, item.getUser_portrait(), circleImageView);

        if (!SPUtils.getInstance().getString(SpConfig.USER_ID).equals(String.valueOf(item.getUser_id()))){
            UserUtils.setUserClap(circleImageView, String.valueOf(item.getUser_id()), "search");
        }
    }
}
