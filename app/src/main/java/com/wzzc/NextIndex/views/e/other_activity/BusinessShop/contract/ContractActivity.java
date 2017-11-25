package com.wzzc.NextIndex.views.e.other_activity.BusinessShop.contract;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wzzc.NextIndex.views.e.User;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.OnClick;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.NextIndex.views.e.other_activity.BusinessShop.contract.main_view.ContractView;
import com.wzzc.NextIndex.views.e.LoginActivity;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.ImageHelper;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_view.extendView.ExtendListView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.os.Environment.getExternalStorageDirectory;

/**
 * Created by zcyb365 on 2016/11/22.
 */
public class ContractActivity extends BaseActivity {
    private CollectionAdapter adapter;
    @ViewInject(R.id.main_view)
    private ExtendListView main_view;
    @ViewInject(R.id.lab_noshopping)
    private TextView lab_noshopping;
    @ViewInject(R.id.input_Recommend)
    EditText input_Recommend;
    @ViewInject(R.id.input_shop)
    EditText input_shop;
    @ViewInject(R.id.btn_Choice)
    private Button btn_Choice;
    @ViewInject(R.id.img)
    ImageView img;
    @ViewInject(R.id.btn_addcontract)
    Button btn_addcontract;
    private boolean isloading = false;
    private int pageindex = 1;
    private int pagecount = 10;

    /**
     * 选择图片的返回码
     */
    public final static int SELECT_IMAGE_RESULT_CODE = 200;
    /**
     * 当前选择的图片的路径
     */
    public String mImagePath;

    /**
     * 回调数据给Fragment的接口
     */
    public interface OnFragmentResult {
        void onResult(String mImagePath);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddBack();
        AddTitle("我的合同");
        btn_addcontract.setOnClickListener(submitListener);
        if (ContextCompat.checkSelfPermission(Default.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //请求权限
            ActivityCompat.requestPermissions(Default.getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1002);
            //判断是否需要 向用户解释，为什么要申请该权限
            ActivityCompat.shouldShowRequestPermissionRationale(Default.getActivity(),Manifest.permission.READ_CONTACTS);
        }
    }

    @Override
    protected void viewFirstLoad() {
        adapter = new CollectionAdapter(this);
        main_view.setAdapter(adapter);
        GetServerInfo();
    }

    public void GetServerInfo() {
        if (isloading) {
            return;
        }
        isloading = true;
        JSONObject para = new JSONObject();
        AsynServer.BackObject(ContractActivity.this, "contract/list", para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                try {
                    ArrayList<JSONObject> json = new ArrayList<JSONObject>();
                    JSONObject status = sender.getJSONObject("status");
                    if (status.getInt("succeed") == 0) {
                        AddActivity(LoginActivity.class);
                    } else {
                        JSONArray arr = sender.getJSONArray("data");
                        if (arr.length() == 0) {
                            lab_noshopping.setVisibility(View.VISIBLE);
                        } else {
                            for (int i = 0; i < arr.length(); i++) {
                                json.add(arr.getJSONObject(i));
                            }
                        }
                        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) main_view.getLayoutParams();
                        WindowManager wm = (WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE);
                        int height = wm.getDefaultDisplay().getHeight();
                        params1.height = (Default.dip2px((arr.length() * 102), getBaseContext()));
                        main_view.setLayoutParams(params1);
                        adapter.addData(json);
                        pageindex++;
                        isloading = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class CollectionAdapter extends BaseAdapter {
        private ArrayList<JSONObject> data;
        private Context content;


        public CollectionAdapter(Context context) {
            this.content = context;
            this.data = new ArrayList<>();
        }


        public void addData(ArrayList<JSONObject> data) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            int count = this.data.size();
            return count;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ContractView view;
            if (convertView != null) {
                view = (ContractView) convertView;
            } else {
                view = new ContractView(this.content);
            }
            JSONObject[] arr1;
            int index = position;
            arr1 = new JSONObject[]{this.data.get(index)};
            view.setInfo(arr1);
            return view;
        }
    }

    @OnClick({R.id.btn_Choice})
    public void btn_goto_onclick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(new String[]{"拍摄照片", "选择照片", "取消"},
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        switch (position) {
                            case 0://拍照
                                takePhoto();
                                break;
                            case 1://相册选择图片
                                pickPhoto();
                                break;
                            case 2://取消
                                break;
                            default:
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    /**
     * 拍照获取图片
     */
    private void takePhoto() {
        // 执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            /**
             * 通过指定图片存储路径，解决部分机型onActivityResult回调 data返回为null的情况
             */
            //获取与应用相关联的路径
            String imageFilePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
            //根据当前时间生成图片的名称
            String timestamp = "/" + formatter.format(new Date()) + ".jpg";
            File imageFile = new File(imageFilePath, timestamp);// 通过路径创建保存文件
            mImagePath = imageFile.getAbsolutePath();
            Uri imageFileUri = Uri.fromFile(imageFile);// 获取文件的Uri
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);// 告诉相机拍摄完毕输出图片到指定的Uri
            startActivityForResult(intent, SELECT_IMAGE_RESULT_CODE);
        } else {
            Toast.makeText(this, "内存卡不存在!", Toast.LENGTH_LONG).show();
        }
    }

    /***
     * 从相册中取图片
     */
    private void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_IMAGE_RESULT_CODE);
        Log.d("asd000000000000", String.valueOf(intent));
    }

    Bitmap bm;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_IMAGE_RESULT_CODE) {
                Uri originalUri = data.getData();        //获得图片的uri
                if (originalUri == null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        bm = (Bitmap) bundle.get("data");
                    }
                } else {
                    ContentResolver cr = getContentResolver();
                    try {
                        bm = MediaStore.Images.Media.getBitmap(cr, originalUri);
                    } catch (IOException | SecurityException e) {
                        e.printStackTrace();
                    }
                }
                img.setImageBitmap(bm);
            }
        }
    }

    private View.OnClickListener submitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            JSONObject para = new JSONObject();
            try {
                para.put("session", Default.GetSession());
                para.put("name", input_Recommend.toString());
                para.put("shop_name", input_shop.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            HashMap<String, Object> info = new HashMap<>();
            info.put("json", para.toString());
            info.put("img_src",bm);
            ImageHelper.upLoadImage(ContractActivity.this ,"hd",  "--", "mypxqcreated", "\r\n", info, Default.MainURL + "contract/upload", getExternalStorageDirectory() + "/zc_hd", new ImageHelper.UpLoadListener() {
                @Override
                public void upLoadOK(Context c, int requestTime, String result) {
                    super.upLoadOK(c, requestTime, result);
                    if (result.equals("1")) {
                        AsynServer.BackObject(c, Default.MainURL + "get_session", true, null, new AsynServer.BackObject() {
                            @Override
                            public void Back(JSONObject sender) {
                                JSONObject jon = JsonClass.jj(sender, "data");
                                JSONObject session = JsonClass.jj(jon, "session");
                                User.setUserID(JsonClass.sj(session,"uid"));
                                User.setSession(JsonClass.sj(session, "sid"));
                                btn_addcontract.callOnClick();
                            }
                        });
                    } else{
                        Default.showToast("上传成功");
                    }
                }

                @Override
                public void upLoadError(Context c, int requestTime, int errorCode) {
                    super.upLoadError(c, requestTime, errorCode);
                }

                @Override
                public void createFileError(Context c, File file) {
                    super.createFileError(c, file);
                }

                @Override
                public void timeOut(Context c, int requestTime) {
                    super.timeOut(c, requestTime);
                }

                @Override
                public void deleteImageError(Context c, File imageFile) {
                    super.deleteImageError(c, imageFile);
                }

                @Override
                public void otherError(Context c, int requestTime, String error) {
                    super.otherError(c, requestTime, error);
                }
            });
        }
    };
}
