package com.mobile.yunyou.bike;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
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

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnCameraChangeListener;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.mobile.yunyou.R;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.bike.manager.SafeAreaManager;
import com.mobile.yunyou.bike.manager.SelfLocationManager;
import com.mobile.yunyou.map.data.LocationEx;
import com.mobile.yunyou.map.util.WebManager;
import com.mobile.yunyou.model.BikeType;
import com.mobile.yunyou.model.BikeType.BikeGetArea;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.Courier;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.PopWindowFactory;
import com.mobile.yunyou.util.Utils;

public class SafeActivity extends Activity implements OnCameraChangeListener,IRequestCallback, 
												OnClickListener, 
												OnCheckedChangeListener,
												SelfLocationManager.ILocationUpdate,
												SafeAreaManager.ISafeAreaResult{

	private static final CommonLog log = LogFactory.createLog();
	private static final int MSG_UPDATE_ADRESS = 0x0001;
	private static final int MSG_GET_ADRESS = 0x0002;
	private static final int MSG_GET_AREA = 0x0003;
	
	private Context mContext;
	
	private AMap aMap;
	
	private MapView mMapView;								
	private UiSettings mUiSettings;		
	private View mRootView;

//	public static final double DOUBLE_STUDEN_LON = 114.1017;
//	public static final double DOUBLE_STUDEN_LAT = 22.6644;
//	public static final LatLng ORIGIN_POSITION = new LatLng(DOUBLE_STUDEN_LAT, DOUBLE_STUDEN_LON);// 北京市中关村经纬度
	
	private YunyouApplication mApplication;
	private NetworkCenterEx mNetworkCenterEx;
	private SelfLocationManager mSelfLocationManager;
	private LatLng mLatlng;
	private String mAddress = "";
	private int mRadius;
	private Handler mHandler;

	private EditText mETLocation;
	private CheckBox mCBSwitch;
	private Button mBtnSave;
	private Button mBtnBack;
	
	private ImageView 			mZoomInBtn;							// 缩小
	private ImageView 			mZoomOutBtn;						// 放大
	private ImageView 			mFocusPosition;
	
	private RadioGroup mRGRadius;
	
	private ExecutorService mExecutorService = null;
	private GeocodeSearch mGeocoderSearch;
	
//	private CircleOptions mCircleOptions;
	
	private SafeAreaManager mSafeAreaManager;
	private BikeType.BikeGetArea mArea = null;
	private SafeMarket mSafeMarket;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.safe_layout);
		
		mMapView = (MapView) findViewById(R.id.map);
		mMapView.onCreate(savedInstanceState);
		aMap = mMapView.getMap();
		aMap.setOnCameraChangeListener(this);
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
			updateCamarra(15);
			updateCamarra(mSelfLocationManager.getLastLocation());
			mHandler.sendEmptyMessageDelayed(MSG_GET_AREA, 200);
			isFirstResume = false;
		}
	
		
		mSelfLocationManager.startLocationCheck();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();

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

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
		
	//	mSelfLocationManager.removeObservser(this);
	}
	

	private void setupViews(){
		mRootView = findViewById(R.id.rootView);
		
		
		mUiSettings = aMap.getUiSettings();
		mUiSettings.setZoomControlsEnabled(false);

		mETLocation = (EditText) findViewById(R.id.et_location);
		
		mCBSwitch = (CheckBox) findViewById(R.id.cb_switch);
		
		mBtnSave = (Button) findViewById(R.id.btn_save);
		mBtnSave.setOnClickListener(this);

		mBtnBack = (Button) findViewById(R.id.btn_back);
    	mBtnBack.setOnClickListener(this);
		
		mRGRadius = (RadioGroup) findViewById(R.id.rg_radius);
		mRGRadius.setOnCheckedChangeListener(this);
		((RadioButton) mRGRadius.getChildAt(0)).toggle();
		mRadius = 100;
		
		mZoomInBtn = (ImageView) findViewById(R.id.bt_zoomin_pos);
		mZoomInBtn.setOnClickListener(this);
		
		mZoomOutBtn = (ImageView) findViewById(R.id.bt_zoomout_pos);
		mZoomOutBtn.setOnClickListener(this);
		
		mFocusPosition = (ImageView) findViewById(R.id.bt_focus_pos);
		mFocusPosition.setOnClickListener(this);	
		
	}
	
	private void initData(){
		  mApplication = YunyouApplication.getInstance();
		  mNetworkCenterEx = NetworkCenterEx.getInstance();
//		  mNetworkCenterEx.setDid("A000000012000087");
//		  mNetworkCenterEx.setSid("A12null000007135");
//		  mNetworkCenterEx.initNetwork();
		  
		  mSelfLocationManager = SelfLocationManager.getInstance();
	//	  mSelfLocationManager.addObserver(this);
		  
		  if (mExecutorService == null)
			{
				mExecutorService = Executors.newFixedThreadPool(1);
			}
		  mGeocoderSearch = new GeocodeSearch(this);
		  
		  mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				switch(msg.what){
				case MSG_UPDATE_ADRESS:
					String address = (String) msg.obj;
					updateAddress(address);
					break;
				case MSG_GET_ADRESS:
					mExecutorService.execute(new TaskRunnable(mGeocoderSearch, mLatlng.latitude, mLatlng.longitude));
					break;
				case MSG_GET_AREA:
					mSafeAreaManager.RequestSafeArea(SafeActivity.this);
					showRequestDialog(true);
					break;
				}
			}
			  
		  };
//			mCircleOptions = new CircleOptions();
//			mCircleOptions.strokeColor(Color.argb(50, 1, 1, 100));
//			mCircleOptions.fillColor(Color.argb(50, 1, 1, 100));
//			mCircleOptions.strokeWidth(1);
		  mSafeAreaManager = SafeAreaManager.getInstance();
			mSafeMarket = new SafeMarket(R.drawable.red_point); 
			
			mArea = new BikeType.BikeGetArea();
			
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
			mPopupWindow = PopWindowFactory.creatLoadingPopWindow(this, R.string.request_data);
			mPopupWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);
		}
	
	}

	private void updateCamarra(LocationEx location){
		if (location != null){
			LatLng latLng = new LatLng(location.getOffsetLat(), location.getOffsetLon());
			int zoomLevel = getZoomByRadius(mRadius);
			log.e("updateCamarra (" + location.getOffsetLat() + ", " + location.getOffsetLon() + "),zoomLevel = " + zoomLevel);
			if (zoomLevel != 0){
				CameraUpdate object = CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel);
				aMap.moveCamera(object);
			}
		}
	}
	
	private void moveCamara(LatLng latlon, int radius){
		float zoomLevel = getZoomByRadius(radius);
		if (latlon != null){
			CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latlon, zoomLevel);
			aMap.moveCamera(update);
		}
	}
	
//	private void updateCamarra(BikeType.BikeGetArea object){
//		if (object != null){
//			mArea = object;
//			mRadius = mArea.mRadius;
//			LatLng latLng = new LatLng(object.mOffsetLat, object.mOffsetLon);
//			int zoomLevel = getZoomByRadius(object.mRadius);
//			if (zoomLevel != 0){
//				CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel);
//				aMap.moveCamera(update);
//			}
//		}
//	}
	
	private void updateCamarra(float zoomLevel){
		log.e("updateCamarra zoomLevel = " + zoomLevel);
		CameraPosition position = aMap.getCameraPosition();
		CameraUpdate object = CameraUpdateFactory.zoomTo(zoomLevel);
		aMap.moveCamera(object);
	}
	
	private void updateLanLon(LatLng value){
		mLatlng = value;
	
		updateCircle(value);
	}
	private void updateAddress(String value){
		mAddress = value;
		log.e("updateAddress = " + mAddress);
		mETLocation.setText(mAddress);
	}
	
	private void updateCircle(LatLng value){
		log.e("updateCirlce mRadius = " + mRadius);
		
		mArea.mOffsetLat = value.latitude;
		mArea.mOffsetLon = value.longitude;
		mArea.mRadius = mRadius;
		mSafeMarket.setArea(mArea);
		aMap.clear();
		aMap.addMarker(mSafeMarket.newMarkerOptions());
		aMap.addCircle(mSafeMarket.newCircleOptions());
		
//		mCircleOptions.center(value);
//		mCircleOptions.radius(mRadius);
//		aMap.clear();
//		aMap.addCircle(mCircleOptions);

	}
	
	
	@Override
	public void onCameraChange(CameraPosition position) {
		LatLng traget = position.target;
		updateLanLon(traget);
	}

	@Override
	public void onCameraChangeFinish(CameraPosition position) {
		log.e("onCameraChangeFinish");
		LatLng traget = position.target;
		updateLanLon(traget);
		mHandler.removeMessages(MSG_GET_ADRESS);
		mHandler.sendEmptyMessageDelayed(MSG_GET_ADRESS, 1000);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_save:
			save();
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
	
	@Override
	public void onCheckedChanged(RadioGroup radioGroup, int pos) {
		int radius = getRadiusByRB(radioGroup);
		if (radius != 0){
			mRadius = radius;
		}

		int zoomLevel =  getZoomByRadius(mRadius);
		if (zoomLevel != 0){
			updateCamarra(zoomLevel);
		}

	}
	
	public int getZoomByRadius(int radius){
		switch(radius){
		case 100:
			return 18;
		case 300:
			return 17;
		case 800:
			return 15;
		case 1500:
			return 14;
		case 3000:
			return 13;
		}
		
		return 18;
	}
	
	public int getRadiusByRB(RadioGroup radioGroup){
		int id = radioGroup.getCheckedRadioButtonId();
		switch(id){
			case R.id.rb1:
				return 100;
			case R.id.rb2:
				return 300;
			case R.id.rb3:
				return 800;
			case R.id.rb4:
				return 1500;
			case R.id.rb5:
				return 3000;
		}	
		return 0;
	}
	
	public void setCheckedByRadius(int radius){
		
		int index = 0;
		switch(radius){
			case 100:
				index = 0;
				break;
			case 300:
				index = 1;
				break;
			case 800:
				index = 2;
				break;
			case 1500:
				index = 3;
				break;
			case 3000:
				index = 4;
				break;
			default:
					break;
		}
		((RadioButton) mRGRadius.getChildAt(index)).toggle();
	}
	
	private void save(){
	
		if (mCBSwitch.isChecked()){
			log.e("save mLatlng = " + mLatlng + "mRadius = " + mRadius);
			if (mLatlng != null){
				BikeType.BikeSetArea object = new BikeType.BikeSetArea();
				object.mLat = mLatlng.latitude;
				object.mLon = mLatlng.longitude;
				object.mRadius = mRadius;
				object.mName = mAddress;
				mNetworkCenterEx.StartRequestToServer(BikeType.BIKE_SETAREA_MASITD, object, this);
			}
		}else{
			mNetworkCenterEx.StartRequestToServer(BikeType.BIKE_DELAREA_MASITD, null, this);
		}

		showRequestDialog(true);
	}
	
	
	
	private void zomOut(){
		aMap.moveCamera(CameraUpdateFactory.zoomOut());
	}
	
	private void zomIn(){
		aMap.moveCamera(CameraUpdateFactory.zoomIn());
	}
	

	private void focusPosition(){
		updateCamarra(mSelfLocationManager.getLastLocation());	
	}
	
	@Override
	public boolean onComplete(int requestAction, ResponseDataPacket dataPacket) {

		String jsString = "null";
		if (dataPacket != null)
		{
			jsString = dataPacket.toString();
		}
	
		log.e("requestAction = " + Utils.toHexString(requestAction) + "\nResponseDataPacket = \n" +jsString);
		
		
		switch(requestAction){
		case BikeType.BIKE_SETAREA_MASITD:
			onBikeSetAreaResult(dataPacket);
			break;
		case BikeType.BIKE_DELAREA_MASITD:
			onBikeDelAreaResult(dataPacket);
			break;
		}
		
		showRequestDialog(false);
		
		return true;
	}

	private void onBikeSetAreaResult(ResponseDataPacket dataPacket)
	{
		if (dataPacket == null || dataPacket.rsp == 0)
		{
			Utils.showToast(this, R.string.set_data_fail);
			
			return ;
		}
		
		Utils.showToast(this, R.string.toask_setArea_success);
		
		
	}
	
	private void onBikeDelAreaResult(ResponseDataPacket dataPacket)
	{
		if (dataPacket == null || dataPacket.rsp == 0)
		{
			Utils.showToast(this, R.string.set_data_fail);
			
			return ;
		}
		
		Utils.showToast(this, R.string.toask_delArea_success);
		
		
	}

	@Override
	public void onLocationUpdate(final LocationEx location, AMapLocation aMapLocation) {

//		runOnUiThread(new Runnable() {
//			
//			@Override
//			public void run() {
//				updateCamarra(location);
//			}
//		});
		

	}


	class TaskRunnable implements Runnable
	{
		
		private Courier mCourier = null;
		private GeocodeSearch mGeocodeSearch;
		private double lat;
		private double lon;
		
		public TaskRunnable(GeocodeSearch object, double lat, double lon)
		{
			mGeocodeSearch = object;
			this.lat = lat;
			this.lon = lon;
		}
		
		@Override
		public void run() {
			String address = WebManager.getAdressByGaodeEX(mGeocoderSearch, lat, lon);
			Message msg = mHandler.obtainMessage(MSG_UPDATE_ADRESS, address);
			msg.sendToTarget();
		}
	}


	@Override
	public void onRequestFail() {
		Utils.showToast(mContext, R.string.request_data_fail);
		showRequestDialog(false);
		updateCamarra(mSelfLocationManager.getLastLocation());
	}
	@Override
	public void onEmpty() {
		Utils.showToast(mContext, R.string.safe_text_noarea);
		showRequestDialog(false);
		updateCamarra(mSelfLocationManager.getLastLocation());
	}
	@Override
	public void onSafeArea(final BikeGetArea area) {
		log.e("onSafeArea = " + area);
		runOnUiThread(new Runnable() {
		
			@Override
			public void run() {
				showRequestDialog(false);
				if (area == null){
					Utils.showToast(mContext, R.string.safe_text_areaerror);
					updateCamarra(mSelfLocationManager.getLastLocation());
					return ;
				}
				
				Utils.showToast(mContext, R.string.safe_text_areasuccess);
				mArea = area;
				updateAddress(mArea.mName);
				moveCamara(new LatLng(mArea.mOffsetLat, mArea.mOffsetLon), mArea.mRadius);
				setCheckedByRadius(mArea.mRadius);
				

			}
		});
	}
}
