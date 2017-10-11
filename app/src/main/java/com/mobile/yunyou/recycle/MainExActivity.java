package com.mobile.yunyou.recycle;
//package com.mobile.yunyou.activity;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.mobile.yunyou.R;
//import com.mobile.yunyou.YunyouApplication;
//import com.mobile.yunyou.bike.FollowBikeActivity;
//import com.mobile.yunyou.bike.MoGuActivity;
//import com.mobile.yunyou.bike.RunLRecordActivity;
//import com.mobile.yunyou.bike.SafeActivity;
//import com.mobile.yunyou.bike.tmp.NewBikeExActivity;
//import com.mobile.yunyou.map.util.LocationUtil;
//import com.mobile.yunyou.model.GloalType;
//import com.mobile.yunyou.msg.MessageExActivity;
//import com.mobile.yunyou.network.api.HeadFileConfigure;
//import com.mobile.yunyou.set.SetPersonActivity;
//import com.mobile.yunyou.set.SettingExActivity;
//import com.mobile.yunyou.util.CommonLog;
//import com.mobile.yunyou.util.DialogFactory;
//import com.mobile.yunyou.util.FileManager;
//import com.mobile.yunyou.util.LogFactory;
//import com.mobile.yunyou.util.Utils;
//
//public class MainExActivity extends Activity implements OnClickListener{
//
//	private static final CommonLog log = LogFactory.createLog();
//
//	private View mView;
//	
//	private LinearLayout mLLGoLocation;
//	private LinearLayout mLLGoSafe;
//	private LinearLayout mLLGoNewRun;
//	private LinearLayout mLLGoRunRecord;
//	private LinearLayout mLLGoInfo;
//	private LinearLayout mLLGoSetting;
//	
//	  private ImageView mHeadImageView;
//	  private TextView mTVNickName;
//	  private TextView mTVDistance;
//	
//	  private YunyouApplication mApplication;
//
//	
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		
//		log.e("MainExActivity onCreate");
//		setContentView(R.layout.navitation_channel_layout);
//		
//		setupViews();
//		
//
//	}
//
//
//	@Override
//	public void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//		
//		log.e("MainExActivity onDestroy");
//	}
//	
//	
//
//	@Override
//	public void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		
//		updateHead();
//		
//		if (LocationUtil.isGPSEnable(this) == false)
//		{
//			showGPSDialog(true);
//			return ;
//		}
//		
//		if (!mApplication.getBindFlag()){
//			showBindDialog(true);
//		}
//	}
//
//	private void setupViews(){
//		
//		mHeadImageView =  (ImageView) findViewById(R.id.iv_head);
//		mLLGoLocation = (LinearLayout) findViewById(R.id.ll_goLocation);
//		mLLGoSafe = (LinearLayout) findViewById(R.id.ll_goSafe);
//		mLLGoNewRun = (LinearLayout)findViewById(R.id.ll_goNewRun);
//		mLLGoRunRecord = (LinearLayout) findViewById(R.id.ll_goRunRecord);
//		mLLGoInfo = (LinearLayout)findViewById(R.id.ll_goGoInfo);
//		mLLGoSetting = (LinearLayout) findViewById(R.id.ll_goSetting);
//		
//		mTVNickName = (TextView) findViewById(R.id.tv_nickname);
//		mTVDistance = (TextView) findViewById(R.id.tv_distance);
//		
//		mHeadImageView.setOnClickListener(this);
//		mLLGoLocation.setOnClickListener(this);
//		mLLGoSafe.setOnClickListener(this);
//		mLLGoNewRun.setOnClickListener(this);
//		mLLGoRunRecord.setOnClickListener(this);
//		mLLGoInfo.setOnClickListener(this);
//		mLLGoSetting.setOnClickListener(this);
//		
//	
//		
//		mApplication = YunyouApplication.getInstance();
//		mTVNickName.setText(mApplication.getUserInfoEx().mTrueName);
//	}
//
//	private Dialog mDialog = null;
//	private void showGPSDialog(boolean bShow)
//	{
//		if (mDialog != null)
//		{
//			mDialog.dismiss();
//			mDialog = null;
//		}
//		
//		OnClickListener onClickListener = new OnClickListener() {
//			
//			@Override
//			public void onClick(View view) {			
//				goSetGpsPage();
//			}
//		};
//
//		if (bShow)
//		{
//			mDialog = DialogFactory.creatDoubleDialog(this, R.string.dialog_title_gogps, R.string.dialog_msg_gogps,
//																R.string.btn_yes, R.string.btn_no, onClickListener);
//			mDialog.show();
//		}
//	
//	}
//	private void showBindDialog(boolean bShow)
//	{
//		if (mDialog != null)
//		{
//			mDialog.dismiss();
//			mDialog = null;
//		}
//		
//		DialogFactory.ISelectComplete onClickListener = new DialogFactory.ISelectComplete() {
//
//			@Override
//			public void onSelectComplete(boolean flag) {
//				if (flag){
//					Intent intent = new Intent();
//					intent.setClass(MainExActivity.this, MoGuActivity.class);
//					startActivity(intent);
//				}else{
//					mApplication.setBindFlag(true);
//				}
//			}
//			
//		
//		};
//
//		if (bShow)
//		{
//			mDialog = DialogFactory.creatSelectDialog(this, R.string.dialog_title_bind, R.string.dialog_msg_bind,
//																R.string.btn_bind, R.string.btn_ignore, onClickListener);
//			mDialog.show();
//		}
//	
//	}
//	
//	private void goSetGpsPage(){
//		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//		startActivity(intent);
//	}
//	
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.ll_goLocation:
//			goLocation();
//			break;
//		case R.id.ll_goSafe:
//			goSafe();
//			break;		
//		case R.id.ll_goNewRun:
//			goNewRun();	
//			break;
//		case R.id.ll_goRunRecord:
//			goRunRecord();
//			break;
//		case R.id.ll_goGoInfo:
//			goInfo();
//			break;
//		case R.id.ll_goSetting:
//			goSetting();
//			break;
//		case R.id.iv_head:
//			goPersion();
//			break;
//		default:
//			break;
//		}
//	}
//
//	public void goFollowBike(){
//		Intent intent = new Intent();
//		intent.setClass(this, FollowBikeActivity.class);
//		startActivity(intent);
//	}
//	
//	
//	private void goLocation(){
//		
//		if (mApplication.isBindDevice()){
//			goFollowBike();
//		}else{
//			Utils.showToast(this, R.string.toask_unbind_bike);
//		}
//
//	}
//	
//	private void goNewRun(){
//		Intent intent = new Intent();
//		intent.setClass(this, NewBikeExActivity.class);
//		startActivity(intent);
//	}
//	
//	public void goRunRecord(){
//		Intent intent = new Intent();
//		intent.setClass(this, RunLRecordActivity.class);
//		startActivity(intent);
//	}
//	
//	private void goSafe(){
//		Intent intent = new Intent();
//		intent.setClass(this, SafeActivity.class);
//		startActivity(intent);
//	}
//	
//
//	
//	private void goInfo(){
//		Intent intent = new Intent();
//		intent.setClass(this, MessageExActivity.class);
//		startActivity(intent);
//	}
//	
//	private void goSetting(){
//		Intent intent = new Intent();
//		intent.setClass(this, SettingExActivity.class);
//		startActivity(intent);
//	}
//	
//	private void goPersion(){
//		Intent intent = new Intent();
//    	intent.setClass(this, SetPersonActivity.class);
//    	startActivity(intent);
//	}
//	
//	  private boolean loadHead = false;
//	public void updateHead(){
//		
//		GloalType.UserInfoEx userInfoEx = mApplication.getUserInfoEx();
//		int type = userInfoEx.mType;
//		
//		switch(type){
//		case 0:
//			if (!loadHead){
//				String uri = HeadFileConfigure.getAccountUri(mApplication.getUserInfoEx().mSid);
//				String filePath = FileManager.getSavePath(uri);
//				Bitmap bitmap = BitmapFactory.decodeFile(filePath);
//				if (bitmap != null){
//					mHeadImageView.setImageBitmap(bitmap);
//					loadHead = true;
//				}else{
//					log.e("can't find the bitmap from filePath:" + filePath);
//				}
//			}
//			break;
//		case 1:
//			if (!loadHead){
//				String uri = HeadFileConfigure.getRequestUri(mApplication.getCurDid());
//				String filePath = FileManager.getSavePath(uri);
//				Bitmap bitmap = BitmapFactory.decodeFile(filePath);
//				if (bitmap != null){
//					mHeadImageView.setImageBitmap(bitmap);
//					loadHead = true;
//				}else{
//					log.e("can't find the bitmap from filePath:" + filePath);
//				}
//			}	
//			break;
//		}
//	
//		
//	}
//}
