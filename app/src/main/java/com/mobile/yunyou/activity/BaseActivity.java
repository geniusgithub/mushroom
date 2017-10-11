package com.mobile.yunyou.activity;



import com.mobile.yunyou.YunyouApplication;

import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		YunyouApplication.onCatchError(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		YunyouApplication.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		YunyouApplication.onResume(this);
	}

}
