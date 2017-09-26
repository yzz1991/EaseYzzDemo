package com.em.yzzdemo.widgit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.em.yzzdemo.MyApplication;
import com.em.yzzdemo.R;
import com.em.yzzdemo.utils.DateUtil;

/**
 * Created by Geri on 2016/12/20.
 */

public class RecordView extends View {
    private Paint mPaint;
    private Rect mBound;
    // 上下文对象
    protected Context mContext;
    /**
     * 文本
     */
    private String mTitleText;
    /**
     * 文本的颜色
     */
    private int mTitleTextColor = 0xffffffff;
    /**
     * 录音按钮文本的大小
     */
    private int mTitleTextSize = MyApplication.getContext().getResources().getDimensionPixelSize(R.dimen.record_font_16);

    /**
     * 外圆的颜色、宽
     * 时间字体的大小、颜色、
     */
    private int mOuterColor= 0x0f3e5c78;
    private int mOuterWidth = MyApplication.getContext().getResources().getDimensionPixelSize(R.dimen.outer_width_100);
    private String mTiemText;
    private int mTimeTextColor = 0xff000000;
    private int mTimeTextSize = MyApplication.getContext().getResources().getDimensionPixelSize(R.dimen.record_font_16);
    /**
     * 内圈录音按钮的颜色、宽
     */
    private int mTouchColor = 0xff00ba6e;
    private int mRecordWidth = MyApplication.getContext().getResources().getDimensionPixelSize(R.dimen.record_width_90);

    /**
     * 外圈背景颜色
     */
    private int mBackgroundColor = 0xfff2f2f2;
    //是否在录音区域按下
    private boolean isDown = false;
    // 录制开始时间
    protected long startTime = 0L;
    // 录制持续时间
    protected int recordTime = 0;
    // 录音控件回调接口
    protected MLRecordCallback mRecordCallback;


    public RecordView(Context context) {
        this(context, null);
    }

    public RecordView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
        mContext = context;
    }

    /**
     * 获得我自定义的样式属性
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public RecordView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs,defStyle);
        /**
         * 获得自定义的样式属性
         */
        if(attrs != null){
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTitleView);
            mTiemText = a.getString(R.styleable.CustomTitleView_timeText);
            mTimeTextColor = a.getColor(R.styleable.CustomTitleView_timeTextColor,mTimeTextColor);
            mTimeTextSize = a.getDimensionPixelOffset(R.styleable.CustomTitleView_timeTextSize,mTimeTextSize);
            mTitleText = a.getString(R.styleable.CustomTitleView_titleText);
            mTitleTextColor = a.getColor(R.styleable.CustomTitleView_titleTextColor,mTitleTextColor);
            mTitleTextSize = a.getDimensionPixelOffset(R.styleable.CustomTitleView_titleTextSize,mTitleTextSize);
            mRecordWidth = a.getDimensionPixelOffset(R.styleable.CustomTitleView_record_waveform_width,mRecordWidth);
            mOuterWidth = a.getDimensionPixelOffset(R.styleable.CustomTitleView_outer_width,mOuterWidth);
            a.recycle();
        }


        /**
         * 获得绘制文本的宽和高
         */
        mPaint = new Paint();
        // 设置抗锯齿
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mTitleTextSize);
        mBound = new Rect();
        mPaint.getTextBounds(mTitleText,0,mTitleText.length(),mBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("draw","ddddddd");
        /**
         * 绘制背景
         */
        mPaint.setColor(mBackgroundColor);
        //绘制矩形
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);
        /**
         * 绘制背景里的时间文字
         */
//        mPaint.setColor(mTimeTextColor);
//        canvas.drawText(mTiemText,getWidth() / 2 - mBound.width() / 2,getMeasuredHeight()/6-mBound.height(),mPaint);
        drawTimeText(canvas);
//        /**
//         * 绘制外圆
//         */
//        mPaint.setColor(mOuterColor);
//        canvas.drawCircle(getMeasuredWidth()/2,getMeasuredHeight()/2,mOuterWidth/2,mPaint);
        /**
         * 绘制录音按钮
         */
        mPaint.setColor(mTouchColor);
        //绘制圆形
        canvas.drawCircle(getMeasuredWidth()/2,getMeasuredHeight()/2,mRecordWidth/2,mPaint);
        /**
         * 绘制文字
         */
        mPaint.setColor(mTitleTextColor);
        canvas.drawText(mTitleText, getWidth() / 2 - mBound.width() / 2, getHeight() / 2 + mBound.height() / 2, mPaint);
    }

    /**
     * 画时间文字
     *
     * @param canvas
     */
    protected void drawTimeText(Canvas canvas) {
            mPaint.setColor(mTimeTextColor);
            mPaint.setStrokeWidth(1);
            mPaint.setTextSize(mTimeTextSize);
            String timeText = "";
            int minute = recordTime / 1000 / 60;
            if (minute < 10) {
                timeText = "0" + minute;
            } else {
                timeText = "" + minute;
            }
            int seconds = recordTime / 1000 % 60;
            if (seconds < 10) {
                timeText = timeText + ":0" + seconds;
            } else {
                timeText = timeText + ":" + seconds;
            }
            int millisecond = recordTime % 1000 / 100;
            timeText = timeText + "." + millisecond;
            float textWidth = mPaint.measureText(timeText);
            canvas.drawText(mTiemText,getWidth() / 2 - textWidth / 2,getMeasuredHeight()/6-mBound.height(),mPaint);
//            canvas.drawText(timeText, viewHeight / 2 + textWidth / 2, viewHeight / 2 + textSize / 3, textPaint);
    }

    public void startRecord(String path){
        // 调用录音机开始录制音频
        int recordError = Recorder.getInstance().startRecordVoice(path);
        if(recordError == Recorder.ERROR_NONE){
            // 开始录音
            // 初始化开始录制时间
            startTime = DateUtil.getCurrentMillisecond();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (Recorder.getInstance().isRecording()) {
                        // 睡眠 100 毫秒，
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        recordTime = getRecordTime();
                        // MLLog.i("麦克风监听声音分贝：%d", decibel);
                        postInvalidate();
                    }
                }
            }).start();
        } else if (recordError == Recorder.ERROR_RECORDING) {
            // 录音进行中
        } else if (recordError == Recorder.ERROR_SYSTEM) {
            if (mRecordCallback != null) {
                // 媒体录音器准备失败，调用取消
                Recorder.getInstance().cancelRecordVoice();
                mRecordCallback.onFailed(recordError);
            }
        } else {
            if (mRecordCallback != null) {
                // 录音开始
                mRecordCallback.onStart();
            }
        }
    }

    /**
     * 获取录音持续时间
     *
     * @return
     */
    private int getRecordTime() {return (int) (DateUtil.getCurrentMillisecond() - startTime);}

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        switch (action){
            //表示用户开始
            case MotionEvent.ACTION_DOWN:
                if(x > getMeasuredWidth()/2-mRecordWidth/2 && x < getMeasuredWidth()/2+mRecordWidth/2 &&
                        y > getMeasuredHeight()/2-mRecordWidth/2 && y < getMeasuredHeight()/2+mRecordWidth/2){
                    isDown = true;
                    mTimeTextColor = 0xffff0000;
                    mTouchColor = 0xffff0000;
                    startRecord(null);
                    postInvalidate();
                    Log.e("action1",event.getAction()+"");
                }

                break;

            //表示用户在移动
            case MotionEvent.ACTION_MOVE:
                if(isDown){
                    if(x < getMeasuredWidth()/2-mRecordWidth/2 || x > getMeasuredWidth()/2+mRecordWidth/2 ||
                            y < getMeasuredHeight()/2-mRecordWidth/2 || y > getMeasuredHeight()/2+mRecordWidth/2){
                        mTimeTextColor = 0xffffffff;
                        mTouchColor = 0xffffffff;
                        mBackgroundColor = 0xffff0000;
                        mTitleTextColor = 0xffff0000;
                        postInvalidate();
                        Log.e("action3",event.getAction()+"");
                    }else{
                        mTimeTextColor = 0xffff0000;
                        mTouchColor = 0xffff0000;
                        mBackgroundColor = 0xfff2f2f2;
                        mTitleTextColor = 0xffffffff;
                        postInvalidate();
                    }
                }
                break;

            //表示用户抬起手指
            case MotionEvent.ACTION_UP:
                isDown = false;
                mTimeTextColor = 0xff000000;
                mTouchColor = 0xff00ba6e;
                mBackgroundColor = 0xfff2f2f2;
                mTitleTextColor = 0xffffffff;
                postInvalidate();
                Log.e("action2",event.getAction()+"");
                break;
        }
        return true;
    }

    /**
     * 设置录音回调
     *
     * @param callback
     */
    public void setRecordCallback(MLRecordCallback callback) {
        mRecordCallback = callback;
    }

    /**
     * 录音控件的回调接口，用于回调给调用者录音结果
     */
    public interface MLRecordCallback {

        /**
         * 录音取消
         */
        public void onCancel();

        /**
         * 录音失败
         *
         * @param error 失败的错误信息
         */
        public void onFailed(int error);

        /**
         * 录音开始
         */
        public void onStart();

        /**
         * 录音成功
         *
         * @param path 录音文件的路径
         * @param time 录音时长
         */
        public void onSuccess(String path, int time);

    }
}
