package com.wzzc.other_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by toutou on 2017/4/5.
 * <p>
 * 省/市/区/街道
 */

public class RegionView extends BaseView {

    //region 组件
    @ViewInject(R.id.spinner_province)
    private Spinner spinner_province;
    @ViewInject(R.id.spinner_city)
    private Spinner spinner_city;
    @ViewInject(R.id.spinner_district)
    private Spinner spinner_district;
    @ViewInject(R.id.spinner_street)
    private Spinner spinner_street;
    @ViewInject(R.id.layout_contain_street)
    private LinearLayout layout_contain_street;
    //endregion

    private int table_type;
    private String region_id_province, region_id_city, region_id_district, region_id_street;

    public RegionView(Context context) {
        super(context);
        init();
    }

    public RegionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray t = getContext().obtainStyledAttributes(attrs, R.styleable.RegionViewElement);
        table_type = t.getInt(R.styleable.RegionViewElement_tableType, 0);
        init();
        initialized();
        t.recycle();
    }

    private void init() {

    }

    public void setInfo(int table_type) {
        this.table_type = table_type;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initialized();
            }
        }, 1);
    }

    public void setInfo() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initialized();
            }
        }, 1);
    }

    private void initialized() {
        if (table_type == 0) {
            layout_contain_street.setVisibility(VISIBLE);
        } else {
            layout_contain_street.setVisibility(GONE);
        }
        JSONObject para = new JSONObject();
        try {
            para.put("table_type", table_type);//0新的区域表用于入住商或定位,1旧的区域表,用于收货地址的区域选择
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(getContext(), "region_all",  para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                //region 获取数据
                try {
                    JSONObject json_status = sender.getJSONObject("status");
                    int succeed = json_status.getInt("succeed");
                    if (succeed == 1) {
                        JSONArray data = sender.getJSONArray("data");
                        spinner_province.setAdapter(initAdapter(data));

                        spinner_province.setOnItemSelectedListener(provinceSelect(data));
                    } else {
                        Default.showToast("服务器访问失败",Toast.LENGTH_LONG);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //endregion
            }
        });


    }

    //region itemSelect
    private AdapterView.OnItemSelectedListener provinceSelect(final JSONArray jrr) {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    try {
                        JSONObject json = jrr.getJSONObject(position - 1);
                        region_id_province = json.getString("region_id");
                        if (json.has("sub")) {
                            spinner_city.setAdapter(initAdapter(json.getJSONArray("sub")));
                            spinner_city.setOnItemSelectedListener(citySelect(json.getJSONArray("sub")));
                            region_id_city = null;
                            spinner_district.setAdapter(initAdapter(null));
                            region_id_district = null;
                            spinner_street.setAdapter(initAdapter(null));
                            region_id_street = null;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    private AdapterView.OnItemSelectedListener citySelect(final JSONArray jrr) {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    try {
                        JSONObject json = jrr.getJSONObject(position - 1);
                        region_id_city = json.getString("region_id");
                        if (json.has("sub")) {
                            spinner_district.setAdapter(initAdapter(json.getJSONArray("sub")));
                            spinner_district.setOnItemSelectedListener(districtSelect(json.getJSONArray("sub")));
                            region_id_district = null;
                            spinner_street.setAdapter(initAdapter(null));
                            region_id_street = null;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    private AdapterView.OnItemSelectedListener districtSelect(final JSONArray jrr) {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    try {
                        JSONObject json = jrr.getJSONObject(position - 1);
                        region_id_district = json.getString("region_id");
                        JSONObject para = new JSONObject();
                        para.put("type", json.getString("sub_type"));
                        para.put("parent_id", region_id_district);
                        if (table_type != 1) {
                            AsynServer.BackObject(getContext(), "region_agent", para, new AsynServer.BackObject() {
                                @Override
                                public void Back(JSONObject s) {
                                    JSONObject json_status = JsonClass.jj(s, "status");
                                    if (JsonClass.ij(json_status, "succeed") == 0) {
                                        MessageBox.Show(JsonClass.sj(json_status, "error_desc"));
                                    } else {
                                        JSONObject sender = JsonClass.jj(s, "data");
                                        JSONArray jrr = JsonClass.jrrj(sender,"regions");
                                        spinner_street.setAdapter(initAdapter(jrr));
                                        spinner_street.setOnItemSelectedListener(streetSelect(jrr));
                                        region_id_street = null;
                                        if (jrr.length() == 0) {
                                            region_id_street = "";
                                        }
                                    }
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    private AdapterView.OnItemSelectedListener streetSelect(final JSONArray jrr) {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    try {
                        region_id_street = jrr.getJSONObject(position - 1).getString("region_id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }
    //endregion

    //region adapter
    protected SpinnerAdapter initAdapter(JSONArray jrr) {
        final JSONArray data = new JSONArray();
        if (jrr != null && jrr.length() > 0) {
            data.put(new JSONObject() {{
                try {
                    put("region_name", "请选择");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }});
        }

        if (jrr == null) {
            jrr = new JSONArray();
        }

        for (int i = 0; i < jrr.length(); i++) {
            try {
                data.put(jrr.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return new SpinnerAdapter() {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = new TextView(getContext());
                    convertView.setPadding(3, 3, 3, 3);
                }
                TextView tv = (TextView) convertView;
                try {
                    String name = data.getJSONObject(position).getString("region_name");
                    tv.setText(name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return convertView;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public int getCount() {
                return data.length();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = new TextView(getContext());
                    convertView.setPadding(3, 3, 3, 3);
                }
                TextView tv = (TextView) convertView;
                try {
                    tv.setText(data.getJSONObject(position).getString("region_name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return convertView;
            }

            @Override
            public int getItemViewType(int position) {
                return 1;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        };
    }
    //endregion

    public String getRegionName() {
        StringBuilder stringBuilder = new StringBuilder();
        if (spinner_province.getSelectedItemPosition() > 0) {
            stringBuilder.append(((TextView) spinner_province.getSelectedView()).getText());
        }

        if (spinner_city.getSelectedItemPosition() > 0) {
            stringBuilder.append("/").append(((TextView) spinner_city.getSelectedView()).getText());
        }

        if (spinner_district.getSelectedItemPosition() > 0) {
            stringBuilder.append("/").append(((TextView) spinner_district.getSelectedView()).getText());
        }

        if (spinner_street.getSelectedItemPosition() > 0) {
            stringBuilder.append(((TextView) spinner_street.getSelectedView()).getText());
        }

        return stringBuilder.toString();
    }

    public ArrayList getRegionNameArray() {
        ArrayList<CharSequence> arrList = new ArrayList<>();
        if (spinner_province.getSelectedItemPosition() > 0) {
            arrList.add(((TextView) spinner_province.getSelectedView()).getText());
        }

        if (spinner_city.getSelectedItemPosition() > 0) {
            arrList.add(((TextView) spinner_city.getSelectedView()).getText());
        }

        if (spinner_district.getSelectedItemPosition() > 0) {
            arrList.add(((TextView) spinner_district.getSelectedView()).getText());
        }

        if (spinner_street.getSelectedItemPosition() > 0) {
            arrList.add(((TextView) spinner_street.getSelectedView()).getText());
        }
        return arrList;
    }

    public String[] getRegionId() {
        if (region_id_province == null || region_id_city == null || region_id_district == null) {

            Default.showToast("区域未选择完整", Toast.LENGTH_LONG);
            return null;
        } else {
            if (table_type == 0 && region_id_street == null) {
                Default.showToast("区域未选择完整", Toast.LENGTH_LONG);
                return null;
            } else
                return new String[]{region_id_province, region_id_city, region_id_district, region_id_street};
        }
    }

    public String[] getRegionLocationId() {
        if (region_id_province == null || region_id_city == null) {

            Default.showToast("区域未选择完整", Toast.LENGTH_LONG);
            return null;
        } else {
            return new String[]{region_id_province, region_id_city, region_id_district, region_id_street};
        }
    }

}
