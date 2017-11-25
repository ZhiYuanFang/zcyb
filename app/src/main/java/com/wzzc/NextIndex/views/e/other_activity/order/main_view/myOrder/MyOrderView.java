package com.wzzc.NextIndex.views.e.other_activity.order.main_view.myOrder;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wzzc.other_view.production.detail.gcb.DetailGcbActivity;
import com.wzzc.other_view.production.detail.zcb.DetailZcbActivity;
import com.wzzc.NextIndex.views.e.other_activity.order.MyOrderDelegate;
import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/3/1.
 *
 */

public class MyOrderView extends BaseView {
    MyOrderDelegate mod;
    @ViewInject(R.id.bt_confirm)
    TextView bt_confirm;
    @ViewInject(R.id.img_call)
    ImageView img_call;
    @ViewInject(R.id.lab_state)
    TextView lab_state;
    @ViewInject(R.id.lab_sumery)
    TextView lab_sumery;
    @ViewInject(R.id.lab_sum_money)
    TextView lab_sum_money;
    @ViewInject(R.id.bt_cancel)
    TextView bt_cancel;
    @ViewInject(R.id.list_order_detail)
    ListView list_order_detail;
    @ViewInject(R.id.bt_detail)
    TextView bt_detail;
    @ViewInject(R.id.tv_shop_name)
            TextView tv_shop_name;
    String order_id;
    /**
     * 订单状态
     */
    String pay_status;
    /**
     * 是否历史账单
     */
    boolean is_old;
    /**
     * 商家电话
     */
    String phone;
    /**
     * 是否可以取消订单
     */
    int can_cancel;

    boolean isZCB;

    int store_id ;
    CurrentAdapter cAdapter;

    public MyOrderView(Context context) {
        super(context);
        init();
    }

    private void init() {
        tv_shop_name.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/6/21 前往商铺详情 store_id
            }
        });
        img_call.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //region call
                if (phone != null && phone.length() > 0) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                    if (ActivityCompat.checkSelfPermission(GetBaseActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        MessageBox.Show("请开启拨打权限");
                        return;
                    }
                    GetBaseActivity().startActivity(intent);
                } else {
                    MessageBox.Show("商家尚未提供联系方式");
                }
                //endregion
            }
        });

        bt_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //region 取消
                if (is_old) {
                    //历史订单
                    Default.showToast("历史订单，无法取消", Toast.LENGTH_SHORT);
                } else {
                    //普通订单
                    if (mod != null) {
                        mod.cancelOrder(order_id);
                    }
                }
                //endregion
            }
        });

        bt_detail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mod != null) {
                    mod.lookOrderDetail(order_id,is_old ? 1 : 0);
                }
            }
        });
        
        bt_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mod != null) {
                    mod.confirmOrder(order_id);
                }
            }
        });
    }

    public void setInfo(JSONObject data,MyOrderDelegate mod) {
        this.mod = mod;
        try {
            JSONObject json_store = data.getJSONObject("store");
            store_id = json_store.getInt("id");
            tv_shop_name.setText(json_store.getString("name"));
            phone = json_store.getString("phone");
            int can_affirm_received = JsonClass.ij(data,"can_affirm_received");
            if (can_affirm_received == 0) {
                bt_confirm.setVisibility(GONE);
            }else {
                bt_confirm.setVisibility(VISIBLE);
            }
            order_id = data.getString("order_id");
            pay_status = data.getString("pay_status");
            can_cancel = data.getInt("can_cancel");
            isZCB = data.getInt("is_exchange") == 1;
            if (can_cancel == 1) {
                bt_cancel.setVisibility(VISIBLE);
            } else {
                bt_cancel.setVisibility(GONE);
            }
            is_old = data.getInt("is_old") == 1;

            JSONArray jrr_goods_list = data.getJSONArray("goods_list");
            lab_state.setText(data.getString("order_status"));
            lab_sumery.setText("共" + jrr_goods_list.length() + "件商品");
            lab_sum_money.setText(data.getString("total_fee"));


            final CurrentAdapter currentAdapter = new CurrentAdapter(GetBaseActivity(), jrr_goods_list);
            list_order_detail.setAdapter(currentAdapter);
            list_order_detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    try {
                        intent.putExtra(DetailGcbActivity.GOODSID, currentAdapter.jrr_goods_list.getJSONObject(position).getString("goods_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (isZCB) {
                        //子成币产品
                        GetBaseActivity().AddActivity(DetailZcbActivity.class, 0, intent);
                    } else {
                        //工厂币产品
                        GetBaseActivity().AddActivity(DetailGcbActivity.class, 0, intent);
                    }

                }
            });
            Default.fixListViewHeight(list_order_detail);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class CurrentAdapter extends BaseAdapter {
        Context c;
        JSONArray jrr_goods_list;

        CurrentAdapter(Context c, JSONArray jrr_goods_list) {
            this.c = c;
            this.jrr_goods_list = jrr_goods_list;
        }

        @Override
        public int getCount() {
            return jrr_goods_list.length();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CurrentViewHolder currentViewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(c).inflate(R.layout.order_detail, null);
                currentViewHolder = new CurrentViewHolder();
                currentViewHolder.img_photo = (ExtendImageView) convertView.findViewById(R.id.img_photo);
                currentViewHolder.lab_format_price = (TextView) convertView.findViewById(R.id.lab_format_price);
                currentViewHolder.lab_goods_number = (TextView) convertView.findViewById(R.id.lab_goods_number);
                currentViewHolder.lab_model = (TextView) convertView.findViewById(R.id.lab_model);
                currentViewHolder.lab_name = (TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(currentViewHolder);
            } else {
                currentViewHolder = (CurrentViewHolder) convertView.getTag();
            }
            try {
                JSONObject jsonObject = jrr_goods_list.getJSONObject(position);
                currentViewHolder.img_photo.setPath(jsonObject.getJSONObject("img").getString("url"));
                currentViewHolder.lab_format_price.setText(jsonObject.getString("formated_shop_price"));
                currentViewHolder.lab_name.setText(jsonObject.getString("name"));
//                currentViewHolder.lab_model.setText(jsonObject.getString(""));
                currentViewHolder.lab_goods_number.setText("x" + jsonObject.getString("goods_number"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return convertView;
        }

        class CurrentViewHolder {
            ExtendImageView img_photo;
            TextView lab_name, lab_model, lab_format_price, lab_goods_number;
        }
    }

}
