package com.cui.mvvmdemo.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import com.cui.mvvmdemo.R;

import androidx.annotation.Nullable;

/**
 * description : TODO:类的作用
 * author : cuiqingchao
 * date : 2019/11/30 17:56
 */
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_layout);
    }
}
