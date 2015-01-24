package com.mobile.yunyou.map.data;

import android.location.Location;
import android.os.Handler;

import com.mobile.yunyou.map.util.WebManager;
import com.mobile.yunyou.model.DeviceSetType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;


public class DeviceAreaManager implements IRequestCallback{
	
	
	public static interface ICorrectLocation
	{
		public void isCorrestSuccess(boolean flag);
	}

	private static final CommonLog log = LogFactory.createLog();


	
	private DeviceSetType.DeviceAreaResult mAreaObject;
	
	private Handler mMainHandler;
	
	public static DeviceAreaManager mInstance;
	
	public static synchronized DeviceAreaManager getInstance(Handler mainHandler)
	{
		if (mInstance == null)
		{
			mInstance = new DeviceAreaManager(mainHandler);
		}
		
		return mInstance;
	}
	
	public static synchronized DeviceAreaManager getInstance()
	{
		return mInstance;
	}
	
	private DeviceAreaManager(Handler mainHandler)
	{
		mMainHandler = mainHandler;
	}
	
	public void setAreaObject(DeviceSetType.DeviceAreaResult object)
	{
		log.e("DeviceAreaManager setAreaObject = " + object);
			
		mAreaObject = object;
	}
	
	public DeviceSetType.DeviceAreaResult getAreaObject()
	{
		return mAreaObject;
	}
	
	public void syncSetAreaObject(final DeviceSetType.DeviceAreaResult object,final ICorrectLocation listener)
	{
	
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				boolean flag = false;
				Location location = WebManager.correctPosToMap(object.mLat, object.mLon);
				if (location != null)
				{
					object.mOffsetLat = location.getLatitude();
					object.mOffsetLon = location.getLongitude();
					log.d("get object.mLat = " + object.mLat + ", object.mLon = " + object.mLon);
					setAreaObject(object);
					flag = true;
				}else{
					setAreaObject(null);
				}
				
				if (listener != null)
				{
					listener.isCorrestSuccess(flag);
				}
			}
		});
		
		thread.start();
	}

	@Override
	public boolean onComplete(int requestAction, ResponseDataPacket dataPacket) {
		// TODO Auto-generated method stub
		return false;
	}
}
