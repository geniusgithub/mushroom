package com.mobile.yunyou.bike;


import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnCameraChangeListener;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.mobile.yunyou.R;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.bike.manager.BikeLocationManager;
import com.mobile.yunyou.bike.manager.SafeAreaManager;
import com.mobile.yunyou.bike.manager.SelfLocationManager;
import com.mobile.yunyou.bike.manager.BikeLocationManager.IBikeLocationUpdate;
import com.mobile.yunyou.bike.tmp.NewBikeCenter;
import com.mobile.yunyou.map.data.LocationEx;
import com.mobile.yunyou.map.util.WebManager;
import com.mobile.yunyou.model.BikeType;
import com.mobile.yunyou.model.BikeType.BikeGetArea;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.Courier;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.DialogFactory;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.PopWindowFactory;
import com.mobile.yunyou.util.Utils;

public class FollowBikeActivity extends Activity implements OnClickListener,
															IBikeLocationUpdate,
															OnMarkerClickListener,
															InfoWindowAdapter,
															OnInfoWindowClickListener{

	private static final CommonLog log = LogFactory.createLog();
	private static final int MSG_REFRESH_BIKE = 0x0001;
	private static final int MSG_SHOW_POP = 0x0002;
	

	private Context mContext;
	
	private AMap aMap;
	
	private MapView mMapView;								
	private UiSettings mUiSettings;		
	private View mRootView;


	private YunyouApplication mApplication;
	private NetworkCenterEx mNetworkCenterEx;

	private Handler mHandler;
	private Button mBtnBack;
	private Button mBtnFollow;
	
	private ImageView 			mZoomInBtn;							// 缩小
	private ImageView 			mZoomOutBtn;						// 放大
	private ImageView 			mFocusPosition;
	
	private BikeLocationManager mBikeLocationManager;
	private BikeMarket mBikeMarket;
	private Marker mMarker;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.follow_bike_layout);
		
		mMapView = (MapView) findViewById(R.id.map);
		mMapView.onCreate(savedInstanceState);
		aMap = mMapView.getMap();
		aMap.setOnMarkerClickListener(this);
		aMap.setInfoWindowAdapter(this);
		aMap.setOnInfoWindowClickListener(this);
		mContext = this;
		setupViews();
		initData();

	}
	
	
	private boolean isFirstResume = true;
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();

		
		if (isFirstResume){
			mBikeLocationManager.addObserver(this);
			mBikeLocationManager.startLocationCheck();
			
			LocationEx locationEx = mBikeLocationManager.getLastLocation();
			if (locationEx == null){
				locationEx = SelfLocationManager.getInstance().getLastLocation();
			}
			updateCamarra(13);
			moveCamara(locationEx);

			isFirstResume = false;
		}

		

	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();


	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
		
		mBikeLocationManager.removeObservser(this);
		mBikeLocationManager.stopLocationCheck();

	}
	
	
	@Override
	public void onBackPressed() {
	
		showExitDialog(true);
		
	}


	private void setupViews(){
		mRootView = findViewById(R.id.rootView);
		
		
		mUiSettings = aMap.getUiSettings();
		mUiSettings.setZoomControlsEnabled(false);

		mBtnBack = (Button) findViewById(R.id.btn_back);
    	mBtnBack.setOnClickListener(this);

    	mBtnFollow = (Button) findViewById(R.id.btn_follow);
    	mBtnFollow.setOnClickListener(this);
    	
		mZoomInBtn = (ImageView) findViewById(R.id.bt_zoomin_pos);
		mZoomInBtn.setOnClickListener(this);
		
		mZoomOutBtn = (ImageView) findViewById(R.id.bt_zoomout_pos);
		mZoomOutBtn.setOnClickListener(this);
		
		mFocusPosition = (ImageView) findViewById(R.id.bt_focus_pos);
		mFocusPosition.setOnClickListener(this);	
		
		mBikeMarket = new BikeMarket(R.drawable.point_start, R.drawable.bike_point_on, R.drawable.bike_point_off);
		
		
	}
	
	private void initData(){
		  mApplication = YunyouApplication.getInstance();
		  mNetworkCenterEx = NetworkCenterEx.getInstance();
		  
		  mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
					switch(msg.what){
						case MSG_REFRESH_BIKE:
							refreshBike();
							break;
						case MSG_SHOW_POP:
							if (mMarker != null){
								mMarker.showInfoWindow();
							}
							break;
					}
			}
		  };
		  mBikeLocationManager = BikeLocationManager.getInstance();
	}

	private Dialog mDialog = null;
	private void showExitDialog(boolean bShow)
	{
		if (mDialog != null)
		{
			mDialog.dismiss();
			mDialog = null;
		}
		
		OnClickListener onClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				finish();
			}
		};

		if (bShow)
		{
			mDialog = DialogFactory.creatDoubleDialog(this, R.string.dialog_title_exitFollow, R.string.dialog_msg_exitFollow, onClickListener);
			mDialog.show();
		}
	
	}
	
	public void searchBike(){

		mBikeMarket.reset();
		Message msg = mHandler.obtainMessage(MSG_REFRESH_BIKE);
		msg.sendToTarget();
		
		mBikeLocationManager.requesetNow();
		focusPosition();
		
	}
	
	private void refreshBike(){
		boolean isShow = false;
		if (mMarker != null){
			isShow = mMarker.isInfoWindowShown();
		}
		
		boolean isNeedUpdate = true;
		if (mBikeMarket != null){
			isNeedUpdate = mBikeMarket.isNeedUpdate();
		}
		
		log.e("showView IViewConstant.IVC_BIKE_POS isShow = " + isShow + ", isNeedUpdate = " + isNeedUpdate);
		if (!isNeedUpdate){
			return ;
		}
		mMarker = null;
		
		
		aMap.clear();
		List<PolylineOptions> list = mBikeMarket.getPLineList();
		int size = list.size();
		for(int i = 0;i < size; i++){
			aMap.addPolyline(list.get(i));
		}
		if (mBikeMarket.getLastLocation() != null){
			Marker marker = aMap.addMarker(mBikeMarket.newCurrentMarkerOptions());
			marker.setTitle("BIKE");
			mMarker = marker;
		}
		
		if (isShow){
		//	mHandler.sendEmptyMessage(MSG_SHOW_POP);
			mMarker.showInfoWindow();
		}

		moveCamara(mBikeMarket.getLastLatLon());
		
	}


	

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_follow:
			searchBike();
			break;
		case R.id.bt_focus_pos:
			focusPosition();
			break;
		case R.id.bt_zoomout_pos:
			zomOut();
			break;
		case R.id.bt_zoomin_pos:
			zomIn();
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
	
		aMap.moveCamera(CameraUpdateFactory.zoomTo(zoomLevel));
	}

	
	private void moveCamara(LatLng latlon){
		if (latlon != null){	
			CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latlon, getZoomLevel());
			aMap.moveCamera(update);
		}
	}
	
	private void moveCamara(LocationEx location){
		if (location != null){
			LatLng latLng = new LatLng(location.getOffsetLat(), location.getOffsetLon());
			log.e("updateCamarra (" + location.getOffsetLat() + ", " + location.getOffsetLon());
			CameraUpdate object = CameraUpdateFactory.newLatLngZoom(latLng, getZoomLevel());
			aMap.moveCamera(object);

		}
	}
	
	private float getZoomLevel(){
		CameraPosition position = aMap.getCameraPosition();
		return position.zoom;
	}
	
	private void focusPosition(){
		log.e("focusPosition");
		LatLng latLng = mBikeMarket.getLastLatLon();
		
		if (latLng != null){
			updateCamarra(13);
			moveCamara(latLng);
		}	
	}
	

	@Override
	public void onBikeLocationUpdate(final LocationEx location) {
		log.e("onBikeLocationUpdate (" + location.getLatitude() + "," + location.getLongitude() + ")");
		runOnUiThread(new Runnable() {
		
			@Override
			public void run() {
	

				mBikeMarket.addLocation(location);
	
				Message msg = mHandler.obtainMessage(MSG_REFRESH_BIKE);
				msg.sendToTarget();
	
			}
		});
	}

	@Override
	public void onInfoWindowClick(Marker market) {
		market.hideInfoWindow();
	}
	
	@Override
	public View getInfoContents(Marker market) {
		String title = market.getTitle();
		if (title == null){
			return null;
		}
		log.e("getInfoContents market.title = " + title);
		if (title.equals("BIKE")){
			View infoContent = getLayoutInflater().inflate(R.layout.maptip_layout, null);
			mBikeMarket.render(infoContent);
			return infoContent;
		}
		return null;
	}
	
	@Override
	public View getInfoWindow(Marker market) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean onMarkerClick(Marker market) {
		// TODO Auto-generated method stub
		return false;
	}



}
