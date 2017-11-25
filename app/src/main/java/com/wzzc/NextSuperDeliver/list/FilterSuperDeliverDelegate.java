package com.wzzc.NextSuperDeliver.list;

import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/8/8.
 */

public interface FilterSuperDeliverDelegate extends Parcelable{
    /**
     * @param category 分类
     */
    void filter(Category category);//筛选
    void specialFilterChild (Category category ,BackChildList backChildList ) ;
    void specialFilterMain (Category category ,BackMainList backMainList ) ;

    public interface BackChildList {
        void backCatParentInfo (JSONObject json) ;
        void backChildSelect(JSONArray jrr);
    }
    public interface BackMainList {
        void backMainSelect(JSONArray jrr);
    }
}
