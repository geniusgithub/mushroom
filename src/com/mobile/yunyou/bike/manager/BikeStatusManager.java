package com.mobile.yunyou.bike.manager;

import java.util.Timer;
import java.util.TimerTask;

import com.mobile.yunyou.model.BikeType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.Utils;

public class BikeStatusManager implements IRequestCallback{
	
	public static interface IBikeResultCallback{
		public void onBikeStatus(boolean isResult);
	}

	private static final CommonLog log = LogFactory.createLog();
	private final static int TIME_INTERVAL =  5 * 1000;

	public static BikeStatusManager mInstance;
	private Timer mTimer;
	private MyTimeTask mTimeTask;
	
	private BikeType.BikeStatus mBikeStatus = new BikeType.BikeStatus();
	private NetworkCenterEx mNetworkCenterEx;
	
	private IBikeResultCallback mBikeResultCallBack;
	
	public static synchronized BikeStatusManager getInstance()
	{
		if (mInstance == null)
		{
			mInstance = new BikeStatusManager();
		}
		
		return mInstance;
	}
	
	
	private BikeStatusManager()
	{
		mTimer = new Timer();
		mNetworkCenterEx = NetworkCenterEx.getInstance();
	}
	
	public void addListener(IBikeResultCallback callback ){
		synchronized (this) {
			mBikeResultCallBack = callback;
		}
	}
	
	public void removeListener(){
		synchronized (this) {
			mBikeResultCallBack = null;
		}
	}
	
	public BikeType.BikeStatus getBikeStatus(){
		return mBikeStatus;
	}
	
	
	public void startTimer(){
		if (mTimeTask == null)
		{
			mTimeTask = new MyTimeTask();
			mTimer.schedule(mTimeTask, 0, TIME_INTERVAL);
		}
	}
	
	public void stopTimer(){
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
			
			log.e("StartRequestToServer BikeType.BIKE_STATUS_MASITD");
			mNetworkCenterEx.StartRequestToServer(BikeType.BIKE_STATUS_MASITD, null, BikeStatusManager.this);
			
		
		}
		
	}


	@Override
	public boolean onComplete(int requestAction, ResponseDataPacket dataPacket) {


		String jsString = "null";
		if (dataPacket != null)
		{
			jsString = dataPacket.toString();
		}
		
		
		log.e("requestAction = " + Utils.toHexString(requestAction) + "\nResponseDataPacket = \n" +jsString);
		 
		boolean ret = false;
		if (requestAction == BikeType.BIKE_STATUS_MASITD){
			BikeType.BikeStatus object = new BikeType.BikeStatus();
			try {
				ret = object.parseString(dataPacket.data.toString());
				if (ret){
					mBikeStatus = object;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		synchronized (this) {
			if (mBikeResultCallBack != null){
				mBikeResultCallBack.onBikeStatus(ret);
			}
		}
		
		return ret;
	}
	
}
