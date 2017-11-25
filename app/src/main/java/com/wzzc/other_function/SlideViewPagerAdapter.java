package com.wzzc.other_function;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 10254 on 2016-10-06.
 *
 * viewPager 的适配器 可适用大部分viewPager
 */

public class SlideViewPagerAdapter extends PagerAdapter {

    List<View> viewLists;
    public SlideViewPagerAdapter() {
        viewLists = new ArrayList<>();
    }
    public SlideViewPagerAdapter(List<View> lists) {
        viewLists = lists;
    }

    public void clearView () {
        this.viewLists.clear();
    }
    public void addView (List<View> lists) {
        this.viewLists.addAll(lists);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return viewLists.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(ViewGroup view, int position, Object object) {
        view.removeView(viewLists.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View v = viewLists.get(position);
        if (v.getParent() != null) {
            ((ViewGroup)v.getParent()).removeView(v);
        }
        view.addView(v,0);
        return v;
    }


}