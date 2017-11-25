package com.wzzc.onePurchase.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.onePurchase.activity.index.main_view.allProduction.OnePurchaseAllProductionActivity;
import com.wzzc.onePurchase.activity.index.main_view.center.OnePurchaseMyCloudShopView;
import com.wzzc.onePurchase.activity.index.main_view.home.OnePurchaseIndexView;
import com.wzzc.onePurchase.activity.index.main_view.shopCar.OnePurchaseShopCarActivity;
import com.wzzc.zcyb365.R;

import java.util.ArrayList;

/**
 * Created by toutou on 2017/3/29.
 *
 * 一元购主页入口
 */

public class OnePurchaseIndexActivity extends BaseActivity implements View.OnClickListener{

    //region 组件
    @ViewInject(R.id.tv_home)
    private TextView tv_home;
    @ViewInject(R.id.tv_production)
    private TextView tv_production;
    @ViewInject(R.id.tv_car)
    private TextView tv_car;
    @ViewInject(R.id.tv_center)
    private TextView tv_center;
    @ViewInject(R.id.contain_layout)
    private RelativeLayout contain_layout;
    //endregion

    private Drawable drawable_noSelect_car,drawable_noSelect_production,drawable_noSelect_home,drawable_noSelect_center;
    private Drawable drawable_Select_car,drawable_Select_production,drawable_Select_home,drawable_Select_center;
    private ArrayList<TextView> arrList_item;
    /** 添加的view*/
    private ArrayList<View> arrList_view;

    private OnePurchaseIndexView onePurchaseIndexView;
    private OnePurchaseMyCloudShopView onePurchaseMyCloudShopView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }
    private void init () {
        arrList_view = new ArrayList<>();

        arrList_item = new ArrayList<TextView>(){{
            add(tv_home);
            add(tv_production);
            add(tv_car);
            add(tv_center);
        }};

        drawable_noSelect_car = ContextCompat.getDrawable(this,R.drawable.onepurchase_shop_car);
        drawable_noSelect_production = ContextCompat.getDrawable(this,R.drawable.onepurchase_allproduction_noselect);
        drawable_noSelect_home = ContextCompat.getDrawable(this,R.drawable.onepurchase_index_notselect);
        drawable_noSelect_center = ContextCompat.getDrawable(this,R.drawable.onepurchase_mycloud_noselect);

        drawable_Select_car = ContextCompat.getDrawable(this,R.drawable.onepurchase_shop_car_select);
        drawable_Select_production = ContextCompat.getDrawable(this,R.drawable.onepurchase_allproduction_select);
        drawable_Select_home = ContextCompat.getDrawable(this,R.drawable.onepurchase_index_select);
        drawable_Select_center = ContextCompat.getDrawable(this,R.drawable.onepurchase_mycloud_select);

        for (int i = 0 ; i < arrList_item.size() ; i ++) {
            arrList_item.get(i).setOnClickListener(this);
        }

        tv_home.callOnClick();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_home:
                changeFocus((TextView) v);
                setTopDrawable(new TextView[]{tv_home,tv_production,tv_car,tv_center},new Drawable[]{drawable_Select_home,drawable_noSelect_production , drawable_noSelect_car,drawable_noSelect_center});
                if (onePurchaseIndexView == null) {
                    onePurchaseIndexView = new OnePurchaseIndexView(this);
                    arrList_view.add(onePurchaseIndexView);
                    contain_layout.addView(onePurchaseIndexView);
                }
                onePurchaseIndexView.setInfo();

                showView(onePurchaseIndexView);
                break;
            case R.id.tv_production:
                AddActivity(OnePurchaseAllProductionActivity.class);
                break;
            case R.id.tv_car:
                AddActivity(OnePurchaseShopCarActivity.class);
                break;
            case R.id.tv_center:
                changeFocus((TextView) v);
                setTopDrawable(new TextView[]{tv_home,tv_production,tv_car,tv_center},new Drawable[]{drawable_noSelect_home,drawable_noSelect_production , drawable_noSelect_car,drawable_Select_center});
                if (onePurchaseMyCloudShopView == null) {
                    onePurchaseMyCloudShopView = new OnePurchaseMyCloudShopView(this);
                    arrList_view.add(onePurchaseMyCloudShopView);
                    contain_layout.addView(onePurchaseMyCloudShopView);
                }
                onePurchaseMyCloudShopView.setInfo();
                showView(onePurchaseMyCloudShopView);
                break;
        }
    }

    private void showView (View view) {
        for (int i = 0 ; i < arrList_view.size() ; i ++) {
            arrList_view.get(i).setVisibility(View.GONE);
        }
        view.setVisibility(View.VISIBLE);
    }

    private void setTopDrawable (TextView[] tv , Drawable[] drawables) {
        for (int i = 0 ; i < tv.length ; i ++) {
            tv[i].setCompoundDrawablesWithIntrinsicBounds(null , drawables[i],null,null);
        }
    }

    private void changeFocus (TextView tv) {
        for (int i = 0 ; i < arrList_item.size() ; i ++) {
            arrList_item.get(i).setTextColor(ContextCompat.getColor(this,R.color.tv_Gray));
        }
        tv.setTextColor(ContextCompat.getColor(this,R.color.tv_Red));
    }
}
