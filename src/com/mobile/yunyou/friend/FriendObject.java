package com.mobile.yunyou.friend;

import com.mobile.yunyou.model.GloalType.DeviceInfoEx;
import com.mobile.yunyou.network.api.HeadFileConfigure;
import com.mobile.yunyou.util.FileManager;

public class FriendObject {

	public  String mHeadUrl = "";
	public  String mUserName = "";
	public  String mIndividualSign = "";
	public  String mPhone = "";
	public  String mDid = "";
	
	public FriendObject()
	{
		
	}
	
	public FriendObject(DeviceInfoEx infoEx)
	{
		mUserName = infoEx.mAlias;
		mHeadUrl = HeadFileConfigure.getRequestUri(infoEx);
		mDid = infoEx.mDid;
	}
	
	
}
