package com.wzzc.onePurchase.childview.info;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.Default;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

/**
 * Created by toutou on 2017/3/20.
 *
 *最新揭晓
 */

public class OnePurchaseLatestAnnouncementInfo extends RelativeLayout {

    private Integer HEIGHT_ICON;
    private String userIconPath, userName,  luckyCode, announcement_time;
    private Integer enterAllNumber;
    private boolean hasInit;
    ExtendImageView eiv;TextView tv_name;TextView tv_number;TextView tv_luckyCode;TextView tv_showTime;
    public OnePurchaseLatestAnnouncementInfo(Context context) {
        super(context);
        HEIGHT_ICON = Default.dip2px(60, getContext());
        init();
    }

    private void init(){
        Integer space = 5;
        Integer textSize = Default.dip2px(7,getContext());

        LinearLayout linearLayoutVerticalAllParent = new LinearLayout(getContext());
        LinearLayout.LayoutParams lp_allParent = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT );
        linearLayoutVerticalAllParent.setLayoutParams(lp_allParent);
        linearLayoutVerticalAllParent.setOrientation(LinearLayout.VERTICAL);

        //region add user
        LinearLayout linearLayoutUserLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams lp_userLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , HEIGHT_ICON);

        linearLayoutUserLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutUserLayout.setLayoutParams(lp_userLayout);
        eiv = new ExtendImageView(getContext());
        LayoutParams lp_eiv = new LayoutParams(HEIGHT_ICON , ViewGroup.LayoutParams.MATCH_PARENT );
        eiv.setLayoutParams(lp_eiv);
        eiv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        linearLayoutUserLayout.addView(eiv);

        LinearLayout layout_Info = new LinearLayout(getContext());
        LayoutParams lp_info = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT );
        layout_Info.setOrientation(LinearLayout.VERTICAL);
        layout_Info.setWeightSum(2);
        layout_Info.setLayoutParams(lp_info);

        tv_name = new TextView(getContext());
        LinearLayout.LayoutParams lp_name = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT );
        lp_name.weight = 1;
        tv_name.setLayoutParams(lp_name);
        tv_name.setTextSize(textSize);
        tv_name.setSingleLine(true);
        tv_name.setGravity(Gravity.BOTTOM);
        layout_Info.addView(tv_name);

        tv_number = new TextView(getContext());
        LinearLayout.LayoutParams lp_number = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT );
        lp_number.weight = 1;
        tv_number.setSingleLine(true);
        tv_number.setLayoutParams(lp_number);

        tv_number.setTextSize(textSize);
        tv_number.setGravity(Gravity.START);
        linearLayoutUserLayout.addView(layout_Info);
        layout_Info.addView(tv_number);

        linearLayoutVerticalAllParent.addView(linearLayoutUserLayout);

        //endregion

        //region add luckyCode
        tv_luckyCode = new TextView(getContext());

        LinearLayout.LayoutParams lp_luckyCode = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT );
        tv_luckyCode.setGravity(Gravity.CENTER_VERTICAL);
        tv_luckyCode.setLayoutParams(lp_luckyCode);
        tv_luckyCode.setTextSize(textSize);
        linearLayoutVerticalAllParent.addView(tv_luckyCode);
        //endregion

        //region add showTime
        tv_showTime = new TextView(getContext());
        LinearLayout.LayoutParams lp_showTime = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT );

        tv_showTime.setGravity(Gravity.CENTER_VERTICAL);
        tv_showTime.setLayoutParams(lp_showTime);
        tv_showTime.setTextSize(textSize);
        tv_showTime.setTextColor(ContextCompat.getColor(getContext(), R.color.tv_Gray));
        linearLayoutVerticalAllParent.addView(tv_showTime);
        //endregion

        addView(linearLayoutVerticalAllParent);
    }

    public void setInfo(String userIconPath, String userName, Integer enterAllNumber, String luckyCode, String announcement_time) {
        this.userIconPath = userIconPath;
        this.userName = userName;
        this.enterAllNumber = enterAllNumber;
        this.luckyCode = luckyCode;
        this.announcement_time = announcement_time;
        initialized();
    }

    private void initialized() {
        eiv.setPath(userIconPath);
        SpannableStringBuilder sb_name = new SpannableStringBuilder();
        SpannableString spa_name = new SpannableString(userName);
        spa_name.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(),R.color.tv_Red)),0,spa_name.length() , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb_name.append("获得者：").append(spa_name);
        tv_name.setText(sb_name);
        SpannableStringBuilder sb_number = new SpannableStringBuilder();
        SpannableString spa_number = new SpannableString(String.valueOf(enterAllNumber));
        spa_number.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(),R.color.tv_Red)),0,spa_number.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb_number.append("本期云购：").append(spa_number);
        tv_number.setText(sb_number);
        SpannableString spa_luckyCode = new SpannableString(luckyCode);
        spa_luckyCode.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.tv_Red)),0,spa_luckyCode.length() , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableStringBuilder sb_luckyCode = new SpannableStringBuilder();
        sb_luckyCode.append("幸运码：").append(spa_luckyCode);
        tv_luckyCode.setText(sb_luckyCode);
        tv_showTime.setText("揭晓时间："+ announcement_time);

    }
}
