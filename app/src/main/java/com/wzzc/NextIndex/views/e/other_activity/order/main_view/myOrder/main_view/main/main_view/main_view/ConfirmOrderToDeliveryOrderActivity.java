package com.wzzc.NextIndex.views.e.other_activity.order.main_view.myOrder.main_view.main.main_view.main_view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.extendView.ExtendListView;
import com.wzzc.NextIndex.views.e.other_activity.order.main_view.businiess.OrderDetailsActivity;
import com.wzzc.NextIndex.views.e.other_activity.order.main_view.myOrder.main_view.main.main_view.main_view.main_view.DeliveryGoodsView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by toutou on 2017/4/12.
 *
 * 生成发货单
 */

public class ConfirmOrderToDeliveryOrderActivity extends BaseActivity implements View.OnClickListener{
    public static final String DATA = "data";
    private GoodsAdapter adapter_goods;
    private OrderDetailsActivity.Delivery1Adapter adapter_action;


    //region 组件
    @ViewInject(R.id.main_view_goods)
    ExtendListView main_view_goods;
    @ViewInject(R.id.main_view_action)
    ExtendListView main_view_action;
    @ViewInject(R.id.lab_orderNumber)
    private TextView lab_orderNumber;
    @ViewInject(R.id.tv_name)
    private TextView lab_name;
    @ViewInject(R.id.lab_express)
    private TextView lab_express;
    @ViewInject(R.id.lab_Consignee)
    private TextView lab_Consignee;
    @ViewInject(R.id.lab_address)
    private TextView lab_address;
    @ViewInject(R.id.lab_phone)
    private TextView lab_phone;
    @ViewInject(R.id.lab_time)
    private TextView lab_time;
    @ViewInject(R.id.lab_message)
    private TextView lab_message;
    @ViewInject(R.id.et_action_note)
    private EditText et_action_note;
    @ViewInject(R.id.tv_createDeliveryOrder)
    private TextView tv_createDeliveryOrder;
    @ViewInject(R.id.tv_cancel)
    private TextView tv_cancel;
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddBack();
        AddTitle("订单详情");
        init();
        try {
            JSONObject data = new JSONObject(GetIntentData(DATA).toString());
            setInfo(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void init () {

        tv_createDeliveryOrder.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);

    }

    public void setInfo (JSONObject data) {
        adapter_goods = new GoodsAdapter(this);
        main_view_goods.setAdapter(adapter_goods);
        adapter_action = new OrderDetailsActivity.Delivery1Adapter(this);
        main_view_action.setAdapter(adapter_action);
        try {
            JSONObject order = data.getJSONObject("order");
            //region order
            lab_orderNumber.setText(order.getString("order_sn"));
            lab_name.setText(order.getString("user_name"));
            lab_express.setText(order.getString("shipping_name"));
            lab_Consignee.setText(order.getString("consignee"));
            lab_address.setText(order.getString("address"));
            lab_phone.setText(order.getString("mobile"));
            lab_time.setText(order.getString("how_oos"));
            lab_message.setText(order.getString("card_message"));
            //endregion

            //region goods_list
            ArrayList<JSONObject> json_goods = new ArrayList<JSONObject>();
            JSONArray arr_goods = data.getJSONArray("goods_list");
            for (int i = 0; i < arr_goods.length(); i++) {
                json_goods.add(arr_goods.getJSONObject(i));
            }
            adapter_goods.addData(json_goods);
            Default.fixListViewHeight(main_view_goods);
            //endregion

            //region action_list
            ArrayList<JSONObject> json_action = new ArrayList<JSONObject>();
            JSONArray arr_action = data.getJSONArray("action_list");
            for (int i = 0; i < arr_action.length(); i++) {
                json_action.add(arr_action.getJSONObject(i));
            }
            adapter_action.addData(json_action);
            Default.fixListViewHeight(main_view_action);
            //endregion
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static class GoodsAdapter extends BaseAdapter{
        private ArrayList<DeliveryGoodsView> list_goodsView;
        ArrayList<JSONObject> data;
        Context c;
        public GoodsAdapter (Context c) {
            this.c = c;
            this.data = new ArrayList<>();
            list_goodsView = new ArrayList<>();
        }

        public void addData (ArrayList<JSONObject> data) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = new DeliveryGoodsView(c);
            }
            DeliveryGoodsView goodsView = (DeliveryGoodsView) convertView;
            goodsView.setInfo(data.get(position));
            goodsView.setTag(position);
            boolean hasAdded = false;
            for (int i = 0 ; i < list_goodsView.size() ; i ++) {
                if (list_goodsView.get(i).getTag().equals(position)) {
                    hasAdded = true;
                    break;
                }
            }
            if (!hasAdded) {
                list_goodsView.add(goodsView);
            }
            return convertView;
        }

        public String getGoodsNumber () {
            StringBuilder printBuilder = new StringBuilder();
            for (int i = 0 ; i < list_goodsView.size() ; i ++) {
                if (i > 0) {
                    printBuilder.append("|");
                }
                printBuilder.append(list_goodsView.get(i).getSendNumber());
            }
            return printBuilder.toString();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_createDeliveryOrder :
                // TODO: 2017/4/12 生成单
                Intent intent = new Intent(ConfirmOrderToDeliveryOrderActivity.this,OrderDetailsActivity.class);
                intent.putExtra(OrderDetailsActivity.NUMBER,adapter_goods.getGoodsNumber());
                Default.toClass(ConfirmOrderToDeliveryOrderActivity.this,intent);
                break;
            case R.id.tv_cancel:
                onBackPressed();
                break;
            default:
        }
    }
}
