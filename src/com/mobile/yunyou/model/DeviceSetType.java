package com.mobile.yunyou.model;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mobile.yunyou.bike.NewBikeActivity;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;

import android.R.integer;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


// 0x0100 - 0x0199
public class DeviceSetType {

	private static final CommonLog log = LogFactory.createLog();
	
	public final static String KEY_ARRAY = "array";
	
	// 按键设置
	public final static int DEVICE_KEYSET_MASID = 0x0100;
	public static class KeySet implements Parcelable, IParseString, IToJsonObject
	{
		public final static String KEY_KEY = "key";
		public final static String KEY_PHONENUMBER = "phoneNumber";
		public final static String KEY_NAME = "name";
		
		public final static int FIELD_COUNT = 3;
		public String mKey = "";
		public String mPhoneNumber = "";
		public String mName = "";

		@Override
		public boolean parseString(String jsonString) throws Exception {
			// TODO Auto-generated method stub
			
			JSONObject jsonObject = new JSONObject(jsonString);
			mKey = jsonObject.getString(KEY_KEY);
			mPhoneNumber = jsonObject.getString(KEY_PHONENUMBER);
			mName = jsonObject.getString(KEY_NAME);
			return true;
		}

		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			// TODO Auto-generated method stub
			Bundle mBundle=new Bundle(); 
			
			mBundle.putString(KEY_KEY, mKey);
			mBundle.putString(KEY_PHONENUMBER, mPhoneNumber);
			mBundle.putString(KEY_NAME, mName);
		
			dest.writeBundle(mBundle);
		}
		
		public static final Parcelable.Creator<KeySet> CREATOR = new Parcelable.Creator<KeySet>()
		 {

			@Override
			public KeySet createFromParcel(Parcel source) {
				// TODO Auto-generated method stub
				KeySet Data = new KeySet();

				Bundle mBundle=new Bundle(); 
				mBundle = source.readBundle();
				Data.mKey = mBundle.getString(KEY_KEY);
				Data.mPhoneNumber = mBundle.getString(KEY_PHONENUMBER);
				Data.mName = mBundle.getString(KEY_NAME);
		
				
				return Data;
			}

			@Override
			public KeySet[] newArray(int size) {
				// TODO Auto-generated method stub
				return new KeySet[size];
			}
			 
		 };

		@Override
		public JSONObject toJsonObject() throws JSONException {
			// TODO Auto-generated method stub

			JSONObject jsonObject = new JSONObject();
			
			jsonObject.put(KEY_KEY, mKey);
			jsonObject.put(KEY_PHONENUMBER, mPhoneNumber);
			jsonObject.put(KEY_NAME, mName);
			
			return jsonObject;
		}

	}
	
	public static class KeySetGroup implements IParseString, IToJsonObject
	{
		public List<KeySet> mKeySetList = new ArrayList<KeySet>();

		@Override
		public boolean parseString(String jsonString) throws Exception {
			// TODO Auto-generated method stub
			
			JSONObject jsonObject = new JSONObject(jsonString);			
			JSONArray jsonArray = jsonObject.getJSONArray(KEY_ARRAY);
			int size = jsonArray.length();
			for(int i = 0; i < size; i++)
			{
				KeySet object = new KeySet();
				
				try {
					object.parseString(jsonArray.getJSONObject(i).toString());
					mKeySetList.add(object);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

			
			return true;
		}

		@Override
		public JSONObject toJsonObject() throws JSONException {
			// TODO Auto-generated method stub

			JSONObject jsonObject = new JSONObject();
			
			JSONArray jsonArray = new JSONArray();
			int size = mKeySetList.size();
			for(int i = 0; i < size; i++)
			{
				jsonArray.put(mKeySetList.get(i).toJsonObject());
			}
			jsonObject.put(KEY_ARRAY, jsonArray);
			
			return jsonObject;
		}

	
	}
	public final static int DEVICE_GET_KEYSET_MASID = 0x0101;
	
	

	// 通话时长设置
	public final static int DEVICE_CALL_TIME_MASID = 0x0102;
	public static class CallTime implements IParseString, Parcelable, IToJsonObject
	{
		public final static String KEY_TIME = "value";
		
		public int mTime;

		@Override
		public boolean parseString(String jsonString) throws Exception {
			// TODO Auto-generated method stub

			JSONObject jsonObject = new JSONObject(jsonString);	
			mTime = jsonObject.getInt(KEY_TIME);
			
			return true;
		}

		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			// TODO Auto-generated method stub
			Bundle mBundle=new Bundle(); 
			
			mBundle.putInt(KEY_TIME, mTime);

		
			dest.writeBundle(mBundle);
		} 
		
		public static final Parcelable.Creator<CallTime> CREATOR = new Parcelable.Creator<CallTime>()
		 {

			@Override
			public CallTime createFromParcel(Parcel source) {
				// TODO Auto-generated method stub
				CallTime Data = new CallTime();

				Bundle mBundle=new Bundle(); 
				mBundle = source.readBundle();
				Data.mTime = mBundle.getInt(KEY_TIME);
		
				
				return Data;
			}

			@Override
			public CallTime[] newArray(int size) {
				// TODO Auto-generated method stub
				return new CallTime[size];
			}
			 
		 };

		@Override
		public JSONObject toJsonObject() throws JSONException {
			// TODO Auto-generated method stub
			JSONObject jsonObject = new JSONObject();
			
			jsonObject.put(KEY_TIME, mTime);
			
			return jsonObject;
		}
	}
	public final static int DEVICE_GET_CALL_TIME_MASID = 0x0103;
	
	
	
	// 白名单设置
	public final static int DEVICE_WHITELIST_SET_MASID = 0x0104;
	public static class WhiteListSet implements Parcelable, IParseString, IToJsonObject
	{
		public final static String KEY_PHONENUMBER = "phoneNumber";
		public final static String KEY_NAME = "name";
		
		public final static int FIELD_COUNT = 2;
		public String mPhoneNumber = "";
		public String mName = "";
		
		
		@Override
		public boolean parseString(String jsonString) throws Exception {
			// TODO Auto-generated method stub
			
			JSONObject jsonObject = new JSONObject(jsonString);
			mPhoneNumber = jsonObject.getString(KEY_PHONENUMBER);
			mName = jsonObject.getString(KEY_NAME);
			
			return true;
		}
		
		
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			// TODO Auto-generated method stub
			Bundle mBundle=new Bundle(); 
			
			mBundle.putString(KEY_PHONENUMBER, mPhoneNumber);
			mBundle.putString(KEY_NAME, mName);
		
			dest.writeBundle(mBundle);
		}
		
		public static final Parcelable.Creator<WhiteListSet> CREATOR = new Parcelable.Creator<WhiteListSet>()
		 {

			@Override
			public WhiteListSet createFromParcel(Parcel source) {
				// TODO Auto-generated method stub
				WhiteListSet Data = new WhiteListSet();

				Bundle mBundle=new Bundle(); 
				mBundle = source.readBundle();
				Data.mPhoneNumber = mBundle.getString(KEY_PHONENUMBER);
				Data.mName = mBundle.getString(KEY_NAME);
		
				
				return Data;
			}

			@Override
			public WhiteListSet[] newArray(int size) {
				// TODO Auto-generated method stub
				return new WhiteListSet[size];
			}
			 
		 };


		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}


		@Override
		public JSONObject toJsonObject() throws JSONException {
			// TODO Auto-generated method stub
			JSONObject jsonObject = new JSONObject();
			
			jsonObject.put(KEY_PHONENUMBER, mPhoneNumber);
			jsonObject.put(KEY_NAME, mName);
			
			return jsonObject;
		}
	}
	public static class WhiteListSetGroup implements IParseString, IToJsonObject
	{
		public List<WhiteListSet> mWhiteListSetList = new ArrayList<WhiteListSet>();

		@Override
		public boolean parseString(String jsonString) throws Exception {
			// TODO Auto-generated method stub
			
			JSONObject jsonObject = new JSONObject(jsonString);			
			JSONArray jsonArray = jsonObject.getJSONArray(KEY_ARRAY);
			int size = jsonArray.length();
			for(int i = 0; i < size; i++)
			{
				WhiteListSet object = new WhiteListSet();
				
				try {
					object.parseString(jsonArray.getJSONObject(i).toString());
					mWhiteListSetList.add(object);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			
			return true;
		}

		@Override
		public JSONObject toJsonObject() throws JSONException {
			// TODO Auto-generated method stub
			JSONObject jsonObject = new JSONObject();
			
			JSONArray jsonArray = new JSONArray();
			int size = mWhiteListSetList.size();
			for(int i = 0; i < size; i++)
			{
				jsonArray.put(mWhiteListSetList.get(i).toJsonObject());
			}
			jsonObject.put(KEY_ARRAY, jsonArray);
			
			return jsonObject;
		}
	}
	public final static int DEVICE_GET_WHITELIST_MASID = 0x0105;
	
	
	
	
	// 闹钟设置
	public final static int DEVICE_CLOCK_SET_MASID = 0x0106;
	public static class ClockSet implements Parcelable, IParseString, IToJsonObject
	{
		

		private final static String KEY_NAME = "name";
		private final static String KEY_TIME_STRING = "time";
		private final static String KEY_CYCLE= "cycle";
		private final static String KEY_WEEK_TIMIE = "wk";
		private final static String KEY_MSWITCH = "on";
		
		
		public final static int FIELD_COUNT = 5;
		public String mName = "";
		public String mTimeString = "";
		public int mCycle = 0;
		public String mWeekString = "";
		public int mSwitch = 0;
		
		@Override
		public boolean parseString(String jsonString) throws Exception {
			// TODO Auto-generated method stub

			
			JSONObject jsonObject = new JSONObject(jsonString);
		

			mName = jsonObject.getString(KEY_NAME);
			mTimeString = jsonObject.getString(KEY_TIME_STRING);				
			mWeekString = jsonObject.getString(KEY_WEEK_TIMIE);		
			log.e("mWeekString = " + mWeekString);
			mCycle = jsonObject.getInt(KEY_CYCLE);		
			mSwitch = jsonObject.getInt(KEY_MSWITCH);
	
			
			return true;
		}
		
		
		
		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}
		
		public String toTimeString() {
			// TODO Auto-generated method stub
			StringBuffer sBuffer = new StringBuffer(mTimeString);
			try {
				sBuffer.insert(2, ":");
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			return sBuffer.toString();
		
		}
		
		
		public String toWeekString() {
			// TODO Auto-generated method stub


			String str = mWeekString.replace("\"1\"", "周一").replace("\"2\"", "周二").replace("\"3\"", "周三").replace("\"4\"", "周四").replace("\"5\"", "周五").replace("\"6\"", "周六").replace("\"7\"", "周日");
			str = str.substring(1, str.length() - 1);
			return str;
		}
		
		
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			// TODO Auto-generated method stub

			Bundle mBundle=new Bundle(); 
			
			mBundle.putString(KEY_NAME, mName);
			mBundle.putString(KEY_TIME_STRING, mTimeString);
			mBundle.putInt(KEY_CYCLE, mCycle);
			mBundle.putString(KEY_WEEK_TIMIE, mWeekString);
			mBundle.putInt(KEY_MSWITCH, mSwitch);
			dest.writeBundle(mBundle);
		}
		
		public static final Parcelable.Creator<ClockSet> CREATOR = new Parcelable.Creator<ClockSet>()
		 {

			@Override
			public ClockSet createFromParcel(Parcel source) {
				// TODO Auto-generated method stub
				ClockSet Data = new ClockSet();

				Bundle mBundle=new Bundle(); 
				mBundle = source.readBundle();
				Data.mName = mBundle.getString(KEY_NAME);
				Data.mTimeString = mBundle.getString(KEY_TIME_STRING);
				Data.mCycle = mBundle.getInt(KEY_CYCLE);
				Data.mWeekString = mBundle.getString(KEY_WEEK_TIMIE);
				Data.mSwitch = mBundle.getInt(KEY_MSWITCH);
				
				
				return Data;
			}

			@Override
			public ClockSet[] newArray(int size) {
				// TODO Auto-generated method stub
				return new ClockSet[size];
			}
			 
		 };

		@Override
		public JSONObject toJsonObject() throws JSONException {
			// TODO Auto-generated method stub
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(KEY_NAME, mName);
			jsonObject.put(KEY_TIME_STRING, mTimeString);
			jsonObject.put(KEY_CYCLE, mCycle);
			jsonObject.put(KEY_WEEK_TIMIE, mWeekString);
			jsonObject.put(KEY_MSWITCH, mSwitch);
			
			
			return jsonObject;
		}
		 
	}
	
	
	public static class ClockSetGroup implements IParseString, IToJsonObject
	{
		public List<DeviceSetType.ClockSet> mClockSetList = new ArrayList<DeviceSetType.ClockSet>();

		@Override
		public boolean parseString(String jsonString) throws Exception {
			// TODO Auto-generated method stub
			
			JSONObject jsonObject = new JSONObject(jsonString);			
			JSONArray jsonArray = jsonObject.getJSONArray(KEY_ARRAY);
			int size = jsonArray.length();
			for(int i = 0; i < size; i++)
			{
				ClockSet object = new ClockSet();
				
				try {
					object.parseString(jsonArray.getJSONObject(i).toString());
					mClockSetList.add(object);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			
			return true;
		}

		@Override
		public JSONObject toJsonObject() throws JSONException {
			// TODO Auto-generated method stub
			JSONObject jsonObject = new JSONObject();
			
			JSONArray jsonArray = new JSONArray();
			int size = mClockSetList.size();
			for(int i = 0; i < size; i++)
			{
				jsonArray.put(mClockSetList.get(i).toJsonObject());
			}
			jsonObject.put(KEY_ARRAY, jsonArray);
			
			return jsonObject;
		}
	}
	public final static int DEVICE_GET_CLOCK_MASID = 0x0107;

	
	
	

	// 情景模式				// silent, ring, shake, r&s
	public final static int DEVICE_SCENEMODE_SET_MASID = 0x0108;
	public static class SceneMode implements IParseString, IToJsonObject
	{
		private final static String KEY_MODE = "value";
		
		public String mModeString = "";

		@Override
		public boolean parseString(String jsonString) throws Exception {
			// TODO Auto-generated method stub

			JSONObject jsonObject = new JSONObject(jsonString);	

			mModeString = jsonObject.getString(KEY_MODE);				

			return true;
			
		}
	    
		public int getIndex()
		{
			int index = 0;
			if (mModeString.equals("ring"))
			{
				index = 0;
			}else if (mModeString.equals("shake"))
			{
				index = 1;
			}else if (mModeString.equals("silent"))
			{
				index = 2;
			}else 
			{
				index = 3;
			}
			
			return index;
		}
		
		public void setMode(int index)
		{
			switch(index)
			{
			case 0:
				mModeString = "ring";
				break;
			case 1:
				mModeString = "shake";
				break;
			case 2:
				mModeString = "silent";
				break;
			case 3:
				mModeString = "r&s";
				break;
			}
		}

		@Override
		public JSONObject toJsonObject() throws JSONException {
			// TODO Auto-generated method stub

			JSONObject jsonObject = new JSONObject();
			jsonObject.put(KEY_MODE, mModeString);
			return jsonObject;
		}
	}
	public final static int DEVICE_GET_SCENEMODE_MASID = 0x0109;
	
	
	
	// 电源设置			//	poweroff, sleep, work
	public final static int DEVICE_POWER_SET_MASID = 0x0110;
	public static class PowerSet implements IParseString, IToJsonObject
	{
		private final static String KEY_MODE = "value";
		
		public String mSceenString = "";

		@Override
		public boolean parseString(String jsonString) throws Exception {
			// TODO Auto-generated method stub

			JSONObject jsonObject = new JSONObject(jsonString);	

			mSceenString = jsonObject.getString(KEY_MODE);				

			return true;
		}
		
		public int getIndex()
		{
			int index = 0;
			if (mSceenString.equals("sleep"))
			{
				index = 0;
			}else 
			{
				index = 1;
			}
			
			return index;
		}
		
		public void setPower(int index)
		{
			switch(index)
			{
			case 0:
				mSceenString = "sleep";
				break;
			case 1:
				mSceenString = "work";
				break;
			}
		}

		@Override
		public JSONObject toJsonObject() throws JSONException {
			// TODO Auto-generated method stub
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(KEY_MODE, mSceenString);
			return jsonObject;
		}
	}
	public final static int DEVICE_GET_POWER_MASID = 0x0111;
	
	
	
	// 低电量提醒								// 1-6
	public final static int DEVICE_LOWPOWER_WARN_SET_MASID = 0x0112;
	public static class LowPowerWarn implements IParseString, Parcelable, IToJsonObject
	{
		private final static String KEY_LOWER_WARN = "value";
		
		
		public int mPowerLevel = 0;

		@Override
		public boolean parseString(String jsonString) throws Exception {
			// TODO Auto-generated method stub

			JSONObject jsonObject = new JSONObject(jsonString);	

			mPowerLevel = jsonObject.getInt(KEY_LOWER_WARN);				

			return true;
			
		}
		
		public void setLevelByRate(int level)
		{
			mPowerLevel = level;
		}
		
//		public void setLevelByRate(int rate)
//		{
//			if (rate < 0 || rate > 100)
//			{
//				mPowerLevel = 6;
//				return ;
//			}
//			
//			mPowerLevel = (rate / 17) + 1;
//			if (mPowerLevel > 6)
//			{
//				mPowerLevel = 6;
//			}
//		}
		
//		public int getRate()
//		{
//			int rate = 0;
//			
//			switch(mPowerLevel)
//			{
//			case 1:
//				rate = 16;
//				break;
//			case 2:
//				rate = 33;
//				break;
//			case 3:
//				rate = 50;
//				break;
//			case 4:
//				rate = 68;
//				break;
//			case 5:
//				rate = 84;
//				break;
//			case 6:
//				rate = 100;
//				break;
//			}
//			
//			return rate;
//		}

		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			// TODO Auto-generated method stub
			Bundle mBundle=new Bundle(); 
			
			mBundle.putInt(KEY_LOWER_WARN, mPowerLevel);

		
			dest.writeBundle(mBundle);
		} 
		
		public static final Parcelable.Creator<LowPowerWarn> CREATOR = new Parcelable.Creator<LowPowerWarn>()
		 {

			@Override
			public LowPowerWarn createFromParcel(Parcel source) {
				// TODO Auto-generated method stub
				LowPowerWarn Data = new LowPowerWarn();

				Bundle mBundle=new Bundle(); 
				mBundle = source.readBundle();
				Data.mPowerLevel = mBundle.getInt(KEY_LOWER_WARN);
		
				
				return Data;
			}

			@Override
			public LowPowerWarn[] newArray(int size) {
				// TODO Auto-generated method stub
				return new LowPowerWarn[size];
			}
			 
		 };

		@Override
		public JSONObject toJsonObject() throws JSONException {
			// TODO Auto-generated method stub
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(KEY_LOWER_WARN, mPowerLevel);
			return jsonObject;
		}
	}
	public final static int DEVICE_GET_LOWPOWER_WARN_MASID = 0x0113;
	
	
	// GPS间隔
	public final static int DEVICE_GPS_INTERVAL_SET_MASID = 0x0114;
	public static class GpsInterval implements IParseString,  IToJsonObject
	{
		private final static String KEY_GPS_INTERVAL = "value";
		
		public int mGPSInterval = 0;

		@Override
		public boolean parseString(String jsonString) throws Exception {
			// TODO Auto-generated method stub
			JSONObject jsonObject = new JSONObject(jsonString);	

			mGPSInterval = jsonObject.getInt(KEY_GPS_INTERVAL);				

			return true;
		}

		@Override
		public JSONObject toJsonObject() throws JSONException {
			// TODO Auto-generated method stub
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(KEY_GPS_INTERVAL, mGPSInterval);
			return jsonObject;
		}
	}
	public final static int DEVICE_GET_GPS_INTERVAL_MASID = 0x0115;
	

	// GPS上报时间
	public final static int DEVICE_GPS_STILLTIME_SET_MASID = 0x0116;
	public static class GpsStillTime implements Parcelable, IParseString, IToJsonObject
	{
		private final static String KEY_STARTTIME = "startTime";
		private final static String KEY_ENDTIME = "endTime";
		private final static String KEY_WEEK_TIMIE = "wk";
		
		
		public final static int FIELD_COUNT = 3;
		public String mWeekString = "";
		public String mStartTimeString = "";
		public String mEndTimeString = "";
		
		@Override
		public boolean parseString(String jsonString) throws Exception {
			// TODO Auto-generated method stub	
			JSONObject jsonObject = new JSONObject(jsonString);		

			mStartTimeString = jsonObject.getString(KEY_STARTTIME);				
			mEndTimeString = jsonObject.getString(KEY_ENDTIME);		
			mWeekString = jsonObject.getString(KEY_WEEK_TIMIE);		
		
			return true;
			
		}
		
		public int getStartHour () throws Exception 
		{
			String result = mStartTimeString.substring(0, 2);
			return Integer.valueOf(result);
		}
		
		public int getStartMinute() throws Exception 
		{
			String result = mStartTimeString.substring(2);
			return Integer.valueOf(result);
		}
		
		public int getEndHour() throws Exception 
		{
			String result = mEndTimeString.substring(0, 2);
			return Integer.valueOf(result);
		}
		
		public int getEndMinute() throws Exception 
		{
			String result = mEndTimeString.substring(2);
			return Integer.valueOf(result);
		}
		
		
		public String toStartTimeString() {
			// TODO Auto-generated method stub
			StringBuffer sBuffer = new StringBuffer(mStartTimeString);
			try {
				sBuffer.insert(2, ":");
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			return sBuffer.toString();
		
		}
		
		public String toEndTimeString() {
			// TODO Auto-generated method stub
			StringBuffer sBuffer = new StringBuffer(mEndTimeString);
			try {
				sBuffer.insert(2, ":");
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			return sBuffer.toString();
		
		}
		
		
		public String toWeekString() {
			// TODO Auto-generated method stub

			String str = mWeekString.replace("\"1\"", "周一").replace("\"2\"", "周二").replace("\"3\"", "周三").replace("\"4\"", "周四").replace("\"5\"", "周五").replace("\"6\"", "周六").replace("\"7\"", "周日");
			str = str.substring(1, str.length() - 1);
			
			return str;
		}

		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			// TODO Auto-generated method stub
			
			Bundle mBundle=new Bundle(); 
			
			mBundle.putString(KEY_STARTTIME, mStartTimeString);
			mBundle.putString(KEY_ENDTIME, mEndTimeString);
			mBundle.putString(KEY_WEEK_TIMIE, mWeekString);

			dest.writeBundle(mBundle);
			
		}
		
		public static final Parcelable.Creator<GpsStillTime> CREATOR = new Parcelable.Creator<GpsStillTime>()
		 {

			@Override
			public GpsStillTime createFromParcel(Parcel source) {
				// TODO Auto-generated method stub
				GpsStillTime Data = new GpsStillTime();

				Bundle mBundle=new Bundle(); 
				mBundle = source.readBundle();
				
				Data.mStartTimeString = mBundle.getString(KEY_STARTTIME);
				Data.mEndTimeString = mBundle.getString(KEY_ENDTIME);
				Data.mWeekString = mBundle.getString(KEY_WEEK_TIMIE);		
				return Data;
			}

			@Override
			public GpsStillTime[] newArray(int size) {
				// TODO Auto-generated method stub
				return new GpsStillTime[size];
			}
			 
		 };

		@Override
		public JSONObject toJsonObject() throws JSONException {
			// TODO Auto-generated method stub
			
			JSONObject jsonObject = new JSONObject();
			
			jsonObject.put(KEY_WEEK_TIMIE, mWeekString);
			jsonObject.put(KEY_STARTTIME, mStartTimeString);
			jsonObject.put(KEY_ENDTIME, mEndTimeString);
			
			return jsonObject;
		}

	

	}
	
	public static class GpsStillTimeGroup implements IParseString,  IToJsonObject
	{
		public List<GpsStillTime> mGpsStillTimeList = new ArrayList<DeviceSetType.GpsStillTime>();

		@Override
		public boolean parseString(String jsonString) throws Exception {
			// TODO Auto-generated method stub
		
			JSONObject jsonObject = new JSONObject(jsonString);			
			JSONArray jsonArray = jsonObject.getJSONArray(KEY_ARRAY);
			int size = jsonArray.length();
			for(int i = 0; i < size; i++)
			{
				GpsStillTime object = new GpsStillTime();
				
				try {
					object.parseString(jsonArray.getJSONObject(i).toString());
					mGpsStillTimeList.add(object);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			
			return true;
			
		}

		@Override
		public JSONObject toJsonObject() throws JSONException {
			// TODO Auto-generated method stub
			JSONObject jsonObject = new JSONObject();
			
			JSONArray jsonArray = new JSONArray();
			int size = mGpsStillTimeList.size();
			for(int i = 0; i < size; i++)
			{
				jsonArray.put(mGpsStillTimeList.get(i).toJsonObject());
			}
			jsonObject.put(KEY_ARRAY, jsonArray);
			
			return jsonObject;
		}
	}
	public final static int DEVICE_GET_GPS_STILLTIME_MASID = 0x0117;
	public static class GetGpsStillTime implements IToJsonObject
	{

		private final static String KEY_TIME = "time";
		
		public String data = "";
		@Override
		public JSONObject toJsonObject() throws JSONException {

			JSONObject jsonObject = new JSONObject();
			jsonObject.put(KEY_TIME, data);
			
			return jsonObject;
		}
		
	}
	
	
	// 来电模式					// white/shield/allow
	public final static int DEVICE_PHONE_CALL_MODE_SET_MASID = 0x0118;
	public static class PhoneCallMode implements IParseString, IToJsonObject
	{
		private final static String KEY_MODE = "value";
		
		public String mModeString = "";

		@Override
		public boolean parseString(String jsonString) throws Exception {
			// TODO Auto-generated method stub

			JSONObject jsonObject = new JSONObject(jsonString);	

			mModeString = jsonObject.getString(KEY_MODE);				

			return true;
			
		}
	    
		public int getIndex()
		{
			int index = 0;
			if (mModeString.equals("white"))
			{
				index = 0;
			}else if (mModeString.equals("shield"))
			{
				index = 1;
			}else 
			{
				index = 2;
			}
			
			return index;
		}
		
		public void setMode(int index)
		{
			switch(index)
			{
			case 0:
				mModeString = "white";
				break;
			case 1:
				mModeString = "shield";
				break;
			case 2:
				mModeString = "allow";
				break;
			}
		}

		@Override
		public JSONObject toJsonObject() throws JSONException {
			// TODO Auto-generated method stub
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(KEY_MODE, mModeString);
			return jsonObject;
		}
	}
	public final static int DEVICE_GET_PHONE_CALL_MODE_MASID = 0x0119;
	
	
	
	// 远程监听
	public final static int DEVICE_REMOTE_MONITOR_MASID = 0x0120;
	public static class RemoteMonitor  implements IToJsonObject
	{
		
		public final static String KEY_VALUE = "value";
		public String mPhone = "";

		@Override
		public JSONObject toJsonObject() throws JSONException {
			// TODO Auto-generated method stub
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(KEY_VALUE, mPhone);
			return jsonObject;
		}
	}
	
	
	
//	{"cmd":" locationget_instant ","rsp":1,”data” : {“power”:1,“lat”:22.132,”lon”:113.232,”upload_time”:”yyyy-MM-dd HH:mm:ss”, ”create_time”:”yyyy-MM-dd HH:mm:ss”,”type”:0/1/2}}
//	Type:0—GPS,1—CELL
//	如果没有即时信息，没有data属性。
//	说明：upload_time是指终端上报此坐标位置的时间。
//	Create_time 是指终端获取到这个坐标位置，upload_time>=create_time。
//	如果终端一直在某个点没动，那么create_time会一直不变。
	// 终端GPS位置获取
	public final static int DEVICE_GET_LOCATION_MASID = 0x0121;
	public static class DeviceLocation implements IParseString, IToJsonObject
	{
		private final static String KEY_POWER = "power";
		private final static String KEY_LAT = "lat";
		private final static String KEY_LON = "lon";
		private final static String KEY_TYPE = "type";
		private final static String KEY_UPDATETIME = "upload_time";
		private final static String KEY_CREATETIME = "create_time";
		private final static String KEY_ONLINE = "online";
		private final static String KEY_DID = "did";
		
		
		public String mLat = "";
		public String mLon = "";
		public String mUploadTime = "";
		public String mCreateTime = "";
		public int    mType = 0;
		public int    mPower = 0;
		public int    mOnline = 0;
		public String    mDID = "";
		
		@Override
		public boolean parseString(String jsonString) throws Exception {
			// TODO Auto-generated method stub

			JSONObject jsonObject = new JSONObject(jsonString);		

			mLat = jsonObject.getString(KEY_LAT);				
			mLon = jsonObject.getString(KEY_LON);		
			mUploadTime = jsonObject.getString(KEY_UPDATETIME);	
			mCreateTime = jsonObject.getString(KEY_CREATETIME);
			mType = jsonObject.getInt(KEY_TYPE);
			mPower = jsonObject.getInt(KEY_POWER);
			mOnline = jsonObject.getInt(KEY_ONLINE);
			mDID = jsonObject.optString(KEY_DID);
			
			return true;
			
		}

		@Override
		public JSONObject toJsonObject() throws JSONException {
			// TODO Auto-generated method stub
			JSONObject jsonObject = new JSONObject();
			
			jsonObject.put(KEY_LAT, mLat);
			jsonObject.put(KEY_LON, mLon);
			jsonObject.put(KEY_UPDATETIME, mUploadTime);
			jsonObject.put(KEY_CREATETIME, mCreateTime);
			jsonObject.put(KEY_TYPE, mType);
			jsonObject.put(KEY_POWER, mPower);
			jsonObject.put(KEY_ONLINE, mOnline);
			jsonObject.put(KEY_DID, mDID);
			
			return jsonObject;

		}
	}
	
	
	// 终端 在线状态获取
	public final static int DEVICE_GET_ONELINE_MASID = 0x0122;
	public static class DeviceOnlineStatus implements IParseString
	{
		private final static String KEY_DID = "did";
		private final static String KEY_ONLINE = "value";

	//	public String mDid = "";
		public int mOnline = 0;	
		
		@Override
		public boolean parseString(String jsonString) throws Exception {
			// TODO Auto-generated method stub

			JSONObject jsonObject = new JSONObject(jsonString);		

		//	mDid = jsonObject.getString(KEY_DID);
			mOnline = jsonObject.getInt(KEY_ONLINE);						
		
			return true;
			
		}

	}
	
	
	// 终端 消息获取
	public final static int DEVICE_GET_MSG_MASID = 0x0123;
	public static class DeviceRequestMsg implements IToJsonObject
	{
		public final static String KEY_OFFSET = "offset";
		public final static String KEY_NUM = "num";
		public final static String KEY_SINCEID = "sinceId";
		
		public int mOffset = 0;
		public int mNum = 0;
		public int mSinceID = Integer.MAX_VALUE;
		
		@Override
		public JSONObject toJsonObject() throws JSONException {
			// TODO Auto-generated method stub
			JSONObject jsonObject  = new JSONObject();
			jsonObject.put(KEY_OFFSET, mOffset);
			jsonObject.put(KEY_NUM, mNum);
			jsonObject.put(KEY_SINCEID, mSinceID);
			return jsonObject;
			
		}	
	}
	
	public static class DeviceMsgData implements IParseString, IToJsonObject
	{
		private final static String KEY_TYPE = "type";
		private final static String KEY_MESSAGE = "message";
		private final static String KEY_DID = "did";
		private final static String KEY_ALIAS = "alias";
		private final static String KEY_TIME = "time";
		private final static String KEY_ID = "id";
		private final static String KEY_ISREAD = "isRead";
		
		public int mType = 0;
		public String mMessage = "";	
		public String mDid = "";	
		public String mTime = "";	
		public String mAlias = "";	
		public int    mID = 0;
		public int    mIsRead = 0;
		
		@Override
		public boolean parseString(String jsonString) throws Exception {
			// TODO Auto-generated method stub

			JSONObject jsonObject = new JSONObject(jsonString);		

			mType = jsonObject.getInt(KEY_TYPE);				
			mMessage = jsonObject.getString(KEY_MESSAGE);			
			mDid = jsonObject.getString(KEY_DID);	
			mTime = jsonObject.getString(KEY_TIME);	
			mAlias = jsonObject.getString(KEY_ALIAS);
			mID = jsonObject.getInt(KEY_ID);	
			mIsRead = jsonObject.getInt(KEY_ISREAD);	
			
			
			return true;
			
		}
		
		public JSONObject toJsonObject()
		{
			JSONObject jsonObject = new JSONObject();
			
			try {
				jsonObject.put(KEY_TYPE, mType);
				jsonObject.put(KEY_MESSAGE, mMessage);
				jsonObject.put(KEY_DID, mDid);
				jsonObject.put(KEY_TIME, mTime);
				jsonObject.put(KEY_ALIAS, mAlias);
				jsonObject.put(KEY_ID, mID);
				jsonObject.put(KEY_ISREAD, mIsRead);
	
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
	
			
			return jsonObject;
		}
	}
	
	public static class DeviceMsgDataGroup implements IParseString, IToJsonObject
	{
		private final static String KEY_SINCEID = "sinceId";
		private final static String KEY_ARRAY = "array";
		
		public int mSindID = 0;
		public LinkedList<DeviceMsgData> mDeviceMsglist = new LinkedList<DeviceSetType.DeviceMsgData>();
		
		
		@Override
		public boolean parseString(String jsonString) throws Exception {
			// TODO Auto-generated method stub


			JSONObject jsonObject = new JSONObject(jsonString);		
			mSindID = jsonObject.getInt(KEY_SINCEID);
			JSONArray jsonArray = jsonObject.getJSONArray(KEY_ARRAY);
			int size = jsonArray.length();
			for(int i = 0; i < size; i++)
			{
				DeviceMsgData object = new DeviceMsgData();
				
				try {
					object.parseString(jsonArray.getJSONObject(i).toString());
					mDeviceMsglist.addLast(object);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			
			return true;
		}
		
		public JSONObject toJsonObject()
		{
			JSONArray jsonArray = new JSONArray();
			int size = mDeviceMsglist.size();
			for(int i = 0; i < size; i++)
			{
				DeviceMsgData object = mDeviceMsglist.get(i);
				
				JSONObject jsonObject = object.toJsonObject();
				if (jsonObject != null)
				{
					jsonArray.put(jsonObject);
				}
			}
			
			JSONObject resultJsonObject = new JSONObject();
			try {
				resultJsonObject.put(KEY_ARRAY, jsonArray);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
			return resultJsonObject;
		}
	}

	// 终端 未读消息数获取
	public final static int DEVICE_GET_UNREAD_MSG_MASID = 0x0124;
	public static class DeviceUnReadMsgCount implements IParseString, IToJsonObject
	{
		private final static String KEY_COUNT = "alertcount";
		private final static String KEY_ALERT= "alert";
		private final static String KEY_ALERTWAY = "alertway";
		
		
		public int mCount = 0;	
		public String mAlert = "";
		public BikeType.BikeAlertWay mBikeAlertWay = new BikeType.BikeAlertWay();
		
		@Override
		public boolean parseString(String jsonString) throws Exception {
			// TODO Auto-generated method stub

			JSONObject jsonObject = new JSONObject(jsonString);		

			mCount = jsonObject.getInt(KEY_COUNT);	
			if (mCount > 0){
				mAlert = jsonObject.getString(KEY_ALERT);
				String alertString = jsonObject.getString(KEY_ALERTWAY);
				log.e("alertString = " + alertString);
				mBikeAlertWay.anyleString(alertString);
			}
		
			return true;
			
		}
		
		@Override
		public JSONObject toJsonObject() throws JSONException {
			// TODO Auto-generated method stub
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(KEY_COUNT, mCount);
			return jsonObject;
		}
		
		public String toString (){
			StringBuffer sBuffer = new StringBuffer();
			sBuffer.append("mCount = " + mCount);
			sBuffer.append("\nmAlert = " + mAlert);
			sBuffer.append("\nmRing = " + mBikeAlertWay.mRing);
			sBuffer.append("\nmVibe = " + mBikeAlertWay.mVibe);
			sBuffer.append("\nmPush= " + mBikeAlertWay.mPush);
			sBuffer.append("\nmMessage = " + mBikeAlertWay.mMessage);
			sBuffer.append("\nmPhone = " + mBikeAlertWay.mPhone);
			return sBuffer.toString();
		}
	}
	
	
	// 消息删除
	public final static int DEVICE_DEL_MSG_MASID = 0x0125;
	public static class DeviceDelMsg  implements IToJsonObject
	{
		private final static String KEY_IDS = "ids";
		
		public List<Integer> mList = new ArrayList<Integer>();
		
		public JSONObject toJsonObject() throws JSONException
		{
			JSONObject jsonObject = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			
			int size = mList.size();
			for(int i = 0; i < size; i++)
			{
				jsonArray.put(mList.get(i));
			}
			
		
			jsonObject.put(KEY_IDS, jsonArray);
			return jsonObject;
		}	
	}
	
	
	// 围栏信息获取
	public final static int DEVICE_GET_AREA_MASID = 0x0126;
	public static class DeviceArea implements IToJsonObject
	{
		private final static String KEY_TYPE = "type";
		private final static String KEY_OFFSET = "offset";
		private final static String KEY_NUM = "num";
		
		public String mType = "all";
		public int mOffset = 0;
		public int mNum = 0;
		

		@Override
		public JSONObject toJsonObject() throws JSONException {
			// TODO Auto-generated method stub
			JSONObject jsonObject = new JSONObject();
			
			jsonObject.put(KEY_TYPE, mType);
			jsonObject.put(KEY_OFFSET, mOffset);
			jsonObject.put(KEY_NUM, mNum);
			
			return jsonObject;

		}
	}

	public static class DeviceAreaResult implements IParseString, IToJsonObject
	{
		private final static String KEY_TYPE = "type";
		private final static String KEY_LAt = "lat";
		private final static String KEY_LON = "lon";
		private final static String KEY_NAME = "name";
		private final static String KEY_RADIUS = "radius";
		private final static String KEY_ID = "id";
		private final static String KEY_UPDATETIME = "updatetime";
		private final static String KEY_WK = "wk";
		private final static String KEY_STARTTIME = "startTime";
		private final static String KEY_ENDTIME = "endTime";
		
		public String mType = "";
		public double mLat = 0;
		public double mLon = 0;
		public int mRadius = 0;
		public String mName = "";	
		public int mId  = 0;
		public String mUpdateTime = "";
		public String mWK = "";
		public String mStartTime  = "";
		public String mEndTime = "";
		public double mOffsetLat = 0;
		public double mOffsetLon = 0;

		@Override
		public JSONObject toJsonObject() throws JSONException {
			// TODO Auto-generated method stub
			JSONObject jsonObject = new JSONObject();
			
			jsonObject.put(KEY_TYPE, mType);
			jsonObject.put(KEY_LAt, mLat);
			jsonObject.put(KEY_LON, mLon);
			jsonObject.put(KEY_NAME, mName);
			jsonObject.put(KEY_RADIUS, mRadius);
			jsonObject.put(KEY_ID, mId);
			jsonObject.put(KEY_UPDATETIME, mUpdateTime);
			jsonObject.put(KEY_WK, mWK);
			jsonObject.put(KEY_STARTTIME, mStartTime);
			jsonObject.put(KEY_ENDTIME, mEndTime);
			
			return jsonObject;

		}

		@Override
		public boolean parseString(String jString) throws Exception {
			// TODO Auto-generated method stub

			JSONObject jsonObject = new JSONObject(jString);
			mType = jsonObject.getString(KEY_TYPE);
			mLat = jsonObject.getDouble(KEY_LAt);
			mLon = jsonObject.getDouble(KEY_LON);
			mRadius = jsonObject.getInt(KEY_RADIUS);
			mName = jsonObject.getString(KEY_NAME);
			mId = jsonObject.getInt(KEY_ID);
			mUpdateTime = jsonObject.getString(KEY_UPDATETIME);
			mWK = jsonObject.getString(KEY_WK);
			mStartTime = jsonObject.getString(KEY_STARTTIME);
			mEndTime = jsonObject.getString(KEY_ENDTIME);
			
			return true;
		}
	}
	
	
	public static class DeviceAreaResultGrounp implements IParseString
	{
		public List<DeviceAreaResult> mDeviceArealist = new ArrayList<DeviceAreaResult>();
		
		
		@Override
		public boolean parseString(String jsonString) throws Exception {
			// TODO Auto-generated method stub


			JSONObject jsonObject = new JSONObject(jsonString);			
			JSONArray jsonArray = jsonObject.getJSONArray(KEY_ARRAY);
			int size = jsonArray.length();
			for(int i = 0; i < size; i++)
			{
				DeviceAreaResult object = new DeviceAreaResult();
				
				try {
					object.parseString(jsonArray.getJSONObject(i).toString());
					mDeviceArealist.add(object);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			
			return true;
		}
		
	}
	
	
	
	// 历史轨迹获取获取
	public final static int DEVICE_GET_DEVICE_HISTORY_MASID = 0x0127;
	public static class DeviceHistory implements IToJsonObject
	{
		private final static String KEY_STARTTIME = "startTime";
		private final static String KEY_ENDTIME = "endTime";
		
		public String mStartTime = "";
		public String mEndTime = "";
		

		@Override
		public JSONObject toJsonObject() throws JSONException {
			// TODO Auto-generated method stub
			JSONObject jsonObject = new JSONObject();
			
			jsonObject.put(KEY_STARTTIME, mStartTime);
			jsonObject.put(KEY_ENDTIME, mEndTime);
			
			return jsonObject;

		}
	}
	
	
	public static class DeviceHistoryResult implements IParseString
	{
		private final static String KEY_LAT = "lat";
		private final static String KEY_LON = "lon";
		private final static String KEY_UPLOADTIME = "upload_time";
		private final static String KEY_CREATETIME = "create_time";
		private final static String KEY_TYPE = "type";
		private final static String KEY_POWER = "power";
		
		public double mLat = 0;
		public double mLon = 0;
		public String mUploadTime = "";
		public String mCreateTime = "";
		public int mType = 0;
		public int mPower = 0;
		
		public double mOffsetLat = 0;
		public double mOffsetLon = 0;
		
		@Override
		public boolean parseString(String jString) throws Exception {
			// TODO Auto-generated method stub
			
			JSONObject jsonObject = new JSONObject(jString);
			mLat = jsonObject.getDouble(KEY_LAT);
			mLon = jsonObject.getDouble(KEY_LON);
			mUploadTime = jsonObject.getString(KEY_UPLOADTIME);
			mCreateTime = jsonObject.getString(KEY_CREATETIME);
			mType = jsonObject.getInt(KEY_TYPE);
			mPower = jsonObject.getInt(KEY_POWER);
			
			return true;
		}
		

	}
	
	public static class DeviceHistoryResultGrounp implements IParseString
	{
		public List<DeviceHistoryResult> mDeviceHistorylist = new ArrayList<DeviceHistoryResult>();
		
		
		@Override
		public boolean parseString(String jsonString) throws Exception {
			// TODO Auto-generated method stub


			JSONObject jsonObject = new JSONObject(jsonString);			
			JSONArray jsonArray = jsonObject.getJSONArray(KEY_ARRAY);
			int size = jsonArray.length();
			for(int i = 0; i < size; i++)
			{
				DeviceHistoryResult object = new DeviceHistoryResult();
				
				try {
					object.parseString(jsonArray.getJSONObject(i).toString());
					mDeviceHistorylist.add(object);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			
			return true;
		}
		
	}
	
	
	
	// 设置休眠时段
	public final static int DEVICE_SET_SLEEPTIME_MASID = 0x0128;
	// 获取休眠时段
	public final static int DEVICE_GET_SLEEPTIME_MASID = 0x0129;
	
	
}
