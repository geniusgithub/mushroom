package com.mobile.yunyou.bike;
import android.graphics.Color;
import android.util.Log;

import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.mobile.yunyou.map.data.LocationEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;


public class SelfMarket{

	private static final CommonLog log = LogFactory.createLog();
	
	private LocationEx mLastLocation;
	private MarkerOptions mMarkerOptions;
	private int drawableID = 0;
	
	private CircleOptions mCircleOptions;
	
	private boolean isNeedUpdateLocation = true;
	
	public SelfMarket(int drawableID){
		this.drawableID = drawableID;
		mMarkerOptions = new MarkerOptions();
		mMarkerOptions.draggable(true);
		
		initCircle();
	}
	
	private void initCircle()
	{
		mCircleOptions = new CircleOptions();
		mCircleOptions.strokeColor(Color.argb(50, 1, 1, 100));
		mCircleOptions.fillColor(Color.argb(50, 1, 1, 100));
		mCircleOptions.strokeWidth(1);
	}
	
	public MarkerOptions newMarkerOptions(){
		if (mLastLocation != null && drawableID != 0){
			mMarkerOptions.icon(BitmapDescriptorFactory.fromResource(drawableID));
			mMarkerOptions.position(new LatLng(mLastLocation.getOffsetLat(), mLastLocation.getOffsetLon()));
		}

		return mMarkerOptions;
	}
	
	
	public void setLocation(LocationEx location){
		if (location != null){

			setUpdateFlag(true);
			if (mLastLocation != null){
				double distance = MapUtils.getDistanByLatlon(mLastLocation, location);
				log.e("selfLocation distance = " + distance);
				if (distance < 1){
					setUpdateFlag(false);
				}
			}
			
			mLastLocation = location;	
		}
	}
	
	public LocationEx getLocation(){
		return mLastLocation;
	}
	
	public void setUpdateFlag(boolean flag){
		isNeedUpdateLocation = flag;
	}
	
	public boolean isNeedUpdate(){
		return isNeedUpdateLocation;
	}
	
	public LatLng getLatLon(){
		if (mLastLocation != null){
			LatLng latLng = new LatLng(mLastLocation.getOffsetLat(), mLastLocation.getOffsetLon());
			return latLng;
		}
		
		return null;
	}
	
	
	public CircleOptions newCircleOptions(){
		if (mLastLocation != null){
			mCircleOptions.center(getLatLon());
			mCircleOptions.radius(mLastLocation.getAccuracy());
			return mCircleOptions;
		}
		
		return null;
	}
	
}
