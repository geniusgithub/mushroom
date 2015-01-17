//package com.mobile.yunyou.map.data;
//
//
//import com.mobile.yunyou.map.UploadGPSManager;
//import com.mobile.yunyou.map.util.WebManager;
//import com.mobile.yunyou.util.YunTimeUtils;
//import com.mobile.yunyou.util.Utils;
//
//import android.app.Activity;
//import android.content.Context;
//import android.location.Location;
//import android.os.Bundle;
//import android.os.Handler;
//import android.location.LocationListener;
//
//public class GpsLocationListener implements LocationListener{
//
//	private Handler mHandler;
//	private IUpdateLocationRunable mRunnable;
//	private Activity mActivity;
//	
//	private UploadGPSManager mUploadGPSManager;
//	
//	public GpsLocationListener(Activity activity, Handler handler, IUpdateLocationRunable runnable)
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
//		if (mHandler != null && location != null)
//		{
//			Thread thread = new InnerThread(location);
//			
//			thread.start();
//		}
//		
//		
//	
//	}
//	
//	class InnerThread extends Thread
//	{
//		private Location mLocation;
//		
//		public InnerThread( Location location)
//		{
//			mLocation = location;		
//			
//		}
//
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//	
//			mUploadGPSManager.UploadGPSInfo(mLocation.getLatitude(), mLocation.getLongitude());
//			
//			LocationEx locationEx = new LocationEx(mLocation);
//			Location newLocation = WebManager.correctPosToMap(locationEx.getLatitude(), locationEx.getLongitude());
//			if (newLocation == null)
//			{
//				return ;
//			}
//			locationEx.setOffsetLonLat(newLocation.getLatitude(), newLocation.getLongitude());
//			
//			
//			String timeString = YunTimeUtils.getFormatTime(locationEx.getTime());
//			locationEx.setUpdateTimeString(timeString);
//			
//			try {
//				//mRunnable.setAdress(WebManager.getAddressByGoogle(mLocation));
//				String adString = WebManager.getAddressByGaoDe(mActivity, newLocation);
//				locationEx.setAdress(adString);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			mRunnable.setLocation(locationEx);
//			mHandler.post(mRunnable);
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
