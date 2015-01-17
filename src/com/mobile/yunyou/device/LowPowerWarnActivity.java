package com.mobile.yunyou.device;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mobile.yunyou.R;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.custom.AbstractSpinerAdapter;
import com.mobile.yunyou.custom.SpinerPopWindow;
import com.mobile.yunyou.model.DeviceSetType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.PopWindowFactory;
import com.mobile.yunyou.util.Utils;


public class LowPowerWarnActivity extends Activity implements OnClickListener, 
											AbstractSpinerAdapter.IOnItemSelectListener,
											IRequestCallback{

	private static final CommonLog log = LogFactory.createLog();	
	
	private NetworkCenterEx mNetworkCenterEx;
	
	
	private View mRootView;
	private Button mBtnBack;
	private Button mBtnSave;
	private Button mBtnCancel;
	private TextView mTVWarnView;
	private ImageButton mBtnDropDown;
	private List<String> nameList = new ArrayList<String>();
	private YunyouApplication mApplication;
	private int mLevel = -1;
	private DeviceSetType.LowPowerWarn lowPowerWarn = new DeviceSetType.LowPowerWarn();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dev_lowpower_warn_layout);
		
	
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra(DeviceIntentConstant.KEY_DATA_BUNDLE);
		if (bundle == null)
		{
			log.e("bundle == null");
			finish();
			return ;
		}
		
		lowPowerWarn = bundle.getParcelable(DeviceIntentConstant.KEY_OBJECT_DATA);
		mLevel = lowPowerWarn.mPowerLevel;
		
		mApplication = YunyouApplication.getInstance();
		mNetworkCenterEx = NetworkCenterEx.getInstance();
		
		initView();
		
		initLogic();
	}

	private void initView()
	{
		mRootView = findViewById(R.id.rootView);
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
		mBtnSave = (Button) findViewById(R.id.btn_ok);
		mBtnSave.setOnClickListener(this);
		mBtnCancel = (Button) findViewById(R.id.btn_cancel);
		mBtnCancel.setOnClickListener(this);
		mTVWarnView = (TextView) findViewById(R.id.tv_warnvalue);
		mBtnDropDown = (ImageButton) findViewById(R.id.bt_dropdown);
		mBtnDropDown.setOnClickListener(this);
		
		
		
	}
	
	private void initLogic(){
		String[] names = getResources().getStringArray(R.array.dev_lowerwarn_name);
		for(int i = 0; i < names.length; i++){
			nameList.add(names[i]);
		}
		setWarnLevel(mLevel - 1);
		mSpinerPopWindow = new SpinerPopWindow(this);
		mSpinerPopWindow.refreshData(nameList, 0);
		mSpinerPopWindow.setItemListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
			case R.id.btn_back:
				finish();
				break;
			case R.id.btn_ok:
				OnOK();
				break;
			case R.id.btn_cancel:
				finish();
				break;
			case R.id.bt_dropdown:
				showSpinWindow();
				break;
		}
	}

	private void OnOK()
	{
		
		if (!mApplication.isDeviceOnline()){
			Utils.showToast(this, R.string.toask_device_not_line);
			return ;
		}

		commitLowWarn(mLevel);
	}
	

	
	private void commitLowWarn(int level)
	{
		if (level != -1){
			DeviceSetType.LowPowerWarn lowPowerWarn= new DeviceSetType.LowPowerWarn();
			lowPowerWarn.setLevelByRate(level);
			
			mNetworkCenterEx.StartRequestToServer(DeviceSetType.DEVICE_LOWPOWER_WARN_SET_MASID, lowPowerWarn, this);
			
			showRequestDialog(true);
		}		
	}

	
	private SpinerPopWindow mSpinerPopWindow;
	private void showSpinWindow(){
		log.e("showSpinWindow");
		mSpinerPopWindow.setWidth(mTVWarnView.getWidth());
		mSpinerPopWindow.showAsDropDown(mTVWarnView);
	}

	@Override
	public void onItemClick(int pos) {
		// TODO Auto-generated method stub
		setWarnLevel(pos);
		mLevel = pos + 1;
	}

	private void setWarnLevel(int pos){
		if (pos >= 0 && pos <= nameList.size()){
			String value = nameList.get(pos);
			log.e("value = " + value);
			mTVWarnView.setText(value);
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
		
		if (dataPacket == null)
		{
			Utils.showToast(this, R.string.set_data_fail);
			return false;
		}
		
		switch(requestAction)
		{
			case DeviceSetType.DEVICE_LOWPOWER_WARN_SET_MASID:
			{
				if (dataPacket.rsp == 1)
				{
					Utils.showToast(this, R.string.set_data_success);
					finish();
				}else{
					Utils.showToast(this, R.string.set_data_fail);
				}
			}
			break;
		}
		
		
		return true;
	}
}
