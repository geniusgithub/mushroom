package com.mobile.yunyou.bike;


import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnCameraChangeListener;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.LatLngBounds.Builder;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.mobile.yunyou.R;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.bike.manager.BikeLocationManager;
import com.mobile.yunyou.bike.manager.MapMarketManager;
import com.mobile.yunyou.bike.manager.SafeAreaManager;
import com.mobile.yunyou.bike.manager.SelfLocationManager;
import com.mobile.yunyou.fragment.MainMapFragment;
import com.mobile.yunyou.map.data.LocationEx;
import com.mobile.yunyou.map.util.LocationUtil;
import com.mobile.yunyou.map.util.StringUtil;
import com.mobile.yunyou.map.util.WebManager;
import com.mobile.yunyou.model.BikeType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.model.BikeType.BikeGetArea;
import com.mobile.yunyou.model.BikeType.BikeLRecordResult;
import com.mobile.yunyou.model.BikeType.MinRunRecord;
import com.mobile.yunyou.network.Courier;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.DialogFactory;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.PopWindowFactory;
import com.mobile.yunyou.util.Utils;
import com.mobile.yunyou.util.YunTimeUtils;

public class RecordMapActivity extends Activity implements OnClickListener, 
															OnMapLoadedListener,
															OnMarkerClickListener,
															InfoWindowAdapter,
															OnInfoWindowClickListener{


	private static final CommonLog log = LogFactory.createLog();

	private static final int MSG_REFRESH_BIKERECORD = 0x0004;
	
	
	private Context mContext;
	
	private AMap aMap;
	private View mRootView;
	
	private MapView mMapView;								
	private UiSettings mUiSettings;			
	private ImageView 			mZoomInBtn;							// 缩小
	private ImageView 			mZoomOutBtn;						// 放大
	private ImageView 			mFocusPosition;
	private Button mBtnBack;
//	private YunyouApplication mApplication;
//	private NetworkCenterEx mNetworkCenter;
	
	private TextView mTVDistance;
	private TextView mTVTimeInterval;
	private TextView mTVSpeed;
	
//	private BikeType.RunRecordGroup mRunRecordGroup;
	private BikeLRecordResult mCurRecord;
	private BikeType.BikeLRecordSubResultGroup mRunRecordGroup;
	private RunRecordMarket mBikeRecordMarket;
	
	private Handler mHandler;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recordmap_layout);
		
		mMapView = (MapView) findViewById(R.id.map);
		mMapView.onCreate(savedInstanceState);
		aMap = mMapView.getMap();
		aMap.setOnMapLoadedListener(this);
		//aMap.setOnMarkerClickListener(this);
		//aMap.setInfoWindowAdapter(this);
		//aMap.setOnInfoWindowClickListener(this);
		
		setupViews();
		initData();

	}
	

	private boolean isFirstResume = true;
	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
		

	}

	/**
	 * 方法必须重写
	 * map的生命周期方法
	 */
	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	/**
	 * 方法必须重写
	 * map的生命周期方法
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 * map的生命周期方法
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
	}
	

	private void setupViews(){
		
		mUiSettings = aMap.getUiSettings();
		mUiSettings.setZoomControlsEnabled(false);
		
		mRootView = findViewById(R.id.rootView);
		
		mZoomInBtn = (ImageView)findViewById(R.id.bt_zoomin_pos);
		mZoomInBtn.setOnClickListener(this);
		
		mZoomOutBtn = (ImageView)findViewById(R.id.bt_zoomout_pos);
		mZoomOutBtn.setOnClickListener(this);
		
		mFocusPosition = (ImageView)findViewById(R.id.bt_focus_pos);
		mFocusPosition.setOnClickListener(this);	
		
    	mBtnBack = (Button) findViewById(R.id.btn_back);
    	mBtnBack.setOnClickListener(this);
    	
    	mTVDistance = (TextView) findViewById(R.id.tv_distance);
		mTVTimeInterval = (TextView) findViewById(R.id.tv_timeinterval);
		mTVSpeed = (TextView) findViewById(R.id.tv_speed);

		mBikeRecordMarket = new RunRecordMarket(R.drawable.point_start, R.drawable.point_end);
		
		mCurRecord = YunyouApplication.getInstance().getRunRecord();
		mRunRecordGroup = YunyouApplication.getInstance().getRunRecordSub();
		mBikeRecordMarket.setRunRecord(mRunRecordGroup);
		updateView(mCurRecord);
	}
    
	
	private void initData(){

		  mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				
				case MSG_REFRESH_BIKERECORD:
	
					break;
				default:
					break;
				}
			}
			  
		  };

	}
	
	private void updateView(BikeType.BikeLRecordResult object){

		mTVDistance.setText(getShowDistance(object.mTotalDistance));
		long timeInterval = YunTimeUtils.getTimeInterval(object.mEndTime, object.mStartTime);
		mTVTimeInterval.setText(YunTimeUtils.getFormatTimeInterval(timeInterval));
		double speed = object.mTotalDistance / (timeInterval / 1000.0 / 60 / 60);
		mTVSpeed.setText(StringUtil.ConvertByDoubeString(speed) + "km/h");
	}

	
	private void focusPosition(){
		log.e("focusPosition");
		LatLngBounds bounds = mBikeRecordMarket.getBound();
		if (bounds != null){
			moveCamara(bounds);
		}
//		
//		LatLng latLng = mBikeRecordMarket.getLatlng();
//		if (latLng != null){
//			moveCamara(latLng);
//		}
	}
		
	
	public void showBikeRecord(){
		log.e("showBikeRecord");
		aMap.clear();
		if (mBikeRecordMarket.isValidLine()){
			
			List<PolylineOptions> list = mBikeRecordMarket.getPLineList();
			int size = list.size();
			for(int i = 0;i < size; i++){
				aMap.addPolyline(list.get(i));
			}
			
			aMap.addMarker(mBikeRecordMarket.newStartMarkerOptions());
			aMap.addMarker(mBikeRecordMarket.newEndMarkerOptions());
			
			LatLngBounds bounds = mBikeRecordMarket.getBound();
			if (bounds != null){
				moveCamara(bounds);
			}
			
//			moveCamara(mBikeRecordMarket.getLatlng());
			
//			LatLng latLng = mBikeRecordMarket.getLatlng();
//			if (latLng != null){
//				moveCamara(latLng);
//			}
		}
	}

		
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.bt_focus_pos:
			focusPosition();
			break;
		case R.id.bt_zoomout_pos:
			zomOut();
			break;
		case R.id.bt_zoomin_pos:
			zomIn();
			break;
		case R.id.btn_back:
			finish();
			break;
		}
	}

	private void zomOut(){
		aMap.moveCamera(CameraUpdateFactory.zoomOut());
	}
	
	private void zomIn(){
		aMap.moveCamera(CameraUpdateFactory.zoomIn());
	}
	
	private void updateCamarra(float zoomLevel){
		log.e("updateCamarra zoomLevel = " + zoomLevel);
		CameraPosition position = aMap.getCameraPosition();
		CameraUpdate object = CameraUpdateFactory.zoomTo(zoomLevel);
		aMap.moveCamera(object);
	}

	
	private void moveCamara(LatLngBounds latLngBounds){
		if (latLngBounds != null){
			CameraUpdate update = CameraUpdateFactory.newLatLngBounds(latLngBounds, 10);
			moveCamara( update);
		}
	}
	
	
	private void moveCamara(LatLng latlon){
		if (latlon != null){
			CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latlon, MapUtils.getZoomLevel(aMap));
			moveCamara(update);
		}
	}
	
	private void moveCamara(CameraUpdate object){
		aMap.moveCamera(object);
	}

	@Override
	public void onMapLoaded() {
		log.e("onMapLoaded");
		
		showBikeRecord();
	}

	   public String getShowDistance(double distance){
	    	String string = Double.valueOf(distance) + "公里";
	    	return string;
	    }

	@Override
	public boolean onMarkerClick(Marker market) {

	//	market.showInfoWindow();
		return true;
	}

	@Override
	public View getInfoContents(Marker market) {
//		View infoContent = getLayoutInflater().inflate(
//				R.layout.maptip_layout, null);
//		mBikeRecordMarket.render(infoContent);
//		return infoContent;
		return null;
	}

	@Override
	public View getInfoWindow(Marker market) {
	
		return null;
	}

	@Override
	public void onInfoWindowClick(Marker market) {

		market.hideInfoWindow();
	}
	
}
