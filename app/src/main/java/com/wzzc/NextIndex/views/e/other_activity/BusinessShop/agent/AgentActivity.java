package com.wzzc.NextIndex.views.e.other_activity.BusinessShop.agent;

import android.os.Bundle;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_activity.web.main_view.WebBrowser;
import com.wzzc.zcyb365.R;

/**
 * Created by zcyb365 on 2016/11/23.
 *
 * 代理商查询
 */
public class AgentActivity extends BaseActivity {
    @ViewInject(R.id.main_webview)
    public WebBrowser main_webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main_webview.LoadURL("http://test.zcgj168.com/mobile/daili.php");
        main_webview.loadUrl("javascript:function setTop(){document.querySelector('id=ads').style.display=\"none\";}setTop();");
    }


}
