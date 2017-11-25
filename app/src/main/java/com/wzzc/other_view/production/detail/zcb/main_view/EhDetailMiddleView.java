package com.wzzc.other_view.production.detail.zcb.main_view;

import android.content.Context;
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
 * Created by zcyb365 on 2016/11/10.
 * <p>
 * 子成产品介绍
 */
public class EhDetailMiddleView extends BaseView {
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
    @ViewInject(R.id.lab_brand)
    private TextView lab_brand;
    @ViewInject(R.id.lab_number)
    private TextView lab_number;
    @ViewInject(R.id.tv_zc_discount)
    TextView tv_zc_discount;
    @ViewInject(R.id.list_sku)
    private LinearLayout list_sku;
    int smoll;
    ArrayList<EhDetailSKUView> detailSKUViews = new ArrayList<>();
    /**
     * 保证该View加载后调用方法
     */
    public boolean hasCheckedSelect = false;

    public EhDetailMiddleView(Context context) {
        super(context);
        init();
    }

    public EhDetailMiddleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        loadLayout_name.setTv_text_Gravity(Gravity.CENTER);
    }

    public void setInfo(JSONObject sender) {
        if (isInEditMode()) {
            return;
        }
        Calculation();
        try {
            if (sender != null) {
                String zc_discount = sender.getString("zc_discount_text");
                if (zc_discount.length() > 0) {
                    tv_zc_discount.setVisibility(VISIBLE);
                    tv_zc_discount.setText(zc_discount);
                } else {
                    tv_zc_discount.setVisibility(GONE);
                }
                loadLayout_name.loadOk(sender.getString("goods_name"), R.color.tv_production_name);
                lab_money1.setText(sender.getString("exchange_integral"));
                lab_brand.setText(sender.getString("brand_name"));
                smoll = sender.getInt("moq");
                detail_text.setText(Integer.toString(smoll));
                lab_number.setText("此商品最小起订量为" + smoll);
                JSONArray jsonarr = sender.getJSONArray("specification");
                for (int i = 0; i < jsonarr.length(); i++) {
                    EhDetailSKUView detailSKUView = new EhDetailSKUView(GetBaseActivity());
                    detailSKUView.setInfo(jsonarr.getJSONObject(i));
                    list_sku.addView(detailSKUView);
                    detailSKUViews.add(detailSKUView);
                }
            } else {
                loadLayout_name.noData("--", R.color.tv_production_name);
            }
            EhDetailMiddleView.this.hasCheckedSelect = true;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Calculation() {

        detail_plus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.valueOf(detail_text.getText().toString());
                num += smoll;
                detail_text.setText(Integer.toString(num));

            }
        });
        detail_reduce.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.valueOf(detail_text.getText().toString());
                if (num > smoll) {
                    num -= smoll;
                }
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
