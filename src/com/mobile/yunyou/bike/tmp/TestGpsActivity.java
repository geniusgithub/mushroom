package com.mobile.yunyou.bike.tmp;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.mobile.yunyou.R;
import com.mobile.yunyou.bike.manager.GaoDeGPSManager;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.YunTimeUtils;

public class TestGpsActivity extends Activity implements OnClickListener, LocationListener{

	private static final CommonLog log = LogFactory.createLog();
	
	public static final int  MSG_UPDATE = 0x0001;
	private Context mContext;
	
	private TextView mTextViewCurPos;
	private TextView mTextViewPosText;  
	private TextView mTextViewProvider; 
	
	private Button mBtnGet;
	private Button mBtnClear;
	private Button mBtnSwitch;
	
	private StringBuffer stringBuffer = new StringBuffer();
	
	
	private TestGpsManager gpsManager;
	private GaoDeGPSManager gaoDeGPSManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tmp_layout);
		
		setupViews();
		initData();
	}
	
	private void setupViews(){
		mTextViewCurPos = (TextView) findViewById(R.id.tv_curPos);
		mTextViewPosText = (TextView) findViewById(R.id.tv_gpstext);
		mTextViewProvider = (TextView) findViewById(R.id.tv_provider);
		
		mBtnGet = (Button) findViewById(R.id.btn_getpos);
		mBtnClear = (Button) findViewById(R.id.btn_clearPos);
		mBtnSwitch = (Button) findViewById(R.id.btn_switchProvider);
		mBtnGet.setOnClickListener(this);
		mBtnClear.setOnClickListener(this);
		mBtnSwitch.setOnClickListener(this);
	}
	
	private void initData(){

		gpsManager = new TestGpsManager(this);
		gaoDeGPSManager = new GaoDeGPSManager(this);
		
		updateProviderView();
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		
	//	gpsManager.unRegisterListen();
		gaoDeGPSManager.unRegisterListen();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
	//	gpsManager.registerListen(this);
		gaoDeGPSManager.registerListen(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_clearPos:
			clearPos();
			break;
		case R.id.btn_getpos:
			getPos();
			break;
		case R.id.btn_switchProvider:
			switchProvider();
			break;
		}
	}

	
	private void clearPos(){
		stringBuffer = new StringBuffer();
		mTextViewPosText.setText("");
	}
	
	private void getPos(){
		Location location = gpsManager.getLastLocation();
		
		updateCurLocationView(location);
	}
	
	private void switchProvider(){
		boolean flag = gpsManager.isGPSProvider();
		gpsManager.switchGPSProvide(!flag);
		updateProviderView();
		gpsManager.unRegisterListen();
		gpsManager.registerListen(this);
	}
	
	private void updateProviderView(){
		boolean flag = gpsManager.isGPSProvider();
		if (flag){
			mTextViewProvider.setText("GPS");
		}else{
			mTextViewProvider.setText("NETWORK");
		}
	}

	private void updateCurLocationView(Location location){
		if (location != null){
			double lat = location.getLatitude();
			double lon = location.getLongitude();
			
			long time = System.currentTimeMillis();
			String curTime = YunTimeUtils.getFormatTime2(time);
			String text1 = curTime + "   (" + String.valueOf(lat) + ", " + String.valueOf(lon) + ")";
			
			long gpsTime = location.getTime();
			String text2 =  YunTimeUtils.getFormatTime2(gpsTime);

			text1 += "-->" + text2 + "\n";


			mTextViewCurPos.setText("curPos:" + text1);
		}

	}

	private void updateLocationView(Location location){
		if (location != null){
			double lat = location.getLatitude();
			double lon = location.getLongitude();
			
			long time = System.currentTimeMillis();
			String curTime = YunTimeUtils.getFormatTime2(time);
			String text1 = curTime + "   (" + String.valueOf(lat) + ", " + String.valueOf(lon) + ")";
			
			long gpsTime = location.getTime();
			String text2 =  YunTimeUtils.getFormatTime2(gpsTime);

			text1 += "-->" + text2  + "\n";

			stringBuffer = new StringBuffer(text1 + stringBuffer.toString());
			stringBuffer.insert(0, text1);
			//log.e("stringBuffer = " + stringBuffer.toString());
			mTextViewPosText.setText(stringBuffer.toString());
		}

	}

	@Override
	public void onLocationChanged(Location location) {
		log.e("onLocationChanged location = (" + location.getLatitude() + ", " + location.getLongitude() + ")");
		updateLocationView(location);
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
}
