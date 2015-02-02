package com.mobile.yunyou.bike;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.mobile.yunyou.R;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.model.GloalType;
import com.mobile.yunyou.model.GloalType.DeviceInfoEx;
import com.mobile.yunyou.model.PublicType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.set.ChangeDevicePwdActivity;
import com.mobile.yunyou.set.ChangePwdActivity;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.PopWindowFactory;
import com.mobile.yunyou.util.Utils;

public class MoGuActivity extends Activity implements OnClickListener, IRequestCallback{

private static final int MSG_GET_WARNING = 0x0001;
	
	private static final CommonLog log = LogFactory.createLog();
	

	private Button mBtnBack;
	private Button mBtnBind;
	private Button mBtnUnBind;
	private Button mBtnChangePwd;
	
	private EditText mETAccount;
	private EditText mETPassword;
	
	private View mRootView;
	
	private YunyouApplication mApplication;
	private NetworkCenterEx mNetworkCenterEx;
	
	private boolean isBind = false;
	private Handler mHandler;
	
	private DeviceInfoEx mDeviceInfoEx = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mogu_layout);
		setupViews();
		initData();
		
		
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		
	}


	
	
	@Override
	public void onBackPressed() {

		if (!mApplication.isBindDevice()){
			Utils.showToast(this, R.string.toask_bind_device);
			return ;
		}
		
		super.onBackPressed();
	}


	private void setupViews(){
		mRootView = findViewById(R.id.rootView);
		
		mBtnBind = (Button) findViewById(R.id.btn_bind);
		mBtnBind.setOnClickListener(this);
		mBtnUnBind = (Button) findViewById(R.id.btn_unbind);
		mBtnUnBind.setOnClickListener(this);
		mBtnChangePwd = (Button) findViewById(R.id.btn_changepwd);
		mBtnChangePwd.setOnClickListener(this);
		//mBtnChangePwd.setVisibility(View.GONE);
		
		mETAccount = (EditText) findViewById(R.id.et_account);
		mETPassword = (EditText) findViewById(R.id.et_password);
    	mBtnBack = (Button) findViewById(R.id.btn_back);
    	mBtnBack.setOnClickListener(this);

	}
	
	private void initData(){
		  mNetworkCenterEx = NetworkCenterEx.getInstance();
		  
		  mApplication = YunyouApplication.getInstance();
		  isBind = mApplication.isBindDevice();
		
//	  	mNetworkCenterEx.setDid("A000000012000087");
//		mNetworkCenterEx.setSid("A12null000007135");
//		mNetworkCenterEx.initNetwork();
		  
		  mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				
			}
			  
		  };
		  
		  if (isBind){
			  mBtnBind.setVisibility(View.GONE);
			  mBtnUnBind.setVisibility(View.VISIBLE);
			  mDeviceInfoEx =   mApplication.getCurDevice();
			  mETAccount.setText(mDeviceInfoEx.mAlias);
			  mETAccount.setEnabled(false);
			  mETPassword.setEnabled(false);
		  }else{
			  mBtnBind.setVisibility(View.VISIBLE);
			  mBtnUnBind.setVisibility(View.GONE); 
			  mBtnChangePwd.setVisibility(View.GONE); 
		  }
	}
	
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_bind:
			bind();
			break;
		case R.id.btn_unbind:
			unbind();
			break;
		case R.id.btn_changepwd:
			changepwd();
			break;
		default:
			break;
		}
	}
	
	private void bind(){
		String name = mETAccount.getText().toString();
		String pwd = mETPassword.getText().toString();
		
		if (name.length() == 0)
		{
			Utils.showToast(this, R.string.toask_name_not_null);
			return ;
		}
		
		if (pwd.length() == 0)
		{
			Utils.showToast(this, R.string.toask_pwd_not_null);
			return ;
		}
		
		PublicType.UserBind userBind = new PublicType.UserBind();
		userBind.mUserName = name;
		userBind.mPassword = pwd;
			
		mNetworkCenterEx.StartRequestToServer(PublicType.USER_BIND_MASID, userBind, this);
	
		showRequestDialog(true);
	}
	
	private void unbind(){
		mNetworkCenterEx.StartRequestToServer(PublicType.USER_UNBIND_MASID, mDeviceInfoEx.mDid, null, this);			
		showRequestDialog(true);
	}
	
	private void changepwd(){
		Intent intent = new Intent();
    	intent.setClass(this, ChangeDevicePwdActivity.class);
    	startActivity(intent);
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
		case PublicType.USER_BIND_MASID:
			OnBindResult(dataPacket);
			break;
		case PublicType.USER_UNBIND_MASID:
			OnUnBindResult(dataPacket);
		}
		
		showRequestDialog(false);
		
		return true;
	}
	
	private void OnBindResult(ResponseDataPacket dataPacket)
	{
		if (dataPacket == null)
		{
			Utils.showToast(this, R.string.toask_tip_bind_fail);
			return ;
		}
		
		if (dataPacket.rsp == 0)
		{
			String msg = dataPacket.msg;
			if (msg.length() > 0)
			{
				Utils.showToast(this, msg);
			}else{
				Utils.showToast(this, R.string.toask_tip_bind_fail);
			}
			return ;
		}
		
		GloalType.DeviceInfoEx infoEx = new GloalType.DeviceInfoEx();
		try {
			infoEx.parseString(dataPacket.data.toString());
			
			List<DeviceInfoEx> list = mApplication.getDeviceList();
			list.add(infoEx);
			mApplication.setCurDevice(infoEx);
			
		
			Utils.showToast(this, R.string.toask_tip_bind_success);
			mApplication.setBindFlag(true);
			finish();
		} catch (Exception e) {
			e.printStackTrace();
			Utils.showToast(this, R.string.analyze_data_fail);
		}
	}
	
	private void OnUnBindResult(ResponseDataPacket dataPacket)
	{
		if (dataPacket == null)
		{
			Utils.showToast(this, R.string.toask_tip_unbind_fail);
			return ;
		}
		
		if (dataPacket.rsp == 0)
		{
			Utils.showToast(this, R.string.toask_tip_unbind_fail);
			return ;
		}
		
	
		mApplication.setCurDevice(null);
		mApplication.clearDeviceList();
		Utils.showToast(this, R.string.toask_tip_unbind_success);
		finish();
	}
}
