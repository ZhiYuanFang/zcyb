package com.wzzc.index.ShoppingCart.a.main_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.index.ShoppingCart.a.ACartDelegate;
import com.wzzc.index.ShoppingCart.a.main_view.main_view.ACartGoodsItemView;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by by Administrator on 2017/6/16.
 */

public class ACartShopItemView extends BaseView implements ACartDelegate {
    //region ```
    @ViewInject(R.id.check_shop)
    CheckBox check_shop;
    @ViewInject(R.id.img_go_shop)
    ImageView img_go_shop;
    @ViewInject(R.id.tv_full_reduction)
    TextView tv_full_reduction;
    @ViewInject(R.id.layout_full_reduction)
    RelativeLayout layout_full_reduction;
    @ViewInject(R.id.listView)
    ListView listView;
    @ViewInject(R.id.tv_shop_all)
    TextView tv_shop_all;
    //endregion
    ACartDelegate acd;
    MyAdapter myAdapter;
    ArrayList<ACartGoodsItemView> listGoodsView;
    String shop_id;

    public ACartShopItemView(ACartDelegate acd, Context context) {
        super(context);
        this.acd = acd;
        init();
    }

    public ACartShopItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        listGoodsView = new ArrayList<>();
        check_shop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (ACartGoodsItemView acgiv : listGoodsView) {
                    acgiv.check(isChecked);
                }
            }
        });
        img_go_shop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/6/17 前往商铺详情
            }
        });
    }

    /**
     * {
     * "goods_list": [
     * {
     * "rec_id": "13497",
     * "goods_id": "2259",
     * }
     * ],
     * "supplier_name": "1002",
     * "supplier_name": "子成100自营店",
     * "goods_number": 1,
     * "sub_total": 2599,
     * "sub_total_format": "￥2599.00"
     * }
     */
    public void setInfo(JSONObject jsonStore) {
        myAdapter = new MyAdapter(getContext());
        listView.setAdapter(myAdapter);
        shop_id = sj(jsonStore, "supplier_id");
        int goods_number = 0;
        try {
            goods_number = jsonStore.getInt("goods_number");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String str_shop = sj(jsonStore, "supplier_name") + "(共" + goods_number + "件)";
        check_shop.setText(str_shop);
        layout_full_reduction.setVisibility(GONE);
        tv_shop_all.setText(sj(jsonStore, "sub_total_format"));
        try {
            JSONArray jrr_goods = jsonStore.getJSONArray("goods_list");
            ArrayList<JSONObject> data = new ArrayList<>();
            for (int i = 0; i < jrr_goods.length(); i++) {
                data.add(jrr_goods.getJSONObject(i));
            }
            myAdapter.addData(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listGoodsView.clear();
        Default.fixListViewHeight(listView);
    }

    public String getShop_id() {
        return shop_id;
    }

    public void check(boolean isChecked) {
        check_shop.setChecked(isChecked);
    }

    private String sj(JSONObject json, String key) {
        try {
            return json.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return "error";
        }
    }

    @Override
    public void change(boolean changeMoney, boolean changeNumber, boolean delete, boolean add, String rec_id, int itemNumber, String goods_price_original, String market_price_original) {
        if (delete) {
            //region 删除item
            //如果是删除 则先重置所有数据
            for (ACartGoodsItemView acgiv : listGoodsView) {
                acgiv.check(false);
            }
            myAdapter.removeData(rec_id);
            if (listGoodsView.size() == 0) {
                //当前商铺下的产品已经被全部删除
                acd.change(false, false, true, false, shop_id, 0, null, null);
            }
            JSONObject para = new JSONObject();
            try {
                para.put("rec_id", rec_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            AsynServer.BackObject(getContext(), "cart/delete", false, para, new AsynServer.BackObject() {
                @Override
                public void Back(JSONObject sender) {
                    try {
                        JSONObject json_status = sender.getJSONObject("status");
                        int succeed = json_status.getInt("succeed");
                        if (succeed == 1) {
                            Default.showToast("删除成功");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            //endregion
        } else {
            acd.change(changeMoney, changeNumber, false, add, rec_id, itemNumber, goods_price_original, market_price_original);
        }
    }

    class MyAdapter extends BaseAdapter {
        ArrayList<JSONObject> data;
        Context c;

        MyAdapter(Context c) {
            this.c = c;
            data = new ArrayList<>();
        }

        public void addData(ArrayList<JSONObject> data) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        private void removeData(String rec_id) {
            for (int i = 0; i < data.size(); i++) {
                JSONObject json = getItem(i);
                if (sj(json, "rec_id").equals(rec_id)) {
                    data.remove(json);
                    break;
                }
            }
            notifyDataSetChanged();
            listGoodsView.clear();
            Default.fixListViewHeight(listView);

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
                convertView = new ACartGoodsItemView(ACartShopItemView.this, c);
            }
            ((ACartGoodsItemView) convertView).setInfo(data.get(position));

            for (int i = 0; i < listGoodsView.size(); i++) {
                ACartGoodsItemView acgiv = listGoodsView.get(i);
                try {
                    if (acgiv.getRec_id().equals(data.get(position).getString("rec_id"))) {
                        listGoodsView.remove(acgiv);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            listGoodsView.add((ACartGoodsItemView) convertView);
            return convertView;
        }
    }
}
