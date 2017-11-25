package com.wzzc.other_function.action;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.TradeResult;
import com.wzzc.NextSuperDeliver.list.Category;
import com.wzzc.NextSuperDeliver.list.SuperDeliverList;
import com.wzzc.NextTBSearch.JdpageActivity;
import com.wzzc.base.Default;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.other_function.action.toActivity.ArticleActivity;
import com.wzzc.other_view.production.detail.gcb.DetailGcbActivity;
import com.wzzc.other_view.production.detail.zcb.DetailZcbActivity;
import com.wzzc.other_view.production.list.BrowseProductionActivity;
import com.wzzc.taobao.TBDetailActivity;
import com.wzzc.taobao.TaoBao;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/8/5.
 */

public class ItemClick {
    public static final String SuperDiscount_goods = "SuperDiscount_goods";
    public static final String SuperDiscount_list = "SuperDiscount_list";
    public static final String SuperDiscount_list_1 = "SuperDiscount_list_1";
    public static final String SuperDiscount_list_2 = "SuperDiscount_list_2";
    public static final String SuperDiscount_list_3 = "SuperDiscount_list_3";
    public static final String SuperDiscount_list_4 = "SuperDiscount_list_4";
    public static final String GOODS = "goods";
    public static final String CATEGORY = "category";
    public static final String EXCHANGE_GOODS = "exchange_goods";
    public static final String EXCHANGE_CATEGORY = "exchange_category";
    public static final String ARTICLE = "article";
    public static final String ARTICLE_CAT = "article_cat";

    /**
     * 接入其他点击事件时，应重载此方法
     * @param data_type 分类信息
     * @param ad_link 对应id
     * @param otherID 产品对应淘宝客id
     * @return
     */
    public static View.OnClickListener listener(final ClickDelegate clickDelegate , final String data_type, final String ad_link, final String otherID) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!switchNormalListener(data_type,ad_link)) {
                    judgeSpecialListener(clickDelegate,v,data_type,ad_link,otherID);
                }
            }
        };
    }

    //.......................................................................

    public static boolean switchNormalListener(String data_type, String ad_link) {
        Log.i("ItemClick",data_type + " -- " + ad_link);
        switch (data_type) {
            case SuperDiscount_list: {
                //region 超值送分类
                Intent intent = new Intent();
                Category category = new Category();
                category.setCategoryID(ad_link);
                category.setCategoryName(category.getCategoryID().equals(Category.AllCategoryID) ? Category.AllCategoryName : "二级筛选");
                intent.putExtra(SuperDeliverList.CATEGORY, category);
                Default.getActivity().AddActivity(SuperDeliverList.class, 0, intent);
                //endregion
                break;
            }
            case SuperDiscount_list_1: {
                //region 超值送-今日上新
                Intent intent = new Intent();
                Category category = new Category();
                category.setCategoryID(ad_link);
                category.setpType(1);
                category.setCategoryName(category.getCategoryID().equals(Category.AllCategoryID) ? Category.AllCategoryName : "二级筛选");
                intent.putExtra(SuperDeliverList.CATEGORY, category);
                Default.getActivity().AddActivity(SuperDeliverList.class, 0, intent);
                //endregion
                break;
            }
            case SuperDiscount_list_2: {
                //region 超值送-超级秒杀
                Intent intent = new Intent();
                Category category = new Category();
                category.setCategoryID(ad_link);
                category.setpType(2);
                category.setCategoryName(category.getCategoryID().equals(Category.AllCategoryID) ? Category.AllCategoryName : "二级筛选");
                intent.putExtra(SuperDeliverList.CATEGORY, category);
                Default.getActivity().AddActivity(SuperDeliverList.class, 0, intent);
                //endregion
                break;
            }
            case SuperDiscount_list_3: {
                //region 超值送-高返利推介
                Intent intent = new Intent();
                Category category = new Category();
                category.setCategoryID(ad_link);
                category.setpType(3);
                category.setCategoryName(category.getCategoryID().equals(Category.AllCategoryID) ? Category.AllCategoryName : "二级筛选");
                intent.putExtra(SuperDeliverList.CATEGORY, category);
                Default.getActivity().AddActivity(SuperDeliverList.class, 0, intent);
                //endregion
                break;
            }
            case SuperDiscount_list_4: {
                //region 超值送-九块九专区
                Intent intent = new Intent();
                Category category = new Category();
                category.setCategoryID(ad_link);
                category.setpType(4);
                category.setCategoryName(category.getCategoryID().equals(Category.AllCategoryID) ? Category.AllCategoryName : "二级筛选");
                intent.putExtra(SuperDeliverList.CATEGORY, category);
                Default.getActivity().AddActivity(SuperDeliverList.class, 0, intent);
                //endregion
                break;
            }
            case GOODS: {
                Intent intent = new Intent();
                intent.putExtra(DetailGcbActivity.GOODSID, ad_link);
                Default.getActivity().AddActivity(DetailGcbActivity.class, 0, intent);
                break;
            }
            case CATEGORY: {
                Intent intent = new Intent();
                intent.putExtra(BrowseProductionActivity.CATEGORY_ID, ad_link);
                intent.putExtra(BrowseProductionActivity.TYPEID, BrowseProductionActivity.TYPEVALUE_GCB_PRODUCTION);
                Default.getActivity().AddActivity(BrowseProductionActivity.class, 0, intent);
                break;
            }
            case EXCHANGE_GOODS: {
                Intent intent = new Intent();
                intent.putExtra(DetailZcbActivity.GOODSID, ad_link);
                Default.getActivity().AddActivity(DetailZcbActivity.class, 0, intent);
                break;
            }
            case EXCHANGE_CATEGORY: {
                Intent intent = new Intent();
                intent.putExtra(BrowseProductionActivity.CATEGORY_ID, ad_link);
                intent.putExtra(BrowseProductionActivity.TYPEID, BrowseProductionActivity.TYPEVALUE_ZCB_PRODUCTION);
                Default.getActivity().AddActivity(BrowseProductionActivity.class, 0, intent);
                break;
            }
            case ARTICLE: {
                Intent intent = new Intent();
                intent.putExtra(ArticleActivity.ARTICLE_ID, ad_link);
                Default.getActivity().AddActivity(ArticleActivity.class, 0, intent);
                break;
            }
            default:
                return false;//不符合普通机制 则返回false
        }
        return true;
    }

    public static void judgeSpecialListener(ClickDelegate clickDelegate ,View currentView,String data_type, String ad_link, String otherID) {
        switch (data_type) {
            case SuperDiscount_goods:
                //region 超值送 购买
                if (otherID != null && otherID.length() > 0) {
                    addSpecialOrder(clickDelegate,currentView,ad_link,otherID);
                } else {
                    Default.showToast("当前产品已失效");
                }
                //endregion
                break;
            default:
                Default.showToast(data_type);
        }
    }

    public static final String Type_TB = "淘宝";
    public static final String Type_JD = "京东";
    public static final String Type_YHD = "一号店";
    public static final String Type_TM = "天猫";

    /**
     * <h1>超值送</h1>
     * 向服务器提交订单
     * @param ad_link   产品ID
     * @param otherID   eg. 淘宝产品推广ID
     */
    public static void addSpecialOrder(final ClickDelegate clickDelegate, final View currentView, String ad_link, final String otherID) {
        if (otherID == null || otherID.length() <= 0) {
            Default.showToast("产品失效");
            return;
        }
        JSONObject para = new JSONObject();
        try {
            para.put("id", ad_link);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(Default.getActivity(), "SuperDiscount/buy", para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject object) {
                JSONObject json_status = JsonClass.jj(object, "status");
                int succeed = JsonClass.ij(json_status, "succeed");
                if (succeed == 1) {
                    JSONObject sender = JsonClass.jj(object, "data");
                    String goods_name = JsonClass.sj(sender, "goods_name");
                    String goods_from = JsonClass.sj(sender,"goods_from");
                    switchOrderFrom(goods_from , Default.useApp, goods_name , otherID);
                } else {
                    int error_code = JsonClass.ij(json_status, "error_code");
                    if (error_code == 100 & clickDelegate != null) {//您尚未登陆
                        clickDelegate.shouldLogin(currentView);
                    } else
                        MessageBox.Show(JsonClass.sj(json_status, "error_desc"));
                }
            }
        });
    }

    /** 淘宝搜索下单*/
    public static void addTaoBaoSearchOrder (final ClickDelegate clickDelegate, final View currentView, final String num_iid) {
        if (num_iid == null || num_iid.length() <= 0) {
            Default.showToast("产品失效");
            return;
        }
        JSONObject para = new JSONObject();
        try {
            para.put("num_iid", num_iid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(Default.getActivity(), "TaobaoGoods/buy", para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject object) {
                JSONObject json_status = JsonClass.jj(object, "status");
                int succeed = JsonClass.ij(json_status, "succeed");
                if (succeed == 1) {
                    JSONObject sender = JsonClass.jj(object, "data");
                    String goods_name = JsonClass.sj(sender, "goods_name");
                    switchOrderFrom(Type_TB , Default.useApp, goods_name , num_iid);
                } else {
                    int error_code = JsonClass.ij(json_status, "error_code");
                    if (error_code == 100 & clickDelegate != null) {//您尚未登陆
                        clickDelegate.shouldLogin(currentView);
                    } else
                        MessageBox.Show(JsonClass.sj(json_status, "error_desc"));
                }
            }
        });
    }

    /** 京东搜索下单*/
    public static void addJingDongSearch(final ClickDelegate clickDelegate, final View currentView, final String num_iid){
        Intent intent = new Intent(Default.getActivity(),JdpageActivity.class);
        intent.putExtra(JdpageActivity.num_iid_key, num_iid);
        Default.getActivity().AddActivity(JdpageActivity.class, 0, intent);
    }

    public static void switchOrderFrom (String goods_from , boolean useApp , String goods_name , String otherID ) {
        if (otherID == null || otherID.length() <= 0) {
            Default.showToast("产品失效");
            return;
        }
        switch (goods_from){
            case Type_TB:
                gotoTaoBao(useApp, goods_name , otherID);
                break;
            default:
                Default.showToast(goods_from);
        }
    }
    //region 淘宝
    public static void gotoTaoBao(boolean useApp, String goods_name , String num_id) {
        if (num_id == null || num_id.length() <= 0) {
            Default.showToast("产品失效");
            return;
        }
        Intent intent = new Intent(Default.getActivity(),TBDetailActivity.class);
        intent.putExtra(TBDetailActivity.Item, num_id);
        intent.putExtra(TBDetailActivity.Type, 0);
        intent.putExtra(TBDetailActivity.GoodsName, goods_name);
        if (useApp) {
            if (TaoBao.hasInstallation()) {
                TaoBao.showItemDetailPage(num_id, new AlibcTradeCallback() {
                    @Override
                    public void onTradeSuccess(TradeResult tradeResult) {
                        Default.showToast("成功打开淘宝详情页", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        MessageBox.Show(s);
                    }
                });
            } else {
                Default.getActivity().AddActivity(TBDetailActivity.class, 0, intent);
            }
        } else {
            Default.getActivity().AddActivity(TBDetailActivity.class, 0, intent);
        }
    }
    //endregion


}
