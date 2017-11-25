package com.wzzc.NextIndex.views.e.other_activity.order.main_view.businiess.main.main_view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.production.detail.gcb.DetailGcbActivity;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by toutou on 2016/11/26.
 *
 * 商家订单 产品列表 item
 */
public class OrderDetailsView extends BaseView {
    //region 组件
    @ViewInject(R.id.tv_name)
    private TextView lab_name;
    @ViewInject(R.id.lab_money)
    private TextView lab_mony;
    @ViewInject(R.id.img_icon)
    private RelativeLayout img_icon;
    //endregion
    private String goods_id;
    private ExtendImageView img_view1;
    public OrderDetailsView(Context context) {
        super(context);
        init();
    }

    private void init (){
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(DetailGcbActivity.GOODSID,goods_id);
                GetBaseActivity().AddActivity(DetailGcbActivity.class,0,intent);
            }
        });
    }
    public void setInfo(JSONObject[] data) {
        try {
            goods_id = data[0].getString("goods_id");
            lab_name.setText(data[0].getString("goods_name"));
            lab_mony.setText(data[0].getString("goods_price"));
            ExtendImageView.Create(img_icon).setPath(data[0].getString("goods_thumb"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
