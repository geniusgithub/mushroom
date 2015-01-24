package com.mobile.yunyou.set;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.mobile.yunyou.bike.MoGuActivity;
import com.mobile.yunyou.bike.WarningActivity;
import com.mobile.yunyou.bike.tmp.OfflineMapActivity;
import com.mobile.yunyou.model.GloalType;
import com.mobile.yunyou.model.GloalType.UserInfoEx;
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

public class SettingExActivity extends Activity implements OnClickListener, IRequestCallback{

	  private static final CommonLog log = LogFactory.createLog();
	
	  
	  private View mRootView;
	  
	  private View mGoOfflineMapView;
	  private View mGoWarningView;
	  private View mGoMoGuView;

	  private View mGoVersionView;
	  private View mGoAboutView;
	  private View mGoHelpView;
	  private View mExit;
	  
	  private Button mBtnBack;
	  
	  private ImageView mHeadImageView;
	  private boolean loadHead = false;
	  private TextView mTVAccount;
	  
	  private YunyouApplication mApplication;
	  private NetworkCenterEx mNetworkCenterEx;
		
		
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
		Utils.showToast(this, "goVersionActivity");
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
				
				mApplication.exit();
			}
		};

		if (bShow)
		{
			mDialog = DialogFactory.creatDoubleDialog(this, R.string.dialog_title_exit, R.string.dialog_msg_exit, onClickListener);
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
		
//		switch(requestAction)
//		{
//		case PublicType.USER_GET_INFO_MASID:
//			onGetUserInfoResult(dataPacket);
//			break;
//		case ProductType.PRODUCT_GET_PACKET_MASID:
//			onGetMealResult(dataPacket);
//			break;
//		}
		
		return true;
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
