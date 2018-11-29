package com.zx.mvvmdemo.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.zx.mvvmdemo.adapter.NewsAdapter;

public class MainVM extends ViewModel {
    private static final String TAG = "NewsVM";
    MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
    private NewsAdapter mAdapter;
    private int currPage = 1; //当前页数
    private int loadType; //加载数据的类型

    public MainVM( NewsAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    public LiveData getLiveData(){
        return mutableLiveData;
    }

    public void loadData(){
        mutableLiveData.setValue("test");
    }
}

