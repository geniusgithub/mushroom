package com.mobile.yunyou.bike.tmp;

import java.util.LinkedList;

import org.json.JSONException;
import org.json.JSONObject;

import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.model.BikeType.BikeLRecordResult;
import com.mobile.yunyou.model.BikeType.BikeLRecordSubResult;
import com.mobile.yunyou.model.BikeType.BikeRecordUpload;
import com.mobile.yunyou.model.BikeType.MinLRunRecord;
import com.mobile.yunyou.network.ClientEngineEx;
import com.mobile.yunyou.util.Utils;



public class DataFactory {

	public static BikeRecordUpload getBikeUpload(BikeLRecordResult object){
		BikeRecordUpload upload = new BikeRecordUpload();
		
		upload.mTotalDistance = object.mTotalDistance;
		upload.mStartTime = object.mStartTime;
		upload.mEndTime = object.mEndTime;
		upload.mCal = object.mCal;
		upload.mHeight = object.mHeight;
		upload.mHSpeed = object.mHSpeed;
		upload.mLSpeed = object.mLSpeed;
		
		LinkedList<MinLRunRecord> mBikeRecordList = upload.mBikeRecordList;
		LinkedList<BikeLRecordSubResult> mBikeSubRecordResultList = object.mBikeSubRecordResultList;
		int size = mBikeSubRecordResultList.size();
		for(int i = 0; i < size; i++){
			BikeLRecordSubResult subResult = mBikeSubRecordResultList.get(i);
			MinLRunRecord record = getLRunRecord(subResult);
			mBikeRecordList.add(record);			
		}
		
		return upload;
		
	}
	
	public static MinLRunRecord getLRunRecord(BikeLRecordSubResult object){
		MinLRunRecord record = new MinLRunRecord();
		record.mLat = object.mLat;
		record.mLon = object.mLon;
		record.mType = object.mType;
		record.mHeight = 0;
		record.mCreateTime = object.mCreateTime;
		return record;
	}
	
	
	
	public static String buildUploadJsString(String cmd, String sid, String did) throws Exception{
		ClientEngineEx clientEngineEx = new ClientEngineEx();
		clientEngineEx.setDid(did);
		clientEngineEx.setSid(sid);
		
		JSONObject jsonObject = clientEngineEx.getInitJsonObject();
		jsonObject.put("cmd", "deviceset_avatar");
		jsonObject.put("data", new JSONObject());
		return jsonObject.toString();
	}
	
}
