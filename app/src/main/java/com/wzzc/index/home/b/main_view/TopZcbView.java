package com.wzzc.index.home.b.main_view;

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
import com.wzzc.other_function.action.ItemClick;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.index.home.b.ZcbDelegate;
import com.wzzc.other_view.production.list.main_view.BrowseProductionView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/5/5.
 *
 */

public class TopZcbView extends BaseView implements View.OnClickListener{
    public ZcbDelegate zd;
    //region 组件
    @ViewInject(R.id.et_browse)
    private EditText et_browse;
    @ViewInject(R.id.ads_11)
    ExtendImageView img_11;
    @ViewInject(R.id.ads_11_1)
    ExtendImageView img_11_1;
    @ViewInject(R.id.ads_11_2)
    ExtendImageView img_11_2;
    @ViewInject(R.id.ads_11_3)
    ExtendImageView img_11_3;
    @ViewInject(R.id.ads_11_4)
    ExtendImageView img_11_4;
    @ViewInject(R.id.ads_21_1)
    ExtendImageView img_21_1;
    @ViewInject(R.id.ads_21_2)
    ExtendImageView img_21_2;
    @ViewInject(R.id.ads_21_3)
    ExtendImageView img_21_3;
    @ViewInject(R.id.ads_21_4)
    ExtendImageView img_21_4;
    @ViewInject(R.id.tv_nomal)
    private TextView tv_nomal;
    @ViewInject(R.id.tv_new)
    private TextView tv_new;
    @ViewInject(R.id.tv_sales)
    private TextView tv_sales;
    //endregion
    String type;
    public TopZcbView(Context context) {
        super(context);
        init();
    }

    public TopZcbView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init () {
        img_11.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img_11_1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img_11_2.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img_11_3.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img_11_4.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img_21_1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img_21_2.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img_21_3.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img_21_4.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Drawable d = ContextCompat.getDrawable(getContext(), R.drawable.search);
        d.setBounds(0, 0, d.getIntrinsicWidth() * 2, d.getMinimumHeight() * 2);
        et_browse.setText("");
        et_browse.setCompoundDrawablesWithIntrinsicBounds(null, null, d, null);
        et_browse.setCompoundDrawablePadding(3);
        et_browse.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (zd != null && et_browse.getRight() - event.getX() - et_browse.getPaddingRight() - 28 * 2/*margin right*/ <= et_browse.getCompoundDrawables()[2].getIntrinsicWidth()) {
                        zd.search(et_browse.getText().toString(),type);
                    }

                }
                return false;
            }
        });
        et_browse.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (zd!=null && actionId == EditorInfo.IME_ACTION_SEARCH) {
                    zd.search(et_browse.getText().toString(),type);
                }
                return false;
            }
        });
        tv_nomal.setOnClickListener(this);
        tv_new.setOnClickListener(this);
        tv_sales.setOnClickListener(this);
        img_11.setOnClickListener(click);
        img_11_1.setOnClickListener(click);
        img_11_2.setOnClickListener(click);
        img_11_3.setOnClickListener(click);
        img_11_4.setOnClickListener(click);
        img_21_1.setOnClickListener(click);
        img_21_2.setOnClickListener(click);
        img_21_3.setOnClickListener(click);
        img_21_4.setOnClickListener(click);

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
            img_11.setPath(sender.getJSONArray("ads1").getJSONObject(0).getString("ad_code"));
            JSONArray arr_ads_2 = sender.getJSONArray("ads2");
            img_11_1.setPath(arr_ads_2.getJSONObject(0).getString("ad_code"));
            img_11_2.setPath(arr_ads_2.getJSONObject(1).getString("ad_code"));
            img_11_3.setPath(arr_ads_2.getJSONObject(2).getString("ad_code"));
            img_11_4.setPath(arr_ads_2.getJSONObject(3).getString("ad_code"));
            img_21_1.setPath(arr_ads_2.getJSONObject(4).getString("ad_code"));
            img_21_2.setPath(arr_ads_2.getJSONObject(5).getString("ad_code"));
            img_21_3.setPath(arr_ads_2.getJSONObject(6).getString("ad_code"));
            img_21_4.setPath(arr_ads_2.getJSONObject(7).getString("ad_code"));
            img_11.setTag(new String[]{sender.getJSONArray("ads1").getJSONObject(0).getString("ad_link"),sender.getJSONArray("ads1").getJSONObject(0).getString("data_type")});
            img_11_1.setTag(new String[]{arr_ads_2.getJSONObject(0).getString("ad_link"),arr_ads_2.getJSONObject(0).getString("data_type")});
            img_11_2.setTag(new String[]{arr_ads_2.getJSONObject(1).getString("ad_link"),arr_ads_2.getJSONObject(1).getString("data_type")});
            img_11_3.setTag(new String[]{arr_ads_2.getJSONObject(2).getString("ad_link"),arr_ads_2.getJSONObject(2).getString("data_type")});
            img_11_4.setTag(new String[]{arr_ads_2.getJSONObject(3).getString("ad_link"),arr_ads_2.getJSONObject(3).getString("data_type")});
            img_21_1.setTag(new String[]{arr_ads_2.getJSONObject(4).getString("ad_link"),arr_ads_2.getJSONObject(4).getString("data_type")});
            img_21_2.setTag(new String[]{arr_ads_2.getJSONObject(5).getString("ad_link"),arr_ads_2.getJSONObject(5).getString("data_type")});
            img_21_3.setTag(new String[]{arr_ads_2.getJSONObject(6).getString("ad_link"),arr_ads_2.getJSONObject(6).getString("data_type")});
            img_21_4.setTag(new String[]{arr_ads_2.getJSONObject(7).getString("ad_link"),arr_ads_2.getJSONObject(7).getString("data_type")});
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
        if (zd!=null) {
            zd.search(et_browse.getText().toString(),type);
        }
    }
}
