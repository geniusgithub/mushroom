package com.mobile.yunyou.map.data;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.model.DeviceSetType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;

public class DeviceLocationManager implements IRequestCallback {

	private final static int CHECK_POSITION_INTERVAL = 60 * 1000;
	
	private static final CommonLog log = LogFactory.createLog();

	private YunyouApplication mApplication;
	
    private NetworkCenterEx mNetworkCenter;
	
	private Context mContext;
	
	private Timer mTimer;
	
	private MyTimeTask mTimeTask;

	private LocationListener mListener;
	
	private String requestDid = "";
	
	public DeviceLocationManager(Context context)
	{
		mContext = context;
		
		mTimer = new Timer();

		mNetworkCenter = NetworkCenterEx.getInstance();
		
		mApplication = YunyouApplication.getInstance();
	}
	

	public void registerListen(LocationListener listener)
	{
		
	
		if (mListener == null)
		{
			mListener = listener;
	
			startTimer(0);
		}

	
	}
	
	public void registerListen(LocationListener listener, int delay)
	{
		
	
		if (mListener == null)
		{
			mListener = listener;
	
			startTimer(delay);
		}

	
	}
	
	public void unRegisterListen()
	{
		stopTimer();
					
		mListener = null;		
	}
	
	public void requesetNow(){
		if (mApplication.isBindDevice())
		{
			log.e("requesetNow");
			requestDid = mApplication.getCurDid();
			mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_GET_LOCATION_MASID, null, DeviceLocationManager.this);
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
			// TODO Auto-generated method stub
			
			
			if (mApplication.isBindDevice())
			{
				requestDid = mApplication.getCurDid();
				mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_GET_LOCATION_MASID, null, DeviceLocationManager.this);
			}
			
			
		}
		
	}




	@Override
	public boolean onComplete(int requestAction, ResponseDataPacket dataPacket) {
		// TODO Auto-generated method stub



		String jsString = "null";
		if (dataPacket != null)
		{
			jsString = dataPacket.toString();
		}
		
		
//		log.d("requestAction = " + Utils.toHexString(requestAction) + "\nResponseDataPacket = \n" +jsString);
		
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
			
			if (mListener != null)
			{
			//	log.e("mListener != null  requestDid = " + requestDid);
			    LocationEx locationEx = new LocationEx(location);
			    locationEx.setUpdateTimeString(deviceLocation.mUploadTime);
			    locationEx.setCreateTimeString(deviceLocation.mCreateTime);
			    locationEx.setOnline(deviceLocation.mOnline);		    
			    locationEx.setDID(requestDid);
				mListener.onLocationChanged(locationEx);
			}else{
				log.e("mListener = null!!!");
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.e("anylize device location error!!!");
		}
	}
}
