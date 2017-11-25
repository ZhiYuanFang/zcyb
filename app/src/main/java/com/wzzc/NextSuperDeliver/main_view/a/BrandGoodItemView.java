package com.wzzc.NextSuperDeliver.main_view.a;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.wzzc.NextSuperDeliver.Production;
import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/8/22.
 * 商铺产品item
 */
@ContentView(R.layout.brand_good_layout)
public class BrandGoodItemView extends BaseView {

    @ViewInject(R.id.eiv_production)
    ExtendImageView eiv_production;
    @ViewInject(R.id.tv_productionName)
    TextView tv_productionName;
    @ViewInject(R.id.tv_coupons)
    TextView tv_price;
    @ViewInject(R.id.iv_high_back)
    ImageView iv_high_back;

    public BrandGoodItemView(Context context) {
        super(context);
        init();
    }

    public BrandGoodItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init () {

    }

    public void setInfo (Production production) {
        /*if (production.is_best()) {
            iv_high_back.setVisibility(VISIBLE);
        } else{
            iv_high_back.setVisibility(GONE);
        }*/
        eiv_production.setPath(production.getGoods_thumb());
        tv_productionName.setText(production.getGoods_name());
        tv_price.setText("券后价￥" + production.getShop_price());
    }
}
