package com.wzzc.other_view.viewPager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.wzzc.other_function.SlideViewPagerAdapter;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/6/6.
 *
 */

public class MyViewPager extends ViewPager {
    public MyViewPagerDelegate mvpd;
    public MyViewPager(Context context) {
        super(context);
        init();
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        addOnPageChangeListener(onPageChangeListener);
    }

    public void setInfo (ArrayList<View> listView) {
        if (getAdapter() == null) {
            setAdapter(new SlideViewPagerAdapter());
        }
        ((SlideViewPagerAdapter)getAdapter()).clearView();
        ((SlideViewPagerAdapter)getAdapter()).addView(listView);
    }

    OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(final int position) {
             if (mvpd != null) {
                mvpd.onPageSelected(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void setCurrentItem(int item) {
        if (mvpd != null && item == getCurrentItem()) {
            mvpd.onPageSelected(item);
        }
        super.setCurrentItem(item);

    }
}
