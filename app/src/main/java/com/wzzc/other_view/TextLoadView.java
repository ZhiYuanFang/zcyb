package com.wzzc.other_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.progressDialog.CustomProgressDialog;
import com.wzzc.other_view.progressDialog.CustomProgressDialogView;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/4/6.
 */

public class TextLoadView extends BaseView {
    @ViewInject(R.id.tv_text)
    private TextView tv_text;
    @ViewInject(R.id.progressBar)
    CustomProgressDialogView progressDialogView;
    public TextLoadView(Context context) {
        super(context);
        init();
    }

    public TextLoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init () {

    }

    public void setTv_text_Gravity (int gravity) {
        tv_text.setGravity(gravity);
    }

    public void setTv_textColor (@ColorInt int color) {
        tv_text.setTextColor(color);
    }

    public TextView getTv_text () {
        return tv_text;
    }

    public void isLoading () {
        progressDialogView.setVisibility(VISIBLE);
        tv_text.setVisibility(GONE);
    }

    public void loadOk (CharSequence text) {
        tv_text.setText(text);
        tv_text.setVisibility(VISIBLE);
        progressDialogView.setVisibility(GONE);

    }

    public void loadOk (CharSequence text , @ColorRes int colorRes) {
        tv_text.setText(text);
        tv_text.setTextColor(ContextCompat.getColor(getContext(),colorRes));
        tv_text.setVisibility(VISIBLE);
        progressDialogView.setVisibility(GONE);


    }

    public void noData (CharSequence text) {
        tv_text.setText(text);
        tv_text.setVisibility(VISIBLE);
        progressDialogView.setVisibility(GONE);

    }
    public void noData (CharSequence text, @ColorRes int colorRes) {
        tv_text.setText(text);
        tv_text.setTextColor(ContextCompat.getColor(getContext(),colorRes));
        tv_text.setVisibility(VISIBLE);
        progressDialogView.setVisibility(GONE);

    }
    public void noData () {
        tv_text.setText("加载失败");
        tv_text.setVisibility(VISIBLE);
        progressDialogView.setVisibility(GONE);
    }
}
