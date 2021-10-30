package com.queerlab.chat.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.queerlab.chat.bean.BaseBean;

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
public class ActivityAdapter extends BaseQuickAdapter<BaseBean, BaseViewHolder> {
    public ActivityAdapter(int layoutResId, @Nullable List<BaseBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BaseBean item) {

    }
}
