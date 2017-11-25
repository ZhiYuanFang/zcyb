package com.wzzc.other_view.SlideView;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.SlideViewPagerAdapter;
import com.wzzc.other_view.SlideView.slidePager.FixedSpeedScroller;
import com.wzzc.other_view.SlideView.slidePager.SlidePagerCountView;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zcyb365 on 2016/10/6.
 * <p>
 * 首页 滑动pager楼层
 */
public class SlideView extends BaseView {
    //region 组件
    @ViewInject(R.id.main_view)
    private ViewPager viewPager;
    @ViewInject(R.id.page_view)
    private SlidePagerCountView pageCount;
    //endregion
    private Timer main_time;
    private List<View> list;
    //region handler
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int now = viewPager.getCurrentItem() + 1;
            if (now >= viewPager.getAdapter().getCount()) {
                now = 0;
            }
            final int cNow = now;
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(cNow);
                }
            });
        }
    };


    //endregion

    public SlideView(Context context) {
        super(context);
    }

    public SlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void CreateView() {
        FixedSpeedScroller fixedSpeedScroller = new FixedSpeedScroller(this.getContext());
        fixedSpeedScroller.setViewPagerScrollSpeed(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pageCount.setIndex(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
        if (main_time != null) {
            main_time.cancel();
            main_time = null;
        }
        main_time = new Timer(true);
        main_time.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        }, 5000, 5000);


        viewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        if (main_time != null) {
                            main_time.cancel();
                            main_time = null;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        main_time = new Timer(true);
                        main_time.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Message message = new Message();
                                handler.sendMessage(message);
                            }
                        }, 5000, 5000);
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (main_time != null) {
                            main_time.cancel();
                            main_time = null;
                        }
                        break;
                }
                return false;
            }
        });

    }

    public void setInfo (final SlideDelegate slideDelegate , JSONArray jrrItems){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        list = new ArrayList<>();
        for (int i = 0; i < jrrItems.length(); i++) {
            ExtendImageView imageView = new ExtendImageView(this.getContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(params);
            final JSONObject jsonItem = JsonClass.jjrr(jrrItems,i);
            if (jsonItem.has("src")) {
                imageView.setPath(JsonClass.sj(jsonItem,"src"));
            } else {
                imageView.setPath(JsonClass.sj(jsonItem,"ad_code"));
            }
            if (slideDelegate != null) {
                final int finalI = i;
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        slideDelegate.clickItem(finalI , v,jsonItem);
                    }
                });
            }

            list.add(imageView);
        }

        pageCount.setCount(list.size());
        SlideViewPagerAdapter adapter = new SlideViewPagerAdapter(list);
        viewPager.setAdapter(adapter);
        this.CreateView();
    }
    public void setPageCountImageSelected(@DrawableRes int drawable) {
        pageCount.select_img = drawable;
    }
}
