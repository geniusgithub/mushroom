package com.mobile.yunyou.recycle;
//package com.mobile.yunyou.activity;
//
//
//import android.app.Dialog;
//import android.app.TabActivity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.RadioGroup.OnCheckedChangeListener;
//import android.widget.TabHost;
//import android.widget.TabHost.TabSpec;
//import android.widget.TextView;
//
//import com.mobile.yunyou.R;
//import com.mobile.yunyou.YunyouApplication;
//import com.mobile.yunyou.network.NetworkCenterEx;
//import com.mobile.yunyou.util.CommonLog;
//import com.mobile.yunyou.util.DialogFactory;
//import com.mobile.yunyou.util.LogFactory;
//
//public class MainActivity extends TabActivity{
//
//	private static final CommonLog log = LogFactory.createLog();
//	
//	
//	private TabHost		m_tabHost;		
//	private RadioGroup  m_radioGroup;
//	private RadioButton mMsgRadioButton;
//	private NetworkCenterEx mNetworkCenter;
//	private YunyouApplication mApplication;
//	
//
//
//	
//	private TextView mTVUnreadMsgCount;
//	
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main_tab);
//        
//		 mApplication = YunyouApplication.getInstance();
//		 if (mApplication.getLoginState() == false)
//		 {
//			
//			 goLoginActivity();
//			 finish();
//		 }
//        
//	
//		 
//        init();
//	   	 boolean ret = getIntent().getBooleanExtra(YunyouApplication.KEY_NOTIFY, false);
//	   	 {
//	   		 if (ret)
//	   		 {
//	   			mMsgRadioButton.setChecked(true); 
//	   		 }
//	   	 }
//	   	 
//    }
//
//    
//    
//	
//	
//    @Override
//	protected void onNewIntent(Intent intent) {
//		// TODO Auto-generated method stub
//		super.onNewIntent(intent);
//		
//		log.e("onNewIntent");
//		
//		boolean ret = intent.getBooleanExtra(YunyouApplication.KEY_NOTIFY, false);
//		if (ret)
//		{
//			mMsgRadioButton.setChecked(true);
//		}
//		
//		
//	}
//
//
//
//
//
//	private void init()
//	{
//		m_tabHost = getTabHost();
//	
//		mTVUnreadMsgCount = (TextView) findViewById(R.id.tv_unreadmsg);
//		
//		int count = 4;		
//		for(int i = 0; i < count; i++)
//		{	
//			TabSpec tabSpec = m_tabHost.newTabSpec(Constant.mTextviewArray[i]).
//													setIndicator(Constant.mTextviewArray[i]).
//													setContent(getTabItemIntent(i));
//			m_tabHost.addTab(tabSpec);
//		}
//		
//		m_radioGroup = (RadioGroup) findViewById(R.id.main_radiogroup);
//		m_radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				// TODO Auto-generated method stub
//				switch(checkedId){
//				case R.id.main_tab_pos:
//					m_tabHost.setCurrentTabByTag(Constant.mTextviewArray[0]);
//					break;
//				case R.id.main_tab_friend:
//					m_tabHost.setCurrentTabByTag(Constant.mTextviewArray[1]);
//					break;
//				case R.id.main_tab_msg:
//					m_tabHost.setCurrentTabByTag(Constant.mTextviewArray[2]);
//					break;
//				case R.id.main_tab_settings:
//					m_tabHost.setCurrentTabByTag(Constant.mTextviewArray[3]);
//					break;
//				}
//			}
//		});
//		
//		mMsgRadioButton = (RadioButton) findViewById(R.id.main_tab_msg);
//		
//		 ((RadioButton) m_radioGroup.getChildAt(0)).toggle();
//		 
//
//		 mApplication.registerMsgCountTextView(mTVUnreadMsgCount);
//		 mApplication.startYunyouService();
//		
//		 
//		 mNetworkCenter = NetworkCenterEx.getInstance();	
//		 mApplication.startRequest();
//		 
//		
//		
//	}
//	
//	private Intent getTabItemIntent(int index)
//	{
//		Intent intent = new Intent(this, Constant.mTabClassArray[index]);	
//		return intent;
//	}
//
//
//
//	@Override
//	protected void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//
//		
//		
//		mApplication.unRegisterMsgCountTextView();
//	}
//	
//	private void goLoginActivity()
//	{
//		Intent intent = new Intent();
//		intent.setClass(this, LoginActivity.class);
//		
//		startActivity(intent);
//	}
//	
//	
//	private Dialog mDialog = null;
//	private void showExitDialog(boolean bShow)
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
//				// TODO Auto-generated method stub
//				
//				finish();
//				
//				mApplication.exit();
//			}
//		};
//
//		if (bShow)
//		{
//			mDialog = DialogFactory.creatDoubleDialog(this, R.string.dialog_title_exit, R.string.dialog_msg_exit, onClickListener);
//			mDialog.show();
//		}
//	
//	}
//
//
//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//		// TODO Auto-generated method stub
//		
////		
////		if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK)
////		{
////			showExitDialog(true);
////			return true;
////		}
//		
//		
//		
//		return super.dispatchKeyEvent(event);
//	}
//	
//	
//	
//	
//}
