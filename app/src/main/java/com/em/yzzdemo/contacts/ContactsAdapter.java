package com.em.yzzdemo.contacts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Geri on 2016/11/28.
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>{
    private Context mContext;
    private List<String> mUserList;
    public ContactsAdapter(Context context, List<String> userList) {
        mContext = context;
        mUserList = userList;
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    /**
     * 自定义联系人 ViewHolder 用来展示联系人数据
     */
    public static class ContactsViewHolder extends RecyclerView.ViewHolder {


        /**
         * 构造方法
         *
         * @param itemView 显示联系人数据的 ItemView
         */
        public ContactsViewHolder(View itemView) {
            super(itemView);
        }
    }
}
