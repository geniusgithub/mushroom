package com.mobile.yunyou;

//import com.mobile.yunyou.network.NetworkCenter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
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
import com.mobile.yunyou.model.BikeType.BikeLRecordResult;
import com.mobile.yunyou.model.GloalType;
import com.mobile.yunyou.model.MessagePushType;
import com.mobile.yunyou.model.PublicType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.msg.MsgManager;
import com.mobile.yunyou.network.HeartBeatManager;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.network.api.AbstractTaskCallback;
import com.mobile.yunyou.service.YunyouService;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.Utils;
import com.tendcloud.tenddata.TCAgent;


//drawable-hdpi  854*480 800*480 960*540
//drawable-xhdpi  1280*720     
//drawable-xxhdpi  1920*1080    

public class YunyouApplication extends Application implements IRequestCallback, ItatisticsEvent{

	private static final CommonLog log = LogFactory.createLog();
	
	private final static int HANDLER_MSG_REFRESH_UNREADCOUNT = 0x0100;
	private final static int HANDLER_MSG_REFRESHT_EXIT = 0x0110;
	
	private boolean mIsLogin = false;
	
	public boolean mIsDebug = false;
	
	public boolean mIsGetInfo = false;
	
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
	
//	private BikeType.RunRecordGroup mRecordGroup = new BikeType.RunRecordGroup();
	
	private BikeLRecordResult mCurRecord;
	private BikeType.BikeLRecordSubResultGroup mRecordGroup = new BikeType.BikeLRecordSubResultGroup();
	private boolean isIgnoreBind = true;
	
	private boolean isNewVersion = false;
	private PublicType.BikeCheckUpgradeResult mUpdateobject;
	
	private MainSlideActivity mMainSlideActivity;
	
	public synchronized static YunyouApplication getInstance()
	{
		return mApplication;
	}
	
	public void requestUserInfo(){
		mNetworkCenter.StartRequestToServer(PublicType.USER_GET_INFO_MASID, null, this);
	}
	
	public void requestVersionCheck(){
		mNetworkCenter.StartRequestToServer(PublicType.BIKE_CHECKUPGRADE_MASID, null, this);
	}
	

	public void attatchMainActivity(MainSlideActivity activity){
		mMainSlideActivity = activity;
	}
	
	public void finishMainActivity(){
		if (mMainSlideActivity != null){
			log.e("mMainSlideActivity.finish()");
			mMainSlideActivity.finish();
		}
	}
	
	public void setBindFlag(boolean flag){
		isIgnoreBind = flag;
	}
	
	public boolean getBindFlag(){
		return isIgnoreBind;
	}
	
	public void setVersionFlag(boolean flag){
		isNewVersion = flag;
	}
	
	public boolean getVersionFlag(){
		return isNewVersion;
	}
	
	
	public void setGetInfo(boolean flag){
		mIsGetInfo = flag;
	}
	
	public boolean getIsGetInfo(){
		return mIsGetInfo;
	}
	
	public void setVersionObject(PublicType.BikeCheckUpgradeResult object){
		mUpdateobject = object;
	}
	
	public PublicType.BikeCheckUpgradeResult getVersionObject(){
		return mUpdateobject;
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
			
			
			TCAgent.init(this);
			TCAgent.setReportUncaughtExceptions(true);
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
//		for(int i = 0; i < size; i++){
//			mNetworkCenter.requestHeadFileDown(mDeviceList.get(i), new AbstractTaskCallback() {
//				
//				@Override
//				public void downLoadComplete(boolean isSuccess) {
//					
//					log.e("saveUri = " + getSaveUri() + ", isSuccess = " + isSuccess);
//					
//				}
//			});
//		}
		
		if (mUserInfoEx.mType == 0){
			log.e("ready to downLoad userInfoEx's head...");
			mNetworkCenter.requestHeadFileDown(mUserInfoEx, new AbstractTaskCallback() {
				
				@Override
				public void downLoadComplete(boolean isSuccess) {
					
					log.e("load mUserInfoEx head  saveUri = " + getSaveUri() + ", isSuccess = " + isSuccess);
					
					BrocastFactory.sendHeadUpdate(YunyouApplication.getInstance());
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

		Notification.Builder publicBuilder = new Notification.Builder(this);
		publicBuilder.setContentIntent(pendingIntent);
		publicBuilder.setContentTitle(msg.mDid);
		publicBuilder.setContentText(msg.mContent);
		
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
	
	
	
	
//	public void setRunRecord(BikeType.RunRecordGroup group){
//		mRecordGroup = group;
//	}
//	
//	public BikeType.RunRecordGroup getRunRecord(){
//		return mRecordGroup;
//	}
	
	public void attachRunRecords(BikeLRecordResult record, BikeType.BikeLRecordSubResultGroup group){
		mCurRecord = record;
		mRecordGroup = group;
	}
	
	public BikeLRecordResult getRunRecord(){
		return mCurRecord;
	}
	
	public BikeType.BikeLRecordSubResultGroup getRunRecordSub(){
		return mRecordGroup;
	}
	
	@Override
	public boolean onComplete(int requestAction, ResponseDataPacket dataPacket) {
		
		String jsString = "null";
		if (dataPacket != null)
		{
			jsString = dataPacket.toString();
		}
		
		log.e("requestAction = " + Utils.toHexString(requestAction) + "\nResponseDataPacket = \n" +jsString);
		
		switch(requestAction)
		{
		case PublicType.USER_GET_INFO_MASID:
			onGetUserInfoResult(dataPacket);
			break;
		case PublicType.BIKE_CHECKUPGRADE_MASID:
			onCheckUpdate(dataPacket);
			break;
		}
		
		return true;
	}
	
	private void onGetUserInfoResult(ResponseDataPacket dataPacket)
	{
		if (dataPacket == null || dataPacket.rsp == 0)
		{
			return ;
		}
		
		PublicType.UserChangeInfo info = new PublicType.UserChangeInfo();
		try {
			info.parseString(dataPacket.data.toString());
			GloalType.UserInfoEx userInfoEx = mApplication.getUserInfoEx();
			userInfoEx.mTrueName = info.mTrueName;
			userInfoEx.mPhone = info.mPhone;
			userInfoEx.mBirthday = info.mBirthday;
			userInfoEx.mEmail = info.mEmail;
			userInfoEx.mAddr = info.mAddr;
			userInfoEx.mSex = info.mSex;
			
			BrocastFactory.sendUserInfoUpdate(this);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onCheckUpdate(ResponseDataPacket dataPacket){
		
		if (dataPacket == null || dataPacket.rsp == 0)
		{
			return ;
		}
		
		PublicType.BikeCheckUpgradeResult object = new PublicType.BikeCheckUpgradeResult();
		try {
			object.parseString(dataPacket.data.toString());

			log.e("BikeCheckUpgradeResult = " + object.getShowString());
			
			if (object.mNeedUpgrade == 0){
				mApplication.setVersionFlag(false);
				return ;
			}
			
			mApplication.setVersionFlag(true);
			mApplication.setVersionObject(object);
			
			BrocastFactory.sendVersionUpdate(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Utils.showToast(this, R.string.analyze_data_fail);
		}
	}

	@Override
	public void onEvent(String eventID) {
		log.e("eventID = " + eventID);

		TCAgent.onEvent(this, eventID);
	}

	@Override
	public void onEvent(String eventID, HashMap<String, String> map) {
		log.e("eventID = " + eventID);
	
		TCAgent.onEvent(this, eventID, "", map);
	}
	
	public static void onPause(Activity context){

		TCAgent.onPause(context);
	}
	
	public static void onResume(Activity context){
	
		TCAgent.onResume(context);
	}
	
	public static void onCatchError(Context context){
	
		TCAgent.setReportUncaughtExceptions(true);
	}
}
