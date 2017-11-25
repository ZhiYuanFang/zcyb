package com.wzzc.other_function.pay;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.sdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wzzc.NextIndex.views.e.other_activity.order.OrderListActivity;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.MessageBox;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/4/6.
 */

public class PayFromWX implements IWXAPIEventHandler {
    public static final Integer ZCBORDER = 1, GCBORDER = 0;
    public static final String AppID = "wxb50b03c5de52401e";

    private static Context c;
    private static Integer order_type;
    private static IWXAPI api;
    private static MessageBox.MessBtnBack back_ok , back_error;

    /**
     * @param shippingType 配送方式
     * @param surplus      支付余额
     * @param address_id   收货地址ID
     * @param need_insure  选定/取消配送的保价
     * @param postscript   订单留言
     *                     @param integral 使用工厂币数量
     */
    public static void pay(Context context, int orderListType, int type, Integer shippingType, String surplus, String address_id, Integer need_insure, String postscript,String integral) {
        c = context;
        order_type = orderListType;
        JSONObject para = new JSONObject();
        try {
            para.put("session", Default.GetSession());
            para.put("pay_id", 3);
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
                        JSONObject wx = json_data.getJSONObject("wx_data");
                        api = WXAPIFactory.createWXAPI(c, AppID);
                        PayReq req = new PayReq();
                        req.appId = wx.getString("appid");
                        req.partnerId = wx.getString("partnerid");
                        req.prepayId = wx.getString("prepayid");
                        req.nonceStr = wx.getString("noncestr");
                        req.timeStamp = wx.getString("timestamp");
                        req.packageValue = wx.getString("package");
                        req.sign = wx.getString("sign");
                        req.extData = "app data"; // optional
                        api.sendReq(req);
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
                        JSONObject wx = json_data.getJSONObject("wx_data");
                        api = WXAPIFactory.createWXAPI(c, AppID);
                        PayReq req = new PayReq();
                        req.appId = wx.getString("appid");
                        req.partnerId = wx.getString("partnerid");
                        req.prepayId = wx.getString("prepayid");
                        req.nonceStr = wx.getString("noncestr");
                        req.timeStamp = wx.getString("timestamp");
                        req.packageValue = wx.getString("package");
                        req.sign = wx.getString("sign");
                        req.extData = "app data"; // optional
                        api.sendReq(req);
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
        String url = "order/pay";
        JSONObject para = new JSONObject();
        try {
            para.put("session", Default.GetSession());
            para.put("order_id", orderID);
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
                        JSONObject wx = json_data.getJSONObject("wx_data");
                        api = WXAPIFactory.createWXAPI(c, AppID);
                        PayReq req = new PayReq();
                        req.appId = wx.getString("appid");
                        req.partnerId = wx.getString("partnerid");
                        req.prepayId = wx.getString("prepayid");
                        req.nonceStr = wx.getString("noncestr");
                        req.timeStamp = wx.getString("timestamp");
                        req.packageValue = wx.getString("package");
                        req.sign = wx.getString("sign");
                        req.extData = "app data"; // optional
                        api.sendReq(req);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void pay(final Context context, String url, JSONObject para, MessageBox.MessBtnBack ok,MessageBox.MessBtnBack error) {
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
                        JSONObject data = json_data.getJSONObject("wx_data");
                        api = WXAPIFactory.createWXAPI(context, AppID);
                        PayReq req = new PayReq();
                        req.appId = data.getString("appid");
                        req.partnerId = data.getString("partnerid");
                        req.prepayId = data.getString("prepayid");
                        req.nonceStr = data.getString("noncestr");
                        req.timeStamp = data.getString("timestamp");
                        req.packageValue = data.getString("package");
                        req.sign = data.getString("sign");
                        req.extData = "app data"; // optional
                        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                        api.sendReq(req);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        int result;

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
        Default.showToast(c.getString(result), Toast.LENGTH_LONG);
        if (back_ok != null) {
            MessageBox.Show("微信支付", c.getString(result), new String[]{"确定"}, back_ok);
        } else
            toOrderListActivity();
    }

    private static void toOrderListActivity() {
        Intent intent = new Intent(c, OrderListActivity.class);
        intent.putExtra("order_type", order_type);
        Default.toClass(c, intent);
    }

    private void goToShowMsg(ShowMessageFromWX.Req showReq) {
        WXMediaMessage wxMsg = showReq.message;
        WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;
        StringBuffer msg = new StringBuffer(); // 组织一个待显示的消息内容
        msg.append("description: ");
        msg.append(wxMsg.description);
        msg.append("\n");
        msg.append("extInfo: ");
        msg.append(obj.extInfo);
        msg.append("\n");
        msg.append("filePath: ");
        msg.append(obj.filePath);
        Intent intent = new Intent();
        intent.putExtra(Constants.ShowMsgActivity.STitle, wxMsg.title);
        intent.putExtra(Constants.ShowMsgActivity.SMessage, msg.toString());
        intent.putExtra(Constants.ShowMsgActivity.BAThumbData, wxMsg.thumbData);
        // TODO: 2017/4/24 some view
        ((BaseActivity) c).finish();
    }
}
