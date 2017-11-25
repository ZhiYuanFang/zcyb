package com.wzzc.welcome;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.NextIndex.NextIndex;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.index.home.PopDelegate;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.FileInfo;
import com.wzzc.other_function.ImageHelper;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.action.ClickDelegate;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zcyb365 on 2016/12/17.
 * <p>
 * 检查更新界面
 */
public class WellcomeActivity extends BaseActivity implements PopDelegate, ClickDelegate {
    //region 组件
    @ViewInject(R.id.dongimg)
    ImageView homeImage;
    @ViewInject(R.id.versionNumber)
    private TextView versionNumber;

    @ViewInject(R.id.layout_pop)
    RelativeLayout layout_pop;
    @ViewInject(R.id.popView)
    PopView popView;
    //endregion

    String clearKey = "14";//当更新版本时需要清除之前版本的缓存数据时的标识符--- 往上加 不可往下减

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

                if (ContextCompat.checkSelfPermission(Default.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //请求权限
                    ActivityCompat.requestPermissions(Default.getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1002);
                    //判断是否需要 向用户解释，为什么要申请该权限
                    ActivityCompat.shouldShowRequestPermissionRationale(Default.getActivity(), Manifest.permission.READ_CONTACTS);
                } else {
                    if (!FileInfo.IsAtUserString(clearKey, WellcomeActivity.this)) {
                        FileInfo.ClearUserString(WellcomeActivity.this);//删除缓存数据
                        ImageHelper.clearImage(WellcomeActivity.this);//删除缓存图片
                        FileInfo.SetUserString(clearKey, clearKey, WellcomeActivity.this);
                    }
                    Default.firstComing = true;
                    String currentVersion = /*"当前版本号:" +*/ Default.getVersion(WellcomeActivity.this);
                    versionNumber.setText(currentVersion);
                    showPop();
                }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int index = 0; index < permissions.length; index++) {
            switch (permissions[index]) {
                case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                    if (requestCode == 1002) {
                        if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                            /*用户已经受权*/
                            if (!FileInfo.IsAtUserString(clearKey, WellcomeActivity.this)) {
                                FileInfo.ClearUserString(WellcomeActivity.this);//删除缓存数据
                                ImageHelper.clearImage(this);//删除缓存图片
                                FileInfo.SetUserString(clearKey, clearKey, WellcomeActivity.this);
                            }
                            Default.firstComing = true;
                            String currentVersion = /*"当前版本号:" +*/ Default.getVersion(WellcomeActivity.this);
                            versionNumber.setText(currentVersion);
                            showPop();
                        } else {
                            /*用户应该拒绝了权限*/
                            Default.showToast("拒绝");
                            AddActivity(NextIndex.class);
                        }
                    }
                    break;
                default:
            }
        }
    }

    private void showPop() {
        if (Default.isConnect(this)) {
            AsynServer.BackObject(this, "ad_loading", false, new JSONObject(), new AsynServer.BackObject() {
                @Override
                public void BackError(String error) {
                    super.BackError(error);
                    WellcomeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    AddActivity(NextIndex.class);
                                }
                            }, Default.delayTime);
                        }
                    });
                }

                @Override
                public void Back(final JSONObject sender) {
                    try {
                        JSONObject json_status = sender.getJSONObject("status");
                        int succeed = json_status.getInt("succeed");
                        if (succeed == 1) {
                            JSONObject data = JsonClass.jj(sender, "data");
                            JSONObject ad = JsonClass.jj(data, "ad");
                            JSONArray jrr = new JSONArray();
                            if (ad != null && ad.length() > 0) {
                                jrr.put(ad);
                            }
                            if (jrr.length() > 0) {
                                layout_pop.setVisibility(View.VISIBLE);
                                popView.setInfo(WellcomeActivity.this, WellcomeActivity.this, jrr);
                            } else {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        AddActivity(NextIndex.class);
                                    }
                                }, Default.delayTime);
                            }
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    AddActivity(NextIndex.class);
                                }
                            }, Default.delayTime);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                AddActivity(NextIndex.class);
                            }
                        }, Default.delayTime);
                    }
                }
            });
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AddActivity(NextIndex.class);
                }
            }, Default.delayTime);
        }

    }


    @Override
    public void AddActivity(Class<?> cls) {
        super.AddActivity(cls);
        finish();
    }

    @Override
    public void dismissPopView() {
        AddActivity(NextIndex.class);
    }

    @Override
    public void shouldLogin(View clickView) {
        // TODO: 2017/8/5
    }
    //endregion
}
