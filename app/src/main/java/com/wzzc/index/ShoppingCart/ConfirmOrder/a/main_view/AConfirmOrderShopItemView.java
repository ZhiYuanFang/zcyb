package com.wzzc.index.ShoppingCart.ConfirmOrder.a.main_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.index.ShoppingCart.ConfirmOrder.a.main_view.main_view.AConfirmOrderGoodsItemView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/6/20.
 */

public class AConfirmOrderShopItemView extends BaseView {
    //region ```
    @ViewInject(R.id.tv_shop)
    TextView tv_shop;
    @ViewInject(R.id.listView)
    ListView listView;
    @ViewInject(R.id.tv_money)
    TextView tv_money;
    @ViewInject(R.id.tv_number)
    TextView tv_number;

    //endregion

    String supplier_id;
    public AConfirmOrderShopItemView(Context context) {
        super(context);
        init();
    }

    public AConfirmOrderShopItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init (){
        tv_shop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/6/21 前往商铺 : supplier_id
                System.out.println("supplier_id : " + supplier_id);
            }
        });
    }

    /**
    *  {
                    "goods_list":Array[1],
                    "supplier_name":"非乐迪量贩KTV",
                    "supplier_id":"60",
                    "goods_number":2,
                    "sub_total":792,
                    "sub_total_format":"￥792.00"
                }*/
    public void setInfo(JSONObject json) {
        tv_money.setText(sj(json, "sub_total_format"));
        tv_number.setText("共"+ij(json,"goods_number")+"件商品");
        supplier_id = sj(json,"supplier_id");
        tv_shop.setText(sj(json,"supplier_name") + "(共" + ij(json,"goods_number") + "件)");
        try {
            JSONArray jrr_goods_list = json.getJSONArray("goods_list");
            ArrayList<JSONObject> data = new ArrayList<>();
            for (int i = 0 ; i < jrr_goods_list.length() ; i ++) {
                data.add(jrr_goods_list.getJSONObject(i));
            }
            listView.setAdapter(new MyAdapter(getContext(),data));
            Default.fixListViewHeight(listView);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class MyAdapter extends BaseAdapter{
        Context c ;
        ArrayList<JSONObject> data;
        MyAdapter(Context c , ArrayList<JSONObject> data){
            this.c = c;
            this.data = data;
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
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new AConfirmOrderGoodsItemView(c);
            }
            ((AConfirmOrderGoodsItemView)convertView).setInfo(getItem(position));
            return convertView;
        }
    }

    private String sj(JSONObject json , String key){
        try {
            return json.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return "error";
        }
    }

    private int ij(JSONObject json , String key){
        try {
            return json.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
