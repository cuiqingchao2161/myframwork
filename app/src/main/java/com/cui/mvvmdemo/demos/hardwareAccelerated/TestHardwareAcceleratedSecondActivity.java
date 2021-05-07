package com.cui.mvvmdemo.demos.hardwareAccelerated;

import android.widget.ImageView;

import com.cui.mvvmdemo.R;
import com.cui.lib.base.BaseActivity;

/**
 *
 */
public class TestHardwareAcceleratedSecondActivity extends BaseActivity {
    ImageView mIv;
    static String TAG = "TestClipDrawableActivity";
    static int count = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_big;
    }

    @Override
    protected void initView() {
        mIv = findViewById(R.id.iv_test_img);
    }

    @Override
    protected void initData() {
        mIv.setImageResource(R.mipmap.bg6);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void refreshView() {

    }

    private void click() {

    }
}
