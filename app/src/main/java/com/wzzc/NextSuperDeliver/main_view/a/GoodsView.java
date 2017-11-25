package com.wzzc.NextSuperDeliver.main_view.a;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.wzzc.NextSuperDeliver.Production;
import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/8/22.
 */
@ContentView(R.layout.view_goods)
public class GoodsView extends BaseView {
    @ViewInject(R.id.eiv_goods_icon)
    ExtendImageView eiv_goods_icon;
    @ViewInject(R.id.iv_high_back_icon)
    ImageView iv_high_back_icon;
    @ViewInject(R.id.tv_productionName)
    TextView tv_productionName;
    @ViewInject(R.id.tv_hasSort)
    TextView tv_hasSort;
    @ViewInject(R.id.tv_lastPrice)
    TextView tv_lastPrice;
    @ViewInject(R.id.tv_coupons)
    TextView tv_coupons;
    @ViewInject(R.id.tv_remain_day)
    TextView tv_remain_day;

    public GoodsView(Context context) {
        super(context);
    }

    public GoodsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setInfo (Production production) {
        if (production.is_best()) {
            iv_high_back_icon.setVisibility(VISIBLE);
        } else {
            iv_high_back_icon.setVisibility(GONE);
        }
        eiv_goods_icon.radio = Default.dip2px(3f,getContext());
        eiv_goods_icon.setPath(production.getGoods_thumb());
        tv_productionName.setText(production.getGoods_name());
        tv_lastPrice.setText("券后价"+production.getShop_price());
        tv_coupons.setText(production.getCoupon_price() + "元");
        tv_remain_day.setText("剩" + production.getLeft_day() + "天");
    }
}
