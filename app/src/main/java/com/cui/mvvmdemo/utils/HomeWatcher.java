package com.cui.mvvmdemo.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.cui.lib.util.DeviceUtil;


/**
 *
 * @author xujiangang
 * @date 2017/11/1
 */

public class HomeWatcher {
    private static final String TAG = "HomeWatcher";
    private Context mContext;
    private IntentFilter mIntentFilter;
    private OnHomePressedListener mHomePressedListener;
    private InnerReceiver mReceiver;

    // 回调接口
    public interface OnHomePressedListener {
        /**
         * 监听Home键短按
         */
        void onHomePressed();

        /**
         * 监听Home键长按
         */
        void onHomeLongPressed();

    }

    public HomeWatcher(Context context) {
        mContext = context;
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
    }

    /**
     * 设置监听
     *
     * @param listener
     */
    public void setOnHomePressedListener(OnHomePressedListener listener) {
        mHomePressedListener = listener;
        mReceiver = new InnerReceiver();
    }

    /**
     * 开始监听，注册广播
     */
    public void startWatch() {
        if (mReceiver != null) {
            mContext.registerReceiver(mReceiver, mIntentFilter);
            //XLog.e(TAG, "start watch CLOSE_SYSTEM_DIALOGS");
        }
    }

    /**
     * 停止监听，注销广播
     */
    public void stopWatch() {
        if (mReceiver != null) {
            mContext.unregisterReceiver(mReceiver);
            mReceiver = null;
            //XLog.e(TAG, "stop watch CLOSE_SYSTEM_DIALOGS");
        }
    }

    /**
     * 广播接收者
     */
    class InnerReceiver extends BroadcastReceiver {
        final String SYSTEM_DIALOG_REASON_KEY = "reason";
        final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);

                if (reason != null && DeviceUtil.isFastClick()) {
                    Log.i(TAG, "action:" + action + ",reason:" + reason);
                    if (mHomePressedListener != null) {
                        if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                            // 短按home键
                            mHomePressedListener.onHomePressed();
                        } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                            // 长按home键
                            mHomePressedListener.onHomeLongPressed();
                        }
                    }
                }
            }
        }
    }
}