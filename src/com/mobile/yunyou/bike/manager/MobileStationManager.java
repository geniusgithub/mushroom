package com.mobile.yunyou.bike.manager;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;

import com.mobile.yunyou.map.util.CellIDInfo;
import com.mobile.yunyou.map.util.CellIDInfoManager;
import com.mobile.yunyou.map.util.WebManager;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;

public class MobileStationManager {

	private final static int CHECK_POSITION_INTERVAL = 30 * 1000;
	
	private static final CommonLog log = LogFactory.createLog();
		private Context mContext;
		
		private Timer mTimer;
		
		private MyTimeTask mTimeTask;

		private LocationListener mListener;
		
		public MobileStationManager(Context context)
		{
			mContext = context;
			
			mTimer = new Timer();		
		}
		

		public void registerListen(LocationListener listener)
		{
			if (mListener == null)
			{
				mListener = listener;
			
				startTimer();
			}
	
		}
		
		public void unRegisterListen()
		{
			stopTimer();
						
			mListener = null;		
		}
		
		
		
		
		private void startTimer( )
		{

			if (mTimeTask == null)
			{
				mTimeTask = new MyTimeTask();
				mTimer.scheduleAtFixedRate(mTimeTask, 1000, CHECK_POSITION_INTERVAL);
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
				

				
				List<CellIDInfo> list = null;
				Location location = null;
				//LocationEx locationEx = null;
				
				try {
					list = CellIDInfoManager.getCellIDInfo(mContext, 0);

				//	location = WebManager.callGear(list);
					location = WebManager.callGearByYunyou(list);
				} catch (Exception e) {
				
					log.e("MobileStationManager catch Exceptio = " + e.getMessage());
					e.printStackTrace();
				}
				
				
				
//				if (location != null)
//				{
//					locationEx = new LocationEx(location);
//					locationEx.setUpdateTimeString(YunTimeUtils.getFormatTime(System.currentTimeMillis()));
//				}

				
				if (mListener != null)
				{
					mListener.onLocationChanged(location);
				}
			}
			
		}
}
