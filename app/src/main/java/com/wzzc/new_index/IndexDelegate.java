package com.wzzc.new_index;

import android.view.View;

/**
 * Created by by Administrator on 2017/6/24.
 */

public interface IndexDelegate {
    void showCart();
    void showCenter();
    void showHelp();
    void showHome();
    void showKefu();
    void showLogin(View view);
    void checkVersion (CheckVersionEnd checkVersionEnd);

    interface CheckVersionEnd {
        void end (String loadUrl , String current_versionNum , String new_versionNum , boolean needPublish , String updateInfo);
        void error (String error_message);
    }
}
