package com.em.yzzdemo.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Environment;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Geri on 2016/1/11.
 * 图片处理类，压缩，转换
 */
public class BitmapUtil {

    /**
     * 根据图片路径进行压缩图片
     * @param srcPath
     * @return
     */
    public static Bitmap getimage(String srcPath,int size) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了,表示只返回宽高
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        //当前图片宽高
        float w = newOpts.outWidth;
        float h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 1024f;//这里设置高度为800f
        float ww = 1024f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            Log.e("fileupload","------原始缩放比例 --------" + (newOpts.outWidth / ww));
            be = (int)(newOpts.outWidth / ww);
            //有时会出现be=3.2或5.2现象，如果不做处理压缩还会失败
            if ((newOpts.outWidth / ww) > be) {

                be += 1;
            }
            //be = Math.round((float) newOpts.outWidth / (float) ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            Log.e("fileupload","------原始缩放比例 --------" + (newOpts.outHeight / hh));
            be = (int)(newOpts.outHeight / hh);
            if ((newOpts.outHeight / hh) > be) {

                be += 1;
            }
            //be = Math.round((float) newOpts.outHeight / (float) hh);
        }
        if (be <= 0){

            be = 1;
        }
        newOpts.inSampleSize = be;//设置缩放比例
        Log.e("fileupload","------设置缩放比例 --------" + newOpts.inSampleSize);
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap,size);//压缩好比例大小后再进行质量压缩
    }

    /**
     * 压缩图片
     * @param image
     * @param size
     * @return
     */
    private static Bitmap compressImage(Bitmap image,int size) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;

        while ((baos.toByteArray().length / 1024) >= size) {  //循环判断如果压缩后图片是否大于等于size,大于等于继续压缩
            Log.e("fileupload","------ByteArray--------" + baos.toByteArray().length / 1024);
            baos.reset();//重置baos即清空baos
            options -= 5;//每次都减少5
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            Log.e("fileupload","------压缩质量--------" + options);
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 获取最佳缩放比例
     *
     * @param actualWidth  Bitmap的实际宽度
     * @param actualHeight Bitmap的实际高度
     * @param dimension    定义压缩后最大尺寸
     * @return 返回最佳缩放比例
     */
    public static float getZoomScale(int actualWidth, int actualHeight, int dimension) {
        float scale = 1.0f;
        if (actualWidth > actualHeight) {
            scale = (float) actualWidth / dimension;
        } else {
            scale = (float) actualHeight / dimension;
        }
        return scale;
    }



}
