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
import com.wzzc.onePurchase.childview.info.OnePurchaseShowWinnerInfo;
import com.wzzc.onePurchase.view.NextArrorView;
import com.wzzc.zcyb365.R;

/**
 * Created by toutou on 2017/3/20.
 *
 * 我的云购记录/全部记录/已揭晓
 * 个人主页/夺宝记录/已揭晓
 * 云购详情
 */

public class OnePurchaseHasWinedInCloudShoppingItem extends RelativeLayout{
    private Integer HEIGHT_IMG;
    private Integer WIDTH_NEXT_ARROR;

    private String goods_id;
    private String imgPath ;
    private String productionName , nowPrice ;
    private String winnerName;
    /** 揭晓时间*/
    private String announcement_time;
    private boolean hasInit;
    ExtendImageView eiv_production;
    OnePurchaseShowWinnerInfo onePurchaseShowWinnerInfo;
    NextArrorView rLayout_Arror;

    public OnePurchaseHasWinedInCloudShoppingItem(Context context) {
        super(context);
        init();
    }
    public OnePurchaseHasWinedInCloudShoppingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    LinearLayout linearLayoutHORIZONTALAllParent;
    private void init(){
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(lp);
        HEIGHT_IMG = Default.dip2px(80f,getContext());
        WIDTH_NEXT_ARROR = HEIGHT_IMG/3;
        setOnTouchListener(new LayoutTouchListener(getContext()));
        float alphaForIntroduction = 0.8f;
        Integer space = Default.dip2px(5,getContext());
        Integer smallTextSize = Default.dip2px(7,getContext());

        linearLayoutHORIZONTALAllParent = new LinearLayout(getContext());
        LinearLayout.LayoutParams lp_allParent = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
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
        relativeLayoutIMG.setGravity(Gravity.BOTTOM);
        lp_IMGLayout.weight = 0;
        //region Add ExtendImageView to IMGLayout TOP = 0;
        eiv_production = new ExtendImageView(getContext());
        LayoutParams lp_productionIMG = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT);
        eiv_production.setLayoutParams(lp_productionIMG);
        eiv_production.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        relativeLayoutIMG.addView(eiv_production);
        //endregion

        //region Add Introductions to IMGLayout TOP = 1
        TextView tv_introduction = new TextView(getContext());
        LayoutParams lp_introduction = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , HEIGHT_IMG/4);
        tv_introduction.setLayoutParams(lp_introduction);
        tv_introduction.setText(R.string.hasOKForOnePurchase);
        tv_introduction.setTextSize(smallTextSize);
        tv_introduction.setGravity(Gravity.CENTER);
        tv_introduction.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_White));
        tv_introduction.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.bg_hasOK));
        tv_introduction.setAlpha(alphaForIntroduction);
        relativeLayoutIMG.addView(tv_introduction);

        relativeLayoutIMG.setPadding(0,0,space * 2,0);
        //endregion

        linearLayoutHORIZONTALAllParent.addView(relativeLayoutIMG);
        //endregion

        //region Add ProductionInfo to allParent WEIGHT = 1
        onePurchaseShowWinnerInfo = new OnePurchaseShowWinnerInfo(getContext());
        LinearLayout.LayoutParams lp_info = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT);
        onePurchaseShowWinnerInfo.setLayoutParams(lp_info);
        lp_info.weight = 1;
        lp_info.setMarginEnd(20);
        linearLayoutHORIZONTALAllParent.addView(onePurchaseShowWinnerInfo);
        //endregion

        //region Add NextArror to allParent WEIGHT = 0 ; WIDTH == WIDTH_NEXT_ARROR
       rLayout_Arror = new NextArrorView(getContext());

        //endregion
        addView(linearLayoutHORIZONTALAllParent);
    }
boolean hasNextArror;
    public void setInfo (String goods_id,String imgPath ,String productionName,String nowPrice,String winnerName , String announcement_time , boolean hasNextArror) {
        this.goods_id = goods_id;
        this.imgPath = imgPath;
        this.productionName = productionName;
        this.nowPrice = nowPrice;
        this.winnerName = winnerName;
        this.announcement_time = announcement_time;
        this.hasNextArror = hasNextArror;
        initialized ();
    }

    private void initialized() {
        eiv_production.setPath(imgPath);
        onePurchaseShowWinnerInfo.setInfo(productionName,nowPrice,winnerName,announcement_time);
        try {
            linearLayoutHORIZONTALAllParent.removeView(rLayout_Arror);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (hasNextArror) {
            linearLayoutHORIZONTALAllParent.addView(rLayout_Arror);
        }
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
