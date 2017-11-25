package com.wzzc.NextIndex.views.e.other_activity.RebateRanking;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wzzc.base.ExtendBaseActivity;
import com.wzzc.base.ExtendBaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.FileInfo;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by toutou on 2017/6/10.
 *
 * 返利排行榜
 */

public class RebateRankingActivity extends ExtendBaseActivity{
    private static final String TAG = "RebateRankingActivity";
    //region ````
    @ViewInject(R.id.listView)
    ListView listView;
    @ViewInject(R.id.eiv_top)
    ExtendImageView eiv_top;
    //endregion
    MyAdapter myAdapter;

    @Override
    protected void init() {
        initBackTouch();
        myAdapter = new MyAdapter(this);
        listView.setAdapter(myAdapter);
        super.init();
    }

    @Override
    protected String getFileKey() {
        return TAG;
    }

    @Override
    protected ExtendBaseView.ServerCallBack serverCallBack() {
        return new ExtendBaseView.ServerCallBack() {
            @Override
            public void callBack(Object json_data) {
                JSONObject data = (JSONObject) json_data;
                try {
                    eiv_top.setPath(data.getString("banner"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ArrayList<JSONObject> arr = new ArrayList<>();
                try {
                    JSONArray jrr = data.getJSONArray("rebate_ranking_list");
                    for (int i = 0 ; i < jrr.length() ; i ++){
                        arr.add(jrr.getJSONObject(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("``` arr.size : " + arr.size());
                myAdapter.addData(arr);
            }
        };
    }

    @Override
    protected void setInfoFromService(String type) {
        AsynServer.BackObject(this, "user/rebate_ranking", true, new JSONObject(), new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                FileInfo.SetUserString(getFileKey(),sender.toString(),RebateRankingActivity.this);
                if (firstInitFromFile) {
                    myAdapter.clearData();
                    firstInitFromFile = false;
                }
                AsynServer.wantShowDialog = false;
                try {
                    initialized(sender,serverCallBack());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class MyAdapter extends BaseAdapter {

        Context c;
        ArrayList<JSONObject> data;
        private MyAdapter (Context c) {
            this.c = c;
            data = new ArrayList<>();
        }

        void addData (ArrayList<JSONObject> data) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        void clearData () {
            data = new ArrayList<>();
        }
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = LayoutInflater.from(c).inflate(R.layout.rebate_ranking_item_layout,null);
                vh.img_rank = (ImageView) convertView.findViewById(R.id.img_rank);
                vh.tv_rank = (TextView) convertView.findViewById(R.id.tv_rank);
                vh.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
                vh.tv_reback_money = (TextView) convertView.findViewById(R.id.tv_reback_money);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            try {
                vh.tv_rank.setText("" + data.get(position).getInt("index"));
                switch (data.get(position).getInt("index")){
                    case 1:
                        vh.img_rank.setBackground(ContextCompat.getDrawable(c,R.drawable.reback_rank_1));
                        vh.tv_rank.setVisibility(View.GONE);
                        vh.img_rank.setVisibility(View.VISIBLE);
                        break;
                    case 2 :
                        vh.img_rank.setBackground(ContextCompat.getDrawable(c,R.drawable.reback_rank_2));
                        vh.tv_rank.setVisibility(View.GONE);
                        vh.img_rank.setVisibility(View.VISIBLE);

                        break;
                    case 3 :
                        vh.img_rank.setBackground(ContextCompat.getDrawable(c,R.drawable.reback_rank_3));
                        vh.tv_rank.setVisibility(View.GONE);
                        vh.img_rank.setVisibility(View.VISIBLE);

                        break;
                    default:
                        vh.tv_rank.setBackground(ContextCompat.getDrawable(c,R.drawable.reback_rank_nomal));
                        vh.tv_rank.setVisibility(View.VISIBLE);
                        vh.img_rank.setVisibility(View.GONE);

                }
                vh.tv_phone.setText(data.get(position).getString("user_name"));
                vh.tv_reback_money.setText(data.get(position).getString("user_total"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return convertView;
        }

        class ViewHolder {
            ImageView img_rank;
            TextView tv_rank , tv_phone , tv_reback_money;
        }
    }
}
