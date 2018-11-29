package com.zx.mvvmdemo.http.retrofitinterface;

import com.zx.mvvmdemo.bean.SCAdvertisementModel;
import com.zx.mvvmdemo.bean.SCResponseModel;
import com.zx.mvvmdemo.http.HttpUtils;
import com.zx.mvvmdemo.http.SCEnum;
import com.zx.mvvmdemo.http.SCResultInterface;
import java.util.List;
import retrofit2.Call;


public class RetrofitImpl {

    public void getAdvertisement(String userId,  SCResultInterface resultInterface, SCEnum.STYLE_GETDATA style) {
        Call<SCResponseModel<List<SCAdvertisementModel>>> call = HttpUtils.getInstance().getRetrofit().getAdvertisement(userId);
        HttpUtils.getInstance().processCall(call, resultInterface, style);
    }
}
