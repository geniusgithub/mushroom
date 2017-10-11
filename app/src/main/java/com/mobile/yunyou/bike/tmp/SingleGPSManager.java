package com.mobile.yunyou.bike.tmp;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.Utils;
import com.mobile.yunyou.util.YunTimeUtils;



public class SingleGPSManager implements AMapLocationListener {

	private static final CommonLog log = LogFactory.createLog();

	private final static int CHECK_POSITION_INTERVAL = 5 * 1000;
	
	private LocationManagerProxy aMapLocManager = null;
	
	private Context mContext;
	private AMapLocationListener mListener;
	private Timer mTimer;
	
	private MyTimeTask mTimeTask;

	private AMapLocation mLocation;
	private String provider = LocationManager.GPS_PROVIDER;
	//private String provider = 	LocationProviderProxy.AMapNetwork;
	private boolean isGetGPS = false;
	
	public SingleGPSManager(Context context)
	{
		mContext = context;
	
		aMapLocManager = LocationManagerProxy.getInstance(mContext);
		mTimer = new Timer();	
		
		if (YunyouApplication.getInstance().mIsDebug){
			provider = 	LocationProviderProxy.AMapNetwork;
		}
	}
	
	public void setGPSProvider(boolean flag){
		if (flag){
			 provider = LocationManager.GPS_PROVIDER;
			 Utils.showToast(mContext, "切换至GPS定位  provider = " + provider);
		}else{
			 provider = LocationProviderProxy.AMapNetwork;
			 Utils.showToast(mContext, "切换至网络定位" + provider);
		}
		
	}
	
	public boolean isGPSProvider(){
		if (provider.equalsIgnoreCase(LocationManager.GPS_PROVIDER)){
			return true;
		}
		
		return false;
	}

	
	public  void  registerListen(AMapLocationListener listener)
	{
		if (YunyouApplication.getInstance().mIsDebug){
			if (provider.equals(LocationManager.GPS_PROVIDER)){
				Utils.showToast(mContext, "使用GPS定位");
			}else{
				Utils.showToast(mContext, "使用网络定位");
			}
			
			
		}
		synchronized(this){
			if (mListener == null)
			{
				mListener = listener;
				aMapLocManager.requestLocationUpdates(provider, 2000, 0, this);
				
			//	startTimer();
				
				isGetGPS = false;
			}
		}
	}
	
	public  void unRegisterListen()
	{
		synchronized(this){
			if(mListener != null)
			{
				aMapLocManager.removeUpdates(this);
				mListener = null;
			//	stopTimer();
				mLocation = null;
				isGetGPS = false;
			}
		}

	}

	
//	private void startTimer( )
//	{
//
//		if (mTimeTask == null)
//		{
//			mTimeTask = new MyTimeTask();
//			mTimer.scheduleAtFixedRate(mTimeTask, 0, CHECK_POSITION_INTERVAL);
//		}
//	}
//	
//	private void stopTimer()
//	{
//		if (mTimeTask != null)
//		{
//			mTimeTask.cancel();
//			mTimeTask = null;
//		}
//	}

	class MyTimeTask extends TimerTask
	{

		@Override
		public void run() {
			synchronized(SingleGPSManager.this){
				if (mListener != null){
					if (isGetGPS){
						mListener.onLocationChanged(mLocation);
					}else{
						log.e("isGetGPS = false");
						mListener.onLocationChanged(null);
					}
				}
			}
		}
		
	}
	
	@Override
	public void onLocationChanged(Location location) {

	}



	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onLocationChanged(AMapLocation location) {
		log.d("(" + location.getLatitude() + ", " + location.getLongitude() + ")  " + YunTimeUtils.getFormatTime2(location.getTime()));
		isGetGPS = true;
		mLocation = location;
		if (mListener != null){
			mListener.onLocationChanged(mLocation);
		}
	}

}
