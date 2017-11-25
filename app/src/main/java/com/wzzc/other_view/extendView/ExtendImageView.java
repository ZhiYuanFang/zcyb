package com.wzzc.other_view.extendView;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.wzzc.base.Default;
import com.wzzc.other_function.ImageHelper;
import com.wzzc.zcyb365.R;

/**
 * Created by mypxq on 16-7-16.
 */
public class ExtendImageView extends AppCompatImageView {

    /**
     * 图片路径
     */
    public String imagepath = "";
//    private boolean isloaded = false;
    /**
     * 默认外切
     */
    private ScaleType nowScaleType = ScaleType.FIT_XY;

    public ExtendImageView(Context context) {
        super(context);
    }

    public ExtendImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public float radio;

    /**
     * 添加图片
     */
    public void setImage() {
        ImageHelper.handlerImage(getContext(), imagepath, getViewWidth(), getViewHeight(), new ImageHelper.HandlerImage() {
            @Override
            protected void imageBack(Bitmap bitmap) {
                if (bitmap != null) {
                    setScaleType(nowScaleType);
                    setImageBitmap(bitmap);
                } else {
                    setLoadingFail();
                }
            }
        });
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        super.setScaleType(scaleType);
        nowScaleType = scaleType;
    }

    /**
     * 设置加载界面
     */
    public void setLoading() {
        super.setScaleType(ScaleType.CENTER_INSIDE);
        setImageBitmap(null);
        setImageResource(R.drawable.bg_circle_solid_light_gray);
    }

    /**
     * 设置加载失败界面
     */
    public void setLoadingFail() {
        super.setScaleType(ScaleType.CENTER_INSIDE);
        setImageBitmap(null);
        setImageResource(R.drawable.loading_fail);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        if (radio != 0) {
            bm = ImageHelper.tailorBitmapToRoundCorner(bm, radio);
        }
        super.setImageBitmap(bm);
    }

    /**
     * <table >
     * <tr>
     * <td style="border: solid thin purple ">
     * <font color=blue>path</font> == {@link ExtendImageView#imagepath}
     * </td>
     * <td style="border: solid thin purple ">
     * <font color=orange>return</font>(无操作)
     * </td>
     * </tr>
     * <tr>
     * <td style="border: solid thin purple ">
     * <font color=blue>path</font> != {@link ExtendImageView#imagepath}
     * </td>
     * <td style="border: solid thin purple ">
     * 1、设置加载界面{@link ExtendImageView#setLoading()}<br>
     * 2、当加载过图片了<font color=orange>&</font>{@link ExtendImageView#imagepath}不为空数据 ，
     * 设置ImageView 让他自动加载图片 并存储到缓存之中
     * </td>
     * </tr>
     * </table>
     *
     * @param path 图片路径
     */
    public void setPath(String path) {
        if (this.imagepath.equals(path)) {
            return;
        }
        imagepath = path;
        setLoading();
        if (!"".equals(imagepath)) {
            ImageHelper.handlerImage(getContext(), imagepath, getViewWidth(), getViewHeight(), new ImageHelper.HandlerImage() {
                @Override
                protected void imageBack(Bitmap bitmap) {
                    if (bitmap != null) {
                        setScaleType(nowScaleType);
                        ExtendImageView.this.setImageBitmap(bitmap);
                    } else{
                        setLoadingFail();
                    }

                }
            });
        }
    }

    public int getViewHeight() {
        /*while (getMeasuredHeight() <= 0){
            measure(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        }*/
        if (getMeasuredHeight() <= 0) {

            return 48;
        }
        return getMeasuredHeight();
    }

    public int getViewWidth() {
        /*while (getMeasuredWidth() <= 0){
            measure(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        }*/
        if (getMeasuredWidth() <= 0) {
            return 32;
        }
        return getMeasuredWidth();
    }

    /**
     * 将<font color = blue>layout</font>作为容器 ， 存放{@link ExtendImageView}对象
     *
     * @param layout 一个布局容器
     * @return 一个 {@link ExtendImageView}对象
     */
    public static ExtendImageView Create(final RelativeLayout layout) {
        final ExtendImageView extendImageView = new ExtendImageView(layout.getContext());
        Default.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                layout.addView(extendImageView);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) extendImageView.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                extendImageView.setLayoutParams(params);
            }
        });
        return extendImageView;
    }

    /**
     * 图片加载完毕
     */
    interface ImageLoadFinsh {
        void OnFinsh(Bitmap image);
    }
}
