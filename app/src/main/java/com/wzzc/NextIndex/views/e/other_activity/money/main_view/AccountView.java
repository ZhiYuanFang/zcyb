package com.wzzc.NextIndex.views.e.other_activity.money.main_view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.other_function.MessageBox;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.NoNetView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zcyb365 on 2016/11/12.
 *
 * 账户明细
 */
public class AccountView extends BaseView {
    //region 组件
    @ViewInject(R.id.contain_layout)
    private RelativeLayout contain_layout;
    @ViewInject(R.id.listView)
    private ListView listView;
    //endregion
    CurrentAdapter cAdapter;
    public AccountView(Context context) {
        super(context);
    }

    public void setInfo () {
        if (Default.isConnect(getContext())) {
            cAdapter = new CurrentAdapter(getContext());
            listView.setAdapter(cAdapter);
            JSONObject para = new JSONObject();
            try {
                para.put("session",Default.GetSession());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            AsynServer.BackObject(getContext(), "account/detail", false, listView,para , new AsynServer.BackObject() {
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

    private class CurrentAdapter extends BaseAdapter {
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
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.account_item,null);
                viewHolder.tv_info = (TextView) convertView.findViewById(R.id.tv_category);
                viewHolder.tv_other = (TextView) convertView.findViewById(R.id.tv_other);
                viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            try {
                String user_money = data.get(position).getString("user_money");
                String frozen_money = data.get(position).getString("frozen_money");
                String rank_points = data.get(position).getString("rank_points");
                String pay_points = data.get(position).getString("pay_points");
                SpannableStringBuilder info = new SpannableStringBuilder();
                if (data.get(position).has("user_money_type")) {
                    String user_money_type = data.get(position).getString("user_money_type");
                    info.append(user_money_type).append(":");
                    SpannableString money = new SpannableString(user_money);
                    money.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(),R.color.tv_Red)),0,money.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    info.append(money).append("\n");
                }
                if (data.get(position).has("frozen_money_type")) {
                    String frozen_money_type = data.get(position).getString("frozen_money_type");
                    info.append(frozen_money_type).append(":");
                    SpannableString money = new SpannableString(frozen_money);
                    money.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(),R.color.tv_Red)),0,money.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    info.append(money).append("\n");
                }
                if (data.get(position).has("rank_points_type")) {
                    String rank_points_type = data.get(position).getString("rank_points_type");
                    info.append(rank_points_type).append(":");
                    SpannableString money = new SpannableString(rank_points);
                    money.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(),R.color.tv_Red)),0,money.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    info.append(money).append("\n");
                }
                if (data.get(position).has("pay_points_type")) {
                    String pay_points_type = data.get(position).getString("pay_points_type");
                    info.append(pay_points_type).append(":");
                    SpannableString money = new SpannableString(pay_points);
                    money.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(),R.color.tv_Red)),0,money.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    info.append(money).append("\n");
                }
                String change_time = data.get(position).getString("change_time");
                String short_change_desc = data.get(position).getString("short_change_desc");

                System.out.println("``` info : " + info);
                viewHolder.tv_info.setText(info);
                viewHolder.tv_other.setText("备注:" + short_change_desc);
                viewHolder.tv_time.setText(change_time);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return convertView;
        }

        private class ViewHolder {
            TextView tv_info,tv_other,tv_time;
        }
    }
}
