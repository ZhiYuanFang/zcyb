package com.wzzc.base;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.Toast;

import com.wzzc.NextIndex.views.e.User;
import com.wzzc.other_function.FileInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;

/**
 * Created by mypxq on 16-7-16.
 *
 */
public class Default {
    public static String MainURL = "http://test.zcgj168.com/zcybapp/?version=3.3&from=1&url=/";
//    public static String MainURL = "https://www.zcyb365.com//zcybapp/?version=3.3&from=1&url=/";
    public static boolean debug = false;//上线的话需要改成false
    public static boolean useApp = false;//关于淘宝是否使用App打开
    public static String APPWebClient = "zcyb365";
    public static String APPWebNewURL = "zcyb365newurl";
    public static String APPWebHTML = "zcyb365html";
    public static String AppName = getApplicationName(getActivity());
    public static int ScreenWidth = Default.px2dip(getScreenWidth(getActivity()), getActivity());
    public static int ScreenHeight = Default.px2dip(getScreenHeight(getActivity()), getActivity());
    public static String ProjectName = getProjectName(getActivity());
    public static float ScreenScale = getScale(getActivity());
    public static int ScreenSizeWidth = getScreenWidth(getActivity());
    public static int ScreenSizeHeight = getScreenHeight(getActivity());
    public static boolean firstComing;
    public static int delayTime = 200;//经多次实验得到的最佳延迟毫秒
    private static Toast toast;

    /**
     * 悬浮框管理
     */
    public static void showToast(final String str, final Integer duration) {
        if (getActivity() == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (toast == null) {
                    toast = Toast.makeText(getActivity(), str, duration);
                } else {
                    toast.setDuration(duration);
                    toast.setText(str);
                }

                if (!toast.getView().isShown()) {
                    toast.show();
                }
            }
        });
    }

    /**
     * 启动到应用商店app详情界面
     *
     * @param appPkg    目标App的包名
     * @param marketPkg 应用商店包名 ,如果为""则由系统弹出应用商店列表供用户选择,否则调转到目标市场的应用详情界面，某些应用商店可能会失败
     */
    public static void launchAppDetail(String appPkg, String marketPkg) {
        try {
            if (TextUtils.isEmpty(appPkg)) return;
            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg)) {
                intent.setPackage(marketPkg);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkPackage(Context context ,String packageName){
        if (packageName == null || "".equals(packageName))
            return false;
        try{
            context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            System.out.println("``` 应用存在");
            return true;
        }catch (PackageManager.NameNotFoundException e){
            System.out.println("``` 应用不存在");
            return false;
        }

    }

    public static Drawable getFixDrawable (Context c , @DrawableRes int draRes , int width , int height) {
        Drawable drawable = ContextCompat.getDrawable(c,draRes);
        return getFixDrawable(c, drawable,width,height);
    }

    public static Drawable getFixDrawable (Context c , Drawable drawable , int width , int height) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        return new BitmapDrawable(c.getResources(), Bitmap.createScaledBitmap(bitmap, Default.dip2px(width,c), Default.dip2px(height,c), true));
    }

    public static void readParse(final String urlPath , final Handler.Callback callback) {
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] data = new byte[1024];
                int len = 0;
                URL url = null;
                try {
                    url = new URL(urlPath);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    int response = conn.getResponseCode();
                    if (response == HttpURLConnection.HTTP_OK) {
                        InputStream inStream = conn.getInputStream();
                        while ((len = inStream.read(data)) != -1) {
                            outStream.write(data, 0, len);
                        }
                        inStream.close();
                        if (callback != null) {
                            Message msg = new Message();
                            msg.obj = new String(outStream.toByteArray());
                            callback.handleMessage(msg);
                        }
                    } else {
                        if (callback != null) {
                            Message msg = new Message();
                            msg.arg1 = -1;
                            callback.handleMessage(msg);
                        }
                    }
                    outStream.close();
                    conn.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                    if (callback != null) {
                        Message msg = new Message();
                        msg.arg1 = -1;
                        callback.handleMessage(msg);
                    }
                }

            }
        }).start();

    }

    // 获取当前版本的版本号
    public static String getVersion(Context c) {
        try {
            PackageManager packageManager = c.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(c.getPackageName(), 0);
            Log.d("当前版本号", packageInfo.versionName);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "版本号未知";
        }
    }

    public static void scrollToListviewTop(final ListView listView) {
        if (listView == null || listView.getAdapter() == null) {
            return;
        }
        listView.smoothScrollToPosition(0);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (listView.getFirstVisiblePosition() > 0)
                {
//                    listView.smoothScrollToPosition(0);
                    listView.smoothScrollBy(getScrollY1(listView), 500);
                    handler.postDelayed(this, 100);
                }
            }
        }, 100);
    }

    private static int getScrollY1(ListView listView){
        View c = listView.getChildAt(0);
        if (c == null)
        {
            return 0;
        }
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int top = c.getTop();

        if (firstVisiblePosition == 0)
        {
            return top;
        }
        else
        {
            return top - (firstVisiblePosition ) * c.getHeight() - 17;
        }
    }
    /**
     * 获取最后一个activity
     *
     * @return
     */
    public static BaseActivity getActivity() {
        try {
            if (BaseActivity.AllBaseActivity.size() > 0) {
                return BaseActivity.AllBaseActivity.get(BaseActivity.AllBaseActivity.size() - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void toClass (Context c , Intent intent) {
        ComponentName cmpName = intent.resolveActivity(c.getPackageManager());
        boolean bIsExist = false;
        for (BaseActivity baseActivity : BaseActivity.AllBaseActivity) {
            if (baseActivity.getComponentName().equals(cmpName)) {
                bIsExist = true;
                break;
            }
        }

        System.out.println("bIsExist : " + bIsExist);
        if (bIsExist) {
            ((BaseActivity) c).BackActivity(intent);
        } else {
            ((BaseActivity) c).AddActivity(0,intent);
        }

        ((BaseActivity) c).finish();
    }

    public static boolean isConnect(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED && info.isAvailable()) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
// TODO: handle exception
            Log.v("error", e.toString());
        }
        showToast("NoNet !");
        return false;
    }

    /**
     * 获取应用程序名
     *
     * @param context
     * @return
     */
    private static String getApplicationName(Context context) {
        if (context == null) {
            return "";
        }
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }

    /**
     * 获取项目名
     *
     * @param context
     * @return
     */
    public static String getProjectName(Context context) {
        if (context == null) {
            return "";
        }
        PackageManager pm = context.getPackageManager();
        return context.getApplicationInfo().loadLabel(pm).toString();
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Activity context) {
        if (context == null) {
            return 480;
        }
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.heightPixels;
    }

    public static int getStateBarHeight () {
        Rect rect = new Rect();

        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        return rect.top;
    }
    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Activity context) {
        if (context == null) {
            return 320;
        }
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.widthPixels;
    }

    /**
     * 获取屏幕的缩放比例
     *
     * @param context
     * @return
     */
    public static float getScale(Context context) {
        if (context == null) {
            return 1;
        }
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue, Context context) {
        if (context == null) {
            return 0;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static int px2sp(float pxValue, Context context) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(float spValue, Context context) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 得到剪贴板管理器
     */
    public static String GetClipboardString(Context context) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getPrimaryClip().getItemAt(0).getText().toString();
    }

    /**
     * 添加文字到剪贴板管理器
     */
    public static void SetClipboardString(CharSequence value, Context context) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setPrimaryClip(ClipData.newPlainText("text", value));
        showToast("已复制到剪贴板",Toast.LENGTH_LONG);
    }

    /**
     * 将汉字转换为网址里的内容
     *
     * @param paramString
     * @return
     */
    public static String URLEncoded(String paramString) {
        String retstr = "";
        if (paramString == null || paramString.equals("")) {
            retstr = "";
        } else {
            try {
                String str = new String(paramString.getBytes(), "UTF-8");
                retstr = URLEncoder.encode(str, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return retstr;
    }

    /**
     * 将网址里的编码转换成文字
     *
     * @param paramString
     * @return
     */
    public static String URLDecoded(String paramString) {
        String retstr = "";
        if (paramString == null || paramString.equals("")) {
            retstr = "";
        } else {
            try {
                String str = new String(paramString.getBytes(), "UTF-8");
                retstr = URLDecoder.decode(str, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return retstr;
    }

    /**
     * 返回php里面返回的无符号编码
     *
     * @param str
     * @return
     */
    public static String UnicodeToString(String str) {
        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;

        while ((i = str.indexOf("\\u", pos)) != -1) {
            sb.append(str.substring(pos, i));
            if (i + 5 < str.length()) {
                pos = i + 6;
                sb.append((char) Integer.parseInt(str.substring(i + 2, i + 6), 16));
            }
        }
        return sb.toString();
    }

    /**
     * 获取 父级方法到第几层
     *
     * @param count 第几层
     * @return
     */
    public static String GetParentMethod(int count) {
        StackTraceElement[] temp = Thread.currentThread().getStackTrace();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < count + 1 && i < temp.length; i++) {
            if (i != 1) {
                stringBuilder.append("\r\n");
            }
            stringBuilder.append(temp[i].getClassName() + "   " + temp[i].getMethodName() + "   " + temp[0].getLineNumber());
        }
        return stringBuilder.toString();
    }

    /**
     * 检测手机是否被root
     *
     * @return
     */
    public static boolean isRootSystem() {
        int kSystemRootStateUnknow = -1;
        int kSystemRootStateDisable = 0;
        int kSystemRootStateEnable = 1;
        int systemRootState = kSystemRootStateUnknow;

        if (systemRootState == kSystemRootStateEnable) {
            return true;
        } else if (systemRootState == kSystemRootStateDisable) {
            return false;
        }
        File f = null;
        final String kSuSearchPaths[] = {"/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/"};
        try {
            for (int i = 0; i < kSuSearchPaths.length; i++) {
                f = new File(kSuSearchPaths[i] + "su");
                if (f != null && f.exists()) {
                    systemRootState = kSystemRootStateEnable;
                    return true;
                }
            }
        } catch (Exception e) {
        }
        systemRootState = kSystemRootStateDisable;
        return false;
    }

//    private static JSONObject json_session;
    /**
     * 得到登陆用户的uid、sid
     *
     * @return
     */
    public static JSONObject GetSession() {
        JSONObject json_session = new JSONObject();
        try {
            json_session.put("uid", User.getUserID());
            json_session.put("sid",User.getSession());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json_session;
    }

    /**
     * 清空uid
     */
    public static void ClearSession() {
        FileInfo.SetUserString("userid", "", getActivity());
    }

    /**
     * 初始化listView的高度
     */
    public static void fixListViewHeight(ListView listView) {
        // 如果没有设置数据适配器，则ListView没有子项，返回。
        Adapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        if (listAdapter == null) {
            return;
        }
        for (int index = 0, len = listAdapter.getCount(); index < len; index++) {
            View listViewItem = listAdapter.getView(index, null, listView);
            // 计算子项View 的宽高
            if (listViewItem != null) {
                listViewItem.measure(0, 0);
                // 计算所有子项的高度和
                totalHeight += listViewItem.getMeasuredHeight();
            }

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // listView.getDividerHeight()获取子项间分隔符的高度
        // params.height设置ListView完全显示需要的高度
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static int getListViewHight (ListView listView) {
        Adapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        if (listAdapter == null) {
            return 0;
        }
        for (int index = 0, len = listAdapter.getCount(); index < len; index++) {
            View listViewItem = listAdapter.getView(index, null, listView);
            // 计算子项View 的宽高
            if (listViewItem != null) {
                listViewItem.measure(0, 0);
                // 计算所有子项的高度和
                totalHeight += listViewItem.getMeasuredHeight();
            }

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // listView.getDividerHeight()获取子项间分隔符的高度
        // params.height设置ListView完全显示需要的高度
        return totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    }

    public static String m2(Double f) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(f);
    }

    public static void showToast(String string) {
        showToast(string,Toast.LENGTH_LONG);
    }
}