package com.wzzc.other_function;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.wzzc.base.Default;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by mypxq on 16-7-16.
 */
public class FileInfo {
    public static HashMap<String, String> cacheMap;//第三级缓存
    //region save key
    public static final String PRODUCTION_ZCB_INTEGRATED = "production_zcb_integrated";
    public static final String PRODUCTION_ZCB_SALES = "production_zcb_sales";
    public static final String PRODUCTION_ZCB_NEW_PRODUCTION = "production_zcb_new_production";
    public static final String PRODUCTION_ZCB_PRICE = "production_zcb_price";
    public static final String PRODUCTION_GCB_INTEGRATED = "production_gcb_integrated";
    public static final String PRODUCTION_GCB_SALES = "production_gcb_sales";
    public static final String PRODUCTION_GCB_NEW_PRODUCTION = "production_gcb_new_production";
    public static final String PRODUCTION_GCB_PRICE = "production_gcb_price";
    //endregion

    //region 文件文本读写

    /**
     * 获取文件的文本
     */
    public static String GetFileString(String path) {
        String resstr = "";
        try {
            FileInputStream fis = new FileInputStream(path);
            int length = fis.available();
            byte[] buffer = new byte[length];
            fis.read(buffer);
            resstr = new String(buffer, "UTF-8");
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resstr;
    }

    /**
     * 获取Assets里面文件的文本
     */
    public static String GetFileString(String path, Context context) {
        String result = "";
        try {
            InputStream in = context.getResources().getAssets().open(path);
            int lenght = in.available();
            byte[] buffer = new byte[lenght];
            in.read(buffer);
            result = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 向文件写入文本
     */
    public static void SetFileString(String text, String path) {
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            out.write(text.getBytes("UTF-8"));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //endregion

    //region 文件字节读写

    /**
     * 获取文件的字节
     */
    public static byte[] GetFile(String path) {
        byte[] arr = new byte[0];
        try {
            FileInputStream fis = new FileInputStream(path);
            int length = fis.available();
            byte[] buffer = new byte[length];
            fis.read(buffer);
            fis.close();
            arr = buffer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr;
    }


    /**
     * 向文件写入字节
     */
    public static boolean SetFile(byte[] arr, String path) {
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            out.write(arr);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //endregion

    //region 文件操作

    /**
     * 获取文件在/data/data/的路径
     */
    public static String GetPathOnDocument(String path, Context context) {
        return context.getFilesDir().getAbsolutePath() + "/" + path;
    }

    /**
     * 创建文件夹
     */
    public static void CreateDirectory(String path) {
        File destDir = new File(path);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
    }

    /**
     * 删除文件夹
     */
    public static void DeleleDirectory(String path) {
        File destDir = new File(path);
        if (destDir.isDirectory()) {
            File[] childFiles = destDir.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                destDir.delete();
                return;
            }

            for (File childFile : childFiles) {
                DeleteFile(childFile.getPath());
            }
            destDir.delete();
        }
    }


    /**
     * 判断文件是否存在
     */
    public static boolean FileIsExists(String path) {
        System.out.print("FileInfo exists : " + path + " ? ");
        try {
            if (path == null) {
                System.out.println("null");
                return false;
            }
            File f = new File(path);
            if (!f.exists()) {
                System.out.println("false");
                return false;
            }
            System.out.println("true");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("false");
            return false;
        }
        return true;
    }

    /**
     * 删除文件
     */
    public static void DeleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
                System.out.println("FileInfo delete " + path);
            }
        }
    }

    //endregion

    //region 属性读写

    /**
     * 获取属性设置
     */
    public static String GetUserString(String name, Context context) {
        if (cacheMap != null && cacheMap.containsKey(name)) {
            return cacheMap.get(name);
        } else {
            try {
                SharedPreferences settings = context.getSharedPreferences("settings", 0);
                System.out.println("FileInfo GetUserString(" + name + ") : " + settings.getString(name, ""));
                String str = settings.getString(name, "");
                if (cacheMap == null) {
                    cacheMap = new HashMap<>();
                }
                cacheMap.put(name,str);
                return str;
            } catch (NullPointerException e) {
                return "";
            }
        }
    }

    public static String GetStringFromCacheMap(String name) {
        if (cacheMap != null && cacheMap.containsKey(name)) {
            return cacheMap.get(name);
        } else
            return GetUserString(name,Default.getActivity());
    }

    /**
     * 设置属性设置
     */
    public static void SetUserString(final String name, final String value, final Context context) {
        SharedPreferences settings = context.getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(name, value);
        editor.apply();
        System.out.println("FileInfo SetUserString(" + name + ") : " + value);
        SetStringToCacheMap(name,value);
    }

    public static void ClearUserString (Context context) {
        System.out.println("FileInfo ClearUserString");
        String userid = GetUserString("userid",context);
        String sessionid = GetUserString("sessionid",context);
        SharedPreferences settings = context.getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.apply();
        SetUserString("userid",userid,context);
        SetUserString("sessionid",sessionid,context);
    }

    public static void SetStringToCacheMap(String name, String value) {
        if (cacheMap == null) {
            cacheMap = new HashMap<>();
        }
        cacheMap.put(name, value);
        if (!IsAtUserString(name,Default.getActivity())) {
            SetUserString(name,value,Default.getActivity());
        }
    }

    public static void RemoveUserString(String name, Context context) {
        RemoveStringFromCacheMap(name);
        SharedPreferences settings = context.getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        if (settings.contains(name)) {
            editor.remove(name);
            editor.apply();
            System.out.println("FileInfo RemoveUserString(" + name + ") !");
        }
    }

    public static void RemoveStringFromCacheMap(String name) {
        if (cacheMap != null && cacheMap.containsKey(name)) {
            cacheMap.remove(name);
        }
    }

    public static boolean IsAtUserString(String name, Context context) {
        if (name == null) {
        return false;
        }
        if (cacheMap!=null && cacheMap.containsKey(name)) {
            return true;
        }
        SharedPreferences settings = context.getSharedPreferences("settings", 0);
        return settings.contains(name);
    }

    public static boolean IsAtCacheMap(String name) {
        return cacheMap != null && cacheMap.containsKey(name);
    }

    //endregion

    //region 图片文件读写
    public static void saveImage(Bitmap bmp, String title, String description) {
        String SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
        MediaStore.Images.Media.insertImage(Default.getActivity().getContentResolver(), bmp, title, description);
        Default.getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(SDCARD_ROOT + title + ".jpg"))));
    }

    /**
     * 写入图片文件
     */
    public static void SetFileImage(Bitmap image, String path) {
        if (image == null) {
            return;
        }
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            String fileName = f.getName();
            String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (prefix.equals("png")) {
                image.compress(Bitmap.CompressFormat.PNG, 100, out);
            } else {
                image.compress(Bitmap.CompressFormat.JPEG, 100, out);
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取图片文件
     */
    public static Bitmap GetFileImage(String path) {
        return GetFileImage(path, -1, -1);
    }

    /**
     * 读取图片文件
     */
    public static Bitmap GetFileImage(String path, int minwidth, int minheight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = computeSampleSize(options, -1, minwidth * minheight);
        options.inJustDecodeBounds = false;
       /* if (minheight <= 0 || minwidth <= 0) {
            options.inSampleSize = 1;
        } else {
            int sw = options.outWidth / minwidth;
            int sh = options.outHeight / minheight;
            options.inSampleSize = sw > sh ? sw : sh;
            System.out.println("inSampleSize : " + options.inSampleSize);
        }*/
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(path, options);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return /*GetRotateImage(path, bitmap)*/bitmap;
    }

    private static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * 从Assets读取图片文件
     */
    public static Bitmap GetFileImage(String path, Context context) {
        return GetFileImage(path, context, -1, -1);
    }

    /**
     * 从Assets读取图片文件
     */
    public static Bitmap GetFileImage(String path, Context context, int minwidth, int minheight) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, options);
            options.inJustDecodeBounds = false;
            if (minheight < 0 || minwidth < 0) {
                options.inSampleSize = 1;
            } else {
                int sw = options.outWidth / minwidth;
                int sh = options.outHeight / minheight;
                options.inSampleSize = sw > sh ? sw : sh;
            }
            image = BitmapFactory.decodeStream(is, null, options);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return /*GetRotateImage(path, image)*/image;
    }

    /**
     * 获取正确的旋转图片
     */
    public static Bitmap GetRotateImage(String path, Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (degree == 0) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    //endregion
}
