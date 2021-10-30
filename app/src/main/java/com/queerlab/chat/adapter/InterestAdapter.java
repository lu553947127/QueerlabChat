package com.queerlab.chat.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.queerlab.chat.R;

import java.util.List;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.adapter
 * @ClassName: InterestAdapter
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 5/18/21 10:05 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/18/21 10:05 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class InterestAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private final String type;
    public InterestAdapter(int layoutResId, @Nullable List<String> data, String type) {
        super(layoutResId, data);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_title, item);
        switch (type){
            case "first"://第一次添加兴趣
                helper.setBackgroundRes(R.id.tv_title, R.drawable.shape_wathet_25);
                break;
            case "user"://地图用户列表
//                helper.setBackgroundRes(R.id.tv_title, R.drawable.shape_yellow2_25);
                helper.setBackgroundRes(R.id.tv_title, R.drawable.shape_wathet_25);
                break;
        }
    }
}
