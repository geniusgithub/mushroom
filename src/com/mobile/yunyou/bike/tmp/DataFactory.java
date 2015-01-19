package com.mobile.yunyou.bike.tmp;

import com.mobile.yunyou.model.BikeType.BikeLRecordResult;
import com.mobile.yunyou.model.BikeType.BikeRecordUpload;


public class DataFactory {

	public BikeRecordUpload getBikeUpload(BikeLRecordResult object){
		BikeRecordUpload upload = new BikeRecordUpload();
		
		upload.mTotalDistance = object.mTotalDistance;
		upload.mStartTime = object.mStartTime;
		upload.mEndTime = object.mEndTime;
		upload.mCal = object.mCal;
		upload.mHeight = object.mHeight;
		upload.mHSpeed = object.mHSpeed;
		upload.mLSpeed = object.mLSpeed;
		
		upload.mBikeRecordList = object.mBikeRecordList;
		
		
		return upload;
		
	}
}
