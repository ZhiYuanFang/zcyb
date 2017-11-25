package com.wzzc.onePurchase.item;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wzzc.base.Default;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.onePurchase.action.LayoutTouchListener;
import com.wzzc.onePurchase.childview.info.OnePurchaseLuckyCodeInfo;
import com.wzzc.onePurchase.view.NextArrorView;

/**
 * Created by toutou on 2017/3/20.
 *
 * 个人主页/晒单/获得的商品
 * 我的晒单/已晒单
 * 中奖商品
 * 我要晒单/晒单详情（显示产品信息）
 */

public class OnePurchaseShowOrderItem extends RelativeLayout {

    private Integer HEIGHT_IMG;
    private Integer WIDTH_NEXT_ARROR;

    private String goods_id;
    private String imgPath ;
    private String productionName , nowPrice ;
    private String lucky_code;
    /** 揭晓时间*/
    private String announcement_time;
    private boolean hasInit;
    ExtendImageView eiv_production; OnePurchaseLuckyCodeInfo onePurchaseLuckyCodeInfo;
    public OnePurchaseShowOrderItem(Context context) {
        super(context);
        init();
    }
    public OnePurchaseShowOrderItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public void setInfo (String goods_id,String imgPath ,String productionName,String nowPrice,String lucky_code , String announcement_time) {
        this.goods_id = goods_id;
        this.imgPath = imgPath;
        this.productionName = productionName;
        this.nowPrice = nowPrice;
        this.lucky_code = lucky_code;
        this.announcement_time = announcement_time;
        initialized();
    }

    private void init(){
        setOnTouchListener(new LayoutTouchListener(getContext()));
        HEIGHT_IMG = Default.dip2px(80f,getContext());
        WIDTH_NEXT_ARROR = HEIGHT_IMG/3;
        Integer space = Default.dip2px(5,getContext());
        Integer smallTextSize = Default.dip2px(8,getContext());

        LinearLayout linearLayoutHORIZONTALAllParent = new LinearLayout(getContext());
        LinearLayout.LayoutParams lp_allParent = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT);
        //region set AllParentLayout WEIGHT_SUM = 1
        linearLayoutHORIZONTALAllParent.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutHORIZONTALAllParent.setLayoutParams(lp_allParent);
        linearLayoutHORIZONTALAllParent.setWeightSum(1);
        linearLayoutHORIZONTALAllParent.setOnClickListener(comeToDetailForProduction);
        //endregion

        //region Add IMGLayout to allParent WEIGHT = 0 , WIDTH == HEIGHT_IMG;
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

        //region Add ProductionInfo to allParent WEIGHT = 1
        onePurchaseLuckyCodeInfo = new OnePurchaseLuckyCodeInfo(getContext());
        LinearLayout.LayoutParams lp_info = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        onePurchaseLuckyCodeInfo.setLayoutParams(lp_info);
        lp_info.weight = 1;
        lp_info.setMarginEnd(20);
        linearLayoutHORIZONTALAllParent.addView(onePurchaseLuckyCodeInfo);
        //endregion

        //region Add NextArror to allParent WEIGHT = 0 ; WIDTH == WIDTH_NEXT_ARROR
        NextArrorView rLayout_Arror = new NextArrorView(getContext());

        linearLayoutHORIZONTALAllParent.addView(rLayout_Arror);
        //endregion
            addView(linearLayoutHORIZONTALAllParent);

    }

    private void initialized () {
        eiv_production.setPath(imgPath);
        onePurchaseLuckyCodeInfo.setInfo(productionName,nowPrice,lucky_code,announcement_time);

    }

    //region Action
    private OnClickListener comeToDetailForProduction = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO: 2017/3/15 进入商品详情页面
            Default.showToast("id" + goods_id, Toast.LENGTH_LONG);

        }
    };
    //endregion
}
