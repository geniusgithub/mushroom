package com.mobile.yunyou.network;

import com.mobile.yunyou.model.IToJsonObject;

public class Courier {

	public String mDid = "";
	public int mAction = 0;
	public IRequestCallback mCallback;
	public IToJsonObject mToJsonObject;
	
	public Courier(IToJsonObject object, int msgID, IRequestCallback callback)
	{
		mToJsonObject = object;
		mAction = msgID;
		mCallback = callback;
	}
	
	public Courier(IToJsonObject object, int msgID, IRequestCallback callback, String did)
	{
		mToJsonObject = object;
		mAction = msgID;
		mCallback = callback;
		mDid = did;
	}
}
