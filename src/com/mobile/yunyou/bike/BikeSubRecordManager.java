package com.mobile.yunyou.bike;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;

import com.mobile.yunyou.R;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.map.util.WebManager;
import com.mobile.yunyou.model.BaseType;
import com.mobile.yunyou.model.BikeType;
import com.mobile.yunyou.model.BikeType.BikeRecordSubResult;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.Utils;


public class BikeSubRecordManager implements IRequestCallback{
	
	
	public static interface IBikeSubRecordResult
	{
		public void onRequestFail();
		public void onEmpty();
		public void onBikeSubRecordResult(LinkedList<BikeRecordSubResult> list);
	}

	private NetworkCenterEx mNetworkCenter;
	private IBikeSubRecordResult mListener;
	private static final CommonLog log = LogFactory.createLog();

	private Context mContext;
	private LinkedList<BikeRecordSubResult> mBikeSubRecordResultList = new LinkedList<BikeType.BikeRecordSubResult>(); 
	public static BikeSubRecordManager mInstance;

	public static synchronized BikeSubRecordManager getInstance()
	{
		if (mInstance == null)
		{
			mInstance = new BikeSubRecordManager();
		}
		
		return mInstance;
	}
	
	private BikeSubRecordManager()
	{
		mContext = YunyouApplication.getInstance();
		mNetworkCenter = NetworkCenterEx.getInstance();
	}
	
	public  void setLinkList(LinkedList<BikeRecordSubResult> list){
		mBikeSubRecordResultList = list;
	}
	
	public LinkedList<BikeRecordSubResult> getLinkList(){
		return mBikeSubRecordResultList;
	}
	
	public void RequestSubRecord(int id, IBikeSubRecordResult listener)
	{
		mListener = listener;
		BikeType.BikeRecordSub record = new BikeType.BikeRecordSub();
		record.mID = id;
		mNetworkCenter.StartRequestToServer(BikeType.BIKE_RECORDSUB_MASITD, record, this);
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
			case BikeType.BIKE_RECORDSUB_MASITD:
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
		
		final BikeType.BikeRecordSubResultGroup group = new BikeType.BikeRecordSubResultGroup();
		try {
			group.parseString(dataString);
			LinkedList<BikeRecordSubResult> mBikeSubRecordResultList = group.mBikeSubRecordResultList;
			if (mBikeSubRecordResultList.size() == 0){
				if (mListener != null){
					mListener.onEmpty();
				}
				return ;
			}
			
			InnerThread innerThread = new InnerThread(mBikeSubRecordResultList);
			innerThread.start();
		} catch (Exception e) {
			e.printStackTrace();
			Utils.showToast(mContext, R.string.analyze_data_fail);
		}
	}
	
	class InnerThread extends Thread
	{
		private LinkedList<BikeRecordSubResult> mBikeSubRecordResultList;

		
		public InnerThread(LinkedList<BikeRecordSubResult> list)
		{
			mBikeSubRecordResultList = list;
		}

		@Override
		public void run() {
			boolean flag = false;
			int size = mBikeSubRecordResultList.size();
			List<BaseType.BaseLocation> locationsList = new ArrayList<BaseType.BaseLocation>();
			for(int i = 0; i < size; i++)
			{
				BaseType.BaseLocation object = new BaseType.BaseLocation();
				object.lat = mBikeSubRecordResultList.get(i).mLat;
				object.lon = mBikeSubRecordResultList.get(i).mLon;
				locationsList.add(object);
			}
			
			try {
				
				List<BaseType.BaseLocation> locationsListresult = WebManager.correctPosToMap(locationsList);
				if (locationsListresult != null)
				{
					int size1 = locationsListresult.size();
					for(int i = 0; i < size1; i++)
					{
						mBikeSubRecordResultList.get(i).mOffsetLat = locationsListresult.get(i).lat;
						mBikeSubRecordResultList.get(i).mOffsetLon = locationsListresult.get(i).lon;
						flag = true;
					}
				}else{
					mBikeSubRecordResultList = null;
				}
				
				mListener.onBikeSubRecordResult(mBikeSubRecordResultList);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}
}
