package com.wzzc.other_view.production.detail.zcb;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.other_view.extendView.scrollView.ExtendScrollView;
import com.wzzc.other_view.extendView.scrollView.UIScrollListener;
import com.wzzc.other_view.extendView.scrollView.UIScrollView;
import com.wzzc.other_view.production.detail.zcb.main_view.EhDetailBottomView;
import com.wzzc.other_view.production.detail.zcb.main_view.EhDetailMiddleView;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/5/15.
 *
 */

public class DetailZcbActivity extends ExtendBaseActivity {
    private static final String TAG = "DetailZcbActivity";
    public static final String GOODSID = "id";
    //region 组件
    @ViewInject(R.id.uisv)
    UIScrollView uisv;
    @ViewInject(R.id.dtv)
    RelativeLayout dtv;
    @ViewInject(R.id.esv)
    ExtendScrollView esv;
    @ViewInject(R.id.dbv)
    EhDetailBottomView dbv;
    @ViewInject(R.id.dmv)
    EhDetailMiddleView dmv;
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
                int cheight = Default.dip2px(44, DetailZcbActivity.this);
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
                    eiv.setPath(sender.getJSONObject("img").getString("url"));
                    eiv.setScaleType(ImageView.ScaleType.CENTER_CROP);
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
        AsynServer.BackObject(this, "exchange_goods", !hsf, para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                FileInfo.SetUserString(getFileKey(),sender.toString(),DetailZcbActivity.this);
                try {
                    initialized(sender,serverCallBack());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick({R.id.lab_factory, R.id.lab_factory1, R.id.btn_addcart, R.id.btn_buying})
    public void btn_bottom_click(View view) {
        int tag = Integer.parseInt(view.getTag().toString());
        if (tag == 0) {
            if (User.isLogin()) {
                JSONObject para = new JSONObject();
                try {
                    para.put("goods_id", GetIntentData(GOODSID).toString());
                    AsynServer.BackObject(DetailZcbActivity.this , "user/collect/create", para, new AsynServer.BackObject() {
                        @Override
                        public void Back(JSONObject sender) {
                            JSONObject json_status = JsonClass.jj(sender,"status");
                            if (JsonClass.ij(json_status,"succeed") == 1) {
                                MessageBox.Show("该商品已经成功地加入了您的收藏夹。");
                            } else {
                                MessageBox.Show(JsonClass.sj(json_status,"error_desc"));
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
                AsynServer.BackObject(DetailZcbActivity.this , "excart/create", para, new AsynServer.BackObject() {
                    @Override
                    public void Back(JSONObject sender) {
                        JSONObject json_status = JsonClass.jj(sender,"status");
                        if (JsonClass.ij(json_status,"succeed") == 1) {
                            MessageBox.Show(Default.AppName, "商品已添加到兑换车！", new String[]{"再逛会", "去结算"},  new MessageBox.MessBtnBack() {
                                @Override
                                public void Back(int index) {
                                    switch (index){
                                        case 0:
                                            break;
                                        case 1:
                                            Intent intent = new Intent();
                                            intent.putExtra(ShoppingCartActivity.TYPE,ShoppingCartActivity.EXCHAGE_CART);
                                            AddActivity(ShoppingCartActivity.class,0,intent);
                                            break;
                                        default:
                                    }
                                }
                            });
                        } else {
                            MessageBox.Show(JsonClass.sj(json_status,"error_desc"));
                        }
                    }
                });
            } else{
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
                Intent intent = new Intent();
                intent.putExtra(ShoppingCartActivity.TYPE, ShoppingCartActivity.EXCHAGE_CART);
                AddActivity(ShoppingCartActivity.class,0,intent);
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
