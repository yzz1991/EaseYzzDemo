package com.em.yzzdemo.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.em.yzzdemo.BaseFragment;
import com.em.yzzdemo.MyHyphenate;
import com.em.yzzdemo.R;
import com.em.yzzdemo.sign.SignInActivity;
import com.hyphenate.EMCallBack;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Geri on 2016/11/27.
 */

public class SettingFragment extends BaseFragment {
    @BindView(R.id.bt_logout)
    Button btLogout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActivity = getActivity();

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyHyphenate.getInstance().signOut(new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        startActivity(new Intent(mActivity, SignInActivity.class));
                        mActivity.finish();
                    }

                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });
    }


}
