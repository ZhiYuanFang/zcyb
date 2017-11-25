package com.wzzc.NextIndex.views;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.index.ShoppingCart.ConfirmOrder.b.BConfirmOrderActivity;
import com.wzzc.index.ShoppingCart.b.BCartDelegate;
import com.wzzc.index.ShoppingCart.b.main_view.BCartGoodsItemView;
import com.wzzc.index.ShoppingCart.main_view.ShoppingCartNoDataView;
import com.wzzc.NextIndex.views.e.other_activity.address.AddressActivity;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/8/5.
 *
 */
@ContentView(R.layout.d_view)
public class D extends BaseView implements BCartDelegate {
    //region ```
    @ViewInject(R.id.listView)
    ListView listView;
    @ViewInject(R.id.tv_exchage)
    TextView tv_exchage;
    @ViewInject(R.id.tv_submit)
    TextView tv_submit;
    @ViewInject(R.id.layout_cart)
    RelativeLayout layout_cart;
    //endregion
    MyAdapter myAdapter;
    ShoppingCartNoDataView noDataView;
    public D(Context context) {
        super(context);
        init();
    }

    public D(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init () {
        noDataView = new ShoppingCartNoDataView(getContext());
        layout_cart.addView(noDataView);
        noDataView.setVisibility(GONE);
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AsynServer.BackObject(getContext(), "user/default_address",  new JSONObject(), new AsynServer.BackObject() {
                    @Override
                    public void Back(JSONObject s) {
                        JSONObject json_status = JsonClass.jj(s, "status");
                        if (JsonClass.ij(json_status, "succeed") == 0) {
                            MessageBox.Show(JsonClass.sj(json_status, "error_desc"));
                        } else {
                            JSONObject sender = JsonClass.jj(s, "data");
                            try {
                                int address_id = sender.getInt("default_address_id");
                                if (address_id == 0) {
                                    //没有默认地址
                                    Intent intent = new Intent();
                                    intent.putExtra(AddressActivity.SELECT, true);
                                    intent.putExtra(AddressActivity.ORDERTYPE, 1);
                                    GetBaseActivity().AddActivity(AddressActivity.class, 0, intent);

                                } else {
                                    //有默认地址
                                    Intent intent = new Intent();
                                    intent.putExtra(BConfirmOrderActivity.ADDRESS, address_id);
                                    GetBaseActivity().AddActivity(BConfirmOrderActivity.class, 0, intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }
    public void setInfo (JSONObject json) {
        listView.setVisibility(VISIBLE);
        noDataView.setVisibility(GONE);
        myAdapter = new MyAdapter(getContext());
        listView.setAdapter(myAdapter);
        ArrayList<JSONObject> data = new ArrayList<JSONObject>();
        try {
            JSONArray jrr_goods = json.getJSONArray("goods_list");
            for (int i = 0 ; i < jrr_goods.length() ; i ++) {
                data.add(jrr_goods.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (data.size() == 0) {
            noDataView.setVisibility(VISIBLE);
        }
        myAdapter.addData(data);
        Default.fixListViewHeight(listView);
        try {
            JSONObject total = json.getJSONObject("total");
            tv_exchage.setText(String.valueOf(ij(total,"exc_integral")));
            tv_submit.setText("结算(" + ij(total,"real_goods_count") + ")");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void change(String rec_id , int number, int price) {
        int forNumber = Integer.parseInt(tv_exchage.getText().toString());
        tv_exchage.setText(String.valueOf(forNumber + number*price));
        myAdapter.changeGoodsNumber(rec_id,number);
    }


    @Override
    public void delete(String rec_id) {
        String str_submit = tv_submit.getText().toString();
        int forNumber = Integer.parseInt(str_submit.substring(3, str_submit.length() - 1));
        tv_submit.setText("结算(" + (forNumber - 1) + ")");

        myAdapter.removeData(rec_id);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (myAdapter.getCount() == 0) {
                    listView.setVisibility(GONE);
                    noDataView.setVisibility(VISIBLE);
                }
            }
        },Default.delayTime);

        JSONObject para = new JSONObject();
        try {
            para.put("rec_id",rec_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(getContext(), "excart/delete", false, para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                try {
                    JSONObject json_status = sender.getJSONObject("status");
                    int succeed = json_status.getInt("succeed");
                    if (succeed == 1) {
                        Default.showToast("删除成功");
                    } else {
                        MessageBox.Show(sj(json_status,"error_desc"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String sj(JSONObject json, String key) {
        try {
            return json.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return "error";
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

    class MyAdapter extends BaseAdapter {
        ArrayList<JSONObject> data;
        Context c;
        MyAdapter (Context c) {
            this.c = c;
            data = new ArrayList<>();
        }
        public void addData (ArrayList<JSONObject> data) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        void removeData(String rec_id){
            for (int i = 0 ; i < data.size() ; i ++) {
                if (sj(data.get(i),"rec_id").equals(rec_id)) {
                    data.remove(i);
                    break;
                }
            }
            notifyDataSetChanged();
        }

        void changeGoodsNumber (String rec_id , int changeNumber) {
            for (int i = 0 ; i < data.size() ; i ++) {
                if (sj(data.get(i),"rec_id").equals(rec_id)) {
                    try {
                        int forNumber = Integer.parseInt(data.get(i).getString("goods_number"));
                        data.get(i).put("goods_number",forNumber + changeNumber);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
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
            try {
                return Long.parseLong(data.get(position).getString("rec_id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new BCartGoodsItemView(D.this,c);
            }
            ((BCartGoodsItemView) convertView).setInfo(getItem(position));
            return convertView;
        }
    }

}
