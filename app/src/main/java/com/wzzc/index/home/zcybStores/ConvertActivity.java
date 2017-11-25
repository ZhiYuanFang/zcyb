package com.wzzc.index.home.zcybStores;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.zcyb365.R;

/**
 * Created by zcyb365 on 2016/12/3.
 *
 * 子城100自营店
 */
public class ConvertActivity extends BaseActivity {
    public static final String DPId = "dpid";
    @ViewInject(R.id.lab_exchange)
    private LinearLayout lab_exchange;
    @ViewInject(R.id.lab_payment)
    private LinearLayout lab_payment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddBack();
        AddTitle("子城100自营店");

        lab_exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("id", String.valueOf(GetIntentData(DPId)));
                AddActivity(GiftExchangeActivity.class,0,intent);
            }
        });
        lab_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("id", String.valueOf(GetIntentData(DPId)));
                AddActivity(PaymentActivity.class,0,intent);
            }
        });

    }
}
