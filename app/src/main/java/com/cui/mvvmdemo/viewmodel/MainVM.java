package com.cui.mvvmdemo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


public class MainVM extends AndroidViewModel {
    private static final String TAG = "NewsVM";
    MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
    private int currPage = 1; //当前页数
    private int loadType; //加载数据的类型

    public MainVM(@NonNull Application application) {
        super(application);
    }

    public LiveData getLiveData(){
        return mutableLiveData;
    }

    public void loadData(){
        mutableLiveData.setValue("test");
    }
}

