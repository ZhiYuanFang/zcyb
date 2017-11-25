package com.wzzc.NextTBSearch.main_view;

import android.os.Parcelable;
import android.view.View;

/**
 * Created by by Administrator on 2017/7/4.
 */

public interface TBrebackItemDelegate extends Parcelable{
    String Sort_Method_Default = "default";
    String Sort_Method_Number = "number";
    String Sort_Method_Rebate = "rebate";

    String Sort_Order_Desc = "DESC";//从高到低
    String Sort_Order_Asc = "ASC";

    void showLogin(View v);
    void search (String key) ;
    void showFilter(String keyWords , boolean is_TMail);
    void showFilterList(String filterStartBack ,String filterEndBack , String filterMoneyBack , String filterMoneyEnd ,String sold_method ,String sort_order, boolean is_TMail , String keyWords );
    void showQuan(String goods_name, String coupon_click_url);
}
