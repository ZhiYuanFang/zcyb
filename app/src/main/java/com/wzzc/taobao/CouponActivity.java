package com.wzzc.taobao;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.TradeResult;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_activity.web.tou.MyWebView;
import com.wzzc.other_activity.web.tou.MyWebViewClient;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/7/12.
 *
 * 优惠券
 */
@ContentView(R.layout.activity_coupon)
public class CouponActivity extends BaseActivity{
    public static final String CouponUrl = "url";
    public static final String HighBackInfo = "high_back";
    @ViewInject(R.id.btn_back)
    RelativeLayout layout_back;
    @ViewInject(R.id.myWebView)
    MyWebView myWebView;
    @ViewInject(R.id.tv_info)
    TextView tv_info;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBackTouch();
        layout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (GetIntentData(CouponUrl) != null && GetIntentData(HighBackInfo) != null) {
            tv_info.setText(GetIntentData(HighBackInfo).toString());
            TaoBao.showItemFromWebView(1,myWebView, new MyWebViewClient(myWebView), new WebChromeClient(), GetIntentData(CouponUrl).toString(), new AlibcTradeCallback() {
                @Override
                public void onTradeSuccess(TradeResult tradeResult) {

                }

                @Override
                public void onFailure(int i, String s) {
                    System.out.println("error code : " + i + " error msg : " + s);
                    Default.showToast(s);
                }
            });
        }
    }
}
