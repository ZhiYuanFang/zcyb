package com.wzzc.NextIndex.views.e.other_activity.money.main_view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.other_function.MessageBox;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.NextIndex.views.e.other_activity.money.recharge.RechargesActivity;
import com.wzzc.other_view.NoNetView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by toutou on 2016/11/12.
 *
 * 资金明细
 */
public class CapitalView extends BaseView {
    //region 组件
    @ViewInject(R.id.contain_layout)
    private RelativeLayout contain_layout;
    @ViewInject(R.id.listView)
    private ListView listView;
    //endregion
    CurrentAdapter cAdapter;
    public CapitalView(Context context) {
        super(context);
    }

    public void setInfo () {
        if (Default.isConnect(getContext())) {
            cAdapter = new CurrentAdapter(getContext());
            listView.setAdapter(cAdapter);
            JSONObject para = new JSONObject();
            AsynServer.BackObject(getContext(), "account/info", false, listView,para , new AsynServer.BackObject() {
                @Override
                public void Back(JSONObject sender) {
                    try {

                        JSONObject status = sender.getJSONObject("status");
                        int succeed = status.getInt("succeed");
                        if (succeed == 0) {
                            MessageBox.Show(status.getString("error_code"));
                        } else {
                            JSONObject data = sender.getJSONObject("data");
                            JSONArray jrr_account_log = data.getJSONArray("account_log");
                            ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();
                            for (int i = 0 ; i < jrr_account_log.length() ; i ++) {
                                arrayList.add(jrr_account_log.getJSONObject(i));
                            }
                            cAdapter.addData(arrayList);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            contain_layout.removeAllViews();
            contain_layout.addView(new NoNetView(getContext()));
        }
    }

    private class CurrentAdapter extends BaseAdapter{
        Context c;
        ArrayList<JSONObject> data;
        private CurrentAdapter (Context c) {
            this.c = c;
            data = new ArrayList<>();
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
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.capital_item,null);
                viewHolder.tv_info = (TextView) convertView.findViewById(R.id.tv_category);
                viewHolder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
                viewHolder.tv_pay = (TextView) convertView.findViewById(R.id.tv_pay);
                viewHolder.tv_cancel = (TextView) convertView.findViewById(R.id.tv_cancel);
                viewHolder.layout_operate = (LinearLayout) convertView.findViewById(R.id.layout_operator);
                viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            try {
                String type = data.get(position).getString("type");
                String pay_status = data.get(position).getString("pay_status");
                String add_time = data.get(position).getString("add_time");
                String is_paid = data.get(position).getString("is_paid");
                String process_type = data.get(position).getString("process_type");
                final String amount = data.get(position).getString("amount");
                final String operaID = data.get(position).getString("id");
                final String user_note = data.get(position).getString("user_note");
                final String payID;
                if (data.get(position).has("payment_id")) {
                    payID = data.get(position).getString("payment_id");
                } else {
                    payID = "0";
                }
                viewHolder.tv_info.setText(type + ":" + amount);
                viewHolder.tv_state.setText(pay_status);
                viewHolder.tv_time.setText(add_time);
                //region 付款按钮
                if (is_paid.equals("0") && process_type.equals("0")) {
                    viewHolder.tv_pay.setVisibility(VISIBLE);
                } else {
                    //已付款
                    viewHolder.tv_pay.setVisibility(GONE);
                }
                //endregion

                //region 取消按钮
                if (is_paid.equals("0") && process_type.equals("1") || viewHolder.tv_pay.getVisibility() == VISIBLE) {
                    viewHolder.tv_cancel.setVisibility(VISIBLE);
                } else {
                    viewHolder.tv_cancel.setVisibility(GONE);
                }
                //endregion

                //region 按钮图层
                if (pay_status.equals("已完成")) {
                    viewHolder.layout_operate.setVisibility(GONE);
                } else {
                    viewHolder.layout_operate.setVisibility(VISIBLE);
                }
                //endregion

                //region Action cancel
                viewHolder.tv_cancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("tv_cancel operaID : " + operaID);
                        MessageBox.Show(getContext(), "取消", "取消后将无法复原，确定要取消么？", new String[]{"取消", "确定"}, new MessageBox.MessBtnBack() {
                            @Override
                            public void Back(int index) {
                                switch (index){
                                    case 0:
                                        break;
                                    case 1 :
                                        JSONObject para = new JSONObject();
                                        try {
                                            para.put("id",operaID);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        AsynServer.BackObject(getContext(), "account/cancel", para, new AsynServer.BackObject() {
                                            @Override
                                            public void Back(JSONObject sender) {
                                                setInfo();
                                            }
                                        });
                                        break;
                                    default:
                                }
                            }
                        });
                    }
                });
                //endregion

                //region Action pay
                viewHolder.tv_pay.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra(RechargesActivity.AMOUNT,amount);
                        intent.putExtra(RechargesActivity.USERNOTE,user_note);
                        intent.putExtra(RechargesActivity.PAYMENTID,payID);
                        GetBaseActivity().AddActivity(RechargesActivity.class,0,intent);
                    }
                });
                //endregion

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return convertView;
        }

        private class ViewHolder {
            TextView tv_info,tv_state,tv_pay,tv_cancel,tv_time;
            LinearLayout layout_operate;
        }
    }
}
