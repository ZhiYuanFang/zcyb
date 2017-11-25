package com.wzzc.onePurchase.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.OnClick;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/3/22.
 */

public class OnePurchasePanelOperationButtonLayoutView extends BaseView {
    @ViewInject(R.id.bt_addToCart)
    private Button bt_addToCart;
    @ViewInject(R.id.bt_buyNow)
    private Button bt_buyNow;
    private String goods_id;

    public OnePurchasePanelOperationButtonLayoutView(Context context) {
        super(context);
        init();
    }

    public OnePurchasePanelOperationButtonLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setInfo(String goods_id) {
        this.goods_id = goods_id;
    }

    private void init() {
        bt_addToCart.setOnClickListener(addToCart());
        bt_buyNow.setOnClickListener(buyNow());
    }

    public OnClickListener buyNow() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                Default.showToast("立即购买产品 " + goods_id, Toast.LENGTH_SHORT);
                // TODO: 2017/3/22 立即购买产品
            }
        };
    }

    public OnClickListener addToCart() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                Default.showToast("将产品 " + goods_id + " 加入购物车", Toast.LENGTH_SHORT);
                // TODO: 2017/3/22 将产品加入购物车
            }
        };
    }
}
