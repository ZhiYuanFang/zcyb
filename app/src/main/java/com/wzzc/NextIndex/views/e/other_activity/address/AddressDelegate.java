package com.wzzc.NextIndex.views.e.other_activity.address;

/**
 * Created by by Administrator on 2017/6/15.
 *
 */

public interface AddressDelegate {
    void deleteAddress (String addressID);
    void setDefaultAddress (String addressID);
    void editAddress (String addressID);
    void lookAddress (String addressID);
    void addAddress ();
}
