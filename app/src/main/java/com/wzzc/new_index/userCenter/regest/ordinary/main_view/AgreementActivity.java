package com.wzzc.new_index.userCenter.regest.ordinary.main_view;

import android.os.Bundle;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_activity.web.main_view.WebBrowser;
import com.wzzc.zcyb365.R;

/**
 * Created by zcyb365 on 2016/12/2.
 */
public class AgreementActivity extends BaseActivity {

    @ViewInject(R.id.main_webview)
    public WebBrowser main_webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main_webview.LoadURL("http://test.zcgj168.com/app_agreement.php");
    }

}
