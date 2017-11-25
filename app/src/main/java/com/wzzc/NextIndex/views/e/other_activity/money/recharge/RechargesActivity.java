package com.wzzc.NextIndex.views.e.other_activity.money.recharge;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wzzc.NextIndex.views.e.other_activity.order.waitSendOrder.BackOrdersActivity;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.other_function.pay.PayFromWX;
import com.wzzc.other_function.pay.PayResult;
import com.wzzc.other_view.TextLoadView;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by zcyb365 on 2016/11/12.
 * <p>
 * 金额充值确认
 */
public class RechargesActivity extends BaseActivity implements IWXAPIEventHandler {

    private IWXAPI api;
    int id;
    //region 组件
    @ViewInject(R.id.amount)
    private TextView amount;
    @ViewInject(R.id.pay_id)
    private TextView pay_id;
    @ViewInject(R.id.pay_fee)
    private TextView pay_fee;
    @ViewInject(R.id.btn_tj)
    private Button btn_tj;
    @ViewInject(R.id.loadLayout_money)
    private TextLoadView loadLayout_money;
    @ViewInject(R.id.loadLayout_method)
    private TextLoadView loadLayout_method;
    @ViewInject(R.id.loadLayout_payMoney)
    private TextLoadView loadLayout_payMoney;
    //endregion
    public static final String AMOUNT = "amount";
    public static final String USERNOTE = "user_note";
    public static final String PAYMENTID = "payment_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddBack();
        AddTitle("充值");
        loadLayout_method.setTv_text_Gravity(Gravity.CENTER_VERTICAL | Gravity.START);
        loadLayout_money.setTv_text_Gravity(Gravity.CENTER_VERTICAL | Gravity.START);
        loadLayout_payMoney.setTv_text_Gravity(Gravity.CENTER_VERTICAL | Gravity.START);
        loadLayout_method.isLoading();
        loadLayout_money.isLoading();
        loadLayout_payMoney.isLoading();
        JSONObject para = new JSONObject();
        JSONObject sender = Default.GetSession();
        try {
            para.put("session", Default.GetSession());
            para.put("amount", GetIntentData(AMOUNT));
            para.put("user_note", GetIntentData(USERNOTE));
            para.put("payment_id", GetIntentData(PAYMENTID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(RechargesActivity.this, "account/deposit", para, new AsynServer.BackObject() {
            @Override
            public void Back(final JSONObject sender) {
                try {
                    JSONObject status = sender.getJSONObject("status");
                    if (status.getInt("succeed") == 0) {
                        loadLayout_money.noData();
                        loadLayout_method.noData();
                        loadLayout_payMoney.noData();
                        MessageBox.Show(RechargesActivity.this.getString(R.string.app_name), status.getString("error_desc"), new String[]{"确定"}, new MessageBox.MessBtnBack() {
                            @Override
                            public void Back(int index) {
                                finish();
                            }
                        });
                    } else {
                        JSONObject jon = sender.getJSONObject("data");
                        JSONObject jon1 = jon.getJSONObject("payment");
                        loadLayout_money.loadOk(jon.getString("amount"));
                        loadLayout_method.loadOk(jon1.getString("pay_name"));
                        loadLayout_payMoney.loadOk(jon.getString("pay_fee"));
                        if ((int) jon1.getInt("pay_id") == 3) {
                            JSONObject data = jon.getJSONObject("wx_data");
                            api = WXAPIFactory.createWXAPI(RechargesActivity.this, PayFromWX.AppID);
                            final PayReq req = new PayReq();
                            req.appId = data.getString("appid");
                            req.partnerId = data.getString("partnerid");
                            req.prepayId = data.getString("prepayid");
                            req.nonceStr = data.getString("noncestr");
                            req.timeStamp = data.getString("timestamp");
                            req.packageValue = data.getString("package");
                            req.sign = data.getString("sign");
                            req.extData = "app data"; // optional
                            btn_tj.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(RechargesActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                                    api.sendReq(req);
                                }
                            });
                        } else {
                            final String authInfo;
                            authInfo = jon.getString("ali_data");
                            btn_tj.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Runnable authRunnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            // 构造AuthTask 对象
                                            AuthTask authTask = new AuthTask(RechargesActivity.this);
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
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

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
                    para.put("order_id", id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AsynServer.BackObject(RechargesActivity.this, "order/status", para, new AsynServer.BackObject() {
                    @Override
                    public void Back(JSONObject s) {

                        JSONObject json_status = JsonClass.jj(s, "status");
                        if (JsonClass.ij(json_status, "succeed") == 0) {
                            Toast.makeText(RechargesActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RechargesActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                            AddActivity(BackOrdersActivity.class);
                        }
                    }
                });
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                Toast.makeText(RechargesActivity.this, "支付失败" + resultStatus, Toast.LENGTH_SHORT).show();
            }
        }
    };

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
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }
}
