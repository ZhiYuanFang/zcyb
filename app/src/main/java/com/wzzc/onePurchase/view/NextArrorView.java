package com.wzzc.onePurchase.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.Default;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/3/22.
 */

public class NextArrorView extends RelativeLayout {

    private Integer WIDTH_NEXT_ARROR;
    Integer space;

    public NextArrorView(Context context) {
        super(context);
        init();
    }

    public NextArrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        WIDTH_NEXT_ARROR = Default.dip2px(27f, getContext());
        space = Default.dip2px(5, getContext());
        LinearLayout.LayoutParams lp_rLayout = new LinearLayout.LayoutParams(WIDTH_NEXT_ARROR, ViewGroup.LayoutParams.MATCH_PARENT);
        lp_rLayout.weight = 0;
        setLayoutParams(lp_rLayout);
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bg_hasOK));
        lp_rLayout.setMargins(0, space * 2, 0, space * 2);
        setGravity(Gravity.CENTER);

        TextView tv_nextArror = new TextView(getContext());
        LinearLayout.LayoutParams lp_arror = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv_nextArror.setLayoutParams(lp_arror);
        tv_nextArror.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.next_arror));

        addView(tv_nextArror);
    }
}
