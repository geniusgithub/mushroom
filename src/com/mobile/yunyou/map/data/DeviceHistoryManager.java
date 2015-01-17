//package com.mobile.yunyou.map.data;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import android.location.Location;
//import android.os.Handler;
//import android.os.Message;
//
//import com.a.a.a.d;
//import com.mobile.yunyou.map.MapABCActivity;
//import com.mobile.yunyou.map.data.BaseStationLocationManager.MyTimeTask;
//import com.mobile.yunyou.map.data.DeviceAreaManager.ICorrectLocation;
//import com.mobile.yunyou.map.util.CellIDInfo;
//import com.mobile.yunyou.map.util.CellIDInfoManager;
//import com.mobile.yunyou.map.util.WebManager;
//import com.mobile.yunyou.model.BaseType;
//import com.mobile.yunyou.model.DeviceSetType;
//import com.mobile.yunyou.model.DeviceSetType.GpsStillTime;
//import com.mobile.yunyou.util.CommonLog;
//import com.mobile.yunyou.util.LogFactory;
//import com.mobile.yunyou.util.Utils;
//import com.mobile.yunyou.util.YunTimeUtils;
//
//public class DeviceHistoryManager {
//
//	public static interface ICorrectLocation
//	{
//		public void isCorrestSuccess(boolean flag);
//	}
//
//	private static final CommonLog log = LogFactory.createLog();
//	private final static int MOVE_INTERVAL = 1000;
//	
//	private List<DeviceSetType.DeviceHistoryResult> mDeviceHistoryList;	
//	private Handler mMainHandler;
//	private Set<Integer> mStaticPointSet;
//	
//	public static DeviceHistoryManager mInstance;
//	
//	private Timer mTimer;
//	
//	private MyTimeTask mTimeTask;
//	
//	private List<GpsStillTime> mGpsTimeList;
//	private BaseType.Birthday mQueryData = new BaseType.Birthday();
//	
//	public static synchronized DeviceHistoryManager getInstance(Handler mainHandler)
//	{
//		if (mInstance == null)
//		{
//			mInstance = new DeviceHistoryManager(mainHandler);
//		}
//		
//		return mInstance;
//	}
//	
//	public static synchronized DeviceHistoryManager getInstance()
//	{
//		return mInstance;
//	}
//	
//	private DeviceHistoryManager(Handler mainHandler)
//	{
//		mMainHandler = mainHandler;
//		mStaticPointSet = new HashSet<Integer>();
//	}
//	
//	
//	public void startTimer(){
//		if (mTimeTask == null)
//		{
//			mTimeTask = new MyTimeTask();
//			mTimer.schedule(mTimeTask, 0, MOVE_INTERVAL);
//		}
//	}
//	
//	public void stopTimer(){
//		if (mTimeTask != null)
//		{
//			mTimeTask.cancel();
//			mTimeTask = null;
//		}
//	}
//	
//	
//	public void setLocationList(List<DeviceSetType.DeviceHistoryResult> list)
//	{			
//		mDeviceHistoryList = list;
//	}
//	
//	public List<DeviceSetType.DeviceHistoryResult> getLocationList()
//	{
//		return mDeviceHistoryList;
//	}
//	
//	public void setQueryDate(int year, int month, int day)
//	{
//		mQueryData.year = year;
//		mQueryData.month = month;
//		mQueryData.day = day;
//	}
//	
//	public BaseType.Birthday getQueryDate()
//	{
//		return mQueryData;
//	}
//	
//	public void setGpsStillTimeList(List<GpsStillTime> list)
//	{			
//		mGpsTimeList = list;
//	}
//	
//	public List<GpsStillTime> getGpsStillTimeList()
//	{
//		return mGpsTimeList;
//	}
//	
//	public void setStaticPointSet(Set<Integer> set){
//		mStaticPointSet = set;
//	}
//	
//	public Set<Integer> getStaticPointSet(){
//		return mStaticPointSet;
//	}
//	
//	
//	
//	
//	public void syncSetLocationList(final List<DeviceSetType.DeviceHistoryResult> list,final ICorrectLocation listener)
//	{
//	
//		Thread thread = new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				
//				int size = list.size();
//				List<BaseType.BaseLocation> locationsList = new ArrayList<BaseType.BaseLocation>();
//				for(int i = 0; i < size; i++)
//				{
//					BaseType.BaseLocation object = new BaseType.BaseLocation();
//					object.lat = list.get(i).mLat;
//					object.lon = list.get(i).mLon;
//					locationsList.add(object);
//				}
//				
//				boolean flag = false;
//				try {
//					
//					List<BaseType.BaseLocation> locationsListresult = WebManager.correctPosToMap(locationsList);
//					if (locationsListresult != null)
//					{
//						int size1 = locationsListresult.size();
//						for(int i = 0; i < size1; i++)
//						{
//							list.get(i).mOffsetLat = locationsListresult.get(i).lat;
//							list.get(i).mOffsetLon = locationsListresult.get(i).lon;
//							setLocationList(list);
//							flag = true;
//						}
//					}else{
//						setLocationList(null);
//					}
//					
//				} catch (Exception e) {
//					// TODO: handle exception
//					e.printStackTrace();
//				}
//				
//				if (listener != null)
//				{
//					listener.isCorrestSuccess(flag);
//				}
//			}
//		});
//		
//		thread.start();
//	}
//	
//
//	class MyTimeTask extends TimerTask
//	{
//
//		@Override
//		public void run() {
//			
//			Message msg = mMainHandler.obtainMessage(MapABCActivity.MAP_MESSAGE_REFRESH_MOVE_POINT);
//			msg.sendToTarget();
//		
//		}
//		
//	}
//	
//	
//	public static BaseType.RangeTime getRangeTime(DeviceSetType.GpsStillTime gpsStillTime, BaseType.Birthday data)
//	{
//		BaseType.RangeTime time = new BaseType.RangeTime();
//		
//		time.year = data.year;
//		time.month = data.month;
//		time.day = data.day;
//		
//		try {
//			time.startHour = gpsStillTime.getStartHour();
//			time.startMinute = gpsStillTime.getStartMinute();
//			time.endHour = gpsStillTime.getEndHour();
//			time.endMinute = gpsStillTime.getEndMinute();
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//			return null;
//		}
//		
//		return time;
//	}
//	
//}
