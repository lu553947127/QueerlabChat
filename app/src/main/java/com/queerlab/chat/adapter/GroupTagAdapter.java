package com.queerlab.chat.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.queerlab.chat.R;
import com.queerlab.chat.bean.GroupEmoBean;

import java.util.List;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.adapter
 * @ClassName: GroupTagAdapter
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 6/7/21 2:28 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 6/7/21 2:28 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class GroupTagAdapter extends BaseQuickAdapter<GroupEmoBean.ListBean, BaseViewHolder> {
    public GroupTagAdapter(int layoutResId, @Nullable List<GroupEmoBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GroupEmoBean.ListBean item) {
        helper.setText(R.id.tv_title, item.getCharacter());
    }
}
