package com.wzzc.NextIndex.views.e.other_activity.BusinessShop.invoice.main_view.delivery;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by toutou on 2016/11/25.
 *
 * 商家订单操作记录item
 */
public class Delivery1View extends BaseView {
    @ViewInject(R.id.lab_number)
    private TextView lab_number;
    @ViewInject(R.id.tv_name)
    private TextView lab_name;
    @ViewInject(R.id.lab_state)
    private TextView lab_state;
    @ViewInject(R.id.tv_note)
    private TextView tv_note;
    public Delivery1View(Context context) {
        super(context);
    }
    public void setInfo(JSONObject[] data) {
        try {
            lab_number.setText(data[0].getString("action_time"));
            lab_name.setText(data[0].getString("action_user"));
            SpannableString order_status = new SpannableString(data[0].getString("order_status"));
            if (data[0].getString("order_status").contains("取消")) {
                order_status.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.tv_Red)), 0, order_status.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            SpannableStringBuilder state = new SpannableStringBuilder();
            state.append(order_status).append(","+data[0].getString("shipping_status")+","+data[0].getString("pay_status"));
            lab_state.setText(state);
            if (data[0].getString("action_note").length() > 0) {
                tv_note.setText(data[0].getString("action_note"));
                tv_note.setVisibility(VISIBLE);
            } else {
                tv_note.setVisibility(GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
