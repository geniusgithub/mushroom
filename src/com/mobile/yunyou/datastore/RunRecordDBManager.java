package com.mobile.yunyou.datastore;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.map.util.StringUtil;
import com.mobile.yunyou.model.BikeType;
import com.mobile.yunyou.model.BikeType.BikeLRecordSubResult;
import com.mobile.yunyou.model.BikeType.MinLRunRecord;
import com.mobile.yunyou.model.DeviceSetType;
import com.mobile.yunyou.model.BikeType.MinRunRecord;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class RunRecordDBManager{
		
	private static final CommonLog log = LogFactory.createLog();
	
	private static final int DATABASE_VERSION = 4;
	
		private RunRecordDataBaseHelper m_dbHelper;
		private Context m_context;
		private SQLiteDatabase m_db;

		public static final String TABLE_NAME = "RunRecordInfoTable";
		
		public static final String ID = "_id";
		public final static String KEY_ID = "ids";						// 
		public final static String KEY_USER = "user";					// 用户
		public final static String KEY_DISTANCE = "distance";			// 距离 
		public final static String KEY_STARTTIME = "start_time";		// 开始时间
		public final static String KEY_ENDTIME = "end_time";			// 结束时间
		public final static String KEY_HSPEED = "hspeed";				// 最高速度
		public final static String KEY_LSPEED = "lspeed";				// 最低速度
		public final static String KEY_HEIGHT = "height";				// 最高海拔
		//public final static String KEY_AVERAGESPEED = "averagespeed";	// 平均速度
		public final static String KEY_CAL = "cal";						// 卡路里
		public final static String KEY_RECORDLIST = "record";			// 轨迹列表
		




		public static final String CREATE_TABLE = 
								"create table if not exists " + TABLE_NAME + "(" +
								ID + " integer primary key autoincrement, " + 
								KEY_ID + " integer not null, " + 
								KEY_USER + " text not null, " + 
								KEY_DISTANCE + " double not null, " + 
								KEY_STARTTIME + " text not null, " + 
								KEY_ENDTIME + " text not null, " + 
								KEY_HSPEED + " double not null, " + 
								KEY_LSPEED + " double not null, " + 
								KEY_HEIGHT + " integer not null, " +
								KEY_CAL + " integer not null, " +
								KEY_RECORDLIST + " text not null);";						
		

		public static final String[] COLUMES = {KEY_ID, KEY_USER, KEY_DISTANCE, KEY_STARTTIME, KEY_ENDTIME, KEY_HSPEED, KEY_LSPEED, KEY_HEIGHT, KEY_CAL, KEY_RECORDLIST};

		private static RunRecordDBManager mDBManagerInstance;
		
		public synchronized static RunRecordDBManager getInstance()
		{
			if (mDBManagerInstance == null)
			{
				mDBManagerInstance = new RunRecordDBManager(YunyouApplication.getInstance());
				mDBManagerInstance.open();
			}
			
			return mDBManagerInstance;
		}

		private RunRecordDBManager(Context context) {
			// TODO Auto-generated constructor stub
			m_context = context;
			m_dbHelper = new RunRecordDataBaseHelper(m_context,
					RunRecordDataBaseHelper.DB_NAME, null, DATABASE_VERSION);
		}
		
		public boolean open()
		{
			m_db = m_dbHelper.getWritableDatabase();
			
			return true;
		}
		
		public void close()
		{
			m_db.close();
		}
		
//		public synchronized boolean refreshGPSSettingGroup(Protocol_base.GPSStillTimeGroup gpsSettingGroup)
//		{
//
//			
//			m_db.beginTransaction();			//开始事务
//			
//			boolean flag = true;
//			
//			try {
//				deleteAll();
//				
//				List<PublicType.GPSStillTime> list = gpsSettingGroup.mGPSStillTimesSettingArray;
//				int size = list.size();
//				
//				PublicType.GPSStillTime gpsSetting = null;
//				for(int i = 0; i < size; i++)
//				{
//					gpsSetting = list.get(i);
//					
//					if (gpsSetting.status.equals("add"))
//					{
//						insert(gpsSetting);
//					}
//				}
//				
//				 m_db.setTransactionSuccessful();//调用此方法会在执行到endTransaction() 时提交当前事务，如果不调用此方法会回滚事务		
//			} catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//				flag = false;
//			}
//			
//		   
//			m_db.endTransaction();			//由事务的标志决定是提交事务，还是回滚事务
//			
//
//			
//			return flag;
//		}
		
		public synchronized boolean insert(BikeType.BikeLRecordResult group) throws Exception
		{
			if (m_db == null || !m_db.isOpen())
			{
				return false;
			}
			
			ContentValues values = new ContentValues();
			values.put(KEY_USER, group.mUserID);
			values.put(KEY_DISTANCE, group.mTotalDistance);
			values.put(KEY_STARTTIME, group.mStartTime);
			values.put(KEY_ENDTIME, group.mEndTime);
			values.put(KEY_HSPEED, group.mHSpeed);
			values.put(KEY_LSPEED, group.mLSpeed);
			values.put(KEY_HEIGHT, group.mHeight);
			values.put(KEY_ID, group.mID);
			values.put(KEY_CAL, group.mCal);
			
			JSONArray jsonArray = new JSONArray();
			for(BikeType.BikeLRecordSubResult object : group.mBikeSubRecordResultList){
				jsonArray.put(object.toJsonObject());
			}
			values.put(KEY_RECORDLIST, jsonArray.toString());
			
			//log.e("insert : \n " + group.getShowString());
			
			
			long ret = m_db.insert(TABLE_NAME, null, values);
			if (ret == -1)
			{
				return false;
			}
			
			return true;
			
		}
		
		public synchronized boolean delete(BikeType.BikeLRecordResult object) throws Exception
		{
			if (m_db == null || !m_db.isOpen())
			{
				return false;
			}
	
			int ret = m_db.delete(TABLE_NAME, KEY_USER + "=?" + " and " +KEY_STARTTIME + "=?" ,  new String[]{object.mUserID , object.mStartTime});

			if (ret == -1)
			{
				return false;
			}
			
			return true;
			
		}

		public synchronized boolean deleteAll()
		{
			if (m_db == null || !m_db.isOpen())
			{
				return false;
			}
			
			int ret = m_db.delete(TABLE_NAME, null, null);
			if (ret == 0)
			{
				return false;
			}
			
			
			return true;
		}
		
		public synchronized boolean queryAll(LinkedList<BikeType.BikeLRecordResult> groupList) throws Exception
		{

			if (m_db == null || !m_db.isOpen())
			{
				return false;
			}		

			String userName = YunyouApplication.getInstance().getUserInfoEx().mSid;
			DeviceSetType.DeviceMsgData msgData = null;
			Cursor cursor = m_db.query(TABLE_NAME, COLUMES, KEY_USER + "=?", new String[]{userName}, null, null, null);
			
			if (cursor != null)
			{
				while(cursor.moveToNext())
				{
					
					BikeType.BikeLRecordResult group = new BikeType.BikeLRecordResult();
					
					String userID = cursor.getString(cursor.getColumnIndex(KEY_USER));
					int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
					double distance = cursor.getDouble(cursor.getColumnIndex(KEY_DISTANCE));
					String startTime = cursor.getString(cursor.getColumnIndex(KEY_STARTTIME));
					String endtTime = cursor.getString(cursor.getColumnIndex(KEY_ENDTIME));
					double hSpeed = cursor.getDouble(cursor.getColumnIndex(KEY_HSPEED));
					double lSpeed = cursor.getDouble(cursor.getColumnIndex(KEY_LSPEED));
					int height = cursor.getInt(cursor.getColumnIndex(KEY_HEIGHT));
					int cal = cursor.getInt(cursor.getColumnIndex(KEY_CAL));
					String recordList = cursor.getString(cursor.getColumnIndex(KEY_RECORDLIST));
					
					group.mUserID = userID;
					group.mID = id;
					group.mTotalDistance = distance;
					group.mStartTime = startTime;
					group.mEndTime = endtTime;
					group.mHSpeed = hSpeed;
					group.mLSpeed = lSpeed;
					group.mHeight = height;
					group.mCal = cal;
					group.isLocal = true;

					try {
						JSONArray jsonArray = new JSONArray(recordList);
						int size = jsonArray.length();
						for(int i = 0; i < size; i++){
							JSONObject object = jsonArray.getJSONObject(i);
							BikeLRecordSubResult record = new BikeLRecordSubResult();
							try {
								record.parseString(object.toString());
								group.mBikeSubRecordResultList.add(record);
							} catch (Exception e) {
								e.printStackTrace();
							}
							
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					
					groupList.add(group);
				}
			
				cursor.close();
			}
			
			return true;
		}

	
}
