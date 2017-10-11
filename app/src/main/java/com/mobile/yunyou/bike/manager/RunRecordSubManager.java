package com.mobile.yunyou.bike.manager;

import java.util.LinkedList;

import android.content.Context;

import com.mobile.yunyou.R;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.model.BikeType;
import com.mobile.yunyou.model.BikeType.BikeLRecordSubResult;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.Utils;


public class RunRecordSubManager implements IRequestCallback{
	
	
	public static interface IBikeSubRecordResult
	{
		public void onRequestFail();
		public void onEmpty();
		public void onBikeSubRecordResult(LinkedList<BikeLRecordSubResult> mBikeSubRecordResultList);
	}

	private NetworkCenterEx mNetworkCenter;
	private IBikeSubRecordResult mListener;
	private static final CommonLog log = LogFactory.createLog();

	private Context mContext;
	//private LinkedList<BikeLRecordSubResult> mBikeSubRecordResultList = new LinkedList<BikeType.BikeLRecordSubResult>(); 
	public static RunRecordSubManager mInstance;

	public static synchronized RunRecordSubManager getInstance()
	{
		if (mInstance == null)
		{
			mInstance = new RunRecordSubManager();
		}
		
		return mInstance;
	}
	
	private RunRecordSubManager()
	{
		mContext = YunyouApplication.getInstance();
		mNetworkCenter = NetworkCenterEx.getInstance();
	}
	
//	public  void setLinkList(LinkedList<BikeLRecordSubResult> list){
//		mBikeSubRecordResultList = list;
//	}
//	
//	public LinkedList<BikeLRecordSubResult> getLinkList(){
//		return mBikeSubRecordResultList;
//	}
	
	public void RequestSubRecord(int id, IBikeSubRecordResult listener)
	{
		mListener = listener;
		BikeType.BikeLRecordSub record = new BikeType.BikeLRecordSub();
		record.mID = id;
		mNetworkCenter.StartRequestToServer(BikeType.BIKE_LRECORDSUB_MASITD, record, this);
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
			if (mListener != null){
				mListener.onRequestFail();
			}
			return false;
		}
		switch(requestAction)
		{
			case BikeType.BIKE_LRECORDSUB_MASITD:
			{
				onBikeSubRecordResult(dataPacket);
			}
			break;

		}
		
		return true;
	}
	
	private void onBikeSubRecordResult(ResponseDataPacket dataPacket)
	{
		if (dataPacket.rsp == 0)
		{
			if (mListener != null){
				mListener.onRequestFail();
			}
			return ;
		}
		
		String dataString = dataPacket.dataArray.toString();
		if (dataString.isEmpty()){
			if (mListener != null){
				mListener.onEmpty();
			}
			return ;
		}
		
		final BikeType.BikeLRecordSubResultGroup group = new BikeType.BikeLRecordSubResultGroup();
		try {
			group.parseString(dataString);
			LinkedList<BikeLRecordSubResult> mBikeSubRecordResultList = group.mBikeSubRecordResultList;
			if (mBikeSubRecordResultList.size() == 0){
				if (mListener != null){
					mListener.onEmpty();
				}
				return ;
			}
			
			if (mListener != null){
				mListener.onBikeSubRecordResult(mBikeSubRecordResultList);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			Utils.showToast(mContext, R.string.analyze_data_fail);
			if (mListener != null){
				mListener.onRequestFail();
			}
		}
	}
	
}
