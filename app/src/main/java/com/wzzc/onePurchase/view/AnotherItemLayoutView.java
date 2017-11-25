package com.wzzc.onePurchase.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.OnClick;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.onePurchase.action.LayoutTouchListener;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/3/23.
 */

public class AnotherItemLayoutView extends BaseView {
    @ViewInject(R.id.tv_goods_name)
    private TextView tv_name;
    @ViewInject(R.id.arrow)
    private TextView arrow;
    @ViewInject(R.id.tv_other)
    private TextView tv_other;
    private Drawable arrow_drawable;
    private String title;
    private Integer title_size;
    private
    @ColorInt
    Integer title_color;
    private Drawable titleDrawable;

    public AnotherItemLayoutView(Context context) {
        super(context);

    }

    public AnotherItemLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    private void init(AttributeSet attrs) {
        TypedArray t = getContext().obtainStyledAttributes(attrs, R.styleable.OnePurchaseAnotherItemElement);
        arrow_drawable = t.getDrawable(R.styleable.OnePurchaseAnotherItemElement_arrow);
        title = t.getString(R.styleable.OnePurchaseAnotherItemElement_title);
        title_size = t.getInt(R.styleable.OnePurchaseAnotherItemElement_titleSize, 0);
        title_color = t.getColor(R.styleable.OnePurchaseAnotherItemElement_titleColor, ContextCompat.getColor(getContext(), R.color.tv_Gray));
        titleDrawable = t.getDrawable(R.styleable.OnePurchaseAnotherItemElement_titleDrawable);

        t.recycle();
    }

    @Override
    protected void viewFirstLoad() {
        super.viewFirstLoad();
        init();
    }

    public void setOtherInfo (String str) {
        tv_other.setText(str);
    }

    private void init() {
        Integer space = Default.dip2px(5, getContext());
        setPadding(0, space * 2, 0, space * 2);

        if (titleDrawable != null) {
            tv_name.setCompoundDrawablesWithIntrinsicBounds(titleDrawable,null,null,null);
        }

        if (arrow_drawable == null) {
            arrow_drawable = ContextCompat.getDrawable(getContext(), R.drawable.goto_shop);
        }

        if (title == null) {
            title = "---";
        }

        if (!(title_size == null || title_size == 0)) {
            tv_name.setTextSize(title_size);
        }

        if (title_color != null) {
            tv_name.setTextColor(title_color);
        }

        arrow.setBackground(arrow_drawable);

        tv_name.setText(title);

        if (!notUseTouchListener) {
            setOnTouchListener(new LayoutTouchListener(getContext()));
        }

    }

    public void setArrowOnClickListener (OnClickListener clickListener) {
        arrow.setOnClickListener(clickListener);
    }

    boolean notUseTouchListener;
    public void setNotUseTouchListener (boolean bool) {
        notUseTouchListener = bool;
    }

    public void setArrow(Drawable drawable) {
        arrow.setBackground(drawable);
    }

    public TextView getArrowText() {
        return arrow;
    }

    public void setInfo(CharSequence name) {
        tv_name.setText(name);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = 10;
        int h = 9;

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }
}
