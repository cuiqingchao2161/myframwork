package com.cui.mvvmdemo.retrofitinterface;

import com.cui.lib.net.URLConstant;
import com.cui.mvvmdemo.bean.GirlsData;
import com.cui.mvvmdemo.bean.NewsBean;
import com.cui.mvvmdemo.bean.NewsData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface RetrofitInterface {

    //获取“分类中搜索商品”的数据
    @GET(URLConstant.URL_PATH)
    Observable<NewsBean> getNewsData();

    @GET("compile/data/Android/{size}/{index}")
    Observable<NewsData> getAndroidData(@Path("size") String size, @Path("index") String index);

    @GET  ("compile/data/福利/{size}/{index}")
    Observable<GirlsData> getFuliData(@Path("size") String size, @Path("index") String index);

    @GET ("https://image.baidu.com/search/index?tn=baiduimage&ct=201326592&lm=-1&cl=2&ie=gb18030&word=%C3%C0%C5%AE%CD%BC&fr=ala&ala=1&alatpl=adress&pos=0&hs=2&xthttps=111111")
    Observable<GirlsData> getFulData();
}
