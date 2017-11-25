package com.wzzc.other_function;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.LruCache;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.wzzc.base.Default;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;
import java.util.concurrent.TimeoutException;

/**
 * Created by TouTou on 16/10/26.
 */

public class ImageHelper {
    private static View v;

    /**
     * 获取高斯模糊图片 （模糊程度为 0 ~ 25%）
     *
     * @param bitmap 需要进行高斯模糊的图片
     */
    public static Bitmap blurBitmap(Context c, Bitmap bitmap) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                RenderScript rs = RenderScript.create(c.getApplicationContext());
                ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
                Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
                Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
                blurScript.setRadius(25.0f);
                blurScript.setInput(allIn);
                blurScript.forEach(allOut);
                allOut.copyTo(outBitmap);
                bitmap.recycle();
                rs.destroy();
                return outBitmap;
            } else {
                // TODO: 2016/12/20
                Toast.makeText(c, "SDK < 17 ", Toast.LENGTH_LONG).show();
                return bitmap;
            }
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            return bitmap;
        }
    }

    /**
     * 获取当前屏幕的截屏，此方法执行完后需要执行{@link ImageHelper#stopShotScreen()}
     * 防止内存泄漏
     */
    public static Bitmap shotScreen(Context c) {
        v = ((Activity) c).getWindow().getDecorView();
        v.setDrawingCacheEnabled(true);
        return v.getDrawingCache();
    }

    /**
     * 停止屏幕截屏
     *
     * @see ImageHelper#shotScreen(Context)
     */
    public static void stopShotScreen() {
        if (v != null) {
            v.setDrawingCacheEnabled(false);
            v = null;
        }
    }

    public static Bitmap truncationView(View v) {
        Bitmap b;
        if (v.getMeasuredHeight() <= 0) {
            v.measure(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            v.draw(c);

        } else {
            b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
            v.draw(c);

        }
        System.out.println("truncation bitmap : " + b);
        return b;
    }

    public static byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 渲染
     */
    public static Bitmap exaggerationView(Bitmap bitmap) {
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RectShape());
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
        Paint paint = shapeDrawable.getPaint();
        Rect rect = new Rect(300, 200  /* 开始镜像的点*/, bitmap.getWidth() /*镜像图片的宽度*/, bitmap.getHeight() /*镜像图片的高度*/);
        shapeDrawable.setBounds(rect);
        paint.setShader(bitmapShader);
        Canvas canvas = new Canvas(bitmap);
        shapeDrawable.draw(canvas);
        return bitmap;
    }

    //<editor-folder desk="[Tailor 剪裁:当前传入bitmap的整个作为剪裁对象]">

    /**
     * <h1>图片剪裁</h1>
     * <h2>圆角</h2>
     */
    public static Bitmap tailorBitmapToRoundCorner(@NonNull Bitmap bitmap, float degree) {
        if (bitmap == null) {
            return null;
        }
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        Paint paint = new Paint();
        canvas.drawRoundRect(rectF, degree, degree, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rectF, paint);

        return outBitmap;
    }

    /**
     * <h1>图片剪裁</h1>
     * <h2>圆形</h2>
     */
    public static Bitmap tailorBitmapToCircle(@NonNull Bitmap bitmap, PointF centerPointF, float radius) throws OutOfMemoryError{
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        Paint paint = new Paint();
        canvas.drawCircle(centerPointF.x, centerPointF.y, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rectF, paint);

        return outBitmap;
    }

    /**
     * <h1>图片剪裁</h1>
     * <h2>圆弧</h2>
     *
     * @param startAngle 开始角度
     * @param sweepAngle 旋转角度
     * @param useCenter  是否显示半径
     * @param width      宽度
     */
    public static Bitmap tailorBitmapToArc(@NonNull Bitmap bitmap, float startAngle, float sweepAngle, boolean useCenter, int width) {
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(width);
        canvas.drawArc(rectF, startAngle, sweepAngle, useCenter, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rectF, paint);

        return outBitmap;
    }


    /**
     * <h1>图片剪裁</h1>
     * <h2>椭圆</h2>
     */
    public static Bitmap tailorBitmapToOval(@NonNull Bitmap bitmap) {
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        Paint paint = new Paint();
        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rectF, paint);
        return outBitmap;
    }

    /**
     * <h1>图片剪裁</h1>
     * <h2>文字</h2>
     *
     * @param text     想要剪裁成什么文字
     * @param textSize 剪裁出来的文字的大小 : <font color=blue>80</font>
     * @param path     文字路线的形态 :
     *                 <table style="border: solid thin purple">
     *                 Path path = <font color=purple>new</font> Path();<br>
     *                 path.addArc(<font color=blue>0,0</font>,<font color=purple>blogLaptop.</font>getMeasuredWidth(),<font color=purple>blogLaptop.</font>getMeasuredHeight(),<font color=blue>180,180<i color=gray>【在原来的角度上旋转180°】</i></font>);
     *                 <br>&gt<p style="border: solid thin green">
     *                 &middot path.moveTo(<font color=blue>100,100</font>);<br>
     *                 &middot path.lineTo(<font color=blue>600,100</font>);</p>
     *                 </table>
     * @param hOffset  横向偏移量 ：<font color=blue>0</font>
     * @param vOffset  纵向偏移量 ：bitmap.<font color=purple>getHeight()</font>/2
     * @return
     */
    public static Bitmap tailorBitmapToTextOnPath(@NonNull Bitmap bitmap, String text, int textSize, Path path, float hOffset, float vOffset) {
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        canvas.drawTextOnPath(text, path, hOffset, vOffset, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rectF, paint);
        return outBitmap;
    }

    /**
     * <h1>图片剪裁</h1>
     * <h2>线条</h2>
     * <table style="border: solid thin purple">
     * Bitmap exaBT = ImageHelper.tailorBitmapToLines(ImageHelper.truncationView(<font color=purple>blogLaptop</font>),<font color=purple>new</font> ArrayList<PointF[]>(){{
     * <br>&nbsp&nbsp&nbsp&nbsp add(<font color=purple>new</font> PointF[]{<font color=purple>new</font> PointF(<font color=blue>0,1</font>),<font color=purple>new</font> PointF(blogLaptop.getMeasuredWidth() , 1)});
     * <br>&nbsp&nbsp&nbsp&nbsp add(<font color=purple>new</font> PointF[]{<font color=purple>new</font> PointF(<font color=blue>0,100</font>),<font color=purple>new</font> PointF(blogLaptop.getMeasuredWidth() , 100)});
     * <br>&nbsp&nbsp&nbsp&nbsp add(<font color=purple>new</font> PointF[]{<font color=purple>new</font> PointF(<font color=blue>0,200</font>),<font color=purple>new</font> PointF(blogLaptop.getMeasuredWidth() , 200)});
     * <br>&nbsp&nbsp&nbsp&nbsp add(<font color=purple>new</font> PointF[]{<font color=purple>new</font> PointF(<font color=blue>0,300</font>),<font color=purple>new</font> PointF(blogLaptop.getMeasuredWidth() , 300)});
     * <br>}},<font color=blue>20.f</font> );
     * </table>
     *
     * @param pointFs 直线的两端坐标的集合
     *                <table style="border:solid thin purple">
     *                {<br>&nbsp[startPointF , endPointF],<br>&nbsp[startPointF , endPointF],<br>&nbsp[startPointF , endPointF]<br>}
     *                </table>
     * @param width   线条的宽度 : <font color=blue>20.0f</font>
     * @return
     */
    public static Bitmap tailorBitmapToLines(@NonNull Bitmap bitmap, ArrayList<PointF[]> pointFs, float width) {
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        Paint paint = new Paint();
        paint.setStrokeWidth(width);
        float[] pts = new float[pointFs.size() * 4];
        int z = 0;
        for (int i = 0; i < pointFs.size(); i++) {
            PointF[] pfs = pointFs.get(i);
            pts[z] = pfs[0].x;
            z++;
            pts[z] = pfs[0].y;
            z++;
            pts[z] = pfs[1].x;
            z++;
            pts[z] = pfs[1].y;
            z++;
        }
        canvas.drawLines(pts, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rectF, paint);
        return outBitmap;
    }

    /**
     * <h1>图片剪裁</h1>
     * <h2>矩形</h2>
     *
     * @param rect 想裁剪的范围
     */
    public static Bitmap tailorBitmapRect(@NonNull Bitmap bitmap, Rect rect) {

        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        Paint paint = new Paint();
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return outBitmap;
    }

    //</editor-folder>

    //<editor-folder desk="[Tissue 薄纱 : 根据传入的不同位置在bitmap上添加薄纱]">

    /**
     * <h1>为 Bitmap 添加一层薄纱</h1>
     * <h2>矩形</h2>
     *
     * @param startPointF 薄纱添加的起点坐标（相对传入的bitmap左上方）：(<font color=blue>0,0</font>)
     * @param endPointF   薄纱添加的终点坐标（相对传入的bitmap右下方）：<font color=purple>bitmap</font>.getWidth() ,<font color=purple>bitmap</font>.getHeight()
     * @param color       薄纱的颜色 ：Color.<font color=purple>RED</font>
     * @param mode        薄纱的显示形态:PorterDuff.Mode.<font color=purple>DARKEN</font>
     */
    public static Bitmap tissueRectForBitmap(@NonNull Bitmap bitmap, PointF startPointF, PointF endPointF, @ColorInt int color, @NonNull PorterDuff.Mode mode, Paint paint) {
        Bitmap outBitmap;
        outBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
        Canvas canvas = new Canvas(outBitmap);
        Rect rect = new Rect((int) startPointF.x, (int) startPointF.y, (int) endPointF.x, (int) endPointF.y);
        if (paint == null) {
            paint = new Paint();
        }
        paint.setColor(color);
        paint.setXfermode(new PorterDuffXfermode(mode));
        canvas.drawRect(rect, paint);
        return outBitmap;
    }

    /**
     * <h1>为 Bitmap 添加一层薄纱</h1>
     * <h2>圆角矩形</h2>
     *
     * @param startPointF 薄纱添加的起点坐标（相对传入的bitmap左上方）：(<font color=blue>0,0</font>)
     * @param endPointF   薄纱添加的终点坐标（相对传入的bitmap右下方）：<font color=purple>bitmap.</font>getWidth() , <font color=purple>bitmap.</font>getHeight()
     * @param rx          x方向上的圆角角度 : <font color=blue>30.0f</font>
     * @param ry          y方向上的圆角角度 : <font color=blue>60.0f</font>
     * @param color       薄纱的颜色 ：Color.<font color=purple>RED</font>
     * @param mode        薄纱的显示形态:PorterDuff.Mode.<font color=purple>DARKEN</font>
     */
    public static Bitmap tissueRoundRectForBitmap(@NonNull Bitmap bitmap, PointF startPointF, PointF endPointF, float rx, float ry, @ColorInt int color, @NonNull PorterDuff.Mode mode, Paint paint) {
        Bitmap outBitmap;
        outBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
        Canvas canvas = new Canvas(outBitmap);
        RectF rectF = new RectF((int) startPointF.x, (int) startPointF.y, (int) endPointF.x, (int) endPointF.y);
        if (paint == null) {
            paint = new Paint();
        }
        paint.setColor(color);
        paint.setXfermode(new PorterDuffXfermode(mode));
        canvas.drawRoundRect(rectF, rx, ry, paint);
        return outBitmap;
    }

    /**
     * <h1>为 Bitmap 添加一层薄纱</h1>
     * <h2>圆弧形</h2>
     *
     * @param startPointF 薄纱添加的起点坐标（相对传入的bitmap左上方）：(<font color=blue>0,0</font>)
     * @param endPointF   薄纱添加的终点坐标（相对传入的bitmap右下方）：<font color=purple>bitmap</font>.getWidth() , <font color=purple>bitmap</font>.getHeight()
     * @param startAngle  圆弧的起始角度。<font color=blue>180.0f</font>
     * @param sweepAngle  圆弧的角度: <font color=blue>180.0f</font>
     * @param useCenter   是否显示半径连线，<font color=purple>true</font>表示显示圆弧与圆心的半径连线，<font color=purple>false</font>表示不显示。
     * @param color       薄纱的颜色 ：Color.<font color=purple>RED</font>
     * @param width       圆弧的宽度 : <font color=blue>10</font>
     * @param mode        薄纱的显示形态:PorterDuff.<font color=purple>Mode.DARKEN</font>
     */
    public static Bitmap tissueArcForBitmap(@NonNull Bitmap bitmap, PointF startPointF, PointF endPointF, float startAngle, float sweepAngle, boolean useCenter, float width, @ColorInt int color, @NonNull PorterDuff.Mode mode, Paint paint) {
        Bitmap outBitmap;
        outBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
        Canvas canvas = new Canvas(outBitmap);
        RectF rectF = new RectF((int) startPointF.x, (int) startPointF.y, (int) endPointF.x, (int) endPointF.y);
        if (paint == null) {
            paint = new Paint();
        }
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(width);
        paint.setXfermode(new PorterDuffXfermode(mode));
        canvas.drawArc(rectF, startAngle, sweepAngle, useCenter, paint);
        return outBitmap;
    }

    /**
     * <h1>为 Bitmap 添加一层薄纱</h1>
     * <h2>圆形</h2>
     * <table style="border: solid thin purple">
     * Paint paint = <font color=purple>new</font> Paint();<br>
     * paint.setStyle(Paint.Style.<font color=purple>STROKE</font>);<br>
     * paint.setStrokeWidth(<font color=blue>10f</font>);<br><br>
     * Bitmap exaBT = ImageHelper.tissueCircleForBitmap(ImageHelper.truncationView(<font color=purple>blogLaptop</font>),<font color=purple>new</font> PointF(<font color blue>200,150</font>), <font color=blue>100</font> , Color<font color=purple>.RED</font> , PorterDuff.Mode.<font color=purple>DARKEN</font> , paint);
     * </table>
     *
     * @param centerPointF 圆心的坐标 : (<font color=blue>200,150</font>)
     * @param radius       圆形的半径 : <font color=blue>100</font>
     * @param color        薄纱的颜色 ：Color<font color=purple>.RED</font>
     * @param mode         薄纱的显示形态:PorterDuff.Mode<font color=purple>.DARKEN</font>
     */
    public static Bitmap tissueCircleForBitmap(@NonNull Bitmap bitmap, PointF centerPointF, float radius, @ColorInt int color, @NonNull PorterDuff.Mode mode, Paint paint) {
        Bitmap outBitmap;
        outBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
        Canvas canvas = new Canvas(outBitmap);
        if (paint == null) {
            paint = new Paint();
        }
        paint.setColor(color);
        paint.setXfermode(new PorterDuffXfermode(mode));
        canvas.drawCircle(centerPointF.x, centerPointF.y, radius, paint);
        return outBitmap;
    }


    /**
     * <h1>为 Bitmap 添加一层薄纱</h1>
     * <h2>沿着Path路径绘制文本</h2>
     *
     * @param path     文字所在路径形状 <br>
     *                 <table style="border: solid thin purple">
     *                 &middot Path path = <font color=purple>new</font> Path();<br>
     *                 &middot path.addArc(<font color=blue>0,0</font>,<font color=purple>blogLaptop.</font>getMeasuredWidth(),<font color=purple>blogLaptop.</font>getMeasuredHeight(),<font color=blue>180,180<i color=gray>【在原来的角度上旋转180°】</i></font>);
     *                 <br>&gt<p style="border: solid thin green">
     *                 &middot path.moveTo(<font color=blue>100,100</font>);<br>
     *                 &middot path.lineTo(<font color=blue>600,100</font>);</p>
     *                 </table>
     * @param text     文本内容
     * @param textSize 文本大小 : <font color=blue>80</font>
     * @param hOffset  水平偏移量 : <font color=blue>0</font>
     * @param vOffset  竖直偏移量 : <font color=blue>bitmap.getHeight</font>/2
     * @param color    薄纱的颜色 ：Color<font color=purple>.RED</font>
     * @param mode     薄纱的显示形态:PorterDuff.Mode<font color=purple>.DARKEN</font>
     */
    public static Bitmap tissueTextOnPathForBitmap(@NonNull Bitmap bitmap, String text, float textSize, @NonNull Path path, float hOffset, float vOffset, @ColorInt int color, @NonNull PorterDuff.Mode mode, Paint paint) {
        Bitmap outBitmap;
        outBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
        Canvas canvas = new Canvas(outBitmap);
        if (paint == null) {
            paint = new Paint();
        }
        paint.setColor(color);
        paint.setTextSize(textSize);
        paint.setXfermode(new PorterDuffXfermode(mode));
        canvas.drawTextOnPath(text, path, hOffset, vOffset, paint);
        return outBitmap;
    }


    /**
     * <h1>为 Bitmap 添加一层薄纱</h1>
     * <h2>线形</h2>
     * <table style="border: solid thin purple">
     * Bitmap exaBT = ImageHelper.tissueLinesForBitmap(ImageHelper.truncationView(<font color=purple>blogLaptop</font>),<font color=purple>new</font> ArrayList<PointF[]>(){{
     * <br>&nbsp&nbsp&nbsp&nbsp add(<font color=purple>new</font> PointF[]{<font color=purple>new</font> PointF(<font color=blue>0,1</font>),<font color=purple>new</font> PointF(blogLaptop.getMeasuredWidth() , 1)});
     * <br>&nbsp&nbsp&nbsp&nbsp add(<font color=purple>new</font> PointF[]{<font color=purple>new</font> PointF(<font color=blue>0,100</font>),<font color=purple>new</font> PointF(blogLaptop.getMeasuredWidth() , 100)});
     * <br>&nbsp&nbsp&nbsp&nbsp add(<font color=purple>new</font> PointF[]{<font color=purple>new</font> PointF(<font color=blue>0,200</font>),<font color=purple>new</font> PointF(blogLaptop.getMeasuredWidth() , 200)});
     * <br>&nbsp&nbsp&nbsp&nbsp add(<font color=purple>new</font> PointF[]{<font color=purple>new</font> PointF(<font color=blue>0,300</font>),<font color=purple>new</font> PointF(blogLaptop.getMeasuredWidth() , 300)});
     * <br>}},<font color=blue>20.f</font> , Color<font color=purple>.RED</font> , PorterDuff<font color=purple>.Mode.DARKEN</font> , <font color=purple>null</font>);
     * </table>
     *
     * @param pointFs 直线的两端坐标的集合 <br>
     *                <table style="border:solid thin purple">
     *                {<br>&nbsp[startPointF , endPointF],<br>&nbsp[startPointF , endPointF],<br>&nbsp[startPointF , endPointF]<br>}
     *                </table>
     * @param width   线条的宽度 : <font color=blue>20.0f</font>
     * @param color   薄纱的颜色 ：Color<font color=purple>.RED</font>
     * @param mode    薄纱的显示形态:PorterDuff.Mode<font color=purple>.DARKEN</font>
     */
    public static Bitmap tissueLinesForBitmap(@NonNull Bitmap bitmap, ArrayList<PointF[]> pointFs, float width, @ColorInt int color, @NonNull PorterDuff.Mode mode, Paint paint) {
        Bitmap outBitmap;
        outBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
        Canvas canvas = new Canvas(outBitmap);
        if (paint == null) {
            paint = new Paint();
        }
        paint.setStrokeWidth(width);
        paint.setColor(color);
        paint.setXfermode(new PorterDuffXfermode(mode));

        float[] pts = new float[pointFs.size() * 4];
        int z = 0;
        for (int i = 0; i < pointFs.size(); i++) {
            PointF[] pfs = pointFs.get(i);
            pts[z] = pfs[0].x;
            z++;
            pts[z] = pfs[0].y;
            z++;
            pts[z] = pfs[1].x;
            z++;
            pts[z] = pfs[1].y;
            z++;
        }

        canvas.drawLines(pts, paint);
        return outBitmap;
    }


    /**
     * <h1>为 Bitmap 添加一层薄纱</h1>
     * <h2>椭圆形</h2>
     * <table style="border:solid thin purple">
     * Bitmap exaBT = ImageHelper.tissueOvalForBitmap(ImageHelper.truncationView(<font color=purple>blogLaptop</font>),<font color=purple>new</font> PointF(<font color=blue>0,0</font>),new PointF(<font color=purple>blogLaptop.</font>getMeasuredWidth() , <font color=purple>blogLaptop.</font>getMeasuredHeight() ), Color<font color=purple>.RED</font> , PorterDuff.Mode.<font color=purple>DARKEN</font> , <font color=purple>null</font>);
     * </table>
     *
     * @param startPointF 薄纱添加的起点坐标（相对传入的<font color=purple>bitmap</font>左上方）：(<font color=blue>0,0</font>)
     * @param endPointF   薄纱添加的终点坐标（相对传入的<font color=purple>bitmap</font>右下方）：<font color=purple>bitmap.</font>getWidth() , <font color=purple>bitmap.</font>getHeight()
     * @param color       薄纱的颜色 ：Color<font color=purple>.RED</font>
     * @param mode        薄纱的显示形态:PorterDuff.Mode<font color=purple>.DARKEN</font>
     */
    public static Bitmap tissueOvalForBitmap(@NonNull Bitmap bitmap, @NonNull PointF startPointF, @NonNull PointF endPointF, @ColorInt int color, @NonNull PorterDuff.Mode mode, Paint paint) {
        Bitmap outBitmap;
        outBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
        Canvas canvas = new Canvas(outBitmap);
        RectF rectF = new RectF((int) startPointF.x, (int) startPointF.y, (int) endPointF.x, (int) endPointF.y);
        if (paint == null) {
            paint = new Paint();
        }
        paint.setColor(color);
        paint.setXfermode(new PorterDuffXfermode(mode));
        canvas.drawOval(rectF, paint);
        return outBitmap;
    }


    //</editor-folder>

    //<editor-folder desk="[二维码]">

    /**
     * 用字符串生成二维码
     *
     * @param str
     * @return
     * @throws WriterException
     * @author zhouzhe@lenovo-cw.com
     */
    public static Bitmap create2DCode(Bitmap logo, String str) throws WriterException {
//        str = html;
        //生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败.
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hints = new Hashtable<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 300, 300, hints);//设置大小
        matrix = updateBit(matrix, 1);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        //二维矩阵转为一维像素数组,也就是一直横着排了
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                } else {
                    pixels[y * width + x] = 0xffffffff;
                }

            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return addLogo(bitmap, tailorBitmapToRoundCorner(logo, 90));
    }

    private static BitMatrix updateBit(BitMatrix matrix, int margin) {
        int tempM = margin * 2;
        int[] rec = matrix.getEnclosingRectangle();   //获取二维码图案的属性
        int resWidth = rec[2] + tempM;
        int resHeight = rec[3] + tempM;
        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight); // 按照自定义边框生成新的BitMatrix
        resMatrix.clear();
        for (int i = margin; i < resWidth - margin; i++) {   //循环，将二维码图案绘制到新的bitMatrix中
            for (int j = margin; j < resHeight - margin; j++) {
                if (matrix.get(i - margin + rec[0], j - margin + rec[1])) {
                    resMatrix.set(i, j);
                }
            }
        }
        return resMatrix;
    }

    /**
     * 在二维码中间添加Logo图案
     */
    private static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }

        if (logo == null) {
            return src;
        }

        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        if (srcWidth == 0 || srcHeight == 0) {
            System.out.println("``` src size == 0");
            return null;
        } else {
            System.out.println("``` src size == " + srcWidth);
        }

        if (logoWidth == 0 || logoHeight == 0) {
            System.out.println("``` logo size == 0");
            return src;
        } else {
            System.out.println("``` logo size == " + logoWidth);
        }

        //logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 4 / logoWidth;

        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }

        return bitmap;
    }

    //</editor-folder>

    //<editor-folder desk="缓存图片处理">
    private static LruCache<String, Bitmap> imageCache;
    private static ArrayList<String> loadingList;

    public static void clearImage(Context c) {
        imageCache = null;
        loadingList = null;
        File destDir = new File(getPathOnDocument("cache", c));
        if (destDir.isDirectory()) {
            File[] childFiles = destDir.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                destDir.delete();
                return;
            }

            for (File childFile : childFiles) {
                deleteFile(childFile.getPath());
            }
            destDir.delete();
        }
        deleleDirectory(getPathOnDocument("cache", c));
    }

    /**
     * <h1>三级缓存</h1>
     * <i>图片缓存最大内存：系统内存的1/8</i>
     *
     * @param urlPath 图片下载地址
     * @param width   预图片宽
     * @param height  预图片高（最好能实际布局尺寸）
     * @param handler 图片的回掉
     */
    public static void handlerImage(final Context c, String urlPath, final int width, final int height, final HandlerImage handler) {
        if (imageCache == null) {
            int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 8);
            imageCache = new LruCache<String, Bitmap>(memoryCacheSize) {
                //重写此方法,测量Bitmap的大小
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getByteCount();
                }
            };
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = computeSampleSize(options, -1, width * height);
        options.inJustDecodeBounds = false;
        System.out.println("imageCache.size() : " + imageCache.size() + " imageCache.maxSize() : " + imageCache.maxSize());
        if (imageCache.size() >= imageCache.maxSize()) {
            System.out.println("imageCache trim to size !");
            imageCache.trimToSize(imageCache.maxSize());
        }
        String imageName = urlPath;
        while (imageName.contains(":")) {
            imageName = imageName.replaceAll(":", "_");
        }
        while (imageName.contains("/")) {
            imageName = imageName.replaceAll("/", "_");
        }
        if (imageCache.get(imageName) == null) {
            if (fileIsExists(getPathOnDocument("cache/" + imageName, c))) {
                try {
                    Bitmap bitmap = BitmapFactory.decodeFile(getPathOnDocument("cache/" + imageName, c), options);
                    imageCache.put(imageName, bitmap);
                    handler.imageBack(bitmap);
                } catch (OutOfMemoryError | NullPointerException e) {
                    e.printStackTrace();
                    deleteFile(getPathOnDocument("cache/" + imageName, c));
                    handler.imageBack(null);
                }
            } else {
                if (loadingList == null) {
                    loadingList = new ArrayList<>();
                }
                if (loadingList.contains(imageName)) {
                    return;
                } else {
                    loadingList.add(imageName);
                }
                new MyAsyncTask().execute(new Object[]{urlPath, imageName}, new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        String urlPath = (String) ((Object[]) msg.obj)[0];
                        String imageName = (String) ((Object[]) msg.obj)[1];
                        System.out.println("urlPath : " + urlPath);
                        final byte[] imgarr = get(c, urlPath);
                        String dir = getPathOnDocument("cache", c);
                        if (!fileIsExists(dir)) {
                            createDirectory(dir);
                        }
                        String path = getPathOnDocument("cache/" + imageName, c);
                        setFile(imgarr, path);
                        loadingList.remove(imageName);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imgarr, 0, imgarr.length, options);
                        try {
                            imageCache.put(imageName, bitmap);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                        getActivity(c).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    handler.imageBack(BitmapFactory.decodeByteArray(imgarr, 0, imgarr.length, options));
                                } catch (OutOfMemoryError outOfMemoryError) {
                                    outOfMemoryError.printStackTrace();
                                }
                            }
                        });
                        return false;
                    }
                });
            }
        } else {
            handler.imageBack(imageCache.get(imageName));
        }
    }

    public static abstract class HandlerImage {
        protected abstract void imageBack(Bitmap bitmap);
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
     * 通过GET获取服务器上的字节
     */
    private static byte[] get(final Context c, String strurl) {
        try {
            URL url = new URL(strurl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("GET");
            switch (httpURLConnection.getResponseCode()) {
                case HttpURLConnection.HTTP_OK:
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    InputStream inputStream = httpURLConnection.getInputStream();
                    while ((len = inputStream.read(buffer)) != -1) {
                        output.write(buffer, 0, len);
                    }
                    output.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return output.toByteArray();
                default:

            }
            httpURLConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new byte[0];
    }

    /**
     * 获取文件在/data/data/的路径
     */
    private static String getPathOnDocument(String path, Context context) {
        return context.getFilesDir().getAbsolutePath() + "/" + path;
    }

    /**
     * 判断文件是否存在
     */
    private static boolean fileIsExists(String path) {
        System.out.print("File exists : " + path + " ? ");
        try {
            if (path == null) {
                return false;
            }
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 创建文件夹
     */
    private static void createDirectory(String path) {
        File destDir = new File(path);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
    }

    /**
     * 删除文件
     */
    private static void deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
                System.out.println("FileInfo delete " + path);
            }
        }
    }

    /**
     * 删除文件夹
     */
    private static void deleleDirectory(String path) {
        File destDir = new File(path);
        if (destDir.isDirectory()) {
            File[] childFiles = destDir.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                destDir.delete();
                return;
            }

            for (File childFile : childFiles) {
                deleteFile(childFile.getPath());
            }
            destDir.delete();
        }
    }

    /**
     * 向文件写入字节
     */
    private static boolean setFile(byte[] arr, String path) {
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

    //</editor-folder>

    //<editor-folder desk="上传图片">

    /**
     * <h1>此方法适用于所有post请求</h1>
     *
     * @param type           上传类型 自定义 例如传递 "head"
     * @param twoHyphens
     * @param boundary       分隔符 mypxqcreated
     * @param end
     * @param info           普通参数以String/JSONObject 类型存储， 图片参数以bitmap类型存储
     * @param strUrl         服务端地址
     * @param saveDir        图片存储目录 Environment.getExternalStorageDirectory() + "/temple"
     * @param upLoadListener 上传过程完成后执行操作
     */
    public static void upLoadImage(Context c, String type, String twoHyphens, String boundary, String end, HashMap<String, Object> info, String strUrl, String saveDir, UpLoadListener upLoadListener) {
        new MyAsyncTask().execute(new Object[]{type, twoHyphens, boundary, end, info, strUrl, saveDir, upLoadListener, c}, new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Object[] objects = (Object[]) msg.obj;
                String type = (String) objects[0];
                String twoHyphens = (String) objects[1];
                String boundary = (String) objects[2];
                String end = (String) objects[3];
                HashMap<String, Object> info = (HashMap<String, Object>) objects[4];
                String strUrl = (String) objects[5];
                String saveDir = (String) objects[6];
                final long responseTime = System.currentTimeMillis();
                UpLoadListener upLoadListener = (UpLoadListener) objects[7];
                Context c = (Context) objects[8];
                System.out.println("Load : info " + info.toString());
                try {
                    URL url = new URL(strUrl);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout(30*1000);
                    con.setConnectTimeout(30*1000);
                    con.setRequestMethod("POST");
                    con.setDoInput(true); // 允许输入流
                    con.setDoOutput(true); // 允许输出流
                    con.setUseCaches(false); // 不允许使用缓存
                    con.setRequestProperty("Charset", "UTF-8"); // 设置编码
                    con.setRequestProperty("Content-Type", "multipart/form-data" + ";boundary=" + boundary);
                    DataOutputStream ds = new DataOutputStream(con.getOutputStream());

                    Set<String> set = info.keySet();
                    HashMap<String, File> info_file = new HashMap<>();
                    HashMap<String, String> info_string = new HashMap<>();
                    for (String name : set) {
                        Object value = info.get(name);
                        System.out.println("Load : value : " + value);
                        if (value instanceof JSONObject) {
                            value = value.toString();
                        }
                        if (value instanceof String) {
                            info_string.put(name, (String) value);
                        } else if (value instanceof Bitmap) {
                            System.out.println("Load : hasBitmap !");
                            File dir = new File(saveDir + type + "_" + name + ".jpg");
                            if (!dir.exists()) {
                                if (!dir.getParentFile().exists()) {
                                    if (dir.getParentFile().mkdirs()) {
                                        System.out.println("Load : 父文件夹创建成功");
                                    } else {
                                        System.out.println("Load : 父文件夹创建失败");
                                        if (upLoadListener != null) {
                                            upLoadListener.createFileError(c, dir.getParentFile());
                                            return false;
                                        }
                                    }
                                }
                            }
                            System.out.println("Load : 文件夹创建完毕");
                            try {
                                dir.createNewFile();
                            } catch (IOException e) {e.printStackTrace();
                                System.out.println("Load : " + e.getMessage());
                            }
                            System.out.println("Load : 存入文件");
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            ((Bitmap) value).compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] byteArray = baos.toByteArray();
                            FileOutputStream fos = new FileOutputStream(dir);
                            BufferedOutputStream bos = new BufferedOutputStream(fos);
                            bos.write(byteArray);
                            info_file.put(name, dir);
                            bos.close();
                            fos.close();
                            baos.close();
                        } else {
                            Default.showToast("无法解析");
                        }
                    }
                    System.out.println("Load : info_string --> " + info_string.toString());
                    System.out.println("Load : info_file --> " + info_file.toString());

                    writeStringParam(responseTime, twoHyphens, boundary, end, ds, info_string, upLoadListener);
                    writeFileParam(c, responseTime, twoHyphens, boundary, end, ds, info_file, upLoadListener);

                    int responseCode = 0;
                    try {
                        responseCode = con.getResponseCode();
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("responseCode " + e.getMessage());
                    }
                    System.out.println("responseCode : " + responseCode);
                    switch (responseCode) {
                        case HttpURLConnection.HTTP_OK:
                            InputStream inputStream = con.getInputStream();
                            ByteArrayOutputStream output = new ByteArrayOutputStream();
                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = inputStream.read(buffer)) != -1) {
                                output.write(buffer, 0, len);
                            }
                            String result = new String(output.toByteArray(), "UTF-8");
                            if (upLoadListener != null) {
                                upLoadListener.upLoadOK(c, (int) ((System.currentTimeMillis() - responseTime) / 1000), result);
                            }
                            inputStream.close();
                            output.close();
                            break;
                        default:
                            if (upLoadListener != null) {
                                upLoadListener.upLoadError(c, (int) ((System.currentTimeMillis() - responseTime) / 1000), responseCode);
                            }
                    }
                    ds.close();
                    con.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (e instanceof TimeoutException) {
                        if (upLoadListener != null) {
                            upLoadListener.timeOut(c, (int) ((System.currentTimeMillis() - responseTime) / 1000));
                        }
                    } else if (upLoadListener != null) {
                        upLoadListener.otherError(c, (int) ((System.currentTimeMillis() - responseTime) / 1000), e.getMessage());
                    }
                }
                return false;
            }
        });
    }

    /***
     * 传输文件
     * @param ds ：数据传输通道
     * @param params ：参数
     */
    private static void writeFileParam(Context c, long responseTime, String twoHyphens, String boundary, String end, DataOutputStream ds, HashMap<String, File> params, UpLoadListener upLoadListener) throws IOException {
        Set<String> keySet = params.keySet();

        for (String fileKey : keySet) {
            System.out.println("Load : file -- > key : " + fileKey);

            File file = params.get(fileKey);
            System.out.println("Load : file.getName() -- > file : " + file.getName());

            StringBuilder sb = new StringBuilder();
            sb.append(twoHyphens).append(boundary).append(end);
            sb.append("Content-Disposition:form-data; name=\"").append(fileKey).append("\"; filename=\"").
                    append(file.getName()).append("\"").append(end);
            sb.append("Content-Type:image/jpeg").append(end); // 这里配置的Content-type很重要的 ，用于服务器端辨别文件的类型的
            sb.append(end);
            ds.write(sb.toString().getBytes());

            InputStream is = new FileInputStream(file);
            upLoadListener.setMaxSize(c,Double.valueOf(String.valueOf(file.length())));
           /* try {
                Thread.sleep(1 * 1000);
                System.out.println("Load : sleep !");
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("Load : " + e.getMessage());
            }
            System.out.println("Load : awake !");*/
            byte[] bytes = new byte[1024];
            int len = 0;
            int curLen = 0;
            while ((len = is.read(bytes)) != -1) {
                try {
                    Thread.sleep(3);
                    System.out.println("Load : sleep !");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("Load : " + e.getMessage());
                }
                System.out.println("Load : awake !");
                curLen += len;
                ds.write(bytes, 0, len);
                upLoadListener.onUploadProcess(c, (int) ((System.currentTimeMillis() - responseTime) / 1000), curLen);
                System.out.println("Load : " + curLen);
            }
            upLoadListener.dissMissProgressDialog(c);
            ds.write(end.getBytes());
            byte[] end_data = (twoHyphens + boundary + twoHyphens + end).getBytes();
            ds.write(end_data);
            is.close();
            ds.flush();
        }
    }


    /**
     * 传普通参数(第二种方法)
     *
     * @param params 参数
     */
    private static void writeStringParam(long responseTime, String twoHyphens, String boundary, String end, DataOutputStream ds, HashMap<String, String> params, UpLoadListener upLoadListener) throws IOException {
        Set<String> keySet = params.keySet();
        StringBuilder body = new StringBuilder();
        for (String key : keySet) {
            System.out.println("Load : write string --> key : " + key);
            String value = params.get(key);
            System.out.println("Load : write string --> value : " + value);
            body.append(twoHyphens).append(boundary).append(end);
            body.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(end).append(end);
            body.append(value).append(end);
            ds.write(body.toString().getBytes());
        }
    }

    public abstract static class UpLoadListener {
        private double maxSize;
        protected ProgressDialog pd;
        public void setMaxSize (final Context c , final Double maxSize) {
            getActivity(c).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pd == null) {
                        pd = new ProgressDialog(c);
                        pd.setMessage("0%");
                    }
                    UpLoadListener.this.maxSize = maxSize;
                    if (!pd.isShowing()) {
                        System.out.println("Load : pd.show");
                        pd.show();
                    }
                }
            });
        }

        public String toPersent(Double allupSize, Double now) {
            DecimalFormat df = new DecimalFormat("0.00");
            String sdf = df.format(now/allupSize * 100);
            int in = 0;
            try {
                in = Integer.valueOf(sdf.substring(0,sdf.length() - 3));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return in + "%";
        }

        public void onUploadProcess(final Context c, int requestTime, final int upLoadingSize) {
            getActivity(c).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pd == null) {
                        pd = new ProgressDialog(c);
                    }
                    pd.setMax((int)maxSize);
                    pd.setProgress(upLoadingSize);
                    System.out.println("onUploadProcess : " + toPersent(maxSize, (double)upLoadingSize) + " maxSize : " + maxSize + " size : " + upLoadingSize);
                    pd.setMessage("正在上传图片 ："+toPersent(maxSize, (double)upLoadingSize));
                    if (!pd.isShowing()) {
                        System.out.println("Load : pd.show");
                        pd.show();
                    }
                }
            });
        }

        protected void dissMissProgressDialog(Context c) {
            if (pd != null) {
                getActivity(c).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Load : pd.dismiss");
                        pd.hide();
                        pd.dismiss();
                    }
                });
            }
        }

        public void upLoadOK(final Context c, int requestTime, final String result) {
            getActivity(c).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dissMissProgressDialog(c);
                    System.out.println("Load : result --> " + result);
                    Toast.makeText(c, "上传成功", Toast.LENGTH_LONG).show();
                }
            });
        }

        public void upLoadError(final Context c, int requestTime, int errorCode) {
            getActivity(c).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dissMissProgressDialog(c);
                    Toast.makeText(c, "上传失败", Toast.LENGTH_LONG).show();
                }
            });

        }

        /**
         * if (ContextCompat.checkSelfPermission(Default.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
         * //请求权限
         * ActivityCompat.requestPermissions(c, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1002);
         * //判断是否需要 向用户解释，为什么要申请该权限
         * ActivityCompat.shouldShowRequestPermissionRationale(c,Manifest.permission.READ_CONTACTS);
         * }
         */
        public void createFileError(final Context c, final File file) {
            getActivity(c).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dissMissProgressDialog(c);
                    Toast.makeText(c, "文件夹:" + file.getName() + " 创建失败", Toast.LENGTH_LONG).show();
                }
            });
        }

        public void timeOut(final Context c, int requestTime) {
            getActivity(c).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dissMissProgressDialog(c);
                    Toast.makeText(c, "网络连接超时", Toast.LENGTH_LONG).show();
                }
            });

        }

        public void deleteImageError(final Context c, final File imageFile) {
            getActivity(c).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dissMissProgressDialog(c);
                    Toast.makeText(c, imageFile.getPath() + "已存在，请手动删除" + imageFile.getName(), Toast.LENGTH_LONG).show();
                }
            });

        }

        public void otherError(final Context c, int requestTime, final String error) {
            getActivity(c).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dissMissProgressDialog(c);
                    Toast.makeText(c, error, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
//</editor-folder>

    private static class MyAsyncTask extends AsyncTask<Object, Object, Handler.Callback> {

        @Override
        protected Handler.Callback doInBackground(Object... params) {
            Object obj = params[0];
            Handler.Callback callback = (Handler.Callback) params[1];
            Message message = new Message();
            message.obj = obj;
            callback.handleMessage(message);
            return callback;
        }

        @Override
        protected void onPostExecute(Handler.Callback callback) {
            super.onPostExecute(callback);
        }
    }

    public static Activity getActivity (Context c) {
        if (c instanceof Activity) {
            return ((Activity) c);
        } else if (c instanceof ContextWrapper) {
            return ((Activity) ((ContextWrapper) c).getBaseContext());
        } else
            return null;
    }
}
