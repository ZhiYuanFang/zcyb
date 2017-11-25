package com.wzzc.other_view.popDialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.wzzc.other_view.SlideView.slidePager.SlidePagerCountView;
import com.wzzc.other_function.SlideViewPagerAdapter;
import com.wzzc.zcyb365.R;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/7/3.
 */

public class PopDialog extends Dialog {
    ViewPager viewPager;
    SlidePagerCountView pageCount;
    ImageButton ib_close;

    public PopDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public PopDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init();
    }

    protected void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_pop_dialog, null);
        ib_close = (ImageButton) view.findViewById(R.id.imageButton);
        ib_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        pageCount = (SlidePagerCountView) view.findViewById(R.id.pageCount);
        setContentView(view);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pageCount.setIndex(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setInfo (ArrayList<View> views,int position) {
        for (View v : views) {
            if (v.getParent() != null){
                ((ViewGroup)v.getParent()).removeAllViews();// 从父布局脱离
            }
        }
        pageCount.setCount(views.size());
        viewPager.setAdapter(new SlideViewPagerAdapter(views));
        viewPager.setCurrentItem(position);
    }
}
