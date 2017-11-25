package com.wzzc.other_view.production.detail.gcb.main_view;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.TextLoadView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zcyb365 on 2016/10/18.
 */
public class DetailMiddleView extends BaseView {

    @ViewInject(R.id.detail_plus)
    private TextView detail_plus;
    @ViewInject(R.id.detail_reduce)
    private TextView detail_reduce;
    @ViewInject(R.id.detail_text)
    private EditText detail_text;
    @ViewInject(R.id.loadLayout_name)
    private TextLoadView loadLayout_name;
    @ViewInject(R.id.lab_money1)
    private TextView lab_money1;
    @ViewInject(R.id.lab_money2)
    private TextView lab_money2;
    @ViewInject(R.id.lab_money3)
    private TextView lab_money3;
    @ViewInject(R.id.lab_brand)
    private TextView lab_brand;
    @ViewInject(R.id.lab_integral)
    private TextView lab_integral;
    @ViewInject(R.id.goods_number)
    private TextView goods_number;
    @ViewInject(R.id.list_sku)
    private LinearLayout list_sku;
    Double str;
    ArrayList<DetailSKUView> detailSKUViews = new ArrayList<>();
    /**
     * 保证该View加载后调用方法
     */
    public boolean hasCheckedSelect = false;

    public DetailMiddleView(Context context) {
        super(context);
        init();
    }

    public DetailMiddleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        loadLayout_name.setTv_text_Gravity(Gravity.CENTER);
    }

    public void setInfo (JSONObject sender) {
        if (isInEditMode()) {
            return;
        }
        Calculation();
        try {
            if (sender != null) {
                loadLayout_name.loadOk(sender.getString("goods_name"), R.color.tv_production_name);
                lab_money1.setText(sender.getString("rank_price"));
                lab_money2.setText(sender.getString("market_price"));
                lab_money2.setPaintFlags(lab_money2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                lab_brand.setText(sender.getString("brand_name"));
                lab_integral.setText(sender.getString("integral"));
                goods_number.setText(sender.getString("goods_number"));

                str = Double.parseDouble(sender.getString("rank_price"));

                int num = Integer.valueOf(detail_text.getText().toString());
                lab_money3.setText(String.valueOf(str * num));
                JSONArray jsonarr = sender.getJSONArray("specification");
                for (int i = 0; i < jsonarr.length(); i++) {
                    DetailSKUView detailSKUView = new DetailSKUView(GetBaseActivity());
                    detailSKUView.setInfo(jsonarr.getJSONObject(i));
                    list_sku.addView(detailSKUView);
                    detailSKUViews.add(detailSKUView);
                }
            } else {
                loadLayout_name.noData("--", R.color.tv_production_name);

            }
            DetailMiddleView.this.hasCheckedSelect = true;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Calculation() {
        detail_plus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.valueOf(detail_text.getText().toString());
                if (num < Integer.valueOf(goods_number.getText().toString())) {
                    num++;
                } else {
                    num = Integer.valueOf(goods_number.getText().toString());
                }
                lab_money3.setText(String.valueOf(str * num));
                detail_text.setText(Integer.toString(num));

            }
        });
        detail_reduce.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.valueOf(detail_text.getText().toString());
                if (num > 1) {
                    if (num <= Integer.valueOf(goods_number.getText().toString())) {
                        num--;
                    } else {
                        num = Integer.valueOf(goods_number.getText().toString());
                    }
                }
                lab_money3.setText(String.valueOf(str * num));
                detail_text.setText(Integer.toString(num));
            }
        });
    }

    public int GetGoodsNumber() {
        return Integer.parseInt(detail_text.getText().toString());
    }

    public ArrayList<String> GetSpec() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < detailSKUViews.size(); i++) {
            String value = detailSKUViews.get(i).GetSpec();
            if (value == null) {
                return null;
            }
            list.add(value);
        }
        return list;
    }
}
