package com.wzzc.index.home.h.main_view.main_view.main_view.c;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.index.home.h.main_view.main_view.ShopDetailsActivity;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by toutou on 2017/5/2.
 * 附进商家店铺
 */

public class NearbyShopView extends BaseView {
    //region 组件
    @ViewInject(R.id.rv_img)
    ExtendImageView rv_img;
    @ViewInject(R.id.tv_distreet)
    TextView tv_distreet;
    @ViewInject(R.id.tv_name)
    TextView tv_name;
    @ViewInject(R.id.tv_fenshu)
    TextView tv_fenshu;
    @ViewInject(R.id.rb)
    RatingBar rb;
    @ViewInject(R.id.lv_shop)
    RelativeLayout lv_shop;
    //endregion
    private String supplier_id;
    private String supplier_name;
    private float pingfen;
    private String shortest_path;
    private int shortest_sort;
    private String logo;

    public NearbyShopView(Context context) {
        super(context);
        init();
    }

    private void init() {
        lv_shop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(ShopDetailsActivity.SELLER_ID, supplier_id);
                GetBaseActivity().AddActivity(ShopDetailsActivity.class, 0, intent);
            }
        });
    }

    public void setInfo(JSONObject sender) throws JSONException {
        supplier_id = sender.getString("supplier_id");
        logo = sender.getString("logo");
        supplier_name = sender.getString("supplier_name");
        Object obj_step = sender.get("pingfen");
        pingfen = Float.valueOf(String.valueOf(obj_step));
        if (pingfen == 0) {
            pingfen = 5;
        }
        shortest_path = sender.getString("shortest_path");
        shortest_sort = sender.getInt("shortest_sort");
        rv_img.radio = GetBaseActivity().getResources().getDimension(R.dimen.RoundRadio);;
        rv_img.setPath(logo);
        tv_name.setText(supplier_name);
        rb.setSelected(false);
        rb.setRating(pingfen);
        tv_distreet.setText(shortest_path);
        tv_fenshu.setText(pingfen + "分");
    }
}
