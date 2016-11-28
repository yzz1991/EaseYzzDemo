package com.em.yzzdemo.contacts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.em.yzzdemo.BaseFragment;
import com.em.yzzdemo.R;

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
        mActivity = getParentFragment().getActivity();
//        mAdapter = new ContactsAdapter(mActivity);
    }


}
