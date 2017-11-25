package com.wzzc.other_activity.web.tou;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.wzzc.base.Default;

/**
 * Created by TouTou on 2017/1/3.
 * <p>
 * loadUrl("file:///android_asset/html/index.html")
 * <p>
 *
 * <!DOCTYPE>
 * <html>
 * <body>
 * <input type="button" value="Say hello" onClick="showAndroidToast('Hello Android!')" />
 * </body>
 * <script type="text/javascript">
 * function showAndroidToast(toast) {
 * Android.showToast(toast);
 * }
 * </script>
 * </html>
 *
 */

public class MyWebView extends WebView implements WebViewClientDelegate{
    private String url;
    private ProgressDialog progressDialog;

    public MyWebView(Context context) {
        super(context);
        init();
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public void loadUrl(String url) {
        this.url = url;
        super.loadUrl(url);
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void init() {
        WebSettings websettings = getSettings();
        websettings.setSupportZoom(true);
        websettings.setJavaScriptEnabled(true);
        websettings.setAppCachePath(getContext().getCacheDir().getAbsolutePath());
        websettings.setAllowFileAccess(true);
        websettings.setAppCacheEnabled(true);
        websettings.setDomStorageEnabled(true);
        websettings.setDatabaseEnabled(true);
        websettings.setGeolocationEnabled(true);
        websettings.setJavaScriptCanOpenWindowsAutomatically(true);
        websettings.setAllowUniversalAccessFromFileURLs(true);
        String UserAgent = websettings.getUserAgentString();
        websettings.setUserAgentString(UserAgent + " " + Default.APPWebClient);
        setWebViewClient(new MyWebViewClient(this));
        setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(Default.AppName).setMessage(message).setPositiveButton("确定", null);
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();
                result.confirm();
                return true;
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
                super.onGeolocationPermissionsShowPrompt(origin, callback);
                callback.invoke(origin, true, false);
            }
        });
    }

    @Override
    public void changeHeadToHttps() {
        if (url != null) {
            try {
                url = url.replace("http://","https://");
                loadUrl(url);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void noNetFind() {
        dissmissProgress();
    }

    @Override
    public void pageFinish() {
        dissmissProgress();
    }

    @Override
    public void pageStart() {
        showProgress("loading..." , null);
    }

    @Override
    public void changeProgress(int i) {
        showProgress(null , i);
    }

    //region Helper

    private void showProgress (String message , Integer progress) {
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(getContext());
            }
            if (message != null) {
                progressDialog.setMessage(message);
            }
            if (progress != null) {
                progressDialog.setProgress(progress);

            }
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }catch (Exception e ){
            e.printStackTrace();
        }
    }

    private void dissmissProgress () {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
    //endregion
}
