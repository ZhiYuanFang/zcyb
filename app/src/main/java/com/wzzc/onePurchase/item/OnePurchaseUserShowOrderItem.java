package com.wzzc.onePurchase.item;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wzzc.base.Default;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.onePurchase.childview.info.OnePurchaseUserShowOrderInfo;
import com.wzzc.onePurchase.view.NextArrorView;
import com.wzzc.zcyb365.R;

/**
 * Created by toutou on 2017/3/20.
 *
 * 没用
 */

public class OnePurchaseUserShowOrderItem extends RelativeLayout {
    private Integer HEIGHT_IMG;

    private String rec_id;
    private String imgPath;
    private String userName;
    private Integer allEnterBuyNumber;
    private String lucky_code;
    private String announcement_time;
    private String buyTime;
    private boolean hasInit;
    ExtendImageView eiv_production;
    OnePurchaseUserShowOrderInfo onePurchaseUserShowOrderInfo;
    boolean hasPublished;ImageView img_coat;
    public OnePurchaseUserShowOrderItem(Context context) {
        super(context);

        init();
    }

    public OnePurchaseUserShowOrderItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setInfo(boolean hasPublished , String rec_id, String imgPath, String userName, Integer allEnterBuyNumber, String lucky_code, String announcement_time, String buyTime) {
        this.hasPublished = hasPublished;
        this.rec_id = rec_id;
        this.imgPath = imgPath;
        this.userName = userName;
        this.allEnterBuyNumber = allEnterBuyNumber;
        this.lucky_code = lucky_code;
        this.announcement_time = announcement_time;
        this.buyTime = buyTime;
        initialized();
    }

    private void init() {

        HEIGHT_IMG = Default.dip2px(80f, getContext());
//        setOnTouchListener(new LayoutTouchListener(getContext()));
        Integer space = Default.dip2px(5, getContext());
        setPadding(0,space * 3,0,space*3);
        LinearLayout linearLayoutHORIZONTALAllParent = new LinearLayout(getContext());
        LinearLayout.LayoutParams lp_allParent = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
        LayoutParams lp_productionIMG = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        eiv_production.setLayoutParams(lp_productionIMG);

        eiv_production.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        relativeLayoutIMG.addView(eiv_production);

        img_coat = new ImageView(getContext());
        LayoutParams lp_coat = new LayoutParams(HEIGHT_IMG *2 /3 , HEIGHT_IMG *2 /3);
        img_coat.setLayoutParams(lp_coat);

        relativeLayoutIMG.addView(img_coat);
        //endregion

        linearLayoutHORIZONTALAllParent.addView(relativeLayoutIMG);
        //endregion

        //region Add ProductionInfo to allParent WEIGHT = 1
        onePurchaseUserShowOrderInfo = new OnePurchaseUserShowOrderInfo(getContext());
        LinearLayout.LayoutParams lp_info = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        onePurchaseUserShowOrderInfo.setLayoutParams(lp_info);
        lp_info.weight = 1;
        lp_info.setMarginEnd(20);
        linearLayoutHORIZONTALAllParent.addView(onePurchaseUserShowOrderInfo);
        //endregion

        //region Add NextArror to allParent WEIGHT = 0 ; WIDTH == WIDTH_NEXT_ARROR
        NextArrorView rLayout_Arror = new NextArrorView(getContext());

        linearLayoutHORIZONTALAllParent.addView(rLayout_Arror);
        //endregion
        addView(linearLayoutHORIZONTALAllParent);

    }

    private void initialized() {
        eiv_production.setPath(imgPath);
        onePurchaseUserShowOrderInfo.setInfo(userName, allEnterBuyNumber, lucky_code, announcement_time, buyTime);
        if (hasPublished) {
            img_coat.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.last_win_coat));
        } else {
            img_coat.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.product_win_coat));
        }
    }

    //region Action
    private OnClickListener comeToDetailForProduction = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO: 2017/3/15 进入用户详情页面
            Default.showToast("用户id" + rec_id, Toast.LENGTH_LONG);

        }
    };
    //endregion
}
