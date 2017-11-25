package com.wzzc.NextIndex.views.e.other_activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.FileInfo;
import com.wzzc.other_function.ImageHelper;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.other_view.TextLoadView;
import com.wzzc.other_view.fragment.SelectFragment;
import com.wzzc.other_view.fragment.ShareFragment;
import com.wzzc.wx.WX;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by by Administrator on 2017/5/27.
 * 邀请码
 */

public class InvitedCodeActivity extends BaseActivity{
    //region ```
    @ViewInject(R.id.tlv_code)
    TextLoadView tlv_code;
    @ViewInject(R.id.tv_category)
    TextView tv_info;
    @ViewInject(R.id.tv_link)
    TextLoadView tv_link;
    @ViewInject(R.id.btn_copy)
    TextView btn_copy;
    @ViewInject(R.id.tv_second_info)
    TextView tv_second_info;
    @ViewInject(R.id.img_code)
    ImageView img_code;
    //endregion
    String shareTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddBack();
        initBackTouch();
        tlv_code.isLoading();
        tv_link.isLoading();
        setInfoFromService();
        init();
    }

    protected void init() {
        btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Default.SetClipboardString(tv_link.getTv_text().getText(), InvitedCodeActivity.this);
                btn_copy.setTextColor(ContextCompat.getColor(InvitedCodeActivity.this, R.color.tv_green));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btn_copy.setTextColor(ContextCompat.getColor(InvitedCodeActivity.this, R.color.tv_White));
                    }
                }, 100);
            }
        });

        img_code.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                invitedDialog(img_code);
                return true;
            }
        });
    }
    protected void invitedDialog(final ImageView imgView) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.push_bottom_in,R.anim.push_bottom_out);
        SelectFragment selectFragment = new SelectFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray(SelectFragment.ITEM_INFOS,new String[]{"保存二维码到手机", "发送给朋友"});
        SelectFragment.SelectCallBack callBack = new SelectFragment.SelectCallBack() {
            @Override
            public void call(int index) {
                switch (index){
                    case 0:
                        //region 保存二维码
                        Default.showToast("二维码已存入相册", Toast.LENGTH_LONG);
                        Bitmap bitmap = ImageHelper.truncationView(imgView);

                        FileInfo.saveImage(bitmap, "zcyb_share", "推广码");
                        //endregion
                        break;
                    case 1:{
                                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.setCustomAnimations(R.anim.push_bottom_in,R.anim.push_bottom_out);
                                ShareFragment shareFragment = new ShareFragment();
                                Bundle bundle = new Bundle();
                                bundle.putByteArray(ShareFragment.ImageByte,ImageHelper.bitmapToByte(ImageHelper.truncationView(img_code)));
                                bundle.putString(ShareFragment.Message,"message");
                                bundle.putString(ShareFragment.Title,shareTitle);
                                bundle.putString(ShareFragment.WebUrl,tv_link.getTv_text().getText().toString());
                                shareFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.contain_fragment,shareFragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                                hasFragment = true;
                        break;}
                    default:
                }
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {

            }
        };
        bundle.putParcelable(SelectFragment.ITEM_CallBack,callBack);
        selectFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.contain_fragment,selectFragment);
        fragmentTransaction.addToBackStack(null);
        outFragment();
        fragmentTransaction.commit();
        hasFragment = true;
    }

    /**
     * {
     * "data": {
     * "invitation_code": 368636,
     * "invitation_link": "http://www.zcyb365.com/mobile/user.php?act=register&invi=368636",
     * "text": "其它人在本站注册时，输入您的邀请码，即可绑定为您的下线，您的下线会员在本站超值送版块下单，订单完成后，您将会得到分成奖励。",
     * "text2": "您也可以把邀请链接分享给朋友，当他们点击这个链接到本站注册时，会自动绑定为您的下线。\n 该链接有效期为24小时，如果链接失效，请重新复制新链接给你的朋友。"
     * },
     * "status": {
     * "succeed": 1
     * }
     * }
     */
    protected void setInfoFromService() {
        AsynServer.BackObject(this, "user/invitation_code", false, new JSONObject(), new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                try {
                    JSONObject json_status = sender.getJSONObject("status");
                    int succeed = json_status.getInt("succeed");
                    if (succeed == 1) {
                        JSONObject data = sender.getJSONObject("data");
                        try {
                            shareTitle = JsonClass.sj(data,"share_text");
                            int invitation_code = data.getInt("invitation_code");
                            String text = data.getString("text");
                            tlv_code.loadOk("" + invitation_code, R.color.tv_Red);
                            String link = data.getString("invitation_link");
                            try {
                                img_code.setBackground(new BitmapDrawable(getResources(),ImageHelper.create2DCode(BitmapFactory.decodeResource(Default.getActivity().getResources(), R.mipmap.applogo),link)));
                            } catch (WriterException e) {
                                e.printStackTrace();
                            }
                            setElseCode(JsonClass.jrrj(data, "qr_else"));
                            tv_link.loadOk(link);
                            tv_second_info.setText(data.getString("text2"));
                            tv_info.setText(text);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            tlv_code.noData();
                            tv_link.noData();
                        }
                    } else {
                        MessageBox.Show(json_status.getString("error_desc"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected void setElseCode(JSONArray elseCodeJsonArray){
        if(null == elseCodeJsonArray || 0 == elseCodeJsonArray.length()){
            return;
        }
        for(int i = 0; i<elseCodeJsonArray.length(); ++i){
            JSONObject dataJson = elseCodeJsonArray.optJSONObject(i);
            String titleStr = dataJson.optString("title", "");
            String qr_codeLink = dataJson.optString("qr_link", "");
            String descStr = dataJson.optString("desc", "");

            // View 加载
            View newView = LayoutInflater.from(this).inflate(R.layout.invite_code_view, null);
            TextView titleTv = (TextView) newView.findViewById(R.id.textView172);
            final ImageView imgView = (ImageView) newView.findViewById(R.id.img_code);
            TextView descTv = (TextView) newView.findViewById(R.id.textView186);

            // 监听设置
            imgView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    invitedDialog(imgView);
                    return true;
                }
            });

            // 注入数据
            titleTv.setText(titleStr);
            try {
                imgView.setBackground(new BitmapDrawable(getResources(),ImageHelper.create2DCode(BitmapFactory.decodeResource(Default.getActivity().getResources(), R.mipmap.applogo),qr_codeLink)));
            } catch (WriterException e) {
                e.printStackTrace();
            }
            descTv.setText(descStr);

            // 加入Layout显示
            LinearLayout scrollview_layout = (LinearLayout) findViewById(R.id.scrollview_layout);
            scrollview_layout.addView(newView);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        WX.getInstance().getWXAPI().handleIntent(intent,WX.getInstance());
    }
}
