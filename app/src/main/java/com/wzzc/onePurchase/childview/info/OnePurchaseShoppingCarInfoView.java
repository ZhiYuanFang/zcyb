package com.wzzc.onePurchase.childview.info;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.onePurchase.OnePurchaseShoppingCarDelegate;
import com.wzzc.zcyb365.R;

/**
 * Created by toutou on 2017/3/20.
 */

public class OnePurchaseShoppingCarInfoView extends BaseView{

    protected OnePurchaseShoppingCarDelegate onePurchaseShoppingCarDelegate;
    protected String productionName , price;
    protected Integer remaindNumber;
    private Integer wantBuyNumber ,lastNumber;
    private Double priceOne;
    String goods_id;

    //region 组件
    @ViewInject(R.id.tv_productionName)
    private TextView tv_productionName;
    @ViewInject(R.id.tv_remaindNumber)
    private TextView tv_remaindNumber;
    @ViewInject(R.id.tv_coupons)
    private TextView tv_price;
    @ViewInject(R.id.tv_release)
    private TextView tv_release;
    @ViewInject(R.id.et_number)
    private EditText et_number;
    @ViewInject(R.id.tv_Add)
    private TextView tv_add;
    //endregion
    public OnePurchaseShoppingCarInfoView(Context context) {
        super(context);
        wantBuyNumber = 1;
        lastNumber = 0;
    }

    public void setInfo (OnePurchaseShoppingCarDelegate onePurchaseShoppingCarDelegate , String goods_id ,String productionName , Integer remaindNumber , String price ) {
        this.onePurchaseShoppingCarDelegate = onePurchaseShoppingCarDelegate;
        this.goods_id = goods_id;
        this.productionName = productionName;
        this.remaindNumber = remaindNumber;
        this.price = price;
        initialized();
    }

    private void initialized () {
        tv_productionName.setText(productionName);
        SpannableStringBuilder sb_number = new SpannableStringBuilder();
        SpannableString spa_number = new SpannableString(String.valueOf(remaindNumber));
        sb_number.append("剩余").append(spa_number).append("人次");
        tv_remaindNumber.setText(sb_number);
        priceOne = Double.valueOf(price.substring(1,price.length()));
        tv_price.setText(price);
        tv_release.setOnClickListener(releaseClick);
        tv_add.setOnClickListener(addClick);
        et_number.addTextChangedListener(changeListener());
        buttonChangeStage();

    }

    private void buttonChangeStage () {
        if (wantBuyNumber <= 1) {
            tv_release.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
            tv_release.setClickable(false);
        } else {
            tv_release.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Black));
            tv_release.setClickable(true);
        }

        if (wantBuyNumber >= remaindNumber) {
            tv_add.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
            tv_add.setClickable(false);
        } else {
            tv_add.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Black));
            tv_add.setClickable(true);
        }

        et_number.setText(String.valueOf(wantBuyNumber));
    }

    //region Action
    private OnClickListener releaseClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            wantBuyNumber -- ;
            buttonChangeStage();
        }
    };

    private OnClickListener addClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            wantBuyNumber ++;
            buttonChangeStage();
        }
    };
    //endregion

    //region Watcher
    private TextWatcher changeListener () {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().equals("")) {
                    return;
                }
                lastNumber = Integer.valueOf(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    return;
                }
                wantBuyNumber = Integer.valueOf(s.toString());
                if (onePurchaseShoppingCarDelegate != null) {
                    onePurchaseShoppingCarDelegate.changeTotalAmount(goods_id,(wantBuyNumber - lastNumber),priceOne * (wantBuyNumber - lastNumber));
                }
            }
        };
        return textWatcher;
    }
    //endregion
}
