package com.mobile.yunyou.device;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.LevelListDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.mobile.yunyou.R;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.device.adapter.DeviceAdapter;
import com.mobile.yunyou.device.adapter.DeviceItemObject;
import com.mobile.yunyou.model.DeviceSetType;
import com.mobile.yunyou.model.DeviceSetType.ClockSetGroup;
import com.mobile.yunyou.model.DeviceSetType.GpsStillTimeGroup;
import com.mobile.yunyou.model.DeviceSetType.KeySetGroup;
import com.mobile.yunyou.model.DeviceSetType.WhiteListSetGroup;
import com.mobile.yunyou.model.GloalType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.PopWindowFactory;
import com.mobile.yunyou.util.Utils;

public class DeviceSetActivity extends Activity implements OnItemClickListener, OnClickListener, IRequestCallback{

	private static final CommonLog log = LogFactory.createLog();
	
	private View mRootView;
	
	private Button mBtnBack;
	private ListView mDeviceListView;
	private DeviceAdapter mDeviceAdapter;
	private List<DeviceItemObject> mDeviceItemArrays = new ArrayList<DeviceItemObject>();
	private GloalType.DeviceInfoEx mDeviceInfoEx;
	private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
	private Map<Integer, Integer> mapPos = new HashMap<Integer, Integer>();
	
	private NetworkCenterEx mNetworkCenterEx;
	private YunyouApplication mApplication;
	
	private DeviceSetDataCenter mDeviceSetDataCenter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_layout);
		

		mNetworkCenterEx = NetworkCenterEx.getInstance();
	//	mNetworkCenterEx.initNetwork();
		
		mApplication = YunyouApplication.getInstance();
		mDeviceInfoEx = mApplication.getCurDevice();
		if (mDeviceInfoEx == null){
			finish();
		}
		
		initView();
		
		initData();
		
	
	}

	
	public void initView()
	{
		mRootView = findViewById(R.id.dev_rootView);
		mDeviceListView = (ListView) findViewById(R.id.listview);
		
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
	}
	
	
	public void initData()
	{
		initMap(mDeviceInfoEx, map);
		
		String device_name[] = getResources().getStringArray(R.array.device_set_name);
		LevelListDrawable	mLevelListDrawable = (LevelListDrawable) getResources().getDrawable(R.drawable.device_set_level_list);
		
		int size = device_name.length;
		int index = 0;
		for(int i = 0; i < size; i++)
		{
			DeviceItemObject object = new DeviceItemObject();
			Boolean bShow = map.get(i);
			if (bShow != null && bShow){
				mapPos.put(index, i);
				index++;
				mLevelListDrawable.setLevel(i);
				object.mDrawable = mLevelListDrawable.getCurrent();
				object.mDeviceName = device_name[i];
				mDeviceItemArrays.add(object);
			}
			
	
		}

		mDeviceAdapter = new DeviceAdapter(this, mDeviceItemArrays);
	
		mDeviceListView.setAdapter(mDeviceAdapter);
		
		mDeviceListView.setOnItemClickListener(this);
	
		mNetworkCenterEx = NetworkCenterEx.getInstance();
		
		mDeviceSetDataCenter = new DeviceSetDataCenter(this, mRootView);
	}

	public void initMap(GloalType.DeviceInfoEx device,  Map<Integer, Boolean> map){
		
		GloalType.DeviceFunction function = device.mDeviceFunction;
		
		
		map.put(0, function.key);
		map.put(1, function.whitelist);
		map.put(2, function.clock);		
		map.put(3, function.gpsuploadtimetable);
		map.put(4, function.scenemode);
		map.put(5, function.callmodel);
		map.put(6, function.power);
		map.put(7, function.lowpower);
		map.put(8, function.calltime);
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
		showRequestDialog(true);
		
		Integer integer = mapPos.get(position);
		if (integer == null){
			return ;
		}
		
		position = integer;
		
		switch(position)
		{
			case 0:					// 按键设置
				mNetworkCenterEx.StartRequestToServer(DeviceSetType.DEVICE_GET_KEYSET_MASID, null, this);
				break;
			case 1:					// 白名单设置
				mNetworkCenterEx.StartRequestToServer(DeviceSetType.DEVICE_GET_WHITELIST_MASID, null, this);
				break;
			case 2:					// 闹钟设置
				mNetworkCenterEx.StartRequestToServer(DeviceSetType.DEVICE_GET_CLOCK_MASID, null, this);
				break;
			case 3:					// 关注时段设置
				mNetworkCenterEx.StartRequestToServer(DeviceSetType.DEVICE_GET_GPS_STILLTIME_MASID, null, this);
				break;
			case 4:					// 情景设置
				mNetworkCenterEx.StartRequestToServer(DeviceSetType.DEVICE_GET_SCENEMODE_MASID, null, this);
				break;
			case 5:					// 来电模式
				mNetworkCenterEx.StartRequestToServer(DeviceSetType.DEVICE_GET_PHONE_CALL_MODE_MASID, null, this);
				break;
//			case 6:					// 电源设置
//				mNetworkCenterEx.StartRequestToServer(DeviceSetType.DEVICE_GET_POWER_MASID, null, this);
//				break;
			case 6:					// 休眠时段
				mNetworkCenterEx.StartRequestToServer(DeviceSetType.DEVICE_GET_SLEEPTIME_MASID, null, this);
				break;
			case 7:					// 低电量提醒
				mNetworkCenterEx.StartRequestToServer(DeviceSetType.DEVICE_GET_LOWPOWER_WARN_MASID, null, this);
				break;
			case 8:					// 通话时长设置
				mNetworkCenterEx.StartRequestToServer(DeviceSetType.DEVICE_GET_CALL_TIME_MASID, null, this);
				break;
				default:
					break;
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
			default:
				break;
		}
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
		// TODO Auto-generated method stub
	
		String jsString = "null";
		if (dataPacket != null)
		{
			jsString = dataPacket.toString();
		}
		
		log.e("requestAction = " + Utils.toHexString(requestAction) + "\nResponseDataPacket = \n" +jsString);
		
		switch(requestAction)
		{
			case DeviceSetType.DEVICE_GET_KEYSET_MASID:			// 获取按键设置
				OnGetKeysetResult(dataPacket);
				break;
			case DeviceSetType.DEVICE_GET_WHITELIST_MASID:		// 获取白名单设置
				OnGetWhiteListResult(dataPacket);
				break;
			case DeviceSetType.DEVICE_GET_CLOCK_MASID:			// 获取闹钟设置
				OnGetClockResult(dataPacket);
				break;
			case DeviceSetType.DEVICE_GET_GPS_STILLTIME_MASID:	// 获取关注时段
				OnGetCareTimeResult(dataPacket);
				break;
			case DeviceSetType.DEVICE_GET_SLEEPTIME_MASID:		// 获取休眠时段
				OnGetSleepTimeResult(dataPacket);			
				break;
			case DeviceSetType.DEVICE_GET_SCENEMODE_MASID:		// 获取情景模式
				OnGetScenemodeResult(dataPacket);
				break;
			case DeviceSetType.DEVICE_GET_PHONE_CALL_MODE_MASID:// 获取来电模式
				OnGetPhoneCallModeResult(dataPacket);
				break;
//			case DeviceSetType.DEVICE_GET_POWER_MASID:			// 获取电源设置
//				OnGetPowerModeResult(dataPacket);
//				break;
			case DeviceSetType.DEVICE_GET_LOWPOWER_WARN_MASID:	// 获取低电告警
				OnGetLowPowerWarnResult(dataPacket);
				break;
			case DeviceSetType.DEVICE_GET_CALL_TIME_MASID:		// 获取通话时长
				OnGetCallTimeWarnResult(dataPacket);
				break;
				default:
					break;
		}
		
		showRequestDialog(false);
		
		return true;

	}

	private void OnGetKeysetResult(ResponseDataPacket dataPacket)
	{
		if (dataPacket == null || dataPacket.rsp == 0)
		{
			Utils.showToast(this, R.string.request_data_fail);
			
			return ;
		}
		
		KeySetGroup group = new KeySetGroup();
		try {
			group.parseString(dataPacket.data.toString());
			
			Intent intent = new Intent();
			intent.setClass(this, KeySettingActivity.class);
			
			Bundle bundle = new Bundle();
			bundle.putParcelableArrayList(DeviceIntentConstant.KEY_LIST_DATA, (ArrayList)group.mKeySetList);
		
			intent.putExtra(DeviceIntentConstant.KEY_DATA_BUNDLE, bundle);
			startActivity(intent);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Utils.showToast(this, R.string.analyze_data_fail);
		}	
		
	}
	
	private void OnGetWhiteListResult(ResponseDataPacket dataPacket)
	{
		if (dataPacket == null || dataPacket.rsp == 0)
		{
			Utils.showToast(this, R.string.request_data_fail);
			
			return ;
		}
		
		WhiteListSetGroup group = new WhiteListSetGroup();
		
	
		try {
			group.parseString(dataPacket.data.toString());
			
			Intent intent = new Intent();
			intent.setClass(this, WhiteListActivity.class);
			
			Bundle bundle = new Bundle();
			bundle.putParcelableArrayList(DeviceIntentConstant.KEY_LIST_DATA, (ArrayList)group.mWhiteListSetList);
		
			intent.putExtra(DeviceIntentConstant.KEY_DATA_BUNDLE, bundle);
			startActivity(intent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Utils.showToast(this, R.string.analyze_data_fail);
		}
	
		
		
	}
	
	private void OnGetClockResult(ResponseDataPacket dataPacket)
	{
		if (dataPacket == null || dataPacket.rsp == 0)
		{
			Utils.showToast(this, R.string.request_data_fail);
			return ;
		}
		
		
		ClockSetGroup group = new ClockSetGroup();
		
		
		try {
			group.parseString(dataPacket.data.toString());
			
			Intent intent = new Intent();
			intent.setClass(this, AlarmClockActivity.class);
			
			Bundle bundle = new Bundle();
			bundle.putParcelableArrayList(DeviceIntentConstant.KEY_LIST_DATA, (ArrayList)group.mClockSetList);
		
			intent.putExtra(DeviceIntentConstant.KEY_DATA_BUNDLE, bundle);
			startActivity(intent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Utils.showToast(this, R.string.analyze_data_fail);
		}
		
		
	}

	private void OnGetCareTimeResult(ResponseDataPacket dataPacket)
	{
		if (dataPacket == null || dataPacket.rsp == 0)
		{
			Utils.showToast(this, R.string.request_data_fail);
			return ;
		}
		
		
		GpsStillTimeGroup group = new GpsStillTimeGroup();
		
		
		try {
			group.parseString(dataPacket.data.toString());
			
			Intent intent = new Intent();
			intent.setClass(this, GuardTimeActivity.class);
			intent.putExtra(GuardTimeActivity.VIEW_KEY, GuardTimeActivity.IViewMode.IVM_CARE_TIME);
			
			Bundle bundle = new Bundle();
			bundle.putParcelableArrayList(DeviceIntentConstant.KEY_LIST_DATA, (ArrayList)group.mGpsStillTimeList);
		
			intent.putExtra(DeviceIntentConstant.KEY_DATA_BUNDLE, bundle);
			startActivity(intent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Utils.showToast(this, R.string.analyze_data_fail);
		}
		
		
	}
	
	private void OnGetSleepTimeResult(ResponseDataPacket dataPacket)
	{
		if (dataPacket == null || dataPacket.rsp == 0)
		{
			Utils.showToast(this, R.string.request_data_fail);
			return ;
		}
		
		
		GpsStillTimeGroup group = new GpsStillTimeGroup();
		
		
		try {
			group.parseString(dataPacket.data.toString());
			
			Intent intent = new Intent();
			intent.setClass(this, GuardTimeActivity.class);
			intent.putExtra(GuardTimeActivity.VIEW_KEY, GuardTimeActivity.IViewMode.IVM_SLEEP_TIME);
			
			Bundle bundle = new Bundle();
			bundle.putParcelableArrayList(DeviceIntentConstant.KEY_LIST_DATA, (ArrayList)group.mGpsStillTimeList);
		
			intent.putExtra(DeviceIntentConstant.KEY_DATA_BUNDLE, bundle);
			startActivity(intent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Utils.showToast(this, R.string.analyze_data_fail);
		}
		
		
	}
	
	
	private void OnGetScenemodeResult(ResponseDataPacket dataPacket)
	{
		if (dataPacket == null || dataPacket.rsp == 0)
		{
			Utils.showToast(this, R.string.request_data_fail);
			return ;
		}
		
		DeviceSetType.SceneMode sceneMode = new DeviceSetType.SceneMode();
		try {
			sceneMode.parseString(dataPacket.data.toString());
			
			mDeviceSetDataCenter.showSceneModeWindow(sceneMode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Utils.showToast(this, R.string.analyze_data_fail);
		}
		
	
	}
	
	private void OnGetPhoneCallModeResult(ResponseDataPacket dataPacket)
	{
		if (dataPacket == null || dataPacket.rsp == 0)
		{
			Utils.showToast(this, R.string.request_data_fail);
			return ;
		}
		
		DeviceSetType.PhoneCallMode callMode = new DeviceSetType.PhoneCallMode();
		try {
			callMode.parseString(dataPacket.data.toString());
			
			mDeviceSetDataCenter.showPhoneCallModeWindow(callMode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Utils.showToast(this, R.string.analyze_data_fail);
		}
	

	}
	
	private void OnGetPowerModeResult(ResponseDataPacket dataPacket)
	{
		if (dataPacket == null || dataPacket.rsp == 0)
		{
			Utils.showToast(this, R.string.request_data_fail);
			return ;
		}
		
		DeviceSetType.PowerSet powerSet = new DeviceSetType.PowerSet();
		try {
			powerSet.parseString(dataPacket.data.toString());
			
			mDeviceSetDataCenter.showPowerModeWindow(powerSet);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Utils.showToast(this, R.string.analyze_data_fail);
		}
	
	}

	private void OnGetLowPowerWarnResult(ResponseDataPacket dataPacket)
	{
		if (dataPacket == null || dataPacket.rsp == 0)
		{
			Utils.showToast(this, R.string.request_data_fail);
			return ;
		}
		
		DeviceSetType.LowPowerWarn lowPowerWarn = new DeviceSetType.LowPowerWarn();
		try {
			lowPowerWarn.parseString(dataPacket.data.toString());
			Intent intent = new Intent();
		//	intent.putExtra(SimpleDevSetActivity.VIEW_KEY, SimpleDevSetActivity.IViewMode.IVM_LOWER_WARN);
			
			Bundle bundle = new Bundle();
			bundle.putParcelable(DeviceIntentConstant.KEY_OBJECT_DATA, lowPowerWarn);		
			intent.putExtra(DeviceIntentConstant.KEY_DATA_BUNDLE, bundle);
			
			intent.setClass(this, LowPowerWarnActivity.class);
			startActivity(intent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Utils.showToast(this, R.string.analyze_data_fail);
		}

		
	}
	
	private void OnGetCallTimeWarnResult(ResponseDataPacket dataPacket)
	{
		if (dataPacket == null || dataPacket.rsp == 0)
		{
			Utils.showToast(this, R.string.request_data_fail);
			return ;
		}
		

		DeviceSetType.CallTime callTime = new DeviceSetType.CallTime();
		try {
			callTime.parseString(dataPacket.data.toString());
			Intent intent = new Intent();
			intent.putExtra(SimpleDevSetActivity.VIEW_KEY, SimpleDevSetActivity.IViewMode.IVM_CALL_TIME);
			
			Bundle bundle = new Bundle();
			bundle.putParcelable(DeviceIntentConstant.KEY_OBJECT_DATA, callTime);
			intent.putExtra(DeviceIntentConstant.KEY_DATA_BUNDLE, bundle);
			
			intent.setClass(this, SimpleDevSetActivity.class);
			startActivity(intent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Utils.showToast(this, R.string.analyze_data_fail);
		}
	
		
	}
	
	
}
