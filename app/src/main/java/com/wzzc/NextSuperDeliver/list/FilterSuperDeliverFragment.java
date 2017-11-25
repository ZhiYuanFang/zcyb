package com.wzzc.NextSuperDeliver.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.Default;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_view.production.detail.main_view.FlowLayout;
import com.wzzc.other_view.progressDialog.CustomProgressDialogView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/8/8.
 *
 */

public class FilterSuperDeliverFragment extends Fragment implements View.OnClickListener {
    public static final String FilterSuperDeliverDelegate = "delegate";
    public static final String CateGory = "category";//分类

    //region 返利区间
    EditText et_back_one, et_back_tow;
    TextView tv_reback_1, tv_reback_2, tv_reback_3, tv_reback_4, tv_reback_5/*, tv_reback_6*/;
    ArrayList<TextView> reBackArrayList;
    //endregion
    //region 价格区间
    EditText et_money_one, et_money_tow;
    TextView tv_money_1, tv_money_2, tv_money_3, tv_money_4, tv_money_5, tv_money_6;
    ArrayList<TextView> moneyArrayList;
    //endregion
    //region 销量
    TextView tv_number_up_low, tv_number_low_up;
    ArrayList<TextView> numberArrayList;
    //endregion
    //region 分类
    TextView tv_category;
    FlowLayout flowLayout;
    FlowLayout flowLayoutHasSelect;
    ArrayList<Category> hasSelectCategoryArrayList;
    //endregion
    RelativeLayout layout_out;//外部推出屏
    //region 底部按钮
    Button bt_reset, bt_finish;
    ArrayList<Button> bottomButtonArrayList;
    //endregion
    LayoutInflater inflater;
    FilterSuperDeliverDelegate filterSuperDeliverDelegate;
    Category category;
    String current_method;
    String current_order_type;
    CustomProgressDialogView customProgressDialogView;//分类对话框
    //endregion
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_filter_super_deliver, null);
        layout_out = (RelativeLayout) v.findViewById(R.id.layout_out);
        //region 返利区间
        et_back_one = (EditText) v.findViewById(R.id.et_back_one);
        et_back_tow = (EditText) v.findViewById(R.id.et_back_tow);
        tv_reback_1 = (TextView) v.findViewById(R.id.tv_reback_1);
        tv_reback_2 = (TextView) v.findViewById(R.id.tv_reback_2);
        tv_reback_3 = (TextView) v.findViewById(R.id.tv_reback_3);
        tv_reback_4 = (TextView) v.findViewById(R.id.tv_reback_4);
        tv_reback_5 = (TextView) v.findViewById(R.id.tv_reback_5);
//        tv_reback_6 = (TextView) v.findViewById(R.id.tv_reback_6);
        reBackArrayList = new ArrayList<TextView>() {{
            add(tv_reback_1);
            add(tv_reback_2);
            add(tv_reback_3);
            add(tv_reback_4);
            add(tv_reback_5);
//            add(tv_reback_6);
        }};
        for (TextView tv : reBackArrayList) {
            tv.setOnClickListener(reBackOnClick);
        }
        //endregion

        //region 价格区间
        et_money_one = (EditText) v.findViewById(R.id.et_money_one);
        et_money_tow = (EditText) v.findViewById(R.id.et_money_tow);
        tv_money_1 = (TextView) v.findViewById(R.id.tv_money_1);
        tv_money_2 = (TextView) v.findViewById(R.id.tv_money_2);
        tv_money_3 = (TextView) v.findViewById(R.id.tv_money_3);
        tv_money_4 = (TextView) v.findViewById(R.id.tv_money_4);
        tv_money_5 = (TextView) v.findViewById(R.id.tv_money_5);
        tv_money_6 = (TextView) v.findViewById(R.id.tv_money_6);
        moneyArrayList = new ArrayList<TextView>() {{
            add(tv_money_1);
            add(tv_money_2);
            add(tv_money_3);
            add(tv_money_4);
            add(tv_money_5);
            add(tv_money_6);

        }};
        for (TextView tv : moneyArrayList) {
            tv.setOnClickListener(moneyOnClick);
        }
        //endregion

        //region 销量选择
        tv_number_low_up = (TextView) v.findViewById(R.id.tv_number_low_up);
        tv_number_up_low = (TextView) v.findViewById(R.id.tv_number_up_low);
        numberArrayList = new ArrayList<TextView>() {{
            add(tv_number_low_up);
            add(tv_number_up_low);
        }};
        for (TextView tv : numberArrayList) {
            tv.setOnClickListener(numberOnClick);
        }
        //endregion

        //region 底部按钮
        bt_reset = (Button) v.findViewById(R.id.bt_reset);
        bt_finish = (Button) v.findViewById(R.id.bt_finish);
        bottomButtonArrayList = new ArrayList<Button>() {{
            add(bt_reset);
            add(bt_finish);
        }};
        for (Button bt : bottomButtonArrayList) {
            bt.setOnClickListener(this);
        }
        //endregion

        //region 分类选择
        tv_category = (TextView) v.findViewById(R.id.tv_category);
        flowLayout = (FlowLayout) v.findViewById(R.id.flowLayout);
        flowLayoutHasSelect = (FlowLayout) v.findViewById(R.id.flowLayoutHasSelect);
        tv_category.setOnClickListener(this);
        //endregion
        layout_out.setOnClickListener(this);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inflater = LayoutInflater.from(getContext());
        filterSuperDeliverDelegate = getArguments().getParcelable(FilterSuperDeliverDelegate);
        category = getArguments().getParcelable(CateGory);
        System.out.println("category : " + category.getCategoryID() + category.getCategoryName());
        init();
    }

    protected void outFragment() {
        Default.getActivity().onBackPressed();
    }

    protected void reset() {
        category = getArguments().getParcelable(CateGory);
        init();
    }

    public void init () {
        hasSelectCategoryArrayList = new ArrayList<>();
        if (!(Integer.parseInt(category.getMinRebate()) == 0)) {
            et_back_one.setText(category.getMinRebate());
        }
        if (!(Integer.parseInt(category.getMaxRebate()) == 0)) {
            et_back_tow.setText(category.getMaxRebate());
        }
        if (!(Integer.parseInt(category.getMinPrice()) == 0)) {
            et_money_one.setText(category.getMinPrice());
        }
        if (!(Integer.parseInt(category.getMaxPrice()) == 0)) {
            et_money_tow.setText(category.getMaxPrice());
        }
        initReBack(Integer.parseInt(category.getMinRebate()), Integer.parseInt(category.getMaxRebate()));
        initMoney(Integer.parseInt(category.getMinPrice()) , Integer.parseInt(category.getMaxPrice()) );
        initNumber(category.getSort_method(), category.getSort_order());
        tv_category.setTag(Category.AllCategoryID);
        tv_category.setText(Category.AllCategoryName);
        arrangeCatListSub(category.getCategoryID());
    }

    protected void submit() {
        useCategory();
        filterSuperDeliverDelegate.filter(category);
    }

    /** 筛选之前需要使用*/
    public void useCategory () {
        if (current_method != null && current_order_type != null) {
            category.setSort_method(current_method);
            category.setSort_order(current_order_type);
            current_method = null;
            current_order_type = null;
        }
        if (hasSelectCategoryArrayList != null && hasSelectCategoryArrayList.size() > 0) {
            category = hasSelectCategoryArrayList.get(hasSelectCategoryArrayList.size() - 1);
        } else {
            category.setParentCategoryID(Category.AllCategoryID);
            category.setCategoryID(Category.AllCategoryID);
            category.setCategoryName(Category.AllCategoryName);
        }
        category.setMinPrice(et_money_one.getText().toString());
        category.setMaxPrice(et_money_tow.getText().toString());
        category.setMinRebate(et_back_one.getText().toString());
        category.setMaxRebate( et_back_tow.getText().toString());
    }

    //region Method
    protected void initNumber(String sold_method, String sold_order_type) {
        if (sold_method.equals(Filter.FilterNumber)) {
            if (sold_order_type.equals(Filter.SortOrderDesc)) {
                focusNumberItem(tv_number_up_low);
            }
            if (sold_order_type.equals(Filter.SortOrderAsc)) {
                focusNumberItem(tv_number_low_up);
            }
        }
    }

    protected void initReBack(Integer minReBack, Integer maxReBack) {
        if (minReBack == null) {
            minReBack = 0;
        }
        if (maxReBack == null) {
            maxReBack = 0;
        }
        if (minReBack == 0 && maxReBack == 5) {
            focusReBackItem(tv_reback_1);
        }
        if (minReBack == 5 && maxReBack == 10) {
            focusReBackItem(tv_reback_2);
        }
        if (minReBack == 10 && maxReBack == 15) {
            focusReBackItem(tv_reback_3);
        }
        if (minReBack == 15 && maxReBack == 20) {
            focusReBackItem(tv_reback_4);
        }
        if (minReBack == 20 && maxReBack == 0) {
            focusReBackItem(tv_reback_5);
        }
       /* if (minReBack == 25 && maxReBack == 0) {
            focusReBackItem(tv_reback_6);
        }*/
    }

    protected void initMoney(Integer minMoney, Integer maxMoney) {
        if (minMoney == null) {
            minMoney = 0;
        }
        if (maxMoney == null) {
            maxMoney = 0;
        }
        if (minMoney == 0 && maxMoney == 100) {
            focusMoneyItem(tv_money_1);
        }
        if (minMoney == 100 && maxMoney == 200) {
            focusMoneyItem(tv_money_2);
        }
        if (minMoney == 200 && maxMoney == 300) {
            focusMoneyItem(tv_money_3);
        }
        if (minMoney == 300 && maxMoney == 500) {
            focusMoneyItem(tv_money_4);
        }
        if (minMoney == 500 && maxMoney == 700) {
            focusMoneyItem(tv_money_5);
        }
        if (minMoney == 700 && maxMoney == 0) {
            focusMoneyItem(tv_money_6);
        }
    }

    protected void judgeReBackItemAndCorporate(TextView tv_reBack, Integer minReBack, Integer maxReBack) {
        switch (Integer.valueOf(tv_reBack.getTag().toString())) {
            case 0://未选择,则选择
                clearSelectReBack();
                focusReBackItem(tv_reBack);
                et_back_one.setText(String.valueOf(minReBack == null ? "" : minReBack));
                et_back_tow.setText(String.valueOf(maxReBack == null ? "" : maxReBack));
                break;
            case 1://已选择，则取消选择
                cancelSelectedReBackItem(tv_reBack);
                et_back_one.setText("");
                et_back_tow.setText("");
                break;
            default:
        }
    }

    protected void judgeMoneyItemAndCorporate(TextView tv_money, Integer minMoney, Integer maxMoney) {
        switch (Integer.valueOf(tv_money.getTag().toString())) {
            case 0://未选择,则选择
                clearSelectMoney();
                focusMoneyItem(tv_money);
                et_money_one.setText(String.valueOf(minMoney == null ? "" : minMoney));
                et_money_tow.setText(String.valueOf(maxMoney == null ? "" : maxMoney));
                break;
            case 1://已选择，则取消选择
                cancelSelectedMoneyItem(tv_money);
                et_money_one.setText("");
                et_money_tow.setText("");
                break;
            default:
        }
    }
    //endregion

    //region Action

    @Override
    public void onClick(final View v) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (v.getId()) {
                    case R.id.bt_reset:
                        reset();
                        break;
                    case R.id.bt_finish:
                        outFragment();
                        break;
                    case R.id.layout_out:
                        outFragment();
                        break;
                    case R.id.tv_category:
                        removeAllTop();
                        break;
                    default:
                }
            }
        });

    }

    //region 销量
    public View.OnClickListener numberOnClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    current_method = Filter.FilterNumber;
                    switch (v.getId()) {
                        case R.id.tv_number_up_low:
                            if (Integer.valueOf(v.getTag().toString()) == 0) {
                                current_order_type = Filter.SortOrderDesc;
                                clearSelectNumber();

                                focusNumberItem((TextView) v);
                            } else {
                                current_order_type = null;
                                cancelSelectedNumberItem((TextView) v);
                            }
                            break;
                        case R.id.tv_number_low_up:
                            if (Integer.valueOf(v.getTag().toString()) == 0) {
                                current_order_type = Filter.SortOrderAsc;
                                clearSelectNumber();
                                focusNumberItem((TextView) v);
                            } else {
                                current_order_type = null;
                                cancelSelectedNumberItem((TextView) v);
                            }
                            break;
                        default:
                    }
                    submit();
                }
            });
        }
    };
    //endregion

    //region 返利区间
    public View.OnClickListener reBackOnClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView tv_reBack = (TextView) v;
                    switch (v.getId()) {
                        case R.id.tv_reback_1:
                            judgeReBackItemAndCorporate(tv_reBack, 0, 5);
                            break;
                        case R.id.tv_reback_2:
                            judgeReBackItemAndCorporate(tv_reBack, 5, 10);
                            break;
                        case R.id.tv_reback_3:
                            judgeReBackItemAndCorporate(tv_reBack, 10, 15);
                            break;
                        case R.id.tv_reback_4:
                            judgeReBackItemAndCorporate(tv_reBack, 15, 20);
                            break;
                        case R.id.tv_reback_5:
                            judgeReBackItemAndCorporate(tv_reBack, 20, null);
                            break;
                        /*case R.id.tv_reback_6:
                            judgeReBackItemAndCorporate(tv_reBack, 25, null);
                            break;*/
                        default:
                    }
                }
            });
            submit();
        }
    };
    //endregion

    //region 金币区间
    public View.OnClickListener moneyOnClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView tv_money = (TextView) v;
                    switch (v.getId()) {
                        case R.id.tv_money_1:
                            judgeMoneyItemAndCorporate(tv_money, 0, 100);
                            break;
                        case R.id.tv_money_2:
                            judgeMoneyItemAndCorporate(tv_money, 100, 200);
                            break;
                        case R.id.tv_money_3:
                            judgeMoneyItemAndCorporate(tv_money, 200, 300);
                            break;
                        case R.id.tv_money_4:
                            judgeMoneyItemAndCorporate(tv_money, 300, 500);
                            break;
                        case R.id.tv_money_5:
                            judgeMoneyItemAndCorporate(tv_money, 500, 700);
                            break;
                        case R.id.tv_money_6:
                            judgeMoneyItemAndCorporate(tv_money, 700, null);
                            break;
                        default:
                    }
                }
            });
            submit();
        }
    };
    //endregion

    //endregion

    //region Helper
    //region 销量操作
    private void focusNumberItem(TextView tv_number) {
        tv_number.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_angle_alpha_red));
        tv_number.setTextColor(ContextCompat.getColor(getActivity(),R.color.tv_White));
        tv_number.setTag(1);
    }

    /**
     * 清除所有选择
     */
    private void clearSelectNumber() {
        for (TextView tv : numberArrayList) {
            tv.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_angle_gray));
            tv.setTextColor(ContextCompat.getColor(getActivity(),R.color.tv_Gray));
            tv.setTag(0);
        }
    }

    /**
     * 取消选择某一个项目
     */
    private void cancelSelectedNumberItem(TextView tv_number) {
        tv_number.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_angle_gray));
        tv_number.setTextColor(ContextCompat.getColor(getActivity(),R.color.tv_Gray));
        tv_number.setTag(0);
    }
    //endregion

    //region 返利区间操作

    /**
     * 选择一个项目
     */
    private void focusReBackItem(TextView tv_reBack) {
        tv_reBack.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_angle_alpha_red));
        tv_reBack.setTextColor(ContextCompat.getColor(getActivity(),R.color.tv_White));
        tv_reBack.setTag(1);
    }

    /**
     * 清除所有选择
     */
    private void clearSelectReBack() {
        for (TextView tv : reBackArrayList) {
            tv.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_angle_gray));
            tv.setTextColor(ContextCompat.getColor(getActivity(),R.color.tv_Gray));
            tv.setTag(0);
        }
    }

    /**
     * 取消选择某一个项目
     */
    private void cancelSelectedReBackItem(TextView tv_reBack) {
        tv_reBack.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_angle_gray));
        tv_reBack.setTextColor(ContextCompat.getColor(getActivity(),R.color.tv_Gray));
        tv_reBack.setTag(0);
    }
    //endregion

    //region 价格区间操作

    /**
     * 选择一个项目
     */
    private void focusMoneyItem(TextView tv_money) {
        tv_money.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_angle_alpha_red));
        tv_money.setTextColor(ContextCompat.getColor(getActivity(),R.color.tv_White));
        tv_money.setTag(1);
    }

    /**
     * 清除所有选择
     */
    private void clearSelectMoney() {
        for (TextView tv : moneyArrayList) {
            tv.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_angle_gray));
            tv.setTextColor(ContextCompat.getColor(getActivity(),R.color.tv_Gray));
            tv.setTag(0);
        }
    }

    /**
     * 取消选择某一个项目
     */
    private void cancelSelectedMoneyItem(TextView tv_money) {
        tv_money.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_angle_gray));
        tv_money.setTextColor(ContextCompat.getColor(getActivity(),R.color.tv_Gray));
        tv_money.setTag(0);
    }
    //endregion

    //region 分类操作

    private void showFlowDialog () {
        if (customProgressDialogView == null) {
            customProgressDialogView = new CustomProgressDialogView(getContext());
        }
        flowLayout.removeAllViews();
        flowLayout.addView(customProgressDialogView);
    }

    /**
     * 获取更多的子分类
     */
    private void arrangeCatListSub(String category_id) {
        useCategory();
        category.setCategoryID(category_id);
        showFlowDialog();
        if (category_id.equals(Category.AllCategoryID)) {
            filterSuperDeliverDelegate.specialFilterMain(category,backMainList);
        } else
            filterSuperDeliverDelegate.specialFilterChild(category,backChildList);
    }

    /**
     * 将子分类添加到界面
     */
    private void addSecondSelectItem(ArrayList<JSONObject> cats) {
        flowLayout.removeAllViews();
        for (final JSONObject json : cats) {
            View v = inflater.inflate(R.layout.search_item_layout, null);
            TextView tv_searchItem = (TextView) v.findViewById(R.id.tv_searchItem);
            tv_searchItem.setText(JsonClass.sj(json, "cat_name"));
            tv_searchItem.setTag(JsonClass.sj(json, "cat_id"));//在tag 中存储当前项目的分类ID
            tv_searchItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Category category = new Category();
                    category.setCategoryID(v.getTag().toString());
                    category.setCategoryName(((TextView) v).getText().toString());
                    category.setParentCategoryID(JsonClass.sj(json, "parent_id"));
                    category.setpType(FilterSuperDeliverFragment.this.category.getpType());
                    arrangeCatListSub(category.getCategoryID());
                }
            });
            flowLayout.addView(v);
        }
    }

    /**
     * 将选择的分类放到上方
     * 仅仅放
     * */
    private void justAddChildSelectCategory(final Category category) {
        if (hasSelectCategoryArrayList == null) {
            hasSelectCategoryArrayList = new ArrayList<>();
        }
        for (int j = 0 ; j < hasSelectCategoryArrayList.size() ; j ++ ) {
            Category selectCategory = hasSelectCategoryArrayList.get(j);
            if (selectCategory.getCategoryID().equals(category.getCategoryID())){
                hasSelectCategoryArrayList.remove(selectCategory);
                for (int i = 0 ; i < flowLayoutHasSelect.getChildCount() ; i ++) {
                    View v = flowLayoutHasSelect.getChildAt(i);
                    if ( ((Category)v.getTag()).getCategoryID().equals(selectCategory.getCategoryID())){
                        flowLayoutHasSelect.removeView(v);
                    }
                }
            }
        }
        if (!category.getCategoryID().equals(Category.AllCategoryID)) {
            hasSelectCategoryArrayList.add(category);
            View itemView = inflater.inflate(R.layout.layout_hasselect_category_item, null);
            TextView tv_item = (TextView) itemView.findViewById(R.id.tv_item);
            tv_item.setText(category.getCategoryName());
            itemView.setTag(category);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeTop((Category) v.getTag());
                }
            });
            flowLayoutHasSelect.addView(itemView);
            /*flowLayoutHasSelect.removeAllViews();
            for (final Category selectCategory : hasSelectCategoryArrayList) {
                View itemView = inflater.inflate(R.layout.layout_hasselect_category_item, null);
                TextView tv_item = (TextView) itemView.findViewById(R.id.tv_item);
                tv_item.setText(selectCategory.getCategoryName());
                itemView.setTag(selectCategory);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeTop((Category) v.getTag());
                    }
                });
                flowLayoutHasSelect.addView(itemView);
            }*/
        }
    }

    /**
     * 移除上方选项
     */
    protected void removeTop(Category category) {
        int removeIndex = hasSelectCategoryArrayList.size() - 1;
        for (int i = 0; i < hasSelectCategoryArrayList.size(); i++) {
            if (hasSelectCategoryArrayList.get(i).getCategoryID().equals(category.getCategoryID())) {
                Log.i("RemoveTop","remove categoryID : " + category.getCategoryID());
                removeIndex = i;
            }
        }
        Log.i("RemoveTop", "hasSelectCategoryArrayList.size() - 1 : " + (hasSelectCategoryArrayList.size() - 1) + " removeIndex : " + removeIndex);
        for (int i = hasSelectCategoryArrayList.size() - 1; i >= removeIndex; i--) {
            Log.i("RemoveTop", "i : " + i);
            hasSelectCategoryArrayList.remove(i);
            flowLayoutHasSelect.removeView(flowLayoutHasSelect.getChildAt(i));
        }
        Log.i("RemoveTop", "arrangeCatListSub id : " + category.getParentCategoryID());
        arrangeCatListSub(category.getParentCategoryID());
    }

    /**
     * 移除上方选项
     */
    protected void removeAllTop() {
        hasSelectCategoryArrayList.clear();
        flowLayoutHasSelect.removeAllViews();
        arrangeCatListSub(Category.AllCategoryID);
    }

    /**
     * 子分类回调
     */
    protected com.wzzc.NextSuperDeliver.list.FilterSuperDeliverDelegate.BackChildList backChildList = new FilterSuperDeliverDelegate.BackChildList() {
        @Override
        public void backCatParentInfo(JSONObject json) {
            Category category = new Category();
            Log.i("RemoveTop","parentID : " + JsonClass.sj(json,"parent_id"));
            category.setCategoryID(JsonClass.sj(json,"parent_id"));
            category.setCategoryName(JsonClass.sj(json,"parent_name"));
            justAddChildSelectCategory(category);
            Category c_2 = new Category();
            Log.i("RemoveTop","childID : " + JsonClass.sj(json,"cat_id"));
            c_2.setCategoryID(JsonClass.sj(json,"cat_id"));
            c_2.setCategoryName(JsonClass.sj(json,"cat_name"));
            c_2.setParentCategoryID(JsonClass.sj(json,"parent_id"));
            justAddChildSelectCategory(c_2);
        }

        @Override
        public void backChildSelect(JSONArray cat_list_sub) {
            ArrayList<JSONObject> arr = new ArrayList<>();
            for (int i = 0; i < cat_list_sub.length(); i++) {
                arr.add(JsonClass.jjrr(cat_list_sub, i));
            }
            addSecondSelectItem(arr);
        }
    };

    /**
     * 主分类回调
     */
    protected com.wzzc.NextSuperDeliver.list.FilterSuperDeliverDelegate.BackMainList backMainList = new FilterSuperDeliverDelegate.BackMainList() {
        @Override
        public void backMainSelect(JSONArray cat_list) {
            ArrayList<JSONObject> arr = new ArrayList<>();
            for (int i = 0; i < cat_list.length(); i++) {
                arr.add(JsonClass.jjrr(cat_list, i));
            }
            addSecondSelectItem(arr);
        }
    };
    //endregion
    //endregion
}
