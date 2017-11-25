package com.wzzc.onePurchase.activity.index.main_view.center.activity.myShowOrder.main_view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.onePurchase.activity.index.main_view.center.activity.myShowOrder.OnePurchaseMyShowOrderActivity;
import com.wzzc.onePurchase.item.OnePurchaseShowOrderItem;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by toutou on 2017/3/31.
 *
 * 我的晒单
 */

public class OnePurchaseMyShowOrderView extends BaseView implements View.OnClickListener{

    //region 组件
    @ViewInject(R.id.tv_hasShow)
    private TextView tv_hasShow;
    @ViewInject(R.id.tv_notShow)
    private TextView tv_notShow;
    @ViewInject(R.id.layout_hasShow)
    private RelativeLayout layout_hasShow;
    @ViewInject(R.id.layout_notShow)
    private RelativeLayout layout_notShow;
    @ViewInject(R.id.listView_hasShow)
    private ListView listView_hasShow;
    @ViewInject(R.id.listView_notShow)
    private ListView listView_notShow;
    //endregion
    private Adapter_ShowOrder adapter_showOrder;
    private Adapter_NotShowOrder adapter_notShowOrder;
    private String rec_id;
    public OnePurchaseMyShowOrderView(Context context) {
        super(context);
        init();
    }

    private void init () {
        tv_hasShow.setOnClickListener(this);
        tv_notShow.setOnClickListener(this);

        adapter_showOrder = new Adapter_ShowOrder(getContext());
        adapter_notShowOrder = new Adapter_NotShowOrder(getContext());

        listView_hasShow.setAdapter(adapter_showOrder);
        listView_notShow.setAdapter(adapter_notShowOrder);

    }

    public void setInfo (JSONObject sender) {

        if (sender!= null) {
            try {
                rec_id = sender.getString(OnePurchaseMyShowOrderActivity.RECID);
            } catch (JSONException e) {
                e.printStackTrace();
                nullInit ();
            }
        } else {
            nullInit ();
        }

        initialized();
    }

    private void initialized () {

        tv_hasShow.callOnClick();
    }

    private void nullInit () {
        rec_id = "";
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_hasShow:
                tv_hasShow.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Red));
                tv_notShow.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
                if (adapter_showOrder.hasData()){
                    showHasShowOrder(false);
                } else {
                    showHasShowOrder(true);
                }

                break;
            case R.id.tv_notShow:
                tv_hasShow.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
                tv_notShow.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Red));
                if (adapter_notShowOrder.hasData()) {
                    showNotShowOrder(false);
                } else {
                    showNotShowOrder(true);
                }

                break;
            default:
                Default.showToast(getContext().getString(R.string.notDevelop), Toast.LENGTH_SHORT);
        }
    }

    //region Method
    protected void showHasShowOrder (boolean publish) {

        layout_hasShow.setVisibility(VISIBLE);
        layout_notShow.setVisibility(GONE);
        if (publish) {
            adapter_showOrder.clearData();
            // TODO: 2017/3/31 访问服务器 获取数据
            //region 假数据
            ArrayList<JSONObject> data = new ArrayList<>();
            for (int i = 0 ; i < 10 ;i ++) {
                JSONObject json = new JSONObject();
                data.add(json);
            }
            adapter_showOrder.addData(data);
        }

        //endregion
    }

    protected void showNotShowOrder (boolean publish) {

        layout_hasShow.setVisibility(GONE);
        layout_notShow.setVisibility(VISIBLE);
        if (publish) {
            adapter_notShowOrder.clearData();

            //region 假数据
            ArrayList<JSONObject> data = new ArrayList<>();
            for (int i = 0 ; i < 10 ;i ++) {
                JSONObject json = new JSONObject();
                data.add(json);
            }
            adapter_notShowOrder.addData(data);
            //endregion
        }

    }
    //endregion

    //region Adapter
    protected class Adapter_ShowOrder extends BaseAdapter{
        Context c;
        ArrayList<JSONObject> data;
        public Adapter_ShowOrder(Context c){
            this.c = c;
            data = new ArrayList<>();
        }

        public void addData (ArrayList<JSONObject> data) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        public boolean hasData () {
            return data.size() > 0;
        }

        public void clearData () {
            this.data = new ArrayList<>();
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

            if (convertView == null) {
                convertView = new OnePurchaseShowOrderItem(c);
            }
            OnePurchaseShowOrderItem onePurchaseShowOrderItem = (OnePurchaseShowOrderItem) convertView;
            //region 假数据
            String goods_id = "2332";
            String imgPath = "";
            String productionName = "OppR9s";
            String nowPrice = "$46.10";
            String lucky_code = "W213";
            String announcement_time = "2015/12/30 10:58:41";
            //endregion
            onePurchaseShowOrderItem.setInfo(goods_id,imgPath,productionName,nowPrice,lucky_code,announcement_time);
            return convertView;
        }
    }

    protected class Adapter_NotShowOrder extends BaseAdapter{
        Context c;
        ArrayList<JSONObject> data;
        public Adapter_NotShowOrder(Context c){
            this.c = c;
            data = new ArrayList<>();
        }

        public void addData (ArrayList<JSONObject> data) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        public boolean hasData () {
            return data.size() > 0;
        }
        public void clearData () {
            this.data = new ArrayList<>();
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

            if (convertView == null) {
                convertView = new OnePurchaseShowOrderItem(c);
            }
            OnePurchaseShowOrderItem onePurchaseShowOrderItem = (OnePurchaseShowOrderItem) convertView;
            //region 假数据
            String goods_id = "2332";
            String imgPath = "";
            String productionName = "OppR9s";
            String nowPrice = "$46.10";
            String lucky_code = "W213";
            String announcement_time = "2015/12/30 10:58:41";
            //endregion
            onePurchaseShowOrderItem.setInfo(goods_id,imgPath,productionName,nowPrice,lucky_code,announcement_time);
            return convertView;
        }
    }
    //endregion
}
