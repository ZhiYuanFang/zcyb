package com.wzzc.NextIndex.views.e;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wzzc.NextIndex.IndexDialog;
import com.wzzc.NextIndex.NextDelegate;
import com.wzzc.NextIndex.views.e.other_activity.BusinessShop.agent.AgentActivity;
import com.wzzc.NextIndex.views.e.other_activity.BusinessShop.contract.ContractActivity;
import com.wzzc.NextIndex.views.e.other_activity.BusinessShop.data.MerchantDataActivity;
import com.wzzc.NextIndex.views.e.other_activity.BusinessShop.invoice.InvoiceActivity;
import com.wzzc.NextIndex.views.e.other_activity.BusinessShop.money.commission.detail.ShopDetailedActivity;
import com.wzzc.NextIndex.views.e.other_activity.BusinessShop.money.commission.statistic.CommissionActivity;
import com.wzzc.NextIndex.views.e.other_activity.BusinessShop.money.commission.withdrawals.ShopWithdrawalsActivity;
import com.wzzc.NextIndex.views.e.other_activity.BusinessShop.money.replenishment.ReplenishmentActivity;
import com.wzzc.NextIndex.views.e.other_activity.BusinessShop.money.sendProduction.GiveProductsActivity;
import com.wzzc.NextIndex.views.e.other_activity.CBI.CBIActivity;
import com.wzzc.NextIndex.views.e.other_activity.InvitedCodeActivity;
import com.wzzc.NextIndex.views.e.other_activity.NextRelation.NextRelations;
import com.wzzc.NextIndex.views.e.other_activity.NextRelation.ReferralPerson;
import com.wzzc.NextIndex.views.e.other_activity.RebateRanking.RebateRankingActivity;
import com.wzzc.NextIndex.views.e.other_activity.RecommendBusinessShops.RecommendBusinessShopsActivity;
import com.wzzc.NextIndex.views.e.other_activity.address.AddressActivity;
import com.wzzc.NextIndex.views.e.other_activity.backmoney.BackMoneyActivity;
import com.wzzc.NextIndex.views.e.other_activity.collection.CollectionActivity;
import com.wzzc.NextIndex.views.e.other_activity.money.MoneyActivity;
import com.wzzc.NextIndex.views.e.other_activity.order.OrderListActivity;
import com.wzzc.NextIndex.views.e.other_activity.order.hasFinishOrder.CompletedActivity;
import com.wzzc.NextIndex.views.e.other_activity.order.waitCollectOrder.CollectThemActivity;
import com.wzzc.NextIndex.views.e.other_activity.order.waitPostOrder.PostOrderActivity;
import com.wzzc.NextIndex.views.e.other_activity.order.waitSendOrder.BackOrdersActivity;
import com.wzzc.NextIndex.views.e.other_activity.settings.SettingActivity;
import com.wzzc.NextSuperDeliver.SuperDeliver;
import com.wzzc.NextSuperDeliver.list.Category;
import com.wzzc.NextSuperDeliver.list.SuperDeliverList;
import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.index.PhotoDelegate;
import com.wzzc.new_index.userCenter.password.PasswordEditActivity;
import com.wzzc.new_index.userCenter.regest.personalAgent.RegisterPersonalAgentActivity;
import com.wzzc.new_index.userCenter.regest.personalAgent.SignupAgentPreActivity;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.ImageHelper;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.other_function.action.ClickDelegate;
import com.wzzc.other_function.pay.PayFromWX;
import com.wzzc.other_function.pay.PayFromZFB;
import com.wzzc.other_function.pay.PayView;
import com.wzzc.other_view.progressDialog.CustomProgressDialog;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by by Administrator on 2017/8/5.
 */
@ContentView(R.layout.view_usercenter)
public class UserCenter extends BaseView implements View.OnClickListener {
    public NextDelegate nextDelegate;
    PhotoDelegate photoDelegate;
    LoginDelegate loginDelegate;
    ClickDelegate clickDelegate;
    //region 组件
    @ViewInject(R.id.tv_check_version)
    TextView tv_check_version;
    @ViewInject(R.id.tv_upGrate)
    TextView tv_upGrate;
    @ViewInject(R.id.shares_rate_tv)
    TextView shares_rate_tv;
    @ViewInject(R.id.ib_look)
    ImageButton ib_look;
    @ViewInject(R.id.img_touxiang)
    ImageView img_touxiang;
    @ViewInject(R.id.user_names)
    TextView user_names;
    @ViewInject(R.id.user_money)
    TextView user_money;
    @ViewInject(R.id.user_recharge)
    LinearLayout user_recharge;
    @ViewInject(R.id.user_withdrawals)
    LinearLayout user_withdrawals;
    @ViewInject(R.id.layout_usercenter_part)
    LinearLayout layout_usercenter_part;
    @ViewInject(R.id.layout_usercenter_item)
    LinearLayout layout_usercenter_item;
    @ViewInject(R.id.tv_tag)
    TextView tv_tag;
    @ViewInject(R.id.tv_date)
    TextView tv_date;
    @ViewInject(R.id.tv_submit)
    TextView tv_submit;
    @ViewInject(R.id.layout_a)
    LinearLayout layout_a;
    @ViewInject(R.id.layout_b)
    LinearLayout layout_b;
    @ViewInject(R.id.lab_setting)
    TextView lab_setting;
    @ViewInject(R.id.user_nopayment)
    LinearLayout user_nopayment;
    @ViewInject(R.id.user_goods)
    LinearLayout user_goods;
    @ViewInject(R.id.user_nogoods)
    LinearLayout user_nogoods;
    @ViewInject(R.id.user_completed)
    LinearLayout user_completed;
    //endregion
    LinearLayout layout_current_part;
    LinearLayout layout_current_item;
    public static final Integer MYMONEY = 101, MYREDPACKAGE = 102, MYORDER = 103, MYREPORTCHART = 104, MYCOLLECTION = 105,
            MYADDRESS = 106, MYPASSWORD = 107, UPLOAD = 108, PROMOTION = 109, ADDMORE = 120, FREEPRODUCTION = 121, COMMISSIONDETAIL = 122,
            COMMISSIONGET = 123, BUSSINESSORDERMENNAGE = 124, DELIVERY = 125, FINDBUSSINESS = 126, BACKMONEY = 127, INVITRF_CODE = 128/*邀请码*/,
            CBI = 129/*c米提现*/, RelationsReport = 130/*推荐关系*/, RebackRanking = 131/*返利排行*/;

    private String userID;

    public UserCenter(Context context) {
        super(context);
        init();
    }

    public UserCenter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        tv_date.setText((month + 1) + "月" + day + "日");
        user_nopayment.setOnClickListener(this);
        user_goods.setOnClickListener(this);
        user_nogoods.setOnClickListener(this);
        user_completed.setOnClickListener(this);
        img_touxiang.setOnClickListener(this);
        ib_look.setOnClickListener(this);
        tv_upGrate.setOnClickListener(this);
        user_names.setOnClickListener(this);
        user_money.setOnClickListener(this);
        layout_usercenter_part.setOnClickListener(this);
        layout_usercenter_item.setOnClickListener(this);
        tv_tag.setOnClickListener(this);
        tv_date.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
        layout_a.setOnClickListener(this);
        layout_b.setOnClickListener(this);
        user_recharge.setOnClickListener(this);
        user_withdrawals.setOnClickListener(this);
        lab_setting.setOnClickListener(this);
        tv_check_version.setOnClickListener(this);
    }

    public void setInfo(LoginDelegate loginDelegate, ClickDelegate clickDelegate, PhotoDelegate photoDelegate, JSONObject sender) {
        this.loginDelegate = loginDelegate;
        this.clickDelegate = clickDelegate;
        this.photoDelegate = photoDelegate;
        noLogin();//先显示为未登陆状态
        userID = JsonClass.sj(sender, "id");
        JSONObject json_activity = JsonClass.jj(sender, "is_activate");
        User.setIs_activate(JsonClass.ij(json_activity, "status") == 1);
        User.setActivity_msg(JsonClass.sj(json_activity, "msg"));
        User.setActivity_btn_cancel(JsonClass.sj(json_activity, "btn3"));
        User.setActivity_btn_goActivity(JsonClass.sj(json_activity, "btn1"));
        User.setActivity_btn_justBuy(JsonClass.sj(json_activity, "btn2"));
        User.setActivity_check_text(JsonClass.sj(json_activity, "checkbox"));
        User.setHeadUrl(JsonClass.sj(sender, "avatar"));
        User.setRankName(JsonClass.sj(sender, "rank_name"));
        User.setRankNameNew(JsonClass.sj(sender, "rank_name_new"));
        User.setUserName(JsonClass.sj(sender, "name"));
        User.setSurplus(JsonClass.sj(sender, "surplus").substring(1, JsonClass.sj(sender, "surplus").length()));
        User.setShowMoney(JsonClass.ij(sender, "user_money_sw") == 0);
        User.setCbi_withdrawal_rate(JsonClass.sj(sender, "cbi_withdrawal_rate"));
        User.setUser_rank(JsonClass.sj(sender, "user_rank"));
        User.setCard_id(JsonClass.sj(sender, "card_id"));
        User.setElse_menu(JsonClass.isHaveData(sender,"else_menu"));
        User.setShares_rate(JsonClass.sj(sender,"shares_rate"));
        if(User.else_menu()){
            JSONObject elseMenuJson = sender.optJSONObject("else_menu");
            if(null != elseMenuJson) {
                User.setElse_menu_id(JsonClass.getElseMenuId(elseMenuJson.optJSONArray("data")));
            }
        }
        if ("商圈代理".equals(User.getRankName())) {
            //region 商圈代理
            layout_current_item = (LinearLayout) LayoutInflater.from(Default.getActivity()).inflate(R.layout.usercenter_item_business, null);
            layout_current_part = (LinearLayout) LayoutInflater.from(Default.getActivity()).inflate(R.layout.usercenter_parts_business, null);
            TextView tv_business_member = (TextView) layout_current_part.findViewById(R.id.business_member);
            TextView tv_business_money = (TextView) layout_current_part.findViewById(R.id.business_money);
            TextView tv_cb = (TextView) layout_current_part.findViewById(R.id.registra_cb);
            LinearLayout layout_mymoney = (LinearLayout) layout_current_item.findViewById(R.id.usercenter_item_business_mymoney);
            LinearLayout layout_order = (LinearLayout) layout_current_item.findViewById(R.id.usercenter_item_business_order);
            LinearLayout layout_report_chart = (LinearLayout) layout_current_item.findViewById(R.id.usercenter_item_business_report_chart);
            LinearLayout layout_collection = (LinearLayout) layout_current_item.findViewById(R.id.usercenter_item_business_collection);
            LinearLayout layout_address = (LinearLayout) layout_current_item.findViewById(R.id.usercenter_item_business_address);
            LinearLayout layout_password = (LinearLayout) layout_current_item.findViewById(R.id.usercenter_item_business_password);
            LinearLayout layout_find = (LinearLayout) layout_current_item.findViewById(R.id.usercenter_item_business_find);
            LinearLayout layout_upload = (LinearLayout) layout_current_item.findViewById(R.id.usercenter_item_business_upload);
            LinearLayout layout_promotion = (LinearLayout) layout_current_item.findViewById(R.id.usercenter_item_business_promotion);
            LinearLayout layout_add = (LinearLayout) layout_current_item.findViewById(R.id.usercenter_item_business_add);
            LinearLayout layout_invited_code = (LinearLayout) layout_current_item.findViewById(R.id.invited_code);
            LinearLayout layout_relations_report = (LinearLayout) layout_current_item.findViewById(R.id.relations_report);
            layout_relations_report.setTag(RelationsReport);
            layout_relations_report.setOnClickListener(this);
            LinearLayout layout_reback_ranking = (LinearLayout) layout_current_item.findViewById(R.id.reback_ranking);
            layout_reback_ranking.setTag(RebackRanking);
            layout_reback_ranking.setOnClickListener(this);

            tv_business_member.setText(JsonClass.sj(sender, "user_num"));
            tv_business_money.setText(JsonClass.sj(sender, "frozen_money"));
            User.setCbi(JsonClass.sj(sender, "cbi"));
            if (sender.has("cbi")) {
                tv_cb.setText(User.getCbi());
            }
            layout_invited_code.setTag(INVITRF_CODE);
            layout_invited_code.setOnClickListener(this);
            layout_mymoney.setTag(MYMONEY);
            layout_mymoney.setOnClickListener(this);
            layout_order.setTag(MYORDER);
            layout_order.setOnClickListener(this);
            layout_report_chart.setTag(MYREPORTCHART);
            layout_report_chart.setOnClickListener(this);
            layout_collection.setTag(MYCOLLECTION);
            layout_collection.setOnClickListener(this);
            layout_address.setTag(MYADDRESS);
            layout_address.setOnClickListener(this);
            layout_password.setTag(MYPASSWORD);
            layout_password.setOnClickListener(this);
            layout_find.setTag(FINDBUSSINESS);
            layout_find.setOnClickListener(this);
            layout_upload.setTag(UPLOAD);
            layout_upload.setOnClickListener(this);
            layout_promotion.setTag(PROMOTION);
            layout_promotion.setOnClickListener(this);
            layout_add.setTag(ADDMORE);
            layout_add.setOnClickListener(this);
            //endregion
        } else if ("合作店铺".equals(User.getRankName())) {
            //region 合作店铺
            layout_current_item = (LinearLayout) LayoutInflater.from(Default.getActivity()).inflate(R.layout.usercenter_item_shop, null);
            layout_current_part = (LinearLayout) LayoutInflater.from(Default.getActivity()).inflate(R.layout.usercenter_parts_shop, null);
            TextView tv_shop_member = (TextView) layout_current_part.findViewById(R.id.shop_member);
            TextView tv_shop_gcb = (TextView) layout_current_part.findViewById(R.id.shop_gcb);
            TextView tv_shop_zcb = (TextView) layout_current_part.findViewById(R.id.shop_zcb);
            TextView tv_shop_money = (TextView) layout_current_part.findViewById(R.id.shop_money);
            TextView tv_cb = (TextView) layout_current_part.findViewById(R.id.registra_cb);
            LinearLayout layout_money = (LinearLayout) layout_current_item.findViewById(R.id.usercenter_item_shop_mymoney);
            LinearLayout layout_order = (LinearLayout) layout_current_item.findViewById(R.id.usercenter_item_shop_order);
            LinearLayout layout_freeproduction = (LinearLayout) layout_current_item.findViewById(R.id.usercenter_item_shop_free_production);
            LinearLayout layout_collection = (LinearLayout) layout_current_item.findViewById(R.id.usercenter_item_shop_collection);
            LinearLayout layout_address = (LinearLayout) layout_current_item.findViewById(R.id.usercenter_item_shop_address);
            LinearLayout layout_password = (LinearLayout) layout_current_item.findViewById(R.id.usercenter_item_shop_password);
            LinearLayout layout_commission_detail = (LinearLayout) layout_current_item.findViewById(R.id.usercenter_item_shop_commission_detail);
            LinearLayout layout_commission_get = (LinearLayout) layout_current_item.findViewById(R.id.usercenter_item_shop_commission_get);
            LinearLayout layout_commission_order = (LinearLayout) layout_current_item.findViewById(R.id.usercenter_item_shop_commission_order);
            LinearLayout layout_delivery = (LinearLayout) layout_current_item.findViewById(R.id.usercenter_item_shop_commission_delivery);
            LinearLayout layout_cbi = (LinearLayout) layout_current_item.findViewById(R.id.cbi);
            LinearLayout layout_invited_code = (LinearLayout) layout_current_item.findViewById(R.id.invited_code);
            LinearLayout layout_relations_report = (LinearLayout) layout_current_item.findViewById(R.id.relations_report);
            layout_relations_report.setTag(RelationsReport);
            layout_relations_report.setOnClickListener(this);
            LinearLayout layout_reback_ranking = (LinearLayout) layout_current_item.findViewById(R.id.reback_ranking);
            layout_reback_ranking.setTag(RebackRanking);
            layout_reback_ranking.setOnClickListener(this);

            tv_shop_member.setText(JsonClass.sj(sender, "user_num"));
            tv_shop_gcb.setText(JsonClass.sj(sender, "integral"));
            tv_shop_zcb.setText(JsonClass.sj(sender, "rank_points"));
            tv_shop_money.setText(JsonClass.sj(sender, "frozen_money"));
            User.setCbi(JsonClass.sj(sender, "cbi"));
            if (sender.has("cbi")) {
                tv_cb.setText(User.getCbi());
            }
            layout_invited_code.setTag(INVITRF_CODE);
            layout_invited_code.setOnClickListener(this);
            layout_cbi.setTag(CBI);
            layout_cbi.setOnClickListener(this);
            layout_money.setTag(MYMONEY);
            layout_money.setOnClickListener(this);
            layout_order.setTag(MYORDER);
            layout_order.setOnClickListener(this);
            layout_freeproduction.setTag(FREEPRODUCTION);
            layout_freeproduction.setOnClickListener(this);
            layout_collection.setTag(MYCOLLECTION);
            layout_collection.setOnClickListener(this);
            layout_address.setTag(MYADDRESS);
            layout_address.setOnClickListener(this);
            layout_password.setTag(MYPASSWORD);
            layout_password.setOnClickListener(this);
            layout_commission_detail.setTag(COMMISSIONDETAIL);
            layout_commission_detail.setOnClickListener(this);
            layout_commission_get.setTag(COMMISSIONGET);
            layout_commission_get.setOnClickListener(this);
            layout_commission_order.setTag(BUSSINESSORDERMENNAGE);
            layout_commission_order.setOnClickListener(this);
            layout_delivery.setTag(DELIVERY);
            layout_delivery.setOnClickListener(this);
            //endregion
        } else if ("市级代理".equals(User.getRankName())) {
            //region 市级代理
            layout_current_item = (LinearLayout) LayoutInflater.from(Default.getActivity()).inflate(R.layout.usercenter_item_municipal, null);
            layout_current_part = (LinearLayout) LayoutInflater.from(Default.getActivity()).inflate(R.layout.usercenter_parts_municipal, null);
            TextView tv_gcb = (TextView) layout_current_part.findViewById(R.id.municipal_gcb);
            TextView tv_zcb = (TextView) layout_current_part.findViewById(R.id.municipal_zcb);
            TextView tv_cb = (TextView) layout_current_part.findViewById(R.id.registra_cb);
            LinearLayout layout_money = (LinearLayout) layout_current_item.findViewById(R.id.municipal_mymoney);
            LinearLayout layout_order = (LinearLayout) layout_current_item.findViewById(R.id.municipal_order);
            LinearLayout layout_report_chart = (LinearLayout) layout_current_item.findViewById(R.id.municipal_report_chart);
            LinearLayout layout_collection = (LinearLayout) layout_current_item.findViewById(R.id.municipal_collection);
            LinearLayout layout_address = (LinearLayout) layout_current_item.findViewById(R.id.municipal_address);
            LinearLayout layout_password = (LinearLayout) layout_current_item.findViewById(R.id.municipal_password);
            LinearLayout layout_invited_code = (LinearLayout) layout_current_item.findViewById(R.id.invited_code);
            LinearLayout layout_relations_report = (LinearLayout) layout_current_item.findViewById(R.id.relations_report);
            layout_relations_report.setTag(RelationsReport);
            layout_relations_report.setOnClickListener(this);
            LinearLayout layout_reback_ranking = (LinearLayout) layout_current_item.findViewById(R.id.reback_ranking);
            layout_reback_ranking.setTag(RebackRanking);
            layout_reback_ranking.setOnClickListener(this);

            tv_gcb.setText(JsonClass.sj(sender, "integral"));
            tv_zcb.setText(JsonClass.sj(sender, "rank_points"));
            User.setCbi(JsonClass.sj(sender, "cbi"));
            if (sender.has("cbi")) {
                tv_cb.setText(User.getCbi());
            }
            layout_invited_code.setTag(INVITRF_CODE);
            layout_invited_code.setOnClickListener(this);
            layout_money.setTag(MYMONEY);
            layout_money.setOnClickListener(this);
            layout_order.setTag(MYORDER);
            layout_order.setOnClickListener(this);
            layout_report_chart.setTag(MYREPORTCHART);
            layout_report_chart.setOnClickListener(this);
            layout_collection.setTag(MYCOLLECTION);
            layout_collection.setOnClickListener(this);
            layout_address.setTag(MYADDRESS);
            layout_address.setOnClickListener(this);
            layout_password.setTag(MYPASSWORD);
            layout_password.setOnClickListener(this);
            //endregion
        } else if ("注册用户".equals(User.getRankName())) {
            //region 注册用户
//            tv_upGrate.setVisibility(VISIBLE);
//            tv_upGrate.setOnClickListener(personAgentListener());

            layout_current_item = (LinearLayout) LayoutInflater.from(Default.getActivity()).inflate(R.layout.usercenter_item_registra, null);
            layout_current_part = (LinearLayout) LayoutInflater.from(Default.getActivity()).inflate(R.layout.usercenter_parts_registra, null);
            TextView tv_gcb = (TextView) layout_current_part.findViewById(R.id.registra_gcb);
            TextView tv_zcb = (TextView) layout_current_part.findViewById(R.id.registra_zcb);
            TextView tv_cb = (TextView) layout_current_part.findViewById(R.id.registra_cb);
            TextView tv_ygfl = (TextView) layout_current_part.findViewById(R.id.registra_ygfl);
            LinearLayout layout_money = (LinearLayout) layout_current_item.findViewById(R.id.registra_my_money);
            LinearLayout layout_order = (LinearLayout) layout_current_item.findViewById(R.id.registra_order);
            LinearLayout layout_collection = (LinearLayout) layout_current_item.findViewById(R.id.registra_collection);
            LinearLayout layout_address = (LinearLayout) layout_current_item.findViewById(R.id.registra_address);
            LinearLayout layout_password = (LinearLayout) layout_current_item.findViewById(R.id.registra_password);
            LinearLayout layout_back_money = (LinearLayout) layout_current_item.findViewById(R.id.back_money);
            LinearLayout layout_invited_code = (LinearLayout) layout_current_item.findViewById(R.id.invited_code);
            LinearLayout layout_relations_report = (LinearLayout) layout_current_item.findViewById(R.id.relations_report);
            LinearLayout layout_cbi = (LinearLayout) layout_current_item.findViewById(R.id.cbi);
            layout_cbi.setTag(CBI);
            layout_cbi.setOnClickListener(this);
            layout_relations_report.setTag(RelationsReport);
            layout_relations_report.setOnClickListener(this);
            LinearLayout layout_reback_ranking = (LinearLayout) layout_current_item.findViewById(R.id.reback_ranking);
            layout_reback_ranking.setTag(RebackRanking);
            layout_reback_ranking.setOnClickListener(this);

            tv_gcb.setText(JsonClass.sj(sender, "integral"));
            tv_zcb.setText(JsonClass.sj(sender, "rank_points"));
            User.setCbi(JsonClass.sj(sender, "cbi"));
            if (sender.has("cbi")) {
                tv_cb.setText(User.getCbi());
            }
            tv_ygfl.setText(JsonClass.sj(sender, "ygfl"));
            layout_invited_code.setTag(INVITRF_CODE);
            layout_invited_code.setOnClickListener(this);
            layout_back_money.setTag(BACKMONEY);
            layout_back_money.setOnClickListener(this);
            layout_money.setTag(MYMONEY);
            layout_money.setOnClickListener(this);
            layout_order.setTag(MYORDER);
            layout_order.setOnClickListener(this);
            layout_collection.setTag(MYCOLLECTION);
            layout_collection.setOnClickListener(this);
            layout_address.setTag(MYADDRESS);
            layout_address.setOnClickListener(this);
            layout_password.setTag(MYPASSWORD);
            layout_password.setOnClickListener(this);
            tv_tag.setVisibility(GONE);
            //endregion
        } else if ("区县代理".equals(User.getRankName())) {
            //region 区县代理
            layout_current_item = (LinearLayout) LayoutInflater.from(Default.getActivity()).inflate(R.layout.usercenter_item_county_agency, null);
            layout_current_part = (LinearLayout) LayoutInflater.from(Default.getActivity()).inflate(R.layout.usercenter_parts_municipal, null);
            TextView tv_gcb = (TextView) layout_current_part.findViewById(R.id.municipal_gcb);
            TextView tv_zcb = (TextView) layout_current_part.findViewById(R.id.municipal_zcb);
            TextView tv_cb = (TextView) layout_current_part.findViewById(R.id.registra_cb);
            LinearLayout layout_money = (LinearLayout) layout_current_item.findViewById(R.id.municipal_mymoney);
            LinearLayout layout_country_agency_bussiness_find = (LinearLayout) layout_current_item.findViewById(R.id.country_agency_bussiness_find);
            LinearLayout layout_order = (LinearLayout) layout_current_item.findViewById(R.id.municipal_order);
            LinearLayout layout_report_chart = (LinearLayout) layout_current_item.findViewById(R.id.municipal_report_chart);
            LinearLayout layout_collection = (LinearLayout) layout_current_item.findViewById(R.id.municipal_collection);
            LinearLayout layout_address = (LinearLayout) layout_current_item.findViewById(R.id.municipal_address);
            LinearLayout layout_password = (LinearLayout) layout_current_item.findViewById(R.id.municipal_password);
            LinearLayout layout_invited_code = (LinearLayout) layout_current_item.findViewById(R.id.invited_code);
            LinearLayout layout_relations_report = (LinearLayout) layout_current_item.findViewById(R.id.relations_report);
            layout_relations_report.setTag(RelationsReport);
            layout_relations_report.setOnClickListener(this);
            LinearLayout layout_reback_ranking = (LinearLayout) layout_current_item.findViewById(R.id.reback_ranking);
            layout_reback_ranking.setTag(RebackRanking);
            layout_reback_ranking.setOnClickListener(this);

            tv_gcb.setText(JsonClass.sj(sender, "integral"));
            tv_zcb.setText(JsonClass.sj(sender, "rank_points"));
            User.setCbi(JsonClass.sj(sender, "cbi"));
            if (sender.has("cbi")) {
                tv_cb.setText(User.getCbi());
            }
            layout_invited_code.setTag(INVITRF_CODE);
            layout_invited_code.setOnClickListener(this);
            layout_money.setTag(MYMONEY);
            layout_money.setOnClickListener(this);
            layout_country_agency_bussiness_find.setTag(FINDBUSSINESS);
            layout_country_agency_bussiness_find.setOnClickListener(this);
            layout_order.setTag(MYORDER);
            layout_order.setOnClickListener(this);
            layout_report_chart.setTag(MYREPORTCHART);
            layout_report_chart.setOnClickListener(this);
            layout_collection.setTag(MYCOLLECTION);
            layout_collection.setOnClickListener(this);
            layout_address.setTag(MYADDRESS);
            layout_address.setOnClickListener(this);
            layout_password.setTag(MYPASSWORD);
            layout_password.setOnClickListener(this);
            //endregion
        } else if ("个人代理".equals(User.getRankName())) {
            //region 个人代理
            layout_current_item = (LinearLayout) LayoutInflater.from(Default.getActivity()).inflate(R.layout.usercenter_item_personal_agent, null);
            layout_current_part = (LinearLayout) LayoutInflater.from(Default.getActivity()).inflate(R.layout.usercenter_parts_registra, null);
            TextView tv_gcb = (TextView) layout_current_part.findViewById(R.id.registra_gcb);
            TextView tv_zcb = (TextView) layout_current_part.findViewById(R.id.registra_zcb);
            TextView tv_cb = (TextView) layout_current_part.findViewById(R.id.registra_cb);
            TextView tv_ygfl = (TextView) layout_current_part.findViewById(R.id.registra_ygfl);
            LinearLayout layout_money = (LinearLayout) layout_current_item.findViewById(R.id.registra_my_money);
            LinearLayout layout_bussiness_promet = (LinearLayout) layout_current_item.findViewById(R.id.registra_bussiness_promet);
            LinearLayout layout_order = (LinearLayout) layout_current_item.findViewById(R.id.registra_order);
            LinearLayout layout_collection = (LinearLayout) layout_current_item.findViewById(R.id.registra_collection);
            LinearLayout layout_address = (LinearLayout) layout_current_item.findViewById(R.id.registra_address);
            LinearLayout layout_password = (LinearLayout) layout_current_item.findViewById(R.id.registra_password);
            LinearLayout layout_back_money = (LinearLayout) layout_current_item.findViewById(R.id.back_money);
            LinearLayout layout_invited_code = (LinearLayout) layout_current_item.findViewById(R.id.invited_code);
            LinearLayout layout_relations_report = (LinearLayout) layout_current_item.findViewById(R.id.relations_report);
            LinearLayout layout_cbi = (LinearLayout) layout_current_item.findViewById(R.id.cbi);
            layout_cbi.setTag(CBI);
            layout_cbi.setOnClickListener(this);
            layout_relations_report.setTag(RelationsReport);
            layout_relations_report.setOnClickListener(this);
            LinearLayout layout_reback_ranking = (LinearLayout) layout_current_item.findViewById(R.id.reback_ranking);
            layout_reback_ranking.setTag(RebackRanking);
            layout_reback_ranking.setOnClickListener(this);

            tv_gcb.setText(JsonClass.sj(sender, "integral"));
            tv_zcb.setText(JsonClass.sj(sender, "rank_points"));
            User.setCbi(JsonClass.sj(sender, "cbi"));
            if (sender.has("cbi")) {
                tv_cb.setText(User.getCbi());
            }
            tv_ygfl.setText(JsonClass.sj(sender, "ygfl"));
            layout_invited_code.setTag(INVITRF_CODE);
            layout_invited_code.setOnClickListener(this);
            layout_back_money.setTag(BACKMONEY);
            layout_back_money.setOnClickListener(this);
            layout_money.setTag(MYMONEY);
            layout_money.setOnClickListener(this);
            layout_bussiness_promet.setTag(PROMOTION);
            layout_bussiness_promet.setOnClickListener(this);
            layout_order.setTag(MYORDER);
            layout_order.setOnClickListener(this);
            layout_collection.setTag(MYCOLLECTION);
            layout_collection.setOnClickListener(this);
            layout_address.setTag(MYADDRESS);
            layout_address.setOnClickListener(this);
            layout_password.setTag(MYPASSWORD);
            layout_password.setOnClickListener(this);
            //endregion
        } else {
            Default.showToast(getContext().getString(R.string.notDevelop), Toast.LENGTH_SHORT);
            return;
        }

        // 判断淘金店主或者个人代理是否显示
        if(null != layout_current_item){
            ImageView goldOwnerIv = (ImageView) layout_current_item.findViewById(R.id.gold_onwer_iv);
            TextView goldOwnerTv = (TextView) layout_current_item.findViewById(R.id.gold_onwer_tv);
            ImageView personalAgencyIv = (ImageView) layout_current_item.findViewById(R.id.personal_agency_iv);
            TextView personalAgencyTv = (TextView) layout_current_item.findViewById(R.id.personal_agency_tv);

            if(User.else_menu()){
                // 设置监听
                LinearLayout goldLayout = (LinearLayout) layout_current_item.findViewById(R.id.gold_onwer);
                LinearLayout personalLayout = (LinearLayout) layout_current_item.findViewById(R.id.personal_agency);
                goldLayout.setOnClickListener(elseMenuListener);
                personalLayout.setOnClickListener(elseMenuListener);

                // 显示
                String elseMenuIdStr = User.else_menu_id();
                String[] elseMenuIdList = elseMenuIdStr.split("\\|");
                if(1 == elseMenuIdList.length){
                    if("个人代理".equals(User.getRankName())||"市级代理".equals(User.getRankName())){
                        LinearLayout bottomLayout = (LinearLayout) layout_current_item.findViewById(R.id.bottom_layout);
                        bottomLayout.setVisibility(View.GONE);
                    }
                    personalAgencyIv.setVisibility(View.GONE);
                    personalAgencyTv.setVisibility(View.GONE);
                    if("5".equals(elseMenuIdList[0])){
                        goldOwnerIv.setVisibility(View.VISIBLE);
                        goldOwnerTv.setVisibility(View.VISIBLE);
                        goldOwnerIv.setBackgroundResource(R.drawable.usercenter_gold_rush_onwer);
                        goldOwnerTv.setText("淘金店主");
                        goldLayout.setTag("SuperDiscount_list_5");
                    }else if("6".equals(elseMenuIdList[0])){
                        goldOwnerIv.setVisibility(View.VISIBLE);
                        goldOwnerTv.setVisibility(View.VISIBLE);
                        goldOwnerIv.setBackgroundResource(R.drawable.usercenter_personal_agency);
                        goldOwnerTv.setText("个人代理");
                        goldLayout.setTag("SuperDiscount_list_6");
                    }else{
                        goldOwnerIv.setVisibility(View.GONE);
                        goldOwnerTv.setVisibility(View.GONE);
                    }
                }else if(2 == elseMenuIdList.length) {
                    goldLayout.setTag("SuperDiscount_list_5");
                    personalLayout.setTag("SuperDiscount_list_6");
                    goldOwnerIv.setVisibility(View.VISIBLE);
                    goldOwnerTv.setVisibility(View.VISIBLE);
                    personalAgencyIv.setVisibility(View.VISIBLE);
                    personalAgencyTv.setVisibility(View.VISIBLE);
                }
            }else{
                // 隐藏显示
                goldOwnerIv.setVisibility(View.GONE);
                goldOwnerTv.setVisibility(View.GONE);
                personalAgencyIv.setVisibility(View.GONE);
                personalAgencyTv.setVisibility(View.GONE);
                if("个人代理".equals(User.getRankName())||"市级代理".equals(User.getRankName())){
                    LinearLayout bottomLayout = (LinearLayout) layout_current_item.findViewById(R.id.bottom_layout);
                    bottomLayout.setVisibility(View.GONE);
                }
            }
        }

        User.saveUser(getContext());
        if (User.isLogin()) {
            setUser();
        }
    }

    public void setPhoto(Bitmap bitmap) throws Exception {
        if (bitmap == null) {
            img_touxiang.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.headimg));
            return;
        }

        float length = bitmap.getWidth() > bitmap.getHeight() ? bitmap.getHeight() / 2 : bitmap.getWidth() / 2;
        Bitmap bmp = null;
        try {
            bmp = ImageHelper.tailorBitmapToCircle(bitmap, new PointF(bitmap.getWidth() / 2, bitmap.getHeight() / 2), length);
            ;
        } catch (OutOfMemoryError outOfMemoryError) {
            outOfMemoryError.printStackTrace();
        }
        if (bmp == null) {
            img_touxiang.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.headimg));
        } else {
            img_touxiang.setImageBitmap(bmp);
        }

    }

    private void noLogin() {
        User.noLogin();
        layout_current_item = null;
        layout_current_part = null;
        setUser();
        tv_upGrate.setVisibility(View.GONE);
        shares_rate_tv.setVisibility(View.GONE);
    }

    private void setUser() {
        if (User.isLogin()) {
            tv_submit.setText("注销登陆");
        } else {
            shares_rate_tv.setVisibility(View.GONE);
            tv_submit.setText("立即登陆");
        }
        ImageHelper.handlerImage(getContext(), User.getHeadUrl(), img_touxiang.getMeasuredWidth(), img_touxiang.getMeasuredHeight(), new ImageHelper.HandlerImage() {
            @Override
            protected void imageBack(Bitmap bitmap) {
                try {
                    setPhoto(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        user_names.setText(User.getUserName());
        tv_tag.setText(User.getRankNameNew());
        if (User.getRankNameNew().length() > 0) {
            tv_tag.setVisibility(VISIBLE);
        } else {
            tv_tag.setVisibility(GONE);
        }
        if (User.isShowMoney()) {
            ib_look.setBackgroundResource(R.drawable.usercenter_2);
            user_money.setText(User.getSurplus());
        } else {
            ib_look.setBackgroundResource(R.drawable.usercenter_1);
            user_money.setText("*******");
        }
        if (layout_current_item != null) {
            layout_current_item.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layout_usercenter_item.removeAllViews();
            if (User.isLogin()) {
                layout_usercenter_item.addView(layout_current_item);
            }
        } else {
            layout_usercenter_item.removeAllViews();
        }

        if (layout_current_part != null && User.isLogin()) {
            layout_current_part.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layout_usercenter_part.removeAllViews();
            if (User.isLogin()) {
                layout_usercenter_part.addView(layout_current_part);
            } else {
                layout_usercenter_part.removeAllViews();
            }
        }

        if(null != User.shares_rate() && 0 != User.shares_rate().length()){
            shares_rate_tv.setVisibility(View.VISIBLE);
            shares_rate_tv.setText(User.shares_rate());
        }else{
            shares_rate_tv.setVisibility(View.GONE);
        }
    }

    //region Action
    private void login() {
        if (loginDelegate != null) {
            Intent intent = new Intent();
            intent.putExtra(LoginActivity.LoginDelegate, loginDelegate);
            GetBaseActivity().AddActivity(LoginActivity.class, 0, intent);
        } else {
            GetBaseActivity().AddActivity(LoginActivity.class);
        }
    }

    @Override
    public void onClick(View view) {
        int tag = Integer.parseInt(String.valueOf(view.getTag() == null ? "-1" : view.getTag()));
        if (tag == 0) {
            Log.d("asd", "username");
        } else if (tag == 1) {
            //region 设置
            if (User.isLogin()) {
                GetBaseActivity().AddActivity(SettingActivity.class);
            } else login();
            //endregion
        } else if (tag == 6) {
            //region 充值
            Intent intent = new Intent();
            intent.putExtra(MoneyActivity.SHOWTYPE, MoneyActivity.RECHARGE);
            if (User.isLogin()) {
                this.GetBaseActivity().AddActivity(MoneyActivity.class, 0, intent);
            } else login();
            //endregion
        } else if (tag == 7) {
            //region 账户余额提现
            Intent intent = new Intent();
            intent.putExtra(MoneyActivity.SHOWTYPE, MoneyActivity.WITHDRAWALS);
            if (User.isLogin()) {
                this.GetBaseActivity().AddActivity(MoneyActivity.class, 0, intent);
            } else login();
            //endregion
        } else if (tag == 9) {
            //region 待付款订单
            if (User.isLogin()) {
//                MessageBox.Show(getContext(), Default.AppName, "前往待付款订单", new String[]{"淘宝订单", "子成订单"}, new MessageBox.MessBtnBack() {
//                    @Override
//                    public void Back(int index) {
//                        switch (index){
//                            case 0:{
//                                Intent intent = new Intent();
//                                intent.putExtra(OrderActivity.Status,1);
//                                GetBaseActivity().AddActivity(OrderActivity.class,0,intent);
//                                break;}
//                            case 1:{
//                                GetBaseActivity().AddActivity(PostOrderActivity.class);
//                                break;}
//                            default:
//                        }
//                    }
//                });
                GetBaseActivity().AddActivity(PostOrderActivity.class);
            } else login();
            //endregion
        } else if (tag == 10) {
            //region 待发货订单
            if (User.isLogin()) {
//                MessageBox.Show(getContext(), Default.AppName, "前往待发货订单", new String[]{"淘宝订单", "子成订单"}, new MessageBox.MessBtnBack() {
//                    @Override
//                    public void Back(int index) {
//                        switch (index){
//                            case 0:{
//                                Intent intent = new Intent();
//                                intent.putExtra(OrderActivity.Status,2);
//                                GetBaseActivity().AddActivity(OrderActivity.class,0,intent);
//                                break;}
//                            case 1:{
//                                GetBaseActivity().AddActivity(BackOrdersActivity.class);
//                                break;}
//                            default:
//                        }
//                    }
//                });
                GetBaseActivity().AddActivity(BackOrdersActivity.class);
            } else login();
            //endregion
        } else if (tag == 11) {
            //region 待收货订单
            if (User.isLogin()) {
//                MessageBox.Show(getContext(), Default.AppName, "前往待收货订单", new String[]{"淘宝订单", "子成订单"}, new MessageBox.MessBtnBack() {
//                    @Override
//                    public void Back(int index) {
//                        switch (index){
//                            case 0:{
//                                Intent intent = new Intent();
//                                intent.putExtra(OrderActivity.Status,3);
//                                GetBaseActivity().AddActivity(OrderActivity.class,0,intent);
//                                break;}
//                            case 1:{
//                                GetBaseActivity().AddActivity(CollectThemActivity.class);
//                                break;}
//                            default:
//                        }
//                    }
//                });
                GetBaseActivity().AddActivity(CollectThemActivity.class);
            } else login();
            //endregion
        } else if (tag == 12) {
            //region 已完成订单
            if (User.isLogin()) {
//                MessageBox.Show(getContext(), Default.AppName, "前往已完成订单\n(淘宝订单为待评价)", new String[]{"淘宝订单", "子成订单"}, new MessageBox.MessBtnBack() {
//                    @Override
//                    public void Back(int index) {
//                        switch (index){
//                            case 0:{
//                                Intent intent = new Intent();
//                                intent.putExtra(OrderActivity.Status,4);
//                                GetBaseActivity().AddActivity(OrderActivity.class,0,intent);
//                                break;}
//                            case 1:{
//                                GetBaseActivity().AddActivity(CompletedActivity.class);
//                                break;}
//                            default:
//                        }
//                    }
//                });
                GetBaseActivity().AddActivity(CompletedActivity.class);
            } else login();
            //endregion
        } else if (tag == 20) {
            //region 注销登陆
            if (User.isLogin()) {
                AsynServer.BackObject(GetBaseActivity(), "user/signout", new JSONObject(), new AsynServer.BackObject() {
                    @Override
                    public void Back(JSONObject sender) {
                        if (sender != null) {
                            try {
                                if (sender.has("status")) {
                                    JSONObject jsons = sender.getJSONObject("status");
                                    if (jsons.has("succeed")) {
                                        if (jsons.getInt("succeed") == 1) {
                                            //注销成功
                                            noLogin();
                                            User.signOut(GetBaseActivity());
                                            login();
                                            return;
                                        } else {
                                            MessageBox.Show(JsonClass.sj(jsons, "error_desc"));
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Default.showToast("注销失败，请重试。", Toast.LENGTH_LONG);
                    }
                });
            } else {
                login();
            }

            //endregion
        } else if (tag == 21) {
            //region 更新
            final CustomProgressDialog customProgressDialog =CustomProgressDialog.createDialog(getContext(),R.style.CustomProgressDialog);
            customProgressDialog.changeMessage("检查更新");
            if (nextDelegate != null) {
                customProgressDialog.show();
                nextDelegate.checkVersion(new NextDelegate.CheckVersionEnd() {
                    @Override
                    public void end(final String loadUrl, String current_versionNum, String new_versionNum, boolean needPublish, String updateInfo) {
                        customProgressDialog.dismiss();
                        if (needPublish) {
                            IndexDialog.showUpdateNotificationDialog(getContext(), "请升级APP至版本" + new_versionNum, updateInfo,
                                    new String[]{"取消", "确定"}, new MessageBox.MessBtnBack() {
                                        @Override
                                        public void Back(int index) {
                                            switch (index) {
                                                case 0:
                                                    MessageBox.dismiss();
                                                    break;
                                                case 1:
                                                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                                            //请求权限
                                                            ActivityCompat.requestPermissions(ImageHelper.getActivity(getContext()), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                                                            //判断是否需要 向用户解释，为什么要申请该权限
                                                            ActivityCompat.shouldShowRequestPermissionRationale(ImageHelper.getActivity(getContext()), Manifest.permission.READ_CONTACTS);
                                                            break;
                                                        } else {
                                                            nextDelegate.downFile(loadUrl);
                                                        }
                                                    } else {
                                                        Default.showToast("请提供写入权限更新", Toast.LENGTH_LONG);
                                                    }
                                                    break;
                                                default:
                                            }
                                        }
                                    });
                        } else {
                            MessageBox.createNewDialog();
                            MessageBox.Show("版本更新","当前为最新版：" + new_versionNum);
                        }
                    }

                    @Override
                    public void error(String error_message) {
                        customProgressDialog.dismiss();
                        Default.showToast(error_message);
                    }
                });
            }
            //endregion
        } else if (tag == 28) {
            //region touxiang
            if (photoDelegate != null) {
                photoDelegate.changePhoto();
            }
        } else if (tag == 29) {
            // region LookMoney
            String password = "*******";
            JSONObject para = new JSONObject();
            if (user_money.getText().equals(password)) {
                user_money.setText(User.getSurplus());
                ib_look.setBackgroundResource(R.drawable.usercenter_2);
                try {
                    para.put("sw", 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    para.put("sw", 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                user_money.setText(password);
                ib_look.setBackgroundResource(R.drawable.usercenter_1);
            }
            AsynServer.BackObject(getContext(), "user/user_money_sw", false, para, null);
            //endregion
        } else if (tag == 15) {
            //region 赠送子成币
            this.GetBaseActivity().AddActivity(ReplenishmentActivity.class);
            //endregion
        } else if (tag == 23) {
            //region 商铺数据报表
            this.GetBaseActivity().AddActivity(MerchantDataActivity.class);
            //endregion
        } else if (tag == 24) {
            //region 商家佣金统计
            this.GetBaseActivity().AddActivity(CommissionActivity.class);
            //endregion
        } else if (tag == MYMONEY) {
            //region 我的资产
            Intent intent = new Intent();
            intent.putExtra(MoneyActivity.SHOWTYPE, MoneyActivity.CAPITAL);
            this.GetBaseActivity().AddActivity(MoneyActivity.class, 0, intent);
            //endregion
        } else if (tag == MYREDPACKAGE) {
            //region 我的红包
//            GetBaseActivity().AddActivity(UseRedActivity.class);
            //endregion
        } else if (tag == MYORDER) {
            //region 我的订单
            Intent intent = new Intent();
            intent.putExtra("order_type", 0);
            this.GetBaseActivity().AddActivity(OrderListActivity.class, 0, intent);
            //endregion
        } else if (tag == MYREPORTCHART) {
            //region 商铺数据报表
            this.GetBaseActivity().AddActivity(MerchantDataActivity.class);
            //endregion
        } else if (tag == MYCOLLECTION) {
            //region 我的收藏
            this.GetBaseActivity().AddActivity(CollectionActivity.class);
            //endregion
        } else if (tag == MYADDRESS) {
            //region 收货地址
            this.GetBaseActivity().AddActivity(AddressActivity.class);

            //endregion
        } else if (tag == MYPASSWORD) {
            //region 修改密码
            this.GetBaseActivity().AddActivity(PasswordEditActivity.class);
            //endregion
        } else if (tag == UPLOAD) {
            //region 我的合同
            this.GetBaseActivity().AddActivity(ContractActivity.class);
            //endregion
        } else if (tag == PROMOTION) {
            //region 推荐店铺
            this.GetBaseActivity().AddActivity(RecommendBusinessShopsActivity.class);
            //endregion
        } else if (tag == ADDMORE) {
            //region 分配店铺补货额
            this.GetBaseActivity().AddActivity(ReplenishmentActivity.class);
            //endregion
        } else if (tag == FREEPRODUCTION) {
            //region 赠送产品
            this.GetBaseActivity().AddActivity(GiveProductsActivity.class);
            //endregion
        } else if (tag == COMMISSIONDETAIL) {
            //region 商家佣金明细
            this.GetBaseActivity().AddActivity(ShopDetailedActivity.class);
            //endregion
        } else if (tag == COMMISSIONGET) {
            //region 商家佣金提现
            this.GetBaseActivity().AddActivity(ShopWithdrawalsActivity.class);
            //endregion
        } else if (tag == BUSSINESSORDERMENNAGE) {
            //region 商家订单列表
            Intent intent = new Intent();
            intent.putExtra("order_type", 1);
            this.GetBaseActivity().AddActivity(OrderListActivity.class, 0, intent);
            //endregion
        } else if (tag == DELIVERY) {
            //region 发货单列表
            this.GetBaseActivity().AddActivity(InvoiceActivity.class);
            //endregion
        } else if (tag == FINDBUSSINESS) {
            //region 代理商查询
            this.GetBaseActivity().AddActivity(AgentActivity.class);
            //endregion
        } else if (tag == BACKMONEY) {
            //region 返利订单
            this.GetBaseActivity().AddActivity(BackMoneyActivity.class);
            //endregion
        } else if (tag == INVITRF_CODE) {
            // TODO: 2017/5/27 邀请码
            this.GetBaseActivity().AddActivity(InvitedCodeActivity.class);
        } else if (tag == CBI) {
            // TODO: 2017/6/1 C米
            this.GetBaseActivity().AddActivity(CBIActivity.class);
        } else if (tag == RelationsReport) {
            // TODO: 2017/6/10 推荐关系
//            this.GetBaseActivity().AddActivity(RelationsActivity.class);
            Intent intent = new Intent();
            ReferralPerson referralPerson = new ReferralPerson();
            referralPerson.setUserID(userID);
            intent.putExtra(NextRelations.REFPerson, referralPerson);
            this.GetBaseActivity().AddActivity(NextRelations.class, 0, intent);
        } else if (tag == RebackRanking) {
            // TODO: 2017/6/10 返利排行
            this.GetBaseActivity().AddActivity(RebateRankingActivity.class);
        }
    }

    protected OnClickListener elseMenuListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.gold_onwer:
                    String tagType = String.valueOf(v.getTag() == null ? "-1" : v.getTag());
                    int data_type = -1;
                    if("SuperDiscount_list_5".equals(tagType)){
                        data_type = 5;
                    }else if("SuperDiscount_list_6".equals(tagType)){
                        data_type = 6;
                    }
                    Intent intent = new Intent();
//                    com.wzzc.NextSuperDeliver.list.Category category = new Category();
//                    category.setpType(data_type);
                    intent.putExtra(SignupAgentPreActivity.CATEGORY,data_type);
                    GetBaseActivity().AddActivity(SignupAgentPreActivity.class,0,intent);
                    break;
                case R.id.personal_agency:
                    String tagType1 = String.valueOf(v.getTag() == null ? "-1" : v.getTag());
                    int data_type1 = -1;
                    if("SuperDiscount_list_5".equals(tagType1)){
                        data_type1 = 5;
                    }else if("SuperDiscount_list_6".equals(tagType1)){
                        data_type1 = 6;
                    }
                    Intent intent1 = new Intent();
//                    com.wzzc.NextSuperDeliver.list.Category category1 = new Category();
//                    category1.setpType(data_type1);
                    intent1.putExtra(SignupAgentPreActivity.CATEGORY,data_type1);
                    GetBaseActivity().AddActivity(SignupAgentPreActivity.class,0,intent1);
                    break;
            }
        }
    };

    protected OnClickListener personAgentListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (User.isLogin()) {
                    AsynServer.BackObject(getContext(), "user/info", new JSONObject(), new AsynServer.BackObject() {
                        @Override
                        public void Back(JSONObject s) {
                            JSONObject json_status = JsonClass.jj(s, "status");
                            if (JsonClass.ij(json_status, "succeed") == 0) {
                                MessageBox.Show(JsonClass.sj(json_status, "error_desc"));
                            } else {
                                JSONObject sender = JsonClass.jj(s, "data");
                                String agent_address = JsonClass.sj(sender, "agent_address");
                                String agent_fee = JsonClass.sj(sender, "agent_fee");
                                if (agent_address.equals("")) {
                                    //地址不存在，需要重新填写表单
                                    Intent intent = new Intent();
                                    intent.putExtra(RegisterPersonalAgentActivity.NONE, true);
                                    GetBaseActivity().AddActivity(RegisterPersonalAgentActivity.class, 0, intent);
                                } else {
                                    //地址存在，不需要填写表单
                                    final PayView payView = new PayView(getContext());
                                    if (sender.has("agent_order")) {
                                        //有订单，直接进入支付环节
                                        JSONObject json_agent_order = JsonClass.jj(sender, "agent_order");
                                        payView.setInfo(true, Integer.valueOf(JsonClass.sj(json_agent_order, "payment_id")));
                                        payView.removeAllViews();
                                        TextView tv = new TextView(getContext());
                                        tv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                        tv.setGravity(CENTER_IN_PARENT);
                                        tv.setText(JsonClass.sj(json_agent_order, "payment") + ":" + agent_fee + "元");
                                        tv.setTextSize(16);
                                        tv.setTextColor(ContextCompat.getColor(getContext(), R.color.tv_Black));
                                        payView.addView(tv);
                                    } else {
                                        //没有订单，进入选择支付方式界面
                                        payView.setInfo(false, null);
                                    }

                                    MessageBox.Show("个人代理激活", payView, new String[]{"取消", "确定"}, new MessageBox.MessBtnBack() {
                                        @Override
                                        public void Back(int index) {
                                            switch (index) {
                                                case 0:
                                                    break;
                                                case 1:
                                                    JSONObject para = new JSONObject();
                                                    try {
                                                        para.put("session", Default.GetSession());
                                                        para.put("payment_id", payView.getPayment_id());
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    switch (payView.getPayment_id()) {
                                                        case 0:
                                                            Default.showToast("请选择支付方式", Toast.LENGTH_LONG);
                                                            break;
                                                        case 1:
                                                            PayFromZFB.pay(getContext(), "user/signupAgentPayment", para, new MessageBox.MessBtnBack() {
                                                                @Override
                                                                public void Back(int index) {
                                                                    login();
                                                                }
                                                            }, new MessageBox.MessBtnBack() {
                                                                @Override
                                                                public void Back(int index) {
                                                                    //失败
                                                                }
                                                            });
                                                            break;
                                                        case 3:
                                                            PayFromWX.pay(getContext(), "user/signupAgentPayment", para, new MessageBox.MessBtnBack() {
                                                                @Override
                                                                public void Back(int index) {
                                                                    login();
                                                                }
                                                            }, new MessageBox.MessBtnBack() {
                                                                @Override
                                                                public void Back(int index) {
                                                                    //失败
                                                                }
                                                            });
                                                            break;
                                                    }
                                                    break;
                                                default:
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                } else {
                    login();
                }
            }
        };
    }

    //endregion
}
