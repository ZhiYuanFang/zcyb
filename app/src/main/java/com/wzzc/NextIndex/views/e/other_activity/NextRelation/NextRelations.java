package com.wzzc.NextIndex.views.e.other_activity.NextRelation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.wzzc.NextIndex.views.e.other_activity.NextRelation.main_view.RelationDetail;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.base.new_base.NewBaseActivity;
import com.wzzc.new_index.NomalAdapter;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.AsynServer.ListViewScrollDelegate;
import com.wzzc.other_function.JsonClass;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/8/15.
 */
@ContentView(R.layout.next_relation)
public class NextRelations extends NewBaseActivity implements ListViewScrollDelegate , RelationDetailItemDelegate{
    public static final String REFPerson = "referralPerson";

    private static final String FIRST ="10011";
    private static final String SECOND ="10012";

    @ViewInject(R.id.bt_first)
    Button bt_first;
    @ViewInject(R.id.bt_second)
    Button bt_second;
    @ViewInject(R.id.layout_first)
    RelativeLayout layout_first;
    @ViewInject(R.id.layout_second)
    RelativeLayout layout_second;
    @ViewInject(R.id.lv_first)
    ListView lv_first;
    @ViewInject(R.id.lv_second)
    ListView lv_second;
    @ViewInject(R.id.noDataLayout)
    RelativeLayout noDataLayout;

    ArrayList<Button> buttonArrayList;//按钮集合
    ArrayList<RelativeLayout> layoutArrayList;//界面集合
    ReferralPerson referralPerson;//当前推介对象

    int level;//直推0，间推1
    NomalAdapter firstAdapter;
    NomalAdapter secondAdapter;
    boolean firstInit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        referralPerson = bundle.getParcelable(REFPerson);
        buttonArrayList = new ArrayList<Button>() {{
            add(bt_first);
            add(bt_second);
        }};
        for (Button button : buttonArrayList) {
            button.setOnClickListener(this);
        }

        layoutArrayList = new ArrayList<RelativeLayout>() {{
            add(layout_first);
            add(layout_second);
        }};
    }

    @Override
    protected void init() {
        bt_first.callOnClick();
    }

    @Override
    protected String getFileKey() {
        return referralPerson.getUserID() + level;
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
        bt_first.setText("直推(" + JsonClass.sj(data,"direct_users_total") + ")");
        bt_second.setText("间推(" + JsonClass.sj(data,"indirect_users_total") + ")");
        JSONArray jrrList = JsonClass.jrrj(data,"list");
        ArrayList<Object> arrayList = new ArrayList<>();
        for (int i = 0 ; i < jrrList.length() ; i ++) {
            JSONObject json = JsonClass.jjrr(jrrList,i);
            ReferralPerson referralPerson = new ReferralPerson(
                    JsonClass.sj(json,"user_id"),
                    JsonClass.sj(json,"user_name"),
                    JsonClass.sj(json,"mobile"),
                    JsonClass.sj(json,"avatar"),
                    null,
                    JsonClass.ij(json,"direct_count"));
            arrayList.add(referralPerson);
        }
        switch (s){
            case FIRST:
                firstAdapter.addData(arrayList);
                break;
            case SECOND:
                secondAdapter.addData(arrayList);
                break;
            default:
        }
        if (arrayList.size() == 0) {
            noDataLayout.setVisibility(View.VISIBLE);
        } else {
            noDataLayout.setVisibility(View.GONE);
        }

        firstInit = false;
    }

    @Override
    protected void newServerDataFromServer(JSONObject sender, String s) {
        super.newServerDataFromServer(sender, s);
        AsynServer.wantShowDialog = false;
        switch (s){
            case FIRST:
                firstAdapter.clearData();
                break;
            case SECOND:
                secondAdapter.clearData();
                break;
            default:
        }
    }

    @Override
    protected void publish() {
        bt_first.callOnClick();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_first:
                newServer = true;
                firstInit = true;
                showFirstReferral();
                break;
            case R.id.bt_second:
                newServer = true;
                firstInit = true;
                showSecondReferral();
                break;
            default:
        }
    }

    //region Method
    public void showFirstReferral() {
        if (firstAdapter == null) {
            firstAdapter = new FirstAdapter(this);
            lv_first.setAdapter(firstAdapter);
        }
        defaultAllSelect();
        focusSelect(bt_first);
        level = 0;
        JSONObject para = new JSONObject();
        try {
            para.put("keywords","");
            para.put("level",level);
            para.put("parent_user_id",referralPerson.getUserID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initDataFromServer(this,"user/recommend_report",firstInit,para,lv_first,FIRST);
    }

    public void showSecondReferral() {
        if (secondAdapter == null) {
            secondAdapter = new SecondAdapter(this);
            lv_second.setAdapter(secondAdapter);
        }
        defaultAllSelect();
        focusSelect(bt_second);
        level = 1;
        JSONObject para = new JSONObject();
        try {
            para.put("keywords","");
            para.put("level",level);
            para.put("parent_user_id",referralPerson.getUserID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initDataFromServer(this,"user/recommend_report",firstInit,para,lv_second,SECOND);
    }
    //endregion

    //region Helper
    private void defaultAllSelect () {
       for (Button button : buttonArrayList) {
           button.setBackgroundColor(ContextCompat.getColor(this,R.color.alibc_transparent));
           button.setTextColor(ContextCompat.getColor(this,R.color.tv_Black));
       }

       for (RelativeLayout layout : layoutArrayList) {
           layout.setVisibility(View.GONE);
       }
    }

    private void focusSelect (Button button) {
        button.setBackground(ContextCompat.getDrawable(this,R.drawable.drawable_bottom_red));
        button.setTextColor(ContextCompat.getColor(this,R.color.tv_Red));
        switch (button.getId()){
            case R.id.bt_first:
                layout_first.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_second:
                layout_second.setVisibility(View.VISIBLE);
                break;
            default:
        }
    }

    //endregion

    //region ListViewScrollDelegate
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

    //region RelationDetailItemDelegate
    @Override
    public void lookShopDetail(int position, ReferralPerson referralPerson) {
        switch (level){
            case 0:{//直推
                Intent intent = new Intent();
                intent.putExtra(RelationDetail.REFPerson,referralPerson);
                AddActivity(RelationDetail.class,0,intent);
                break;}
            case 1:{//间推
                Intent intent = new Intent();
                intent.putExtra(RelationShopDetail.REFPerson,referralPerson);
                AddActivity(RelationShopDetail.class,0,intent);
                break;}
            default:
        }
    }
    //endregion

    //region MyAdapter
    private class FirstAdapter extends NomalAdapter{
        FirstAdapter(Context c) {
            super(c);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (!(convertView instanceof ReferralFirstItem)) {
                convertView = new ReferralFirstItem(c);
            }
            ((ReferralFirstItem)convertView).setInfo(NextRelations.this,(ReferralPerson) getItem(position),position);
            return convertView;
        }
    }

    private class SecondAdapter extends NomalAdapter {
        SecondAdapter(Context c) {
            super(c);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (!(convertView instanceof ReferralSecondItem)) {
                convertView = new ReferralSecondItem(c);
            }
            ((ReferralSecondItem)convertView).setInfo(NextRelations.this,(ReferralPerson) getItem(position),position);
            return convertView;
        }
    }
    //endregion
}
