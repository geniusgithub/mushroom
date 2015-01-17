//package com.mobile.yunyou.map;
//
//import java.security.spec.MGF1ParameterSpec;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//import java.util.Set;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.text.InputType;
//import android.view.Gravity;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ListView;
//import android.widget.PopupWindow;
//
//
//import com.mobile.yunyou.R;
//import com.mobile.yunyou.YunyouApplication;
//import com.mobile.yunyou.custom.CustomTimeExPopWindow;
//import com.mobile.yunyou.custom.DeviceAreaDialog;
//import com.mobile.yunyou.custom.DeviceHistoryDialog;
//import com.mobile.yunyou.custom.RangeTimeDialog;
//import com.mobile.yunyou.custom.SingleEditPopWindow;
//import com.mobile.yunyou.device.DeviceIntentConstant;
//import com.mobile.yunyou.device.GuardTimeActivity;
//import com.mobile.yunyou.map.data.DeviceAreaManager;
//import com.mobile.yunyou.map.data.DeviceHistoryManager;
//import com.mobile.yunyou.map.data.DeviceLocationManager;
//import com.mobile.yunyou.map.util.ArithmeticUtil;
//import com.mobile.yunyou.map.util.WebManager;
//import com.mobile.yunyou.model.BaseType;
//import com.mobile.yunyou.model.DeviceSetType;
//import com.mobile.yunyou.model.GloalType;
//import com.mobile.yunyou.model.DeviceSetType.DeviceHistoryResult;
//import com.mobile.yunyou.model.DeviceSetType.GpsStillTime;
//import com.mobile.yunyou.model.DeviceSetType.GpsStillTimeGroup;
//import com.mobile.yunyou.model.GloalType.DeviceInfoEx;
//import com.mobile.yunyou.model.ResponseDataPacket;
//import com.mobile.yunyou.network.IRequestCallback;
//import com.mobile.yunyou.network.NetworkCenterEx;
//import com.mobile.yunyou.util.CommonLog;
//import com.mobile.yunyou.util.LogFactory;
//import com.mobile.yunyou.util.PopWindowFactory;
//import com.mobile.yunyou.util.Utils;
//import com.mobile.yunyou.util.VertifyUtil;
//import com.mobile.yunyou.util.YunTimeUtils;
//
//public class DeviceSetProxy implements IRequestCallback{
//
//	private static final CommonLog log = LogFactory.createLog();
//	
//	private YunyouApplication mApplication;
//	private NetworkCenterEx mNetworkCenter;
//	
//	private Context mContext;
//	private View mRootView;
//	
//	private SingleEditPopWindow mSingleEditPopWindow;
//	private DeviceSelPopWindow mDeviceSelPopWindow;
//	private DeviceAreaDialog mDeviceAreaDialog;
//	private RangeTimeDialog mRangeTimeDialog;
//	private CustomTimeExPopWindow mCustomTimeExPopWindow;
//	private DeviceHistoryDialog mDeviceHistoryDialog;
//	
//	private View mDeviceSelView;
//	private ListView mDeviceListView;
//	private DeviceSelAdapter mDeviceSelAdapter;
//
//	private DeviceLocationManager mDeviceLocationManager;
//	
//	private Handler mMapHandler;
//	
//	public DeviceSetProxy(Context context, View parentView, Handler mapHandler)
//	{
//		mContext = context;
//		mRootView = parentView;
//		mMapHandler = mapHandler;
//		
//		init();
//	}
//
//	public void setLocationMng(DeviceLocationManager object){
//		mDeviceLocationManager = object;
//	}
//	
//	public void init()
//	{
//		mApplication = YunyouApplication.getInstance();
//		mNetworkCenter = NetworkCenterEx.getInstance();
//		
//		mSingleEditPopWindow = new SingleEditPopWindow(mContext, mRootView);
//		mSingleEditPopWindow.setTitle(R.string.popwindow_title_listener);
//		mSingleEditPopWindow.setTextString(R.string.popwindow_text_localphone);
//		mSingleEditPopWindow.setInputType(InputType.TYPE_CLASS_NUMBER);
//		mSingleEditPopWindow.setOnOKButtonListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				
//				onSendListener();				
//			}
//		});
//		
//		mDeviceSelPopWindow = new DeviceSelPopWindow(mContext);
//		mDeviceSelPopWindow.setSelChangeListener(new DeviceSelAdapter.SelChangeListener() {
//
//			@Override
//			public void onItemChange(DeviceInfoEx deviceInfoEx) {
//				// TODO Auto-generated method stub
//				mApplication.setCurDevice(deviceInfoEx);
//				showDeviceSelWindowAsDropDown(mRootView, false);
//				
//				if (mDeviceLocationManager != null){
//					mDeviceLocationManager.requesetNow();
//				}
//
//				Message msg = mMapHandler.obtainMessage(MapABCActivity.MAP_MESSAGE_REFRESH_POS);
//				msg.sendToTarget();
//			}
//			
//	
//		});
//		
//		
//		mDeviceAreaDialog = new DeviceAreaDialog(mContext, new ArrayList<DeviceSetType.DeviceAreaResult>());
//		mRangeTimeDialog = new RangeTimeDialog(mContext);
//	
//		mCustomTimeExPopWindow = new CustomTimeExPopWindow(mContext, mRootView);
//		mCustomTimeExPopWindow.setTitle(mContext.getResources().getString(R.string.popwindow_title_date));
//		
//		mDeviceHistoryDialog = new DeviceHistoryDialog(mContext, new ArrayList<DeviceSetType.GpsStillTime>());
//	}
//	
//	
//	public void showDeviceSelWindowAsDropDown(View view, boolean bShow)
//	{
//		if (bShow == false)
//		{
//			mDeviceSelPopWindow.dismiss();
//			return ;
//		}
//		
//		if (mApplication.isBindDevice() == false)
//		{
//			Utils.showToast(mContext, R.string.toask_tip_unbind);
//			return ;
//		}
//
//		List<GloalType.DeviceInfoEx> list = mApplication.getDeviceList();
//		GloalType.DeviceInfoEx curDeviceInfoEx = mApplication.getCurDevice();
//		int index = list.indexOf(curDeviceInfoEx);
//		mDeviceSelPopWindow.refreshData(list, index);
//		mDeviceSelPopWindow.showAsDropDown(view);
//	
//	}
//	
//	public boolean isDeviceSelWindowShown()
//	{
//		return mDeviceSelPopWindow.isShowing();
//	}
//
//	
//	public void showListenerWindow()
//	{
//		mSingleEditPopWindow.setEditString("");
//		mSingleEditPopWindow.show(true);
//	}
//	
//	public void showRangeTimeDialog()
//	{
//		mRangeTimeDialog.clear();
//		mRangeTimeDialog.show();
//		
//		mRangeTimeDialog.setOnOKButtonListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View view) {
//				// TODO Auto-generated method stub
//				BaseType.RangeTime rangeTime = mRangeTimeDialog.getRangeTime();
//				if (rangeTime != null)
//				{
//					if (rangeTime.isValid())
//					{
//						mRangeTimeDialog.dismiss();
//						RequestLocationHistroy(rangeTime);
//					}else{
//						Utils.showToast(mContext, "请输入合法日期");
//					}
//				}else{
//					Utils.showToast(mContext, "请输入合法日期");
//				}			
//			}
//		});
//	}
//	
//	public void showCustomTimeExDialog()
//	{
//		mCustomTimeExPopWindow.show(true);
//		mCustomTimeExPopWindow.setOnOKButtonListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				int year = mCustomTimeExPopWindow.getYear();
//				int month = mCustomTimeExPopWindow.getMonth();
//				int day = mCustomTimeExPopWindow.getDay();
//				onRequestQuery(year, month, day);
//			}
//		});
//	}
//	
//	private void onRequestQuery(int year, int month, int day)
//	{
//		if (!VertifyUtil.isVaildDate(year, month, day))
//		{
//			Utils.showToast(mContext, "请选择合法日期");
//			return ;
//		}
//		
//		log.e("year = " + year + ", month = " + month + ", day = " + day);
//		BaseType.Birthday oBirthday = new BaseType.Birthday();
//		oBirthday.year = year;
//		oBirthday.month = month;
//		oBirthday.day = day;
//		DeviceSetType.GetGpsStillTime object = new DeviceSetType.GetGpsStillTime();
//		object.data = oBirthday.toString();
//		DeviceHistoryManager deviceHistoryManager = DeviceHistoryManager.getInstance();
//		deviceHistoryManager.setQueryDate(year, month, day);
//		mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_GET_GPS_STILLTIME_MASID, object, this);
//		showRequestDialog(true);
//	}
//	
//	public void shoutDown()
//	{
//		
//		 if (mApplication.isBindDevice() == false)
//		 {
//			 Utils.showToast(mContext, R.string.toask_tip_unbind);
//			 return ;
//		 }	 
//		 
//		 
//		 if (mApplication.isDeviceOnline() == false)
//		 {
//			 Utils.showToast(mContext,  R.string.toask_tip_unable_shutdown);
//			 return ;
//		 }
//		 
//		DeviceSetType.PowerSet powerSet = new DeviceSetType.PowerSet();
//		powerSet.mSceenString = "poweroff";
//		mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_POWER_SET_MASID, powerSet, this);
//		
//		showRequestDialog(true);
//	}
//	
//	private  void onSendListener()
//	{
//		String phone = mSingleEditPopWindow.getEditString();
//		
//		if (phone.length() == 0)
//		{
//			Utils.showToast(mContext, R.string.toask_phone_not_null);
//			return ;
//		}
//		
//		if (VertifyUtil.isMobileNumber(phone) == false)
//		{
//			Utils.showToast(mContext, R.string.toask_error_phone);
//			return ;
//		}
//		
//		DeviceSetType.RemoteMonitor remoteMonitor = new DeviceSetType.RemoteMonitor();
//		remoteMonitor.mPhone = phone;
//		
//		mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_REMOTE_MONITOR_MASID, remoteMonitor, this);
//		showRequestDialog(true);
//	}
//	
//	public void RequestDeviceArea()
//	{
//		DeviceSetType.DeviceArea object = new DeviceSetType.DeviceArea();
//		object.mType = "all";
//		object.mOffset = 0;
//		object.mNum = 10;
//
//		mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_GET_AREA_MASID, object, this);
//		showRequestDialog(true);
//	}
//	
//	public void RequestLocationHistroy(BaseType.RangeTime rangeTime)
//	{
//		DeviceSetType.DeviceHistory object = new DeviceSetType.DeviceHistory();
//		object.mStartTime = rangeTime.getStartTime();
//		object.mEndTime = rangeTime.getEndTime();
//
//		
//		mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_GET_DEVICE_HISTORY_MASID, object, this);
//		showRequestDialog(true);
//	}
//	
//	private PopupWindow mPopupWindow = null;
//	public void showRequestDialog(boolean bShow)
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
//			mPopupWindow = PopWindowFactory.creatLoadingPopWindow(mContext, R.string.sending_request);
//			mPopupWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);
//		}
//	
//	}
//	
//
//	@Override
//	public boolean onComplete(int requestAction, ResponseDataPacket dataPacket) {
//		// TODO Auto-generated method stub
//
//		showRequestDialog(false);
//		String jsString = "null";
//		if (dataPacket != null)
//		{
//			jsString = dataPacket.toString();
//		}
//		
//		log.e("requestAction = " + Utils.toHexString(requestAction) + "\nResponseDataPacket = \n" +jsString);
//		
//		if (dataPacket == null)
//		{
//			Utils.showToast(mContext, R.string.toask_request_fail);
//			return false;
//		}
//		switch(requestAction)
//		{
//			case DeviceSetType.DEVICE_REMOTE_MONITOR_MASID:
//			{
//				if (dataPacket.rsp == 1)
//				{
//					Utils.showToast(mContext, R.string.toask_request_success);
//				}else{
//					Utils.showToast(mContext, R.string.toask_request_fail);
//				}
//			}
//			break;
//			case DeviceSetType.DEVICE_POWER_SET_MASID:
//			{
//				if (dataPacket.rsp == 1)
//				{
//					Utils.showToast(mContext, R.string.toask_request_success);
//				}else{
//					Utils.showToast(mContext, R.string.toask_request_fail);
//				}
//			}
//			break;
//			case DeviceSetType.DEVICE_GET_AREA_MASID:
//			{
//				onDeviceAreaResult(dataPacket);
//			}
//			break;
//			case DeviceSetType.DEVICE_GET_DEVICE_HISTORY_MASID:
//			{
//				onDeviceHistoryResult(dataPacket);
//			}
//			break;
//			case DeviceSetType.DEVICE_GET_GPS_STILLTIME_MASID:
//			{
//				onGetGPSUploadTimeResult(dataPacket);
//			}
//				break;
//		}
//		
//		return true;
//	}
//	
//	private void onDeviceAreaResult(ResponseDataPacket dataPacket)
//	{
//		if (dataPacket.rsp == 0)
//		{
//			Utils.showToast(mContext, R.string.toask_request_fail);
//			return ;
//		}
//		
//		final DeviceSetType.DeviceAreaResultGrounp grounp = new DeviceSetType.DeviceAreaResultGrounp();
//		try {
//			grounp.parseString(dataPacket.data.toString());
//			
//			if (grounp.mDeviceArealist.size() == 0)
//			{
//				Utils.showToast(mContext, "当前无围栏信息...");
//				return ;
//			}
//			
//			mDeviceAreaDialog.refreshData(grounp.mDeviceArealist);
//			mDeviceAreaDialog.show();
//			mDeviceAreaDialog.setItemClickListener(new OnItemClickListener() {
//
//				@Override
//				public void onItemClick(AdapterView<?> arg0, View arg1,
//						int position, long arg3) {
//					// TODO Auto-generated method stub
//					log.e("mDeviceAreaDialog onItemClick pos = " + position);
//					DeviceAreaManager manager = DeviceAreaManager.getInstance();
//					Utils.showToast(mContext, "正在获取围栏信息...");
//					DeviceAreaManager.ICorrectLocation listener = new DeviceAreaManager.ICorrectLocation() {
//						
//						@Override
//						public void isCorrestSuccess(boolean flag) {
//							// TODO Auto-generated method stub
//							
//							Message msg = mMapHandler.obtainMessage(MapABCActivity.MAP_MESSAGE_REFRESH_DEVICE_AREA);
//							msg.sendToTarget();
//						}
//					};
//					
//
//					manager.syncSetAreaObject(grounp.mDeviceArealist.get(position), listener); 
//				
//					
//				}
//				
//				
//			});
//			
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			Utils.showToast(mContext, R.string.analyze_data_fail);
//		}
//	}
//	
//	
//	private void onDeviceHistoryResult(ResponseDataPacket dataPacket)
//	{
//		if (dataPacket.rsp == 0)
//		{
//			Utils.showToast(mContext, R.string.toask_request_fail);
//			return ;
//		}
//		
//		final DeviceSetType.DeviceHistoryResultGrounp grounp = new DeviceSetType.DeviceHistoryResultGrounp();
//		try {
//			grounp.parseString(dataPacket.data.toString());
//			
//			if (grounp.mDeviceHistorylist.size() == 0)
//			{
//				Utils.showToast(mContext, "当前无轨迹信息...");
//				return ;
//			}
//			
//			List<DeviceHistoryResult> mDeviceHistorylist = grounp.mDeviceHistorylist;
//			
//			log.e("before filter size = " + mDeviceHistorylist.size());	
//			ArithmeticUtil.filterLocationHistory(mDeviceHistorylist);
//			log.e("after filter size = " + mDeviceHistorylist.size());
//			
//			Set<Integer> set = ArithmeticUtil.getStaticPoint(mDeviceHistorylist);
//			log.e("static point set size = " + set.size());
//
//			DeviceHistoryManager deviceHistoryManager = DeviceHistoryManager.getInstance();
//			deviceHistoryManager.setStaticPointSet(set);
//			
//			
//			deviceHistoryManager.syncSetLocationList(grounp.mDeviceHistorylist, new DeviceHistoryManager.ICorrectLocation() {
//				
//				@Override
//				public void isCorrestSuccess(boolean flag) {
//					// TODO Auto-generated method stub
//					Message msg = mMapHandler.obtainMessage(MapABCActivity.MAP_MESSAGE_REFRESH_DEVICE_HISTORY);
//					msg.sendToTarget();
//				}
//			});
//
//			Utils.showToast(mContext, "正在获取轨迹信息...");
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			Utils.showToast(mContext, R.string.analyze_data_fail);
//		}
//
//	}
//	
//
//	private void onGetGPSUploadTimeResult(ResponseDataPacket dataPacket)
//	{
//		if (dataPacket == null || dataPacket.rsp == 0)
//		{
//			Utils.showToast(mContext, R.string.request_data_fail);
//			return ;
//		}
//		
//		GpsStillTimeGroup group = new GpsStillTimeGroup();				
//		try {
//			group.parseString(dataPacket.data.toString());
//			
//			List<GpsStillTime> list = group.mGpsStillTimeList;
//			if (list.size() == 0)
//			{
//				Utils.showToast(mContext, "当前无轨迹信息...");
//				return ;
//			}
//			
//			DeviceHistoryManager manager = DeviceHistoryManager.getInstance();
//			BaseType.Birthday object = manager.getQueryDate();
//			
//			log.e("before filter size = " + list.size());
//			YunTimeUtils.filterGpsStillTime(list, object.year, object.month, object.day);
//			log.e("after filter size = " + list.size());
//		
//			if (list.size() == 0)
//			{
//				Utils.showToast(mContext, "当前无轨迹信息...");
//				return ;
//			}
//			manager.setGpsStillTimeList(list);
//			showRuestDeviceHistoryDialog();
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			Utils.showToast(mContext, R.string.analyze_data_fail);
//		}
//
//	}
//	
//	
//	private void showRuestDeviceHistoryDialog()
//	{
//		final DeviceHistoryManager manager = DeviceHistoryManager.getInstance();
//		mDeviceHistoryDialog.refreshData(manager.getGpsStillTimeList());
//		mDeviceHistoryDialog.show();
//		
//		mDeviceHistoryDialog.setItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View arg1, int position,
//					long arg3) {
//				// TODO Auto-generated method stub
//				log.e("mDeviceHistoryDialog onItemClick pos = " + position);
//				
//				DeviceSetType.GpsStillTime object = (GpsStillTime) parent.getItemAtPosition(position);			
//				BaseType.RangeTime time = DeviceHistoryManager.getRangeTime(object, manager.getQueryDate());
//				RequestLocationHistroy(time);
//			}
//		});
//	}
//	
//}
