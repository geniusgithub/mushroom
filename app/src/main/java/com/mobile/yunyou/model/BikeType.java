package com.mobile.yunyou.model;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mobile.yunyou.map.util.StringUtil;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;

//0x0400 - 0x0499
public class BikeType {

	private static final CommonLog log = LogFactory.createLog();
	// 开始骑行		
	public final static int BIKE_STAR_MASITD = 0x0400;
	// 暂停骑行		
	public final static int BIKE_PAUSE_MASITD = 0x0401;	
	// 停止骑行		
	public final static int BIKE_STOP_MASITD = 0x0402;	
	

	// 获取骑行状态	
	public final static int BIKE_STATUS_MASITD = 0x0403;
	public static class BikeStatus  implements IParseString
	{

		public final static String KEY_LAT = "lat";
		public final static String KEY_LON = "lon";
		public final static String KEY_DISTANCE = "distance";
		public final static String KEY_SPEED = "speed";
		public final static String KEY_ELEVATION = "elevation";
		public final static String KEY_STATUS = "status";
		public final static String KEY_CAL = "cal";
		public final static String KEY_CID = "cid";
		public final static String KEY_SPEEDMAX = "speed_max";
		public final static String KEY_SPEEDMIN = "speed_min";
		public final static String KEY_TIMEINTERVAL = "time_interval";
		
		
		public double mLat = 0;
		public double mLon = 0;
		public int mDistance = 0;
		public double mSpeed = 0;
		public double mElevation = 0;
		public int mStatus = 1;
		public int mCal = 0;
		public String mCid = "";
		public double mSpeedMax = 0;
		public double mSpeedMin = 0;
		public int mTimeInterval = 0;
		
		@Override
		public boolean parseString(String jsonString) throws Exception {
			JSONObject jsonObject = new JSONObject(jsonString);	

			String lat = jsonObject.getString(KEY_LAT);		
			if(lat.length() == 0){
				mLat = 0;
			}
			String lon = jsonObject.getString(KEY_LON);
			if(lon.length() == 0){
				mLon = 0;
			}
			mDistance = jsonObject.getInt(KEY_DISTANCE);	
			mSpeed = jsonObject.getDouble(KEY_SPEED);
			mElevation = jsonObject.getDouble(KEY_ELEVATION);
			mStatus = jsonObject.getInt(KEY_STATUS);
			mCal = jsonObject.getInt(KEY_CAL);
			mCid = jsonObject.getString(KEY_CID);
			mSpeedMax = jsonObject.getDouble(KEY_SPEEDMAX);
			mSpeedMin = jsonObject.getDouble(KEY_SPEEDMIN);
			mTimeInterval = jsonObject.getInt(KEY_TIMEINTERVAL);
			
			
			return true;
		}

	}

	// 获取骑行记录
//	public final static int BIKE_RECORD_MASITD = 0x0404;
//	public static class BikeRecord  implements IToJsonObject
//	{
//		
//		public final static String KEY_OFFSET = "offset";
//		public final static String KEY_NUM = "num";
//		public final static String KEY_SINCEID = "sinceId";
//		
//		public int mOffset = 0;
//		public int mNum = 0;
//		public int mSinceID = -1;
//		
//		@Override
//		public JSONObject toJsonObject() throws JSONException {
//			// TODO Auto-generated method stub
//			JSONObject jsonObject  = new JSONObject();
//			jsonObject.put(KEY_OFFSET, mOffset);
//			jsonObject.put(KEY_NUM, mNum);
//			jsonObject.put(KEY_SINCEID, mSinceID);
//			return jsonObject;
//			
//		}	
//	}
	
//	public static class BikeRecordResultGroup implements IParseString
//	{
//		private final static String KEY_SINCEID = "sinceId";
//		private final static String KEY_ARRAY = "array";
//		
//		public int mSindID = -1;		
//		public LinkedList<BikeRecordResult> mBikeRecordResultList = new LinkedList<BikeRecordResult>();
//
//		@Override
//		public boolean parseString(String jsonString) throws Exception {
//			// TODO Auto-generated method stub
//					
//			JSONObject jsonObject = new JSONObject(jsonString);
//			mSindID = jsonObject.getInt(KEY_SINCEID);
//			JSONArray jsonArray = jsonObject.getJSONArray(KEY_ARRAY);
//			int size = jsonArray.length();
//			for(int i = 0; i < size; i++)
//			{
//				BikeRecordResult object = new BikeRecordResult();
//				
//				try {
//					object.parseString(jsonArray.getJSONObject(i).toString());
//					mBikeRecordResultList.add(object);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//
//			
//			return true;
//		}
//
//	
//	}
//	
//	
//	public static class BikeRecordResult implements IParseString
//	{
//		public final static String KEY_ID = "id";
//		public final static String KEY_STARTTIME = "start_time";
//		public final static String KEY_ENDTIME = "end_time";
//		public final static String KEY_DISTANCE = "distance";
//		public final static String KEY_CAL = "cal";
//		
//		public int mID = 0;
//		public String mStartTimeString = "";
//		public String mEndTimeString = "";
//		public int mDistance = 0;
//		public int mCal = 0;
//		
//		
//		public String mTimeTop = "";
//		public String mTimeRight = "";
//		public long mTimeInterval = 0;
//		
//		@Override
//		public boolean parseString(String jsonString) throws Exception {
//			
//			JSONObject jsonObject = new JSONObject(jsonString);	
//
//			mID = jsonObject.optInt(KEY_ID);
//			mStartTimeString = jsonObject.optString(KEY_STARTTIME);
//			mEndTimeString = jsonObject.optString(KEY_ENDTIME);		
//			mDistance = jsonObject.optInt(KEY_DISTANCE);	
//			mCal = jsonObject.optInt(KEY_CAL);
//
//			long timeStart = YunTimeUtils.getTimeMillison(mStartTimeString);
//			mTimeTop = YunTimeUtils.getFormatTime1(timeStart);
//			
//			long timeEnd = YunTimeUtils.getTimeMillison(mEndTimeString);
//			mTimeInterval = (int) (timeEnd - timeStart);
//			mTimeRight = YunTimeUtils.getFormatTimeInterval(mTimeInterval);
//			return true;
//		}
//
//	}
//	
	
	
	
//	
//	// 获取骑行轨迹
//	public final static int BIKE_RECORDSUB_MASITD = 0x0405;
//	public static class BikeRecordSub  implements IToJsonObject
//	{
//		public final static String KEY_RECORD_ID = "record_id";
//		
//		public int mID = 0;
//
//		@Override
//		public JSONObject toJsonObject() throws JSONException {
//			
//			JSONObject jsonObject = new JSONObject();
//			jsonObject.put(KEY_RECORD_ID, mID);
//			
//			return jsonObject;
//			
//		}
//	}
//	
//	public static class BikeRecordSubResultGroup implements IParseString
//	{
//			
//		public LinkedList<BikeRecordSubResult> mBikeSubRecordResultList = new LinkedList<BikeRecordSubResult>();
//
//		@Override
//		public boolean parseString(String jsonString) throws Exception {
//			// TODO Auto-generated method stub
//					
//
//			JSONArray jsonArray = new JSONArray(jsonString);
//			int size = jsonArray.length();
//			for(int i = 0; i < size; i++)
//			{
//				BikeRecordSubResult object = new BikeRecordSubResult();
//				
//				try {
//					object.parseString(jsonArray.getJSONObject(i).toString());
//					mBikeSubRecordResultList.add(object);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//
//			
//			return true;
//		}
//
//	
//	}

//	public static class BikeRecordSubResult  implements IParseString
//	{
//		public final static String KEY_LAT = "w";
//		public final static String KEY_LON = "j";
//		public final static String KEY_UPDATE_TIME = "u";
//		public final static String KEY_CREATE_TIME = "c";
//		public final static String KEY_TYPE = "t";
//		public final static String KEY_POWER = "p";
//		
//		public double mLat = 0;
//		public double mLon = 0;
//		public String mUpdateTime = "";
//		public String mCreateTime = "";
//		public int mType = 0;
//		public int mPower = 0;
//		
//		public double mOffsetLat = 0;
//		public double mOffsetLon = 0;
//		
//		
//		@Override
//		public boolean parseString(String jsonString) throws Exception {
//			
//			JSONObject jsonObject = new JSONObject(jsonString);	
//
//			mLat = jsonObject.optDouble(KEY_LAT);
//			mLon = jsonObject.optDouble(KEY_LON);
//			mUpdateTime = jsonObject.optString(KEY_UPDATE_TIME);
//			mCreateTime = jsonObject.optString(KEY_CREATE_TIME);
//			mType = jsonObject.optInt(KEY_TYPE);
//			mPower = jsonObject.optInt(KEY_POWER);
//			
//			
//			return true;
//		}
//	}
	
	// 设置防盗区域
	public final static int BIKE_SETAREA_MASITD = 0x0406;
	public static class BikeSetArea  implements IToJsonObject
	{

		public final static String KEY_LAT = "lat";
		public final static String KEY_LON = "lon";
		public final static String KEY_NAME = "name";
		public final static String KEY_RADIUS = "radius";
		
		
		public double mLat = 0;
		public double mLon = 0;
		public String mName = "";
		public int mRadius = 0;


		@Override
		public JSONObject toJsonObject() throws JSONException {
		
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(KEY_LAT, mLat);
			jsonObject.put(KEY_LON, mLon);
			jsonObject.put(KEY_NAME, mName);
			jsonObject.put(KEY_RADIUS, mRadius);
			
			return jsonObject;
		}
	}
	
	// 取消防盗区域
	public final static int BIKE_DELAREA_MASITD = 0x0407;
	
	// 获取防盗区域
	public final static int BIKE_GETAREA_MASITD = 0x0408;
	public static class BikeGetArea  implements IParseString
	{

		public final static String KEY_LAT = "lat";
		public final static String KEY_LON = "lon";
		public final static String KEY_NAME = "name";
		public final static String KEY_RADIUS = "radius";
		public final static String KEY_ID = "id";
		public final static String KEY_UPDATETIME = "updatetime";
		
		
		public double mLat = 0;
		public double mLon = 0;
		public String mName = "";
		public int mRadius = 0;
		public int mID = 0;
		public String mUpdateTime = "";
		
		public double mOffsetLat = 0;
		public double mOffsetLon = 0;

		@Override
		public boolean parseString(String str) throws Exception {
			JSONObject jsonObject = new JSONObject(str);
			mLat = jsonObject.getDouble(KEY_LAT);
			mLon = jsonObject.getDouble(KEY_LON);
			mName = jsonObject.getString(KEY_NAME);
			mRadius = jsonObject.getInt(KEY_RADIUS);
			mID = jsonObject.getInt(KEY_ID);
			mUpdateTime = jsonObject.getString(KEY_UPDATETIME);
			return true;
		}
		
		public String toString(){
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("name = " + mName + 
								"\nmLat = " + mLat + 
								"\nmLon = " + mLon + 
								"\nmRadius = " + mRadius + 
								"\nmUpdateTime = " + mUpdateTime + 
								"\noffsetLat = " + mOffsetLat + 
								"\noffsetLon = " + mOffsetLon + 
								"\n");
			
			return stringBuffer.toString();
		}
	}
	
	
	// 获取告警消息
	public final static int  BIKE_GETALERTWAY = 0x0409;
	public static class BikeAlertWay implements  IParseString, IToJsonObject
	{

		private final static String KEY_VALUE = "value";
		
	
		public String mValue = "";

		public boolean mRing = false;
		public boolean mVibe = false;
		public boolean mPush = false;
		public boolean mMessage = false;
		public String mPhone = "";
		@Override
		public boolean parseString(String jsonString) throws Exception {
			// TODO Auto-generated method stub

			log.e("BikeAlertWay parseString = " + jsonString);
			JSONObject jsonObject = new JSONObject(jsonString);		
			mValue = jsonObject.getString(KEY_VALUE);
			
			updateData(mValue);
			
			return true;
		}
		
		public boolean anyleString(String String) throws Exception {
	
			mValue = String;
			
			updateData(mValue);
			
			return true;
		}
		
		public void setData(boolean isRing, boolean isVibe, boolean isPush, boolean isMsg, String phone){
			mRing = isRing;
			mVibe = isVibe;
			mPush = isPush;
			mMessage = isMsg;
			mPhone = phone;
			
		    StringBuffer sBuffer = new StringBuffer();
		    sBuffer.append(mRing ? "1" : "0");
		    sBuffer.append(",");
		    sBuffer.append(mVibe ? "1" : "0");
		    sBuffer.append(",");
		    sBuffer.append(mPush ? "1" : "0");
		    sBuffer.append(",");
		    sBuffer.append(mMessage ? "1" : "0");
		    if (isMsg){
			    sBuffer.append(",");
			    sBuffer.append(mPhone);
		    }
		    
		    mValue = sBuffer.toString();
		}
		
		private void updateData(String data) throws Exception {
			log.e("updateData = " + data);
			String ayyay[] = data.split(",");
			mRing = Integer.parseInt(ayyay[0]) != 0;
			mVibe = Integer.parseInt(ayyay[1]) != 0;
			mPush = Integer.parseInt(ayyay[2]) != 0;
	

			mMessage = Integer.parseInt(ayyay[3]) != 0;
			
			if (ayyay.length > 4){
				mPhone = ayyay[4];
			}else{
				mPhone = "";
			}

		}

		@Override
		public JSONObject toJsonObject() throws JSONException {
			JSONObject jsonObject = new JSONObject();
			setData(mRing, mVibe, mPush, mMessage, mPhone);
			jsonObject.put(KEY_VALUE, mValue);
			
			return jsonObject;
		}
		
	}
	
	// 设置告警消息
	public final static int  BIKE_SETALERTWAY = 0x0410;
	
	
	
	
	
	public static class RunRecordGroup  implements IParseString, IToJsonObject
	{
		public final static String KEY_DISTANCE = "distance";
		public final static String KEY_STARTTIME = "start_time";
		public final static String KEY_ENDTIME = "end_time";
		public final static String KEY_HSPEED = "hspeed";
		public final static String KEY_AVERAGESPEED = "averagespeed";
		public final static String KEY_CAL = "cal";
		
		
		public int mTotalDistance = 0;
		public String mStartTime = "";
		public String mEndTime = "";
		public double mHSpeed = 0;
		public double mAverageSpeed = 0;
		public int mCal = 0;
		
		public long mTimeMillsion = 0;
	
		public final static String KEY_ARRAY = "array";
		public LinkedList<MinRunRecord> mRunRecordList = new LinkedList<MinRunRecord>();

		@Override
		public boolean parseString(String jsonString) throws Exception {
			
			JSONObject jsonObject = new JSONObject(jsonString);	
			mTotalDistance = jsonObject.getInt(KEY_DISTANCE);
			mStartTime = jsonObject.getString(KEY_STARTTIME);
			mEndTime = jsonObject.getString(KEY_ENDTIME);
			mHSpeed = jsonObject.getDouble(KEY_HSPEED);
			mAverageSpeed = jsonObject.getDouble(KEY_AVERAGESPEED);
			mCal = jsonObject.getInt(KEY_CAL);
			
			JSONArray array = jsonObject.getJSONArray(KEY_ARRAY);
			int size = array.length();
			for(int i = 0; i < size; i++){
				JSONObject object = array.getJSONObject(i);
				MinRunRecord record = new MinRunRecord();
				try {
					record.parseString(object.toString());
					mRunRecordList.add(record);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
			mTimeMillsion = StringUtil.getTimeMillson(mStartTime, mEndTime);
			
			return true;
		}

		@Override
		public JSONObject toJsonObject() throws JSONException {
		
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(KEY_DISTANCE, mTotalDistance);
			jsonObject.put(KEY_STARTTIME, mStartTime);
			jsonObject.put(KEY_ENDTIME, mEndTime);
			jsonObject.put(KEY_HSPEED, mHSpeed);
			jsonObject.put(KEY_AVERAGESPEED, mAverageSpeed);
			jsonObject.put(KEY_CAL, mCal);
			
			JSONArray jsonArray = new JSONArray();
			int size = mRunRecordList.size();
			for(MinRunRecord object : mRunRecordList){
				jsonArray.put(object.toJsonObject());
			}
			jsonObject.put(KEY_ARRAY, jsonArray);
			
			return jsonObject;
		}
		

		
		public String getShowString(){
			StringBuffer sBuffer = new StringBuffer();
			String content = "";
			
			JSONArray jsonArray = new JSONArray();
			int size = mRunRecordList.size();
			for(MinRunRecord object : mRunRecordList){
				try {
					jsonArray.put(object.toJsonObject());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			content = jsonArray.toString();
			
			sBuffer.append(KEY_DISTANCE + " = " + mTotalDistance + "\n" + 
							KEY_STARTTIME + " = " + mStartTime + "\n" + 
							KEY_ENDTIME + " = " + mEndTime + "\n" + 
							KEY_HSPEED + " = " + mHSpeed + "\n" + 
							KEY_AVERAGESPEED + " = " + mAverageSpeed +"\n" + 
							KEY_CAL + " = " + mCal + "\n" + 
							"recordList = " + content );
			
			return sBuffer.toString();
		}
		
	}
	
	public static class MinRunRecord  implements IParseString,IToJsonObject
	{
		public final static String KEY_LAT = "w";
		public final static String KEY_LON = "j";
		public final static String KEY_CREATE_TIME = "c";
		
		public double mLat = 0;
		public double mLon = 0;
		public String mCreateTime = "";
		
		
		@Override
		public boolean parseString(String str) throws Exception {
			JSONObject jsonObject = new JSONObject(str);	
			mLat = jsonObject.getDouble(KEY_LAT);
			mLon = jsonObject.getDouble(KEY_LON);
			mCreateTime = jsonObject.getString(KEY_CREATE_TIME);
			return true;
		}
		@Override
		public JSONObject toJsonObject() throws JSONException {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(KEY_LAT, mLat);
			jsonObject.put(KEY_LON, mLon);
			jsonObject.put(KEY_CREATE_TIME, mCreateTime);
			return jsonObject;
		}
		
	}	
	

	// 上传骑行记录
	public final static int BIKE_RECORDUPLOAD_MASITD = 0x0411;
	public static class BikeRecordUpload  implements IToJsonObject
	{
		
		public final static String KEY_DISTANCE = "total_distance";
		public final static String KEY_STARTTIME = "start_time";
		public final static String KEY_ENDTIME = "end_time";
		public final static String KEY_CAL = "total_cal";		
		public final static String KEY_HSPEED = "max_speed";
		public final static String KEY_LSPEED = "min_speed";
		public final static String KEY_HEIGHT = "max_elevation";
	
		public final static String KEY_DATA = "data";
		
		public double mTotalDistance = 0;
		public String mStartTime = "";
		public String mEndTime = "";
		public double mCal = 0;
		public double mHSpeed = 0;
		public double mLSpeed = 0;
		public int mHeight = 0;
		
		
		public LinkedList<MinLRunRecord> mBikeRecordList = new LinkedList<MinLRunRecord>();
		
		@Override
		public JSONObject toJsonObject() throws JSONException {
			// TODO Auto-generated method stub
			JSONObject jsonObject  = new JSONObject();
			jsonObject.put(KEY_DISTANCE, mTotalDistance);
			jsonObject.put(KEY_STARTTIME, mStartTime);
			jsonObject.put(KEY_ENDTIME, mEndTime);
			jsonObject.put(KEY_CAL, mCal);
			jsonObject.put(KEY_HSPEED, mHSpeed);
			jsonObject.put(KEY_LSPEED, mLSpeed);
			jsonObject.put(KEY_HEIGHT, mHeight);
			 
			int size = mBikeRecordList.size();
			JSONArray jsonArray = new JSONArray();
			for(int i = 0; i < size; i++){
				MinLRunRecord object = mBikeRecordList.get(i);
				jsonArray.put(object.toJsonObject());
			}
			
			jsonObject.put(KEY_DATA, jsonArray);
			
			return jsonObject;
			
		}	
	}

	public static class MinLRunRecord  implements IToJsonObject, IParseString
	{
		public final static String KEY_LAT = "lat";
		public final static String KEY_LON = "lon";
		public final static String KEY_TYPE = "type";
		public final static String KEY_ELEVATION = "elevation";
		public final static String KEY_CREATE_TIME = "time";
		
		public double mLat = 0;
		public double mLon = 0;
		public int mType = 0;
		public int mHeight = 0;
		public String mCreateTime = "";
		

		@Override
		public JSONObject toJsonObject() throws JSONException {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(KEY_LAT, mLat);
			jsonObject.put(KEY_LON, mLon);
			jsonObject.put(KEY_TYPE, mType);
			jsonObject.put(KEY_ELEVATION, mHeight);
			jsonObject.put(KEY_CREATE_TIME, mCreateTime);
			return jsonObject;
		}


		@Override
		public boolean parseString(String str) throws Exception {
			
			JSONObject jsonObject = new JSONObject(str);
			mLat = jsonObject.getDouble(KEY_LAT);
			mLon = jsonObject.getDouble(KEY_LON);
			mType = jsonObject.getInt(KEY_TYPE);
			mHeight = jsonObject.getInt(KEY_ELEVATION);
			mCreateTime = jsonObject.getString(KEY_CREATE_TIME);
			
			return true;
			
		}
	}
	
	// 获取骑行记录
	public final static int BIKE_LRECORD_MASITD = 0x0412;
	public static class BikeLRecord  implements IToJsonObject
	{
		
		public final static String KEY_OFFSET = "offset";
		public final static String KEY_NUM = "num";
		public final static String KEY_SINCEID = "sinceId";
		
		public int mOffset = 0;
		public int mNum = 0;
		public int mSinceID = -1;
		
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

	


	
	public static class BikeLRecordResultGroup  implements IParseString{

		private final static String KEY_SINCEID = "sinceId";
		private final static String KEY_ARRAY = "array";
		
		public int mSindID = -1;		
		public LinkedList<BikeLRecordResult> mBikeRecordList = new LinkedList<BikeLRecordResult>();
		
		@Override
		public boolean parseString(String str) throws Exception {
			JSONObject jsonObject = new JSONObject(str);
			mSindID = jsonObject.getInt(KEY_SINCEID);
			
			JSONArray jsonArray = jsonObject.getJSONArray(KEY_ARRAY);
			int size = jsonArray.length();
			for(int i = 0; i < size; i++)
			{
				BikeLRecordResult object = new BikeLRecordResult();
				
				try {
					object.parseString(jsonArray.getJSONObject(i).toString());
					mBikeRecordList.add(object);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			
			return true;
			
		}
		
	}
	
	public static class BikeLRecordResult  implements IParseString
	{
		
		public final static String KEY_DISTANCE = "distance";
		public final static String KEY_STARTTIME = "start_time";
		public final static String KEY_ENDTIME = "end_time";
		public final static String KEY_CAL = "total_cal";		
		public final static String KEY_HSPEED = "max_speed";
		public final static String KEY_LSPEED = "min_speed";
		public final static String KEY_HEIGHT = "max_elevation";
	
		public final static String KEY_ID = "id";
		public final static String KEY_USERID = "user_id";
		
		
		public double mTotalDistance = 0;
		public String mStartTime = "";
		public String mEndTime = "";
		public double mCal = 0;
		public double mHSpeed = 0;
		public double mLSpeed = 0;
		public int mHeight = 0;
		
		public int mID = 0;
		public String mUserID = "";
		
		public boolean isLocal = false;
		public LinkedList<BikeLRecordSubResult> mBikeSubRecordResultList = new LinkedList<BikeLRecordSubResult>();
		@Override
		public boolean parseString(String str) throws Exception {
			JSONObject jsonObject = new JSONObject(str);
			mTotalDistance = jsonObject.getDouble(KEY_DISTANCE);
			mStartTime = jsonObject.getString(KEY_STARTTIME);
			mEndTime = jsonObject.getString(KEY_ENDTIME);
			mCal = jsonObject.getDouble(KEY_CAL);
			mHSpeed = jsonObject.getDouble(KEY_HSPEED);
			mLSpeed = jsonObject.getDouble(KEY_LSPEED);
			mHeight = jsonObject.getInt(KEY_HEIGHT);
			mID = jsonObject.getInt(KEY_ID);
			mUserID = jsonObject.getString(KEY_USERID);
			return true;
		}	
	}
	
	// 获取骑行详细记录
	public final static int BIKE_LRECORDSUB_MASITD = 0x0413;
	public static class BikeLRecordSub  implements IToJsonObject
	{
		public final static String KEY_RECORD_ID = "record_id";
		
		public int mID = 0;

		@Override
		public JSONObject toJsonObject() throws JSONException {
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(KEY_RECORD_ID, mID);
			
			return jsonObject;
			
		}
	}
	
	public static class BikeLRecordSubResultGroup implements IParseString
	{
			
		public LinkedList<BikeLRecordSubResult> mBikeSubRecordResultList = new LinkedList<BikeLRecordSubResult>();

		@Override
		public boolean parseString(String jsonString) throws Exception {
			// TODO Auto-generated method stub
					

			JSONArray jsonArray = new JSONArray(jsonString);
			int size = jsonArray.length();
			for(int i = 0; i < size; i++)
			{
				BikeLRecordSubResult object = new BikeLRecordSubResult();
				
				try {
					object.parseString(jsonArray.getJSONObject(i).toString());
					mBikeSubRecordResultList.add(object);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			
			return true;
		}
		
		
	}
	public static class BikeLRecordSubResult  implements IParseString, IToJsonObject
	{
		public final static String KEY_LAT = "w";
		public final static String KEY_LON = "j";
		public final static String KEY_UPDATE_TIME = "u";
		public final static String KEY_CREATE_TIME = "c";
		public final static String KEY_TYPE = "t";
		public final static String KEY_POWER = "p";
		
		public double mLat = 0;
		public double mLon = 0;
		public String mUpdateTime = "";
		public String mCreateTime = "";
		public int mType = 0;
		public int mPower = 0;
		
//		public double mOffsetLat = 0;
//		public double mOffsetLon = 0;
//		
		
		@Override
		public boolean parseString(String jsonString) throws Exception {
			
			JSONObject jsonObject = new JSONObject(jsonString);	

			mLat = jsonObject.optDouble(KEY_LAT);
			mLon = jsonObject.optDouble(KEY_LON);
			mUpdateTime = jsonObject.optString(KEY_UPDATE_TIME);
			mCreateTime = jsonObject.optString(KEY_CREATE_TIME);
			mType = jsonObject.optInt(KEY_TYPE);
			mPower = jsonObject.optInt(KEY_POWER);
			
			
			return true;
		}

		@Override
		public JSONObject toJsonObject() throws JSONException {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(KEY_LAT, mLat);
			jsonObject.put(KEY_LON, mLon);
			jsonObject.put(KEY_UPDATE_TIME, mUpdateTime);
			jsonObject.put(KEY_CREATE_TIME, mCreateTime);
			jsonObject.put(KEY_TYPE, mType);
			jsonObject.put(KEY_POWER, mPower);
			return jsonObject;
			
		}
	}
	

	
}
