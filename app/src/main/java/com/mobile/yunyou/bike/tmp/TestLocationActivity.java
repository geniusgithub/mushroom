package com.mobile.yunyou.bike.tmp;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.mobile.yunyou.R;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;

public class TestLocationActivity extends Activity implements LocationSource, AMapLocationListener{

	private static final CommonLog log = LogFactory.createLog();
	
	private AMap aMap;
	private MapView mapView;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locationmodesource_activity);
       
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
	}

	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		aMap.setLocationSource(this);// 设置定位监听
	//	aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		
		//设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种 
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
	}

	
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
		
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		log.e("activate");
		
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
			mAMapLocationManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 5000, 10, this);
		}

	}

	@Override
	public void deactivate() {
		log.e("deactivate");
		
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		
		if (mListener != null && aLocation != null) {
			mListener.onLocationChanged(aLocation);// 显示系统小蓝点	
		}
	}

//	/**
//	 * 此方法已经废弃
//	 */
//	@Override
//	public void onLocationChanged(Location location) {
//	}
//
//	@Override
//	public void onProviderDisabled(String provider) {
//	}
//
//	@Override
//	public void onProviderEnabled(String provider) {
//	}
//
//	@Override
//	public void onStatusChanged(String provider, int status, Bundle extras) {
//	}


}
