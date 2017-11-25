package com.wzzc.other_view.production.detail.gcb;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wzzc.NextIndex.views.e.LoginActivity;
import com.wzzc.NextIndex.views.e.User;
import com.wzzc.base.Default;
import com.wzzc.base.ExtendBaseActivity;
import com.wzzc.base.ExtendBaseView;
import com.wzzc.base.annotation.OnClick;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.index.ShoppingCart.ShoppingCartActivity;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.CallPhone;
import com.wzzc.other_function.FileInfo;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.other_view.SlideView.slidePager.SlidePagerCountView;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.other_view.extendView.scrollView.ExtendScrollView;
import com.wzzc.other_view.extendView.scrollView.UIScrollListener;
import com.wzzc.other_view.extendView.scrollView.UIScrollView;
import com.wzzc.other_view.production.detail.gcb.main_view.DetailBottomView;
import com.wzzc.other_view.production.detail.gcb.main_view.DetailMiddleView;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by toutou on 2017/5/11.
 *
 */

public class DetailGcbActivity extends ExtendBaseActivity {
    private static final String TAG = "DetailGcbActivity";
    public static final String GOODSID = "id";
    //region 组件
    @ViewInject(R.id.uisv)
    UIScrollView uisv;
    @ViewInject(R.id.dtv)
    RelativeLayout dtv;
    @ViewInject(R.id.esv)
    ExtendScrollView esv;
    @ViewInject(R.id.dbv)
    DetailBottomView dbv;
    @ViewInject(R.id.dmv)
    DetailMiddleView dmv;
    @ViewInject(R.id.viewPager)
    ViewPager viewPager;
    @ViewInject(R.id.pageCount)
    SlidePagerCountView pageCount;
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init() {
        // region ```
        uisv.setScrollChange(new UIScrollListener() {
            @Override
            public void onScrollChanged(int top) {

            }

            @Override
            public boolean onScrollEnd(int height) {
                int nowheight = height - esv.getVerticalOffset();
                int cheight = Default.dip2px(44, DetailGcbActivity.this);
                if (nowheight > cheight) {
                    final int contentheight = uisv.getHeight();
                    ValueAnimator animator = ValueAnimator.ofInt(0, contentheight);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int value = (int) animation.getAnimatedValue();

                            RelativeLayout.LayoutParams paras1 = (RelativeLayout.LayoutParams) esv.getLayoutParams();
                            paras1.height = contentheight - value;
                            esv.setLayoutParams(paras1);

                            RelativeLayout.LayoutParams paras2 = (RelativeLayout.LayoutParams) dbv.getLayoutParams();
                            paras2.height = value;
                            dbv.setLayoutParams(paras2);
                        }
                    });

                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            RelativeLayout.LayoutParams paras1 = (RelativeLayout.LayoutParams) esv.getLayoutParams();
                            paras1.height = 0;
                            esv.setLayoutParams(paras1);

                            RelativeLayout.LayoutParams paras2 = (RelativeLayout.LayoutParams) dbv.getLayoutParams();
                            paras2.height = RelativeLayout.LayoutParams.MATCH_PARENT;
                            dbv.setLayoutParams(paras2);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    animator.setDuration(300);
                    animator.start();
                    return true;
                }
                return false;
            }
        });
        //endregion

        super.init();
    }

    public void RestoreView() {
        final int contentheight = uisv.getHeight();
        ValueAnimator animator = ValueAnimator.ofInt(0, contentheight);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();

                RelativeLayout.LayoutParams paras1 = (RelativeLayout.LayoutParams) esv.getLayoutParams();
                paras1.height = value;
                esv.setLayoutParams(paras1);

                RelativeLayout.LayoutParams paras2 = (RelativeLayout.LayoutParams) dbv.getLayoutParams();
                paras2.height = contentheight - value;
                dbv.setLayoutParams(paras2);
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                RelativeLayout.LayoutParams paras1 = (RelativeLayout.LayoutParams) esv.getLayoutParams();
                paras1.height = RelativeLayout.LayoutParams.MATCH_PARENT;
                esv.setLayoutParams(paras1);

                RelativeLayout.LayoutParams paras2 = (RelativeLayout.LayoutParams) dbv.getLayoutParams();
                paras2.height = 0;
                dbv.setLayoutParams(paras2);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(300);
        animator.start();
    }

    @Override
    protected String getFileKey() {
        return TAG + GetIntentData(GOODSID);
    }

    @Override
    protected ExtendBaseView.ServerCallBack serverCallBack() {
        return new ExtendBaseView.ServerCallBack() {
            @Override
            public void callBack(Object json_data) {
                JSONObject sender = (JSONObject) json_data;
                try {
                    AddTitle(sender.getString("goods_name"));
                    //region TOP
                    ExtendImageView eiv = ExtendImageView.Create(dtv);
                    eiv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    eiv.setPath(sender.getJSONObject("img").getString("url"));
                    //endregion
                    dmv.setInfo(sender);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    protected void setInfoFromService(String type) {
        JSONObject para = new JSONObject();
        try {
            para.put("goods_id", GetIntentData(GOODSID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(this, "goods", !hsf, para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                FileInfo.SetUserString(getFileKey(),sender.toString(),DetailGcbActivity.this);
                try {
                    initialized(sender,serverCallBack());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @OnClick({R.id.lab_factory, R.id.lab_factory1, R.id.btn_addcart, R.id.btn_buying})
    public void btn_bottom_click(final View view) {
        int tag = Integer.parseInt(view.getTag().toString());
        if (tag == 0) {
            if(User.isLogin()){
                JSONObject para = new JSONObject();
                try {
                    para.put("goods_id", GetIntentData(GOODSID).toString());
                    AsynServer.BackObject(DetailGcbActivity.this , "user/collect/create", para, new AsynServer.BackObject() {
                        @Override
                        public void Back(JSONObject sender) {
                            JSONObject json_status = JsonClass.jj(sender,"status");
                            if (JsonClass.ij(json_status,"succeed") == 1) {
                                MessageBox.Show("该商品已经成功地加入了您的收藏夹。");
                            }  else {
                                Default.showToast("操作失败,请确认登陆", Toast.LENGTH_LONG);
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                MessageBox.Show(this, Default.AppName, "前往个人中心登陆！", new String[]{"取消", "登陆"}, new MessageBox.MessBtnBack() {
                    @Override
                    public void Back(int index) {
                        switch (index) {
                            case 0:
                                break;
                            case 1:
                                AddActivity(LoginActivity.class);
                                break;
                            default:
                        }
                    }
                });
            }

        } else if (tag == 1) {
            CallPhone.call(CallPhone.KEFU);
        } else if (tag == 2) {
            if (!dmv.hasCheckedSelect) {
                //防快手
                return;
            }
            if (User.isLogin()) {
                JSONObject para = new JSONObject();
                try {
                    para.put("goods_id", GetIntentData(GOODSID).toString());
                    para.put("number", dmv.GetGoodsNumber() + "");
                    ArrayList<String> list = dmv.GetSpec();
                    if (list == null) {

                        MessageBox.Show("规格没有选择全");
                        return;
                    }

                    StringBuilder strspec = new StringBuilder();
                    for (int i = 0; i < list.size(); i++) {
                        strspec.append(list.get(i));
                        strspec.append(",");
                    }
                    para.put("spec", strspec.substring(0, strspec.length()));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //region 购物车动画
                // TODO: 2017/2/18 购物车动画
            /*//region animator

            //创建跳转动画对像
            final ImageView img = new ImageView(this);
            img.setBackgroundColor(Color.RED);

            detail_layout.addView(img);

            PointF startValue = new PointF(Default.getScreenWidth(this) / 2, Default.getScreenHeight(this) - 110);
            PointF endValue = new PointF(Default.getScreenWidth(this) / 4 * 3, Default.getScreenHeight(this) );

            RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(Default.dip2px(10, this) , Default.dip2px(10, this));
            ImageView img1 = new ImageView(this);
            img1.setBackgroundColor(Color.BLUE);
            detail_layout.addView(img1);
            lp1.setMargins((int) startValue.x, (int) startValue.y, 0, 0);
            img.setLayoutParams(lp1);

            RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(Default.dip2px(10, this) , Default.dip2px(10, this));
            ImageView img2 = new ImageView(this);
            img2.setBackgroundColor(Color.BLACK);
            detail_layout.addView(img2);
            lp2.setMargins((int) endValue.x, (int) endValue.y, 0, 0);
            img.setLayoutParams(lp2);

            ValueAnimator va = ValueAnimator.ofObject(new TypeEvaluator<PointF>() {
                @Override
                public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
                    //fraction = 开始动画 0 ~ 结束动画 1

                    PointF outPutPointF = new PointF();

                    int f = 200;

                    float high = f;
                    float k = (high + endValue.y) * 3 /( 4 * (endValue.x - startValue.x));
                    float a = endValue.x - startValue.x;
                    float d = -a * k;

                    if (fraction <= 0.5) {
                        // TODO: 2017/2/18 正弦
                        float x = fraction * 2 * (endValue.x - startValue.x) / 3 + startValue.x;
                        float y = (float) (startValue.y -  f * Math.sin(x * 3.14 / (2 * (endValue.x - startValue.x) / 3 + startValue.x) ));
                        outPutPointF.set(x , y);
                    }

                    if (fraction > 0.5) {
                        // TODO: 2017/2/18 一次函数

                        float x = fraction * 2 * (endValue.x - startValue.x) / 3 + startValue.x;
                        float y = endValue.y - (k * x + d );
                        outPutPointF.set(x , y);
                    }

                    return outPutPointF;
                }
            }, startValue, endValue);

            final RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(Default.dip2px(10, this), Default.dip2px(10, this));
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    //这里得到的对象就是我们在上面吐出的outPutPointF
                    PointF pointF = (PointF) animation.getAnimatedValue();
                    //根据每刻得到的坐标值，来规划面板
                    lp.setMargins((int) pointF.x, (int) pointF.y, 0, 0);
                    //每一刻的重设view组件的位置，形成点到线的动画效果
                    img.setLayoutParams(lp);
                }
            });

            va.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    detail_layout.removeView(img);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            //设置动画时长为3秒
            va.setDuration(3000);
            //启动动画
            va.start();

            //endregion*/

                //endregion

                AsynServer.BackObject(DetailGcbActivity.this , "cart/create", para, new AsynServer.BackObject() {
                    @Override
                    public void Back(JSONObject sender) {
                        JSONObject json_status = JsonClass.jj(sender,"status");
                        if (JsonClass.ij(json_status,"succeed") == 1) {
                            // region ```
                            MessageBox.Show(Default.AppName, "商品已添加到购物车！", new String[]{"再逛会", "去结算"}, new MessageBox.MessBtnBack() {
                                @Override
                                public void Back(int index) {
                                    switch (index){
                                        case 0:
                                            break;
                                        case 1:
                                            Intent intent = new Intent();
                                            intent.putExtra(ShoppingCartActivity.TYPE, ShoppingCartActivity.SHOP_CART);
                                            AddActivity(ShoppingCartActivity.class,0,intent);
                                            break;
                                        default:
                                    }
                                }
                            });
                            //endregion
                        } else {
                            MessageBox.Show(JsonClass.sj(json_status,"error_desc"));
                        }
                    }
                });
            } else {
                MessageBox.Show(this, Default.AppName, "前往个人中心登陆！", new String[]{"取消", "登陆"}, new MessageBox.MessBtnBack() {
                    @Override
                    public void Back(int index) {
                        switch (index) {
                            case 0:
                                break;
                            case 1:
                                AddActivity(LoginActivity.class);
                                break;
                            default:
                        }
                    }
                });
            }

        } else if (tag == 3) {
            if (!dmv.hasCheckedSelect) {
                /* 防快手*/
                return;
            }
            if (User.isLogin()) {
                JSONObject para = new JSONObject();
                try {
                    para.put("goods_id", GetIntentData(GOODSID).toString());
                    para.put("number", dmv.GetGoodsNumber() + "");
                    ArrayList<String> list = dmv.GetSpec();
                    if (list == null) {
                        MessageBox.Show("规格没有选择全");
                        return;
                    }
                    StringBuilder strspec = new StringBuilder();
                    for (int i = 0; i < list.size(); i++) {
                        strspec.append(list.get(i));
                        strspec.append(",");
                    }
                    if (strspec.length() != 0) {
                        para.put("spec", strspec.substring(0, strspec.length() - 1));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AsynServer.BackObject(DetailGcbActivity.this, "cart/create", para, new AsynServer.BackObject() {
                    @Override
                    public void Back(JSONObject sender) {
                        try {
                            JSONObject json_status = sender.getJSONObject("status");
                            int succeed = json_status.getInt("succeed");
                            if (succeed == 1) {
                                Intent intent = new Intent();
                                intent.putExtra(ShoppingCartActivity.TYPE,ShoppingCartActivity.SHOP_CART);
                                AddActivity(ShoppingCartActivity.class,0,intent);
                            } else {
                                MessageBox.Show(json_status.getString("error_desc"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                MessageBox.Show(this, Default.AppName, "前往个人中心登陆！", new String[]{"取消", "登陆"}, new MessageBox.MessBtnBack() {
                    @Override
                    public void Back(int index) {
                        switch (index) {
                            case 0:
                                break;
                            case 1:
                               AddActivity(LoginActivity.class);
                                break;
                            default:
                        }
                    }
                });
            }
        }

    }
}
