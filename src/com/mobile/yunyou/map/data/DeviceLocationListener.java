//package com.mobile.yunyou.map.data;
//
//import com.mobile.yunyou.map.UploadGPSManager;
//import com.mobile.yunyou.map.data.GpsLocationListener.InnerThread;
//import com.mobile.yunyou.map.util.WebManager;
//import com.mobile.yunyou.util.CommonLog;
//import com.mobile.yunyou.util.LogFactory;
//
//import android.app.Activity;
//import android.content.Context;
//import android.location.Location;
//import android.location.LocationListener;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//
//public class DeviceLocationListener  implements LocationListener{
//
//	private static final CommonLog log = LogFactory.createLog();
//	
//	private Handler mHandler;
//	private IUpdateLocationRunable mRunnable;
//	private Activity mActivity;
//	
//
//	
//	public DeviceLocationListener(Activity activity, Handler handler, IUpdateLocationRunable runnable)
//	{
//		mHandler = handler;
//		mRunnable = runnable;
//		mActivity = activity;
//		
//
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
//	//		log.d("DeviceLocationListener  InnerThread   run...");
//			
//			LocationEx locationEx = (LocationEx) mLocation;
//			Location newLocation = WebManager.correctPosToMap(locationEx.getLatitude(), locationEx.getLongitude());
//			if (newLocation == null)
//			{
//				log.e("DeviceLocationListener newLocation = null");
//				return ;
//			}
//		
//			locationEx.setOffsetLonLat(newLocation.getLatitude(), newLocation.getLongitude());
//			
//			try {
//				//mRunnable.setAdress(WebManager.getAddressByGoogle(mLocation));
//				String adreString = WebManager.getAddressByGaoDe(mActivity, newLocation);
//				locationEx.setAdress(adreString);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				Log.e("", "device   getAddressByGaoDe catch exception");
//			}
//			
//	
//			mRunnable.setLocation(locationEx);
//			
//			
//			log.d("DeviceLocationListener mHandler.post(mRunnable);...");
//			mHandler.post(mRunnable);
//		}
//		
//		
//	}
//	
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
