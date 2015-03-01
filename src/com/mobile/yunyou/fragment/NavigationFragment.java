package com.mobile.yunyou.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobile.yunyou.BrocastFactory;
import com.mobile.yunyou.R;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.activity.MainSlideActivity;
import com.mobile.yunyou.bike.SafeActivity;
import com.mobile.yunyou.datastore.CurDayDataObject;
import com.mobile.yunyou.datastore.CurDayRecordDBManager;
import com.mobile.yunyou.map.util.StringUtil;
import com.mobile.yunyou.model.GloalType;
import com.mobile.yunyou.msg.MessageExActivity;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.network.api.HeadFileConfigure;
import com.mobile.yunyou.set.SetPersonActivity;
import com.mobile.yunyou.set.SettingExActivity;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.FileManager;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.Utils;
import com.mobile.yunyou.util.YunTimeUtils;
import com.mobile.yunyou.widget.CustomImageView;

public class NavigationFragment extends Fragment implements OnClickListener{

	private static final CommonLog log = LogFactory.createLog();

	private View mView;
	
	private LinearLayout mLLGoLocation;
	private LinearLayout mLLGoSafe;
	private LinearLayout mLLGoNewRun;
	private LinearLayout mLLGoRunRecord;
	//private LinearLayout mLLGoWarning;
	private LinearLayout mLLGoInfo;
	private LinearLayout mLLGoSetting;
	private RelativeLayout mRLHead;
	
	
	  private CustomImageView mHeadImageView;
	  private TextView mTVNickName;
	  private TextView mTVDistance;
	
	  private YunyouApplication mApplication;
		private NetworkCenterEx mNetworkCenter;
		
	private Context mContext;
	private BroadcastReceiver mReceiver;
	
	private CurDayRecordDBManager mCurDayRecordDBManager;
	
	
	public NavigationFragment(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		log.e("NavigationFragment onCreate");

	}


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		log.e("NavigationFragment onDestroy");
		mContext.unregisterReceiver(mReceiver);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		log.e("NavigationFragment onCreateView");
		
		mView = inflater.inflate(R.layout.navitation_channel_layout, null);
		return mView;	
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		log.e("NavigationFragment onActivityCreated");
		
		mContext = getActivity();
		
		setupViews();
		
		mReceiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context content, Intent intent) {
				if (intent.getAction().equalsIgnoreCase(BrocastFactory.BROCAST_UPDATE_NICKNAME)){
					updateNickname();
				}else if (intent.getAction().equalsIgnoreCase(BrocastFactory.BROCAST_UPDATE_HEADICON)){
					loadHead = false;
					updateHead();
				}
			}
		};
		
		IntentFilter intentFilter = new IntentFilter(BrocastFactory.BROCAST_UPDATE_NICKNAME);
		intentFilter.addAction(BrocastFactory.BROCAST_UPDATE_HEADICON);
		mContext.registerReceiver(mReceiver, intentFilter);
		

	}
	

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		updateHead();
	}

	private void setupViews(){
		
		mRLHead = (RelativeLayout) mView.findViewById(R.id.rl_head);
		
		mHeadImageView =  (CustomImageView) mView.findViewById(R.id.iv_head);
		mLLGoLocation = (LinearLayout) mView.findViewById(R.id.ll_goLocation);
		mLLGoSafe = (LinearLayout) mView.findViewById(R.id.ll_goSafe);
		mLLGoNewRun = (LinearLayout) mView.findViewById(R.id.ll_goNewRun);
		mLLGoRunRecord = (LinearLayout) mView.findViewById(R.id.ll_goRunRecord);
		//mLLGoWarning = (LinearLayout) mView.findViewById(R.id.ll_goWarning);
		mLLGoInfo = (LinearLayout) mView.findViewById(R.id.ll_goGoInfo);
		mLLGoSetting = (LinearLayout) mView.findViewById(R.id.ll_goSetting);
		
		mTVNickName = (TextView) mView.findViewById(R.id.tv_nickname);
		mTVDistance = (TextView) mView.findViewById(R.id.tv_curDidtance);
		
		mRLHead.setOnClickListener(this);
		mHeadImageView.setOnClickListener(this);
		mLLGoLocation.setOnClickListener(this);
		mLLGoSafe.setOnClickListener(this);
		mLLGoNewRun.setOnClickListener(this);
		mLLGoRunRecord.setOnClickListener(this);
		//mLLGoWarning.setOnClickListener(this);
		mLLGoInfo.setOnClickListener(this);
		mLLGoSetting.setOnClickListener(this);
		
	
		
		mApplication = YunyouApplication.getInstance();

		
		mNetworkCenter = NetworkCenterEx.getInstance();
		
		mCurDayRecordDBManager = CurDayRecordDBManager.getInstance();
		
		updateDistance();
		updateNickname();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_goLocation:
			goLocation();
			break;
		case R.id.ll_goSafe:
			goSafe();
			break;		
		case R.id.ll_goNewRun:
			goNewRun();	
			break;
		case R.id.ll_goRunRecord:
			goRunRecord();
			break;
//		case R.id.ll_goWarning:
//			goWarning();
//			break;
		case R.id.ll_goGoInfo:
			goInfo();
			break;
		case R.id.ll_goSetting:
			goSetting();
			break;
		case R.id.rl_head:
		case R.id.iv_head:
			goPersion();
			break;
		default:
			break;
		}
	}


	
	private void goLocation(){
		
		if (mApplication.isBindDevice()){
			FragmentActivity activity = getActivity();
			if (activity instanceof MainSlideActivity){
				MainSlideActivity mainSlideActivity = (MainSlideActivity) activity;
				mainSlideActivity.goFollowBike();
			}
		}else{
			Utils.showToast(getActivity(), R.string.toask_unbind_bike);
		}

	}
	
	private void goNewRun(){
//		Intent intent = new Intent();
//		intent.setClass(getActivity(), NewBikeExActivity.class);
//		startActivity(intent);
		FragmentActivity activity = getActivity();
		if (activity instanceof MainSlideActivity){
			MainSlideActivity mainSlideActivity = (MainSlideActivity) activity;
			mainSlideActivity.goNewBike();
		}
	}
	
	private void goRunRecord(){
		FragmentActivity activity = getActivity();
		if (activity instanceof MainSlideActivity){
			MainSlideActivity mainSlideActivity = (MainSlideActivity) activity;
			mainSlideActivity.goRunRecord();
		}
	}
	
	private void goSafe(){
		
		if (!mApplication.isBindDevice()){
			Utils.showToast(getActivity(), R.string.toask_bind_device);
			return ;
		}
		Intent intent = new Intent();
		intent.setClass(getActivity(), SafeActivity.class);
		startActivity(intent);
	}
	

//	private void goWarning(){
//		Intent intent = new Intent();
//		intent.setClass(getActivity(), WarningActivity.class);
//		startActivity(intent);
//	}
	
	private void goInfo(){
		Intent intent = new Intent();
		intent.setClass(getActivity(), MessageExActivity.class);
		startActivity(intent);
	}
	
	private void goSetting(){
		Intent intent = new Intent();
		intent.setClass(getActivity(), SettingExActivity.class);
		startActivity(intent);
	}
	
	private void goPersion(){
		Intent intent = new Intent();
    	intent.setClass(getActivity(), SetPersonActivity.class);
    	startActivity(intent);
	}
	
	public void updateDistance(){
		log.e("NavigationFragment updateDistance");
		String userID = mApplication.getUserInfoEx().mSid;
		CurDayDataObject object = null;
		try {
			object = mCurDayRecordDBManager.query(userID);
			log.e("mCurDayRecordDBManager query \n" +  
					"\nuserid = " + object.userID + 
					"\nstarttime = " + object.startTime + 
					"\ndistance = " + object.distance);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		double distance = 0;
		if (object != null){
			String curTeString = object.startTime;
			long time = System.currentTimeMillis();
			String curTime = YunTimeUtils.getFormatTime1(time);
			
			log.e("curTeString = " + curTeString + ", curTime = " + curTime);

			if (curTeString.equalsIgnoreCase(curTime)){
				distance = object.distance;			
			}else{
				
			}

			log.e("NavigationFragment distance = " + distance);
		}else{
			log.e("updateDistance object = null");
		}
		
		
		String text = "当天里程: " + StringUtil.ConvertByDoubeString(distance) + "km";
		mTVDistance.setText(text);
	}
	
	private void updateNickname(){
		mTVNickName.setText(mApplication.getUserInfoEx().mTrueName);
	}
	
    public  boolean  loadHead = false;
	public void updateHead(){
	
		GloalType.UserInfoEx userInfoEx = mApplication.getUserInfoEx();
		int type = userInfoEx.mType;
		log.e("NavigationFragment	updateHead type = " + type + ", loadHead = " + loadHead);
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
//				String uri = HeadFileConfigure.getRequestUri(mApplication.getCurDid());
//				String filePath = FileManager.getSavePath(uri);
//				Bitmap bitmap = BitmapFactory.decodeFile(filePath);
//				if (bitmap != null){
//					Bitmap bitmap2 = BitmapUtils.getRoundedCornerBitmap(bitmap);
//					if (bitmap2 != null){
//						mHeadImageView.setImageBitmap(bitmap2);
//					}else{
//						mHeadImageView.setImageBitmap(bitmap);
//					}
//
//					mHeadImageView.setImageBitmap(bitmap);
//					loadHead = true;
//				}else{
//					log.e("can't find the bitmap from filePath:" + filePath);
//				}
			}	
			break;
		}
	
		
	}

	
}
