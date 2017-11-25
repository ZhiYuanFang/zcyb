package com.wzzc.other_view.SlideView.slidePager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wzzc.base.Default;
import com.wzzc.zcyb365.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by storecode on 15-11-17.
 *
 * 配合viewPager使用 浮动点
 */
public class SlidePagerCountView extends LinearLayout {

    private List<ImageView> img_round = new ArrayList<>();
    public int select_img = R.drawable.img_android_round11;
    public int noselect_img = R.drawable.img_android_round12;

    public SlidePagerCountView(Context context) {
        super(context);
        loadView(context);
    }

    public SlidePagerCountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadView(context);
    }

    public SlidePagerCountView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadView(context);
    }

    public void loadView(Context context) {
        if (isInEditMode()) {
            return;
        }
        LayoutInflater.from(context).inflate(R.layout.view_slidepagercount, this);
    }

    public void changeImage(int select, int noselect) {
        select_img = select;
        noselect_img = noselect;
    }

    public void setCount(int count) {
        this.removeAllViews();
        LayoutParams ll1 = new LayoutParams(Default.dip2px(15, this.getContext()), Default.dip2px(37, this.getContext()));
        ll1.weight = 1;

        RelativeLayout.LayoutParams rl2 = new RelativeLayout.LayoutParams(/*Default.dip2px(7, this.getContext())*/ViewGroup.LayoutParams.WRAP_CONTENT, Default.dip2px(7, this.getContext()));
        rl2.addRule(RelativeLayout.CENTER_IN_PARENT);
        for (int i = 0; i < count; i++) {
            RelativeLayout prl = new RelativeLayout(this.getContext());
            prl.setLayoutParams(ll1);
            this.addView(prl);

            ImageView img_view = new ImageView(this.getContext());
            img_view.setLayoutParams(rl2);
            img_view.setImageResource(i == 0 ? select_img : noselect_img);
            prl.addView(img_view);
            img_round.add(img_view);
        }
    }

    public void setIndex(int index) {
        for (int i = 0; i < img_round.size(); i++) {
            img_round.get(i).setImageResource(noselect_img);
        }
        img_round.get(index).setImageResource(select_img);
    }
}
