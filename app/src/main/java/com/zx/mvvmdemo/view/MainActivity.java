package com.zx.mvvmdemo.view;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zx.mvvmdemo.R;
import com.zx.mvvmdemo.adapter.NewsAdapter;
import com.zx.mvvmdemo.databinding.ActivityMainBinding;
import com.zx.mvvmdemo.helper.DialogHelper;
import com.zx.mvvmdemo.utils.ToastUtils;
import com.zx.mvvmdemo.viewmodel.MainVM;
import com.zx.mvvmdemo.viewmodel.NewsVM;
import com.zx.mvvmdemo.viewmodel.TestLifecycleObserver;

import static com.zx.mvvmdemo.constant.MainConstant.LoadData.FIRST_LOAD;

public class MainActivity extends AppCompatActivity implements INewsView, XRecyclerView.LoadingListener {
    private Context mContext;
    private ActivityMainBinding binding;
    private NewsAdapter newsAdapter; //新闻列表的适配器
    private NewsVM newsVM;
    TestLifecycleObserver testLifecycleObserver;
    private LifecycleRegistry mLifecycleRegistry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mContext = this;
        initRecyclerView();
        newsVM = new NewsVM(this, newsAdapter);

//        mLifecycleRegistry = new LifecycleRegistry(this);
//        this.mLifecycleRegistry.markState(android.arch.lifecycle.Lifecycle.State.CREATED);
//        testLifecycleObserver = new TestLifecycleObserver();
//        getLifecycle().addObserver(testLifecycleObserver);
//        LiveData<String> liveData = new LiveData<String>() {
//            @Override
//            protected void postValue(String value) {
//                super.postValue(value);
//            }
//
//            @Override
//            protected void setValue(String value) {
//                super.setValue(value);
//            }
//
//            @Override
//            protected void onActive() {
//                super.onActive();
//            }
//
//            @Override
//            protected void onInactive() {
//                super.onInactive();
//            }
//        }
//        liveData.observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//
//            }
//        });
        MainVM viewModel = ViewModelProviders.of(this).get(MainVM.class);
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        binding.newsRv.setRefreshProgressStyle(ProgressStyle.BallClipRotate); //设置下拉刷新的样式
        binding.newsRv.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate); //设置上拉加载更多的样式
        binding.newsRv.setArrowImageView(R.mipmap.pull_down_arrow);
        binding.newsRv.setLoadingListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.newsRv.setLayoutManager(layoutManager);
        newsAdapter = new NewsAdapter(this);
        binding.newsRv.setAdapter(newsAdapter);
    }

    @Override
    public void onRefresh() {
        //下拉刷新
        newsVM.loadRefreshData();
    }

    @Override
    public void onLoadMore() {
        //上拉加载更多
        newsVM.loadMoreData();
    }

    @Override
    public void loadStart(int loadType) {
        if (loadType == FIRST_LOAD) {
            DialogHelper.getInstance().show(mContext, "加载中...");
        }
    }

    @Override
    public void loadComplete() {
        DialogHelper.getInstance().close();
        binding.newsRv.loadMoreComplete(); //结束加载
        binding.newsRv.refreshComplete(); //结束刷新
    }

    @Override
    public void loadFailure(String message) {
        DialogHelper.getInstance().close();
        binding.newsRv.loadMoreComplete(); //结束加载
        binding.newsRv.refreshComplete(); //结束刷新
        ToastUtils.show(mContext, message);
    }
}
