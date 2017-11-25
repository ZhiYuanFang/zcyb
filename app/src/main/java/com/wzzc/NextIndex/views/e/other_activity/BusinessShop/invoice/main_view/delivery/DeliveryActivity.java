package com.wzzc.NextIndex.views.e.other_activity.BusinessShop.invoice.main_view.delivery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.extendView.ExtendListView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zcyb365 on 2016/11/25.
 *
 * 发货单详情
 */
public class DeliveryActivity extends BaseActivity implements View.OnClickListener{

    private DeliveryAdapter adapter;
    private boolean isloading = false;
    private int pageindex = 1;
    private Delivery1Adapter adapter1;
    String ship;
    //region 组件
    @ViewInject(R.id.main_view)
    ExtendListView main_view;
    @ViewInject(R.id.main_view1)
    ExtendListView main_view1;
    @ViewInject(R.id.lab_number)
    private TextView lab_number;
    @ViewInject(R.id.lab_number1)
    private TextView lab_number1;
    @ViewInject(R.id.tv_name)
    private TextView lab_name;
    @ViewInject(R.id.lab_express)
    private TextView lab_express;
    @ViewInject(R.id.lab_inputnumber)
    private TextView lab_inputnumber;
    @ViewInject(R.id.lab_Consignee)
    private TextView lab_Consignee;
    @ViewInject(R.id.lab_address)
    private TextView lab_address;
    @ViewInject(R.id.lab_phone)
    private TextView lab_phone;
    @ViewInject(R.id.lab_time)
    private TextView lab_time;
    @ViewInject(R.id.lab_message)
    private TextView lab_message;
    @ViewInject(R.id.lab_ship)
    private TextView lab_ship;
    @ViewInject(R.id.lab_input)
    private EditText lab_input;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddBack();
        AddTitle("发货单详情");
        GetServerInfo();
    }

    public void GetServerInfo() {
        adapter = new DeliveryAdapter(this);
        main_view.setAdapter(adapter);
        adapter1 = new Delivery1Adapter(this);
        main_view1.setAdapter(adapter1);
        if (isloading) {
            return;
        }
        isloading = true;
        JSONObject para = new JSONObject();
        try {
            para.put("delivery_id", GetIntentData("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AsynServer.BackObject(DeliveryActivity.this,"seller/delivery_detail", para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {

                try {
                    JSONObject jon = sender.getJSONObject("data");
                    JSONObject jon1 = jon.getJSONObject("delivery_order");
                    lab_number.setText(jon1.getString("delivery_sn") + "(" + jon1.getString("formated_update_time") + ")");
                    lab_number1.setText(jon1.getString("order_sn") + "(" + jon1.getString("formated_add_time") + ")");
                    lab_name.setText(jon1.getString("user_name"));
                    lab_express.setText(jon1.getString("shipping_name"));
                    lab_inputnumber.setText(jon1.getString("invoice_no"));
                    lab_Consignee.setText(jon1.getString("consignee"));
                    lab_address.setText(jon1.getString("region"));
                    lab_phone.setText(jon1.getString("tel"));
                    lab_time.setText(jon1.getString("best_time"));
                    lab_message.setText(jon1.getString("postscript"));
                    ship = jon.getString("operable_list");
                    if ("[\"unship\"]".equals(jon.getString("operable_list"))) {
                        lab_ship.setText("取消发货");
                    } else if ("[\"ship\"]".equals(jon.getString("operable_list"))) {
                        lab_ship.setText("发货");
                    }
                    lab_ship.setOnClickListener(DeliveryActivity.this);
                    ArrayList<JSONObject> json = new ArrayList<JSONObject>();
                    JSONArray arr = jon.getJSONArray("goods_list");
                    for (int i = 0; i < arr.length(); i++) {
                        json.add(arr.getJSONObject(i));
                    }
                    adapter.addData(json);
                    Default.fixListViewHeight(main_view);

                    ArrayList<JSONObject> json1 = new ArrayList<JSONObject>();
                    JSONArray arr1 = jon.getJSONArray("action_list");
                    for (int i = 0; i < arr1.length(); i++) {
                        json1.add(arr1.getJSONObject(i));
                    }
                    adapter1.addData(json1);
                    Default.fixListViewHeight(main_view1);
                    pageindex++;
                    isloading = false;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lab_ship:
                //region ship
                if ("[\"unship\"]".equals(ship)) {
                    JSONObject para = new JSONObject();
                    try {
                        para.put("delivery_id", GetIntentData("id"));
                        para.put("operation","unship");
                        para.put("action_note",lab_input.getText());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    AsynServer.BackObject(DeliveryActivity.this ,"seller/delivery_detail", para, new AsynServer.BackObject() {
                        @Override
                        public void Back(JSONObject sender) {
                            String id= String.valueOf(GetIntentData("id"));
                            Intent intent=new Intent();
                            intent.putExtra("id",id);
                            AddActivity(DeliveryActivity.class,0,intent);
                            finish();
                        }
                    });
                } else if ("[\"ship\"]".equals(ship)) {
                    JSONObject para = new JSONObject();
                    try {
                        para.put("delivery_id", GetIntentData("id"));
                        para.put("operation","ship");
                        para.put("action_note",lab_input.getText());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AsynServer.BackObject(DeliveryActivity.this,"seller/delivery_detail", para, new AsynServer.BackObject() {
                        @Override
                        public void Back(JSONObject sender) {
                            String id= String.valueOf(GetIntentData("id"));
                            Intent intent=new Intent();
                            intent.putExtra("id",id);
                            AddActivity(DeliveryActivity.class,0,intent);
                            finish();
                        }
                    });
                }
                //endregion
                break;
            default:
        }
    }

    private class DeliveryAdapter extends BaseAdapter {
        private ArrayList<JSONObject> data;
        private Context content;


        private DeliveryAdapter(Context context) {
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

            DeliveryView view;
            if (convertView != null) {
                view = (DeliveryView) convertView;
            } else {
                view = new DeliveryView(this.content);
            }
            JSONObject[] arr1;
            int index = position;
            arr1 = new JSONObject[]{this.data.get(index)};
            view.setInfo(arr1);
            return view;
        }
    }

    private class Delivery1Adapter extends BaseAdapter {
        private ArrayList<JSONObject> data1;
        private Context content;


        private Delivery1Adapter(Context context) {
            this.content = context;
            this.data1 = new ArrayList<>();
        }


        public void addData(ArrayList<JSONObject> data) {
            this.data1.addAll(data);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            int count = this.data1.size();
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
