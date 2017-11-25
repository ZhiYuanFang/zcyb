package com.wzzc.NextIndex.views.e.other_activity.BusinessShop.invoice.main_view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.OnClick;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.NextIndex.views.e.other_activity.BusinessShop.invoice.InvoiceActivity;
import com.wzzc.NextIndex.views.e.other_activity.BusinessShop.invoice.main_view.delivery.DeliveryActivity;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zcyb365 on 2016/11/25.
 *
 * 发货单item
 *
 */
public class InvoiceView extends BaseView {

    //region 组件
    @ViewInject(R.id.lab_invoice)
    private TextView lab_invoice;
    @ViewInject(R.id.lab_number)
    private TextView lab_number;
    @ViewInject(R.id.lab_time)
    private TextView lab_time;
    @ViewInject(R.id.lab_address)
    private TextView lab_address;
    @ViewInject(R.id.lab_time1)
    private TextView lab_time1;
    @ViewInject(R.id.lab_state)
    private TextView lab_state;
    @ViewInject(R.id.tv_name)
    private TextView lab_name;
    @ViewInject(R.id.lab_dianji)
    private LinearLayout lab_dianji;
    //endregion

    public static final String DELIVERYID = "id";
    public int delivery_id;

    public InvoiceView(Context context) {
        super(context);
        lab_dianji.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(DELIVERYID, delivery_id);
                GetBaseActivity().AddActivity(DeliveryActivity.class, 0, intent);
            }
        });
    }

    public void setInfo(JSONObject data) {
        try {
            delivery_id = data.getInt("delivery_id");
            lab_invoice.setText("发货单流水号：" + data.getString("delivery_sn"));
            lab_address.setText("收货人：" + data.getString("consignee"));
            lab_time1.setText("发货时间：" + data.getString("update_time"));
            lab_name.setText("操作人：" + data.getString("action_user"));
            lab_number.setText("订单号：" + data.getString("order_sn"));
            lab_time.setText("下单时间：" + data.getString("add_time"));
            lab_state.setText("发货单状态：" + data.getString("status_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.lab_see, R.id.lab_delete})
    public void btn_bottom_click(Button view) {
        int tag = Integer.parseInt(view.getTag().toString());
        if (tag == 1) {
            Intent intent = new Intent();
            intent.putExtra("id", delivery_id);
            GetBaseActivity().AddActivity(DeliveryActivity.class, 0, intent);
        } else if (tag == 2) {
            JSONObject para = new JSONObject();
            JSONObject sender = Default.GetSession();
            try {
                JSONObject filter = new JSONObject();
                filter.put("uid", sender.getString("uid"));
                filter.put("sid", sender.getString("sid"));
                para.put("session", filter);
                para.put("delivery_id", delivery_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            AsynServer.BackObject(GetBaseActivity() , "seller/delivery_list", para, new AsynServer.BackObject() {
                @Override
                public void Back(JSONObject sender) {
                    GetBaseActivity().AddActivity(InvoiceActivity.class);
                    GetBaseActivity().finish();
                }
            });
        }

    }

}
