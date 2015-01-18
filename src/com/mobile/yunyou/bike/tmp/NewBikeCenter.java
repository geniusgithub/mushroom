package com.mobile.yunyou.bike.tmp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.model.LatLng;
import com.mobile.yunyou.bike.MapUtils;
import com.mobile.yunyou.bike.tmp.SingleGPSManager.MyTimeTask;
import com.mobile.yunyou.map.data.LocationEx;
import com.mobile.yunyou.map.util.LocationUtil;
import com.mobile.yunyou.map.util.WebManager;
import com.mobile.yunyou.model.BikeType;
import com.mobile.yunyou.model.BikeType.MinLRunRecord;
import com.mobile.yunyou.model.BikeType.MinRunRecord;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.YunTimeUtils;

public class NewBikeCenter implements LocationListener{

	private static final CommonLog log = LogFactory.createLog();
	
	public static interface IStatusCallBack{
		public void onTimeChange(int timeMillson);
		public void onStatusChange(NewBikeEntiy entiy);
		public void onLatlngUpdate(LatLng latLng);
	}
	
	
	public static interface IRunStatus{
		int STOP = 0;
		int RUNNING = 1;
		int PAUSE = 2;
	}
	private final static int CHECK_POSITION_INTERVAL = 5 * 1000;
	private final static int MSG_UPDATE_LOCATION = 0x0001;
	private final static int MSG_REFRESH_TIME = 0x0002;
	
	public IStatusCallBack mStatusCallBack;
	
	private int mRunStatus = IRunStatus.STOP;

	private List<LatLng> mlLatLngLists  = new ArrayList<LatLng>();
	private LatLng mLastLatLng = null;
	private int mTimeMilllons = 0;
	
	private long mStartTimeMilllons = 0;
	private long mEndTimeMilllons = 0;
	
	private int mTotalDistance = 0;
	private double mCurSpeed = 0;
	private double mHSpeed = 0;
	private double mAverage = 0;
	private int mCal = 0;
	private int mHeight = 0;
	
	private Handler mHandler;
	public SingleGPSManager mGpsManager;
	private Context mContext;
	
	private Timer mTimer;
	private MyTimeTask mTimeTask;
	
	public NewBikeCenter(Context context){
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				switch(msg.what){
					case MSG_UPDATE_LOCATION:
						if (msg.obj == null){
							addLocation(null);
						}else{
							addLocation((LocationEx)msg.obj);
						}
						break;
					case MSG_REFRESH_TIME:
						mTimeMilllons += 1000;
						if (mStatusCallBack != null){
							mStatusCallBack.onTimeChange(mTimeMilllons);
						}
						mHandler.sendEmptyMessageDelayed(MSG_REFRESH_TIME, 1000);
						break;
				}
			}
			
		};
		mContext = context;
		mGpsManager = new SingleGPSManager(context);
		mTimer = new Timer();	
	}
	
	public int getRunStatus(){
		return mRunStatus;
	}
	
	public void setCallback(IStatusCallBack callback){
		mStatusCallBack = callback;
	}
	
	public void clear(){
		mTotalDistance = 0;
		mCurSpeed = 0;
		mHSpeed = 0;
		mAverage = 0;
		mCal = 0;
		mTimeMilllons = 0; 
		mStartTimeMilllons = 0;
		mEndTimeMilllons = 0;
		mLastLatLng = null;
		mlLatLngLists.clear();
	}
	
	public void startRunning(){
		switch (mRunStatus) {
			case IRunStatus.STOP:
				start();
				break;
			case IRunStatus.RUNNING:
				return;
			case IRunStatus.PAUSE:
				restart();
				break;
		default:
			break;
		}
	}
	
	public void pauseRunning(){
		switch (mRunStatus) {
			case IRunStatus.STOP:
			case IRunStatus.PAUSE:
				return ;
			case IRunStatus.RUNNING:
				pause();
				break;
			default:
				break;
		}
	}
	
	public void stopRunning(){
		switch (mRunStatus) {
			case IRunStatus.STOP:
				return ;
			case IRunStatus.RUNNING:
			case IRunStatus.PAUSE:
				stop();
				break;
			default:
				break;
		}
	}
	
	
	private void start(){
		log.e("start");
		clear();
		mTimeMilllons = 0;
		mStartTimeMilllons = System.currentTimeMillis();
		mGpsManager.registerListen(this);
		mHandler.removeMessages(MSG_REFRESH_TIME);
		mHandler.sendEmptyMessageDelayed(MSG_REFRESH_TIME, 1000);
		mRunStatus = IRunStatus.RUNNING;
	}
	private void restart(){
		mGpsManager.registerListen(this);
		mHandler.removeMessages(MSG_REFRESH_TIME);
		mHandler.sendEmptyMessageDelayed(MSG_REFRESH_TIME, 1000);
		mRunStatus = IRunStatus.RUNNING;
	}
	private void pause(){
		log.e("pause");
		mGpsManager.unRegisterListen();
		mLastLatLng = null;
		mHandler.removeMessages(MSG_REFRESH_TIME);
		mRunStatus = IRunStatus.PAUSE;
	}
	private void stop(){
		log.e("stop");
		mEndTimeMilllons = System.currentTimeMillis();
		mGpsManager.unRegisterListen();
		mHandler.removeMessages(MSG_REFRESH_TIME);
		mRunStatus = IRunStatus.STOP;
	}
	
	private NewBikeEntiy getEntiy(){
		NewBikeEntiy entiy = new NewBikeEntiy();
//		private int mTimeMilllons = 0;
//		
//		private int mTotalDistance = 0;
//		private double mSpeed = 0;
//		private double mHSpeed = 0;
//		private double mLSpeed = 0;
//		private int mCal = 0;
		
		entiy.mTimeMilllons = mTimeMilllons;
		entiy.mTotalDistance = mTotalDistance;
		entiy.mCurSpeed = mCurSpeed;
		entiy.mHSpeed = mHSpeed;
		entiy.mAverageSpeed = mAverage;
		entiy.mCal = mCal;
		entiy.mHeight = mHeight;
		
		return entiy;
	}
	
	public BikeType.BikeRecordUpload newBikeRecord(){
		
		BikeType.BikeRecordUpload group = new BikeType.BikeRecordUpload();
		group.mStartTime = YunTimeUtils.getFormatTime(mStartTimeMilllons);
		group.mEndTime = YunTimeUtils.getFormatTime(mEndTimeMilllons);
		group.mTotalDistance =  mTotalDistance / 1000.0;
		group.mCal = 0;
		group.mHSpeed = mHSpeed * 3.6;
		group.mLSpeed = 0;
		group.mHeight = 0;
		
		 LinkedList<MinLRunRecord> list = new LinkedList<MinLRunRecord>();
		 for(LatLng object : mlLatLngLists){
			 MinLRunRecord record = new MinLRunRecord();
			 record.mLat = object.latitude;
			 record.mLon = object.longitude;
			 record.mType = 1;
			 record.mHeight = 0;
			 record.mCreateTime = "";
			 list.add(record);
		 }
		 group.mBikeRecordList = list;
		
		
		return group;
	}
//	public BikeType.RunRecordGroup newRunRecord(){
//		 BikeType.RunRecordGroup group = new BikeType.RunRecordGroup();
//		 group.mTimeMillsion = mTimeMilllons;
//		 group.mTotalDistance = mTotalDistance;
//		 group.mHSpeed = mHSpeed;
//		 group.mAverageSpeed = mAverage;
//		 group.mCal = mCal;
//		 group.mStartTime = YunTimeUtils.getFormatTime(mStartTimeMilllons);
//		 group.mEndTime = YunTimeUtils.getFormatTime(mEndTimeMilllons);
//		 LinkedList<MinRunRecord> list = new LinkedList<MinRunRecord>();
//		 for(LatLng object : mlLatLngLists){
//			 MinRunRecord record = new MinRunRecord();
//			 record.mLat = object.latitude;
//			 record.mLon = object.longitude;
//			 record.mCreateTime = "";
//			 list.add(record);
//		 }
//		 group.mRunRecordList = list;
//		 
//		 return group;
//	}
	
	public void addLocation(LocationEx location){
		if (location != null){
			log.e("NewBikeCenter  addLocation(" + location.getOffsetLat() + "," + location.getOffsetLon());
		}else{
			log.e("NewBikeCenter  addLocation = null");
		}
		
		if (location != null){
			mHeight = (int) location.getAltitude();
		}

		if (mLastLatLng == null){
			if (location != null){
				mLastLatLng = new LatLng(location.getOffsetLat(), location.getOffsetLon());
				mlLatLngLists.add(mLastLatLng);
				
				if (mStatusCallBack != null){
					mStatusCallBack.onStatusChange(getEntiy());
					mStatusCallBack.onLatlngUpdate(mLastLatLng);
				}
			}

			return ;
		}
		
		double distance = 0;
		LatLng latLng = null;
		if (location == null){
			mCurSpeed = 0;
			mAverage = mTotalDistance / mTimeMilllons * 1000;
		}else{

			latLng = new LatLng(location.getOffsetLat(), location.getOffsetLon());	
		
			distance = MapUtils.getDistanByLatlon(mLastLatLng, latLng);
			log.e("distance = " + distance);
			mTotalDistance += distance;
			mCurSpeed  = distance / CHECK_POSITION_INTERVAL * 1000;
			if (mCurSpeed > mHSpeed){
				mHSpeed = mCurSpeed;
			}
			
			mAverage = mTotalDistance * 1.0 / mTimeMilllons * 1000;
			log.e("mTotalDistance = " + mTotalDistance + "\n curspeed = " + mCurSpeed +  
					"\n hspeed = " + mHSpeed + "\n averagespeed = " + mAverage + 
					"\nHeight = " + mHeight + 
					"\n mlLatLngLists.size = " + mlLatLngLists.size());
		}
		

		
		if (distance > 0.1){
			mLastLatLng = latLng;
			mlLatLngLists.add(mLastLatLng);
		}
		

		
		if (mStatusCallBack != null){
			mStatusCallBack.onStatusChange(getEntiy());
			if (distance > 0.1){
				mStatusCallBack.onLatlngUpdate(mLastLatLng);
			}
		}
		
	}
	

	@Override
	public void onLocationChanged(Location location) {
		Message msg = mHandler.obtainMessage(MSG_UPDATE_LOCATION);	
		if (location == null ){
			msg.sendToTarget();
			return ;
		}
		
		LocationEx	locationEx = new LocationEx(location);
		String timeString = YunTimeUtils.getFormatTime(location.getTime());
		log.e("NewBikeCenter onLocationChanged:" +
				"\nprovier = " + location.getProvider() + 
				"\nAccuracy = " + location.getAccuracy() + 
				"\ntime:" + timeString + 
				"\nlatlon = (" + location.getLatitude() + "," + location.getLongitude() + ")");
//		if (location.getProvider().equalsIgnoreCase(LocationProviderProxy.AMapNetwork)){
//			locationEx.setOffsetLonLat(location.getLatitude(), location.getLongitude());
//			msg.obj = locationEx;
//			msg.sendToTarget();
//			return ;
//		}

//		Location newLocation = WebManager.correctPosToMap(locationEx.getLatitude(), locationEx.getLongitude());
//		if (newLocation == null)
//		{
//			log.e("correctPosToMap fail!!!");
//			return ;
//		}
		
		locationEx.setOffsetLonLat(locationEx.getLatitude(), locationEx.getLongitude());
		msg.obj = locationEx;
		msg.sendToTarget();
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
