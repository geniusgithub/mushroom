//package com.mobile.yunyou.map;
//
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.app.admin.DevicePolicyManager;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.location.Location;
//import android.location.GpsStatus.Listener;
//import android.net.NetworkInfo.State;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.provider.Settings;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.amap.mapapi.core.GeoPoint;
//import com.amap.mapapi.map.MapActivity;
//import com.amap.mapapi.map.MapController;
//import com.amap.mapapi.map.MapView;
//import com.amap.mapapi.map.Overlay;
//import com.amap.mapapi.route.Route;
//import com.mobile.yunyou.ISelDeviceUnbind;
//import com.mobile.yunyou.R;
//import com.mobile.yunyou.TestActivity;
//import com.mobile.yunyou.YunyouApplication;
//import com.mobile.yunyou.device.DeviceSetActivity;
//import com.mobile.yunyou.map.data.BaseStationLocationListener;
//import com.mobile.yunyou.map.data.BaseStationLocationManager;
//import com.mobile.yunyou.map.data.DeviceAreaManager;
//import com.mobile.yunyou.map.data.DeviceHistoryManager;
//import com.mobile.yunyou.map.data.DeviceLocationListener;
//import com.mobile.yunyou.map.data.DeviceLocationManager;
//import com.mobile.yunyou.map.data.DevicePosManager;
//import com.mobile.yunyou.map.data.GPSLocationManager;
//import com.mobile.yunyou.map.data.GpsLocationListener;
//import com.mobile.yunyou.map.data.IUpdateLocationRunable;
//import com.mobile.yunyou.map.data.LocationEx;
//import com.mobile.yunyou.map.util.LocationUtil;
//import com.mobile.yunyou.model.DeviceSetType;
//import com.mobile.yunyou.model.GloalType;
//import com.mobile.yunyou.model.ResponseDataPacket;
//import com.mobile.yunyou.network.IRequestCallback;
//import com.mobile.yunyou.network.NetworkCenterEx;
//import com.mobile.yunyou.util.CommonLog;
//import com.mobile.yunyou.util.DialogFactory;
//import com.mobile.yunyou.util.LogFactory;
//import com.mobile.yunyou.util.Utils;
//
//
//
//
//
//public class MapABCActivity extends MapActivity implements OnClickListener, IRequestCallback, ISelDeviceUnbind{
//
//	
//	private static final CommonLog log = LogFactory.createLog();
//	
//	
//	public static final double DOUBLE_STUDEN_LON = 114.1017;
//	public static final double DOUBLE_STUDEN_LAT = 22.6644;
//	
//	
//	
//	private MapView mMapView;									//地图VIEW
//	private MapController mMapController;						//控制噄1�7
//	private List<Overlay> mOverlayList;							//地图图层容器
//	private PopViewManager mPopViewManager;
//	private DeviceAreaPopViewManager mDeviceAreaPopViewManager;
//	private DeviceHistoryPopViewManager mDeviceHistoryPopViewManager;
//	
//	private Button 			mZoomInBtn;							// 缩小
//	private Button 			mZoomOutBtn;						// 放大
//	private Button 			mFocusPosition;
//
//	
//	private View 			mRootView;		
//	private Button 			mMenuBtn;	
//
//	
//	
//	private MapMenuPopWindow mMapMenuPopWindow;	
//	private Button mMenuDownBtn;
//						
//							
//
//	private YunyouApplication mApplication;
//	private NetworkCenterEx mNetworkCenter;
//	
//	private DeviceSetProxy mDeviceSetProxy;
//	
//	private GPSLocationManager mGpsLocationManager;
//	private GpsLocationListener mGpsLocationListener;
//	private PositionOverlay mSelfPositionOverlay;
//	
//	
//	private BaseStationLocationManager mBaseStationLocationManager;
//	private BaseStationLocationListener mBaseStationLocationListener;
//
//	private DeviceLocationListener mDeviceLocatinListener;
//	private DeviceLocationManager mDeviceLocationManager;
//	private DevicePosOverlay mDevicePositionOverlay;
//	
//	private DevicePosManager mDevicePosManager;
//	private DeviceAreaManager mDeviceAreaManager;
//	private DeviceAreaOverlay mDeviceAreanOverlay;
//	
//	
//	private DeviceHistoryManager mDeviceHistoryManager;
//	private DeviceHistoryOverlay mDeviceHistoryOverlay;
//	
//	
//	private MapPositionManager mMapPositionManager;
//	@Override
//	protected void onCreate(Bundle bundle) {
//		// TODO Auto-generated method stub
//		super.onCreate(bundle);
//		
//		setContentView(R.layout.mapabc_layout);
//		
//		initView();
//		
//		initLogic();
//		
//	}
//
//	@Override
//	protected void onPause() {
//		// TODO Auto-generated method stub
//		super.onPause();
//
//		
//		mGpsLocationManager.unRegisterListen();
//		mBaseStationLocationManager.unRegisterListen();
//		mDeviceLocationManager.unRegisterListen();
//		
//		mMapMenuPopWindow.dismiss();
//		mDeviceSetProxy.showDeviceSelWindowAsDropDown(null, false);
//		
//		mMapController.stopAnimation(true);
//		
//		mDeviceAreanOverlay.clear();
//		mPopViewManager.showPopView(false);
//		
//		mDeviceHistoryOverlay.clear();
//		
////		if (LocationUtil.isGPSEnable(this) == false)
////		{
////			LocationUtil.toggleGPS(this);
////		}
//	}
//
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//
//
//		
//		if (LocationUtil.isGPSEnable(this) == false)
//		{
//			showGPSDialog(true);
//		}
//
//		mGpsLocationManager.registerListen(mGpsLocationListener);
//		mBaseStationLocationManager.registerListen(mBaseStationLocationListener);
//		mDeviceLocationManager.registerListen(mDeviceLocatinListener);
//		
//		if (!isFistCenter)
//		{
//			focusPosition();
//		}
//		
//	
//		mMapPositionManager.showDevicePos();
//	}
//	
//	@Override
//	protected void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//		
//		mApplication.unRegisterDeviceBtn();
//	}
//
//	public void initView()
//	{
//		
//		
//		mRootView = findViewById(R.id.rootView);
//		
//		mMapView = (MapView) findViewById(R.id.main_mapView);
//		//mMapView.setBuiltInZoomControls(true);  			// 设置启用内置的缩放控仄1�7
//		mMapController = mMapView.getController();  		// 得到mMapView的控制权,可以用它控制和驱动平移和缩放
//		mOverlayList = mMapView.getOverlays();				// 得到图层容器
//		mMapController.setZoom(13);    						//设置地图zoom级别
//		GeoPoint point = new GeoPoint((int) (DOUBLE_STUDEN_LAT * 1E6),
//				(int) (DOUBLE_STUDEN_LON * 1E6));			//用给定的经纬度构造一个GeoPoint＄1�7
//		mMapController.setCenter(point);  					//设置地图中心炄1�7
//		
//		View mPopView = LayoutInflater.from(this).inflate(R.layout.map_poptip_layout, null);
//		mMapView.addView(mPopView,new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT, 
//															MapView.LayoutParams.WRAP_CONTENT,
//															null,
//															MapView.LayoutParams.BOTTOM_CENTER));  
//
//		mPopView.setVisibility(View.GONE);
//		
//		
//		mPopViewManager = new PopViewManager(this, mMapView, mPopView);		
//		mDeviceAreaPopViewManager = new DeviceAreaPopViewManager(this, mMapView, mPopView);
//		mDeviceHistoryPopViewManager = new DeviceHistoryPopViewManager(this, mMapView, mPopView);
//		
//		
//		mMenuBtn = (Button) findViewById(R.id.bt_map_menu);
//		mMenuBtn.setOnClickListener(this);
//		
//		
//		mMenuDownBtn = (Button) findViewById(R.id.bt_menu_down);
//		mMenuDownBtn.setOnClickListener(this);
//		
//		
//		mMapMenuPopWindow = new MapMenuPopWindow(this);
//		mMapMenuPopWindow.setClickiListener(this);
//		
//		mZoomInBtn = (Button) findViewById(R.id.bt_zoomin_pos);
//		mZoomInBtn.setOnClickListener(this);
//		
//		mZoomOutBtn = (Button) findViewById(R.id.bt_zoomout_pos);
//		mZoomOutBtn.setOnClickListener(this);
//		
//		mFocusPosition = (Button) findViewById(R.id.bt_focus_pos);
//		mFocusPosition.setOnClickListener(this);	
//		
//		mSelfPositionOverlay = new PositionOverlay(this, getResources().getDrawable(R.drawable.self_pos), mPopViewManager);
//		mOverlayList.add(mSelfPositionOverlay);
//
//		mDevicePositionOverlay = new DevicePosOverlay(this, getResources().getDrawable(R.drawable.device_pos), mPopViewManager);
//		mOverlayList.add(mDevicePositionOverlay);
//		
//		mDeviceAreanOverlay = new DeviceAreaOverlay(this, getResources().getDrawable(R.drawable.point1), mDeviceAreaPopViewManager);
//		mOverlayList.add(mDeviceAreanOverlay);
//
//		mDeviceHistoryOverlay = new DeviceHistoryOverlay(this, getResources().getDrawable(R.drawable.point1), mDeviceHistoryPopViewManager);
//		mOverlayList.add(mDeviceHistoryOverlay);
//		
//		mMapPositionManager = new MapPositionManager(mOverlayList);
//		mMapPositionManager.setDevicePosOverlay(mDevicePositionOverlay);
//		mMapPositionManager.setPosOverlay(mSelfPositionOverlay);
//		mMapPositionManager.setDeviceAreaOverlay(mDeviceAreanOverlay);
//		mMapPositionManager.setDeviceHistoryOverlay(mDeviceHistoryOverlay);
//		
//		mPopViewManager.bindMapPosMng(mMapPositionManager);
//		mDeviceAreaPopViewManager.bindMapPosMng(mMapPositionManager);
//		
//	}
//	
//	public void initLogic()
//	{
//		mApplication = YunyouApplication.getInstance();
//		mNetworkCenter = NetworkCenterEx.getInstance();
//		
//		mDeviceSetProxy =  new DeviceSetProxy(this, mRootView, mHandler);
//		//mDeviceSetProxy.setDeviceSelView(mMenuDownView);
//	
//		
//		
//		Location location = null;
//
//		mGpsLocationManager = new GPSLocationManager(this);
//		mGpsLocationListener = new GpsLocationListener(this, mHandler, new UpdateLocationRunnable());
//		
//		mBaseStationLocationManager = new BaseStationLocationManager(this);
//		mBaseStationLocationListener = new BaseStationLocationListener(this, mHandler, new UpdateStationLocationRunnable());
//	
//		mDeviceLocationManager = new DeviceLocationManager(this);
//		mDeviceLocatinListener = new DeviceLocationListener(this, mHandler, new UpdateDeviceLocationRunnable());
//		mDeviceSetProxy.setLocationMng(mDeviceLocationManager);
//		
//		mApplication.registerDeviceBtn(mMenuDownBtn);
//		mApplication.setSelDevChange(this);
//		
//		mApplication.setCurDevice(mApplication.getCurDevice());
//		
//		mDevicePosManager = new DevicePosManager(mHandler);
//		mDeviceAreaManager =  DeviceAreaManager.getInstance(mHandler);
//		mDeviceHistoryManager = DeviceHistoryManager.getInstance(mHandler);
//	}
//	
//	private void goSetGpsPage(){
//		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//		startActivity(intent);
//	}
//	
//	private Dialog mDialog = null;
//	private void showGPSDialog(boolean bShow)
//	{
//		if (mDialog != null)
//		{
//			mDialog.dismiss();
//			mDialog = null;
//		}
//		
//		OnClickListener onClickListener = new OnClickListener() {
//			
//			@Override
//			public void onClick(View view) {			
//				goSetGpsPage();
//			}
//		};
//
//		if (bShow)
//		{
//			mDialog = DialogFactory.creatDoubleDialog(this, R.string.dialog_title_gogps, R.string.dialog_msg_gogps,
//																R.string.btn_yes, R.string.btn_no, onClickListener);
//			mDialog.show();
//		}
//	
//	}
//	
//
//	// 更新android API获取位置的显示
//	class UpdateLocationRunnable implements IUpdateLocationRunable
//	{
//		private LocationEx mlLocation;
//		private String adString;
//		
//		public UpdateLocationRunnable()
//		{
//			
//		}
//		
//		public void setLocation(LocationEx location)
//		{
//			mlLocation = location;
//		}
//		
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//
////			
//			if (mlLocation == null)
//			{
//				log.e("UpdateLocationRunnable	mlLocation = null!");
//				return ;
//			}
//		
//			if (LocationUtil.isBetterLocation(mlLocation, mSelfPositionOverlay.getLastLocation()))
//			{
//				mSelfPositionOverlay.setLocation(mlLocation);
//				
//				if (isFistCenter)
//				{
//					firsCenter(mlLocation);
//				}
//				
//				mMapView.postInvalidate();
//			}
//				
//		
//		}
//		
//	}
//	
//	// 更新google基站获取位置显示
//	class UpdateStationLocationRunnable implements IUpdateLocationRunable
//	{
//		private LocationEx mlLocation;
//		private String adString;
//		
//		public UpdateStationLocationRunnable()
//		{
//		
//		}
//		
//		public void setLocation(LocationEx location)
//		{
//			mlLocation = location;
//		}
//		
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			log.e("UpdateStationLocationRunnable        run");
//			
//			if (mlLocation == null)
//			{
//				log.e("UpdateStationLocationRunnable	mlLocation = null!");
//				return ;
//			}
//			
//			if (LocationUtil.isBetterLocation(mlLocation, mSelfPositionOverlay.getLastLocation()))
//			{
//				mSelfPositionOverlay.setLocation(mlLocation);
//				
//				if (isFistCenter)
//				{
//					firsCenter(mlLocation);
//				}
//				
//				mMapView.postInvalidate();
//			}else{
//				log.e("no better");
//			}
//			
//			
//		}
//
//	}
//	
//	
//	// 更新设备获取位置的显礄1�7
//	class UpdateDeviceLocationRunnable implements IUpdateLocationRunable
//	{
//		private LocationEx mlLocation;
//		private String adString;
//		
//		public UpdateDeviceLocationRunnable()
//		{
//		
//		}
//		
//		public void setLocation(LocationEx location)
//		{
//			mlLocation = location;
//		}
//		
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			
//			if (mlLocation == null)
//			{
//				log.e("UpdateDeviceLocationRunnable	mlLocation = null!");
//				return ;
//			}
//			
//		
//			mDevicePosManager.addLocationEx(mlLocation);
//
//			Message msg = mHandler.obtainMessage(MAP_MESSAGE_REFRESH_POS);
//			msg.sendToTarget();
//
//		}
//
//		
//	}
//	
//	private boolean isFistCenter = true;
//    
//    
//    // 首次出现位置定位，地图中心移至该位置
//    public void firsCenter(Location location)
//    {
//    	try {
//    		if (location != null)
//        	{
//    			GeoPoint focusGeoPoint = new GeoPoint((int) (location.getLatitude() * 1E6),
//    					(int) (location.getLongitude() * 1E6));
//    		
//    			mMapController.animateTo(focusGeoPoint);
//    			
//    			isFistCenter = false;
//        	}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		
//	
//    	
//    }
//	
//    public final static int MAP_MESSAGE_REFRESH_POS = 0x0100;
//    public final static int MAP_MESSAGE_REFRESH_DEVICE_AREA = 0x0101;
//    public final static int MAP_MESSAGE_REFRESH_DEVICE_HISTORY = 0x0102;
//    public final static int MAP_MESSAGE_REFRESH_MOVE_POINT = 0x0103;
//    
//	private Handler mHandler = new Handler() {
//		public void handleMessage(Message msg) {
//	//		log.e("Map mHandler.msg = " + msg.what);
//			switch(msg.what)
//			{
//			case MAP_MESSAGE_REFRESH_POS:
//				int statue = mMapPositionManager.getCurShowState();
//				if (MapPositionManager.IViewConstant.IVC_DEVICE_POS == statue){
//					refreshDevicePos();
//				}				
//				break;
//			case MAP_MESSAGE_REFRESH_DEVICE_AREA:
//				refreshDeviceArea();
//				break;
//			case MAP_MESSAGE_REFRESH_DEVICE_HISTORY:
//				refreshDeviceHistory();
//				break;
//			case MAP_MESSAGE_REFRESH_MOVE_POINT:
//				moveDeviceHistory();
//				break;
//			}
//		}
//    };
//	
//    
//    private void refreshDevicePos()
//    {
//    	
//    	String did = mApplication.getCurDid();
//   // 	log.e("mApplication.getCurDid() = " + did);
//    	if (did != null && did.equals("") == false)
//    	{
//    		LocationEx locationEx = mDevicePosManager.getLocationExExist(did);
//    		Bitmap bitmap = mDevicePosManager.getBitmap(did);
//    
//    		if (locationEx != null)
//    		{
//    			mDevicePositionOverlay.setBitmap(bitmap);
//    			mDevicePositionOverlay.setLocation(locationEx);
//				
//				if (isFistCenter)
//				{
//					firsCenter(locationEx);
//				}else{
//					focusPosition();
//				}
//			
//				mMapView.postInvalidate();
//				
//				return ;
//    		}
//    	}
//    	
//    
//    	mDevicePositionOverlay.clear();
//    //	focusPosition();
//    	mMapView.postInvalidate();
//    }
//    
//    private void refreshDeviceArea()
//    {
//		DeviceSetType.DeviceAreaResult object = mDeviceAreaManager.getAreaObject();
//    	if (object != null)
//    	{
//    		mDeviceAreanOverlay.setArea(object);
//    	}else{
//
//    		mDeviceAreanOverlay.clear();
//    	}	
//    	
//    	focusDeviceArea();
//    	mPopViewManager.showPopView(false);
//    	mMapView.postInvalidate();
//    	
//    	mMapPositionManager.showAreaPos();
//    }
//    
//    private void refreshDeviceHistory()
//    {
//    	log.e("refreshDeviceHistory");
//    	 List<DeviceSetType.DeviceHistoryResult> list = mDeviceHistoryManager.getLocationList();
//    	 Set<Integer> set = mDeviceHistoryManager.getStaticPointSet();
//    	 
//    	if (list != null)
//    	{
//    		mDeviceHistoryOverlay.setLocationList(list, set);
//    		
//    		focusDeviceHistory();
//    	}else{
//    		Utils.showToast(this, "无法显示轨迹信息");
//    	}	
//
//    
//    	mMapView.postInvalidate();
//    	
//    	mMapPositionManager.showHistoryPos();
//    }
//    
//    private void moveDeviceHistory()
//    {
//    //	log.e("moveDeviceHistory");
//    	
//    	boolean ret = mDeviceHistoryOverlay.showNext();
//    	if (!ret){
//    		mDeviceHistoryOverlay.reset();
//    		mDeviceHistoryManager.stopTimer();
//    	}
//    
//    	mMapView.postInvalidate();
//    	
//    	
//    }
//
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		switch(v.getId())
//		{
//		case R.id.bt_map_menu:
//			mMapMenuPopWindow.showWindow(mRootView);
//			mPopViewManager.showPopView(false);
//	//		goTestActivity();
//			break;
//		case R.id.btn_menu_deviceset:
//			goDeviceSetActivity();
//		//	goDeviceSetActivityEx();	
//			break;
//		case R.id.btn_menu_listener:
//			listener();
//			break;
//		case R.id.btn_menu_shutdown:
//			mDeviceSetProxy.shoutDown();
//	//		goTestActivity();
//			break;
//		case R.id.btn_menu_distance:
//	//		focusPosition();
//			calDistance();
//			break;
//		case R.id.btn_menu_history:
//			log.e("btn_menu_history");
//	//		mDeviceSetProxy.showRangeTimeDialog();
//			mDeviceSetProxy.showCustomTimeExDialog();
//			mMapMenuPopWindow.dismiss();
//			break;
//		case R.id.btn_menu_pen:
//			log.e("btn_menu_pen");
//			mDeviceSetProxy.RequestDeviceArea();
//			mMapMenuPopWindow.dismiss();
//			break;
//		case R.id.bt_zoomin_pos:
//			zoomIn();
//			break;
//		case R.id.bt_zoomout_pos:
//			zoomOut();
//			break;
//		case R.id.bt_focus_pos:
//			focusPosition();
//			break;
//		case R.id.bt_menu_down:
//			mDeviceSetProxy.showDeviceSelWindowAsDropDown(mMenuDownBtn, !mDeviceSetProxy.isDeviceSelWindowShown());
//		    mPopViewManager.showPopView(false);
//		//	goTestActivity();
//			break;
//			default:
//				break;
//		}
//	}
//	
//	public void setMenuDownBtnText(String text)
//	{
//		mMenuDownBtn.setText(text);
//	}
//	
//	public void zoomIn()
//	{
//		int lev = mMapView.getZoomLevel();
//		mMapController.setZoom(lev - 1);
//	}
//	
//	public void zoomOut()
//	{
//		int lev = mMapView.getZoomLevel();
//		mMapController.setZoom(lev + 1);
//	}
//	
//	public void focusPosition()
//	{
//		
//			GeoPoint geoPoint1 = mSelfPositionOverlay.getGeoPoint();
//			GeoPoint geoPoint2 = mDevicePositionOverlay.getGeoPoint();
//			
//			if (geoPoint1 == null && geoPoint2 == null)
//			{
//				Utils.showToast(this, R.string.toask_tip_unable_focus);
//				return ;
//			}
//			
//			mPopViewManager.showPopView(false);
//			
//			mMapPositionManager.showDevicePos();
//			
//			if (geoPoint1 == null)
//			{
//				mMapController.animateTo(geoPoint2);
//				return ;
//			}
//			
//			if (geoPoint2 == null)
//			{
//				mMapController.animateTo(geoPoint1);
//				return ;
//			}
//			
//		
//			mMapController.animateTo(geoPoint2);
////			LocationEx location1  = mSelfPositionOverlay.getLastLocation();
////			LocationEx location2 = mDevicePositionOverlay.getLastLocation();
////			
////			double lat = (location1.getOffsetLat() + location2.getOffsetLat()) / 2;
////			double lon = (location1.getOffsetLon() + location2.getOffsetLon()) / 2;
////			
////			GeoPoint point = new GeoPoint((int)(lat * 1E6), (int)(lon * 1E6));
////			mMapController.animateTo(point);
////			
////			
////			List<GeoPoint> list = new ArrayList<GeoPoint>();
////			list.add(geoPoint1);
////			list.add(geoPoint2);
////			list.add(point);
////			
////			mMapController.setFitView(list);		
//			
//			mMapView.postInvalidate();
//			
//	}
//	
//	
//	public void focusDeviceArea()
//	{
//		log.e("focusDeviceArea");
//		GeoPoint geoPoint = mDeviceAreanOverlay.getGeoPoint();
//		if (geoPoint == null)
//		{
//			return ;
//		}
//		
//		mDeviceAreanOverlay.showPopView();
//		
//		mMapController.animateTo(geoPoint);
//		
//		
//	}
//	
//	public void focusDeviceHistory()
//	{
//		List<GeoPoint> geoPointList = mDeviceHistoryOverlay.getGeoPoint();
//		if (geoPointList == null)
//		{
//			Utils.showToast(this, "无法显示轨迹信息");
//			return ;
//		}
//		
//		if (geoPointList.size() == 1){
//			mMapController.animateTo(geoPointList.get(0));
//		}else{
//			mMapController.setFitView(geoPointList);
//		}
//		
//		
//		
//		
//	}
//	 
//	 public void goDeviceSetActivity()
//	 {
//		 if (mApplication.isBindDevice() == false)
//		 {
//			 Utils.showToast(this, R.string.toask_tip_unbind);
//			 return ;
//		 }
//		 
////		 if (mApplication.isDeviceOnline() == false)
////		 {
////			 Utils.showToast(this, R.string.toask_tip_unable_setting);
////			 return ;
////		 }
//		 
//			Intent intent = new Intent();
//	    	intent.setClass(this, DeviceSetActivity.class);
//	    	startActivity(intent);
//	 }
//	 
//	 public void listener()
//	 {
//		 if (mApplication.isBindDevice() == false)
//		 {
//			 Utils.showToast(this, R.string.toask_tip_unbind);
//			 return ;
//		 }	 
//		 
//		 if (mApplication.isDeviceOnline() == false)
//		 {
//			 Utils.showToast(this, R.string.toask_tip_unable_listener);
//			 return ;
//		 }
//	
//		 mDeviceSetProxy.showListenerWindow();
//
//	 }
//	 
//	 public void calDistance()
//	 {
//		 
//
// 
//		Location location1 = mSelfPositionOverlay.getLastLocation();
//		Location location2 = mDevicePositionOverlay.getLastLocation();
//
//		if (location1 == null || location2 == null)
//		{
//			Utils.showToast(this, R.string.toask_tip_unable_distance);
//			return ;
//		}
//		
//		float results[]  = new float[1];
//		Location.distanceBetween(location1.getLatitude(), location1.getLongitude(), 
//										location2.getLatitude(),  location2.getLongitude(), results);
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
//		Utils.showToast(this, showString);
//
//
//		GeoPoint geoPoint1 = mSelfPositionOverlay.getGeoPoint();
//		GeoPoint geoPoint2 = mDevicePositionOverlay.getGeoPoint();		
//		List<GeoPoint> list = new ArrayList<GeoPoint>();
//		list.add(geoPoint1);
//		list.add(geoPoint2);
//		
//		mMapController.setFitView(list);	
//		mMapPositionManager.showDevicePos();
//	 }
//
//	 public void goDeviceSetActivityEx()
//	 {
////			Intent intent = new Intent();
////	    	intent.setClass(this, TestActivity.class);
////	    	startActivity(intent);
//	 }
//	 
//
//	 public void goTestActivity()
//	 {
//			Intent intent = new Intent();
//	    	intent.setClass(this, TestActivity.class);
//	    	startActivity(intent);
//	 }
//
//	@Override
//	public boolean onComplete(int requestAction, ResponseDataPacket dataPacket) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public void onSelDeviceChange() {
//
//
//		 GloalType.DeviceInfoEx deviceInfoEx = mApplication.getCurDevice();
//		 if (deviceInfoEx != null){
//			 mMenuDownBtn.setText(deviceInfoEx.mAlias);
//			 log.e("onSelDeviceChange mAlias = " + deviceInfoEx.mAlias);
//		 }else{
//			 mMenuDownBtn.setText("终端选择");
//		 }
//		 	
//		 mHandler.sendEmptyMessage(MAP_MESSAGE_REFRESH_POS);
//		
//	}
//
//	
//
//}
