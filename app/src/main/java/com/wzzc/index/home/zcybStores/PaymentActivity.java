package com.wzzc.index.home.zcybStores;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.alipay.sdk.app.AuthTask;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.NextIndex.views.e.other_activity.order.waitSendOrder.BackOrdersActivity;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.other_function.pay.PayResult;
import com.wzzc.zcyb365.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;

/**
 * Created by zcyb365 on 2016/12/3.
 *
 * 线上支付
 */
@ContentView(R.layout.activity_payment)
public class PaymentActivity extends BaseActivity implements IWXAPIEventHandler {
    private IWXAPI api;
    @ViewInject(R.id.edit_money)
    private EditText edit_money;

    @ViewInject(R.id.lab_pay1)
    private LinearLayout lab_pay;
    @ViewInject(R.id.lab_pay2)
    private LinearLayout lab_pay1;

    @ViewInject(R.id.radioButton)
    private RadioButton radioButton;
    @ViewInject(R.id.radioButton2)
    private RadioButton radioButton2;
    @ViewInject(R.id.radioGroup_pay)
    private RadioGroup radioGroup_pay;
    @ViewInject(R.id.btn_sure)
    private Button btn_sure;
    private RadioButton checkRadioButton;
    int id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddBack();
        AddTitle("线上支付");

        lab_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton.setChecked(true);
            }
        });
        lab_pay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton2.setChecked(true);
            }
        });

        radioGroup_pay.check(R.id.radioButton);
        checkRadioButton = (RadioButton) radioGroup_pay.findViewById(radioGroup_pay.getCheckedRadioButtonId());
        radioGroup_pay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //点击事件获取的选择对象
                checkRadioButton = (RadioButton) radioGroup_pay.findViewById(checkedId);
//                Toast.makeText(getApplicationContext(), checkRadioButton.getTag().toString(), Toast.LENGTH_LONG).show();
            }
        });


            btn_sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (edit_money.getText()!=null&&!"".equals(edit_money.getText())) {
                    JSONObject para = new JSONObject();
                    JSONObject sender = Default.GetSession();
                    try {
                        JSONObject filter = new JSONObject();
                        filter.put("uid", sender.getString("uid"));
                        filter.put("sid", sender.getString("sid"));
                        para.put("session", filter);
                        para.put("amount", edit_money.getText());
                        para.put("seller_id", GetIntentData("id"));
                        if (checkRadioButton.getTag() == null) {
                            checkRadioButton.setTag(1);
                            para.put("payment_id", 1);
                        } else {
                            para.put("payment_id", checkRadioButton.getTag());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    AsynServer.BackObject(PaymentActivity.this , "stores_pay", para, new AsynServer.BackObject() {
                        @Override
                        public void Back(JSONObject s) {
                            JSONObject json_status = JsonClass.jj(s, "status");
                            if (JsonClass.ij(json_status, "succeed") == 0) {
                                MessageBox.Show(JsonClass.sj(json_status, "error_desc"));
                            } else {
                                JSONObject sender = JsonClass.jj(s,"data");
                                try {
                                    int i = Integer.parseInt(checkRadioButton.getTag().toString());
                                    if (i == 3) {
                                        JSONObject data = sender.getJSONObject("wx_data");
                                        api = WXAPIFactory.createWXAPI(PaymentActivity.this, "wxb50b03c5de52401e");
                                        PayReq req = new PayReq();
                                        req.appId = data.getString("appid");
                                        req.partnerId = data.getString("partnerid");
                                        req.prepayId = data.getString("prepayid");
                                        req.nonceStr = data.getString("noncestr");
                                        req.timeStamp = data.getString("timestamp");
                                        req.packageValue = data.getString("package");
                                        req.sign = data.getString("sign");
                                        req.extData = "app data"; // optional
                                        Toast.makeText(PaymentActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                                        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                                        api.sendReq(req);

                                    } else {
                                        final String authInfo = sender.getString("ali_data");
                                        id = sender.getJSONObject("payment").getInt("pay_order");
                                        Runnable authRunnable = new Runnable() {
                                            @Override
                                            public void run() {
                                                // 构造AuthTask 对象
                                                AuthTask authTask = new AuthTask(PaymentActivity.this);
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
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
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
                AsynServer.BackObject(PaymentActivity.this , "order/status", para, new AsynServer.BackObject() {
                    @Override
                    public void Back(JSONObject sender) {
                        JSONObject json_status = JsonClass.jj(sender,"status");
                        if (JsonClass.ij(json_status,"succeed") == 0) {
                            Toast.makeText(PaymentActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PaymentActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                            AddActivity(BackOrdersActivity.class);
                        }
                    }
                });
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                Toast.makeText(PaymentActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
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
