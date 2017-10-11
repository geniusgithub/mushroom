package com.mobile.yunyou.model;

public class UserInfoEx {

	public String mAccountName = "yunyou2012";
	public String mPassword = "123456";
	
	public String mTrueName = "king";
	public String mSex = "M";
	public String mPhone = "13067306371";
	public String mEmail = "yunyou@163.com";
	public String mAddr = "google";
	public String mBirthday= "1990-01-01";

	public UserInfoEx()
	{
		
	}
	
	public UserInfoEx(UserInfoEx userInfoEx)
	{
		mAccountName = userInfoEx.mAccountName;
		mTrueName = userInfoEx.mTrueName;
		mSex = userInfoEx.mSex;
		mPhone = userInfoEx.mPhone;
		mEmail = userInfoEx.mEmail;
		mAddr = userInfoEx.mAddr;
		mBirthday = userInfoEx.mBirthday;
	}
}
