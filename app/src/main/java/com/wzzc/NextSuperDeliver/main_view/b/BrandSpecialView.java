package com.wzzc.NextSuperDeliver.main_view.b;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.wzzc.NextSuperDeliver.ShopBean;
import com.wzzc.NextSuperDeliver.main_view.a.BrandViewDelegate;
import com.wzzc.NextSuperDeliver.main_view.a_b.BrandActivity;
import com.wzzc.NextSuperDeliver.main_view.a_b.OtherBrandView;
import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.new_index.NomalAdapter;
import com.wzzc.zcyb365.R;

import java.util.ArrayList;

/**
 * Created by toutou on 2017/8/24.
 * 品牌特惠
 */
@ContentView(R.layout.view_brand_special)
public class BrandSpecialView extends BaseView implements BrandViewDelegate {
    @ViewInject(R.id.btn_back)
    RelativeLayout btn_back;
    @ViewInject(R.id.listView)
    ListView listView;
    NomalAdapter myAdapter;
    public BrandSpecialView(Context context) {
        super(context);init();
    }

    public BrandSpecialView(Context context, AttributeSet attrs) {
        super(context, attrs);init();
    }

    protected void init () {
        btn_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                GetBaseActivity().BackActivity();
            }
        });
        myAdapter = new MyAdapter(getContext());
        listView.setAdapter(myAdapter);
    }

    public ListView getListView() {
        return listView;
    }

    public NomalAdapter getMyAdapter () {
        return myAdapter;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    public void setInfo ( ArrayList<ShopBean> shopBeanArrayList) {
        ArrayList<Object> arrayList = new ArrayList<>();
        for (ShopBean shopBean : shopBeanArrayList) {
            arrayList.add(shopBean);
        }
        myAdapter.addData(arrayList);
    }
    //region BrandViewDelegate
    @Override
    public void chooseShop(ShopBean shopBean) {
        Intent intent = new Intent();
        intent.putExtra(BrandActivity.SHOPBEAN,shopBean);
        GetBaseActivity().AddActivity(BrandActivity.class,0,intent);
    }
    //endregion

    private class MyAdapter extends NomalAdapter {
        public MyAdapter(Context c) {
            super(c);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (!(convertView instanceof OtherBrandView)) {
                convertView = new OtherBrandView(c);
            }
            ((OtherBrandView)convertView).setInfo(BrandSpecialView.this,(ShopBean) getItem(position),false);
            return convertView;
        }
    }
}
