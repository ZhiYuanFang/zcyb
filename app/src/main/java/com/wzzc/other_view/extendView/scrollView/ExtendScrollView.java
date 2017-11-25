package com.wzzc.other_view.extendView.scrollView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.wzzc.other_view.extendView.ExtendAnimation;

/**
 * Created by storecode on 15-12-9.
 */
public class ExtendScrollView extends ScrollView implements UIScrollViewDelegate {
    public ExtendScrollView(Context context) {
        super(context);
        this.setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public ExtendScrollView(Context context, AttributeSet attrs) {
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
        return getVerticalOffset() + height >= getVerticalSize();
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
                    smoothScrollTo(0, value);
                }

                @Override
                public void onEnd() {

                }
            });
            startAnimation(animation);
        } else {
            smoothScrollTo(0, Y);
        }
    }

}
