package com.wzzc.NextSuperDeliver.main_view.a;

import com.wzzc.NextSuperDeliver.Production;
import com.wzzc.NextSuperDeliver.list.Category;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/8/21.
 */

public interface SuperDeliverHomeDelegate {
    void search (Category category);
    void showSelectFragment (ArrayList<Category> categories);
    void getCoupons (Production production);
}
