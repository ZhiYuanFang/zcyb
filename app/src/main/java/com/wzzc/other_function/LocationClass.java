package com.wzzc.other_function;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.WindowManager;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;

/**
 * Created by by Administrator on 2017/6/26.
 *
 */

public class LocationClass {
    public static final int MY_PERMISSIONS_LOCATION = 10011;
    private static LocationClient mLocationClient;
    private static BDLocationListener mBDLocationListener;
    private static LocationBack myLocationBack;
    private static ProgressDialog progressDialog;

    /* 获取定位权限*/
    public static boolean getLocationPermission(Context c) {
        if (ContextCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //请求权限
            ActivityCompat.requestPermissions((Activity) c, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_LOCATION);
            //判断是否需要 向用户解释，为什么要申请该权限
            ActivityCompat.shouldShowRequestPermissionRationale((Activity) c, Manifest.permission.READ_CONTACTS);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 是否开启定位
     *
     * @return 1 : AGPS 0 : GPS null : 未开启定位
     */

    public static int isOpenGPS(Context c) {
        LocationManager locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (network) {
            return 1;
        }

        if (gps) {
            return 0;
        }

        return -1;
    }

    public static void goOpenGPS(Context c) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        ((BaseActivity) c).startActivityForResult(intent, MY_PERMISSIONS_LOCATION);
    }

    public static void findLocation(Context c, LocationBack locationBack, boolean showDialog) {
        System.out.println("----------------findLocation-------------");
        myLocationBack = locationBack;
        if (Default.getActivity() != null) {
            if (mLocationClient == null){
                mLocationClient = new LocationClient(Default.getActivity());
                mBDLocationListener = new MyBDLocationListener();
            }
            getLocation(c, showDialog);
        } else {
            if (myLocationBack != null) {
                myLocationBack.backError("未运行");
            }
        }
    }

    /**
     * 获得所在位置经纬度及详细地址
     */
    private static void getLocation(Context c, boolean showDialog) {
        if (showDialog)
            showProgressDialog(c);
        // 声明定位参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式 高精度
        option.setPriority(LocationClientOption.NetWorkFirst); // 设置网络优先
        option.setCoorType("bd09ll");// 设置返回定位结果是百度经纬度 默认gcj02
        option.setScanSpan(2000);// 设置发起定位请求的时间间隔 单位ms
        option.setIsNeedAddress(true);// 设置定位结果包含地址信息
        option.setProdName("ZcbyLocation");	//设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
        option.setNeedDeviceDirect(true);// 设置定位结果包含手机机头 的方向
        option.setIgnoreKillProcess(true);
        option.setTimeOut(10000);
        // 设置定位参数
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(mBDLocationListener);
        // 启动定位
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
    }

    public static void stopConnection (){
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
    }

    private static class MyBDLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(final BDLocation location) {
            dismissProgressDialog();
            Default.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 非空判断
                    if (location != null) {
                        // 根据BDLocation 对象获得经纬度以及详细地址信息
                        if (location.getLocType() == BDLocation.TypeGpsLocation) {
//                            Default.showToast("GPS");
                            if (myLocationBack != null) {
                                myLocationBack.backOK(location);
                            }
                        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
//                            Default.showToast("NetWork");
                            if (myLocationBack != null) {
                                myLocationBack.backOK(location);
                            }
                        } else if (location.getLocType() == BDLocation.TypeNetWorkException){
                            if (myLocationBack != null) {
                                myLocationBack.backError("网络不给力导致定位失败，请检查网络是否通畅！");
                            }
                        }else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                            if (myLocationBack != null) {
                                myLocationBack.backError("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机！");
                            }
                        } else {
                            if (myLocationBack != null) {
                                myLocationBack.backError("未知错误！-- > " + location.getLocType());
                            }
                        }
                        // 获得位置之后停止定位
                        mLocationClient.stop();
                        mLocationClient = null;
                    } else {
                        if (myLocationBack != null) {
                            myLocationBack.backError("定位失败，请稍后重试！");
                        }
                    }
                }
            });
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    public interface LocationBack {
        void backOK(BDLocation bdLocation);

        void backError(String error);
    }

    private static void showProgressDialog(Context c) {
        if (c != null) {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(c);
            }
            progressDialog.setMessage("获取定位信息");
            if (!progressDialog.isShowing()) {
                try {
                    Thread.sleep(400);
                    progressDialog.show();
                } catch (WindowManager.BadTokenException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
