package com.cui.mvvmdemo.ui.widgets.zoomableImageView;

import android.os.Build;
import android.view.View;

/**
 * Created by Administrator on 2018/12/15.
 */

public class Compat {
    private static final int SIXTY_FPS_INTERVAL = 16;

    Compat() {
    }

    public static void postOnAnimation(View view, Runnable runnable) {
        if(Build.VERSION.SDK_INT >= 16) {
            SDK16.postOnAnimation(view, runnable);
        } else {
            view.postDelayed(runnable, 16L);
        }

    }
}
