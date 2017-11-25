package com.wzzc.onePurchase.childview.info;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.Default;
import com.wzzc.zcyb365.R;

/**
 * Created by toutou on 2017/3/20.
 */

public class OnePurchaseLuckyCodeInfo extends RelativeLayout {

    private String productionName , nowPrice  ;
    private String lucky_code;
    /** 揭晓时间*/
    private String announcement_time;
    private boolean hasInit;
    TextView tv_productionName;TextView tv_price;TextView tv_winner;TextView tv_annoucementTime;
    public OnePurchaseLuckyCodeInfo(Context context) {
        super(context);
        init();
    }



    public void setInfo (String productionName,String nowPrice,String lucky_code , String announcement_time) {
        this.productionName = productionName;
        this.nowPrice = nowPrice;
        this.lucky_code = lucky_code;
        this.announcement_time = announcement_time;
        initialized();
    }


    private void init(){
        Integer space = 15;
        Integer smalltextSize = Default.dip2px(8,getContext());

        LinearLayout linearLayoutVerticalAllParent = new LinearLayout(getContext());
        LinearLayout.LayoutParams lp_allParent = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayoutVerticalAllParent.setLayoutParams(lp_allParent);
        linearLayoutVerticalAllParent.setOrientation(LinearLayout.VERTICAL);

        //region Add ProductionProLayout to AllParent
        LinearLayout linearLayoutVerticalProductionPro = new LinearLayout(getContext());
        LinearLayout.LayoutParams lp_productionProLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT );
        //region setProductionProLayout
        linearLayoutVerticalProductionPro.setLayoutParams(lp_productionProLayout);
        linearLayoutVerticalProductionPro.setOrientation(LinearLayout.VERTICAL);
        linearLayoutVerticalProductionPro.setGravity(Gravity.CENTER);
        //endregion

        //region Add ProductionName to ProductionProLayout
        tv_productionName = new TextView(getContext());
        LinearLayout.LayoutParams lp_productionName = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        //region setProductionName TextSize = Default.px2dip(36,getContext())
        tv_productionName.setLayoutParams(lp_productionName);

        tv_productionName.setTextColor(ContextCompat.getColor(getContext(), R.color.tv_Black));
        tv_productionName.setTextSize(smalltextSize * 1.2f);
        //endregion
        linearLayoutVerticalProductionPro.addView(tv_productionName);
        //endregion

        //region Add ProductionPrice to ProductionProLayout
        tv_price = new TextView(getContext());
        LinearLayout.LayoutParams lp_productionPrice = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        //region setProductionPrice
        tv_price.setLayoutParams(lp_productionPrice);
        tv_price.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
        tv_price.setTextSize(smalltextSize);
        //endregion
        linearLayoutVerticalProductionPro.addView(tv_price);
        //endregion

        linearLayoutVerticalAllParent.addView(linearLayoutVerticalProductionPro);
        //endregion

        //region Add ActivityFitnessLayout to AllParent
        LinearLayout linearLayoutVerticalActivityFitness = new LinearLayout(getContext());
        LinearLayout.LayoutParams lp_fitness = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        //region SetFitnessLayout
        linearLayoutVerticalActivityFitness.setLayoutParams(lp_fitness);
        linearLayoutVerticalActivityFitness.setOrientation(LinearLayout.VERTICAL);
        //endregion

        //region Add winnerTextView to activityFitnessLayout
        tv_winner = new TextView(getContext());
        LinearLayout.LayoutParams lp_winner = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT);
        tv_winner.setLayoutParams(lp_winner);

        tv_winner.setTextSize(smalltextSize);
        tv_winner.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
        linearLayoutVerticalActivityFitness.addView(tv_winner);
        //endregion

        //region Add announcement_time to activityFitnessLayout
        tv_annoucementTime = new TextView(getContext());
        LinearLayout.LayoutParams lp_annoucementTiem = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT);
        tv_annoucementTime.setLayoutParams(lp_annoucementTiem);
        tv_annoucementTime.setTextSize(smalltextSize);
        tv_annoucementTime.setSingleLine(true);
        tv_annoucementTime.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
        linearLayoutVerticalActivityFitness.addView(tv_annoucementTime);

        //endregion

        linearLayoutVerticalAllParent.addView(linearLayoutVerticalActivityFitness);
        //endregion


        addView(linearLayoutVerticalAllParent);
    }

    private void initialized () {
        tv_productionName.setText(productionName);
        tv_price.setText("价值：" + nowPrice);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        SpannableString spannableString = new SpannableString(lucky_code);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(),R.color.tv_Red)),0,spannableString.length() , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append("幸运码：").append(spannableString);
        tv_winner.setText(spannableStringBuilder);
        tv_annoucementTime.setText("揭晓时间：" + announcement_time);

    }

}
