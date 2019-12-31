package com.cui.mvvmdemo.listener;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;


/**
 * @author xujiangang
 */
public class NavigationFinishClickListener implements View.OnClickListener {

    private final Activity activity;

    public NavigationFinishClickListener(@NonNull Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        activity.onBackPressed();//和back按键保持相同处理，使用baseActivity中的转场动画
//        ActivityCompat.finishAfterTransition(activity);
    }

}
