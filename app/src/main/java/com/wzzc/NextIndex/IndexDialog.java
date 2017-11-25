package com.wzzc.NextIndex;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wzzc.base.Default;
import com.wzzc.other_function.ImageHelper;
import com.wzzc.other_function.MessageBox;
import com.wzzc.zcyb365.R;

import java.text.DecimalFormat;

/**
 * Created by by Administrator on 2017/8/17.
 */

public class IndexDialog {
    private static Dialog dialog;

    /**
     * 更新通知对话框
     */
    public static void showUpdateNotificationDialog(
            final Context c, final String title, final String mess, final String[] button, final MessageBox.MessBtnBack back) {
        ImageHelper.getActivity(c).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog == null || !dialog.isShowing()) {
                    dialog = new Dialog(c, R.style.CustomAlertDialog);
                }
                dialog.setCancelable(false);
                dialog.show();
                View view = LayoutInflater.from(c).inflate(R.layout.layout_dialog_update_notify, null);
                TextView tv_title = (TextView) view.findViewById(R.id.title);
                TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);
                tv_title.setText(title);
                if (mess == null || mess.length() == 0) {
                    tv_msg.setText("1、优化界面，修复潜在bug！");
                } else {
                    tv_msg.setText(mess);
                }
                tv_msg.setMovementMethod(new ScrollingMovementMethod());
                LinearLayout lv = (LinearLayout) view.findViewById(R.id.layout_button);
                if (button != null && button.length == 0) {
                    lv.setVisibility(View.GONE);
                } else {
                    lv.setWeightSum(button.length);
                    final View[] views = new View[button.length];
                    for (int i = 0; i < button.length; i++) {
                        TextView tv = new TextView(c);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        lp.weight = 1;
                        final int finalI = i;
                        tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (back != null) {
                                    back.Back(finalI);
                                }
                                dismissDialog(c);
                            }
                        });
                        tv.setLayoutParams(lp);
                        tv.setGravity(Gravity.CENTER);
                        tv.setTextColor(ContextCompat.getColor(c, R.color.tv_Red));
                        tv.setTextSize(16);
                        tv.setText(button[i]);
                        if (i == button.length - 1) {
                            tv.setTextColor(ContextCompat.getColor(c, R.color.tv_White));
                            tv.setBackground(ContextCompat.getDrawable(c, R.drawable.bg_dialog_just_right_bottom));
                        }
                        views[i] = tv;
                        lv.addView(tv);
                        //region 中间线
                        if (i < button.length - 1) {
                            TextView tv_line = new TextView(c);
                            LinearLayout.LayoutParams lp_tv = new LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT);
                            lp_tv.weight = 0;
                            tv_line.setLayoutParams(lp_tv);
                            tv_line.setBackgroundColor(ContextCompat.getColor(c, R.color.tv_hint_red));
                            lv.addView(tv_line);
                        }
                        //endregion
                    }

                    for (int index = 0 ; index < views.length ; index ++ ) {
                        View v = views[index];
                        final int finalIndex = index;
                        v.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                switch (event.getAction()) {
                                    case MotionEvent.ACTION_DOWN:
                                        for (int i = 0; i < views.length; i++) {
                                            views[i].setBackgroundColor(ContextCompat.getColor(c, android.R.color.transparent));
                                            ((TextView) views[i]).setTextColor(ContextCompat.getColor(c, R.color.tv_Black));
                                        }
                                        Drawable bg;
                                        if (finalIndex == 0) {
                                            bg = ContextCompat.getDrawable(c, R.drawable.bg_dialog_just_left_bottom);
                                        } else if (finalIndex == views.length - 1) {
                                            bg = ContextCompat.getDrawable(c, R.drawable.bg_dialog_just_right_bottom);
                                        } else {
                                            //中间
                                            bg = ContextCompat.getDrawable(c, R.drawable.bg_dialog_middon_bottom);
                                        }
                                        v.setBackground(bg);
                                        ((TextView) v).setTextColor(ContextCompat.getColor(c, R.color.tv_White));
                                        break;
                                    case MotionEvent.ACTION_CANCEL:
                                        break;
                                    default:
                                }
                                return false;
                            }
                        });
                    }
                }
                dialog.setContentView(view);
                Default.getActivity().getWindow().getAttributes().gravity = Gravity.CENTER;
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = Default.dip2px(300, c);
                dialog.getWindow().setAttributes(params);
            }
        });
    }

    private static TextView tv_info;//更新进度
    private static RoundProgressBar progressBar;//进度条
    public static void showUpdateProgressDialog(final Context c, final Double maxSize, final Double curSize) {
        if (maxSize <= 0) {
            return;
        }
        ImageHelper.getActivity(c).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tv_info == null) {
                    //region 创建dialog
                    dialog = new Dialog(c, R.style.CustomAlertDialog);
                    dialog.setCancelable(false);
                    dialog.show();
                    View view = LayoutInflater.from(c).inflate(R.layout.layout_dialog_update_loading, null);
                    tv_info = (TextView) view.findViewById(R.id.tv_info);
                    progressBar = (RoundProgressBar) view.findViewById(R.id.roundProgressBar);
                    progressBar.setMax((int) (double) maxSize);
                    dialog.setContentView(view);
                    Default.getActivity().getWindow().getAttributes().gravity = Gravity.CENTER;
                }
                progressBar.setProgress((int) (double) curSize);
                tv_info.setText(toPersent(maxSize, curSize));
            }
        });
    }
    private static String toPersent(Double allupSize, Double now) {
        DecimalFormat df = new DecimalFormat("0.00");
        String sdf = df.format(now / allupSize * 100);
        int in = 0;
        try {
            in = Integer.valueOf(sdf.substring(0, sdf.length() - 3));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return in + "%";
    }
    public static void dismissDialog(Context c) {
        ImageHelper.getActivity(c).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog != null) {
                    dialog.hide();
                    dialog.dismiss();
                }
            }
        });
    }
}
