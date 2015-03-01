package com.mobile.yunyou.activity;



import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.mobile.yunyou.R;
import com.mobile.yunyou.model.PublicType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.PopWindowFactory;
import com.mobile.yunyou.util.Utils;

public class ForgetPwdActivity extends BaseActivity implements OnClickListener, IRequestCallback{

	private static final CommonLog log = LogFactory.createLog();
	 
	private View mRootView;
	private Button mBtnBack;
	private Button mBtnSave;
	private EditText mOldPwdEditText;
	private EditText mNewPwdEditText1;
	private EditText mNewPwdEditText2;
	
	  private NetworkCenterEx mNetworkCenterEx;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.change_pwd_layout);
		
		initView();
		initData();
	}

	
	private void initView(){
		mRootView = findViewById(R.id.rootView);
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBtnSave = (Button)findViewById(R.id.btn_save);
		mOldPwdEditText = (EditText) findViewById(R.id.et_oldpwd);
		mNewPwdEditText1 = (EditText) findViewById(R.id.et_newpwd1);
		mNewPwdEditText2 = (EditText) findViewById(R.id.et_newpwd2);
		mBtnBack.setOnClickListener(this);
		mBtnSave.setOnClickListener(this);

		
	}

	private void initData(){
		mNetworkCenterEx = NetworkCenterEx.getInstance();
	}

	@Override
	public void onClick(View view) {
		
		
		switch(view.getId()){
			case R.id.btn_back:
				finish();
				break;
			case R.id.btn_save:
				save();
				break;
		
		}
	}
	
	
	
	private void save(){
		
		String oldString  = mOldPwdEditText.getText().toString();
		String newString1 = mNewPwdEditText1.getText().toString();
		String newString2 = mNewPwdEditText2.getText().toString();
		
		
		if (oldString.length() == 0 || newString1.length() == 0 || newString2.length() == 0 )
		{
			Utils.showToast(this, R.string.toask_pwd_not_null);
			return ;
		}
		
		if (!newString1.equals(newString2)){
			Utils.showToast(this, R.string.toask_newpwd_not_equal);
			return ;
		}
		
		PublicType.UserChangePwd object = new PublicType.UserChangePwd();
		object.mOldPassword = oldString;
		object.mNewPassword = newString1;
		
		mNetworkCenterEx.StartRequestToServer(PublicType.USER_CHANGE_PASSWORD_MASID, object, this);
		showRequestDialog(true);
	}

	private PopupWindow mPopupWindow = null;
	private void showRequestDialog(boolean bShow)
	{
	
		if (mPopupWindow != null)
		{
			mPopupWindow.dismiss();
			mPopupWindow = null;
		}
		
		if (bShow)
		{
			mPopupWindow = PopWindowFactory.creatLoadingPopWindow(this, R.string.sending_request);
			mPopupWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);
		}
	
	}

	@Override
	public boolean onComplete(int requestAction, ResponseDataPacket dataPacket) {

		
		showRequestDialog(false);
		String jsString = "null";
		if (dataPacket != null)
		{
			jsString = dataPacket.toString();
		}
		
		log.e("requestAction = " + Utils.toHexString(requestAction) + "\nResponseDataPacket = \n" +jsString);
		
		switch(requestAction)
		{
		case PublicType.USER_CHANGE_PASSWORD_MASID:
			onChangePwd(dataPacket);
			break;
		}
		
		return true;
	}
	
	private void onChangePwd(ResponseDataPacket dataPacket){
		if (dataPacket == null){
			Utils.showToast(this, R.string.set_data_fail);		
			return ;
		}
		
		if (dataPacket.rsp == 0)
		{
			String msg = dataPacket.msg;
			if (msg.length() > 0){
				Utils.showToast(this, msg);
			}else{
				Utils.showToast(this, R.string.set_data_fail);
			}
			
			return ;
		}
		
		Utils.showToast(this, R.string.set_data_success);
		finish();
	}
}
