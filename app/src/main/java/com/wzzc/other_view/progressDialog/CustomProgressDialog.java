package com.wzzc.other_view.progressDialog;

/**
 * Created by by Administrator on 2017/2/20.
 */

import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wzzc.base.Default;
import com.wzzc.zcyb365.R;


/********************************************************************
 * [Summary]
 *       TODO 请在此处简要描述此类所实现的功能。因为这项注释主要是为了在IDE环境中生成tip帮助，务必简明扼要
 * [Remarks]
 *       TODO 请在此处详细描述类的功能、调用方法、注意事项、以及与其它类的关系.
 *******************************************************************/

public class CustomProgressDialog extends Dialog {

    private CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public static CustomProgressDialog createDialog(Context context, Integer style) {
        if (style == null) {
            style = R.style.CustomProgressDialog;
        }
        CustomProgressDialog customProgressDialog = new CustomProgressDialog(context, style);

//        customProgressDialog.setCancelable(false);
        customProgressDialog.setContentView(LayoutInflater.from(context).inflate(R.layout.layout_special_dialog,null));
        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        ProgressBar progressBar = (ProgressBar) customProgressDialog.findViewById(R.id.progressBar);
        progressBar.setIndeterminateDrawable(ContextCompat.getDrawable(context,R.drawable.progressbar_background));
//        GifImageView gifImageView = (GifImageView) customProgressDialog.findViewById(R.id.giv);
//        gifImageView.setImageResource(R.drawable.dialog);
        return customProgressDialog;
    }

    public void showProgress(String message) {
        if (message != null) {
            setMessage(message);
        }
        show();
    }

    public void changeMessage (String message) {
        if (message != null) {
            setMessage(message);
        }
    }

    @Override
    public void show() {
                if (!isShowing()) {
                    try {
                        super.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
    }

    /**
     * [Summary]
     * setMessage 提示内容
     *
     * @param strMessage
     * @return
     */
    public void setMessage(final String strMessage) {
        final TextView tvMsg = (TextView) findViewById(R.id.tv_loadingmsg);

        Default.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tvMsg != null) {
                    tvMsg.setText(strMessage);
                }
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        System.out.println("dialog dismiss");
    }
}