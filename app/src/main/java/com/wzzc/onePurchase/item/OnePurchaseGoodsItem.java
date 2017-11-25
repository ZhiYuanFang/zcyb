package com.wzzc.onePurchase.item;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wzzc.base.Default;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.onePurchase.action.LayoutTouchListener;
import com.wzzc.onePurchase.childview.info.OnePurchaseWaitingBuyInfo;
import com.wzzc.zcyb365.R;

/**
 * Created by toutou on 2017/3/20.
 *
 * 一元购商品/人气
 */

public class OnePurchaseGoodsItem extends RelativeLayout {

    private Integer HEIGHT_IMG;
    private Integer HEIGHT_ADDCART;

    private String goods_id;
    private String imgPath ;
    private String productionName , nowPrice ;
    private Integer hasEnteredNumber , allNeedNumber , remainingEnterNumber;
    private boolean hasInit;
    OnePurchaseWaitingBuyInfo onePurchaseWaitingBuyInfo; ExtendImageView eiv_production;
    public OnePurchaseGoodsItem(Context context) {
        super(context);
        init();
    }
    public OnePurchaseGoodsItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public void setInfo (String goods_id,String imgPath , String productionName , String nowPrice , Integer hasEnteredNumber , Integer allNeedNumber ,
                         Integer remainingEnterNumber) {
        this.goods_id = goods_id;
        this.imgPath = imgPath;
        this.productionName = productionName;
        this.nowPrice = nowPrice;
        this.hasEnteredNumber = hasEnteredNumber;
        this.allNeedNumber = allNeedNumber;
        this.remainingEnterNumber = remainingEnterNumber;
        initialized ();
    }
    Integer smallTextSize;
    private void init(){

        setOnTouchListener(new LayoutTouchListener(getContext()));
        smallTextSize = Default.dip2px(7, getContext());
        HEIGHT_IMG = Default.dip2px(80,getContext());
        HEIGHT_ADDCART = HEIGHT_IMG/4;
        Integer space = Default.dip2px(5,getContext());
        setPadding(space,space,space,space);
        LinearLayout linearLayoutHORIZONTALAllParent = new LinearLayout(getContext());
        LinearLayout.LayoutParams lp_allParent = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT);
        //region set AllParentLayout WEIGHT_SUM = 1
        linearLayoutHORIZONTALAllParent.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutHORIZONTALAllParent.setLayoutParams(lp_allParent);
        linearLayoutHORIZONTALAllParent.setWeightSum(1);
        linearLayoutHORIZONTALAllParent.setOnClickListener(comeToDetailForProduction);
        //endregion

        //region Add IMGLayout to allParent WEIGHT = 0 , HEIGHT == WIDTH == HEIGHT_IMG;
        RelativeLayout relativeLayoutIMG = new RelativeLayout(getContext());
        LinearLayout.LayoutParams lp_IMGLayout = new LinearLayout.LayoutParams(HEIGHT_IMG, ViewGroup.LayoutParams.MATCH_PARENT);
        relativeLayoutIMG.setLayoutParams(lp_IMGLayout);
        lp_IMGLayout.weight = 0;
        //region Add ExtendImageView to IMGLayout TOP = 0;
        eiv_production = new ExtendImageView(getContext());
        LayoutParams lp_productionIMG = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT);
        eiv_production.setLayoutParams(lp_productionIMG);
        eiv_production.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        relativeLayoutIMG.addView(eiv_production);
        //endregion

        linearLayoutHORIZONTALAllParent.addView(relativeLayoutIMG);
        //endregion

        //region Add ProductionInfo to allParent WEIGHT =1
        onePurchaseWaitingBuyInfo = new OnePurchaseWaitingBuyInfo(getContext());
        LinearLayout.LayoutParams lp_info = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        onePurchaseWaitingBuyInfo.setLayoutParams(lp_info);
        lp_info.weight = 1f;
        onePurchaseWaitingBuyInfo.setPadding(space,0,space*2,0);
        linearLayoutHORIZONTALAllParent.addView(onePurchaseWaitingBuyInfo);
        //endregion

        //region Add car to allParent WEIGHT = 0 ; WIDTH == WIDTH_NEXT_ARROR
//        NextArrorView rLayout_Arror = new NextArrorView(getContext());
        RelativeLayout rv_car = new RelativeLayout(getContext());
        LinearLayout.LayoutParams lp_car = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rv_car.setLayoutParams(lp_car);
        lp_car.weight = 0;
        rv_car.setGravity(Gravity.BOTTOM);
        TextView tv_car = new TextView(getContext());
        LinearLayout.LayoutParams lp_car_tv = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv_car.setLayoutParams(lp_car_tv);
        tv_car.setPadding(space,space,space,space);
        tv_car.setText("+ 购物车");
        tv_car.setTextSize(smallTextSize);
        tv_car.setOnClickListener(addToCart);
        tv_car.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_White));
        tv_car.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.bg_hasOK));
        rv_car.addView(tv_car);
        linearLayoutHORIZONTALAllParent.addView(rv_car);
        //endregion
        addView(linearLayoutHORIZONTALAllParent);

    }

    private void initialized () {
        eiv_production.setPath(imgPath);
        onePurchaseWaitingBuyInfo.setTextSize(smallTextSize/1.2f);
        onePurchaseWaitingBuyInfo.setInfo(productionName,nowPrice,hasEnteredNumber,allNeedNumber,remainingEnterNumber);

    }

    //region Action
    private OnClickListener comeToDetailForProduction = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO: 2017/3/15 进入商品详情页面
            Default.showToast("id" + goods_id, Toast.LENGTH_LONG);

        }
    };

    private OnClickListener addToCart = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO: 2017/3/20 加入购物车
            Default.showToast("加入购物车" + goods_id, Toast.LENGTH_LONG);

        }
    };
    //endregion
}
