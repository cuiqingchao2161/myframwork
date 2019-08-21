package com.cui.mvvmdemo.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void process() {
        View content = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        if (content != null) {
            content.setFitsSystemWindows(true);
        }
    }
}
