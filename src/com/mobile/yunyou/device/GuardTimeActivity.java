package com.mobile.yunyou.device;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mobile.yunyou.R;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.device.adapter.GuardTimeAdapter;
import com.mobile.yunyou.model.DeviceSetType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.PopWindowFactory;
import com.mobile.yunyou.util.Utils;
import com.mobile.yunyou.util.VertifyUtil;

public class GuardTimeActivity extends Activity implements OnClickListener, OnItemClickListener, IRequestCallback{
	
	private static final CommonLog log = LogFactory.createLog();
	
	private final int REQUEST_CODE_SET_TIME = 0x0001;
	
	
	public final static String VIEW_KEY = "view_key";
	private int viewState = IViewMode.IVM_NONE;
	
	private NetworkCenterEx mNetworkCenterEx;
	
	private View mRootView;
	
	private Button mBtnAdd;
	private Button mBtnBack;
	private Button mBtnSave;
	private TextView mSimTitle;
	
	public static interface IViewMode
	{
		int IVM_NONE = -1;
		int IVM_CARE_TIME = 0;					    // 关注时段
		int IVM_IGNORE_TIME = 1;				    // 忽略时段
		int IVM_SLEEP_TIME = 2	;				    // 休眠时段
	}
	
	

	private ListView mGuardTimeListView;
	private GuardTimeAdapter mGuardTimeAdapter;
	private List<DeviceSetType.GpsStillTime> mGuardTimeItemArrays = new ArrayList<DeviceSetType.GpsStillTime>();
	private boolean mBoolean[];
	
	private int setTimePos = -1;
	private YunyouApplication mApplication;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dev_guard_layout);
		
		Intent intent = getIntent();
		viewState  = intent.getIntExtra(VIEW_KEY, IViewMode.IVM_NONE);
	
		
		switch(viewState)
		{
		case IViewMode.IVM_CARE_TIME:
			initCareTimeView();
			break;
		case IViewMode.IVM_IGNORE_TIME:
			initIgnoretimeView();
			break;
		case IViewMode.IVM_SLEEP_TIME:
			initSleeptimeView();
			break;
			default:
				finish();
				break;
		}
		
		Bundle bundle = intent.getBundleExtra(DeviceIntentConstant.KEY_DATA_BUNDLE);
		if (bundle == null)
		{
			log.e("bundle == null");
			finish();
			return ;
		}
		
		mGuardTimeItemArrays = bundle.getParcelableArrayList(DeviceIntentConstant.KEY_LIST_DATA);
		
		
		initView();
		
		initData();
	}

	private void initView()
	{
		mSimTitle = (TextView) findViewById(R.id.title);	
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
		mBtnAdd = (Button) findViewById(R.id.btn_addtime);
		mBtnAdd.setOnClickListener(this);
		mBtnSave = (Button) findViewById(R.id.btn_save);
		mBtnSave.setOnClickListener(this);
		
		mGuardTimeListView = (ListView) findViewById(R.id.listview);

		mRootView = findViewById(R.id.rootView);
	}
	
	private void initCareTimeView()
	{
		initView();
		
		mSimTitle.setText(R.string.guardtime_text_title1);		
	}
	
	private void initIgnoretimeView()
	{
		initView();
		
		mSimTitle.setText(R.string.guardtime_text_title2);
	}
	
	private void initSleeptimeView()
	{
		initView();
		
		mSimTitle.setText(R.string.guardtime_text_title3);
	}
	
	private final static int MAX_COUNT = 100;
	private void initData()
	{
		mNetworkCenterEx = NetworkCenterEx.getInstance();
		mApplication = YunyouApplication.getInstance();
		
		mBoolean = new boolean[MAX_COUNT];
		clearBoolean(mBoolean);
	
		
		mGuardTimeAdapter = new GuardTimeAdapter(this, mGuardTimeItemArrays, mBoolean);
		
		mGuardTimeListView.setAdapter(mGuardTimeAdapter);
		mGuardTimeListView.setOnItemClickListener(this);
		

	}
	
	private void clearBoolean(boolean flag[])
	{
		int size = flag.length;
		for(int i = 0; i < size; i++)
		{
			flag[i] = true; 
		}
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_addtime:
			addTime();
			break;
		case R.id.btn_save:
			save();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		goSetGuardTimeActivity(position);
	}
	
	private void goSetGuardTimeActivity(int index)
	{

		
		Intent intent = new Intent();
		switch(viewState)
		{
			case IViewMode.IVM_CARE_TIME:
				intent.putExtra(SetGuardTimeActivity.TITLE_KEY, getResources().getString(R.string.guardtime_text_title1));
				break;
			case IViewMode.IVM_IGNORE_TIME:
				intent.putExtra(SetGuardTimeActivity.TITLE_KEY, getResources().getString(R.string.guardtime_text_title2));
				break;
			case IViewMode.IVM_SLEEP_TIME:
				intent.putExtra(SetGuardTimeActivity.TITLE_KEY, getResources().getString(R.string.guardtime_text_title3));
				break;
				default:
					return ;
		}
		
		setTimePos = index;	
		
	
		intent.setClass(this, SetGuardTimeActivity.class);
		intent.putExtra(SetGuardTimeActivity.DATA_KEY, mGuardTimeItemArrays.get(index));
		startActivityForResult(intent, REQUEST_CODE_SET_TIME);

	}
	

	private void addTime()
	{
		setTimePos = mGuardTimeItemArrays.size();
		
		Intent intent = new Intent();
		intent.setClass(this, SetGuardTimeActivity.class);
	
		DeviceSetType.GpsStillTime set = new DeviceSetType.GpsStillTime();
		set.mStartTimeString = "0000";
		set.mEndTimeString = "1200";
		set.mWeekString = "[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\"]";
		intent.putExtra(SetClockActivity.DATA_KEY,set);
		startActivityForResult(intent, REQUEST_CODE_SET_TIME);
	}

	private void save()
	{
		if (!mApplication.isDeviceOnline()){
			Utils.showToast(this, R.string.toask_device_not_line);
			return ;
		}
		List<DeviceSetType.GpsStillTime> newGpsStillTimeArrays = new ArrayList<DeviceSetType.GpsStillTime>();
		int size = mGuardTimeItemArrays.size();
		for(int i = 0; i < size; i++)
		{
			if (mBoolean[i]== true)
			{
				newGpsStillTimeArrays.add(mGuardTimeItemArrays.get(i));
			}
		}
		
		if (vertifySuccess(newGpsStillTimeArrays) == false)
		{
			Utils.showToast(this, R.string.toask_error_guard_time);
			return ;
		}
		
		DeviceSetType.GpsStillTimeGroup group = new DeviceSetType.GpsStillTimeGroup();
		group.mGpsStillTimeList = newGpsStillTimeArrays;
		
		
		switch (viewState) {
		case IViewMode.IVM_CARE_TIME:
			mNetworkCenterEx.StartRequestToServer(DeviceSetType.DEVICE_GPS_STILLTIME_SET_MASID, group, this);
			break;
		case IViewMode.IVM_SLEEP_TIME:
			mNetworkCenterEx.StartRequestToServer(DeviceSetType.DEVICE_SET_SLEEPTIME_MASID, group, this);
			break;
		default:
			break;
		}
		
		
		
		showRequestDialog(true);
	}
	
	private boolean vertifySuccess(List<DeviceSetType.GpsStillTime> arrays)
	{

		List<DeviceSetType.GpsStillTime> tmpArray = new ArrayList<DeviceSetType.GpsStillTime>();
		int size = arrays.size();
		for(int i = 0; i < size; i++)
		{

			DeviceSetType.GpsStillTime time = arrays.get(i);
			for(int j = 0; j < tmpArray.size(); j++)
			{
				if (VertifyUtil.isIntersect(time, tmpArray.get(j)))
				{
					return false;
				}
			}

			tmpArray.add(time);
		}
		
		
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		switch(requestCode)
		{
		case REQUEST_CODE_SET_TIME:
			onSetTimeResult(resultCode, data);
			break;
		}
	}
	
	private void onSetTimeResult(int resultCode, Intent data)
	{
		if (resultCode == RESULT_OK)
		{
			DeviceSetType.GpsStillTime set = data.getParcelableExtra(SetClockActivity.DATA_KEY);
			if (set != null)
			{
				try {
					if (setTimePos < mGuardTimeItemArrays.size())
					{
						DeviceSetType.GpsStillTime gpsStillTime = mGuardTimeItemArrays.get(setTimePos);
						gpsStillTime.mStartTimeString = set.mStartTimeString;
						gpsStillTime.mEndTimeString = set.mEndTimeString;
						gpsStillTime.mWeekString = set.mWeekString;
					}else{
						mGuardTimeItemArrays.add(set);
						mBoolean[setTimePos] = true;
					}
				
					mGuardTimeAdapter.notifyDataSetChanged();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
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
			case DeviceSetType.DEVICE_GPS_STILLTIME_SET_MASID:
			{
				if (dataPacket.rsp == 0)
				{
					String msg = dataPacket.msg;
					if (msg.length() > 0)
					{
						Utils.showToast(this, msg);
					}else{
						Utils.showToast(this, R.string.set_data_fail);
					}

					return false;
				}
				
				finish();
			}
			break;
			case DeviceSetType.DEVICE_GET_SLEEPTIME_MASID:
			{
				if (dataPacket.rsp == 0)
				{
					String msg = dataPacket.msg;
					if (msg.length() > 0)
					{
						Utils.showToast(this, msg);
					}else{
						Utils.showToast(this, R.string.set_data_fail);
					}

					return false;
				}
				
				finish();
			}
			break;
			case DeviceSetType.DEVICE_SET_SLEEPTIME_MASID:
			{
				if (dataPacket.rsp == 0)
				{
					String msg = dataPacket.msg;
					if (msg.length() > 0)
					{
						Utils.showToast(this, msg);
					}else{
						Utils.showToast(this, R.string.set_data_fail);
					}

					return false;
				}
				
				finish();
			}
			break;
		}
		
		
		return true;
		
	}
	

}
