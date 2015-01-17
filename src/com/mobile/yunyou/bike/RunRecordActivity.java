package com.mobile.yunyou.bike;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.mobile.yunyou.R;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.bike.manager.BikeRecordProxy;
import com.mobile.yunyou.datastore.RunRecordDBManager;
import com.mobile.yunyou.model.BikeType;
import com.mobile.yunyou.model.BikeType.BikeRecordResult;
import com.mobile.yunyou.model.BikeType.BikeRecordSubResult;
import com.mobile.yunyou.model.BikeType.RunRecordGroup;
import com.mobile.yunyou.model.DeviceSetType.DeviceMsgData;
import com.mobile.yunyou.model.DeviceSetType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.msg.ChatMsgEntity;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.PopWindowFactory;
import com.mobile.yunyou.util.Utils;
import com.mobile.yunyou.util.YunTimeUtils;
import com.mobile.yunyou.widget.RefreshListView;

public class RunRecordActivity extends Activity implements OnClickListener, 
															OnItemClickListener,
															RefreshListView.IOnRefreshListener,
															RefreshListView.IOnLoadMoreListener{

	private static final CommonLog log = LogFactory.createLog();
	
	private static final int MSG_GET_RECORD = 0x0001;
	private static final int MSG_REFRESH_RECORD = 0x0002;
	
	private View mRootView;
	private ProgressBar mLoadProgressBar;
	
	private TextView mTVDistance;
	private TextView mTVCal;
	private TextView mTVTimeInterval;
	private TextView mTVRecordCount;
	private Button mBtnBack;
	private RefreshListView mListView;
	private RunRecordAdapter mAdapter;
	
	public List<BikeType.RunRecordGroup> mRunRecordList = new ArrayList<BikeType.RunRecordGroup>();
	private RunRecordDBManager mRunRecordDBManager;
	
	private int mDistance = 0;
	private int mRecordCount = 0;
	private long mTimeMillsion = 0;
	private int mCal = 0;
	
	private Handler mHandler;
	
	private Button mBtnDel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bikerecord_layout);
		setupViews();
		initData();
	}
	
	

	private boolean isFirstResume = true;
	@Override
	protected void onResume() {
		super.onResume();
		
		updateDatasView();
		
		if (isFirstResume){
			isFirstResume = false;
			mHandler.sendEmptyMessageDelayed(MSG_GET_RECORD, 200);
			
		}
	}



	private void setupViews(){
		mRootView = findViewById(R.id.rootView);
	
		mListView = (RefreshListView) findViewById(R.id.listview);
    	mBtnBack = (Button) findViewById(R.id.btn_back);
    	mBtnBack.setOnClickListener(this);
		mLoadProgressBar = (ProgressBar) findViewById(R.id.load_progress);
		
		mTVDistance = (TextView) findViewById(R.id.tv_distance);
		mTVCal = (TextView) findViewById(R.id.tv_cal);
		mTVTimeInterval = (TextView) findViewById(R.id.tv_timeinterval);
		mTVRecordCount = (TextView) findViewById(R.id.tv_bikecount);
		mBtnDel = (Button) findViewById(R.id.btn_del);
		mBtnDel.setOnClickListener(this);
		
		mAdapter = new RunRecordAdapter(this, mRunRecordList);
		
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		mListView.setOnRefreshListener(this);
		mListView.removeFootView();
	
		
		mLoadProgressBar.setVisibility(View.VISIBLE);
	}
	
	private void initData(){
		
		clearData();
		
		mHandler = new Handler(){

				@Override
				public void handleMessage(Message msg) {
					switch(msg.what){
					case MSG_GET_RECORD:
						load();
						break;
					case MSG_REFRESH_RECORD:
						onRunRecordResult((List<RunRecordGroup>) msg.obj);
						break;
					}
				}
				  
			  };
				  
		mRunRecordDBManager = RunRecordDBManager.getInstance();
	}

	private void calData(List<BikeType.RunRecordGroup> list){
		mRecordCount = 0;
		mTimeMillsion = 0;
		mCal = 0;
		mDistance = 0;
		int size = list.size();
		mRecordCount = size;
		for(int i = 0; i < size; i++){
			BikeType.RunRecordGroup object = list.get(i);
			mCal += object.mCal;
			mTimeMillsion += object.mTimeMillsion;
			mDistance += object.mTotalDistance;
		}
		
		updateDatasView();
	}
	
	private void clearData(){
		mRecordCount = 0;
		mTimeMillsion = 0;
		mCal = 0;
		mDistance = 0;
		updateDatasView();
	}
	
	private void updateDatasView(){
		mTVCal.setText(String.valueOf(mCal));
		mTVDistance.setText(getShowDistance(mDistance));
		mTVRecordCount.setText(String.valueOf(mRecordCount));
		mTVTimeInterval.setText(YunTimeUtils.getFormatTimeInterval(mTimeMillsion));
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_load:
			load();
			break;
		case R.id.btn_del:
			del();
			break;
		}
	}
	
	private void load(){

		mLoadProgressBar.setVisibility(View.VISIBLE);

		Message msg = mHandler.obtainMessage(MSG_REFRESH_RECORD);
		InnerThread thread = new InnerThread(msg);
		thread.start();
	}
	
	private void del(){

		mRunRecordDBManager.deleteAll();
		onRunRecordResult(new ArrayList<BikeType.RunRecordGroup>());
	}
	
	
	class InnerThread extends Thread
	{
		private Message mMessage;
		public InnerThread(Message message)
		{
			mMessage = message;
		}

		@Override
		public void run() {

			List<BikeType.RunRecordGroup> mList = new ArrayList<BikeType.RunRecordGroup>();
			try {
				mRunRecordDBManager.queryAll(mList);
				mMessage.obj = mList;
				mMessage.sendToTarget();
			} catch (Exception e) {
				e.printStackTrace();
				mMessage.obj = null;
				mMessage.sendToTarget();
			}
		}
	}
	
	private PopupWindow mPopupWindow = null;
	public void showRequestDialog(boolean bShow)
	{
	
		if (mPopupWindow != null)
		{
			mPopupWindow.dismiss();
			mPopupWindow = null;
		}
		
		if (bShow)
		{
			mPopupWindow = PopWindowFactory.creatLoadingPopWindow(this, R.string.request_data);
			mPopupWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);
		}
	
	}
	

	public void onRunRecordResult(List<BikeType.RunRecordGroup> list) {
		if (list == null){
			list  =new ArrayList<BikeType.RunRecordGroup>();
			Utils.showToast(this, "查询数据库出错");
		}
		log.e("onRunRecordResult list.size = " + list.size());
		mLoadProgressBar.setVisibility(View.GONE);	
		mRunRecordList = list;
		if (mRunRecordList.size() == 0)
		{	
			clearData();
		}
		
		mAdapter.setData(mRunRecordList);
		mAdapter.notifyDataSetChanged();
		
		mLoadProgressBar.setVisibility(View.GONE);
		mListView.onRefreshComplete();
		
		calData(mRunRecordList);
	}
	

//	
//	private void onBikeRecordSubResult(ResponseDataPacket dataPacket)
//	{
//		if (dataPacket == null || dataPacket.rsp == 0)
//		{
//			Utils.showToast(this, R.string.request_data_fail);
//			
//			return ;
//		}
//		
//		DeviceSetType.DeviceHistoryResultGrounp group = new DeviceSetType.DeviceHistoryResultGrounp();
//		try {
//			boolean ret = group.parseString(dataPacket.dataArray.toString());
//		} catch (Exception e) {
//			e.printStackTrace();
//			Utils.showToast(this, R.string.analyze_data_fail);
//		}
//		
//	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg, long pos) {
		
	//	Toast.makeText(this, "pos = " + pos, Toast.LENGTH_SHORT).show();
		
		if (pos >= 0 && pos < mRunRecordList.size()){
			BikeType.RunRecordGroup object =  mRunRecordList.get((int) pos);
			YunyouApplication.getInstance().setRunRecord(object);
			startRecordManActivity();
		}
		
	}


	@Override
	public void OnLoadMore() {

	}


	@Override
	public void OnRefresh() {
		Message msg = mHandler.obtainMessage(MSG_REFRESH_RECORD);
		InnerThread thread = new InnerThread(msg);
		thread.start();
	}
	
    public String getShowDistance(int distance){
    	double value = distance * 1.0 / 1000;
    	String string = Double.valueOf(value) + "公里";
    	return string;
    }

    private void startRecordManActivity(){
    	Intent intent = new Intent();
    	intent.setClass(this, RecordMapActivity.class);
    	startActivity(intent);
    }

	

//	@Override
//	public void onBikeSubRecordResult(final LinkedList<BikeRecordSubResult> list) {
//		
//		runOnUiThread(new Runnable() {
//			
//			@Override
//			public void run() {
//				showRequestDialog(false);
//				mBikeSubRecordManager.setLinkList(list);
//				if (list == null){
//					Utils.showToast(RunRecordActivity.this, R.string.bikerecord_text_getRecordFail);
//					setResult(RESULT_OK);
//					finish();
//					return ;
//				}
//				
//				setResult(RESULT_OK);
//				finish();
//			}
//		});
//		
//		
//	
//	}
}
