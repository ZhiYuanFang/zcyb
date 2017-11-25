package com.wzzc.NextIndex;

import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/8/5.
 */

public interface NextDelegate  {

    void showA ();
    void showB ();
    void showC ();
    void showD ();
    void showE ();
    void scanForTDCode ();//二维码扫描
    void searchProduction (String keyWords) ;//关键字搜索
    void findCurrentLocation ();
    void changeLocation ();//改变当前位置
    void showAdvertising (JSONObject json_pop);//显示广告弹窗
    void login ();
    void checkVersion (CheckVersionEnd checkVersionEnd);//检查版本
    void downFile(final String loadUrl);
    interface CheckVersionEnd {
        void end (String loadUrl , String current_versionNum , String new_versionNum , boolean needPublish , String updateInfo);
        void error (String error_message);
    }
}
