package com.wzzc.index.home.a.main_view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.index.home.a.GcbDelegate;
import com.wzzc.other_function.action.ItemClick;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.other_view.production.list.main_view.BrowseProductionView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/5/4.
 *
 */

public class TopGcbView extends BaseView implements View.OnClickListener{
    public GcbDelegate gd;
    //region 组件
    @ViewInject(R.id.et_browse)
    private EditText et_browse;
    //region ads
    @ViewInject(R.id.ads_1)
    ExtendImageView img_1;
    @ViewInject(R.id.ads_2_1)
    ExtendImageView img_2_1;
    @ViewInject(R.id.ads_2_2)
    ExtendImageView img_2_2;
    @ViewInject(R.id.ads_3_1)
    ExtendImageView img_3_1;
    @ViewInject(R.id.ads_3_2)
    ExtendImageView img_3_2;
    @ViewInject(R.id.ads_3_3)
    ExtendImageView img_3_3;
    @ViewInject(R.id.ads_3_4)
    ExtendImageView img_3_4;
    //endregion
    @ViewInject(R.id.tv_nomal)
    private TextView tv_nomal;
    @ViewInject(R.id.tv_new)
    private TextView tv_new;
    @ViewInject(R.id.tv_sales)
    private TextView tv_sales;
    //endregion
    String type;
    public TopGcbView(Context context) {
        super(context);
        init();
    }

    public TopGcbView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init () {
        Drawable d = ContextCompat.getDrawable(getContext(), R.drawable.search);
        d.setBounds(0, 0, d.getIntrinsicWidth() * 2, d.getMinimumHeight() * 2);
        et_browse.setText("");
        et_browse.setCompoundDrawablesWithIntrinsicBounds(null, null, d, null);
        et_browse.setCompoundDrawablePadding(3);
        et_browse.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (gd != null && et_browse.getRight() - event.getX() - et_browse.getPaddingRight() - 28 * 2/*margin right*/ <= et_browse.getCompoundDrawables()[2].getIntrinsicWidth()) {
                        gd.search(et_browse.getText().toString(),type);
                    }

                }
                return false;
            }
        });
        et_browse.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (gd!=null && actionId == EditorInfo.IME_ACTION_SEARCH) {
                    gd.search(et_browse.getText().toString(),type);
                }
                return false;
            }
        });
        tv_nomal.setOnClickListener(this);
        tv_new.setOnClickListener(this);
        tv_sales.setOnClickListener(this);
        img_1.setOnClickListener(click);
        img_2_1.setOnClickListener(click);
        img_2_2.setOnClickListener(click);
        img_3_1.setOnClickListener(click);
        img_3_2.setOnClickListener(click);
        img_3_3.setOnClickListener(click);
        img_3_4.setOnClickListener(click);

    }
    //region Action
    OnClickListener click = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String[] tags = (String[]) v.getTag();
            String da_link = tags[0];
            String data_type = tags[1];
            ItemClick.switchNormalListener(data_type, da_link);
        }
    };
    //endregion
    public void setInfo (JSONObject sender , String focusType) {
        this.type = focusType;
        tv_nomal.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
        tv_new.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
        tv_sales.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
        switch (type){
            case BrowseProductionView.SORT_BY_INTEGRATED:
                tv_nomal.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Red));
                break;
            case BrowseProductionView.SORT_BY_NEW_PRODUCTIONS:
                tv_new.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Red));
                break;
            case BrowseProductionView.SORT_BY_SALES:
                tv_sales.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Red));
                break;
            default:
        }
        try {
            JSONArray arr_ads_1 = sender.getJSONArray("ads1");
            JSONObject js_ads_1 = arr_ads_1.getJSONObject(0);
            String str_ads_1_code = js_ads_1.getString("ad_code");
            img_1.setPath(str_ads_1_code);
            JSONArray arr_ads_2 = sender.getJSONArray("ads2");
            JSONObject js_ads_2_1 = arr_ads_2.getJSONObject(0);
            JSONObject js_ads_2_2 = arr_ads_2.getJSONObject(1);
            String str_ads_2_1_code = js_ads_2_1.getString("ad_code");
            String str_ads_2_2_code = js_ads_2_2.getString("ad_code");
            img_2_1.setPath(str_ads_2_1_code);
            img_2_1.setScaleType(ImageView.ScaleType.FIT_XY);
            img_2_2.setPath(str_ads_2_2_code);
            img_2_2.setScaleType(ImageView.ScaleType.FIT_XY);

            JSONArray arr_ads_3 = sender.getJSONArray("ads3");
            JSONObject js_ads_3_1 = arr_ads_3.getJSONObject(0);
            JSONObject js_ads_3_2 = arr_ads_3.getJSONObject(1);
            JSONObject js_ads_3_3 = arr_ads_3.getJSONObject(2);
            JSONObject js_ads_3_4 = arr_ads_3.getJSONObject(3);
            String str_ads_3_1_code = js_ads_3_1.getString("ad_code");
            String str_ads_3_2_code = js_ads_3_2.getString("ad_code");
            String str_ads_3_3_code = js_ads_3_3.getString("ad_code");
            String str_ads_3_4_code = js_ads_3_4.getString("ad_code");
            img_3_1.setPath(str_ads_3_1_code);
            img_3_2.setPath(str_ads_3_2_code);
            img_3_3.setPath(str_ads_3_3_code);
            img_3_4.setPath(str_ads_3_4_code);
            img_1.setTag(new String[]{js_ads_1.getString("ad_link"),js_ads_1.getString("data_type")});
            img_2_1.setTag(new String[]{js_ads_2_1.getString("ad_link"),js_ads_2_1.getString("data_type")});
            img_2_2.setTag(new String[]{js_ads_2_2.getString("ad_link"),js_ads_2_2.getString("data_type")});
            img_3_1.setTag(new String[]{js_ads_3_1.getString("ad_link"),js_ads_3_1.getString("data_type")});
            img_3_2.setTag(new String[]{js_ads_3_2.getString("ad_link"),js_ads_3_2.getString("data_type")});
            img_3_3.setTag(new String[]{js_ads_3_3.getString("ad_link"),js_ads_3_3.getString("data_type")});
            img_3_4.setTag(new String[]{js_ads_3_4.getString("ad_link"),js_ads_3_4.getString("data_type")});
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        tv_nomal.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
        tv_new.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
        tv_sales.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
        switch (v.getId()) {
            case R.id.tv_nomal:
                type = BrowseProductionView.SORT_BY_INTEGRATED;
                tv_nomal.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Red));
                break;
            case R.id.tv_new:
                type = BrowseProductionView.SORT_BY_NEW_PRODUCTIONS;
                tv_new.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Red));
                break;
            case R.id.tv_sales:
                type = BrowseProductionView.SORT_BY_SALES;
                tv_sales.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Red));
                break;
            default:
        }
        if (gd!=null) {
            gd.search(et_browse.getText().toString(),type);
        }
    }
}
