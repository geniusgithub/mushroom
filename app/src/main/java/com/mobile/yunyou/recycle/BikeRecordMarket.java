package com.mobile.yunyou.recycle;
//package com.mobile.yunyou.bike;
//
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//
//import android.graphics.Color;
//
//import com.amap.api.maps.model.BitmapDescriptorFactory;
//import com.amap.api.maps.model.LatLng;
//import com.amap.api.maps.model.LatLngBounds;
//import com.amap.api.maps.model.MarkerOptions;
//import com.amap.api.maps.model.PolylineOptions;
//import com.mobile.yunyou.model.BikeType;
//import com.mobile.yunyou.model.BikeType.BikeRecordSubResult;
//import com.mobile.yunyou.util.CommonLog;
//import com.mobile.yunyou.util.LogFactory;
//
//
//
//public class BikeRecordMarket {
//
//	private static final CommonLog log = LogFactory.createLog();
//
//	private MarkerOptions mMarkerOptionsStart;
//	private MarkerOptions mMarkerOptionsEnd;
//	private int drawableIDStart = 0;
//	private int drawableIDEnd = 0;
//	
//	private LinkedList<BikeRecordSubResult> mBikeSubRecordResultList = new LinkedList<BikeType.BikeRecordSubResult>();
//	private List<LatLng> mLatLngList = new ArrayList<LatLng>();
//	private List<PolylineOptions> mPLineList = new ArrayList<PolylineOptions>();
//	
//	private LatLngBounds mBounds = null;
//	
//	public BikeRecordMarket(int drawableStart, int drawableEnd){
//		drawableIDStart = drawableStart;
//		drawableIDEnd = drawableEnd;
//		mMarkerOptionsStart = new MarkerOptions();
//		mMarkerOptionsStart.draggable(true);
//		
//		mMarkerOptionsEnd = new MarkerOptions();
//		mMarkerOptionsEnd.draggable(true);
//
//	}
//	
//	public boolean isValidLine(){
//		if (mLatLngList.size() < 2){
//			return false;
//		}
//		return true;
//	}
//	
//	public MarkerOptions newStartMarkerOptions(){
//		if (mLatLngList.size() != 0 && drawableIDStart != 0){
//			mMarkerOptionsStart.icon(BitmapDescriptorFactory.fromResource(drawableIDStart));
//			LatLng latLng = getLatLon(0);
//			if (latLng != null){
//				mMarkerOptionsStart.position(latLng);
//			}
//		}
//
//		return mMarkerOptionsStart;
//	}
//	
//	
//	public MarkerOptions newEndMarkerOptions(){
//		if (mLatLngList.size() != 0 && drawableIDEnd != 0){
//			mMarkerOptionsEnd.icon(BitmapDescriptorFactory.fromResource(drawableIDStart));
//			LatLng latLng = getLatLon(mLatLngList.size() - 1);
//			if (latLng != null){
//				mMarkerOptionsEnd.position(latLng);
//			}
//		}
//
//		return mMarkerOptionsEnd;
//	}
//	
//	
//	
//	public LatLng getLatLon(int i){
//		if (i >= 0 && i < mLatLngList.size()){
//			return mLatLngList.get(i);
//		}
//		return null;
//	}
//	
//	public List<PolylineOptions> getPLineList (){
//		return mPLineList;
//	}
//	public void setBikeRecordList(LinkedList<BikeRecordSubResult> list)
//	{
//		if (list != null)
//		{
//		
//			log.e("setBikeRecordList size = " + list.size());
//			mBikeSubRecordResultList = list;
//			mLatLngList.clear();
//			mPLineList.clear();
//			mBounds = null;
//			int size = list.size();		
//			int index = 0;
//			LatLng preLatLng = null;
//			for(int i = 0; i < size; i++)
//			{
//				BikeType.BikeRecordSubResult object = list.get(i);	
//				LatLng latLng = new LatLng(object.mOffsetLat, object.mOffsetLon);
//				mLatLngList.add(latLng);
//				if (preLatLng != null){
//					PolylineOptions options = new PolylineOptions();
//					options.add(preLatLng, latLng);
//					options.width(10);
//					options.color(Color.BLACK);	
//					mPLineList.add(options);
//					if (mBounds == null){
//						mBounds = new LatLngBounds(preLatLng, latLng);
//					}else{
//						mBounds.including(latLng);
//					}
//				}
//				preLatLng = latLng;
//			}
//		}
//	}
//	
//	public LatLngBounds getBound(){
//		return mBounds;
//	}
//	
//
//}
