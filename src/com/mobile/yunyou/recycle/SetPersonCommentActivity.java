package com.mobile.yunyou.recycle;
//package com.mobile.yunyou.set;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.mobile.yunyou.R;
//import com.mobile.yunyou.device.DeviceIntentConstant;
//import com.mobile.yunyou.model.BaseType.Birthday;
//import com.mobile.yunyou.util.CommonLog;
//import com.mobile.yunyou.util.LogFactory;
//import com.mobile.yunyou.util.Utils;
//import com.mobile.yunyou.util.VertifyUtil;
//
//public class SetPersonCommentActivity extends Activity implements OnClickListener{
//
//private static final CommonLog log = LogFactory.createLog();
//	
//	public final static String VIEW_KEY = "view_key";
//	public static interface IViewMode
//	{
//		int IVM_NONE = -1;
//		int IVM_EMAIL = 0;					
//		int IVM_PHONE = 1;				   
//		int IVM_NAME = 2;				 
//		int IVM_BIRTHDAY = 3;			
//	
//	}
//	
//	private int viewState = IViewMode.IVM_NONE;
//	
//	private TextView mTextViewTitle;
//	private Button mBtnSave;
//	private Button mBtnBack;
//
//	
//	public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.set_person_comment_layout);
//
//        Intent intent = getIntent();
//		Bundle bundle = intent.getBundleExtra(DeviceIntentConstant.KEY_DATA_BUNDLE);
//		if (bundle == null)
//		{
//			log.e("bundle == null");
//			finish();
//			return ;
//		}
//		viewState  = intent.getIntExtra(VIEW_KEY, IViewMode.IVM_NONE);
//		switch(viewState)
//		{
//		case IViewMode.IVM_EMAIL:
//			initEmailView(bundle);
//			mEmailString = bundle.getString(SetPersonIntentConstant.KEY_OBJECT_DATA);
//			mEmailEditText.setText(mEmailString);
//			break;
//		case IViewMode.IVM_PHONE:
//			initPhoneslView(bundle);
//			mPhoneString = bundle.getString(SetPersonIntentConstant.KEY_OBJECT_DATA);
//			mPhoneEditText.setText(mPhoneString);
//			break;
//		case IViewMode.IVM_NAME:
//			initTureNamelView(bundle);
//			mNameString = bundle.getString(SetPersonIntentConstant.KEY_OBJECT_DATA);
//			mNameEditText.setText(mNameString);
//			break;
//		case IViewMode.IVM_BIRTHDAY:
//			initBirthdaylView(bundle);
//			String biString = bundle.getString(SetPersonIntentConstant.KEY_OBJECT_DATA);
//
//				try {
//					mBirthday.parseString(biString);
//					mYearEditText.setText("" + mBirthday.year);
//					mMonthEditText.setText("" + mBirthday.month);
//					mDayEditText.setText("" + mBirthday.day);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//	
//			break;
//			default:
//				finish();
//				break;
//		}
//	
//		
//    }
//	
//	
//	private void initView()
//	{
//		mTextViewTitle = (TextView) findViewById(R.id.tv_title);
//		mBtnSave = (Button) findViewById(R.id.btn_save);
//		mBtnBack = (Button) findViewById(R.id.btn_back);
//		
//		mBtnSave.setOnClickListener(this);
//		mBtnBack.setOnClickListener(this);
//		
//		mEmailView = findViewById(R.id.ll_emailview);
//		mNameView = findViewById(R.id.ll_nameview);
//		mPhoenView = findViewById(R.id.ll_phoneview);
//		mBirthdayView = findViewById(R.id.ll_birthdayview);
//		
//		mEmailEditText = (EditText) findViewById(R.id.et_email);
//		mNameEditText = (EditText) findViewById(R.id.et_name);
//		mPhoneEditText = (EditText) findViewById(R.id.et_phone);
//	
//		mYearEditText = (EditText) findViewById(R.id.et_year);
//		mMonthEditText = (EditText) findViewById(R.id.et_month);
//		mDayEditText = (EditText) findViewById(R.id.et_day);
//	}
//	
//	private void setTitles(int id)
//	{
//		String str = getResources().getString(id);
//		mTextViewTitle.setText(str);
//	}
//	
//	
//	private View mEmailView;
//	private EditText mEmailEditText;
//	private String mEmailString;
//	private void initEmailView(Bundle bundle)
//	{
//		initView();
//		setTitles(R.string.setting_text_email);
//		mEmailView.setVisibility(View.VISIBLE);
//	}
//	
//	private View mPhoenView;
//	private EditText mPhoneEditText;
//	private String mPhoneString;
//	private void initPhoneslView(Bundle bundle)
//	{
//		initView();
//		setTitles(R.string.setting_text_phone);
//		mPhoenView.setVisibility(View.VISIBLE);
//	}
//	
//	
//	private View mNameView;
//	private EditText mNameEditText;
//	private String mNameString;
//	private void initTureNamelView(Bundle bundle)
//	{
//		initView();
//		setTitles(R.string.setting_text_name);
//		mNameView.setVisibility(View.VISIBLE);
//	}
//	
//	
//
//	private View mBirthdayView;
//	private EditText mYearEditText;
//	private EditText mMonthEditText;
//	private EditText mDayEditText;
//	private Birthday mBirthday = new Birthday();
//	private void initBirthdaylView(Bundle bundle)
//	{
//		initView();
//		setTitles(R.string.setting_text_birthday);
//		mBirthdayView.setVisibility(View.VISIBLE);
//	}
//	
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		switch(v.getId())
//		{
//		case R.id.btn_save:
//			save();
//			break;
//		case R.id.btn_back:
//			finish();
//			break;
//		}
//	}
//	
//	
//	private void save()
//	{
//	
//		switch(viewState)
//		{
//		case IViewMode.IVM_EMAIL:
//			commitEmailSet();
//			break;
//		case IViewMode.IVM_PHONE:
//			commitPhoneSet();
//			break;
//		case IViewMode.IVM_NAME:
//			commitNameSet();
//			break;
//		case IViewMode.IVM_BIRTHDAY:
//			commitBirthdaySet();
//			break;
//			default:
//				break;
//		}
//
//	}
//	
//	private void commitEmailSet()
//	{
//		mEmailString = mEmailEditText.getText().toString();
//		
//		if (mEmailString.length() == 0)
//		{
//			Utils.showToast(this, R.string.toask_email_not_null);
//			return ;
//		}
//		
//		if (VertifyUtil.isEMail(mEmailString) == false)
//		{
//			Utils.showToast(this, R.string.toask_error_email);
//			return ;
//		}
//		
//		Intent intent = new Intent();
//		intent.setClass(this, SetPersonActivity.class);
//		Bundle bundle = new Bundle();
//		
//		
//		intent.putExtra(VIEW_KEY, IViewMode.IVM_EMAIL);
//		bundle.putString(SetPersonIntentConstant.KEY_OBJECT_DATA, mEmailString);
//		
//		intent.putExtra(DeviceIntentConstant.KEY_DATA_BUNDLE, bundle);
//		
//		setResult(RESULT_OK, intent);
//		
//		finish();
//	}
//	
//	private void commitPhoneSet()
//	{
//		mPhoneString = mPhoneEditText.getText().toString();	
//	
//		if (mPhoneString.length() == 0)
//		{
//			Utils.showToast(this, R.string.toask_phone_not_null);
//			return ;
//		}
//		
//		if (VertifyUtil.isMobileNumber(mPhoneString) == false)
//		{
//			Utils.showToast(this, R.string.toask_error_phone);
//			return ;
//		}
//		
//		
//		Intent intent = new Intent();
//		intent.setClass(this, SetPersonActivity.class);
//		Bundle bundle = new Bundle();
//		
//		
//		intent.putExtra(VIEW_KEY, IViewMode.IVM_PHONE);
//		bundle.putString(SetPersonIntentConstant.KEY_OBJECT_DATA, mPhoneString);
//		
//		intent.putExtra(DeviceIntentConstant.KEY_DATA_BUNDLE, bundle);
//		
//		setResult(RESULT_OK, intent);
//		
//		finish();
//	}
//	
//	private void commitNameSet()
//	{
//		mNameString = mNameEditText.getText().toString();	
//		
//		if (mNameString.length() == 0)
//		{
//			Utils.showToast(this, R.string.toask_name_not_null);
//			return ;
//		}
//		
//		Intent intent = new Intent();
//		intent.setClass(this, SetPersonActivity.class);
//		Bundle bundle = new Bundle();
//		
//		
//		intent.putExtra(VIEW_KEY, IViewMode.IVM_NAME);
//		bundle.putString(SetPersonIntentConstant.KEY_OBJECT_DATA, mNameString);
//		
//		intent.putExtra(DeviceIntentConstant.KEY_DATA_BUNDLE, bundle);
//		
//		setResult(RESULT_OK, intent);
//		
//		finish();
//	}
//	
//	private void commitBirthdaySet()
//	{
//		String year = mYearEditText.getText().toString();
//		String month = mMonthEditText.getText().toString();
//		String day = mDayEditText.getText().toString();
//		
//		mBirthday.year = Integer.valueOf(year);
//		mBirthday.month = Integer.valueOf(month);
//		mBirthday.day = Integer.valueOf(day);
//		
//		if (VertifyUtil.isVaildBirthday(mBirthday) == false)
//		{
//			Utils.showToast(this, R.string.toask_error_birthday);
//			return ;
//		}
//		
//		
//		Intent intent = new Intent();
//		intent.setClass(this, SetPersonActivity.class);
//		Bundle bundle = new Bundle();
//		
//		
//		intent.putExtra(VIEW_KEY, IViewMode.IVM_BIRTHDAY);
//		bundle.putString(SetPersonIntentConstant.KEY_OBJECT_DATA, mBirthday.toString());
//		
//		intent.putExtra(DeviceIntentConstant.KEY_DATA_BUNDLE, bundle);
//		
//		setResult(RESULT_OK, intent);
//		
//		finish();
//	}
//}
