package com.wzzc.onePurchase.activity.index.main_view.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.other_view.SlideView.slidePager.SlidePagerCountView;
import com.wzzc.other_function.SlideViewPagerAdapter;
import com.wzzc.onePurchase.action.LayoutTouchListener;
import com.wzzc.onePurchase.activity.index.main_view.allProduction.OnePurchaseAllProductionActivity;
import com.wzzc.onePurchase.activity.index.main_view.limiteRevealed.LimitedRevealedActivity;
import com.wzzc.onePurchase.activity.specialProduction.SpecialProductionActivity;
import com.wzzc.onePurchase.item.OnePurchaseIndexHasWinedItem;
import com.wzzc.onePurchase.item.OnePurchaseIndexProductionWaitingBuyItem;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/3/29.
 */

public class OnePurchaseIndexView extends BaseView implements View.OnClickListener{


    //region 组件
    @ViewInject(R.id.contain_producPager)
    private RelativeLayout contain_producPager;
    @ViewInject(R.id.lab_allProduction)
    private TextView lab_allProduction;
    @ViewInject(R.id.lab_lastestPublish)
    private TextView lab_lastestPublish;
    @ViewInject(R.id.lab_shotTime)
    private TextView lab_shotTime;
    @ViewInject(R.id.lab_showOrder)
    private TextView lab_showOrder;
    @ViewInject(R.id.headline_lastpublish)
    private OnePurchaseItemHeadlineView headline_lastpublish;
    @ViewInject(R.id.contain_hasWinedViwe)
    private LinearLayout contain_hasWinedViwe;
    @ViewInject(R.id.headline_shortTime)
    private OnePurchaseItemHeadlineView headline_shortTime;
    @ViewInject(R.id.contain_waitBuyViwe)
    private LinearLayout contain_waitBuyViwe;
    @ViewInject(R.id.headline_promator)
    private OnePurchaseItemHeadlineView headline_promator;
    @ViewInject(R.id.contain_promator)
    private LinearLayout contain_promator;
    @ViewInject(R.id.viewPager)
    private ViewPager viewPager;
    @ViewInject(R.id.countPager)
    private SlidePagerCountView countPager;
    //endregion

    /** 互动图片集合*/
    private JSONArray jrr_slidPagers;
    /** 最新揭晓数据集合*/
    private JSONArray jrr_lastPublishs;
    /** 今日限时数据集合*/
    private JSONArray jrr_shortTimes;
    /** 热门推荐数据集合*/
    private JSONArray jrr_promates;
    public OnePurchaseIndexView(Context context) {
        super(context);
        init();
    }
    private void init () {
        WIDTH = (Default.getScreenWidth((Activity)getContext()) - Default.dip2px(20,getContext()) ) / 3;
        //region click
        lab_allProduction.setOnClickListener(this);
        lab_lastestPublish.setOnClickListener(this);
        lab_shotTime.setOnClickListener(this);
        lab_showOrder.setOnClickListener(this);
        headline_lastpublish.setOnClickListener(this);
        headline_shortTime.setOnClickListener(this);
        headline_promator.setOnClickListener(this);

        lab_allProduction.setOnTouchListener(new LayoutTouchListener(getContext()));
        lab_lastestPublish.setOnTouchListener(new LayoutTouchListener(getContext()));
        lab_shotTime.setOnTouchListener(new LayoutTouchListener(getContext()));
        lab_showOrder.setOnTouchListener(new LayoutTouchListener(getContext()));
        //endregion
        jrr_lastPublishs = new JSONArray();
        jrr_promates = new JSONArray();
        jrr_shortTimes = new JSONArray();
        jrr_slidPagers = new JSONArray();

    }

    public void setInfo () {
        getServerInfo();
    }

    protected void getServerInfo () {
        //region 访问服务器

        //region 获取数据
        //假数据
        for (int i = 0 ; i < 4 ; i ++) {
            JSONObject json = new JSONObject();
            jrr_lastPublishs.put(json);
            jrr_promates.put(json);
            jrr_shortTimes.put(json);
            jrr_slidPagers.put(json);
        }
        //获取互动图片集合
        //获取最新揭晓数据
        //获取今日限时数据
        //获取热门推荐数据
        //endregion

        //region 设定界面
        //为viewPager添加数据
        setViewPagerDate();
        //为contain_hasWinedViwe添加数据
        setLastPublishDate();
        //为contain_waitBuyViwe添加数据
        setShortTimeDate();
        //为listview_promator添加数据
        setPromateDate();
        //endregion

        //endregion
    }

    //region Helper
    private void setViewPagerDate () {
//        jrr_lastPublishs
        if (jrr_slidPagers == null) return;

        ArrayList<View> arr_pager = new ArrayList<>();
        for (int i  = 0 ; i < jrr_slidPagers.length() ; i ++) {
            try {
                View v = createPagerItemView(jrr_slidPagers.getJSONObject(i));
                arr_pager.add(v);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        SlideViewPagerAdapter slideViewPagerAdapter = new SlideViewPagerAdapter(arr_pager);
        viewPager.setAdapter(slideViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerListener());
        countPager.setCount(jrr_slidPagers.length());
    }
    String url = "http://www.zcyb365.com/mobile/images/br2.jpg";
    Integer WIDTH ;
    private void setLastPublishDate () {
//        jrr_lastPublishs
        if (jrr_lastPublishs == null) return;

        contain_hasWinedViwe.setWeightSum(jrr_lastPublishs.length());
        for (int i = 0 ; i < jrr_lastPublishs.length() ; i ++) {
            try {
                JSONObject json = jrr_lastPublishs.getJSONObject(i);
                String goods_id = /*json.getString("")*/"G20183";
                String imgPath = /*json.getString("")*/url;
                String winMan = /*json.getString("")*/"JJ";
                OnePurchaseIndexHasWinedItem onePurchaseIndexHasWinedItem = new OnePurchaseIndexHasWinedItem(getContext());
                onePurchaseIndexHasWinedItem.setInfo(goods_id,imgPath,winMan);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(WIDTH, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.weight = 1;
                onePurchaseIndexHasWinedItem.setLayoutParams(lp);
                contain_hasWinedViwe.addView(onePurchaseIndexHasWinedItem);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void setShortTimeDate () {
//        jrr_shortTimes

        if (jrr_shortTimes == null) return;

        contain_waitBuyViwe.setWeightSum(jrr_shortTimes.length());
        for (int i = 0 ; i < jrr_shortTimes.length() ; i ++) {
            try {
                JSONObject json = jrr_shortTimes.getJSONObject(i);
                //region 假数据
                String goods_id = "X02932";
                Long shortTime = 5464L;
                String imgPath = url;
                String productionName = "OppR9s";
                String nowPrice = "$3899.00";
                Integer hasEnteredNumber = 29;
                Integer allNeedNumber = 50;
                Integer remainingEnterNumber = 21;

                //endregion
                OnePurchaseIndexProductionWaitingBuyItem onePurchaseIndexProductionWaitingBuyItem = new OnePurchaseIndexProductionWaitingBuyItem(getContext());
                onePurchaseIndexProductionWaitingBuyItem.setInfo(goods_id,shortTime,imgPath,productionName,nowPrice,hasEnteredNumber,allNeedNumber,remainingEnterNumber);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(WIDTH, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.weight = 1;
                onePurchaseIndexProductionWaitingBuyItem.setLayoutParams(lp);
                contain_waitBuyViwe.addView(onePurchaseIndexProductionWaitingBuyItem);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setPromateDate () {
//        jrr_promates
        if (jrr_promates == null) return;
        //这里我默认是双数，不显示单数也是为了界面的美观
        contain_promator.setWeightSum(jrr_promates.length()/2);
        for (int i = 0 ; i < jrr_promates.length() ; i += 2) {
            try {
                JSONObject json_1 = jrr_promates.getJSONObject(i);
                JSONObject json_2 = jrr_promates.getJSONObject(2);
                //region 假数据
                String goods_id_1 = "X02932";
                String imgPath_1 = url;
                String productionName_1 = "OppR9s";
                String nowPrice_1 = "$3899.00";
                Integer hasEnteredNumber_1 = 29;
                Integer allNeedNumber_1 = 50;
                Integer remainingEnterNumber_1 = 21;

                String goods_id_2 = "X02932";
                String imgPath_2 = url;
                String productionName_2 = "OppR9s";
                String nowPrice_2 = "$3899.00";
                Integer hasEnteredNumber_2 = 29;
                Integer allNeedNumber_2 = 50;
                Integer remainingEnterNumber_2 = 21;
                //endregion
                OnePurchaseIndexProductionWaitingBuyItem onePurchaseIndexProductionWaitingBuyItem_1 = new OnePurchaseIndexProductionWaitingBuyItem(getContext());
                onePurchaseIndexProductionWaitingBuyItem_1.setInfo(goods_id_1,null , imgPath_1,productionName_1,nowPrice_1
                        ,hasEnteredNumber_1,allNeedNumber_1,remainingEnterNumber_1);
                LinearLayout.LayoutParams lp_1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp_1.weight = 1;
                onePurchaseIndexProductionWaitingBuyItem_1.setLayoutParams(lp_1);

                OnePurchaseIndexProductionWaitingBuyItem onePurchaseIndexProductionWaitingBuyItem_2 = new OnePurchaseIndexProductionWaitingBuyItem(getContext());
                onePurchaseIndexProductionWaitingBuyItem_2.setInfo(goods_id_2,null , imgPath_2,productionName_2,nowPrice_2
                        ,hasEnteredNumber_2,allNeedNumber_2,remainingEnterNumber_2);
                LinearLayout.LayoutParams lp_2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp_2.weight = 1;
                onePurchaseIndexProductionWaitingBuyItem_2.setLayoutParams(lp_2);
                //region doubleLayout
                LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setWeightSum(2);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.weight = 1;
                linearLayout.setLayoutParams(layoutParams);
                linearLayout.addView(onePurchaseIndexProductionWaitingBuyItem_1);
                linearLayout.addView(onePurchaseIndexProductionWaitingBuyItem_2);
                //endregion

                contain_promator.addView(linearLayout);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    //endregion

    //region Layout
    /** 创建滑动的每一项布局*/
    public View createPagerItemView (JSONObject json) {
        String url = null;
        try {
            url = json.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RelativeLayout layout = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT );
        layout.setLayoutParams(lp);
        ExtendImageView ex = ExtendImageView.Create(layout);
        if (url == null) {
            url = /*""*/this.url;
        }
        ex.setPath(url);
        ex.setOnClickListener(slidItemClickListener(json));
        return layout;
    }
    //endregion

    //region Action
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lab_allProduction:
                // 所有商品
                GetBaseActivity().AddActivity(OnePurchaseAllProductionActivity.class);
                break;
            case R.id.lab_lastestPublish:{
                // 最新揭晓
                Intent intent = new Intent();
                intent.putExtra(SpecialProductionActivity.CURRENTITEM,SpecialProductionActivity.PUBLISHED);
                GetBaseActivity().AddActivity(SpecialProductionActivity.class,0,intent);
                break;}
            case R.id.lab_shotTime:
                // 限时夺宝
                GetBaseActivity().AddActivity(LimitedRevealedActivity.class);
                break;
            case R.id.lab_showOrder:{
                // 夺宝晒单
                Intent intent = new Intent();
                intent.putExtra(SpecialProductionActivity.CURRENTITEM,SpecialProductionActivity.SHOWORDER);
                GetBaseActivity().AddActivity(SpecialProductionActivity.class,0,intent);
                break;}
            case R.id.headline_lastpublish:{
                // 最新揭晓
                Intent intent = new Intent();
                intent.putExtra(SpecialProductionActivity.CURRENTITEM,SpecialProductionActivity.PUBLISHED);
                GetBaseActivity().AddActivity(SpecialProductionActivity.class,0,intent);
                break;}
            case R.id.headline_shortTime:{
                // 今日限时
                GetBaseActivity().AddActivity(LimitedRevealedActivity.class);
                break;}
            case R.id.headline_promator:
                // 热门推荐
                GetBaseActivity().AddActivity(OnePurchaseAllProductionActivity.class);
                break;
            default:
        }
    }

    protected View.OnClickListener slidItemClickListener (JSONObject json) {
        View.OnClickListener click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/3/25 分析obj 进入跳转
                Default.showToast("slidItem", Toast.LENGTH_SHORT);
            }
        };
        return click;
    }

    protected ViewPager.OnPageChangeListener viewPagerListener () {
        ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                countPager.setIndex(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        return listener;
    }
    //endregion
}
