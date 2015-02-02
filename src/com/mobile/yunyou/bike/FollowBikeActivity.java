package com.mobile.yunyou.bike;


import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.LatLngBounds.Builder;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.PolylineOptions;
import com.mobile.yunyou.R;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.bike.manager.BikeLocationManager;
import com.mobile.yunyou.bike.manager.BikeLocationManager.IBikeLocationUpdate;
import com.mobile.yunyou.bike.manager.SelfLocationManager;
import com.mobile.yunyou.bike.manager.SelfLocationManager.ILocationUpdate;
import com.mobile.yunyou.map.data.LocationEx;
import com.mobile.yunyou.map.util.LocationUtil;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.DialogFactory;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.Utils;

public class FollowBikeActivity extends Activity implements OnClickListener,
															ILocationUpdate,	
															IBikeLocationUpdate,
															OnMarkerClickListener,
															InfoWindowAdapter,
															OnInfoWindowClickListener,
															LocationSource{

	private static final CommonLog log = LogFactory.createLog();
	private static final int MSG_REFRESH_SELF = 0x0001;
	private static final int MSG_REFRESH_BIKE = 0x0002;
	private static final int MSG_REFRESH_VIEW = 0x0003;

	

	private Context mContext;
	
	private AMap aMap;
	
	private MapView mMapView;								
	private UiSettings mUiSettings;		
	private View mRootView;
	private OnLocationChangedListener mListener;


	private YunyouApplication mApplication;
	private NetworkCenterEx mNetworkCenterEx;

	private Handler mHandler;
	private Button mBtnBack;
	private Button mBtnFollow;
	
	private ImageView 			mZoomInBtn;							// 缩小
	private ImageView 			mZoomOutBtn;						// 放大
	private ImageView 			mFocusPosition;
	private ImageView 			mDistanceBtn;
	
	
	private BikeLocationManager mBikeLocationManager;
	private BikeMarket mBikeMarket;
	//private Marker mSelfMarket;
	
	private SelfLocationManager mSelfLocationManager;
	//private SelfMarket mSelfMarket;
	private boolean mIsFirstCenter = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); 
		
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
		initMap();
	}
	
	
	private boolean isFirstResume = true;
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();

		mSelfLocationManager.addObserver(this);
		mSelfLocationManager.startLocationCheck();
		
		if (isFirstResume){
		
			LocationEx locationEx = mBikeLocationManager.getLastLocation();
			if (locationEx == null){
				locationEx = mSelfLocationManager.getLastLocation();
			}
			updateCamarra(13);
			moveCamara(locationEx);

			isFirstResume = false;
		}
		
		searchBike();

		if (LocationUtil.isGPSEnable(mContext) == false)
		{
			showGPSDialog(true);
		}

	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();

		mSelfLocationManager.removeObservser(this);
		mSelfLocationManager.stopLocationCheck();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}

	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		mBikeLocationManager.addObserver(this);
		mBikeLocationManager.startLocationCheck();
		
	}

	@Override
	protected void onStop() {
		super.onStop();
		
		
		mBikeLocationManager.removeObservser(this);
		mBikeLocationManager.stopLocationCheck();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
		


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
    	mBtnFollow.setVisibility(View.GONE);
    	
		mZoomInBtn = (ImageView) findViewById(R.id.bt_zoomin_pos);
		mZoomInBtn.setOnClickListener(this);
		
		mZoomOutBtn = (ImageView) findViewById(R.id.bt_zoomout_pos);
		mZoomOutBtn.setOnClickListener(this);
		
		mFocusPosition = (ImageView) findViewById(R.id.bt_focus_pos);
		mFocusPosition.setOnClickListener(this);	
		
		mDistanceBtn = (ImageView) findViewById(R.id.bt_map_distance);
		mDistanceBtn.setOnClickListener(this);	
		
		
		mBikeMarket = new BikeMarket(R.drawable.point_start, R.drawable.bike_point_on, R.drawable.bike_point_off);
	//	mSelfMarket = new SelfMarket(R.drawable.self_pos);	
		
	}
	
	private void initData(){
		  mApplication = YunyouApplication.getInstance();
		  mNetworkCenterEx = NetworkCenterEx.getInstance();
		  
		  mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
					switch(msg.what){
						case MSG_REFRESH_SELF:
						//	refreshSelf();
							break;
						case MSG_REFRESH_BIKE:
							refreshBike();
							break;
						case MSG_REFRESH_VIEW:
							refreshView();
							break;
					}
			}
		  };
		  mBikeLocationManager = BikeLocationManager.getInstance();
		  mSelfLocationManager = SelfLocationManager.getInstance();
	}
	
	private void initMap(){
//		ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
//		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point1));
//		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point2));
//		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point3));
//		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point4));
//		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point5));
//		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point6));
//		mSelfMarket = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).icons(giflist).period(50));
		
		// 自定义系统定位小蓝点
//		MyLocationStyle myLocationStyle = new MyLocationStyle();
//		myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
//		myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
//		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
//		myLocationStyle.strokeWidth(0.1f);// 设置圆形的边框粗细
//		
//		
//		aMap.setMyLocationStyle(myLocationStyle);
//		aMap.setMyLocationRotateAngle(180);
		aMap.setLocationSource(this);// 设置定位监听
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		//设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种 
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
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

	private void showGPSDialog(boolean bShow)
	{
		if (mDialog != null)
		{
			mDialog.dismiss();
			mDialog = null;
		}
		
		OnClickListener onClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View view) {			
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(intent);
			}
		};

		if (bShow)
		{
			mDialog = DialogFactory.creatDoubleDialog(mContext, R.string.dialog_title_gogps, R.string.dialog_msg_gogps,
																R.string.btn_yes, R.string.btn_no, onClickListener);
			mDialog.show();
		}
	}
	
	public void searchBike(){
		log.e("searchBike");
		mBikeMarket.reset();
		Message msg = mHandler.obtainMessage(MSG_REFRESH_VIEW);
		msg.sendToTarget();
		
		mBikeLocationManager.requesetNow();
		focusPosition();
		
	}
	
//	private void refreshSelf(){
//		boolean isShow = false;
//		if (mMarker != null){
//			isShow = mMarker.isInfoWindowShown();
//		}
//		
//		boolean isNeedUpdate = true;
//		if (mSelfMarket != null){
//			isNeedUpdate = mSelfMarket.isNeedUpdate();
//		}
//		
//		log.e("showView refreshSelf isShow = " + isShow + ", isNeedUpdate = " + isNeedUpdate);
//		if (!isNeedUpdate){
//			return ;
//		}
//		
//		aMap.clear();
//
//		if (mSelfMarket.getLocation() != null){
//			aMap.addMarker(mSelfMarket.newMarkerOptions());
//		}
//		
//		List<PolylineOptions> list = mBikeMarket.getPLineList();
//		int size = list.size();
//		for(int i = 0;i < size; i++){
//			aMap.addPolyline(list.get(i));
//		}
//		if (mBikeMarket.getLastLocation() != null){
//			Marker marker = aMap.addMarker(mBikeMarket.newCurrentMarkerOptions());
//			marker.setTitle("BIKE");
//			mMarker = marker;
//		}
//		
//		
//		
//		if (isShow){
//		//	mHandler.sendEmptyMessage(MSG_SHOW_POP);
//			mMarker.showInfoWindow();
//		}
//		
//	}
	
	private void refreshView(){
		aMap.clear();
		
		if (mBikeMarket.getLastLocation() != null){
			Marker marker = aMap.addMarker(mBikeMarket.newCurrentMarkerOptions());
			marker.setTitle("BIKE");
			mBikeMarket.attchMarket(marker);
		}
		
		List<PolylineOptions> list = mBikeMarket.getPLineList();
		int size = list.size();
		for(int i = 0; i < size; i++){
			aMap.addPolyline(list.get(i));
		}
	}
	
	private void refreshBike(){
		log.e("refreshBike");
	

		Marker marker = mBikeMarket.getMarket();
		if (marker == null){
			if (mBikeMarket.getLastLocation() != null){
				marker = aMap.addMarker(mBikeMarket.newCurrentMarkerOptions());
				marker.setTitle("BIKE");
				mBikeMarket.attchMarket(marker);

			}
			return ;
		}

		
		LatLng latLng = mBikeMarket.getLastLatLon();
		if (latLng != null){
			marker.setPosition(latLng);
		}
		
		PolylineOptions polylineOptions = mBikeMarket.getLastPolyline();
		if (polylineOptions != null){
			aMap.addPolyline(polylineOptions);
		}
		

		boolean isShow = marker.isInfoWindowShown();
		if (isShow){
			mBikeMarket.updatePopViewSelf();
		}
//		if (mSelfMarket.getLocation() != null){
//			aMap.addMarker(mSelfMarket.newMarkerOptions());
//		}
		

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
		case R.id.bt_map_distance:
			mapDistance();
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
	
	private void moveCamara(LatLngBounds latLngBounds){
		if (latLngBounds != null){
			CameraUpdate update = CameraUpdateFactory.newLatLngBounds(latLngBounds, 14);
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
		}else{
			moveCamara(mSelfLocationManager.getLastLocation());
		}
	}
	
	private void mapDistance(){
	
		LatLng latLng1= null;
		LocationEx locationEx = mSelfLocationManager.getLastLocation();
		if (locationEx != null){
			latLng1 = new LatLng(locationEx.getOffsetLat(), locationEx.getOffsetLon());
		}
		LatLng latLng2= mBikeMarket.getLastLatLon();
		
		if (latLng1 == null || latLng2 == null){
			Utils.showToast(mContext, R.string.map_text_nodistance);
			return ;
		}
		
		float results[]  = new float[1];
		Location.distanceBetween(latLng1.latitude, latLng1.longitude, 
				latLng2.latitude,  latLng2.longitude, results);
		
		String showString = "";
		if (results[0] < 1000)
		{
			int distance = (int)results[0];
			showString = "距离" + distance + "米";
		}else{
			int distance = (int) (results[0]/1000);
			showString = "距离" + distance  + "千米";
		}
		
		Utils.showToast(mContext, showString);
		
		Builder builder = LatLngBounds.builder().include(latLng1).include(latLng2);
		moveCamara(builder.build());

	
	}

	@Override
	public void onBikeLocationUpdate(final LocationEx location) {
		log.e("onBikeLocationUpdate (" + location.getLatitude() + "," + location.getLongitude() + ")");
		runOnUiThread(new Runnable() {
		
			@Override
			public void run() {
	
	
				boolean ret = mBikeMarket.addLocation(location);
				if (mIsFirstCenter){
					mIsFirstCenter = false;
					moveCamara(mBikeMarket.getLastLocation());
				}
				if (ret){			
					Message msg = mHandler.obtainMessage(MSG_REFRESH_BIKE);
					msg.sendToTarget();
				}

	
			}
		});
	}
	
	
	@Override
	public void onLocationUpdate(final LocationEx location, AMapLocation aMapLocation) {
		log.e("onLocationUpdate (" + location.getLatitude() + "," + location.getLongitude() + ")");
		
		if (mListener != null && aMapLocation != null) {
			mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点	
		}
		
//		runOnUiThread(new Runnable() {
//		
//			@Override
//			public void run() {
//	
//				mSelfMarket.setLocation(location);
//				if (mApplication.mIsDebug){
//					Utils.showToast(FollowBikeActivity.this, "provider = " + location.getProvider());
//				}
//	
//				Message msg = mHandler.obtainMessage(MSG_REFRESH_SELF);
//				msg.sendToTarget();
//
//			}
//		});
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
			mBikeMarket.attachPopView(infoContent);
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

	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		
	}

	



}
