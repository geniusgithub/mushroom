package com.mobile.yunyou.recycle;
//package com.mobile.yunyou.device;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.Gravity;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.PopupWindow;
//import android.widget.TextView;
//
//import com.mobile.yunyou.R;
//import com.mobile.yunyou.YunyouApplication;
//import com.mobile.yunyou.model.DeviceSetType;
//import com.mobile.yunyou.model.ResponseDataPacket;
//import com.mobile.yunyou.network.IRequestCallback;
//import com.mobile.yunyou.network.NetworkCenterEx;
//import com.mobile.yunyou.util.CommonLog;
//import com.mobile.yunyou.util.LogFactory;
//import com.mobile.yunyou.util.PopWindowFactory;
//import com.mobile.yunyou.util.Utils;
//import com.mobile.yunyou.util.VertifyUtil;
//
//public class SimpleDevSetActivity extends Activity implements OnClickListener, IRequestCallback{
//
//	private static final CommonLog log = LogFactory.createLog();
//	
//	public final static String VIEW_KEY = "view_key";
//	public static interface IViewMode
//	{
//		int IVM_NONE = -1;
//		int IVM_LOWER_WARN = 0;					// 低电告警
//		int IVM_CALL_TIME = 1;				    // 通话时长
//	}
//	
//	private NetworkCenterEx mNetworkCenterEx;
//	
//	private int viewState = IViewMode.IVM_NONE;
//	
//	private View mRootView;
//	private View mLowWarnView;
//	private View mCallTimeView;
//	
//	private Button mBtnBack;
//	private TextView mSimTitle;
//	private Button mBtnSave;
//	private Button mBtnCancel;
//	private EditText mEditTextLowWarn;
//	private EditText mEditTextCallTime;
//	
//	private YunyouApplication mApplication;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.dev_simpleset_layout);
//		
//		
//		Intent intent = getIntent();
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
//		case IViewMode.IVM_LOWER_WARN:
//			initLowerWarnView(bundle);
//			break;
//		case IViewMode.IVM_CALL_TIME:
//			initCalltimeView(bundle);
//			break;
//			default:
//				finish();
//				break;
//		}
//		
//
//		
//		mApplication = YunyouApplication.getInstance();
//		mNetworkCenterEx = NetworkCenterEx.getInstance();
//	}
//
//	private void initView()
//	{
//		mSimTitle = (TextView) findViewById(R.id.title);
//		mBtnBack = (Button) findViewById(R.id.btn_back);
//		mBtnBack.setOnClickListener(this);
//		mBtnSave = (Button) findViewById(R.id.btn_ok);
//		mBtnSave.setOnClickListener(this);
//		mBtnCancel = (Button) findViewById(R.id.btn_cancel);
//		mBtnCancel.setOnClickListener(this);
//		
//		mEditTextLowWarn = (EditText) findViewById(R.id.et_lowwarn);
//		mEditTextCallTime = (EditText) findViewById(R.id.et_calltime);
//		
//		mLowWarnView = findViewById(R.id.ll_lowwarn);
//		mCallTimeView = findViewById(R.id.ll_calltime);
//		mRootView = findViewById(R.id.rootView);
//	}
//	
//	
//	private DeviceSetType.LowPowerWarn lowPowerWarn = new DeviceSetType.LowPowerWarn();
//	private void initLowerWarnView(Bundle bundle)
//	{
//		initView();
//		
//		mSimTitle.setText(R.string.simple_text_title1);
//		mLowWarnView.setVisibility(View.VISIBLE);	
//		
//		lowPowerWarn = bundle.getParcelable(DeviceIntentConstant.KEY_OBJECT_DATA);
//		//int rate = lowPowerWarn.getRate();
//		int rate = 0;
//		mEditTextLowWarn.setText("" + rate);
//	}
//	
//	private DeviceSetType.CallTime callTime = new DeviceSetType.CallTime();
//	private void initCalltimeView(Bundle bundle)
//	{
//		initView();
//		
//		mSimTitle.setText(R.string.simple_text_title2);
//		mCallTimeView.setVisibility(View.VISIBLE);
//		
//		callTime = bundle.getParcelable(DeviceIntentConstant.KEY_OBJECT_DATA);
//		mEditTextCallTime.setText("" + callTime.mTime);
//	}
//
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		switch(v.getId())
//		{
//			case R.id.btn_back:
//				finish();
//				break;
//			case R.id.btn_ok:
//				OnOK();
//				break;
//			case R.id.btn_cancel:
//				finish();
//				break;
//		}
//	}
//	
//	private void OnOK()
//	{
//		
//		if (!mApplication.isDeviceOnline()){
//			Utils.showToast(this, R.string.toask_device_not_line);
//			return ;
//		}
//		switch(viewState)
//		{
//		case IViewMode.IVM_LOWER_WARN:
//			commitLowWarn();
//			break;
//		case IViewMode.IVM_CALL_TIME:
//			commitCallTime();
//			break;
//			default:
//				finish();
//				break;
//		}
//	}
//	
//
//	
//	private void commitLowWarn()
//	{
//		String num = mEditTextLowWarn.getText().toString();
//		if (VertifyUtil.isAllNumber(num) == false)
//		{
//			Utils.showToast(this, R.string.toask_error_number);
//			return ;
//		}
//		
//		int rate = Integer.valueOf(num);
//		
//		lowPowerWarn.setLevelByRate(rate);
//		
//		mNetworkCenterEx.StartRequestToServer(DeviceSetType.DEVICE_LOWPOWER_WARN_SET_MASID, lowPowerWarn, this);
//		
//		showRequestDialog(true);
//		
//	}
//	
//	private void commitCallTime()
//	{
//		String num = mEditTextCallTime.getText().toString();
//		if (VertifyUtil.isAllNumber(num) == false)
//		{
//			Utils.showToast(this, R.string.toask_error_number);
//			return ;
//		}
//		
//		callTime.mTime = Integer.valueOf(num);
//		
//		mNetworkCenterEx.StartRequestToServer(DeviceSetType.DEVICE_CALL_TIME_MASID, callTime, this);
//		
//		showRequestDialog(true);
//		
//		
//	}
//	
//	
//	private PopupWindow mPopupWindow = null;
//	private void showRequestDialog(boolean bShow)
//	{
//	
//		if (mPopupWindow != null)
//		{
//			mPopupWindow.dismiss();
//			mPopupWindow = null;
//		}
//		
//		if (bShow)
//		{
//			mPopupWindow = PopWindowFactory.creatLoadingPopWindow(this, R.string.sending_request);
//			mPopupWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);
//		}
//	
//	}
//
//	@Override
//	public boolean onComplete(int requestAction, ResponseDataPacket dataPacket) {
//		// TODO Auto-generated method stub
//
//
//		showRequestDialog(false);
//		
//		String jsString = "null";
//		if (dataPacket != null)
//		{
//			jsString = dataPacket.toString();
//		}
//		
//		
//		log.e("requestAction = " + Utils.toHexString(requestAction) + "\nResponseDataPacket = \n" +jsString);
//		
//		if (dataPacket == null)
//		{
//			Utils.showToast(this, R.string.set_data_fail);
//			return false;
//		}
//		
//		switch(requestAction)
//		{
//			case DeviceSetType.DEVICE_LOWPOWER_WARN_SET_MASID:
//			case DeviceSetType.DEVICE_CALL_TIME_MASID:
//			{
//				if (dataPacket.rsp == 1)
//				{
//					Utils.showToast(this, R.string.set_data_success);
//					finish();
//				}else{
//					Utils.showToast(this, R.string.set_data_fail);
//				}
//			}
//			break;
//		}
//		
//		
//		return true;
//	}
//	
//}
