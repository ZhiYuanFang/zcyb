package com.wzzc.NextIndex.views.e.other_activity.NextRelation;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by by Administrator on 2017/8/15.
 * 推介关系人
 */

public class ReferralPerson implements Parcelable {
    private String userID;
    private String name;//名字
    private String phoneNumber;//电话号码
    private String headUrl;//头像
    private String reBackMoney;//返利金额
    private int directReferralNumber;//直推人数
    public ReferralPerson(){

    }
    public ReferralPerson (String userID , String name , String phoneNumber , String headUrl , String reBackMoney , int directReferralNumber) {
        this.userID = userID;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.headUrl = headUrl;
        this.reBackMoney = reBackMoney;
        this.directReferralNumber = directReferralNumber;
    }

    //region Set/Get


    public int getDirectReferralNumber() {
        return directReferralNumber;
    }

    public void setDirectReferralNumber(int directReferralNumber) {

        this.directReferralNumber = directReferralNumber;
    }

    public String getUserID() {
        if (userID == null) {
            userID = "未知";
        }
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        if (name == null) {
            name = "未知";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        if (phoneNumber == null) {
            phoneNumber = "未知";
        }
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getHeadUrl() {
        if (headUrl == null) {
            headUrl = "未知";
        }
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getReBackMoney() {
        if (reBackMoney == null || reBackMoney.length() == 0) {
            reBackMoney = "0.00";
        }
        return reBackMoney;
    }

    public void setReBackMoney(String reBackMoney) {
        this.reBackMoney = reBackMoney;
    }
    //endregion

    //region Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userID);
        dest.writeString(name);
        dest.writeString(phoneNumber);
        dest.writeString(headUrl);
        dest.writeString(reBackMoney);
        dest.writeInt(directReferralNumber);
    }

    public static final Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new ReferralPerson(
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
            source.readInt());
        }

        @Override
        public Object[] newArray(int size) {
            return new ReferralPerson[0];
        }
    };
    //endregion
}
