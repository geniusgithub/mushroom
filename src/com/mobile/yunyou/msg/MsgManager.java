package com.mobile.yunyou.msg;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import com.mobile.yunyou.YunyouApplication;
//import com.mobile.yunyou.datastore.MsgDBManager;
import com.mobile.yunyou.model.DeviceSetType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.Utils;
import com.mobile.yunyou.util.notifactionUtils;

public class MsgManager implements IRequestCallback{
	
	private final static int REQUEST_MSG_INTERVAL = 10 * 1000;
	
	private static final CommonLog log = LogFactory.createLog();
	
	private YunyouApplication mApplication;
	
    private NetworkCenterEx mNetworkCenterEx;
    
    private static MsgManager mInstance;
	
	private Context mContext;
	
	private Timer mTimer;
	
	private MyTimeTask mTimeTask;

	public synchronized static MsgManager getInstance()
	{
		if (mInstance == null)
		{
			mInstance = new MsgManager(YunyouApplication.getInstance());
		}
		
		return mInstance;
	}
	
	private MsgManager(Context context)
	{
		mContext = context;
		
		mTimer = new Timer();

		mNetworkCenterEx = NetworkCenterEx.getInstance();
		
		mApplication = YunyouApplication.getInstance();
		
//		mMsgDBManager = MsgDBManager.getInstance(context);
	}
	

	public void startRequest()
	{	
		startTimer(0);
	}
	
	public void stopRequest()
	{
		stopTimer();	
	}
	
	
	private void startTimer(int delay)
	{
		if (mTimeTask == null)
		{
			mTimeTask = new MyTimeTask();
			mTimer.schedule(mTimeTask, delay, REQUEST_MSG_INTERVAL);
		}
	}
	
	private void stopTimer()
	{
		if (mTimeTask != null)
		{
			mTimeTask.cancel();
			mTimeTask = null;
		}
	}
	
	
	
	
	class MyTimeTask extends TimerTask
	{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			
			if (mApplication.isBindDevice())
			{
				mNetworkCenterEx.StartRequestToServer(DeviceSetType.DEVICE_GET_UNREAD_MSG_MASID, null, MsgManager.this);
			}else{
				log.e("mApplication.isBindDevice = false;");
			}
			
			
		}
		
	}




	@Override
	public boolean onComplete(int requestAction, ResponseDataPacket dataPacket) {
		// TODO Auto-generated method stub



		String jsString = "null";
		if (dataPacket != null)
		{
			jsString = dataPacket.toString();
		}
		
		
	//	log.d("requestAction = " + Utils.toHexString(requestAction) + "\nResponseDataPacket = \n" +jsString);
		if (dataPacket == null)
		{
			log.e("can't get the unread messgae!!!");		
			return false;
		}
		
		switch(requestAction)
		{
			case DeviceSetType.DEVICE_GET_UNREAD_MSG_MASID:
			{				
				if (dataPacket.rsp == 1)
				{
					DeviceSetType.DeviceUnReadMsgCount info = new DeviceSetType.DeviceUnReadMsgCount();
					try {
						info.parseString(dataPacket.data.toString());
						log.e("DEVICE_GET_UNREAD_MSG_MASID success...count = " + info.mCount);
						mApplication.setUnreadCount(info.mCount);
						
						if (info.mCount > 0){
							notifactionUtils.resendWarning(mContext, info);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					log.e("DEVICE_GET_UNREAD_MSG_MASID fail...");
				}
				
			}
			break;
		}
		
		
		return true;
	}
	
	
//	private void onMsgResult(ResponseDataPacket dataPacket)
//	{
//		if (dataPacket.rsp == 0)
//		{
//			log.e("can't get the device msg!!!");
//			
//			return ;
//		}
//
//		DeviceSetType.DeviceMsgData msg = new DeviceSetType.DeviceMsgData();
//		
//		try {
//			msg.parseString(dataPacket.data.toString());
//			
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();			
//		}
//	}
	
	
//	private MsgDBManager mMsgDBManager;
	
//	private List<DeviceSetType.DeviceMsgData> mMsgList = new ArrayList<DeviceSetType.DeviceMsgData>();
//	private int mCurIndex = -1;
	
	
//	public boolean resetMsgFromDatabase()
//	{
//		mCurIndex = -1;
//		mMsgList.clear();
//		try {
//			mMsgDBManager.queryAll(mMsgList);
//			
//			log.e("msg count in database is " + mMsgList.size());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		}
//		
//		return true;
//	}
	
	
//	public void clearMsg()
//	{
//		mMsgList.clear();
//	//	mMsgDBManager.deleteAll();
//	}
//	
//	public List<DeviceSetType.DeviceMsgData>  getAllData()
//	{
//		return mMsgList;
//	}
//	
//	public List<DeviceSetType.DeviceMsgData> queryHistory(int count)
//	{
//		 List<DeviceSetType.DeviceMsgData> list = new ArrayList<DeviceSetType.DeviceMsgData>();
//		 int size = mMsgList.size(); 
//		 if (size == 0 || mCurIndex == 0 || count <= 0)
//		 {
//			 log.e("size = " + size + ", mCurIndex = " + mCurIndex + ", count = " + count);
//			 return list;
//		 }
//		 
//		
//		 if (mCurIndex == -1)
//		 {
//			 mCurIndex = size;
//		 }
//		 int curPos = mCurIndex;
//		 
//		 log.e("query history before curPos = " + curPos);
//		 
//		 int calCount = 0;
//		 for(int i = curPos - 1; i >= 0; i--)
//		 {
//			 DeviceSetType.DeviceMsgData data = mMsgList.get(i);
//			 list.add(data);
//			 calCount++;
//			 if (calCount >= count)
//			 {
//				 break;
//			 }
//		 }
//		 
//		 mCurIndex -= calCount;
//		 if (mCurIndex < 0)
//		 {
//			 
//			 mCurIndex = 0;
//		 }
//		 
//		 log.e("query history after curPos = " + mCurIndex);
//		 
//		 return list;
//	}
//	
////	public boolean delMsg(List<Integer> list)
////	{
////		try {
////			mMsgDBManager.delete(list);
////		} catch (Exception e) {
////			// TODO: handle exception
////			e.printStackTrace();
////			return false;
////		}
////	
////		return true;
////	}
//	
//	public boolean insertMsg(DeviceSetType.DeviceMsgData data)
//	{
//		
//			mMsgList.add(data);
////			try {
////				mMsgDBManager.insert(data);
////			} catch (Exception e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
//		
//		return true;
//	}
//	
//	public boolean insertMsg(List<DeviceSetType.DeviceMsgData> list)
//	{
//		int size = list.size();
//		for(int i = 0; i < size; i++)
//		{
//			mMsgList.add(list.get(i));
////			try {
////				mMsgDBManager.insert(list.get(i));
////			} catch (Exception e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
//		}
//		
//		return true;
//	}
} 
