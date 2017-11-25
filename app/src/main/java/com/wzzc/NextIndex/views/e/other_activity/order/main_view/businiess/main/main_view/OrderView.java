package com.wzzc.NextIndex.views.e.other_activity.order.main_view.businiess.main.main_view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.NextIndex.views.e.other_activity.order.main_view.myOrder.main_view.main.DetailsActivity;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zcyb365 on 2016/11/4.
 */
public class OrderView extends BaseView{
    @ViewInject(R.id.lab_number)
    private TextView number;
    @ViewInject(R.id.lab_money)
    private TextView money;
    @ViewInject(R.id.lab_time)
    private TextView time;
    @ViewInject(R.id.lab_state)
    private TextView state;
    @ViewInject(R.id.btn_cancel)
    private Button cancle;
    @ViewInject(R.id.lab_detalis)
    private RelativeLayout lab_detalis;
    int id;

    public OrderView(Context context) {
        super(context);
    }

    public OrderView(Context context, AttributeSet attrs) {
        super(context, attrs);



    }
    public void setInfo(JSONObject[] data) {
        try {
            id=data[0].getInt("order_id");
            number.setText(data[0].getString("order_sn"));
            money.setText(data[0].getString("total_fee"));
            time.setText(data[0].getString("order_time"));
            state.setText(data[0].getString("order_status"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        lab_detalis.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("id",id);
                GetBaseActivity().AddActivity(DetailsActivity.class,0,intent);
            }
        });
        cancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject para = new JSONObject();
                JSONObject sender = Default.GetSession();
                try {
                    JSONObject filter = new JSONObject();
                    filter.put("uid", sender.getString("uid"));
                    filter.put("sid", sender.getString("sid"));
                    para.put("session", filter);
                    para.put("order_id", id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AsynServer.BackObject(GetBaseActivity() , "order/cancel", para, new AsynServer.BackObject() {
                    @Override
                    public void Back(JSONObject sender) {
                        GetBaseActivity().AddActivity(GetBaseActivity().getClass());
                        GetBaseActivity().finish();
                    }
                });
            }
        });
    }

}
