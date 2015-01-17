package com.mobile.yunyou.network;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;

import android.os.Handler;

import com.huizhilian.android.MessagePushHandler;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.model.BaseType;
import com.mobile.yunyou.model.GloalType;
import com.mobile.yunyou.model.IToJsonObject;
import com.mobile.yunyou.model.MessagePushType;
import com.mobile.yunyou.model.PublicType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.api.AbstractFileDownTask;
import com.mobile.yunyou.network.api.AbstractTaskCallback;
import com.mobile.yunyou.network.api.HeadFileConfigure;
import com.mobile.yunyou.network.api.HeadFileDownTask;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.FileManager;
import com.mobile.yunyou.util.LogFactory;


public class NetworkCenterEx  implements MessagePushHandler{

	
	private static final CommonLog log = LogFactory.createLog();
	private static final int THREAD_COUNT = 3;
	private static final int MESSAGE_EXECUTE_CALLBACK = 0x0001;
	
	private static NetworkCenterEx mSNetWorkCenter = null;
	private YunyouApplication mApplication; 

	private Handler mUIHandler = null;
	
	private ClientEngineEx mClientEngine = null;
	private ExecutorService mExecutorService = null;
	private ExecutorService mFileDownLoadExecutorService = null;
	
	public static synchronized NetworkCenterEx getInstance()
	{
		if(mSNetWorkCenter == null)
		{
			mSNetWorkCenter = new NetworkCenterEx(); 
		}
		
		return 	mSNetWorkCenter;
	}
	
	
	
	public void setSid(String sid)
	{
		mClientEngine.setSid(sid);
	}
	
	public void setDid(String did)
	{
		mClientEngine.setDid(did);
	}
	
	
	private NetworkCenterEx()
	{

		mClientEngine = new ClientEngineEx();
		mClientEngine.setMessagePushHandler(this);
		
		mApplication = YunyouApplication.getInstance();
		
		mUIHandler = new Handler()
		{			
		};
	}
	
	// RUN ON UI THREAD
	public boolean initNetwork()
	{
		log.e("NetworkCenter       initNetwork...");
		if (mExecutorService == null)
		{
			mExecutorService = Executors.newFixedThreadPool(THREAD_COUNT);
		//	mExecutorService = Executors.newCachedThreadPool();
		//	mExecutorService = Executors.newSingleThreadExecutor();
		}
		
		if (mFileDownLoadExecutorService == null){
			mFileDownLoadExecutorService = Executors.newFixedThreadPool(THREAD_COUNT);
		}

		return true;
	}
	
	// RUN ON UI THREAD
	public boolean unInitNetwork()
	{
		log.e("NetworkCenter      unInitNetwork...");
		if (mExecutorService != null)
		{
			mExecutorService.shutdown();
			mExecutorService = null;
		}
		
		return true;
	}
	
	
	public synchronized boolean StartRequestToServer(int action, IToJsonObject object, IRequestCallback callback)
	{
		if (mExecutorService == null)
		{
			return false;
		}
		
		Courier courier = new Courier(object, action, callback);
	
		
		mExecutorService.execute(new TaskRunnable(courier));
		
		return false;
	}
	
	public synchronized boolean StartRequestToServer(int action, String did, IToJsonObject object, IRequestCallback callback)
	{
		if (mExecutorService == null)
		{
			return false;
		}
		
		Courier courier = new Courier(object, action, callback, did);
	
		
		mExecutorService.execute(new TaskRunnable(courier));
		
		return false;
	}
	
	
	public boolean requestHeadFileDown(GloalType.DeviceInfoEx dev, AbstractTaskCallback callback){
		
		if (mFileDownLoadExecutorService == null)
		{
			return false;
		}
		
		String requestUri = HeadFileConfigure.getRequestUri(dev);	
		String saveUri = FileManager.getSavePath(requestUri);
		AbstractFileDownTask task = new HeadFileDownTask(requestUri, saveUri, callback);
		mFileDownLoadExecutorService.execute(task);
		
		return true;
	}
	
	public boolean requestHeadFileDown(GloalType.UserInfoEx userInfoEx, AbstractTaskCallback callback){
		
		if (mFileDownLoadExecutorService == null)
		{
			return false;
		}
		
		String requestUri = HeadFileConfigure.getAccountUri(userInfoEx.mSid);	
		log.e("load account head url = " + requestUri);
		String saveUri = FileManager.getSavePath(requestUri);
		AbstractFileDownTask task = new HeadFileDownTask(requestUri, saveUri, callback);
		mFileDownLoadExecutorService.execute(task);
		
		return true;
	}
	
	
	class TaskRunnable implements Runnable
	{
		
		private Courier mCourier = null;
		
		public TaskRunnable(Courier courier)
		{
			mCourier = courier;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub

			ResponseDataPacket responseDataPacket = null;
			
			
			try {
				if (mCourier.mDid != null && mCourier.mDid.length() > 0){
					responseDataPacket = mClientEngine.Request(mCourier.mAction, mCourier.mDid, mCourier.mToJsonObject);
				}else{
					responseDataPacket = mClientEngine.Request(mCourier.mAction, mCourier.mToJsonObject);
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (responseDataPacket == null)
			{
				responseDataPacket = new ResponseDataPacket();
			}
			if (mCourier.mCallback != null)
			{
				mUIHandler.postDelayed(new CallbackRunnable(mCourier.mCallback, mCourier.mAction, responseDataPacket), 1000);
			}
		}
		
		
	}
	
	class CallbackRunnable implements Runnable
	{
		private IRequestCallback callback;
		private int action;
		private ResponseDataPacket dataPacket;
		
		
		public CallbackRunnable(IRequestCallback callback, int action, ResponseDataPacket dataPacket)
		{
			this.callback = callback;
			this.action = action;
			this.dataPacket = dataPacket;
		}
		
		@Override
		public void run() {
			
			
			
			try {
				callback.onComplete(action, dataPacket);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				log.e("callback.onComplete(action, dataPacket) catch Exception!!!");
			}
		
		}
		
		
	}
	
	@Override
	public void messageHandler(JSONObject obj) {
		// TODO Auto-generated method stub
		log.e("messageHandler JSONObject.toString = " + obj.toString());
	
		
		MessagePushType.DeviceWarnMsg msgObject = new MessagePushType.DeviceWarnMsg();
		
		try {
			msgObject.parseString(obj.toString());		
			log.e("messageHandler parse success...badge = "  + msgObject.mBadge);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ;
		}
		
		mApplication.setUnreadCount(msgObject.mBadge);
		mApplication.notifyMessage(msgObject);
	}



	@Override
	public void forceLogoutHandler(JSONObject obj) {
		// TODO Auto-generated method stub
		log.e("forceLogoutHandler JSONObject.toString = " + obj.toString());
	}



	@Override
	public void terminalStatusChangeHandler(JSONObject obj) {
		// TODO Auto-generated method stub
		log.e("terminalStatusChangeHandler JSONObject.toString = " + obj.toString());
		
		MessagePushType.TerminalStatus terminalStatus = new MessagePushType.TerminalStatus();
		
		try {
			terminalStatus.parseString(obj.toString());		
			log.e("terminalStatusChangeHandler parse success...");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ;
		}
		
		mApplication.changeStatusLine(terminalStatus.mDid, terminalStatus.mValue);
	}



	@Override
	public void otherHandler(JSONObject obj) {
		// TODO Auto-generated method stub
		log.e("otherHandler JSONObject.toString = " + obj.toString());
	}



	@Override
	public void exceptionHandler(Exception ex) {
		// TODO Auto-generated method stub
		
	}
}
