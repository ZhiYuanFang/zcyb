package com.wzzc.NextSuperDeliver;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by by Administrator on 2017/8/12.
 * 超值送产品类
 */

public class Production implements Parcelable {
    private String goods_id;
    private   String goods_name;
    private   String shop_price;
    private   boolean is_new;//今日上新
    private   boolean is_seckill;//超级秒杀
    private    boolean is_best;//高返
    private    boolean is_hot;
    private    String goods_thumb;
    private    String goods_img;
    private    String rebates;
    private    String rebate_text;
    private    String icon;
    private    String num_iid;
    private     boolean show_coupon;
    private     String coupon_info;
    private     String coupon_click_url;
    private    String footer_title;
    private    String goods_from;
    private int left_day;//剩余几天
    private String coupon_price;//优惠券金额
    public Production(){};
    public Production (String goods_id,
            String goods_name,
            String shop_price,
            boolean is_new,
            boolean is_seckill,
            boolean is_best,
            boolean is_hot,
            String goods_thumb,
            String goods_img,
            String rebates,
            String rebate_text,
            String icon,
            String num_iid,
            boolean show_coupon,
            String coupon_info,
            String coupon_click_url,
            String footer_title,
            String goods_from,
                       int left_day,
                       String coupon_price) {
        this.goods_id = goods_id;
        this.goods_name = goods_name;
        this.shop_price = shop_price;
        this.is_new = is_new;
        this.is_seckill = is_seckill;
        this.is_best = is_best;
        this.is_hot = is_hot;
        this.goods_thumb = goods_thumb;
        this.goods_img = goods_img;
        this.rebates = rebates;
        this.rebate_text = rebate_text;
        this.icon = icon;
        this.num_iid = num_iid;
        this.show_coupon = show_coupon;
        this.coupon_info = coupon_info;
        this.coupon_click_url = coupon_click_url;
        this.footer_title = footer_title;
        this.goods_from = goods_from;
        this.left_day = left_day;
        this.coupon_price = coupon_price;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getShop_price() {
        return shop_price;
    }

    public void setShop_price(String shop_price) {
        this.shop_price = shop_price;
    }

    public boolean is_new() {
        return is_new;
    }

    public void setIs_new(boolean is_new) {
        this.is_new = is_new;
    }

    public boolean is_seckill() {
        return is_seckill;
    }

    public void setIs_seckill(boolean is_seckill) {
        this.is_seckill = is_seckill;
    }

    public boolean is_best() {
        return is_best;
    }

    public void setIs_best(boolean is_best) {
        this.is_best = is_best;
    }

    public boolean is_hot() {
        return is_hot;
    }

    public void setIs_hot(boolean is_hot) {
        this.is_hot = is_hot;
    }

    public String getGoods_thumb() {
        return goods_thumb;
    }

    public void setGoods_thumb(String goods_thumb) {
        this.goods_thumb = goods_thumb;
    }

    public String getGoods_img() {
        return goods_img;
    }

    public void setGoods_img(String goods_img) {
        this.goods_img = goods_img;
    }

    public String getRebates() {
        return rebates;
    }

    public void setRebates(String rebates) {
        this.rebates = rebates;
    }

    public String getRebate_text() {
        return rebate_text;
    }

    public void setRebate_text(String rebate_text) {
        this.rebate_text = rebate_text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getNum_iid() {
        return num_iid;
    }

    public void setNum_iid(String num_iid) {
        this.num_iid = num_iid;
    }

    public boolean isShow_coupon() {
        return show_coupon;
    }

    public void setShow_coupon(boolean show_coupon) {
        this.show_coupon = show_coupon;
    }

    public String getCoupon_info() {
        return coupon_info;
    }

    public void setCoupon_info(String coupon_info) {
        this.coupon_info = coupon_info;
    }

    public String getCoupon_click_url() {
        return coupon_click_url;
    }

    public void setCoupon_click_url(String coupon_click_url) {
        this.coupon_click_url = coupon_click_url;
    }

    public String getFooter_title() {
        return footer_title;
    }

    public void setFooter_title(String footer_title) {
        this.footer_title = footer_title;
    }

    public String getGoods_from() {
        return goods_from;
    }

    public void setGoods_from(String goods_from) {
        this.goods_from = goods_from;
    }

    public int getLeft_day() {
        return left_day;
    }

    public void setLeft_day(int left_day) {
        this.left_day = left_day;
    }

    public String getCoupon_price() {
        if (coupon_price == null || coupon_price.length() == 0) {
            coupon_price = "0";
        }
        return coupon_price;
    }

    public void setCoupon_price(String coupon_price) {
        this.coupon_price = coupon_price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(goods_id);
        dest.writeString(goods_name);
        dest.writeString(shop_price);
        dest.writeByte((byte) (is_new ? 1 : 0));
        dest.writeByte((byte) (is_seckill ? 1 : 0));
        dest.writeByte((byte) (is_best ? 1 : 0));
        dest.writeByte((byte) (is_hot ? 1 : 0));
        dest.writeString(goods_thumb);
        dest.writeString(goods_img);
        dest.writeString(rebates);
        dest.writeString(rebate_text);
        dest.writeString(icon);
        dest.writeString(num_iid);
        dest.writeByte((byte) (show_coupon ? 1 : 0));
        dest.writeString(coupon_info);
        dest.writeString(coupon_click_url);
        dest.writeString(footer_title);
        dest.writeString(goods_from);
        dest.writeInt(left_day);
        dest.writeString(getCoupon_price());
    }

    public static final Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new Production(source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readByte() == 1,
                    source.readByte() == 1,
                    source.readByte() == 1,
                    source.readByte() == 1,
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readByte() == 1,
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readInt(),
                    source.readString());
        }

        @Override
        public Object[] newArray(int size) {
            return new Production[0];
        }
    };
}
