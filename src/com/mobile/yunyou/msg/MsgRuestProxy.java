package com.mobile.yunyou.msg;

import java.util.LinkedList;
import java.util.Set;

import com.mobile.yunyou.model.DeviceSetType;
import com.mobile.yunyou.model.DeviceSetType.DeviceMsgData;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.Utils;

public class MsgRuestProxy implements IRequestCallback{

	public static interface IRequestComplete{
		public void onGetResult(boolean success);
	}
	
	   private static MsgRuestProxy mInstance;
	
	private static final CommonLog log = LogFactory.createLog();
	
	private final static int QUERY_COUNT = 20;
	
	private NetworkCenterEx mNetworkCenter;
	private IRequestComplete mIRequestComplete;
	
	private LinkedList<DeviceMsgData> mDataArrays = new LinkedList<DeviceMsgData>();
	private int mSinceID = -1;
	
	
	public synchronized static MsgRuestProxy getInstance(NetworkCenterEx networkCenterEx)
	{
		if (mInstance == null)
		{
			mInstance = new MsgRuestProxy(networkCenterEx);
		}
		
		return mInstance;
	}
	
	private MsgRuestProxy(NetworkCenterEx networkCenterEx){
		mNetworkCenter = networkCenterEx;
	}
	
	public void setIRequestComplete(IRequestComplete listener){
		mIRequestComplete = listener;
	}
	
	public LinkedList<DeviceMsgData> getDataArray(){
		return mDataArrays;
	}
	
	public void requestLast(int unreadCount){
		mSinceID = -1;
		DeviceSetType.DeviceRequestMsg object = new DeviceSetType.DeviceRequestMsg();
		object.mOffset = 0;
		object.mNum = Math.max(QUERY_COUNT, unreadCount);
		object.mSinceID = mSinceID;
		mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_GET_MSG_MASID, object, this);
		
	}
	
	public void requestHistory(){
		DeviceSetType.DeviceRequestMsg object = new DeviceSetType.DeviceRequestMsg();
		object.mOffset = 0;
		object.mNum = QUERY_COUNT;
		object.mSinceID = mSinceID;
		mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_GET_MSG_MASID, object, this);
	}
	
	public void delMsgList(Set<Integer> set){
		for(int i = 0; i < mDataArrays.size(); ){
			int ID = mDataArrays.get(i).mID;
			if (set.contains(ID)){		
				log.e("size = " + "remove ID = " + ID + ", i = " + i);
				set.remove(ID);
				mDataArrays.remove(i);
			}else{
				i++;
			}
		}
	}
	
//	public void delMsgList(List<DeviceMsgData> list){
//		int size = list.size();
//		Set<Integer> set = new HashSet<Integer>();
//		for(int i = 0; i < size; i++){
//			set.add(list.get(i).mID);
//		}
//		size = mDataArrays.size();
//		for(int i = 0; i < size; ){
//			int ID = mDataArrays.get(i).mID;
//			if (set.contains(ID)){
//				set.remove(ID);
//				mDataArrays.remove(i);
//			}else{
//				i++;
//			}
//		}
//	}
	


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
			case DeviceSetType.DEVICE_GET_MSG_MASID:
				boolean rest = GetMsgResult(dataPacket);
				if (mIRequestComplete != null){
					mIRequestComplete.onGetResult(rest);
				}
				break;
			default:
				break;
		}
		
		return true;
	}
	
	private boolean GetMsgResult(ResponseDataPacket dataPacket)
	{
		
		if (dataPacket.rsp == 0)
		{	
			return false;
		}
		
		boolean flag = false;
		DeviceSetType.DeviceMsgDataGroup group = new DeviceSetType.DeviceMsgDataGroup();
			
		try {
			group.parseString(dataPacket.data.toString());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		if (flag == false){
			return false;
		}
	
		LinkedList<DeviceMsgData> dataList = group.mDeviceMsglist;
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
