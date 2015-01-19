package com.mobile.yunyou.bike.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.amap.api.services.geocoder.GeocodeSearch;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.bike.manager.SelfLocationManager.ILocationUpdate;
import com.mobile.yunyou.map.data.LocationEx;
import com.mobile.yunyou.map.util.LocationUtil;
import com.mobile.yunyou.map.util.WebManager;
import com.mobile.yunyou.model.DeviceSetType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.Utils;

public class BikeLocationManager implements IRequestCallback {

	private final static int CHECK_POSITION_INTERVAL = 5 * 1000;
	
	private static final CommonLog log = LogFactory.createLog();
	
	public static interface IBikeLocationUpdate{
		public void onBikeLocationUpdate(LocationEx location);
	}


	public static BikeLocationManager mInstance;
	
	private YunyouApplication mApplication;
    private NetworkCenterEx mNetworkCenter;
	
	private Context mContext;
	private Timer mTimer;
	private MyTimeTask mTimeTask;
	
	private String requestDid = "";
	

	private GeocodeSearch mGeocoderSearch;
	private LocationEx mLocationEx;
	
	private List<IBikeLocationUpdate> mObserverList = new ArrayList<BikeLocationManager.IBikeLocationUpdate>();
	
	public static  BikeLocationManager getInstance()
	{
		if (mInstance == null)
		{
			mInstance = new BikeLocationManager(YunyouApplication.getInstance());
		}


		return mInstance;
	}
	
	private BikeLocationManager(Context context)
	{
		mContext = context;	
		mTimer = new Timer();

		mNetworkCenter = NetworkCenterEx.getInstance();	
		mApplication = YunyouApplication.getInstance();
		
		mGeocoderSearch = new GeocodeSearch(YunyouApplication.getInstance());
	}
	
	public LocationEx getLastLocation(){
		return mLocationEx;
	}

	public void startLocationCheck(){
		startTimer(0);
	}
	
	public void stopLocationCheck(){
		stopTimer();
	}
	

	public void addObserver(IBikeLocationUpdate object){
		synchronized (mObserverList) {
			mObserverList.add(object);
		}
	}
	
	public void removeObservser(IBikeLocationUpdate object){
		synchronized (mObserverList) {
			mObserverList.remove(object);
		}
	}
	
	public void requesetNow(){

		log.e("requesetNow");
		if (mApplication.isBindDevice())
		{
			requestDid = mApplication.getCurDid();
			mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_GET_LOCATION_MASID, null, BikeLocationManager.this);
		}
	}
	
	
	private void startTimer(int delay)
	{
		if (mTimeTask == null)
		{
			mTimeTask = new MyTimeTask();
			mTimer.schedule(mTimeTask, delay, CHECK_POSITION_INTERVAL);
		}
	}
	
	private void stopTimer()
	{
		if (mTimeTask != null)
		{
			mTimeTask.cancel();
			mTimeTask = null;
		}
	}
	
	
	
	
	class MyTimeTask extends TimerTask
	{

		@Override
		public void run() {			
			if (mApplication.isBindDevice())
			{
				requestDid = mApplication.getCurDid();
				mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_GET_LOCATION_MASID, null, BikeLocationManager.this);
			}
		}
		
	}




	@Override
	public boolean onComplete(int requestAction, ResponseDataPacket dataPacket) {

		String jsString = "null";
		if (dataPacket != null)
		{
			jsString = dataPacket.toString();
		}
		
		log.d("requestAction = " + Utils.toHexString(requestAction) + "\nResponseDataPacket = \n" +jsString);
		
		switch(requestAction)
		{
			case DeviceSetType.DEVICE_GET_LOCATION_MASID:
			{
				onLocationResult(dataPacket);
			}
			break;
		}
		
		
		return true;
	}
	
	private void onLocationResult(ResponseDataPacket datapacket)
	{
		if (datapacket == null || datapacket.rsp == 0)
		{
			log.e("can't get the device location!!!");
			
			return ;
		}

		
		DeviceSetType.DeviceLocation deviceLocation = new DeviceSetType.DeviceLocation();
		
		try {
			deviceLocation.parseString(datapacket.data.toString());
			double lat = Double.valueOf(deviceLocation.mLat);
			double lon = Double.valueOf(deviceLocation.mLon);
			String provider = deviceLocation.mType == 0 ? LocationManager.GPS_PROVIDER : LocationManager.NETWORK_PROVIDER;
			
			Location location = new Location(provider);
			location.setLatitude(lat);
			location.setLongitude(lon);

		    LocationEx locationEx = new LocationEx(location);
		    locationEx.setUpdateTimeString(deviceLocation.mUploadTime);
		    locationEx.setCreateTimeString(deviceLocation.mCreateTime);
		    locationEx.setOnline(deviceLocation.mOnline);		    
		    locationEx.setDID(requestDid);
		    locationEx.setPowerDletai(deviceLocation.mPowerDetail);
		    
		    InnerThread innerThread = new InnerThread(locationEx);
		    innerThread.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.e("anylize device location error!!!");
		}
	}
	
	class InnerThread extends Thread
	{
		private LocationEx mLocation;
		
		public InnerThread( LocationEx location)
		{
			mLocation = location;		
		}

		@Override
		public void run() {
			
			Location newLocation = WebManager.correctPosToMap(mLocation.getLatitude(), mLocation.getLongitude());
			if (newLocation == null)
			{
				log.e("correctPosToMap fail!!!");
				return ;
			}
			
			mLocation.setOffsetLonLat(newLocation.getLatitude(), newLocation.getLongitude());
			
			String address = WebManager.getAdressByGaodeEX( mGeocoderSearch, mLocation.getOffsetLat(), mLocation.getOffsetLon());
			mLocation.setAdress(address);

			mLocationEx = mLocation;
			doNotifyChange();
		}
		
		
	}
	
	
	private void doNotifyChange(){
		synchronized (mObserverList) {
			int size = mObserverList.size();
			for(int i = 0; i < size; i++){
				mObserverList.get(i).onBikeLocationUpdate(mLocationEx);
			}
		}
	}
}
