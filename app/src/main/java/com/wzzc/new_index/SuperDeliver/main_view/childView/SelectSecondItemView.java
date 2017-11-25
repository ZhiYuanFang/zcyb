package com.wzzc.new_index.SuperDeliver.main_view.childView;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_view.production.detail.main_view.FlowLayout;
import com.wzzc.other_function.MessageBox;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by toutou on 2017/6/2.
 */

public class SelectSecondItemView extends BaseView {
    public SelectSecondDelegate ssd;
    @ViewInject(R.id.flowLayout)
    FlowLayout flowLayout;

    ArrayList<TextView> arrItem;

    public SelectSecondItemView(Context context) {
        super(context);
        init();
    }

    public SelectSecondItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
    }

    public int ShowCount = 11;
    public static final int ItemHeight = Default.dip2px(22, Default.getActivity());
    public static final int ItemSpace = Default.dip2px(6, Default.getActivity());
    public static final int TwoItemSpace = Default.dip2px(2, Default.getActivity());

    public void setInfo(final JSONArray jrr, final String cat_id) {
        ShowCount = 11;
        arrItem = new ArrayList<>();
        flowLayout.removeAllViews();
        if (ShowCount > jrr.length()) {
            ShowCount = jrr.length();
        }
        for (int i = 0; i < ShowCount; i++) {
            TextView tv = new TextView(getContext());
            try {
                JSONObject json = jrr.getJSONObject(i);
                tv.setText(json.getString("cat_name"));
                tv.setTag(json.getString("cat_id"));
                if (tv.getTag().equals(cat_id)) {
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.tv_Red));
                    tv.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_circle_red));
                } else {
                    tv.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.border));
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.tv_Gray));
                }
                tv.setPadding(ItemSpace, 0, ItemSpace, 0);
                tv.setGravity(Gravity.CENTER);
                tv.setOnClickListener(itemClick);
            } catch (JSONException e) {
                e.printStackTrace();
                MessageBox.Show(e.getMessage());
            }
            tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ItemHeight));
            RelativeLayout rv = new RelativeLayout(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rv.setPadding(TwoItemSpace, TwoItemSpace , TwoItemSpace, TwoItemSpace);
            rv.setLayoutParams(lp);
            rv.addView(tv);
            flowLayout.addView(rv);
            arrItem.add(tv);
        }

        if (jrr.length() > ShowCount) {
            TextView tv = new TextView(getContext());
            tv.setText("...");
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(ItemSpace, 0, ItemSpace, 0);
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setVisibility(GONE);
                    for (int i = ShowCount; i < jrr.length(); i++) {
                        TextView tv = new TextView(getContext());

                        JSONObject json = JsonClass.jjrr(jrr,i);
                        tv.setText(JsonClass.sj(json,"cat_name"));
                        tv.setTag(JsonClass.sj(json,"cat_id"));
                        if (tv.getTag().equals(cat_id)) {
                            tv.setTextColor(ContextCompat.getColor(getContext(), R.color.tv_Red));
                            tv.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_circle_red));
                        } else {
                            tv.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.border));
                            tv.setTextColor(ContextCompat.getColor(getContext(), R.color.tv_Gray));
                        }
                        tv.setPadding(ItemSpace, 0, ItemSpace, 0);
                        tv.setGravity(Gravity.CENTER);
                        tv.setOnClickListener(itemClick);

                        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ItemHeight));
                        RelativeLayout rv = new RelativeLayout(getContext());
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        rv.setPadding(TwoItemSpace, TwoItemSpace , TwoItemSpace, TwoItemSpace );
                        rv.setLayoutParams(lp);
                        rv.addView(tv);
                        flowLayout.addView(rv);
                        arrItem.add(tv);
                    }
                }
            });
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ItemHeight);
            lp.setMargins(TwoItemSpace, TwoItemSpace, TwoItemSpace, TwoItemSpace);
            tv.setLayoutParams(lp);
            flowLayout.addView(tv);
        }
    }

    protected OnClickListener itemClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String cat_id = (String) v.getTag();
            if (ssd != null) {
                ssd.secondClick(cat_id, ((TextView) (v)).getText().toString());
            }
            for (int i = 0; i < arrItem.size(); i++) {
                arrItem.get(i).setTextColor(ContextCompat.getColor(getContext(), R.color.tv_Gray));
                arrItem.get(i).setBackground(ContextCompat.getDrawable(getContext(), R.drawable.border));
                if (arrItem.get(i).getTag().equals(cat_id)) {
                    arrItem.get(i).setTextColor(ContextCompat.getColor(getContext(), R.color.tv_Red));
                    arrItem.get(i).setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_circle_red));
                }
            }
        }
    };

}
