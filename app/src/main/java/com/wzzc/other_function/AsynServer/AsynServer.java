package com.wzzc.other_function.AsynServer;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.wzzc.NextIndex.views.e.User;
import com.wzzc.NextSuperDeliver.main_view.a_b.NoMore;
import com.wzzc.base.Default;
import com.wzzc.other_function.AsynServer.Beans.HttpParams;
import com.wzzc.other_function.AsynServer.Beans.ServerInfo;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_view.progressDialog.CustomProgressDialog;
import com.wzzc.other_view.progressDialog.CustomProgressDialogView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

/**
 * Created by toutou on 2016-10-06.
 *
 */

public class AsynServer {
    public static ListViewScrollDelegate listViewScrollDelegate;
    private static final int count = 10;
    public static boolean wantShowDialog;
    public static boolean isLoading;
    private static View footerView;

    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Object[] objects = (Object[]) msg.obj;
            loadServerEnd(String.valueOf(objects[0]), (ListView) objects[1]);
        }
    };
    /**
     * 使用此方法 将直接为list 添加加载动画
     */
    public static void BackObject(Context c, ListViewScrollDelegate scrollDelegate , String url, Boolean showDialog, ListView list, JSONObject para, BackObject back) {
        listViewScrollDelegate = scrollDelegate;
        if (para == null) {
            para = new JSONObject();
        }
        wantShowDialog = showDialog;
        if (list == null) {
            list = new ListView(c);
        }
        int page = 1;
        JSONObject pagination = new JSONObject();
        try {
            pagination.put("page", page);
            pagination.put("count", count);
            para.put("pagination", pagination);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustomProgressDialog customProgressDialog = CustomProgressDialog.createDialog(c,R.style.CustomProgressDialog);
        Object[] objects = new Object[]{c,customProgressDialog,para,url,page,back};
        list.setTag(objects);
        list.setOnScrollListener(listScrollerListener);//必须在tag后
        Start(list,wantShowDialog);
    }
    public static void BackObject(Context c, String url, Boolean showDialog, ListView list, JSONObject para, BackObject back) {
        listViewScrollDelegate = null;
        if (para == null) {
            para = new JSONObject();
        }
        wantShowDialog = showDialog;
        if (list == null) {
           list = new ListView(c);
        }
        int page = 1;
        JSONObject pagination = new JSONObject();
        try {
            pagination.put("page", page);
            pagination.put("count", count);
            para.put("pagination", pagination);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustomProgressDialog customProgressDialog = CustomProgressDialog.createDialog(c,R.style.CustomProgressDialog);
        Object[] objects = new Object[]{c,customProgressDialog,para,url,page,back};
        list.setTag(objects);
        list.setOnScrollListener(listScrollerListener);//必须在tag后
        Start(list,wantShowDialog);
    }
    public static void BackObject(Context c, String url, Boolean showDialog, JSONObject para, BackObject back) {
        listViewScrollDelegate = null;
        if (para == null) {
            para = new JSONObject();
        }
        wantShowDialog = showDialog;
        ListView list = new ListView(c);
        int page = 1;
        JSONObject pagination = new JSONObject();
        try {
            pagination.put("page", page);
            pagination.put("count", count);
            para.put("pagination", pagination);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustomProgressDialog customProgressDialog = CustomProgressDialog.createDialog(c,R.style.CustomProgressDialog);
        Object[] objects = new Object[]{c,customProgressDialog,para,url,page,back};
        list.setTag(objects);
        Start(list,wantShowDialog);
    }
    public static void BackObject(Context c, String url,  JSONObject para, BackObject back) {
        listViewScrollDelegate = null;
        if (para == null) {
            para = new JSONObject();
        }
        wantShowDialog = true;
        ListView list = new ListView(c);
        int page = 1;
        JSONObject pagination = new JSONObject();
        try {
            pagination.put("page", page);
            pagination.put("count", count);
            para.put("pagination", pagination);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustomProgressDialog customProgressDialog = CustomProgressDialog.createDialog(c,R.style.CustomProgressDialog);
        Object[] objects = new Object[]{c,customProgressDialog,para,url,page,back};
        list.setTag(objects);
        Start(list,wantShowDialog);
    }
    //region Helper

    /**
     * @return 传入的数据
     */
    private static String getJSOMString(JSONObject para) {
        if (para == null) {
            return "{}";
        }
        try {
            para.put("session", Default.GetSession());
            para.put("sid", Default.GetSession().getString("sid"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return para.toString();
    }

    private static void Start(final ListView listView, boolean showDialog) {
        try {
            listView.removeFooterView((footerView));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Object[] objects = (Object[]) listView.getTag();
        Context c = (Context) objects[0];
        CustomProgressDialog dialog = (CustomProgressDialog) objects[1];
        if (Default.isConnect(c)) {
            if (showDialog) {
                dialog.show();
            }
            new Thread() {
                @Override
                public void run() {
                    loadServer(listView);
                }
            }.start();
        } else {
            Default.showToast("NoNet !");
        }
    }

    private static void loadServer(ListView listView) {
        Object[] objects = (Object[]) listView.getTag();
        Context c = (Context) objects[0];
        CustomProgressDialog dialog = (CustomProgressDialog) objects[1];
        JSONObject para = (JSONObject) objects[2];
        String url = (String) objects[3];
        BackObject backObject = (BackObject) objects[5];
        String jsonString = getJSOMString(para);
        HttpParams aspara = new HttpParams("json", jsonString);
        String asurl = Default.MainURL + url;
        try {
            Log.v("AsynServer","post start");
            byte[] backValue = ServerInfo.Post(asurl, aspara,backObject);
            Log.v("AsynServer","post end");
            String backString = new String(backValue, "UTF-8");//返回数据
            Message msg = new Message();
            String backString1 = "1";//失效
            if (backString.equals(backString1)) {
                String url1 = Default.MainURL + "get_session";
                byte[] backValue1 = ServerInfo.Post(url1, aspara,backObject);
                String backString12 = new String(backValue1, "UTF-8");
                JSONObject json = new JSONObject(backString12);
                JSONObject jon = JsonClass.jj(json, "data");
                JSONObject session = JsonClass.jj(jon, "session");
                User.setUserID(JsonClass.sj(session,"uid"));
                User.setSession(JsonClass.sj(session, "sid"));
                loadServer(listView);
            } else {
                msg.obj = new Object[]{backString,listView};
                handler.sendMessage(msg);
            }
            dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("AsynServer",e.getMessage());
            dialog.dismiss();
            if (e instanceof UnknownHostException) {
                backObject.BackError("nointernet");
            } else if (e instanceof TimeoutException || e instanceof SocketTimeoutException) {
                backObject.BackError("网络连接超时");
                backObject.timeOut();
            } else if (e instanceof MalformedURLException) {
                backObject.BackError("网络错误");
            } else {
                backObject.BackError(null);
            }
        }
    }

    private static void loadServerEnd(String sender, ListView listView) {
        isLoading = false;
        Object[] objects = (Object[]) listView.getTag();
        Context c = (Context) objects[0];
        CustomProgressDialog dialog = (CustomProgressDialog) objects[1];
        JSONObject para = (JSONObject) objects[2];
        String url = (String) objects[3];
        int page = (int) objects[4];
        BackObject back = (BackObject) objects[5];
        if (listView.getLayoutParams() != null) {
            try {
                listView.removeFooterView(cpd);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("serverBack : -----------------------------------------------------");
        System.out.println("serverBack : " +  url);
        System.out.println("serverBack : "+getJSOMString(para));
        System.out.println("serverBack : "+sender);
        try {
            JSONObject jsonObject = (JSONObject) new JSONTokener(sender).nextValue();
            //region Judge page
            JSONObject status = jsonObject.getJSONObject("status");
            int succeed = status.getInt("succeed");
            if (succeed == 1) {
                if (jsonObject.has("data")) {
                    Object data = jsonObject.get("data");
                    JSONObject pages = null;
                    if (data instanceof JSONObject) {
                        if (new JSONObject(data.toString()).has("pages")) {
                            pages = new JSONObject(data.toString()).getJSONObject("pages");
                        } else {
                            if (jsonObject.has("pages")) {
                                pages = jsonObject.getJSONObject("pages");
                            }
                            if (jsonObject.has("paginated")) {
                                pages = jsonObject.getJSONObject("paginated");
                            }

                        }
                    }
                    if (data instanceof JSONArray) {
                        if (jsonObject.has("pages")) {
                            pages = jsonObject.getJSONObject("pages");
                        }
                        if (jsonObject.has("paginated")) {
                            pages = jsonObject.getJSONObject("paginated");
                        }
                    }


                    if (pages != null) {
                        if (pages.has("next")) {
                            if (pages.getInt("next") > 0) {
                                page++;
                            } else {
                                page = 0;
                            }
                        }
                        if (pages.has("more")) {
                            if (pages.getInt("more") > 0) {
                                page++;
                            } else {
                                page = 0;
                            }
                        }

                        if (pages.has("record_count")) {
                            int record_count = pages.getInt("record_count");
                            int page_count = pages.getInt("page_count");
                            if (page_count < record_count) {
                                page++;
                            } else {
                                page = 0;
                            }
                        }
                    } else {
                        page = 0;
                    }
                }
            }
            //endregion
            System.out.println("``` next page : " + page);
            Object[] obs = new Object[]{c,dialog,para,url,page,back};
            listView.setTag(obs);
            if (back != null) {
                back.Back(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
            /*MessageBox.Show(c.getString(R.string.app_name), "数据解析错误", new String[]{"确定"}, new MessageBox.MessBtnBack() {
                @Override
                public void Back(int index) {
                    Default.getActivity().onBackPressed();
                }
            });*/
        }
        dialog.dismiss();
    }
    //endregion

    //region Interface
    public abstract static class BackObject {
        public int responseCode;
        public abstract void Back(JSONObject sender);
        /** 错误返回*/
        public void BackError (String error){
            if (error != null) {
                Default.showToast(error);
            }
        }
        public void timeOut(){
            Default.showToast(Default.getActivity().getString(R.string.aliusersdk_session_error));
        }
    }

    //endregion
    private static CustomProgressDialogView cpd;//底部对话框
    private static int scrState;
    private static AbsListView.OnScrollListener listScrollerListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            System.out.println("``` state : " + scrollState);
            scrState = scrollState;

            //region 下拉刷新
            /*if (view.getAdapter().getCount() == oriCount && scrollState == SCROLL_STATE_FLING && listView.getFirstVisiblePosition() == 0) {
                if (cpd == null) {
                    cpd = new CustomProgressDialogView(context);
                }
                if (listView.getHeaderViewsCount() == 0) {
                    listView.addHeaderView(cpd);
                }
            }

            if (view.getAdapter().getCount() == oriCount + 1 && scrollState == SCROLL_STATE_FLING && listView.getFirstVisiblePosition() == 0) {
                // TODO: 2017/5/16 刷新
            }*/

            /*if (scrState == SCROLL_STATE_TOUCH_SCROLL && listView.getFirstVisiblePosition() == 0 && listView.getHeaderViewsCount() == 0) {
                if (cpd == null) {
                    cpd = new CustomProgressDialogView(context);
                }
                listView.addHeaderView(cpd);
            }*/
            //endregion

            //region 通知
            if (listViewScrollDelegate != null) {
                listViewScrollDelegate.scrollChanged(scrollState);
            }
            //endregion

            //region 上拉加载
            if (view.getAdapter().getCount() - 1== view.getLastVisiblePosition()) {
                load((ListView) view);
            }
            //endregion
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            System.out.println("``` onScroll -- firstVisibleItem : " + firstVisibleItem + " visibleItemCount : " + visibleItemCount + " totalItemCount : " + totalItemCount + " scrState : " + scrState);

            //region 加载
            int lastVisibleItem = firstVisibleItem + visibleItemCount - 1;
            if ((lastVisibleItem == totalItemCount - 5 || lastVisibleItem == totalItemCount - 3) && totalItemCount > 1) {
                load((ListView) view);
            }
            //endregion

            if (listViewScrollDelegate != null) {
                if (view.getFirstVisiblePosition() > 1) {
                    listViewScrollDelegate.showToastComponent();
                } else {
                    listViewScrollDelegate.dismissToastComponent();
                }
            }
        }
    };

    private static void load(ListView listView) {
        Object[] objects = (Object[]) listView.getTag();
        System.out.println("load objects length : " + objects.length + "  " + objects);
        Context c = (Context) objects[0];
        JSONObject para = (JSONObject) objects[2];
        int page = (int) objects[4];
        if (!isLoading) {
            System.out.println("``` load ~~~~`!");
            //region 加载
            if (cpd == null) {
                cpd = new CustomProgressDialogView(c);
                cpd.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,Default.dip2px(50, c)));
            }
            cpd.setMessage("loading...");
            cpd.showProgress();

            if (page > 0) {
                isLoading = true;
                System.out.println("``` load set true !");
                try {
                    listView.removeFooterView((footerView));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (listView.getFooterViewsCount() == 0) {
                    listView.addFooterView(cpd);
                }
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("page", page);
                    jsonObject.put("count", count);
                    para.put("pagination", jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
               Start(listView,wantShowDialog);
            } else {
                try {
                    listView.removeFooterView(cpd);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (listView.getFooterViewsCount() == 0) {
                    footerView = new NoMore(c);
                    listView.addFooterView((footerView));
                }
            }
            //endregion
        } else {
            System.out.println("````````````````` hasLoad will loadpage : " + page);
        }
    }

}
