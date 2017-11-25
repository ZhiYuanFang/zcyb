package com.wzzc.NextSuperDeliver.main_view.a_b;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wzzc.NextSuperDeliver.Production;
import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/8/23.
 * 专区产品
 */
@ContentView(R.layout.brandproductionview)
public class BrandProductionView extends BaseView{
    BrandProductionDelegate brandProductionDelegate;
    @ViewInject(R.id.layout_contain)
    LinearLayout layout_contain;
    @ViewInject(R.id.eiv_icon)
    ExtendImageView eiv_icon;
    @ViewInject(R.id.tv_from)
    TextView tv_from;
    @ViewInject(R.id.tv_productionName)
    TextView tv_productionName;
    @ViewInject(R.id.tv_price)
    TextView tv_price;
    @ViewInject(R.id.tv_coupon)
    TextView tv_coupon;
    public BrandProductionView(Context context) {
        super(context);
        init();
    }

    public BrandProductionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init () {
        layout_contain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                brandProductionDelegate.choose((Production) v.getTag());
            }
        });
    }

    public void setInfo (BrandProductionDelegate brandProductionDelegate , Production production) {
        this.brandProductionDelegate  = brandProductionDelegate;
        eiv_icon.radio = Default.dip2px(3,getContext());
        eiv_icon.setPath(production.getGoods_thumb());
        tv_from.setText(production.getGoods_from());
        tv_productionName.setText(production.getGoods_name());
        tv_price.setText("￥"+production.getShop_price());
        tv_coupon.setText(production.getCoupon_info());
        layout_contain.setTag(production);
    }
}
