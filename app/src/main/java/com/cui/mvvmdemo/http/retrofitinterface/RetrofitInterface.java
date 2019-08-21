package com.cui.mvvmdemo.http.retrofitinterface;

import com.cui.mvvmdemo.bean.GirlsData;
import com.cui.mvvmdemo.bean.NewsBean;
import com.cui.mvvmdemo.bean.NewsData;
import com.cui.mvvmdemo.constant.URLConstant;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface RetrofitInterface {

    //获取“分类中搜索商品”的数据
    @GET(URLConstant.URL_PATH)
    Observable<NewsBean> getNewsData();

    @GET("compile/data/Android/{size}/{index}")
    Observable<NewsData> getAndroidData(@Path("size") String size, @Path("index") String index);

    @GET  ("compile/data/福利/{size}/{index}")
    Observable<GirlsData> getFuliData(@Path("size") String size, @Path("index") String index);

//    @GET("services/RestServices/yundihealth/messageCenter/advert")
//    Call<SCResponseModel<List<SCAdvertisementModel>>> getAdvertisement(@Query("comcode") String comCode);

    @GET ("https://image.baidu.com/search/index?tn=baiduimage&ct=201326592&lm=-1&cl=2&ie=gb18030&word=%C3%C0%C5%AE%CD%BC&fr=ala&ala=1&alatpl=adress&pos=0&hs=2&xthttps=111111")
    Observable<GirlsData> getFulData();
}
