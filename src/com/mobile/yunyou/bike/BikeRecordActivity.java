//package com.mobile.yunyou.bike;
//
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.view.Gravity;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.Button;
//import android.widget.PopupWindow;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.mobile.yunyou.R;
//import com.mobile.yunyou.YunyouApplication;
//import com.mobile.yunyou.bike.manager.BikeRecordProxy;
//import com.mobile.yunyou.model.BikeType;
//import com.mobile.yunyou.model.BikeType.BikeRecordResult;
//import com.mobile.yunyou.model.BikeType.BikeRecordSubResult;
//import com.mobile.yunyou.model.DeviceSetType.DeviceMsgData;
//import com.mobile.yunyou.model.DeviceSetType;
//import com.mobile.yunyou.model.ResponseDataPacket;
//import com.mobile.yunyou.msg.ChatMsgEntity;
//import com.mobile.yunyou.network.IRequestCallback;
//import com.mobile.yunyou.network.NetworkCenterEx;
//import com.mobile.yunyou.util.CommonLog;
//import com.mobile.yunyou.util.LogFactory;
//import com.mobile.yunyou.util.PopWindowFactory;
//import com.mobile.yunyou.util.Utils;
//import com.mobile.yunyou.util.YunTimeUtils;
//import com.mobile.yunyou.widget.RefreshListView;
//
//public class BikeRecordActivity extends Activity implements OnClickListener, 
//															IRequestCallback, 
//															BikeRecordProxy.IRequestComplete,
//															OnItemClickListener,
//															BikeSubRecordManager.IBikeSubRecordResult,
//															RefreshListView.IOnRefreshListener,
//															RefreshListView.IOnLoadMoreListener{
//
//	private static final CommonLog log = LogFactory.createLog();
//	
//	private static final int MSG_GET_MESSAGE = 0x0001;
//	
//	private YunyouApplication mApplication;
//	private NetworkCenterEx mNetworkCenterEx;
//	
//	private View mRootView;
//	private ProgressBar mLoadProgressBar;
//	private Button mBtnRefresh;
//	
//	private TextView mTVDistance;
//	private TextView mTVCal;
//	private TextView mTVTimeInterval;
//	private TextView mTVRecordCount;
//	
//	private RefreshListView mListView;
//	private BikeRecordAdapter mAdapter;
//	
//	public List<BikeRecordResult> mBikeRecordResultList = new ArrayList<BikeRecordResult>();
//	private BikeRecordProxy mBikeRecordProxy;
//	private BikeSubRecordManager mBikeSubRecordManager;
//	
//	private int mDistance = 0;
//	private int mRecordCount = 0;
//	private long mTimeMillsion = 0;
//	private int mCal = 0;
//	
//	private Handler mHandler;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.bikerecord_layout);
//		setupViews();
//		initData();
//	}
//	
//	
//
//	private boolean isFirstResume = true;
//	@Override
//	protected void onResume() {
//		super.onResume();
//		
//		updateDatasView();
//		
//		if (isFirstResume){
//			isFirstResume = false;
//			mHandler.sendEmptyMessageDelayed(MSG_GET_MESSAGE, 500);
//			
//		}
//	}
//
//
//
//	private void setupViews(){
//		mRootView = findViewById(R.id.rootView);
//	
//		mListView = (RefreshListView) findViewById(R.id.listview);
//		
//		mLoadProgressBar = (ProgressBar) findViewById(R.id.load_progress);
//		mBtnRefresh = (Button) findViewById(R.id.btn_load);
//		mBtnRefresh.setOnClickListener(this);
//		
//		mTVDistance = (TextView) findViewById(R.id.tv_distance);
//		mTVCal = (TextView) findViewById(R.id.tv_cal);
//		mTVTimeInterval = (TextView) findViewById(R.id.tv_timeinterval);
//		mTVRecordCount = (TextView) findViewById(R.id.tv_bikecount);
//		
//		mAdapter = new BikeRecordAdapter(this, mBikeRecordResultList);
//		
//		mListView.setAdapter(mAdapter);
//		mListView.setOnItemClickListener(this);
//		mListView.setOnRefreshListener(this);
//		mListView.setOnLoadMoreListener(this);
//		
//		mBtnRefresh.setVisibility(View.GONE);
//		mLoadProgressBar.setVisibility(View.VISIBLE);
//		mListView.setVisibility(View.GONE);
//	}
//	
//	private void initData(){
//		  mApplication = YunyouApplication.getInstance();
//		  mNetworkCenterEx = NetworkCenterEx.getInstance();
//		  
////		  	mNetworkCenterEx.setDid("A000000012000087");
////			mNetworkCenterEx.setSid("A12null000007135");
////			mNetworkCenterEx.initNetwork();
//		 
//			mBikeRecordProxy = BikeRecordProxy.getInstance(mNetworkCenterEx);
//			mBikeRecordProxy.setIRequestComplete(this);
//			
//			mBikeSubRecordManager = BikeSubRecordManager.getInstance();
//			
//			clearData();
//			
//			  mHandler = new Handler(){
//
//					@Override
//					public void handleMessage(Message msg) {
//						switch(msg.what){
//						case MSG_GET_MESSAGE:
//							load();
//							break;
//						}
//					}
//					  
//				  };
//	}
//
//	private void calData(List<BikeRecordResult> list){
//		mRecordCount = 0;
//		mTimeMillsion = 0;
//		mCal = 0;
//		mDistance = 0;
//		int size = list.size();
//		mRecordCount = size;
//		for(int i = 0; i < size; i++){
//			BikeRecordResult object = list.get(i);
//			mCal += object.mCal;
//			mTimeMillsion += object.mTimeInterval;
//			mDistance += object.mDistance;
//		}
//		
//		updateDatasView();
//	}
//	
//	private void clearData(){
//		mRecordCount = 0;
//		mTimeMillsion = 0;
//		mCal = 0;
//		mDistance = 0;
//		updateDatasView();
//	}
//	
//	private void updateDatasView(){
//		mTVCal.setText(String.valueOf(mCal));
//		mTVDistance.setText(getShowDistance(mDistance));
//		mTVRecordCount.setText(String.valueOf(mRecordCount));
//		mTVTimeInterval.setText(YunTimeUtils.getFormatTimeInterval(mTimeMillsion));
//	}
//	
//	private void refreshList(){
//		mAdapter.setData(mBikeRecordResultList);
//	}
//	
//	private boolean isExistLocalRecord()
//	{
//		return mBikeRecordResultList.size() == 0 ? false : true;
//	}
//	
//
//	@Override
//	public void onClick(View v) {
//		switch(v.getId()){
//		case R.id.btn_load:
//			load();
//			break;
//		}
//	}
//	
//	private void load(){
//
//		mBikeRecordProxy.requestLast();
//		
//		mBtnRefresh.setVisibility(View.GONE);
//		mLoadProgressBar.setVisibility(View.VISIBLE);
//
//	}
//	
//	private PopupWindow mPopupWindow = null;
//	public void showRequestDialog(boolean bShow)
//	{
//	
//		if (mPopupWindow != null)
//		{
//			mPopupWindow.dismiss();
//			mPopupWindow = null;
//		}
//		
//		if (bShow)
//		{
//			mPopupWindow = PopWindowFactory.creatLoadingPopWindow(this, R.string.request_data);
//			mPopupWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);
//		}
//	
//	}
//
//
//	@Override
//	public boolean onComplete(int requestAction, ResponseDataPacket dataPacket) {
//
//		String jsString = "null";
//		if (dataPacket != null)
//		{
//			jsString = dataPacket.toString();
//		}
//	
//		log.e("requestAction = " + Utils.toHexString(requestAction) + "\nResponseDataPacket = \n" +jsString);
//		
//		
//		switch(requestAction){
//			case BikeType.BIKE_RECORDSUB_MASITD:
//	
//				break;
//		}
//
//		showRequestDialog(false);
//
//	
//		return true;
//	
//	}
//	
//	@Override
//	public void onGetBikeResult(boolean success) {
//		log.e("onGetBikeResult success = " + success);
//		if (!success)
//		{
//			Utils.showToast(this, R.string.request_data_fail);
//			if (isExistLocalRecord() == false)
//			{
//				mBtnRefresh.setVisibility(View.VISIBLE);
//				mLoadProgressBar.setVisibility(View.GONE);
//			}else{
//				mListView.onRefreshComplete();
//				mListView.onLoadMoreComplete(false);
//			}			
//			
//			return ;
//		}
//		
//		mBikeRecordResultList = mBikeRecordProxy.getDataArray();
//		int size = mBikeRecordResultList.size();		
//		if (size == 0)
//		{
//			mBtnRefresh.setVisibility(View.VISIBLE);
//			mLoadProgressBar.setVisibility(View.GONE);	
//			clearData();
//			return ;
//		}
//		
//		mAdapter.setData(mBikeRecordResultList);
//		mAdapter.notifyDataSetChanged();
//		
//		mBtnRefresh.setVisibility(View.GONE);
//		mLoadProgressBar.setVisibility(View.GONE);
//		mListView.setVisibility(View.VISIBLE);
//		mListView.onRefreshComplete();
//		mListView.onLoadMoreComplete(false);
//		
//		calData(mBikeRecordResultList);
//	}
//	
//
////	
////	private void onBikeRecordSubResult(ResponseDataPacket dataPacket)
////	{
////		if (dataPacket == null || dataPacket.rsp == 0)
////		{
////			Utils.showToast(this, R.string.request_data_fail);
////			
////			return ;
////		}
////		
////		DeviceSetType.DeviceHistoryResultGrounp group = new DeviceSetType.DeviceHistoryResultGrounp();
////		try {
////			boolean ret = group.parseString(dataPacket.dataArray.toString());
////		} catch (Exception e) {
////			e.printStackTrace();
////			Utils.showToast(this, R.string.analyze_data_fail);
////		}
////		
////	}
//
//
//	@Override
//	public void onItemClick(AdapterView<?> arg0, View arg1, int arg, long pos) {
//		
//		//Toast.makeText(this, "pos = " + pos, Toast.LENGTH_SHORT).show();
//		
//		if (pos >= 0 && pos < mBikeRecordResultList.size()){
//			BikeRecordResult object =  mBikeRecordResultList.get((int) pos);
//			mBikeSubRecordManager.RequestSubRecord(object.mID, this);
//			showRequestDialog(true);
//		}
//		
//	}
//
//
//	@Override
//	public void OnLoadMore() {
//		mBikeRecordProxy.requestHistory();
//	}
//
//
//	@Override
//	public void OnRefresh() {
//		mBikeRecordProxy.requestLast();
//	}
//
//
//
//	
//	
//	
//	
//    public String getShowDistance(int distance){
//    	double value = distance * 1.0 / 1000;
//    	String string = Double.valueOf(value) + "公里";
//    	return string;
//    }
//
//
//
//	@Override
//	public void onRequestFail() {
//		showRequestDialog(false);
//		
//		Utils.showToast(this, R.string.request_data_fail);
//	}
//
//
//
//	@Override
//	public void onEmpty() {
//		showRequestDialog(false);
//		
//		Utils.showToast(this, R.string.bikerecord_text_noRecord);
//	}
//
//
//
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
//					Utils.showToast(BikeRecordActivity.this, R.string.bikerecord_text_getRecordFail);
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
//}
