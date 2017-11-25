package com.wzzc.other_view.progressDialog;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/4/6.
 */

public class CustomProgressDialogView extends BaseView {
    @ViewInject(R.id.progress)
    private ProgressBar progressBar;
    @ViewInject(R.id.tv_loadingmsg)
    TextView tv_loadingmsg;
    @ViewInject(R.id.layout_progress)
    RelativeLayout layout_progress;
    public CustomProgressDialogView(Context context) {
        super(context);
        init();
    }

    public CustomProgressDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init () {
        progressBar.setIndeterminateDrawable(ContextCompat.getDrawable(getContext(),R.drawable.progressbar_background));
    }

    public void showProgress () {
        progressBar.setVisibility(VISIBLE);
    }

    public void setImage (@DrawableRes int drawable) {
        progressBar.setVisibility(GONE);
    }
    public void setMessage (CharSequence charSequence) {
        tv_loadingmsg.setText(charSequence);
    }
}
