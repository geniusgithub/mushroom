//package com.mobile.yunyou.bike.manager;
//
//import java.util.HashSet;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Set;
//
//import android.view.View;
//
//import com.mobile.yunyou.R;
//import com.mobile.yunyou.YunyouApplication;
//import com.mobile.yunyou.model.BikeType;
//import com.mobile.yunyou.model.DeviceSetType;
//import com.mobile.yunyou.model.ResponseDataPacket;
//import com.mobile.yunyou.model.DeviceSetType.DeviceMsgData;
//import com.mobile.yunyou.network.IRequestCallback;
//import com.mobile.yunyou.network.NetworkCenterEx;
//import com.mobile.yunyou.util.CommonLog;
//import com.mobile.yunyou.util.LogFactory;
//import com.mobile.yunyou.util.Utils;
//
//public class BikeRecordProxy implements IRequestCallback{
//
//	public static interface IRequestComplete{
//		public void onGetBikeResult(boolean success);
//	}
//	
//    private static BikeRecordProxy mInstance;
//	
//	private static final CommonLog log = LogFactory.createLog();
//	
//	private final static int QUERY_COUNT = 5;
//	
//	private NetworkCenterEx mNetworkCenter;
//	private IRequestComplete mIRequestComplete;
//	
//	private LinkedList<BikeType.BikeRecordResult> mDataArrays = new LinkedList<BikeType.BikeRecordResult>();
//	private int mSinceID = -1;
//	
//	
//	public synchronized static BikeRecordProxy getInstance(NetworkCenterEx networkCenterEx)
//	{
//		if (mInstance == null)
//		{
//			mInstance = new BikeRecordProxy(networkCenterEx);
//		}
//		
//		return mInstance;
//	}
//	
//	private BikeRecordProxy(NetworkCenterEx networkCenterEx){
//		mNetworkCenter = networkCenterEx;
//	}
//	
//	public void setIRequestComplete(IRequestComplete listener){
//		mIRequestComplete = listener;
//	}
//	
//	public LinkedList<BikeType.BikeRecordResult> getDataArray(){
//		return mDataArrays;
//	}
//	
//	public void requestLast(){
//		mSinceID = -1;
//		BikeType.BikeRecord object = new BikeType.BikeRecord();
//		object.mOffset = 0;
//		object.mNum = QUERY_COUNT;
//		object.mSinceID = mSinceID;
//		mNetworkCenter.StartRequestToServer(BikeType.BIKE_RECORD_MASITD, object, this);
//		
//	}
//	
//	public void requestHistory(){
//		BikeType.BikeRecord object = new BikeType.BikeRecord();
//		object.mOffset = 0;
//		object.mNum = QUERY_COUNT;
//		object.mSinceID = mSinceID;
//		mNetworkCenter.StartRequestToServer(BikeType.BIKE_RECORD_MASITD, object, this);
//	}
//
//
//	@Override
//	public boolean onComplete(int requestAction, ResponseDataPacket dataPacket) {
//		String jsString = "null";
//		if (dataPacket != null)
//		{
//			jsString = dataPacket.toString();
//		}
//		
//		log.e("requestAction = " + Utils.toHexString(requestAction) + "\nResponseDataPacket = \n" +jsString);
//		
//		switch(requestAction)
//		{
//			case BikeType.BIKE_RECORD_MASITD:
//				boolean rest = GetBikeResult(dataPacket);
//				if (mIRequestComplete != null){
//					mIRequestComplete.onGetBikeResult(rest);
//				}
//				break;
//			default:
//				break;
//		}
//		
//		return true;
//	}
//	
//	private boolean GetBikeResult(ResponseDataPacket dataPacket)
//	{
//		
//		if (dataPacket.rsp == 0)
//		{	
//			return false;
//		}
//		
//		boolean flag = false;
//		BikeType.BikeRecordResultGroup group = new BikeType.BikeRecordResultGroup();
//			
//		try {
//			group.parseString(dataPacket.data.toString());
//			flag = true;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	
//		if (flag == false){
//			return false;
//		}
//	
//		LinkedList<BikeType.BikeRecordResult> dataList = group.mBikeRecordResultList;
//		int size = dataList.size();
//		
//		if (mSinceID == -1){
//			mDataArrays = dataList;
//			mSinceID = group.mSindID;
//			log.e("dataList.size = " + mDataArrays.size() + ", msinceID = " + mSinceID);
//		}else{
//			mSinceID = group.mSindID;
//			for(int i = 0; i < size; i++)
//			{
//				mDataArrays.addLast(dataList.get(i));	
//			}
//			log.e("dataList.size = " + mDataArrays.size() + ", msinceID = " + mSinceID);
//		}
//		
//		return true;
//		
//		
//
//	}
//}
