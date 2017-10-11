package com.mobile.yunyou.map.data;

import android.location.Location;

public class LocationEx extends Location{


	private String mUpdateTime = "";
	private String mAdress = "";
	private String mCreateTime = "";
	private int mOnline = 0;
	private String mDID = "";
	private String mPowerDetail = "";
	
	private double mOffsetLat = 0;
	private double mOffsetLon = 0;
	private double mAccary = 0;
	
	public LocationEx(String provider) {
		super(provider);
	
	}
	
	
	public LocationEx(Location location){
		super(location);
	}
	
	
	public void SetObject(LocationEx locationEx)
	{
		mUpdateTime = locationEx.mUpdateTime;
		mAdress = locationEx.mAdress;
		mCreateTime = locationEx.mCreateTime;
		mOnline = locationEx.mOnline;
		mDID  = locationEx.mDID;
		mOffsetLat = locationEx.mOffsetLat;
		mOffsetLon = locationEx.mOffsetLon;
		
		setLatitude(locationEx.getLatitude());
		setLongitude(locationEx.getLongitude());
		setTime(locationEx.getTime());
		setProvider(locationEx.getProvider());
	}
	
	public void setUpdateTimeString(String time)
	{
		if (time != null)
		{
			mUpdateTime = time;
		}
	
	}
	
	
	
	public String getUpdateTimeString()
	{
		return mUpdateTime;
	}
	
	public void setCreateTimeString(String time)
	{
		if (time != null)
		{
			mCreateTime = time;
		}
	
	}
	
	public String getCreateTimeString()
	{
		return mCreateTime;
	}
	
	public void setAdress(String adress)
	{
		if (adress != null)
		{
			mAdress = adress;
		}
	
	}
	
	public String getAdress()
	{
		return mAdress;
	}

	public void setOnline(int status)
	{
		mOnline = status;
	}
	
	public int getOnline()
	{
		return mOnline;
	}
	
	public void setOffsetLonLat(double lat, double lon)
	{
		mOffsetLat = lat;
		mOffsetLon = lon;
	}
	
	public double getOffsetLat()
	{
		return mOffsetLat;
	}
	
	public double getOffsetLon()
	{
		return mOffsetLon;
	}
	
	public String getDID()
	{
		return mDID;
	}
	
	public void setDID(String did)
	{
		mDID = did;
	}
	
	public String getPowerDletai()
	{
		return mPowerDetail;
	}
	
	public void setPowerDletai(String value)
	{
		mPowerDetail = value;
	}
}
