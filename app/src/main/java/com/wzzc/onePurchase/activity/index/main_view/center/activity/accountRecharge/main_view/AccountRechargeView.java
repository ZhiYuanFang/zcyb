package com.wzzc.onePurchase.activity.index.main_view.center.activity.accountRecharge.main_view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.onePurchase.activity.index.main_view.center.activity.accountRecharge.AccountRechargeActivity;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.other_function.pay.PayResult;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by by Administrator on 2017/3/28.
 *
 * 账户充值
 */

public class AccountRechargeView extends BaseView implements View.OnClickListener , CompoundButton.OnCheckedChangeListener ,IWXAPIEventHandler {

    //region 组件
    @ViewInject(R.id.tv_balance)
    private TextView tv_balance;
    @ViewInject(R.id.tv_confirm)
    private TextView tv_confirm;
    @ViewInject(R.id.tv_zfb)
    private TextView tv_zfb;
    @ViewInject(R.id.tv_wx)
    private TextView tv_wx;
    @ViewInject(R.id.bt_submit)
    private TextView bt_submit;
    @ViewInject(R.id.radio_zfb)
    private RadioButton radio_zfb;
    @ViewInject(R.id.radio_wx)
    private RadioButton radio_wx;
    @ViewInject(R.id.tv_ten)
    private TextView tv_ten;
    @ViewInject(R.id.tv_twn)
    private TextView tv_twn;
    @ViewInject(R.id.tv_thirth)
    private TextView tv_thirth;
    @ViewInject(R.id.tv_hun)
    private TextView tv_hun;
    @ViewInject(R.id.tv_tun)
    private TextView tv_tun;
    @ViewInject(R.id.et_other)
    private EditText et_other;
    //endregion

    private ArrayList<TextView> arrList_number;
    private String balance;
    private String rec_id;
    /** 充值数额*/
    private Integer rechangre;
    //支付
    private String pay_id;
    private IWXAPI api;

    public AccountRechargeView(Context context) {
        super(context);
        init();
    }

    private void init () {
        arrList_number = new ArrayList<TextView>(){{
            add(tv_ten);
            add(tv_twn);
            add(tv_thirth);
            add(tv_hun);
            add(tv_tun);
        }};

        tv_ten.setOnClickListener(this);
        tv_twn.setOnClickListener(this);
        tv_thirth.setOnClickListener(this);
        tv_hun.setOnClickListener(this);
        tv_tun.setOnClickListener(this);

        radio_zfb.setOnCheckedChangeListener(this);
        radio_wx.setOnCheckedChangeListener(this);

        et_other.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    for (int i = 0 ; i < arrList_number.size() ; i ++) {
                        arrList_number.get(i).setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
                        arrList_number.get(i).setBackground(ContextCompat.getDrawable(getContext(),R.drawable.bg_angle_line));
                    }
                }

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    rechangre = Integer.valueOf(et_other.getText().toString());
                    changeConfirm(rechangre);
                }
                return false;
            }
        });

        bt_submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Default.showToast("提交 : " + rechangre + " pay_id : " + pay_id, Toast.LENGTH_SHORT);
//                payNow();
            }
        });

        tv_ten.callOnClick();
    }

    public void setInfo (JSONObject sender) {
        if (sender != null) {
            try {
                balance = sender.getString(AccountRechargeActivity.CURRENT);
                rec_id = sender.getString(AccountRechargeActivity.RECID);
            } catch (JSONException e) {
                e.printStackTrace();
                nullInit ();
            }
        } else nullInit ();

        initialized();
    }

    private void nullInit () {
        balance = "";
        rec_id = "";
    }

    private void initialized () {
        StringBuilder sBuilder = new StringBuilder();
        SpannableString spa = new SpannableString(balance);
        spa.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(),R.color.tv_Red)),0,spa.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sBuilder.append("你的当前余额").append(spa).append("元");
        tv_balance.setText(sBuilder);
    }

    //region Helper

    private void changeFocus (TextView tv) {
        for (int i = 0 ; i < arrList_number.size() ; i ++) {
            arrList_number.get(i).setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
            arrList_number.get(i).setBackground(ContextCompat.getDrawable(getContext(),R.drawable.bg_angle_line));
        }

        tv.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Red));
        tv.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.bg_circle_red));
    }

    private void changeConfirm (Integer integer) {
        SpannableString spa = new SpannableString(String.valueOf(integer));
        spa.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(),R.color.tv_Red)),0,spa.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableStringBuilder sbuilder = new SpannableStringBuilder();
        sbuilder.append("选择平台充值").append(spa).append("元");
        tv_confirm.setText(sbuilder);
    }

    //endregion

    //region Action
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        pay_id = String.valueOf(buttonView.getTag());
    }

    @Override
    public void onClick(View v) {
        TextView textView = (TextView) v;
        String str = textView.getText().toString();
        rechangre = Integer.valueOf(str.substring(0,str.length() - 1));
        changeFocus(textView);
        changeConfirm(rechangre);
    }
    //endregion

    //region Handler
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            @SuppressWarnings("unchecked")
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            /**
             对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
             */
            String resultInfo = payResult.getResult();// 同步返回需要验证的信息
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                final JSONObject para = new JSONObject();
                try {
                    para.put("order_id", 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AsynServer.BackObject(getContext() , "order/status", para, new AsynServer.BackObject() {
                    @Override
                    public void Back(JSONObject sender) {
                        JSONObject json_status = JsonClass.jj(sender,"status");
                        if (JsonClass.ij(json_status,"succeed") == 0) {
                            MessageBox.Show(JsonClass.sj(json_status,"error_desc"));
                        } else {
                            JSONObject json_data = JsonClass.jj(sender,"data");
                            if (JsonClass.ij(json_data,"status") == 0) {
                                Toast.makeText(getContext(), "支付失败", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "支付成功", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                Toast.makeText(getContext(), "支付失败", Toast.LENGTH_SHORT).show();
                // TODO: 2017/3/28 支付失败
            }
        }
    };

    //endregion

    //region Pay
    protected void payNow (){

        if (pay_id == null) {
            Default.showToast("请选择支付方式",Toast.LENGTH_SHORT);
            return;
        }

        JSONObject para = new JSONObject();
        AsynServer.BackObject(getContext() , "account/deposit", para, new AsynServer.BackObject() {
            @Override
            public void Back(final JSONObject sender) {
                try {
                    JSONObject status=sender.getJSONObject("status");
                    if (status.getInt("succeed") == 0) {
                        // TODO: 2017/3/28 失败
                    } else {
                        JSONObject jon = sender.getJSONObject("data");
                        JSONObject jon1 = jon.getJSONObject("payment");
                        if ((int) jon1.getInt("pay_id") == 3) {
                            //微信支付
                            JSONObject data = jon.getJSONObject("wx_data");
                            api = WXAPIFactory.createWXAPI(getContext(), "wxb50b03c5de52401e");
                            final PayReq req = new PayReq();
                            req.appId = data.getString("appid");
                            req.partnerId = data.getString("partnerid");
                            req.prepayId = data.getString("prepayid");
                            req.nonceStr = data.getString("noncestr");
                            req.timeStamp = data.getString("timestamp");
                            req.packageValue = data.getString("package");
                            req.sign = data.getString("sign");
                            req.extData = "app data"; // optional
                            api.sendReq(req);
                        } else {
                            final String authInfo;
                            authInfo = jon.getString("ali_data");
                            Runnable authRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    // 构造AuthTask 对象
                                    AuthTask authTask = new AuthTask((Activity) getContext());
                                    // 调用授权接口，获取授权结果
                                    Map<String, String> result = authTask.authV2(authInfo, true);
                                    Message msg = new Message();
                                    msg.obj = result;
                                    mHandler.sendMessage(msg);
                                }
                            };
                            // 必须异步调用
                            Thread authThread = new Thread(authRunnable);
                            authThread.start();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {

    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        int result = 0;

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = R.string.errcode_success;
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.errcode_cancel;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.errcode_deny;
                break;
            default:
                result = R.string.errcode_unknown;
                break;
        }
        Default.showToast(getContext().getString(result), Toast.LENGTH_LONG);
    }
    //endregion
}