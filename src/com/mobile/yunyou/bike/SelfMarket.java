package com.mobile.yunyou.bike;
import android.graphics.Color;

import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.mobile.yunyou.map.data.LocationEx;


public class SelfMarket{

	private LocationEx mLastLocation;
	private MarkerOptions mMarkerOptions;
	private int drawableID = 0;
	
	private CircleOptions mCircleOptions;
	
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
			mLastLocation = location;	
		}
	}
	
	public LocationEx getLocation(){
		return mLastLocation;
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
