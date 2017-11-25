package com.wzzc.index.home.h.main_view.item;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.OnClick;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.production.detail.gcb.DetailGcbActivity;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zcyb365 on 2016/11/18.
 */
public class NearOneView extends BaseView {
    @ViewInject(R.id.imageview_003)
    private RelativeLayout imageview_003;
    @ViewInject(R.id.lab_money3)
    private TextView lab_money3;
    @ViewInject(R.id.lab_name3)
    private TextView lab_name3;
    @ViewInject(R.id.imageview_002)
    private RelativeLayout imageview_002;
    @ViewInject(R.id.lab_money2)
    private TextView lab_money2;
    @ViewInject(R.id.lab_name2)
    private TextView lab_name2;
    @ViewInject(R.id.imageview_001)
    private RelativeLayout imageview_001;
    @ViewInject(R.id.lab_money1)
    private TextView lab_money1;
    @ViewInject(R.id.lab_name1)
    private TextView lab_name1;
    @ViewInject(R.id.imageview_000)
    private RelativeLayout imageview_000;
    @ViewInject(R.id.lab_money)
    private TextView lab_money;
    @ViewInject(R.id.tv_name)
    private TextView lab_name;
    @ViewInject(R.id.lay_01)
    private RelativeLayout lay1;
    @ViewInject(R.id.lay_02)
    private RelativeLayout lay2;
    @ViewInject(R.id.lay_03)
    private RelativeLayout lay3;
    @ViewInject(R.id.lay_04)
    private RelativeLayout lay4;

    private ExtendImageView img_view1;
    private ExtendImageView img_view2;
    private ExtendImageView img_view3;
    private ExtendImageView img_view4;
    private int[] idlist = new int[4];

    public NearOneView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NearOneView(Context context) {
        super(context);
        img_view1 = ExtendImageView.Create(imageview_000);
        img_view2 = ExtendImageView.Create(imageview_001);
        img_view3 = ExtendImageView.Create(imageview_002);
        img_view4 = ExtendImageView.Create(imageview_003);
    }

    public void setInfo(JSONObject[] data) {
        try {
            if (data.length == 1) {
                lay1.setVisibility(VISIBLE);
                idlist[0]=data[0].getInt("goods_id");
                lab_money.setText(data[0].getString("shop_price"));
                lab_name.setText(data[0].getString("goods_name"));
                img_view1.setPath(data[0].getString("goods_thumb"));
            }else if (data.length==2){
                lay2.setVisibility(VISIBLE);
                idlist[1]=data[1].getInt("goods_id");
                lab_money1.setText(data[1].getString("shop_price"));
                lab_name1.setText(data[1].getString("goods_name"));
                img_view2.setPath(data[1].getString("goods_thumb"));
            }else if (data.length==3){
                lay3.setVisibility(VISIBLE);
                idlist[2]=data[2].getInt("goods_id");
                lab_money2.setText(data[2].getString("shop_price"));
                lab_name2.setText(data[2].getString("goods_name"));
                img_view3.setPath(data[2].getString("goods_thumb"));
            }else if (data.length==4){
                lay4.setVisibility(VISIBLE);
                idlist[3]=data[3].getInt("goods_id");
                lab_money3.setText(data[3].getString("shop_price"));
                lab_name3.setText(data[3].getString("goods_name"));
                img_view4.setPath(data[3].getString("goods_thumb"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.lay_01, R.id.lay_02, R.id.lay_03, R.id.lay_04})
    public void btn_bottom_click(RelativeLayout view) {
        int tag = Integer.parseInt(view.getTag().toString());
        Intent intent = new Intent();
        intent.putExtra(DetailGcbActivity.GOODSID,idlist[tag]);
        GetBaseActivity().AddActivity(DetailGcbActivity.class, 0, intent);
    }

}
