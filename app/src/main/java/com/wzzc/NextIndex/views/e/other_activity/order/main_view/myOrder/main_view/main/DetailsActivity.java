package com.wzzc.NextIndex.views.e.other_activity.order.main_view.myOrder.main_view.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_view.extendView.ExtendListView;
import com.wzzc.NextIndex.views.e.other_activity.order.waitSendOrder.BackOrdersActivity;
import com.wzzc.other_function.pay.PayResult;
import com.wzzc.NextIndex.views.e.other_activity.order.main_view.myOrder.main_view.main.main_view.DetailsView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by zcyb365 on 2016/11/7.
 */
public class DetailsActivity extends BaseActivity implements IWXAPIEventHandler {

    private IWXAPI api;
    private DetailsAdapter adapter;
    @ViewInject(R.id.main_view)
    private ExtendListView main_view;
    @ViewInject(R.id.lab_ordernumber)
    private TextView ordernumber;
    @ViewInject(R.id.lab_orderstate)
    private TextView orderstate;
    @ViewInject(R.id.lab_paystate)
    private TextView paystate;
    @ViewInject(R.id.lab_pay)
    private TextView pay;
    @ViewInject(R.id.lab_distribution)
    private TextView distribution;
    @ViewInject(R.id.lab_money)
    private TextView money;
    @ViewInject(R.id.lab_countmoney)
    private TextView countmoney;
    @ViewInject(R.id.lab_username)
    private TextView username;
    @ViewInject(R.id.userphone)
    private TextView userphone;
    @ViewInject(R.id.user_address)
    private TextView address;
    @ViewInject(R.id.lab_paymode)
    private TextView paymode;
    @ViewInject(R.id.lab_express)
    private TextView express;
    @ViewInject(R.id.lab_noshopping)
    private TextView noshopping;
    @ViewInject(R.id.lab_ispay)
    private LinearLayout ispay;
    int id;
    private boolean isloading = false;
    private int pageindex = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddBack();
        AddTitle("订单详情");

    }

    @Override
    protected void viewFirstLoad() {
        adapter = new DetailsAdapter(this);
        main_view.setAdapter(adapter);
        GetServerInfo();
    }

    public void GetServerInfo() {
        if (isloading) {
            return;
        }
        isloading = true;
        JSONObject para = new JSONObject();
        try {
            JSONObject filter = new JSONObject();
            para.put("session", filter);
            para.put("order_id", GetIntentData("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(DetailsActivity.this , "order/details", para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                try {
                    ArrayList<JSONObject> json = new ArrayList<JSONObject>();
                    JSONObject data = sender.getJSONObject("data");
                    orderstate.setText(data.getString("order_status"));
                    paystate.setText(data.getString("pay_status"));
                    distribution.setText(data.getString("shipping_status"));
                    final JSONObject order = data.getJSONObject("order");
                    ordernumber.setText(order.getString("order_sn"));
                    money.setText(order.getString("formated_goods_amount"));
                    countmoney.setText(order.getString("formated_order_amount"));
                    if (!"".equals(order.getString("pay_online").trim()) && null != order.getString("pay_online")) {
                        pay.setText("使用" + order.getString("pay_name"));
                        pay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                JSONObject para1 = new JSONObject();
                                try {
                                    JSONObject filter = new JSONObject();
                                    para1.put("order_id", GetIntentData("id"));
                                    AsynServer.BackObject(DetailsActivity.this , "order/pay", para1, new AsynServer.BackObject() {
                                        @Override
                                        public void Back(JSONObject sender) {
                                            try {
                                                JSONObject jon = sender.getJSONObject("data");
                                                if (jon.getInt("pay_id") == 1) {
                                                    final String authInfo = jon.getString("ali_data");
                                                    id = (int) GetIntentData("id");
                                                    Runnable authRunnable = new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            // 构造AuthTask 对象
                                                            AuthTask authTask = new AuthTask(DetailsActivity.this);
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

                                                } else if (jon.getInt("pay_id") == 3) {
                                                    JSONObject data = jon.getJSONObject("wx_data");
                                                    api = WXAPIFactory.createWXAPI(DetailsActivity.this, data.getString("appid"));
                                                    PayReq req = new PayReq();
                                                    req.appId = data.getString("appid");
                                                    req.partnerId = data.getString("partnerid");
                                                    req.prepayId = data.getString("prepayid");
                                                    req.nonceStr = data.getString("noncestr");
                                                    req.timeStamp = data.getString("timestamp");
                                                    req.packageValue = data.getString("package");
                                                    req.sign = data.getString("sign");
                                                    req.extData = "app data"; // optional
                                                    Toast.makeText(DetailsActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                                                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                                                    api.sendReq(req);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        ispay.setVisibility(View.GONE);
                    }
                    username.setText(order.getString("consignee"));
                    userphone.setText(order.getString("tel") + "   " + order.getString("mobile"));
                    address.setText(order.getString("address"));
                    paymode.setText(order.getString("pay_name"));
                    express.setText(order.getString("shipping_name"));
                    noshopping.setText(order.getString("how_oos"));
                    JSONArray arr = data.getJSONArray("goods_list");
                    for (int i = 0; i < arr.length(); i++) {
                        json.add(arr.getJSONObject(i));
                    }
                    LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) main_view.getLayoutParams();
                    WindowManager wm = (WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE);
                    int height = wm.getDefaultDisplay().getHeight();
                    if (arr.length() < height / 106) {
                        params1.height = (Default.dip2px((arr.length() * 106), getBaseContext()));
                        main_view.setLayoutParams(params1);
                    } else {
                        params1.height = (Default.dip2px(RelativeLayout.LayoutParams.MATCH_PARENT, getBaseContext()));
                        main_view.setLayoutParams(params1);
                    }
                    adapter.addData(json);
                    pageindex++;
                    isloading = false;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class DetailsAdapter extends BaseAdapter {
        private ArrayList<JSONObject> data;
        private Context content;


        public DetailsAdapter(Context context) {
            this.content = context;
            this.data = new ArrayList<>();
        }


        public void addData(ArrayList<JSONObject> data) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            int count = this.data.size();
            if (this.data.size() == 1) {
                count++;
            }
            return count;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            DetailsView view;
            if (convertView != null) {
                view = (DetailsView) convertView;
            } else {
                view = new DetailsView(this.content);
            }
            JSONObject[] arr1;
            int index = position;
            arr1 = new JSONObject[]{this.data.get(index)};
            view.setInfo(arr1);
            return view;
        }
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
                AsynServer.BackObject(DetailsActivity.this , "order/status", para, new AsynServer.BackObject() {
                    @Override
                    public void Back(JSONObject sender) {
                        JSONObject json_status = JsonClass.jj(sender,"status");
                        if (JsonClass.ij(json_status,"succeed") == 0) {
                            Toast.makeText(DetailsActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DetailsActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                            AddActivity(BackOrdersActivity.class);
                        }

                    }
                });
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                Toast.makeText(DetailsActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
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
