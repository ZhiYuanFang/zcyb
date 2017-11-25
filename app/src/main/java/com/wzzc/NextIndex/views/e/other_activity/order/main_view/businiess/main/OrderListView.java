package com.wzzc.NextIndex.views.e.other_activity.order.main_view.businiess.main;

import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.OnClick;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.NextIndex.views.e.other_activity.order.main_view.businiess.OrderDetailsActivity;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zcyb365 on 2016/11/24.
 *
 * 商家订单列表
 */
public class OrderListView extends BaseView {
    public int order_id;

    //region 组件
    @ViewInject(R.id.lab_number)
    private TextView lab_number;
    @ViewInject(R.id.lab_money)
    private TextView lab_money;
    @ViewInject(R.id.lab_money1)
    private TextView lab_money1;
    @ViewInject(R.id.lab_time)
    private TextView lab_time;
    @ViewInject(R.id.lab_state)
    private TextView lab_state;
    //endregion

    public OrderListView(Context context) {
        super(context);
    }

    public void setInfo(JSONObject data) {
        try {
            order_id=data.getInt("order_id");
            lab_money.setText(data.getString("total_fee"));
            lab_number.setText(data.getString("order_sn"));
            lab_time.setText(data.getString("order_time"));
            lab_state.setText(data.getString("order_status"));
            JSONObject jon=data.getJSONObject("order_info");
            lab_money1.setText(jon.getString("order_amount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.lab_dianji})
    public void btn_bottom_click(LinearLayout view){
        int tag = Integer.parseInt(view.getTag().toString());
        if (tag==0){
            Intent intent=new Intent();
            intent.putExtra(OrderDetailsActivity.ORDERID,order_id);
            GetBaseActivity().AddActivity(OrderDetailsActivity.class,0,intent);
        }
    }
}
