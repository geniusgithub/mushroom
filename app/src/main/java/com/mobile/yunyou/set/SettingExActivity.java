package com.mobile.yunyou.set;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mobile.yunyou.R;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.activity.BaseActivity;
import com.mobile.yunyou.activity.LoginActivity;
import com.mobile.yunyou.bike.MoGuActivity;
import com.mobile.yunyou.bike.WarningActivity;
import com.mobile.yunyou.bike.tmp.OfflineMapActivity;
import com.mobile.yunyou.model.GloalType;
import com.mobile.yunyou.model.GloalType.UserInfoEx;
import com.mobile.yunyou.model.PublicType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.network.api.HeadFileConfigure;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.DialogFactory;
import com.mobile.yunyou.util.FileManager;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.PopWindowFactory;
import com.mobile.yunyou.util.Utils;

public class SettingExActivity extends BaseActivity implements OnClickListener, IRequestCallback{

	  private static final CommonLog log = LogFactory.createLog();
	
	  
	  private View mRootView;
	  
	  private View mGoOfflineMapView;
	  private View mGoWarningView;
	  private View mGoMoGuView;
	  private View mChangePWDView;
	  
	  private View mGoVersionView;
	  private View mGoAboutView;
	  private View mGoHelpView;
	  private View mLogOffView;
	  private View mExit;
	 
	  private ImageView mIVUpageIcon;
	  
	  private Button mBtnBack;
	  
	  private ImageView mHeadImageView;
	  private boolean loadHead = false;
	  private TextView mTVAccount;
	  
	  private YunyouApplication mApplication;
	  private NetworkCenterEx mNetworkCenterEx;
		
	  private boolean isFristQueryVersion = true;
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.setex_layout);
	        
	        initView();
	        
	        initData();
	    }
	  
	  

 
	  @Override
	protected void onResume() {
		super.onResume();
		
	//	updateHead();
	}




	public void initView()
	  {
		  mRootView = findViewById(R.id.rootView);


		  mGoVersionView = findViewById(R.id.ll_goVersion);
		  mGoAboutView = findViewById(R.id.ll_goabout);
		  mGoHelpView = findViewById(R.id.ll_goHelp);
		  mGoHelpView.setVisibility(View.GONE);
		  mGoOfflineMapView = findViewById(R.id.ll_goOfflineMap);
		  mGoWarningView = findViewById(R.id.ll_goWarn);
		  mGoMoGuView = findViewById(R.id.ll_goMoGu);
		  mChangePWDView = findViewById(R.id.ll_changePwd);
		  mChangePWDView.setOnClickListener(this);
		  mLogOffView = findViewById(R.id.ll_logOff);
		  mLogOffView.setOnClickListener(this);
		  mLogOffView.setVisibility(View.GONE);
		  mIVUpageIcon = (ImageView) findViewById(R.id.iv_updateicon);
		  mHeadImageView = (ImageView) findViewById(R.id.iv_account_head);
		  mBtnBack = (Button) findViewById(R.id.btn_back);
		  mBtnBack.setOnClickListener(this);

		  
		  mGoWarningView.setOnClickListener(this);
		  mGoMoGuView.setOnClickListener(this);
		  mGoVersionView.setOnClickListener(this);
		  mGoAboutView.setOnClickListener(this);
		  mGoHelpView.setOnClickListener(this);
		  mGoOfflineMapView.setOnClickListener(this);
		  
		  mExit = findViewById(R.id.ll_exit);
		  mExit.setOnClickListener(this);
		  
		  mTVAccount = (TextView) findViewById(R.id.tv_account);
	  }

	  public void initData()
	  {
		  mApplication = YunyouApplication.getInstance();
		  mNetworkCenterEx = NetworkCenterEx.getInstance();
		  
		  UserInfoEx infoEx = mApplication.getUserInfoEx();
		  
		  if (mApplication.getVersionFlag()){
	    		showUpdateIcon(true);
	    	}else{
	    		showUpdateIcon(false);
	    		goVersionActivity();
	    	}
	  }

	  
	  private void showUpdateIcon(boolean flag){
	    	if (flag){
	    		mIVUpageIcon.setVisibility(View.VISIBLE);
	    	}else{
	    		mIVUpageIcon.setVisibility(View.GONE);
	    	}
	    }
	  
	  
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch(v.getId())
		{
		case R.id.ll_goOfflineMap:
			goOfflineMapActivity();
			break;
		case R.id.ll_goWarn:
				goWarnActivity();
				break;
		case R.id.ll_goMoGu:
			goMoGoActivity();
			break;
		case R.id.ll_goVersion:
			goVersionActivity();
			break;
		case R.id.ll_goabout:
			goAboutActivity();
			break;
		case R.id.ll_goHelp:
			goHelpActivity();
			break;
		case R.id.ll_changePwd:
			changepwd();
			break;
		case R.id.ll_logOff:
			showLogOffDialog(true);
			break;
//		case R.id.ll_gorecharge:
//			goRechargeActivity();
//			break;
//		case R.id.ll_gobalance:
//			goQueryBalanceActivity();
//			break;

		case R.id.ll_exit:
			showExitDialog(true);
			break;
		case R.id.btn_back:
			finish();
			break;
		default:
			break;
		}
	}
	
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
	
	private void changepwd(){
		Intent intent = new Intent();
    	intent.setClass(this, ChangePwdActivity.class);
    	startActivity(intent);
	}
	
	private void logOff(){
		log.e("logOff");
		Intent intent = new Intent();
    	intent.setClass(this, LoginActivity.class);
    	startActivity(intent);
		YunyouApplication.getInstance().finishMainActivity();
		finish();


	}
	
	public void goAboutActivity()
	{
		Intent intent = new Intent();
    	intent.setClass(this, AboutActivity.class);
    	startActivity(intent);
	}
	
//	public void goPasswordActivity()
//	{
//		Intent intent = new Intent();
//    	intent.setClass(this, ChangePwdActivity.class);
//    	startActivity(intent);
//	}
//	
//	public void goPersonActivity()
//	{
//		Intent intent = new Intent();
//    	intent.setClass(this, SetPersonActivity.class);
//    	startActivity(intent);
//	}
	
	public void goWarnActivity()
	{
		Intent intent = new Intent();
		intent.setClass(this, WarningActivity.class);
		startActivity(intent);
	}
	
	
	public void goMoGoActivity()
	{
		Intent intent = new Intent();
		intent.setClass(this, MoGuActivity.class);
		startActivity(intent);
	}
	
	public void goVersionActivity()
	{
		if (mApplication.getVersionFlag()){
			showVersionDialog(true);
		}else{
			mNetworkCenterEx.StartRequestToServer(PublicType.BIKE_CHECKUPGRADE_MASID, null, this);	
		}

	}
	
	public void goHelpActivity()
	{
		Utils.showToast(this, "goHelpActivity");
	}
	
//	public void goRechargeActivity()
//	{
//		Intent intent = new Intent();
//    	intent.setClass(this, RechargeActivity.class);
//    	startActivity(intent);
//	}
	
	public void goOfflineMapActivity()
	{
		Intent intent = new Intent();
    	intent.setClass(this, OfflineMapActivity.class);
    	startActivity(intent);
	}
//	
//	public void goQueryBalanceActivity()
//	{
//		Intent intent = new Intent();
//    	intent.setClass(this, SetQueryBalanceActivity.class);
//    	startActivity(intent);
//	}
//	
//	public void goQueryMealActivity()
//	{
//		Intent intent = new Intent();
//    	intent.setClass(this, QueryMealActivity.class);
//    	startActivity(intent);
//	}
	
	 public void goTestActivity()
	 {
//			Intent intent = new Intent();
//	    	intent.setClass(this, TestActivity.class);
//	    	startActivity(intent);
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
			if (mPopupWindow != null){
				mPopupWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);
			}
			
		}
	
	}
	
	private Dialog mDialog = null;
	private void showExitDialog(boolean bShow)
	{
		if (mDialog != null)
		{
			mDialog.dismiss();
			mDialog = null;
		}
		
		OnClickListener onClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				
				finish();
				YunyouApplication.getInstance().finishMainActivity();
				mApplication.exit();
			}
		};

		if (bShow)
		{
			mDialog = DialogFactory.creatDoubleDialog(this, R.string.dialog_title_exit, R.string.dialog_msg_exit, onClickListener);
			mDialog.show();
		}
	
	}
	
	private void showLogOffDialog(boolean bShow)
	{
		if (mDialog != null)
		{
			mDialog.dismiss();
			mDialog = null;
		}
		
		OnClickListener onClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				
				logOff();
			}
		};

		if (bShow)
		{
			mDialog = DialogFactory.creatDoubleDialog(this, R.string.dialog_title_logoff, R.string.dialog_msg_logoff, onClickListener);
			mDialog.show();
		}
	
	}
	
	
	private void showVersionDialog(boolean bShow)
	{
		if (mDialog != null)
		{
			mDialog.dismiss();
			mDialog = null;
		}
		
		DialogFactory.ISelectComplete listener = new DialogFactory.ISelectComplete() {

			@Override
			public void onSelectComplete(boolean flag) {
				if (flag){
					PublicType.BikeCheckUpgradeResult object = mApplication.getVersionObject();	
					if (object != null){
						Intent intents = new Intent(Intent.ACTION_VIEW);
						intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intents.setData(Uri.parse(object.mUrl));
						startActivity(intents);
						log.e("jump to url:" + object.mUrl);
					}
				}else{
					
				}
			}
			
		
		};
		
		if (bShow)
		{
			PublicType.BikeCheckUpgradeResult object = mApplication.getVersionObject();			
			String title = getResources().getString(R.string.dialog_title_version_update);
			String content = object.mContent != null ? object.mContent : "";
			mDialog = DialogFactory.creatSelectDialog(SettingExActivity.this, title, content,
					getResources().getString(R.string.btn_sure), getResources().getString(R.string.btn_cancel),  listener);
			mDialog.show();
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
		
		switch(requestAction)
		{
		case PublicType.BIKE_CHECKUPGRADE_MASID:
			onCheckUpdate(dataPacket);
			break;
//		case ProductType.PRODUCT_GET_PACKET_MASID:
//			onGetMealResult(dataPacket);
//			break;
		}
		
		return true;
	}
	
	public void onCheckUpdate(ResponseDataPacket dataPacket){
	
		if (dataPacket == null || dataPacket.rsp == 0)
		{
			if (isFristQueryVersion){
				
				isFristQueryVersion = false;
			}else{
				Utils.showToast(this, R.string.request_data_fail);
			}
			return ;
		}
		
		PublicType.BikeCheckUpgradeResult object = new PublicType.BikeCheckUpgradeResult();
		try {
			object.parseString(dataPacket.data.toString());

			log.e("BikeCheckUpgradeResult = " + object.getShowString());
			
			if (object.mNeedUpgrade == 0){
				if (isFristQueryVersion){
					isFristQueryVersion = false;
				}else{
					Utils.showToast(this, R.string.toask_no_new_version);	
				}

				mApplication.setVersionFlag(false);
				return ;
			}
			
			mApplication.setVersionFlag(true);
			mApplication.setVersionObject(object);
			showUpdateIcon(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Utils.showToast(this, R.string.analyze_data_fail);
		}
	}
	
//	private void onGetUserInfoResult(ResponseDataPacket dataPacket)
//	{
//		if (dataPacket == null || dataPacket.rsp == 0)
//		{
//			Utils.showToast(this, R.string.request_data_fail);
//			
//			return ;
//		}
//		
//		PublicType.UserChangeInfo info = new PublicType.UserChangeInfo();
//		try {
//			info.parseString(dataPacket.data.toString());
//			
//			GloalType.UserInfoEx userInfoEx = mApplication.getUserInfoEx();
//			userInfoEx.mTrueName = info.mTrueName;
//			userInfoEx.mPhone = info.mPhone;
//			userInfoEx.mBirthday = info.mBirthday;
//			userInfoEx.mEmail = info.mEmail;
//			userInfoEx.mAddr = info.mAddr;
//			userInfoEx.mSex = info.mSex;
//			
//			goPersonActivity();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			Utils.showToast(this, R.string.analyze_data_fail);
//		}
//		
//		
//	}
	
//	private void onGetMealResult(ResponseDataPacket dataPacket)
//	{
//		if (dataPacket == null || dataPacket.rsp == 0)
//		{
//			Utils.showToast(this, R.string.request_data_fail);
//			
//			return ;
//		}
//		
//		ProductType.GetPackageGroup group = new ProductType.GetPackageGroup();
//		try {
//			group.parseString(dataPacket.data.toString());
//			
//			List<GetPackage> list = group.mGetPacageList;
//			
//			log.e("PRODUCT_GET_PACKET_MASID success...size = " + group.mGetPacageList.size());
//			int size = list.size();
//			for(int i = 0; i < size; i++){
//				ProductType.GetPackage info = list.get(i);
//				log.e("i = " + i + "\n" + 
//						"id = " + info.mID + "\n" + 
//						"name = " + info.mName + "\n" + 
//					    "price = " + info.mPrice + "\n" + 
//						"mValidTime = " + info.mValidTime + "\n" + 
//						"mDesc = " + info.mDesc + "\n" + 
//						"mDetail = " + info.mDetail);
//			}
//			ProductMealManager.getInstance().setPackageList(list);
//			goQueryMealActivity();
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			log.e("PRODUCT_GET_PACKET_MASID fail...");
//			Utils.showToast(this, R.string.analyze_data_fail);
//		}		
//		
//	}
}
