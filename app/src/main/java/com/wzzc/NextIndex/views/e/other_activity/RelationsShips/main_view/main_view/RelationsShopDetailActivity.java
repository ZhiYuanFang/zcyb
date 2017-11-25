package com.wzzc.NextIndex.views.e.other_activity.RelationsShips.main_view.main_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wzzc.base.ExtendBaseActivity;
import com.wzzc.base.ExtendBaseView;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.FileInfo;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/6/13.
 */
@ContentView(R.layout.activity_relationsshopdetail)
public class RelationsShopDetailActivity extends ExtendBaseActivity {
    private static final String TAG = "RelationsShopDetailActivity";
    public static final String USEID = "user_id";
    String user_id;
    //region ```
    @ViewInject(R.id.listView)
    ListView listView;
    //endregion
    MyAdapter myAdapter;

    @Override
    protected void init() {
        user_id = (String) GetIntentData(USEID);
        myAdapter = new MyAdapter(this);
        listView.setAdapter(myAdapter);
        super.init();
    }

    @Override
    protected String getFileKey() {
        return TAG + user_id;
    }

    @Override
    protected ExtendBaseView.ServerCallBack serverCallBack() {
        return new ExtendBaseView.ServerCallBack() {
            @Override
            public void callBack(Object json_data) {
                JSONObject sender = (JSONObject) json_data;
                try {
                    JSONArray jrr_pre_order = sender.getJSONArray("pre_order");
                    ArrayList<JSONObject> arr = new ArrayList<>();
                    for (int i = 0 ; i < jrr_pre_order.length() ; i ++) {
                        arr.add(jrr_pre_order.getJSONObject(i));
                    }
                    myAdapter.addData(arr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    protected void setInfoFromService(String type) {
        JSONObject para = new JSONObject();
        try {
            para.put("user_id",user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(this, "user/recommend_report_detail", true, listView, para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                if (firstInitFromFile) {
                    myAdapter.clearData();
                    firstInitFromFile = false;
                }
                FileInfo.SetUserString(getFileKey(),sender.toString(),RelationsShopDetailActivity.this);
                try {
                    initialized(sender,serverCallBack());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class MyAdapter extends BaseAdapter {
        Context c;
        ArrayList<JSONObject> data;
        MyAdapter (Context c ) {
            this.c = c;
            this.data = new ArrayList<>();
        }

        public void addData (ArrayList<JSONObject> data) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        public void clearData () {
            data = new ArrayList<>();
        }
        @Override
        public int getCount() {
            return data.size();
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
            ViewHolder vh ;
            if (convertView == null) {
                convertView = LayoutInflater.from(c).inflate(R.layout.layout_relations_shop_detail_item,null);
                vh = new ViewHolder();
                vh.tv_cm = (TextView) convertView.findViewById(R.id.tv_cm);
                vh.tv_id = (TextView) convertView.findViewById(R.id.tv_id);
                vh.tv_proName = (TextView) convertView.findViewById(R.id.tv_production_name);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            try {
                vh.tv_id.setText(data.get(position).getString("taobaoke_order_id"));
                vh.tv_proName.setText(data.get(position).getString("goods_name"));
                vh.tv_cm.setText(data.get(position).getString("rebate_commission"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return convertView;
        }

        class ViewHolder {
            TextView tv_id , tv_proName , tv_cm;
        }
    }
}
