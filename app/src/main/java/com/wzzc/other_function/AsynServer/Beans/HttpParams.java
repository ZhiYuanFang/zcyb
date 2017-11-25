package com.wzzc.other_function.AsynServer.Beans;

import android.graphics.Bitmap;

import java.io.*;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mypxq on 2016-07-16.
 */
public class HttpParams {

    protected ConcurrentHashMap<String, String> urlParams = new ConcurrentHashMap<>();
    protected ConcurrentHashMap<String, FileWrapper> fileParams = new ConcurrentHashMap<>();

    public HttpParams() {
    }

    public HttpParams(Map<String, String> source) {
        for (Map.Entry<String, String> entry : source.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 生成一个http参数
     *
     * @param key
     * @param value
     */
    public HttpParams(String key, String value) {
        put(key, value);
    }

    /**
     * 生成一个http参数
     *
     * @param keysAndValues key,value,key,value
     */
    public HttpParams(Object... keysAndValues) {
        int len = keysAndValues.length;
        if (len == 0 || len % 2 != 0) {
            return;
        }
        for (int i = 0; i < len; i += 2) {
            String key = String.valueOf(keysAndValues[i]);
            byte[] arr = FileWrapper.getFile(keysAndValues[i + 1]);
            if (arr == null) {
                put(key, String.valueOf(keysAndValues[i + 1]));
            } else {
                put(key, arr);
            }
        }
    }

    public void put(String key, Object value) {
        if (key != null && value != null) {
            urlParams.put(key, value.toString());
        }
    }

    public void put(String key, String value) {
        if (key != null && value != null) {
            urlParams.put(key, value);
        }
    }

    public void put(String key, byte[] value) {
        fileParams.put(key, new FileWrapper(value, null));
    }

    public void put(String key, Bitmap bitmap) {
        put(key, bitmap, null);
    }

    public void put(String key, Bitmap bitmap, String fileName) {
        fileParams.put(key, new FileWrapper(FileWrapper.getFile(bitmap), fileName));
    }

    public void put(String key, InputStream stream) {
        fileParams.put(key, new FileWrapper(FileWrapper.getFile(stream), null));
    }

    public void put(String key, File file) {
        fileParams.put(key, new FileWrapper(FileWrapper.getFile(file), file.getName()));
    }

    /**
     * 移除指定key
     *
     * @param key
     */
    public void remove(String key) {
        urlParams.remove(key);
        fileParams.remove(key);
    }

    /**
     * 判断是否存在文件
     *
     * @return
     */
    public boolean hasFile() {
        return fileParams.size() > 0;
    }

    @Override
    public String toString() {
        StringBuilder body = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = urlParams.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String name = entry.getKey();
            String value = entry.getValue();
            try {
                body.append(String.format("%s=%s&", name, URLEncoder.encode(value, "UTF-8")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        System.out.println("body : " + body);
        return body.substring(0, body.length() - 1);
    }

    public byte[] toByteArray() {
        try {
            String boundary = "mypxqcreated";
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            Iterator<Map.Entry<String, String>> striterator = urlParams.entrySet().iterator();
            while (striterator.hasNext()) {
                Map.Entry<String, String> entry = striterator.next();
                byte[] data1 = String.format("\r\n--%s\r\n", boundary).getBytes("UTF-8");
                byte[] data2 = String.format("Content-Disposition: form-data; name=\"%s\"\r\n\r\n", entry.getKey()).getBytes("UTF-8");
                byte[] data3 = entry.getValue().getBytes("UTF-8");
                byteArray.write(data1);
                byteArray.write(data2);
                byteArray.write(data3);
            }
            Iterator<Map.Entry<String, FileWrapper>> fileiterator = fileParams.entrySet().iterator();
            while (fileiterator.hasNext()) {
                Map.Entry<String, FileWrapper> entry = fileiterator.next();
                byte[] data1 = String.format("\r\n--%s\r\n", boundary).getBytes("UTF-8");
                byte[] data2 = String.format("Content-Disposition: form-data; name=\"%s\"; filename=\"%s\"\r\n", entry.getKey(), entry.getValue().getFileName()).getBytes("UTF-8");
                byte[] data3 = "Content-Type: application/octet-stream\r\n\r\n".getBytes("UTF-8");
                byteArray.write(data1);
                byteArray.write(data2);
                byteArray.write(data3);
                byteArray.write(entry.getValue().fileData);
            }
            if (fileParams.size() > 0 || urlParams.size() > 0) {
                byte[] data = String.format("\r\n--%s--\r\n", boundary).getBytes("UTF-8");
                byteArray.write(data);
            }
            return byteArray.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private static class FileWrapper {
        public byte[] fileData;
        public String fileName;

        public FileWrapper(byte[] fileData, String fileName) {
            this.fileData = fileData;
            this.fileName = fileName;
        }

        public String getFileName() {
            if (fileName != null) {
                return fileName;
            } else {
                return "uploadfile";
            }
        }

        public static byte[] getFile(Object data) {
            try {
                if (data instanceof byte[]) {
                    return (byte[]) data;
                }
                if (data instanceof Bitmap) {
                    Bitmap image = (Bitmap) data;
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    return byteArrayOutputStream.toByteArray();
                }
                if (data instanceof File) {
                    data = new FileInputStream((File) data);
                }
                if (data instanceof InputStream) {
                    InputStream inputStream = (InputStream) data;
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    byte[] buffer = new byte[4096];
                    int len;
                    while ((len = inputStream.read(buffer)) != -1) {
                        output.write(buffer, 0, len);
                    }
                    return output.toByteArray();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
