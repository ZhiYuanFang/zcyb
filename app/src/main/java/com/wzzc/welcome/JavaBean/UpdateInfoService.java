package com.wzzc.welcome.JavaBean;

import android.os.Handler;
import android.os.Message;

import com.wzzc.base.Default;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by zcyb365 on 2016/12/26.
 */
public class UpdateInfoService {
    public static UpdateInfo getUpDateInfo(){

        final UpdateInfo[] updateInfo = new UpdateInfo[1];

        Default.readParse(GetServerUrl.getUrl(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                try {
                    JSONObject myJsonObject = new JSONObject(msg.obj.toString());
                    updateInfo[0] =  new UpdateInfo(myJsonObject.getString("version"),myJsonObject.getString("title"),myJsonObject.getString("url"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        return updateInfo[0];
//        String path = GetServerUrl.getUrl();
//        StringBuffer sb = new StringBuffer();
//        String line = null;
//        BufferedReader reader = null;
//        try {
//            // 创建一个url对象
//            URL url = new URL(path);
//            // 通過url对象，创建一个HttpURLConnection对象（连接）
//            HttpURLConnection urlConnection = (HttpURLConnection) url
//                    .openConnection();
//            // 通过HttpURLConnection对象，得到InputStream
//            reader = new BufferedReader(new InputStreamReader(
//                    urlConnection.getInputStream()));
//            // 使用io流读取文件
//            while ((line = reader.readLine()) != null) {
//                sb.append(line);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (reader != null) {
//                    reader.close();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        String info = sb.toString();
//        UpdateInfo updateInfo = new UpdateInfo();
//        Log.d("zxcvbnm",info );
//
//        String value1 = null;
//        String Description = null;
//        String url = null;
//        try
//        {
//            //将字符串转换成jsonObject对象
//            JSONObject myJsonObject = new JSONObject(info);
//            //获取对应的值
//            value1 = myJsonObject.getString("version");
//            Description=myJsonObject.getString("title");
//            url=myJsonObject.getString("url");
//        }
//        catch (JSONException e) {
//        }
//
//        Log.d("最新版本",value1 );
//        Log.d("提示描述语言",Description );
//        Log.d("下载地址",url );
//
//        updateInfo.setVersion(value1);
//        updateInfo.setDescription(Description);
//        updateInfo.setUrl(url);
//
//        return updateInfo;
    }

}
