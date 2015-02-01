package com.mobile.yunyou.activity;

//import net.sf.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.yunyou.R;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.bike.manager.CheckUploadManager;
import com.mobile.yunyou.datastore.YunyouSharePreference;
import com.mobile.yunyou.model.GloalType;
import com.mobile.yunyou.model.PublicType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.msg.MsgDeleteActivity;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.PopWindowFactory;
import com.mobile.yunyou.util.Utils;

public class LoginActivity extends Activity implements OnClickListener, IRequestCallback{

	private static final CommonLog log = LogFactory.createLog();
	
	private View mRootView;
	private TextView mBtnRegister;
	private TextView mBtnForgetPwd;
	private Button mBtnLogin;
	
	private EditText mEtAccoun;
	private EditText mEtPassword;
	private CheckBox mCBPwd;

	private String mAccount = "";
	private String mPasswrod = "";
	
	private YunyouApplication mApplication;
	private NetworkCenterEx mNetworkCenter;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
       
        
        
		 mApplication = YunyouApplication.getInstance();
		 if (mApplication.getLoginState() == true)
		 {
			 goMainActivity();
			 finish();
		 }
		 
		 
        
    	long timeMillis1 = System.currentTimeMillis();		
		
        initView();
        
        initData();
        
    	long timeMillis2 = System.currentTimeMillis();
		log.e("loginactivity onCreate cost = " + (timeMillis2 - timeMillis1) + "!!!");
    }
    

	private void initView()
    {
    	mRootView = findViewById(R.id.login_root_view);
    	mBtnRegister = (TextView) findViewById(R.id.tvRegister);
    	mBtnRegister.setOnClickListener(this);
    	mBtnRegister.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
    	
    	mBtnForgetPwd = (TextView) findViewById(R.id.tvForgetPassword);
    	mBtnForgetPwd.setOnClickListener(this);	
    	mBtnForgetPwd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
    	
    	mBtnLogin = (Button) findViewById(R.id.login);
    	mBtnLogin.setOnClickListener(this);
    	
    	mCBPwd = (CheckBox) findViewById(R.id.auto_save_password);
    	mCBPwd.setChecked(YunyouSharePreference.getRememberFlag(this));
    	
    	mEtAccoun = (EditText) findViewById(R.id.accounts);
    	mEtPassword = (EditText) findViewById(R.id.password);

    	mEtAccoun.setText(YunyouSharePreference.getUserName(this));
    	mEtPassword.setText(YunyouSharePreference.getPwd(this));
    }
    
    private void initData()
    {
    	mApplication = (YunyouApplication) getApplication();
    	mNetworkCenter = mApplication.getNetworkCenter();
    	mNetworkCenter.initNetwork();
    	
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId())
		{
		case R.id.tvRegister:
			goRegisterActivity();
			break;
		case R.id.login:
			commitLogin();
			break;
		case R.id.tvForgetPassword:
			findBackPassword();
			break;
			default:
				break;
		}
	}
    
	private void goRegisterActivity()
    {
    	Intent intent = new Intent();
    	intent.setClass(this, RegisterActivity.class);
    	startActivity(intent);
    }
	   

	private void goMainActivity()
    {

    	Intent intent = new Intent();
    	//intent.setClass(this, MainActivity.class);
    	intent.setClass(this, MainSlideActivity.class);
    	startActivity(intent);
    	finish();
    	
    	CheckUploadManager.getInstance().reCheckUpload();
    }
    
	private void goMsgDelActivity()
    {

    	Intent intent = new Intent();
    	intent.setClass(this, MsgDeleteActivity.class);
    	startActivity(intent);
    	finish();
    }
	
	private void goTestActivity()
    {
//    	showRequestDialog(false);
//    	Intent intent = new Intent();
//    	intent.setClass(this, TestActivity.class);
//    	startActivity(intent);
//    	finish();
    }
    
	private PopupWindow mPopupWindow = null;
//	private Dialog mDialog = null;
	private void showRequestDialog(boolean bShow)
	{
//		if (mDialog != null)
//		{
//			mDialog.dismiss();
//			mDialog = null;
//		}
//		
//		if (bShow)
//		{
//			mDialog = DialogFactory.creatRequestDialog(this, "正在验证账号...");
//			mDialog.show();
//		}
		
		if (mPopupWindow != null)
		{
			mPopupWindow.dismiss();
			mPopupWindow = null;
		}
		
		if (bShow)
		{
			mPopupWindow = PopWindowFactory.creatLoadingPopWindow(this, R.string.login_sending);
			mPopupWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);
		}
	
	}


	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		mApplication.exit();
	}
	
	private void findBackPassword()
	{
//    	Intent intent = new Intent();
//    	intent.setClass(this, ForgetPwdActivity.class);
//    	startActivity(intent);
	}
	

	private void commitLogin()
	{
		mAccount = mEtAccoun.getText().toString();
		
		if (mAccount.equals(""))
		{
			showToast(getResources().getString(R.string.toask_input_account));
			return ;
		}

		mPasswrod = mEtPassword.getText().toString();
		if (mPasswrod.equals(""))
		{
			showToast(getResources().getString(R.string.toask_input_password));
			return ;
		}
		
		
		PublicType.UserLogin userLogin = new PublicType.UserLogin();
		userLogin.mUserName = mAccount;
		userLogin.mPassword = mPasswrod;
		
		mNetworkCenter.StartRequestToServer(PublicType.USER_LOGIN_MASID, userLogin, this);
		
		showRequestDialog(true);
		

	}
	
	private void showToast(String text)
	{
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onComplete(int requestAction,  ResponseDataPacket dataPacket) {
		// TODO Auto-generated method stub
		
	
		String jsString = "null";
		if (dataPacket != null)
		{
			jsString = dataPacket.toString();
		}
		log.e("requestAction = " + Utils.toHexString(requestAction) + "\nResponseDataPacket = \n" +jsString);
		
		switch(requestAction)
		{
			case PublicType.USER_LOGIN_MASID:
				OnLoginResult(dataPacket);
				break;
			default:
				break;
		}
		
		showRequestDialog(false);
		
		return true;
	}
	
	private void OnLoginResult(ResponseDataPacket dataPacket)
	{
		if (dataPacket == null)
		{
			Utils.showToast(this, R.string.toask_login_fail);
			return ;
		}
		
		if (dataPacket.rsp == 0)
		{
			String msg = dataPacket.msg;
			if (msg.length() > 0)
			{
				Utils.showToast(this, msg);
			}else{
				Utils.showToast(this, R.string.toask_login_fail);
			}

			return ;
		}
		
		PublicType.UserLoginResult loginResult = new PublicType.UserLoginResult();
		
		try {
			loginResult.parseString(dataPacket.data.toString());
			
			GloalType.UserInfoEx userInfoEx = mApplication.getUserInfoEx();
			userInfoEx.mAccountName = mAccount;
			userInfoEx.mPassword = mPasswrod;	
			userInfoEx.mType = loginResult.mType;
			userInfoEx.mSid = dataPacket.sid;
			mApplication.setDeviceList(loginResult.deviceList);		
			mNetworkCenter.setSid(dataPacket.sid);
			
			if (loginResult.deviceList.size() != 0)
			{
				mApplication.setCurDevice(loginResult.deviceList.get(0));
			}else{
				mApplication.setBindFlag(false);
			}
		
			if (mCBPwd.isChecked())
			{
				YunyouSharePreference.putUserName(this, mAccount);
				YunyouSharePreference.putPwd(this, mPasswrod);
				YunyouSharePreference.putRememberFlag(this, true);
			}else{
				YunyouSharePreference.putUserName(this, "");
				YunyouSharePreference.putPwd(this, "");
				YunyouSharePreference.putRememberFlag(this, false);
			}
			
			mApplication.setLoginState(true);
			mApplication.downLoadHeadProfile();
			mApplication.requestUserInfo();
			
			goMainActivity();
		//	goMsgDelActivity();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Utils.showToast(this, R.string.analyze_data_fail);
		}

	
	}
}
