package com.wzzc.onePurchase.activity.personal_home_page.main_view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.onePurchase.item.OnePurchaseHasWinedInCloudShoppingItem;
import com.wzzc.onePurchase.item.OnePurchaseWaitingBuyInCloudShoppingItem;
import com.wzzc.zcyb365.R;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by toutou on 2017/3/28.
 *
 * 个人主页
 */

public class PersonalHomePageView extends BaseView implements View.OnClickListener , AbsListView.OnScrollListener{

    //region 组件
    @ViewInject(R.id.tv_goods_name)
    private TextView tv_name;
    @ViewInject(R.id.tv_signature)
    private TextView tv_signature;
    @ViewInject(R.id.tv_indiana)
    private TextView tv_indiana;
    @ViewInject(R.id.tv_hasWined)
    private TextView tv_hasWined;
    @ViewInject(R.id.tv_showOrder)
    private TextView tv_showOrder;
    @ViewInject(R.id.listView_indiana)
    private ListView listView_indiana;
    @ViewInject(R.id.listView_hasWined)
    private ListView listView_hasWined;
    @ViewInject(R.id.listView_showOrder)
    private ListView listView_showOrder;
    @ViewInject(R.id.contain_indiana)
    private RelativeLayout contain_indiana;
    @ViewInject(R.id.contain_hasWined)
    private RelativeLayout contain_hasWined;
    @ViewInject(R.id.contain_showOrder)
    private RelativeLayout contain_showOrder;
    @ViewInject(R.id.head_icon)
    private ExtendImageView head_icon;
    //endregion

    private ArrayList<TextView> arrList_tv_select;
    private ArrayList<RelativeLayout> arrList_rv_production;
    private String red_id;
    private int pager_indiana,pager_hasWined,pager_showOrder;
    private int count_indiana,count_hasWined,count_showOrder;
    public PersonalHomePageView(Context context) {
        super(context);
        init();
    }
    private void init (){
        lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,Default.dip2px(80,getContext()));//listItem 固定高度

        arrList_tv_select = new ArrayList<TextView>(){{
            add(tv_indiana);
            add(tv_hasWined);
            add(tv_showOrder);
        }};

        arrList_rv_production = new ArrayList<RelativeLayout>(){{
            add(contain_indiana);
            add(contain_hasWined);
            add(contain_showOrder);
        }};

        tv_indiana.setOnClickListener(this);
        tv_hasWined.setOnClickListener(this);
        tv_showOrder.setOnClickListener(this);

        listView_indiana.setOnScrollListener(this);
        listView_hasWined.setOnScrollListener(this);
        listView_showOrder.setOnScrollListener(this);


    }

    public void setInfo (JSONObject sender) {
        initialized();
    }

    private void initialized (){
        tv_indiana.callOnClick();
    }

    @Override
    public void onClick(View v) {
        changeFocus((TextView) v);
        switch (v.getId()){
            case R.id.tv_indiana:
                addIndiana();
                break;
            case R.id.tv_hasWined:
                addHasWined();
                break;
            case R.id.tv_showOrder:
                addShowOrder();
                break;
            default:
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    //region Method
    //夺宝记录
    private void addIndiana () {
        if (!hasAdapter(listView_indiana)) {
            AdapterIndiana adapterIndiana = new AdapterIndiana(getContext());
            ArrayList<JSONObject> data = new ArrayList<>();
            // TODO: 2017/3/28 访问服务器 获取数据，设置adapger
            //region 假数据
            JSONObject json = new JSONObject();
            for (int i = 0 ; i < 10 ; i ++) {
                data.add(json);
            }
            //endregion
            adapterIndiana.addData(data);
            listView_indiana.setAdapter(adapterIndiana);
        }
    }

    //获得的商品
    private void addHasWined () {
        if (!hasAdapter(listView_hasWined)) {
            AdapterHasWined adapterHasWined = new AdapterHasWined(getContext());
            ArrayList<JSONObject> data = new ArrayList<>();
            // TODO: 2017/3/28 访问服务器 获取数据，设置adapger
            //region 假数据
            JSONObject json = new JSONObject();
            for (int i = 0 ; i < 10 ; i ++) {
                data.add(json);
            }
            //endregion
            adapterHasWined.addData(data);
            listView_hasWined.setAdapter(adapterHasWined);
        }
    }

    //晒单
    private void addShowOrder () {
        if (!hasAdapter(listView_showOrder)) {
            AdapterShowOrder adapterShowOrder = new AdapterShowOrder(getContext());
            ArrayList<JSONObject> data = new ArrayList<>();
            // TODO: 2017/3/28 访问服务器 获取数据，设置adapger
            //region 假数据
            JSONObject json = new JSONObject();
            for (int i = 0 ; i < 10 ; i ++) {
                data.add(json);
            }
            //endregion
            adapterShowOrder.addData(data);
            listView_showOrder.setAdapter(adapterShowOrder);
        }
    }

    private int judgeTypeView (JSONObject json) {
        return 0;
    }
    //endregion

    //region Helper
    private void changeFocus (TextView tv) {
        for (int i = 0 ; i < arrList_tv_select.size() ; i ++) {
            arrList_tv_select.get(i).setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
        }

        tv.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Red));

        for (int i = 0 ; i < arrList_rv_production.size() ; i ++) {
            arrList_rv_production.get(i).setVisibility(GONE);
            if (arrList_rv_production.get(i).getTag().equals(tv.getTag())) {
                arrList_rv_production.get(i).setVisibility(VISIBLE);
            }
        }

    }

    private boolean hasAdapter (ListView lv) {
        return lv.getAdapter()!=null;
    }


    //endregion

    String url = "http://www.zcyb365.com/mobile/images/br2.jpg";
    //region Adapter
    LayoutParams lp;

    protected class AdapterIndiana extends BaseAdapter {
        Context c;
        ArrayList<JSONObject> data;
        public AdapterIndiana (Context c) {
            this.c = c;
            data = new ArrayList<>();
        }

        public void addData (ArrayList<JSONObject> data){
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
        public int getItemViewType(int position) {

            return judgeTypeView(data.get(position));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            String goods_id = "Xoi230";
            String imgPath = url;
            String productionName = "OppR9s";
            String nowPrice = "$2999";
            String winnerName = "薇薇";
            String announcement_time = "2017/05/28 19:58:05";
            Integer hasEnteredNumber = 32;
            Integer allNeedNumber = 50;
            Integer remainingEnterNumber = 18;
            switch (getItemViewType(position)){
                case TYPE_HASWINNED :
                    if (convertView == null) {
                        convertView = new OnePurchaseHasWinedInCloudShoppingItem(getContext());
                    }

                    OnePurchaseHasWinedInCloudShoppingItem onePurchaseHasWinedInCloudShoppingItem = (OnePurchaseHasWinedInCloudShoppingItem) convertView;
                    onePurchaseHasWinedInCloudShoppingItem.setInfo(goods_id,imgPath,productionName,nowPrice,winnerName,announcement_time,true);

                    break;
                case TYPE_WAITTING :
                    if (convertView == null) {
                        convertView = new OnePurchaseWaitingBuyInCloudShoppingItem(getContext());
                    }

                    OnePurchaseWaitingBuyInCloudShoppingItem onePurchaseWaitingBuyInCloudShoppingItem = (OnePurchaseWaitingBuyInCloudShoppingItem) convertView;
                    onePurchaseWaitingBuyInCloudShoppingItem.setInfo(goods_id,imgPath,productionName,nowPrice,hasEnteredNumber,allNeedNumber,remainingEnterNumber,true);
                    break;
                default:
            }


            convertView.setLayoutParams(lp);
            return convertView;
        }

        private static final int TYPE_HASWINNED = 0 , TYPE_WAITTING = 1;
    }

    protected class AdapterHasWined extends BaseAdapter {
        Context c;
        ArrayList<JSONObject> data;
        public AdapterHasWined (Context c) {
            this.c = c;
            data = new ArrayList<>();
        }

        public void addData (ArrayList<JSONObject> data){
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
            String goods_id = "Xoi230";
            String imgPath = url;
            String productionName = "OppR9s";
            String nowPrice = "$2999";
            String winnerName = "薇薇";
            String announcement_time = "2017/05/28 19:58:05";
            if (convertView == null) {
                convertView = new OnePurchaseHasWinedInCloudShoppingItem(getContext());
            }

            OnePurchaseHasWinedInCloudShoppingItem onePurchaseHasWinedInCloudShoppingItem = (OnePurchaseHasWinedInCloudShoppingItem) convertView;
            onePurchaseHasWinedInCloudShoppingItem.setInfo(goods_id,imgPath,productionName,nowPrice,winnerName,announcement_time,true);
            convertView.setLayoutParams(lp);
            return convertView;
        }
    }

    protected class AdapterShowOrder extends BaseAdapter {
        Context c;
        ArrayList<JSONObject> data;
        public AdapterShowOrder (Context c) {
            this.c = c;
            data = new ArrayList<>();
        }

        public void addData (ArrayList<JSONObject> data){
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
            String goods_id = "Xoi230";
            String imgPath = url;
            String productionName = "OppR9s";
            String nowPrice = "$2999";
            Integer hasEnteredNumber = 32;
            Integer allNeedNumber = 50;
            Integer remainingEnterNumber = 18;
            if (convertView == null) {
                convertView = new OnePurchaseWaitingBuyInCloudShoppingItem(getContext());
            }

            OnePurchaseWaitingBuyInCloudShoppingItem onePurchaseWaitingBuyInCloudShoppingItem = (OnePurchaseWaitingBuyInCloudShoppingItem) convertView;
            onePurchaseWaitingBuyInCloudShoppingItem.setInfo(goods_id,imgPath,productionName,nowPrice,hasEnteredNumber,allNeedNumber,remainingEnterNumber,true);
            convertView.setLayoutParams(lp);
            return convertView;
        }
    }
    //endregion
}
