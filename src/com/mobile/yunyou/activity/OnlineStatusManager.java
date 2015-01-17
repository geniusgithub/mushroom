package com.mobile.yunyou.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;

import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.model.DeviceSetType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.Utils;

public class OnlineStatusManager implements IRequestCallback{

	private final static int REQUEST_STATUS_INTERVAL = 60 * 1000;
	
	private static final CommonLog log = LogFactory.createLog();
	
	private YunyouApplication mApplication;
	
    private NetworkCenterEx mNetworkCenter;
    
    private static OnlineStatusManager mInstance;
	
	private Context mContext;
	
	private Timer mTimer;
	
	private MyTimeTask mTimeTask;
	
	private String requestDid = "";

	public synchronized static OnlineStatusManager getInstance()
	{
		if (mInstance == null)
		{
			mInstance = new OnlineStatusManager(YunyouApplication.getInstance());
		}
		
		return mInstance;
	}
	
	private OnlineStatusManager(Context context)
	{
		mContext = context;
		
		mTimer = new Timer();

		mNetworkCenter = NetworkCenterEx.getInstance();
		
		mApplication = YunyouApplication.getInstance();
		
	}
	

	public void startRequest()
	{	
		startTimer(0);
	}
	
	public void stopRequest()
	{
		stopTimer();	
	}
	
	
	private void startTimer(int delay)
	{
		if (mTimeTask == null)
		{
			mTimeTask = new MyTimeTask();
			mTimer.schedule(mTimeTask, delay, REQUEST_STATUS_INTERVAL);
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
				mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_GET_ONELINE_MASID, null, OnlineStatusManager.this);
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
			case DeviceSetType.DEVICE_GET_ONELINE_MASID:
			{
				if (dataPacket == null || dataPacket.rsp == 0)
				{
					log.e("can't get the online status!!!");
					
					return false;
				}			
				
				DeviceSetType.DeviceOnlineStatus status = new DeviceSetType.DeviceOnlineStatus();
				try {
					status.parseString(dataPacket.data.toString());
					log.e("DEVICE_GET_ONELINE_MASID success... online = " + status.mOnline);
					mApplication.changeStatusLine(requestDid, status.mOnline);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			break;
		}
		
		
		return true;
	}
	
	
	


}
