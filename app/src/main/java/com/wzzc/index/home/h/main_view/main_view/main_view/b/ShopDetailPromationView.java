package com.wzzc.index.home.h.main_view.main_view.main_view.b;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.production.detail.gcb.DetailGcbActivity;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by toutou on 2017/3/11.
 *
 * 商家推荐
 */

public class ShopDetailPromationView extends BaseView  {
    @ViewInject(R.id.layout_info)
    LinearLayout layout_info;

    public ShopDetailPromationView(Context context) {
        super(context);
    }

    public ShopDetailPromationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setInfo (final JSONArray jrr) {
        int number = 4;
        for (int i = 0; i < jrr.length(); i += number) {
            LinearLayout layout = new LinearLayout(getContext());
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setWeightSum(number);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Default.dip2px(106, getContext()));
            layoutParams.setMargins(0,Default.dip2px(13,getContext()),0,0);
            layout.setLayoutParams(layoutParams);
            for (int j = 0; j < number; j++) {
                final int index = i + j;
                LinearLayout linearLayout = new LinearLayout(getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.setMarginEnd(Default.dip2px(4,getContext()));
                params.weight = 1;
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setLayoutParams(params);
                linearLayout.setWeightSum(2);
                ExtendImageView eiv = new ExtendImageView(getContext());
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params1.weight = 0.6f;
                eiv.setLayoutParams(params1);
                if (jrr.length() > index) {
                    try {
                        eiv.radio = GetBaseActivity().getResources().getDimension(R.dimen.RoundRadio);;
                        eiv.setPath(jrr.getJSONObject(index).getString("thumb"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                linearLayout.addView(eiv);
                TextView textView = new TextView(getContext());
                textView.setTextSize(Default.dip2px(6,getContext()));
                textView.setSingleLine(true);
                if (jrr.length() > index) {
                    try {
                        textView.setText(jrr.getJSONObject(index).getString("name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params2.weight = 1.4f;
                textView.setLayoutParams(params2);
                linearLayout.addView(textView);
                final ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f,1.1f,1.0f, 1.1f,
                        Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

                scaleAnimation.setDuration(300);
                linearLayout.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        System.out.println("event.getAction : " + event.getAction());
                        switch (event.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                scaleAnimation.setFillAfter(true);
                                v.startAnimation(scaleAnimation);
                                break;
                            case MotionEvent.ACTION_CANCEL:
                                scaleAnimation.setFillAfter(false);
                                break;
                            case MotionEvent.ACTION_UP:
                                scaleAnimation.setFillAfter(false);
                                break;
                            default:
                        }
                        return false;
                    }
                });

                linearLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent();
                            int id = Integer.valueOf(jrr.getJSONObject(index).getString("id"));
                            intent.putExtra(DetailGcbActivity.GOODSID,id);
                            GetBaseActivity().AddActivity(DetailGcbActivity.class, 0, intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                layout.addView(linearLayout);
            }
            layout_info.removeAllViews();
            layout_info.addView(layout);
        }
    }
}
