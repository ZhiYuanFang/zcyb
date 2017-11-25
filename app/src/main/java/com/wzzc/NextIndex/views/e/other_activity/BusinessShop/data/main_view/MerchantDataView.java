package com.wzzc.NextIndex.views.e.other_activity.BusinessShop.data.main_view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.NextIndex.views.e.other_activity.BusinessShop.data.main_view.tradingarea.ShopeDateActivity;
import com.wzzc.zcyb365.R;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zcyb365 on 2017/1/5.
 */
public class MerchantDataView extends BaseView {

    @ViewInject(R.id.tv_name)
    private TextView lab_name;
    @ViewInject(R.id.lab_number)
    private TextView lab_number;
    @ViewInject(R.id.btn_see)
    private Button btn_see;
    @ViewInject(R.id.lab_lastnumber)
    private TextView lab_lastnumber;
    @ViewInject(R.id.lab_lastmoney)
    private TextView lab_lastmoney;

    int id;

    public MerchantDataView(Context context) {
        super(context);
        btn_see.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("uid",id);
                intent.putExtra("name",lab_name.getText());
                intent.putExtra("number",lab_number.getText());
                GetBaseActivity().AddActivity(ShopeDateActivity.class,0,intent);
            }
        });
    }
    public void setInfo(JSONObject[] data) {
        try {
            id=data[0].getInt("user_id");
            lab_name.setText(data[0].getString("supplier_name"));
            lab_number.setText(data[0].getString("user_name"));
            lab_lastnumber.setText(data[0].getString("order_count"));
            lab_lastmoney.setText(data[0].getString("order_amount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
