package com.wzzc.NextIndex.views.e.other_activity.NextRelation;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.base.new_base.NewBaseActivity;
import com.wzzc.new_index.NomalAdapter;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.AsynServer.ListViewScrollDelegate;
import com.wzzc.other_function.FileInfo;
import com.wzzc.other_function.JsonClass;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/8/15.
 * 购物详情
 */
@ContentView(R.layout.activity_relationsshopdetail)
public class RelationShopDetail extends NewBaseActivity implements ListViewScrollDelegate{
    @ViewInject(R.id.listView)
    ListView listView;
    @ViewInject(R.id.layout_noMore)
    RelativeLayout layout_noMore;

    public static final String REFPerson = "referralPerson";

    private ReferralPerson referralPerson;
    private NomalAdapter myAdapter;
    boolean initFirst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        referralPerson = bundle.getParcelable(REFPerson);

    }

    @Override
    protected void init() {
        initFirst = true;
        myAdapter = new MyAdapter(this);
        listView.setAdapter(myAdapter);
        JSONObject para = new JSONObject();
        try {
            para.put("user_id",referralPerson.getUserID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initDataFromServer(this,"user/recommend_report_detail",true,para,listView,null);
    }

    @Override
    protected String getFileKey() {
        return "RelationShopDetail" + referralPerson.getUserID();
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
        JSONObject data = (JSONObject) obj;
        JSONArray jrr_pre_order = JsonClass.jrrj(data,"pre_order");
        ArrayList<Object> arrayList = new ArrayList<>();
        for (int i = 0 ; i < jrr_pre_order.length() ; i ++) {
            JSONObject json = JsonClass.jjrr(jrr_pre_order,i);
            Shopping shopping = new Shopping(JsonClass.sj(json,"taobaoke_order_id"),JsonClass.sj(json,"rebate_commission"));
            arrayList.add(shopping);
        }
        myAdapter.addData(arrayList);
        if (initFirst && arrayList.size() == 0) {
            layout_noMore.setVisibility(View.VISIBLE);
        } else {
            layout_noMore.setVisibility(View.GONE);
        }
        initFirst = false;
    }

    @Override
    protected void newServerDataFromServer(JSONObject sender, String s) {
        super.newServerDataFromServer(sender, s);
        AsynServer.wantShowDialog = false;
        myAdapter.clearData();
    }

    @Override
    protected void publish() {
        JSONObject para = new JSONObject();
        try {
            para.put("user_id",referralPerson.getUserID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initDataFromServer(this,"user/recommend_report_detail",!FileInfo.IsAtUserString(getFileKey(),this),para,listView,null);
    }

    @Override
    public void onClick(View v) {

    }

    //region ListViewDelegate
    @Override
    public void dismissToastComponent() {

    }

    @Override
    public void showToastComponent() {

    }

    @Override
    public void scrollChanged(int state) {

    }
    //endregion

    class MyAdapter extends NomalAdapter {

        public MyAdapter(Context c) {
            super(c);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {
                convertView = LayoutInflater.from(c).inflate(R.layout.layout_relations_shop_detail_item, null);
                vh = new ViewHolder();
                vh.tv_cm = (TextView) convertView.findViewById(R.id.tv_cm);
                vh.tv_id = (TextView) convertView.findViewById(R.id.tv_id);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            vh.tv_id.setText(((Shopping) getItem(position)).getOrderNumber());
            vh.tv_cm.setText(((Shopping) getItem(position)).getReBackMoney());

            return convertView;
        }

        class ViewHolder {
            TextView tv_id, tv_cm;
        }
    }
}
