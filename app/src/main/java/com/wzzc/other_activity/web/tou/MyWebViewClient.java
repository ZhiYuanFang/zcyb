package com.wzzc.other_activity.web.tou;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by TouTou on 2016/12/7.
 */

public class MyWebViewClient extends WebViewClient {

    public WebViewClientDelegate sd;
    private int waitNum;

    public MyWebViewClient (WebViewClientDelegate sd) {
        waitNum = 0;
        this.sd = sd;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        if  (sd != null) {
            sd.noNetFind();
        }
        if (sd != null) {
            sd.changeProgress(waitNum ++);
        }

    }

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        System.out.println("received http error");
        if (sd != null) {
            sd.changeHeadToHttps();
        }
        if (sd != null) {
            sd.changeProgress(waitNum ++);
        }
        super.onReceivedHttpError(view, request, errorResponse);

    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();//等待证书
        if (sd != null) {
            sd.changeProgress(waitNum ++);
        }

    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (sd != null) {
            sd.pageFinish();
        }
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
        if (sd != null) {
            sd.pageFinish();
        }
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (sd != null) {
            sd.pageStart();
        }

    }


}
