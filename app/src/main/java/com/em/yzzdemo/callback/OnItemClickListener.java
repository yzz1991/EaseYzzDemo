package com.em.yzzdemo.callback;

import android.view.View;

/**
 * Created by Geri on 2016/12/2.
 * recycleView的点击回调接口
 */

public interface OnItemClickListener {

    //点击
    void onItemClick(View view, int position);

    //长按
    void onItemLongClick(View view, int position);
}
