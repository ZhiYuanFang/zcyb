package com.wzzc.welcome.JavaBean;

import com.wzzc.base.Default;

/**
 * Created by zcyb365 on 2016/12/26.
 */
public class GetServerUrl {
    static String url = "http://www.zcyb365.com/zcybapp/version_android.json";   //正式版
    static String url_debug = "http://www.zcyb365.com/zcybapp/version_android_test.json";   //测试版

    public static String getUrl() {
        if (Default.debug) {
            return url_debug;
        } else
            return url;
    }
}
