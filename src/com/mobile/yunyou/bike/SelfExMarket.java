package com.mobile.yunyou.bike;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.location.LocationManager;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.mobile.yunyou.R;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.map.data.LocationEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.Utils;

public class SelfExMarket {

	private static final CommonLog log = LogFactory.createLog();
	
	private LocationEx mLastLocation;

	private MarkerOptions mMarkerOptionsStart;
	private int drawableIDStart = 0;
	private Marker mLastMarket;
	
	private LatLng mPreLatLng;
	
	private CircleOptions mCircleOptions;
	private Circle mCircle;
	
	public SelfExMarket(int drawableStart){
		drawableIDStart = drawableStart;
		
		mMarkerOptionsStart = new MarkerOptions();
		mMarkerOptionsStart.draggable(true);
		
		initCircle();
	}
	
	private void initCircle()
	{
		mCircleOptions = new CircleOptions();
		mCircleOptions.strokeColor(Color.argb(50, 1, 1, 100));
		mCircleOptions.fillColor(Color.argb(50, 1, 1, 100));
		mCircleOptions.strokeWidth(1);
	}
	
	public void attchMarket(Marker marker){
		mLastMarket = marker;
	}
	
	public Marker getMarket(){
		return mLastMarket;
	}
	
	public void attchCircle(Circle circle){
		mCircle = circle;
	}
	
	
	
	public Circle getCircle(){
		return mCircle;
	}

	public void updateCircle(){
		if (mCircle != null){
			if (mLastLocation != null){
				mCircle.setCenter(getLastLatLon());
				mCircle.setRadius(mLastLocation.getAccuracy());
			}

		}
	}
	
	public void clear(){
		reset();
		mLastLocation = null;
	}
	
	public void reset(){
		mLastMarket = null;
		mPreLatLng = null;
		mCircle = null;
	}


	public MarkerOptions newStartMarkerOptions(){
		if (mLastLocation != null && drawableIDStart != 0){
			mMarkerOptionsStart.icon(BitmapDescriptorFactory.fromResource(drawableIDStart));
			LatLng latLng = getLastLatLon();
			if (latLng != null){
				mMarkerOptionsStart.position(latLng);
			}
		}

		return mMarkerOptionsStart;
	}
	
	public CircleOptions newCircleOptions(){
		if (mLastLocation != null){
			mCircleOptions.center(getLastLatLon());
			mCircleOptions.radius(mLastLocation.getAccuracy());
			return mCircleOptions;
		}
		
		return null;
	}
	
	
	public boolean setLocation(LocationEx location){
		if (location != null){
			log.e("setLocation...");				

			mLastLocation = location;
			LatLng latLng = new LatLng(location.getOffsetLat(), location.getOffsetLon());
			mPreLatLng = latLng;

			return true;
		}
		
		return false;
	}
	

	
	public LocationEx getLastLocation(){
		return mLastLocation;
	}

	
	public LatLng getLastLatLon(){
		if (mLastLocation != null){
			LatLng latLng = new LatLng(mLastLocation.getOffsetLat(), mLastLocation.getOffsetLon());
			return latLng;
		}
		
		return null;
	}
	

}
