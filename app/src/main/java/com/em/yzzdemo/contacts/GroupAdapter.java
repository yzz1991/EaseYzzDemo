package com.em.yzzdemo.contacts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.em.yzzdemo.R;
import com.hyphenate.chat.EMGroup;

import java.util.List;

/**
 * Created by Geri on 2016/11/29.
 */
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder>{
    private Context mContext;
    private List<EMGroup> mGroupList;
    private LayoutInflater mInflater;
    public GroupAdapter(Context context, List<EMGroup> groupList) {
        mContext = context;
        mGroupList = groupList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GroupViewHolder(mInflater.inflate(R.layout.item_group,null));
    }

    @Override
    public void onBindViewHolder(GroupViewHolder viewHolder, int position) {
        viewHolder.groupNameView.setText(mGroupList.get(position).getGroupName());

    }

    @Override
    public int getItemCount() {
        return mGroupList.size();
    }

    /**
     * 自定义群组 ViewHolder 用来展示群组数据
     */
    public static class GroupViewHolder extends RecyclerView.ViewHolder{
        ImageView avatarView;
        TextView groupNameView;
        TextView groupMembersNumView;
        /**
         * 构造方法
         *
         * @param itemView 显示群组数据的 ItemView
         */
        public GroupViewHolder(View itemView) {
            super(itemView);
            avatarView = (ImageView) itemView.findViewById(R.id.iv_avatar);
            groupNameView = (TextView) itemView.findViewById(R.id.tv_groupName);
            groupMembersNumView = (TextView) itemView.findViewById(R.id.tv_groupMembersNum);

        }
    }


}
