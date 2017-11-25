package com.wzzc.onePurchase.childview.info;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.wzzc.base.Default;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/3/20.
 */

public class OnePurchaseWaitingBuyInfo extends RelativeLayout {

    private String productionName, nowPrice;
    private Integer hasEnteredNumber, allNeedNumber, remainingEnterNumber;
    private boolean hasInit;
    private boolean hasPublished;
    TextView tv_productionName;
    TextView tv_price;
    SeekBar seekBar;
    TextView tv_number_hasEnteredNumber, tv_number_allNeedNumber, tv_number_remainingEnterNumber;
    LinearLayout linearLayoutVerticalActivityFitness;LinearLayout linearLayoutHorizontalFitnessInfo;
    TextView tv_published;TextView tv_profile_hasEntered;TextView tv_profile_allNeedNumber;
    TextView tv_profile_remainingEnter;
    public OnePurchaseWaitingBuyInfo(Context context) {
        super(context);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(lp);
        init();
    }

    public void setInfo(boolean hasPublished, String productionName, String nowPrice, Integer hasEnteredNumber, Integer allNeedNumber,
                        Integer remainingEnterNumber) {
        this.hasPublished = hasPublished;
        this.productionName = productionName;
        this.nowPrice = nowPrice;
        this.hasEnteredNumber = hasEnteredNumber;
        this.allNeedNumber = allNeedNumber;
        this.remainingEnterNumber = remainingEnterNumber;
        initialized();
    }
    float smalltextSize;

    /** 需要在setInfo之前调用此方法才能生效 推荐显示揭晓界面设置7*/
    public void setTextSize (float size) {
        smalltextSize = size;

    }

    public void setInfo(String productionName, String nowPrice, Integer hasEnteredNumber, Integer allNeedNumber,
                        Integer remainingEnterNumber) {
        this.productionName = productionName;
        this.nowPrice = nowPrice;
        this.hasEnteredNumber = hasEnteredNumber;
        this.allNeedNumber = allNeedNumber;
        this.remainingEnterNumber = remainingEnterNumber;
        initialized();
    }

    private void init() {
        Integer space = 5;
        smalltextSize = Default.dip2px(4, getContext());

        LinearLayout linearLayoutVerticalAllParent = new LinearLayout(getContext());
        LinearLayout.LayoutParams lp_allParent = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayoutVerticalAllParent.setLayoutParams(lp_allParent);
        linearLayoutVerticalAllParent.setWeightSum(1);
        linearLayoutVerticalAllParent.setOrientation(LinearLayout.VERTICAL);

        //region Add ProductionProLayout to AllParent WEIGHT == 0.5 , WEIGHT_SUM = 2
        LinearLayout linearLayoutVerticalProductionPro = new LinearLayout(getContext());
        LinearLayout.LayoutParams lp_productionProLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //region setProductionProLayout
        linearLayoutVerticalProductionPro.setLayoutParams(lp_productionProLayout);
        linearLayoutVerticalProductionPro.setOrientation(LinearLayout.VERTICAL);
        lp_productionProLayout.weight = 0.5f;
        linearLayoutVerticalProductionPro.setWeightSum(2);
        linearLayoutVerticalProductionPro.setGravity(Gravity.CENTER);
        //endregion

        //region Add ProductionName to ProductionProLayout WEIGHT = 1.5 , TextSize = Default.px2dip(36,getContext())
        tv_productionName = new TextView(getContext());
        LinearLayout.LayoutParams lp_productionName = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //region setProductionName TextSize = Default.px2dip(36,getContext())
        tv_productionName.setLayoutParams(lp_productionName);
        lp_productionName.weight = 1.5f;
        tv_productionName.setTextColor(ContextCompat.getColor(getContext(), R.color.tv_Black));

        //endregion
        linearLayoutVerticalProductionPro.addView(tv_productionName);
        //endregion

        //region Add ProductionPrice to ProductionProLayout WEIGHT = 0.5
        tv_price = new TextView(getContext());
        LinearLayout.LayoutParams lp_productionPrice = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //region setProductionPrice
        tv_price.setLayoutParams(lp_productionPrice);
        lp_productionPrice.weight = 0.5f;
        //endregion
        linearLayoutVerticalProductionPro.addView(tv_price);
        //endregion

        linearLayoutVerticalAllParent.addView(linearLayoutVerticalProductionPro);
        //endregion

        //region Add ActivityFitnessLayout to AllParent WEIGHT == 0.5
        linearLayoutVerticalActivityFitness = new LinearLayout(getContext());
        LinearLayout.LayoutParams lp_fitness = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //region SetFitnessLayout WEIGHT_SUM = 1
        linearLayoutVerticalActivityFitness.setLayoutParams(lp_fitness);
        lp_fitness.weight = 0.5f;
        linearLayoutVerticalActivityFitness.setWeightSum(1);
        linearLayoutVerticalActivityFitness.setOrientation(LinearLayout.VERTICAL);
        //endregion


        tv_published = new TextView(getContext());
        tv_published.setText("本期已揭晓");
        tv_published.setTextColor(ContextCompat.getColor(getContext(), R.color.tv_Gray));
        LayoutParams lp_published = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tv_published.setLayoutParams(lp_published);
        tv_published.setGravity(Gravity.CENTER);


        //region AddSeekBar to FitnessLayout WEIGHT = 0
        seekBar = new SeekBar(getContext());
        LinearLayout.LayoutParams lp_seekBar = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //region SetSeekBar
        lp_seekBar.weight = 0;
        seekBar.setLayoutParams(lp_seekBar);

        seekBar.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        seekBar.setThumb(ContextCompat.getDrawable(getContext(), R.drawable.seekbar_fitness_thumb));
        //endregion

        //endregion

        //region AddFitnessInfoLayout to FitnessLayout WEIGHT = 1 , WEIGHT_SUM = 3
        linearLayoutHorizontalFitnessInfo = new LinearLayout(getContext());
        LinearLayout.LayoutParams lp_fitnessInfoLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //region SetFitnessInfoLayout
        linearLayoutHorizontalFitnessInfo.setLayoutParams(lp_fitnessInfoLayout);
        lp_fitnessInfoLayout.weight = 1;
        linearLayoutHorizontalFitnessInfo.setWeightSum(3);

        //endregion


        //region 已参与
        //region linearLayoutVerticalFitnessInfoChildhasEntered to FitnessInfoLayout WEIGHT = 1 , WEIGHT_SUM = 2
        LinearLayout linearLayoutVerticalFitnessInfoChildhasEntered = new LinearLayout(getContext());
        LinearLayout.LayoutParams lp_fitnessInfoChild = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //region SetFitnessInfoChild
        linearLayoutVerticalFitnessInfoChildhasEntered.setOrientation(LinearLayout.VERTICAL);
        linearLayoutVerticalFitnessInfoChildhasEntered.setLayoutParams(lp_fitnessInfoChild);
        linearLayoutVerticalFitnessInfoChildhasEntered.setWeightSum(2);
        lp_fitnessInfoChild.weight = 1;
        //endregion

        //endregion

        //region Add infoChildTextNumber to InfoChild
        tv_number_hasEnteredNumber = new TextView(getContext());
        LinearLayout.LayoutParams lp_number = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv_number_hasEnteredNumber.setLayoutParams(lp_number);
        lp_number.weight = 1;
        tv_number_hasEnteredNumber.setTextColor(ContextCompat.getColor(getContext(), R.color.tv_Red));
        tv_number_hasEnteredNumber.setSingleLine(true);
        tv_number_hasEnteredNumber.setGravity(Gravity.START);
        linearLayoutVerticalFitnessInfoChildhasEntered.addView(tv_number_hasEnteredNumber);
        //endregion

        //region Add infoChildTextProfile to infoChild
        tv_profile_hasEntered = new TextView(getContext());
        LinearLayout.LayoutParams lp_profile = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv_profile_hasEntered.setLayoutParams(lp_profile);
        lp_profile.weight = 1;
        tv_profile_hasEntered.setTextColor(ContextCompat.getColor(getContext(), R.color.tv_Gray));

        tv_profile_hasEntered.setSingleLine(true);
        tv_profile_hasEntered.setText("已参与");
        linearLayoutVerticalFitnessInfoChildhasEntered.addView(tv_profile_hasEntered);
        //endregion
        //endregion

        //region 总人次
        //region linearLayoutVerticalFitnessInfoChildhasEntered to FitnessInfoLayout WEIGHT = 1 , WEIGHT_SUM = 2
        LinearLayout linearLayoutVerticalFitnessInfoChildallNeed = new LinearLayout(getContext());
        //region SetFitnessInfoChild
        linearLayoutVerticalFitnessInfoChildallNeed.setOrientation(LinearLayout.VERTICAL);
        linearLayoutVerticalFitnessInfoChildallNeed.setLayoutParams(lp_fitnessInfoChild);
        linearLayoutVerticalFitnessInfoChildallNeed.setWeightSum(2);
        //endregion

        //endregion

        //region Add infoChildTextNumber to InfoChild
        tv_number_allNeedNumber = new TextView(getContext());
        tv_number_allNeedNumber.setLayoutParams(lp_number);
        tv_number_allNeedNumber.setTextColor(ContextCompat.getColor(getContext(), R.color.tv_Red));
        tv_number_allNeedNumber.setSingleLine(true);
        tv_number_allNeedNumber.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayoutVerticalFitnessInfoChildallNeed.addView(tv_number_allNeedNumber);
        //endregion

        //region Add infoChildTextProfile to infoChild
        tv_profile_allNeedNumber = new TextView(getContext());
        tv_profile_allNeedNumber.setLayoutParams(lp_profile);
        tv_profile_allNeedNumber.setTextColor(ContextCompat.getColor(getContext(), R.color.tv_Gray));
        tv_profile_allNeedNumber.setSingleLine(true);
        tv_profile_allNeedNumber.setGravity(Gravity.CENTER_HORIZONTAL);
        tv_profile_allNeedNumber.setText("总人次");
        linearLayoutVerticalFitnessInfoChildallNeed.addView(tv_profile_allNeedNumber);
        //endregion
        //endregion

        //region 剩余
        //region linearLayoutVerticalFitnessInfoChildhasEntered to FitnessInfoLayout WEIGHT = 1 , WEIGHT_SUM = 2
        LinearLayout linearLayoutVerticalFitnessInfoChildremainingEnter = new LinearLayout(getContext());
        //region SetFitnessInfoChild
        linearLayoutVerticalFitnessInfoChildremainingEnter.setOrientation(LinearLayout.VERTICAL);
        linearLayoutVerticalFitnessInfoChildremainingEnter.setLayoutParams(lp_fitnessInfoChild);
        linearLayoutVerticalFitnessInfoChildremainingEnter.setWeightSum(2);
        //endregion

        //endregion

        //region Add infoChildTextNumber to InfoChild
        tv_number_remainingEnterNumber = new TextView(getContext());
        tv_number_remainingEnterNumber.setLayoutParams(lp_number);
        tv_number_remainingEnterNumber.setTextColor(ContextCompat.getColor(getContext(), R.color.tv_Red));
        tv_number_remainingEnterNumber.setSingleLine(true);

        tv_number_remainingEnterNumber.setGravity(Gravity.END);
        linearLayoutVerticalFitnessInfoChildremainingEnter.addView(tv_number_remainingEnterNumber);
        //endregion

        //region Add infoChildTextProfile to infoChild
        tv_profile_remainingEnter = new TextView(getContext());
        tv_profile_remainingEnter.setLayoutParams(lp_profile);
        tv_profile_remainingEnter.setTextColor(ContextCompat.getColor(getContext(), R.color.tv_Gray));

        tv_profile_remainingEnter.setSingleLine(true);
        tv_profile_remainingEnter.setText("剩余");
        tv_profile_remainingEnter.setGravity(Gravity.END);
        linearLayoutVerticalFitnessInfoChildremainingEnter.addView(tv_profile_remainingEnter);
        //endregion
        //endregion

        linearLayoutHorizontalFitnessInfo.addView(linearLayoutVerticalFitnessInfoChildhasEntered);
        linearLayoutHorizontalFitnessInfo.addView(linearLayoutVerticalFitnessInfoChildallNeed);
        linearLayoutHorizontalFitnessInfo.addView(linearLayoutVerticalFitnessInfoChildremainingEnter);

        //endregion



        linearLayoutVerticalAllParent.addView(linearLayoutVerticalActivityFitness);
    //endregion

    addView(linearLayoutVerticalAllParent);

}

    private void initialized() {

        //region setSize
        tv_productionName.setTextSize(smalltextSize * 1.2f);
        tv_price.setTextSize(smalltextSize);
        tv_published.setTextSize(smalltextSize);
        tv_number_hasEnteredNumber.setTextSize(smalltextSize);
        tv_profile_hasEntered.setTextSize(smalltextSize);
        tv_number_remainingEnterNumber.setTextSize(smalltextSize);
        tv_number_allNeedNumber.setTextSize(smalltextSize);
        tv_profile_allNeedNumber.setTextSize(smalltextSize);
        tv_profile_remainingEnter.setTextSize(smalltextSize);

        //endregion

        tv_productionName.setText(productionName);
        if (nowPrice != null) {
            SpannableStringBuilder sbuilder = new SpannableStringBuilder();
            SpannableString spa = new SpannableString(nowPrice);
            spa.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.tv_Red)), 0, spa.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            sbuilder.append(spa);
            tv_price.setText(sbuilder);
        } else {
            tv_price.setText("");
        }

        seekBar.setMax(allNeedNumber);
        seekBar.setProgress(hasEnteredNumber);
        tv_number_hasEnteredNumber.setText(String.valueOf(hasEnteredNumber));
        tv_number_allNeedNumber.setText(String.valueOf(allNeedNumber));
        tv_number_remainingEnterNumber.setText(String.valueOf(remainingEnterNumber));
        linearLayoutVerticalActivityFitness.removeAllViews();
        if (hasPublished) {
            linearLayoutVerticalActivityFitness.addView(tv_published);
        } else {
            linearLayoutVerticalActivityFitness.addView(seekBar);
            linearLayoutVerticalActivityFitness.addView(linearLayoutHorizontalFitnessInfo);
        }
    }
}
