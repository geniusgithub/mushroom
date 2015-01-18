package com.mobile.yunyou.activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.mobile.yunyou.R;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.bike.RunLRecordActivity;
import com.mobile.yunyou.fragment.MainMapFragment;
import com.mobile.yunyou.fragment.NavigationFragment;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;



public class MainSlideActivity extends SlidingFragmentActivity implements OnClickListener{

	private static final CommonLog log = LogFactory.createLog();
	private static final int MSG_REQUEST_RECORD = 0x0001;
	
	private String mTitle;
	private Fragment mContent;
	
	private MainMapFragment mMapExFragment;
	
	private ImageView mLeftIcon;
	private TextView mTitleTextView;
	
	private YunyouApplication mApplication;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	
		
		setContentView(R.layout.main_slidemenu_layout);
		
		setupViews();
		
		initData();
	}
	
	
	
	private void setupViews(){

	
		
		initActionBar();
		
		initSlideMenu();
		
	
	}
	
	private void initSlideMenu(){
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);


		setBehindContentView(R.layout.left_menu_frame);
		sm.setSlidingEnabled(true);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.left_menu_frame, new NavigationFragment())
		.commit();
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setBehindScrollScale(0);
		sm.setFadeEnabled(true);
		sm.setFadeDegree(0.45f);
		


	}
	
	private void initActionBar(){
		ActionBar actionBar = getSupportActionBar();
		actionBar.setCustomView(R.layout.actionbar_layout);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//		actionBar.setDisplayShowCustomEnabled(true);
//		actionBar.setDisplayHomeAsUpEnabled(false);
//		actionBar.setDisplayUseLogoEnabled(false);
		
		mLeftIcon = (ImageView) findViewById(R.id.iv_left_icon);
		mLeftIcon.setOnClickListener(this);
		
		mTitleTextView = (TextView) findViewById(R.id.tv_title);
	}
	
	private void initData(){
		mMapExFragment = MainMapFragment.newInstance();
		switchContent(mMapExFragment);
		
		mApplication = YunyouApplication.getInstance();
		 mApplication.startYunyouService();	
		 mApplication.startRequest();
		 
//		 if (mApplication.isBindDevice()){
//			 Utils.showToast(this, "您已绑定蘑菇伴侣");
//		 }else{
//			 Utils.showToast(this, "您未绑定蘑菇伴侣");
//		 }
	}
	
	
	public void searchBike(){
		mMapExFragment.searchBike();
	}
	
	public void goRunRecord(){
		Intent intent = new Intent();
		intent.setClass(this, RunLRecordActivity.class);
		startActivityForResult(intent, MSG_REQUEST_RECORD);
	}
	
	public void switchContent(final Fragment fragment) {

		mContent = fragment;

		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.content_frame, mContent).commit();
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				getSlidingMenu().showContent();
			}
		}, 50);
	//	mTitleTextView.setText(mTitle);
	}



	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.iv_left_icon:
			toggle();
			break;
		}
	}	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MSG_REQUEST_RECORD){
			if (resultCode == RESULT_OK){
				mMapExFragment.showBikeRecord();
			}
		}
	}
	
	
	
	
	
}

