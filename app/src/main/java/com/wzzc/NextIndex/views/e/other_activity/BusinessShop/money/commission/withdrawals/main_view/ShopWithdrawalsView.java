package com.wzzc.NextIndex.views.e.other_activity.BusinessShop.money.commission.withdrawals.main_view;

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
public class ShopWithdrawalsView extends BaseView {
    @ViewInject(R.id.txt_time)
    private TextView txt_time;
    @ViewInject(R.id.txt_number)
    private TextView txt_number;
    @ViewInject(R.id.txt_money)
    private TextView txt_money;
    @ViewInject(R.id.txt_state)
    private TextView txt_state;
    @ViewInject(R.id.txt_result)
    private TextView txt_result;

    public ShopWithdrawalsView(Context context) {
        super(context);
    }

    public void setInfo(JSONObject[] data) {
        try {
            txt_time.setText("提现时间：" + data[0].getString("add_time"));
            txt_number.setText("编号：" + data[0].getString("id"));
            txt_money.setText(data[0].getString("amount"));
            txt_state.setText(data[0].getString("is_paid"));
            txt_result.setText("处理结果：" + data[0].getString("admin_note"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
