package com.cui.mvvmdemo.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cui.mvvmdemo.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Hashtable;

/**
 * Created by qingchao.cui on 2019/3/22.
 */

public class ImageUtil {
    public static RequestOptions getOption(){
        RequestOptions options = new RequestOptions();
        options.fitCenter();
//        options.override(1920);
        options.format(DecodeFormat.PREFER_RGB_565);
//        if(QfAdTvApp.Companion.getApp().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            options.error(R.mipmap.glide_loading_error_land);
            options.placeholder(R.mipmap.zhaoxi);
//        } else if(QfAdTvApp.Companion.getApp().getResources().getConfiguration().orientation ==Configuration.ORIENTATION_PORTRAIT) {
//            options.error(R.mipmap.glide_loading_error_port);
//            options.placeholder(R.mipmap.glide_loading_port);
//        }
//        options.dontAnimate();
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        return options;
    }

    public static RequestOptions getOptionForQrCode(){
        RequestOptions options = new RequestOptions();
        options.fitCenter();
        options.format(DecodeFormat.PREFER_RGB_565);
        options.override(160,160);
//        options.dontAnimate();
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        return options;
    }

    public static RequestOptions getOptionForVR(){
        RequestOptions options = new RequestOptions();
//        options.fitCenter();
//        options.override(1920,960);
//        options.format(DecodeFormat.PREFER_RGB_565);
//        options.dontAnimate();
//        options.skipMemoryCache(true);
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        return options;
    }

    public static RequestOptions getOptionForVRItem(){
        RequestOptions options = new RequestOptions();
        options.fitCenter();
        options.override(120);
        options.format(DecodeFormat.PREFER_RGB_565);
//        options.dontAnimate();
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        return options;
    }


    public static Bitmap compressMatrix(Bitmap bm) {
        Matrix matrix = new Matrix();
        matrix.setScale(0.5f, 0.5f);
        Bitmap bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        bm = null;
        return bitmap;
    }

    public static void releaseImageViewResouce(ImageView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }

    public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }

}
