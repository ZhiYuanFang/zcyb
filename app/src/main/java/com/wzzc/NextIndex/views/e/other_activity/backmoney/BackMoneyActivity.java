package com.wzzc.NextIndex.views.e.other_activity.backmoney;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.wzzc.base.annotation.ViewInject;
import com.wzzc.base.new_base.NewBaseActivity;
import com.wzzc.NextIndex.views.e.other_activity.backmoney.main_view.BackMoneyView;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.AsynServer.ListViewScrollDelegate;
import com.wzzc.other_function.JsonClass;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/5/17.
 *
 * 返利订单
 */

public class BackMoneyActivity extends NewBaseActivity implements ListViewScrollDelegate{
    private static final String TAG = "BackMoneyActivity";
    @ViewInject(R.id.listView)
    ListView listView;
    MyAdapter myAdapter ;

    @Override
    protected void init() {
        initBackTouch();
        myAdapter = new MyAdapter(this);
        listView.setAdapter(myAdapter);
        showOrder();
    }

    @Override
    protected String getFileKey() {
        return TAG;
    }

    @Override
    protected CacheCallBack cacheCallBack() {
        return new CacheCallBack() {
            @Override
            public void callBack(Object obj, String s) {
                cb(obj,s);
            }
        };
    }

    @Override
    protected ServerCallBack serverCallBack() {
        return new ServerCallBack() {
            @Override
            public void callBack(Object obj, String s) {
                cb(obj,s);
            }
        };
    }

    private void cb (Object obj, String s) {
        JSONArray sender = (JSONArray) obj;
        ArrayList<JSONObject> data = new ArrayList<>();
        for (int i = 0 ; i < sender.length() ; i ++) {
            data.add(JsonClass.jjrr(sender,i));
        }
        myAdapter.addData(data);
    }

    public void showOrder () {
        initDataFromCache(null);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initDataFromServer(BackMoneyActivity.this,"SuperDiscount/order",true,new JSONObject(),listView,null);
            }
        },1);
    }

    @Override
    protected void publish() {
        showOrder();
    }

    @Override
    protected void newServerDataFromServer(JSONObject sender, String s) {
        super.newServerDataFromServer(sender, s);
        myAdapter.clearData();
        AsynServer.wantShowDialog = false;
    }
    @Override
    public void onClick(View v) {

    }

    @Override
    public void dismissToastComponent() {

    }

    @Override
    public void showToastComponent() {

    }

    @Override
    public void scrollChanged(int state) {

    }

    private class MyAdapter extends BaseAdapter{
        ArrayList<JSONObject> data ;
        Context context;
        private MyAdapter (Context context) {
            this.context = context;
            data = new ArrayList<>();
        }

        public void addData (ArrayList<JSONObject> data) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        public void clearData () {
            this.data = new ArrayList<>();
        }
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public JSONObject getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new BackMoneyView(context);
            }
            ((BackMoneyView)convertView).setInfo(getItem(position));
            return convertView;
        }
    }
}
