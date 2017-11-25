package com.wzzc.NextIndex.views.e.other_activity.BusinessShop.data;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.extendView.ExtendListView;
import com.wzzc.NextIndex.views.e.other_activity.BusinessShop.data.main_view.MerchantDataView;
import com.wzzc.NextIndex.views.e.LoginActivity;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zcyb365 on 2016/12/30.
 */
public class MerchantDataActivity extends BaseActivity {

    @ViewInject(R.id.tv_name)
    private TextView lab_name;
    @ViewInject(R.id.lab_number)
    private TextView lab_number;
    @ViewInject(R.id.lab_lastnumber)
    private TextView lab_lastnumber;
    @ViewInject(R.id.lab_lastmoney)
    private TextView lab_lastmoney;
    @ViewInject(R.id.lab_lastnumber1)
    private TextView lab_lastnumber1;
    @ViewInject(R.id.lab_lastmoney1)
    private TextView lab_lastmoney1;

    private CollectionAdapter adapter;
    @ViewInject(R.id.main_view)
    private ExtendListView main_view;
    @ViewInject(R.id.lab_noshopping)
    private LinearLayout lab_noshopping;
    TextView footertextView;
    private boolean isloading = false;
    private int pageindex = 1;
    private int pagecount = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddBack();
        AddTitle("商铺数据报表");

        footertextView = new TextView(this);
        footertextView.setText("正在加载...");
        footertextView.setTextSize(16);
        footertextView.setGravity(Gravity.CENTER);
        footertextView.setTextColor(Color.parseColor("#999999"));
        main_view.addFooterView(footertextView);
        AbsListView.LayoutParams para = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, Default.dip2px(44, this));
        footertextView.setLayoutParams(para);
    }

    @Override
    protected void viewFirstLoad() {
        adapter = new CollectionAdapter(this);
        main_view.setAdapter(adapter);
        GetServerInfo();
    }

    public void GetServerInfo() {
        if (isloading) {
            return;
        }
        isloading = true;
        JSONObject para = new JSONObject();
        JSONObject sender = Default.GetSession();
        try {
            JSONObject filter = new JSONObject();
            filter.put("uid", sender.getString("uid"));
            filter.put("sid", sender.getString("sid"));
            para.put("session", filter);
            para.put("user_id", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(MerchantDataActivity.this , "seller/datacharts", para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                try {
                    lab_name.setText(sender.getJSONObject("data").getString("map_street"));
                    lab_number.setText("账号："+sender.getJSONObject("data").getString("user_name"));
                    lab_lastnumber.setText(sender.getJSONObject("data").getString("order_count_sum"));
                    lab_lastmoney.setText(sender.getJSONObject("data").getString("order_goods_sum"));
                    lab_lastnumber1.setText(sender.getJSONObject("data").getString("other_order_goods_sum"));
                    lab_lastmoney1.setText(sender.getJSONObject("data").getString("other_order_goods_sum"));
                    ArrayList<JSONObject> json = new ArrayList<JSONObject>();
                    JSONObject status=sender.getJSONObject("status");
                    if (status.getInt("succeed")==0){
                        AddActivity(LoginActivity.class);
                    }else {
                        JSONArray arr = sender.getJSONObject("data").getJSONArray("store_code");
                        if (arr.length() == 0) {
                            lab_noshopping.setVisibility(View.VISIBLE);
                        } else {
                            for (int i = 0; i < arr.length(); i++) {
                                json.add(arr.getJSONObject(i));
                            }
                        }

                        adapter.addData(json);
                        main_view.removeFooterView(footertextView);
                        pageindex++;
                        isloading = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class CollectionAdapter extends BaseAdapter {
        private ArrayList<JSONObject> data;
        private Context content;


        public CollectionAdapter(Context context) {
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

            MerchantDataView view;
            if (convertView != null) {
                view = (MerchantDataView) convertView;
            } else {
                view = new MerchantDataView(this.content);
            }
            JSONObject[] arr1;
            int index = position;
            arr1 = new JSONObject[]{this.data.get(index)};
            view.setInfo(arr1);
            return view;
        }
    }

}
