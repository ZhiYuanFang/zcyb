package com.wzzc.NextIndex.views;

import android.content.Context;
import android.util.AttributeSet;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_activity.web.tou.MyWebView;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/8/5.
 */
@ContentView(R.layout.view_help)
public class C extends BaseView{
    //region ```
    @ViewInject(R.id.webBrowser)
    MyWebView webBrowser;
    //endregion
    public C(Context context) {
        super(context);
    }

    public C(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setInfo (String loadUrl) {
        webBrowser.loadUrl(loadUrl);
    }
}
