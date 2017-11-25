package com.wzzc.other_function;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.wzzc.base.Default;

/**
 * Created by by Administrator on 2017/4/25.
 */

public class CallPhone {
    private static final int MY_PERMISSIONS_CALL_PHNOE = 10012;
    public static final String KEFU = "4008888986";
    public static void call(final String phone) {
        Default.showToast("请开启拨打权限", Toast.LENGTH_LONG);
        if (canCallPhone()) {
            MessageBox.Show(Default.getActivity(), Default.AppName, "您将呼叫 : " + KEFU, new String[]{"取消", "确定"}, new MessageBox.MessBtnBack() {
                @Override
                public void Back(int index) {
                    switch (index){
                        case 0:
                            break;
                        case 1:
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                            if (ActivityCompat.checkSelfPermission(Default.getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            Default.getActivity().startActivity(intent);
                            break;
                        default:
                    }
                }
            });
        }
    }

    private static boolean canCallPhone() {
        if (ContextCompat.checkSelfPermission(Default.getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //请求权限
            ActivityCompat.requestPermissions(Default.getActivity(), new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_CALL_PHNOE);
            //判断是否需要 向用户解释，为什么要申请该权限
            ActivityCompat.shouldShowRequestPermissionRationale(Default.getActivity(),Manifest.permission.READ_CONTACTS);
            return false;
        } else {
            return true;
        }
    }

}
