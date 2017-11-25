package com.application;

import android.app.Application;
import android.app.Notification;
import android.util.Log;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;
import com.alibaba.baichuan.android.trade.model.AlibcTaokeParams;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by toutou on 2017/4/25.
 *
 * 机关注册
 */

public class StartApplication extends Application {
    private static final String p_id = "mm_115049534_0_0";
    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("start application");
        //region Jpush
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this);
//        builder.statusBarDrawable = R.drawable.jpush_notification_icon;
//        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
//                | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
        builder.notificationDefaults = Notification.DEFAULT_SOUND
//                | Notification.DEFAULT_VIBRATE
                | Notification.DEFAULT_LIGHTS;
        JPushInterface.setPushNotificationBuilder(1,builder);
        //endregion

        //region Alibc
        AlibcTradeSDK.asyncInit(this, new AlibcTradeInitCallback() {
            @Override
            public void onSuccess() {
                //初始化成功，设置相关的全局配置参数
                Log.d("AlibcTradeSDK : " , "成功");
            }

            @Override
            public void onFailure(int code, String msg) {
                //初始化失败，可以根据code和msg判断失败原因，详情参见错误说明
                Log.d("AlibcTradeSDK : " , "失败 : " + code + " " + msg);

            }
        });
        AlibcTradeSDK.setTaokeParams(new AlibcTaokeParams(p_id, null, null));
        //endregion
    }
}
