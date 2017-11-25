package com.wzzc.welcome;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.index.home.PopDelegate;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.action.ClickDelegate;
import com.wzzc.other_function.action.ItemClick;
import com.wzzc.other_view.SlideView.slidePager.FixedSpeedScroller;
import com.wzzc.other_view.SlideView.slidePager.SlidePagerCountView;
import com.wzzc.other_function.SlideViewPagerAdapter;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by by Administrator on 2017/6/22.
 */

public class PopView extends BaseView {
    PopDelegate pd;
    ClickDelegate clickDelegate;
    //region 组件
    @ViewInject(R.id.main_view)
    private ViewPager viewPager;
    @ViewInject(R.id.page_view)
    private SlidePagerCountView pageCount;
    @ViewInject(R.id.tv_turn)
    TextView tv_turn;
    //endregion
    private Timer main_time;
    private static final int scrollTime = 3000;
    boolean hasDismiss;
    //region handler
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (!hasDismiss) {
                int now = viewPager.getCurrentItem() + 1;
                if (now >= viewPager.getAdapter().getCount()) {
                    pd.dismissPopView();
                    hasDismiss = true;
                }
                final int cNow = now;
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem(cNow);
                    }
                });
            }

        }
    };


    //endregion
    public PopView(Context context) {
        super(context);
        init();
    }

    public PopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init (){
        tv_turn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.dismissPopView();
                hasDismiss = true;
            }
        });
    }
    public void setInfo(ClickDelegate clickDelegate , PopDelegate pd , JSONArray data) {
        this.clickDelegate = clickDelegate;
        System.out.println("``` popView data : " + data.toString());
        this.pd = pd;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        ArrayList<View> list = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            ExtendImageView imageView = new ExtendImageView(this.getContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(params);
            imageView.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            try {
                JSONObject json = JsonClass.jjrr(data,i);
                imageView.setTag(new String[]{JsonClass.sj(json,"ad_link"),JsonClass.sj(json,"data_type"),JsonClass.sj(json,"num_iid")});
                imageView.setOnClickListener(click);
                imageView.setPath(data.getJSONObject(i).getString("ad_code"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            list.add(imageView);
        }

        pageCount.setCount(data.length());
        SlideViewPagerAdapter adapter = new SlideViewPagerAdapter(list);
        viewPager.setAdapter(adapter);
        CreateView();

    }
    //region Action
    OnClickListener click = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String[] tags = (String[]) v.getTag();
            String da_link = tags[0];
            String data_type = tags[1];
            String num_iid = tags[2];

            if (!ItemClick.switchNormalListener(data_type, da_link)) {
                ItemClick.judgeSpecialListener(clickDelegate,v,data_type,da_link,num_iid);
            }
        }
    };
    //endregion
    private void CreateView() {
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
        }, scrollTime, scrollTime);


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
                        }, scrollTime, scrollTime);
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
}
