package com.cui.mvvmdemo.demos.drawabledemo;

import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cui.mvvmdemo.R;
import com.cui.lib.base.BaseActivity;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;

/**
 *
 */
public class TestClipDrawableActivity extends BaseActivity {
    private LayerDrawable layerDrawable;
    static String TAG = "TestClipDrawableActivity";
    static int count = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_clip_drawable_test;
    }

    @Override
    protected void initView() {
        ((TextView)findViewById(R.id.title)).setText("TestClipDrawable");
    }

    @Override
    protected void initData() {
        layerDrawable = (LayerDrawable) ((ImageView)findViewById(R.id.iv_test_clip_drawable)).getDrawable();
    }

    @Override
    protected void initListener() {
        ((ImageView)findViewById(R.id.iv_test_clip_drawable)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clip();
            }
        });
    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void refreshView() {

    }

    private void clip() {
        //level在0-10000之间，0表示完全裁剪，10000表示完全不裁剪。设置的值越大，裁剪的范围就越小。
        Random random = new Random();
        int min = 1;
        int max = 10000;
        count = 0;

        //每100ms发送一次，进度递增为1，max不设置的话默认是100
        Observable.interval(200, TimeUnit.MILLISECONDS)
                .takeUntil(new Predicate<Long>() {
                    @Override
                    public boolean test(Long aLong) throws Exception {
                        return count == 100;
                    }
                })
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        count ++;
                        int num = random.nextInt(max - min + 1) + min;
                        Log.i(TAG, "num:"+num + ",count:" + count);
                        layerDrawable.setLevel(num);
                    }
                });
    }
}
