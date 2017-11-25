package com.wzzc.NextIndex.views.e.other_activity.NextRelation;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by by Administrator on 2017/8/15.
 * 购物详情
 */

public class Shopping implements Parcelable {
    private String orderNumber;//订单号
    private String reBackMoney;//返利金额
    public Shopping(){}
    public Shopping(String orderNumber , String reBackMoney){
        this.orderNumber = orderNumber;
        this.reBackMoney = reBackMoney;
    }

    public String getOrderNumber() {
        if (orderNumber == null) {
            orderNumber = "未知";
        }
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getReBackMoney() {
        if (reBackMoney == null) {
            reBackMoney = "未知";
        }
        return reBackMoney;
    }

    public void setReBackMoney(String reBackMoney) {
        this.reBackMoney = reBackMoney;
    }

    //region Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderNumber);
        dest.writeString(reBackMoney);
    }

    public static final Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return  new Shopping(source.readString(),source.readString());
        }

        @Override
        public Object[] newArray(int size) {
            return new Shopping[0];
        }
    };
    //endregion
}
