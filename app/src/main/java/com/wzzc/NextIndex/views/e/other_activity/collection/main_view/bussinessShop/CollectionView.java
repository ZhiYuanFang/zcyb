package com.wzzc.NextIndex.views.e.other_activity.collection.main_view.bussinessShop;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.other_view.production.detail.gcb.DetailGcbActivity;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.OnClick;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.NextIndex.views.e.other_activity.collection.CollectionActivity;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zcyb365 on 2016/10/24.
 */
public class CollectionView extends BaseView{

    @ViewInject(R.id.img_view)
    private RelativeLayout img_view;
    @ViewInject(R.id.tv_name)
    private TextView lab_name;
    @ViewInject(R.id.lab_money)
    private TextView lab_money;
    private ArrayList<String> mainid1 = new ArrayList<>();

    private int[] idlist = new int[1];
    private int[] idlist1 = new int[1];

    private ExtendImageView img_view1;

    public CollectionView(Context context) {
        super(context);
        img_view1 = ExtendImageView.Create(img_view);

    }
    public CollectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setInfo(JSONObject[] data) {
            try {
            lab_name.setText(data[0].getString("name"));
            lab_money.setText(data[0].getString("shop_price"));
            img_view1.setPath(data[0].getJSONObject("img").getString("url"));
                mainid1.add(data[0].getString("goods_id"));
            idlist1[0]=data[0].getInt("goods_id");
            idlist[0] = data[0].getInt("rec_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @OnClick({R.id.img_delete,R.id.img_view,R.id.lab_dj})
    public void btn_goto_onclick(View view) {
        int tag = Integer.parseInt(view.getTag().toString());
        if (tag==0){
            JSONObject para = new JSONObject();
            try {
                para.put("rec_id",idlist[tag]);
                para.put("del_store",0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            AsynServer.BackObject(GetBaseActivity() , "user/collect/delete", para, new AsynServer.BackObject() {
                @Override
                public void Back(JSONObject sender) {
                    GetBaseActivity().AddActivity(CollectionActivity.class);
                    GetBaseActivity().finish();
                }
            });
        }else{
            Intent intent = new Intent();
            intent.putExtra(DetailGcbActivity.GOODSID,idlist1[0]);
            GetBaseActivity().AddActivity(DetailGcbActivity.class, 0, intent);
        }

    }

}
