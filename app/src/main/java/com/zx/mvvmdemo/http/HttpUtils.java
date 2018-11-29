package com.zx.mvvmdemo.http;

import android.content.Context;
import android.content.Intent;

import com.zx.mvvmdemo.bean.HeaderModel;
import com.zx.mvvmdemo.bean.SCResponseModel;
import com.zx.mvvmdemo.constant.SCConstants;
import com.zx.mvvmdemo.constant.URLConstant;
import com.zx.mvvmdemo.utils.SCUtil;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FilterInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.cache.DiskLruCache;
import okhttp3.internal.io.FileSystem;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import com.zx.mvvmdemo.http.retrofitinterface.RetrofitInterface;

public class HttpUtils {

    private static Context mContext;
    private static final int DEFAULT_TIMEOUT = 20; //连接 超时的时间，单位：秒
    private static OkHttpClient mOkHttpClient;
    private static HttpUtils me;
    private static Retrofit retrofit;
    private static RetrofitInterface retrofitInterface;
    private static Interceptor mHeaderInterceptor;
    private static Interceptor mReWriteCacheControlInterceptor;

    public static HttpUtils getInstance(){
        if(me == null){
            me = new HttpUtils();
        }
        return me;
    }
    public synchronized RetrofitInterface getRetrofit() {
        //初始化retrofit的配置
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(URLConstant.URL_BASE)
                    .client(mOkHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            retrofitInterface = retrofit.create(RetrofitInterface.class);
        }
        return retrofitInterface;
    }

    public static void init(Context context){
        mContext = context;
        mHeaderInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("Cache-Control", "max-age=0").build();
                return chain.proceed(newRequest);
            }
        };

//        loadHeaderFromCache();

        /** Dangerous interceptor that rewrites the server's cache-control header. */
        mReWriteCacheControlInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .header("Cache-Control", String.format("max-age=%d, only-if-cached, max-stale=%d", 2592000, 2592000))
                        .build();
            }
        };

        mOkHttpClient = new OkHttpClient.Builder().
                connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).
                readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).
                writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).
                cache(new Cache(mContext.getCacheDir(),20000000)).
                build();

        mOkHttpClient.interceptors().add(mHeaderInterceptor);
        mOkHttpClient.networkInterceptors().add(mReWriteCacheControlInterceptor);
    }

    /**
     * load cache header
     */
//    private void loadHeaderFromCache() {
//
//        SCUserModelDao scUserModelDao = SCDBHelper
//                .getmInstance(mContext).getmDaoSession()
//                .getSCUserModelDao();
//        List<SCUserModel> scUserModels = scUserModelDao.queryBuilder()
//                .where(SCUserModelDao.Properties.IsLogin.eq(true))
//                .list();
//        if (scUserModels == null || scUserModels.isEmpty()) {
//            ApplicationInfo appInfo = null;
//            try {
//                appInfo = mContext.getPackageManager()
//                        .getApplicationInfo(mContext.getPackageName(),
//                                PackageManager.GET_META_DATA);
//                String appInformation = appInfo.metaData
//                        .getString("SYBERCARE_APPKEY");
//                if (appInformation != null && appInformation.length() > 0) {
//                    String a[] = appInformation.split("#");
//                    if (a != null && a.length > 1) {
//                        SCHeaderModel headerModel = new SCHeaderModel();
//                        headerModel.setSyb_appId(a[0]);
//                        headerModel.setSyb_appKey(a[1]);
//                        setHeader(headerModel);
//                    }
//                }
//            } catch (PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
//            }
//        } else {
//            for (int i = 0; i < scUserModels.size(); i++) {
//                SCUserModel scUserModel = scUserModels.get(i);
//                if (null != scUserModel) {
//                    SCHeaderModelDao scHeaderModelDao = SCDBHelper
//                            .getmInstance(mContext).getmDaoSession()
//                            .getSCHeaderModelDao();
//                    List<SCHeaderModel> list = scHeaderModelDao
//                            .queryBuilder()
//                            .where(SCHeaderModelDao.Properties.Syb_operatorCode
//                                    .eq(scUserModel.getUserId())).list();
//                    if (null != list) {
//                        for (int j = 0; j < list.size(); j++) {
//                            setHeader(list.get(j));
//                        }
//                    } else {
//                        //something wrong goes here
//                    }
//                }
//            }
//        }
//
//    }

    /**
     * set header to our retrofit client
     *
     * @param scHeaderModel
     */
    public void setHeader(final HeaderModel scHeaderModel) {

        if (mHeaderInterceptor != null) {
            mOkHttpClient.interceptors().remove(mHeaderInterceptor);
        }

        mHeaderInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request.Builder builder = chain.request().newBuilder();
                builder.header("syb_consumerSeqNo", "");
                try {
                    Field[] field = scHeaderModel.getClass().getDeclaredFields();
                    for (Field f : field) {
                        String name = f.getName();
                        name = name.substring(0, 1).toUpperCase() + name.substring(1);

                        Method method = scHeaderModel.getClass()
                                .getMethod("get" + name);

                        Object value = method.invoke(scHeaderModel); //
                        if (value != null) {
                            builder.header(f.getName().toString(),
                                    value.toString());
                        }
                    }
                } catch (NoSuchMethodException e) {

                } catch (IllegalAccessException e) {

                } catch (IllegalArgumentException e) {

                } catch (InvocationTargetException e) {

                }

                Request request = builder.cacheControl(CacheControl.FORCE_NETWORK).build();
                return chain.proceed(request);

            }
        };

        mOkHttpClient.interceptors().add(mHeaderInterceptor);

    }


    /**
     * process all sybercare call. Notice:Currently, we keep our original get data style, but it's not the best. We need to do more work to improve cache performance.
     *
     * @param call
     * @param resultInterface
     * @param style
     * @param <T>
     */
    public  <T> void processCall(Call<T> call, SCResultInterface resultInterface, SCEnum.STYLE_GETDATA style) {

        CustomCallback<T> customCallback = generateCustomCallback(resultInterface);

        Request request = buildRequestFromCall(call);

        if (style == SCEnum.STYLE_GETDATA.LOCALONLY) {

            if (request.method().equals("GET")) {

                if (hasCache(request)) {

                    T cacheObject = getLocalFromCache(request, call);

                    if (cacheObject instanceof SCResponseModel) {
                        resultInterface.onSuccess(((SCResponseModel) cacheObject).getSyb_data(), SCSuccess.defaultSuccess(), null);
                    }
                } else {
                    //on local data not found
                    SCError mScError = new SCError(1000, SCConstants.SCErrorCode.get(1000)
                            .toString(), "", "");
                    resultInterface.onFailure(null, mScError, null);
                }


            } else {
                call.enqueue(customCallback);
            }

        } else if (style == SCEnum.STYLE_GETDATA.SERVERONLY) {

            if (!SCUtil.isNetworkConnected(mContext)) {

                if (request.method().equals("GET")) {

                    if (hasCache(request)) {

                        T cacheObject = getLocalFromCache(request, call);

                        if (cacheObject instanceof SCResponseModel) {
                            resultInterface.onSuccess(((SCResponseModel) cacheObject).getSyb_data(), SCSuccess.defaultSuccess(), null);
                        }

                    } else {

                        //on network failure
                        SCError mScError = new SCError(1005, SCConstants.SCErrorCode.get(1005)
                                .toString(), "", "");
                        resultInterface.onFailure(null, mScError, null);

                    }

                } else {

                    //on network failure
                    SCError mScError = new SCError(1005, SCConstants.SCErrorCode.get(1005)
                            .toString(), "", "");
                    resultInterface.onFailure(null, mScError, null);
                }

            } else {
                call.enqueue(customCallback);
            }


        } else if (style == SCEnum.STYLE_GETDATA.LOCALANDSERVER) {

            if (hasCache(request)) {

                T cacheObject = getLocalFromCache(request, call);

                if (cacheObject != null && cacheObject instanceof SCResponseModel) {
                    customCallback.setEnableCallback(true);
                    resultInterface.onSuccess(((SCResponseModel) cacheObject).getSyb_data(), SCSuccess.defaultSuccess(), null);
                } else {
                    customCallback.setEnableCallback(true);
                }

            }

            call.enqueue(customCallback);

        } else {

            throw new IllegalArgumentException("Illegal get data style");

        }

    }

    /**
     * get local data from cache
     *
     * @param request
     * @param call
     * @param <T>
     * @return
     */
    public <T> T getLocalFromCache(Request request, Call<T> call) {

        String responseStr = getResponseStr(request.url().toString());

        try {

            return (T) JSONUtil.parseObject(responseStr, getType(call));

        } catch (Exception e) {
            e.printStackTrace();
            //防止由于syb_data返回类型自动解析失败，无法对syb_status进行判断
            try {
                JSONObject jsonObject = new JSONObject(responseStr);
                if (jsonObject.getString(SCConstants.SC_STATUS).equals(SCConstants.SC_STATUS_SESSION_TOKEN_FAILED)) {
                    mContext.sendBroadcast(new Intent(SCConstants.BROADCAST_RECEIVER_LOGOUT));
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        return null;

    }


    private String getResponseStr(String url) {

        return getCachedString(url);
    }


    /***
     * Inspects an OkHttp-powered Call<T> and builds a Request
     * * @return A valid Request (that contains query parameters, right method and endpoint)
     */
    private Request buildRequestFromCall(Call baseCall) {
        try {
            //get call from ExecutorCallAdapterFactory.ExecutorCallbackCall first
            Field delegateField = baseCall.getClass().getDeclaredField("delegate");
            delegateField.setAccessible(true);
            Object trueCall = delegateField.get(baseCall);

            Field argsField = trueCall.getClass().getDeclaredField("args");
            argsField.setAccessible(true);
            Object[] args = (Object[]) argsField.get(trueCall);

            Field requestFactoryField = trueCall.getClass().getDeclaredField("requestFactory");
            requestFactoryField.setAccessible(true);
            Object requestFactory = requestFactoryField.get(trueCall);
            Method createMethod = requestFactory.getClass().getDeclaredMethod("create", Object[].class);
            createMethod.setAccessible(true);
            return (Request) createMethod.invoke(requestFactory, new Object[]{args});
        } catch (Exception exc) {
            return null;
        }
    }

    /**
     * important!!!!!
     * Used to get actual call type. Use trick
     *
     * @param baseCall
     * @return
     */
    private Type getType(Call baseCall) {

        try {
            //get call from ExecutorCallAdapterFactory.ExecutorCallbackCall first
            Field delegateField = baseCall.getClass().getDeclaredField("delegate");
            delegateField.setAccessible(true);
            Object trueCall = delegateField.get(baseCall);

            Field convertField = trueCall.getClass().getDeclaredField("responseConverter");
            convertField.setAccessible(true);
            Object responseConverter = convertField.get(trueCall);

            Field typeField = responseConverter.getClass().getDeclaredField("type");
            typeField.setAccessible(true);
            Object type = typeField.get(responseConverter);

            return (Type) type;

        } catch (Exception exc) {
            return null;
        }

    }

    /**
     * Generate custom call back to keep compatible with old SCResultInterface
     *
     * @param mResultInterface
     * @param <T>
     * @return
     */
    private <T> CustomCallback<T> generateCustomCallback(SCResultInterface mResultInterface) {

        return new CustomCallback<>(mResultInterface, mContext);

    }


    /**
     * 检查是否存在缓存
     *
     * @param request
     * @return
     */
    public boolean hasCache(Request request) {

        DiskLruCache cache = DiskLruCache.create(FileSystem.SYSTEM, mContext.getCacheDir(), 201105, 2, 20000000);
        try {
            cache.flush();
            String key = DigestUtils.md5Hex(request.url().toString());
            final DiskLruCache.Snapshot snapshot;
            try {
                snapshot = cache.get(key);
                if (snapshot == null) {
                    return false;
                }
            } catch (IOException e) {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

    public FilterInputStream getFromCache(String url) throws Exception {

        DiskLruCache cache = DiskLruCache.create(FileSystem.SYSTEM, mContext.getCacheDir(), 201105, 2, 20000000);
        cache.flush();
        String key = DigestUtils.md5Hex(url);
        final DiskLruCache.Snapshot snapshot;
        try {
            snapshot = cache.get(key);
            if (snapshot == null) {
                return null;
            }
        } catch (IOException e) {
            return null;
        }

        okio.Source source = snapshot.getSource(1);
        BufferedSource metadata = Okio.buffer(source);
        FilterInputStream bodyIn = new FilterInputStream(metadata.inputStream()) {
            @Override
            public void close() throws IOException {
                snapshot.close();
                super.close();
            }
        };
        return bodyIn;
    }

    private String getCachedString(String url) {

        Scanner sc = null;
        try {
            sc = new Scanner(getFromCache(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder str = new StringBuilder();
        String s;
        while (sc != null && sc.hasNext() && (s = sc.nextLine()) != null) {
            str.append(s);
        }

        return str.toString();

    }

}
