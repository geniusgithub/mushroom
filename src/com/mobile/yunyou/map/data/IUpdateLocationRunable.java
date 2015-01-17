package com.mobile.yunyou.map.data;

import android.location.Location;

public interface IUpdateLocationRunable extends Runnable{

	public void setLocation(LocationEx location);
	
}
