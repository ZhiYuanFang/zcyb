package com.wzzc.NextIndex.views.e;

import android.content.Context;
import android.util.Log;

import com.wzzc.other_function.FileInfo;

import java.io.File;
import java.util.Calendar;

/**
 * Created by by Administrator on 2017/8/7.
 */

public class User {
    private static final String TAG = "User";
    private static String session;//当前用户的session
    private static String userID;//当前用户的uid
    private static boolean is_activate;//值为1时该会员已经激活，0则未激活
    private static String activity_msg;//未激活是提示语句
    private static String activity_btn_goActivity;//前往激活下单
    private static String activity_btn_justBuy;//直接购买
    private static String activity_btn_cancel;//先逛逛！
    private static String activity_check_text;//今天不再提醒
    private static String rankName;//用户类型
    private static String rankNameNew;//显示用户类型
    private static String userName;//用户名
    private static String passWord;//密码
    private static String cbi;//c米
    private static String zcb;//子成币数量
    private static String surplus;//余额 0.00
    private static String headUrl;//头像
    private static String willRebackMoney;//预返利金额
    private static boolean showMoney;//是否显示金币
    private static String cbi_withdrawal_rate;//提现比例
    private static String card_id;//银行卡号
    private static String user_rank;//用户类型 0\1（注册用户）,6（店铺）,15 （个人代理）可以C米提现
    private static boolean else_menu;//淘金店主和个人代理
    private static String else_menu_id;// 其他按键ID
    private static String shares_rate;  // 分红股

    public static void saveUser(Context context) {
        FileInfo.SetUserString("sessionid", session, context);
        Log.v(TAG, "saveUser.sessionid --> " + session);
        FileInfo.SetUserString("userid", userID, context);
        Log.v(TAG, "saveUser.userid --> " + userID);
        FileInfo.SetUserString("is_activate", (is_activate ? "1" : "0"), context);
        Log.v(TAG, "saveUser.is_activate --> " + is_activate);
        FileInfo.SetUserString("activity_msg", activity_msg, context);
        Log.v(TAG, "saveUser.activity_msg --> " + activity_msg);
        FileInfo.SetUserString("activity_btn_goActivity", activity_btn_goActivity, context);
        Log.v(TAG, "saveUser.activity_btn_goActivity --> " + activity_btn_goActivity);
        FileInfo.SetUserString("activity_btn_justBuy", activity_btn_justBuy, context);
        Log.v(TAG, "saveUser.activity_btn_justBuy --> " + activity_btn_justBuy);
        FileInfo.SetUserString("activity_btn_cancel", activity_btn_cancel, context);
        Log.v(TAG, "saveUser.activity_btn_cancel --> " + activity_btn_cancel);
        FileInfo.SetUserString("activity_check_text", activity_check_text, context);
        Log.v(TAG, "saveUser.activity_check_text --> " + activity_check_text);
        FileInfo.SetUserString("rankName", rankName, context);
        Log.v(TAG, "saveUser.rankName --> " + rankName);
        FileInfo.SetUserString("userName", userName, context);
        Log.v(TAG, "saveUser.userName --> " + userName);
        FileInfo.SetUserString("passWord", passWord, context);
        Log.v(TAG, "saveUser.passWord --> " + passWord);
        FileInfo.SetUserString("cbi", cbi, context);
        Log.v(TAG, "saveUser.cbi --> " + cbi);
        FileInfo.SetUserString("zcb", zcb, context);
        Log.v(TAG, "saveUser.zcb --> " + zcb);
        FileInfo.SetUserString("surplus", surplus, context);
        Log.v(TAG, "saveUser.surplus --> " + surplus);
        FileInfo.SetUserString("headUrl", headUrl, context);
        Log.v(TAG, "saveUser.headUrl --> " + headUrl);
        FileInfo.SetUserString("willRebackMoney", willRebackMoney, context);
        Log.v(TAG, "saveUser.willRebackMoney --> " + willRebackMoney);
        FileInfo.SetUserString("showMoney", (showMoney ? "1" : "0"), context);
        Log.v(TAG, "saveUser.showMoney --> " + showMoney);
        FileInfo.SetUserString("rankNameNew", rankNameNew, context);
        Log.v(TAG, "saveUser.rankNameNew --> " + rankNameNew);
        FileInfo.SetUserString("cbi_withdrawal_rate", cbi_withdrawal_rate, context);
        Log.v(TAG, "saveUser.cbi_withdrawal_rate --> " + cbi_withdrawal_rate);
        FileInfo.SetUserString("card_id", card_id, context);
        Log.v(TAG, "saveUser.card_id --> " + card_id);
        FileInfo.SetUserString("user_rank", user_rank, context);
        Log.v(TAG, "saveUser.user_rank --> " + user_rank);
        FileInfo.SetUserString("else_menu", (else_menu ? "1" : "0"), context);
        Log.v(TAG, "saveUser.else_menu --> " + else_menu);
        FileInfo.SetUserString("else_menu_id", else_menu_id, context);
        Log.v(TAG, "saveUser.else_menu_id --> " + else_menu_id);
        FileInfo.SetUserString("shares_rate", shares_rate, context);
    }

    public static void getUser(Context context) {
        session = FileInfo.GetUserString("sessionid", context);
        Log.i(TAG, "session ? " + session);
        userID = FileInfo.GetUserString("userid", context);
        Log.i(TAG, "userID ? " + userID);
        is_activate = FileInfo.GetUserString("is_activate", context).equals("1");
        Log.i(TAG, "is_activate ? " + is_activate);
        else_menu = FileInfo.GetUserString("else_menu",context).equals("1");
        Log.i(TAG, "else_menu ? " + else_menu);
        else_menu_id = FileInfo.GetUserString("else_menu_id",context);
        Log.i(TAG, "else_menu_id ? " + else_menu_id);
        activity_msg = FileInfo.GetUserString("activity_msg", context);
        shares_rate = FileInfo.GetUserString("shares_rate",context);
        Log.i(TAG, "activity_msg ? " + activity_msg);
        activity_btn_goActivity = FileInfo.GetUserString("activity_btn_goActivity", context);
        Log.i(TAG, "activity_btn_goActivity ? " + activity_btn_goActivity);
        activity_btn_justBuy = FileInfo.GetUserString("activity_btn_justBuy", context);
        Log.i(TAG, "activity_btn_justBuy ? " + activity_btn_justBuy);
        activity_btn_cancel = FileInfo.GetUserString("activity_btn_cancel", context);
        Log.i(TAG, "activity_btn_cancel ? " + activity_btn_cancel);
        activity_check_text = FileInfo.GetUserString("activity_check_text", context);
        Log.i(TAG, "activity_check_text ? " + activity_check_text);
        rankName = FileInfo.GetUserString("rankName", context);
        Log.i(TAG, "rankName ? " + rankName);
        userName = FileInfo.GetUserString("userName", context);
        Log.i(TAG, "userName ? " + userName);
        passWord = FileInfo.GetUserString("passWord", context);
        Log.i(TAG, "passWord ? " + passWord);
        cbi = FileInfo.GetUserString("cbi", context);
        Log.i(TAG, "cbi ? " + cbi);
        zcb = FileInfo.GetUserString("zcb", context);
        Log.i(TAG, "zcb ? " + zcb);
        surplus = FileInfo.GetUserString("surplus", context);
        Log.i(TAG, "surplus ? " + surplus);
        headUrl = FileInfo.GetUserString("headUrl", context);
        Log.i(TAG, "headUrl ? " + headUrl);
        willRebackMoney = FileInfo.GetUserString("willRebackMoney", context);
        Log.i(TAG, "willRebackMoney ? " + willRebackMoney);
        showMoney = FileInfo.GetUserString("showMoney", context).equals("1");
        Log.i(TAG, "showMoney ? " + showMoney);
        rankNameNew = FileInfo.GetUserString("rankNameNew", context);
        Log.i(TAG, "rankNameNew ? " + rankNameNew);
    }

    public static void clearUser(Context context) {
        FileInfo.RemoveUserString("sessionid", context);
        FileInfo.RemoveUserString("userid", context);
        FileInfo.RemoveUserString("is_activate", context);
        FileInfo.RemoveUserString("else_menu", context);
        FileInfo.RemoveUserString("else_menu_id", context);
        FileInfo.RemoveUserString("shares_rate", context);
        FileInfo.RemoveUserString("activity_msg", context);
        FileInfo.RemoveUserString("activity_btn_goActivity", context);
        FileInfo.RemoveUserString("activity_btn_justBuy", context);
        FileInfo.RemoveUserString("activity_btn_cancel", context);
        FileInfo.RemoveUserString("activity_check_text", context);
        FileInfo.RemoveUserString("rankName", context);
        FileInfo.RemoveUserString("userName", context);
        FileInfo.RemoveUserString("passWord", context);
        FileInfo.RemoveUserString("cbi", context);
        FileInfo.RemoveUserString("zcb", context);
        FileInfo.RemoveUserString("surplus", context);
        FileInfo.RemoveUserString("headUrl", context);
        FileInfo.RemoveUserString("willRebackMoney", context);
        FileInfo.RemoveUserString("showMoney", context);
        FileInfo.RemoveUserString("rankNameNew", context);
        FileInfo.RemoveUserString("cbi_withdrawal_rate", context);
        FileInfo.RemoveUserString("card_id", context);
        FileInfo.RemoveUserString("user_rank", context);
        noLogin();
    }

    public static String getTAG() {
        return TAG;
    }

    public static String getCbi_withdrawal_rate() {
        if (cbi_withdrawal_rate == null) {
            cbi_withdrawal_rate = "";
        }
        return cbi_withdrawal_rate;
    }

    public static void setCbi_withdrawal_rate(String cbi_withdrawal_rate) {
        User.cbi_withdrawal_rate = cbi_withdrawal_rate;
    }

    public static String getCard_id() {
        if (card_id == null) {
            card_id = "";
        }
        return card_id;
    }

    public static void setCard_id(String card_id) {
        User.card_id = card_id;
    }

    public static String getUser_rank() {
        return user_rank;
    }

    public static void setUser_rank(String user_rank) {
        if (user_rank == null) {
            user_rank = "";
        }
        User.user_rank = user_rank;
    }

    public static String getRankNameNew() {
        return rankNameNew;
    }

    public static void setRankNameNew(String rankNameNew) {
        Log.i(TAG, "User.setRankNameNew --> " + rankNameNew);
        User.rankNameNew = rankNameNew;
    }

    public static boolean isShowActivity(Context c) {
        String str_day = FileInfo.GetUserString("not_showActivity_day" + userName, c);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        Log.i(TAG, "isShowActivity ? " + (!String.valueOf(day).equals(str_day)));
        return !String.valueOf(day).equals(str_day);
    }

    public static void setShowActivity(Context c, boolean showActivity) {
        if (!showActivity) {
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            FileInfo.SetUserString("not_showActivity_day" + userName, String.valueOf(day), c);
        } else {
            FileInfo.RemoveUserString("not_showActivity_day" + userName, c);
        }
        Log.i(TAG, "User.setShowActivity --> " + showActivity);
    }

    public static String getActivity_msg() {
        Log.i(TAG, "activity_msg ? " + activity_msg);
        return activity_msg;
    }

    public static void setActivity_msg(String activity_msg) {
        Log.i(TAG, "User.setActivity_msg --> " + activity_msg);
        User.activity_msg = activity_msg;
    }

    public static String getActivity_btn_goActivity() {
        if (activity_btn_goActivity.length() == 0) {
            activity_btn_goActivity = "前往激活下单";
        }
        Log.i(TAG, "activity_btn_goActivity ? " + activity_btn_goActivity);
        return activity_btn_goActivity;
    }

    public static void setActivity_btn_goActivity(String activity_btn_goActivity) {
        Log.i(TAG, "User.setActivity_btn_goActivity --> " + activity_btn_goActivity);
        User.activity_btn_goActivity = activity_btn_goActivity;
    }

    public static String getActivity_btn_justBuy() {
        if (activity_btn_justBuy.length() == 0) {
            activity_btn_justBuy = "直接购买";
        }
        Log.i(TAG, "activity_btn_justBuy ? " + activity_btn_justBuy);
        return activity_btn_justBuy;
    }

    public static void setActivity_btn_justBuy(String activity_btn_justBuy) {
        Log.i(TAG, "User.setActivity_btn_justBuy --> " + activity_btn_justBuy);
        User.activity_btn_justBuy = activity_btn_justBuy;
    }

    public static String getActivity_btn_cancel() {
        if (activity_btn_cancel.length() == 0) {
            activity_btn_cancel = "先逛逛";
        }
        Log.i(TAG, "activity_btn_cancel ? " + activity_btn_cancel);
        return activity_btn_cancel;
    }

    public static void setActivity_btn_cancel(String activity_btn_cancel) {
        Log.i(TAG, "User.setActivity_btn_cancel --> " + activity_btn_cancel);
        User.activity_btn_cancel = activity_btn_cancel;
    }

    public static String getActivity_check_text() {
        if (activity_check_text.length() == 0) {
            activity_check_text = "今天不再提醒";
        }
        Log.i(TAG, "activity_check_text ? " + activity_check_text);
        return activity_check_text;
    }

    public static void setActivity_check_text(String activity_check_text) {
        Log.i(TAG, "User.setActivity_check_text --> " + activity_check_text);
        User.activity_check_text = activity_check_text;
    }

    public static String getSession() {
        Log.i(TAG, "session ? " + session);
        return session;
    }

    public static void setSession(String session) {
        Log.i(TAG, "User.setSession --> " + session);
        User.session = session;
    }

    public static String getUserID() {
        Log.i(TAG, "userID ? " + userID);
        return userID;
    }

    public static void setUserID(String userID) {
        Log.i(TAG, "User.setUserID --> " + userID);
        User.userID = userID;
    }

    public static boolean is_activate() {
        Log.i(TAG, "is_activate ? " + is_activate);
        return is_activate;
    }

    public static void setIs_activate(boolean is_activate) {
        Log.i(TAG, "User.setIs_activate --> " + is_activate);
        User.is_activate = is_activate;
    }

    public static boolean else_menu(){
        Log.i(TAG, "else_menu ? " + else_menu);
        return else_menu;
    }

    public static void setElse_menu(boolean else_menu){
        Log.i(TAG, "User.setElse_menu --> " + is_activate);
        User.else_menu = else_menu;
    }

    public static String shares_rate(){
        return shares_rate;
    }

    public static void setShares_rate(String shares_rate){
        User.shares_rate= shares_rate;
    }

    public static String else_menu_id(){
        return else_menu_id;
    }

    public static void setElse_menu_id(String id){
        User.else_menu_id = id;
    }

    public static boolean isLogin() {
        Log.i(TAG, "isLogin ? " + (rankName.length() > 0 && !rankName.equals("0")));
        return rankName.length() > 0 && !rankName.equals("0");
    }

    public static void noLogin() {
        user_rank = "";
        card_id = "";
        rankName = "";
        userName = "尚未登陆";
        passWord = "none";
        cbi = "0.00";
        zcb = "0.00";
        surplus = "0.00";
        headUrl = "";
        willRebackMoney = "0.00";
        activity_msg = "";
    }

    public static void signOut(Context context) {
        session = "";
        userID = "";
        clearUser(context);
    }

    public static String getRankName() {
        Log.i(TAG, "rankName ? " + rankName);
        return rankName;
    }

    public static void setRankName(String rankName) {
        Log.i(TAG, "User.setRankName --> " + rankName);
        User.rankName = rankName;
    }

    public static String getUserName() {
        Log.i(TAG, "userName ? " + userName);
        return userName;
    }

    public static void setUserName(String userName) {
        if (userName == null || userName.length() < 1) {
            User.userName = "尚未登陆";
        } else
            User.userName = userName;
        Log.i(TAG, "User.setUserName --> " + userName);
    }

    public static String getPassWord() {
        Log.i(TAG, "passWord ? " + passWord);
        return passWord;
    }

    public static void setPassWord(String passWord) {
        if (passWord == null || passWord.length() < 1) {
            User.passWord = "none";
        } else
            User.passWord = passWord;
        Log.i(TAG, "User.setPassWord --> " + passWord);
    }

    public static String getCbi() {
        Log.i(TAG, "cbi ? " + cbi);
        return cbi;
    }

    public static void setCbi(String cbi) {
        if (cbi == null || cbi.length() < 1) {
            User.cbi = "0.00";
        } else
            User.cbi = cbi;
        Log.i(TAG, "User.setCbi --> " + cbi);
    }

    public static String getZcb() {
        Log.i(TAG, "zcb ? " + zcb);
        return zcb;
    }

    public static void setZcb(String zcb) {
        if (zcb == null || zcb.length() < 1) {
            User.zcb = "0.00";
        } else
            User.zcb = zcb;
        Log.i(TAG, "User.setZcb --> " + zcb);
    }

    public static String getSurplus() {
        Log.i(TAG, "surplus ? " + surplus);
        return surplus;
    }

    public static void setSurplus(String surplus) {
        if (surplus == null || surplus.length() < 1) {
            User.surplus = "0.00";
        } else
            User.surplus = surplus;
        Log.i(TAG, "User.setSurplus --> " + surplus);
    }

    public static String getHeadUrl() {
        Log.i(TAG, "headUrl ? " + headUrl);
        return headUrl;
    }

    public static void setHeadUrl(String headUrl) {
        Log.i(TAG, "User.setHeadUrl --> " + headUrl);
        User.headUrl = headUrl;
    }

    public static String getWillRebackMoney() {
        Log.i(TAG, "willRebackMoney ? " + willRebackMoney);
        return willRebackMoney;
    }

    public static void setWillRebackMoney(String willRebackMoney) {
        if (willRebackMoney == null || willRebackMoney.length() < 1) {
            User.willRebackMoney = "0.00";
        } else
            User.willRebackMoney = willRebackMoney;
        Log.i(TAG, "User.setWillRebackMoney --> " + willRebackMoney);
    }

    public static boolean isShowMoney() {
        Log.i(TAG, "showMoney ? " + showMoney);
        return showMoney;
    }

    public static void setShowMoney(boolean showMoney) {
        Log.i(TAG, "User.setShowMoney --> " + showMoney);
        User.showMoney = showMoney;
    }
}
