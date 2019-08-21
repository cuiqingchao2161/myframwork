package com.cui.mvvmdemo.view;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.cui.mvvmdemo.R;
import com.cui.mvvmdemo.adapter.GirlsAdapter;
import com.cui.mvvmdemo.adapter.NewAdapter;
import com.cui.mvvmdemo.base.BaseActivity;
import com.cui.mvvmdemo.bean.Girl;
import com.cui.mvvmdemo.bean.NewsData;
import com.cui.mvvmdemo.databinding.ActivityMainBinding;
import com.cui.mvvmdemo.utils.NewStatusBarUtil;
import com.cui.mvvmdemo.viewmodel.GirlsVM;
import com.cui.mvvmdemo.viewmodel.MainVM;
import com.cui.mvvmdemo.viewmodel.NewsItemClickCallback;
import com.cui.mvvmdemo.viewmodel.NewsVM;
import com.cui.mvvmdemo.viewmodel.TestLifecycleObserver;

import java.util.ArrayList;
import java.util.List;


public class VirtualLocationMainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private NewAdapter newAdapter; //新闻列表的适配器
    private GirlsAdapter girlsAdapter; //新闻列表的适配器
    private TestLifecycleObserver testLifecycleObserver;
    private LifecycleRegistry mLifecycleRegistry;
    private List<Girl> mGirlList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NewStatusBarUtil.transparencyBar(this);
        setContentView( R.layout.activity_main);
        process();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mLifecycleRegistry = new LifecycleRegistry(this);
        this.mLifecycleRegistry.markState(android.arch.lifecycle.Lifecycle.State.CREATED);
        MainVM mainVM = ViewModelProviders.of(this).get(MainVM.class);
        NewsVM newsVM = ViewModelProviders.of(this).get(NewsVM.class);
        GirlsVM girlsVM = ViewModelProviders.of(this).get(GirlsVM.class);

        mGirlList = new ArrayList<>();
        initGirlsData();
        newAdapter = new NewAdapter(girlItemClickCallback);
        girlsAdapter = new GirlsAdapter(this,mGirlList);
        binding.newsList.setAdapter(girlsAdapter);


//        subscribeToModel(newsVM);
//        newsVM.getLiveData().observe(this, new Observer() {
//            @Override
//            public void onChanged(@Nullable Object o) {
//                List<SimpleNewsBean> beans = (List<SimpleNewsBean>) o;
//                newsAdapter.refreshData(beans);
//            }
//        });
//        newsVM.loadRefreshData();

    }

    private void initGirlsData(){
        Girl girl1 = new Girl("崔莹莹","模特，180cm,爱好健身跑步","file:///android_asset/1.jpg");
        Girl girl2 = new Girl("崔莹莹","模特，180cm,爱好健身跑步","file:///android_asset/2.jpg");
        Girl girl3 = new Girl("崔莹莹","模特，180cm,爱好健身跑步","file:///android_asset/3.jpg");
        Girl girl4 = new Girl("崔莹莹","模特，180cm,爱好健身跑步","file:///android_asset/4.jpg");
        Girl girl5 = new Girl("崔莹莹","模特，180cm,爱好健身跑步","file:///android_asset/5.jpg");
        Girl girl6 = new Girl("崔莹莹","模特，180cm,爱好健身跑步","file:///android_asset/6.jpg");
        Girl girl7 = new Girl("崔莹莹","模特，180cm,爱好健身跑步","file:///android_asset/7.jpg");
        Girl girl8 = new Girl("崔莹莹","模特，180cm,爱好健身跑步","file:///android_asset/8.jpg");
        Girl girl9 = new Girl("崔莹莹","模特，180cm,爱好健身跑步","file:///android_asset/9.jpg");
        Girl girl10 = new Girl("崔莹莹","模特，180cm,爱好健身跑步","file:///android_asset/10.jpg");
        Girl girl11 = new Girl("崔莹莹","模特，180cm,爱好健身跑步","file:///android_asset/11.jpg");
        mGirlList.add(girl1);
        mGirlList.add(girl2);
        mGirlList.add(girl3);
        mGirlList.add(girl4);
        mGirlList.add(girl5);
        mGirlList.add(girl6);
        mGirlList.add(girl7);
        mGirlList.add(girl8);
        mGirlList.add(girl9);
        mGirlList.add(girl10);
        mGirlList.add(girl11);
    }

    NewsItemClickCallback girlItemClickCallback = new NewsItemClickCallback() {
        @Override
        public void onClick(NewsData.ResultsBean fuliItem) {
            Toast.makeText(VirtualLocationMainActivity.this, fuliItem.getDesc(), Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onStart() {
        super.onStart();
        NewStatusBarUtil.setStatusBarColor(this,R.color.transparent);//设置状态栏颜色和顶部布局背景色一致
        NewStatusBarUtil.setStatusBarTextColor(this,true);
    }
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
//                newAdapter.setGirlsList(newsData.getResults());
            }
        });
    }


}
