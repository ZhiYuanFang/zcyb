package com.wzzc.wx;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wzzc.base.Default;

import java.util.List;

/**
 * Created by by Administrator on 2017/6/14.
 *
 */

public class WX implements IWXAPIEventHandler {
    private IWXAPI api;
    public static final String APP_ID = "wx844d15834697abd5";

    private WX() {
        api = WXAPIFactory.createWXAPI(Default.getActivity(),APP_ID);
        api.registerApp(APP_ID);
    }

    public static WX getInstance () {
        return new WX();
    }

    public void shareImg (int flag , Bitmap bmp) {
        WXImageObject wxImageObject = new WXImageObject();
        wxImageObject.imageData = Util.bmpToByteArray(bmp,false);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = wxImageObject;
        msg.thumbData = Util.bmpToByteArray(bmp,true);
        msg.description = "推荐二维码";
        msg.title = "zc100";

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = msg;
        req.scene = flag;
        req.transaction = String.valueOf(System.currentTimeMillis());
        api.sendReq(req);
    }

    public void shareWebPage ( int flag ,String webpageUrl,Bitmap thumb, String title, String description) {

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = webpageUrl;
        System.out.println("webpageUrl : " + webpageUrl);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = webpage;
        msg.title = title;
        msg.description = description;
        msg.thumbData = Util.bmpToByteArray(thumb,true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag;
        api.sendReq(req);
    }

    public IWXAPI getWXAPI () {
        return api;
    }

    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        String result = "";
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //分享成功
                result = "分享成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //分享取消
                result = "分享取消";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //分享拒绝
                result = "分享拒绝";
                break;
            default:
        }
        Default.showToast(result, Toast.LENGTH_LONG);
    }
}
