package com.wzzc.other_view.SlideView;

import android.view.View;

import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/8/5.
 */

public interface SlideDelegate {
    void clickItem (Integer clickPosition , View clickView, JSONObject json_item);//某一项被点击触发事件

}
