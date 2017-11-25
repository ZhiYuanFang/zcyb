package com.wzzc.NextSuperDeliver.list;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by by Administrator on 2017/8/8.
 */

public class Category implements Parcelable {
    public static final String AllCategoryID = "0";
    public static final String AllCategoryName = "全部";

    private String categoryName;
    private String categoryID;
    private String parentCategoryID;
    private String minPrice;//最低价格 整数
    private String maxPrice;// 最高价格 整数
    private String minRebate;//最低分成比 例如1 代表百分之1
    private String maxRebate;//最高分成比 例如1 代表百分之1
    private int pType;//1今日上新 2超级秒杀 3高返利推荐 4九块九专区
    private String keyWords;//搜索关键词
    private String sort_method;// 排序方式 default综合 number销量 price价格 rebate返利比
    private String sort_order;//排序顺序 DESC从高到底 ASC从低到高
    private int isCoupon;//0全部 1有券

    public Category(){}

    public Category(String categoryID,String categoryName,String parentCategoryID){
        this.categoryName = categoryName;
        this.categoryID = categoryID;
        this.parentCategoryID = parentCategoryID;
    }
    public Category(String categoryID ,String categoryName, String parentCategoryID,String minRebate, String maxRebate
            , String minPrice, String maxPrice, int pType, String keyWords,String sort_method, String order, int isCoupon){
        this.categoryName = categoryName;
        this.categoryID = categoryID;
        this.parentCategoryID = parentCategoryID;
        this.keyWords = keyWords;
        this.pType = pType;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minRebate = minRebate;
        this.maxRebate = maxRebate;
        this.sort_method = sort_method;
        this.sort_order = order;
        this.isCoupon = isCoupon;
    }
    public String getPTypeString () {
        String type;
        switch (pType){
            case 0:
                type = "全部";
                break;
            case 1:
                type = "今日上新";
                break;
            case 2:
                type = "超级秒杀";
                break;
            case 3:
                type = "高返利推荐";
                break;
            case 4:
                type = "九块九专区";
                break;
            case 5:
                type = "淘金店主";
                break;
            case 6:
                type = "个人代理";
                break;
            default:
                type = "全部";
        }
        return type;
    }
    public boolean hasSetFilter () {
        if ((minRebate == null || minRebate.equals("0")) &&
                (maxRebate == null || maxRebate.equals("0")) &&
                (minPrice == null || minPrice.equals("0")) &&
                (maxPrice == null || maxPrice.equals("0")))  {
            return false;
        } else
            return true;
    }
    public String getMinPrice() {
        if (minPrice == null || minPrice.length() == 0) {
            return "0";
        } else
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPrice() {
        if (maxPrice == null || maxPrice.length() == 0) {
            return "0";
        } else
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getMinRebate() {
        if (minRebate == null || minRebate.length() == 0) {
            return "0";
        } else
        return minRebate;
    }

    public void setMinRebate(String minRebate) {

        this.minRebate = minRebate;
    }

    public String getMaxRebate() {
        if (maxRebate == null || maxRebate.length() == 0) {
            return "0";
        } else
        return maxRebate;
    }

    public void setMaxRebate(String maxRebate) {
        this.maxRebate = maxRebate;
    }

    public int getpType() {
        return pType;
    }

    public void setpType(int pType) {
        this.pType = pType;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public String getSort_method() {
        if (sort_method == null) {
            sort_method = Filter.FilterDefault;
        }
        return sort_method;
    }

    public void setSort_method(String sort_method) {

        this.sort_method = sort_method;
    }

    public String getSort_order() {
        if (sort_order == null) {
            sort_order = Filter.SortOrderDesc;
        }
        return sort_order;
    }

    public void setSort_order(String sort_order) {
        this.sort_order = sort_order;
    }

    public int getIsCoupon() {
        return isCoupon;
    }

    public void setIsCoupon(int isCoupon) {
        this.isCoupon = isCoupon;
    }

    public String getParentCategoryID() {
        if (parentCategoryID == null) {
            parentCategoryID = AllCategoryID;
        }
        return parentCategoryID;
    }

    public void setParentCategoryID(String parentCategoryID) {
        if (parentCategoryID == null) {
            parentCategoryID = AllCategoryID;
        }
        this.parentCategoryID = parentCategoryID;
    }

    public String getCategoryName() {
        if (categoryName == null || categoryName.length() == 0) {
            categoryName = "全部";
        }
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryID() {
        if (categoryID == null) {
            categoryID = AllCategoryID;
        }
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        if (categoryID == null) {
            categoryID = AllCategoryID;
        }
        this.categoryID = categoryID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(categoryID);
        dest.writeString(categoryName);
        dest.writeString(parentCategoryID);
        dest.writeString(minPrice);
        dest.writeString(maxPrice);
        dest.writeString(minRebate);
        dest.writeString(maxRebate);
        dest.writeInt(pType);
        dest.writeString(keyWords);
        dest.writeString(sort_method);
        dest.writeString(sort_order);
        dest.writeInt(isCoupon);

    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readInt(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readInt());
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[0];
        }
    };
}
