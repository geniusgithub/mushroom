package com.mobile.yunyou.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mobile.yunyou.map.util.CellIDInfo;


// 0x0001 - 0x0099
public class PublicType {

	// 用户登录
	public final static int USER_LOGIN_MASID = 0x0001;	
	public static class UserLogin  implements IToJsonObject
	{
		public final static String KEY_USERNAME = "username";
		public final static String KEY_PASSWORD = "password";
		
		public String mUserName = "";
		public String mPassword = "";
		
		
		@Override
		public JSONObject toJsonObject() throws JSONException {
			// TODO Auto-generated method stub

			JSONObject jsonObject = new JSONObject();
			jsonObject.put(KEY_USERNAME, mUserName);
			jsonObject.put(KEY_PASSWORD, mPassword);
			
			return jsonObject;
		}
		
		
	}
	
	public static class UserLoginResult implements IParseString
	{

		public final static String KEY_DEVICES = "devices";
		public final static String KEY_TYPE = "type";

		
		public List<GloalType.DeviceInfoEx> deviceList = new ArrayList<GloalType.DeviceInfoEx>();
		public int mType = 0;

		
		@Override
		public boolean parseString(String jsonString) throws Exception {
			// TODO Auto-generated method stub
			
			
			JSONObject jsonObject = new JSONObject(jsonString);
			
			mType = jsonObject.getInt(KEY_TYPE);
	
			
			JSONArray jsonArray = jsonObject.getJSONArray(KEY_DEVICES);
			
			int size = jsonArray.length();		
			for(int i = 0; i < size; i++)
			{
				try {
					GloalType.DeviceInfoEx infoEx = new GloalType.DeviceInfoEx();
					infoEx.parseString(jsonArray.getJSONObject(i).toString());
					deviceList.add(infoEx);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

			
			return true;
		}
		
		
		
	}
	
	// 用户注册
	public final static int USER_REGISTER_MASID = 0x0002;
	public static class UserRegister implements IToJsonObject
	{
		
		public final static String KEY_TYPE = "type";
		public final static String KEY_COMPANY = "company";
		public final static String KEY_USERNAME = "username";
		public final static String KEY_PASSWORD = "password";
		public final static String KEY_PHONENUM = "phoneNum";
		public final static String KEY_EMAIL = "email";
		public final static String KEY_TRUENAME = "trueName";
		
		
		public String mType = "";
		public String mCompany = "company";
		public String mUserName = "";
		public String mPassword = "";
		public String mPhone = "";
		public String mEmail = "";
		public String mTrueName = "";
		
		
		@Override
		public JSONObject toJsonObject() throws JSONException {
			// TODO Auto-generated method stub
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(KEY_TYPE, mType);
			jsonObject.put(KEY_COMPANY, mCompany);
			jsonObject.put(KEY_USERNAME, mUserName);
			jsonObject.put(KEY_PASSWORD, mPassword);
			jsonObject.put(KEY_PHONENUM, mPhone);
			jsonObject.put(KEY_EMAIL, mEmail);
			jsonObject.put(KEY_TRUENAME, mTrueName);
			
			return jsonObject;
		}
	}
	

	
	
		
	// 修改密码
	public final static int USER_CHANGE_PASSWORD_MASID = 0x0003;	
	public static class UserChangePwd  implements IToJsonObject
	{
		public final static String KEY_ORIPWD = "orignalPassword";
		public final static String KEY_NEWPWD = "newPassword";
		
		public String mOldPassword = "";
		public String mNewPassword = "";
		
		
		@Override
		public JSONObject toJsonObject() throws JSONException {
			// TODO Auto-generated method stub
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(KEY_ORIPWD, mOldPassword);
			jsonObject.put(KEY_NEWPWD, mNewPassword);
			return jsonObject;
		}
		
	}	
	
	
	// 修改用户信息
	public final static int USER_CHANGE_INFO_MASID = 0x0004;
	public static  class UserChangeInfo implements IParseString, IToJsonObject
	{
			
		public final static String KEY_BIRTHDAY = "birthday";
		public final static String KEY_ADDR = "addr";
		public final static String KEY_SEX = "sex";
		public final static String KEY_PHONENUM = "phoneNum";
		public final static String KEY_TRUENAME = "trueName";
		public final static String KEY_EMAIL = "email";
	
		public String mTrueName = "";
		public String mPhone = "";
		public String mBirthday= "";
		public String mEmail = "";
		public String mAddr = "";
		public String mSex = "";
		
		
		@Override
		public boolean parseString(String jsonString) throws Exception {
			// TODO Auto-generated method stub

			JSONObject jsonObject = new JSONObject(jsonString);
			mTrueName = jsonObject.getString(KEY_TRUENAME);
			mPhone = jsonObject.getString(KEY_PHONENUM);
			mBirthday = jsonObject.getString(KEY_BIRTHDAY);
			mEmail = jsonObject.getString(KEY_EMAIL);
			mAddr = jsonObject.getString(KEY_ADDR);
			mSex = jsonObject.getString(KEY_SEX);
			
			return true;
			
			
		}


		@Override
		public JSONObject toJsonObject() throws JSONException {


			JSONObject object = new JSONObject();
			object.put(KEY_TRUENAME, mTrueName);
			object.put(KEY_PHONENUM, mPhone);
			object.put(KEY_BIRTHDAY, mBirthday);
			object.put(KEY_EMAIL, mEmail);
			object.put(KEY_ADDR, mAddr);
			object.put(KEY_SEX, mSex);
			return object;
		}
		
		
		
	}
	
	// 获取用户信息
	public final static int USER_GET_INFO_MASID = 0x0005;
	
	// 注销用户
	public final static int USER_LOGOUIT_MASID = 0x0006;
	
	
	// 心跳包
	public final static int USER_HEART_MASID = 0x0007;
	

	// 绑定设备
	public final static int USER_BIND_MASID = 0x0008;	
	public static class UserBind  implements IToJsonObject
	{
		public final static String KEY_USERNAME = "username";
		public final static String KEY_PASSWORD = "password";
		
		public String mUserName = "";
		public String mPassword = "";
		
		
		@Override
		public JSONObject toJsonObject() throws JSONException {
			// TODO Auto-generated method stub

			JSONObject jsonObject = new JSONObject();
			jsonObject.put(KEY_USERNAME, mUserName);
			jsonObject.put(KEY_PASSWORD, mPassword);
			
			return jsonObject;
		}
		
		
	}
	
	public final static int USER_UNBIND_MASID = 0x0009;	
	
	
	
	
	
//	{"cmd":"user_ uploadgps ","sid":"DSFASDFASDFASDF2143123SXX",
//	”data”:{“lat”:double,”lon”:double, "cells":{[ ci:int,lac:int,mcc:int,mnc:int,rxlev:int]} }  }
//	 输出报文说明
//	 {"cmd":"user_ uploadgps ","rsp":1 }
	public final static int USER_UPLOAD_GPS_MASID = 0x0010;	
	public static class UserUploadGps implements IToJsonObject
	{
		public final static String KEY_LAT = "lat";
		public final static String KEY_LON = "lon";
		public final static String KEY_CELLS = "cells";
		
		public double mLat = 0;
		public double mLon = 0;
		public CellsGroup mCellsGroup = new CellsGroup();
		
		
		public JSONObject toJsonObject()
		{
			JSONObject jsonObject = new JSONObject();

			try {
				jsonObject.put(KEY_LAT, mLat);
				jsonObject.put(KEY_LON, mLon);
				jsonObject.put(KEY_CELLS, mCellsGroup.toJsonArray());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return jsonObject;
		}
	}
	
	public static class CellsGroup
	{
		public List<CellIDInfo> mList = new ArrayList<CellIDInfo>();
		
		public final static String KEY_CID = "ci";
		public final static String KEY_LAC = "lac";
		public final static String KEY_MCC = "mcc";
		public final static String KEY_MNC = "mnc";
		public final static String KEY_RXLEV = "rxlev";
		public final static String KEY_ARFCN = "arfcn";
		public final static String KEY_BSIC = "bsic";
		
		public JSONArray toJsonArray()
		{
			JSONArray jsonArray = new JSONArray();
			int size = mList.size();
			for(int i = 0; i < size; i++)
			{
				CellIDInfo info = mList.get(i);
				
				try {

					JSONObject jsonObject = new JSONObject();
					jsonObject.put(KEY_CID, info.cellId);
					jsonObject.put(KEY_LAC, info.locationAreaCode);
					jsonObject.put(KEY_MCC, Integer.valueOf(info.mobileCountryCode));
					jsonObject.put(KEY_MNC, Integer.valueOf(info.mobileNetworkCode));
					jsonObject.put(KEY_RXLEV, info.rxlv);
					jsonObject.put(KEY_ARFCN, 0);
					jsonObject.put(KEY_BSIC, 0);
					
					jsonArray.put(jsonObject);		
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}
			
			return jsonArray;
		}


		
	}
}
