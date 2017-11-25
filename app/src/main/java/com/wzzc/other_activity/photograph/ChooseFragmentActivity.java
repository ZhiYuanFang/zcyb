package com.wzzc.other_activity.photograph;

import android.content.Intent;
import android.os.Bundle;

/**
 * 装入UpLoadFragment的Activity<br>
 * 这里写死一个Fragment实际场景有多个Frament,区别大同小异
 */
public class ChooseFragmentActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_choose_fragment);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String imagePath = "";
		if(requestCode == SELECT_IMAGE_RESULT_CODE && resultCode== RESULT_OK){
			if(data != null && data.getData() != null){//有数据返回直接使用返回的图片地址
				imagePath = ImageUtils.getFilePathByFileUri(this, data.getData());
			}else{//无数据使用指定的图片路径
				imagePath = mImagePath;
			}
			mOnFragmentResult.onResult(imagePath);
		}
	}
}
