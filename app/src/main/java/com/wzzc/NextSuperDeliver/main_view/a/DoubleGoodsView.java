package com.wzzc.NextSuperDeliver.main_view.a;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.wzzc.NextSuperDeliver.Production;
import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/8/22.
 *
 */
@ContentView(R.layout.view_double_goods)
public class DoubleGoodsView extends BaseView implements View.OnClickListener{
    DoubleGoodsDelegate doubleGoodsDelegate;
    @ViewInject(R.id.gv_0)
    GoodsView gv_0;
    @ViewInject(R.id.gv_1)
    GoodsView gv_1;
    public DoubleGoodsView(Context context) {
        super(context);
        init();
    }

    public DoubleGoodsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init () {
        gv_0.setOnClickListener(this);
        gv_1.setOnClickListener(this);

    }

    public void setInfo (DoubleGoodsDelegate doubleGoodsDelegates , Production[] productions){
        this.doubleGoodsDelegate = doubleGoodsDelegates;
        if (productions[0] == null && productions[1] == null) {
            noOne();
        } else if (productions[0] != null && productions[1] == null) {
            justOne();
            gv_0.setInfo(productions[0]);
            gv_0.setTag(productions[0]);
        } else {
            all();
            gv_0.setInfo(productions[0]);
            gv_0.setTag(productions[0]);
            gv_1.setInfo(productions[1]);
            gv_1.setTag(productions[1]);
        }
    }

    private void noOne (){
        ViewGroup.LayoutParams lp_0 = gv_0.getLayoutParams();
        lp_0.height = 0;
        gv_0.setLayoutParams(lp_0);
        ViewGroup.LayoutParams lp_1 = gv_1.getLayoutParams();
        lp_1.height = 0;
        gv_1.setLayoutParams(lp_1);
    }

    private void justOne () {
        ViewGroup.LayoutParams lp_0 = gv_0.getLayoutParams();
        lp_0.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        gv_0.setLayoutParams(lp_0);
        ViewGroup.LayoutParams lp_1 = gv_1.getLayoutParams();
        lp_1.height = 0;
        gv_1.setLayoutParams(lp_1);
    }

    private void all () {
        ViewGroup.LayoutParams lp_0 = gv_0.getLayoutParams();
        lp_0.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        gv_0.setLayoutParams(lp_0);
        ViewGroup.LayoutParams lp_1 = gv_1.getLayoutParams();
        lp_1.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        gv_1.setLayoutParams(lp_1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.gv_0:
                doubleGoodsDelegate.chooseProduction((Production) v.getTag());
                break;
            case R.id.gv_1:
                doubleGoodsDelegate.chooseProduction((Production) v.getTag());
                break;
            default:
        }
    }
}
