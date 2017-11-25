package com.wzzc.other_function.action;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 2017/8/21.
 */

public class ClickBean implements Parcelable {
    private String ad_link;
    private String ad_code;
    private String data_type;
    private String num_iid;

    public ClickBean (){}
    public ClickBean (String ad_link,String ad_code , String data_type , String num_iid) {
        this.ad_link = ad_link;
        this.ad_code = ad_code;
        this.data_type = data_type;
        this.num_iid = num_iid;
    }

    //region GET/SET

    public String getAd_link() {
        if (ad_link == null) ad_link = "";
        return ad_link;
    }

    public void setAd_link(String ad_link) {
        this.ad_link = ad_link;
    }

    public String getAd_code() {
        if (ad_code == null) ad_code = "";
        return ad_code;
    }

    public void setAd_code(String ad_code) {
        this.ad_code = ad_code;
    }

    public String getData_type() {
        if (data_type == null) data_type = "";
        return data_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }

    public String getNum_iid() {
        if (num_iid == null) num_iid = "";
        return num_iid;
    }

    public void setNum_iid(String num_iid) {
        this.num_iid = num_iid;
    }

    //endregion

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getAd_link());
        dest.writeString(getAd_code());
        dest.writeString(getData_type());
        dest.writeString(getNum_iid());
    }

    public static final Parcelable.Creator CREATOR = new ClassLoaderCreator<ClickBean>() {
        @Override
        public ClickBean createFromParcel(Parcel source) {
            return new ClickBean(source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString());
        }

        @Override
        public ClickBean[] newArray(int size) {
            return new ClickBean[0];
        }

        @Override
        public ClickBean createFromParcel(Parcel source, ClassLoader loader) {
            return new ClickBean(source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString());
        }
    };
}
