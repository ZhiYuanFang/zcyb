package com.wzzc.NextIndex.saomiao;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;

import com.wzzc.NextIndex.views.e.User;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.NextIndex.saomiao.android.CaptureActivity;
import com.wzzc.index.home.zcybStores.ConvertActivity;
import com.wzzc.NextIndex.views.e.LoginActivity;
import com.wzzc.new_index.userCenter.regest.ordinary.RegisterActivity;
import com.wzzc.other_function.MessageBox;
import com.wzzc.zcyb365.R;

/**
 * Created by zcyb365 on 2016/11/28.
 */
public class SaomiaoActivity extends BaseActivity {
    private final static int SCANNIN_GREQUEST_CODE = 1;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private static final int REQUEST_CODE_SCAN = 0x0000;
    /**
     * 显示扫描结果
     */
    @ViewInject(R.id.resultTv)
    private TextView resultTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public static final int MY_PERMISSIONS_CAMERA = 10022;
    protected void viewFirstLoad() {

        if (ContextCompat.checkSelfPermission(Default.getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //请求权限
            ActivityCompat.requestPermissions(Default.getActivity(), new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_CAMERA);
            //判断是否需要 向用户解释，为什么要申请该权限
            ActivityCompat.shouldShowRequestPermissionRationale(Default.getActivity(),Manifest.permission.READ_CONTACTS);
        } else {
            scan();
        }


    }
    private void scan () {
        Intent intent = new Intent(SaomiaoActivity.this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }
    private static final int waitTime = 3;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //权限申请结果
        if (requestCode == MY_PERMISSIONS_CAMERA) {
            for (int index = 0; index < permissions.length; index++) {
                switch (permissions[index]) {
                    case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                        if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                            /**用户已经受权*/
                            scan();
                        } else if (grantResults[index] == PackageManager.PERMISSION_DENIED) {
                            /**用户拒绝了权限*/
                            Message msg = new Message();
                            msg.obj = waitTime;
                            handler.handleMessage(msg);
                        } else {
                            /**用户应该拒绝了权限*/
                            Message msg = new Message();
                            msg.obj = waitTime;
                            handler.handleMessage(msg);
                        }
                        break;
                    default:
                }
            }
        }
    }
    //region Handle
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            if (msg.obj.equals(waitTime)) {
                MessageBox.Show("扫描", "无法获取相机信息，" + msg.obj + "秒后即将退出", new String[]{"立刻退出"}, new MessageBox.MessBtnBack() {
                    @Override
                    public void Back(int index) {
                        BackActivity();
                    }
                });
            } else {
                MessageBox.changeMessage("无法获取相机信息，" + msg.obj + "秒后即将退出");
            }

            if (Integer.valueOf(msg.obj.toString()) > 0) {
                Message message = new Message();
                message.obj = Integer.valueOf(msg.obj.toString()) - 1 ;
                sendMessageDelayed(message , 1000);
            } else  {
                BackActivity();
            }
        }
    };
    //endregion


    public static final String ErWeiUrl = "http://www.zcyb365.com/s/";
    public static final String ShopsUrl = "http://www.zcyb365.com/mobile/supplier.php?go=exchange&suppId=";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                Log.d("zxcvbnm", content);
                //图片
//                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);
                resultTv.setText("解码结果： \n" + content);
                // String d=Uri.parse(String.valueOf(sender)).getQueryParameter("suppId");
                if (content.contains(ErWeiUrl)) {
                    //region 二维码
                    String ewCode = content.replace(ErWeiUrl,"");
                    Intent intent = new Intent(this, RegisterActivity.class);
                    intent.putExtra(RegisterActivity.EWCode , ewCode);
                    Default.toClass(this,intent);
                    //endregion
                }else if (content.contains(ShopsUrl)) {
                    //region 商铺推介
                    String dpID = content.replace(ShopsUrl,"");
                    if (User.isLogin()) {
                        Intent intent = new Intent();
                        intent.putExtra(ConvertActivity.DPId, dpID);
                        AddActivity(ConvertActivity.class, 0, intent);
                        finish();
                    } else {
                        Intent intent = new Intent(this, LoginActivity.class);
                        intent.putExtra(LoginActivity.DPId , dpID);
                        Default.toClass(this,intent);
                    }
                    //endregion
                } else {
                    Intent intent=new Intent();
                    intent.putExtra("url",content);
                    AddActivity(OtherWZActivity.class,0,intent);
                }
            }
        }
        finish();
    }
}

