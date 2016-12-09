package com.em.yzzdemo.contacts;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.em.yzzdemo.R;

/**
 * Created by Geri on 2016/11/30.
 */

public class ContactsClickPopWindow extends PopupWindow {
    private View conentView;

    public ContactsClickPopWindow(final Activity context, View.OnClickListener itemsOnClick, String username) {
        super(context);
        initView(context,itemsOnClick,username);


    }

    private void initView(final Activity context,View.OnClickListener itemsOnClick,String username) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.pop_contacts, null);
        LinearLayout callView = (LinearLayout) conentView.findViewById(R.id.pop_call);
        LinearLayout chatView = (LinearLayout) conentView.findViewById(R.id.pop_chat);
        LinearLayout videoView = (LinearLayout) conentView.findViewById(R.id.pop_video);
        TextView userNameView = (TextView) conentView.findViewById(R.id.tv_username);
        userNameView.setText(username);
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        backgroundAlpha(context,0.5f);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);
        this.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                backgroundAlpha(context, 1f);
            }
        });

        callView.setOnClickListener(itemsOnClick);
        chatView.setOnClickListener(itemsOnClick);
        videoView.setOnClickListener(itemsOnClick);
    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(Activity context, float bgAlpha)
    {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAtLocation(parent, Gravity.CENTER, 0, 0);
        } else {
            dismiss();
        }
    }
}
