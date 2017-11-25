package com.wzzc.onePurchase.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.onePurchase.OnePurchaseShoppingCarDelegate;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/3/25.
 */

public class OnePurchaseSearchTitleView extends BaseView implements View.OnClickListener , OnePurchaseShopCarNumberDelegate{

    @ViewInject(R.id.shop_car)
    private RelativeLayout shop_car;
    @ViewInject(R.id.tv_shopCarNumber)
    private TextView tv_shopCarNumber;
    @ViewInject(R.id.ibn_home)
    private ImageButton ibn_home;
    @ViewInject(R.id.ibn_search)
    private ImageButton ibn_search;
    @ViewInject(R.id.et_searchText)
    private EditText et_searchText;

    public OnePurchaseSearchTitleView(Context context) {
        super(context);
        init();
    }

    public OnePurchaseSearchTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        ibn_home.setOnClickListener(this);
        ibn_search.setOnClickListener(this);
        shop_car.setOnClickListener(this);

        et_searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    turnToProduction();
                }
                return false;
            }
        });
    }

    protected void turnToProduction () {
        String keyWords = String.valueOf(et_searchText.getText());
        if (keyWords == null) {

        }

        // TODO: 2017/3/25 传递keywords跳转到产品信息界面

        Intent intent = new Intent();
        intent.putExtra("keyWords",keyWords);
//        GetBaseActivity().AddActivity(OnePurchaseProductionInfoActivity.class,intent);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ibn_home:
                GetBaseActivity().BackActivity(0,null);
                break;
            case R.id.ibn_search:
                turnToProduction();
                break;
            case R.id.shop_car :
                // TODO: 2017/3/25 跳转到云购物车界面 OR 显示购物列表
                break;
            default:
        }
    }

    @Override
    public void changeNumber(Integer number) {
        if (number != null) {
            Integer shopNumber = tv_shopCarNumber.getText() == null ?  0 : Integer.valueOf(String.valueOf(tv_shopCarNumber.getText()));
            shopNumber += number;
            tv_shopCarNumber.setText(String.valueOf(shopNumber));
        }
    }
}
