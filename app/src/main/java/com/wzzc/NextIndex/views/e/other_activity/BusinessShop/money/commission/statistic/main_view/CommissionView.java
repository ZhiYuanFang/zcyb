package com.wzzc.NextIndex.views.e.other_activity.BusinessShop.money.commission.statistic.main_view;

import android.content.Context;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zcyb365 on 2016/11/24.
 */
public class CommissionView extends BaseView {
    @ViewInject(R.id.lab_time)
    private TextView lab_time;
    @ViewInject(R.id.lab_number)
    private TextView lab_number;
    @ViewInject(R.id.lab_money)
    private TextView lab_money;
    @ViewInject(R.id.lab_platformfee)
    private TextView lab_platformfee;
    @ViewInject(R.id.lab_payfee)
    private TextView lab_payfee;
    @ViewInject(R.id.lab_commission)
    private TextView lab_commission;
    @ViewInject(R.id.lab_inputmoney)
    private TextView lab_inputmoney;
    @ViewInject(R.id.lab_pay)
    private TextView lab_pay;

    public CommissionView(Context context) {
        super(context);
    }

    public void setInfo(JSONObject[] data) {
        try {
            lab_time.setText("账户变动时间："+data[0].getString("change_time"));
            lab_number.setText("订单号："+data[0].getString("order_sn"));
            lab_money.setText(data[0].getString("goods_amount")+"元");
            lab_platformfee.setText(data[0].getString("rebate_money")+"元");
            lab_payfee.setText(data[0].getString("pay_fee")+"元");
            lab_commission.setText(data[0].getString("commission_amount")+"元");
            lab_inputmoney.setText(data[0].getString("commission")+"元");
            lab_pay.setText("支付方式："+data[0].getString("pay_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
