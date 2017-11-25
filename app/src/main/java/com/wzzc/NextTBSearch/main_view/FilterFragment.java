package com.wzzc.NextTBSearch.main_view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.wzzc.base.Default;
import com.wzzc.new_index.SuperDeliver.SuperDeliverDelegate;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/7/25.
 */

public class FilterFragment extends Fragment implements View.OnClickListener{
    RelativeLayout layout_out;
    EditText et_back_one, et_back_tow, et_money_one, et_money_tow;
    Spinner spinner_sales;
    Button bt_reset, bt_finish;
    TBrebackItemDelegate tBrebackItemDelegate;
    String category_id;
    TextView tv_reback_1,tv_reback_2,tv_reback_3,tv_reback_4,tv_reback_5/*,tv_reback_6*/,
            tv_money_1,tv_money_2,tv_money_3,tv_money_4,tv_money_5,tv_money_6;
    int pType;
    boolean hasCoupons,is_tmail;
    String keyWords;
    String sold_method;
    String sold_order_type;
    public static final String TBD = "tBrebackItemDelegate";
    public static final String KeyWords = "kw";
    public static final String CID = "category_id";
    public static final String P_TYPE = "p_type";
    public static final String HasCoupons = "coupons";
    public static final String Is_Tmail = "is_Tmail";
    public static final String Money_start = "mf";
    public static final String Money_end = "me";
    public static final String Reback_start = "rs";
    public static final String Reback_end = "re";
    public static final String Sold_method = "sm";
    public static final String Sold_order_type = "sot";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_filter, null);
        layout_out = (RelativeLayout) v.findViewById(R.id.layout_out);
        spinner_sales = (Spinner) v.findViewById(R.id.spinner_sold);
        et_back_one = (EditText) v.findViewById(R.id.et_back_one);
        et_back_tow = (EditText) v.findViewById(R.id.et_back_tow);
        et_money_one = (EditText) v.findViewById(R.id.et_money_one);
        et_money_tow = (EditText) v.findViewById(R.id.et_money_tow);
        tv_reback_1 = (TextView) v.findViewById(R.id.tv_reback_1);
        tv_reback_2 = (TextView) v.findViewById(R.id.tv_reback_2);
        tv_reback_3 = (TextView) v.findViewById(R.id.tv_reback_3);
        tv_reback_4 = (TextView) v.findViewById(R.id.tv_reback_4);
        tv_reback_5 = (TextView) v.findViewById(R.id.tv_reback_5);
//        tv_reback_6 = (TextView) v.findViewById(R.id.tv_reback_6);
        tv_money_1 = (TextView) v.findViewById(R.id.tv_money_1);
        tv_money_2 = (TextView) v.findViewById(R.id.tv_money_2);
        tv_money_3 = (TextView) v.findViewById(R.id.tv_money_3);
        tv_money_4 = (TextView) v.findViewById(R.id.tv_money_4);
        tv_money_5 = (TextView) v.findViewById(R.id.tv_money_5);
        tv_money_6 = (TextView) v.findViewById(R.id.tv_money_6);
        bt_reset = (Button) v.findViewById(R.id.bt_reset);
        bt_finish = (Button) v.findViewById(R.id.bt_finish);

        tv_reback_1.setOnClickListener(this);
        tv_reback_2.setOnClickListener(this);
        tv_reback_3.setOnClickListener(this);
        tv_reback_4.setOnClickListener(this);
        tv_reback_5.setOnClickListener(this);
//        tv_reback_6.setOnClickListener(this);
        tv_money_1.setOnClickListener(this);
        tv_money_2.setOnClickListener(this);
        tv_money_3.setOnClickListener(this);
        tv_money_4.setOnClickListener(this);
        tv_money_5.setOnClickListener(this);
        tv_money_6.setOnClickListener(this);
        bt_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });
        bt_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outFragment();
                submit();
            }
        });
        layout_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outFragment();
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle.containsKey(TBD)) {
            tBrebackItemDelegate = bundle.getParcelable(TBD);
        }
        if (bundle.containsKey(CID)) {
            category_id = bundle.getString(CID);

        }
        if (bundle.containsKey(KeyWords)) {
            keyWords = bundle.getString(KeyWords);
        } else {
            keyWords = "";
        }
        if (bundle.containsKey(P_TYPE)) {
            pType = bundle.getInt(P_TYPE);

        }
        if (bundle.containsKey(HasCoupons)) {
            hasCoupons = bundle.getBoolean(HasCoupons);
        }
        if (bundle.containsKey(Is_Tmail)) {
            is_tmail = bundle.getBoolean(Is_Tmail);
        }

        String filterStartBack =  (bundle.getString(Reback_start) == null || bundle.getString(Reback_start).length() == 0) ? "0" : bundle.getString(Reback_start);
        String filterEndBack =  (bundle.getString(Reback_end) == null || bundle.getString(Reback_end).length() == 0) ? "0" : bundle.getString(Reback_end);
        initRebackSelect(Integer.parseInt(filterStartBack),Integer.parseInt(filterEndBack));

        String filterMoneyBack =  (bundle.getString(Money_start) == null || bundle.getString(Money_start).length() == 0) ? "0" : bundle.getString(Money_start);
        String filterMoneyEnd =  (bundle.getString(Money_end) == null || bundle.getString(Money_end).length() == 0) ? "0" : bundle.getString(Money_end);
        initMoneySelect(Integer.parseInt(filterMoneyBack),Integer.parseInt(filterMoneyEnd));

        et_back_one.setText(bundle.getString(Reback_start));
        et_back_tow.setText(bundle.getString(Reback_end));
        et_money_one.setText(bundle.getString(Money_start));
        et_money_tow.setText(bundle.getString(Money_end));

        sold_order_type = bundle.getString(Sold_order_type);
        if (sold_order_type == null) {
            sold_order_type = "";
        }
        sold_method = bundle.getString(Sold_method);
        String[] strings = new String[]{"综合排序","从高到低", "从低到高"};
        //适配器
        ArrayAdapter<String> arr_adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, strings);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sales.setAdapter(arr_adapter);
        spinner_sales.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (tBrebackItemDelegate != null) {
                    sold_method = TBrebackItemDelegate.Sort_Method_Number;
                    switch (position) {
                        case 0:
                            sold_order_type = "";
                            break;
                        case 1:
                            sold_order_type = TBrebackItemDelegate.Sort_Order_Desc;
                            break;
                        case 2:
                            sold_order_type = TBrebackItemDelegate.Sort_Order_Asc;
                            break;
                        default:
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (sold_method.equals(SuperDeliverDelegate.Sort_Method_Number)) {
            switch (sold_order_type){
                case SuperDeliverDelegate.Sort_Order_Desc:
                    spinner_sales.setSelection(1);
                    break;
                case SuperDeliverDelegate.Sort_Order_Asc:
                    spinner_sales.setSelection(2);
                    break;
                default:
            }
        } else if (sold_method.equals(TBrebackItemDelegate.Sort_Method_Number)) {
            switch (sold_order_type){
                case TBrebackItemDelegate.Sort_Order_Desc:
                    spinner_sales.setSelection(1);
                    break;
                case TBrebackItemDelegate.Sort_Order_Asc:
                    spinner_sales.setSelection(2);
                    break;
                default:
            }
        }
    }

    protected void outFragment() {
        Default.getActivity().onBackPressed();
    }

    protected void reset() {
        tv_reback_1.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_angle_gray));
        tv_reback_2.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_angle_gray));
        tv_reback_3.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_angle_gray));
        tv_reback_4.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_angle_gray));
        tv_reback_5.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_angle_gray));
//        tv_reback_6.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_angle_gray));
        tv_money_1.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_angle_gray));
        tv_money_2.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_angle_gray));
        tv_money_3.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_angle_gray));
        tv_money_4.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_angle_gray));
        tv_money_5.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_angle_gray));
        tv_money_6.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_angle_gray));
        et_back_one.setText("");
        et_back_tow.setText("");
        et_money_one.setText("");
        et_money_tow.setText("");
    }


    protected void submit() {
        if (tBrebackItemDelegate != null) {
            tBrebackItemDelegate.showFilterList(et_back_one.getText().toString(), et_back_tow.getText().toString(),
                    et_money_one.getText().toString(), et_money_tow.getText().toString(), sold_method,sold_order_type, hasCoupons, keyWords);

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_reback_1:
                if (et_back_one.getText().toString().equals("0") && et_back_tow.getText().toString().equals("5")) {
                    clearSelectBack();
                    et_back_one.setText("");
                    et_back_tow.setText("");
                } else {
                    et_back_one.setText("0");
                    et_back_tow.setText("5");
                    changeSelectReback(v);
                }
                break;
            case R.id.tv_reback_2:
                if (et_back_one.getText().toString().equals("5") && et_back_tow.getText().toString().equals("10")) {
                    clearSelectBack();
                    et_back_one.setText("");
                    et_back_tow.setText("");
                } else {
                    et_back_one.setText("5");
                    et_back_tow.setText("10");
                    changeSelectReback(v);
                }
                break;
            case R.id.tv_reback_3:
                if (et_back_one.getText().toString().equals("10") && et_back_tow.getText().toString().equals("15")) {
                    clearSelectBack();
                    et_back_one.setText("");
                    et_back_tow.setText("");
                } else {
                    et_back_one.setText("10");
                    et_back_tow.setText("15");
                    changeSelectReback(v);
                }
                break;
            case R.id.tv_reback_4:
                if (et_back_one.getText().toString().equals("15") && et_back_tow.getText().toString().equals("20")) {
                    clearSelectBack();
                    et_back_one.setText("");
                    et_back_tow.setText("");
                } else {
                    et_back_one.setText("15");
                    et_back_tow.setText("20");
                    changeSelectReback(v);
                }
                break;
            case R.id.tv_reback_5:
                if (et_back_one.getText().toString().equals("20") && et_back_tow.getText().toString().equals("")) {
                    clearSelectBack();
                    et_back_one.setText("");
                    et_back_tow.setText("");
                } else {
                    et_back_one.setText("20");
                    et_back_tow.setText("");
                    changeSelectReback(v);
                }
                break;
            /*case R.id.tv_reback_6:
                if (et_back_one.getText().toString().equals("25") && et_back_tow.getText().toString().equals("")) {
                    clearSelectBack();
                    et_back_one.setText("");
                    et_back_tow.setText("");
                } else {
                    et_back_one.setText("25");
                    et_back_tow.setText("");
                    changeSelectReback(v);
                }
                break;*/
            case R.id.tv_money_1:
                if (et_money_one.getText().toString().equals("0") && et_money_tow.getText().toString().equals("100")) {
                    clearSelectMoney();
                    et_money_one.setText("");
                    et_money_tow.setText("");
                } else {
                    et_money_one.setText("0");
                    et_money_tow.setText("100");
                    changeSelectMoney(v);
                }
                break;
            case R.id.tv_money_2:
                if (et_money_one.getText().toString().equals("100") && et_money_tow.getText().toString().equals("200")) {
                    clearSelectMoney();
                    et_money_one.setText("");
                    et_money_tow.setText("");
                } else {
                    et_money_one.setText("100");
                    et_money_tow.setText("200");
                    changeSelectMoney(v);
                }
                break;
            case R.id.tv_money_3:
                if (et_money_one.getText().toString().equals("200") && et_money_tow.getText().toString().equals("300")) {
                    clearSelectMoney();
                    et_money_one.setText("");
                    et_money_tow.setText("");
                } else {
                    et_money_one.setText("200");
                    et_money_tow.setText("300");
                    changeSelectMoney(v);
                }
                break;
            case R.id.tv_money_4:
                if (et_money_one.getText().toString().equals("300") && et_money_tow.getText().toString().equals("500")) {
                    clearSelectMoney();
                    et_money_one.setText("");
                    et_money_tow.setText("");
                } else {
                    et_money_one.setText("300");
                    et_money_tow.setText("500");
                    changeSelectMoney(v);
                }
                break;
            case R.id.tv_money_5:
                if (et_money_one.getText().toString().equals("500") && et_money_tow.getText().toString().equals("700")) {
                    clearSelectMoney();
                    et_money_one.setText("");
                    et_money_tow.setText("");
                } else {
                    et_money_one.setText("500");
                    et_money_tow.setText("700");
                    changeSelectMoney(v);
                }
                break;
            case R.id.tv_money_6:
                if (et_money_one.getText().toString().equals("700") && et_money_tow.getText().toString().equals("")) {
                    clearSelectMoney();
                    et_money_one.setText("");
                    et_money_tow.setText("");
                } else {
                    et_money_one.setText("700");
                    et_money_tow.setText("");
                    changeSelectMoney(v);
                }

                break;
            default:
        }
    }

    private void initRebackSelect (int filterStartBack ,int filterEndBack ) {
        if (filterStartBack == 0 && filterEndBack == 0) {
            return;
        }
        /*if (filterStartBack >= 25) {
            changeSelectReback(tv_reback_6);
        }*/
        if (filterStartBack >= 20) {
            changeSelectReback(tv_reback_5);
        }
        else if (filterEndBack <= 5) {
            changeSelectReback(tv_reback_1);
        } else if (filterEndBack <= 10) {
            changeSelectReback(tv_reback_2);
        }else if (filterEndBack <= 15) {
            changeSelectReback(tv_reback_3);
        }else if (filterEndBack <= 20) {
            changeSelectReback(tv_reback_4);
        }/*else if (filterEndBack <= 25) {
            changeSelectReback(tv_reback_5);
        }*/
    }
    private void initMoneySelect ( int filterMoneyBack , int filterMoneyEnd ) {
        if (filterMoneyBack == 0 && filterMoneyEnd == 0) {
            return;
        }
        if (filterMoneyBack >= 700) {
            changeSelectMoney(tv_money_6);
        }else if (filterMoneyEnd <= 100) {
            changeSelectMoney(tv_money_1);
        } else if (filterMoneyEnd <= 200) {
            changeSelectMoney(tv_money_2);
        }else if (filterMoneyEnd <= 300) {
            changeSelectMoney(tv_money_3);
        }else if (filterMoneyEnd <= 500) {
            changeSelectMoney(tv_money_4);
        }else if (filterMoneyEnd <= 700) {
            changeSelectMoney(tv_money_5);
        }
    }

    private void changeSelectReback (View v){
        clearSelectBack();
        v.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_angle_alpha_red));
    }

    private void changeSelectMoney (View v){
        clearSelectMoney();
        v.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_angle_alpha_red));
    }

    private void clearSelectMoney (){
        tv_money_1.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_angle_gray));
        tv_money_2.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_angle_gray));
        tv_money_3.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_angle_gray));
        tv_money_4.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_angle_gray));
        tv_money_5.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_angle_gray));
        tv_money_6.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_angle_gray));
    }

    private void clearSelectBack () {
        tv_reback_1.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_angle_gray));
        tv_reback_2.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_angle_gray));
        tv_reback_3.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_angle_gray));
        tv_reback_4.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_angle_gray));
        tv_reback_5.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_angle_gray));
//        tv_reback_6.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_angle_gray));
    }
}
