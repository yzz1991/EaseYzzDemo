package com.em.yzzdemo.contacts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.em.yzzdemo.R;
import com.em.yzzdemo.bean.UserEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by Geri on 2016/11/28.
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>{
    private Context mContext;
    private List<UserEntity> mUserList;
    private LayoutInflater mInflater;

    public ContactsAdapter(Context context, List<UserEntity> userList) {
        mContext = context;
        mUserList = userList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactsViewHolder(mInflater.inflate(R.layout.item_contacts,null));
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, int position) {
        UserEntity userEntity = mUserList.get(position);
        holder.usernameView.setText(userEntity.getUserName());
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    /**
     * 自定义联系人 ViewHolder 用来展示联系人数据
     */
    public static class ContactsViewHolder extends RecyclerView.ViewHolder {

        ImageView avatarView;
        TextView usernameView;


        /**
         * 构造方法
         *
         * @param itemView 显示联系人数据的 ItemView
         */
        public ContactsViewHolder(View itemView) {
            super(itemView);
            avatarView = (ImageView) itemView.findViewById(R.id.iv_avatar);
            usernameView = (TextView) itemView.findViewById(R.id.tv_username);
        }
    }
}
