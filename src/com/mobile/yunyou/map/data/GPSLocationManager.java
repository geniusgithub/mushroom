package com.mobile.yunyou.map.data;

import com.mobile.yunyou.map.UploadGPSManager;

import android.content.Context;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;

public class GPSLocationManager {

	private final static int CHECK_POSITION_INTERVAL = 30 * 1000;
	
	private LocationManager mlocationManager;
	
	private Context mContext;
	
	private LocationListener mListener;
	
	private Criteria criteria;
	

	
	public GPSLocationManager(Context context)
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
	

	
	public void registerListen(LocationListener listener)
	{
		if (mListener == null)
		{
			mListener = listener;

		//	String provider = mlocationManager.getBestProvider(criteria,true);
			String provider = LocationManager.GPS_PROVIDER;
			
			if (provider != null)
			{
				mlocationManager.requestLocationUpdates(provider,CHECK_POSITION_INTERVAL, 0, mListener);
			}
		}

	}
	
	public void unRegisterListen()
	{
		if(mListener != null)
		{
			mlocationManager.removeUpdates(mListener);
			
			mListener = null;
			
		}
	}
	
}
