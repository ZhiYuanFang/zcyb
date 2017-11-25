package com.wzzc.NextIndex.views.e.other_activity.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.wzzc.base.ExtendBaseActivity;
import com.wzzc.base.ExtendBaseView;
import com.wzzc.NextIndex.views.e.other_activity.order.main_view.myOrder.main_view.MyOrderDetailsActivity;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.Default;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.NextIndex.views.e.other_activity.order.main_view.myOrder.MyOrderView;
import com.wzzc.NextIndex.views.e.other_activity.order.main_view.businiess.main.OrderListView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zcyb365 on 2016/11/24.
 *
 * type 1 : 商家订单列表
 * type 0 : 普通订单列表
 */
public class OrderListActivity extends ExtendBaseActivity implements MyOrderDelegate{
    private static final String TAG = "OrderListActivity";
    private CollectionAdapter adapter;
    //region ```
    @ViewInject(R.id.main_view)
    private ListView listView;
    @ViewInject(R.id.lab_noshopping)
    private LinearLayout lab_noshopping;
    //endregion
    private static final String Type = "all";
    private int order_type;// 0 普通订单 ；1 商家订单列表
    public static final String TypeClickItem = "type_click_item";
    boolean wantClear;
    @Override
    protected void init() {
        adapter = new CollectionAdapter(OrderListActivity.this);
        listView.setAdapter(adapter);

        order_type = (int) GetIntentData("order_type");

        if (order_type == 0) {
            AddTitle("我的订单");
        } else {
            AddTitle("商家订单列表");
        }
        super.init();
    }

    @Override
    protected String getFileKey() {
        return TAG + order_type;
    }

    @Override
    protected ExtendBaseView.ServerCallBack serverCallBack() {
        return new ExtendBaseView.ServerCallBack() {
            @Override
            public void callBack(Object json_data) {
                JSONArray arr = (JSONArray) json_data;
                ArrayList<JSONObject> data = new ArrayList<>();
                for (int i = 0; i < arr.length(); i++) {
                    data.add(JsonClass.jjrr(arr,i));
                }
                adapter.addData(data);
                if (adapter.getCount() == 0){
                    lab_noshopping.setVisibility(View.VISIBLE);
                } else {
                    lab_noshopping.setVisibility(View.GONE);

                }
            }
        };
    }

    @Override
    protected void setInfoFromService(String t) {
        if (t != null && t.equals(TypeClickItem)) {
            wantClear = true;
        }
        JSONObject para = new JSONObject();
        try {
            para.put("type",Type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url;
        if (order_type == 0) {
            url = "order/list";
        } else {
            url = "seller/order_list";
        }
        AsynServer.BackObject(OrderListActivity.this, url, true,listView, para,  new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                if (wantClear) {
                    adapter.clearData();
                    wantClear = false;
                }
                try {
                    initialized(sender,serverCallBack());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(Bundle bundle) {
        super.onActivityResult(bundle);
//        judgeNetConnectedAndSetInfoFromService(TypeClickItem);
    }

    @Override
    public void confirmOrder(String order_id) {
        JSONObject para = new JSONObject();
        try {
            para.put("order_id",order_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(this, "order/affirmReceived", para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                JSONObject json_status = JsonClass.jj(sender,"status");
                int succeed = JsonClass.ij(json_status,"succeed");
                if (succeed == 1) {
                    judgeNetConnectedAndSetInfoFromService(TypeClickItem);
                    Default.showToast("确认收货成功!");
                } else {
                    MessageBox.Show(JsonClass.sj(json_status,"error_desc"));
                }
            }
        });
    }

    @Override
    public void lookOrderDetail(String order_id,int isOld) {
        Intent intent = new Intent();
        intent.putExtra("order_id", order_id);
        intent.putExtra("is_old", isOld);
        AddActivity(MyOrderDetailsActivity.class, 0, intent);
    }

    @Override
    public void cancelOrder(String order_id) {
        JSONObject para = new JSONObject();
        try {
            para.put("order_id", order_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(this, "order/cancel",para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                try {
                    if (sender.getJSONObject("status").getInt("succeed") == 1) {
                        judgeNetConnectedAndSetInfoFromService(TypeClickItem);
                        Default.showToast("取消订单成功");
                    } else {
                        Default.showToast("处理失败", Toast.LENGTH_LONG);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class CollectionAdapter extends BaseAdapter {
        private ArrayList<JSONObject> data;
        private Context content;


        private CollectionAdapter(Context context) {
            this.content = context;
            this.data = new ArrayList<>();
        }


        public void addData(ArrayList<JSONObject> data) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        public void clearData () {
            this.data.clear();
        }
        @Override
        public int getCount() {
            return this.data.size();
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
            if (order_type == 0) {
                MyOrderView view;
                if (convertView != null) {
                    view = (MyOrderView) convertView;
                } else {
                    view = new MyOrderView(this.content);
                }
                view.setInfo(getItem(position),OrderListActivity.this);
                return view;
            } else {
                OrderListView view;
                if (convertView != null) {
                    view = (OrderListView) convertView;
                } else {
                    view = new OrderListView(this.content);
                }
                view.setInfo(getItem(position));
                return view;
            }
        }
    }
}
