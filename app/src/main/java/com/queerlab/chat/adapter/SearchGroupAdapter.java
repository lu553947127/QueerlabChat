package com.queerlab.chat.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.queerlab.chat.R;
import com.queerlab.chat.bean.GroupListBean;
import com.queerlab.chat.utils.HtmlUtils;

import java.util.List;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.adapter
 * @ClassName: SearchGroupAdapter
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 6/4/21 1:54 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 6/4/21 1:54 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class SearchGroupAdapter extends BaseQuickAdapter<GroupListBean.ListBean, BaseViewHolder> {
    private String keyword = "";
    public SearchGroupAdapter(int layoutResId, @Nullable List<GroupListBean.ListBean> data) {
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
    protected void convert(BaseViewHolder helper, GroupListBean.ListBean item) {
        helper.setText(R.id.tv_name, HtmlUtils.setSpan(mContext, item.getGroup_type()  + item.getGroup_name(), keyword));
    }
}
