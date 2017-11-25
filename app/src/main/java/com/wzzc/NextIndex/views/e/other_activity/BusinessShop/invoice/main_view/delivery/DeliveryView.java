package com.wzzc.NextIndex.views.e.other_activity.BusinessShop.invoice.main_view.delivery;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zcyb365 on 2016/11/25.
 */
public class DeliveryView extends BaseView {

    @ViewInject(R.id.tv_name)
    private TextView lab_name;
    @ViewInject(R.id.lab_number)
    private TextView lab_number;
    @ViewInject(R.id.lab_number1)
    private TextView lab_number1;
    @ViewInject(R.id.lab_state)
    private TextView lab_state;
    @ViewInject(R.id.lab_number2)
    private TextView lab_number2;
    @ViewInject(R.id.img_icon)
    private RelativeLayout img_icon;
    private ExtendImageView img_view1;

    public DeliveryView(Context context) {
        super(context);
        img_view1 = ExtendImageView.Create(img_icon);
    }
    public void setInfo(JSONObject[] data) {
        try {
            lab_name.setText(data[0].getString("goods_name"));
            lab_number.setText(data[0].getString("goods_sn"));
            lab_number1.setText(data[0].getString("brand_name"));
            lab_state.setText(data[0].getString("goods_attr"));
            lab_number2.setText("x"+data[0].getString("send_number"));
            img_view1.setPath(data[0].getString("goods_thumb"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
