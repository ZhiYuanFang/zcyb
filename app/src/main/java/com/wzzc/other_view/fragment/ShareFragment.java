package com.wzzc.other_view.fragment;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wzzc.base.Default;
import com.wzzc.wx.WX;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/7/13.
 *
 */
public class ShareFragment extends Fragment implements View.OnClickListener {
    public static final String ImageByte = "IMAGE";
    public static final String WebUrl = "url";
    public static final String Title = "title";
    public static final String Message = "message";

    RelativeLayout layout_out;
    ImageButton layout_wx,layout_friends,layout_weibo,layout_qq;

    private byte[] image;
    private String webUrl,title,message;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_fragment_share,null);
        layout_out = (RelativeLayout) v.findViewById(R.id.layout_out);
        layout_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissMissFragment();
            }
        });
        layout_wx = (ImageButton) v.findViewById(R.id.layout_wx);
        layout_friends = (ImageButton) v.findViewById(R.id.layout_friends);
        layout_weibo = (ImageButton) v.findViewById(R.id.layout_weibo);
        layout_qq = (ImageButton) v.findViewById(R.id.layout_qq);

        layout_wx.setOnClickListener(this);
        layout_friends.setOnClickListener(this);
        layout_weibo.setOnClickListener(this);
        layout_qq.setOnClickListener(this);


        return v;
    }

    private void dissMissFragment (){
        Default.getActivity().onBackPressed();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle.containsKey(ImageByte)) {
            image = bundle.getByteArray(ImageByte);
        }
        if (bundle.containsKey(WebUrl)) {
            webUrl = bundle.getString(WebUrl);
        }
        if (bundle.containsKey(Title)) {
            title = bundle.getString(Title);
        }
        if (bundle.containsKey(Message)) {
            message = bundle.getString(Message);
        }
    }

    @Override
    public void onClick(View v) {
        dissMissFragment();
        switch (v.getId()){
            case R.id.layout_wx:{
                if (WX.isWeixinAvilible(Default.getActivity()) && image != null) {
                    Default.showToast("分享二维码给微信好友");
                    WX.getInstance().shareImg(0, BitmapFactory.decodeByteArray(image,0,image.length));
                } else {
                    Default.showToast("未安装微信", Toast.LENGTH_LONG);
                }
                break;}
            case R.id.layout_friends:{
                if (WX.isWeixinAvilible(Default.getActivity()) && webUrl != null && title != null && message != null) {
                    Default.showToast("将以网页链接形式发送到朋友圈");
                    WX.getInstance().shareWebPage(1,webUrl,BitmapFactory.decodeByteArray(image,0,image.length),title,message);
                } else {
                    Default.showToast("未安装微信", Toast.LENGTH_LONG);
                }
                break;}
            case R.id.layout_weibo:{
                Default.showToast(Default.getActivity().getString(R.string.notDevelop));
                break;}
            case R.id.layout_qq:{
                Default.showToast(Default.getActivity().getString(R.string.notDevelop));
                break;}
            default:
        }
    }
}

