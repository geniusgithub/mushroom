package com.mobile.yunyou.bike.tmp;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;

import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;

public class RunBikeMarket {

	private static final CommonLog log = LogFactory.createLog();

	private MarkerOptions mMarkerOptionsCurrent;
	private int drawableIDCurrent = 0;
	
	private LatLng mLastLatLng;
	private List<LatLng> mLatLngList = new ArrayList<LatLng>();
	private List<PolylineOptions> mPLineList = new ArrayList<PolylineOptions>();

	
	public RunBikeMarket(int drawableCurrent){
		drawableIDCurrent = drawableCurrent;
		
		mMarkerOptionsCurrent = new MarkerOptions();
		mMarkerOptionsCurrent.draggable(true);
	}

	public void reset(){
		mLastLatLng = null;
		mLatLngList.clear();
		mPLineList.clear();
	}
	
	public MarkerOptions newCurrentMarkerOptions(){
		if (mLastLatLng != null && drawableIDCurrent != 0){
			mMarkerOptionsCurrent.icon(BitmapDescriptorFactory.fromResource(drawableIDCurrent));
			if (mLastLatLng != null){
				mMarkerOptionsCurrent.position(mLastLatLng);
			}
		}

		return mMarkerOptionsCurrent;
	}
	
	public void addLocation(LatLng latlon){
		if (latlon != null){
			log.e("RunBikeMarket addLocation...(" + latlon.latitude + "," + latlon.longitude);
			
			if (mLastLatLng == null){
				mLatLngList.add(latlon);
				mLastLatLng = latlon;
				return ;
			}
			
			mLatLngList.add(latlon);
			
			PolylineOptions options = new PolylineOptions();
			options.add(mLastLatLng, latlon);
			options.width(5);
			options.color(Color.RED);	
			mPLineList.add(options);
			log.e("mPLineList.size = " + mPLineList.size());
			mLastLatLng = latlon;
		}
	}
	
	public List<PolylineOptions> getPLineList (){
		return mPLineList;
	}
	

	
	public LatLng getLastLatLon(){
		 return mLastLatLng;
	}
	
}
