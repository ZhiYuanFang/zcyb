package com.wzzc.index.classification;

import org.json.JSONArray;

/**
 * Created by by Administrator on 2017/7/22.
 */

public class Classify {
    public String id;
    public String name;
    public JSONArray cat_id;
    public boolean focus;
    Classify () {}
    Classify (String id , String name , JSONArray cat_id) {
        this.cat_id = cat_id;
        this.name = name;
        this.id = id;
    }


}
