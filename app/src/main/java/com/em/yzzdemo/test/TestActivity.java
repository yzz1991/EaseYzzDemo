package com.em.yzzdemo.test;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.em.yzzdemo.BaseActivity;
import com.em.yzzdemo.R;
import com.em.yzzdemo.widgit.RecordView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Geri on 2016/12/20.
 */
public class TestActivity extends BaseActivity {
    @BindView(R.id.recordView)
    RecordView recordView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
    }
}
