package com.mobile.yunyou.bike.manager;

import org.json.JSONObject;

import android.content.Context;

import com.mobile.yunyou.R;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.model.BikeType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.Utils;


public class SafeAreaManager implements IRequestCallback{
	
	
	public static interface ISafeAreaResult
	{
		public void onRequestFail();
		public void onEmpty();
		public void onSafeArea(BikeType.BikeGetArea area);
	}

	private NetworkCenterEx mNetworkCenter;
	private ISafeAreaResult mListener;
	private static final CommonLog log = LogFactory.createLog();

	private Context mContext;
	
	public static SafeAreaManager mInstance;

	public static synchronized SafeAreaManager getInstance()
	{
		if (mInstance == null)
		{
			mInstance = new SafeAreaManager();
		}
		
		return mInstance;
	}
	
	private SafeAreaManager()
	{
		mContext = YunyouApplication.getInstance();
		mNetworkCenter = NetworkCenterEx.getInstance();
	}
	
	public void RequestSafeArea(ISafeAreaResult listener)
	{
		mListener = listener;
		mNetworkCenter.StartRequestToServer(BikeType.BIKE_GETAREA_MASITD, null, this);
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
			case BikeType.BIKE_GETAREA_MASITD:
			{
				onBikeAreaResult(dataPacket);
			}
			break;

		}
		
		return true;
	}
	
	private void onBikeAreaResult(ResponseDataPacket dataPacket)
	{
		if (dataPacket.rsp == 0)
		{
			if (mListener != null){
				mListener.onRequestFail();
			}
			return ;
		}
		
		String dataString = dataPacket.data.toString();
		log.e("dataString = " + dataString);
		JSONObject jsonObject = new JSONObject();
		if (dataString.isEmpty() || dataString.equalsIgnoreCase("{}")){
			
			if (mListener != null){
				mListener.onEmpty();
			}
			return ;
		}
		
		final BikeType.BikeGetArea area = new BikeType.BikeGetArea();
		try {
			area.parseString(dataString);
			
			InnerThread innerThread = new InnerThread(area);
			innerThread.start();
		} catch (Exception e) {
			e.printStackTrace();
			Utils.showToast(mContext, R.string.analyze_data_fail);
		}
	}
	
	class InnerThread extends Thread
	{
		private BikeType.BikeGetArea mArea;

		
		public InnerThread(BikeType.BikeGetArea object)
		{
			mArea = object;
		}

		@Override
		public void run() {
			boolean flag = false;
			mArea.mOffsetLat = mArea.mLat;
			mArea.mOffsetLon = mArea.mLon;
			
//			Location location = WebManager.correctPosToMap(mArea.mLat, mArea.mLon);
//			if (location != null)
//			{
//				mArea.mOffsetLat = location.getLatitude();
//				mArea.mOffsetLon = location.getLongitude();
//
//				log.d("get mArea.mOffsetLat = " + mArea.mOffsetLat + ", mArea.mOffsetLon = " + mArea.mOffsetLon);
//	
//			}else{
//				mArea = null;
//			}
			mListener.onSafeArea(mArea);
		}
		
		
	}
}
