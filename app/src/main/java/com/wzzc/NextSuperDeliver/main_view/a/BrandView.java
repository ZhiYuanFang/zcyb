package com.wzzc.NextSuperDeliver.main_view.a;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.NextSuperDeliver.Production;
import com.wzzc.NextSuperDeliver.ShopBean;
import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/8/22.
 * 商铺ITEM
 */
@ContentView(R.layout.view_brand)
public class BrandView extends BaseView implements View.OnClickListener{
    BrandViewDelegate brandViewDelegate;
    @ViewInject(R.id.layout_contain)
    LinearLayout layout_contain;
    @ViewInject(R.id.tv_brand_title)
    TextView tv_brand_title;
    @ViewInject(R.id.layout_brand)
    RelativeLayout layout_brand;
    @ViewInject(R.id.eiv_logo)
    ExtendImageView eiv_logo;
    @ViewInject(R.id.tv_name)
    TextView tv_name;
    @ViewInject(R.id.tv_number)
    TextView tv_number;
    @ViewInject(R.id.bgiv_0)
    BrandGoodItemView bgiv_0;
    @ViewInject(R.id.bgiv_1)
    BrandGoodItemView bgiv_1;
    @ViewInject(R.id.bgiv_2)
    BrandGoodItemView bgiv_2;
    public BrandView(Context context) {
        super(context);
        init();
    }

    public BrandView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init () {
        layout_contain.setOnClickListener(this);

    }

    public void setInfo (BrandViewDelegate brandViewDelegate , ShopBean shopBean , boolean showTitle) {
        this.brandViewDelegate = brandViewDelegate;
        if (showTitle) {
            tv_brand_title.setVisibility(VISIBLE);
        } else {
            tv_brand_title.setVisibility(GONE);
        }
        layout_brand.setTag(shopBean);
        layout_contain.setTag(shopBean);
        System.out.println(shopBean.getBrand_name() + " : " + shopBean.getBrand_logo());
        eiv_logo.setPath(shopBean.getBrand_logo());
        tv_name.setText(shopBean.getBrand_desc());
        tv_number.setText("剩" + shopBean.getLeft_day() + "天");
        ArrayList<Production> pros = shopBean.getProductionArrayList();
        if (pros.size() == 0) {
            allMiss();
        } else if (pros.size() == 1){
            oneShow();
            bgiv_0.setInfo(pros.get(0));
            bgiv_0.setTag(pros.get(0));
        } else if (pros.size() == 2) {
            twoShow();
            bgiv_0.setInfo(pros.get(0));
            bgiv_0.setTag(pros.get(0));
            bgiv_1.setInfo(pros.get(1));
            bgiv_1.setTag(pros.get(1));
        } else {
            allShow();
            bgiv_0.setInfo(pros.get(0));
            bgiv_0.setTag(pros.get(0));
            bgiv_1.setInfo(pros.get(1));
            bgiv_1.setTag(pros.get(1));
            bgiv_2.setInfo(pros.get(2));
            bgiv_2.setTag(pros.get(2));
        }
    }

    private void allMiss () {
        ViewGroup.LayoutParams lp_0 = bgiv_0.getLayoutParams();
        lp_0.height = 0;
        bgiv_0.setLayoutParams(lp_0);
        ViewGroup.LayoutParams lp_1 = bgiv_1.getLayoutParams();
        lp_1.height = 0;
        bgiv_1.setLayoutParams(lp_1);
        ViewGroup.LayoutParams lp_2 = bgiv_2.getLayoutParams();
        lp_2.height = 0;
        bgiv_2.setLayoutParams(lp_2);
    }

    private void oneShow () {
        ViewGroup.LayoutParams lp_0 = bgiv_0.getLayoutParams();
        lp_0.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        bgiv_0.setLayoutParams(lp_0);
        ViewGroup.LayoutParams lp_1 = bgiv_1.getLayoutParams();
        lp_1.height = 0;
        bgiv_1.setLayoutParams(lp_1);
        ViewGroup.LayoutParams lp_2 = bgiv_2.getLayoutParams();
        lp_2.height = 0;
        bgiv_2.setLayoutParams(lp_2);
    }

    private void twoShow () {
        ViewGroup.LayoutParams lp_0 = bgiv_0.getLayoutParams();
        lp_0.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        bgiv_0.setLayoutParams(lp_0);
        ViewGroup.LayoutParams lp_1 = bgiv_1.getLayoutParams();
        lp_1.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        bgiv_1.setLayoutParams(lp_1);
        ViewGroup.LayoutParams lp_2 = bgiv_2.getLayoutParams();
        lp_2.height = 0;
        bgiv_2.setLayoutParams(lp_2);
    }

    private void allShow () {
        ViewGroup.LayoutParams lp_0 = bgiv_0.getLayoutParams();
        lp_0.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        bgiv_0.setLayoutParams(lp_0);
        ViewGroup.LayoutParams lp_1 = bgiv_1.getLayoutParams();
        lp_1.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        bgiv_1.setLayoutParams(lp_1);
        ViewGroup.LayoutParams lp_2 = bgiv_2.getLayoutParams();
        lp_2.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        bgiv_2.setLayoutParams(lp_2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_contain:
                brandViewDelegate.chooseShop((ShopBean) v.getTag());
                break;
            default:
        }
    }
}
