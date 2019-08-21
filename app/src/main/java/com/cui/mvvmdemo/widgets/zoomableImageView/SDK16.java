package com.cui.mvvmdemo.widgets.zoomableImageView;

import android.annotation.TargetApi;
import android.view.View;

/**
 * Created by Administrator on 2018/12/15.
 */

@TargetApi(16)
class SDK16 {
    SDK16() {
    }

    public static void postOnAnimation(View view, Runnable r) {
        view.postOnAnimation(r);
    }
}
