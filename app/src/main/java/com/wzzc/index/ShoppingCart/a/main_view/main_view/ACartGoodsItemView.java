package com.wzzc.index.ShoppingCart.a.main_view.main_view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.index.ShoppingCart.a.ACartDelegate;
import com.wzzc.other_view.production.detail.gcb.DetailGcbActivity;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.MessageBox;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/6/17.
 */

public class ACartGoodsItemView extends BaseView implements View.OnClickListener {
    //region ```
    @ViewInject(R.id.check_goods)
    CheckBox check_goods;
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
    ACartDelegate acd;
    String rec_id;//购物车ID 删除操作
    String goods_id;//商品ID 查看操作
    String number = "1";
    int moq = 1;

    public ACartGoodsItemView(ACartDelegate acd, Context context) {
        super(context);
        this.acd = acd;
        init();
    }

    public ACartGoodsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        tv_for_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(DetailGcbActivity.GOODSID, goods_id);
                GetBaseActivity().AddActivity(DetailGcbActivity.class, 0, intent);
            }
        });
        img_delete.setOnClickListener(this);
        layout_reduce.setOnClickListener(this);
        layout_add.setOnClickListener(this);
        check_goods.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                acd.change(true, true, false, isChecked, rec_id, Integer.parseInt(editNumber.getText().toString()), tv_price.getText().toString(), tv_for_price.getText().toString());
            }
        });

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
                    if(!s.toString().equals(number)){
                        if (Integer.parseInt(s.toString()) < moq) {
                            editNumber.setText(number);
                            Default.showToast("不可小于" + moq + " !");
                        } else {
                            updateCart(s.toString());
                            int changeNumber = Integer.parseInt(s.toString()) - Integer.parseInt(number);
                            acd.change(check_goods.isChecked(), false, false, changeNumber > 0, rec_id, Math.abs(changeNumber), tv_price.getText().toString(), tv_for_price.getText().toString());
                            number = s.toString();
                        }
                    }
                }else {
                    editNumber.setText(number);
                }
            }
        });
    }

    /**
     * {
     * "rec_id": "13497",
     * "goods_id": "2259",
     * "goods_sn": "U03S",
     * "goods_name": "语音对讲多功能机器人",
     * "market_price": "￥4158.00",
     * "goods_price": "￥2599.00",
     * "goods_number": "1",
     * "goods_attr": [
     * {
     * "name": "颜色",
     * "value": "白色 "
     * }
     * ],
     * "is_real": "1",
     * "extension_code": "",
     * "parent_id": "0",
     * "rec_type": "0",
     * "is_gift": "0",
     * "is_shipping": "1",
     * "can_handsel": "0",
     * "goods_attr_id": "11218",
     * "package_attr_id": "",
     * "exc_integral": "0",
     * "pid": "2259",
     * "supplier_id": "0",
     * "seller": "子成100自营店",
     * "selected": 1,
     * "goods_price_original": "2599.00",
     * "market_price_original": "4158.00",
     * "subtotal": "￥2599.00",
     * "goods_thumb": "http://www.zcyb365.com/images/201611/thumb_img/2259_thumb_G_1479861424927.jpg"
     * }
     */
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
        eiv_production.setPath(sj(json_goods, "goods_thumb"));
        tv_price.setText(sj(json_goods, "goods_price_original"));
        tv_for_price.setText(sj(json_goods, "market_price_original"));
    }

    public void check(boolean isChecked) {
        check_goods.setChecked(isChecked);
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
                acd.change(check_goods.isChecked(), true, true, false, rec_id, Integer.parseInt(editNumber.getText().toString()), tv_price.getText().toString(), tv_for_price.getText().toString());
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
        AsynServer.BackObject(getContext(), "cart/update", false, para, new AsynServer.BackObject() {
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
