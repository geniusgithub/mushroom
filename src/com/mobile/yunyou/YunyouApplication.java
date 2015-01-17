package com.mobile.yunyou;

//import com.mobile.yunyou.network.NetworkCenter;
import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.maps.MapsInitializer;
import com.mobile.yunyou.activity.MainSlideActivity;
import com.mobile.yunyou.activity.OnlineStatusManager;
import com.mobile.yunyou.model.BikeType;
import com.mobile.yunyou.model.GloalType;
import com.mobile.yunyou.model.MessagePushType;
import com.mobile.yunyou.msg.MsgManager;
import com.mobile.yunyou.network.HeartBeatManager;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.network.api.AbstractTaskCallback;
import com.mobile.yunyou.service.YunyouService;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.Utils;


//drawable-hdpi  854*480 800*480 960*540
//drawable-xhdpi  1280*720     
//drawable-xxhdpi  1920*1080    

public class YunyouApplication extends Application{

	private static final CommonLog log = LogFactory.createLog();
	
	private final static int HANDLER_MSG_REFRESH_UNREADCOUNT = 0x0100;
	private final static int HANDLER_MSG_REFRESHT_EXIT = 0x0110;
	
	private boolean mIsLogin = false;
	
	public boolean mIsDebug = false;
	
	private Handler mHandler;
	
	private static YunyouApplication mApplication;
	
	private NetworkCenterEx mNetworkCenter;

	
	private GloalType.UserInfoEx mUserInfoEx;
	
	private  List<GloalType.DeviceInfoEx> mDeviceList = new ArrayList<GloalType.DeviceInfoEx>();
	
	private GloalType.DeviceInfoEx mCurBindDevice = null;
	
	
	private HeartBeatManager mHeartBeatManager;
	private MsgManager mMsgManager;
	private OnlineStatusManager mOnlineStatusManager;
	
	private ISelDeviceUnbind mISelDeviceChange;
	
	private BikeType.RunRecordGroup mRecordGroup = new BikeType.RunRecordGroup();
	
	private boolean isIgnoreBind = true;
	
	public synchronized static YunyouApplication getInstance()
	{
		return mApplication;
	}
	
	public void setBindFlag(boolean flag){
		isIgnoreBind = flag;
	}
	
	public boolean getBindFlag(){
		return isIgnoreBind;
	}
	
	public void onCreate() {
		// TODO Auto-generated method stub
		mApplication = this;
		
		log.e("yunyou  -->   MyApplication	onCreate!!!");
		
		long timeMillis1 = System.currentTimeMillis();
		mNetworkCenter = NetworkCenterEx.getInstance();
		long timeMillis2 = System.currentTimeMillis();
		log.e("yunyou  -->  NetworkCenter.getInstance() cost = " + (timeMillis2 - timeMillis1) + "!!!");
		
		mUserInfoEx = new GloalType.UserInfoEx();
		
		mHandler = new Handler()
		{

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what)
				{
				case HANDLER_MSG_REFRESH_UNREADCOUNT:
				{
					if (mTVUnreadMsgCount != null)
					{
						if (mUnreadCount > 0)
						{
							mTVUnreadMsgCount.setText("" + mUnreadCount);
							mTVUnreadMsgCount.setVisibility(View.VISIBLE);
						}else{
							mTVUnreadMsgCount.setVisibility(View.GONE);
						}
					}
					
				}				
					break;
				case HANDLER_MSG_REFRESHT_EXIT:
				{
					System.exit(0);
				}
					break;
					
				}
			}
			
		};
		
		
		 mHeartBeatManager = HeartBeatManager.getInstance();	 
		 mMsgManager = MsgManager.getInstance();				 
		 mOnlineStatusManager = OnlineStatusManager.getInstance();

		 int screenWidth = Utils.getScreenWidth(this);
		 int screenHeight = Utils.getScreenHeight(this);
		 log.e("screenWidth = " + screenWidth + ", screenHeight = " + screenHeight);
		 
			try {
				MapsInitializer.initialize(this);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public void setSelDevChange(ISelDeviceUnbind listener){
		mISelDeviceChange = listener;
	}
	
	public void performSelDev(){
		if (mISelDeviceChange != null){
			mISelDeviceChange.onSelDeviceChange();
		}
	}
	
	public void startRequest()
	{
		if (mIsDebug){
			Utils.showToast(this, "您已开启DEBUG模式");
			return ;
		}
		 mHeartBeatManager.startHeartBeat();
		 mMsgManager.startRequest();
		 mOnlineStatusManager.startRequest();
	}

	public void  downLoadHeadProfile(){
		int size = mDeviceList.size();
		for(int i = 0; i < size; i++){
			mNetworkCenter.requestHeadFileDown(mDeviceList.get(i), new AbstractTaskCallback() {
				
				@Override
				public void downLoadComplete(boolean isSuccess) {
					
					log.e("saveUri = " + getSaveUri() + ", isSuccess = " + isSuccess);
					
				}
			});
		}
		
		if (mUserInfoEx.mType == 0){
			log.e("ready to downLoad userInfoEx's head...");
			mNetworkCenter.requestHeadFileDown(mUserInfoEx, new AbstractTaskCallback() {
				
				@Override
				public void downLoadComplete(boolean isSuccess) {
					
					log.e("load mUserInfoEx head  saveUri = " + getSaveUri() + ", isSuccess = " + isSuccess);
					
				}
			});
		}
	
	}
	
	public NetworkCenterEx getNetworkCenter()
	{
		return mNetworkCenter;
	}
	
	public void setUserInfoEx(GloalType.UserInfoEx userInfoEx)
	{
		mUserInfoEx = userInfoEx;
	}
	
	public GloalType.UserInfoEx getUserInfoEx()
	{
		return mUserInfoEx;
	}
	
	public void clearDeviceList(){
		mDeviceList.clear();
	}
	
	public void setDeviceList(List<GloalType.DeviceInfoEx> list)
	{
		mDeviceList = list;
	}
	
	public List<GloalType.DeviceInfoEx> getDeviceList()
	{
		return mDeviceList;
	}
	
	public void setCurDevice(GloalType.DeviceInfoEx device)
	{
		log.e("setCurDevice = " + device);
		mCurBindDevice = device;
		if (mCurBindDevice == null)
		{
			mNetworkCenter.setDid("");
			if (mBtnDevice != null)
			{
				mBtnDevice.setText("终端选择");
			}
		}else {
			log.e("mCurBindDevice.isOnLine = " + mCurBindDevice.mOnline + ", alias = " + mCurBindDevice.mAlias);
			mNetworkCenter.setDid(mCurBindDevice.mDid);
			if (mBtnDevice != null)
			{
				mBtnDevice.setText(mCurBindDevice.mAlias);
			}
		}
	}
	
	public GloalType.DeviceInfoEx getCurDevice()
	{
		return mCurBindDevice;
	}
	
	public void setLoginState(boolean flag)
	{
		mIsLogin = flag;
	}
	
	public boolean getLoginState()
	{
		return mIsLogin;
	}
	
	public void changeStatusLine(String did, int onLine)
	{
		int size = mDeviceList.size();
		for(int i = 0; i < size; i++)
		{
			GloalType.DeviceInfoEx deviceInfoEx = mDeviceList.get(i);
			if (deviceInfoEx.mDid.equals(did))
			{
				deviceInfoEx.mOnline = onLine;
			}
		}
	}
	
	public void changeStatusLine(int onLine)
	{
		if (mCurBindDevice != null)
		{
			mCurBindDevice.mOnline = onLine;
		}
	}
	
	public boolean isDeviceOnline()
	{
		
		if (mCurBindDevice != null)
		{
			log.e("mCurBindDevice.isOnline = " + mCurBindDevice.mOnline);
			return (mCurBindDevice.mOnline != 0 ? true : false);
		}
		
		return false;
	}
	
	
	public boolean isBindDevice()
	{
		return mCurBindDevice == null ? false : true;
	}
	
	public boolean isNetworkAccount()
	{
		if (mUserInfoEx != null)
		{
			return (mUserInfoEx.mType == 0 ? true : false);
		}
		
		return false;
	}
	
	public String getCurDid()
	{
		if (mCurBindDevice != null)
		{
			return mCurBindDevice.mDid;
		}
		
		return "";
	}
	
	public void exit()
	{
		log.e("yunyou  -->   MyApplication	exit!!!");
		
		if (mHeartBeatManager != null)
		{
			mHeartBeatManager.stopHeartBeat();
		}
		
		if (mMsgManager != null)
		{
			mMsgManager.stopRequest();
		}
		
		if (mOnlineStatusManager != null)
		{
			mOnlineStatusManager.stopRequest();
		}
	
		
		mNetworkCenter.unInitNetwork();
		
		stopYunyouService();
		
		exitDelay(1000);
	}
	
	private int mUnreadCount = 0;
	private TextView mTVUnreadMsgCount;
	public void registerMsgCountTextView(TextView textView)
	{
		mTVUnreadMsgCount = textView;
	}
	
	public void unRegisterMsgCountTextView()
	{
		mTVUnreadMsgCount = null;
	}
	
	
	public  void setUnreadCount(int count)
	{

		mUnreadCount = count;
		
		mHandler.sendEmptyMessage(HANDLER_MSG_REFRESH_UNREADCOUNT);
	}
	
	public int getUnreadCount()
	{
		return mUnreadCount;
	}
	
	public final static String KEY_NOTIFY = "KEY_NOTIFY";
	public void notifyMessage(MessagePushType.DeviceWarnMsg msg)
	{
		
		//获得通知管理器			 
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); 
		
		//构建一个通知对象			 
		Notification notification = new Notification(R.drawable.icon, "", System.currentTimeMillis()); 
		
		Intent intent = new Intent(this,MainSlideActivity.class);
		intent.putExtra(KEY_NOTIFY, true);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,0);
		
		
		notification.setLatestEventInfo(getApplicationContext(), msg.mDid, msg.mContent, pendingIntent); 
		
		notification.flags|=Notification.FLAG_AUTO_CANCEL; //自动终止
		 
		notification.defaults |= Notification.DEFAULT_SOUND; //默认声音
		 
		manager.notify(0, notification);//发起通知
	}
	
	private Button mBtnDevice;
	public void registerDeviceBtn(Button btn)
	{
		mBtnDevice = btn;
	}
	
	public void unRegisterDeviceBtn()
	{
		mBtnDevice = null;
	}
	
	
	public void startYunyouService()
	{
		Intent intent = new Intent();
		intent.setClass(this, YunyouService.class);
		startService(intent);
	}
	
	public void stopYunyouService()
	{
		Intent intent = new Intent();
		intent.setClass(this, YunyouService.class);
		stopService(intent);
		
	}
	
	private void exitDelay(int timeMillis)
	{
		mHandler.sendEmptyMessageDelayed(HANDLER_MSG_REFRESHT_EXIT, timeMillis);
	}
	
	
	
	
	public void setRunRecord(BikeType.RunRecordGroup group){
		mRecordGroup = group;
	}
	
	public BikeType.RunRecordGroup getRunRecord(){
		return mRecordGroup;
	}
}
