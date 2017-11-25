package com.wzzc.other_view.extendView;

import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by storecode on 15-12-9.
 */
public class ExtendAnimation extends Animation {
    private int mstartvalue;
    private int mdeltavalue;
    private ExtendAnimationnListener main_listener;

    public ExtendAnimation(int startvalue, int endvalue) {
        mstartvalue = startvalue;
        mdeltavalue = endvalue - startvalue;
        this.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                main_listener.onEnd();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        main_listener.onChange((int) (mstartvalue + mdeltavalue * interpolatedTime));
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }

    public void setChangeListene(ExtendAnimationnListener listene) {
        this.main_listener = listene;
    }

    public interface ExtendAnimationnListener {
        void onChange(int value);
        void onEnd();
    }
}
