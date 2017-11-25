package com.wzzc.NextIndex.saomiao;

import android.os.Bundle;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_activity.web.main_view.WebBrowser;
import com.wzzc.zcyb365.R;

/**
 * Created by zcyb365 on 2016/12/6.
 */
public class WebviewActivity extends BaseActivity {
    @ViewInject(R.id.main_webview)
    public WebBrowser main_webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main_webview.LoadURL(String.valueOf(GetIntentData("url")));
    }

}
