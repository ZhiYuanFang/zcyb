package com.wzzc.onePurchase.activity.index.main_view.home;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/3/25.
 */

public class OnePurchaseItemHeadlineView extends BaseView{
    @ViewInject(R.id.tv_headline_name)
    private TextView tv_headline_name;
    @ViewInject(R.id.tv_icon)
    private TextView tv_icon;
    @ViewInject(R.id.iv_goto)
    private ImageView iv_goto;
    private String headLine;
    private @ColorInt Integer color;
    private @ColorInt Integer headColor;
    private Drawable nextDrawable;
    public OnePurchaseItemHeadlineView(Context context) {
        super(context);
        init();
    }

    public OnePurchaseItemHeadlineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }


    private void init(AttributeSet attrs){
        TypedArray t = getContext().obtainStyledAttributes(attrs, R.styleable.OnePurchaseItemHeadlineElement);
        headLine = t.getString(R.styleable.OnePurchaseItemHeadlineElement_headline);
        color = t.getColor(R.styleable.OnePurchaseItemHeadlineElement_iconColor, ContextCompat.getColor(getContext(),R.color.bg_hasOK));
        headColor = t.getColor(R.styleable.OnePurchaseItemHeadlineElement_headColor,ContextCompat.getColor(getContext(),R.color.tv_Black));
        nextDrawable = t.getDrawable(R.styleable.OnePurchaseItemHeadlineElement_nextDrawable);
        init();
    }

    private void init(){
        if (headLine == null) {
            headLine = "";
        }

        if (nextDrawable == null) {
            nextDrawable = ContextCompat.getDrawable(getContext(),R.drawable.settinggoto);
        }

        if (color == null) {
            color = ContextCompat.getColor(getContext(),R.color.bg_hasOK);
        }

        if (headColor == null) {
            headColor = ContextCompat.getColor(getContext(),R.color.tv_Black);
        }
        tv_headline_name.setText(headLine);
        iv_goto.setBackground(nextDrawable);
        tv_icon.setBackgroundColor(color);
        tv_headline_name.setTextColor(headColor);
    }

}
