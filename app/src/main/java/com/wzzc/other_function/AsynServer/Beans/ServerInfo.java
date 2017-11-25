package com.wzzc.other_function.AsynServer.Beans;

import com.wzzc.other_function.AsynServer.AsynServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mypxq on 2016-07-16.
 */
public class ServerInfo {

    private static final int ConnectedTimeOut = 100 * 1000;
    static HttpURLConnection httpURLConnection;
    //region POST
    public static void stopConnection () {
        if (httpURLConnection != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    httpURLConnection.disconnect();
                    httpURLConnection = null;
                }
            }).start();
        }
    }
    /**
     * 通过POST获取服务器上的字节
     */
    public static byte[] Post(String strurl, HttpParams para, AsynServer.BackObject back) throws Exception {
        if (para == null) {
            return Get(strurl, para, back);
        }
        return PostByString(strurl, para, back);
    }

    /**
     * 通过GET获取服务器上的字节
     */
    private static byte[] Get(String strurl, HttpParams para, AsynServer.BackObject back) throws Exception {
        URL url;
        if (para == null) {
            url = new URL(strurl);
        } else {
            url = new URL(strurl + "?" + para.toString());
        }
        httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setConnectTimeout(ConnectedTimeOut);//设置连接主机超时（单位：毫秒）
        httpURLConnection.setReadTimeout(ConnectedTimeOut);//设置从主机读取数据超时（单位：毫秒）
        httpURLConnection.setRequestMethod("GET");
        int response = httpURLConnection.getResponseCode();
        if (back != null) {
            back.responseCode = response;
        }
        if (response == HttpURLConnection.HTTP_OK) {
            return toByteArray(httpURLConnection.getInputStream());
        }

        return new byte[0];
    }

    /**
     * 普通POST提交获取服务器上的字节
     */
    private static byte[] PostByString(String strurl, HttpParams para, AsynServer.BackObject back) throws Exception {
        byte[] data = para.toString().getBytes("UTF-8");
        URL url = new URL(strurl);
        httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setConnectTimeout(ConnectedTimeOut);//设置连接主机超时（单位：毫秒）
        httpURLConnection.setReadTimeout(ConnectedTimeOut);//设置从主机读取数据超时（单位：毫秒）
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
        OutputStream outputStream = httpURLConnection.getOutputStream();
        outputStream.write(data);
        int response = httpURLConnection.getResponseCode();
        if (back!= null){
            back.responseCode = response;
        }
        if (response == HttpURLConnection.HTTP_OK) {
            return toByteArray(httpURLConnection.getInputStream());
        }

        return new byte[0];
    }

    //endregion

    /**
     * 将InputStream转成字节
     */
    private static byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            output.write(buffer, 0, len);
        }
        return output.toByteArray();
    }


}
