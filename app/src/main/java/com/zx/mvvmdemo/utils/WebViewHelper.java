package com.zx.mvvmdemo.utils;

import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;

import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.zx.mvvmdemo.base.MyApplication;

/**
 * Created by Administrator on 2019/1/1.
 */

public class WebViewHelper {
    private static WebView mWebView;

    public static WebView createWebView(){
        mWebView = new WebView(MyApplication.getInstance().getApplicationContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        mWebView.setLayoutParams(layoutParams);

        initConfig();

        return mWebView;
    }

    private static void initConfig() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);//渲染优先级，默认普通
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setBlockNetworkImage(false);//网络图片资源加载关闭
        webSettings.setAppCachePath("");
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//缓存模式
        webSettings.setDefaultFontSize(10);
        /**
         * MIXED_CONTENT_ALWAYS_ALLOW：允许从任何来源加载内容，即使起源是不安全的；
         * MIXED_CONTENT_NEVER_ALLOW：不允许Https加载Http的内容，即不允许从安全的起源去加载一个不安全的资源；
         * MIXED_CONTENT_COMPATIBILITY_MODE：当涉及到混合式内容时，WebView 会尝试去兼容最新Web浏览器的风格。
         * (5.0以下默认允许加载http和https混合的页面，5.0+默认禁止)
         **/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

//        mWebView.setWebViewClient(new WebViewClient(){
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
//                return true;
//            }
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
//                return super.shouldOverrideUrlLoading(webView, webResourceRequest);
//            }
//
//            @Override
//            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
//                super.onPageStarted(webView, s, bitmap);
//            }
//
//            @Override
//            public void onPageFinished(WebView webView, String s) {
//                super.onPageFinished(webView, s);
//            }
//
//            @Override
//            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
////                super.onReceivedSslError(webView, sslErrorHandler, sslError);
//                sslErrorHandler.proceed();//不验证证书
//            }
//        });
    }


    public static WebView getWebView(){
        if(mWebView==null){
            createWebView();
        }
        return mWebView;
    }
}
