package com.wzzc.NextIndex.views.e.other_activity.BusinessShop.money.commission.detail.main_view;

import android.content.Context;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zcyb365 on 2017/1/4.
 */
public class ShopDetailedTwoView extends BaseView {
    @ViewInject(R.id.txt_time)
    private TextView txt_time;
    @ViewInject(R.id.txt_money)
    private TextView txt_money;
    public ShopDetailedTwoView(Context context) {
        super(context);
    }

    public void setInfo(JSONObject[] data) {
        try {
            txt_time.setText("账户变动时间：" + data[0].getString("change_time"));
            txt_money.setText("￥"+data[0].getString("commission")+"元");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
