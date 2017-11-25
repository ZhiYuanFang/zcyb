package com.wzzc.other_view.extendView.scrollView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.wzzc.other_view.extendView.ExtendAnimation;

/**
 * Created by storecode on 15-12-9.
 */
public class UIScrollView extends RelativeLayout implements View.OnTouchListener {
    private UIScrollListener scrollListener;
    private UIScrollViewDelegate mainview;
    private ViewGroup topview;
    private ViewGroup bottomview;

    private int oldTY = 0;
    private int oldBY = 0;
    private int oldY = 0;
    private boolean istop = false;
    private boolean isbottom = false;



    public UIScrollView(Context context) {
        super(context);
        CreateView();
    }

    public UIScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        CreateView();
    }

    public UIScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        CreateView();
    }

    public void CreateView() {
        if (!isInEditMode()) {

        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!isInEditMode()) {
            if (mainview == null) {
                switch (getChildCount()) {
                    case 1:
                        mainview = (UIScrollViewDelegate) getChildAt(0);
                        break;
                    case 2:
                        if (getChildAt(0) instanceof UIScrollViewDelegate) {
                            mainview = (UIScrollViewDelegate) getChildAt(0);
                            bottomview = (ViewGroup) getChildAt(1);
                        } else {
                            topview = (ViewGroup) getChildAt(0);
                            mainview = (UIScrollViewDelegate) getChildAt(1);
                        }
                        break;
                    case 3:
                        topview = (ViewGroup) getChildAt(0);
                        mainview = (UIScrollViewDelegate) getChildAt(1);
                        bottomview = (ViewGroup) getChildAt(2);
                        break;
                }
                mainview.setOnTouchListener(this);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                oldTY = 0;
                oldBY = getHeight();
                istop = false;
                isbottom = false;
                oldY = Y;
                break;
            case MotionEvent.ACTION_UP:
                oldTY = 0;
                oldBY = getHeight();
                istop = false;
                isbottom = false;
                oldY = 0;
                EndTopBottomView();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                boolean isrun = false;
                if (oldY < Y && mainview.isTop()) {
                    if (Y - oldTY >= 0) {
                        if (!istop) {
                            oldTY = Y;
                            istop = true;
                        }
                        ChangeTopBottomView(Y - oldTY);
                        isrun = true;
                    }
                }
                if (oldY > Y && mainview.isBottom(getHeight())) {
                    if (Y - oldBY <= 0) {
                        if (!isbottom) {
                            oldBY = Y;
                            isbottom = true;
                        }
                        ChangeTopBottomView(Y - oldBY);
                        isrun = true;
                    }
                }
                if (!isrun) {
                    oldTY = 0;
                    oldBY = getHeight();
                    istop = false;
                    isbottom = false;
                    RecoverTopBottomView();
                }
                break;
        }
        if (scrollListener != null) {
            scrollListener.onScrollChanged(getVerticalOffset());
        }
        return istop || isbottom;
    }

    private int getTopViewHeight() {
        if (topview != null) {
            return topview.getHeight();
        }
        return 0;
    }

    private int getBottomViewHeight() {
        if (bottomview != null) {
            return bottomview.getHeight();
        }
        return 0;
    }

    public int getVerticalOffset() {
        if (topview != null && topview.getHeight() > 0) {
            return -topview.getHeight();
        } else if (bottomview != null && bottomview.getHeight() > 0) {
            return mainview.getVerticalOffset() + bottomview.getHeight();
        }
        return mainview.getVerticalOffset();
    }

    public void setVerticalOffset(int position) {
        if (position < 0) {
            mainview.scrollToY(0, false);
            ExtendAnimation animation = new ExtendAnimation(0, -position);
            animation.setDuration(200);
            animation.setInterpolator(new DecelerateInterpolator());
            animation.setChangeListene(new ExtendAnimation.ExtendAnimationnListener() {
                @Override
                public void onChange(int value) {
                    ViewGroup.LayoutParams para = topview.getLayoutParams();
                    para.height = value;
                    topview.setLayoutParams(para);
                    if (scrollListener != null) {
                        scrollListener.onScrollChanged(getVerticalOffset());
                    }
                }

                @Override
                public void onEnd() {
                    scrollListener.onScrollEnd(getVerticalOffset());
                }
            });
            topview.startAnimation(animation);
        } else {
            if (position > getVerticalOffset() + getHeight()) {
                bottomview.clearAnimation();
                ExtendAnimation animation = new ExtendAnimation(0, position - getVerticalOffset() + getHeight());
                animation.setDuration(200);
                animation.setInterpolator(new DecelerateInterpolator());
                animation.setChangeListene(new ExtendAnimation.ExtendAnimationnListener() {
                    @Override
                    public void onChange(int value) {
                        ViewGroup.LayoutParams para = bottomview.getLayoutParams();
                        para.height = value;
                        bottomview.setLayoutParams(para);
                        if (scrollListener != null) {
                            scrollListener.onScrollChanged(getVerticalOffset());
                        }
                    }

                    @Override
                    public void onEnd() {
                        scrollListener.onScrollEnd(getVerticalOffset());
                    }
                });
                bottomview.startAnimation(animation);
            } else {
                mainview.scrollToY(position, true);
            }
        }
    }

    public int getVerticalSize() {
        return mainview.getVerticalSize();
    }

    private void ChangeTopBottomView(int value) {

        int maxheight = (int) (300 * this.getContext().getResources().getDisplayMetrics().density + 0.5f);
        if (value > 0) {
            if (topview == null) {
                return;
            }
            if (value > maxheight) {
                value = maxheight;
            }
            LayoutParams lpara = (LayoutParams) topview.getLayoutParams();
            lpara.height = GetChangeValue(value);
            topview.setLayoutParams(lpara);
        } else {
            if (bottomview == null) {
                return;
            }
            if (value < -maxheight) {
                value = -maxheight;
            }
            value = GetChangeValue(-value);
            mainview.scrollTo(0, getVerticalOffset() + value);
            LayoutParams lpara = (LayoutParams) bottomview.getLayoutParams();
            lpara.height = value;
            bottomview.setLayoutParams(lpara);

        }
    }

    public void EndTopBottomView() {
        boolean bo = false;
        if (scrollListener != null) {
            bo = scrollListener.onScrollEnd(getVerticalOffset());
        }
        if (!bo) {
            RecoverTopBottomView();
        }
    }

    public void RecoverTopBottomView() {
        if (topview != null && topview.getHeight() != 0) {
            topview.clearAnimation();
            ExtendAnimation animation = new ExtendAnimation(topview.getHeight(), 0);
            animation.setDuration(200);
            animation.setInterpolator(new DecelerateInterpolator());
            animation.setChangeListene(new ExtendAnimation.ExtendAnimationnListener() {
                @Override
                public void onChange(int value) {
                    ViewGroup.LayoutParams para = topview.getLayoutParams();
                    para.height = value;
                    topview.setLayoutParams(para);
                    if (scrollListener != null) {
                        scrollListener.onScrollChanged(getVerticalOffset());
                    }
                }

                @Override
                public void onEnd() {

                }
            });
            topview.startAnimation(animation);
        } else if (bottomview != null && bottomview.getHeight() != 0) {
            bottomview.clearAnimation();
            ExtendAnimation animation = new ExtendAnimation(bottomview.getHeight(), 0);
            animation.setDuration(200);
            animation.setInterpolator(new DecelerateInterpolator());
            animation.setChangeListene(new ExtendAnimation.ExtendAnimationnListener() {
                @Override
                public void onChange(int value) {
                    ViewGroup.LayoutParams para = bottomview.getLayoutParams();
                    para.height = value;
                    bottomview.setLayoutParams(para);
                    if (scrollListener != null) {
                        scrollListener.onScrollChanged(getVerticalOffset());
                    }
                }

                @Override
                public void onEnd() {

                }
            });
            bottomview.startAnimation(animation);
        }
    }

    private int GetChangeValue(int value) {
        value = (int) ((149.0 / 16) * Math.pow(value, 0.5) - (133.0 / 16));
        return value < 0 ? 0 : value;
    }

    public void setScrollChange(UIScrollListener listener) {
        this.scrollListener = listener;
    }


}
