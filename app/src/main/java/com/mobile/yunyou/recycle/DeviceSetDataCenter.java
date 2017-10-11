package com.mobile.yunyou.recycle;
//package com.mobile.yunyou.device;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.Context;
//import android.view.Gravity;
//import android.view.View;
//import android.widget.PopupWindow;
//
//import com.mobile.yunyou.R;
//import com.mobile.yunyou.YunyouApplication;
//import com.mobile.yunyou.custom.SingleChoicePopWindow;
//import com.mobile.yunyou.model.DeviceSetType;
//import com.mobile.yunyou.model.ResponseDataPacket;
//import com.mobile.yunyou.network.IRequestCallback;
//import com.mobile.yunyou.network.NetworkCenterEx;
//import com.mobile.yunyou.util.CommonLog;
//import com.mobile.yunyou.util.LogFactory;
//import com.mobile.yunyou.util.PopWindowFactory;
//import com.mobile.yunyou.util.Utils;
//
//public class DeviceSetDataCenter implements IRequestCallback{
//
//	private static final CommonLog log = LogFactory.createLog();
//	
//	private NetworkCenterEx mNetworkCenter;
//	
//	private Context mContext;
//	private View mRootView;
//	
//	private SingleChoicePopWindow mSingleChoicePopWindow;
//	
//	private List<String> mDataList = new ArrayList<String>(); 
//	
//	
//	private String[] mSceneModeArrays = null;
//	private List<String> mSceneModeList = new ArrayList<String>(); 
//	
//	private String[] mPhoneCallModeArrays = null;
//	private List<String> mPhoneCallList = new ArrayList<String>(); 
//	
//	private String[] mPowerModeArrays = null;
//	private List<String> mPowerModeList = new ArrayList<String>(); 
//	
//	private YunyouApplication mApplication;
//	
//	public DeviceSetDataCenter(Context context, View parentView)
//	{
//		mContext = context;
//		mRootView = parentView;
//
//		init();
//	}
//
//	
//	public void init()
//	{
//		mNetworkCenter = NetworkCenterEx.getInstance();
//		mApplication = YunyouApplication.getInstance();
//		
//		mSingleChoicePopWindow = new SingleChoicePopWindow(mContext, mRootView, mDataList);
//		
//		mSceneModeArrays = mContext.getResources().getStringArray(R.array.dev_scenemode_name);
//		for(int i = 0; i < mSceneModeArrays.length; i++)
//		{
//			mSceneModeList.add(mSceneModeArrays[i]);
//		}
//		
//		
//		mPhoneCallModeArrays = mContext.getResources().getStringArray(R.array.dev_phonecall_mode_name);
//		for(int i = 0; i < mPhoneCallModeArrays.length; i++)
//		{
//			mPhoneCallList.add(mPhoneCallModeArrays[i]);
//		}
//		
//		
//		
//		mPowerModeArrays = mContext.getResources().getStringArray(R.array.dev_power_name);
//		for(int i = 0; i < mPowerModeArrays.length; i++)
//		{
//			mPowerModeList.add(mPowerModeArrays[i]);
//		}
//	}
//	
//	
//	private DeviceSetType.SceneMode sceneMode;
//	// silent, ring, shake, r&s
//	public void showSceneModeWindow(DeviceSetType.SceneMode object)
//	{
//		sceneMode = object;
//		mSingleChoicePopWindow.refreshData(mSceneModeList, sceneMode.getIndex());
//		mSingleChoicePopWindow.setTitle(mContext.getResources().getString(R.string.scenemode_text_title));
//		
//		mSingleChoicePopWindow.setOnOKButtonListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				int selItem = mSingleChoicePopWindow.getSelectItem();
//			
//				sceneMode.setMode(selItem);
//				
//				if (!mApplication.isDeviceOnline()){
//					Utils.showToast(mContext, R.string.toask_device_not_line);
//					return ;
//				}
//				
//				mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_SCENEMODE_SET_MASID, sceneMode, DeviceSetDataCenter.this);
//				showRequestDialog(true);
//			}
//		});
//		
//		mSingleChoicePopWindow.show(true);
//			
//	}
//	
//	private DeviceSetType.PhoneCallMode callMode;
//	// white/shield/allow
//	public void showPhoneCallModeWindow(DeviceSetType.PhoneCallMode object)
//	{
//		callMode = object;
//		
//		mSingleChoicePopWindow.refreshData(mPhoneCallList, callMode.getIndex());
//		mSingleChoicePopWindow.setTitle(mContext.getResources().getString(R.string.phonecall_text_title));
//		
//		mSingleChoicePopWindow.setOnOKButtonListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				int selItem = mSingleChoicePopWindow.getSelectItem();
//
//				callMode.setMode(selItem);
//				
//				if (!mApplication.isDeviceOnline()){
//					Utils.showToast(mContext, R.string.toask_device_not_line);
//					return ;
//				}
//				
//				mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_PHONE_CALL_MODE_SET_MASID, callMode, DeviceSetDataCenter.this);
//				showRequestDialog(true);
//				
//			}
//		});
//		
//		mSingleChoicePopWindow.show(true);
//	}
//	
//	private DeviceSetType.PowerSet powerSet;
//	public void showPowerModeWindow(DeviceSetType.PowerSet object)
//	{
//		powerSet = object;
//		
//		mSingleChoicePopWindow.refreshData(mPowerModeList, powerSet.getIndex());
//		mSingleChoicePopWindow.setTitle(mContext.getResources().getString(R.string.powermode_text_title));
//		
//		mSingleChoicePopWindow.setOnOKButtonListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				int selItem = mSingleChoicePopWindow.getSelectItem();
//			
//				powerSet.setPower(selItem);
//				
//				if (!mApplication.isDeviceOnline()){
//					Utils.showToast(mContext, R.string.toask_device_not_line);
//					return ;
//				}
//				
//				
//				mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_POWER_SET_MASID, powerSet, DeviceSetDataCenter.this);
//				showRequestDialog(true);
//			}
//		});
//		
//		mSingleChoicePopWindow.show(true);
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
//			Utils.showToast(mContext, R.string.set_data_fail);
//			return false;
//		}
//		
//		switch(requestAction)
//		{
//			case DeviceSetType.DEVICE_SCENEMODE_SET_MASID:			
//			{
//				if (dataPacket.rsp == 1)
//				{
//					Utils.showToast(mContext, R.string.set_data_success);
//				}else{
//					Utils.showToast(mContext, R.string.set_data_fail);
//				}
//			}
//			break;
//			case DeviceSetType.DEVICE_POWER_SET_MASID:			
//			{
//				if (dataPacket.rsp == 1)
//				{
//					Utils.showToast(mContext, R.string.set_data_success);
//				}else{
//					Utils.showToast(mContext, R.string.set_data_fail);
//				}
//			}
//			break;
//			case DeviceSetType.DEVICE_PHONE_CALL_MODE_SET_MASID:			
//			{
//				if (dataPacket.rsp == 1)
//				{
//					Utils.showToast(mContext, R.string.set_data_success);
//				}else{
//					Utils.showToast(mContext, R.string.set_data_fail);
//				}
//			}
//			break;
//		}
//		
//		return true;
//	}
//	
//	
//	
//}
