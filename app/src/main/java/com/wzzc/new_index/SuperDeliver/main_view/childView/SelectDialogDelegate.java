package com.wzzc.new_index.SuperDeliver.main_view.childView;

import android.os.Parcelable;

import com.wzzc.NextSuperDeliver.list.Category;

/**
 * Created by by Administrator on 2017/5/4.
 *
 */

public interface SelectDialogDelegate extends Parcelable{
    void clickOK (Category category);
    void clickCancel () ;
}
