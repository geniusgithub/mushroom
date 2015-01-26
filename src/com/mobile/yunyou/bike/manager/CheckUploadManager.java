package com.mobile.yunyou.bike.manager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.bike.NewBikeActivity;
import com.mobile.yunyou.bike.manager.SelfLocationManager.ILocationUpdate;
import com.mobile.yunyou.bike.tmp.DataFactory;
import com.mobile.yunyou.datastore.RunRecordDBManager;
import com.mobile.yunyou.model.BikeType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.model.BikeType.BikeLRecordResult;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.Utils;

public class CheckUploadManager implements IRequestCallback{

	public static interface IDelObser{
		public void onDelRecord(BikeType.BikeLRecordResult object);
	}
	
	private IDelObser mObserver = null;
	
	private static final CommonLog log = LogFactory.createLog();
	
	private final static int DELAY_TIME = 10 * 1000;
	
	private final static int MSG_REFRESH = 0x0001;
	private final static int MSG_STARTUPLOAD = 0x0002;
	
	
    private NetworkCenterEx mNetworkCenter;
    
    private static CheckUploadManager mInstance;
	
    private LinkedList<BikeType.BikeLRecordResult> mList = new LinkedList<BikeType.BikeLRecordResult>();
	private Context mContext;
	private Handler mHandler;
	
	private RunRecordDBManager rDbManager = RunRecordDBManager.getInstance();
	
	private BikeType.BikeLRecordResult mCuRecordResult;
	private int uploadIndex = 0;
	private boolean isNeedUpload = false;
	
	public synchronized static CheckUploadManager getInstance()
	{
		if (mInstance == null)
		{
			mInstance = new CheckUploadManager(YunyouApplication.getInstance());
		}
		
		return mInstance;
	}
	
	private CheckUploadManager(Context context)
	{
		mContext = context;

		mNetworkCenter = NetworkCenterEx.getInstance();
		
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				switch(msg.what){
					case MSG_REFRESH:
						mList = (LinkedList<BikeLRecordResult>) msg.obj;
						startUpload(uploadIndex);
						break;
					case MSG_STARTUPLOAD:
						startUpload(uploadIndex);
						break;
						
				}
			}
			
		};

	}
	
	public void setDelListener(IDelObser listener){
		mObserver = listener;
	}
	
	
	public void reCheckUpload(){
		stopUpload();
		isNeedUpload = true;
		mList = new LinkedList<BikeType.BikeLRecordResult>();
		uploadIndex = 0;
		syncQueryRecord();
	}
	
	
	
	public void attachCurRecord(BikeType.BikeLRecordResult object){
		mCuRecordResult = object;
	}
	
	public void requestUpload(BikeType.BikeRecordUpload object)
	{
		
		mNetworkCenter.StartRequestToServer(BikeType.BIKE_RECORDUPLOAD_MASITD, object, this);

	}
	
	public void addRecord(BikeType.BikeLRecordResult object){
		mList.add(object);
	}
	
	public void attemptToUpload(){
		isNeedUpload = true;
		log.e("attemptToUpload mCuRecordResult = " + mCuRecordResult);
		if (mCuRecordResult != null){
			return ;
		}
		
		boolean ret = mHandler.hasMessages(MSG_STARTUPLOAD);
		log.e("attemptToUpload hasMessages = " + ret);
		if (ret){
			return ;
		}
		

		if (mList.size() != 0 && isNeedUpload){
			uploadIndex = 0;
			BikeLRecordResult result = mList.get(uploadIndex);
			attachCurRecord(result);
			BikeType.BikeRecordUpload upload = DataFactory.getBikeUpload(result);
			requestUpload(upload);

		}
	}
	
	private void startUpload(int index){
		log.e("startUpload index = " + index + ", isneedUplaod =  " + isNeedUpload);

		if (mList.size() != 0 && isNeedUpload){
			if (index < 0 ){
				index = 0;
			}else if (index >= mList.size()){
				index = 0;
			}
			uploadIndex = index;
			BikeLRecordResult result = mList.get(index);
			attachCurRecord(result);
			BikeType.BikeRecordUpload upload = DataFactory.getBikeUpload(result);
			requestUpload(upload);
		}
	}
	
	public void stopUpload(){
		log.e("stopUpload ");
		isNeedUpload = false;
		mHandler.removeMessages(MSG_STARTUPLOAD);
	}
	
	
	
	private void syncQueryRecord(){
		Message msg = mHandler.obtainMessage(MSG_REFRESH);	
		InnerThread innerThread = new InnerThread(msg);
		innerThread.start();
	}



	class InnerThread extends Thread
	{
		private Message message;
		public InnerThread(Message msg)
		{
			message = msg;
		}

		@Override
		public void run() {
			LinkedList<BikeType.BikeLRecordResult> groupList = new LinkedList<BikeType.BikeLRecordResult>();
			boolean ret = false;
			try {
				ret = rDbManager.queryAll(groupList);
			} catch (Exception e) {
				e.printStackTrace();
			}
			log.e("rDbManager.queryAll ret = " + ret + ", size = " + groupList.size());
			
			message.obj = groupList;
			message.sendToTarget();
		}
	}
	
	@Override
	public boolean onComplete(int requestAction, ResponseDataPacket dataPacket) {
	
		String jsString = "null";
		if (dataPacket != null)
		{
			jsString = dataPacket.toString();
		}
		
		log.e("requestAction = " + Utils.toHexString(requestAction) + "\nResponseDataPacket = \n" +jsString);
		
		if (dataPacket == null)
		{
			return false;
		}
		
		switch(requestAction)
		{
			case BikeType.BIKE_RECORDUPLOAD_MASITD:
			{
				onUploadResult(dataPacket);
			}
			break;
	
		}
		
		return true;
	}

	private void onUploadResult(ResponseDataPacket dataPacket)
	{
		boolean isDebug = YunyouApplication.getInstance().mIsDebug;
		if (dataPacket.rsp == 0)
		{
			log.e("upload fail...");
			if (isDebug){
				Utils.showToast(YunyouApplication.getInstance(), "upload fail...");
			}
			uploadIndex++;
			attachCurRecord(null);
			mHandler.sendEmptyMessageAtTime(MSG_STARTUPLOAD, DELAY_TIME);
			return ;
		}
		
		log.e("upload success...");
		if (isDebug){
			Utils.showToast(YunyouApplication.getInstance(), "upload success...");
		}
		
		RunRecordDBManager rDbManager = RunRecordDBManager.getInstance();
		boolean ret = false;
		if (mCuRecordResult != null){
			try {
				ret = rDbManager.delete(mCuRecordResult);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		log.e("rDbManager.delete ret = " + ret);
		if (ret && mObserver != null){
			mObserver.onDelRecord(mCuRecordResult);
		}
		if (mCuRecordResult != null){
			boolean ret2 = mList.remove(mCuRecordResult);
			log.e("mList.remove ret2 = " + ret2);
		}

		if (isDebug){
			Utils.showToast(YunyouApplication.getInstance(), "delete data from database ret = ..." + ret);
		}
		attachCurRecord(null);
		startUpload(uploadIndex);
	}
}
