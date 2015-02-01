package com.mobile.yunyou.set;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.mobile.yunyou.BrocastFactory;
import com.mobile.yunyou.R;
import com.mobile.yunyou.ServiceIPConfig;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.bike.tmp.DataFactory;
import com.mobile.yunyou.custom.CustomTimeExPopWindow;
import com.mobile.yunyou.custom.SingleChoicePopWindow;
import com.mobile.yunyou.fragment.NavigationFragment;
import com.mobile.yunyou.model.BaseType.Birthday;
import com.mobile.yunyou.model.GloalType;
import com.mobile.yunyou.model.PublicType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.network.api.HeadFileConfigure;
import com.mobile.yunyou.set.SetPersonCommentActivity.IViewMode;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.DialogFactory;
import com.mobile.yunyou.util.FileManager;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.PopWindowFactory;
import com.mobile.yunyou.util.UploadExUtil;
import com.mobile.yunyou.util.UploadExUtil.OnUploadProcessListener;
import com.mobile.yunyou.util.Utils;
import com.mobile.yunyou.util.VertifyUtil;
import com.mobile.yunyou.widget.CustomImageView;

@SuppressLint("NewApi")
public class SetPersonActivity extends Activity implements OnClickListener, 
															IRequestCallback,
															OnUploadProcessListener,
															OnCheckedChangeListener{


	
	private static final CommonLog log = LogFactory.createLog();
	

	private static final int MSG_GET_INFO = 0x0001;
	
	private View mRootView;
//	private View mEmailView;
//	private View mNameView;
	//private View mPwdView;
	//private View mPhoenView;
	private View mBirthdayView;
	
	private Button mBtnSave;
	private Button mBtnBack;
	
	private TextView mTVAccount;
	private EditText mETEmail;
	private EditText mETTruename;
	private TextView mTVPhone;	
	private TextView mTVBirthday;
	private RadioGroup mRGSex;
	
	private String mEmailString;
	private String mNameString;
	//private String mPhoneString;
	private String mSexString;
	private String mBirthdayString;
	private CustomImageView mHeadImageView;
	
	private SingleChoicePopWindow mSingleChoicePopWindow;
	private String[] mSexArrays = null;
	private List<String> mSexList = new ArrayList<String>(); 
	
	
	private YunyouApplication mApplication;
	private NetworkCenterEx mNetworkCenter;
	private GloalType.UserInfoEx mUserInfoEx;
	
	private boolean isFirstResume = true;
	private Handler mHandler;
	
	private CustomTimeExPopWindow mCustomTimePopWindow;
	
	private PublicType.UserChangeInfo mUsChangeInfo = new PublicType.UserChangeInfo();
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_person_layout);

       initView();
       
       initData();
      
   		updateHead();
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
		  progressDialog = new ProgressDialog(this);   
		mBtnSave = (Button) findViewById(R.id.btn_save);
		mBtnSave.setOnClickListener(this);
		
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
		
		mHeadImageView = (CustomImageView) findViewById(R.id.iv_head);
		mHeadImageView.setOnClickListener(this);
		
		mRootView = findViewById(R.id.rootView);
		mRGSex = (RadioGroup) findViewById(R.id.rg_sex);
		//mPhoenView = findViewById(R.id.ll_phoneset);

		mBirthdayView = findViewById(R.id.ll_birthdayset);
		mBirthdayView.setClickable(false);
		

		//mPwdView.setOnClickListener(this);
		//mPhoenView.setOnClickListener(this);

		mBirthdayView.setOnClickListener(this);
		
		mTVAccount = (TextView) findViewById(R.id.tv_account);
		mETEmail = (EditText) findViewById(R.id.et_email);

		mETTruename = (EditText) findViewById(R.id.et_truename);
		

		mTVPhone = (TextView) findViewById(R.id.tv_phone);

		mTVBirthday = (TextView) findViewById(R.id.tv_birthday);

		mSingleChoicePopWindow = new SingleChoicePopWindow(this, mRootView, new ArrayList<String>());
		
		mCustomTimePopWindow = new CustomTimeExPopWindow(this, mRootView);
		mCustomTimePopWindow.setOnOKButtonListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBirthdayTime();
			}
		});

		mRGSex.setOnCheckedChangeListener(this);
		((RadioButton) mRGSex.getChildAt(0)).toggle();
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
		mETEmail.setText(email);
	}
	
	private void setTurename(String name)
	{
		mNameString = name;
		mETTruename.setText(name);
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
		
		if (str.equalsIgnoreCase("m"))
		{
			((RadioButton) mRGSex.getChildAt(0)).toggle();
		}else {
			((RadioButton) mRGSex.getChildAt(1)).toggle();
		}
	}
	
	private void setBirthday(String birthday)
	{
		mBirthdayString = birthday;
		mTVBirthday.setText(birthday);
	}
	
	private void onBirthdayTime()
	{
		int year = mCustomTimePopWindow.getYear();
		int month = mCustomTimePopWindow.getMonth();
		int day = mCustomTimePopWindow.getDay();
		
		Birthday mBirthday = new Birthday();
		mBirthday.year = year;
		mBirthday.month = month;
		mBirthday.day = day;
		
		
		if (VertifyUtil.isVaildBirthday(mBirthday) == false)
		{
			Utils.showToast(this, R.string.toask_error_birthday);
			return ;
		}
		
	
		setBirthday(mBirthday.toString());
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
//			case R.id.ll_emailset:
//			{			
//				Intent intent = new Intent();
//				intent.setClass(this, SetPersonCommentActivity.class);
//				intent.putExtra(SetPersonCommentActivity.VIEW_KEY, SetPersonCommentActivity.IViewMode.IVM_EMAIL);
//				
//				Bundle bundle = new Bundle();
//				bundle.putString(SetPersonIntentConstant.KEY_OBJECT_DATA, mEmailString);o
//			
//				intent.putExtra(DeviceIntentConstant.KEY_DATA_BUNDLE, bundle);
//				startActivityForResult(intent, REQUEST_CODE_SET_COMMENT);
//				
//			}
//				break;
//			case R.id.ll_nameset:
//			{			
//				Intent intent = new Intent();
//				intent.setClass(this, SetPersonCommentActivity.class);
//				intent.putExtra(SetPersonCommentActivity.VIEW_KEY, SetPersonCommentActivity.IViewMode.IVM_NAME);
//				
//				Bundle bundle = new Bundle();
//				bundle.putString(SetPersonIntentConstant.KEY_OBJECT_DATA, mNameString);
//			
//				intent.putExtra(DeviceIntentConstant.KEY_DATA_BUNDLE, bundle);
//				startActivityForResult(intent, REQUEST_CODE_SET_COMMENT);
//				
//			}
//				break;
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
//			case R.id.ll_sexset:
//				showSexWindow();
//				break;
			case R.id.ll_birthdayset:
			{
				showBirthTimeWindow();
//				Intent intent = new Intent();
//				intent.setClass(this, SetPersonCommentActivity.class);
//				intent.putExtra(SetPersonCommentActivity.VIEW_KEY, SetPersonCommentActivity.IViewMode.IVM_BIRTHDAY);
//				
//				Bundle bundle = new Bundle();
//				bundle.putString(SetPersonIntentConstant.KEY_OBJECT_DATA, mBirthdayString);
//			
//				intent.putExtra(DeviceIntentConstant.KEY_DATA_BUNDLE, bundle);
//				startActivityForResult(intent, REQUEST_CODE_SET_COMMENT);
			}
				break;
			case R.id.btn_save:
				save();
				break;
			case R.id.btn_back:
				finish();
				break;
			case R.id.iv_head:
				selectPhoto();
				break;
		}
	}
	
	
	private boolean checkEmailSet()
	{
		mEmailString = mETEmail.getText().toString();
		
		if (mEmailString.length() == 0)
		{
			Utils.showToast(this, R.string.toask_email_not_null);
			return false;
		}
		
		if (VertifyUtil.isEMail(mEmailString) == false)
		{
			Utils.showToast(this, R.string.toask_error_email);
			return false;
		}

		return true;
	}
	
	private boolean checkNameSet()
	{
		mNameString = mETTruename.getText().toString();	
		
		
		if (mNameString.length() == 0)
		{
			Utils.showToast(this, R.string.toask_name_not_null);
			return false;
		}
		
		return true;
	}
	
	private void save()
	{
		boolean ret = checkEmailSet();
		if (!ret){
			return ;
		}
		
		ret = checkNameSet();
		if (!ret){
			return ;
		}
		
		mUserInfoEx.mEmail = mEmailString;
		//mUserInfoEx.mPhone = mPhoneString;
		mUserInfoEx.mTrueName = mNameString;
		mUserInfoEx.mBirthday = mBirthdayString;
		mUserInfoEx.mSex = mSexString;
		
		PublicType.UserChangeInfo info = mUsChangeInfo;
		info.mTrueName = mNameString;
		//info.mPhone = mPhoneString;
		info.mBirthday = mBirthdayString;
		info.mAddr = mUserInfoEx.mAddr;
		info.mEmail = mEmailString;
		info.mSex = mSexString;
		
		mNetworkCenter.StartRequestToServer(PublicType.USER_CHANGE_INFO_MASID, info, this);
		
		showRequestDialog(true);
	}
	
	private void selectPhoto()
	{
		showPhotoDialog(true);
	}
	
	private void takePhone(){
		Intent intent = new Intent(
				MediaStore.ACTION_IMAGE_CAPTURE);
		//下面这句指定调用相机拍照后的照片存储的路径
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
				.fromFile(new File(Environment
						.getExternalStorageDirectory(),
						"yunyou.jpg")));
		startActivityForResult(intent, TAKE_PHOTO);
	}
	
	private void selectFromLocal(){
		Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);
		openAlbumIntent.setType("image/*");
		startActivityForResult(openAlbumIntent, CHOOSE_PHOTO);
	}
	
//	public void showSexWindow()
//	{
//		String str = mSexString.toLowerCase();
//		int index = 0;
//		if (str.equals("m"))
//		{
//			index = 0;
//		}else{
//			index = 1;
//		}
//		
//		mSingleChoicePopWindow.refreshData(mSexList, index);
//		mSingleChoicePopWindow.setTitle(getResources().getString(R.string.popwindow_title_sex));
//		
//		mSingleChoicePopWindow.setOnOKButtonListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				int selItem = mSingleChoicePopWindow.getSelectItem();
//			
//				setSex(selItem == 0 ? "m" : "f");
//			}
//		});
//		
//		mSingleChoicePopWindow.show(true);
//			
//	}

	private Dialog mDialog = null;
	private void showPhotoDialog(boolean bShow)
	{
		if (mDialog != null)
		{
			mDialog.dismiss();
			mDialog = null;
		}
		
		DialogFactory.ISelectComplete listener = new DialogFactory.ISelectComplete() {

			@Override
			public void onSelectComplete(boolean flag) {
				if (flag){
					takePhone();
				}else{
					selectFromLocal();
				}
			}
			
		};

		if (bShow)
		{
			mDialog = DialogFactory.creatSelectDialog(SetPersonActivity.this, R.string.dialog_title_getphone, R.string.dialog_msg_getphone,
									R.string.btn_takephone, R.string.btn_localphone,  listener);
			mDialog.show();
		}
	
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
	
	
	private void showBirthTimeWindow()
	{
		Birthday birthday = new Birthday();
		try {
			birthday.parseString(mBirthdayString);
			mCustomTimePopWindow.setYear(birthday.year);
			mCustomTimePopWindow.setMonth(birthday.month);
			mCustomTimePopWindow.setDay(birthday.day);
			mCustomTimePopWindow.show(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	
	
	public final static int REQUEST_CODE_SET_COMMENT = 0x0001;
	private static final int CHOOSE_PHOTO = 0x0002;
	private static final int TAKE_PHOTO = 0x0003;
	private static final int PICK_PHOTO = 0x0004;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		log.e("requestCode = " + requestCode + ", resultCode = " + resultCode);
		switch(requestCode)
		{
		case REQUEST_CODE_SET_COMMENT:
			onSetResult(resultCode, data);
			break;
		case TAKE_PHOTO:
			onTakeResult(resultCode, data);
			break;
		case CHOOSE_PHOTO:
			onChooseResult(resultCode, data);
			break;
		case PICK_PHOTO:
			log.e("PICK_PHOTO data = " + data);
			if(data != null){
				setPicToView(data);
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
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

	private void onTakeResult(int resultCode, Intent data){
		if (resultCode == RESULT_OK)
		{
			File temp = new File(Environment.getExternalStorageDirectory()
					+ "/yunyou.jpg");
			startPhotoZoom(Uri.fromFile(temp));
		}else{
		//	Utils.showToast(this, "获取图片失败...");
		}
	}
	
	private void onChooseResult(int resultCode, Intent data){
		if (resultCode == RESULT_OK)
		{
			startPhotoZoom(data.getData());
		}else{
		//	Utils.showToast(this, "获取图片失败...");
		}
	}
	
	private void onPickResult(int resultCode, Intent data){
		if (resultCode == RESULT_OK)
		{
			startPhotoZoom(data.getData());
		}else{
			Utils.showToast(this, "裁剪图片失败...");
		}
	}
	
	private final static int CROP_WIDTH = 320;
	private final static int CROP_HEIGHT = 320;
	/**
	 * 裁剪图片方法实现
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		log.e("startPhotoZoom uri = " + uri);
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		//下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", CROP_WIDTH);
		intent.putExtra("outputY", CROP_HEIGHT);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PICK_PHOTO);
	}
	
	
	private String uploadFilePath = "";
	private Bitmap curBitmap = null;
	/**
	 * 保存裁剪之后的图片数据
	 * @param picdata
	 */
	@SuppressLint("NewApi")
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
//			int bytes =  photo.getByteCount();
//			log.e("bytes = " + bytes / 1024.0 + "KB");
			curBitmap = photo;
//			mHeadImageView.setImageBitmap(photo);

			uploadFilePath = saveTmpBitmap(photo, mApplication.getUserInfoEx().mSid + ".jpg");
			log.e("saveMyBitmap filePath = " + uploadFilePath);
			if (uploadFilePath == null){
				Utils.showToast(this, "保存图片失败!!!");
				return ;
			}
			
			log.e("saveMyBitmap filePath = " + uploadFilePath);
			toUploadFile(uploadFilePath);
			
		}
	}
	
	public String saveTmpBitmap(Bitmap mBitmap,String bitName)  {
		String filePath = Environment.getExternalStorageDirectory() + "/" + bitName;
		File temp = new File(filePath);
        FileOutputStream fOut = null;
        try {
                fOut = new FileOutputStream(temp);
        } catch (FileNotFoundException e) {
                e.printStackTrace();
        }
        if (fOut == null){
        	return null;
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
                fOut.flush();
                fOut.close();
                return filePath;
        } catch (IOException e) {
                e.printStackTrace();
        }
   
        return null;
        
	}
	
	private	ProgressDialog progressDialog;
	private void toUploadFile(String filePath)
	{

		progressDialog.setMessage("正在上传头像...");
		progressDialog.show();
		String fileKey = "file";
		
		
		String jsonDATA = "";
		try {
			jsonDATA = DataFactory.buildUploadJsString("deviceset_avatar", mApplication.getUserInfoEx().mSid, "0");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		UploadExUtil uploadUtil = UploadExUtil.getInstance();
		uploadUtil.setOnUploadProcessListener(this);  //设置监听器监听上传状态
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("json", jsonDATA);

		uploadUtil.uploadFile( filePath,fileKey,ServiceIPConfig.addr,params);
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
			mUsChangeInfo = info;
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
				
				BrocastFactory.sendUserInfoUpdate(this);

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
		
		BrocastFactory.sendUserInfoUpdate(this);
		
		finish();
	}
	
	
	 private boolean loadHead = false;
		public void updateHead(){

			GloalType.UserInfoEx userInfoEx = mApplication.getUserInfoEx();
			int type = userInfoEx.mType;
			log.e("SetPersonActivity updateHead type = " + type);
			switch(type){
			case 0:
				if (!loadHead){
					String uri = HeadFileConfigure.getAccountUri(mApplication.getUserInfoEx().mSid);
					String filePath = FileManager.getSavePath(uri);
					Bitmap bitmap = BitmapFactory.decodeFile(filePath);
					if (bitmap != null){
						mHeadImageView.setImageBitmap(bitmap);
						loadHead = true;
					}else{
						log.e("can't find the bitmap from filePath:" + filePath);
					}
				}
				break;
			case 1:
				if (!loadHead){
//					String uri = HeadFileConfigure.getRequestUri(mApplication.getCurDid());
//					String filePath = FileManager.getSavePath(uri);
//					Bitmap bitmap = BitmapFactory.decodeFile(filePath);
//					if (bitmap != null){
//						mHeadImageView.setImageBitmap(bitmap);
//						loadHead = true;
//					}else{
//						log.e("can't find the bitmap from filePath:" + filePath);
//					}
				}	
				break;
			}
		
			
		}
		
		
		/**
		 * 去上传文件
		 */
		protected static final int TO_UPLOAD_FILE = 1;  
		/**
		 * 上传文件响应
		 */
		protected static final int UPLOAD_FILE_DONE = 2;  //
		/**
		 * 选择文件
		 */
		public static final int TO_SELECT_PHOTO = 3;
		/**
		 * 上传初始化
		 */
		private static final int UPLOAD_INIT_PROCESS = 4;
		/**
		 * 上传中
		 */
		private static final int UPLOAD_IN_PROCESS = 5;
		@Override
		public void onUploadDone(int responseCode, String message) {
			progressDialog.dismiss();
			Message msg = Message.obtain();
			msg.what = UPLOAD_FILE_DONE;
			msg.arg1 = responseCode;
			msg.obj = message;
			handler.sendMessage(msg);
		}


		@Override
		public void onUploadProcess(int uploadSize) {
			Message msg = Message.obtain();
			msg.what = UPLOAD_IN_PROCESS;
			msg.arg1 = uploadSize;
			handler.sendMessage(msg);
		}


		@Override
		public void initUpload(int fileSize) {
			Message msg = Message.obtain();
			msg.what = UPLOAD_INIT_PROCESS;
			msg.arg1 = fileSize;
			handler.sendMessage(msg );
		}	
		
		private Handler handler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
						switch (msg.what) {
							case UPLOAD_INIT_PROCESS:				
								break;
								
							case UPLOAD_IN_PROCESS:				
								break;
								
							case UPLOAD_FILE_DONE:
								String result = "响应码："+msg.arg1+"\n响应信息："+msg.obj+"\n耗时："+UploadExUtil.getRequestTime()+"秒";
								log.e("result = " + result);
								if (mApplication.mIsDebug){
									Utils.showToast(SetPersonActivity.this, result);
								}
								
								if (msg.arg1 == 200){
									Utils.showToast(SetPersonActivity.this, "上传成功...");
									
									String uri = HeadFileConfigure.getAccountUri(mApplication.getUserInfoEx().mSid);
									String filePath = FileManager.getSavePath(uri);
									
									File temp = new File(filePath);
							        FileOutputStream fOut = null;
							        try {
							                fOut = new FileOutputStream(temp);
							        } catch (FileNotFoundException e) {
							                e.printStackTrace();
							        }
							        if (fOut == null || curBitmap == null){
							        	Utils.showToast(SetPersonActivity.this, "设置本地头像失败...");
							        	return ;
							        }
							        
						
							        
							        curBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
							        try {
							                fOut.flush();
							                fOut.close();
							        } catch (IOException e) {
							                e.printStackTrace();
							            	Utils.showToast(SetPersonActivity.this, "设置本地头像失败...");
							            	return ;
							        }		
							        log.e("filepath = " + filePath + "\nwrite success!!!curBitmap = " + curBitmap);
		
							        loadHead = false;
							        updateHead();
		
			
									BrocastFactory.sendHeadUpdate(SetPersonActivity.this);
								}else{
									Utils.showToast(SetPersonActivity.this, "上传失败...");
								}
	
								break;
							default:
								break;
						}
				}
		};

		@Override
		public void onCheckedChanged(RadioGroup rb, int pos) {
			int id = rb.getCheckedRadioButtonId();
			if (id == R.id.rb_mail){
				mSexString = "M";
			}else{
				mSexString = "F";
			}
		}
}
