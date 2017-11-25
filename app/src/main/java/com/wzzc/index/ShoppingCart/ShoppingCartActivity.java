package com.wzzc.index.ShoppingCart;

import android.os.Bundle;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.index.ShoppingCart.main_view.ShoppingCartView;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/6/16.
 *
 */

public class ShoppingCartActivity  extends BaseActivity{
    public static final String TYPE = "type";
    public static final int SHOP_CART = 0;
    public static final int EXCHAGE_CART = 1;
    int type;
    @ViewInject(R.id.scv)
    ShoppingCartView scv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBackTouch();
        type = (int) GetIntentData(TYPE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        scv.setInfo(type);
    }
}
