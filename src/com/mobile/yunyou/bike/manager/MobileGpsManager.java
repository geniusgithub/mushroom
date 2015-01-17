package com.mobile.yunyou.bike.manager;

import com.mobile.yunyou.map.data.LocationEx;
import com.mobile.yunyou.map.util.WebManager;
import com.mobile.yunyou.util.YunTimeUtils;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class MobileGpsManager implements LocationListener{

	private final static int CHECK_POSITION_INTERVAL = 30 * 1000;
	
	private LocationManager mlocationManager;
	
	private Context mContext;
	
	private LocationListener mListener;
	
	private Criteria criteria;
	

	
	public MobileGpsManager(Context context)
	{
		mContext = context;
	
		
		mlocationManager=(LocationManager)context.getSystemService(context.LOCATION_SERVICE);
		

		//查询条件
//        criteria=new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_FINE);
//        criteria.setAltitudeRequired(false);
//        criteria.setBearingRequired(false);
//        criteria.setCostAllowed(true);
//        criteria.setPowerRequirement(Criteria.POWER_LOW);
	}
	

	
	public  void registerListen(LocationListener listener)
	{
		if (mListener == null)
		{
			mListener = listener;

		//	String provider = mlocationManager.getBestProvider(criteria,true);
			String provider = LocationManager.GPS_PROVIDER;
			
			if (provider != null)
			{
				mlocationManager.requestLocationUpdates(provider,CHECK_POSITION_INTERVAL, 0, this);
			}
		}

	}
	
	public  void unRegisterListen()
	{
		if(mListener != null)
		{
			mlocationManager.removeUpdates(this);
			
			mListener = null;
			
		}
	}

	class InnerThread extends Thread
	{
		private Location mLocation;
		private LocationListener listener;
		
		public InnerThread( Location location, LocationListener listener)
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

	@Override
	public void onLocationChanged(Location location) {

		InnerThread innerThread = new InnerThread(location, mListener);
		innerThread.start();
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
	
}
