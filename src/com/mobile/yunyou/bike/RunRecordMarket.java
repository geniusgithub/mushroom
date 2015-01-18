package com.mobile.yunyou.bike;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.LatLngBounds.Builder;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.mobile.yunyou.R;
import com.mobile.yunyou.model.BikeType;
import com.mobile.yunyou.model.BikeType.BikeLRecordSubResult;
import com.mobile.yunyou.model.BikeType.MinRunRecord;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;



public class RunRecordMarket {

	private static final CommonLog log = LogFactory.createLog();

	private MarkerOptions mMarkerOptionsStart;
	private MarkerOptions mMarkerOptionsEnd;
	private int drawableIDStart = 0;
	private int drawableIDEnd = 0;
	
	private BikeType.BikeLRecordSubResultGroup mRecordGroup = new BikeType.BikeLRecordSubResultGroup();
	private List<LatLng> mLatLngList = new ArrayList<LatLng>();
	private List<PolylineOptions> mPLineList = new ArrayList<PolylineOptions>();
	
	private LatLngBounds mBounds = null;
	
	public RunRecordMarket(int drawableStart, int drawableEnd){
		drawableIDStart = drawableStart;
		drawableIDEnd = drawableEnd;
		mMarkerOptionsStart = new MarkerOptions();
		mMarkerOptionsStart.draggable(true);
		
		mMarkerOptionsEnd = new MarkerOptions();
		mMarkerOptionsEnd.draggable(true);

	}
	
	public boolean isValidLine(){
		if (mLatLngList.size() < 2){
			return false;
		}
		return true;
	}
	
	public MarkerOptions newStartMarkerOptions(){
		if (mLatLngList.size() != 0 && drawableIDStart != 0){
			mMarkerOptionsStart.icon(BitmapDescriptorFactory.fromResource(drawableIDStart));
			//mMarkerOptionsStart.title("");
			LatLng latLng = getLatLon(0);
			if (latLng != null){
				mMarkerOptionsStart.position(latLng);
			}
		}

		return mMarkerOptionsStart;
	}
	
	
	public MarkerOptions newEndMarkerOptions(){
		if (mLatLngList.size() != 0 && drawableIDEnd != 0){
			mMarkerOptionsEnd.icon(BitmapDescriptorFactory.fromResource(drawableIDEnd));
			//mMarkerOptionsEnd.title("");
			LatLng latLng = getLatLon(mLatLngList.size() - 1);
			if (latLng != null){
				mMarkerOptionsEnd.position(latLng);
			}
		}

		return mMarkerOptionsEnd;
	}
	
	
	
	public LatLng getLatLon(int i){
		if (i >= 0 && i < mLatLngList.size()){
			return mLatLngList.get(i);
		}
		return null;
	}
	
	public List<PolylineOptions> getPLineList (){
		return mPLineList;
	}
	public void setRunRecord(BikeType.BikeLRecordSubResultGroup group){
		if (group != null)
		{
			LinkedList<BikeLRecordSubResult> mRunRecordList = group.mBikeSubRecordResultList;
			log.e("setRunRecord size = " + mRunRecordList.size());
			mRecordGroup = group;
			mLatLngList.clear();
			mPLineList.clear();
			mBounds = null;
			int size = mRunRecordList.size();
			int index = 0;
			LatLng preLatLng = null;
			Builder build = LatLngBounds.builder();
			for(int i = 0; i < size; i++)
			{
				BikeLRecordSubResult object = mRunRecordList.get(i);	
				LatLng latLng = new LatLng(object.mLat, object.mLon);
				mLatLngList.add(latLng);
				build.include(latLng);
				if (preLatLng != null){
					PolylineOptions options = new PolylineOptions();
					options.add(preLatLng, latLng);
					options.width(5);
					options.color(Color.RED);	
					mPLineList.add(options);
				}
				preLatLng = latLng;
			}
			
			if (size != 0){
				mBounds = build.build();
			}
			
		}
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
		try {
		
			String adressString = "位置:  " + "test location";	
			String timeString = "时间:  " + "2015-01-08 12:02:34";
			String statusString = "状态:  " + "基站定位/移动";
			
			
			
			
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
