package com.mobile.yunyou.bike;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.mobile.yunyou.R;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.bike.manager.BikeStatusManager;
import com.mobile.yunyou.model.BikeType;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.YunTimeUtils;

public class NewBikeActivity extends Activity implements OnClickListener, OnCheckedChangeListener, BikeStatusManager.IBikeResultCallback{

	public static final int  MSG_UPDATE = 0x0001;
	
	
	private Context mContext;
	
	private AMap aMap;
	
	private MapView mMapView;								
	private UiSettings mUiSettings;			

	public static final double DOUBLE_STUDEN_LON = 114.1017;
	public static final double DOUBLE_STUDEN_LAT = 22.6644;
	public static final LatLng ORIGIN_POSITION = new LatLng(DOUBLE_STUDEN_LAT, DOUBLE_STUDEN_LON);// 北京市中关村经纬度
	
	private YunyouApplication mApplication;
	private NetworkCenterEx mNetworkCenterEx;
	private Handler mHandler;
	  
	private BikeStatusManager mBikeStatusManager;
	  
	private TextView mTextViewTime;
	private TextView mTextViewSpeed;  
	private TextView mTextViewDistance;
	private TextView mTextViewHSpeed; 
	private TextView mTextViewLSpeed;
	private TextView mTextViewCal; 
	
	private Button mBtnStart;
	private Button mBtnStop;
	
	private CheckBox mCBLock;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newbike_layout);
		
		mMapView = (MapView) findViewById(R.id.map);
		mMapView.onCreate(savedInstanceState);
		aMap = mMapView.getMap();
		
		setupViews();
		initData();
	}
	
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
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
		mBikeStatusManager.stopTimer();
		mBikeStatusManager.removeListener();
	}
	
	

	@Override
	public void onBackPressed() {
	
		if (mCBLock.isChecked()){
			return ;
		}
		
		super.onBackPressed();
	}

	private void setupViews(){
		mUiSettings = aMap.getUiSettings();
		mUiSettings.setZoomControlsEnabled(false);

		mTextViewTime = (TextView) findViewById(R.id.tv_timeinterval);
		mTextViewSpeed = (TextView) findViewById(R.id.tv_speed);
		mTextViewDistance = (TextView) findViewById(R.id.tv_distance);
		mTextViewHSpeed = (TextView) findViewById(R.id.tv_hspeed);
		mTextViewLSpeed = (TextView) findViewById(R.id.tv_lspeed);
		mTextViewCal = (TextView) findViewById(R.id.tv_cal);
		
		mBtnStart = (Button) findViewById(R.id.btn_start);
		mBtnStop = (Button) findViewById(R.id.btn_stop);
		mBtnStart.setOnClickListener(this);
		mBtnStop.setOnClickListener(this);

		mCBLock = (CheckBox) findViewById(R.id.cb_lock);
		mCBLock.setChecked(false);
		mCBLock.setOnCheckedChangeListener(this);
		
	}
	
	private void initData(){
		  mApplication = YunyouApplication.getInstance();
		  mNetworkCenterEx = NetworkCenterEx.getInstance();
		  
		  
//		  mNetworkCenterEx.setSid("A12null000007135");
//		  mNetworkCenterEx.setDid("A000000012000087"); 
//		  mNetworkCenterEx.setDid("A000000012000086");
//		  mNetworkCenterEx.setSid("A128966000007152");
//		  mNetworkCenterEx.initNetwork();


		  
		  mBikeStatusManager = BikeStatusManager.getInstance();
		  mBikeStatusManager.addListener(this);
		  
		  mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {

				switch(msg.what){
					case MSG_UPDATE:
						updateData(mBikeStatusManager.getBikeStatus());
						break;
				}
			}
			  
		  };
	}

	
	private void updateData(BikeType.BikeStatus value){

		String time = YunTimeUtils.getFormatTimeInterval(value.mTimeInterval);
		mTextViewTime.setText(time);
		mTextViewSpeed.setText(String.valueOf(value.mSpeed));
		mTextViewDistance.setText(String.valueOf(value.mDistance));
		mTextViewHSpeed.setText(String.valueOf(value.mSpeedMax));
		mTextViewLSpeed.setText(String.valueOf(value.mSpeedMin));
		mTextViewCal.setText(String.valueOf(value.mCal));
		
	}
	
	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean flag) {
		if (flag){
			Toast.makeText(this, "lock", Toast.LENGTH_SHORT).show();
			
			mBtnStart.setEnabled(false);
			mBtnStop.setEnabled(false);
		}else{
			Toast.makeText(this, "unlock", Toast.LENGTH_SHORT).show();
			
			mBtnStart.setEnabled(true);
			mBtnStop.setEnabled(true);
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_start:
				startBike();
				break;
			case R.id.btn_stop:
				stopBike();
				break;
		}
	}
	
	private void startBike(){
		Toast.makeText(this, "startBike", Toast.LENGTH_SHORT).show();
		mBikeStatusManager.startTimer();
	}
	
	private void stopBike(){
		Toast.makeText(this, "stopBike", Toast.LENGTH_SHORT).show();
		mBikeStatusManager.stopTimer();
	}

	@Override
	public void onBikeStatus(boolean isResult) {
		if (isResult){
			mHandler.sendEmptyMessage(MSG_UPDATE);
		}else{
			Toast.makeText(this, "get data fail!!!", Toast.LENGTH_SHORT).show();
		}
	}


}
