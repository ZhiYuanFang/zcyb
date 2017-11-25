package com.wzzc.onePurchase.childview.info;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.Default;
import com.wzzc.zcyb365.R;

/**
 * Created by toutou on 2017/3/20.
 * <p>
 * 晒单详情（显示用户信息）
 */

public class OnePurchaseUserShowOrderInfo extends RelativeLayout {

    private String userName;
    private Integer allEnterBuyNumber;
    private String lucky_code;
    private String announcement_time;
    private String buyTime;
    private boolean hasInit;
    TextView tv_luckyBoy;
    TextView tv_allNumber;
    TextView tv_lucky_code;
    TextView tv_showTime;
    TextView tv_buyTime;

    public OnePurchaseUserShowOrderInfo(Context context) {
        super(context);
        init();
    }

    public void setInfo(String userName, Integer allEnterBuyNumber, String lucky_code, String announcement_time, String buyTime) {
        this.userName = userName;
        this.allEnterBuyNumber = allEnterBuyNumber;
        this.lucky_code = lucky_code;
        this.announcement_time = announcement_time;
        this.buyTime = buyTime;
        initialized();
    }

    private void init() {
        Integer space = Default.dip2px(5, getContext());
        Integer textSize = Default.dip2px(8, getContext());

        LinearLayout linearLayoutAllInfo = new LinearLayout(getContext());
        LayoutParams lp_allInfo = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayoutAllInfo.setLayoutParams(lp_allInfo);
        linearLayoutAllInfo.setOrientation(LinearLayout.VERTICAL);

        tv_luckyBoy = new TextView(getContext());
        LayoutParams lp_luckyBoy = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv_luckyBoy.setLayoutParams(lp_luckyBoy);
        tv_luckyBoy.setSingleLine(true);
        tv_luckyBoy.setTextSize(textSize);
        linearLayoutAllInfo.addView(tv_luckyBoy);

        tv_allNumber = new TextView(getContext());
        LayoutParams lp_allNumber = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv_allNumber.setLayoutParams(lp_allNumber);
        tv_allNumber.setSingleLine(true);
        tv_allNumber.setTextSize(textSize);
        linearLayoutAllInfo.addView(tv_allNumber);

        tv_lucky_code = new TextView(getContext());
        LayoutParams lp_lucky_code = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv_lucky_code.setLayoutParams(lp_lucky_code);
        tv_lucky_code.setSingleLine(true);
        tv_lucky_code.setTextSize(textSize);
        linearLayoutAllInfo.addView(tv_lucky_code);

        tv_showTime = new TextView(getContext());
        LayoutParams lp_showTime = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv_showTime.setLayoutParams(lp_showTime);
        tv_showTime.setSingleLine(true);
        tv_showTime.setTextSize(textSize);
        linearLayoutAllInfo.addView(tv_showTime);

//        if (buyTime != null) {
        tv_buyTime = new TextView(getContext());
        LayoutParams lp_buyTime = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv_buyTime.setLayoutParams(lp_buyTime);
        tv_buyTime.setSingleLine(true);
        tv_buyTime.setTextSize(textSize);
        linearLayoutAllInfo.addView(tv_buyTime);
//        }


        addView(linearLayoutAllInfo);

    }

    private void initialized() {
        SpannableStringBuilder sb_luckyBoy = new SpannableStringBuilder();
        SpannableString spa_luckyBoy = new SpannableString(userName);
        spa_luckyBoy.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.tv_Red)), 0, spa_luckyBoy.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb_luckyBoy.append("幸运获得者：").append(spa_luckyBoy);
        tv_luckyBoy.setText(sb_luckyBoy);
        SpannableStringBuilder sb_allNumber = new SpannableStringBuilder();
        SpannableString spa_allNumber = new SpannableString(String.valueOf(allEnterBuyNumber));
        spa_allNumber.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.tv_Red)), 0, spa_allNumber.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb_allNumber.append("总共云购：").append(spa_allNumber).append("人次");
        tv_allNumber.setText(sb_allNumber);
        SpannableStringBuilder sb_lucky_code = new SpannableStringBuilder();
        SpannableString spa_lucky_code = new SpannableString(lucky_code);
        spa_lucky_code.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.tv_Red)), 0, spa_lucky_code.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb_lucky_code.append("幸运运购码：").append(spa_lucky_code);
        tv_lucky_code.setText(sb_lucky_code);
        tv_showTime.setText("揭晓时间：" + announcement_time);
        if (buyTime != null) {
            tv_buyTime.setText("云购时间：" + buyTime);
            tv_buyTime.setVisibility(VISIBLE);
        } else {
            tv_buyTime.setVisibility(GONE);
        }


    }
}
