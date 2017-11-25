package com.wzzc.other_view.production.list.main_view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wzzc.base.annotation.ContentView;
import com.wzzc.index.ShoppingCart.ShoppingCartActivity;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.other_function.MessageBox;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_function.action.ItemClick;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.other_view.production.detail.gcb.main_view.DetailSKUView;
import com.wzzc.other_view.TextLoadView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by TOUTOU on 2017/3/7.
 *
 */
@ContentView(R.layout.view_browse_production)
public class Browse_ProductionView extends BaseView {

    //region 组件
    @ViewInject(R.id.layout_contain)
    private LinearLayout layout_contain;
    @ViewInject(R.id.elv_browse_production_icon)
    private ExtendImageView elv_browse_production_icon;
    @ViewInject(R.id.tv_browse_production_name)
    private TextView tv_browse_production_name;
    @ViewInject(R.id.tv_nowPrice)
    private TextView tv_nowPrice;
    @ViewInject(R.id.tv_forPrice)
    private TextView tv_forPrice;
    @ViewInject(R.id.ibn_add_cart)
    private ImageButton ibn_add_cart;
    //endregion
    private String goods_id;
    private boolean zcb;
    private int moq = 1;

    public Browse_ProductionView(Context context) {
        super(context);
        init();
        tv_forPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public Browse_ProductionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        tv_forPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    private void init() {
        elv_browse_production_icon.setScaleType(ImageView.ScaleType.CENTER_CROP);

        ibn_add_cart.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // TODO: 2017/3/7 change img
                        break;
                    case MotionEvent.ACTION_UP:
                        // TODO: 2017/3/7 return img
                        break;
                }
                return false;
            }
        });

        ibn_add_cart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Default.isConnect(getContext())) {
                    showSpecificDialog();
                } else {
                    Default.showToast("noNet ! " , Toast.LENGTH_LONG);
                }
            }
        });
    }

    public void setInfo(JSONObject sender, boolean zcb) {
        this.zcb = zcb;
        if (sender != null) {
            try {
                if (sender.has("moq")) {
                    Object obj_moq = sender.get("moq");
                    moq = obj_moq instanceof Integer ? Integer.valueOf(obj_moq.toString()) : 1;
                }

                if (sender.has("goods_id")) {
                    Object obj_goods_id = sender.get("goods_id");
                    goods_id = obj_goods_id instanceof String ? String.valueOf(obj_goods_id) : null;
                    String type = zcb ? ItemClick.EXCHANGE_GOODS : ItemClick.GOODS;
                    elv_browse_production_icon.setOnClickListener(ItemClick.listener(null,type,goods_id,null));
                }

                if (sender.has("name")) {
                    Object name = sender.get("name");
                    String productionName = name instanceof String ? String.valueOf(name) : getContext().getString(R.string.cannot_getName);
                    tv_browse_production_name.setText(productionName);
                } else {
                    tv_browse_production_name.setText(getContext().getString(R.string.cannot_getName));
                }

                if (zcb) {
                    if (tv_nowPrice.getBackground() == null) {
                        tv_nowPrice.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_collection));
                    }
                    if (sender.has("exchange_integral")) {
                        Object price = sender.get("exchange_integral");
                        String zcb_price = price instanceof String ? String.valueOf(price) : null;
                        if (zcb_price != null) {
                            tv_nowPrice.setText(getContext().getString(R.string.zcb) + ":" + zcb_price);
                        } else {
                            tv_nowPrice.setText(getContext().getString(R.string.cannot_buy));
                        }
                    } else {
                        tv_nowPrice.setText(getContext().getString(R.string.cannot_buy));

                    }
                } else {
                    if (sender.has("shop_price")) {
                        Object price = sender.get("shop_price");
                        String gcb_shop_price = price instanceof String ? String.valueOf(price) : getContext().getString(R.string.cannot_buy);
                        tv_nowPrice.setText(gcb_shop_price);
                    } else {
                        tv_nowPrice.setText(getContext().getString(R.string.cannot_buy));

                    }

                    if (sender.has("market_price")) {
                        Object price = sender.get("market_price");
                        String gcb_market_price = price instanceof String ? String.valueOf(price) : getContext().getString(R.string.cannot_buy);
                        tv_forPrice.setText(gcb_market_price);
                    } else {
                        tv_forPrice.setText(getContext().getString(R.string.cannot_buy));

                    }

                }

                if (sender.has("img")) {
                    Object imgs = sender.get("img");
                    JSONObject img_json = imgs instanceof JSONObject ? new JSONObject(String.valueOf(imgs)) : null;
                    if (img_json != null && img_json.has("url")) {
                        Object img_url = img_json.get("url");
                        String url = img_url instanceof String ? String.valueOf(img_url) : null;
                        elv_browse_production_icon.setPath(url);
                    } else {
                        elv_browse_production_icon.setPath("");
                    }
                } else {
                    elv_browse_production_icon.setPath("");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    //region Method
    public void setBackGroundColor (@ColorInt int color) {
        layout_contain.setBackgroundColor(color);
    }

    public void setProductionNameColor (@ColorInt int color) {
        tv_browse_production_name.setTextColor(color);
    }

    public void setTv_nowPriceColor (@ColorInt int color) {
        tv_nowPrice.setTextColor(color);
    }

    public void setTv_nowPriceBackground (Drawable drawable) {
        tv_nowPrice.setBackground(drawable);
    }
    //endregion

    //region Dialog
    private ArrayList<String> getSpec(ArrayList<DetailSKUView> detailSKUViews) {
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

    private void showSpecificDialog () {
        final ArrayList<DetailSKUView> detailSKUViews = new ArrayList<>();
        final LinearLayout layout = new LinearLayout(getContext());
        layout.setPadding(Default.dip2px(5f, getContext()), Default.dip2px(10f, getContext()), Default.dip2px(5f, getContext()), Default.dip2px(10f, getContext()));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(params);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        //region add progress
        final TextLoadView textLoadView = new TextLoadView(getContext());
        ViewGroup.LayoutParams lp_load = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textLoadView.setLayoutParams(lp_load);
        layout.addView(textLoadView);
        //endregion
        final EditText et_num = new EditText(getContext());
        //region ADD select
        if (goods_id != null) {
            try {
                JSONObject para = new JSONObject();
                para.put("goods_id", goods_id);
                AsynServer.BackObject(getContext(), "goods",false,para, new AsynServer.BackObject() {
                    @Override
                    public void Back(JSONObject sender) {
                        try {
                            Object obj_status = sender.get("status");
                            JSONObject json_status = obj_status instanceof JSONObject ? new JSONObject(String.valueOf(obj_status)) : new JSONObject() {{
                                put("succeed", 0);
                            }};
                            if (json_status.has("succeed")) {
                                layout.removeView(textLoadView);
                                Object obj_succeed = json_status.get("succeed");
                                Integer succeed = obj_succeed instanceof Integer ? Integer.parseInt(String.valueOf(obj_succeed)) : 0;
                                if (succeed == 1) {
                                    if (sender.has("data")) {
                                        Object obj_data = sender.get("data");
                                        JSONObject data = obj_data instanceof JSONObject ? new JSONObject(String.valueOf(obj_data)) : new JSONObject() {{
                                            put("specification", new JSONArray());
                                        }};
                                        if (data.has("specification")) {
                                            Object obj_specification = data.get("specification");
                                            JSONArray jrr_specific = obj_specification instanceof JSONArray ? new JSONArray(String.valueOf(obj_specification)) : new JSONArray();
                                            for (int i = 0; i < jrr_specific.length(); i++) {
                                                DetailSKUView dsv = new DetailSKUView(getContext());
                                                dsv.setInfo(jrr_specific.get(i) instanceof JSONObject ? jrr_specific.getJSONObject(i) : new JSONObject());
                                                layout.addView(dsv);
                                                detailSKUViews.add(dsv);
                                            }

                                            //region ADD NUM
                                            int space = 10;
                                            LinearLayout lv_num = new LinearLayout(getContext());
                                            lv_num.setOrientation(LinearLayout.HORIZONTAL);
                                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                            lp.setMargins(0,space,0,space);
                                            lv_num.setLayoutParams(lp);
                                            lv_num.setGravity(Gravity.CENTER_VERTICAL);
                                            TextView tv_num = new TextView(getContext());
                                            LinearLayout.LayoutParams lp_num = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                            tv_num.setLayoutParams(lp_num);
                                            tv_num.setText("数量：");
                                            tv_num.setGravity(Gravity.CENTER);
                                            lv_num.addView(tv_num);
                                            LinearLayout lv_select = new LinearLayout(getContext());
                                            LinearLayout.LayoutParams lp_select = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                            lv_select.setLayoutParams(lp_select);
                                            lv_select.setGravity(Gravity.CENTER_VERTICAL);
//                                            lv_select.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_collection));
                                            lv_select.setOrientation(LinearLayout.HORIZONTAL);
                                            TextView bt_release = new TextView(getContext());
                                            LinearLayout.LayoutParams lp_bt = new LinearLayout.LayoutParams(Default.dip2px(30,getContext()), ViewGroup.LayoutParams.MATCH_PARENT);

                                            bt_release.setLayoutParams(lp_bt);
                                            bt_release.setText("-");
                                            bt_release.setGravity(Gravity.CENTER);
                                            bt_release.setTextSize(18);
                                            bt_release.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.bg_collection));
//                                            bt_release.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.angel_border_selected));
                                            lv_select.addView(bt_release);
                                            LinearLayout.LayoutParams lp_et = new LinearLayout.LayoutParams(Default.dip2px(150,getContext()), ViewGroup.LayoutParams.MATCH_PARENT);
                                            et_num.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_collection));
                                            et_num.setLayoutParams(lp_et);
                                            et_num.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Black));
                                            et_num.setGravity(Gravity.CENTER);
                                            et_num.setText(String.valueOf(moq));
                                            et_num.setMaxLines(1);
                                            et_num.setTextSize(18);
                                            lv_select.addView(et_num);
                                            TextView bt_add = new TextView(getContext());
                                            bt_add.setGravity(Gravity.CENTER);
                                            bt_add.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.bg_collection));
                                            bt_add.setLayoutParams(lp_bt);
                                            bt_add.setText("+");
                                            bt_add.setTextSize(18);
                                            lv_select.addView(bt_add);

                                            lv_num.addView(lv_select);

                                            layout.addView(lv_num);

                                            bt_add.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Integer num = Integer.parseInt(String.valueOf(et_num.getText()));
                                                    num += moq;
                                                    et_num.setText(String.valueOf(num));
                                                }
                                            });

                                            bt_release.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Integer num = Integer.parseInt(String.valueOf(et_num.getText()));
                                                    if (num > moq) {
                                                        num -= moq;
                                                        et_num.setText(String.valueOf(num));
                                                    }
                                                }
                                            });
                                            //endregion
                                        }
                                    }
                                }
                            } else {
                                textLoadView.loadOk(json_status.getString("error_desc"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //endregion
        MessageBox.ShowFlexibleDialog(tv_browse_production_name.getText().toString(),layout,new String[]{"确定"}, new MessageBox.MessBtnBack() {
            @Override
            public void Back(int index) {
                //region Action
                try {
                    JSONObject para = new JSONObject();
                    para.put("goods_id", goods_id);
                    para.put("number", et_num.getText().toString());
                    ArrayList<String> list = getSpec(detailSKUViews);
                    if (list == null) {

                        MessageBox.Show("规格没有选择全");
                        return;
                    }

                    StringBuilder strspec = new StringBuilder();
                    for (int i = 0; i < list.size(); i++) {
                        strspec.append(list.get(i));
                        strspec.append(",");
                    }
                    para.put("spec", strspec.substring(0, strspec.length()));

                    String url = zcb ? "excart/create" : "cart/create";
                    AsynServer.BackObject(getContext(), url, para, new AsynServer.BackObject() {
                        @Override
                        public void Back(JSONObject sender) {
                            try {
                                JSONObject json_status = sender.getJSONObject("status");
                                int succeed = json_status.getInt("succeed");
                                String production_name = tv_browse_production_name.getText().toString();
                                switch (succeed) {
                                    case 0:
                                        MessageBox.Show(json_status.getString("error_desc"));
                                        break;
                                    case 1:
                                        // region ```
                                        String message = zcb ? "到兑换车" : "到购物车";
//                                                                                Default.showToast(production_name + "\n" + message, Toast.LENGTH_LONG);
                                        MessageBox.Show(getContext(),Default.AppName, "添加成功 : " + production_name + "。", new String[]{"再逛会", "去结算"}, new MessageBox.MessBtnBack() {
                                            @Override
                                            public void Back(int index) {
                                                switch (index) {
                                                    case 0:
                                                        break;
                                                    case 1:
                                                        int cart_type = zcb ? ShoppingCartActivity.EXCHAGE_CART : ShoppingCartActivity.SHOP_CART;
                                                        Intent intent = new Intent();
                                                        intent.putExtra(ShoppingCartActivity.TYPE, cart_type);
                                                        GetBaseActivity().AddActivity(ShoppingCartActivity.class,0,intent);
                                                        break;
                                                    default:
                                                }
                                            }
                                        });
                                        //endregion
                                        break;
                                    default:
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //endregion
            }
        });
    }
    //endregion
}
