package com.mobile.yunyou.bike.tmp;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.location.LocationManager;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.mobile.yunyou.R;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.bike.MapUtils;
import com.mobile.yunyou.map.data.LocationEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.Utils;

public class RunBikeMarket {

	private static final CommonLog log = LogFactory.createLog();
	
	private LocationEx mFirstLocation;
	private LocationEx mLastLocation;

	private MarkerOptions mMarkerOptionsStart;
	private MarkerOptions mMarkerOptionsCurrent;
	private int drawableIDStart = 0;
	private int drawableIDEnd = 0;
	private Marker mLastMarket;
	
	private LatLng mPreLatLng;
	private List<LatLng> mLatLngList = new ArrayList<LatLng>();
	private List<PolylineOptions> mPLineList = new ArrayList<PolylineOptions>();
	private PolylineOptions mLastPolyline;
	//private LatLngBounds mBounds = null;
	
	//private boolean isRunning = true;
	
	
	
	
	public RunBikeMarket(int drawableStart, int drawableEnd){
		drawableIDStart = drawableStart;
		drawableIDEnd = drawableEnd;
		
		mMarkerOptionsStart = new MarkerOptions();
		mMarkerOptionsStart.draggable(true);
		
		mMarkerOptionsCurrent = new MarkerOptions();
		mMarkerOptionsCurrent.draggable(true);
	}
	
	public void attchMarket(Marker marker){
		mLastMarket = marker;
	}
	
	public Marker getMarket(){
		return mLastMarket;
	}
	

	public void clear(){
		reset();
		
		mLastLocation = null;
		mFirstLocation = null;
	}
	public void reset(){
		mPreLatLng = null;
		mLatLngList.clear();
		mPLineList.clear();
		mFirstLocation = mLastLocation;
	//	isRunning = true;
	//	mBounds = null;
		mLastPolyline = null;
	}

	public MarkerOptions newStartMarkerOptions(){
		if (mFirstLocation != null && drawableIDStart != 0){
			mMarkerOptionsStart.icon(BitmapDescriptorFactory.fromResource(drawableIDStart));

			LatLng latLng = getFirstLatLon();
			if (latLng != null){
				mMarkerOptionsStart.position(latLng);
			}
		}

		return mMarkerOptionsStart;
	}
	
	
	public MarkerOptions newCurrentMarkerOptions(){
		if (mLastLocation != null && drawableIDEnd != 0){
			mMarkerOptionsStart.icon(BitmapDescriptorFactory.fromResource(drawableIDEnd));
	
			LatLng latLng = getLastLatLon();
			if (latLng != null){
				mMarkerOptionsCurrent.position(latLng);
			}
		}

		return mMarkerOptionsCurrent;
	}
	
	public boolean addLocation(LocationEx location){
		if (location != null){
			log.e("addLocation...");		

			if (mLastLocation != null){
				double distance = MapUtils.getDistanByLatlon(mLastLocation, location);
				log.e("BikeMarket distance = " + distance);
				if (distance < 1){
					return false;
				}
			}
			

			mLastLocation = location;
			LatLng latLng = new LatLng(location.getOffsetLat(), location.getOffsetLon());
			mLatLngList.add(latLng);

			if (mPreLatLng == null){
				mFirstLocation = location;
				mPreLatLng = latLng;
//				if (mBounds == null){
//					mBounds = new LatLngBounds(getFirstLatLon(), getLastLatLon());
//				}
				return true;
			}

	
			PolylineOptions options = new PolylineOptions();
			options.add(mPreLatLng, latLng);
			options.width(5);
			options.color(Color.RED);
			mPLineList.add(options);
			log.e("mPLineList.size = " + mPLineList.size());
			mPreLatLng = latLng;
			mLastPolyline = options;
//			mBounds.including(latLng);
			
			return true;
		}
		
		return false;
	}
	
	public PolylineOptions getLastPolyline(){
		return mLastPolyline;
	}
	public List<PolylineOptions> getPLineList (){
		return mPLineList;
	}
	
//	public void setFirstLocation(LocationEx location){
//		if (location != null){
//			mFirstLocation = location;	
//		}
//	}
	
	public LocationEx getFirstLocation(){
		return mFirstLocation;
	}
	
//	public void setLastLocation(LocationEx location){
//		if (location != null){
//			mLastLocation = location;	
//			mFirstLocation = location;
////			if (mBounds == null){
////				mBounds = new LatLngBounds(getFirstLatLon(), getLastLatLon());
////			}
//		}
//	}
	
	public LocationEx getLastLocation(){
		return mLastLocation;
	}
	
	public LatLng getFirstLatLon(){
		if (mFirstLocation != null){
			LatLng latLng = new LatLng(mFirstLocation.getOffsetLat(), mFirstLocation.getOffsetLon());
			return latLng;
		}
		
		return null;
	}
	
	public LatLng getLastLatLon(){
		if (mLastLocation != null){
			LatLng latLng = new LatLng(mLastLocation.getOffsetLat(), mLastLocation.getOffsetLon());
			return latLng;
		}
		
		return null;
	}
	
//	public LatLngBounds getBound(){
//		return mBounds;
//	}
	
//	public void render(View view){
//		TextView tvContent = (TextView)view.findViewById(R.id.tv_content);
//		tvContent.setText(getShowContent());
//		
//		
//		String adressString = "位置:  " + mLastLocation.getAdress();	
//		String timeString = "时间:  " + mLastLocation.getUpdateTimeString();
//
//		int width1 = Utils.getFitWidth(YunyouApplication.getInstance(), timeString, (int)tvContent.getTextSize());
//		int width2 = Utils.getFitWidth(YunyouApplication.getInstance(), adressString, (int)tvContent.getTextSize());
//		int w = Math.max(width1, width2);
//		
//		tvContent.setWidth(w);
//	}
	
//	public  String getShowContent()
//	{
//		if (mLastLocation == null){
//			return "null";
//		}
//		
//		try {
//		
//			String adressString = "位置:  " + mLastLocation.getAdress();	
//			String timeString = "时间:  " + mLastLocation.getUpdateTimeString();
//			String statusString = "状态:  ";
//			String powerDetail = "电量:  " + mLastLocation.getPowerDletai();
//	
//			String provider = mLastLocation.getProvider();
//
//			if (mLastLocation.getOnline() == 0)
//			{
//				statusString += "离线";
//			}else
//			{
//				
//				if (provider.equals(LocationManager.GPS_PROVIDER))
//				{
//					statusString += "GPS定位";
//				}else{
//					statusString += "基站定位";
//				}
//				
//				if (MapUtils.isMove(mLastLocation))
//				{
//					statusString += "/移动";
//				}else{
//					statusString += "/静止";
//				}
//			}
//			
//			StringBuffer sBuffer = new StringBuffer();
//			sBuffer.append(adressString + "\n" + 
//							timeString + "\n" + 
//							statusString + "\n" + 
//							powerDetail);
//			
//			
//			return sBuffer.toString();
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return "can't get the content";
//	}
	
	

	
}
