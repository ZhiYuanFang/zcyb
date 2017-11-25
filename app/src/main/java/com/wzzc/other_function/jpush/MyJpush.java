package com.wzzc.other_function.jpush;

import android.os.Handler;

import com.wzzc.base.Default;
import com.wzzc.other_view.progressDialog.CustomProgressDialog;
import com.wzzc.zcyb365.R;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by by Administrator on 2017/7/8.
 */

public class MyJpush {
    private static AliasOK ok;
    private static CustomProgressDialog cpd;

    public interface AliasOK {
        void aliasOK();
    }

    public static void setAlias(String alias, AliasOK aliasOK) {
        ok = aliasOK;
        mHandler.sendMessageDelayed(mHandler.obtainMessage(0, alias), 1000);
    }

    private static final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if (cpd == null)
                cpd = CustomProgressDialog.createDialog(Default.getActivity(), R.style.CustomProgressDialog);
            cpd.setMessage("初始化用户");
            cpd.show();
            // 调用 JPush 接口来设置别名。
            JPushInterface.setAliasAndTags(Default.getActivity().getApplicationContext(),
                    (String) msg.obj,
                    null,
                    mAliasCallback);
        }

    };

    private static final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    cpd.dismiss();
                    ok.aliasOK();
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(0, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
            }
            System.out.println(logs);
        }
    };
}
