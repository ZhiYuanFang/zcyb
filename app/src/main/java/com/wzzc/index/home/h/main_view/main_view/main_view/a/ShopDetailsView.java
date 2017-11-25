package com.wzzc.index.home.h.main_view.main_view.main_view.a;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.production.detail.gcb.DetailGcbActivity;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/3/11.
 * <p>
 * 商家介绍
 */

public class ShopDetailsView extends BaseView {

    @ViewInject(R.id.eiv_image)
    private ExtendImageView eiv_image;
    @ViewInject(R.id.tv_goods_name)
    private TextView tv_name;
    @ViewInject(R.id.tv_forPrice)
    private TextView tv_forPrice;
    @ViewInject(R.id.tv_nowPrice)
    private TextView tv_nowPrice;
    @ViewInject(R.id.tv_buy)
    private TextView tv_buy;
    @ViewInject(R.id.tv_hasSold)
    TextView tv_hasSold;
    private int id;

    public ShopDetailsView(Context context) {
        super(context);
        tv_buy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(DetailGcbActivity.GOODSID, id);
                GetBaseActivity().AddActivity(DetailGcbActivity.class, 0, intent);
            }
        });
    }

    public static final String DetailA = "detailA";
    public static final String DetailB = "detailB";

    public void setInfo(JSONObject json, String detailType) throws JSONException {
        switch (detailType) {
            case DetailA:
            case DetailB: {
                id = json.getInt("goods_id");
                tv_name.setText(json.getString("goods_name"));
                tv_nowPrice.setText(json.getString("shop_price"));
                tv_forPrice.setText("本店价 : " + json.getString("market_price"));
                eiv_image.radio = GetBaseActivity().getResources().getDimension(R.dimen.RoundRadio);;
                eiv_image.setPath(json.getString("goods_img"));
                break;
            }
            default:
        }
    }
}
