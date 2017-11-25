package com.wzzc.new_index.SuperDeliver;

import android.os.Parcelable;
import android.view.View;

/**
 * Created by by Administrator on 2017/6/27.
 */

public interface SuperDeliverDelegate extends Parcelable{
     String Sort_Method_Default = "default";
     String Sort_Method_Number = "number";
     String Sort_Method_Price = "price";
     String Sort_Method_Rebate = "rebate";

     String Sort_Order_Desc = "DESC";//从高到低
     String Sort_Order_Asc = "ASC";

     void showLogin(View view);
     void showIndex ();
     void showPlay (String key ,String country_id,String province_id, String city_id,String district_id , String type);
     void showUserCenter ();
}
