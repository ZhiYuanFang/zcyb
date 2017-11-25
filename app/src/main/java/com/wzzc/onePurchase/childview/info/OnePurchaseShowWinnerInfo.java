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
import android.widget.SeekBar;
import android.widget.TextView;

import com.wzzc.base.Default;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/3/20.
 */

public class OnePurchaseShowWinnerInfo extends RelativeLayout{

    private String productionName , nowPrice  ;
    private String winnerName;
    /** 揭晓时间*/
    private String announcement_time;
    private boolean hasInit;
    TextView tv_winner;TextView tv_price;TextView tv_productionName;
    TextView tv_annoucementTime;
    public OnePurchaseShowWinnerInfo(Context context) {
        super(context);
        init();
    }

    public void setInfo (String productionName,String nowPrice,String winnerName , String announcement_time) {
        this.productionName = productionName;
        this.nowPrice = nowPrice;
        this.winnerName = winnerName;
        this.announcement_time = announcement_time;
        initialized();
    }

    private void init(){
        Integer space = Default.dip2px(5,getContext());
        Integer smalltextSize = Default.dip2px(7,getContext());

        LinearLayout linearLayoutVerticalAllParent = new LinearLayout(getContext());
        LinearLayout.LayoutParams lp_allParent = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayoutVerticalAllParent.setLayoutParams(lp_allParent);
        linearLayoutVerticalAllParent.setWeightSum(2);
        linearLayoutVerticalAllParent.setOrientation(LinearLayout.VERTICAL);

        //region Add ProductionProLayout to AllParent
        LinearLayout linearLayoutVerticalProductionPro = new LinearLayout(getContext());
        LinearLayout.LayoutParams lp_productionProLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT );
        //region setProductionProLayout
        lp_productionProLayout.weight = 0.9f;
        linearLayoutVerticalProductionPro.setWeightSum(2);
        linearLayoutVerticalProductionPro.setLayoutParams(lp_productionProLayout);
        linearLayoutVerticalProductionPro.setOrientation(LinearLayout.VERTICAL);
        linearLayoutVerticalProductionPro.setGravity(Gravity.CENTER);
        //endregion

        //region Add ProductionName to ProductionProLayout
       tv_productionName = new TextView(getContext());
        LinearLayout.LayoutParams lp_productionName = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        lp_productionName.weight = 1;
        //region setProductionName
        tv_productionName.setLayoutParams(lp_productionName);
        tv_productionName.setTextColor(ContextCompat.getColor(getContext(), R.color.tv_Black));
        tv_productionName.setTextSize(smalltextSize * 1.2f );
        //endregion
        linearLayoutVerticalProductionPro.addView(tv_productionName);
        //endregion

        //region Add ProductionPrice to ProductionProLayout
        tv_price = new TextView(getContext());
        LinearLayout.LayoutParams lp_productionPrice = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        lp_productionPrice.weight = 1;
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
        //region SetFitnessLayout WEIGHT_SUM = 2
        linearLayoutVerticalActivityFitness.setLayoutParams(lp_fitness);
        lp_fitness.weight = 1.1f;
        linearLayoutVerticalActivityFitness.setWeightSum(2);
        linearLayoutVerticalActivityFitness.setOrientation(LinearLayout.VERTICAL);
        //endregion

        //region Add winnerTextView to activityFitnessLayout
        tv_winner = new TextView(getContext());
        LinearLayout.LayoutParams lp_winner = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        lp_winner.weight = 1;
        tv_winner.setLayoutParams(lp_winner);
        tv_winner.setTextSize(smalltextSize);
        tv_winner.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
        linearLayoutVerticalActivityFitness.addView(tv_winner);
        //endregion

        //region Add announcement_time to activityFitnessLayout
        tv_annoucementTime = new TextView(getContext());
        LinearLayout.LayoutParams lp_annoucementTiem = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        lp_annoucementTiem.weight = 1;
        tv_annoucementTime.setLayoutParams(lp_annoucementTiem);
        tv_annoucementTime.setTextSize(smalltextSize);
        tv_annoucementTime.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
        linearLayoutVerticalActivityFitness.addView(tv_annoucementTime);

        //endregion

        linearLayoutVerticalAllParent.addView(linearLayoutVerticalActivityFitness);
        //endregion

        addView(linearLayoutVerticalAllParent);
    }

    private void initialized () {
        tv_annoucementTime.setText("揭晓时间：" + announcement_time);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        SpannableString spannableString = new SpannableString(winnerName);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(),R.color.tv_Red)),0,spannableString.length() , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append("获得者：").append(spannableString);
        tv_winner.setText(spannableStringBuilder);
        if (nowPrice != null) {
            tv_price.setText(nowPrice);
        } else {
            tv_price.setText("");
        }
        tv_productionName.setText(productionName);

    }
}
