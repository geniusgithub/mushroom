package com.mobile.yunyou.activity;


import com.mobile.yunyou.R;
import com.mobile.yunyou.network.NetworkCenterEx;
//import com.mobile.yunyou.network.NetworkCenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;




public class WelcomeActivity extends Activity{
    /** Called when the activity is first created. */

	private Handler mHandler;
	
	private NetworkCenterEx mNetworkCenter;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcom);
       
        initView();
        
        initData();
    }
    
    
    public void initView()
    {
    	mHandler = new Handler();
    	mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				goLoginActivity();
			}
    	}, 2000);
    }
    
    public void initData()
    {
    	mNetworkCenter = NetworkCenterEx.getInstance();
    	
    }
    
    public void goLoginActivity()
    {
    	Intent intent = new Intent();
    	intent.setClass(this, LoginActivity.class);
    	startActivity(intent);
    	finish();
    }


	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		
		
	}
    
    
    
}