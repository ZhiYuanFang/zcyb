package com.wzzc.NextIndex.views.e.other_activity.order.main_view.myOrder.main_view.main.main_view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zcyb365 on 2016/11/7.
 */
public class DetailsView extends BaseView {

    @ViewInject(R.id.img_view)
    private RelativeLayout img_view;
    @ViewInject(R.id.tv_name)
    private TextView lab_name;
    @ViewInject(R.id.lab_money)
    private TextView lab_money;
    @ViewInject(R.id.lab_number)
    private TextView lab_number;
    private ExtendImageView img_view_1;
    public DetailsView(Context context) {
        super(context);
        img_view_1 = ExtendImageView.Create(img_view);
    }
    public DetailsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void setInfo(JSONObject[] data) {
        try {
            lab_name.setText(data[0].getString("goods_name"));
            lab_money.setText(data[0].getString("goods_price"));
            lab_number.setText("x"+data[0].getString("goods_number"));
            img_view_1.setPath(data[0].getString("goods_thumb"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
