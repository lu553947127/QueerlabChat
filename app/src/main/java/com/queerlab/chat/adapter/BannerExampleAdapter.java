package com.queerlab.chat.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.queerlab.chat.R;
import com.queerlab.chat.widget.image.ImageConfig;
import com.queerlab.chat.widget.image.ImageLoader;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.adapter
 * @ClassName: BannerExampleAdapter
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 2021/10/30 13:29
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/10/30 13:29
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class BannerExampleAdapter extends BannerAdapter<String, BannerExampleAdapter.BannerViewHolder> {
    public Context context;
    public BannerExampleAdapter(Context context, List<String> beanList) {
        //设置数据，也可以调用banner提供的方法,或者自己在adapter中实现
        super(beanList);
        this.context = context;
    }

    //创建ViewHolder，可以用viewType这个字段来区分不同的ViewHolder
    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new BannerViewHolder(imageView);
    }

    @Override
    public void onBindView(BannerViewHolder holder, String item, int position, int size) {
        ImageLoader.load(context, new ImageConfig.Builder()
                .url(item)
                .imageView(holder.imageView)
                .placeholder(R.drawable.icon_images_empty)
                .errorPic(R.drawable.icon_images_empty)
                .build());
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public BannerViewHolder(@NonNull ImageView view) {
            super(view);
            this.imageView = view;
        }
    }
}
