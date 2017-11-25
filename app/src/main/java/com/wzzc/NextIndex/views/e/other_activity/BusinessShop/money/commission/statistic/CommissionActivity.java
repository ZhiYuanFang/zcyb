package com.wzzc.NextIndex.views.e.other_activity.BusinessShop.money.commission.statistic;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.extendView.ExtendListView;
import com.wzzc.NextIndex.views.e.LoginActivity;
import com.wzzc.NextIndex.views.e.other_activity.BusinessShop.money.commission.statistic.main_view.CommissionView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zcyb365 on 2016/11/24.
 */
public class CommissionActivity extends BaseActivity {
    private CollectionAdapter adapter;
    @ViewInject(R.id.main_view)
    private ExtendListView main_view;
    @ViewInject(R.id.lab_noshopping)
    private LinearLayout lab_noshopping;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddBack();
        AddTitle("商家佣金统计");
    }

    @Override
    protected void viewFirstLoad() {
        adapter = new CollectionAdapter(this);
        main_view.setAdapter(adapter);
        GetServerInfo();
    }

    public void GetServerInfo() {
        JSONObject para = new JSONObject();
        AsynServer.BackObject(CommissionActivity.this, "seller/rebate_list", para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                try {
                    ArrayList<JSONObject> json = new ArrayList<JSONObject>();
                    JSONObject status=sender.getJSONObject("status");
                    if (status.getInt("succeed")==0){
                        AddActivity(LoginActivity.class);
                    }else {
                        JSONArray arr = sender.getJSONArray("data");
                        if (arr.length() == 0) {
                            lab_noshopping.setVisibility(View.VISIBLE);
                        } else {
                            for (int i = 0; i < arr.length(); i++) {
                                json.add(arr.getJSONObject(i));
                            }
                        }
                        adapter.addData(json);
                        Default.fixListViewHeight(main_view);
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

            CommissionView view;
            if (convertView != null) {
                view = (CommissionView) convertView;
            } else {
                view = new CommissionView(this.content);
            }
            JSONObject[] arr1;
            int index = position;
            arr1 = new JSONObject[]{this.data.get(index)};
            view.setInfo(arr1);
            return view;
        }
    }

}
