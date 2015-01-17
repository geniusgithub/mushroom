//package com.mobile.yunyou.map.data;
//
//import com.mobile.yunyou.map.UploadGPSManager;
//import com.mobile.yunyou.map.util.WebManager;
//
//import android.app.Activity;
//import android.content.Context;
//import android.location.Location;
//import android.location.LocationListener;
//import android.os.Bundle;
//import android.os.Handler;
//
//public class BaseStationLocationListener implements LocationListener{
//
//	private Handler mHandler;
//	private IUpdateLocationRunable mRunnable;
//	private Activity mActivity;
//	
//	private UploadGPSManager mUploadGPSManager;
//	
//	public BaseStationLocationListener(Activity activity, Handler handler, IUpdateLocationRunable runnable)
//	{
//		mHandler = handler;
//		mRunnable = runnable;
//		mActivity = activity;
//		
//		 mUploadGPSManager = UploadGPSManager.getInstance();
//	}
//	
//	@Override
//	public void onLocationChanged(Location location) {
//		// TODO Auto-generated method stub
//		
//	
//	
//		
//		if (mHandler != null && location != null)
//		{
//			
//	//		mUploadGPSManager.UploadGPSInfo(location.getLatitude(), location.getLongitude());
//			
//			LocationEx locationEx = (LocationEx) location;
//			Location newLocation = WebManager.correctPosToMap(locationEx.getLatitude(), locationEx.getLongitude());
//			if (newLocation == null)
//			{
//				return ;
//			}
//			locationEx.setOffsetLonLat(newLocation.getLatitude(), newLocation.getLongitude());
//			
//			try {
//				//mRunnable.setAdress(WebManager.getAddressByGoogle(location));
//				String adressString = WebManager.getAddressByGaoDe(mActivity, newLocation);
//				locationEx.setAdress(adressString);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			mRunnable.setLocation(locationEx);
//			
//			mHandler.post( mRunnable);
//		}
//	
//	
//	}
//
//	@Override
//	public void onProviderDisabled(String provider) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void onProviderEnabled(String provider) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void onStatusChanged(String provider, int status, Bundle extras) {
//		// TODO Auto-generated method stub
//		
//	}
//	
//}
