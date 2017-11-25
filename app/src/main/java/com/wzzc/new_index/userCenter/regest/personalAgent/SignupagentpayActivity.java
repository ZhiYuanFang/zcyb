package com.wzzc.new_index.userCenter.regest.personalAgent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wzzc.NextIndex.views.e.LoginActivity;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_function.MessageBox;
import com.wzzc.other_function.pay.PayFromWX;
import com.wzzc.other_function.pay.PayFromZFB;
import com.wzzc.other_view.BasicTitleView;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupagentpayActivity extends BaseActivity implements View.OnClickListener {
    public static final String Type_Key = "type_key";
    public static final String Money_Key = "money_key";

    protected BasicTitleView mTitleView;
    protected int mType;
    protected String mMoney;
    protected int mPayChannel = -1;

    @ViewInject(R.id.ok_pay_btn)
    protected TextView ok_pay_btn;
    @ViewInject(R.id.wx_layout)
    protected LinearLayout wx_layout;
    @ViewInject(R.id.zfb_layout)
    protected LinearLayout zfb_layout;
    @ViewInject(R.id.wx_selected_img)
    protected ImageView wx_selected_img;
    @ViewInject(R.id.zfb_selected_img)
    protected ImageView zfb_selected_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initBackTouch();
    }

    protected void init(){
        // 数据
        mType = (int)GetIntentData(Type_Key);
        mMoney = (String)GetIntentData(Money_Key);

        // 数据填充
        mTitleView = (BasicTitleView) findViewById(R.id.basicTitleView);
        mTitleView.setGoHomeGone();
        switch (mType){
            case 0:
                mTitleView.setTitle("淘金店主支付");
                break;
            case 1:
                mTitleView.setTitle("个人代理支付");
                break;
            default:
                break;
        }
        ok_pay_btn.setText("确认支付 "+mMoney+"元");

        // 监听
        ok_pay_btn.setOnClickListener(this);
        wx_layout.setOnClickListener(this);
        zfb_layout.setOnClickListener(this);
    }

    protected void payMoney(){
        if(mPayChannel == -1){
            Default.showToast("请先选择支付渠道", Toast.LENGTH_SHORT);
            return;
        }

        JSONObject params = new JSONObject();
        try{
            params.put("payment_id", mPayChannel);
            params.put("type", mType);
        }catch (JSONException e){
        }
        if(1 == mPayChannel){
            // 阿里支付
            aliPay(params);
        }else if(3== mPayChannel){
            // 微信支付
            wxPay(params);
        }
    }

    protected void wxPay(JSONObject params){
        PayFromWX.pay(SignupagentpayActivity.this,"user/signupAgentPayment", params, new MessageBox.MessBtnBack() {
            @Override
            public void Back(int index) {
                mTitleView.goTop();
            }
        }, new MessageBox.MessBtnBack() {
            @Override
            public void Back(int index) {
                //失败
            }
        });
    }

    protected void aliPay(JSONObject params){
        PayFromZFB.pay(SignupagentpayActivity.this, "user/signupAgentPayment", params, new MessageBox.MessBtnBack() {
            @Override
            public void Back(int index) {
                mTitleView.goTop();
            }
        }, new MessageBox.MessBtnBack() {
            @Override
            public void Back(int index) {
                //失败
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ok_pay_btn:
                payMoney();
                break;
            case R.id.wx_layout:
                mPayChannel = 3;
                wx_selected_img.setBackgroundResource(R.drawable.btn_radio_noselect1);
                zfb_selected_img.setBackgroundResource(R.drawable.btn_radio_noselect);
                break;
            case R.id.zfb_layout:
                mPayChannel = 1;
                zfb_selected_img.setBackgroundResource(R.drawable.btn_radio_noselect1);
                wx_selected_img.setBackgroundResource(R.drawable.btn_radio_noselect);
                break;
            default:
                break;
        }
    }
}