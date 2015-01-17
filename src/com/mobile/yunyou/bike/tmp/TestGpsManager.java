package com.mobile.yunyou.bike.tmp;

import com.mobile.yunyou.map.data.LocationEx;
import com.mobile.yunyou.map.util.WebManager;
import com.mobile.yunyou.util.YunTimeUtils;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class TestGpsManager implements LocationListener{

	private final static int CHECK_POSITION_INTERVAL = 10 * 1000;
	
	private LocationManager mlocationManager;
	
	private Context mContext;
	
	private LocationListener mListener;
	
	private Criteria criteria;

	private String mprovider = LocationManager.NETWORK_PROVIDER;
	
	public TestGpsManager(Context context)
	{
		mContext = context;
	
		
		mlocationManager=(LocationManager)context.getSystemService(context.LOCATION_SERVICE);
	}
	
	public Location getLastLocation(){
		return mlocationManager.getLastKnownLocation(mprovider);
	}
	
	public void switchGPSProvide(boolean flag){
		if (flag){
			mprovider = LocationManager.GPS_PROVIDER;
		}else{
			mprovider = LocationManager.NETWORK_PROVIDER;
		}
	}
	
	public boolean isGPSProvider(){
		if (mprovider.equalsIgnoreCase(LocationManager.GPS_PROVIDER)){
			return true;
		}else{
			return false;
		}
	}
	
	public  void registerListen(LocationListener listener)
	{
		if (mListener == null)
		{
			mListener = listener;
		//	String provider = LocationManager.GPS_PROVIDER;
		//	String provider = LocationManager.NETWORK_PROVIDER;
			if (mprovider != null)
			{
				mlocationManager.requestLocationUpdates(mprovider,CHECK_POSITION_INTERVAL, 0, this);
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


	@Override
	public void onLocationChanged(Location location) {

		if (mListener != null){
			mListener.onLocationChanged(location);
		}
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
