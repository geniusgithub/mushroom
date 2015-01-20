package com.mobile.yunyou.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.mobile.yunyou.R;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.bike.MoGuActivity;
import com.mobile.yunyou.bike.SelfMarket;
import com.mobile.yunyou.bike.manager.BikeLocationManager.IBikeLocationUpdate;
import com.mobile.yunyou.bike.manager.MapMarketManager;
import com.mobile.yunyou.bike.manager.SelfLocationManager;
import com.mobile.yunyou.bike.manager.SelfLocationManager.ILocationUpdate;
import com.mobile.yunyou.map.data.LocationEx;
import com.mobile.yunyou.model.BikeType;
import com.mobile.yunyou.model.BikeType.BikeGetArea;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.DialogFactory;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.PopWindowFactory;
import com.mobile.yunyou.util.Utils;

public class MainMapFragment extends Fragment implements OnClickListener, 
																ILocationUpdate,															
																OnMarkerClickListener,
																InfoWindowAdapter,
																OnInfoWindowClickListener{
																//SafeAreaManager.ISafeAreaResult{

	private static final CommonLog log = LogFactory.createLog();
	private static final int MSG_REFRESH_SELF = 0x0001;
	private static final int MSG_REFRESH_BIKE = 0x0002;
	private static final int MSG_REFRESH_AREA = 0x0003;
	private static final int MSG_REFRESH_BIKERECORD = 0x0004;
	
	private static MainMapFragment fragment=null;
	
	private Context mContext;
	
	private AMap aMap;
	private View mapLayout;
	private View mRootView;
	
	private MapView mMapView;								
	private UiSettings mUiSettings;			
	private ImageView 			mZoomInBtn;							// 缩小
	private ImageView 			mZoomOutBtn;						// 放大
	private ImageView 			mFocusPosition;
	//private ImageView 			mLocationBtn;
	private ImageView 			mDistanceBtn;
	
	public static final double DOUBLE_STUDEN_LON = 114.1017;
	public static final double DOUBLE_STUDEN_LAT = 22.6644;
	public static final LatLng ORIGIN_POSITION = new LatLng(DOUBLE_STUDEN_LAT, DOUBLE_STUDEN_LON);// 北京市中关村经纬度
	
	private YunyouApplication mApplication;
	private NetworkCenterEx mNetworkCenter;
	private SelfLocationManager mSelfLocationManager;
	//private BikeLocationManager mBikeLocationManager;
	//private SafeAreaManager mSafeAreaManager;
	private SelfMarket mSelfMarket;
//	private BikeMarket mBikeMarket;
	//private SafeMarket mSafeMarket;
//	private BikeRecordMarket mBikeRecordMarket;
	private MapMarketManager mMapMarketManager;
//	private BikeSubRecordManager mBikeSubRecordManager;
	
	private Handler mHandler;
	
	public static MainMapFragment newInstance(){
//		if(fragment==null){
//			synchronized(MainMapFragment.class){
//				if(fragment==null){
//					fragment=new MainMapFragment();
//				}
//			}
//		}
//		return fragment;
		
		fragment = new MainMapFragment();
		return fragment;
	}
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
    	log.e("MainMapFragment onCreateView");
    	if (mapLayout == null) {
			mapLayout = inflater.inflate(R.layout.mapex_layout, null);
			mRootView =  mapLayout.findViewById(R.id.rootView);
			mMapView = (MapView) mapLayout.findViewById(R.id.map);
			mMapView.onCreate(savedInstanceState);
			if (aMap == null) {
				aMap = mMapView.getMap();
				aMap.setOnMarkerClickListener(this);
				aMap.setInfoWindowAdapter(this);
				aMap.setOnInfoWindowClickListener(this);
			}
			setupViews(mapLayout);
    	}else {
			if (mapLayout.getParent() != null) {
				((ViewGroup) mapLayout.getParent()).removeView(mapLayout);
			}
		}
		return mapLayout;
	}
    
    

    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		initData();
	}
    
    
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mContext = getActivity();
    }
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		log.e("MainMapFragment onCreate");
	}

	@Override
	public void onResume() {
		log.e("MainMapFragment onResume");
		super.onResume();
		mMapView.onResume();
		
//		if (LocationUtil.isGPSEnable(mContext) == false)
//		{
//			showGPSDialog(true);
//		}
		
		if (!mApplication.getBindFlag()){
			showBindDialog(true);
		}

		
//		if (!mApplication.mIsDebug){
//
//			mBikeLocationManager.addObserver(this);
//			mBikeLocationManager.startLocationCheck();
//		}

		mSelfLocationManager.addObserver(this);
		mSelfLocationManager.startLocationCheck();
		
		
//		
//		if (mApplication.isBindDevice()){
//			mBikeMarket.setLastLocation(mBikeLocationManager.getLastLocation());
//		}else{
//			mBikeMarket.clear();
//		}
		mSelfMarket.setLocation(mSelfLocationManager.getLastLocation());

		
		focusPosition();

	}

	/**
	 * 方法必须重写
	 * map的生命周期方法
	 */
	@Override
	public void onPause() {
		log.e("MainMapFragment onPause");
		super.onPause();
		mMapView.onPause();
		
		if (!mApplication.mIsDebug){
//			mBikeLocationManager.removeObservser(this);
//			mBikeLocationManager.stopLocationCheck();
		}
		
		mSelfLocationManager.removeObservser(this);
		mSelfLocationManager.stopLocationCheck();
		
	
		
	//	mBikeMarket.reset();
	}

	/**
	 * 方法必须重写
	 * map的生命周期方法
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		log.e("MainMapFragment onSaveInstanceState");
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 * map的生命周期方法
	 */
	@Override
	public void onDestroy() {
		log.e("MainMapFragment onDestroy");
		super.onDestroy();
		mMapView.onDestroy();
	}
	
	private Dialog mDialog = null;
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
				goSetGpsPage();
			}
		};

		if (bShow)
		{
			mDialog = DialogFactory.creatDoubleDialog(mContext, R.string.dialog_title_gogps, R.string.dialog_msg_gogps,
																R.string.btn_yes, R.string.btn_no, onClickListener);
			mDialog.show();
		}
	
	}
	
	private void showBindDialog(boolean bShow)
	{
		if (mDialog != null)
		{
			mDialog.dismiss();
			mDialog = null;
		}
		
		DialogFactory.ISelectComplete onClickListener = new DialogFactory.ISelectComplete() {

			@Override
			public void onSelectComplete(boolean flag) {
				if (flag){
					Intent intent = new Intent();
					intent.setClass(getActivity(), MoGuActivity.class);
					startActivity(intent);
				}else{
					mApplication.setBindFlag(true);
				}
			}
			
		
		};

		if (bShow)
		{
			mDialog = DialogFactory.creatSelectDialog(mContext, R.string.dialog_title_bind, R.string.dialog_msg_bind,
																R.string.btn_bind, R.string.btn_ignore, onClickListener);
			mDialog.show();
		}
	
	}
	
	private void goSetGpsPage(){
		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(intent);
	}
	
	
	private void setupViews(View view){
		
		mUiSettings = aMap.getUiSettings();
		mUiSettings.setZoomControlsEnabled(false);
		
		mZoomInBtn = (ImageView) view.findViewById(R.id.bt_zoomin_pos);
		mZoomInBtn.setOnClickListener(this);
		
		mZoomOutBtn = (ImageView) view.findViewById(R.id.bt_zoomout_pos);
		mZoomOutBtn.setOnClickListener(this);
		
		mFocusPosition = (ImageView) view.findViewById(R.id.bt_focus_pos);
		mFocusPosition.setOnClickListener(this);	
		
//		mLocationBtn = (ImageView) view.findViewById(R.id.bt_map_location);
//		mLocationBtn.setOnClickListener(this);	
		
		mDistanceBtn = (ImageView) view.findViewById(R.id.bt_map_distance);
		mDistanceBtn.setOnClickListener(this);	
		mDistanceBtn.setVisibility(View.GONE);
	
		mSelfMarket = new SelfMarket(R.drawable.self_pos);	
		//mBikeMarket = new BikeMarket(R.drawable.point_start, R.drawable.bike_point_on, R.drawable.bike_point_off);
		//mSafeMarket = new SafeMarket(R.drawable.safe_pos);
		//mBikeRecordMarket = new BikeRecordMarket(R.drawable.bike, R.drawable.bike_end);
		mMapMarketManager = new MapMarketManager(aMap, mMapView);
		mMapMarketManager.setSelfPos(mSelfMarket);
		//mMapMarketManager.setBikePos(mBikeMarket);
		//mMapMarketManager.setSafeAreaPos(mSafeMarket);
		//mMapMarketManager.setBikeRecordPos(mBikeRecordMarket);
	}
	
	private void initData(){
		

		
		  mSelfLocationManager = SelfLocationManager.getInstance();
	//	  mBikeLocationManager = BikeLocationManager.getInstance();
	//	  mSafeAreaManager = SafeAreaManager.getInstance();
	//	  mBikeSubRecordManager = BikeSubRecordManager.getInstance();
		  mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MSG_REFRESH_SELF:
				case MSG_REFRESH_BIKE:
					int statue = mMapMarketManager.getCurShowState();
					if (MapMarketManager.IViewConstant.IVC_BIKE_POS == statue){
						if (isFistCenter){
							isFistCenter = false;
							if (msg.what == MSG_REFRESH_SELF){
								fircenter(mSelfMarket.getLatLon());
							}
//							else{
//								fircenter(mBikeMarket.getLastLatLon());
//							}
						}
						mMapMarketManager.updateBikePos();
					}				
					break;
				case MSG_REFRESH_AREA:
					BikeType.BikeGetArea area = (BikeGetArea) msg.obj;
//					mSafeMarket.setArea(area);
//					mMapMarketManager.updateAreaPos();
//					moveCamara(mSafeMarket.getLatLon());
//					mBikeMarket.reset();
					break;
				case MSG_REFRESH_BIKERECORD:
					//mBikeRecordMarket.setBikeRecordList(mBikeSubRecordManager.getLinkList());
					//mMapMarketManager.updateBikeRecordPos();
					//moveCamara(mBikeRecordMarket.getBound());
	
					break;
				default:
					break;
				}
			}
			  
		  };
		  
		  mApplication = YunyouApplication.getInstance();
		  
		  mNetworkCenter = NetworkCenterEx.getInstance();
//		  mNetworkCenter.setDid("A000000012000087");
//		  mNetworkCenter.setSid("A12null000007135");
//		  mNetworkCenter.initNetwork();
	}
	

	
	private void focusPosition(){
		log.e("focusPosition");
		LatLng latLng1 = mSelfMarket.getLatLon();	
//		LatLng latLng2 = mBikeMarket.getLastLatLon();
		mMapMarketManager.updateBikePos();
		if (latLng1 == null){
			return ;
		}
		
		updateCamarra(18);
		
		if (latLng1 != null){
			moveCamara(latLng1);
		}
	}
		
		
	private void focusSelfPosition(){
		log.e("focusSelfPosition");
		LatLng latLng1 = mSelfMarket.getLatLon();	
		mMapMarketManager.updateBikePos();
		if (latLng1 != null){
			updateCamarra(18);
			moveCamara(latLng1);
		}else{
			Utils.showToast(mContext, R.string.map_text_noself);
		}
		
//		LatLng latLng2 = mBikeMarket.getLatLon();
//		
//		if (latLng1 == null && latLng2 == null){
//			return ;
//		}
//		
//		mMapMarketManager.showBikePos();
//		updateCamarra(18);
//		if (latLng2 != null){
//			moveCamara(latLng2);
//		}else{
//			moveCamara(latLng1);
//		}
		
//		LatLngBounds bounds = new LatLngBounds(latLng1, latLng2);
//		moveCamara(bounds);
	}
	
	public void searchBike(){
//		mBikeMarket.startRunning();
//		LatLng latLng1 = mBikeMarket.getLastLatLon();
//		mMapMarketManager.updateBikePos();
//		if (latLng1 != null){
//			updateCamarra(18);
//			moveCamara(latLng1);
//		}else{
//			Utils.showToast(mContext, R.string.map_text_searchbike);
//			mBikeLocationManager.requesetNow();
//
//		}
		
	}
	
	public void showBikeRecord(){
		Message msg = mHandler.obtainMessage(MSG_REFRESH_BIKERECORD);
		msg.sendToTarget();
	}

	private boolean isFistCenter = true;
	private void fircenter(LatLng latLng){
		log.e("fircenter");
		updateCamarra(18);
		moveCamara(latLng);
	}
		
	@Override
	public void onClick(View v) {
		switch(v.getId()){
//		case R.id.bt_map_location:
//			mapArea();
//			break;
//		case R.id.bt_map_distance:
//			mapDistance();
//			break;
		case R.id.bt_focus_pos:
//			focusSelfPosition();
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
	
	private PopupWindow mPopupWindow = null;
	public void showRequestDialog(boolean bShow)
	{
	
		if (mPopupWindow != null)
		{
			mPopupWindow.dismiss();
			mPopupWindow = null;
		}
		
		if (bShow)
		{
			mPopupWindow = PopWindowFactory.creatLoadingPopWindow(mContext, R.string.sending_request);
			mPopupWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);
		}
	
	}
	
//	
//	private void mapArea(){
//		mSafeAreaManager.RequestSafeArea(this);
//		showRequestDialog(true);
//	}
//	

	
//	private void mapDistance(){
//		
//		LatLng latLng1= mSelfMarket.getLatLon();
//		LatLng latLng2= mBikeMarket.getLastLatLon();
//		
//		if (latLng1 == null || latLng2 == null){
//			Utils.showToast(mContext, R.string.map_text_nodistance);
//			return ;
//		}
//		
//		float results[]  = new float[1];
//		Location.distanceBetween(latLng1.latitude, latLng1.longitude, 
//				latLng2.latitude,  latLng2.longitude, results);
//		
//		String showString = "";
//		if (results[0] < 1000)
//		{
//			int distance = (int)results[0];
//			showString = "距离" + distance + "米";
//		}else{
//			int distance = (int) (results[0]/1000);
//			showString = "距离" + distance  + "千米";
//		}
//		
//		Utils.showToast(mContext, showString);
//		
//		Builder builder = LatLngBounds.builder().include(latLng1).include(latLng2);
//		moveCamara(builder.build());
//		
//		mMapMarketManager.updateBikePos();
//		
//	}
	

	
	private void zomOut(){
		aMap.moveCamera(CameraUpdateFactory.zoomOut());
	}
	
	private void zomIn(){
		aMap.moveCamera(CameraUpdateFactory.zoomIn());
	}
	
	private void updateCamarra(float zoomLevel){
		log.e("updateCamarra zoomLevel = " + zoomLevel);
		CameraPosition position = aMap.getCameraPosition();
		CameraUpdate object = CameraUpdateFactory.newLatLngZoom(position.target, zoomLevel);
		aMap.moveCamera(object);
	}
	
	private void moveCamara(CameraUpdate object){
		aMap.moveCamera(object);
	}
	
	
	private void moveCamara(LatLng latlon){
		if (latlon != null){
			CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latlon, 16);
			moveCamara(update);
		}
	}
	
	private void moveCamara(LatLngBounds latLngBounds){
		if (latLngBounds != null){
			CameraUpdate update = CameraUpdateFactory.newLatLngBounds(latLngBounds, 14);
			moveCamara( update);
		}
	}
	
	@Override
	public void onLocationUpdate(final LocationEx location) {
		log.e("onLocationUpdate (" + location.getLatitude() + "," + location.getLongitude() + ")");
		getActivity().runOnUiThread(new Runnable() {
		
			@Override
			public void run() {
	
				mSelfMarket.setLocation(location);
				if (mApplication.mIsDebug){
					Utils.showToast(getActivity(), "provider = " + location.getProvider());
				}
	
				Message msg = mHandler.obtainMessage(MSG_REFRESH_SELF);
				msg.sendToTarget();

			}
		});
	}
	

//	@Override
//	public void onBikeLocationUpdate(final LocationEx location) {
//		log.e("onBikeLocationUpdate (" + location.getLatitude() + "," + location.getLongitude() + ")");
//		getActivity().runOnUiThread(new Runnable() {
//		
//			@Override
//			public void run() {
//	
//				boolean isRunning = mBikeMarket.isRunning();
//				log.e("mBikeMarket.isRunning() = " + isRunning);
//				if (isRunning){
//					mBikeMarket.addLocation(location);
//				}else{
//					mBikeMarket.setLastLocation(location);
//				}
//				
//		
//				Message msg = mHandler.obtainMessage(MSG_REFRESH_BIKE);
//				msg.sendToTarget();
//	
//			}
//		});
//	}
	@Override
	public void onInfoWindowClick(Marker market) {
		market.hideInfoWindow();
	}
	@Override
	public View getInfoContents(Marker market) {
//		String title = market.getTitle();
//		if (title == null){
//			return null;
//		}
//		log.e("getInfoContents market.title = " + title);
//		if (title.equals("BIKE")){
//			View infoContent = getActivity().getLayoutInflater().inflate(R.layout.maptip_layout, null);
//			mBikeMarket.render(infoContent);
//			return infoContent;
//		}
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
	
//	@Override
//	public void onRequestFail() {
//		Utils.showToast(mContext, R.string.request_data_fail);
//		showRequestDialog(false);
//	}
//	@Override
//	public void onEmpty() {
//		Utils.showToast(mContext, R.string.safe_text_noarea);
//		showRequestDialog(false);
//	}
//	@Override
//	public void onSafeArea(final BikeGetArea area) {
//		log.e("onSafeArea = " + area);
//		getActivity().runOnUiThread(new Runnable() {
//		
//			@Override
//			public void run() {
//				showRequestDialog(false);
//				if (area == null){
//					Utils.showToast(mContext, R.string.safe_text_areaerror);
//					return ;
//				}
//
//				log.e("area = \n" + area.toString());
//				Message msg = mHandler.obtainMessage(MSG_REFRESH_AREA, area);
//				msg.sendToTarget();
//
//			}
//		});
//	}
	

}
