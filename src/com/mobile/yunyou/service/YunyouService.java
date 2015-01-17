package com.mobile.yunyou.service;

import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class YunyouService extends Service{

	private static final CommonLog log = LogFactory.createLog();

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		log.e("YunyouService onCreate................................");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	
		log.e("YunyouService onDestroy................................");
	}


	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
