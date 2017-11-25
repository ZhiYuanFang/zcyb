package com.wzzc.other_activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_activity.web.main_view.WebBrowser;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/5/16.
 *
 */

public class LoadWebActivity extends BaseActivity {
    @ViewInject(R.id.myWebView)
    WebBrowser myWebView;
    @ViewInject(R.id.btn_back)
    RelativeLayout layout_back;
    @ViewInject(R.id.tv_category)
    TextView tv_info;
    @ViewInject(R.id.tv_footer_message)
            TextView tv_footer_message;
    String goods_from;
    public static final String URL = "url";
    public static final String GOODS_FROM = "goods_from";
    public static final String GOODS_NAME = "goods_name";
    public static final String FOOTER_MESSAGE = "footer_message";
    public static final String TAO_BAO = "淘宝";
    public static final String BROWSE = "浏览器";
    public static final String RECHARGE = "充值中心";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        goods_from = String.valueOf(GetIntentData(GOODS_FROM));
        tv_info.setText("即将前往" + goods_from);

        startMissInfo();
    }

    private void startMissInfo () {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1,0);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tv_info.setAlpha(0);
                open(String.valueOf(GetIntentData(URL)));
                if (GetIntentData(FOOTER_MESSAGE) != null) {
                    tv_footer_message.setText(GetIntentData(FOOTER_MESSAGE).toString());
                    tv_footer_message.setVisibility(View.VISIBLE);
                } else{
                    tv_footer_message.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        tv_info.startAnimation(alphaAnimation);
    }

    private void open(String link) {
        String packageName = "";
        switch (goods_from){
            case TAO_BAO:
                packageName = "com.taobao.taobao";
                break;
            case BROWSE :
                packageName = "com.android.browser";
                break;
            case RECHARGE :
                packageName = "null";
                break;
            default:
        }
        if (Default.checkPackage(Default.getActivity(), packageName)) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            String url = "https://s.click.taobao.com/t?e=m%3D2%26s%3DFHj7gePqR1McQipKwQzePOeEDrYVVa64LKpWJ%2Bin0XK3bLqV5UHdqROSkNBmwKXs%2Fl0%2B1yuzCtIHiZhIISVP0YWrRwt6D%2FPas6tUMljaWzBu1SeLOpA%2BkfOmc4WL86KuZ%2FsjjDn37J%2BckHoapvV%2F4POOoLOUxyLSfBL5LlYL98lMUC4q7vEEl%2BqhKt9hVKIjMOXgw2GEHkSHVZGPSFeLwqJn5AyUbPoV&pvid=11_115.218.220.234_1679_1496907442410";
            url = link;
            Uri uri = Uri.parse(url);
            intent.setData(uri);
            Default.getActivity().startActivity(intent);

//            Default.launchAppDetail(packageName,"");
            finish();
        } else {
            myWebView.loadUrl(link);
        }
    }


}
