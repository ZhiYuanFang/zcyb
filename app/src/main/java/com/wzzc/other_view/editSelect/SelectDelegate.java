package com.wzzc.other_view.editSelect;

/**
 * Created by by Administrator on 2017/7/7.
 */

public interface SelectDelegate {
    void changeSelectList (String str);
    void itemClick (String str);
    void closeSelect();
    void hasFocus(boolean hasFocus);
}
