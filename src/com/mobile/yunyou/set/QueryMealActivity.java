package com.mobile.yunyou.set;


import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.mobile.yunyou.R;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.model.GloalType;
import com.mobile.yunyou.model.ProductType;
import com.mobile.yunyou.model.ProductType.GetPackage;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.product.AlixId;
import com.mobile.yunyou.product.BaseHelper;
import com.mobile.yunyou.product.MobileSecurePayHelper;
import com.mobile.yunyou.product.MobileSecurePayer;
import com.mobile.yunyou.product.ProductUtil;
import com.mobile.yunyou.product.ResultChecker;
import com.mobile.yunyou.set.adapter.ProductMealAdapter;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.PopWindowFactory;
import com.mobile.yunyou.util.Utils;



public class QueryMealActivity extends Activity implements OnClickListener, OnItemClickListener,
								DialogInterface.OnCancelListener , IRequestCallback{

	private static final CommonLog log = LogFactory.createLog();
	
	private View mRootView;
	private Button mBtnBack;
	private ListView mListView;
	
	private ProductMealManager mProductMealManager;
	private ProductMealAdapter mAdapter;
	private List<ProductType.GetPackage> mPackageList;
	
	private YunyouApplication mApplication;
	private NetworkCenterEx mNetworkCenterEx;
	
	private ProductType.GetPackage mCurSelPackage;
	private String mCurOrderNo = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_query_meal_layout);
		initView();
		initData();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (mProgress != null){
			mProgress.dismiss();
		}

	}
	
	private void initView(){
		mRootView = findViewById(R.id.rootView);
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
		mListView = (ListView) findViewById(R.id.listview);
	}
	
	
	private void initData(){
		mProductMealManager = ProductMealManager.getInstance();
		mPackageList = mProductMealManager.getPackageList();
		mAdapter = new ProductMealAdapter(this, mPackageList);
		log.e("mPackageList.size = " + mPackageList.size());
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		
		mApplication = YunyouApplication.getInstance();
		mNetworkCenterEx = NetworkCenterEx.getInstance();
	}


	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.btn_back:
			finish();
			break;

		default:
			break;
		}
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
		
		ProductType.GetPackage object = (GetPackage) parent.getItemAtPosition(position);
		requestCreateOrder(object);
	}
	
	private void requestCreateOrder(ProductType.GetPackage object){
	
		String did = mApplication.getCurDid();
		if (did.length() == 0){
			Utils.showToast(this, "当前未绑定任何设备...");
			return ;
		}
		
		ProductType.CreateOrder createOrder = new ProductType.CreateOrder();
		createOrder.mDid = did;
		createOrder.mQuantity = 1;
		createOrder.mPayVia = "alipay";
		createOrder.mPrice = object.mPrice;
		createOrder.mTotalFee = object.mPrice;
		createOrder.mPackageId = object.mID;

		mNetworkCenterEx.StartRequestToServer(ProductType.PRODUCT_CREATE_ORDER_MASID, createOrder, this);
		mCurSelPackage = object;
		showRequestDialog(true);
	}
	
	private void requestPay(ProductType.GetPackage object, String orderNO){
		
		MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(this);
		boolean isMobile_spExist = mspHelper.detectMobile_sp(this);
		if (!isMobile_spExist)
		{
			return;
		}
				

		String signResultString = ProductUtil.getSingResult(object, orderNO);
		
		
		MobileSecurePayer msp = new MobileSecurePayer();
		boolean bRet = msp.pay(signResultString, mHandler, AlixId.RQF_PAY, this);

		if (bRet) {
			// show the progress bar to indicate that we have started
			// paying.
			// 显示“正在支付”进度条
			closeProgress();
			mProgress = BaseHelper.showProgress(this, null, "正在支付", false, true, this);
		} 
		
	}
	
	
	private void terminalmanageBuyReturn(){
		log.e("terminalmanageBuyReturn");
		
		GloalType.UserInfoEx userInfoEx = mApplication.getUserInfoEx();
		ProductType.BuyNotifyReturn oBuyNotifyObject = new ProductType.BuyNotifyReturn();
		oBuyNotifyObject.mOrderID = String.valueOf(mCurSelPackage.mID);
		oBuyNotifyObject.mPayMethod = "alipay";
		oBuyNotifyObject.mTradeNo = "";
		oBuyNotifyObject.mEmail = userInfoEx.mEmail;
		oBuyNotifyObject.mBuyerID = userInfoEx.mAccountName;
		oBuyNotifyObject.mTotalFee = mCurSelPackage.mPrice;
			
		mNetworkCenterEx.StartRequestToServer(ProductType.PRODUCT_BUY_NOTIFY_MASID, oBuyNotifyObject, this);
		mNetworkCenterEx.StartRequestToServer(ProductType.PRODUCT_BUY_RETURN_MASID, oBuyNotifyObject, this);
		
	}
	
	private ProgressDialog mProgress = null;
	// close the progress bar
	// 关闭进度框
	void closeProgress() {
		try {
			if (mProgress != null) {
				mProgress.dismiss();
				mProgress = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void onCancel(DialogInterface arg0) {
		onKeyDown(KeyEvent.KEYCODE_BACK, null);
	}
	
	
	/**
	 * 返回键监听事件
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			return true;
		}

		return false;
	}

	
	
	
	
	
	
	
	//
	// the handler use to receive the pay result.
	// 这里接收支付结果，支付宝手机端同步通知
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				String strRet = (String) msg.obj;
				log.e(strRet);	// strRet范例：resultStatus={9000};memo={};result={partner="2088201564809153"&seller="2088201564809153"&out_trade_no="050917083121576"&subject="123456"&body="2010新款NIKE 耐克902第三代板鞋 耐克男女鞋 386201 白红"&total_fee="0.01"&notify_url="http://notify.java.jpxx.org/index.jsp"&success="true"&sign_type="RSA"&sign="d9pdkfy75G997NiPS1yZoYNCmtRbdOP0usZIMmKCCMVqbSG1P44ohvqMYRztrB6ErgEecIiPj9UldV5nSy9CrBVjV54rBGoT6VSUF/ufjJeCSuL510JwaRpHtRPeURS1LXnSrbwtdkDOktXubQKnIMg2W0PreT1mRXDSaeEECzc="}
				switch (msg.what) {
					case AlixId.RQF_PAY: 
					{
						closeProgress();
						forPay(strRet);						
					}
					break;
				}
				super.handleMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	
	//resultStatus={9000};memo={};result={partner="2088801338887361"&seller="2088801338887361"&out_trade_no="A001000012000094121213223454"&subject="云游半年套餐"&body="购买此套餐后可享受云游儿童终端半年的服务，服务费用中不包括终端告警、提醒等产生的短信费"&total_fee="0.01"&notify_url="http://notify.java.jpxx.org/index.jsp"&success="true"&sign_type="RSA"&sign="MbSuz1bJWwB5/BnpiU2vxhGZf82aVbzjwPzTRaeO9xbtaMP60Uo7l9kD1+ynj05y0hzXm3E9yR4LjiYR6Tn7GNDWJQwi/xl/uFoznaxrJB4mKQoMU/HnuWmgS6PqKhKttmy/xLLDOKkfBzqvg0o67ANdBKlOQ5Kgd1LgFchzE8g="}

	private void forPay(String strRet){
		// 处理交易结果
		try {
			// 获取交易状态码，具体状态代码请参看文档
			String tradeStatus = "resultStatus={";
			int imemoStart = strRet.indexOf("resultStatus=");
			imemoStart += tradeStatus.length();
			int imemoEnd = strRet.indexOf("};memo=");
			tradeStatus = strRet.substring(imemoStart, imemoEnd);
			
			//先验签通知
			ResultChecker resultChecker = new ResultChecker(strRet);
			int retVal = resultChecker.checkSign();
			// 验签失败
			if (retVal == ResultChecker.RESULT_CHECK_SIGN_FAILED) {
				BaseHelper.showDialog(QueryMealActivity.this, "提示", getResources().getString(R.string.check_sign_failed), android.R.drawable.ic_dialog_alert);
			} else {// 验签成功。验签成功后再判断交易状态码
				if(tradeStatus.equals("9000"))//判断交易状态码，只有9000表示交易成功
				{
					BaseHelper.showDialog(QueryMealActivity.this, "提示","支付成功。交易状态码："+tradeStatus, R.drawable.infoicon);
					terminalmanageBuyReturn();
				}
					
				else
				BaseHelper.showDialog(QueryMealActivity.this, "提示", "支付失败。交易状态码:" + tradeStatus, R.drawable.infoicon);
			}

		} catch (Exception e) {
			e.printStackTrace();
			BaseHelper.showDialog(QueryMealActivity.this, "提示", strRet, R.drawable.infoicon);
		}
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
			mPopupWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);
		}
	
	}
	
	

	@Override
	public boolean onComplete(int requestAction, ResponseDataPacket dataPacket) {
		
		showRequestDialog(false);
		
		String jsString = "null";
		if (dataPacket != null)
		{
			jsString = dataPacket.toString();
		}
		
		log.e("requestAction = " + Utils.toHexString(requestAction) + "\nResponseDataPacket = \n" +jsString);
		
		switch(requestAction)
		{
		case ProductType.PRODUCT_CREATE_ORDER_MASID:
			onGetOrderResult(dataPacket);
			break;
		case ProductType.PRODUCT_BUY_NOTIFY_MASID:
			onBuyNotifyReturn(dataPacket);
			break;
		case ProductType.PRODUCT_BUY_RETURN_MASID:
			onBuyNotifyReturn(dataPacket);
			break;
		}
		
		return true;
	}
	
	
	private void onGetOrderResult( ResponseDataPacket dataPacket){
		if (dataPacket == null || dataPacket.rsp == 0)
		{
			Utils.showToast(this, R.string.request_data_fail);
			
			return ;
		}
		
		
		ProductType.CreateOrderResult result = new ProductType.CreateOrderResult();
		try {
			result.parseString(dataPacket.data.toString());

			if (mCurSelPackage != null){	
				mCurOrderNo = result.mOrderID;
				requestPay(mCurSelPackage, result.mOrderID);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void onBuyNotifyReturn(ResponseDataPacket dataPacket){
		if (dataPacket == null || dataPacket.rsp == 0)
		{
			log.e("onBuyNotifyReturn fail!!!");
			return ;
		}
		
		log.e("onBuyNotifyReturn success!!!");
	}

}
