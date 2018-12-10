package com.zx.mvvmdemo.view;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.zx.mvvmdemo.R;
import com.zx.mvvmdemo.adapter.NewAdapter;
import com.zx.mvvmdemo.bean.NewsData;
import com.zx.mvvmdemo.bean.SimpleNewsBean;
import com.zx.mvvmdemo.databinding.ActivityMainBinding;
import com.zx.mvvmdemo.viewmodel.MainVM;
import com.zx.mvvmdemo.viewmodel.NewsItemClickCallback;
import com.zx.mvvmdemo.viewmodel.NewsVM;
import com.zx.mvvmdemo.viewmodel.TestLifecycleObserver;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private NewAdapter newAdapter; //新闻列表的适配器
    private TestLifecycleObserver testLifecycleObserver;
    private LifecycleRegistry mLifecycleRegistry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mLifecycleRegistry = new LifecycleRegistry(this);
        this.mLifecycleRegistry.markState(android.arch.lifecycle.Lifecycle.State.CREATED);
        MainVM mainVM = ViewModelProviders.of(this).get(MainVM.class);
        NewsVM newsVM = ViewModelProviders.of(this).get(NewsVM.class);

        newAdapter = new NewAdapter(girlItemClickCallback);
        binding.newsList.setAdapter(newAdapter);

        newsVM.getData();
        subscribeToModel(newsVM);
        newsVM.getLiveData().observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                List<SimpleNewsBean> beans = (List<SimpleNewsBean>) o;
//                newsAdapter.refreshData(beans);
            }
        });
        newsVM.loadRefreshData();
    }

    NewsItemClickCallback girlItemClickCallback = new NewsItemClickCallback() {
        @Override
        public void onClick(NewsData.ResultsBean fuliItem) {
            Toast.makeText(MainActivity.this, fuliItem.getDesc(), Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 订阅数据变化来刷新UI
     * @param model
     */
    private void subscribeToModel(final NewsVM model){
        //观察数据变化来刷新UI
        model.getLiveObservableData().observe(this, new Observer<NewsData>() {
            @Override
            public void onChanged(@Nullable NewsData newsData) {
                Log.i("danxx", "subscribeToModel onChanged onChanged");
                model.setUiObservableData(newsData);
                newAdapter.setGirlsList(newsData.getResults());
            }
        });
    }


}
