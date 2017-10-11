package com.mobile.yunyou.bike.manager;

import android.content.Context;

import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.datastore.RunRecordDBManager;
import com.mobile.yunyou.model.BikeType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.Utils;


public class RunRecordUploadPoxy implements IRequestCallback{
	
	private NetworkCenterEx mNetworkCenter;
	private static final CommonLog log = LogFactory.createLog();

	private Context mContext;
	
	public static RunRecordUploadPoxy mInstance;
	
	private BikeType.BikeLRecordResult mCuRecordResult;

	public static synchronized RunRecordUploadPoxy getInstance()
	{
		if (mInstance == null)
		{
			mInstance = new RunRecordUploadPoxy();
		}
		
		return mInstance;
	}
	
	private RunRecordUploadPoxy()
	{
		mContext = YunyouApplication.getInstance();
		mNetworkCenter = NetworkCenterEx.getInstance();
	}
	
	public void requestUpload(BikeType.BikeRecordUpload object)
	{
		mNetworkCenter.StartRequestToServer(BikeType.BIKE_RECORDUPLOAD_MASITD, object, this);
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

		if (isDebug){
			Utils.showToast(YunyouApplication.getInstance(), "delete data from database ret = ..." + ret);
		}
	}
	
	public void attachCurRecord(BikeType.BikeLRecordResult object){
		mCuRecordResult = object;
	}
}
