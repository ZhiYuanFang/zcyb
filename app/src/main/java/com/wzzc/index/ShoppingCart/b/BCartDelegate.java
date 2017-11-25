package com.wzzc.index.ShoppingCart.b;

/**
 * Created by by Administrator on 2017/6/19.
 */

public interface BCartDelegate {
    void delete (String rec_id) ;
    void change (String rec_id,int number , int price);
}
