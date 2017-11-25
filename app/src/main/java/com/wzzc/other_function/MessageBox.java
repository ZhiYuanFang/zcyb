package com.wzzc.other_function;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wzzc.base.Default;
import com.wzzc.zcyb365.R;

/**
 * Created by mypxq on 16-7-16.
 * <p>
 * 对话框
 */
public class MessageBox {
    public static Dialog dialog;
    public static boolean autoDismiss = true;

    /**
     * @see MessageBox#Show(String, String)
     */
    public static void Show(String mess) {
        MessageBox.Show(Default.AppName, mess);
    }

    /**
     * 对用户选择没有过多操作
     */
    public static void Show(final String title, final String mess) {
        Default.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Context context = Default.getActivity();
                if (dialog == null || !dialog.isShowing()) {
                    dialog = new Dialog(context, R.style.CustomAlertDialog);
                }

                dialog.show();


                View view = LayoutInflater.from(context).inflate(R.layout.alertdialog, null);
                TextView tv_title = (TextView) view.findViewById(R.id.title);
                tv_msg = (TextView) view.findViewById(R.id.msg);
                tv_title.setText(title);
                tv_msg.setText(mess);
                tv_msg.setMovementMethod(new ScrollingMovementMethod());
                LinearLayout lv = (LinearLayout) view.findViewById(R.id.layout_button);
                Button bt = new Button(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                bt.setLayoutParams(lp);
                bt.setGravity(Gravity.CENTER);
                bt.setTextSize(16);
                bt.setTextColor(ContextCompat.getColor(context, R.color.tv_Red));
                bt.setText("确定");
                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (autoDismiss)
                            dialog.dismiss();
                    }
                });
                bt.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                lv.addView(bt);
                dialog.setContentView(view);
                Default.getActivity().getWindow().getAttributes().gravity = Gravity.CENTER;
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = Default.dip2px(300, context);
//            params.height = Default.dip2px(230, context);
                dialog.getWindow().setAttributes(params);

            }
        });

    }

    static TextView tv_msg;

    /**
     * 对用户选择进行进一步操作
     *
     * @see MessBtnBack
     */
    public static void Show(final String title, final String mess, final String[] button, final MessBtnBack back) {
        Default.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Context context = Default.getActivity();
                if (dialog == null || !dialog.isShowing()) {
                    dialog = new Dialog(context, R.style.CustomAlertDialog);
                }
                dialog.show();

                View view = LayoutInflater.from(context).inflate(R.layout.alertdialog, null);
                TextView tv_title = (TextView) view.findViewById(R.id.title);
                tv_msg = (TextView) view.findViewById(R.id.msg);
                tv_title.setText(title);
                tv_msg.setText("\u3000" + mess);
                tv_msg.setMovementMethod(new ScrollingMovementMethod());
                LinearLayout lv = (LinearLayout) view.findViewById(R.id.layout_button);
                if (button != null && button.length == 0) {
                    lv.setVisibility(View.GONE);
                } else {
                    lv.setWeightSum(button.length);
                    for (int i = 0; i < button.length; i++) {
                        TextView bt = new TextView(context);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        lp.weight = 1;
                        final int finalI = i;
                        bt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (autoDismiss) {
                                    dialog.dismiss();
                                }
                                if (back != null) {
                                    back.Back(finalI);
                                }
                            }
                        });
                        bt.setLayoutParams(lp);
                        bt.setGravity(Gravity.CENTER);
                        bt.setTextColor(ContextCompat.getColor(context, R.color.tv_Red));
                        bt.setTextSize(16);
                        bt.setText(button[i]);
                        lv.addView(bt);
                        //region 中间线
                        if (i < button.length - 1) {
                            TextView tv = new TextView(context);
                            LinearLayout.LayoutParams lp_tv = new LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT);
                            lp_tv.weight = 0;
                            tv.setLayoutParams(lp_tv);
                            tv.setBackgroundColor(ContextCompat.getColor(context, R.color.tv_hint_red));
                            lv.addView(tv);
                        }
                        //endregion
                    }
                }


                dialog.setContentView(view);
                Default.getActivity().getWindow().getAttributes().gravity = Gravity.CENTER;
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = Default.dip2px(300, context);
//        params.height = Default.dip2px(230,context);
                dialog.getWindow().setAttributes(params);

            }
        });
    }

    public static void Show(final String title, final View massage_view, final String[] button, final MessBtnBack back) {
        Default.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Context context = Default.getActivity();
                if (dialog == null || !dialog.isShowing()) {
                    dialog = new Dialog(context, R.style.CustomAlertDialog);
                }
                dialog.show();

                View view = LayoutInflater.from(context).inflate(R.layout.alertdialog, null);
                TextView tv_title = (TextView) view.findViewById(R.id.title);
                tv_title.setText(title);
                tv_msg = (TextView) view.findViewById(R.id.msg);
                tv_msg.setVisibility(View.GONE);
                LinearLayout layout_contain_msg = (LinearLayout) view.findViewById(R.id.layout_contain_msg);
                ScrollView scrollView = (ScrollView) view.findViewById(R.id.scrollview);
                scrollView.setVisibility(View.VISIBLE);
                layout_contain_msg.removeAllViews();
                layout_contain_msg.addView(massage_view);
                LinearLayout lv = (LinearLayout) view.findViewById(R.id.layout_button);
                if (button != null && button.length == 0) {
                    lv.setVisibility(View.GONE);
                } else {
                    lv.setWeightSum(button.length);
                    for (int i = 0; i < button.length; i++) {
                        Button bt = new Button(context);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        lp.weight = 1;
                        final int finalI = i;
                        bt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (autoDismiss) {
                                    dialog.dismiss();
                                }

                                if (back != null) {
                                    back.Back(finalI);
                                }
                            }
                        });
                        bt.setLayoutParams(lp);
                        bt.setGravity(Gravity.CENTER);
                        bt.setTextColor(ContextCompat.getColor(context, R.color.tv_Red));
                        bt.setTextSize(16);
                        bt.setText(button[i]);
                        bt.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                        lv.addView(bt);
                        //region 中间线
                        if (i < button.length - 1) {
                            TextView tv = new TextView(context);
                            LinearLayout.LayoutParams lp_tv = new LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT);
                            lp_tv.weight = 0;
                            tv.setLayoutParams(lp_tv);
                            tv.setBackgroundColor(ContextCompat.getColor(context, R.color.tv_hint_red));
                            lv.addView(tv);
                        }
                        //endregion
                    }
                }

                dialog.setContentView(view);
                Default.getActivity().getWindow().getAttributes().gravity = Gravity.CENTER;
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = Default.dip2px(300, context);
//        params.height = Default.dip2px(230,context);
                dialog.getWindow().setAttributes(params);
            }
        });

    }

    public static void Show(final String title, final View massage_view, final boolean auto, final String[] button, final MessBtnBack back) {
        Default.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                autoDismiss = auto;
                Context context = Default.getActivity();
                if (dialog == null || !dialog.isShowing()) {
                    dialog = new Dialog(context, R.style.CustomAlertDialog);
                }
                dialog.show();

                View view = LayoutInflater.from(context).inflate(R.layout.alertdialog, null);
                TextView tv_title = (TextView) view.findViewById(R.id.title);
                tv_title.setText(title);
                tv_msg = (TextView) view.findViewById(R.id.msg);
                tv_msg.setVisibility(View.GONE);
                LinearLayout layout_contain_msg = (LinearLayout) view.findViewById(R.id.layout_contain_msg);
                ScrollView scrollView = (ScrollView) view.findViewById(R.id.scrollview);
                scrollView.setVisibility(View.VISIBLE);
                layout_contain_msg.removeAllViews();
                layout_contain_msg.addView(massage_view);
                LinearLayout lv = (LinearLayout) view.findViewById(R.id.layout_button);
                if (button != null && button.length == 0) {
                    lv.setVisibility(View.GONE);
                } else {
                    lv.setWeightSum(button.length);
                    for (int i = 0; i < button.length; i++) {
                        Button bt = new Button(context);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        lp.weight = 1;
                        final int finalI = i;
                        bt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (autoDismiss)
                                    dialog.dismiss();
                                if (back != null) {
                                    back.Back(finalI);
                                }
                            }
                        });
                        bt.setLayoutParams(lp);
                        bt.setGravity(Gravity.CENTER);
                        bt.setTextColor(ContextCompat.getColor(context, R.color.tv_Red));
                        bt.setTextSize(16);
                        bt.setText(button[i]);
                        bt.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                        lv.addView(bt);
                        //region 中间线
                        if (i < button.length - 1) {
                            TextView tv = new TextView(context);
                            LinearLayout.LayoutParams lp_tv = new LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT);
                            lp_tv.weight = 0;
                            tv.setLayoutParams(lp_tv);
                            tv.setBackgroundColor(ContextCompat.getColor(context, R.color.tv_hint_red));
                            lv.addView(tv);
                        }
                        //endregion
                    }
                }

                dialog.setContentView(view);
                Default.getActivity().getWindow().getAttributes().gravity = Gravity.CENTER;
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = Default.dip2px(300, context);
//        params.height = Default.dip2px(230,context);
                dialog.getWindow().setAttributes(params);
            }
        });

    }

    public static void ShowFlexibleDialog(final String title, final View massage_view, final String[] button, final MessBtnBack back) {
        Default.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Context context = Default.getActivity();
                if (dialog == null || !dialog.isShowing()) {
                    dialog = new Dialog(context, R.style.CustomAlertDialog);
                }
                dialog.show();

                @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.alertdialog, null);
                TextView tv_title = (TextView) view.findViewById(R.id.title);
                tv_title.setText(title);
                tv_msg = (TextView) view.findViewById(R.id.msg);
                tv_msg.setVisibility(View.GONE);
                LinearLayout layout_contain_msg = (LinearLayout) view.findViewById(R.id.layout_contain_msg);
                ScrollView scrollView = (ScrollView) view.findViewById(R.id.scrollview);
                scrollView.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams lp_scroll = scrollView.getLayoutParams();
                lp_scroll.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                scrollView.setLayoutParams(lp_scroll);
                layout_contain_msg.removeAllViews();
                layout_contain_msg.addView(massage_view);
                LinearLayout lv = (LinearLayout) view.findViewById(R.id.layout_button);
                if (button != null && button.length == 0) {
                    lv.setVisibility(View.GONE);
                } else {
                    lv.setWeightSum(button.length);
                    for (int i = 0; i < button.length; i++) {
                        Button bt = new Button(context);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        lp.weight = 1;
                        final int finalI = i;
                        bt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (autoDismiss)
                                    dialog.dismiss();
                                if (back != null) {
                                    back.Back(finalI);
                                }
                            }
                        });
                        bt.setLayoutParams(lp);
                        bt.setGravity(Gravity.CENTER);
                        bt.setTextColor(ContextCompat.getColor(context, R.color.tv_Red));
                        bt.setTextSize(16);
                        bt.setText(button[i]);
                        bt.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                        lv.addView(bt);
                        //region 中间线
                        if (i < button.length - 1) {
                            TextView tv = new TextView(context);
                            LinearLayout.LayoutParams lp_tv = new LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT);
                            lp_tv.weight = 0;
                            tv.setLayoutParams(lp_tv);
                            tv.setBackgroundColor(ContextCompat.getColor(context, R.color.tv_hint_red));
                            lv.addView(tv);
                        }
                        //endregion
                    }
                }

                dialog.setContentView(view);
                Default.getActivity().getWindow().getAttributes().gravity = Gravity.CENTER;
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = Default.dip2px(300, context);
//        params.height = Default.dip2px(230,context);
                dialog.getWindow().setAttributes(params);
            }
        });

    }

    public static void Show(final Context context ,final String title, final String mess, final View[] button, final MessBtnBack back, final TouchBack touchBack) {
        ImageHelper.getActivity(context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (context == null) {
                    return;
                }

                if (dialog == null || !dialog.isShowing()) {
                    dialog = new Dialog(context, R.style.CustomAlertDialog);
                }
                System.out.println("dialog : " + dialog + " dialog.isShowing() : " + dialog.isShowing());
                dialog.show();

                View view = LayoutInflater.from(context).inflate(R.layout.alertdialog, null);
                TextView tv_title = (TextView) view.findViewById(R.id.title);
                tv_msg = (TextView) view.findViewById(R.id.msg);
                tv_title.setText(title);
                tv_msg.setText("\u3000" + mess);
                tv_msg.setMovementMethod(new ScrollingMovementMethod());
                LinearLayout lv = (LinearLayout) view.findViewById(R.id.layout_button);
                if (button != null && button.length == 0) {
                    lv.setVisibility(View.GONE);
                } else {
                    lv.setWeightSum(button.length);
                    for (int i = 0; i < button.length; i++) {
                        View v = button[i];
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        lp.weight = 1;
                        final int finalI = i;
                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (autoDismiss)
                                    dialog.dismiss();
                                if (back != null) {
                                    back.Back(finalI);
                                }
                            }
                        });
                        v.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                if (touchBack != null) {
                                    touchBack.touchListener(finalI, v, event);
                                }
                                return false;
                            }
                        });
                        v.setLayoutParams(lp);
                        lv.addView(v);
                        //region 中间线
                        if (i < button.length - 1) {
                            TextView tv = new TextView(context);
                            LinearLayout.LayoutParams lp_tv = new LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT);
                            lp_tv.weight = 0;
                            tv.setLayoutParams(lp_tv);
                            tv.setBackgroundColor(ContextCompat.getColor(context, R.color.tv_hint_red));
                            lv.addView(tv);
                        }
                        //endregion

                    }
                }


                dialog.setContentView(view);
                Default.getActivity().getWindow().getAttributes().gravity = Gravity.CENTER;
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = Default.dip2px(300, context);
//        params.height = Default.dip2px(230, context);
                dialog.getWindow().setAttributes(params);
            }
        });
    }

    public static void Show(final Context c, final String title, final String mess, final String[] button, final MessBtnBack back) {
        Default.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final View[] views = new View[button.length];
                for (int i = 0; i < button.length; i++) {
                    TextView tv = new TextView(c);
                    if(button.length > 2){
                        tv.setTextSize(Default.dip2px(4,c));
                    }
                    tv.setText(button[i]);
                    tv.setTextColor(ContextCompat.getColor(c, R.color.tv_Black));
                    tv.setGravity(Gravity.CENTER);
                    if (i == button.length - 1) {
                        tv.setTextColor(ContextCompat.getColor(c, R.color.tv_White));
                        tv.setBackground(ContextCompat.getDrawable(c, R.drawable.bg_dialog_just_right_bottom));
                    }
                    views[i] = tv;
                }

                Show(c , title, mess, views, back, new TouchBack() {
                    @Override
                    public void touchListener(int index, View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                for (int i = 0; i < views.length; i++) {
                                    views[i].setBackgroundColor(ContextCompat.getColor(c, android.R.color.transparent));
                                    ((TextView) views[i]).setTextColor(ContextCompat.getColor(c, R.color.tv_Black));
                                }
                                Drawable bg;
                                if (index == 0) {
                                    bg = ContextCompat.getDrawable(c, R.drawable.bg_dialog_just_left_bottom);
                                } else if (index == views.length - 1) {
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
                    }
                });
            }
        });

    }

    public static void Show(final Context c, final String title, final String mess) {
        Default.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog == null || !dialog.isShowing()) {
                    dialog = new Dialog(c, R.style.CustomAlertDialog);
                }

                dialog.show();

                View view = LayoutInflater.from(c).inflate(R.layout.alertdialog, null);
                TextView tv_title = (TextView) view.findViewById(R.id.title);
                tv_msg = (TextView) view.findViewById(R.id.msg);
                tv_title.setText(title);
                tv_msg.setText(mess);
                tv_msg.setMovementMethod(new ScrollingMovementMethod());
                LinearLayout lv = (LinearLayout) view.findViewById(R.id.layout_button);
                lv.setVisibility(View.GONE);
                dialog.setContentView(view);
                Default.getActivity().getWindow().getAttributes().gravity = Gravity.CENTER;
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = Default.dip2px(300, c);
                dialog.getWindow().setAttributes(params);
            }
        });

    }

    public static void Show(final String title, final String[] messes, final MessBtnBack back) {
        Default.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog == null || !dialog.isShowing()) {
                    dialog = new Dialog(Default.getActivity(), R.style.CustomAlertDialog);
                }

                dialog.show();

                View view = LayoutInflater.from(Default.getActivity()).inflate(R.layout.alertdialog, null);
                TextView tv_title = (TextView) view.findViewById(R.id.title);
                tv_title.setText(title);
                tv_msg = (TextView) view.findViewById(R.id.msg);
                tv_msg.setVisibility(View.GONE);
                LinearLayout layout_contain_msg = (LinearLayout) view.findViewById(R.id.layout_contain_msg);
                ScrollView scrollView = (ScrollView) view.findViewById(R.id.scrollview);
                scrollView.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams lp_scroll = scrollView.getLayoutParams();
                lp_scroll.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                scrollView.setLayoutParams(lp_scroll);
                layout_contain_msg.removeAllViews();
                int space = Default.dip2px(7, Default.getActivity());
                for (int i = 0; i < messes.length; i++) {
                    TextView tv = new TextView(Default.getActivity());
                    tv.setText(messes[i]);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(space, space, space, space);
                    tv.setPadding(space, space, space, space);
                    tv.setLayoutParams(lp);
                    tv.setBackground(ContextCompat.getDrawable(Default.getActivity(), R.drawable.bg_circle_red));
                    final int finalI = i;
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            back.Back(finalI);
                        }
                    });
                    layout_contain_msg.addView(tv);
                }

                LinearLayout lv = (LinearLayout) view.findViewById(R.id.layout_button);
                lv.setVisibility(View.GONE);
                dialog.setContentView(view);
                Default.getActivity().getWindow().getAttributes().gravity = Gravity.CENTER;
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = Default.dip2px(300, Default.getActivity());
                dialog.getWindow().setAttributes(params);
            }
        });

    }

    public static void changeMessage(final String mess) {
        Default.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tv_msg != null) {
                    tv_msg.setText(mess);
                }
            }
        });

    }

    public static void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public static void createNewDialog () {
        dialog = new Dialog(Default.getActivity(), R.style.CustomAlertDialog);
    }

    public static void setDialogCancelable(Boolean cancelable) {
        if (dialog != null) {
            dialog.setCancelable(cancelable);
        }
    }

    /**
     * {@link MessageBox#Show(String, String, String[], MessBtnBack)} 对话框中选择按钮的操作
     */
    public interface MessBtnBack {
        void Back(int index);
    }

    public interface TouchBack {
        void touchListener(int index, View v, MotionEvent event);
    }

}
