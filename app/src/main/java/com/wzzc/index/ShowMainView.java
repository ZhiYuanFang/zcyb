package com.wzzc.index;

import android.os.Parcelable;
import android.view.View;


import com.wzzc.index.ShoppingCart.ShoppingCartActivity;

import java.io.Serializable;

/**
 * Created by toutou on 2017/4/17.
 */

public interface ShowMainView {
    void showMainView();
    void showView(View showView);
    void showView (ShoppingCartActivity myShoppingView, int type);
}
