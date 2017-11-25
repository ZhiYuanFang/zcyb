package com.wzzc.NextIndex.views.e.other_activity.order.main_view.myOrder.main_view.main.main_view.main_view.main_view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by toutou on 2017/4/12.
 *
 * 生成发货单详情预览商品item
 */

public class DeliveryGoodsView extends BaseView {

    //region 组件
    @ViewInject(R.id.img_icon)
    private RelativeLayout img_icon;
    @ViewInject(R.id.tv_goods_name)
    private TextView tv_goods_name;
    @ViewInject(R.id.tv_goods_sn)
    private TextView tv_goods_sn;
    @ViewInject(R.id.tv_extension_code)
    private TextView tv_extension_code;
    @ViewInject(R.id.tv_goods_attr)
    private TextView tv_goods_attr;
    @ViewInject(R.id.tv_goods_number)
    private TextView tv_goods_number;
    @ViewInject(R.id.tv_is_real)
    private TextView tv_is_real;
    @ViewInject(R.id.tv_is_gift)
    private TextView tv_is_gift;
    @ViewInject(R.id.et_number)
    private EditText et_number;
    //endregion

    private String rec_id;
    public DeliveryGoodsView(Context context) {
        super(context);
        init();
    }

    private void init () {
        et_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() < 1) {
                    et_number.setText("1");
                }
            }
        });
    }

    public void setInfo (JSONObject json) {
        try {
            rec_id = json.getString("rec_id");
            ExtendImageView.Create(img_icon).setPath(json.getString("goods_thumb"));
            tv_goods_name.setText(json.getString("goods_name"));
            tv_goods_sn.setText("货号:" + json.getString("goods_sn"));
            tv_extension_code.setText("货品号:" + json.getString("extension_code"));
            tv_goods_attr.setText("属性:" + json.get("goods_attr"));
            tv_goods_number.setText("库存:" + json.get("goods_number"));
            tv_goods_number.setVisibility(GONE);
            tv_is_real.setText("数量:" + json.get("is_real"));
            tv_is_gift.setText("已发货数量:" + json.get("is_gift"));
            et_number.setText("1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getSendNumber () {

        return rec_id + ":" + et_number.getText();
    }

}
