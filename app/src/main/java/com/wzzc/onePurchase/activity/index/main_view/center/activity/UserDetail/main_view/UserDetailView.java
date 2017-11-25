package com.wzzc.onePurchase.activity.index.main_view.center.activity.UserDetail.main_view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.onePurchase.activity.index.main_view.center.activity.accountRecharge.AccountRechargeActivity;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by toutouon 2017/3/30.
 *
 * 账户明细
 */

public class UserDetailView extends BaseView implements View.OnClickListener{

    //region 组件
    @ViewInject(R.id.tv_current_balance)
    private TextView tv_current_balance;
    @ViewInject(R.id.tv_addMoney)
    private TextView tv_addMoney;
    @ViewInject(R.id.tv_shop)
    private TextView tv_shop;
    @ViewInject(R.id.tv_shopPrice)
    private TextView tv_shopPrice;
    @ViewInject(R.id.tv_enearge)
    private TextView tv_enearge;
    @ViewInject(R.id.tv_eneargePrice)
    private TextView tv_eneargePrice;
    @ViewInject(R.id.tv_time)
    private TextView tv_time;
    @ViewInject(R.id.tv_money)
    private TextView tv_money;
    @ViewInject(R.id.layout_shop)
    private LinearLayout layout_shop;
    @ViewInject(R.id.layout_enearge)
    private LinearLayout layout_enearge;
    @ViewInject(R.id.listView_detail)
    private TextView listView_detail;

    //endregion

    private String rec_id;
    private String currentBalance;
    public UserDetailView(Context context) {
        super(context);
        init();
    }
    private void init () {
        tv_addMoney.setOnClickListener(this);
        layout_shop.setOnClickListener(this);
        layout_enearge.setOnClickListener(this);
    }
    public void setInfo (JSONObject sender) {

        if (sender != null) {
            try {
                currentBalance = sender.getString(AccountRechargeActivity.CURRENT);
                rec_id = sender.getString(AccountRechargeActivity.RECID);
            } catch (JSONException e) {
                e.printStackTrace();
                nullInit ();
            }
        } else nullInit ();

        initialized();
    }

    private void nullInit () {
        currentBalance = "";
        rec_id = "";
    }
    public void initialized () {
        SpannableStringBuilder sBuilder = new SpannableStringBuilder();
        SpannableString spa = new SpannableString(currentBalance);
        spa.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(),R.color.tv_Red)),0,spa.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sBuilder.append("当前可用余额").append(spa).append("元");
        tv_current_balance.setText(sBuilder);

        layout_shop.callOnClick();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_shop:{
                tv_shop.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Red));
                tv_shopPrice.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Red));
                tv_enearge.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
                tv_eneargePrice.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
                showShopDetail();
                break;}
            case R.id.layout_enearge:{
                tv_shop.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
                tv_shopPrice.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
                tv_enearge.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Red));
                tv_eneargePrice.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Red));
                showEneargeDetail();
                break;}
            case R.id.tv_addMoney:{
                Intent intent = new Intent();
                intent.putExtra(AccountRechargeActivity.CURRENT,currentBalance);
                intent.putExtra(AccountRechargeActivity.RECID,rec_id);
                GetBaseActivity().AddActivity(AccountRechargeActivity.class,0,intent);
                break;}
            default:
        }
    }

    //region Method
    private void showShopDetail () {}

    private void showEneargeDetail () {}
    //endregion
}
