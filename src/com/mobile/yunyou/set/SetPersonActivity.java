package com.mobile.yunyou.set;

import java.util.ArrayList;
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
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mobile.yunyou.R;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.custom.SingleChoicePopWindow;
import com.mobile.yunyou.device.DeviceIntentConstant;
import com.mobile.yunyou.model.GloalType;
import com.mobile.yunyou.model.PublicType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.set.SetPersonCommentActivity.IViewMode;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.PopWindowFactory;
import com.mobile.yunyou.util.Utils;

public class SetPersonActivity extends Activity implements OnClickListener, IRequestCallback{

	private static final CommonLog log = LogFactory.createLog();
	

	private static final int MSG_GET_INFO = 0x0001;
	
	private View mRootView;
	private View mEmailView;
	private View mNameView;
	private View mPwdView;
	//private View mPhoenView;
	private View mSexView;
	private View mBirthdayView;
	
	private Button mBtnSave;
	private Button mBtnBack;
	
	private TextView mTVAccount;
	private TextView mTVEmail;
	private TextView mTVTruename;
	private TextView mTVPhone;
	private TextView mTVSex;
	private TextView mTVBirthday;
	
	private String mEmailString;
	private String mNameString;
	private String mPhoneString;
	private String mSexString;
	private String mBirthdayString;
	
	
	private SingleChoicePopWindow mSingleChoicePopWindow;
	private String[] mSexArrays = null;
	private List<String> mSexList = new ArrayList<String>(); 
	
	
	private YunyouApplication mApplication;
	private NetworkCenterEx mNetworkCenter;
	private GloalType.UserInfoEx mUserInfoEx;
	
	private boolean isFirstResume = true;
	private Handler mHandler;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_person_layout);

       initView();
       
       initData();
      
    }
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if (isFirstResume){
			isFirstResume = false;
			mHandler.sendEmptyMessageDelayed(MSG_GET_INFO, 200);
			
		}
	}

	private void initView()
	{
		mBtnSave = (Button) findViewById(R.id.btn_save);
		mBtnSave.setOnClickListener(this);
		
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
		
		mRootView = findViewById(R.id.rootView);
		mEmailView = findViewById(R.id.ll_emailset);
		mNameView = findViewById(R.id.ll_nameset);
		mPwdView = findViewById(R.id.ll_changepwd);
		//mPhoenView = findViewById(R.id.ll_phoneset);
		mSexView = findViewById(R.id.ll_sexset);
		mBirthdayView = findViewById(R.id.ll_birthdayset);
				
		mEmailView.setOnClickListener(this);
		mNameView.setOnClickListener(this);
		mPwdView.setOnClickListener(this);
		//mPhoenView.setOnClickListener(this);
		mSexView.setOnClickListener(this);
		mBirthdayView.setOnClickListener(this);
		
		mTVAccount = (TextView) findViewById(R.id.tv_account);
		mTVEmail = (TextView) findViewById(R.id.tv_email);
		mTVTruename = (TextView) findViewById(R.id.tv_truename);
		mTVPhone = (TextView) findViewById(R.id.tv_phone);
		mTVSex = (TextView) findViewById(R.id.tv_sex);
		mTVBirthday = (TextView) findViewById(R.id.tv_birthday);

		mSingleChoicePopWindow = new SingleChoicePopWindow(this, mRootView, new ArrayList<String>());
	}

	private void initData()
	{
	   mApplication = YunyouApplication.getInstance();
	   mNetworkCenter = NetworkCenterEx.getInstance();
	      
	   mUserInfoEx = new GloalType.UserInfoEx(mApplication.getUserInfoEx());
	     
	   setAccont(mUserInfoEx.mAccountName);
	   setEmail(mUserInfoEx.mEmail);
	   setTurename(mUserInfoEx.mTrueName);
	//   setPhone(mUserInfoEx.mPhone);
	   setSex(mUserInfoEx.mSex);
	   setBirthday(mUserInfoEx.mBirthday);
	   
//	   mSexArrays = getResources().getStringArray(R.array.user_sex_name);
//		for(int i = 0; i < mSexArrays.length; i++)
//		{
//			mSexList.add(mSexArrays[i]);
//		}
		
		  mHandler = new Handler(){

				@Override
				public void handleMessage(Message msg) {
					switch(msg.what){
					case MSG_GET_INFO:
						getInfo();
						break;
					}
				}
				  
			  };
	}
	
	private void getInfo(){
		mNetworkCenter.StartRequestToServer(PublicType.USER_GET_INFO_MASID, null, this);
		showRequestDialog(true);
	}
	
	private void setAccont(String account)
	{
		mTVAccount.setText(account);
	}
	
	private void setEmail(String email)
	{
		mEmailString = email;
		mTVEmail.setText(email);
	}
	
	private void setTurename(String name)
	{
		mNameString = name;
		mTVTruename.setText(name);
	}
	
//	private void setPhone(String phone)
//	{
//		mPhoneString = phone;
//		mTVPhone.setText(phone);
//	}
	
	private void setSex(String sex)
	{
		mSexString = sex;
		String str = sex.toLowerCase();
		
		if (str.equals("m"))
		{
			mTVSex.setText("男");
		}else if (str.equals("f"))
		{
			mTVSex.setText("女");
		}else {
			mTVSex.setText("未知");
		}
	}
	
	private void setBirthday(String birthday)
	{
		mBirthdayString = birthday;
		mTVBirthday.setText(birthday);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
			case R.id.ll_emailset:
			{			
				Intent intent = new Intent();
				intent.setClass(this, SetPersonCommentActivity.class);
				intent.putExtra(SetPersonCommentActivity.VIEW_KEY, SetPersonCommentActivity.IViewMode.IVM_EMAIL);
				
				Bundle bundle = new Bundle();
				bundle.putString(SetPersonIntentConstant.KEY_OBJECT_DATA, mEmailString);
			
				intent.putExtra(DeviceIntentConstant.KEY_DATA_BUNDLE, bundle);
				startActivityForResult(intent, REQUEST_CODE_SET_COMMENT);
				
			}
				break;
			case R.id.ll_nameset:
			{			
				Intent intent = new Intent();
				intent.setClass(this, SetPersonCommentActivity.class);
				intent.putExtra(SetPersonCommentActivity.VIEW_KEY, SetPersonCommentActivity.IViewMode.IVM_NAME);
				
				Bundle bundle = new Bundle();
				bundle.putString(SetPersonIntentConstant.KEY_OBJECT_DATA, mNameString);
			
				intent.putExtra(DeviceIntentConstant.KEY_DATA_BUNDLE, bundle);
				startActivityForResult(intent, REQUEST_CODE_SET_COMMENT);
				
			}
				break;
			case R.id.ll_changepwd:
			{			
				Intent intent = new Intent();
				intent.setClass(this, ChangePwdActivity.class);
				startActivity(intent);
				
			}
				break;
				
//			case R.id.ll_phoneset:
//			{			
//				Intent intent = new Intent();
//				intent.setClass(this, SetPersonCommentActivity.class);
//				intent.putExtra(SetPersonCommentActivity.VIEW_KEY, SetPersonCommentActivity.IViewMode.IVM_PHONE);
//				
//				Bundle bundle = new Bundle();
//				bundle.putString(SetPersonIntentConstant.KEY_OBJECT_DATA, mPhoneString);
//			
//				intent.putExtra(DeviceIntentConstant.KEY_DATA_BUNDLE, bundle);
//				startActivityForResult(intent, REQUEST_CODE_SET_COMMENT);
//				
//			}
//				break;
			case R.id.ll_sexset:
				showSexWindow();
				break;
			case R.id.ll_birthdayset:
			{
				Intent intent = new Intent();
				intent.setClass(this, SetPersonCommentActivity.class);
				intent.putExtra(SetPersonCommentActivity.VIEW_KEY, SetPersonCommentActivity.IViewMode.IVM_BIRTHDAY);
				
				Bundle bundle = new Bundle();
				bundle.putString(SetPersonIntentConstant.KEY_OBJECT_DATA, mBirthdayString);
			
				intent.putExtra(DeviceIntentConstant.KEY_DATA_BUNDLE, bundle);
				startActivityForResult(intent, REQUEST_CODE_SET_COMMENT);
			}
				break;
			case R.id.btn_save:
				save();
				break;
			case R.id.btn_back:
				finish();
				break;
		}
	}
	
	private void save()
	{
		
		mUserInfoEx.mEmail = mEmailString;
		mUserInfoEx.mPhone = mPhoneString;
		mUserInfoEx.mTrueName = mNameString;
		mUserInfoEx.mBirthday = mBirthdayString;
		mUserInfoEx.mSex = mSexString;
		
		PublicType.UserChangeInfo info = new PublicType.UserChangeInfo();
		info.mTrueName = mNameString;
		info.mPhone = mPhoneString;
		info.mBirthday = mBirthdayString;
		info.mAddr = mUserInfoEx.mAddr;
		info.mEmail = mEmailString;
		info.mSex = mSexString;
		
		mNetworkCenter.StartRequestToServer(PublicType.USER_CHANGE_INFO_MASID, info, this);
		
		showRequestDialog(true);
	}
	
	public void showSexWindow()
	{
		String str = mSexString.toLowerCase();
		int index = 0;
		if (str.equals("m"))
		{
			index = 0;
		}else{
			index = 1;
		}
		
		mSingleChoicePopWindow.refreshData(mSexList, index);
		mSingleChoicePopWindow.setTitle(getResources().getString(R.string.popwindow_title_sex));
		
		mSingleChoicePopWindow.setOnOKButtonListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int selItem = mSingleChoicePopWindow.getSelectItem();
			
				setSex(selItem == 0 ? "m" : "f");
			}
		});
		
		mSingleChoicePopWindow.show(true);
			
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
	
	
	
	public final static int REQUEST_CODE_SET_COMMENT = 0x0001;
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		log.e("requestCode = " + requestCode + ", resultCode = " + resultCode);
		switch(requestCode)
		{
		case REQUEST_CODE_SET_COMMENT:
			onSetResult(resultCode, data);
			break;
		}
	}
	
	private void onSetResult(int resultCode, Intent data)
	{
		if (resultCode == RESULT_OK)
		{
			int viewState = data.getIntExtra(SetPersonCommentActivity.VIEW_KEY, SetPersonCommentActivity.IViewMode.IVM_NONE);
			Bundle bundle = data.getBundleExtra(SetPersonIntentConstant.KEY_DATA_BUNDLE);
			if (bundle ==  null)
			{
				return ;
			}
			
			switch(viewState)
			{
			case IViewMode.IVM_EMAIL:
				String emailString = bundle.getString(SetPersonIntentConstant.KEY_OBJECT_DATA);
				setEmail(emailString);
				break;
//			case IViewMode.IVM_PHONE:
//				String phoneString = bundle.getString(SetPersonIntentConstant.KEY_OBJECT_DATA);
//				setPhone(phoneString);
//				break;
			case IViewMode.IVM_NAME:
				String nameString = bundle.getString(SetPersonIntentConstant.KEY_OBJECT_DATA);
				setTurename(nameString);
				break;
			case IViewMode.IVM_BIRTHDAY:
				String birthdayString = bundle.getString(SetPersonIntentConstant.KEY_OBJECT_DATA);
				setBirthday(birthdayString);
				break;
			}

		}
	}

	@Override
	public boolean onComplete(int requestAction, ResponseDataPacket dataPacket) {
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
		case PublicType.USER_GET_INFO_MASID:
			onGetUserInfoResult(dataPacket);
			break;
		case PublicType.USER_CHANGE_INFO_MASID:
			OnChangeResult(dataPacket);
			break;
		}
		
		return true;
		
	}
	
	private void onGetUserInfoResult(ResponseDataPacket dataPacket)
	{
		if (dataPacket == null || dataPacket.rsp == 0)
		{
			Utils.showToast(this, R.string.request_data_fail);
			
			return ;
		}
		
		PublicType.UserChangeInfo info = new PublicType.UserChangeInfo();
		try {
			info.parseString(dataPacket.data.toString());
			
			GloalType.UserInfoEx userInfoEx = mApplication.getUserInfoEx();
			userInfoEx.mTrueName = info.mTrueName;
			userInfoEx.mPhone = info.mPhone;
			userInfoEx.mBirthday = info.mBirthday;
			userInfoEx.mEmail = info.mEmail;
			userInfoEx.mAddr = info.mAddr;
			userInfoEx.mSex = info.mSex;
			
			mUserInfoEx = new GloalType.UserInfoEx(userInfoEx);
			
			   setAccont(mUserInfoEx.mAccountName);
			   setEmail(mUserInfoEx.mEmail);
			   setTurename(mUserInfoEx.mTrueName);
			//   setPhone(mUserInfoEx.mPhone);
			   setSex(mUserInfoEx.mSex);
			   setBirthday(mUserInfoEx.mBirthday);
			   
			   mSexArrays = getResources().getStringArray(R.array.user_sex_name);
				for(int i = 0; i < mSexArrays.length; i++)
				{
					mSexList.add(mSexArrays[i]);
				}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Utils.showToast(this, R.string.analyze_data_fail);
			finish();
		}
		
		
	}
	
	private void OnChangeResult(ResponseDataPacket dataPacket)
	{
		if (dataPacket == null || dataPacket.rsp == 0)
		{
			Utils.showToast(this, R.string.set_data_fail);
			
			return ;
		}
		
		Utils.showToast(this, R.string.set_data_success);
		mApplication.setUserInfoEx(mUserInfoEx);
		
		finish();
	}
	
	
	
	
}
