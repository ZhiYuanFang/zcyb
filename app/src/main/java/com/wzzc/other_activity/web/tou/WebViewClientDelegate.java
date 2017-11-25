package com.wzzc.other_activity.web.tou;

/**
 * Created by TouTou on 2016/12/7.
 */

public interface WebViewClientDelegate {
    void changeHeadToHttps() ;
    void noNetFind();
    void pageFinish();
    void pageStart();
    void changeProgress(int i);
}
