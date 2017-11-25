package com.wzzc.welcome.JavaBean;

import android.util.Log;

/**
 * Created by toutou on 2016/12/26.
 *
 * 更新文件类
 */
public class UpdateInfo {
    private String version;
    private String description;
    private String url;

    public UpdateInfo(String version,String description,String url){
        this.version = version;
        this.description = description;
        this.url = url;
        Log.d("最新版本",version );
        Log.d("提示描述语言",description);//\n1.本次更新将为用户自动删除事先缓存数据，首次启动加载较慢；\n2.优化超值返筛选功能；\n3.添加超值返品牌列表；\n4.添加超值返搜索记忆；\n5.为注册用户和个人代理开放了C米提现功能;\n6.在设置中心添加了手动清除缓存功能;\n7.解决部分手机无法进行微信分享问题。
        Log.d("下载地址",url );
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
