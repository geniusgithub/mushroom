package com.mobile.yunyou.bike;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.mobile.yunyou.R;
import com.mobile.yunyou.model.BikeType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.set.SetWarnActivity;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.PopWindowFactory;
import com.mobile.yunyou.util.Utils;

public class WarningActivity extends Activity implements OnClickListener, IRequestCallback{

	private static final int MSG_GET_WARNING = 0x0001;
	
	private static final CommonLog log = LogFactory.createLog();
	
	private Button mBtnSave;
	private Button mBtnBack;
	
	private EditText mEditTextPhone;
	private CheckBox mCBRing;
	private CheckBox mCBVebe;
	private CheckBox mCBMessage;
	
	private View mRootView;
	
	private NetworkCenterEx mNetworkCenterEx;
	
	private boolean isFirstResume = true;
	private Handler mHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.warn_layout);
		setupViews();
		initData();
		
		
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if (isFirstResume){
			isFirstResume = false;
			mHandler.sendEmptyMessageDelayed(MSG_GET_WARNING, 200);
			
		}
	}


	private void setupViews(){
		mRootView = findViewById(R.id.rootView);
		
		mCBRing = (CheckBox) findViewById(R.id.cb_ring);
		mCBVebe = (CheckBox) findViewById(R.id.cb_virbe);
		mCBMessage = (CheckBox) findViewById(R.id.cb_message);
		mEditTextPhone = (EditText) findViewById(R.id.et_phone);
		
    	mBtnBack = (Button) findViewById(R.id.btn_back);
    	mBtnBack.setOnClickListener(this);
		mBtnSave = (Button) findViewById(R.id.btn_save);
		mBtnSave.setOnClickListener(this);
	}
	
	private void initData(){
		  mNetworkCenterEx = NetworkCenterEx.getInstance();
		  
		  
//	  	mNetworkCenterEx.setDid("A000000012000087");
//		mNetworkCenterEx.setSid("A12null000007135");
//		mNetworkCenterEx.initNetwork();
		  
		  mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				switch(msg.what){
				case MSG_GET_WARNING:
					getWarn();
					break;
				}
			}
			  
		  };
	}
	
	
	private BikeType.BikeAlertWay mAlertWay;
	private void updateWarnData(BikeType.BikeAlertWay object){
		mAlertWay = object;
		mCBRing.setChecked(mAlertWay.mRing);
		mCBVebe.setChecked(mAlertWay.mVibe);
		mCBMessage.setChecked(mAlertWay.mMessage);
		mEditTextPhone.setText(mAlertWay.mPhone);
	}

	
	private void getWarn(){
		log.e("getWarn");
		mNetworkCenterEx.StartRequestToServer(BikeType.BIKE_GETALERTWAY, null, this);
		showRequestDialog(true);
	}
	
	private void setWarn(){
		log.e("setWarn");
		mAlertWay.mRing = mCBRing.isChecked();
		mAlertWay.mVibe = mCBVebe.isChecked();
		mAlertWay.mPush = true;
		mAlertWay.mMessage = mCBMessage.isChecked();
		mAlertWay.mPhone = mEditTextPhone.getText().toString();
		mNetworkCenterEx.StartRequestToServer(BikeType.BIKE_SETALERTWAY, mAlertWay, this);
		showRequestDialog(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_save:
			save();
			break;
			case R.id.btn_cancel:
			cancel();
			break;
		default:
			break;
		}
	}
	
	private void save(){
		setWarn();
	}
	
	private void cancel(){
		finish();
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


	@Override
	public boolean onComplete(int requestAction, ResponseDataPacket dataPacket) {
		String jsString = "null";
		if (dataPacket != null)
		{
			jsString = dataPacket.toString();
		}
	
		log.e("requestAction = " + Utils.toHexString(requestAction) + "\nResponseDataPacket = \n" +jsString);
		
		
		switch(requestAction){
		case BikeType.BIKE_GETALERTWAY:
			onGetBikeAlertWay(dataPacket);
			break;
		case BikeType.BIKE_SETALERTWAY:
			onSetBikeAlertWay(dataPacket);
			break;
		}
		
		showRequestDialog(false);
		
		return true;
	}
	
	private void onGetBikeAlertWay(ResponseDataPacket dataPacket){
		if (dataPacket == null || dataPacket.rsp == 0)
		{
			Utils.showToast(this, R.string.request_data_fail);
			finish();
			return ;
		}
		
		BikeType.BikeAlertWay object = new BikeType.BikeAlertWay();
		try {
			object.parseString(dataPacket.data.toString());
			updateWarnData(object);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Utils.showToast(this, R.string.analyze_data_fail);
			finish();
			return ;
		}
		
		
	}
	
	private void onSetBikeAlertWay(ResponseDataPacket dataPacket){
		if (dataPacket == null || dataPacket.rsp == 0)
		{
			Utils.showToast(this, R.string.set_data_fail);
			return ;
		}
		
		Utils.showToast(this, R.string.set_data_success);
		
		
	}
}
