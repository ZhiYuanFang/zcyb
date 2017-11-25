package com.wzzc.other_function;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by by Administrator on 2017/8/4.
 */

public class AnimatorHelper {
    public static boolean isAnimator ;

    public static void changeHeightForZero (final View v , final AnimatorDelegate delegate) {
        final Integer startValue = v.getMeasuredHeight();
        if (startValue == 0) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofInt(v.getMeasuredHeight(),0);
        animator.setEvaluator(new TypeEvaluator<Integer>() {

            @Override
            public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                int i = Math.abs(startValue - endValue);
                float f = i * (1-fraction);
                return (int)f;
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int y = (int) animation.getAnimatedValue();
                if (y == startValue && delegate != null) {
                    delegate.start();
                }
                ViewGroup.LayoutParams lp = v.getLayoutParams();
                lp.height = y;
                v.setLayoutParams(lp);
                if (y == 0 && delegate != null) {
                    delegate.end();
                    isAnimator = false;
                }
            }
        });
        animator.setDuration(500);
        if (!isAnimator) {
            animator.start();
        }
    }

    public static void changeHeightToInteger (final View v , int height , final AnimatorDelegate delegate) {
        System.out.println("changeHeightToInteger : " + height);
        final Integer startValue = v.getMeasuredHeight();
        ValueAnimator animator = ValueAnimator.ofInt(v.getMeasuredHeight(),height);
        animator.setEvaluator(new TypeEvaluator<Integer>() {

            @Override
            public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                int i = Math.abs(startValue - endValue);
                float f = startValue + i * fraction;
                return (int)f;
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int y = (int) animation.getAnimatedValue();
                if (y == startValue && delegate != null) {
                    delegate.start();
                }
                ViewGroup.LayoutParams lp = v.getLayoutParams();
                lp.height = y;
                v.setLayoutParams(lp);
                if (y == 0 && delegate != null) {
                    delegate.end();
                    isAnimator = false;
                }
            }
        });
        animator.setDuration(500);
        if (!isAnimator) {
            animator.start();
        }
    }

    public interface AnimatorDelegate {
        void start ();
        void end ();
    }
}
