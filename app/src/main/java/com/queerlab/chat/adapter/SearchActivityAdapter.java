package com.queerlab.chat.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.queerlab.chat.R;
import com.queerlab.chat.bean.ActivityListBean;
import com.queerlab.chat.utils.HtmlUtils;
import com.queerlab.chat.utils.PictureUtils;
import com.queerlab.chat.widget.CornerImageView;

import java.util.List;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.adapter
 * @ClassName: SearchActivityAdapter
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 2021/11/18 09:20
 * @UpdaeUser: 更新者
 * @UpdateDate: 2021/11/18 09:20
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class SearchActivityAdapter extends BaseQuickAdapter<ActivityListBean.ListBean, BaseViewHolder> {
    private String keyword = "";
    public SearchActivityAdapter(int layoutResId, @Nullable List<ActivityListBean.ListBean> data) {
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
    protected void convert(BaseViewHolder helper, ActivityListBean.ListBean item) {
        helper.setText(R.id.tv_title, HtmlUtils.setSpan(mContext, item.getTitle(), keyword));

        CornerImageView cornerImageView = helper.getView(R.id.iv_images);
        PictureUtils.setImage(mContext, item.getLogo(), cornerImageView);
    }
}
