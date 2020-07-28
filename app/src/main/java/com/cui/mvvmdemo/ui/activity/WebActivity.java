package com.cui.mvvmdemo.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.cui.mvvmdemo.R;
import com.cui.lib.utils.WebViewHelper;

import androidx.appcompat.app.AppCompatActivity;

public class WebActivity extends AppCompatActivity {
    private WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_web);
        LinearLayout rootLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_web,null);
        mWebView = WebViewHelper.getWebView();
        rootLayout.addView(mWebView);
        setContentView(rootLayout);

        initWebConfig();

        mWebView.loadUrl("file:///android_asset/webviewtest.html");
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if(mWebView!=null && mWebView.canGoBack()){
            mWebView.goBack();
        }else {
            finish();
        }
    }

    private void initWebConfig() {
        mWebView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
//                webView.loadUrl(url);
//                Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(viewIntent);
                return false;
            }
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest
//                    webResourceRequest) {
//                return super.shouldOverrideUrlLoading(webView, webResourceRequest);
//            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler
                    sslErrorHandler, SslError sslError) {
//                super.onReceivedSslError(webView, sslErrorHandler, sslError);
                sslErrorHandler.proceed();//不验证证书
            }
        });
    }


}
