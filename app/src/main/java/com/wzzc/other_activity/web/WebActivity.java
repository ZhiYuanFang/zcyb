package com.wzzc.other_activity.web;

import android.os.Bundle;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_activity.web.main_view.WebBrowser;
import com.wzzc.other_activity.web.main_view.WebBrowserListener;
import com.wzzc.zcyb365.R;

/**
 * Created by zcyb365 on 2016/10/6.
 * 附近商铺地址
 */
public class WebActivity extends BaseActivity {

    public static final String URL = "id";
    @ViewInject(R.id.main_view)
    private WebBrowser main_view;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main_view.LoadURL(GetIntentData(URL).toString());
        main_view.setTarget(new WebBrowserListener() {
            @Override
            public void LoadStart(WebBrowser webBrowser) {

            }

            @Override
            public void LoadEnd(WebBrowser webBrowser) {

            }

            @Override
            public boolean NewUrl(WebBrowser webBrowser, String url) {
                return false;
            }
        });
    }
}
