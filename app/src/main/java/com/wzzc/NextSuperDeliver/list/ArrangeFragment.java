package com.wzzc.NextSuperDeliver.list;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/8/9.
 *
 */

public class ArrangeFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {
    public static final String ArrangeDelegate = "arrangeDelegate";
    public static final String SortMethod = "sort_method";
    public static final String SortOrder = "sort_order";

    ArrangeDelegate arrangeDelegate;
    String sortMethod;
    String sortOrder;
    RadioGroup radioGroup;
    RadioButton rb_default , rb_money_desc , rb_money_asc ,
            rb_number_desc, rb_number_asc;
    RelativeLayout layout_out;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_arrange_fragment,null);
        radioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);
        rb_default = (RadioButton) v.findViewById(R.id.rb_default);
        rb_money_desc = (RadioButton) v.findViewById(R.id.rb_money_desc);
        rb_money_asc = (RadioButton) v.findViewById(R.id.rb_money_asc);
        rb_number_desc = (RadioButton) v.findViewById(R.id.rb_number_desc);
        rb_number_asc = (RadioButton) v.findViewById(R.id.rb_number_asc);

        layout_out = (RelativeLayout) v.findViewById(R.id.layout_out);
        layout_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outFragment();
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        arrangeDelegate = bundle.getParcelable(ArrangeDelegate);
        sortMethod = bundle.getString(SortMethod);
        sortOrder = bundle.getString(SortOrder);
        selectItem(sortMethod,sortOrder);
        radioGroup.setOnCheckedChangeListener(this);
    }

    public void outFragment () {
        getActivity().onBackPressed();
    }

    //region Method
    public void selectItem (String sortMethod , String sortOrder) {
        clearRadioTextColor();
        switch (sortMethod){
            case Filter.FilterDefault:
                focusRadio(rb_default);
                break;
            case Filter.FilterMoney:
                switch (sortOrder){
                    case Filter.SortOrderDesc:
                        focusRadio(rb_money_desc);
                        break;
                    case Filter.SortOrderAsc:
                        focusRadio(rb_money_asc);
                        break;
                    default:
                }
                break;
            case Filter.FilterNumber:
                switch (sortOrder){
                    case Filter.SortOrderDesc:
                        focusRadio(rb_number_desc);
                        break;
                    case Filter.SortOrderAsc:
                        focusRadio(rb_number_asc);
                        break;
                    default:
                }
                break;
            default:
        }
    }
    //endregion

    //region Helper
    private void focusRadio (RadioButton rb) {
        selectRadioText(rb);
        rb.setChecked(true);
    }

    private void selectRadioText (RadioButton rb) {
        rb.setTextColor(ContextCompat.getColor(getContext(),R.color.orangered));
    }

    private void clearRadioTextColor () {
        for (int i = 0 ; i < radioGroup.getChildCount() ; i ++) {
            RadioButton rb = (RadioButton) radioGroup.getChildAt(i);
            rb.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId){
            case R.id.rb_default:
                sortMethod = Filter.FilterDefault;
                sortOrder = "";
                break;
            case R.id.rb_money_desc:
                sortMethod = Filter.FilterMoney;
                sortOrder = Filter.SortOrderDesc;
                break;
            case R.id.rb_money_asc:
                sortMethod = Filter.FilterMoney;
                sortOrder = Filter.SortOrderAsc;
                break;
            case R.id.rb_number_desc:
                sortMethod = Filter.FilterNumber;
                sortOrder = Filter.SortOrderDesc;
                break;
            case R.id.rb_number_asc:
                sortMethod = Filter.FilterNumber;
                sortOrder = Filter.SortOrderAsc;
                break;
            default:
        }
        clearRadioTextColor();
        selectRadioText((RadioButton) getView().findViewById(checkedId));
        arrangeDelegate.arrange(sortMethod,sortOrder);
        outFragment();
    }
    //endregion

}
