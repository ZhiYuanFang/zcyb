package com.wzzc.index.ShoppingCart.main_view;

import android.content.Context;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.wzzc.base.Default;
import com.wzzc.base.ExtendBaseActivity;
import com.wzzc.base.ExtendBaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.index.ShoppingCart.a.ACartView;
import com.wzzc.index.ShoppingCart.b.BCartView;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_view.BasicTitleView;
import com.wzzc.other_view.viewPager.MyViewPager;
import com.wzzc.other_view.viewPager.MyViewPagerDelegate;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/6/19.
 */

public class ShoppingCartView extends ExtendBaseView implements MyViewPagerDelegate, View.OnClickListener {
    private static final String TAG = "ShoppingCartView";
    public static final String TYPE = "type";
    public static final int SHOP_CART = 0;
    public static final int EXCHAGE_CART = 1;
    int type;
    //region ```
    @ViewInject(R.id.basicTitleView)
    BasicTitleView basicTitleView;
    @ViewInject(R.id.tv_shop_cart)
    TextView tv_shop_cart;
    @ViewInject(R.id.tv_exchange_cart)
    TextView tv_exchange_cart;
    @ViewInject(R.id.viewPager)
    MyViewPager mvp;
    //endregion
    ACartView acv;
    BCartView bcv;
    ArrayList<View> arrListView;

    public ShoppingCartView(Context context) {
        super(context);
    }

    public ShoppingCartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void init() {
        tv_shop_cart.setOnClickListener(this);
        tv_exchange_cart.setOnClickListener(this);
        acv = new ACartView(getContext());
        bcv = new BCartView(getContext());
        arrListView = new ArrayList<View>() {
            {
                add(acv);
                add(bcv);
            }
        };
        mvp.mvpd = this;
        mvp.setInfo(arrListView);
    }

    @Override
    public void setInfo(JSONObject json) {

    }

    public void setInfo(int t, JSONObject json, boolean showTitle) {
        if (!showTitle) {
            basicTitleView.setVisibility(GONE);
        }

        if (t >= 0) {
            this.type = t;
        }

        switch (type) {
            case SHOP_CART:
                basicTitleView.setTitle("购物车");
                serverCallBackType().callBack("0", json);
                break;
            case EXCHAGE_CART:
                basicTitleView.setTitle("兑换车");
                serverCallBackType().callBack("1", json);
                break;
            default:
        }

    }

    public void setInfo(int t) {
        if (t >= 0) {
            this.type = t;
        }

        switch (type) {
            case SHOP_CART:
                basicTitleView.setTitle("购物车");

                break;
            case EXCHAGE_CART:
                basicTitleView.setTitle("兑换车");
                break;
            default:
        }
        judgeNetConnectedAndSetInfoFromService("0");
    }

    public void showType(int type) {
        if (mvp.getAdapter() != null && mvp.getAdapter().getCount() > 0) {
            mvp.setCurrentItem(type);
        }
    }

    public void onlyShowTitle() {
        basicTitleView.setBackVisible(View.GONE);
        basicTitleView.setHomeVisible(View.GONE);
    }

    @Override
    protected String getFileKey() {
        return TAG + type;
    }

    @Override
    protected ExtendBaseView.ServerCallBack serverCallBack() {
        return new ExtendBaseView.ServerCallBack() {
            @Override
            public void callBack(Object json_data) {

            }
        };
    }

    @Override
    protected ExtendBaseActivity.ServerCallBackType serverCallBackType() {
        return new ExtendBaseActivity.ServerCallBackType() {
            @Override
            public void callBack(String type, Object json_data) {
                switch (type) {
                    case "0": {
                        JSONObject json = (JSONObject) json_data;
                        try {
                            if (json.getJSONArray("store_list").length() == 0) {
                                arrListView.set(SHOP_CART, new ShoppingCartNoDataView(getContext()));
                            } else {
                                acv.setInfo((JSONObject) json_data);
                                arrListView.set(SHOP_CART, acv);
                            }
                            mvp.setInfo(arrListView);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case "1": {
                        JSONObject json = (JSONObject) json_data;
                        try {
                            if (json.getJSONArray("goods_list").length() == 0) {
                                arrListView.set(EXCHAGE_CART, new ShoppingCartNoDataView(getContext()));
                            } else {
                                bcv.setInfo((JSONObject) json_data);
                                arrListView.set(EXCHAGE_CART, bcv);
                            }
                            mvp.setInfo(arrListView);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    default:
                }
            }
        };
    }

    @Override
    protected void setInfoFromService(String type) {
        switch (type) {
            case "0":
                AsynServer.BackObject(getContext(), "cart/list", this.type == SHOP_CART, new JSONObject(), new AsynServer.BackObject() {
                    @Override
                    public void Back(JSONObject sender) {
                        try {
                            initialized(sender, "0", serverCallBackType());
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mvp.setCurrentItem(ShoppingCartView.this.type);
                                    judgeNetConnectedAndSetInfoFromService("1");
                                }
                            }, Default.delayTime);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case "1":
                AsynServer.BackObject(getContext(), "excart/list", this.type == EXCHAGE_CART, new JSONObject(), new AsynServer.BackObject() {
                    @Override
                    public void Back(JSONObject sender) {
                        try {
                            initialized(sender, "1", serverCallBackType());
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mvp.setCurrentItem(ShoppingCartView.this.type);
                                }
                            }, Default.delayTime);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            default:
        }
    }

    @Override
    public void onPageSelected(int position) {
        type = position;
        switch (position) {
            case SHOP_CART:
                tv_shop_cart.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_pressed_solid_circle_red));
                tv_exchange_cart.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_pressed_circle_solid_light_gray));
                basicTitleView.setTitle("购物车");
                break;
            case EXCHAGE_CART:
                tv_exchange_cart.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_pressed_solid_circle_red));
                tv_shop_cart.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_pressed_circle_solid_light_gray));
                basicTitleView.setTitle("兑换车");
                break;
            default:
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_shop_cart:
                mvp.setCurrentItem(SHOP_CART);

                break;
            case R.id.tv_exchange_cart:
                mvp.setCurrentItem(EXCHAGE_CART);

                break;
            default:
        }
    }
}
