package com.mobile.yunyou.bike.manager;

import java.util.LinkedList;

import com.mobile.yunyou.model.BikeType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.Utils;

public class RunRecordQueryProxy implements IRequestCallback{

	public static interface IRequestComplete{
		public void onGetBikeResult(boolean success);
	}
	
    private static RunRecordQueryProxy mInstance;
	
	private static final CommonLog log = LogFactory.createLog();
	
	private final static int QUERY_COUNT = 5;
	
	private NetworkCenterEx mNetworkCenter;
	private IRequestComplete mIRequestComplete;
	
	private LinkedList<BikeType.BikeLRecordResult> mDataArrays = new LinkedList<BikeType.BikeLRecordResult>();
	private int mSinceID = -1;
	
	
	public synchronized static RunRecordQueryProxy getInstance(NetworkCenterEx networkCenterEx)
	{
		if (mInstance == null)
		{
			mInstance = new RunRecordQueryProxy(networkCenterEx);
		}
		
		return mInstance;
	}
	
	private RunRecordQueryProxy(NetworkCenterEx networkCenterEx){
		mNetworkCenter = networkCenterEx;
	}
	
	public void setIRequestComplete(IRequestComplete listener){
		mIRequestComplete = listener;
	}
	
	public LinkedList<BikeType.BikeLRecordResult> getDataArray(){
		return mDataArrays;
	}
	
	public void requestLast(){
		mSinceID = -1;
		BikeType.BikeLRecord object = new BikeType.BikeLRecord();
		object.mOffset = 0;
		object.mNum = QUERY_COUNT;
		object.mSinceID = mSinceID;
		mNetworkCenter.StartRequestToServer(BikeType.BIKE_LRECORD_MASITD, object, this);
		
	}
	
	public void requestHistory(){
		BikeType.BikeLRecord object = new BikeType.BikeLRecord();
		object.mOffset = 0;
		object.mNum = QUERY_COUNT;
		object.mSinceID = mSinceID;
		mNetworkCenter.StartRequestToServer(BikeType.BIKE_LRECORD_MASITD, object, this);
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
			case BikeType.BIKE_LRECORD_MASITD:
				boolean rest = GetBikeResult(dataPacket);
				if (mIRequestComplete != null){
					mIRequestComplete.onGetBikeResult(rest);
				}
				break;
			default:
				break;
		}
		
		return true;
	}
	
	private boolean GetBikeResult(ResponseDataPacket dataPacket)
	{
		
		if (dataPacket.rsp == 0)
		{	
			return false;
		}
		
		boolean flag = false;
		BikeType.BikeLRecordResultGroup group = new BikeType.BikeLRecordResultGroup();
			
		try {
			group.parseString(dataPacket.data.toString());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		if (flag == false){
			return false;
		}
	
		LinkedList<BikeType.BikeLRecordResult> dataList = group.mBikeRecordList;
		int size = dataList.size();
		
		if (mSinceID == -1){
			mDataArrays = dataList;
			mSinceID = group.mSindID;
			log.e("dataList.size = " + mDataArrays.size() + ", msinceID = " + mSinceID);
		}else{
			mSinceID = group.mSindID;
			for(int i = 0; i < size; i++)
			{
				mDataArrays.addLast(dataList.get(i));	
			}
			log.e("dataList.size = " + mDataArrays.size() + ", msinceID = " + mSinceID);
		}
		
		return true;
		
		

	}
}
