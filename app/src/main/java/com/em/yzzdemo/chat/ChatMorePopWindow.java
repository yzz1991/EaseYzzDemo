package com.em.yzzdemo.chat;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.em.yzzdemo.R;

/**
 * Created by Geri on 2016/11/30.
 */

public class ChatMorePopWindow extends PopupWindow {
    private View conentView;

    public ChatMorePopWindow(final Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        initView(context,itemsOnClick);


    }

    private void initView(final Activity context,View.OnClickListener itemsOnClick) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.chat_toolbar_item, null);
        LinearLayout cameraView = (LinearLayout) conentView.findViewById(R.id.item_camera);
        LinearLayout pictureView = (LinearLayout) conentView.findViewById(R.id.item_picture);
        LinearLayout locationView = (LinearLayout) conentView.findViewById(R.id.item_location);
        LinearLayout fileView = (LinearLayout) conentView.findViewById(R.id.item_file);
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
//        backgroundAlpha(context,0.5f);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);
        this.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
//                backgroundAlpha(context, 1f);
            }
        });

        cameraView.setOnClickListener(itemsOnClick);
        pictureView.setOnClickListener(itemsOnClick);
        locationView.setOnClickListener(itemsOnClick);
        fileView.setOnClickListener(itemsOnClick);
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
//            this.showAtLocation(parent, Gravity.CENTER, 0, 0);
            this.showAsDropDown(parent,0 ,0);
        } else {
            dismiss();
        }
    }
}
