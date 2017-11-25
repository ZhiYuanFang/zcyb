package com.wzzc.index.ShoppingCart.b.main_view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.index.ShoppingCart.b.BCartDelegate;
import com.wzzc.other_view.production.detail.zcb.DetailZcbActivity;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.MessageBox;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/6/19.
 */

public class BCartGoodsItemView extends BaseView implements View.OnClickListener{
    //region ```
    @ViewInject(R.id.tv_none)
    TextView tv_none;
    @ViewInject(R.id.eiv_production)
    ExtendImageView eiv_production;
    @ViewInject(R.id.tv_allGone)
    TextView tv_allGone;
    @ViewInject(R.id.tv_production_name)
    TextView tv_production_name;
    @ViewInject(R.id.layout_category)
    RelativeLayout layout_category;
    @ViewInject(R.id.tv_category)
    TextView tv_category;
    @ViewInject(R.id.layout_reduce)
    RelativeLayout layout_reduce;
    @ViewInject(R.id.layout_add)
    RelativeLayout layout_add;
    @ViewInject(R.id.editNumber)
    EditText editNumber;
    @ViewInject(R.id.img_delete)
    ImageView img_delete;
    @ViewInject(R.id.tv_coupons)
    TextView tv_price;
    @ViewInject(R.id.tv_for_price)
    TextView tv_for_price;
    //endregion.
    BCartDelegate bcd;
    String rec_id;//购物车ID 删除操作
    String goods_id;//商品ID 查看操作
    int moq = 1;
    String number = "1";
    public BCartGoodsItemView(BCartDelegate bcd , Context context) {
        super(context);
        this.bcd = bcd;
        init();
    }

    public BCartGoodsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        tv_for_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(DetailZcbActivity.GOODSID, goods_id);
                GetBaseActivity().AddActivity(DetailZcbActivity.class, 0, intent);
            }
        });
        img_delete.setOnClickListener(this);
        layout_reduce.setOnClickListener(this);
        layout_add.setOnClickListener(this);

        editNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    if (!s.toString().equals(number)) {
                        if (Integer.parseInt(s.toString()) < moq) {
                            editNumber.setText(String.valueOf(number));
                            Default.showToast("不可小于" + moq + " !");
                        } else {
                            updateCart(s.toString());
                            int changeNumber = Integer.parseInt(s.toString()) - Integer.parseInt(number);
                            bcd.change(rec_id,changeNumber,Integer.parseInt(tv_price.getText().toString()));
                            number = s.toString();
                        }
                    }
                } else {
                    editNumber.setText(number);
                }
            }
        });
    }

    public void setInfo(JSONObject json_goods) {
        if (json_goods.has("moq")) {
            try {
                moq = json_goods.getInt("moq");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        goods_id = sj(json_goods, "goods_id");
        rec_id = sj(json_goods, "rec_id");
        tv_production_name.setText(sj(json_goods, "goods_name"));
        //region category
        try {
            Object attr = json_goods.get("goods_attr");
            if (attr instanceof JSONArray) {
                JSONArray jrr_attr = (JSONArray) attr;
                if (jrr_attr.length() > 0) {
                    StringBuilder sder = new StringBuilder();
                    for (int i = 0; i < jrr_attr.length(); i++) {
                        String str_attr = sj(jrr_attr.getJSONObject(i), "value") + " ";
                        sder.append(str_attr);
                    }
                    tv_category.setText(sder);
                } else {
                    layout_category.setVisibility(GONE);
                }
            } else{
                layout_category.setVisibility(GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            layout_category.setVisibility(GONE);
        }
        //endregion
        //region number
        number = sj(json_goods,"goods_number");
        try {
            editNumber.setText(json_goods.getString("goods_number"));
        } catch (JSONException e) {
            e.printStackTrace();
            editNumber.setText(String.valueOf(moq));
        }
        number = editNumber.getText().toString();
        //endregion
        try {
            JSONObject json_img = json_goods.getJSONObject("img");
            eiv_production.setPath(sj(json_img, "thumb"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tv_price.setText(sj(json_goods, "exc_integral"));
        tv_for_price.setText(sj(json_goods, "market_price"));
    }

    public String getRec_id() {
        return rec_id;
    }

    private String sj(JSONObject json, String key) {
        try {
            return json.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return "error";
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_delete: {
                int num = Integer.parseInt(editNumber.getText().toString());
                int money = Integer.parseInt(tv_price.getText().toString());
                bcd.change(rec_id,-1*num,money);
                bcd.delete(rec_id);
                break;
            }
            case R.id.layout_reduce: {
                editNumber.setText(String.valueOf(Integer.parseInt(editNumber.getText().toString()) - moq));
                break;
            }
            case R.id.layout_add: {
                editNumber.setText(String.valueOf(Integer.parseInt(editNumber.getText().toString()) + moq));
                break;
            }
            default:
        }
    }

    public void updateCart(Object new_number) {
        JSONObject para = new JSONObject();
        try {
            para.put("rec_id", rec_id);
            para.put("new_number", new_number);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(getContext(), "excart/update", false, para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                try {
                    JSONObject json_status = sender.getJSONObject("status");
                    int succeed = json_status.getInt("succeed");
                    if (succeed == 0) {
                        MessageBox.Show(json_status.getString("error_desc"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
