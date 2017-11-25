package com.wzzc.index.home.a.main_view;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.production.list.main_view.Browse_ProductionView;
import com.wzzc.zcyb365.R;

import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/5/5.
 */
@ContentView(R.layout.view_pro2)
public class Pro2View extends BaseView {
    @ViewInject(R.id.bpv1)
    Browse_ProductionView bpv1;
    @ViewInject(R.id.bpv2)
    Browse_ProductionView bpv2;
    public Pro2View(Context context) {
        super(context);
        //region set
        bpv1.setTv_nowPriceColor(ContextCompat.getColor(getContext(), R.color.gold));
        bpv1.setProductionNameColor(ContextCompat.getColor(getContext(), R.color.tv_White));
        bpv1.setTv_nowPriceBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_gold_arc));
        bpv1.setBackGroundColor(ContextCompat.getColor(getContext(), R.color.bg_hasOK));
        //endregion
        //region set
        bpv2.setTv_nowPriceColor(ContextCompat.getColor(getContext(), R.color.gold));
        bpv2.setProductionNameColor(ContextCompat.getColor(getContext(), R.color.tv_White));
        bpv2.setTv_nowPriceBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_gold_arc));
        bpv2.setBackGroundColor(ContextCompat.getColor(getContext(), R.color.bg_hasOK));
        //endregion
    }

    public void setInfo (JSONObject sender1, JSONObject sender2, boolean zcb) {
        int color = zcb ? ContextCompat.getColor(getContext(),R.color.gold) : ContextCompat.getColor(getContext(),R.color.aqua);
        setBackgroundColor(color);
        bpv1.setInfo(sender1,zcb);
        bpv2.setInfo(sender2,zcb);
    }
}