package com.wzzc.NextIndex.views.e.other_activity.order.main_view.myOrder.main_view;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.other_function.MessageBox;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.other_function.pay.PayFromWX;
import com.wzzc.other_function.pay.PayFromZFB;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/3/1.
 */

public class MyOrderDetailsActivity extends BaseActivity {

    @ViewInject(R.id.layout_order_detail)
    LinearLayout layout_order_detail;
    @ViewInject(R.id.layout_order_production)
    LinearLayout layout_order_production;
    @ViewInject(R.id.layout_address)
    LinearLayout layout_address;
    @ViewInject(R.id.layout_info)
    LinearLayout layout_info;
    @ViewInject(R.id.bt_pay)
    Button bt_pay;

    private Integer pay_id;
    private String order_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddBack();
        AddTitle("订单详情");
        bt_pay.setOnClickListener(payListener());
    }

    @Override
    protected void viewFirstLoad() {
        super.viewFirstLoad();
        String order_id = (String) GetIntentData("order_id");
        Integer is_old = (Integer) GetIntentData("is_old");
        getServerInfo(order_id , is_old);
    }

    public void getServerInfo (String order_id , Integer is_old) {
        pay_id = 1;
        this.order_id = order_id;
        JSONObject para = new JSONObject();
        try {
            para.put("session" , Default.GetSession());
            para.put("order_id" , order_id);
            para.put("is_old" , is_old);

            AsynServer.BackObject(this, "order/details", true, para, new AsynServer.BackObject() {
                @Override
                public void Back(JSONObject sender) {
                    try {
                        JSONObject json_status = sender.getJSONObject("status");
                        if (json_status.getInt("succeed") == 1) {
                            JSONObject json_data = sender.getJSONObject("data");
                            JSONObject json_order = json_data.getJSONObject("order");
                            String pay_status = json_order.getString("pay_status");
                            if (pay_status.equals("2")) {
                                bt_pay.setVisibility(View.GONE);
                            } else {
                                bt_pay.setVisibility(View.VISIBLE);
                            }
                            pay_id = Integer.valueOf(json_order.getString("pay_id"));

                            LinearLayout layout_current_order_1 = (LinearLayout) LayoutInflater.from(Default.getActivity()).inflate(R.layout.myorderdetail_partlayout , null);
                            TextView tv_state_1 = (TextView) layout_current_order_1.findViewById(R.id.tv_state);
                            TextView tv_info_1 = (TextView) layout_current_order_1.findViewById(R.id.tv_category);
                            tv_state_1.setText("订单编号:");
                            tv_info_1.setText(json_order.getString("order_sn"));

                            LinearLayout layout_current_order_2 = (LinearLayout) LayoutInflater.from(Default.getActivity()).inflate(R.layout.myorderdetail_partlayout , null);
                            TextView tv_state_2 = (TextView) layout_current_order_2.findViewById(R.id.tv_state);
                            TextView tv_info_2 = (TextView) layout_current_order_2.findViewById(R.id.tv_category);
                            tv_state_2.setText("消费码:");
                            tv_info_2.setText(json_order.getString("invoice_no"));

                            LinearLayout layout_current_order_3 = (LinearLayout) LayoutInflater.from(Default.getActivity()).inflate(R.layout.myorderdetail_partlayout , null);
                            TextView tv_state_3 = (TextView) layout_current_order_3.findViewById(R.id.tv_state);
                            TextView tv_info_3 = (TextView) layout_current_order_3.findViewById(R.id.tv_category);
                            tv_state_3.setText("订单状态:");
                            tv_info_3.setText(json_data.getString("order_status"));
                            tv_info_3.setTextColor(ContextCompat.getColor(MyOrderDetailsActivity.this , R.color.tv_Red));

                            LinearLayout layout_current_order_4 = (LinearLayout) LayoutInflater.from(Default.getActivity()).inflate(R.layout.myorderdetail_partlayout , null);
                            TextView tv_state_4 = (TextView) layout_current_order_4.findViewById(R.id.tv_state);
                            TextView tv_info_4 = (TextView) layout_current_order_4.findViewById(R.id.tv_category);
                            tv_state_4.setText("支付状态:");
                            tv_info_4.setText(json_data.getString("pay_status"));

                            LinearLayout layout_current_order_5 = (LinearLayout) LayoutInflater.from(Default.getActivity()).inflate(R.layout.myorderdetail_partlayout , null);
                            TextView tv_state_5 = (TextView) layout_current_order_5.findViewById(R.id.tv_state);
                            TextView tv_info_5 = (TextView) layout_current_order_5.findViewById(R.id.tv_category);
                            tv_state_5.setText("在线支付:");
                            tv_info_5.setText(json_order.getString("inv_payee"));

                            LinearLayout layout_current_order_6 = (LinearLayout) LayoutInflater.from(Default.getActivity()).inflate(R.layout.myorderdetail_partlayout , null);
                            TextView tv_state_6 = (TextView) layout_current_order_6.findViewById(R.id.tv_state);
                            TextView tv_info_6 = (TextView) layout_current_order_6.findViewById(R.id.tv_category);
                            tv_state_6.setText("配送状态:");
                            tv_info_6.setText(json_data.getString("shipping_status"));
                            tv_info_6.setTextColor(ContextCompat.getColor(MyOrderDetailsActivity.this , R.color.forestgreen));

                            LinearLayout layout_current_order_7 = (LinearLayout) LayoutInflater.from(Default.getActivity()).inflate(R.layout.myorderdetail_partlayout , null);
                            TextView tv_state_7 = (TextView) layout_current_order_7.findViewById(R.id.tv_state);
                            TextView tv_info_7 = (TextView) layout_current_order_7.findViewById(R.id.tv_category);
                            tv_state_7.setText("商品总价:");
                            tv_info_7.setText("￥" + json_order.getString("goods_amount"));

                            LinearLayout layout_current_order_8 = (LinearLayout) LayoutInflater.from(Default.getActivity()).inflate(R.layout.myorderdetail_partlayout , null);
                            TextView tv_state_8 = (TextView) layout_current_order_8.findViewById(R.id.tv_state);
                            TextView tv_info_8 = (TextView) layout_current_order_8.findViewById(R.id.tv_category);
                            tv_state_8.setText("共需支付:");
                            tv_info_8.setText("￥" + json_order.getString("order_amount"));
                            tv_info_8.setTextColor(ContextCompat.getColor(MyOrderDetailsActivity.this , R.color.forestgreen));

                            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
                            layout_current_order_1.setLayoutParams(layoutParams);
                            layout_current_order_2.setLayoutParams(layoutParams);
                            layout_current_order_3.setLayoutParams(layoutParams);
                            layout_current_order_4.setLayoutParams(layoutParams);
                            layout_current_order_5.setLayoutParams(layoutParams);
                            layout_current_order_6.setLayoutParams(layoutParams);
                            layout_current_order_7.setLayoutParams(layoutParams);
                            layout_current_order_8.setLayoutParams(layoutParams);
                            layout_order_detail.addView(layout_current_order_1);
                            layout_order_detail.addView(layout_current_order_2);
                            layout_order_detail.addView(layout_current_order_3);
                            layout_order_detail.addView(layout_current_order_4);
                            layout_order_detail.addView(layout_current_order_5);
                            layout_order_detail.addView(layout_current_order_6);
                            layout_order_detail.addView(layout_current_order_7);
                            TextView tv_line = new TextView(MyOrderDetailsActivity.this);
                            tv_line.setBackgroundColor(ContextCompat.getColor(MyOrderDetailsActivity.this  , R.color.tv_Gray));
                            RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , 1);
                            tv_line.setLayoutParams(layoutParams1);
                            layout_order_detail.addView(tv_line);
                            layout_order_detail.addView(layout_current_order_8);

                            JSONArray jrr_goods_list = json_data.getJSONArray("goods_list");
                            for (int i = 0 ; i < jrr_goods_list.length() ; i ++) {
                                JSONObject json_goods_list = jrr_goods_list.getJSONObject(i);
                                LinearLayout layout_current_order = (LinearLayout) LayoutInflater.from(Default.getActivity()).inflate(R.layout.layout_order_detail_production , null);
                                ExtendImageView img_thumb = (ExtendImageView) layout_current_order.findViewById(R.id.img_thumb);
                                img_thumb.setPath(json_goods_list.getString("goods_thumb"));
                                TextView tv_production_name = (TextView) layout_current_order.findViewById(R.id.tv_production_name);
                                tv_production_name.setText(json_goods_list.getString("goods_name"));
                                TextView tv_num = (TextView) layout_current_order.findViewById(R.id.tv_num);
                                tv_num.setText("X" + json_goods_list.getString("goods_number"));
                                LinearLayout layout_our_price = (LinearLayout) layout_current_order.findViewById(R.id.layout_our_price);
                                LinearLayout layout_current_order_9 = (LinearLayout) LayoutInflater.from(Default.getActivity()).inflate(R.layout.myorderdetail_partlayout , null);
                                TextView tv_state_9 = (TextView) layout_current_order_9.findViewById(R.id.tv_state);
                                TextView tv_info_9 = (TextView) layout_current_order_9.findViewById(R.id.tv_category);
                                tv_state_9.setText("本店价:");
                                tv_info_9.setText(json_goods_list.getString("goods_price"));
                                tv_info_9.setTextColor(ContextCompat.getColor(MyOrderDetailsActivity.this , R.color.tv_Red));
                                layout_current_order.setLayoutParams(layoutParams);
                                layout_current_order_9.setLayoutParams(layoutParams);
                                layout_our_price.addView(layout_current_order_9);
                                layout_order_production.addView(layout_current_order);
                            }


                            LinearLayout layout_current_order_10 = (LinearLayout) LayoutInflater.from(Default.getActivity()).inflate(R.layout.myorderdetail_partlayout , null);
                            TextView tv_state_10 = (TextView) layout_current_order_10.findViewById(R.id.tv_state);
                            TextView tv_info_10 = (TextView) layout_current_order_10.findViewById(R.id.tv_category);
                            tv_state_10.setText("联络贵宾:");
                            tv_info_10.setText(json_order.getString("consignee"));

                            LinearLayout layout_current_order_11 = (LinearLayout) LayoutInflater.from(Default.getActivity()).inflate(R.layout.myorderdetail_partlayout , null);
                            TextView tv_state_11 = (TextView) layout_current_order_11.findViewById(R.id.tv_state);
                            TextView tv_info_11 = (TextView) layout_current_order_11.findViewById(R.id.tv_category);
                            tv_state_11.setText("联络电话:");
                            tv_info_11.setText(json_order.getString("mobile"));

                            LinearLayout layout_current_order_12 = (LinearLayout) LayoutInflater.from(Default.getActivity()).inflate(R.layout.myorderdetail_partlayout , null);
                            TextView tv_state_12 = (TextView) layout_current_order_12.findViewById(R.id.tv_state);
                            TextView tv_info_12 = (TextView) layout_current_order_12.findViewById(R.id.tv_category);
                            tv_state_12.setText("收货地址:");
                            tv_info_12.setText(json_order.getString("address"));

                            layout_current_order_10.setLayoutParams(layoutParams);
                            layout_current_order_11.setLayoutParams(layoutParams);
                            layout_current_order_12.setLayoutParams(layoutParams);
                            layout_address.addView(layout_current_order_10);
                            layout_address.addView(layout_current_order_11);
                            layout_address.addView(layout_current_order_12);

                            LinearLayout layout_current_order_13 = (LinearLayout) LayoutInflater.from(Default.getActivity()).inflate(R.layout.myorderdetail_partlayout , null);
                            TextView tv_state_13 = (TextView) layout_current_order_13.findViewById(R.id.tv_state);
                            TextView tv_info_13 = (TextView) layout_current_order_13.findViewById(R.id.tv_category);
                            tv_state_13.setText("支付方式:");
                            tv_info_13.setText(json_order.getString("pay_name"));

                            LinearLayout layout_current_order_14 = (LinearLayout) LayoutInflater.from(Default.getActivity()).inflate(R.layout.myorderdetail_partlayout , null);
                            TextView tv_state_14 = (TextView) layout_current_order_14.findViewById(R.id.tv_state);
                            TextView tv_info_14 = (TextView) layout_current_order_14.findViewById(R.id.tv_category);
                            tv_state_14.setText("配送方式:");
                            tv_info_14.setText(json_order.getString("shipping_name"));

                            LinearLayout layout_current_order_15 = (LinearLayout) LayoutInflater.from(Default.getActivity()).inflate(R.layout.myorderdetail_partlayout , null);
                            TextView tv_state_15 = (TextView) layout_current_order_15.findViewById(R.id.tv_state);
                            TextView tv_info_15 = (TextView) layout_current_order_15.findViewById(R.id.tv_category);
                            tv_state_15.setText("缺货处理:");
                            tv_info_15.setText(json_order.getString("how_oos"));

                            layout_current_order_13.setLayoutParams(layoutParams);
                            layout_current_order_14.setLayoutParams(layoutParams);
                            layout_current_order_15.setLayoutParams(layoutParams);
                            layout_info.addView(layout_current_order_13);
                            layout_info.addView(layout_current_order_14);
                            layout_info.addView(layout_current_order_15);
                        } else {
                            MessageBox.Show(json_status.getString("error_desc"));
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

    protected View.OnClickListener payListener () {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            switch (pay_id){
                case 1:{
                    PayFromZFB.pay(MyOrderDetailsActivity.this,0,order_id);
                    break;
                }
                case 3:{
                    PayFromWX.pay(MyOrderDetailsActivity.this,0,order_id);
                    break;
                }
                default:Default.showToast(MyOrderDetailsActivity.this.getString(R.string.notDevelop),Toast.LENGTH_LONG);
            }
            }
        };
    }
}
