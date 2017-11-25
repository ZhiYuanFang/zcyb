package com.wzzc.index.ShoppingCart.a;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.index.ShoppingCart.ConfirmOrder.a.AConfirmOrderActivity;
import com.wzzc.index.ShoppingCart.a.main_view.ACartShopItemView;
import com.wzzc.index.ShoppingCart.main_view.ShoppingCartNoDataView;
import com.wzzc.NextIndex.views.e.other_activity.address.AddressActivity;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.MessageBox;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/6/16.
 */

public class ACartView extends BaseView implements ACartDelegate {
    //region ```
    @ViewInject(R.id.listView)
    ListView listView;
    @ViewInject(R.id.tv_all)
    TextView tv_all;
    @ViewInject(R.id.tv_category)
    TextView tv_info;
    @ViewInject(R.id.tv_submit)
    TextView tv_submit;
    @ViewInject(R.id.check_all)
    CheckBox check_all;
    @ViewInject(R.id.layout_cart)
    RelativeLayout layout_cart;
    @ViewInject(R.id.layout_b)
    LinearLayout layout_b;
    //endregion
    MyAdapter myAdapter;
    ArrayList<ACartShopItemView> listShopView;
    ShoppingCartNoDataView noDataView;
    ArrayList<String> arrRecIDs;

    public ACartView(Context context) {
        super(context);
        init();
    }

    public ACartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        noDataView = new ShoppingCartNoDataView(getContext());
        layout_cart.addView(noDataView);
        noDataView.setVisibility(GONE);
        check_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (ACartShopItemView acsiv : listShopView) {
                    acsiv.check(isChecked);
                }
            }
        });

        tv_submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // region 购物车进入结算功能
                if (arrRecIDs.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0 ; i < arrRecIDs.size() ; i ++) {
                        if (i > 0) {
                            sb.append(",");
                        }
                        sb.append(arrRecIDs.get(i));
                    }
                    JSONObject para = new JSONObject();
                    try {
                        para.put("rec_id",sb.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AsynServer.BackObject(getContext(), "cart/select", para, new AsynServer.BackObject() {
                        @Override
                        public void Back(JSONObject sender) {
                            try {
                                JSONObject json_status = sender.getJSONObject("status");
                                int succeed = ij(json_status,"succeed");
                                if (succeed == 1) {
                                    JSONObject data = sender.getJSONObject("data");
                                    String defaultAddressID = sj(data,"default_address_id");
                                    if (defaultAddressID.length() > 0 && !defaultAddressID.equals("0")) {
                                        //有默认地址
                                        Intent intent = new Intent();
                                        intent.putExtra(AConfirmOrderActivity.ADDRESS, defaultAddressID);
                                        GetBaseActivity().AddActivity(AConfirmOrderActivity.class, 0, intent);
                                    } else {
                                        //没有默认地址
                                        Intent intent = new Intent();
                                        intent.putExtra(AddressActivity.SELECT, true);
                                        intent.putExtra(AddressActivity.ORDERTYPE, 0);
                                        GetBaseActivity().AddActivity(AddressActivity.class,0,intent);
                                    }
                                } else {
                                    MessageBox.Show(sj(json_status,"error_desc"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    Default.showToast("你还没有选择任何商品");
                }
                //endregion
            }
        });
    }

    /**
     * {
     * "store_list": [],
     * "total": {
     * "goods_price": 0,
     * "market_price": 0,
     * "saving": 0,
     * "save_rate": 0,
     * "goods_amount": 0
     * }
     * }
     */
    public void setInfo(JSONObject json) {
        arrRecIDs = new ArrayList<>();
        listView.setVisibility(VISIBLE);
        noDataView.setVisibility(GONE);
        listShopView = new ArrayList<>();
        tv_all.setText("￥0.00");
        tv_submit.setText("结算(0)");
        try {
            String info = sj(json.getJSONObject("total"), "activity_desc");
            if (info.length() <= 0) {
                layout_b.setVisibility(GONE);
            } else {
                layout_b.setVisibility(VISIBLE);
            }
            tv_info.setText(info);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        myAdapter = new MyAdapter(getContext());
        listView.setAdapter(myAdapter);
        try {
            JSONArray jrr_store = json.getJSONArray("store_list");
            ArrayList<JSONObject> data = new ArrayList<>();
            for (int i = 0; i < jrr_store.length(); i++) {
                data.add(jrr_store.getJSONObject(i));
            }
            myAdapter.addData(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void change(boolean changeMoney, boolean changeNumber, boolean delete, boolean add, String rec_id, int itemNumber, String goods_price_original, String market_price_original) {
        if (delete) {
            //region 删除商铺
            for (ACartShopItemView acsiv : listShopView) {
                acsiv.check(false);
            }
            myAdapter.removeData(rec_id);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (listShopView.size() == 0) {
                        listView.setVisibility(GONE);
                        noDataView.setVisibility(VISIBLE);
                    }
                }
            }, Default.delayTime);
            //endregion
        } else {
            if (changeNumber) {
                String str_submit = tv_submit.getText().toString();
                int forNumber = Integer.parseInt(str_submit.substring(3, str_submit.length() - 1));
                tv_submit.setText("结算(" + (forNumber + (add ? 1 : -1)) + ")");
            }
            if (changeMoney) {

                   for (int i = 0 ; i < itemNumber ; i ++) {
                       if (add) {
                           arrRecIDs.add(rec_id);
                       } else {
                           arrRecIDs.remove(rec_id);
                       }
                   }



                String str_money = tv_all.getText().toString();
                Double forMoney = Double.valueOf(str_money.substring(1, str_money.length()));
                Double cm = itemNumber * Double.valueOf(goods_price_original) * (add ? 1 : -1);
                String nowMoney = Default.m2(forMoney + cm);
                tv_all.setText("￥" + nowMoney);
            }
        }
    }

    class MyAdapter extends BaseAdapter {
        Context c;
        ArrayList<JSONObject> data;

        MyAdapter(Context c) {
            this.c = c;
            this.data = new ArrayList<>();
        }

        public void addData(ArrayList<JSONObject> data) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        private void removeData(String supplier_id) {
            for (int i = 0; i < data.size(); i++) {
                JSONObject json = getItem(i);
                if (sj(json, "supplier_id").equals(supplier_id)) {
                    data.remove(i);
                    break;
                }
            }
            listShopView.clear();
            notifyDataSetChanged();
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
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new ACartShopItemView(ACartView.this, c);
            }
            ((ACartShopItemView) convertView).setInfo(data.get(position));
            for (int i = 0; i < listShopView.size(); i++) {
                ACartShopItemView acsiv = listShopView.get(i);
                try {
                    if (acsiv.getShop_id().equals(data.get(position).getString("supplier_id"))) {
                        listShopView.remove(acsiv);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            listShopView.add((ACartShopItemView) convertView);
            return convertView;
        }
    }

    private String sj(JSONObject json, String key) {
        try {
            return json.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    private int ij(JSONObject json, String key) {
        try {
            return json.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
