package com.wzzc.other_view.fragment.address;

import android.os.Parcelable;

/**
 * Created by by Administrator on 2017/6/15.
 */

public interface AddressSelectDelegate extends Parcelable {
    void selectFinish (String[] address , String[] addressID);
    void dismissAddressFragment();
}
