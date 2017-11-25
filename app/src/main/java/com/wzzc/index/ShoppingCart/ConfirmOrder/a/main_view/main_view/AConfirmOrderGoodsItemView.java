package com.wzzc.index.ShoppingCart.ConfirmOrder.a.main_view.main_view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.production.detail.gcb.DetailGcbActivity;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/6/20.
 */

public class AConfirmOrderGoodsItemView extends BaseView{
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
    public AConfirmOrderGoodsItemView(Context context) {
        super(context);
        init();
    }

    public AConfirmOrderGoodsItemView(Context context, AttributeSet attrs) {
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
                intent.putExtra(DetailGcbActivity.GOODSID,goods_id);
                GetBaseActivity().AddActivity(DetailGcbActivity.class,0,intent);
            }
        });
    }
    /**
     * {
     "rec_id":"13523",
     "goods_id":"2500",
     "goods_sn":"003",
     "goods_name":"中包3小时欢唱+酒水套餐B（消费100送200）",
     "market_price":"￥633.00",
     "goods_price":"￥396.00",
     "goods_number":"2",
     "goods_attr":"",
     "is_real":"1",
     "extension_code":"",
     "parent_id":"0",
     "rec_type":"0",
     "is_gift":"0",
     "is_shipping":"1",
     "can_handsel":"0",
     "goods_attr_id":"",
     "package_attr_id":"",
     "exc_integral":"0",
     "pid":"2500",
     "supplier_id":"60",
     "seller":"非乐迪量贩KTV",
     "selected":1,
     "goods_price_original":"396.00",
     "market_price_original":"633.00",
     "subtotal":"￥792.00",
     "goods_thumb":"http://test.zcgj168.com/images/201612/thumb_img/2500_thumb_G_1481673912283.jpg"
     }
     * @param json
     */
    public void setInfo (JSONObject json) {
        goods_id = sj(json,"goods_id");
        eiv_image.setPath(sj(json,"goods_thumb"));
        tv_name.setText(sj(json,"goods_name"));
        tv_price.setText(sj(json,"goods_price"));
        tv_for_price.setText(sj(json,"market_price"));
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
