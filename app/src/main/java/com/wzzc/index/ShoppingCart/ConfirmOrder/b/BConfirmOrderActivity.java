package com.wzzc.index.ShoppingCart.ConfirmOrder.b;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.NextIndex.NextIndex;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.index.ShoppingCart.ConfirmOrder.b.main_view.BConfirmOrderGoodsItemView;
import com.wzzc.NextIndex.views.e.other_activity.address.AddressActivity;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.MessageBox;
import com.wzzc.other_function.pay.PayFromWX;
import com.wzzc.other_function.pay.PayFromZFB;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/6/20.
 */

public class BConfirmOrderActivity extends BaseActivity implements View.OnClickListener{
    //region ```
    @ViewInject(R.id.layout_address)
    RelativeLayout layout_address;
    @ViewInject(R.id.tv_name)
    TextView tv_name;
    @ViewInject(R.id.tv_phone)
    TextView tv_phone;
    @ViewInject(R.id.tv_address)
    TextView tv_address;
    @ViewInject(R.id.tv_addressInfo)
    TextView tv_addressInfo;
    @ViewInject(R.id.listView)
    ListView listView;
    @ViewInject(R.id.et_message)
    EditText et_message;
    @ViewInject(R.id.tv_shipping)
    TextView tv_shipping;
    @ViewInject(R.id.tv_other_shipping)
    TextView tv_other_shipping;
    @ViewInject(R.id.tv_how_oos)
    TextView tv_how_oos;
    @ViewInject(R.id.tv_other_how_oos)
    TextView tv_other_how_oos;
    @ViewInject(R.id.et_use_surplus)
    EditText et_use_surplus;
    @ViewInject(R.id.tv_surplus_remain)
    TextView tv_surplus_remain;
    @ViewInject(R.id.tv_zcb_remain)
    TextView tv_zcb_remain;
    @ViewInject(R.id.tv_production_money)
    TextView tv_production_money;
    @ViewInject(R.id.tv_earn)
    TextView tv_earn;
    @ViewInject(R.id.tv_payNumber)
    TextView tv_payNumber;
    @ViewInject(R.id.tv_payMoney)
    TextView tv_payMoney;
    @ViewInject(R.id.tv_submit)
    TextView tv_submit;
    //endregion
    public static String ADDRESS = "address";
    String address_id;
    int zcb_remain;/*可用子成币数量*/
    ArrayList<JSONObject> arrPayList /*支付方式列表*/;
    String shipping_id;/*配送方式ID*/
    int shipping_fee/*快递价格*/;
    JSONArray jrr_shipping;/*配送方式集*/
    JSONArray jrr_how_oos;/*缺货处理方式集*/
    float surplus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    protected void init() {
        tv_other_how_oos.setOnClickListener(this);
        tv_other_shipping.setOnClickListener(this);
        layout_address.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
        et_use_surplus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() <= 0) {
                    et_use_surplus.setText("0");
                } else {
                    changeAllMoney();
                }
            }
        });

        setInfoFromService(getIntent().getExtras());
    }

    private void setInfoFromService (Bundle bundle) {
        //region 获取数据
        JSONObject para = new JSONObject();
        try {
            para.put("address_id", bundle.get(ADDRESS));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(this, "exflow/checkOrder", para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                try {
                    JSONObject json_status = sender.getJSONObject("status");
                    int succeed = json_status.getInt("succeed");
                    if (succeed == 1) {
                        //region 地址
                        JSONObject json_consignee = sender.getJSONObject("data").getJSONObject("consignee");
                        address_id = sj(json_consignee, "id");
                        tv_name.setText(sj(json_consignee, "consignee"));
                        tv_phone.setText(sj(json_consignee, "mobile"));
                        tv_address.setText(sj(json_consignee, "province_name") + sj(json_consignee, "city_name") + sj(json_consignee, "district_name"));
                        tv_addressInfo.setText(sj(json_consignee, "address"));
                        //endregion

                        //region 产品
                        JSONArray jrr_goods_list = sender.getJSONObject("data").getJSONArray("goods_list");
                        ArrayList<JSONObject> data = new ArrayList<JSONObject>();
                        for (int i = 0; i < jrr_goods_list.length(); i++) {
                            data.add(jrr_goods_list.getJSONObject(i));
                        }
                        listView.setAdapter(new MyAdapter(BConfirmOrderActivity.this, data));
                        Default.fixListViewHeight(listView);
                        //endregion

                        //region 快递
                        try {
                            jrr_shipping = sender.getJSONObject("data").getJSONArray("shipping_list");
                            if (jrr_shipping.length() > 0) {
                                shipping_id = sj(jrr_shipping.getJSONObject(0), "shipping_id");
                                shipping_fee = ij(jrr_shipping.getJSONObject(0), "shipping_fee");
                                tv_shipping.setText(sj(jrr_shipping.getJSONObject(0), "shipping_name") + " " + sj(jrr_shipping.getJSONObject(0), "format_shipping_fee"));
                            } else {
                                MessageBox.Show("地址错误", "该地址不支持配送，请重新下单。", new String[]{"确定"}, new MessageBox.MessBtnBack() {
                                    @Override
                                    public void Back(int index) {
                                        finish();
                                    }
                                });
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                            MessageBox.Show("地址错误", "该地址不支持配送，请重新下单。", new String[]{"确定"}, new MessageBox.MessBtnBack() {
                                @Override
                                public void Back(int index) {
                                    finish();
                                }
                            });
                        }

                        //endregion


                        //region 支付方式
                        JSONArray jrr_payment_list = sender.getJSONObject("data").getJSONArray("payment_list");
                        arrPayList = new ArrayList<JSONObject>();
                        for (int i = 0; i < jrr_payment_list.length(); i++) {
                            arrPayList.add(jrr_payment_list.getJSONObject(i));
                        }
                        //endregion

                        //region 缺货处理
                        jrr_how_oos = sender.getJSONObject("data").getJSONArray("how_oos_list");
                        tv_how_oos.setText(jrr_how_oos.getString(0));
                        //endregion

                        //region 允许使用子成币数量
                        zcb_remain = Integer.valueOf(sj(sender.getJSONObject("data"),"your_rank_points"));
                        tv_zcb_remain.setText("剩余子成币:" + zcb_remain);
                        //endregion

                        //region 余额
                        int allow_use_surplus = ij(sender.getJSONObject("data"),"allow_use_surplus");
                        surplus = Float.valueOf(sj(sender.getJSONObject("data"),"your_surplus"));
                        tv_surplus_remain.setText("余额：" + surplus);
                        if (allow_use_surplus == 0) {
                            et_use_surplus.setFocusable(false);
                        }
                        //endregion

                        //region 支付数额
                        JSONObject json_total = sender.getJSONObject("data").getJSONObject("total");
                        tv_payMoney.setText("￥" + ij(json_total,"goods_price"));
                        tv_production_money.setText(String.valueOf(ij(json_total,"exchange_integral")));
                        //region 数量
                        tv_payNumber.setText("共"+ij(json_total,"real_goods_count")+"件商品 ");
                        //endregion
                        //endregion

                        changeAllMoney();
                    } else {
                        MessageBox.Show(json_status.getString("error_desc"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        //endregion
    }


    @Override
    protected void onActivityResult(Bundle bundle) {
        super.onActivityResult(bundle);
        setInfoFromService(bundle);
    }

    private String sj(JSONObject json, String key) {
        try {
            return json.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return "error";
        }
    }
    private int ij(JSONObject json, String key) {
        try {
            return json.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_address:
                Intent intent = new Intent();
                intent.putExtra(AddressActivity.SELECT, true);
                intent.putExtra(AddressActivity.ORDERTYPE, 1);
                AddActivity(AddressActivity.class, 0, intent);
                break;
            case R.id.tv_other_shipping:
                if (jrr_shipping != null && jrr_shipping.length()>0) {
                    String[] strs = new String[jrr_shipping.length()];
                    for (int i = 0 ; i < jrr_shipping.length() ; i ++) {
                        try {
                            strs[i] = sj(jrr_shipping.getJSONObject(i),"shipping_name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    MessageBox.createNewDialog();
                    MessageBox.Show("选择配送方式", strs, new MessageBox.MessBtnBack() {
                        @Override
                        public void Back(int index) {
                            MessageBox.dismiss();
                            try {
                                shipping_id = sj(jrr_shipping.getJSONObject(index),"shipping_id");
                                shipping_fee = ij(jrr_shipping.getJSONObject(index),"shipping_fee");
                                tv_shipping.setText(sj(jrr_shipping.getJSONObject(index),"shipping_name") + " " + sj(jrr_shipping.getJSONObject(index),"format_shipping_fee"));
                                changeAllMoney();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                break;
            case R.id.tv_other_how_oos:
                if (jrr_how_oos != null && jrr_how_oos.length()>0) {
                    String[] strs = new String[jrr_how_oos.length()];
                    for (int i = 0 ; i < jrr_how_oos.length() ; i ++) {
                        try {
                            strs[i] = jrr_how_oos.getString(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    MessageBox.createNewDialog();
                    MessageBox.Show("选择缺货处理方式", strs, new MessageBox.MessBtnBack() {
                        @Override
                        public void Back(int index) {
                            MessageBox.dismiss();
                            try {
                                tv_how_oos.setText(jrr_how_oos.getString(index));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                break;
            case R.id.tv_submit:
                float useSurplus = Float.valueOf(et_use_surplus.getText().toString());
                if (useSurplus > surplus) {
                    MessageBox.createNewDialog();
                    MessageBox.Show("超出可用余额");
                }else {
                    //region 支付
                    if (arrPayList != null && arrPayList.size() > 0) {
                        String[] strs = new String[arrPayList.size()];
                        for (int i = 0; i < arrPayList.size(); i++) {
                            strs[i] = sj(arrPayList.get(i), "pay_name");
                        }
                        MessageBox.createNewDialog();
                        MessageBox.Show("支付方式", strs, new MessageBox.MessBtnBack() {
                            @Override
                            public void Back(int index) {
                                MessageBox.dismiss();
                                ((NextIndex) AllBaseActivity.get(0)).showA();
                                String payID = sj(arrPayList.get(index), "pay_id");
                                switch (payID) {
                                    case "1"://zfb
                                        PayFromZFB.pay(BConfirmOrderActivity.this, 0, 1, Integer.valueOf(shipping_id), et_use_surplus.getText().toString(), address_id, 0, et_message.getText().toString(), "0");
                                        break;
                                    case "3"://wx
                                        PayFromWX.pay(BConfirmOrderActivity.this, 0, 1, Integer.valueOf(shipping_id), et_use_surplus.getText().toString(), address_id, 0, et_message.getText().toString(), "0");
                                        break;
                                    default:
                                        Default.showToast(getString(R.string.notDevelop));
                                }
                            }
                        });
                    }

                    //endregion
                }
                break;
            default:
        }
    }

    private void changeAllMoney () {
        String strFormMoney = tv_payMoney.getText().toString();
        Float forPayMoney = Float.valueOf(strFormMoney.substring(1,strFormMoney.length()));
        int use_surplus = Integer.parseInt(et_use_surplus.getText().toString());
        tv_payMoney.setText("￥"+(forPayMoney + shipping_fee - use_surplus));
    }

    class MyAdapter extends BaseAdapter {
        Context c;
        ArrayList<JSONObject> data;

        MyAdapter(Context c, ArrayList<JSONObject> data) {
            this.c = c;
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public JSONObject getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new BConfirmOrderGoodsItemView(c);
            }
            ((BConfirmOrderGoodsItemView) convertView).setInfo(getItem(position));
            return convertView;
        }
    }
}
