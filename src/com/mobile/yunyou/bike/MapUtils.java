package com.mobile.yunyou.bike;

import java.text.ParseException;

import android.location.Location;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.mobile.yunyou.map.data.LocationEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.YunTimeUtils;

//import com.amap.api.maps2d.AMap;
//import com.amap.api.maps2d.model.CameraPosition;
//import com.amap.api.maps2d.model.LatLng;

public class MapUtils {

	private static final CommonLog log = LogFactory.createLog();
	
	public static float  getZoomLevel(AMap aMap){
		CameraPosition position = aMap.getCameraPosition();
		return position.zoom;
	}
	
	public static double getDistanByLatlon(LatLng latLng1, LatLng latLng2){
		float results[]  = new float[1];
		Location.distanceBetween(latLng1.latitude, latLng1.longitude, 
				latLng2.latitude,  latLng2.longitude, results);
		
		return  results[0];
	}
	
	public static boolean isMove(LocationEx locationEx)
	{
		int interval = 0;
		try {
			interval = getInterval(locationEx);
	
		} catch (ParseException e) {
			e.printStackTrace();
		}
	
		if (interval > 60 || interval < 0)
		{
			return false;
		}
		return true;
	}
	
	public static int getInterval(LocationEx locationEx) throws ParseException
	{
		long secondTime1 = YunTimeUtils.getTimeMillison(locationEx.getCreateTimeString());
		long secondTime2 = YunTimeUtils.getTimeMillison(locationEx.getUpdateTimeString());
		log.e("secondTime1 = " + secondTime1 + ", secondTime2 = " + secondTime2);
		
		int interval = (int) (secondTime2 - secondTime1);
		
		return interval;
	}
}
