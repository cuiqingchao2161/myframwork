package com.cui.mvvmdemo.demos.hardwareAccelerated;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cui.mvvmdemo.R;
import com.cui.lib.base.BaseActivity;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

/**
 *
 */
public class TestHardwareAcceleratedActivity extends BaseActivity {
    ImageView mIv;
    static String TAG = "TestClipDrawableActivity";
    static int count = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_clip_drawable_test;
    }

    @Override
    protected void initView() {
        ((TextView)findViewById(R.id.title)).setText("TestHardwareAcceleratedActivity");
        mIv = findViewById(R.id.iv_test_clip_drawable);
        mIv.setImageResource(R.mipmap.bg6);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initListener() {
        mIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair imagePair = new androidx.core.util.Pair<View, String>(mIv, "avatarTransform");
                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(TestHardwareAcceleratedActivity.this, imagePair).toBundle();
                Intent intent = new Intent(TestHardwareAcceleratedActivity.this,TestHardwareAcceleratedSecondActivity.class);
                TestHardwareAcceleratedActivity.this.startActivity(intent,bundle);
            }
        });
    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void refreshView() {

    }

}
