package com.mobile.yunyou.model;

import org.json.JSONObject;

public class GloalType {

	
	public static class UserInfoEx {

		public String mAccountName = "";
		public String mPassword = "";
		
		public String mTrueName = "";
		public String mSex = "";
		public String mPhone = "";
		public String mEmail = "";
		public String mAddr = "";
		public String mBirthday = "";
		public String mSid = "";
		public int    mType = 0;

		public UserInfoEx()
		{
			
		}
		
		public UserInfoEx(UserInfoEx userInfoEx)
		{
			mAccountName = userInfoEx.mAccountName;
			mTrueName = userInfoEx.mTrueName;
			mSex = userInfoEx.mSex;
			mPhone = userInfoEx.mPhone;
			mEmail = userInfoEx.mEmail;
			mAddr = userInfoEx.mAddr;
			mBirthday = userInfoEx.mBirthday;
			mSid = userInfoEx.mSid;
		}
	}

	
	
	
	public static class DeviceInfoEx implements IParseString{

		
		
		public final static String KEY_ALIAS = "alias";
		public final static String KEY_DID = "did";
		public final static String KEY_ONLINE = "online";
		public final static String KEY_FUNCTION = "function";
		
		public String mAlias = "";
		public String mDid = "";
		public int mOnline = 0;
		public DeviceFunction mDeviceFunction = new DeviceFunction();
		
		
		@Override
		public boolean parseString(String jsonString) throws Exception {
			// TODO Auto-generated method stub


			JSONObject jsonObject = new JSONObject(jsonString);
			mAlias = jsonObject.getString(KEY_ALIAS);
			mDid = jsonObject.getString(KEY_DID);
			mOnline = jsonObject.optInt(KEY_ONLINE);
			
			JSONObject fuJsonObject = jsonObject.getJSONObject(KEY_FUNCTION);
			mDeviceFunction.parseString(fuJsonObject.toString());
		
			return true;
			
		}


		

		
	}
	
	public static class DeviceFunction implements IParseString{

		public final static String KEY_SCENEMODE = "scenemode";
		public final static String KEY_MODIFYAREA = "modifyarea";
		public final static String KEY_MONITOR = "monitor";
		public final static String KEY_CLOCK = "clock";
		public final static String KEY_CALLMODEL = "callmodel";
		
		public final static String KEY_AVATAR = "avatar";
		public final static String KEY_LOWPOWER = "lowpower";
		public final static String KEY_GPSUPLOADINTERVAL = "gpsuploadinterval";
		public final static String KEY_ADDAREA = "addarea";
		public final static String KEY_DELAREA= "delarea";		
		
		public final static String KEY_CALLTIME = "calltime";
		public final static String KEY_POWER = "power";
		public final static String KEY_MSGSEND = "msgsend";
		public final static String KEY_GPSUPLOADTIMETABLE = "gpsuploadtimetable";		
		public final static String KEY_WHITELIST = "whitelist";
		public final static String KEY_KEY = "key";
		
		public boolean key = true;	
		public boolean whitelist = true;
		public boolean clock = true;
		public boolean gpsuploadtimetable = true;
		public boolean scenemode = true;
		public boolean callmodel = true;
		public boolean power = true;
		public boolean lowpower = true;
		public boolean calltime = true;
	
		public boolean modifyarea = true;
		public boolean avatar = true;
		public boolean addarea = true;
		public boolean delarea = true;
		public boolean gpsuploadinterval = true;
		public boolean msgsend = true;
		public boolean monitor = true;
		
		@Override
		public boolean parseString(String jsonString) throws Exception {
			
			JSONObject jsonObject = new JSONObject(jsonString);
			scenemode = jsonObject.getBoolean(KEY_SCENEMODE);
			modifyarea = jsonObject.getBoolean(KEY_MODIFYAREA);
			monitor = jsonObject.getBoolean(KEY_MONITOR);
			clock = jsonObject.getBoolean(KEY_CLOCK);
			callmodel = jsonObject.getBoolean(KEY_CALLMODEL);
			
			avatar = jsonObject.getBoolean(KEY_AVATAR);
			lowpower = jsonObject.getBoolean(KEY_LOWPOWER);
			gpsuploadinterval = jsonObject.getBoolean(KEY_GPSUPLOADINTERVAL);
			addarea = jsonObject.getBoolean(KEY_ADDAREA);
			delarea = jsonObject.getBoolean(KEY_DELAREA);
			
			calltime = jsonObject.getBoolean(KEY_CALLTIME);
			power = jsonObject.getBoolean(KEY_POWER);
			msgsend = jsonObject.getBoolean(KEY_MSGSEND);
			gpsuploadtimetable = jsonObject.getBoolean(KEY_GPSUPLOADTIMETABLE);
			whitelist = jsonObject.getBoolean(KEY_WHITELIST);
			key = jsonObject.getBoolean(KEY_KEY);
			
			return true;
			
		}
		
	}

}
