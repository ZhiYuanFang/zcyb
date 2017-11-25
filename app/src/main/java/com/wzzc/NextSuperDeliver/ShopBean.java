package com.wzzc.NextSuperDeliver;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/8/22.
 */

public class ShopBean implements Parcelable {
    Object tag;
    String brand_id ;//商铺ID
    String brand_name;//商铺名称
    String brand_logo;//商铺logo
    String brand_desc;
    int left_day;//剩余时间
    ArrayList<Production> productionArrayList ;//商铺名下产品
    //lp 20170831 add brand_desc
    public ShopBean(){}
    public ShopBean(String brand_id , String brand_name , String brand_logo,
                    int left_day){
        this.brand_id = brand_id;
        this.brand_name = brand_name;
        this.brand_logo = brand_logo;
        this.left_day = left_day;
    }
    public ShopBean(String brand_id , String brand_name , String brand_logo,
                    int left_day,String brand_desc){
        this.brand_id = brand_id;
        this.brand_name = brand_name;
        this.brand_logo = brand_logo;
        this.left_day = left_day;
        this.brand_desc = brand_desc;
    }
    public ShopBean(String brand_id , String brand_name , String brand_logo,
                    int left_day , ArrayList<Production> productionArrayList,String brand_desc){
        this.brand_id = brand_id;
        this.brand_name = brand_name;
        this.brand_logo = brand_logo;
        this.left_day = left_day;
        this.productionArrayList = productionArrayList;
        this.brand_desc=brand_desc;//lp 20170831
    }


    //region GET/SET

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public String getBrand_desc() {
        if(brand_desc == null){
            brand_desc = "";
        }
        return brand_desc;
    }

    public void setBrand_desc(String brand_desc) {
        this.brand_desc = brand_desc;
    }

    public void String (String brand_desc) {
        this.brand_desc = brand_desc;
    }

    public String getBrand_id() {
        if (brand_id == null) {
            brand_id = "";
        }
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getBrand_name() {
        if (brand_name == null) {
            brand_name = "";
        }
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getBrand_logo() {
        if (brand_logo == null) {
            brand_logo = "";
        }
        return brand_logo;
    }

    public void setBrand_logo(String brand_logo) {
        this.brand_logo = brand_logo;
    }

    public int getLeft_day() {
        return left_day;
    }

    public void setLeft_day(int left_day) {
        this.left_day = left_day;
    }

    public ArrayList<Production> getProductionArrayList() {
        if (productionArrayList == null) {
            productionArrayList = new ArrayList<>();
        }
        return productionArrayList;
    }

    public void setProductionArrayList(ArrayList<Production> productionArrayList) {
        this.productionArrayList = productionArrayList;
    }

    //endregion

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getBrand_id());
        dest.writeString(getBrand_name());
        dest.writeString(getBrand_logo());
        dest.writeInt(getLeft_day());
        dest.writeList(getProductionArrayList());
        dest.writeString(getBrand_desc());
    }

    public static final Parcelable.Creator CREATOR = new Creator<ShopBean>() {
        @Override
        public ShopBean createFromParcel(Parcel source) {
            String brand_id = source.readString();//商铺ID
            String brand_name = source.readString();//商铺名称
            String brand_logo = source.readString();//商铺logo
            int left_day = source.readInt();//剩余时间
            ArrayList<Production> productions = new ArrayList<>();
            source.readList(productions,Production.class.getClassLoader());
            String brand_desc=source.readString(); //lp add
            return new ShopBean(brand_id,brand_name,brand_logo,left_day,productions,brand_desc);
        }

        @Override
        public ShopBean[] newArray(int size) {
            return new ShopBean[0];
        }
    };
}
