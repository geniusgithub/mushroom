package com.mobile.yunyou.set;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.mobile.yunyou.R;
import com.mobile.yunyou.activity.BaseActivity;

public class SetWarnActivity extends BaseActivity implements OnClickListener{

	private Button mBtnBack;
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_warn_layout);

       initView();
    }
	
	
	private void initView()
	{
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
		
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
			case R.id.btn_back:
				finish();
				break;
		}
	}
}
