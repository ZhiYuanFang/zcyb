package com.wzzc.other_function.pay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.AuthTask;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.Default;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.NextIndex.views.e.other_activity.order.OrderListActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by by Administrator on 2017/4/6.
 */

public class PayFromZFB {

    public static final Integer ZCBORDER = 1, GCBORDER = 0;
    private static String authInfo;
    private static String order_id;
    private static Integer pay_log_id;
    private static Context c;
    private static final String error = "支付失败";
    private static final String ok = "支付成功";
    private static Integer order_type;
    private static MessageBox.MessBtnBack back_ok, back_error;

    /**
     * @param shippingType 配送方式
     * @param type         0 -- 现金 || 1 -- 兑换
     * @param shippingType 配送方式
     * @param surplus      支付余额
     * @param address_id   收货地址ID
     * @param need_insure  选定/取消配送的保价
     * @param postscript   订单留言
     *                     @param integral 使用工厂币数量
     */
    public static void pay(Context context, Integer orderListType, Integer type, Integer shippingType, String surplus, String address_id, Integer need_insure, String postscript,String integral) {
        c = context;
        order_type = orderListType;
        JSONObject para = new JSONObject();
        try {
            para.put("session", Default.GetSession());
            para.put("pay_id", 1);
            para.put("shipping_id", shippingType);
            para.put("surplus", surplus);
            para.put("address_id", address_id);
            para.put("is_done", 1);
            para.put("need_insure", need_insure);
            para.put("postscript", postscript);
            para.put("integral",integral);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = type == GCBORDER ? "flow/done" : "exflow/done";
        AsynServer.BackObject(c, url, para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {

                try {
                    JSONObject json_status = sender.getJSONObject("status");
                    int succeed = json_status.getInt("succeed");
                    if (succeed == 0) {
                        MessageBox.Show(json_status.getString("error_desc"));
                    } else {
                        JSONObject json_data = sender.getJSONObject("data");
                        authInfo = json_data.getString("ali_data");
                        order_id = String.valueOf(json_data.get("order_id"));
                        final Runnable authRunnable = new Runnable() {
                            @Override
                            public void run() {
                                AuthTask authTask = new AuthTask((Activity) c);
                                Map<String, String> result = authTask.authV2(authInfo, true);
                                Message msg = new Message();
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }

                        };
                        Thread authThread = new Thread(authRunnable);
                        authThread.start();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public static void pay(Context context, int orderListType, int type, JSONObject para) {
        c = context;
        order_type = orderListType;
        String url = type == GCBORDER ? "flow/done" : "exflow/done";
        AsynServer.BackObject(c, url, para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {

                try {
                    JSONObject json_status = sender.getJSONObject("status");
                    int succeed = json_status.getInt("succeed");
                    if (succeed == 0) {
                        MessageBox.Show(json_status.getString("error_desc"));
                    } else {
                        JSONObject json_data = sender.getJSONObject("data");
                        authInfo = json_data.getString("ali_data");
                        order_id = String.valueOf(json_data.get("order_id"));
                        final Runnable authRunnable = new Runnable() {
                            @Override
                            public void run() {
                                AuthTask authTask = new AuthTask((Activity) c);
                                Map<String, String> result = authTask.authV2(authInfo, true);
                                Message msg = new Message();
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }

                        };
                        Thread authThread = new Thread(authRunnable);
                        authThread.start();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public static void pay(Context context, int orderListType, String orderID) {
        c = context;
        order_type = orderListType;
        order_id = orderID;
        String url = "order/pay";

        JSONObject para = new JSONObject();
        try {
            para.put("session", Default.GetSession());
            para.put("order_id", order_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(c, url, para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {

                try {
                    JSONObject json_status = sender.getJSONObject("status");
                    int succeed = json_status.getInt("succeed");
                    if (succeed == 0) {
                        MessageBox.Show(json_status.getString("error_desc"));
                    } else {
                        JSONObject json_data = sender.getJSONObject("data");
                        authInfo = json_data.getString("ali_data");
                        final Runnable authRunnable = new Runnable() {
                            @Override
                            public void run() {
                                AuthTask authTask = new AuthTask((Activity) c);
                                Map<String, String> result = authTask.authV2(authInfo, true);
                                Message msg = new Message();
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }

                        };
                        Thread authThread = new Thread(authRunnable);
                        authThread.start();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public static void pay(final Context context, String url, JSONObject para, MessageBox.MessBtnBack ok, MessageBox.MessBtnBack error) {
        back_ok = ok;
        back_error = error;
        AsynServer.BackObject(context, url, para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                try {
                    JSONObject json_status = sender.getJSONObject("status");
                    int succeed = json_status.getInt("succeed");
                    if (succeed == 0) {
                        MessageBox.Show(json_status.getString("error_desc"));
                    } else {
                        JSONObject json_data = sender.getJSONObject("data");
                        JSONObject json_order = json_data.getJSONObject("order");
                        pay_log_id = json_order.getInt("log_id");
                        authInfo = json_data.getString("ali_data");
                        Runnable authRunnable = new Runnable() {
                            @Override
                            public void run() {
                                // 构造AuthTask 对象
                                AuthTask authTask = new AuthTask((Activity) context);
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
        });
    }

    @SuppressLint("HandlerLeak")
    private static Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            @SuppressWarnings("unchecked")
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            /**
             对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
             */
            String resultInfo = payResult.getResult();// 同步返回需要验证的信息
            String resultStatus = payResult.getResultStatus().trim();
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                final JSONObject para = new JSONObject();
                try {
                    para.put("order_id", order_id);
                    para.put("pay_log_id", pay_log_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AsynServer.BackObject(c, "order/status", para, new AsynServer.BackObject() {
                    @Override
                    public void Back(JSONObject sender) {
                        JSONObject json_status = JsonClass.jj(sender,"status");
                        if (JsonClass.ij(json_status,"succeed") == 0) {
                            MessageBox.Show(JsonClass.sj(json_status,"error_desc"));
                        } else {
                            JSONObject json_data = JsonClass.jj(sender,"data");
                            if (JsonClass.ij(json_data,"status") == 0) {
                                if (back_error != null) {
                                    MessageBox.Show("支付宝支付", error, new String[]{"确定"}, back_error);
                                } else toOrderListActivity();
                            } else {
                                if (back_ok != null) {
                                    MessageBox.Show("支付宝支付", error, new String[]{"确定"}, back_ok);
                                } else toOrderListActivity();
                            }
                        }
                    }
                });
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                if (back_error != null) {
                    MessageBox.Show("支付宝支付", error, new String[]{"确定"}, back_error);
                } else toOrderListActivity();
            }
        }
    };

    private static void toOrderListActivity() {
        Intent intent = new Intent(c, OrderListActivity.class);
        intent.putExtra("order_type", order_type);
        Default.toClass(c, intent);
    }


}
