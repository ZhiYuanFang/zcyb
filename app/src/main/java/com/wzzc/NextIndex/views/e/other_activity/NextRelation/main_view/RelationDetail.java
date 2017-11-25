package com.wzzc.NextIndex.views.e.other_activity.NextRelation.main_view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.NextIndex.views.e.other_activity.NextRelation.ReferralPerson;
import com.wzzc.NextIndex.views.e.other_activity.NextRelation.RelationDetailItemDelegate;
import com.wzzc.NextIndex.views.e.other_activity.NextRelation.RelationShopDetail;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.base.new_base.NewBaseActivity;
import com.wzzc.new_index.NomalAdapter;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.AsynServer.ListViewScrollDelegate;
import com.wzzc.other_function.FileInfo;
import com.wzzc.other_function.ImageHelper;
import com.wzzc.other_function.JsonClass;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/8/15.
 * 详情页
 */
@ContentView(R.layout.relation_detail)
public class RelationDetail extends NewBaseActivity implements ListViewScrollDelegate , RelationDetailItemDelegate{
    public static final String REFPerson = "referralPerson";

    @ViewInject(R.id.listView)
    ListView listView;
    @ViewInject(R.id.tv_number)
    TextView tv_number;
    @ViewInject(R.id.tv_cmi)
    TextView tv_cmi;
    @ViewInject(R.id.tv_name)
    TextView tv_name;
    @ViewInject(R.id.tv_phone)
    TextView tv_phone;
    @ViewInject(R.id.iv_head)
    ImageView iv_head;
    @ViewInject(R.id.layout_detail)
    RelativeLayout layout_detail;
    @ViewInject(R.id.imb_detail)
            ImageView imb_detail;
    @ViewInject(R.id.layout_noMore)
            RelativeLayout layout_noMore;
    ReferralPerson referralPerson;
    MyAdapter myAdapter;
    boolean firstInit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        referralPerson = bundle.getParcelable(REFPerson);
    }

    @Override
    protected void init() {
        firstInit = true;
        layout_detail.setOnClickListener(this);
        myAdapter = new MyAdapter(this);
        listView.setAdapter(myAdapter);
        JSONObject para = new JSONObject();
        try {
            para.put("keywords","");
            para.put("level",0);
            para.put("parent_user_id",referralPerson.getUserID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initDataFromServer(this,"user/recommend_report",!FileInfo.IsAtUserString(getFileKey(),this),para,listView,null);
    }

    @Override
    protected String getFileKey() {
        return "RelationDetail" + referralPerson.getUserID();
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
        JSONObject json_cur_user_info = JsonClass.jj(data,"cur_user_info");
        referralPerson.setName(JsonClass.sj(json_cur_user_info,"user_name"));
        referralPerson.setHeadUrl(JsonClass.sj(json_cur_user_info,"avatar"));
        referralPerson.setReBackMoney(JsonClass.sj(json_cur_user_info,"total"));
        referralPerson.setPhoneNumber(JsonClass.sj(json_cur_user_info,"mobile"));
        referralPerson.setDirectReferralNumber(Integer.valueOf(JsonClass.sj(json_cur_user_info,"direct_count")));
        setUserInfo(referralPerson);
        JSONArray jrr_list = JsonClass.jrrj(data,"list");
        ArrayList<Object> arrayList = new ArrayList<>();
        for (int i = 0 ; i < jrr_list.length() ; i ++) {
            JSONObject json = JsonClass.jjrr(jrr_list,i);
            ReferralPerson person = new ReferralPerson(
                    JsonClass.sj(json,"user_id"),
                    JsonClass.sj(json,"user_name"),
                    JsonClass.sj(json,"mobile"),
                    JsonClass.sj(json,"avatar"),
                    null,
                    JsonClass.ij(json,"direct_count"));
            arrayList.add(person);
        }
        myAdapter.addData(arrayList);
        Log.i("More ? ","firstInit --> " + firstInit + " size : " + arrayList.size() );
        if (firstInit && arrayList.size() == 0) {
            layout_noMore.setVisibility(View.VISIBLE);
        } else {
            layout_noMore.setVisibility(View.GONE);
        }
        firstInit = false;
    }

    private void setUserInfo (ReferralPerson person) {
        tv_cmi.setText(person.getReBackMoney());
        tv_name.setText(person.getName());
        tv_phone.setText(person.getPhoneNumber());
        tv_number.setText(String.valueOf(person.getDirectReferralNumber()));
        ImageHelper.handlerImage(this, referralPerson.getHeadUrl(), iv_head.getMeasuredWidth(), iv_head.getMeasuredHeight(), new ImageHelper.HandlerImage() {
            @Override
            protected void imageBack(Bitmap bitmap) {
                setHead(bitmap);
            }
        });
    }

    private void setHead(Bitmap bitmap) {
        if (bitmap == null) {
            iv_head.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.headimg));
            return;
        }
        float length = bitmap.getWidth() > bitmap.getHeight() ? bitmap.getHeight() / 2 : bitmap.getWidth() / 2;
        Bitmap bmp = null;
        try {
            bmp =  ImageHelper.tailorBitmapToCircle(bitmap, new PointF(bitmap.getWidth() / 2, bitmap.getHeight() / 2), length);;
        } catch (OutOfMemoryError outOfMemoryError) {
            outOfMemoryError.printStackTrace();
        }
        if (bmp == null) {
            iv_head.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.headimg));
        } else {
            iv_head.setImageBitmap(bmp);
        }
    }

    @Override
    protected void newServerDataFromServer(JSONObject sender, String s) {
        super.newServerDataFromServer(sender, s);
        AsynServer.wantShowDialog = false;
        myAdapter.clearData();
    }

    @Override
    protected void publish() {
        firstInit = true;
        myAdapter = new MyAdapter(this);
        listView.setAdapter(myAdapter);
        JSONObject para = new JSONObject();
        try {
            para.put("keywords","");
            para.put("level",0);
            para.put("parent_user_id",referralPerson.getUserID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initDataFromServer(this,"user/recommend_report",!FileInfo.IsAtUserString(getFileKey(),this),para,listView,null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_detail:
                Intent intent = new Intent();
                intent.putExtra(RelationShopDetail.REFPerson,referralPerson);
                AddActivity(RelationShopDetail.class,0,intent);
                break;
            default:
        }
    }

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
        Intent intent = new Intent();
        intent.putExtra(RelationShopDetail.REFPerson,referralPerson);
        AddActivity(RelationShopDetail.class,0,intent);
    }
    //endregion

    private class MyAdapter extends NomalAdapter {
        public MyAdapter(Context c) {
            super(c);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (!(convertView instanceof RelationDetailItem)){
                convertView = new RelationDetailItem(c);
            }
            ((RelationDetailItem)convertView).setInfo(RelationDetail.this, (ReferralPerson) getItem(position),position);
            return convertView;
        }
    }
}
