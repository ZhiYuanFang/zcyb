package com.wzzc.other_activity.other.usercenter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_activity.web.main_view.WebBrowser;
import com.wzzc.zcyb365.R;

/**
 * Created by zcyb365 on 2016/11/11.
 */
public class BusinessActivity extends BaseActivity {

    @ViewInject(R.id.main_webview)
    public WebBrowser main_webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main_webview.LoadURL("http://map.zcgj168.com/mobile/shop");

        //设置WebView属性，能够执行Javascript脚本
        main_webview.getSettings().setJavaScriptEnabled(true);

        main_webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("tel:")){
                    Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse(url));
                    if (ActivityCompat.checkSelfPermission(BusinessActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    }
                    startActivity(intent);

                } else if(url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
                }
                return true;
            }});
    }

}
