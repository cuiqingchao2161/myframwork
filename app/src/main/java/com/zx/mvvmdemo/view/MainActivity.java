package com.zx.mvvmdemo.view;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zx.mvvmdemo.R;
import com.zx.mvvmdemo.adapter.NewAdapter;
import com.zx.mvvmdemo.adapter.NewsAdapter;
import com.zx.mvvmdemo.bean.NewsData;
import com.zx.mvvmdemo.bean.SimpleNewsBean;
import com.zx.mvvmdemo.databinding.ActivityMainBinding;
import com.zx.mvvmdemo.helper.DialogHelper;
import com.zx.mvvmdemo.utils.ToastUtils;
import com.zx.mvvmdemo.viewmodel.MainVM;
import com.zx.mvvmdemo.viewmodel.NewsItemClickCallback;
import com.zx.mvvmdemo.viewmodel.NewsVM;
import com.zx.mvvmdemo.viewmodel.TestLifecycleObserver;

import java.util.List;

import static com.zx.mvvmdemo.constant.MainConstant.LoadData.FIRST_LOAD;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private ActivityMainBinding binding;
    private NewsAdapter newsAdapter; //新闻列表的适配器
    private NewAdapter newAdapter; //新闻列表的适配器
    private NewsVM newsVM;
    private TestLifecycleObserver testLifecycleObserver;
    private LifecycleRegistry mLifecycleRegistry;
    private MainVM mainVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mContext = this;
        initRecyclerView();

//        mLifecycleRegistry = new LifecycleRegistry(this);
//        this.mLifecycleRegistry.markState(android.arch.lifecycle.Lifecycle.State.CREATED);
//        testLifecycleObserver = new TestLifecycleObserver();
//        getLifecycle().addObserver(testLifecycleObserver);
//        LiveData<String> liveData = new LiveData<String>() {
        mainVM = ViewModelProviders.of(this).get(MainVM.class);
        NewsVM newsVM = ViewModelProviders.of(this).get(NewsVM.class);

        newAdapter = new NewAdapter(girlItemClickCallback);
        binding.newsList.setAdapter(newAdapter);
        subscribeToModel(newsVM);
        newsVM.getData();

        newsVM.getLiveData().observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                List<SimpleNewsBean> beans = (List<SimpleNewsBean>) o;
                newsAdapter.refreshData(beans);
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
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        binding.newsRv.setRefreshProgressStyle(ProgressStyle.BallClipRotate); //设置下拉刷新的样式
        binding.newsRv.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate); //设置上拉加载更多的样式
        binding.newsRv.setArrowImageView(R.mipmap.pull_down_arrow);
//        binding.newsRv.setLoadingListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.newsRv.setLayoutManager(layoutManager);
        newsAdapter = new NewsAdapter(this);
        binding.newsRv.setAdapter(newsAdapter);
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
                newAdapter.setGirlsList(newsData.getResults());
            }
        });
    }

//    @Override
//    public void onRefresh() {
//        //下拉刷新
//        newsVM.loadRefreshData();
//    }

//    @Override
//    public void onLoadMore() {
//        //上拉加载更多
//        newsVM.loadMoreData();
//    }
//
//    @Override
//    public void loadStart(int loadType) {
//        if (loadType == FIRST_LOAD) {
//            DialogHelper.getInstance().show(mContext, "加载中...");
//        }
//    }
//
//    @Override
//    public void loadComplete() {
//        DialogHelper.getInstance().close();
//        binding.newsRv.loadMoreComplete(); //结束加载
//        binding.newsRv.refreshComplete(); //结束刷新
//    }
//
//    @Override
//    public void loadFailure(String message) {
//        DialogHelper.getInstance().close();
//        binding.newsRv.loadMoreComplete(); //结束加载
//        binding.newsRv.refreshComplete(); //结束刷新
//        ToastUtils.show(mContext, message);
//    }


}
