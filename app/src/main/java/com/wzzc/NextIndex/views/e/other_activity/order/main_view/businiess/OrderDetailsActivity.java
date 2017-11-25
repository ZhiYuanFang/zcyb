package com.wzzc.NextIndex.views.e.other_activity.order.main_view.businiess;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.other_function.MessageBox;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.extendView.ExtendListView;
import com.wzzc.NextIndex.views.e.other_activity.BusinessShop.invoice.InvoiceActivity;
import com.wzzc.NextIndex.views.e.other_activity.BusinessShop.invoice.main_view.delivery.Delivery1View;
import com.wzzc.NextIndex.views.e.other_activity.order.main_view.businiess.main.main_view.OrderDetailsView;
import com.wzzc.NextIndex.views.e.other_activity.order.main_view.myOrder.main_view.main.main_view.main_view.ConfirmOrderToDeliveryOrderActivity;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zcyb365 on 2016/11/24.
 * <p>
 * 商家订单详情
 */
public class OrderDetailsActivity extends BaseActivity implements View.OnClickListener{
    public static final String ORDERID = "id";
    public static final String NUMBER = "number";

    private static final /*配货*/String PREPARE = "prepare", /*取消*/CANCEL = "cancel",/*生成发货单*/SPLIT = "split", /*售后*/AFTERSERVICE = "after_service",
    /*一键发货*/FASTSHIPPING = "fast_shipping", /*去发货*/TODELIVERY = "to_delivery",/*发货*/SHIP = "ship",/*确认*/CONFIRM = "confirm",/*移除*/REMOVE = "remove",
    /*付款*/PAY="pay",/*无效*/INVALID="invalid";
    private DeliveryAdapter adapter_goods;
    private boolean isloading = false;
    private int pageindex = 1;
    private JSONObject json_data;
    /**
     * 订单ID
     */
    private String order_id;
    /** 订单号*/
    private String order_sn;
    //region 组件
    @ViewInject(R.id.main_view)
    ExtendListView main_view_goods;
    private Delivery1Adapter adapter_action;
    @ViewInject(R.id.main_view1)
    ExtendListView main_view_action;
    @ViewInject(R.id.lab_number)
    private TextView lab_number;
    @ViewInject(R.id.lab_state)
    private TextView lab_state;
    @ViewInject(R.id.tv_name)
    private TextView lab_name;
    @ViewInject(R.id.lab_time)
    private TextView lab_time;
    @ViewInject(R.id.lab_pay)
    private TextView lab_pay;
    @ViewInject(R.id.lab_time1)
    private TextView lab_time1;
    @ViewInject(R.id.lab_Distribution)
    private TextView lab_Distribution;
    @ViewInject(R.id.lab_time2)
    private TextView lab_time2;
    @ViewInject(R.id.lab_number1)
    private TextView lab_number1;
    @ViewInject(R.id.lab_count)
    private TextView lab_count;
    @ViewInject(R.id.lab_countmoney)
    private TextView lab_countmoney;
    @ViewInject(R.id.lab_name1)
    private TextView lab_name1;
    @ViewInject(R.id.lab_phone)
    private TextView lab_phone;
    @ViewInject(R.id.lab_address)
    private TextView lab_address;
    @ViewInject(R.id.lab_noshopping)
    private TextView lab_noshopping;
    @ViewInject(R.id.lab_much)
    private LinearLayout lab_much;
    @ViewInject(R.id.et_action_note)
    private EditText et_action_note;
    @ViewInject(R.id.layout_contain_fastDelivery)
    private LinearLayout layout_contain_fastDelivery;
    @ViewInject(R.id.lab_delivery)
    private TextView lab_delivery;
    @ViewInject(R.id.edit_input)
    private EditText edit_input;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddBack();
        AddTitle("订单详情");
        init();
        order_id = String.valueOf(GetIntentData(ORDERID));
        GetServerInfo(null,null,null);
    }

    private void init () {
        lab_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String order_sn = edit_input.getText().toString();
                // TODO: 2017/4/13 一键发货
                if (order_sn.length() > 0) {
                    GetServerInfo(SHIP, order_id, new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            Default.showToast("一键发货:" + msg.obj.toString(),Toast.LENGTH_LONG);
                            return false;
                        }
                    });
                }
            }
        });
    }

    public void GetServerInfo(String operation , String send_number, final Handler.Callback callback) {
        layout_contain_fastDelivery.setVisibility(View.GONE);
        adapter_goods = new DeliveryAdapter(this);
        main_view_goods.setAdapter(adapter_goods);
        adapter_action = new Delivery1Adapter(this);
        main_view_action.setAdapter(adapter_action);
        if (isloading) {
            return;
        }
        isloading = true;
        JSONObject para = new JSONObject();
        try {
            para.put("session", Default.GetSession());
            para.put("order_id", order_id);
            para.put("operation",operation);
            para.put("action_note",et_action_note.getText());
            para.put("send_number",send_number);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(OrderDetailsActivity.this, "seller/order_detail", para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                try {
                    JSONObject json_status = sender.getJSONObject("status");
                    int succeed = json_status.getInt("succeed");
                    if (succeed == 0 && callback != null) {
                        Message msg = new Message();
                        msg.obj = json_status.getString("error_desc");
                        callback.handleMessage(msg);
                    } else {
                        if (callback != null) {
                            Message msg = new Message();
                            msg.obj = "操作成功";
                            callback.handleMessage(msg);
                        }
                        json_data = sender.getJSONObject("data");
                        //region other info
                        JSONObject json_order = json_data.getJSONObject("order");
                        order_sn = json_order.getString("order_sn");
                        if (json_order.has("")) {
                        }
                        if (json_order.has("order_sn")) {
                            lab_number.setText(json_order.getString("order_sn"));
                        }
                        if (json_order.has("status")) {
                            lab_state.setText(json_order.getString("status"));
                        }
                        if (json_order.has("user_name")) {
                            lab_name.setText(json_order.getString("user_name"));
                        }
                        if (json_order.has("pay_time")) {
                            lab_time.setText(json_order.getString("pay_time"));
                        }
                        if (json_order.has("pay_name")) {
                            lab_pay.setText(json_order.getString("pay_name"));
                        }
                        if (json_order.has("formated_add_time")) {
                            lab_time1.setText(json_order.getString("formated_add_time"));
                        }
                        if (json_order.has("shipping_name")) {
                            lab_Distribution.setText(json_order.getString("shipping_name"));
                        }
                        if (json_order.has("shipping_time")) {
                            lab_time2.setText(json_order.getString("shipping_time"));
                        }
                        if (json_order.has("invoice_no")) {
                            lab_number1.setText(json_order.getString("invoice_no"));
                        }
                        if (json_order.has("goods_amount") && json_order.has("shipping_fee") && json_order.has("formated_money_paid")) {
                            lab_count.setText(json_order.getString("goods_amount") + "\n+" + "配送费用:" + json_order.getString("shipping_fee") + "\n-已付款金额:" + json_order.getString("formated_money_paid"));
                        }
                        if (json_order.has("formated_order_amount")) {
                            lab_countmoney.setText(json_order.getString("formated_order_amount"));
                        }
                        if (json_order.has("consignee")) {
                            lab_name1.setText(json_order.getString("consignee"));
                        }
                        if (json_order.has("mobile")) {
                            lab_phone.setText(json_order.getString("mobile"));
                        }

                        if (json_order.has("address")) {
                            lab_address.setText(json_order.getString("address"));
                        }

                        if (json_order.has("how_oos")) {
                            lab_noshopping.setText(json_order.getString("how_oos"));
                        }
                        //endregion
                        //region goods_list
                        ArrayList<JSONObject> json_goods = new ArrayList<JSONObject>();
                        JSONArray arr_goods = json_data.getJSONArray("goods_list");
                        for (int i = 0; i < arr_goods.length(); i++) {
                            json_goods.add(arr_goods.getJSONObject(i));
                        }
                        adapter_goods.addData(json_goods);
                        Default.fixListViewHeight(main_view_goods);
                        //endregion
                        //region action_list
                        ArrayList<JSONObject> json_action = new ArrayList<JSONObject>();
                        JSONArray arr_action = json_data.getJSONArray("action_list");
                        for (int i = 0; i < arr_action.length(); i++) {
                            json_action.add(arr_action.getJSONObject(i));
                        }
                        adapter_action.addData(json_action);
                        Default.fixListViewHeight(main_view_action);
                        //endregion
                        //region operable_list
                        JSONArray jrr_operable_list = json_data.getJSONArray("operable_list");
                        lab_much.removeAllViews();
                        for (int i = 0; i < jrr_operable_list.length(); i++) {
                            TextView tv = new TextView(OrderDetailsActivity.this);
                            String str = jrr_operable_list.getString(i);
                            tv.setTag(str);
                            String text;
                            switch (str) {
                                case FASTSHIPPING:
                                    layout_contain_fastDelivery.setVisibility(View.VISIBLE);
                                    text = "";
                                    break;
                                case PREPARE:
                                    text = "配货";
                                    break;
                                case SPLIT:
                                    text = "生成发货单";
                                    break;
                                case AFTERSERVICE:
                                    text = "售后";
                                    break;
                                case TODELIVERY:
                                    text = "去发货";
                                    break;
                                case CANCEL:
                                    String pay_status = json_order.getString("pay_status");
                                    switch (pay_status){
                                        case "0":
                                            //未付款
                                            text = "取消";
                                            break;
                                        case "2":
                                            //已付款
                                            text = "";
                                            break;
                                        default:
                                            text = "取消";
                                    }
                                    break;
                                case CONFIRM:
                                    text = "确认";
                                    break;
                                case REMOVE:
                                    text = "移除";
                                    break;
                                case PAY:
                                    text = "付款";
                                    break;
                                case INVALID:
                                    text = "无效";
                                    break;
                                default:
                                    text = str;
                            }
                            tv.setText(text);
                            tv.setTextColor(ContextCompat.getColor(OrderDetailsActivity.this, R.color.tv_White));
                            tv.setBackgroundColor(ContextCompat.getColor(OrderDetailsActivity.this, R.color.bg_hasOK));
                            tv.setPadding(3, 3, 3, 3);
                            tv.setGravity(Gravity.CENTER);
                            tv.setOnClickListener(OrderDetailsActivity.this);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            lp.setMarginStart(7);
                            lp.setMarginEnd(7);
                            tv.setLayoutParams(lp);
                            lab_much.addView(tv);
                        }
                        //endregion
                    }
                    pageindex++;
                    isloading = false;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(final View v) {

        switch (v.getTag().toString()){
            case PREPARE:
                // TODO: 2017/4/12 配货
                GetServerInfo(v.getTag().toString(),null, new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        Default.showToast("配货 " + msg.obj.toString(),Toast.LENGTH_LONG);
                        return false;
                    }
                });
                break;
            case CANCEL:
                // TODO: 2017/4/12  取消
                GetServerInfo(v.getTag().toString(),null, new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        Default.showToast("取消 " + msg.obj.toString(),Toast.LENGTH_LONG);
                        return false;
                    }
                });
                break;
            case AFTERSERVICE:
                // TODO: 2017/4/12  售后
                if (et_action_note.getText().toString().trim().length() < 1) {
                    MessageBox.Show("请输入操作备注");
                } else {
                    GetServerInfo(v.getTag().toString(), null,new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            Default.showToast("售后 " + msg.obj.toString(),Toast.LENGTH_LONG);
                            return false;
                        }
                    });
                }
                break;
            case SPLIT:{
                // TODO: 2017/4/12  生成发货单
                Intent intent = new Intent();
                intent.putExtra(ConfirmOrderToDeliveryOrderActivity.DATA,json_data.toString());
                AddActivity(ConfirmOrderToDeliveryOrderActivity.class,0,intent);
                break;}
            case TODELIVERY:{
                // TODO: 2017/4/12  去发货
                Intent intent = new Intent();
                intent.putExtra(InvoiceActivity.ORDERSN,order_sn);
                AddActivity(InvoiceActivity.class,0,intent);
                break;}
            case CONFIRM:
                // TODO: 2017/4/13 确认
                GetServerInfo(v.getTag().toString(), null,new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        Default.showToast("确认 " + msg.obj.toString(),Toast.LENGTH_LONG);
                        return false;
                    }
                });
                break;
            case REMOVE:
                // TODO: 2017/4/13 移除
                MessageBox.Show("商家订单移除", "即将移除订单：" + order_sn + "\n" + "\u3000\u3000移除后将无法撤销！", new String[]{"取消", "确认"}, new MessageBox.MessBtnBack() {
                    @Override
                    public void Back(int index) {
                        switch (index){
                            case 0:
                                break;
                            case 1:
                                GetServerInfo(v.getTag().toString(), null,new Handler.Callback() {
                                    @Override
                                    public boolean handleMessage(Message msg) {
                                        Default.showToast("移除 " + msg.obj.toString(),Toast.LENGTH_LONG);
                                        return false;
                                    }
                                });
                                break;
                            default:
                        }
                    }
                });
                break;
            case PAY:
                // TODO: 2017/4/13 付款
                GetServerInfo(v.getTag().toString(), null,new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        Default.showToast("付款 " + msg.obj.toString(),Toast.LENGTH_LONG);
                        return false;
                    }
                });
                break;
            case INVALID:
                // TODO: 2017/4/13 无效
                GetServerInfo(v.getTag().toString(), null,new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        Default.showToast("无效 " + msg.obj.toString(),Toast.LENGTH_LONG);
                        return false;
                    }
                });
                break;
            default:
                Default.showToast(getString(R.string.notDevelop), Toast.LENGTH_LONG);
        }
    }

    @Override
    protected void onActivityResult(Bundle bundle) {
        super.onActivityResult(bundle);
        if (bundle != null && bundle.containsKey(NUMBER)) {
            String send_number = bundle.getString(NUMBER);
            GetServerInfo(SPLIT, send_number, new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    Default.showToast("生成发货单 " + msg.obj.toString(),Toast.LENGTH_LONG);
                    return false;
                }
            });
        }
    }

    public static class DeliveryAdapter extends BaseAdapter {
        private ArrayList<JSONObject> data;
        private Context content;


        public DeliveryAdapter(Context context) {
            this.content = context;
            this.data = new ArrayList<>();
        }


        public void addData(ArrayList<JSONObject> data) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return this.data.size();
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

            OrderDetailsView view;
            if (convertView != null) {
                view = (OrderDetailsView) convertView;
            } else {
                view = new OrderDetailsView(this.content);
            }
            JSONObject[] arr1;
            int index = position;
            arr1 = new JSONObject[]{this.data.get(index)};
            view.setInfo(arr1);
            return view;
        }
    }

    public static class Delivery1Adapter extends BaseAdapter {
        private ArrayList<JSONObject> data1;
        private Context content;


        public Delivery1Adapter(Context context) {
            this.content = context;
            this.data1 = new ArrayList<>();
        }


        public void addData(ArrayList<JSONObject> data) {
            this.data1.addAll(data);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return this.data1.size();
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

            Delivery1View view;
            if (convertView != null) {
                view = (Delivery1View) convertView;
            } else {
                view = new Delivery1View(this.content);
            }
            JSONObject[] arr1;
            int index = position;
            arr1 = new JSONObject[]{this.data1.get(index)};
            view.setInfo(arr1);
            return view;
        }
    }
}
