package com.cui.mvvmdemo.utils;

import android.app.Instrumentation;
import android.view.KeyEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 *
 * @author xujiangang
 * @date 15/03/2019
 * Email: jiangang.xu@hiscene.com
 */
public class FakeKeyEventUtils {

    public static final int TOUCH_MOVE_LEFT = KeyEvent.KEYCODE_DPAD_LEFT;
    public static final int TOUCH_MOVE_RIGHT = KeyEvent.KEYCODE_DPAD_RIGHT;
    public static final int TOUCH_MOVE_UP = KeyEvent.KEYCODE_DPAD_UP;
    public static final int TOUCH_MOVE_DOWN = KeyEvent.KEYCODE_DPAD_DOWN;
    public static final int TOUCH_ENTER = KeyEvent.KEYCODE_ENTER;
    public static final int TOUCH_NAVIGATOR = 1024;//KeyEvent.KEYCODE_MENU;
    public static final int TOUCH_LEFT_EDGE_SHOW = 1025;
    public static final int TOUCH_LEFT_EDGE_HIDE = 1026;
    public static final int TOUCH_RIGHT_EDGE_SHOW = 1027;
    public static final int TOUCH_RIGHT_EDGE_HIDE = 1028;
    private static final ScheduledExecutorService fixedThreadPool = Executors.newScheduledThreadPool(1);

    public static void sendFakeKeyEvent(final int keyCode){
        fixedThreadPool.execute(() -> {
            try {
                Instrumentation inst = new Instrumentation();
                inst.sendKeyDownUpSync(keyCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
