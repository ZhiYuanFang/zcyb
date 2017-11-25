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
public class ShopDetailedOneView extends BaseView {
    @ViewInject(R.id.txt_time)
    private TextView txt_time;
    @ViewInject(R.id.txt_number)
    private TextView txt_number;
    @ViewInject(R.id.txt_money)
    private TextView txt_money;
    @ViewInject(R.id.txt_money1)
    private TextView txt_money1;
    @ViewInject(R.id.txt_money2)
    private TextView txt_money2;
    @ViewInject(R.id.txt_money3)
    private TextView txt_money3;
    @ViewInject(R.id.txt_money4)
    private TextView txt_money4;
    @ViewInject(R.id.txt_result)
    private TextView txt_result;

    public ShopDetailedOneView(Context context) {
        super(context);

    }

    public void setInfo(JSONObject[] data) {
        try {
            txt_time.setText("提现时间：" + data[0].getString("change_time"));
            txt_number.setText("编号：" + data[0].getString("order_sn"));
            txt_money.setText(data[0].getString("goods_amount"));
            txt_money1.setText(data[0].getString("rebate_money"));
            txt_money2.setText(data[0].getString("pay_fee"));
            txt_money3.setText(data[0].getString("commission_amount"));
            txt_money4.setText(data[0].getString("commission"));
            txt_result.setText("支付方式：" + data[0].getString("pay_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
