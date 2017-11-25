package com.wzzc.NextTBSearch.main_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.NextIndex.views.e.User;
import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.other_function.action.ClickDelegate;
import com.wzzc.other_function.action.ItemClick;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/7/4.
 */
@ContentView(R.layout.view_tbrebacklistitem)
public class TBrebackListItemView extends BaseView {
    //region 组件
    @ViewInject(R.id.pro_layout)
    private ExtendImageView pro_layout;
    @ViewInject(R.id.progressBar)
    private ProgressBar progressBar;
    @ViewInject(R.id.tv_name)
    private TextView tv_name;
    @ViewInject(R.id.tv_money)
    private TextView tv_money;
    @ViewInject(R.id.tv_hasGet)
    private TextView tv_hasGet;
    @ViewInject(R.id.tv_category)
    private TextView tv_info;
    @ViewInject(R.id.tv_earn)
    private TextView tv_earn;
    @ViewInject(R.id.tv_shop)
    private TextView tv_shop;
    @ViewInject(R.id.layout_coupon)
    RelativeLayout layout_coupon;
    @ViewInject(R.id.tv_coupon_info)
    TextView tv_coupon_info;
    //endregion
    TBrebackItemDelegate tBrebackItemDelegate;
    ClickDelegate clickDelegate;

    protected String coupon_click_url;
    protected String goods_name;

    // 是否有券
    public boolean isHaveQuan = false;

    public TBrebackListItemView(Context context) {
        super(context);
        init();
    }

    public TBrebackListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOnClickListener(buyClick);
        tv_shop.setOnClickListener(buyClick);
    }

    private OnClickListener buyClick = new OnClickListener() {
        @Override
        public void onClick(final View v) {
            if (!User.isLogin() && tBrebackItemDelegate != null) {
                MessageBox.Show(getContext(), Default.AppName, "只有登陆后才可购物买返利产品哟！", new String[]{"先逛逛", "前往登陆"}, new MessageBox.MessBtnBack() {
                    @Override
                    public void Back(int index) {
                        switch (index) {
                            case 0:
                                break;
                            case 1:
                                tBrebackItemDelegate.showLogin(v);
                                break;
                            default:
                        }
                    }
                });
            } else {
                showTaoBaoSearchActivityDialog(clickDelegate,v,v.getTag().toString());
            }
        }
    };

    protected void showTaoBaoSearchActivityDialog (final ClickDelegate clickDelegate, final View currentView, final String num_iid) {
        if (!User.is_activate() && User.isShowActivity(Default.getActivity()) && User.getActivity_msg().length() > 0) {
            MessageBox.Show(Default.getActivity(),Default.AppName, User.getActivity_msg(), new String[]{User.getActivity_btn_goActivity(), User.getActivity_check_text(),
                    User.getActivity_btn_justBuy()}, new MessageBox.MessBtnBack() {
                @Override
                public void Back(int index) {
                    switch (index){
                        case 0:
                            ItemClick.switchNormalListener(ItemClick.SuperDiscount_list_3,null);
                            break;
                        case 1:
                            User.setShowActivity(Default.getActivity(),false);
                            break;
                        case 2:
                            if(!isHaveQuan){
                                // 无券
                                ItemClick.addTaoBaoSearchOrder(clickDelegate,currentView,num_iid);
                            }else{
                                // 有券
                                tBrebackItemDelegate.showQuan(goods_name, coupon_click_url);
                            }
                            break;
                        default:
                    }
                }
            });
        } else {
            if(!isHaveQuan){
                // 无券
                ItemClick.addTaoBaoSearchOrder(clickDelegate,currentView,num_iid);
            }else{
                // 有券
                tBrebackItemDelegate.showQuan(goods_name, coupon_click_url);
            }
        }
    }

    public void setInfo(ClickDelegate clickDelegate , TBrebackItemDelegate tBrebackItemDelegate, JSONObject jsonPro, boolean isQuan) {
        this.clickDelegate = clickDelegate;
        this.tBrebackItemDelegate = tBrebackItemDelegate;
        pro_layout.radio = GetBaseActivity().getResources().getDimension(R.dimen.RoundRadio);
        pro_layout.setPath(JsonClass.sj(jsonPro, "pict_url"));
        tv_name.setText(JsonClass.sj(jsonPro, "title"));
        tv_money.setText("￥" + JsonClass.sj(jsonPro, "zk_final_price"));
        int volume = JsonClass.ij(jsonPro,"volume");
        tv_coupon_info.setText(String.valueOf(volume));
        tv_shop.setTag(JsonClass.sj(jsonPro,"num_iid"));
        setTag(JsonClass.sj(jsonPro,"num_iid"));
        isHaveQuan = isQuan;
        coupon_click_url = jsonPro.optString("coupon_click_url", "");
        goods_name = jsonPro.optString("title", "");
    }
}
