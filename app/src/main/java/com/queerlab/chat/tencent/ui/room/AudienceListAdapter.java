package com.queerlab.chat.tencent.ui.room;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.queerlab.chat.R;
import com.queerlab.chat.tencent.ui.widget.msg.AudienceEntity;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 房间在线人员适配器
 */
public class AudienceListAdapter extends RecyclerView.Adapter<AudienceListAdapter.ViewHolder> {
    private static final String TAG = AudienceListAdapter.class.getSimpleName();

    private Context mContext;
    private LinkedList<AudienceEntity> mDataList;
    private HashMap<String, AudienceEntity> mChatSalonMap;

    public AudienceListAdapter(Context context, LinkedList<AudienceEntity> list) {
        this.mContext = context;
        this.mDataList = list;
        mChatSalonMap = new HashMap<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.trtcvoiceroom_item_audience, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AudienceEntity item = mDataList.get(position);
        holder.bind(mContext, item);
    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void addMember(AudienceEntity entity) {
        if (entity == null) {
            return;
        }
        if (entity.userId == null) {
            return;
        }
        if (!mChatSalonMap.containsKey(entity.userId)) {
            mDataList.addFirst(entity);
            mChatSalonMap.put(entity.userId, entity);
            notifyDataSetChanged();
        }
    }

    public void removeMember(String userId) {
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        AudienceEntity localUserInfo = mChatSalonMap.get(userId);
        if (localUserInfo != null) {
            mDataList.remove(localUserInfo);
            mChatSalonMap.remove(userId);
            notifyDataSetChanged();
        }
    }

    public void addMembers(List<AudienceEntity> list) {
        if (list != null) {
            for (AudienceEntity entity : list) {
                addMember(entity);
            }
        }
        notifyDataSetChanged();
    }

    public HashMap<String, AudienceEntity> getChatSalonMap() {
        return mChatSalonMap;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView mImgHead;

        public ViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        public void bind(final Context context,
                         final AudienceEntity entity) {
            if (!TextUtils.isEmpty(entity.userAvatar)) {
                Picasso.get().load(entity.userAvatar).into(mImgHead);
            } else {
                mImgHead.setImageResource(R.drawable.default_head);
            }

        }

        private void initView(@NonNull final View itemView) {
            mImgHead = (CircleImageView) itemView.findViewById(R.id.img_head);
        }
    }
}