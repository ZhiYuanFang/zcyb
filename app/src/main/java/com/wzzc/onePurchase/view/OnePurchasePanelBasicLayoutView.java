package com.wzzc.onePurchase.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.zcyb365.R;

/**
 * Created by toutou on 2017/3/22.
 *
 * 除了主页 其他页面的基本模板
 */

public class OnePurchasePanelBasicLayoutView extends BaseView{

    @ViewInject(R.id.layout_title)
    private RelativeLayout layout_title;
    @ViewInject(R.id.layout_info)
    private RelativeLayout layout_info;
    @ViewInject(R.id.tv_color)
    private TextView tv_color;

    private int top_color;
    private View layout_info_view,layout_title_view;

    public OnePurchasePanelBasicLayoutView(Context context) {
        super(context);
        top_color =  ContextCompat.getColor(getContext(),R.color.bg_hasOK);
        init();
    }

    public OnePurchasePanelBasicLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init (AttributeSet attrs) {
        TypedArray t = getContext().obtainStyledAttributes(attrs,R.styleable.OnePurchasePanelBasicLayoutElement);
        top_color = t.getColor(R.styleable.OnePurchasePanelBasicLayoutElement_top_color, ContextCompat.getColor(getContext(),R.color.bg_hasOK));
        init();
        t.recycle();

    }

    private void init () {
        setTop_color(top_color);
    }

    public void setInfo (View layout_title_view,View layout_info_view) {
        setLayout_info(layout_info_view);
        setLayout_title(layout_title_view);
    }

    public void setTop_color (@ColorInt int top_color) {
        this.top_color = top_color;
        tv_color.setTextColor(top_color);
    }

    public void setTop_height (int height) {
        ViewGroup.LayoutParams lp = tv_color.getLayoutParams();
        lp.height = height;
        tv_color.setLayoutParams(lp);
    }

    protected void setLayout_title (View layout_title_view) {
        this.layout_title_view = layout_title_view;
        if (layout_title_view == null) {
            return;
        }
        if (layout_title.getChildCount() > 0) {
            layout_title.removeAllViews();
        }
        layout_title.addView(layout_title_view);
    }

    protected void setLayout_info (View layout_info_view) {
        this.layout_info_view = layout_info_view;
        if (layout_info_view == null) {
            return;
        }
        if (layout_info.getChildCount() > 0) {
            layout_info.removeAllViews();
        }
        layout_info.addView(layout_info_view);
    }

}
