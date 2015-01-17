package com.mobile.yunyou.network;

import java.util.Timer;
import java.util.TimerTask;
import android.content.Context;

import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.model.PublicType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;

public class HeartBeatManager implements IRequestCallback{

	private final static int SEND_HEARTBEAT_INTERVAL = 60 * 1000;
	
	private static final CommonLog log = LogFactory.createLog();
	
	private static HeartBeatManager mInstance;
	
    private NetworkCenterEx mNetworkCenterEx;
	
	private Context mContext;
	
	private Timer mTimer;
	
	private MyTimeTask mTimeTask;

	public static synchronized HeartBeatManager getInstance()
	{
		if (mInstance == null)
		{
			mInstance = new HeartBeatManager(YunyouApplication.getInstance());
			
		}
		
		return mInstance;
	}
	
	private  HeartBeatManager(Context context)
	{
		mContext = context;
		
		mTimer = new Timer();

		mNetworkCenterEx = NetworkCenterEx.getInstance();

	}
	
	
	
	public void startHeartBeat()
	{
		startTimer();
	}
	
	public void stopHeartBeat()
	{
		stopTimer();
	}
	
	
	private void startTimer()
	{
		if (mTimeTask == null)
		{
			mTimeTask = new MyTimeTask();
			mTimer.schedule(mTimeTask, 0, SEND_HEARTBEAT_INTERVAL);
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

		mNetworkCenterEx.StartRequestToServer(PublicType.USER_HEART_MASID, null, HeartBeatManager.this);

			
		}
		
	}




	@Override
	public boolean onComplete(int requestAction, ResponseDataPacket dataPacket) {
		// TODO Auto-generated method stub



		String jsString = "null";
		if (dataPacket == null)
		{
			log.e("heartbeat fail!!!");
			return false;
		}
		
		
		switch(requestAction)
		{
			case PublicType.USER_HEART_MASID:
			{
				if (dataPacket.rsp == 1)
				{
					log.e("heartbeat success!!!");
				}else{
					log.e("heartbeat fail!!!");
				}
			}
			break;
		}
		
		
		return true;
	}
	

}
