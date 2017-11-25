package com.wzzc.other_view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by by Administrator on 2017/8/5.
 */

public class OverrideScrollView extends ScrollView {
    private OnScrollChangeListener scrollViewListener;
    public OverrideScrollView(Context context) {
        super(context);
    }

    public OverrideScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OverrideScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnScrollChangeListener (OnScrollChangeListener oscl){
        scrollViewListener = oscl;
    };

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    public interface OnScrollChangeListener {
        void onScrollChanged(OverrideScrollView osv , int x, int y, int oldx, int oldy);
    }
}
