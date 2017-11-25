package com.wzzc.onePurchase.item;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wzzc.base.Default;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.onePurchase.action.LayoutTouchListener;
import com.wzzc.onePurchase.childview.info.OnePurchaseWaitingBuyInfo;
import com.wzzc.zcyb365.R;

/**
 * Created by toutou on 2017/3/14.
 * <p>
 * 主页/今日限时 | 热门推介
 * <p>
 * 显示揭晓/今日揭晓
 *
 * 当使用该item时，需要在载体（listView）外添加scrollerView 同时初始化ListView高度。
 */

public class OnePurchaseIndexProductionWaitingBuyItem extends RelativeLayout {

    private static final int TIMER = 0;
    private String goods_id;
    private String imgPath;
    private String productionName, nowPrice;
    private Integer hasEnteredNumber, allNeedNumber, remainingEnterNumber;
    private Long shortTime;
    private boolean hasInit;
    ExtendImageView extendImageViewForProduction;
    OnePurchaseWaitingBuyInfo info;
    RelativeLayout relativeLayoutShortTime;Integer space;Integer smalltextSize;
    TextView tv_shortTime;
    public OnePurchaseIndexProductionWaitingBuyItem(Context context) {
        super(context);
        init();
    }
    public OnePurchaseIndexProductionWaitingBuyItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    /** 需要在setInfo之前调用此方法才能生效 ， 推荐显示揭晓界面设置7*/
    public void setTextSize (Integer size) {
        smalltextSize = Default.dip2px(size, getContext());
    }
    public void setInfo(String goods_id, Long shortTime, String imgPath, String productionName, String nowPrice, Integer hasEnteredNumber, Integer allNeedNumber,
                        Integer remainingEnterNumber) {
        this.goods_id = goods_id;
        this.shortTime = shortTime;
        this.imgPath = imgPath;
        this.productionName = productionName;
        this.nowPrice = nowPrice;
        this.hasEnteredNumber = hasEnteredNumber;
        this.allNeedNumber = allNeedNumber;
        this.remainingEnterNumber = remainingEnterNumber;
        initialized();
    }

    /**
     * 创建界面
     */
    private void init() {
        setOnTouchListener(new LayoutTouchListener(getContext()));
        space = Default.dip2px(3, getContext());
        smalltextSize = Default.dip2px(4, getContext());
        LinearLayout linearLayoutVerticalAllParent = new LinearLayout(getContext());
        LinearLayout.LayoutParams lp_allParent = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayoutVerticalAllParent.setLayoutParams(lp_allParent);
        //region setAllParent WEIGHTSUM = 2
        linearLayoutVerticalAllParent.setOrientation(LinearLayout.VERTICAL);
        linearLayoutVerticalAllParent.setWeightSum(2);
        linearLayoutVerticalAllParent.setOnClickListener(comeToDetailForProduction);
        linearLayoutVerticalAllParent.setPadding(0,0,space,0);

        //endregion

        //region Add shortTime's Layout to AllParent WEIGHT == 0

        relativeLayoutShortTime = new RelativeLayout(getContext());
        LinearLayout.LayoutParams lp_shortTime = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //region setShortTime's Layout
        relativeLayoutShortTime.setLayoutParams(lp_shortTime);
        relativeLayoutShortTime.setGravity(Gravity.CENTER);
        //endregion



//        linearLayoutVerticalAllParent.addView(relativeLayoutShortTime);

        //endregion

        //region Add ProductionExtendImage to AllParent WEIGHT = 0.6
        RelativeLayout relativeLayoutExtendImageView = new RelativeLayout(getContext());
        LinearLayout.LayoutParams lp_ExtendImageView = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 240);
        //region setExtendImage's Layout
        relativeLayoutExtendImageView.setLayoutParams(lp_ExtendImageView);
//        relativeLayoutExtendImageView.setPadding(0, 15, 0, 15);
        lp_ExtendImageView.weight = 0.6f;
        relativeLayoutExtendImageView.setGravity(Gravity.CENTER);
        //endregion

        //region AddExtendImage's Layout
        extendImageViewForProduction = new ExtendImageView(getContext());
        LayoutParams lp_extendImage = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        extendImageViewForProduction.setLayoutParams(lp_extendImage);
        extendImageViewForProduction.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        relativeLayoutExtendImageView.addView(extendImageViewForProduction);

        relativeLayoutExtendImageView.addView(relativeLayoutShortTime);
        //endregion
        linearLayoutVerticalAllParent.addView(relativeLayoutExtendImageView);

        //endregion

        //region Add ProductionWaitingBuyInfo to AllParent WIGHT = 1.4
        info = new OnePurchaseWaitingBuyInfo(getContext());
        LinearLayout.LayoutParams lp_info = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        info.setLayoutParams(lp_info);
        lp_info.weight = 1.4f;
        linearLayoutVerticalAllParent.addView(info);
        //endregion

        addView(linearLayoutVerticalAllParent);


    }

    private void initialized() {


//region AddShortTime to ShortTime's Layout
        tv_shortTime = new TextView(getContext());
        tv_shortTime.setTextSize(smalltextSize);
        LinearLayout.LayoutParams lp_tv_shortTime = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv_shortTime.setLayoutParams(lp_tv_shortTime);
        tv_shortTime.setSingleLine(true);
        tv_shortTime.setPadding(space, space, space, space);
        tv_shortTime.setTextColor(ContextCompat.getColor(getContext(), R.color.tv_White));
        tv_shortTime.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bg_hasOK));
        relativeLayoutShortTime.addView(tv_shortTime);

        if (shortTime != null) {
            relativeLayoutShortTime.setVisibility(VISIBLE);
            Message msg = new Message();
            Long[] date = convertSecondsToDate(shortTime);
            msg.what = TIMER;
            msg.obj = new Object[]{tv_shortTime, date};
            handler.handleMessage(msg);
        } else {
            relativeLayoutShortTime.setVisibility(GONE);
        }
        //endregion
        extendImageViewForProduction.setPath(imgPath);
        info.setTextSize(smalltextSize);
        info.setInfo(productionName, nowPrice, hasEnteredNumber, allNeedNumber, remainingEnterNumber);

    }

    //region Action
    private OnClickListener comeToDetailForProduction = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO: 2017/3/14 进入商品详情页面
            Default.showToast("id" + goods_id, Toast.LENGTH_LONG);
        }
    };
    //endregion

    //region Handler
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIMER:
                    Object obj[] = (Object[]) msg.obj;
                    TextView tv_shortTimer = (TextView) obj[0];
                    Long[] date = (Long[]) obj[1];
                    tv_shortTimer.setText(rangeDateToString(date));


                    if (convertDateToSeconds(date) > 0) {
                        Message message = new Message();
                        message.what = TIMER;
                        message.obj = new Object[]{tv_shortTimer, convertSecondsToDate(convertDateToSeconds(date) - 1)};
                        sendMessageDelayed(message, 1000);//每秒更新一次时间
                    } else {
                        // TODO: 2017/3/15 时间结束
                        tv_shortTimer.setText("Time is over !");
                    }
                    break;
                default:
            }
            super.handleMessage(msg);
        }
    };
    //endregion

    //region Time convert
    private Long[] convertSecondsToDate(Long seconds) {
        Long[] date = null;
        if (seconds > 0) {
            Double hour_double = Math.floor(seconds / 3600);
            Long hour = hour_double.longValue();/*Long.valueOf(String.valueOf(Math.floor(seconds/3600)));*/
            Double minute_double = Math.floor((seconds - hour * 3600) / 60);
            Long minute = minute_double.longValue();
            Long second = seconds - hour * 3600 - minute * 60;
            date = new Long[]{hour, minute, second};
        }
        return date;
    }

    private Long convertDateToSeconds(Long[] date) {
        if (date == null || date.length < 3) {
            return 0L;
        }
        Long hour = date[0];
        Long minute = date[1];
        Long second = date[2];
        return hour * 3600 + minute * 60 + second;
    }

    private String rangeDateToString(Long[] date) {
        if (date == null || date.length < 3) {
            return "";
        }
        Long hour = date[0];
        Long minute = date[1];
        Long second = date[2];

        String str_hour = String.valueOf(hour);
        String str_minute = String.valueOf(minute);
        String str_second = String.valueOf(second);

        if (str_hour.length() == 1) {
            str_hour = "0" + str_hour;
        }
        if (str_minute.length() == 1) {
            str_minute = "0" + str_minute;
        }
        if (str_second.length() == 1) {
            str_second = "0" + str_second;
        }
        return str_hour + "时" + str_minute + "分" + str_second + "秒";
    }

    //endregion
}
