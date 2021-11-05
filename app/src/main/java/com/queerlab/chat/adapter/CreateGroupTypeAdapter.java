package com.queerlab.chat.adapter;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.queerlab.chat.R;
import com.queerlab.chat.bean.GroupTypeBean;

import java.util.List;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.adapter
 * @ClassName: CreateGroupTypeAdapter
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 2021/11/5 15:15
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/11/5 15:15
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class CreateGroupTypeAdapter extends BaseQuickAdapter<GroupTypeBean.ListBean, BaseViewHolder> {
    private String is_select;
    public CreateGroupTypeAdapter(int layoutResId, @Nullable List<GroupTypeBean.ListBean> data) {
        super(layoutResId, data);
    }

    /**
     * 列表选择状态
     *
     * @param is_select
     */
    public void setIsSelect(String is_select) {
        this.is_select = is_select;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder helper, GroupTypeBean.ListBean item) {
        helper.setText(R.id.tv_title, item.getClass_name());

        if (!TextUtils.isEmpty(is_select) && is_select.equals(item.getClass_name())){
            helper.setBackgroundRes(R.id.tv_title, R.drawable.shape_yellow_25)
                    .setTextColor(R.id.tv_title, mContext.getResources().getColor(R.color.white));
        }else {
            helper.setBackgroundRes(R.id.tv_title, R.drawable.shape_wathet_25)
                    .setTextColor(R.id.tv_title, mContext.getResources().getColor(R.color.black));
        }
    }
}
