package com.wzzc.onePurchase.activity.index.main_view.allProduction.main_view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.onePurchase.item.OnePurchaseGoodsItem;
import com.wzzc.zcyb365.R;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by toutou on 2017/3/29.
 * <p>
 * 所有商品
 */


public class OnePurchaseProductionView extends BaseView implements View.OnClickListener {
    //region 组件
    @ViewInject(R.id.tv_willpublished)
    private TextView tv_willpublished;
    @ViewInject(R.id.tv_popular)
    private TextView tv_popular;
    @ViewInject(R.id.tv_new)
    private TextView tv_new;
    @ViewInject(R.id.tv_coupons)
    private TextView tv_price;
    @ViewInject(R.id.spinner)
    private Spinner spinner;
    @ViewInject(R.id.contain_view)
    private RelativeLayout contain_view;
    @ViewInject(R.id.listView)
    private ListView listView;

    //endregion

    private static final Integer ASC = 0, DESC = 1;
    /** 排序*/
    private int order;
    /** 全部分类列表*/
    private ArrayList<String> arrList;
    /** 所有选项*/
    private ArrayList<TextView> arrListTextView;

    public OnePurchaseProductionView(Context context) {
        super(context);
        init();
    }

    private void init() {
        arrList = new ArrayList<>();
        arrList.add("商品分类");
        arrListTextView = new ArrayList<TextView>(){{
            add(tv_willpublished);
            add(tv_popular);
            add(tv_new);
            add(tv_price);
        }};

        tv_willpublished.setOnClickListener(this);
        tv_popular.setOnClickListener(this);
        tv_new.setOnClickListener(this);
        tv_price.setOnClickListener(this);

        tv_price.setTag(ASC);

    }

    public void setInfo() {
        // TODO: 2017/3/29 获取 分类列表

        //region 假数据
        arrList.add("分类1");
        arrList.add("分类2");
        arrList.add("分类3");
        arrList.add("分类4");
        arrList.add("分类5");
        arrList.add("分类6");

        //endregion
        initialized();
    }

    public void initialized() {

        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,arrList);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Default.showToast(arrList.get(position), Toast.LENGTH_SHORT);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tv_willpublished.callOnClick();
    }


    @Override
    public void onClick(View v) {
        changeFocus((TextView) v);
        switch (v.getId()) {
            case R.id.tv_willpublished:
                willPublished();
                break;
            case R.id.tv_popular:
                popular();
                break;
            case R.id.tv_new:
                news();
                break;
            case R.id.tv_coupons:
                Integer tag = Integer.valueOf(String.valueOf(tv_price.getTag()));
                tv_price.setTag(tag.equals(ASC) ? DESC : ASC);
                order = Integer.valueOf(String.valueOf(tv_price.getTag()));
                price();
                break;
            default:
        }
    }

    //region Method
    private void willPublished(){
        ArrayList<JSONObject> data = new ArrayList<>();
        // TODO: 2017/3/29 访问服务器获取数据
        //region 假数据
        for (int i = 0 ; i < 10 ; i ++) {
            JSONObject json = new JSONObject();
            data.add(json);
        }
        //endregion
        CurrentAdapter currentAdapter = new CurrentAdapter(getContext());

        listView.setAdapter(currentAdapter);
        currentAdapter.addData(data);
    }

    private void popular(){
        ArrayList<JSONObject> data = new ArrayList<>();
        // TODO: 2017/3/29 访问服务器获取数据
        //region 假数据
        for (int i = 0 ; i < 10 ; i ++) {
            JSONObject json = new JSONObject();
            data.add(json);
        }
        //endregion
        CurrentAdapter currentAdapter = new CurrentAdapter(getContext());

        listView.setAdapter(currentAdapter);
        currentAdapter.addData(data);}

    private void news(){
        ArrayList<JSONObject> data = new ArrayList<>();
        // TODO: 2017/3/29 访问服务器获取数据
        //region 假数据
        for (int i = 0 ; i < 10 ; i ++) {
            JSONObject json = new JSONObject();
            data.add(json);
        }
        //endregion
        CurrentAdapter currentAdapter = new CurrentAdapter(getContext());

        listView.setAdapter(currentAdapter);
        currentAdapter.addData(data);}

    private void price(){
        ArrayList<JSONObject> data = new ArrayList<>();
        // TODO: 2017/3/29 访问服务器获取数据
        //region 假数据
        for (int i = 0 ; i < 10 ; i ++) {
            JSONObject json = new JSONObject();
            data.add(json);
        }
        //endregion
        CurrentAdapter currentAdapter = new CurrentAdapter(getContext());

        listView.setAdapter(currentAdapter);
        currentAdapter.addData(data);}

    //endregion

    private void changeFocus(TextView tv){
        for (int i = 0 ; i < arrListTextView.size() ; i ++) {
            arrListTextView.get(i).setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
        }
        tv.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Red));    }

    //region adapter
    private class CurrentAdapter extends BaseAdapter {

        private Context c;
        private ArrayList<JSONObject> data;
        public CurrentAdapter (Context c){
            this.c = c;
            data = new ArrayList<>();
        }

        public void addData (ArrayList<JSONObject> data) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        public void clearData () {
            this.data.clear();
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
                convertView = new OnePurchaseGoodsItem(c);
            }
            OnePurchaseGoodsItem cart = (OnePurchaseGoodsItem) convertView;
            String goods_id = "3223";
            String url = "http://www.zcyb365.com/mobile/images/br2.jpg";
            String imgPath = url;
            String productionName  = "OppR9s";
            String nowPrice = "$23.00";
            Integer hasEnteredNumber = 23;
            Integer allNeedNumber = 50;
            Integer remainingEnterNumber = 27;
            cart.setInfo(goods_id,imgPath,productionName,nowPrice,hasEnteredNumber,allNeedNumber,remainingEnterNumber);
            return convertView;
        }
    }
    //endregion
}
