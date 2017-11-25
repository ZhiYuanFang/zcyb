package com.wzzc.other_view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.wzzc.base.Default;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/7/14.
 * 悬浮按钮
 */

@SuppressLint("AppCompatCustomView")
public class FloatButton extends Button {
    private static final String TAG = "FloatButton";
    WindowManager.LayoutParams wmParams;
    WindowManager mWindowManager;
    public FloatButton(Context context) {
        super(context);
        init();
    }

    public FloatButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setWindowManager (WindowManager windowManager){//getSystemService(Context.WINDOW_SERVICE);
        mWindowManager = windowManager;
        if (getParent() != null) {
            if (getParent() instanceof RelativeLayout) {
                ((RelativeLayout)getParent()).removeView(this);
            }
            if (getParent() instanceof WindowManager) {
                ((WindowManager)getParent()).removeView(this);
            }
        }
        mWindowManager.addView(this, wmParams);
    }

    protected void init (){
        wmParams = new WindowManager.LayoutParams();
        //region wmParams
        //设置window type
        wmParams.type = WindowManager.LayoutParams.TYPE_TOAST/*TYPE_PHONE*/;
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧
        wmParams.gravity = Gravity.START | Gravity.TOP;
        //设置悬浮窗口长宽数据
        wmParams.width = wmParams.height = Default.dip2px(30,getContext());
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = Default.getScreenHeight((Activity) getContext())/2;


        //endregion
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                wmParams.y = (int) event.getRawY() - getMeasuredHeight()/2 - Default.getStateBarHeight();
                switch (event.getAction()){
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_DOWN:
                        break;
                    default:
                        wmParams.x = (int) event.getRawX();
                        if (mWindowManager != null) {
                            mWindowManager.updateViewLayout(FloatButton.this, wmParams);
                        } else {
                            Default.showToast("请为"+getContext().getString(R.string.app_name)+"授于浮动权限");
                        }
                        pull();
                }
                return false;
            }
        });

        pull();
    }

    private void pull () {
        if (wmParams.x <= Default.dip2px(30,getContext())){
            wmParams.x = 0;
            if (mWindowManager != null) {
                mWindowManager.updateViewLayout(FloatButton.this, wmParams);
            }
            setTranslationX(-getMeasuredWidth()/2);
        }else
        if (wmParams.x >= Default.getScreenWidth((Activity) getContext()) - getMeasuredWidth() - Default.dip2px(30,getContext())) {
            wmParams.x = Default.getScreenWidth((Activity) getContext()) - getMeasuredWidth();
            if (mWindowManager != null) {
                mWindowManager.updateViewLayout(FloatButton.this, wmParams);
            }
            setTranslationX(getMeasuredWidth()/2);
        } else {
            setTranslationX(0);
        }

    }
}
