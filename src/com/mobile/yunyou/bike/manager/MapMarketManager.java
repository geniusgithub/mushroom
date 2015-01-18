package com.mobile.yunyou.bike.manager;

import java.util.List;

import android.os.Handler;
import android.os.Message;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.PolylineOptions;
import com.mobile.yunyou.bike.BikeMarket;
import com.mobile.yunyou.bike.SafeMarket;
import com.mobile.yunyou.bike.SelfMarket;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;

public class MapMarketManager {

	private static final CommonLog log = LogFactory.createLog();
	
	
	private static final int MSG_SHOW_POP = 0x0001;
	
	
	private AMap mAMap;
	private MapView mMapView;
	private SelfMarket mSelfMarket;
	private BikeMarket mBikeMarket;
	private SafeMarket mSafeMarket;
	//private BikeRecordMarket mBikeRecordMarket;
	
	private Marker mMarketBIKE;
	private int mCurShowState = -1;
	
	private Handler mHandler;
	
	public static interface IViewConstant
	{
		int IVC_BIKE_POS = 0;
		int IVC_AREA_POS = 1;
		int IVC_BIKERECORD_POS = 2;
	}
	
	public MapMarketManager(AMap aMap, MapView mapview){
		mAMap = aMap;
		mMapView = mapview;
		
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				if (msg.what == MSG_SHOW_POP){
					if (mMarketBIKE != null){
						mMarketBIKE.showInfoWindow();
					}
				}
			}
			
		};
	}
	
	
	public void setSelfPos(SelfMarket object)
	{
		mSelfMarket = object;
	}
	
	public void setBikePos(BikeMarket object)
	{
		mBikeMarket = object;
	}
	
	public void setSafeAreaPos(SafeMarket object)
	{
		mSafeMarket = object;
	}
	
	
//	public void setBikeRecordPos(BikeRecordMarket object)
//	{
//		mBikeRecordMarket = object;
//	}
	
	public int getCurShowState(){
		return mCurShowState;
	}
	
	
	private void showView(int viewID)
	{
		switch(viewID)
		{
			case IViewConstant.IVC_BIKE_POS:
			{
				boolean isShow = false;
				if (mMarketBIKE != null){
					isShow = mMarketBIKE.isInfoWindowShown();
				}
				
				boolean isNeedUpdate = true;
				if (mBikeMarket != null){
					isNeedUpdate = mBikeMarket.isNeedUpdate();
				}
				log.e("showView IViewConstant.IVC_BIKE_POS isShow = " + isShow + ", isNeedUpdate = " + isNeedUpdate);
				
				mAMap.clear();
				mMarketBIKE = null;
				if (mSelfMarket.getLocation() != null){
					mAMap.addMarker(mSelfMarket.newMarkerOptions());
				//	mAMap.addCircle(mSelfMarket.newCircleOptions());
				}
				
				List<PolylineOptions> list = mBikeMarket.getPLineList();
				int size = list.size();
				for(int i = 0;i < size; i++){
					mAMap.addPolyline(list.get(i));
				}
				
				
//				if (mBikeMarket.getFirstLocation() != null && mBikeMarket.isRunning()){
//					mAMap.addMarker(mBikeMarket.newStartMarkerOptions());
//				}
				if (mBikeMarket.getLastLocation() != null){
					Marker marker = mAMap.addMarker(mBikeMarket.newCurrentMarkerOptions());
					marker.setTitle("BIKE");
					mMarketBIKE = marker;
					log.e("mMarketBIKE=this");
				}
				
				if (isShow){
					mHandler.sendEmptyMessage(MSG_SHOW_POP);
				}
				
				
			}
				break;
			case IViewConstant.IVC_AREA_POS:
			{
//				log.e("showView IVC_AREA_POS");
//				mAMap.clear();
//				if (mSafeMarket.getArea() != null){
//					mAMap.addMarker(mSafeMarket.newMarkerOptions());
//					mAMap.addCircle(mSafeMarket.newCircleOptions());
//				}

			}
				break;
			case IViewConstant.IVC_BIKERECORD_POS:
			{
				log.e("showView IVC_BIKERECORD_POS");
//				mAMap.clear();
//				if (mBikeRecordMarket.isValidLine()){
//					
//					List<PolylineOptions> list = mBikeRecordMarket.getPLineList();
//					int size = list.size();
//					for(int i = 0;i < size; i++){
//						mAMap.addPolyline(list.get(i));
//					}
//					
//					mAMap.addMarker(mBikeRecordMarket.newStartMarkerOptions());
//					mAMap.addMarker(mBikeRecordMarket.newEndMarkerOptions());
//					
//					LatLngBounds bounds = mBikeRecordMarket.getBound();
//					if (bounds != null){
//						
//					}
//				}
			}
				break;
		}
	}
	
	
	public void updateBikePos()
	{
		showView(IViewConstant.IVC_BIKE_POS);
		mCurShowState = IViewConstant.IVC_BIKE_POS;
	}
	
	public void updateAreaPos()
	{
		showView(IViewConstant.IVC_AREA_POS);
		mCurShowState = IViewConstant.IVC_AREA_POS;
	}
	
	
	public void updateBikeRecordPos()
	{
		showView(IViewConstant.IVC_BIKERECORD_POS);	
		mCurShowState = IViewConstant.IVC_BIKERECORD_POS;
	}
	
	
}
