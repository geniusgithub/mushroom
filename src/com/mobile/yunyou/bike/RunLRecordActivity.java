package com.mobile.yunyou.bike;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.mobile.yunyou.R;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.bike.manager.CheckUploadManager;
import com.mobile.yunyou.bike.manager.RunRecordQueryProxy;
import com.mobile.yunyou.bike.manager.RunRecordSubManager;
import com.mobile.yunyou.bike.manager.CheckUploadManager.IDelObser;
import com.mobile.yunyou.datastore.RunRecordDBManager;
import com.mobile.yunyou.model.BikeType;
import com.mobile.yunyou.model.BikeType.BikeLRecordResult;
import com.mobile.yunyou.model.BikeType.BikeLRecordSubResult;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.PopWindowFactory;
import com.mobile.yunyou.util.Utils;
import com.mobile.yunyou.util.YunTimeUtils;
import com.mobile.yunyou.widget.RefreshListView;

public class RunLRecordActivity extends Activity implements OnClickListener, 
															IRequestCallback, 
															RunRecordQueryProxy.IRequestComplete,
															OnItemClickListener,
															RunRecordSubManager.IBikeSubRecordResult,
															OnRefreshListener2<ListView>,
															IDelObser{

	private static final CommonLog log = LogFactory.createLog();
	
	private static final int MSG_GET_NETWORK = 0x0001;
	
	private YunyouApplication mApplication;
	private NetworkCenterEx mNetworkCenterEx;
	
	private View mRootView;
	private ProgressBar mLoadProgressBar;
	private Button mBtnRefresh;
	private Button mBtnDel;
	
	private TextView mTVDistance;
	private TextView mTVCal;
	private TextView mTVTimeInterval;
	private TextView mTVRecordCount;
	
	//private RefreshListView mListView;
	private PullToRefreshListView mListView;
	private RunLRecordAdapter mAdapter;
	
	public LinkedList<BikeLRecordResult> mNetworkBikeRecordResultList = new LinkedList<BikeLRecordResult>();
	private RunRecordQueryProxy mBikeRecordProxy;
	private RunRecordSubManager mBikeSubRecordManager;
	
	private int mDistance = 0;
	private int mRecordCount = 0;
	private long mTimeMillsion = 0;
	private int mCal = 0;
	
	private Handler mHandler;
	
	private BikeLRecordResult curRecord;
	
	public LinkedList<BikeLRecordResult> mLocalBikeRecordResultList = new LinkedList<BikeLRecordResult>();
	private RunRecordDBManager mRecordDBManager;
	
	private LinkedList<BikeLRecordResult> mBikeRecordResultList = new LinkedList<BikeLRecordResult>();
	
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
			RequestFromDatabase();
		}
	}



	@Override
	protected void onDestroy() {
		CheckUploadManager.getInstance().setDelListener(null);
		super.onDestroy();
	}



	private void setupViews(){
		mRootView = findViewById(R.id.rootView);
	
		mListView = (PullToRefreshListView) findViewById(R.id.listview);
		
		mLoadProgressBar = (ProgressBar) findViewById(R.id.load_progress);
		mBtnRefresh = (Button) findViewById(R.id.btn_load);
		mBtnRefresh.setOnClickListener(this);
		mBtnDel = (Button) findViewById(R.id.btn_del);
		mBtnDel.setOnClickListener(this);

		
		mTVDistance = (TextView) findViewById(R.id.tv_distance);
		mTVCal = (TextView) findViewById(R.id.tv_cal);
		mTVTimeInterval = (TextView) findViewById(R.id.tv_timeinterval);
		mTVRecordCount = (TextView) findViewById(R.id.tv_bikecount);
		
		mAdapter = new RunLRecordAdapter(this, new LinkedList<BikeType.BikeLRecordResult>());
		
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		mListView.setOnRefreshListener(this);
		mListView.setMode(Mode.BOTH);
    	ILoadingLayout startLabels = mListView.getLoadingLayoutProxy(true, false);    
    	startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示    
    	startLabels.setRefreshingLabel("正在载入...");// 刷新时    
    	startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示    
    
    	ILoadingLayout endLabels = mListView.getLoadingLayoutProxy(false, true);    
    	endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示    
    	endLabels.setRefreshingLabel("正在载入...");// 刷新时    
    	endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示    
		
		mBtnRefresh.setVisibility(View.GONE);
		mLoadProgressBar.setVisibility(View.VISIBLE);
		mListView.setVisibility(View.GONE);
	}
	
	private void initData(){
		  mApplication = YunyouApplication.getInstance();
		  mNetworkCenterEx = NetworkCenterEx.getInstance();
		  
		  mRecordDBManager = RunRecordDBManager.getInstance();
		  
//		  	mNetworkCenterEx.setDid("A000000012000087");
//			mNetworkCenterEx.setSid("A12null000007135");
//			mNetworkCenterEx.initNetwork();
		 
			mBikeRecordProxy = RunRecordQueryProxy.getInstance(mNetworkCenterEx);
			mBikeRecordProxy.setIRequestComplete(this);
			
			mBikeSubRecordManager = RunRecordSubManager.getInstance();
			
//			mBikeSubRecordManager = BikeSubRecordManager.getInstance();
			
			clearData();
			
		  mHandler = new Handler(){

				@Override
				public void handleMessage(Message msg) {
					switch(msg.what){
					case MSG_GET_NETWORK:
						RequestFromServer();
						break;
					}
				}
				  
			  };
				  
			if (!mApplication.mIsDebug){
				mBtnDel.setVisibility(View.GONE);
			}
			
			CheckUploadManager.getInstance().setDelListener(this);
			
	}

	private void calData(List<BikeLRecordResult> list){
		mRecordCount = 0;
		mTimeMillsion = 0;
		mCal = 0;
		mDistance = 0;
		int size = list.size();
		mRecordCount = size;
		for(int i = 0; i < size; i++){
			BikeLRecordResult object = list.get(i);
			mCal += object.mCal;
			mTimeMillsion += YunTimeUtils.getTimeInterval(object.mEndTime, object.mStartTime);
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
	
	private LinkedList<BikeLRecordResult> copyFromOther(LinkedList<BikeLRecordResult> list){
		LinkedList<BikeLRecordResult> mlList = new LinkedList<BikeType.BikeLRecordResult>();
		int size = list.size();
		for(int i = 0; i < size;i++){
			mlList.add(list.get(i));
		}
		return mlList;
	}
	
	private void initLocalList(LinkedList<BikeLRecordResult> list){
		mLocalBikeRecordResultList = list;
		mBikeRecordResultList = copyFromOther(list);
		mAdapter.setData(mLocalBikeRecordResultList);
		calData(list);
		mListView.setVisibility(View.VISIBLE);
	}
	
	
	private void initNetworkList(LinkedList<BikeLRecordResult> list){
		mNetworkBikeRecordResultList = list;
		mBikeRecordResultList = copyFromOther(list);
		mAdapter.setData(mNetworkBikeRecordResultList);
		calData(list);
		mListView.setVisibility(View.VISIBLE);
	}
	
	private LinkedList<BikeLRecordResult> mergeList(LinkedList<BikeLRecordResult> localList, LinkedList<BikeLRecordResult> netList){
		
		LinkedList<BikeLRecordResult> list = new LinkedList<BikeType.BikeLRecordResult>();
		
		int size1 = localList.size();
		for(int i = 0;i < size1; i++){
			list.add(localList.get(i));
		}
		
		int size2 = netList.size();
		for(int i = 0;i < size2; i++){
			list.add(netList.get(i));
		}
		
		return list;

	}
	
	private void addList(List<BikeLRecordResult> list){
//		if (list != null){
//			mBikeRecordResultList = list;
//			mAdapter.setData(mBikeRecordResultList);
//		}

	}
	
	private boolean isExistLocalRecord()
	{
		boolean isEmpty1 = mLocalBikeRecordResultList.size() == 0;
		boolean isEmpty2 = mNetworkBikeRecordResultList.size() == 0;
		return !(isEmpty1 && isEmpty2);
	}
	

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_load:
			RequestFromServer();
			break;
		case R.id.btn_del:
			boolean ret = mRecordDBManager.deleteAll();
			Utils.showToast(this, "delete record from Database ret = " + ret);
			initNetworkList(mNetworkBikeRecordResultList);
			break;
		}
	}
	
	private void RequestFromServer(){

		mBikeRecordProxy.requestLast();

	}
	
	private void RequestFromDatabase(){

		LinkedList<BikeType.BikeLRecordResult> mList = new LinkedList<BikeType.BikeLRecordResult>();
		boolean ret = false;
		try {
			ret = mRecordDBManager.queryAll(mList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (!ret){
			Utils.showToast(this, R.string.toask_query_database_fail);
		}
		
		if (mList.size() == 0){
			mHandler.sendEmptyMessageDelayed(MSG_GET_NETWORK, 200);
			return ;
		}
		
		mLoadProgressBar.setVisibility(View.GONE);
		initLocalList(mList);
		
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


	@Override
	public boolean onComplete(int requestAction, ResponseDataPacket dataPacket) {

		String jsString = "null";
		if (dataPacket != null)
		{
			jsString = dataPacket.toString();
		}
	
		log.e("requestAction = " + Utils.toHexString(requestAction) + "\nResponseDataPacket = \n" +jsString);
		
		
		switch(requestAction){
			case BikeType.BIKE_LRECORD_MASITD:
	
				break;
		}

		showRequestDialog(false);

	
		return true;
	
	}
	
	@Override
	public void onGetBikeResult(boolean success) {
		log.e("onGetBikeResult success = " + success);
		if (!success)
		{
			Utils.showToast(this, R.string.request_data_fail);
			if (isExistLocalRecord() == false)
			{
				mBtnRefresh.setVisibility(View.VISIBLE);
				mLoadProgressBar.setVisibility(View.GONE);
			}else{
				mListView.onRefreshComplete();
			}			
			
			return ;
		}
		
		
		LinkedList<BikeType.BikeLRecordResult> list = mBikeRecordProxy.getDataArray();
		if (isExistLocalRecord() == false){
			if (list.size() != 0){
				initNetworkList(list);
				mBtnRefresh.setVisibility(View.GONE);
				mLoadProgressBar.setVisibility(View.GONE);
			}else{
				mBtnRefresh.setVisibility(View.VISIBLE);
				mLoadProgressBar.setVisibility(View.GONE);
			}
			
			return ;
		}
		
		mNetworkBikeRecordResultList = list;	
		mBikeRecordResultList = mergeList(mLocalBikeRecordResultList, mNetworkBikeRecordResultList);
		mAdapter.setData(mBikeRecordResultList);
		
		mListView.onRefreshComplete();
	
		
		calData(mBikeRecordResultList);
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
		
		//Toast.makeText(this, "pos = " + pos, Toast.LENGTH_SHORT).show();
		
		if (pos >= 0 && pos < mBikeRecordResultList.size()){
			BikeLRecordResult object =  mBikeRecordResultList.get((int) pos);
			boolean isLocal = object.isLocal;
			if (!isLocal){
				mBikeSubRecordManager.RequestSubRecord(object.mID, RunLRecordActivity.this);
				showRequestDialog(true);
				curRecord = object;
			}else{
				
				LinkedList<BikeLRecordSubResult> mBikeSubRecordList = object.mBikeSubRecordResultList;
				if (mBikeSubRecordList.size() < 2){
					Utils.showToast(RunLRecordActivity.this, R.string.toask_invalid_record);
					return ;
				}
				
				BikeType.BikeLRecordSubResultGroup group = new BikeType.BikeLRecordSubResultGroup();
				group.mBikeSubRecordResultList = mBikeSubRecordList;
				curRecord = object;
				YunyouApplication.getInstance().attachRunRecords(curRecord, group);
				Intent intent = new Intent();
				intent.setClass(RunLRecordActivity.this, RecordMapActivity.class);
				startActivity(intent);
			}
			

		}
		
	}


	
	
	
    public String getShowDistance(int distance){
    	double value = distance * 1.0 / 1000;
    	String string = Double.valueOf(value) + "公里";
    	return string;
    }


	@Override
	public void onRequestFail() {
		showRequestDialog(false);
		
		Utils.showToast(this, R.string.request_data_fail);
	}



	@Override
	public void onEmpty() {
		showRequestDialog(false);
		
		Utils.showToast(this, R.string.bikerecord_text_noRecord);
	}



	@Override
	public void onBikeSubRecordResult(final LinkedList<BikeType.BikeLRecordSubResult> list) {

			showRequestDialog(false);

			if (list.size() < 2){
				Utils.showToast(RunLRecordActivity.this, R.string.toask_invalid_record);
				return ;
			}
			
			BikeType.BikeLRecordSubResultGroup group = new BikeType.BikeLRecordSubResultGroup();
			group.mBikeSubRecordResultList = list;
			YunyouApplication.getInstance().attachRunRecords(curRecord, group);
			Intent intent = new Intent();
			intent.setClass(RunLRecordActivity.this, RecordMapActivity.class);
			startActivity(intent);
	
	}

	@Override
	public void onDelRecord(BikeLRecordResult object) {
		int size = mLocalBikeRecordResultList.size();
		for(int i = 0; i < size; i++){
			BikeLRecordResult result = mLocalBikeRecordResultList.get(i);
			if (result.mStartTime.equals(object.mStartTime)){
				log.e("find it!!! startTime = "  + object.mStartTime);
				mLocalBikeRecordResultList.remove(result);
				mBikeRecordResultList = mergeList(mLocalBikeRecordResultList, mNetworkBikeRecordResultList);
				mAdapter.setData(mBikeRecordResultList);				
				calData(mBikeRecordResultList);
				continue;
			}
		}
	}



	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		mBikeRecordProxy.requestLast();
	}



	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		mBikeRecordProxy.requestHistory();
	}
}
