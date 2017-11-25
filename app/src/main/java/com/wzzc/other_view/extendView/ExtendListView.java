package com.wzzc.other_view.extendView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.wzzc.other_view.extendView.scrollView.UIScrollViewDelegate;

/**
 * Created by storecode on 15-11-3.
 */
public class ExtendListView extends ListView implements UIScrollViewDelegate {
    public ExtendListView(Context context) {
        super(context);
        this.setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public ExtendListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    public void setScrollBarEnabled(boolean enabled) {
        setHorizontalScrollBarEnabled(enabled);
        setVerticalScrollBarEnabled(enabled);
    }

    @Override
    public void TouchUp() {

    }


    @Override
    public boolean isTop() {
        return getVerticalOffset() == 0;
    }

    @Override
    public boolean isBottom(int height) {
        return false;
    }

    @Override
    public int getVerticalOffset() {
        return computeVerticalScrollOffset();
    }

    @Override
    public int getVerticalSize() {
        return computeVerticalScrollRange();
    }

    public void scrollToY(int Y, boolean animated) {
        if (animated) {
            clearAnimation();
            ExtendAnimation animation = new ExtendAnimation(computeVerticalScrollOffset(), Y);
            animation.setDuration(300);
            animation.setChangeListene(new ExtendAnimation.ExtendAnimationnListener() {
                @Override
                public void onChange(int value) {
                    smoothScrollToPositionFromTop(0, value);
                }

                @Override
                public void onEnd() {

                }
            });
            startAnimation(animation);
        } else {
            smoothScrollToPositionFromTop(0, Y);
        }
    }

}