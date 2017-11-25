package com.wzzc.index.ShoppingCart.ConfirmOrder.b.main_view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.production.detail.zcb.DetailZcbActivity;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/6/20.
 */

public class BConfirmOrderGoodsItemView extends BaseView {
    //region ```
    @ViewInject(R.id.eiv_image)
    ExtendImageView eiv_image;
    @ViewInject(R.id.tv_name)
    TextView tv_name;
    @ViewInject(R.id.tv_coupons)
    TextView tv_price;
    @ViewInject(R.id.tv_for_price)
    TextView tv_for_price;
    @ViewInject(R.id.tv_number)
    TextView tv_number;
    @ViewInject(R.id.tv_category)
    TextView tv_category;
    //endregion
    String goods_id;
    public BConfirmOrderGoodsItemView(Context context) {
        super(context);
        init();
    }

    public BConfirmOrderGoodsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    protected void init () {
        tv_for_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/6/21 前往详情
                Intent intent = new Intent();
                intent.putExtra(DetailZcbActivity.GOODSID,goods_id);
                GetBaseActivity().AddActivity(DetailZcbActivity.class,0,intent);
            }
        });
    }

    /**
     * {
     "rec_id":"13549",
     "user_id":"13591",
     "goods_id":"9059",
     "goods_name":"【浩南】商务拉杆箱（拉丝24寸）",
     "goods_sn":"1002-24",
     "goods_number":"1",
     "exc_integral":2750,
     "market_price":"4400.00",
     "goods_price":"0.00",
     "goods_attr":Array[3],
     "is_real":"1",
     "extension_code":"",
     "parent_id":"0",
     "is_gift":"0",
     "is_shipping":"1",
     "zc_discount":"10",
     "subtotal":"2750",
     "goods_thumb":"http://www.zcyb365.com/images/201706/thumb_img/9059_thumb_G_1496440673435.jpg",
     "formated_market_price":"￥4400.00",
     "formated_goods_price":2750,
     "formated_subtotal":2750
     }
     * @param json
     */
    public void setInfo (JSONObject json) {
        goods_id = sj(json,"goods_id");
        eiv_image.setPath(sj(json,"goods_thumb"));
        tv_name.setText(sj(json,"goods_name"));
        tv_price.setText(sj(json,"exc_integral"));
        tv_for_price.setText("￥"+sj(json,"market_price"));
        try {
            Object goods_attr = json.get("goods_attr");
            StringBuilder sder = new StringBuilder();
            if (goods_attr instanceof JSONArray) {
                JSONArray jrr_attr = (JSONArray) goods_attr;
                for (int i = 0 ; i < jrr_attr.length() ; i ++){
                    sder.append(jrr_attr.getJSONObject(i).getString("value")).append("\t");
                }
            }
            tv_category.setText(sder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            tv_category.setText("");
        }
        tv_number.setText("x "+sj(json,"goods_number"));
    }


    private String sj(JSONObject json , String key){
        try {
            return json.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return "error";
        }
    }
}
