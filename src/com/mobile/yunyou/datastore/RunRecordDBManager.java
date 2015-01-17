package com.mobile.yunyou.datastore;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.map.util.StringUtil;
import com.mobile.yunyou.model.BikeType;
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
	
	private static final int DATABASE_VERSION = 2;
	
		private RunRecordDataBaseHelper m_dbHelper;
		private Context m_context;
		private SQLiteDatabase m_db;

		public static final String TABLE_NAME = "RunRecordInfoTable";
		
		public static final String ID = "_id";
		public final static String KEY_USER = "user";					// 用户
		public final static String KEY_DISTANCE = "distance";			// 距离 
		public final static String KEY_STARTTIME = "start_time";		// 开始时间
		public final static String KEY_ENDTIME = "end_time";			// 结束时间
		public final static String KEY_HSPEED = "hspeed";				// 最高速度
		public final static String KEY_AVERAGESPEED = "averagespeed";	// 平均速度
		public final static String KEY_CAL = "cal";						// 卡路里
		public final static String KEY_RECORDLIST = "record";			// 轨迹列表
		




		public static final String CREATE_TABLE = 
								"create table if not exists " + TABLE_NAME + "(" +
								ID + " integer primary key autoincrement, " + 
								KEY_USER + " text not null, " + 
								KEY_DISTANCE + " integer not null, " + 
								KEY_STARTTIME + " text not null, " + 
								KEY_ENDTIME + " text not null, " + 
								KEY_HSPEED + " double not null, " + 
								KEY_AVERAGESPEED + " double not null, " + 
								KEY_CAL + " integer not null, " +
								KEY_RECORDLIST + " text not null);";						
		

		public static final String[] COLUMES = {KEY_USER, KEY_DISTANCE, KEY_STARTTIME, KEY_ENDTIME, KEY_HSPEED, KEY_AVERAGESPEED, KEY_CAL, KEY_RECORDLIST};

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
		
		public synchronized boolean insert(BikeType.RunRecordGroup group) throws Exception
		{
			if (m_db == null || !m_db.isOpen())
			{
				return false;
			}
			
			ContentValues values = new ContentValues();
			
			String userName = YunyouApplication.getInstance().getUserInfoEx().mAccountName;
			values.put(KEY_USER, userName);
			values.put(KEY_DISTANCE, group.mTotalDistance);
			values.put(KEY_STARTTIME, group.mStartTime);
			values.put(KEY_ENDTIME, group.mEndTime);
			values.put(KEY_HSPEED, group.mHSpeed);
			values.put(KEY_AVERAGESPEED, group.mAverageSpeed);
			values.put(KEY_CAL, group.mCal);
			
			JSONArray jsonArray = new JSONArray();
			for(MinRunRecord object : group.mRunRecordList){
				jsonArray.put(object.toJsonObject());
			}
			values.put(KEY_RECORDLIST, jsonArray.toString());
			
			log.e("insert : \n " + group.getShowString());
			
			
			long ret = m_db.insert(TABLE_NAME, null, values);
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
		
		public synchronized boolean queryAll(List<BikeType.RunRecordGroup> groupList) throws Exception
		{

			if (m_db == null || !m_db.isOpen())
			{
				return false;
			}		

			String userName = YunyouApplication.getInstance().getUserInfoEx().mAccountName;
			DeviceSetType.DeviceMsgData msgData = null;
			Cursor cursor = m_db.query(TABLE_NAME, COLUMES, KEY_USER + "=?", new String[]{userName}, null, null, null);
			if (cursor != null)
			{
				while(cursor.moveToNext())
				{
					
					BikeType.RunRecordGroup group = new BikeType.RunRecordGroup();
					
					int distance = cursor.getInt(cursor.getColumnIndex(KEY_DISTANCE));
					String startTime = cursor.getString(cursor.getColumnIndex(KEY_STARTTIME));
					String endtTime = cursor.getString(cursor.getColumnIndex(KEY_ENDTIME));
					double hSpeed = cursor.getDouble(cursor.getColumnIndex(KEY_HSPEED));
					double aSpeed = cursor.getDouble(cursor.getColumnIndex(KEY_AVERAGESPEED));
					int cal = cursor.getInt(cursor.getColumnIndex(KEY_CAL));
					String recordList = cursor.getString(cursor.getColumnIndex(KEY_RECORDLIST));
					
					group.mTotalDistance = distance;
					group.mStartTime = startTime;
					group.mEndTime = endtTime;
					group.mHSpeed = hSpeed;
					group.mAverageSpeed = aSpeed;
					group.mCal = cal;
				
					
					group.mTimeMillsion = StringUtil.getTimeMillson(group.mStartTime, group.mEndTime);
					
					try {
						JSONArray jsonArray = new JSONArray(recordList);
						int size = jsonArray.length();
						for(int i = 0; i < size; i++){
							JSONObject object = jsonArray.getJSONObject(i);
							MinRunRecord record = new MinRunRecord();
							try {
								record.parseString(object.toString());
								group.mRunRecordList.add(record);
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
