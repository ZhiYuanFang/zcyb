package com.wzzc.onePurchase.activity.index.main_view.shopCar.main_view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.onePurchase.OnePurchaseShoppingCarDelegate;
import com.wzzc.onePurchase.item.OnePurchaseCartItem;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by toutou on 2017/3/29.
 */

public class ShopCarView extends BaseView implements OnePurchaseShoppingCarDelegate{
    @ViewInject(R.id.listView_car)
    private ListView listView_car;
    @ViewInject(R.id.tv_summery)
    private TextView tv_summery;
    @ViewInject(R.id.tv_submit)
    private TextView tv_submit;
    private Integer goodsNumber;
    private Double goodsAmount;
    private ArrayList<String> arrListRecID;
    public ShopCarView(Context context) {
        super(context);
        init();
    }

    private void init () {
        arrListRecID = new ArrayList<>();
        tv_submit.setOnClickListener(summeryListener());
    }

    public void setInfo (JSONArray jrr) {
        goodsNumber = 0;
        goodsAmount = 0D;

        ArrayList<JSONObject> arrList = new ArrayList<>();
        for (int i = 0 ; i < /*jrr.length()*/ 12; i ++) {
            JSONObject json = new JSONObject();
            arrList.add(json);

            String rec_id = "3212";
            int number = 1                                                                                                                                                                        ;
            for (int j = 0 ; j < number ;j ++) {
                arrListRecID.add(rec_id);
            }

            goodsNumber += 1;
            goodsAmount += 1.00;
        }
        ShopCarAdapter shopCarAdapter = new ShopCarAdapter(getContext());
        shopCarAdapter.addData(arrList);
        listView_car.setAdapter(shopCarAdapter);
        Default.fixListViewHeight(listView_car);
        initialized();
    }

    private void initialized (){
        StringBuilder sBuilder = new StringBuilder();
        SpannableString spa_number = new SpannableString(String.valueOf(goodsNumber));
        spa_number.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(),R.color.tv_Red)),0,spa_number.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString spa_amount = new SpannableString(String.valueOf(goodsAmount));
        spa_amount.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(),R.color.tv_Red)),0,spa_amount.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sBuilder.append("总共购买").append(spa_number).append("个商品 合计金额：").append(spa_amount).append("元");
        tv_summery.setText(sBuilder);
    }

    @Override
    public void changeTotalAmount(String goods_id , Integer number , double changeAmount) {
        if (changeAmount == 0) {
            return;
        }
        Default.showToast("changeAmount : " + changeAmount, Toast.LENGTH_SHORT);
        StringBuilder sBuilder = new StringBuilder();
        SpannableString spa_number = new SpannableString(String.valueOf(goodsNumber));
        spa_number.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(),R.color.tv_Red)),0,spa_number.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        goodsAmount += changeAmount;
        SpannableString spa_amount = new SpannableString(String.valueOf(goodsAmount));
        spa_amount.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(),R.color.tv_Red)),0,spa_amount.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sBuilder.append("总共购买").append(spa_number).append("个商品 合计金额：").append(spa_amount).append("元");
        tv_summery.setText(sBuilder);

        if (number > 0 ) {
            for (int i = 0 ; i < number ; i ++) {
                arrListRecID.add(goods_id);
            }
        } else {
            for (int i = 0 ; i < -number ; i ++) {
                arrListRecID.remove(goods_id);
            }
        }


    }

    protected OnClickListener summeryListener () {
        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer sBuffer = new StringBuffer();//提交的goods_id
                for (int i = 0 ; i < arrListRecID.size() ; i ++) {
                    sBuffer.append(arrListRecID.get(i));
                    if (i < arrListRecID.size() - 1) {
                        sBuffer.append(",");
                    }
                }
                // TODO: 2017/3/29 访问服务器 提交结算

            }
        };
        return listener;
    }

    protected class ShopCarAdapter extends BaseAdapter {

        private Context c;
        private ArrayList<JSONObject> data;

        public ShopCarAdapter (Context c) {
            this.c = c;
            data = new ArrayList<>();
            arrList_hasAddPosition = new ArrayList();
            arrList_itemView = new ArrayList();
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
            if (!arrList_hasAddPosition.contains(position)) {
                OnePurchaseCartItem onePurchaseCartItem = new OnePurchaseCartItem(getContext());

                String url = "http://test.zcgj168.com/data/afficheimg/images/zones/banner01.png";
                String goods_id = "2323";
                String imgPath = url;
                String productionName = "OppR9s";
                Integer remaindNumber = 23;
                String price = "$1.0";

                onePurchaseCartItem.setInfo(goods_id,imgPath,productionName,remaindNumber,price,ShopCarView.this);

                arrList_hasAddPosition.add(position);
                arrList_itemView.add(position,onePurchaseCartItem);

            }

            return arrList_itemView.get(position);
        }

        ArrayList<Integer> arrList_hasAddPosition ;
        ArrayList<View> arrList_itemView;
    }
}
