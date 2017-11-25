package com.wzzc.onePurchase.activity.productDetail.mainView;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wzzc.base.Default;
import com.wzzc.onePurchase.activity.productDetail.ProductionDetailFragmentReSetDelegate;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/3/23.
 */

public class ProductDetailTitleLayoutView extends RelativeLayout implements View.OnClickListener {

    ProductionDetailFragmentReSetDelegate productionDetailFragmentReSetDelegate;
    /**
     * 全部的期（第一期，第二期，第三期，第四期...）
     */
    private ArrayList<String> allTimes;
    /**
     * 全部的期的ID（第一期ID，第二期ID，第三期ID，第四期ID...）
     */
    private ArrayList<String> allTimesID;
    /**
     * 全部的期的容器（第一期TextView，第二期TextView，第三期TextView，第四期TextView...）
     */
    private ArrayList<TextView> allTimesItem;
    /**
     * 当前是第几期 1----  0对应第几项
     */
    private Integer nowTime;

    private LinearLayout layout_time;
    private Integer space;
    HorizontalScrollView scrollView;
    public ProductDetailTitleLayoutView(Context context) {
        super(context);
        init();
    }

    private void init() {
        scrollView = new HorizontalScrollView(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        scrollView.setHorizontalScrollBarEnabled(false);
        scrollView.setLayoutParams(lp);

        layout_time = new LinearLayout(getContext());
        LayoutParams lp_item = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout_time.setLayoutParams(lp_item);
        layout_time.setOrientation(LinearLayout.HORIZONTAL);
        layout_time.setGravity(Gravity.CENTER_VERTICAL);
        scrollView.addView(layout_time);

        addView(scrollView);
    }

    public void setInfo(ArrayList<String> allTimes, ArrayList<String> allTimesID, Integer nowTime, ProductionDetailFragmentReSetDelegate productionDetailFragmentReSetDelegate) {
        this.productionDetailFragmentReSetDelegate = productionDetailFragmentReSetDelegate;
        if (allTimes != null) {
            this.allTimes = allTimes;
        } else {
            this.allTimes = new ArrayList<>();
        }
        if (allTimesID != null) {
            this.allTimesID = allTimesID;
        } else {
            this.allTimesID = new ArrayList<>();
        }
        if (nowTime != null) {
            this.nowTime = nowTime;
        } else {
            this.nowTime = 1;
        }

        allTimesItem = new ArrayList<>();
        initialized();
    }

    private void initialized() {
        space = Default.dip2px(5, getContext());
        Integer smallTextSize = Default.dip2px(8, getContext());
        if (layout_time.getChildCount() > 0) {
            layout_time.removeAllViews();
        }

        for (int i = 0; i < allTimes.size(); i++) {
            RelativeLayout clayout = new RelativeLayout(getContext());
            LayoutParams clp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT);
            clayout.setLayoutParams(clp);
            clayout.setGravity(Gravity.CENTER);
            clayout.setPadding(space * 3 , 0, space * 3 ,0 );

            TextView tv_time = new TextView(getContext());
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv_time.setText(allTimes.get(i));
            tv_time.setLayoutParams(lp);
            tv_time.setTag(i + 1);
            tv_time.setOnClickListener(this);
            allTimesItem.add(tv_time);
            tv_time.setPadding(space, space, space, space);
            tv_time.setTextSize(smallTextSize);

            clayout.addView(tv_time);
            layout_time.addView(clayout);
        }

        allTimesItem.get(nowTime - 1).callOnClick();

    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        scrollView.scrollTo(getSholdscrollToX(nowTime - 1),0);
    }

    //region Helper
    private void changeColor() {
        for (int i = 0; i < allTimesItem.size(); i++) {
            allTimesItem.get(i).setTextColor(ContextCompat.getColor(getContext(), R.color.tv_White));
            allTimesItem.get(i).setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bg_hasOK));
            if (i == nowTime - 1) {
                allTimesItem.get(i).setTextColor(ContextCompat.getColor(getContext(), R.color.tv_Red));
                allTimesItem.get(i).setBackground(ContextCompat.getDrawable(getContext(), R.drawable.border));
            }
        }
    }

    private Integer getSholdscrollToX (Integer index){
        Integer paddingWidth = space * 8;
        Integer textWidth = 0;
        for (int i = 0 ; i < index ; i++) {
            TextView tv = allTimesItem.get(i);
            Integer width = tv.getWidth();
            textWidth += width;
        }
        textWidth = paddingWidth * index;
        return textWidth;
    }

    @Override
    public void onClick(View v) {
        Default.showToast(((TextView)v).getText().toString(), Toast.LENGTH_SHORT);
        Integer tag = (Integer) v.getTag();
        nowTime = tag;
        changeColor();

        JSONObject sender = getServerInfo(allTimesID.get(nowTime - 1));

        if ( productionDetailFragmentReSetDelegate != null) {
            productionDetailFragmentReSetDelegate.refreshFragment(sender);
        }
    }
    //endregion

    //region Method
    public JSONObject getServerInfo (String timeID) {
        // TODO: 2017/3/23 访问服务器 获取数据
        JSONObject sender = new JSONObject();
        try {
            sender.put("","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sender;
    }
    //endregion
}
