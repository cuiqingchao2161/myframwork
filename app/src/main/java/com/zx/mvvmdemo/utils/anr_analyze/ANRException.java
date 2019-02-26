package com.zx.mvvmdemo.utils.anr_analyze;

import android.os.Looper;

/**
 * Created by dapen on 2017/11/8.
 */

public class ANRException extends RuntimeException {
    public ANRException() {
        super("应用程序无响应，快来改BUG啊！！");
        Thread mainThread = Looper.getMainLooper().getThread();
        setStackTrace(mainThread.getStackTrace());
    }
}
