package com.wzzc.onePurchase.action;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.wzzc.other_function.MessageBox;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/3/22.
 */

public class LayoutTouchListener implements View.OnTouchListener {
    private Context context;
    ScaleAnimation scaleAnimation;
    public LayoutTouchListener (Context context) {
        this.context = context;
        float toValue = 1.1f;
        scaleAnimation = new ScaleAnimation(1 , toValue , 1 , toValue , Animation.RELATIVE_TO_SELF ,
               0.5f , Animation.RELATIVE_TO_SELF ,0.5f);
        scaleAnimation.setDuration(300);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                v.startAnimation(scaleAnimation);
//                scaleAnimation.setFillAfter(true);
                v.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_collection));
                break;
            case MotionEvent.ACTION_UP:
//                scaleAnimation.setFillAfter(false);
                v.setBackgroundColor(ContextCompat.getColor(context,R.color.bg_White));
                break;
            case MotionEvent.ACTION_CANCEL:
//                scaleAnimation.setFillAfter(false);
                v.setBackgroundColor(ContextCompat.getColor(context,R.color.bg_White));
                break;
            default:
                MessageBox.Show("Action : " + event.getAction());
        }
        return false;
    }
}
