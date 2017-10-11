package com.mobile.yunyou.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.mobile.yunyou.R;
import com.mobile.yunyou.model.PublicType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.DialogFactory;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.Utils;
import com.mobile.yunyou.util.VertifyUtil;

public class RegisterActivity extends BaseActivity implements OnClickListener, IRequestCallback{

	private static final CommonLog log = LogFactory.createLog();
	
	private Button mBtnOK;
	private Button mBtnCancel;
	
	private EditText mEditTextUserName;
	private EditText mEditTextPassword;
	private EditText mEditTextEmail;
	private EditText mEditTextPhone;
	
	private NetworkCenterEx mNetworkCenter;
	
	
	private String mUsername = "";
	private String mPasswrod = "";
	private String mEmail = "";
	private String mPhone = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.register);
		
		initView();
		
		initData();
	}
	
	
	public void initView()
	{
		
		mBtnOK = (Button) findViewById(R.id.register_btn_ok);
		mBtnOK.setOnClickListener(this);
		
		mBtnCancel = (Button) findViewById(R.id.register_btn_cancel);
		mBtnCancel.setOnClickListener(this);
		
		mEditTextUserName = (EditText) findViewById(R.id.name);
		mEditTextPassword = (EditText) findViewById(R.id.password);
		mEditTextEmail = (EditText) findViewById(R.id.email);
		mEditTextPhone = (EditText) findViewById(R.id.phone);
		
	
	}
	
	public void initData()
	{
		mNetworkCenter = NetworkCenterEx.getInstance();

	}
	
	

	private Dialog mDialog = null;
	private void showRequestDialog(boolean bShow)
	{
		if (mDialog != null)
		{
			mDialog.dismiss();
			mDialog = null;
		}
		if (bShow)
		{
			mDialog = DialogFactory.creatRequestDialog(this, "正在注册中...");
			mDialog.show();	
		}
	
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.btn_back:
			finish();
			break;
		case R.id.register_btn_ok:
			commitRegister();
			break;
		case R.id.register_btn_cancel:
			cancelCommit();
			break;
			default:
				break;
		}
	}

	public void commitRegister()
	{
		mEmail = mEditTextEmail.getText().toString();
		if (mEmail.equals(""))
		{
			Utils.showToast(this, R.string.toask_input_email);
			return ;
		}
		
		if (!VertifyUtil.isEMail(mEmail))
		{
			Utils.showToast(this, R.string.toask_error_email);
			return ;
		}
		
		
		mPasswrod = mEditTextPassword.getText().toString();
		if (mPasswrod.equals(""))
		{
			Utils.showToast(this, R.string.toask_input_password);
			return ;
		}
		
		mUsername = mEditTextUserName.getText().toString();		
		if (mUsername.equals(""))
		{
			Utils.showToast(this, R.string.toask_input_username);
			return ;
		}	
		
		mPhone = mEditTextPhone.getText().toString();
		if (mPhone.equals(""))
		{
			Utils.showToast(this, R.string.toask_input_phone);
			return ;
		}
		
		PublicType.UserRegister userRegister = new PublicType.UserRegister();
		userRegister.mUserName = mUsername;
		userRegister.mPassword = mPasswrod;
		userRegister.mEmail = mEmail;
		userRegister.mPhone = mPhone;
		userRegister.mType = "A";

		mNetworkCenter.StartRequestToServer(PublicType.USER_REGISTER_MASID, userRegister, this);
		
		showRequestDialog(true);

	}
	
	public void cancelCommit()
	{
		finish();
	}

	@Override
	public boolean onComplete(int requestAction,  ResponseDataPacket dataPacket) {
		// TODO Auto-generated method stub
		showRequestDialog(false);
		
		String jsString = "null";
		if (dataPacket != null)
		{
			jsString = dataPacket.toString();
		}
		log.e("requestAction = " + Utils.toHexString(requestAction) + "\nResponseDataPacket = \n" +jsString);
		
		switch(requestAction)
		{
			case PublicType.USER_REGISTER_MASID:
				OnRegisterResult(dataPacket);
				break;
			default:
				break;
		}
		
		return true;
	}
	
	public void OnRegisterResult(ResponseDataPacket dataPacket)
	{
		if (dataPacket == null)
		{
			Utils.showToast(this, R.string.toask_register_fail);
			return ;
		}
		
		if (dataPacket.rsp == 0)
		{		
			String msg = dataPacket.msg;
			if (msg.length() > 0)
			{
				Utils.showToast(this, msg);
			}else{
				Utils.showToast(this, R.string.toask_register_fail);
			}
		
			return ;
		}
		
	
		Utils.showToast(this, R.string.toask_register_success);
		
		
		finish();
		
	}
}
