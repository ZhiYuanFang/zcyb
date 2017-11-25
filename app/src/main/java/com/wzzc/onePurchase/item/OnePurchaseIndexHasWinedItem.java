package com.wzzc.onePurchase.item;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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
import com.wzzc.zcyb365.R;

/**
 * Created by toutou on 2017/3/15.
 *
 * 主页/最新揭晓
 */

public class OnePurchaseIndexHasWinedItem extends RelativeLayout {

    private Integer HEIGHT_IMG;
    private String goods_id;
    private String imgPath,winMan;
    ExtendImageView extendImageViewForProduction;TextView tv_winMan;
    public OnePurchaseIndexHasWinedItem(Context context) {
        super(context);

        init();
    }
    public OnePurchaseIndexHasWinedItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public void setInfo (String goods_id,String imgPath , String winMan){
        this.goods_id = goods_id;
        this.imgPath = imgPath;
        this.winMan = winMan;
        initialized();
    }

    private void init () {
        HEIGHT_IMG = Default.dip2px(120,getContext());
        Integer smalltextSize = Default.dip2px(5, getContext());
        setOnTouchListener(new LayoutTouchListener(getContext()));
        Integer space = Default.dip2px(3, getContext());

        LinearLayout linearLayoutVerticalAllParent = new LinearLayout(getContext());
        LinearLayout.LayoutParams lp_allParent = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT);
        //region SetAllParentLayout
        linearLayoutVerticalAllParent.setLayoutParams(lp_allParent);
        linearLayoutVerticalAllParent.setOrientation(LinearLayout.VERTICAL);
        linearLayoutVerticalAllParent.setGravity(CENTER_HORIZONTAL);
        linearLayoutVerticalAllParent.setWeightSum(2);
        linearLayoutVerticalAllParent.setOnClickListener(comeToDetailForProduction);
        linearLayoutVerticalAllParent.setOnTouchListener(new LayoutTouchListener(getContext()));
        linearLayoutVerticalAllParent.setPadding(0,0,space,0);
        //endregion

        //region Add ProductionExtendImage to AllParent WEIGHT = 0.3
        RelativeLayout relativeLayoutExtendImageView = new RelativeLayout(getContext());
        LinearLayout.LayoutParams lp_ExtendImageView = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT );
        //region setExtendImage's Layout
        relativeLayoutExtendImageView.setLayoutParams(lp_ExtendImageView);
        relativeLayoutExtendImageView.setPadding(0,space,0,space);
        lp_ExtendImageView.weight = 0.3f;
        relativeLayoutExtendImageView.setGravity(Gravity.CENTER);
        //endregion

        //region AddExtendImage's Layout
        extendImageViewForProduction = new ExtendImageView(getContext());
        LayoutParams lp_extendImage = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , HEIGHT_IMG);
        extendImageViewForProduction.setLayoutParams(lp_extendImage);
        extendImageViewForProduction.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        relativeLayoutExtendImageView.addView(extendImageViewForProduction);
        //endregion
        linearLayoutVerticalAllParent.addView(relativeLayoutExtendImageView);

        //endregion

        //region Add WinMan to AllParent WEIGHT = 1.7
        tv_winMan = new TextView(getContext());
        LinearLayout.LayoutParams lp_winMan = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT);
        tv_winMan.setLayoutParams(lp_winMan);
        tv_winMan.setTextSize(smalltextSize);
        lp_winMan.weight = 1.7f;

        tv_winMan.setGravity(Gravity.CENTER);
        linearLayoutVerticalAllParent.addView(tv_winMan);
        //endregion


        addView(linearLayoutVerticalAllParent);

    }

    private void initialized (){
        extendImageViewForProduction.setPath(imgPath);
        SpannableString spaPhone = new SpannableString(winMan);
        spaPhone.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext() , R.color.tv_Red)),0 , spaPhone.length() , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        SpannableString cp = new SpannableString("恭喜获得");
        cp.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext() , R.color.tv_Gray)),0,cp.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append(spaPhone).append(cp);
        tv_winMan.setText(ssb);

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
