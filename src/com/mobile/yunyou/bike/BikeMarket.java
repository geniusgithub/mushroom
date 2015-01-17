package com.mobile.yunyou.bike;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.location.LocationManager;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.mobile.yunyou.R;
import com.mobile.yunyou.map.data.LocationEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.YunTimeUtils;

public class BikeMarket {

	private static final CommonLog log = LogFactory.createLog();
	
	private LocationEx mFirstLocation;
	private LocationEx mLastLocation;

	private MarkerOptions mMarkerOptionsStart;
	private MarkerOptions mMarkerOptionsCurrent;
	private int drawableIDStart = 0;
	private int drawableIDCurrentOnline = 0;
	private int drawableIDCurrentOffline = 0;
	
	private LatLng mPreLatLng;
	private List<LatLng> mLatLngList = new ArrayList<LatLng>();
	private List<PolylineOptions> mPLineList = new ArrayList<PolylineOptions>();
	private LatLngBounds mBounds = null;
	
	private boolean isRunning = true;
	
	private boolean isNeedUpdateLocation = true;
	
	public BikeMarket(int drawableStart, int drawableCurrentOnline, int drawableCurrentOffline){
		drawableIDStart = drawableStart;
		drawableIDCurrentOnline = drawableCurrentOnline;
		drawableIDCurrentOffline = drawableCurrentOffline;
		
		mMarkerOptionsStart = new MarkerOptions();
		mMarkerOptionsStart.draggable(true);
		
		mMarkerOptionsCurrent = new MarkerOptions();
		mMarkerOptionsCurrent.draggable(true);
	}
	
	public void startRunning(){
		isRunning = true;
	}
	
	public boolean isRunning(){
		return isRunning;
	}
	
	public void setUpdateFlag(boolean flag){
		isNeedUpdateLocation = flag;
	}
	
	public boolean isNeedUpdate(){
		return isNeedUpdateLocation;
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
		isRunning = true;
		mBounds = null;
	}

	public MarkerOptions newStartMarkerOptions(){
		if (mFirstLocation != null && drawableIDStart != 0){
			mMarkerOptionsStart.icon(BitmapDescriptorFactory.fromResource(drawableIDStart));
			mMarkerOptionsStart.title("");
			LatLng latLng = getFirstLatLon();
			if (latLng != null){
				mMarkerOptionsStart.position(latLng);
			}
		}

		return mMarkerOptionsStart;
	}
	
	
	public MarkerOptions newCurrentMarkerOptions(){
		if (mLastLocation != null && drawableIDCurrentOnline != 0 && drawableIDCurrentOffline != 0){
			if (mLastLocation.getOnline() != 0){
				mMarkerOptionsCurrent.icon(BitmapDescriptorFactory.fromResource(drawableIDCurrentOnline));
			}else{
				mMarkerOptionsCurrent.icon(BitmapDescriptorFactory.fromResource(drawableIDCurrentOffline));
			}
	
			mMarkerOptionsCurrent.title("");
			LatLng latLng = getLastLatLon();
			if (latLng != null){
				mMarkerOptionsCurrent.position(latLng);
			}
		}

		return mMarkerOptionsCurrent;
	}
	
	public void addLocation(LocationEx location){
		if (location != null){
			log.e("addLocation...");

			setUpdateFlag(true);
			if (mLastLocation != null){
				if (mLastLocation.getOnline() == 0 && location.getOnline() == 0){
					setUpdateFlag(false);
				}
			}
			mLastLocation = location;
			LatLng latLng = new LatLng(location.getOffsetLat(), location.getOffsetLon());
			mLatLngList.add(latLng);

			if (mPreLatLng == null){
				mFirstLocation = location;
				mPreLatLng = latLng;
				if (mBounds == null){
					mBounds = new LatLngBounds(getFirstLatLon(), getLastLatLon());
				}
				return ;
			}

	
			PolylineOptions options = new PolylineOptions();
			options.add(mPreLatLng, latLng);
			options.width(10);
			options.color(Color.BLACK);	
			mPLineList.add(options);
			log.e("mPLineList.size = " + mPLineList.size());
			mPreLatLng = latLng;
			mBounds.including(latLng);
		}
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
	
	public void setLastLocation(LocationEx location){
		if (location != null){
			mLastLocation = location;	
			mFirstLocation = location;
			if (mBounds == null){
				mBounds = new LatLngBounds(getFirstLatLon(), getLastLatLon());
			}
		}
	}
	
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
	
	public LatLngBounds getBound(){
		return mBounds;
	}
	
	public void render(View view){
		TextView tvContent = (TextView)view.findViewById(R.id.tv_content);
		tvContent.setText(getShowContent());
	}
	
	public  String getShowContent()
	{
		if (mLastLocation == null){
			return "null";
		}
		
		try {
		
			String adressString = "位置:  " + mLastLocation.getAdress();	
			String timeString = "时间:  " + mLastLocation.getUpdateTimeString();
			String statusString = "状态:  ";
	
			String provider = mLastLocation.getProvider();

			if (mLastLocation.getOnline() == 0)
			{
				statusString += "离线";
			}else
			{
				
				if (provider.equals(LocationManager.GPS_PROVIDER))
				{
					statusString += "GPS定位";
				}else{
					statusString += "基站定位";
				}
				
				if (MapUtils.isMove(mLastLocation))
				{
					statusString += "/移动";
				}else{
					statusString += "/静止";
				}
			}
			
			StringBuffer sBuffer = new StringBuffer();
			sBuffer.append(adressString + "\n" + 
							timeString + "\n" + 
							statusString);
			
			
			return sBuffer.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "can't get the content";
	}
}
