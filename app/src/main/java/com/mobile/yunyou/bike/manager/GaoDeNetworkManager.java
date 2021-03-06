package com.mobile.yunyou.bike.manager;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;



public class GaoDeNetworkManager implements AMapLocationListener {

	private static final CommonLog log = LogFactory.createLog();
	private final static int CHECK_POSITION_INTERVAL = 15 * 1000;
	
	private LocationManagerProxy aMapLocManager = null;
	
	private Context mContext;
	
	private AMapLocationListener mListener;

	private AMapLocation mLastAMapLocation;
	
	public GaoDeNetworkManager(Context context)
	{
		mContext = context;
	
		aMapLocManager = LocationManagerProxy.getInstance(mContext);


	}
	

	
	public void registerListen(AMapLocationListener listener)
	{
		if (mListener == null)
		{
			mListener = listener;
			aMapLocManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, CHECK_POSITION_INTERVAL, 0, this);
			log.e("GaoDeNetworkManager registerListen");
		}

	}
	
	public void unRegisterListen()
	{
		if(mListener != null)
		{
			aMapLocManager.removeUpdates(this);	
			mListener = null;
			log.e("GaoDeNetworkManager unRegisterListen");
		}
	}


	public AMapLocation getAMapLocation(){
		return mLastAMapLocation;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
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
		log.e("GaoDeNetworkManager onLocationChanged = " + location);
		InnerThread innerThread = new InnerThread(location, mListener);
		innerThread.start();
	}
	
	class InnerThread extends Thread
	{
		private AMapLocation mLocation;
		private AMapLocationListener listener;
		
		public InnerThread( AMapLocation location, AMapLocationListener listener)
		{
			mLocation = location;		
			this.listener = listener;
		}

		@Override
		public void run() {
			if (listener != null){
				try {
					listener.onLocationChanged(mLocation);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}



}
