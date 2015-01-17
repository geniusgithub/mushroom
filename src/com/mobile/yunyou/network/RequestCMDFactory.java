package com.mobile.yunyou.network;

import java.util.HashMap;
import java.util.Map;

import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.activity.OnlineStatusManager;
import com.mobile.yunyou.model.BikeType;
import com.mobile.yunyou.model.DeviceSetType;
import com.mobile.yunyou.model.ProductType;
import com.mobile.yunyou.model.PublicType;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;

public class RequestCMDFactory {

	private static final CommonLog log = LogFactory.createLog();

	private static RequestCMDFactory mInstance;
	
	public synchronized static RequestCMDFactory getInstance()
	{
		if (mInstance == null)
		{
			mInstance = new RequestCMDFactory();
		}
		
		return mInstance;
	}
	
	public RequestCMDFactory()
	{
		init();
	}
	

	private  Map<Integer, String> mRequestCMDMap = new HashMap<Integer, String>();
	
	
	public void init()
	{
		mRequestCMDMap.put(PublicType.USER_LOGIN_MASID, "user_login");
		mRequestCMDMap.put(PublicType.USER_REGISTER_MASID, "user_register");
		mRequestCMDMap.put(PublicType.USER_CHANGE_PASSWORD_MASID, "user_changepassword");
		mRequestCMDMap.put(PublicType.USER_CHANGE_INFO_MASID, "user_changeinfo");
		mRequestCMDMap.put(PublicType.USER_GET_INFO_MASID, "user_info");
		mRequestCMDMap.put(PublicType.USER_LOGOUIT_MASID, "user_logout");
		mRequestCMDMap.put(PublicType.USER_HEART_MASID, "user_heartbeat");
		mRequestCMDMap.put(PublicType.USER_BIND_MASID, "user_bind");
		mRequestCMDMap.put(PublicType.USER_UNBIND_MASID, "user_unbind");
		mRequestCMDMap.put(PublicType.USER_UPLOAD_GPS_MASID, "user_uploadgps");
		
		mRequestCMDMap.put(DeviceSetType.DEVICE_KEYSET_MASID, "deviceset_key");
		mRequestCMDMap.put(DeviceSetType.DEVICE_GET_KEYSET_MASID, "deviceget_key");
		mRequestCMDMap.put(DeviceSetType.DEVICE_CALL_TIME_MASID, "deviceset_calltime");
		mRequestCMDMap.put(DeviceSetType.DEVICE_GET_CALL_TIME_MASID, "deviceget_calltime");
		mRequestCMDMap.put(DeviceSetType.DEVICE_WHITELIST_SET_MASID, "deviceset_whitelist");
		mRequestCMDMap.put(DeviceSetType.DEVICE_GET_WHITELIST_MASID, "deviceget_whitelist");
		mRequestCMDMap.put(DeviceSetType.DEVICE_CLOCK_SET_MASID, "deviceset_clock");
		mRequestCMDMap.put(DeviceSetType.DEVICE_GET_CLOCK_MASID, "deviceget_clock");
		mRequestCMDMap.put(DeviceSetType.DEVICE_SCENEMODE_SET_MASID, "deviceset_scenemode");
		mRequestCMDMap.put(DeviceSetType.DEVICE_GET_SCENEMODE_MASID, "deviceget_scenemode");
		mRequestCMDMap.put(DeviceSetType.DEVICE_POWER_SET_MASID, "deviceset_power");
		mRequestCMDMap.put(DeviceSetType.DEVICE_GET_POWER_MASID, "deviceget_power");
		mRequestCMDMap.put(DeviceSetType.DEVICE_LOWPOWER_WARN_SET_MASID, "deviceset_lowpower");
		mRequestCMDMap.put(DeviceSetType.DEVICE_GET_LOWPOWER_WARN_MASID, "deviceget_lowpower");
		mRequestCMDMap.put(DeviceSetType.DEVICE_GPS_INTERVAL_SET_MASID, "deviceset_gpsuploadinterval");
		mRequestCMDMap.put(DeviceSetType.DEVICE_GET_GPS_INTERVAL_MASID, "deviceget_gpsuploadinterval");
		mRequestCMDMap.put(DeviceSetType.DEVICE_GPS_STILLTIME_SET_MASID, "deviceset_gpsuploadtimetable");
		mRequestCMDMap.put(DeviceSetType.DEVICE_GET_GPS_STILLTIME_MASID, "deviceget_gpsuploadtimetable");
		mRequestCMDMap.put(DeviceSetType.DEVICE_PHONE_CALL_MODE_SET_MASID, "deviceset_callmodel");
		mRequestCMDMap.put(DeviceSetType.DEVICE_GET_PHONE_CALL_MODE_MASID, "deviceget_callmodel");
		mRequestCMDMap.put(DeviceSetType.DEVICE_REMOTE_MONITOR_MASID, "deviceset_monitor");
		mRequestCMDMap.put(DeviceSetType.DEVICE_GET_LOCATION_MASID, "locationget_instant");
		mRequestCMDMap.put(DeviceSetType.DEVICE_GET_ONELINE_MASID, "deviceget_online");
		mRequestCMDMap.put(DeviceSetType.DEVICE_GET_MSG_MASID, "user_getalertmessage");
		mRequestCMDMap.put(DeviceSetType.DEVICE_GET_UNREAD_MSG_MASID, "user_getunreadalertmessage");
		mRequestCMDMap.put(DeviceSetType.DEVICE_DEL_MSG_MASID, "user_delalertmessage");		
		mRequestCMDMap.put(DeviceSetType.DEVICE_GET_AREA_MASID, "deviceget_area");		
		mRequestCMDMap.put(DeviceSetType.DEVICE_GET_DEVICE_HISTORY_MASID, "locationget_history");	
		mRequestCMDMap.put(DeviceSetType.DEVICE_GET_SLEEPTIME_MASID, "deviceget_sleeptimetable");	
		mRequestCMDMap.put(DeviceSetType.DEVICE_SET_SLEEPTIME_MASID, "deviceset_sleeptimetable");	
		
		mRequestCMDMap.put(ProductType.PRODUCT_GET_PACKET_MASID, "terminalmanage_getpackages");	
		mRequestCMDMap.put(ProductType.PRODUCT_CREATE_ORDER_MASID, "terminalmanage_createorder");	
		mRequestCMDMap.put(ProductType.PRODUCT_BUY_NOTIFY_MASID, "terminalmanage_buynotify");
		mRequestCMDMap.put(ProductType.PRODUCT_BUY_RETURN_MASID, "terminalmanage_buyreturn");
		mRequestCMDMap.put(ProductType.PRODUCT_GET_PACKAGE_HISTORY_MASID, "terminalmanage_packageshistory");
		
		
		mRequestCMDMap.put(BikeType.BIKE_STAR_MASITD, "bike_start");
		mRequestCMDMap.put(BikeType.BIKE_PAUSE_MASITD, "bike_pause");
		mRequestCMDMap.put(BikeType.BIKE_STOP_MASITD, "bike_stop");
		mRequestCMDMap.put(BikeType.BIKE_STATUS_MASITD, "bike_status");
		mRequestCMDMap.put(BikeType.BIKE_RECORD_MASITD, "bike_record");
		mRequestCMDMap.put(BikeType.BIKE_RECORDSUB_MASITD, "bike_recordsub");
		mRequestCMDMap.put(BikeType.BIKE_SETAREA_MASITD, "bike_setarea");
		mRequestCMDMap.put(BikeType.BIKE_GETAREA_MASITD, "bike_getarea");
		mRequestCMDMap.put(BikeType.BIKE_DELAREA_MASITD, "bike_delarea");
		mRequestCMDMap.put(BikeType.BIKE_GETALERTWAY, "bike_getalertway");
		mRequestCMDMap.put(BikeType.BIKE_SETALERTWAY, "bike_setalertway");
	}
	
	public String getCMDString(int cmd)
	{

		return mRequestCMDMap.get(cmd);
	}
	
}
