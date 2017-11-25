package com.wzzc.onePurchase;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.onePurchase.activity.OnePurchaseIndexActivity;
import com.wzzc.onePurchase.item.OnePurchaseLatestAnnouncementItem;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by toutou on 2017/3/21.
 */

public class OnePurchaseMainActivity extends BaseActivity {
    @ViewInject(R.id.main_view)
    ListView main_view;
    @ViewInject(R.id.button)
    Button button;
    String url;
    CurrentAdapter cAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cAdapter = new CurrentAdapter(this);
        main_view.setAdapter(cAdapter);
        getServerInfo();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddActivity(OnePurchaseIndexActivity.class);
            }
        });

    }

    private void getServerInfo () {
        url = "http://test.zcgj168.com/data/afficheimg/images/zones/banner01.png";
        ArrayList<JSONObject> data = new ArrayList<>();
        for (int i = 0 ; i < 100 ; i ++) {
            JSONObject json = new JSONObject();
            try {
                json.put("goods_id","" + i);
                json.put("img",url);
                json.put("winMan","获胜者" + i);
                json.put("productionName","产品" + i);
                json.put("nowPrice","$100.00");
                json.put("hasEnteredNumber",i+1);
                json.put("allNeedNumber",100);
                json.put("remainingEnterNumber",100 - i - 1);
                json.put("announcement_time","2008/9/10 13:28:46");
                json.put("shortTime",i + 45);
                json.put("userIconPath",url);
                json.put("luckyCode","1000864s");
                json.put("userName","林俊杰");
                json.put("remaindNumber",60);
                json.put("rec_id","" + i);
                json.put("avatarPath",url);
                json.put("showTime","2008/9/10");
                json.put("buyTime","2008/9/10");
                StringBuilder stringBuilder_0 = new StringBuilder();
                StringBuilder stringBuilder_1 = new StringBuilder();

                for (int j = 0 ; j < i ; j ++) {
                    stringBuilder_0.append("这里是0阶第" + (i + j) + "条评论");
                    stringBuilder_1.append("这里是1阶第" + (i + j) + "条评论");

                }
                json.put("comments_0",stringBuilder_0);
                json.put("comments_1",stringBuilder_1);
                JSONArray jrr_commentImages = new JSONArray();
//                for (int k = 0 ; k < 3 ; k ++) {
                    jrr_commentImages.put(url);
//                }
                json.put("commentImages",jrr_commentImages);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            data.add(json);
        }
        cAdapter.addData(data);
        Default.fixListViewHeight(main_view);
    }

    class CurrentAdapter extends BaseAdapter{

        Context c;
        ArrayList<JSONObject> data;
        public CurrentAdapter (Context c) {
            this.c = c;
            this.data = new ArrayList<>();
        }

        public void addData (ArrayList<JSONObject> data){
            this.data.addAll(data);
            notifyDataSetChanged();
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
            OnePurchaseLatestAnnouncementItem item;
            if (convertView == null) {
                convertView = new OnePurchaseLatestAnnouncementItem(c);
            }
            item = (OnePurchaseLatestAnnouncementItem) convertView;
            JSONObject json = data.get(position);
            try {
                String goods_id = json.getString("goods_id");
                String imgPath = json.getString("img");
                String winMan = json.getString("winMan");
                String productionName = json.getString("productionName");
                String nowPrice = json.getString("nowPrice");
                Integer hasEnteredNumber = json.getInt("hasEnteredNumber");
                Integer allNeedNumber = json.getInt("allNeedNumber");
                Integer remainingEnterNumber = json.getInt("remainingEnterNumber");
                String announcement_time = json.getString("announcement_time");
                Long shortTime = json.getLong("shortTime");
                String userIconPath = json.getString("userIconPath");
                String buyTime = json.getString("buyTime");
                String userName = json.getString("userName");
                String luckyCode = json.getString("luckyCode");
                Integer remaindNumber = json.getInt("remaindNumber");
                String rec_id = json.getString("rec_id");
                String avatarPath = json.getString("avatarPath");
                String showTime = json.getString("showTime");
                String comments_0 = json.getString("comments_0");
                String comments_1 = json.getString("comments_1");
                JSONArray jrr_commentImages = json.getJSONArray("commentImages");

                ArrayList<String> arrayListCommentImages = new ArrayList<>();
                for (int i = 0 ; i < position + 1 ; i ++) {

                    arrayListCommentImages.add(url);
                }

                item.setInfo(goods_id,imgPath,userIconPath,userName,hasEnteredNumber,luckyCode,announcement_time);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return item;
        }
    }
}
