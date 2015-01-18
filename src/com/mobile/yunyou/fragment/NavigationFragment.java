package com.mobile.yunyou.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mobile.yunyou.R;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.activity.MainSlideActivity;
import com.mobile.yunyou.bike.SafeActivity;
import com.mobile.yunyou.bike.tmp.NewBikeExActivity;
import com.mobile.yunyou.model.GloalType;
import com.mobile.yunyou.msg.MessageActivity;
import com.mobile.yunyou.network.api.HeadFileConfigure;
import com.mobile.yunyou.set.SetPersonActivity;
import com.mobile.yunyou.set.SettingExActivity;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.FileManager;
import com.mobile.yunyou.util.LogFactory;

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
	
	  private ImageView mHeadImageView;
	
	  private YunyouApplication mApplication;
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
		
		setupViews();
	}
	

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		updateHead();
	}

	private void setupViews(){
		
		mHeadImageView =  (ImageView) mView.findViewById(R.id.iv_head);
		mLLGoLocation = (LinearLayout) mView.findViewById(R.id.ll_goLocation);
		mLLGoSafe = (LinearLayout) mView.findViewById(R.id.ll_goSafe);
		mLLGoNewRun = (LinearLayout) mView.findViewById(R.id.ll_goNewRun);
		mLLGoRunRecord = (LinearLayout) mView.findViewById(R.id.ll_goRunRecord);
		//mLLGoWarning = (LinearLayout) mView.findViewById(R.id.ll_goWarning);
		mLLGoInfo = (LinearLayout) mView.findViewById(R.id.ll_goGoInfo);
		mLLGoSetting = (LinearLayout) mView.findViewById(R.id.ll_goSetting);
		
		mHeadImageView.setOnClickListener(this);
		mLLGoLocation.setOnClickListener(this);
		mLLGoSafe.setOnClickListener(this);
		mLLGoNewRun.setOnClickListener(this);
		mLLGoRunRecord.setOnClickListener(this);
		//mLLGoWarning.setOnClickListener(this);
		mLLGoInfo.setOnClickListener(this);
		mLLGoSetting.setOnClickListener(this);
		
		mApplication = YunyouApplication.getInstance();
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
		case R.id.iv_head:
			goPersion();
			break;
		default:
			break;
		}
	}

	
	private void goLocation(){
		FragmentActivity activity = getActivity();
		if (activity instanceof MainSlideActivity){
			MainSlideActivity mainSlideActivity = (MainSlideActivity) activity;
			mainSlideActivity.toggle();
			mainSlideActivity.searchBike();
		}
	}
	
	private void goNewRun(){
		Intent intent = new Intent();
		intent.setClass(getActivity(), NewBikeExActivity.class);
		startActivity(intent);
	}
	
	private void goRunRecord(){
		FragmentActivity activity = getActivity();
		if (activity instanceof MainSlideActivity){
			MainSlideActivity mainSlideActivity = (MainSlideActivity) activity;
			mainSlideActivity.toggle();
			mainSlideActivity.goRunRecord();
		}
	}
	
	private void goSafe(){
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
		intent.setClass(getActivity(), MessageActivity.class);
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
	
	  private boolean loadHead = false;
	public void updateHead(){
		
		GloalType.UserInfoEx userInfoEx = mApplication.getUserInfoEx();
		int type = userInfoEx.mType;
		
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
				String uri = HeadFileConfigure.getRequestUri(mApplication.getCurDid());
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
		}
	
		
	}

	
}
