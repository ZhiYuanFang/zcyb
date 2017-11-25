package com.wzzc.other_function.AsyncTask;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/5/18.
 *
 * 异步加载
 */

public class MyAsyncTask extends AsyncTask<Object, Object , Handler.Callback> {

    @Override
    protected Handler.Callback doInBackground(Object... params) {
        Object obj = params[0];
        Handler.Callback callback = (Handler.Callback) params[1];
        Message message = new Message();
        message.obj = obj;
        callback.handleMessage(message);
        return callback;
    }

    @Override
    protected void onPostExecute(Handler.Callback callback) {
        super.onPostExecute(callback);
    }
}
