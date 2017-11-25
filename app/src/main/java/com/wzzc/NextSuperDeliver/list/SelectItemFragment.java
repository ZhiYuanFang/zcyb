package com.wzzc.NextSuperDeliver.list;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wzzc.new_index.SuperDeliver.main_view.childView.SelectDialogDelegate;
import com.wzzc.new_index.SuperDeliver.main_view.childView.SelectItemDialogView;

import java.util.ArrayList;

/**
 * Created by toutou on 2017/5/10.
 *
 */

public class SelectItemFragment extends Fragment {
    public static final String SELECT_ITEM_DATA = "select_item_data";
    public static final String FocusCategory = "category";
    public static final String SDD = "sdd";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ArrayList<Category> selectItemData = getArguments().getParcelableArrayList(SELECT_ITEM_DATA);
        Category category = getArguments().getParcelable(FocusCategory);
        SelectDialogDelegate sdd = getArguments().getParcelable(SDD);
        SelectItemDialogView sidv = new SelectItemDialogView(getActivity());
        sidv.sdd = sdd;
        sidv.setInfo(selectItemData,category);
        return sidv;
    }
}
