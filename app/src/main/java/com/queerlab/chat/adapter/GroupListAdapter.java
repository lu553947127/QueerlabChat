package com.queerlab.chat.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.queerlab.chat.R;
import com.queerlab.chat.bean.GroupListBean;

import java.util.List;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.adapter
 * @ClassName: GroupListAdapter
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 5/11/21 9:26 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/11/21 9:26 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class GroupListAdapter extends BaseQuickAdapter<GroupListBean.ListBean, BaseViewHolder> {
    public GroupListAdapter(int layoutResId, @Nullable List<GroupListBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GroupListBean.ListBean item) {
        helper.setText(R.id.tv_tag, item.getGroup_type())
                .setText(R.id.tv_name, item.getGroup_name())
                .setText(R.id.tv_num, item.getUserNum() + "个成员")
                .setVisible(R.id.ll_status, item.getGroup_status() == 1)
                .setText(R.id.tv_status, item.getGroup_status() == 1 ? "'直播中'" : "发起活动中");
    }
}
