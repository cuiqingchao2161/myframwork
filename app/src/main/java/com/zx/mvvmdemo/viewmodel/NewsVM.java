package com.zx.mvvmdemo.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;

import com.zx.mvvmdemo.base.BaseLoadListener;
import com.zx.mvvmdemo.bean.NewsBean;
import com.zx.mvvmdemo.bean.NewsData;
import com.zx.mvvmdemo.bean.SimpleNewsBean;
import com.zx.mvvmdemo.constant.MainConstant;
import com.zx.mvvmdemo.http.HttpUtils;
import com.zx.mvvmdemo.http.NetUtils;
import com.zx.mvvmdemo.http.retrofitinterface.RetrofitInterface;
import com.zx.mvvmdemo.model.INewsModel;
import com.zx.mvvmdemo.model.NewsModelImpl;
import com.zx.mvvmdemo.utils.ToastUtils;
import com.zx.mvvmdemo.widgets.MyProgressDialog;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者： 周旭 on 2017年10月18日 0018.
 * 邮箱：374952705@qq.com
 * 博客：http://www.jianshu.com/u/56db5d78044d
 */

public class NewsVM extends AndroidViewModel {
    private static final String TAG = "NewsVM";
    private INewsModel mNewsModel;
    private int currPage = 1; //当前页数
    //生命周期观察的数据
    private LiveData<NewsData> mLiveObservableData;
    MutableLiveData<List<SimpleNewsBean>> mutableLiveData = new MutableLiveData<>();
//    private MyProgressDialog mProgressDislog;
    private Context mContext;
    private int loadType; //加载数据的类型
    private Application mApplication;
    private static final MutableLiveData ABSENT = new MutableLiveData();
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    //UI使用可观察的数据 ObservableField是一个包装类
    public ObservableField<NewsData> uiObservableData = new ObservableField<>();

    public NewsVM(@NonNull Application application) {
        super(application);
        mApplication = application;
        mNewsModel = new NewsModelImpl();
    }

    public LiveData getLiveData(){
        return mutableLiveData;
    }
    /**
     * LiveData支持了lifecycle生命周期检测
     * @return
     */
    public LiveData<NewsData> getLiveObservableData() {
        return mLiveObservableData;
    }
    /**
     * 第一次获取新闻数据
     */
    private void getNewsData() {
        loadType = MainConstant.LoadData.FIRST_LOAD;
        mNewsModel.loadNewsData(currPage, new BaseLoadListener<SimpleNewsBean>() {
            @Override
            public void loadSuccess(List<SimpleNewsBean> list) {
//                mProgressDislog.hideProgress();
                if (currPage > 1) {
                    //上拉加载的数据
//            mAdapter.loadMoreData(list);
                } else {
                    mutableLiveData.setValue(list);
                    //第一次加载或者下拉刷新的数据
//            mAdapter.refreshData(list);
                }
            }

            @Override
            public void loadFailure(String message) {
//                mProgressDislog.hideProgress();
                // 加载失败后的提示
                if (currPage > 1) {
                    //加载失败需要回到加载之前的页数
                    currPage--;
                }
                ToastUtils.show(mContext,message);
//        mNewsView.loadFailure(message);
            }

            @Override
            public void loadStart() {
//                mProgressDislog.showProgress();
            }

            @Override
            public void loadComplete() {
//                mProgressDislog.hideProgress();
            }
        });
    }

    public void getData(){
        mLiveObservableData = Transformations.switchMap(NetUtils.netConnected(mApplication), new android.arch.core.util.Function<Boolean, LiveData<NewsData>>() {
            @Override
            public LiveData<NewsData> apply(Boolean isNetConnected) {

                Log.i("danxx", "apply------>");
                if (!isNetConnected) {
                    return ABSENT; //网络未连接返回空
                }
                final MutableLiveData<NewsData> applyData = new MutableLiveData<>();

                HttpUtils.getInstance().getRetrofit().getAndroidData("20", "1")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<NewsData>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                mDisposable.add(d);
                            }

                            @Override
                            public void onNext(NewsData value) {
                                applyData.setValue(value);
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onComplete() {
                            }
                        });

                return applyData;
            }
        });
    }
    /**
     * 获取下拉刷新的数据
     */
    public void loadRefreshData() {
        loadType = MainConstant.LoadData.REFRESH;
        currPage = 1;
//        mNewsModel.loadNewsData(currPage, this);
    }

    /**
     * 获取上拉加载更多的数据
     */
    public void loadMoreData() {
        loadType = MainConstant.LoadData.LOAD_MORE;
        currPage++;
//        mNewsModel.loadNewsData(currPage, this);
    }

    /**
     * 设置
     * @param product
     */
    public void setUiObservableData(NewsData product) {
        this.uiObservableData.set(product);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("onCleared","========NewsViewModel--onCleared=========");
        mDisposable.clear();
    }
}

