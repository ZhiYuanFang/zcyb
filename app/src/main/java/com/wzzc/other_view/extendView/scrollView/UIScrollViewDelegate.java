package com.wzzc.other_view.extendView.scrollView;

import android.view.View;

/**
 * Created by storecode on 15-12-9.
 */
public interface UIScrollViewDelegate {
    void scrollTo(int x, int y);

    void setOnTouchListener(View.OnTouchListener listener);

    void setScrollBarEnabled(boolean enabled);

    void TouchUp();

    boolean isTop();

    boolean isBottom(int height);

    int getVerticalOffset();

    int getVerticalSize();

    void scrollToY(int Y, boolean animated);

}
