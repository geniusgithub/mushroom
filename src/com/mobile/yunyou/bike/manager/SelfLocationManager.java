package com.mobile.yunyou.bike.manager;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.amap.api.services.geocoder.GeocodeSearch;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.bike.tmp.SingleGPSManager;
import com.mobile.yunyou.map.data.LocationEx;
import com.mobile.yunyou.map.util.LocationUtil;
import com.mobile.yunyou.map.util.WebManager;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.YunTimeUtils;


public class SelfLocationManager implements LocationListener{
	
	private static final CommonLog log = LogFactory.createLog();
	
	public static interface ILocationUpdate{
		public void onLocationUpdate(LocationEx location);
	}
	
	private List<ILocationUpdate> mObserverList = new ArrayList<SelfLocationManager.ILocationUpdate>();

	public static SelfLocationManager mInstance;
	
	//private GPSLocationManager mGpsLocationManager;
	//private MobileStationManager mMobileStationManager;
	//private MobileNetworkManager mOriginStationManager;
	
	private GaoDeGPSManager mGaoDeGPSManager;
	private GaoDeNetworkManager mGaoDeNetworkManager;
	//private SingleGPSManager mSingleGPSManager;
	
	private GeocodeSearch mGeocoderSearch;
	private LocationEx mLocationEx;
	
	public static  SelfLocationManager getInstance()
	{
		if (mInstance == null)
		{
			mInstance = new SelfLocationManager();
		}


		return mInstance;
	}
	
	public LocationEx getLastLocation(){
		return mLocationEx;
	}
	private SelfLocationManager()
	{
	//	mGpsLocationManager = new GPSLocationManager(YunyouApplication.getInstance());
	//	mMobileStationManager = new MobileStationManager(YunyouApplication.getInstance());
	//	mOriginStationManager = new MobileNetworkManager(YunyouApplication.getInstance());
		
		mGaoDeGPSManager = new GaoDeGPSManager(YunyouApplication.getInstance());
		mGaoDeNetworkManager = new GaoDeNetworkManager(YunyouApplication.getInstance());
		mGeocoderSearch = new GeocodeSearch(YunyouApplication.getInstance());
		
//		mSingleGPSManager = new SingleGPSManager(YunyouApplication.getInstance());
	}
	
	public void startLocationCheck(){
	//	mGpsLocationManager.registerListen(this);
	//	mMobileStationManager.registerListen(this);
	//	mOriginStationManager.registerListen(this);
		mGaoDeGPSManager.registerListen(this);
		mGaoDeNetworkManager.registerListen(this);
		
//		mSingleGPSManager.registerListen(this);
	}
	
	public void stopLocationCheck(){
	//	mGpsLocationManager.unRegisterListen();
	//	mMobileStationManager.unRegisterListen();
	//	mOriginStationManager.unRegisterListen();
		mGaoDeGPSManager.unRegisterListen();
		mGaoDeNetworkManager.unRegisterListen();
		
//		mSingleGPSManager.unRegisterListen();
	}

	public void addObserver(ILocationUpdate object){
		synchronized (mObserverList) {
			mObserverList.add(object);
		}
	}
	
	public void removeObservser(ILocationUpdate object){
		synchronized (mObserverList) {
			mObserverList.remove(object);
		}
	}
	
//	@Override
//	public synchronized void onLocationChanged(Location location) {
//		if (location != null){
//			String timeString = YunTimeUtils.getFormatTime(location.getTime());
//			log.e("SelfLocationManager onLocationChanged:" +
//					"\nprovier = " + location.getProvider() + 
//					"\nAccuracy = " + location.getAccuracy() + 
//					"\ntime:" + timeString + 
//					"\nlatlon = (" + location.getLatitude() + "," + location.getLongitude() + ")");
//			
//	
//			LocationEx	locationEx = new LocationEx(location);
//			locationEx.setUpdateTimeString(YunTimeUtils.getFormatTime(System.currentTimeMillis()));
//			
//			//Location newLocation = null;
//			Location newLocation = WebManager.correctPosToMap(locationEx.getLatitude(), locationEx.getLongitude());
//			if (newLocation == null)
//			{
//				log.e("correctPosToMap fail!!!");
//				return ;
//			}
//			
//			locationEx.setOffsetLonLat(newLocation.getLatitude(), newLocation.getLongitude());
//	
//			
//			String address = WebManager.getAdressByGaodeEX( mGeocoderSearch, locationEx.getOffsetLat(), locationEx.getOffsetLon());
//			locationEx.setAdress(address);
//			
//			if (LocationUtil.isBetterLocation(locationEx, mLocationEx)){
//				log.e("isBetterLocation!!!");
//				mLocationEx = locationEx;
//				doNotifyChange();
//			}
//		
//		}
//	}
	
	@Override
	public synchronized void onLocationChanged(Location location) {
		if (location != null){
			String timeString = YunTimeUtils.getFormatTime(location.getTime());
			log.e("-->SelfLocationManager onLocationChanged:" +
					"\nprovier = " + location.getProvider() + 
					"\nAccuracy = " + location.getAccuracy() + 
					"\ntime:" + timeString + 
					"\nlatlon = (" + location.getLatitude() + "," + location.getLongitude() + ")");
			
	
			LocationEx	locationEx = new LocationEx(location);
			locationEx.setUpdateTimeString(YunTimeUtils.getFormatTime(System.currentTimeMillis()));
			
			
//			if (location.getProvider().equalsIgnoreCase(LocationManager.GPS_PROVIDER)){
//				Location newLocation = WebManager.correctPosToMap(location.getLatitude(), location.getLongitude());
//				if (newLocation == null)
//				{
//					log.e("correctPosToMap fail!!!");
//					return ;
//				}
//
//				locationEx.setOffsetLonLat(newLocation.getLatitude(), newLocation.getLongitude());
//			}else{
//				locationEx.setOffsetLonLat(location.getLatitude(), location.getLongitude());
//			}
			locationEx.setOffsetLonLat(location.getLatitude(), location.getLongitude());
			//String address = WebManager.getAdressByGaodeEX( mGeocoderSearch, locationEx.getOffsetLat(), locationEx.getOffsetLon());
			String address = "";
			locationEx.setAdress(address);
			if (LocationUtil.isBetterLocation(locationEx, mLocationEx)){
				log.e("isBetterLocation!!!");
				mLocationEx = locationEx;
				doNotifyChange();
			}else{
				log.e("no BetterLocation!!!");
			}
		}
	}

	private void doNotifyChange(){
		synchronized (mObserverList) {
			int size = mObserverList.size();
			for(int i = 0; i < size; i++){
				mObserverList.get(i).onLocationUpdate(mLocationEx);
			}
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
