package com.em.yzzdemo.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.em.yzzdemo.BaseFragment;
import com.em.yzzdemo.R;
import com.em.yzzdemo.bean.UserEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Geri on 2016/11/27.
 */

public class ContactsFragmnet extends BaseFragment {

    @BindView(R.id.contacts_view)
    RecyclerView contactsView;
    @BindView(R.id.contacts_group_ll)
    LinearLayout contactsGroupLl;


    private ContactsAdapter mAdapter;
    private List<UserEntity> mUserList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, null);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();

    }

    //初使化联系人列表
    private void initView() {
        mActivity = getActivity();
        mUserList = new ArrayList<>();
        mUserList.addAll(ContactsManager.getInstance(mActivity).getContactList().values());
        mAdapter = new ContactsAdapter(mActivity, mUserList);
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        contactsView.setLayoutManager(manager);
        contactsView.setAdapter(mAdapter);

        //点击进入群组
        contactsGroupLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity,GroupActivity.class));
            }
        });
    }


}
