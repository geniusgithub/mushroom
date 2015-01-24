package com.mobile.yunyou.friend;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.mobile.yunyou.R;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.custom.DoubleEditPopWindow;
import com.mobile.yunyou.model.GloalType;
import com.mobile.yunyou.model.GloalType.DeviceInfoEx;
import com.mobile.yunyou.model.PublicType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.DialogFactory;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.PopWindowFactory;
import com.mobile.yunyou.util.Utils;

public class FriendActivity extends Activity implements TextWatcher, OnClickListener, 
														IRequestCallback,
														FriendAdapter.IPosCallback{
														//OnCreateContextMenuListener,
														

	private static final CommonLog log = LogFactory.createLog();
	
	private YunyouApplication mApplication;
	private NetworkCenterEx mNetworkCenterEx;
	
	
	private View mRootView;
	private View mBottomView;
	private ListView mFriendListView;
	private FriendAdapter mFriendAdapter;
	private List<FriendObject> mFriendObjects = new ArrayList<FriendObject>();
	
	
	private EditText mEditTextSearch;
	private Button mBtnClear;
	private Button mBtnAddPerson;
	
	private DoubleEditPopWindow mDoubleEditPopWindow;
//	private DoubleEditDialog mDoubleEditPopWindow;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_layout);

        initView();
        
        initData();
    }

	
	public void initView()
	{
		mRootView = findViewById(R.id.rootView);
		
		mFriendListView = (ListView) findViewById(R.id.listview);
	

		
		mBtnAddPerson = (Button) findViewById(R.id.btn_addperson);
		mBtnAddPerson.setOnClickListener(this);
	}

	
	
	public void initData()
	{
		mApplication = (YunyouApplication) getApplication();
		mNetworkCenterEx = NetworkCenterEx.getInstance();
		
		if (mApplication.isNetworkAccount() == false)
		{
			mBtnAddPerson.setVisibility(View.GONE);
		}else{
		//	mFriendListView.setOnCreateContextMenuListener(this);
		}
	
		
		List<DeviceInfoEx> list = mApplication.getDeviceList();
		int size = list.size();
		for(int i = 0; i < size; i++)
		{
			FriendObject object = new FriendObject(list.get(i));
			mFriendObjects.add(object);
		}
		
//		int size = nameArrays.length;
//		for(int i = 0; i < size; i++)
//		{
//			FriendObject object = new FriendObject();
//			object.mUserName = nameArrays[i];
//			object.mIndividualSign = signArrays[i];
//			object.mPhone = phoneArrays[i];
//			mFriendObjects.add(object);
//		}

		mFriendAdapter = new FriendAdapter(this, mFriendObjects);
		mFriendAdapter.setPosCallback(this);
		if (mApplication.isNetworkAccount() == false)
		{
			mFriendAdapter.setShowStatus(false);
		}
	
		
		View headView = getLayoutInflater().inflate(R.layout.search_head_view, null);
		mEditTextSearch = (EditText) headView.findViewById(R.id.et_search);
		mBtnClear = (Button) headView.findViewById(R.id.bt_clear_text);
		//mFriendListView.addHeaderView(headView);
		
		mFriendListView.setAdapter(mFriendAdapter);
		
	

		mEditTextSearch.addTextChangedListener(this);
		mBtnClear.setOnClickListener(this);
		
		
		
		mDoubleEditPopWindow = new DoubleEditPopWindow(this, mRootView);
//		mDoubleEditPopWindow = new DoubleEditDialog(this);
		mDoubleEditPopWindow.setTitle(getResources().getString(R.string.popwindow_title_bind));
		mDoubleEditPopWindow.setText1String(R.string.popwindow_text_account);
		mDoubleEditPopWindow.setText2String(R.string.popwindow_text_pwd);
		mDoubleEditPopWindow.getEditText2().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		mDoubleEditPopWindow.setOnOKButtonListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				onAddPerson();
			}
		});
	}

	
	private void onAddPerson()
	{
		String name = mDoubleEditPopWindow.getEdit1String();
		String pwd = mDoubleEditPopWindow.getEdit2String();
		
		if (name.length() == 0)
		{
			Utils.showToast(this, R.string.toask_name_not_null);
			return ;
		}
		
		if (pwd.length() == 0)
		{
			Utils.showToast(this, R.string.toask_pwd_not_null);
			return ;
		}
		
		PublicType.UserBind userBind = new PublicType.UserBind();
		userBind.mUserName = name;
		userBind.mPassword = pwd;
			
		mNetworkCenterEx.StartRequestToServer(PublicType.USER_BIND_MASID, userBind, this);
	
		showRequestDialog(true);
	}

//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v,
//			ContextMenuInfo menuInfo) {
//		// TODO Auto-generated method stub
//		
//		menu.setHeaderTitle(R.string.friend_title_unbind);   
//
//		menu.add(0, 0, 0, R.string.friend_msg_unbind);
//
//	}
	

//	@Override
//	public boolean onContextItemSelected(MenuItem item) {
//		// TODO Auto-generated method stub
//	
//		AdapterContextMenuInfo  menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
//		curSelPos = menuInfo.position;
//
//		mNetworkCenterEx.StartRequestToServer(PublicType.USER_UNBIND_MASID, null, this);
//		
//		showRequestDialog(true);
//		
//		return true;
//
//	}
	
	
	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

		String strComments = mEditTextSearch.getText().toString().trim();
		if (strComments.length() == 0)
		{
			mBtnClear.setVisibility(View.GONE);
		}else{
			mBtnClear.setVisibility(View.VISIBLE);
		}
	}


	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}


	private void showToask(String tip)
	{
		Toast.makeText(this, tip, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.bt_clear_text:
			mEditTextSearch.setText("");
			break;
		case R.id.btn_addperson:
			addPerson();
			default:
				break;
		}
	}
	
	private void addPerson()
	{
//		Intent intent = new Intent();
//		intent.setClass(this, TestActivity.class);
//		startActivity(intent);
		
		mDoubleEditPopWindow.setEdit1String("");
		mDoubleEditPopWindow.setEdit2String("");
		mDoubleEditPopWindow.show(true);
//		mDoubleEditPopWindow.show();
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
		// TODO Auto-generated method stub


		String jsString = "null";
		if (dataPacket != null)
		{
			jsString = dataPacket.toString();
		}
		log.e("requestAction = " + Utils.toHexString(requestAction) + "\nResponseDataPacket = \n" +jsString);
		
		switch(requestAction)
		{
			case PublicType.USER_BIND_MASID:
				OnBindResult(dataPacket);
				break;
			case PublicType.USER_UNBIND_MASID:
				OnUnBindResult(dataPacket);
				break;
			default:
				break;
		}
		
		showRequestDialog(false);
		
		return true;
		
	}
	
	
	private void OnBindResult(ResponseDataPacket dataPacket)
	{
		if (dataPacket == null)
		{
			Utils.showToast(this, R.string.toask_tip_bind_fail);
			return ;
		}
		
		if (dataPacket.rsp == 0)
		{
			String msg = dataPacket.msg;
			if (msg.length() > 0)
			{
				Utils.showToast(this, msg);
			}else{
				Utils.showToast(this, R.string.toask_tip_bind_fail);
			}
			return ;
		}
		
		GloalType.DeviceInfoEx infoEx = new GloalType.DeviceInfoEx();
		try {
			infoEx.parseString(dataPacket.data.toString());
			
			List<DeviceInfoEx> list = mApplication.getDeviceList();
			list.add(infoEx);
			
			if (list.size() == 1)
			{
				mApplication.setCurDevice(list.get(0));
			}
			
			FriendObject friendObject = new FriendObject(infoEx);
			mFriendObjects.add(friendObject);
			
			mFriendAdapter.notifyDataSetChanged();
		
			Utils.showToast(this, R.string.toask_tip_bind_success);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void OnUnBindResult(ResponseDataPacket dataPacket)
	{
		if (dataPacket == null)
		{
			Utils.showToast(this, R.string.toask_tip_unbind_fail);
			return ;
		}
		
		if (dataPacket.rsp == 0)
		{
			Utils.showToast(this, R.string.toask_tip_unbind_fail);
			return ;
		}
		
	
		if (mCurSelPos != -1)
		{
			mFriendObjects.remove(mCurSelPos);
			mFriendAdapter.notifyDataSetChanged();
			
			List<DeviceInfoEx> list = mApplication.getDeviceList();
			DeviceInfoEx deviceInfoEx = list.remove(mCurSelPos);
			
			if (list.size() == 0)
			{
				mApplication.setCurDevice(null);
			}else {
				DeviceInfoEx curDeviceInfoEx =  mApplication.getCurDevice();
				if (curDeviceInfoEx.mDid.equals(deviceInfoEx.mDid)){			
					mApplication.setCurDevice(list.get(0));
					mApplication.performSelDev();
				}
			}
			
			Utils.showToast(this, R.string.toask_tip_unbind_success);
		}

	
	}


	@Override
	public void onPosCallback(int pos) {
		
		mCurSelPos = pos;
		
		if (mCurSelPos >= 0 && mCurSelPos < mFriendObjects.size()){
			FriendObject object = mFriendObjects.get(mCurSelPos);
			String did = object.mDid;
			String curDID = mApplication.getCurDid();
			if (did.equals(curDID)){
				Utils.showToast(this, R.string.friend_msg_dev_cant_unbind);
				return ;
			}
		}
		
		showSureDialog(true);		
			
	}
	
	private int mCurSelPos = -1;
	private Dialog mDialog = null;
	private void showSureDialog(boolean bShow)
	{
		if (mDialog != null)
		{
			mDialog.dismiss();
			mDialog = null;
		}
		
		OnClickListener onClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				log.e("");
				if (mCurSelPos >= 0 && mCurSelPos < mFriendObjects.size()){
					FriendObject object = mFriendObjects.get(mCurSelPos);
					String did = object.mDid;
					if (mApplication.getUserInfoEx().mType == 0){
						mNetworkCenterEx.StartRequestToServer(PublicType.USER_UNBIND_MASID, object.mDid, null, FriendActivity.this);			
						showRequestDialog(true);
					}else{
						Utils.showToast(FriendActivity.this, R.string.friend_msg_dev_cant_unbind);
					}				
				}
			}
		};

		if (bShow)
		{
			mDialog = DialogFactory.creatDoubleDialog(this, R.string.dialog_title_unbind, R.string.dialog_msg_message, onClickListener);
			mDialog.show();
		}
	
	}
	
}