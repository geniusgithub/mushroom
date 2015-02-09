package com.mobile.yunyou.bike;

import android.graphics.Color;

import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.mobile.yunyou.model.BikeType;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;



public class SafeMarket {

	private static final CommonLog log = LogFactory.createLog();

	private MarkerOptions mMarkerOptions;
	private int drawableID = 0;
	private BikeType.BikeGetArea mObject;
	
	private CircleOptions mCircleOptions;
	
	
	
	public SafeMarket(int drawableID){
		this.drawableID = drawableID;
		mMarkerOptions = new MarkerOptions();
		mMarkerOptions.draggable(true);

		
		initCircle();
	}
	
	public MarkerOptions newMarkerOptions(){
		if (mObject != null && drawableID != 0){
			mMarkerOptions.icon(BitmapDescriptorFactory.fromResource(drawableID));
			mMarkerOptions.position(getLatLon());
			mMarkerOptions.anchor(0.5f, 0.5f);
		}

		return mMarkerOptions;
	}
	
	private void initCircle()
	{
		mCircleOptions = new CircleOptions();
		mCircleOptions.strokeColor(Color.argb(50, 1, 1, 100));
		mCircleOptions.fillColor(Color.argb(50, 1, 1, 100));
		mCircleOptions.strokeWidth(1);
	}

	
	public void setArea(BikeType.BikeGetArea object)
	{
		if (object != null)
		{
			log.e("SafeMarket setArea...lat = " + object.mLat + ", lon = " + object.mLon);
			mObject = object;		
		}
	}
	

	public BikeType.BikeGetArea getArea()
	{
		return mObject;
	}
	
	public LatLng getLatLon(){
		if (mObject != null){
			LatLng latLng = new LatLng(mObject.mOffsetLat, mObject.mOffsetLon);
			return latLng;
		}
		
		return null;
	}
	
	public CircleOptions newCircleOptions(){
		if (mObject != null){
			mCircleOptions.center(getLatLon());
			mCircleOptions.radius(mObject.mRadius);
			return mCircleOptions;
		}
		
		return null;
	}
	
}
