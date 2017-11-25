package com.wzzc.other_activity.web.main_view;

import com.wzzc.other_activity.web.main_view.WebBrowser;

/**
 * Created by storecode on 15-11-9.
 */
public interface WebBrowserListener {

    void LoadStart(WebBrowser webBrowser);

    void LoadEnd(WebBrowser webBrowser);

    boolean NewUrl(WebBrowser webBrowser, String url);

}
