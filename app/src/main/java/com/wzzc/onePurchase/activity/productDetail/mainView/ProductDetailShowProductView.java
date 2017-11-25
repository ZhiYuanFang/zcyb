package com.wzzc.onePurchase.activity.productDetail.mainView;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.other_view.SlideView.slidePager.SlidePagerCountView;
import com.wzzc.other_function.SlideViewPagerAdapter;
import com.wzzc.onePurchase.activity.productDetail.ProductionDetailDelegate;
import com.wzzc.zcyb365.R;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/3/23.
 */

public class ProductDetailShowProductView extends BaseView {
    ProductionDetailDelegate detailDelegate;
    @ViewInject(R.id.container)
    private LinearLayout container;
    @ViewInject(R.id.view_pager)
    private ViewPager view_pager;
    @ViewInject(R.id.count_pager)
    private SlidePagerCountView count_pager;
    private ArrayList<String> productionIconsImage;
    private ArrayList<String> productionIconsGoodsID;
    private Integer IMAGEWIDTH;
    private int pager_stage;
    private ArrayList<View> productionLayoutlist;

    public ProductDetailShowProductView(Context context) {
        super(context);
        init();
    }

    private void init() {
        IMAGEWIDTH = Default.dip2px(130, getContext());
        // 1.设置幕后item的缓存数目
        view_pager.setOffscreenPageLimit(3);
        // 2.设置页与页之间的间距
//        view_pager.setPageMargin(10);
        // 3.将父类的touch事件分发至viewPgaer，否则只能滑动中间的一个view对象
        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return view_pager.dispatchTouchEvent(event);
            }
        });
        count_pager.noselect_img = R.drawable.img_android_round22;
        count_pager.select_img = R.drawable.img_android_round21;
    }

    /**
     * 需要在setInfo前调用此方法，不然无效
     */
    public void setIMAGEWIDTH(Integer imagewidth) {
        this.IMAGEWIDTH = imagewidth;
    }

    public void setInfo(ProductionDetailDelegate detailDelegate , ArrayList<String> productionIconsImage, ArrayList<String> productionIconsGoodsID) {
        this.detailDelegate = detailDelegate;
        if (productionIconsImage == null) {
            this.productionIconsImage = new ArrayList<>();
        } else {
            this.productionIconsImage = productionIconsImage;
        }

        if (productionIconsGoodsID == null) {
            this.productionIconsGoodsID = new ArrayList<>();
        } else {
            this.productionIconsGoodsID = productionIconsGoodsID;
        }

        productionLayoutlist = new ArrayList<>();
        initialized();
    }

    private void initialized() {
        for (int i = 0; i < productionIconsImage.size(); i++) {
            ProductionItemLayout layout = new ProductionItemLayout(getContext());
            layout.setInfo(i);
            productionLayoutlist.add(layout);
        }
        SlideViewPagerAdapter adapter = new SlideViewPagerAdapter(productionLayoutlist);
        view_pager.setAdapter(adapter);

        count_pager.setCount(productionLayoutlist.size());
        view_pager.addOnPageChangeListener(productPageChangeListener());

        view_pager.setCurrentItem(productionLayoutlist.size()/2);
    }

    //region Layout
    private class ProductionItemLayout extends RelativeLayout {
        ExtendImageView extendImageView;
        String goods_id;String imagePath;
        protected ProductionItemLayout(Context context) {
            super(context);
            LayoutParams lp = new LayoutParams(IMAGEWIDTH, IMAGEWIDTH);
            ProductionItemLayout.this.setLayoutParams(lp);
            init();
        }

        private void init() {
            RelativeLayout layout = new RelativeLayout(getContext());
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layout.setLayoutParams(layoutParams);
            layout.setGravity(Gravity.CENTER);

            extendImageView = new ExtendImageView(getContext());
            LayoutParams lp_image = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            extendImageView.setLayoutParams(lp_image);
            extendImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            layout.addView(extendImageView);
            ProductionItemLayout.this.addView(layout);

        }

        protected void setInfo(int position) {
            imagePath = productionIconsImage.get(position);
            goods_id = productionIconsGoodsID.get(position);
            extendImageView.setPath(imagePath);
        }

        public void setLayoutBackground(@DrawableRes int drawableID) {
            setBackground(ContextCompat.getDrawable(getContext(), drawableID));
        }

        public void setLayoutBackgroundColor(@ColorRes int color) {
            setBackgroundColor(ContextCompat.getColor(getContext(), color));
        }

    }
    //endregion

    //region Helper
    protected ViewPager.OnPageChangeListener productPageChangeListener (){
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeAllProductionLayoutBackGround(position);
                count_pager.setIndex(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                pager_stage = state;
            }
        };
    }

    protected void changeAllProductionLayoutBackGround(Integer index) {
        for (int i = 0; i < productionLayoutlist.size(); i++) {
            ProductionItemLayout item = (ProductionItemLayout) productionLayoutlist.get(i);
            item.setLayoutBackgroundColor(R.color.white);
            item.setOnClickListener(notFocusPageItemClickListener());
        }

        ProductionItemLayout item = (ProductionItemLayout) productionLayoutlist.get(index);
        item.setLayoutBackground(R.drawable.bg_collection);
        item.setOnClickListener(productionPageItemClickListener(productionIconsGoodsID.get(index)));
        getServerInfo(productionIconsGoodsID.get(index));
    }
    //endregion

    private void getServerInfo (String goods_id) {
        JSONObject sender = new JSONObject();
        // TODO: 2017/3/24 获取产品信息
        if (detailDelegate != null) {
            detailDelegate.refreshInfoProfile(sender);
        }
    }

    //region Action
    protected OnClickListener notFocusPageItemClickListener () {

        return /*new OnClickListener() {
            @Override
            public void onClick(View v) {
                Default.showToast("当前产品未聚焦，请移动到中间。",Toast.LENGTH_SHORT);

            }
        }*/ null;
    }

    protected OnClickListener productionPageItemClickListener(final String goods_id) {

        return /*new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/3/24
                Default.showToast("进入商品" + goods_id + "页面", Toast.LENGTH_SHORT);
            }
        }*/null;
    }
    //endregion
}
