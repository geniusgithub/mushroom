//package com.mobile.yunyou.datastore;
//
//import java.util.LinkedList;
//import java.util.List;
//
//import com.mobile.yunyou.model.DeviceSetType;
//import com.mobile.yunyou.util.CommonLog;
//import com.mobile.yunyou.util.LogFactory;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.util.Log;
//
//
//public class MsgDBManager {
//		
//	private static final CommonLog log = LogFactory.createLog();
//	
//	private static final int DATABASE_VERSION = 2;
//	
//		private YunyouDataBaseHelper m_dbHelper;
//		private Context m_context;
//		private SQLiteDatabase m_db;
//
//		public static final String TABLE_NAME = "MSGInfoTable";
//		
//		public static final String ID = "_id";
//		public static final String DID = "did";				// 设备ID
//		public static final String TYPE = "type";		    // 消息类型，0-防护区域；1-禁区；2-未签到；3-签到；4低电；5-SOS
//		public static final String MESSAGE = "message";		// 告警消息
//		public static final String TIME = "time";		    // ”yyyy-MM-dd HH:mm:ss”
//		public static final String ALIAS = "alias";		    // ”yyyy-MM-dd HH:mm:ss”
//		public static final String IDX = "id";		    // ”yyyy-MM-dd HH:mm:ss”
//		public static final String ISREAD = "isRead";		    // ”yyyy-MM-dd HH:mm:ss”
//
//
//		public static final String CREATE_TABLE = 
//								"create table if not exists " + TABLE_NAME + "(" +
//								ID + " integer primary key autoincrement, " + 
//								DID + " text not null, " + 
//								TYPE + " integer not null, " + 
//								MESSAGE + " text not null, " + 
//								ALIAS + " text not null, " + 
//								IDX + " integer not null, " + 
//								ISREAD + " integer not null, " +
//								TIME + " text not null);";						
//		
//
//		public static final String[] COLUMES = {DID, TYPE, MESSAGE, ALIAS, IDX, ISREAD, TIME};
//
//		private static MsgDBManager mDBManagerInstance;
//		
//		public synchronized static MsgDBManager getInstance(Context context)
//		{
//			if (mDBManagerInstance == null)
//			{
//				mDBManagerInstance = new MsgDBManager(context);
//				mDBManagerInstance.open();
//			}
//			
//			return mDBManagerInstance;
//		}
//		
//		public synchronized static MsgDBManager getInstance()
//		{
//			return mDBManagerInstance;
//		}
//
//		private MsgDBManager(Context context) {
//			// TODO Auto-generated constructor stub
//			m_context = context;
//			m_dbHelper = new YunyouDataBaseHelper(m_context,
//					YunyouDataBaseHelper.DB_NAME, null, DATABASE_VERSION);
//		}
//		
//		public boolean open()
//		{
//			m_db = m_dbHelper.getWritableDatabase();
//			
//			return true;
//		}
//		
//		public void close()
//		{
//			m_db.close();
//		}
//		
////		public synchronized boolean refreshGPSSettingGroup(Protocol_base.GPSStillTimeGroup gpsSettingGroup)
////		{
////
////			
////			m_db.beginTransaction();			//开始事务
////			
////			boolean flag = true;
////			
////			try {
////				deleteAll();
////				
////				List<PublicType.GPSStillTime> list = gpsSettingGroup.mGPSStillTimesSettingArray;
////				int size = list.size();
////				
////				PublicType.GPSStillTime gpsSetting = null;
////				for(int i = 0; i < size; i++)
////				{
////					gpsSetting = list.get(i);
////					
////					if (gpsSetting.status.equals("add"))
////					{
////						insert(gpsSetting);
////					}
////				}
////				
////				 m_db.setTransactionSuccessful();//调用此方法会在执行到endTransaction() 时提交当前事务，如果不调用此方法会回滚事务		
////			} catch (Exception e) {
////				// TODO: handle exception
////				e.printStackTrace();
////				flag = false;
////			}
////			
////		   
////			m_db.endTransaction();			//由事务的标志决定是提交事务，还是回滚事务
////			
////
////			
////			return flag;
////		}
//		
//		public synchronized boolean insert(List<DeviceSetType.DeviceMsgData> list) throws Exception
//		{
//			if (m_db == null || !m_db.isOpen())
//			{
//				return false;
//			}
//			
//
//			int size = list.size();
//			for(int i = 0;  i < size; i++)
//			{
//				DeviceSetType.DeviceMsgData data = list.get(i);
//				ContentValues values = new ContentValues();
//				
//				values.put(DID, data.mDid);
//				values.put(TYPE, data.mType);
//				values.put(MESSAGE, data.mMessage);
//				values.put(TIME, data.mTime);
//				values.put(ALIAS, data.mAlias);
//				values.put(IDX, data.mID);
//				values.put(ISREAD, data.mIsRead);
//				
//				m_db.insert(TABLE_NAME, null, values);
//			}
//			
//			return true;
//		}
//		
//		public synchronized boolean insert(DeviceSetType.DeviceMsgData data)  throws Exception
//		{
//			if (m_db == null || !m_db.isOpen())
//			{
//				return false;
//			}
//			
//
//			ContentValues values = new ContentValues();
//		
//			values.put(DID, data.mDid);
//			values.put(TYPE, data.mType);
//			values.put(MESSAGE, data.mMessage);
//			values.put(TIME, data.mTime);
//			values.put(ALIAS, data.mAlias);
//			values.put(IDX, data.mID);
//			values.put(ISREAD, data.mIsRead);
//	
//			long ret = m_db.insert(TABLE_NAME, null, values);
//			if (ret == -1)
//			{
//				return false;
//			}
//			
//			return true;
//		}
//		
//		//delete from studentinfo where stu_id in (20,32);
//		public synchronized boolean delete(List<Integer> list)
//		{
//			if (m_db == null || !m_db.isOpen())
//			{
//				return false;
//			}
//			
//			StringBuffer sBuffer = new StringBuffer();
//			int size = list.size();
//			for(int i =  0; i < size; i++)
//			{
//				sBuffer.append(String.valueOf(list.get(i)));
//				if (i < size - 1)
//				{
//					sBuffer.append(",");
//				}
//			}
//			
//			
//			String sqlString = "delete from " + TABLE_NAME + " where " + IDX + " in (" + sBuffer.toString() + ")";
//			log.e("delete sqlstring = " + sqlString);
//			
//			
//			m_db.execSQL(sqlString);
//			
//			
//			return true;
//		}
//
//		public synchronized boolean deleteAll()
//		{
//			if (m_db == null || !m_db.isOpen())
//			{
//				return false;
//			}
//			
//			int ret = m_db.delete(TABLE_NAME, null, null);
//			if (ret == 0)
//			{
//				return false;
//			}
//			
//			
//			return true;
//		}
//		
//		public synchronized boolean queryAll(List<DeviceSetType.DeviceMsgData> list) throws Exception
//		{
//
//			if (m_db == null || !m_db.isOpen())
//			{
//				return false;
//			}		
//
//			DeviceSetType.DeviceMsgData msgData = null;
//			Cursor cursor = m_db.query(TABLE_NAME, COLUMES, null, null, null, null, null);
//			if (cursor != null)
//			{
//				while(cursor.moveToNext())
//				{
//					
//					msgData = new DeviceSetType.DeviceMsgData();
//					
//					String did = cursor.getString(cursor.getColumnIndex(DID));
//					int type = cursor.getInt(cursor.getColumnIndex(TYPE));
//					String message = cursor.getString(cursor.getColumnIndex(MESSAGE));
//					String alias = cursor.getString(cursor.getColumnIndex(ALIAS));
//					String time = cursor.getString(cursor.getColumnIndex(TIME));
//					int idx = cursor.getInt(cursor.getColumnIndex(IDX));
//					int isRead = cursor.getInt(cursor.getColumnIndex(ISREAD));
//					
//					msgData.mDid = did;
//					msgData.mType = type;
//					msgData.mMessage = message;
//					msgData.mAlias = alias;
//					msgData.mTime = time;
//					msgData.mID = idx;
//					msgData.mIsRead = isRead;
//					 
//				
//					list.add(msgData);
//				}
//			
//				cursor.close();
//			}
//			
//			return true;
//		}
//
//	
//}
