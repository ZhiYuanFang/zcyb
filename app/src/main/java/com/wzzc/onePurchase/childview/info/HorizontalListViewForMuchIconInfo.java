package com.wzzc.onePurchase.childview.info;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by by Administrator on 2017/3/23.
 */

public class HorizontalListViewForMuchIconInfo extends ListView {

    private GestureDetector mGestureDetector;

    public HorizontalListViewForMuchIconInfo(Context context) {
        super(context);
        init();
    }

    public HorizontalListViewForMuchIconInfo(Context context, AttributeSet attrs) {
        super(context, attrs);
       init();
    }

    private void init () {
        mGestureDetector = new GestureDetector(new YScrollDetector());
        setFadingEdgeLength(0);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev) || mGestureDetector.onTouchEvent(ev);
    }

    private class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            if(distanceY !=0 && distanceX != 0){
                if (Math.abs(distanceY) >= Math.abs(distanceX)) {
                    return true;
                }
                return false;
            }
            return false;
        }
    }
}
