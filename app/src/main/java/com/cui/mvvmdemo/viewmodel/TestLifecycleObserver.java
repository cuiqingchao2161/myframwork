package com.cui.mvvmdemo.viewmodel;


import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * Created by Administrator on 2018/11/21.
 */

public class TestLifecycleObserver implements LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void start(){
        // 启动定位服务
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void stop(){
        // 停止定位服务
    }
}
