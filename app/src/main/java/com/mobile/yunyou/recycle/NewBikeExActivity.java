package com.mobile.yunyou.recycle;
//package com.mobile.yunyou.bike.tmp;
//
//import java.util.ArrayList;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Context;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.CompoundButton.OnCheckedChangeListener;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.amap.api.location.AMapLocation;
//import com.amap.api.maps.AMap;
//import com.amap.api.maps.CameraUpdate;
//import com.amap.api.maps.CameraUpdateFactory;
//import com.amap.api.maps.LocationSource;
//import com.amap.api.maps.MapView;
//import com.amap.api.maps.UiSettings;
//import com.amap.api.maps.model.BitmapDescriptor;
//import com.amap.api.maps.model.BitmapDescriptorFactory;
//import com.amap.api.maps.model.CameraPosition;
//import com.amap.api.maps.model.LatLng;
//import com.amap.api.maps.model.Marker;
//import com.amap.api.maps.model.MarkerOptions;
//import com.amap.api.maps.model.MyLocationStyle;
//import com.amap.api.maps.model.PolylineOptions;
//import com.mobile.yunyou.R;
//import com.mobile.yunyou.bike.MapUtils;
//import com.mobile.yunyou.bike.manager.RunRecordUploadPoxy;
//import com.mobile.yunyou.bike.manager.SelfLocationManager;
//import com.mobile.yunyou.datastore.RunRecordDBManager;
//import com.mobile.yunyou.map.data.LocationEx;
//import com.mobile.yunyou.map.util.StringUtil;
//import com.mobile.yunyou.model.BikeType;
//import com.mobile.yunyou.util.CommonLog;
//import com.mobile.yunyou.util.DialogFactory;
//import com.mobile.yunyou.util.LogFactory;
//import com.mobile.yunyou.util.Utils;
//import com.mobile.yunyou.util.YunTimeUtils;
//
//public class NewBikeExActivity extends Activity implements OnClickListener, 
//														OnCheckedChangeListener,
//														NewBikeCenter.IStatusCallBack{
//	private static final CommonLog log = LogFactory.createLog();
//	public static final int  MSG_UPDATE_VIEW = 0x0001;
//	
//	
//	private Context mContext;
//	
//	private AMap aMap;
//	
//	private MapView mMapView;								
//	private UiSettings mUiSettings;		
//
//
////	public static final double DOUBLE_STUDEN_LON = 114.1017;
////	public static final double DOUBLE_STUDEN_LAT = 22.6644;
////	public static final LatLng ORIGIN_POSITION = new LatLng(DOUBLE_STUDEN_LAT, DOUBLE_STUDEN_LON);// 北京市中关村经纬度
////	
//	private Handler mHandler;
//	  
//	  
//	private TextView mTextViewTime;
//	private TextView mTextViewCurSpeed;  
//	private TextView mTextViewDistance;
//	private TextView mTextViewHSpeed; 
//	private TextView mTextViewAverageSpeed;
//	private TextView mTextViewCal; 
//	private TextView mTextViewHeight; 
//	
//	
//	private Button mBtnStart;
//	private Button mBtnUpload;
//	private Button mBtnStartYet;
//	private Button mBtnStop;
//	private Button mBtnStopYet;
//	private Button mBtnBack;
//	
//	private View mLockView;
//	private CheckBox mCBLock;
//	private NewBikeCenter mNewBikeCenter;
//	
//	private RunBikeMarket mRunBikeMarket;
//	private Marker mSelfMarket;	// 定位雷达小图标
//	
//	private RunRecordDBManager mRunRecordDBManager;
//	
//	private BikeType.BikeLRecordResult mCuRecordResult;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.newbikeex_layout);
//		
//		mContext = this;
//		mMapView = (MapView) findViewById(R.id.map);
//		mMapView.onCreate(savedInstanceState);
//		aMap = mMapView.getMap();
//		
//		setupViews();
//		initData();
//		initMap();
//		clearData();
//	}
//	
//	private boolean isFristResume = true;
//	/**
//	 * 方法必须重写
//	 */
//	@Override
//	protected void onResume() {
//		super.onResume();
//		mMapView.onResume();
//
//		updateCamarra(17);
//		if (isFristResume){
//			  isFristResume = false;
//			  LocationEx locationEx = SelfLocationManager.getInstance().getLastLocation();
//			  if (locationEx != null){
//				  mRunBikeMarket.addLocation(locationEx);
//				  updateNewBikeMapView();
//				  updateCamarra(locationEx);
//			  }
//			  
//			 
//		}
//	}
//
//	/**
//	 * 方法必须重写
//	 */
//	@Override
//	protected void onPause() {
//		super.onPause();
//		mMapView.onPause();
//		
//	}
//
//	/**
//	 * 方法必须重写
//	 */
//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//		mMapView.onSaveInstanceState(outState);
//	}
//
//	/**
//	 * 方法必须重写
//	 */
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		
//		mNewBikeCenter.setCallback(null);
//		mMapView.onDestroy();
//	}
//	
//	
//
//	@Override
//	public void onBackPressed() {
//	
//		if (mCBLock.isChecked()){
//			return ;
//		}
//		
//		if (mNewBikeCenter.getRunStatus() != NewBikeCenter.IRunStatus.STOP){
//			showExitDialog(true);
//			return ;
//		}
//		
//		if (isVaildRecordExist){
//			showUploadDialog(true);
//			return ;
//		}
//		
//		super.onBackPressed();
//	}
//
//	private void setupViews(){
//		mUiSettings = aMap.getUiSettings();
//		mUiSettings.setZoomControlsEnabled(false);
//
//		mTextViewTime = (TextView) findViewById(R.id.tv_timeinterval);
//		mTextViewCurSpeed = (TextView) findViewById(R.id.tv_curspeed);
//		mTextViewDistance = (TextView) findViewById(R.id.tv_distance);
//		mTextViewHSpeed = (TextView) findViewById(R.id.tv_hspeed);
//		mTextViewAverageSpeed = (TextView) findViewById(R.id.tv_averagespeed);
//		mTextViewCal = (TextView) findViewById(R.id.tv_cal);
//		mTextViewHeight = (TextView) findViewById(R.id.tv_height);
//		
//	
//    	mBtnBack = (Button) findViewById(R.id.btn_back);
//    	mBtnBack.setOnClickListener(this);
//		mBtnStart = (Button) findViewById(R.id.btn_start);
//		mBtnStop = (Button) findViewById(R.id.btn_stop);
//		mBtnStartYet = (Button) findViewById(R.id.btn_pause); 
//		mBtnStopYet = (Button) findViewById(R.id.btn_stopyet); 
//		mBtnUpload = (Button) findViewById(R.id.btn_upload); 
//		mBtnStartYet.setEnabled(false);
//		mBtnStopYet.setEnabled(false);
//		
//		mBtnUpload.setOnClickListener(this);
//		mBtnStart.setOnClickListener(this);
//		//mBtnPause.setOnClickListener(this);
//		mBtnStop.setOnClickListener(this);
//		mBtnStop.setEnabled(false);
//
//		
//		mLockView = findViewById(R.id.rl_lock);
//		mLockView.setOnClickListener(this);
//		mCBLock = (CheckBox) findViewById(R.id.cb_lock);
//		mCBLock.setChecked(false);
//		mCBLock.setOnCheckedChangeListener(this);
//		
//		mRunBikeMarket = new RunBikeMarket(R.drawable.point_start, R.drawable.point_end);
//		
//		showButtonType(VIEW_START);
//		
//	}
//	
//	private Dialog mDialog = null;
//	private void showExitDialog(boolean bShow)
//	{
//		if (mDialog != null)
//		{
//			mDialog.dismiss();
//			mDialog = null;
//		}
//		
//		DialogFactory.IThreeSelectComplete listener = new DialogFactory.IThreeSelectComplete() {
//			
//			@Override
//			public void onSelectComplete(int index) {
//				switch(index){
//					case 1:
//						completeRunning();
//						break;
//					case 2:
//						dropRunning();
//						break;
//					case 3:
//						continueRunning();
//						break;
//				}
//			}
//		};
//
//		if (bShow)
//		{
//			mDialog = DialogFactory.creatSelectDialog(NewBikeExActivity.this, R.string.dialog_title_exitRun, R.string.dialog_msg_exitRun,
//									R.string.newbike_btn_complete, R.string.newbike_btn_drop, R.string.newbike_btn_continue, listener);
//			mDialog.show();
//		}
//	
//	}
//	
////	private Dialog mSaveDialog = null;
////	private void showSaveDialog(boolean bShow)
////	{
////		if (mSaveDialog != null)
////		{
////			mSaveDialog.dismiss();
////			mSaveDialog = null;
////		}
////		
////		DialogFactory.ISelectComplete onListener = new DialogFactory.ISelectComplete() {
////
////			@Override
////			public void onSelectComplete(boolean flag) {
////
////				if (flag){
////					BikeType.RunRecordGroup group = mNewBikeCenter.newRunRecord();
////					log.e("mTotalDistance = " + group.mTotalDistance + ", mRunRecordList.size = " + group.mRunRecordList.size());
////					if (group.mTotalDistance == 0 || group.mRunRecordList.size() < 2){
////						
////						Utils.showToast(NewBikeExActivity.this, R.string.toask_unsave_record);
////					}else{
////						boolean ret = false;
////						try {
////							ret = mRunRecordDBManager.insert(group);
////						} catch (Exception e) {
////							e.printStackTrace();
////						}
////						if (ret){
////							Utils.showToast(NewBikeExActivity.this, R.string.toask_saveRecord_success);
////						}else{
////							Utils.showToast(NewBikeExActivity.this, R.string.toask_saveRecord_fail);
////						}
////					}	
////				}
////					
////				
////				finish();
////			}
////
////		};
////
////		if (bShow)
////		{
////			mSaveDialog = DialogFactory.creatSelectDialog(this, R.string.dialog_title_saveRun, R.string.dialog_msg_saveRun, 
////												R.string.btn_save, R.string.btn_drop, onListener);
////			mSaveDialog.show();
////		}
////	
////	}
//	
//	private Dialog mTipDialog = null;
//	private void showTipDialog(boolean bShow)
//	{
//		if (mTipDialog != null)
//		{
//			mTipDialog.dismiss();
//			mTipDialog = null;
//		}
//		
//			View.OnClickListener onListener = new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					finish();
//				}
//			};
//
//
//
//		if (bShow)
//		{
//			mTipDialog = DialogFactory.creatSingleDialog(mContext, R.string.dialog_title_invalidrecord, R.string.dialog_msg_sinvalidrecord, onListener);
//			mTipDialog.setCancelable(false);
//			mTipDialog.show();
//		}
//	
//	}
//	
//	private void showUploadDialog(boolean bShow)
//	{
//		if (mDialog != null)
//		{
//			mDialog.dismiss();
//			mDialog = null;
//		}
//		
//		DialogFactory.ISelectComplete listener = new DialogFactory.ISelectComplete() {
//
//			@Override
//			public void onSelectComplete(boolean flag) {
//				if (flag){
//					uploadRuning();
//				}else{
//					finish();
//				}
//			}
//			
//		
//		};
//		
//		if (bShow)
//		{
//			mDialog = DialogFactory.creatSelectDialog(NewBikeExActivity.this, R.string.dialog_title_uploadrecord, R.string.dialog_msg_uploadrecord,
//									R.string.newbike_btn_upload, R.string.newbike_btn_dropupload,  listener);
//			mDialog.show();
//		}
//	
//	}
//	
//	private void initData(){
//		mNewBikeCenter = new NewBikeCenter(this);
//
//		  mHandler = new Handler(){
//
//			@Override
//			public void handleMessage(Message msg) {
//
////				switch(msg.what){
////					case MSG_UPDATE_VIEW:
////
////						break;
////				}
//			}
//			  
//		  };
//		  
//		  mNewBikeCenter.setCallback(this);
//			
////		  updateStatus();
//		  mRunRecordDBManager = RunRecordDBManager.getInstance();
//
//	}
//	
//	private void initMap(){
//		ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
//		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point1));
//		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point2));
//		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point3));
//		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point4));
//		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point5));
//		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point6));
//		mSelfMarket = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).icons(giflist).period(50));
//	}
//	
//	
//	private void clearData(){
//		updateTimesion(0);
//		updateNewBikeEntiy(new NewBikeEntiy());
//	}
//
//	
//	private final static int VIEW_START = 0x0001;
//	private final static int VIEW_PAUSE = 0x0002;
//	private final static int VIEW_UPLOAD = 0x0003;
//	
//	private void showButtonType(int type){
//		
//		switch (type) {
//			case VIEW_START:
//				mBtnStart.setVisibility(View.VISIBLE);
//				mBtnStartYet.setVisibility(View.GONE);
//				mBtnUpload.setVisibility(View.GONE);
//				mBtnStop.setVisibility(View.VISIBLE);
//				mBtnStopYet.setVisibility(View.GONE);
//				break;
//			case VIEW_PAUSE:
//				mBtnStart.setVisibility(View.GONE);
//				mBtnStartYet.setVisibility(View.VISIBLE);
//				mBtnUpload.setVisibility(View.GONE);
//				mBtnStop.setVisibility(View.VISIBLE);
//				mBtnStopYet.setVisibility(View.GONE);
//				break;
//			case VIEW_UPLOAD:
//				mBtnStart.setVisibility(View.GONE);
//				mBtnStartYet.setVisibility(View.GONE);
//				mBtnUpload.setVisibility(View.VISIBLE);
//				mBtnStop.setVisibility(View.GONE);
//				mBtnStopYet.setVisibility(View.VISIBLE);
//				break;
//			default:
//				break;
//		}
//
//	}
//	
//	@Override
//	public void onCheckedChanged(CompoundButton arg0, boolean flag) {
//		if (flag){
//			Toast.makeText(this, "lock", Toast.LENGTH_SHORT).show();
//			
//			mBtnStart.setEnabled(false);
//			mBtnUpload.setEnabled(false);
//			mBtnStop.setEnabled(false);
//	
//		}else{
//			Toast.makeText(this, "unlock", Toast.LENGTH_SHORT).show();
//			
//			mBtnStart.setEnabled(true);
//			mBtnUpload.setEnabled(true);
//		//	mBtnPause.setEnabled(true);
//			int status = mNewBikeCenter.getRunStatus();
//			if (status == NewBikeCenter.IRunStatus.STOP){
//				mBtnStop.setEnabled(false);
//			}else{
//				mBtnStop.setEnabled(true);
//			}
//			
//		}
//	}
//
//	private boolean isVaildRecordExist = false;
//	private void completeRunning(){
//		log.e("completeRunning");
//		
//		mNewBikeCenter.stopRunning();
//		showButtonType(VIEW_UPLOAD);
//		mCuRecordResult = null;
//		BikeType.BikeLRecordResult group = mNewBikeCenter.newLocalBikeRecord();
//	//	BikeType.BikeRecordUpload upload = mNewBikeCenter.newBikeRecord();
//		log.e("mTotalDistance = " + group.mTotalDistance + ", mRunRecordList.size = " + group.mBikeSubRecordResultList.size());
//		if (group.mTotalDistance == 0 || group.mBikeSubRecordResultList.size() < 2){
//			
//			showTipDialog(true);
//		}else{
//			isVaildRecordExist = true;
//		}
////		else{
////			boolean ret = false;
////			try {
////				ret = mRunRecordDBManager.insert(group);
////			} catch (Exception e) {
////				e.printStackTrace();
////			}
////			if (!ret){
////				Utils.showToast(this, R.string.toask_saveRecord_fail);
////			}else{
////				mCuRecordResult = group;
////			}
////		}	
//
//	}
//	
//	private void dropRunning(){
//		log.e("dropRunning");
//		
//		finish();
//	}
//	
//	private void continueRunning(){
//		
//	}
//	
//	private void uploadRuning(){
//		Utils.showToast(this, "上传中...");
//
//		
//		BikeType.BikeLRecordResult group = mNewBikeCenter.newLocalBikeRecord();
//		boolean ret = false;
//		try {
//			ret = mRunRecordDBManager.insert(group);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		if (!ret){
//			Utils.showToast(this, R.string.toask_saveRecord_fail);
//		}else{
//			mCuRecordResult = group;
//		}
//		
//		BikeType.BikeRecordUpload object = mNewBikeCenter.newBikeRecord();
//		RunRecordUploadPoxy.getInstance().requestUpload(object);
//		RunRecordUploadPoxy.getInstance().attachCurRecord(mCuRecordResult);
//		
//		finish();
//	}
//	
//	@Override
//	public void onClick(View v) {
//		switch(v.getId()){
//			case R.id.btn_back:
//				onBackPressed();
//				break;
//			case R.id.rl_lock:
//				mCBLock.setChecked(!mCBLock.isChecked());
//				break;
//			case R.id.btn_start:
//					mRunBikeMarket.reset();
//					mNewBikeCenter.startRunning();
//					showButtonType(VIEW_PAUSE);
//					mBtnStop.setEnabled(true);
//					//  updateStatus();
//					break;
////			case R.id.btn_pause:
////				mNewBikeCenter.pauseRunning();
////				showStartButton(true);
////				mBtnStop.setEnabled(true);
////				//  updateStatus();
////				break;
//			case R.id.btn_stop:
//				completeRunning();
//				break;
//			case R.id.btn_upload:
//				uploadRuning();
//				break;
//		}
//	}
//
//	@Override
//	public void onTimeChange(int timeMillson) {
//		updateTimesion(timeMillson);
//	}
//
//	@Override
//	public void onStatusChange(NewBikeEntiy entiy) {
//		updateNewBikeEntiy(entiy);
//	}
//	
//	@Override
//	public void onLatlngUpdate(LocationEx locationEx, AMapLocation aMapLocation) {
//		mRunBikeMarket.addLocation(locationEx);
//		updateNewBikeMapView();
//		moveCamara(locationEx);
//		mSelfMarket.setPosition(new LatLng(locationEx.getOffsetLat(), locationEx.getOffsetLon()));
//	}
//	
//	private void updateCamarra(float zoomLevel){
//		log.e("updateCamarra zoomLevel = " + zoomLevel);
//	
//		aMap.moveCamera(CameraUpdateFactory.zoomTo(zoomLevel));
//	}
//	
//	private void updateCamarra(LocationEx location){
//		if (location != null){
//			LatLng latLng = new LatLng(location.getOffsetLat(), location.getOffsetLon());
//			int zoomLevel = 17;
//			log.e("updateCamarra (" + location.getOffsetLat() + ", " + location.getOffsetLon() + "),zoomLevel = " + zoomLevel);
//			if (zoomLevel != 0){
//				CameraUpdate object = CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel);
//				aMap.moveCamera(object);
//			}
//		}
//	}
//	
//	private void moveCamara(LatLng latlon){
//		if (latlon != null){
//			CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latlon, MapUtils.getZoomLevel(aMap));
//			aMap.moveCamera(update);
//		}
//	}
//	
//	private void moveCamara(LocationEx location){
//		if (location != null){
//			LatLng latLng = new LatLng(location.getOffsetLat(), location.getOffsetLon());
//			log.e("updateCamarra (" + location.getOffsetLat() + ", " + location.getOffsetLon());
//			CameraUpdate object = CameraUpdateFactory.newLatLngZoom(latLng, getZoomLevel());
//			aMap.moveCamera(object);
//
//		}
//	}
//	
//	private float getZoomLevel(){
//		CameraPosition position = aMap.getCameraPosition();
//		return position.zoom;
//	}
//	
//	private void updateTimesion(int timeMison){
//		String time = YunTimeUtils.getFormatTimeInterval(timeMison);
//		mTextViewTime.setText(time);
//	}
//	
//	private void updateNewBikeEntiy(NewBikeEntiy entiy){
//		
//		double distance = entiy.mTotalDistance / 1000.0;
//		double speed = entiy.mCurSpeed * 3.6;
//		double hspeed = entiy.mHSpeed * 3.6;
//		double averagespeed = entiy.mAverageSpeed * 3.6;
//		double cal = entiy.mCal;
//		double height = entiy.mHeight;
//		
//		mTextViewDistance.setText(StringUtil.ConvertByDoubeString(distance));
//		mTextViewCurSpeed.setText(StringUtil.ConvertByDoubeString(speed));
//		mTextViewHSpeed.setText(StringUtil.ConvertByDoubeString(hspeed));
//		mTextViewAverageSpeed.setText(StringUtil.ConvertByDoubeString(averagespeed));
//		mTextViewCal.setText(String.valueOf(cal));
//		mTextViewHeight.setText(String.valueOf(height));
//	}
//
//	private void updateNewBikeMapView(){
//		log.e("updateNewBikeMapView");
//		
//		Marker marker = mRunBikeMarket.getMarket();
//		if (marker == null){
//			if (mRunBikeMarket.getLastLocation() != null){
//				marker = aMap.addMarker(mRunBikeMarket.newStartMarkerOptions());
//				mRunBikeMarket.attchMarket(marker);
//
//			}
//			return ;
//		}
//		
//		
//		LatLng latLng = mRunBikeMarket.getFirstLatLon();
//		if (latLng != null){
//			marker.setPosition(latLng);
//		}
//		
//		PolylineOptions polylineOptions = mRunBikeMarket.getLastPolyline();
//		if (polylineOptions != null){
//			aMap.addPolyline(polylineOptions);
//		}
//		
////		aMap.clear();
////			
////		List<PolylineOptions> list = mRunBikeMarket.getPLineList();
////		int size = list.size();
////		for(int i = 0;i < size; i++){
////			aMap.addPolyline(list.get(i));
////		}
////		
////		MarkerOptions option = mRunBikeMarket.newStartMarkerOptions();
////		if (option != null){
////			aMap.addMarker(option);
////		}
//
//	}
//	
//	
////	private void updateStatus(){
////		String textString = getResources().getString(R.string.newbike_text_status_stop);
////		switch(mNewBikeCenter.getRunStatus()){
////				case NewBikeCenter.IRunStatus.STOP:
////					 textString = getResources().getString(R.string.newbike_text_status_stop);
////					break;
////				case NewBikeCenter.IRunStatus.RUNNING:
////					 textString = getResources().getString(R.string.newbike_text_status_run);
////					break;
////				case NewBikeCenter.IRunStatus.PAUSE:
////					 textString = getResources().getString(R.string.newbike_text_status_pause);
////					break;
////		}
////		
////
////	}
//	
//	
//}
