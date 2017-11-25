package com.wzzc.onePurchase.childview.info;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wzzc.base.Default;
import com.wzzc.other_view.extendView.ExtendImageView;

import java.util.ArrayList;

/**
 * Created by toutou on 2017/3/22.
 * <p>
 * 横向图片集 ， Comment(评论图)
 */

public class HorizontalScrollViewForMuchIconInfo extends HorizontalScrollView {
    private ArrayList<String> images;
    LinearLayout layout_Images;
    private Integer ICONWIDTH;
    Integer space;
    private Integer scrollToX;

    public HorizontalScrollViewForMuchIconInfo(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizontalScrollViewForMuchIconInfo(Context context) {
        super(context);
        init();
    }

    private void init() {
        scrollToX = 0;
        ICONWIDTH = Default.dip2px(45, getContext());
        space = Default.dip2px(5, getContext());
        setHorizontalScrollBarEnabled(false);
        layout_Images = new LinearLayout(getContext());
        RelativeLayout.LayoutParams lp_layout_Images = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout_Images.setLayoutParams(lp_layout_Images);
        layout_Images.setOrientation(LinearLayout.HORIZONTAL);
        addView(layout_Images);
    }

    public void setScrollToX(Integer x) {
        this.scrollToX = x;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        scrollTo(scrollToX, 0);
    }

    /**
     * 需要在setInfo前调用此方法，不然无效
     */
    public void setICONWIDTH(Integer iconwidth) {
        ICONWIDTH = Default.dip2px(iconwidth, getContext());
    }

    public void setInfo(ArrayList<String> images) {
        this.images = images;
        initialized();
    }

    public void initialized() {
        if (layout_Images.getChildCount() > 0) {
            layout_Images.removeAllViews();
        }

        for (int i = 0; i < images.size(); i++) {
            RelativeLayout layout = new RelativeLayout(getContext());
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ICONWIDTH, ICONWIDTH);

            layout.setLayoutParams(lp);
            layout_Images.addView(layout);

            ExtendImageView eiv = new ExtendImageView(getContext());
            RelativeLayout.LayoutParams lp_eiv = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            eiv.setLayoutParams(lp_eiv);
            eiv.setPath(images.get(i));
            eiv.setTag(images.get(i));
            eiv.setOnClickListener(clickImageItemListener);
            eiv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            eiv.setPadding(0, 0, space, 0);
            layout.addView(eiv);
        }
    }

    private OnClickListener clickImageItemListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO: 2017/3/21 显示大图片
            Default.showToast("图片路径 ： " + v.getTag(), Toast.LENGTH_SHORT);
        }
    };


    public void setClickImageItemListener(OnClickListener listener) {
        clickImageItemListener = listener;
    }

}
