package com.mobile.yunyou.datastore;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.model.BikeType;
import com.mobile.yunyou.model.BikeType.BikeLRecordSubResult;
import com.mobile.yunyou.model.DeviceSetType;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;


public class CurDayRecordDBManager{
		
	private static final CommonLog log = LogFactory.createLog();
	
	private static final int DATABASE_VERSION = 1;
	
		private CurDayRecordDataBaseHelper m_dbHelper;
		private Context m_context;
		private SQLiteDatabase m_db;

		public static final String TABLE_NAME = "CurDayRecordInfoTable";
		
		public static final String ID = "_id";	
		public final static String KEY_USER = "user";					// 用户
		public final static String KEY_DISTANCE = "distance";			// 距离 
		public final static String KEY_STARTTIME = "start_time";		// 开始时间
		
		




		public static final String CREATE_TABLE = 
								"create table if not exists " + TABLE_NAME + "(" +
								ID + " integer primary key autoincrement, " + 
								KEY_USER + " text not null, " + 
								KEY_DISTANCE + " double not null, " + 
								KEY_STARTTIME + " text not null);";				
		

		public static final String[] COLUMES = {KEY_USER, KEY_DISTANCE, KEY_STARTTIME};

		private static CurDayRecordDBManager mDBManagerInstance;
		
		public synchronized static CurDayRecordDBManager getInstance()
		{
			if (mDBManagerInstance == null)
			{
				mDBManagerInstance = new CurDayRecordDBManager(YunyouApplication.getInstance());
				mDBManagerInstance.open();
			}
			
			return mDBManagerInstance;
		}

		private CurDayRecordDBManager(Context context) {
			// TODO Auto-generated constructor stub
			m_context = context;
			m_dbHelper = new CurDayRecordDataBaseHelper(m_context, CurDayRecordDataBaseHelper.DB_NAME, null, DATABASE_VERSION);
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
		
		public synchronized boolean update(CurDayDataObject object) throws Exception
		{
			if (m_db == null || !m_db.isOpen())
			{
				return false;
			}
			
			log.e("CurDayRecordDBManager insert userID = " + object.userID);
			ContentValues values = new ContentValues();
			values.put(KEY_USER, object.userID);
			values.put(KEY_DISTANCE, object.distance);
			values.put(KEY_STARTTIME, object.startTime);

			 long ret = m_db.update(TABLE_NAME, values,  KEY_USER + "=?" ,  new String[]{object.userID});
				log.e("CurDayRecordDBManager insert ret = " + ret);
			if (ret == 0)
			{
				ret = m_db.insert(TABLE_NAME, null, values);
			}
			
			return ret != 0 ? true : false;
			
		}
		
		public synchronized boolean delete(String userID) throws Exception
		{
			if (m_db == null || !m_db.isOpen())
			{
				return false;
			}
	
			int ret = m_db.delete(TABLE_NAME, KEY_USER + "=?" ,  new String[]{userID});

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
		
		public synchronized CurDayDataObject query(String queryID) throws Exception
		{
			
			if (m_db == null || !m_db.isOpen())
			{
				log.e("CurDayRecordDBManagerm_db == null || !m_db.isOpen()");
				return null;
			}		

			log.e("CurDayRecordDBManager query userID = " + queryID);
			CurDayDataObject object = null;
			Cursor cursor = m_db.query(TABLE_NAME, COLUMES, KEY_USER + "=?", new String[]{queryID}, null, null, null);			
			if (cursor != null)
			{
				while(cursor.moveToNext())
				{
					log.e("CurDayRecordDBManager find it!!!");
					object = new CurDayDataObject();
					String userID = cursor.getString(cursor.getColumnIndex(KEY_USER));
					double distance = cursor.getDouble(cursor.getColumnIndex(KEY_DISTANCE));
					String startTime = cursor.getString(cursor.getColumnIndex(KEY_STARTTIME));

					
					object.userID = userID;
					object.distance = distance;
					object.startTime = startTime;
			
					break;
						
				}
			
				cursor.close();
			}
			
			return object;
		}

	
}
