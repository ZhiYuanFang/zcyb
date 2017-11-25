package com.wzzc.index.home.h;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;

import com.baidu.location.BDLocation;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.base.new_base.NewBaseActivity;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.FileInfo;
import com.wzzc.other_function.LocationClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by toutou on 2017/5/28..
 *
 */

public class NewNearbyActivity extends NewBaseActivity implements LocationDelegate {
    public static final String LOCATION = "location";
    boolean reloadLocation;
    @ViewInject(R.id.nnv)
    NewNearbyView nnv;

    @Override
    protected void init() {
        reGetAddress();
    }

    @Override
    protected String getFileKey() {
        return "NewNearbyActivity";
    }

    @Override
    protected CacheCallBack cacheCallBack() {
        return new CacheCallBack() {
            @Override
            public void callBack(Object obj, String s) {
                cb(obj, s);
//                newServerDataFromServer((JSONObject) obj, s);
            }
        };
    }

    @Override
    protected ServerCallBack serverCallBack() {
        return new ServerCallBack() {
            @Override
            public void callBack(Object obj, String s) {
                cb(obj, s);
            }
        };
    }

    private void cb(Object obj, String s) {
        switch (s) {
            case LOCATION:
                nnv.setInfo(this, (JSONObject) obj);
                break;
            default:
        }
    }

    @Override
    protected void newServerDataFromServer(JSONObject sender, String s) {
        super.newServerDataFromServer(sender, s);
        switch (s) {
            case LOCATION:
                nnv.getAdapter().removeDataFromFirst();
                AsynServer.wantShowDialog = false;
                break;
            default:
        }
    }

    @Override
    protected void publish() {
        reGetAddress();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LocationClass.MY_PERMISSIONS_LOCATION:
                if (resultCode == RESULT_OK) {
                    Default.showToast("开启");
                    reGetAddress();
                } else {
                    Default.showToast("拒绝");
                }
                break;
            default:
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //权限申请结果
        for (int index = 0; index < permissions.length; index++) {
            switch (permissions[index]) {
                case Manifest.permission.ACCESS_FINE_LOCATION:
                    if (requestCode == LocationClass.MY_PERMISSIONS_LOCATION) {
                        if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                            /**用户已经受权*/
                            Default.showToast("开启");
                            reGetAddress();
                        } else if (grantResults[index] == PackageManager.PERMISSION_DENIED) {
                            /**用户拒绝了权限*/
                            Default.showToast("拒绝");
                        } else {
                            /**用户应该拒绝了权限*/
                            Default.showToast("拒绝");
                        }
                    }
                    break;
                default:
            }
        }
    }

    @Override
    public void showNear(final String key, final String country_id, final String province_id, final String city_id, final String district_id, final String type) {
        if (LocationClass.getLocationPermission(this)) {
            switch (LocationClass.isOpenGPS(this)) {
                case 0:
                case 1:
                    if (Default.isConnect(this)) {
                        LocationClass.findLocation(NewNearbyActivity.this,new LocationClass.LocationBack() {
                            @Override
                            public void backOK(final BDLocation bdLocation) {
                                initDataFromCache(LOCATION);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        JSONObject para = new JSONObject();
                                        try {
                                            para.put("keywords", key);
                                            para.put("longitude", bdLocation.getLongitude());
                                            para.put("latitude", bdLocation.getLatitude());
                                            para.put("country_id", country_id);
                                            para.put("province_id", province_id);
                                            para.put("city_id", city_id);
                                            para.put("district_id", district_id);
                                            para.put("id", type);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        initDataFromServer(nnv, "search_stores", !FileInfo.IsAtUserString(getFileKey(),NewNearbyActivity.this), para, nnv.getListView(), LOCATION);
                                    }
                                }, 1);
                            }


                            @Override
                            public void backError(String error) {
                                if(Default.debug){
                                    MessageBox.createNewDialog();
                                    MessageBox.Show(NewNearbyActivity.this, "定位失败", error, new String[]{"返回"}, new MessageBox.MessBtnBack() {
                                        @Override
                                        public void Back(int index) {
                                            onBackPressed();
                                        }
                                    });
                                    MessageBox.setDialogCancelable(false);
                                }
                                JSONObject para = new JSONObject();
                                try {
                                    para.put("keywords", key);
                                    para.put("longitude", "120.599554");
                                    para.put("latitude", "28.025525");
                                    para.put("country_id", country_id);
                                    para.put("province_id", province_id);
                                    para.put("city_id", city_id);
                                    para.put("district_id", district_id);
                                    para.put("id", type);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                initDataFromServer(nnv, "search_stores", !FileInfo.IsAtUserString(getFileKey(),NewNearbyActivity.this), para, nnv.getListView(), LOCATION);
                            }
                        },true);
                    } else {
                        initDataFromCache(LOCATION);
                        if (FileInfo.IsAtUserString(getFileKey(), this)) {
                            Default.showToast("NoNet!");
                        } else
                            showNoNetView();
                    }
                    break;
                case -1:
                    Default.showToast("定位失败");
                    //region 未开启网络
                    MessageBox.Show(this, "定位失败", "前往定位服务并开启WLAN/移动网络定位", new String[]{"取消定位", "重新定位", "立即前往"}, new MessageBox.MessBtnBack() {
                        @Override
                        public void Back(int index) {
                            switch (index) {
                                case 0:
                                    onBackPressed();
                                    break;
                                case 1:
                                    MessageBox.dismiss();
                                    showNear("", country_id, province_id, city_id, district_id, type);
                                        
                                    break;
                                case 2:
                                    reloadLocation = true;
                                        
                                    LocationClass.goOpenGPS(NewNearbyActivity.this);
                                    break;
                                default:
                            }
                        }
                    });
                    MessageBox.dialog.setCancelable(false);
                    //endregion
                    break;
                default:
            }
        } 
    }

    public void reGetAddress() {
        showNear("", null, null, null, null, null);
    }
}
