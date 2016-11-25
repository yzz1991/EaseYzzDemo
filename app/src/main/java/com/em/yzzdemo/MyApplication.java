package com.em.yzzdemo;

import android.app.Application;
import android.content.Context;

/**
 * Created by Geri on 2016/11/25.
 */

public class MyApplication extends Application {

    // 全局的上下文对象
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;

        // 调用自定义初始化方法,封装在 MLHyphenate 类中
        MyHyphenate.getInstance().initHyphenate(context);

    }

    public static Context getContext() {
        return context;
    }
}
