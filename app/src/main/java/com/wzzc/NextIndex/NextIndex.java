package com.wzzc.NextIndex;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.wzzc.NextIndex.saomiao.SaomiaoActivity;
import com.wzzc.NextIndex.views.B;
import com.wzzc.NextIndex.views.C;
import com.wzzc.NextIndex.views.D;
import com.wzzc.NextIndex.views.a.Home;
import com.wzzc.NextIndex.views.e.LoginActivity;
import com.wzzc.NextIndex.views.e.LoginDelegate;
import com.wzzc.NextIndex.views.e.User;
import com.wzzc.NextIndex.views.e.UserCenter;
import com.wzzc.NextTBSearch.main_view.TBrebackListActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.base.new_base.NewBaseActivity;
import com.wzzc.index.PhotoDelegate;
import com.wzzc.other_function.CallPhone;
import com.wzzc.other_function.FileInfo;
import com.wzzc.other_function.ImageHelper;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.LocationClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.other_function.action.ClickDelegate;
import com.wzzc.other_function.action.ItemClick;
import com.wzzc.other_function.jpush.beans.ExampleUtil;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.other_view.fragment.SelectFragment;
import com.wzzc.taobao.TaoBao;
import com.wzzc.welcome.JavaBean.GetServerUrl;
import com.wzzc.welcome.JavaBean.UpdateInfo;
import com.wzzc.zcyb365.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.os.Environment.getExternalStorageDirectory;

/**
 * Created by by Administrator on 2017/8/5.
 */
@ContentView(R.layout.a_activity)
public class NextIndex extends NewBaseActivity implements NextDelegate, ClickDelegate, LoginDelegate, PhotoDelegate, Serializable {
    public static final String USER_CENTER = "user_center";
    public static final String INDEX_HOME = "index_new";
    public static final String EXSHOP_CART = "exshop_cart";
    public static final String HELP = "help";
    public static final int MY_PERMISSIONS_EXTERNAL_STORAGE = 10010;
    public static final int ImageFromFile = 10023;
    public static final int ImageFromCamera = 10024;
    private static NextIndex nextIndex;
    @ViewInject(R.id.contain_view)
    RelativeLayout contain_view;//承载当前控制器内所有布局
    //region 底部按钮集合
    @ViewInject(R.id.bt_home)
    Button bt_home;
    @ViewInject(R.id.bt_call)
    Button bt_call;
    @ViewInject(R.id.bt_help)
    Button bt_help;
    @ViewInject(R.id.bt_cart)
    Button bt_cart;
    @ViewInject(R.id.bt_center)
    Button bt_center;
    //endregion
    ArrayList<Button> buttonArrayList;//底部按钮集合
    //region 所有界面(5)
    Home home;
    B b;
    C c;
    D d;
    UserCenter userCenter;
    //endregion
    ArrayList<View> viewArrayList;//可视化页面集合
    int currentItem;//当前点击的项目
    String loadUrl;//新版本下载路径
    View clickView;
    boolean confirmOut;//确认退出

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User.getUser(this);
        showActivityDialog();
        registerMessageReceiver();
        checkVersion(new CheckVersionEnd() {
            @Override
            public void end(final String loadUrl, String current_versionNum, String new_versionNum, boolean needPublish, String updateInfo) {
                NextIndex.this.loadUrl = loadUrl;
                if (needPublish) {
                    IndexDialog.showUpdateNotificationDialog(NextIndex.this, "请升级APP至版本" + new_versionNum, updateInfo,
                            new String[]{"取消", "确定"}, new MessageBox.MessBtnBack() {
                                @Override
                                public void Back(int index) {
                                    switch (index) {
                                        case 0:
                                            MessageBox.dismiss();
                                            break;
                                        case 1:
                                            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                                if (ContextCompat.checkSelfPermission(NextIndex.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                                    //请求权限
                                                    ActivityCompat.requestPermissions(NextIndex.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_EXTERNAL_STORAGE);
                                                    //判断是否需要 向用户解释，为什么要申请该权限
                                                    ActivityCompat.shouldShowRequestPermissionRationale(NextIndex.this, Manifest.permission.READ_CONTACTS);
                                                    break;
                                                } else {
                                                    downFile(loadUrl);
                                                }
                                            } else {
                                                Default.showToast("请提供写入权限更新", Toast.LENGTH_LONG);
                                            }
                                            break;
                                        default:
                                    }
                                }
                            });
                }
            }

            @Override
            public void error(String error_message) {
                Default.showToast(error_message);
            }
        });
        nextIndex = this;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    /** 激活对话框*/
    protected void showActivityDialog() {
        if (User.isLogin() && !User.is_activate() && User.isShowActivity(Default.getActivity()) && User.getActivity_msg().length() > 0) {
            MessageBox.Show(Default.getActivity(), Default.AppName, User.getActivity_msg(), new String[]{User.getActivity_btn_cancel(), User.getActivity_check_text(),
                    User.getActivity_btn_goActivity()}, new MessageBox.MessBtnBack() {
                @Override
                public void Back(int index) {
                    switch (index) {
                        case 0:
                            break;
                        case 1:
                            User.setShowActivity(Default.getActivity(), false);
                            break;
                        case 2:
                            ItemClick.switchNormalListener(ItemClick.SuperDiscount_list_3, null);
                            break;
                        default:
                    }
                }
            });
        }
    }

    //region 下载文件
    private String m2(Double maxSize,Double currentSize) {
        DecimalFormat df = new DecimalFormat("0.00");
        String sdf = df.format(currentSize/maxSize * 100);
        int in = 0;
        try {
            in = Integer.valueOf(sdf.substring(0,sdf.length() - 3));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return in + "%";
    }

    private Handler downHandler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            Double[] doubles = (Double[]) msg.obj;
            IndexDialog.showUpdateProgressDialog(NextIndex.this,doubles[0],doubles[1]);
        }
    };

    //安装文件，一般固定写法
    protected void installation() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            Uri contentUri = FileProvider.getUriForFile(this, "com.wzzc.zcyb365.fileprovider", new File(getExternalStorageDirectory(), "zcyb.apk"));
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(getExternalStorageDirectory(), "zcyb.apk")), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }
    @Override
    public void downFile(final String loadUrl) {
        new Thread() {
            public void run() {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(loadUrl);
                get.setHeader("Accept-Encoding", "identity");
                HttpResponse response;
                try {
                    response = client.execute(get);
                    HttpEntity entity = response.getEntity();
                    final int length = (int) entity.getContentLength();
                    if (length <= 0) {
                        NextIndex.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Default.showToast("文件解析失败!");
                            }
                        });
                        return;
                    }
                    InputStream is = entity.getContent();
                    if (is != null) {
                        File file = new File(getExternalStorageDirectory(), "zcyb.apk");
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        byte[] buf = new byte[1024];
                        int ch;
                        int process = 0;
                        while ((ch = is.read(buf)) != -1) {
                            fileOutputStream.write(buf, 0, ch);
                            process += ch;
                            Message msg = new Message();
                            msg.obj = new Double[]{Double.valueOf(length), Double.valueOf(process)};
                            downHandler.handleMessage(msg);
                            if (process % 17 == 0) {
                                try {
                                    Thread.sleep(1);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        IndexDialog.dismissDialog(NextIndex.this);
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        installation();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    final String error = "请确保网络连接正常后重试";
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            IndexDialog.dismissDialog(NextIndex.this);
                            MessageBox.createNewDialog();
                            MessageBox.Show("更新失败", error, new String[]{"确定"}, new MessageBox.MessBtnBack() {
                                @Override
                                public void Back(int index) {
                                    MessageBox.dismiss();
                                }
                            });
                        }
                    });
                }
            }
        }.start();
    }

    @Override
    public void checkVersion(final CheckVersionEnd checkVersionEnd) {
        if (Default.isConnect(this)) {
            Default.readParse(GetServerUrl.getUrl(), new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (msg.arg1 == -1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                checkVersionEnd.error("服务器繁忙，请稍后再试！");
                            }
                        });
                    } else {
                        try {
                            JSONObject myJsonObject = new JSONObject(msg.obj.toString());
                            final UpdateInfo info = new UpdateInfo(myJsonObject.getString("version"),
                                    myJsonObject.getString("title"), myJsonObject.getString("url"));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String[] versionNew = info.getVersion().split("\\.");
                                    String[] versionOld = Default.getVersion(NextIndex.this).split("\\.");
                                    for (int i = 0; i < versionNew.length; i++) {
                                        if (versionOld.length > i) {
                                            if (Integer.valueOf(versionOld[i]) < Integer.valueOf(versionNew[i])) {
                                                checkVersionEnd.end(info.getUrl(), Default.getVersion(NextIndex.this), info.getVersion(), true, info.getDescription());
                                                break;
                                            }
                                            if (Integer.valueOf(versionOld[i]) > Integer.valueOf(versionNew[i])) {
                                                checkVersionEnd.end(info.getUrl(), Default.getVersion(NextIndex.this), info.getVersion(), false, info.getDescription());
                                                break;
                                            }
                                        } else {
                                            checkVersionEnd.end(info.getUrl(), Default.getVersion(NextIndex.this), info.getVersion(), true, info.getDescription());
                                            break;
                                        }
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    return false;
                }
            });
        } else {
            checkVersionEnd.error(getString(R.string.aliusersdk_network_error));
        }
    }
    //endregion

    @Override
    protected void init() {
        //region 初始化底部按钮集合
        if (buttonArrayList == null) {
            buttonArrayList = new ArrayList<Button>() {{
                add(bt_home);
                add(bt_call);
                add(bt_help);
                add(bt_cart);
                add(bt_center);
            }};
        }

        for (Button bt : buttonArrayList) {
            bt.setOnClickListener(this);
        }
        //endregion

        //region 初始化所有可视化页面
        home = new Home(this);
        b = new B(this);
        c = new C(this);
        d = new D(this);
        userCenter = new UserCenter(this);
        userCenter.nextDelegate = this;

        viewArrayList = new ArrayList<View>() {{
            add(home);
            add(b);
            add(c);
            add(d);
            add(userCenter);

        }};

        for (View v : viewArrayList) {
            contain_view.addView(v);
        }
        //endregion

        bt_home.callOnClick();
    }

    @Override
    protected String getFileKey() {
        return "A" + currentItem;
    }

    @Override
    protected CacheCallBack cacheCallBack() {
        return new CacheCallBack() {
            @Override
            public void callBack(Object obj, String s) {
                cb(obj, s);
            }
        };
    }

    @Override
    protected ServerCallBack serverCallBack() {
        confirmOut = false;
        return new ServerCallBack() {
            @Override
            public void callBack(Object obj, String s) {
                cb(obj, s);
            }
        };
    }

    private void cb(Object obj, String s) {
        switch (s) {
            case INDEX_HOME:
                home.setInfo(this, this, (JSONObject) obj);
                break;
            case HELP:
                c.setInfo((String) obj);
                break;
            case EXSHOP_CART:
                d.setInfo((JSONObject) obj);
                break;
            case USER_CENTER:
                System.out.println("all views : " + viewArrayList.toString());
                userCenter.setInfo(this, this, this, (JSONObject) obj);
                break;
            default:
        }
    }

    @Override
    protected void publish() {
        switch (currentItem) {
            case 0:
                showA();
                break;
            case 1:
                showB();
                break;
            case 2:
                showC();
                break;
            case 3:
                showD();
                break;
            case 4:
                showE();
                break;
            default:
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_home:
                initializedBottomButton();
                focusBottomButton(bt_home);
                sideAllViews();
                showView(home);
                showA();
                break;
            case R.id.bt_call:
                initializedBottomButton();
                focusBottomButton(bt_call);
//                sideAllViews();
//                showView(b);
                showB();
                break;
            case R.id.bt_help:
                initializedBottomButton();
                focusBottomButton(bt_help);
                sideAllViews();
                showView(c);
                showC();
                break;
            case R.id.bt_cart:
                initializedBottomButton();
                focusBottomButton(bt_cart);
                sideAllViews();
                showView(d);
                showD();
                break;
            case R.id.bt_center:
                initializedBottomButton();
                focusBottomButton(bt_center);
                sideAllViews();
                showView(userCenter);
                showE();
                break;
            default:
        }
    }

    //region NextDelegate
    @Override
    public void showA() {
        currentItem = 0;
        if (FileInfo.IsAtUserString(getFileKey(), this)) {
            initDataFromCache(INDEX_HOME);
        } else {
            initDataFromServer(null, "index_new", true, new JSONObject(), null, INDEX_HOME);
        }
    }

    @Override
    public void showB() {
        currentItem = 1;
        CallPhone.call(CallPhone.KEFU);
    }

    @Override
    public void showC() {
        currentItem = 2;
        c.setInfo("http://www.zcyb365.com/mobile/help.php");
    }

    @Override
    public void showD() {
        currentItem = 3;
        initDataFromCache(EXSHOP_CART);
        initDataFromServer(null, "excart/list", !FileInfo.IsAtUserString(getFileKey(), this), new JSONObject(), null, EXSHOP_CART);
    }

    @Override
    public void showE() {
        currentItem = 4;
        if (User.isLogin()) {
            initDataFromCache(USER_CENTER);
            initDataFromServer(null, "user/info", !FileInfo.IsAtUserString(getFileKey(), this), new JSONObject(), null, USER_CENTER);
        } else {
            MessageBox.Show(this, Default.AppName, "尚未登陆", new String[]{"继续逛逛", "前往登陆"}, new MessageBox.MessBtnBack() {
                @Override
                public void Back(int index) {
                    switch (index) {
                        case 1:
                            login();
                            break;
                        default:
                    }
                }
            });
        }

    }

    @Override
    public void scanForTDCode() {
        //region 扫描
        if (ContextCompat.checkSelfPermission(Default.getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //请求权限
            ActivityCompat.requestPermissions(Default.getActivity(), new String[]{Manifest.permission.CAMERA}, SaomiaoActivity.MY_PERMISSIONS_CAMERA);
            //判断是否需要 向用户解释，为什么要申请该权限
            ActivityCompat.shouldShowRequestPermissionRationale(Default.getActivity(), Manifest.permission.READ_CONTACTS);
        } else {
            AddActivity(SaomiaoActivity.class);
        }
        //endregion
    }

    @Override
    public void searchProduction(String keyWords) {
        Intent intent = new Intent();
        intent.putExtra(TBrebackListActivity.KeyWords, keyWords);
        AddActivity(TBrebackListActivity.class, 0, intent);
    }

    @Override
    public void findCurrentLocation() {
        if (LocationClass.getLocationPermission(this)) {
            switch (LocationClass.isOpenGPS(this)) {
                case 1:
                case 0:
                    LocationClass.findLocation(this, new LocationClass.LocationBack() {
                        @Override
                        public void backOK(BDLocation bdLocation) {
                            Geocoder gc = new Geocoder(NextIndex.this, Locale.getDefault());
                            try {
                                List<Address> results = gc.getFromLocation(bdLocation.getLatitude(), bdLocation.getLongitude(), 1);
                                if (results != null) {
                                    home.setAddress(results.get(0).getLocality());
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void backError(String error) {

                        }
                    }, false);
                    break;
                case -1:
                    Default.showToast("定位失败");

                    break;
                default:
            }
        }
    }

    @Override
    public void changeLocation() {
        // TODO: 2017/8/5 change location
    }

    @Override
    public void showAdvertising(JSONObject json_pop) {
        final Dialog dialog = new Dialog(this, R.style.CustomAlertDialog);
        dialog.show();
        View view = LayoutInflater.from(this).inflate(R.layout.notifydialog, null);
        ExtendImageView eiv = (ExtendImageView) view.findViewById(R.id.eiv_photo);
        ImageButton ib_close = (ImageButton) view.findViewById(R.id.imageButton);
        eiv.setPath(JsonClass.sj(json_pop, "ad_code"));
        eiv.setOnClickListener(ItemClick.listener(this, JsonClass.sj(json_pop, "data_type"), JsonClass.sj(json_pop, "ad_link"), JsonClass.sj(json_pop, "num_iid")));
        eiv.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ib_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.height = Default.dip2px(300, this);
        dialog.getWindow().setAttributes(params);
    }

    @Override
    public void login() {
        this.clickView = null;
        Intent intent = new Intent();
        intent.putExtra(LoginActivity.LoginDelegate, (Parcelable) this);
        AddActivity(LoginActivity.class, 0, intent);
    }
    //endregion

    //region LoginDelegate
    @Override
    public void loginOK() {
        //登陆成功 重新显示个人中心界面e .
        if (clickView == null) {
            showE();
        } else {
            clickView.callOnClick();
        }
    }
    //endregion

    //region PhotoDelegate
    @Override
    public void changePhoto() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.push_bottom_in, R.anim.push_bottom_out);
        SelectFragment selectFragment = new SelectFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray(SelectFragment.ITEM_INFOS, new String[]{"相册", "拍照"});
        bundle.putParcelable(SelectFragment.ITEM_CallBack, new SelectFragment.SelectCallBack() {
            @Override
            public void call(int index) {
                switch (index) {
                    case 0: {
                        if (ContextCompat.checkSelfPermission(Default.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            //请求权限
                            ActivityCompat.requestPermissions(Default.getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, ImageFromFile);
                            //判断是否需要 向用户解释，为什么要申请该权限
                            ActivityCompat.shouldShowRequestPermissionRationale(Default.getActivity(), Manifest.permission.READ_CONTACTS);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent, ImageFromFile);
                        }
                        break;
                    }
                    case 1: {
                        if (ContextCompat.checkSelfPermission(Default.getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            //请求权限
                            ActivityCompat.requestPermissions(Default.getActivity(), new String[]{Manifest.permission.CAMERA}, ImageFromCamera);
                            //判断是否需要 向用户解释，为什么要申请该权限
                            ActivityCompat.shouldShowRequestPermissionRationale(Default.getActivity(), Manifest.permission.READ_CONTACTS);
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, ImageFromCamera);
                        }
                        break;
                    }
                    default:
                }
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {

            }
        });
        selectFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.contain_select, selectFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        hasFragment = true;
    }

    @Override
    public void setPhoto(Bitmap bitmap) {
        try {
            userCenter.setPhoto(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            Default.showToast("请更换其他图片");
        }
    }
    //endregion

    //region ClickDelegate
    @Override
    public void shouldLogin(View clickView) {
        this.clickView = clickView;
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(LoginActivity.LoginDelegate, (Parcelable) this);
        Default.toClass(this, intent);
    }
    //endregion

    //region helper

    /**
     * 初始化所有底部按钮组件
     */
    private void initializedBottomButton() {
        for (Button bt : buttonArrayList) {
            bt.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        }
    }

    /**
     * <b>应当先调用{@link NextIndex#initializedBottomButton()}初始化所有组件</b>
     * 聚焦当前选择的组件
     */
    private void focusBottomButton(Button bt) {
        bt.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_hasOK));
    }

    /**
     * 隐藏所有界面
     */
    private void sideAllViews() {
        home.setVisibility(View.GONE);
        b.setVisibility(View.GONE);
        c.setVisibility(View.GONE);
        d.setVisibility(View.GONE);
        userCenter.setVisibility(View.GONE);
    }

    /**
     * 显示该界面
     */
    private void showView(View v) {
        v.setVisibility(View.VISIBLE);
    }

    //endregion

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < permissions.length; i++) {
            String str = permissions[i];
            switch (str) {
                case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                    if (requestCode == MY_PERMISSIONS_EXTERNAL_STORAGE) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            /*用户已经受权*/
                            downFile(loadUrl);
                        } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            /*用户拒绝了权限*/
                            Default.showToast("已拒绝文件写入权限");
                        } else {
                            /*用户应该拒绝了权限*/
                            Default.showToast("无效授权");
                        }
                    }
                    break;
                case Manifest.permission.ACCESS_FINE_LOCATION:
                    if (requestCode == LocationClass.MY_PERMISSIONS_LOCATION) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            findCurrentLocation();
                        } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            Default.showToast("已拒绝定位权限");
                        } else {
                            /*用户应该拒绝了权限*/
                            Default.showToast("无效授权");
                        }
                    }
                    break;
                case Manifest.permission.READ_EXTERNAL_STORAGE:
                    if (requestCode == ImageFromFile) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            /*用户已经受权*/
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent, ImageFromFile);
                        } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            /*用户拒绝了权限*/
                            Default.showToast("已拒绝文件访问权限");
                        } else {
                            /*用户应该拒绝了权限*/
                            Default.showToast("无效授权");
                        }
                    }
                    break;
                case Manifest.permission.CAMERA:
                    if (requestCode == ImageFromCamera) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            /*用户已经受权*/
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, ImageFromCamera);
                        } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            /*用户拒绝了权限*/
                            Default.showToast("已拒绝拍照权限");
                        } else {
                            /*用户应该拒绝了权限*/
                            Default.showToast("无效授权");
                        }
                    }

                    if (requestCode == SaomiaoActivity.MY_PERMISSIONS_CAMERA) {
                        switch (grantResults[i]) {
                            case PackageManager.PERMISSION_GRANTED:
                                AddActivity(SaomiaoActivity.class);
                                break;
                            case PackageManager.PERMISSION_DENIED:
                                Default.showToast("已拒绝拍照权限");
                                break;
                            default:
                                Default.showToast("无效授权");
                        }
                    }
                    break;
                default:
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LocationClass.MY_PERMISSIONS_LOCATION:
                if (resultCode == RESULT_OK) {
                    Default.showToast("开启");
                    changeLocation();
                } else {
                    Default.showToast("拒绝");
                }
                break;
            case MY_PERMISSIONS_EXTERNAL_STORAGE:
                downFile(loadUrl);
                break;
            case ImageFromFile:
            case ImageFromCamera:
                if (data == null) {
                    return;
                }
                Uri originalUri = data.getData();//获得图片的uri
                Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.headimg);//头像
                if (originalUri == null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        try {
                            bm = (Bitmap) bundle.get("data");
                        } catch (OutOfMemoryError e) {
                            e.printStackTrace();
                            Default.showToast("图片过大，请重新选择！");
                            return;
                        }
                    }
                } else {
                    ContentResolver cr = getContentResolver();
                    try {
                        bm = MediaStore.Images.Media.getBitmap(cr, originalUri);
                    } catch (IOException | SecurityException e) {
                        e.printStackTrace();
                    } catch (OutOfMemoryError outOfMemoryError){
                        outOfMemoryError.printStackTrace();
                        Default.showToast("图片过大，请重新选择！");
                        return;
                    }
                }
                setPhoto(bm);
                HashMap<String, Object> info = new HashMap<>();
                JSONObject para = new JSONObject();
                try {
                    para.put("session", Default.GetSession());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                info.put("json", para);
                info.put("img_src", bm);
                ImageHelper.upLoadImage(this, "head", "--", "mypxqcreated", "\r\n", info, Default.MainURL + "user/upload_avatar", getExternalStorageDirectory() + "/zc_head/", new ImageHelper.UpLoadListener() {});

                break;
            default:
        }
    }

    //region parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public NextIndex createFromParcel(Parcel source) {

            return nextIndex;
        }

        @Override
        public Object[] newArray(int size) {
            return null;
        }
    };
    //endregion

    //region JPush
    public static boolean isForeground = false;
    public static final String MESSAGE_RECEIVED_ACTION = "com.wzzc.other_function.jpush.receiver.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    MessageReceiver mMessageReceiver;

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String title = intent.getStringExtra(KEY_TITLE);
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_TITLE + " : ").append(title).append("\n");
                showMsg.append(KEY_MESSAGE + " : ").append(messge).append("\n");
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : ").append(extras).append("\n");
                }
                setCostomMsg(showMsg.toString());
            }
        }
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (!hasFragment) {
            if (confirmOut) {
                super.onBackPressed();
            } else {
                Default.showToast("再按一次退出"+ getString(R.string.app_name));
                confirmOut = true;
            }
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onDestroy() {
        if (mMessageReceiver != null) {
            unregisterReceiver(mMessageReceiver);
        }
        TaoBao.destroy();
        User.saveUser(this);
        super.onDestroy();

    }

    public void registerMessageReceiver() {
        if (mMessageReceiver != null) {
            unregisterReceiver(mMessageReceiver);
        }
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    private void setCostomMsg(String msg) {
        System.out.println("Jpush CostomMsg : " + msg);
    }

    //endregion
}
