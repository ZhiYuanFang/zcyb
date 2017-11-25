package com.wzzc.other_activity.photograph;

import java.util.LinkedList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import com.wzzc.zcyb365.R;

public class ChooseActivity extends BaseActivity {
	
	/**
	 * 需要上传的图片路径  控制默认图片在最后面需要用LinkedList
	 */
	private LinkedList<String> dataList = new LinkedList<String>();
	/**
	 * 图片上传GridView
	 */
	private GridView uploadGridView;
	/**
	 * 图片上传Adapter
	 */
	private UploadImageAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose);
		findViewById(R.id.btn_change_fragment).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ChooseActivity.this,ChooseFragmentActivity.class));
			}
		});
		uploadGridView = (GridView) findViewById(R.id.grid_upload_pictures);
		dataList.addLast(null);// 初始化第一个添加按钮数据
		adapter = new UploadImageAdapter(this, dataList);
		uploadGridView.setAdapter(adapter);
		uploadGridView.setOnItemClickListener(mItemClick);
		uploadGridView.setOnItemLongClickListener(mItemLongClick);
	}
	
	/**
	 * 上传图片GridView Item单击监听
	 */
	private OnItemClickListener mItemClick = new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if(parent.getItemAtPosition(position) == null){ // 添加图片
				//showPictureDailog();//Dialog形式
				showPicturePopupWindow();//PopupWindow形式
			}
		}
	};
	
	/**
	 * 上传图片GridView Item长按监听
	 */
	private OnItemLongClickListener mItemLongClick = new OnItemLongClickListener(){

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			if(parent.getItemAtPosition(position) != null){ // 长按删除
				dataList.remove(parent.getItemAtPosition(position));
				adapter.update(dataList); // 刷新图片
			}
			return true;
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == SELECT_IMAGE_RESULT_CODE && resultCode == RESULT_OK){
			String imagePath = "";
			if(data != null && data.getData() != null){//有数据返回直接使用返回的图片地址
				imagePath = ImageUtils.getFilePathByFileUri(this, data.getData());
			}else{//无数据使用指定的图片路径
				imagePath = mImagePath;
			}
			dataList.addFirst(imagePath);
			adapter.update(dataList); // 刷新图片
		}
	}
}
