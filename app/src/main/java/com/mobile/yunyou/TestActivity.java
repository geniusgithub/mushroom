package com.mobile.yunyou;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.mobile.yunyou.bike.tmp.DataFactory;
import com.mobile.yunyou.datastore.RunRecordDBManager;
import com.mobile.yunyou.model.BikeType;
import com.mobile.yunyou.model.BikeType.MinRunRecord;
import com.mobile.yunyou.model.BikeType.RunRecordGroup;
import com.mobile.yunyou.model.DeviceSetType;
import com.mobile.yunyou.model.DeviceSetType.DeviceUnReadMsgCount;
import com.mobile.yunyou.model.GloalType;
import com.mobile.yunyou.model.ProductType;
import com.mobile.yunyou.model.ProductType.GetPackage;
import com.mobile.yunyou.model.PublicType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.network.api.AbstractTaskCallback;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.DialogFactory;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.UploadExUtil;
import com.mobile.yunyou.util.UploadExUtil.OnUploadProcessListener;
import com.mobile.yunyou.util.Utils;
import com.mobile.yunyou.util.notifactionUtils;
import com.mobile.yunyou.zxin.CaptureActivity;



public class TestActivity extends Activity implements OnClickListener, IRequestCallback,OnUploadProcessListener{

	 private static final CommonLog log = LogFactory.createLog();
	
	 private ImageView mImageView;
	 private ImageView mImageView2;
	 private Button btnInsert;
	 private Button btnDel;
	 private Button btnQuery;
	 
	 private RunRecordDBManager mRunRecordDBManager;
	 
	 private Button btnTest;
	 private Button btn1;
	 private Button btn2;		
	 private Button btn3;
	 private Button btn4;	
	 private Button btn5;
	 private Button btn6;
	 private Button btn7;
	 private Button btn8;
	 
	 private Button btn9;
	 private Button btn10;
	 private Button btn11;
	 private Button btn12;
	 private Button btn13;
	 private Button btn14;
	 private Button btn15;
	 private Button btn16;
	 private Button btn17;
	 private Button btn18;
	 private Button btn19;
	 private Button btn20;
	 private Button btn21;
	 private Button btn22;
	 private Button btn23;
	 private Button btn24;
	 private Button btn25;
	 private Button btn26;
	 private Button btn27;
	 private Button btn28;
	 private Button btn29;
	 private Button btn30;
	 private Button btn31;
	 private Button btn32;
	 private Button btn33;
	 private Button btn34;
	 private Button btn35;
	 private Button btn36;
	 private Button btn37;
	 private Button btn38;
	 private Button btn39;
	 private Button btn40;
	 private Button btn41;
	 private Button btn42;
	 private Button btn43;
	 private Button btn44;
	 private Button btn45;
	 private Button btn46;
	 private Button btn47;
	 private Button btn48;
	 private Button btn49;
	 private Button btn50;	 
	 private Button btn51;	
	 
	 
		private YunyouApplication mApplication;
		private NetworkCenterEx mNetworkCenter;

		private	ProgressDialog progressDialog;
		
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.test_layout);
	       
	        initView();
	        
	        mApplication = (YunyouApplication) getApplication();
	    	mNetworkCenter = mApplication.getNetworkCenter();

	    	mRunRecordDBManager = RunRecordDBManager.getInstance();
	    	
//	    	mNetworkCenter.setDid("A000000012000086");
//	    	mNetworkCenter.setSid("A128966000007152");
	    //	mNetworkCenter.setDid("A000000012000087");
		//	mNetworkCenter.setSid("A12null000007135");
			mNetworkCenter.setSid("A128966000007112");
			mNetworkCenter.setDid("A000000114002330");
	    	mNetworkCenter.initNetwork();
	    }
	    
	    
	    public void initView()
	    {
	    	mImageView = (ImageView) findViewById(R.id.iv_image);
	      	mImageView2 = (ImageView) findViewById(R.id.iv_image2);
	      	
	        progressDialog = new ProgressDialog(this);    
			 btnInsert = (Button) findViewById(R.id.btn_insert);
			 btnInsert.setOnClickListener(this);
			 btnDel = (Button) findViewById(R.id.btn_del);
			 btnDel.setOnClickListener(this);
			 btnQuery = (Button) findViewById(R.id.btn_query);
			 btnQuery.setOnClickListener(this);
	    	
			btnTest = (Button) findViewById(R.id.btn_test);
			btnTest.setOnClickListener(this);
			 
	    	btn1 = (Button) findViewById(R.id.button1);
	    	btn1.setOnClickListener(this);
	    	btn1.setVisibility(View.GONE);
	    	
	    	btn2 = (Button) findViewById(R.id.button2);
	    	btn2.setOnClickListener(this);
	    	btn2.setVisibility(View.GONE);
	    	
	    	btn3 = (Button) findViewById(R.id.button3);
	    	btn3.setOnClickListener(this);
	    	btn3.setVisibility(View.GONE);
	    	
	    	btn4 = (Button) findViewById(R.id.button4);
	    	btn4.setOnClickListener(this);
	    	btn4.setVisibility(View.GONE);
	    	
	    	btn5 = (Button) findViewById(R.id.button5);
	    	btn5.setOnClickListener(this);
	    	btn5.setVisibility(View.GONE);
	    	
	    	btn6 = (Button) findViewById(R.id.button6);
	    	btn6.setOnClickListener(this);
	    	btn6.setVisibility(View.GONE);
	    	
	    	btn7 = (Button) findViewById(R.id.button7);
	    	btn7.setOnClickListener(this);
	    	btn7.setVisibility(View.GONE);
	    	
	    	btn8 = (Button) findViewById(R.id.button8);
	    	btn8.setOnClickListener(this);
	    	btn8.setVisibility(View.GONE);
	    	
	    	btn9 = (Button) findViewById(R.id.button9);
	    	btn9.setOnClickListener(this);
	    	btn9.setVisibility(View.GONE);
	    	
	    	btn10 = (Button) findViewById(R.id.button10);
	    	btn10.setOnClickListener(this);
	    	btn10.setVisibility(View.GONE);
	    	
	    	btn11 = (Button) findViewById(R.id.button11);
	    	btn11.setOnClickListener(this);
	    	btn11.setVisibility(View.GONE);
	    	
	    	btn12 = (Button) findViewById(R.id.button12);
	    	btn12.setOnClickListener(this);
	    	btn12.setVisibility(View.GONE);
	    	
	    	btn13 = (Button) findViewById(R.id.button13);
	    	btn13.setOnClickListener(this);
	    	btn13.setVisibility(View.GONE);
	    	
	    	btn14 = (Button) findViewById(R.id.button14);
	    	btn14.setOnClickListener(this);
	    	btn14.setVisibility(View.GONE);
	    	
	    	btn15 = (Button) findViewById(R.id.button15);
	    	btn15.setOnClickListener(this);
	    	btn15.setVisibility(View.GONE);
	    	
	    	btn16 = (Button) findViewById(R.id.button16);
	    	btn16.setOnClickListener(this);
	    	btn16.setVisibility(View.GONE);
	    	
	    	
	    	btn17 = (Button) findViewById(R.id.button17);
	    	btn17.setOnClickListener(this);
	    	btn17.setVisibility(View.GONE);
	    	
	    	btn18 = (Button) findViewById(R.id.button18);
	    	btn18.setOnClickListener(this);
	    	btn18.setVisibility(View.GONE);
	    	
	    	btn19 = (Button) findViewById(R.id.button19);
	    	btn19.setOnClickListener(this);
	    	btn19.setVisibility(View.GONE);
	    	
	    	btn20 = (Button) findViewById(R.id.button20);
	    	btn20.setOnClickListener(this);
	    	btn20.setVisibility(View.GONE);
	    	
	    	btn21 = (Button) findViewById(R.id.button21);
	    	btn21.setOnClickListener(this);
	    	btn21.setVisibility(View.GONE);
	    	
	    	btn22 = (Button) findViewById(R.id.button22);
	    	btn22.setOnClickListener(this);
	    	btn22.setVisibility(View.GONE);
	    	
	    	btn23 = (Button) findViewById(R.id.button23);
	    	btn23.setOnClickListener(this);
	      	btn23.setVisibility(View.GONE);
	    	
	    	btn24 = (Button) findViewById(R.id.button24);
	    	btn24.setOnClickListener(this);
	      	btn24.setVisibility(View.GONE);
	      	
	    	btn25 = (Button) findViewById(R.id.button25);
	    	btn25.setOnClickListener(this);
	      	btn25.setVisibility(View.GONE);
	      	
	    	btn26 = (Button) findViewById(R.id.button26);
	    	btn26.setOnClickListener(this);
	      	btn26.setVisibility(View.GONE);
	      	
	    	btn27 = (Button) findViewById(R.id.button27);
	    	btn27.setOnClickListener(this);
	      	btn27.setVisibility(View.GONE);
	      	
	    	btn28 = (Button) findViewById(R.id.button28);
	    	btn28.setOnClickListener(this);
	      	btn28.setVisibility(View.GONE);
	      	
	    	btn29 = (Button) findViewById(R.id.button29);
	    	btn29.setOnClickListener(this);
	      	btn29.setVisibility(View.GONE);
	      	
	       	btn30 = (Button) findViewById(R.id.button30);
	    	btn30.setOnClickListener(this);
	      //	btn30.setVisibility(View.GONE);
	      	
	    	btn31 = (Button) findViewById(R.id.button31);
	    	btn31.setOnClickListener(this);
	      	btn31.setVisibility(View.GONE);
	      	
	    	btn32 = (Button) findViewById(R.id.button32);
	    	btn32.setOnClickListener(this);
	      	btn32.setVisibility(View.GONE);
	      	
	     	btn33 = (Button) findViewById(R.id.button33);
	    	btn33.setOnClickListener(this);
	      	btn33.setVisibility(View.GONE);
	    	
	    	btn34 = (Button) findViewById(R.id.button34);
	    	btn34.setOnClickListener(this);
	      	btn34.setVisibility(View.GONE);
	      	
	    	btn35 = (Button) findViewById(R.id.button35);
	    	btn35.setOnClickListener(this);
	      	btn35.setVisibility(View.GONE);
	      	
	    	btn36 = (Button) findViewById(R.id.button36);
	    	btn36.setOnClickListener(this);
	      	btn36.setVisibility(View.GONE);
	      	
	     	btn37 = (Button) findViewById(R.id.button37);
	    	btn37.setOnClickListener(this);
	      	btn37.setVisibility(View.GONE);
	      	
	    	btn38 = (Button) findViewById(R.id.button38);
	    	btn38.setOnClickListener(this);
	      	btn38.setVisibility(View.GONE);
	      	
	    	btn39 = (Button) findViewById(R.id.button39);
	    	btn39.setOnClickListener(this);
	      	btn39.setVisibility(View.GONE);
	      	
	      	btn40 = (Button) findViewById(R.id.button40);
	    	btn40.setOnClickListener(this);
	      	//btn40.setVisibility(View.GONE);
	      	
	    	btn41 = (Button) findViewById(R.id.button41);
	    	btn41.setOnClickListener(this);
	    	
	     	btn42 = (Button) findViewById(R.id.button42);
	    	btn42.setOnClickListener(this);
	    	
	    	btn43 = (Button) findViewById(R.id.button43);
	    	btn43.setOnClickListener(this);
	    	
	    	btn44 = (Button) findViewById(R.id.button44);
	    	btn44.setOnClickListener(this);
	    	
	    	btn45 = (Button) findViewById(R.id.button45);
	    	btn45.setOnClickListener(this);
	    	
	    	btn46 = (Button) findViewById(R.id.button46);
	    	btn46.setOnClickListener(this);
	    	
	    	btn47 = (Button) findViewById(R.id.button47);
	    	btn47.setOnClickListener(this);
	    	
	    	btn48 = (Button) findViewById(R.id.button48);
	    	btn48.setOnClickListener(this);
	    	
	    	btn49 = (Button) findViewById(R.id.button49);
	    	btn49.setOnClickListener(this);

	    	btn50 = (Button) findViewById(R.id.button50);
	    	btn50.setOnClickListener(this);
	    	
	    	btn51 = (Button) findViewById(R.id.button51);
	    	btn51.setOnClickListener(this);

	    }


		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			switch(view.getId())
			{
				case R.id.btn_test:
					test();
					break;
				case R.id.btn_insert:
					insert();
					break;
				case R.id.btn_del:
					del();
					break;
				case R.id.btn_query:
					query();
					break;
				case R.id.button1:
					doBtn1();
					break;
				case R.id.button2:
					doBtn2();
					break;
				case R.id.button3:
					doBtn3();
					break;
				case R.id.button4:
					doBtn4();
					break;
				case R.id.button5:
					doBtn5();
					break;
				case R.id.button6:
					doBtn6();
					break;
				case R.id.button7:
					doBtn7();
					break;
				case R.id.button8:
					doBtn8();
					break;
				case R.id.button9:
					doBtn9();
					break;
				case R.id.button10:
					doBtn10();
					break;
				case R.id.button11:
					doBtn11();
					break;
				case R.id.button12:
					doBtn12();
					break;
				case R.id.button13:
					doBtn13();
					break;
				case R.id.button14:
					doBtn14();
					break;
				case R.id.button15:
					doBtn15();
					break;
				case R.id.button16:
					doBtn16();
					break;
				case R.id.button17:
					doBtn17();
					break;
				case R.id.button18:
					doBtn18();;
					break;
				case R.id.button19:
					doBtn19();
					break;
				case R.id.button20:
					doBtn20();;
					break;
				case R.id.button21:
					doBtn21();;
					break;
				case R.id.button22:
					doBtn22();
					break;
				case R.id.button23:
					doBtn23();
					break;
				case R.id.button24:
					doBtn24();
					break;
				case R.id.button25:
					doBtn25();
					break;
				case R.id.button26:
					doBtn26();
					break;
				case R.id.button27:
					doBtn27();
					break;
				case R.id.button28:
					doBtn28();
					break;
				case R.id.button29:
					doBtn29();
					break;
				case R.id.button30:
					doBtn30();
					break;
				case R.id.button31:
					doBtn31();
					break;
				case R.id.button32:
					doBtn32();
					break;
				case R.id.button33:
					doBtn33();
					break;
				case R.id.button34:
					doBtn34();
					break;
				case R.id.button35:
					doBtn35();
					break;
				case R.id.button36:
					doBtn36();
					break;
				case R.id.button37:
					doBtn37();
					break;
				case R.id.button38:
					doBtn38();
					break;
				case R.id.button39:
					doBtn39();
					break;
				case R.id.button40:
					doBtn40();
					break;
				case R.id.button41:
					doBtn41();
					break;
				case R.id.button42:
					doBtn42();
					break;
				case R.id.button43:
					doBtn43();
					break;
				case R.id.button44:
					doBtn44();
					break;
				case R.id.button45:
					doBtn45();
					break;
				case R.id.button46:
					doBtn46();
					break;
				case R.id.button47:
					doBtn47();
					break;
				case R.id.button48:
					doBtn48();
					break;
				case R.id.button49:
					doBtn49();
					break;
				case R.id.button50:
					doBtn50();
					break;
				case R.id.button51:
					doBtn51();
					break;
			}
		}
		
		private void test(){
	
//			       Intent intent = new Intent(Intent.ACTION_PICK);  
//			       intent.setType("image/*");//相片类型   
//			       startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);  
			
//			showPhotoDialog(true);
			
			Intent intent = new Intent();
			intent.setClass(this, CaptureActivity.class);
			startActivity(intent);
			  
		}
		
		
		private void insert(){
			
			if (true){
				toUploadFile("/storage/emulated/0/upload.jpg");
				return ;
			}
			BikeType.MinRunRecord record1 = new BikeType.MinRunRecord();
			record1.mLat = 122.343435;
			record1.mLon = 20.343545;
			record1.mCreateTime = "2014-01-02 13:33:32";
			
			
			BikeType.MinRunRecord record2 = new BikeType.MinRunRecord();
			record2.mLat = 122.3435;
			record2.mLon = 20.545;
			record2.mCreateTime = "2014-01-02 13:33:42";
			
			RunRecordGroup group = new RunRecordGroup();
			group.mTotalDistance = 12334;
			group.mStartTime = "2014-01-02 13:33:32";
			group.mEndTime = "2014-01-02 14:30:32";
			group.mHSpeed = 12.345;
			group.mAverageSpeed = 5.323;
			group.mCal = 324;
			
			LinkedList<MinRunRecord> mRunRecordList = new LinkedList<MinRunRecord>();
			mRunRecordList.add(record1);
			mRunRecordList.add(record2);
			group.mRunRecordList = mRunRecordList;
			
//			log.e("insert = \n" + group.getShowString());
//			
//			boolean ret = false;
//			try {
//				ret = mRunRecordDBManager.insert(group);
//				
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			
//			log.e("insert ret = " + ret);
		}

		
		public void del(){

			if (true){
				toUploadFileEx("/storage/emulated/0/upload.jpg");
			}
			boolean ret = mRunRecordDBManager.deleteAll();
			
			log.e("deleteAll ret = " + ret);
		}
		
		
		public void query(){
//			boolean ret = false;
//			List<BikeType.RunRecordGroup> groupList = new ArrayList<BikeType.RunRecordGroup>();
//			try {
//				ret = mRunRecordDBManager.queryAll(groupList);
//			} catch (Exception e) {
//				e.printStackTrace();
//	
//			}
//			
//			int index = 0;
//			for(BikeType.RunRecordGroup group : groupList){
//				log.e("index = " + index + ":\n" + group.getShowString());
//				index++;
//			}
//			log.e("query = ret " + ret);
			
			
		}
		

		// 按键设置
		public void doBtn1()
		{
			DeviceSetType.KeySet keySet1 = new DeviceSetType.KeySet();
			keySet1.mKey = "1";
			keySet1.mPhoneNumber = "13010555666";
			keySet1.mName = "bo";
			
			DeviceSetType.KeySet keySet2 = new DeviceSetType.KeySet();
			keySet2.mKey = "2";
			keySet2.mPhoneNumber = "13010666777";
			keySet2.mName = "ki";
				
			List<DeviceSetType.KeySet> list = new ArrayList<DeviceSetType.KeySet>();
			list.add(keySet1);
			list.add(keySet2);
			
			
			DeviceSetType.KeySetGroup keySetGroup = new DeviceSetType.KeySetGroup();
			keySetGroup.mKeySetList = list;
			
			mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_KEYSET_MASID, keySetGroup, this);
			
		}
		public void doBtn2()
		{
			mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_GET_KEYSET_MASID, null, this);
		}
		
		// 通话时长设置
		public void doBtn3()
		{
			DeviceSetType.CallTime callTime = new DeviceSetType.CallTime();
			callTime.mTime = 3;
			
			mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_CALL_TIME_MASID, callTime, this);
		}
		
		public void doBtn4()
		{
			mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_GET_CALL_TIME_MASID, null, this);
		}
		
		
		// 白名单设置
		public void doBtn5()
		{
			DeviceSetType.WhiteListSet whiteListSet1 = new DeviceSetType.WhiteListSet();
			whiteListSet1.mName = "kira";
			whiteListSet1.mPhoneNumber  = "13510123456";
			
			DeviceSetType.WhiteListSet whiteListSet2 = new DeviceSetType.WhiteListSet();
			whiteListSet2.mName = "kira";
			whiteListSet2.mPhoneNumber  = "13510789456";
			
			List<DeviceSetType.WhiteListSet> list = new ArrayList<DeviceSetType.WhiteListSet>();
			list.add(whiteListSet1);
			list.add(whiteListSet2);
			
			DeviceSetType.WhiteListSetGroup whiteListSetGroup = new DeviceSetType.WhiteListSetGroup();
			whiteListSetGroup.mWhiteListSetList = list;
			
			mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_WHITELIST_SET_MASID, whiteListSetGroup, this);
			
		}
		
		public void doBtn6()
		{
			mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_GET_WHITELIST_MASID, null, this);
		}
		
		
		// 闹钟设置
		public void doBtn7()
		{
			
			DeviceSetType.ClockSet clockSet1 = new DeviceSetType.ClockSet();
			clockSet1.mTimeString = "1330";
			clockSet1.mCycle = 0;
			clockSet1.mWeekString = "1,3,4";
			clockSet1.mSwitch = 1;
				
			DeviceSetType.ClockSet clockSet2 = new DeviceSetType.ClockSet();
			clockSet2.mTimeString = "1530";
			clockSet2.mCycle = 0;
			clockSet2.mWeekString = "1,5";
			clockSet2.mSwitch = 0;
			
			List<DeviceSetType.ClockSet> clockSetList = new ArrayList<DeviceSetType.ClockSet>();
			clockSetList.add(clockSet1);
			clockSetList.add(clockSet2);
			
			DeviceSetType.ClockSetGroup clockSetGroup = new DeviceSetType.ClockSetGroup();
			clockSetGroup.mClockSetList = clockSetList;
			
			mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_CLOCK_SET_MASID, clockSetGroup, this);
		}
		
		public void doBtn8()
		{
			mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_GET_CLOCK_MASID, null, this);
		}
	
		// 情景模式
		public void doBtn9()
		{
			DeviceSetType.SceneMode sceneMode = new DeviceSetType.SceneMode();
			sceneMode.mModeString = "shake";
			
			mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_SCENEMODE_SET_MASID, sceneMode, this);
		}
		public void doBtn10()
		{
			mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_GET_SCENEMODE_MASID, null, this);
		}
		
		// 电源设置
		public void doBtn11()
		{
			DeviceSetType.PowerSet powerSet = new DeviceSetType.PowerSet();
			powerSet.mSceenString = "sleep";
			
			mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_POWER_SET_MASID, powerSet, this);
		}	
		public void doBtn12()
		{
			mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_GET_POWER_MASID, null, this);
		}
		
		
		
		// 低电量提醒
		public void doBtn13()
		{
			DeviceSetType.LowPowerWarn lowPowerWarn = new DeviceSetType.LowPowerWarn();
			lowPowerWarn.mPowerLevel = 1;
			
			mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_LOWPOWER_WARN_SET_MASID, lowPowerWarn, this);
		}
		public void doBtn14()
		{
			mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_GET_LOWPOWER_WARN_MASID, null, this);	
		}
		
		
		
		// GPS间隔
		public void doBtn15()
		{
			DeviceSetType.GpsInterval gpsInterval = new DeviceSetType.GpsInterval();
			gpsInterval.mGPSInterval = 10;
			
			mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_GPS_INTERVAL_SET_MASID, gpsInterval, this);
		}
		public void doBtn16()
		{
			mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_GET_GPS_INTERVAL_MASID, null, this);
		}
		
		// GPS上报时间
		public void doBtn17()
		{
			DeviceSetType.GpsStillTime gpsStillTime1 = new DeviceSetType.GpsStillTime();
			gpsStillTime1.mWeekString = "1,2,3";
			gpsStillTime1.mStartTimeString = "0830";
			gpsStillTime1.mEndTimeString = "1200";
			
			DeviceSetType.GpsStillTime gpsStillTime2 = new DeviceSetType.GpsStillTime();
			gpsStillTime2.mWeekString = "4";
			gpsStillTime2.mStartTimeString = "1430";
			gpsStillTime2.mEndTimeString = "1600";
			
			List<DeviceSetType.GpsStillTime> list = new ArrayList<DeviceSetType.GpsStillTime>();
			list.add(gpsStillTime1);
			list.add(gpsStillTime2);
			
			DeviceSetType.GpsStillTimeGroup gpsStillTimeGroup = new DeviceSetType.GpsStillTimeGroup();
			gpsStillTimeGroup.mGpsStillTimeList = list;
			
			mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_GPS_STILLTIME_SET_MASID, gpsStillTimeGroup, this);
		}
		
		public void doBtn18()
		{
			mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_GET_GPS_STILLTIME_MASID, null, this);
		}
		
		public void doBtn19()
		{
			DeviceSetType.RemoteMonitor monitor = new DeviceSetType.RemoteMonitor();
			monitor.mPhone = "13510527157";
			
			mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_REMOTE_MONITOR_MASID, monitor, this);
		}

		public void doBtn20()
		{
			PublicType.UserChangePwd userChangePwd = new PublicType.UserChangePwd();
//			userChangePwd.mOldPassword = mNetworkCenter.getPwd();
//			userChangePwd.mNewPassword = "111111";
			
			mNetworkCenter.StartRequestToServer(PublicType.USER_CHANGE_PASSWORD_MASID, userChangePwd, this);
		}
		
		public void doBtn21()
		{
			PublicType.UserChangeInfo userInfo = new PublicType.UserChangeInfo();
	
			userInfo.mTrueName = "junjin";
			userInfo.mPhone = "13067306371";
			userInfo.mBirthday = "1990-01-01";
			userInfo.mEmail = "1234567890@qq.com";
			userInfo.mAddr = "google";
			userInfo.mSex = "F";
			
			mNetworkCenter.StartRequestToServer(PublicType.USER_CHANGE_INFO_MASID, userInfo, this);
		}
		
		public void doBtn22()
		{
			
			mNetworkCenter.StartRequestToServer(PublicType.USER_GET_INFO_MASID, null, this);
		}
		
		public void doBtn23()
		{
			
			mNetworkCenter.StartRequestToServer(PublicType.USER_LOGOUIT_MASID, null, this);
		}

		public void doBtn24()
		{
			
			mNetworkCenter.StartRequestToServer(PublicType.USER_HEART_MASID, null, this);
		}

		public void doBtn25()
		{
			
			mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_GET_LOCATION_MASID, null, this);
		}
		
		public void doBtn26()
		{
			PublicType.UserBind userBind = new PublicType.UserBind();
			userBind.mUserName = "13923802301";
			userBind.mPassword = "802301";
			mNetworkCenter.StartRequestToServer(PublicType.USER_BIND_MASID, userBind, this);
		}
		
		public void doBtn27()
		{
			
			mNetworkCenter.StartRequestToServer(PublicType.USER_UNBIND_MASID, null, this);
		}
		
		public void doBtn28()
		{
			
			mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_GET_ONELINE_MASID, null, this);
		}
		
		public void doBtn29()
		{
			DeviceSetType.DeviceRequestMsg object = new DeviceSetType.DeviceRequestMsg();
			object.mOffset = 0;
			object.mNum = 10;
			mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_GET_MSG_MASID, object, this);
		}
	
		public void doBtn30()
		{
			
			mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_GET_UNREAD_MSG_MASID, null, this);
		}
		
		public void doBtn31()
		{
			
			mNetworkCenter.StartRequestToServer(ProductType.PRODUCT_GET_PACKET_MASID, null, this);
		}
		
		
		public void doBtn32()
		{

			ProductType.CreateOrder createOrderInfo = new ProductType.CreateOrder();
			createOrderInfo.mPackageId = 1;
			createOrderInfo.mPrice = 50;
			createOrderInfo.mTotalFee = 50;
			createOrderInfo.mDid = "2132324343";
			mNetworkCenter.StartRequestToServer(ProductType.PRODUCT_CREATE_ORDER_MASID, createOrderInfo, this);
		}
		
		public void doBtn33()
		{
			DeviceSetType.DeviceArea object = new DeviceSetType.DeviceArea();
			object.mType = "all";
			object.mOffset = 0;
			object.mNum = 10;

			mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_GET_AREA_MASID, object, this);
		}
		
		public void doBtn34()
		{
			DeviceSetType.DeviceHistory object = new DeviceSetType.DeviceHistory();
			object.mStartTime = "2012-10-24 00:00:00";
			object.mEndTime = "2012-10-24 17:50:00";

			mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_GET_DEVICE_HISTORY_MASID, object, this);
		}
		
		public void doBtn35()
		{
			HttpURLConnection urlConn = null;
			OutputStream out = null;
			InputStream in = null;
			
			try {
							URL url = new URL("http://www.360lbs.net:8080/");
				//				+"{\"data\":{\"endTime\":\"2012-10-24 23:50:00\",\"startTime\":\"2012-10-24 00:00:00\"},\"headers\":{\"ua\":{\"model\":\"HTC EVO 3D X515m\",\"manufacturer\":\"HTC\",\"client_platform\":\"ANDROID\",\"os_version\":\"2.3.4\",\"client_version\":\"1.0.1\"},\"lang\":\"zh_ch\"},\"did\":\"A000000012000086\",\"sid\":\"A128966000007112\",\"cmd\":\"locationget_history\"}");			
					System.out.println(url);
					urlConn = (HttpURLConnection) url.openConnection();		
					//urlConn.setReadTimeout(5000);
					urlConn.setConnectTimeout(5000);
					urlConn.setRequestMethod("POST");
					urlConn.setDoInput(true);
					urlConn.setDoOutput(true);
					urlConn.setUseCaches(false);
					out = urlConn.getOutputStream();
					out.write("json={\"data\":{\"endTime\":\"2012-10-25 20:00:00\",\"startTime\":\"2012-10-25 00:00:00\"},\"headers\":{\"ua\":{\"model\":\"HTC EVO 3D X515m\",\"manufacturer\":\"HTC\",\"client_platform\":\"ANDROID\",\"os_version\":\"2.3.4\",\"client_version\":\"1.0.1\"},\"lang\":\"zh_ch\"},\"did\":\"A000000012000086\",\"sid\":\"A128966000007112\",\"cmd\":\"locationget_history\"}".getBytes("utf-8"));
					out.flush();
					in = urlConn.getInputStream();
					int getContentLen = urlConn.getContentLength();
				//		Thread.sleep(10);
					//log.e("urlConn.getContentLength() = " + getContentLen);
				
					ByteArrayOutputStream bos = null;
					try {
						// 输出图像到客户端
						bos = new ByteArrayOutputStream();
						int i=-1;
						byte[] block = new byte[8192];
						while ((i = in.read(block)) != -1) {
							bos.write(block,0,i);
							block = new byte[8192];
						}
						byte[] bytes = bos.toByteArray();
						System.out.println(bytes.length);
						System.out.println(new String(bytes).trim());
					} finally {
						if (bos != null) {
							bos.close();
						}
					}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
	
		}
		
		public void doBtn36(){
			
			GloalType.DeviceInfoEx device = mApplication.getCurDevice();

			mNetworkCenter.requestHeadFileDown(device, new FileDownCallback());

		}
		
		public void doBtn37(){

		}
		
		public void doBtn38(){

		}
		
		
		public void doBtn39(){

		}
		
		public void doBtn40(){
			log.e("BIKE_GETALERTWAY begin...");
			
			mNetworkCenter.StartRequestToServer(BikeType.BIKE_GETALERTWAY, null, this);
		}
		public void doBtn41(){

			log.e("BIKE_STAR_MASITD begin...");
			mNetworkCenter.StartRequestToServer(BikeType.BIKE_STAR_MASITD, null, this);
		}
		
		public void doBtn42(){
			log.e("BIKE_PAUSE_MASITD begin...");
			mNetworkCenter.StartRequestToServer(BikeType.BIKE_PAUSE_MASITD, null, this);
		
	
		}
		
		public void doBtn43(){
			log.e("BIKE_STOP_MASITD begin...");
			mNetworkCenter.StartRequestToServer(BikeType.BIKE_STOP_MASITD, null, this);
			
		}
		
		public void doBtn44(){
			log.e("BIKE_STATUS_MASITD begin...");
			mNetworkCenter.StartRequestToServer(BikeType.BIKE_STATUS_MASITD, null, this);
		
	
		}
		
		public void doBtn45(){
			log.e("BIKE_LRECORD_MASITD begin...");
			BikeType.BikeLRecord object = new BikeType.BikeLRecord();
			object.mOffset = 0;
			object.mSinceID = -1;
			object.mNum = 10;
			
			mNetworkCenter.StartRequestToServer(BikeType.BIKE_LRECORD_MASITD, object, this);
		}
		
		public void doBtn46(){
			log.e("BIKE_LRECORDSUB_MASITD begin...");
			BikeType.BikeLRecordSub object = new BikeType.BikeLRecordSub();
			object.mID = 49;
			mNetworkCenter.StartRequestToServer(BikeType.BIKE_LRECORDSUB_MASITD, object, this);
			
		}
		
		public void doBtn47(){
			log.e("BIKE_SETAREA_MASITD begin...");
			BikeType.BikeSetArea object = new BikeType.BikeSetArea();
			object.mLat = 121.323;
			object.mLon = 12.43;
			object.mName = "testname";
			object.mRadius = 100;
			mNetworkCenter.StartRequestToServer(BikeType.BIKE_SETAREA_MASITD, object, this);
			 
		}
		
		public void doBtn48(){
			log.e("BIKE_DELAREA_MASITD begin...");
			mNetworkCenter.StartRequestToServer(BikeType.BIKE_DELAREA_MASITD, null, this);
		}
		
		public void doBtn49(){
			log.e("BIKE_SETALERTWAY begin...");
			BikeType.BikeAlertWay object = new BikeType.BikeAlertWay();
			object.mValue = "1,0,0,1";
			mNetworkCenter.StartRequestToServer(BikeType.BIKE_SETALERTWAY, object, this);
		}
		
		public void doBtn50(){
			log.e("BIKE_GETAREA_MASITD begin...");
			mNetworkCenter.StartRequestToServer(BikeType.BIKE_GETAREA_MASITD, null, this);
		}
		
		private int index = 1;
		public void doBtn51(){
			log.e("doBtn51 begin...");
			DeviceSetType.DeviceUnReadMsgCount object = new DeviceUnReadMsgCount();
			object.mAlert = "测试的筒子";
			object.mAlert += String.valueOf(index);
			index++;
			object.mBikeAlertWay.setData(false, true, false, false, "");
			notifactionUtils.resendWarning(this, object);

		}
		
		@Override
		public boolean onComplete(int requestAction,  ResponseDataPacket dataPacket) {
			// TODO Auto-generated method stub

			String jsString = "null";
			if (dataPacket != null)
			{
				jsString = dataPacket.toString();
			}
			
			
			log.e("requestAction = " + Utils.toHexString(requestAction) + "\nResponseDataPacket = \n" +jsString);
			switch(requestAction)
			{
				case DeviceSetType.DEVICE_GET_KEYSET_MASID:
				{
					DeviceSetType.KeySetGroup group = new DeviceSetType.KeySetGroup();
					try {
						group.parseString(dataPacket.data.toString());
						log.e("DEVICE_GET_KEYSET_MASID success...");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
				break;
				case DeviceSetType.DEVICE_GET_CALL_TIME_MASID:
				{
					DeviceSetType.CallTime callTime = new DeviceSetType.CallTime();
					try {
						callTime.parseString(dataPacket.data.toString());
						log.e("DEVICE_GET_CALL_TIME_MASID success...");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
				break;
				case DeviceSetType.DEVICE_GET_WHITELIST_MASID:
				{
					DeviceSetType.WhiteListSetGroup group = new DeviceSetType.WhiteListSetGroup();
					try {
						group.parseString(dataPacket.data.toString());
						log.e("DEVICE_GET_WHITELIST_MASID success...");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
				break;
				case DeviceSetType.DEVICE_GET_CLOCK_MASID:
				{
					DeviceSetType.ClockSetGroup group = new DeviceSetType.ClockSetGroup();
					try {
						group.parseString(dataPacket.data.toString());
						log.e("DEVICE_GET_CLOCK_MASID success...");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
				break;
				case DeviceSetType.DEVICE_GET_SCENEMODE_MASID:
				{
					DeviceSetType.SceneMode sceen = new DeviceSetType.SceneMode();
					try {
						sceen.parseString(dataPacket.data.toString());
						log.e("DEVICE_GET_SCENEMODE_MASID success...");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
				break;
				case DeviceSetType.DEVICE_GET_POWER_MASID:
				{
					DeviceSetType.PowerSet powerSet = new DeviceSetType.PowerSet();
					try {
						powerSet.parseString(dataPacket.data.toString());
						log.e("DEVICE_GET_POWER_MASID success...");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
				break;
				case DeviceSetType.DEVICE_GET_LOWPOWER_WARN_MASID:
				{
					DeviceSetType.LowPowerWarn lowPowerWarn = new DeviceSetType.LowPowerWarn();
					try {
						lowPowerWarn.parseString(dataPacket.data.toString());
						log.e("DEVICE_GET_LOWPOWER_WARN_MASID success...");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
				break;
				case DeviceSetType.DEVICE_GET_GPS_INTERVAL_MASID:
				{
					DeviceSetType.GpsInterval gpsInterval = new DeviceSetType.GpsInterval();
					try {
						gpsInterval.parseString(dataPacket.data.toString());
						log.e("DEVICE_GET_GPS_INTERVAL_MASID success...");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
				break;
				case DeviceSetType.DEVICE_GET_GPS_STILLTIME_MASID:
				{
					DeviceSetType.GpsStillTimeGroup group = new DeviceSetType.GpsStillTimeGroup();
					try {
						group.parseString(dataPacket.data.toString());
						log.e("DEVICE_GET_GPS_STILLTIME_MASID success...");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
				break;
				case DeviceSetType.DEVICE_GET_LOCATION_MASID:
				{
					DeviceSetType.DeviceLocation location = new DeviceSetType.DeviceLocation();
					try {
						location.parseString(dataPacket.data.toString());
						log.e("DEVICE_GET_LOCATION_MASID success...");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
				break;
				case PublicType.USER_GET_INFO_MASID:
				{
					PublicType.UserChangeInfo info = new PublicType.UserChangeInfo();
					try {
						info.parseString(dataPacket.data.toString());
						log.e("USER_GET_INFO_MASID success...");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
				break;
				case DeviceSetType.DEVICE_GET_ONELINE_MASID:
				{
					DeviceSetType.DeviceOnlineStatus status = new DeviceSetType.DeviceOnlineStatus();
					try {
						status.parseString(dataPacket.data.toString());
						log.e("DEVICE_GET_ONELINE_MASID success...");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
				break;
				case DeviceSetType.DEVICE_GET_MSG_MASID:
				{

					DeviceSetType.DeviceMsgDataGroup group = new DeviceSetType.DeviceMsgDataGroup();
					try {
						group.parseString(dataPacket.data.toString());
						log.e("DEVICE_GET_MSG_MASID success...");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
				break;
				case DeviceSetType.DEVICE_GET_UNREAD_MSG_MASID:
				{
					DeviceSetType.DeviceUnReadMsgCount info = new DeviceSetType.DeviceUnReadMsgCount();
					try {
						//String test = "{\"alertcount\":1,\"alertway\":\"1,1,0,1,15012690183\",\"alert\":\"99你的车被人偷了！！！\"}";
						info.parseString(dataPacket.data.toString());
						log.e("DEVICE_GET_UNREAD_MSG_MASID success...");
						log.e("DeviceUnReadMsgCount = \n" + info.toString());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
				break;
				case ProductType.PRODUCT_GET_PACKET_MASID:
				{
					if (dataPacket != null)
					{
						log.e("ProductType.PRODUCT_GET_PACKET_MASID data = " + dataPacket.data.toString());
					}else{
						log.e("dataPacket = null");
					}
					
					
					ProductType.GetPackageGroup group = new ProductType.GetPackageGroup();
					try {
						group.parseString(dataPacket.data.toString());
						
						List<GetPackage> list = group.mGetPacageList;
						
						log.e("PRODUCT_GET_PACKET_MASID success...size = " + group.mGetPacageList.size());
						int size = list.size();
						for(int i = 0; i < size; i++){
							ProductType.GetPackage info = list.get(i);
							log.e("i = " + i + "\n" + 
									"id = " + info.mID + "\n" + 
									"name = " + info.mName + "\n" + 
								    "price = " + info.mPrice + "\n" + 
									"mValidTime = " + info.mValidTime + "\n" + 
									"mDesc = " + info.mDesc + "\n" + 
									"mDetail = " + info.mDetail);
						}
						
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						log.e("PRODUCT_GET_PACKET_MASID fail...");
					}
				
				}
				break;
				case ProductType.PRODUCT_CREATE_ORDER_MASID:
				{
					ProductType.CreateOrderResult result = new ProductType.CreateOrderResult();
					try {
						result.parseString(dataPacket.data.toString());
						log.e("PRODUCT_CREATE_ORDER_MASID success...");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
				break;
				case DeviceSetType.DEVICE_GET_AREA_MASID:
				{
					DeviceSetType.DeviceAreaResultGrounp grounp = new DeviceSetType.DeviceAreaResultGrounp();
					
					try {
						grounp.parseString(dataPacket.data.toString());
						log.e("DEVICE_GET_AREA_MASID success...");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
				case DeviceSetType.DEVICE_GET_DEVICE_HISTORY_MASID:
				{
					DeviceSetType.DeviceHistoryResultGrounp grounp = new DeviceSetType.DeviceHistoryResultGrounp();
					
					try {
						grounp.parseString(dataPacket.data.toString());
						log.e("DEVICE_GET_DEVICE_HISTORY_MASID success...");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
				case BikeType.BIKE_STATUS_MASITD:
				{
					BikeType.BikeStatus object = new BikeType.BikeStatus();
					try {
						object.parseString(dataPacket.data.toString());
						log.e("BIKE_STATUS_MASITD success...");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
//				case BikeType.BIKE_RECORD_MASITD:
//				{
//					BikeType.BikeRecordResult object = new BikeType.BikeRecordResult();
//					try {
//						object.parseString(dataPacket.data.toString());
//						log.e("BIKE_RECORD_MASITD success...");
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//				break;
//				case BikeType.BIKE_RECORDSUB_MASITD:
//				{
//					BikeType.BikeRecordSubResult object = new BikeType.BikeRecordSubResult();
//					try {
//						object.parseString(dataPacket.data.toString());
//						log.e("BIKE_RECORDSUB_MASITD success...");
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//					break;
				case BikeType.BIKE_LRECORD_MASITD:
				{
					BikeType.BikeLRecordResultGroup object = new BikeType.BikeLRecordResultGroup();
					try {
						object.parseString(dataPacket.data.toString());
						log.e("BIKE_LRECORD_MASITD success...");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
				case BikeType.BIKE_LRECORDSUB_MASITD:
				{
					BikeType.BikeLRecordSubResultGroup object = new BikeType.BikeLRecordSubResultGroup();
					try {
						object.parseString(dataPacket.data.toString());
						log.e("BIKE_LRECORDSUB_MASITD success...");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
					break;
				case BikeType.BIKE_GETALERTWAY:
				{
					BikeType.BikeAlertWay object = new BikeType.BikeAlertWay();
					try {
						object.parseString(dataPacket.data.toString());
						log.e("BIKE_GETALERTWAY success...");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
					break;	
				case BikeType.BIKE_GETAREA_MASITD:
				{
					BikeType.BikeGetArea object = new BikeType.BikeGetArea();
					try {
						object.parseString(dataPacket.data.toString());
						log.e("BIKE_GETAREA_MASITD success...");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
					break;	
			}
			
			
			return true;
		}
		

		
	    class FileDownCallback extends AbstractTaskCallback{

			@Override
			public void downLoadComplete(boolean isSuccess) {		
				String requestUri = getRequestUri();
				String saveUri = getSaveUri();
				log.e("FileDownCallback isSuccess = " + isSuccess + "\nrequestUri = " + requestUri + 
						"\nsaveUri = " + saveUri);	
			}
	    	
	    }

	    
	    private Dialog mDialog = null;
		private void showPhotoDialog(boolean bShow)
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
						takePhone();
					}else{
						selectFromLocal();
					}
				}
				
			};

			if (bShow)
			{
				mDialog = DialogFactory.creatSelectDialog(TestActivity.this, R.string.dialog_title_getphone, R.string.dialog_msg_getphone,
										R.string.btn_takephone, R.string.btn_localphone,  listener);
				mDialog.show();
			}
		
		}
		
		private void takePhone(){
			Intent intent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			//下面这句指定调用相机拍照后的照片存储的路径
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
					.fromFile(new File(Environment
							.getExternalStorageDirectory(),
							"yunyou.jpg")));
			startActivityForResult(intent, TAKE_PHOTO);
		}
		
		private void selectFromLocal(){
			Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);
			openAlbumIntent.setType("image/*");
			startActivityForResult(openAlbumIntent, CHOOSE_PHOTO);
		}
		
		private static final int CHOOSE_PHOTO = 0x0002;
		private static final int TAKE_PHOTO = 0x0003;
		private static final int PICK_PHOTO = 0x0004;
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			// TODO Auto-generated method stub
			log.e("requestCode = " + requestCode + ", resultCode = " + resultCode);
			switch(requestCode)
			{
				case TAKE_PHOTO:
					onTakeResult(resultCode, data);
					break;
				case CHOOSE_PHOTO:
					onChooseResult(resultCode, data);
					break;
				case PICK_PHOTO:
					log.e("PICK_PHOTO data = " + data);
					if(data != null){
						setPicToView(data);
					}
					break;
			}
			super.onActivityResult(requestCode, resultCode, data);
		}
		
		private void onTakeResult(int resultCode, Intent data){
			if (resultCode == RESULT_OK)
			{
				File temp = new File(Environment.getExternalStorageDirectory()
						+ "/yunyou.jpg");
				startPhotoZoom(Uri.fromFile(temp));
			}else{
				Utils.showToast(this, "获取图片失败...");
			}
		}
		
		private void onChooseResult(int resultCode, Intent data){
			if (resultCode == RESULT_OK)
			{
				startPhotoZoom(data.getData());
			}else{
				Utils.showToast(this, "获取图片失败...");
			}
		}
		
		private byte[] jpgeData = null;
		private String filePath = "";
		@SuppressLint("NewApi")
		private void setPicToView(Intent picdata) {
			Bundle extras = picdata.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");
			//	int bytes =  photo.getByteCount();
			//	log.e("bytes = " + bytes / 1024.0 + "KB");
				mImageView.setImageBitmap(photo);
	
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				boolean ret = photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
				//log.e("compress ret = ..." + ret);
				byte[] b = stream.toByteArray();
				//log.e("after compress bytes = " + b.length / 1024.0 + "KB");
				jpgeData = b;
				filePath = saveMyBitmap(photo, "upload.jpg");
				log.e("saveMyBitmap filePath = " + filePath);
				
				Bitmap bmp = BitmapFactory.decodeFile(filePath);
				if (bmp != null){
				//	int bytes2 =  bmp.getByteCount();
				//	log.e("bytes2 = " + bytes2 / 1024.0 + "KB");
					mImageView2.setImageBitmap(bmp);
				}
				/**
				 * 下面注释的方法是将裁剪之后的图片以Base64Coder的字符方式上
				 * 传到服务器，QQ头像上传采用的方法跟这个类似
				 */			
//				tp = new String(Base64Coder.encodeLines(b));
//				这个地方大家可以写下给服务器上传图片的实现，直接把tp直接上传就可以了，
//				服务器处理的方法是服务器那边的事了，吼吼
//				
//				如果下载到的服务器的数据还是以Base64Coder的形式的话，可以用以下方式转换
//				为我们可以用的图片类型就OK啦...吼吼
//				Bitmap dBitmap = BitmapFactory.decodeFile(tp);
//				Drawable drawable = new BitmapDrawable(dBitmap);
			
//				ib.setBackgroundDrawable(drawable);
//				iv.setBackgroundDrawable(drawable);
			}
		}

		
		private void onPickResult(int resultCode, Intent data){
			if (resultCode == RESULT_OK)
			{
				startPhotoZoom(data.getData());
			}else{
				Utils.showToast(this, "裁剪图片失败...");
			}
		}
		
		public String saveMyBitmap(Bitmap mBitmap,String bitName)  {
			String filePath = Environment.getExternalStorageDirectory() + "/" + bitName;
			File temp = new File(filePath);
	        FileOutputStream fOut = null;
	        try {
	                fOut = new FileOutputStream(temp);
	        } catch (FileNotFoundException e) {
	                e.printStackTrace();
	        }
	        if (fOut == null){
	        	return null;
	        }
	        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
	        try {
	                fOut.flush();
	                fOut.close();
	                return filePath;
	        } catch (IOException e) {
	                e.printStackTrace();
	        }
	   
	        return null;
	        
		}
		
		private final static int CROP_WIDTH = 160;
		private final static int CROP_HEIGHT = 160;
		/**
		 * 裁剪图片方法实现
		 * @param uri
		 */
		public void startPhotoZoom(Uri uri) {
			log.e("startPhotoZoom uri = " + uri);
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(uri, "image/*");
			//下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
			intent.putExtra("crop", "true");
			// aspectX aspectY 是宽高的比例
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			// outputX outputY 是裁剪图片宽高
			intent.putExtra("outputX", CROP_WIDTH);
			intent.putExtra("outputY", CROP_HEIGHT);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, PICK_PHOTO);
		}
	    
		private void toUploadFile(final String filePath)
		{
			if (filePath == null || filePath.length() == 0){
				Utils.showToast(this, "暂无图片数据");
				return ;
			}
			progressDialog.setMessage("正在上传文件...");
			progressDialog.show();
			String fileKey = "file";
			
			String jsonDATA = "";
			try {
				jsonDATA = DataFactory.buildUploadJsString("deviceset_avatar", "A128966000007112", "0");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			UploadExUtil uploadUtil = UploadExUtil.getInstance();
			uploadUtil.setOnUploadProcessListener(this);  //设置监听器监听上传状态
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("json", jsonDATA);

//			uploadUtil.uploadFile( filePath,fileKey,requestURL,params);
			Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						testweibo.uploadFile(filePath);
					} catch (IOException e) {
						e.printStackTrace();
						log.e("testweibo.test(filePath); fail...");
					}
				}
			});

			thread.start();
		}
		
		private void toUploadFileEx(String filePath)
		{
			if (filePath == null || filePath.length() == 0){
				Utils.showToast(this, "暂无图片数据");
				return ;
			}
			progressDialog.setMessage("正在上传文件...");
			progressDialog.show();
			String fileKey = "file";
			
			String jsonDATA = "";
			try {
				jsonDATA = DataFactory.buildUploadJsString("deviceset_avatar", "A128966000007112", "0");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			UploadExUtil uploadUtil = UploadExUtil.getInstance();
			uploadUtil.setOnUploadProcessListener(this);  //设置监听器监听上传状态
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("json", jsonDATA);

			uploadUtil.uploadFile( filePath,fileKey,requestURL,params);

		}

		private static String requestURL = "http://www.360lbs.net:8080";
		
		/**
		 * 去上传文件
		 */
		protected static final int TO_UPLOAD_FILE = 1;  
		/**
		 * 上传文件响应
		 */
		protected static final int UPLOAD_FILE_DONE = 2;  //
		/**
		 * 选择文件
		 */
		public static final int TO_SELECT_PHOTO = 3;
		/**
		 * 上传初始化
		 */
		private static final int UPLOAD_INIT_PROCESS = 4;
		/**
		 * 上传中
		 */
		private static final int UPLOAD_IN_PROCESS = 5;
		@Override
		public void onUploadDone(int responseCode, String message) {
			progressDialog.dismiss();
			Message msg = Message.obtain();
			msg.what = UPLOAD_FILE_DONE;
			msg.arg1 = responseCode;
			msg.obj = message;
			handler.sendMessage(msg);
		}


		@Override
		public void onUploadProcess(int uploadSize) {
			Message msg = Message.obtain();
			msg.what = UPLOAD_IN_PROCESS;
			msg.arg1 = uploadSize;
			handler.sendMessage(msg);
		}


		@Override
		public void initUpload(int fileSize) {
			Message msg = Message.obtain();
			msg.what = UPLOAD_INIT_PROCESS;
			msg.arg1 = fileSize;
			handler.sendMessage(msg );
		}	
		
		private Handler handler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
						switch (msg.what) {
							case UPLOAD_INIT_PROCESS:				
								break;
								
							case UPLOAD_IN_PROCESS:				
								break;
								
							case UPLOAD_FILE_DONE:
								String result = "响应码："+msg.arg1+"\n响应信息："+msg.obj+"\n耗时："+UploadExUtil.getRequestTime()+"秒";
								Utils.showToast(TestActivity.this, result);
								break;
								
							default:
								break;
						}
						super.handleMessage(msg);
				}
			};
}
