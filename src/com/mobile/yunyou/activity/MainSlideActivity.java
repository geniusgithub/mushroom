package com.mobile.yunyou.activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.amap.api.location.AMapLocation;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.mobile.yunyou.R;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.bike.FollowBikeActivity;
import com.mobile.yunyou.bike.RunLRecordActivity;
import com.mobile.yunyou.bike.manager.SelfLocationManager;
import com.mobile.yunyou.bike.manager.SelfLocationManager.ILocationUpdate;
import com.mobile.yunyou.fragment.MainMapFragment;
import com.mobile.yunyou.fragment.NavigationFragment;
import com.mobile.yunyou.fragment.NewBikeFragment;
import com.mobile.yunyou.map.data.LocationEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.Utils;



public class MainSlideActivity extends SlidingFragmentActivity implements OnClickListener,
																		ILocationUpdate{

	private static final CommonLog log = LogFactory.createLog();
	private static final int MSG_REQUEST_RECORD = 0x0001;
//	
	private String mTitle;
	private Fragment mContent;
	
	private MainMapFragment mMapExFragment;
	private NewBikeFragment mBikeFragment;
	
	private ImageView mLeftIcon;
	private TextView mTitleTextView;
	
	private YunyouApplication mApplication;
	private SelfLocationManager mSelfLocationManager;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	
		
		setContentView(R.layout.main_slidemenu_layout);
		
		setupViews();
		
		initData();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		mSelfLocationManager.addObserver(this);
		mSelfLocationManager.startLocationCheck();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();

		mSelfLocationManager.removeObservser(this);
		mSelfLocationManager.stopLocationCheck();
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
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
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

		
		OnCloseListener listener = new OnCloseListener() {
			
			@Override
			public void onClose() {
				mBikeFragment.centerSelf();
			}
		};
		
		sm.setOnCloseListener(listener);

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
//		mMapExFragment = MainMapFragment.newInstance();		
//		switchContent(mMapExFragment);
		
		mBikeFragment = NewBikeFragment.newInstance();
		switchContent(mBikeFragment);
		
		mApplication = YunyouApplication.getInstance();
		 mApplication.startYunyouService();	
		 mApplication.startRequest();
		 
//		 if (mApplication.isBindDevice()){
//			 Utils.showToast(this, "您已绑定蘑菇伴侣");
//		 }else{
//			 Utils.showToast(this, "您未绑定蘑菇伴侣");
//		 }
		  mSelfLocationManager = SelfLocationManager.getInstance();
	}
	
	
//	public void searchBike(){
//		mMapExFragment.searchBike();
//	}
	
	public void goNewBike(){
		toggle();
	}
	
	public void goFollowBike(){
		Intent intent = new Intent();
		intent.setClass(this, FollowBikeActivity.class);
		startActivity(intent);
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
			closeCurFragment();
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
	
	
	
//	@Override
//	public void onBackPressed() {
//		
//		SlidingMenu sm = getSlidingMenu();
//		boolean isShow = sm.isMenuShowing();
//		log.e("isShow = " + isShow);
//		if (isShow){
//			finish();
//			return ;
//		}
//
//
//		boolean ret = mBikeFragment.onBackPressed();
//		if (!ret){
//			return ;
//		}
//
//		toggle();
//	}
	
	public void closeCurFragment(){

   		boolean ret = mBikeFragment.onBackPressed();
   		if (ret){
   			toggle();
   		}
   		
	}
	
	public void onFinish(){
		toggle();
	}
	
	@Override
    public boolean dispatchKeyEvent(KeyEvent event) { 
     

            if (event.getAction() == KeyEvent.ACTION_UP
                            && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                     log.e("MainSlideActivity back");
                     
                    SlidingMenu sm = getSlidingMenu();
             		boolean isShow = sm.isMenuShowing();
             		log.e("isShow = " + isShow);
             		if (isShow){
             			finish();
             		}else{
                 		boolean ret = mBikeFragment.onBackPressed();
                 		if (ret){
                 			toggle();
                 		}
             		}

                    return true; 
            }
            return super.dispatchKeyEvent(event);
    }

	private boolean isFircenter = true;
	@Override
	public void onLocationUpdate(final LocationEx location, AMapLocation aMapLocation) {
		log.e("onLocationUpdate (" + location.getLatitude() + "," + location.getLongitude() + ")");
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
	
				if (isFircenter){
					isFircenter = false;
					mBikeFragment.centerSelf();
				}
			}
		});
	}
	
}

