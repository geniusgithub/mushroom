package com.mobile.yunyou.device;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.mobile.yunyou.R;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.custom.DoubleEditPopWindow;
import com.mobile.yunyou.device.adapter.WhiteListAdapter;
import com.mobile.yunyou.model.DeviceSetType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.PopWindowFactory;
import com.mobile.yunyou.util.Utils;
import com.mobile.yunyou.util.VertifyUtil;


public class WhiteListActivity extends Activity implements OnClickListener, IRequestCallback{

	private static final CommonLog log = LogFactory.createLog();
	
	private NetworkCenterEx mNetworkCenterEx;
	
	
	private Context mContext;
	private View mRootView;
	private Button mBtnBack;
	private Button mBtnAdd;
	private Button mBtnDel;
	private Button mBtnSave;
	
	private ListView mWhitelistListView;
	private WhiteListAdapter mWhiteListAdapter;
	private List<DeviceSetType.WhiteListSet> mWhiteListItemArrays = new ArrayList<DeviceSetType.WhiteListSet>();
	private boolean mBoolean[];
	
	private DoubleEditPopWindow mDoubleEditPopWindow;
//	private DoubleEditDialog mDoubleEditPopWindow;
	
	private YunyouApplication mApplication;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dev_whitelist_layout);
		
		
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra(DeviceIntentConstant.KEY_DATA_BUNDLE);
		if (bundle == null)
		{
			log.e("bundle == null");
			finish();
			return ;
		}
		
		mWhiteListItemArrays = bundle.getParcelableArrayList(DeviceIntentConstant.KEY_LIST_DATA);
		
		
		initView();
		
		initData();
	}

	private void initView()
	{
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
		
		mBtnAdd = (Button) findViewById(R.id.btn_addperson);
		mBtnAdd.setOnClickListener(this);
		
		mBtnDel = (Button) findViewById(R.id.btn_del);
		mBtnDel.setOnClickListener(this);
		
		mBtnSave = (Button) findViewById(R.id.btn_save);
		mBtnSave.setOnClickListener(this);
		
		mWhitelistListView = (ListView) findViewById(R.id.listview);
		mRootView = findViewById(R.id.rootView);
	}
	

	private final int  MAX_COUNT = 100;
	private void initData()
	{
		mApplication = YunyouApplication.getInstance();
		mNetworkCenterEx = NetworkCenterEx.getInstance();
		
		mContext = this;
		mBoolean = new boolean[MAX_COUNT];
		clearBoolean(mBoolean);
		
		
		mWhiteListAdapter = new WhiteListAdapter(this, mWhiteListItemArrays, mBoolean);
		
		mWhitelistListView.setAdapter(mWhiteListAdapter);
		mWhitelistListView.setOnItemClickListener(mWhiteListAdapter);
		
		
		mDoubleEditPopWindow = new DoubleEditPopWindow(this, mRootView);
	//	mDoubleEditPopWindow = new DoubleEditDialog(this);
		mDoubleEditPopWindow.setTitle(getResources().getString(R.string.whitelist_title_poptitle));
		mDoubleEditPopWindow.setOnOKButtonListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onAddPerson();
			}
		});
	}
	
	private void clearBoolean(boolean flag[])
	{
		int size = flag.length;
		for(int i = 0; i < size; i++)
		{
			flag[i] = false; 
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_addperson:
			addPerson();
			break;
		case R.id.btn_del:
			delPerson();
			break;
		case R.id.btn_save:
			savePerson();
			break;
		}
	}

	private void addPerson()
	{
		mDoubleEditPopWindow.setEdit1String("");
		mDoubleEditPopWindow.setEdit2String("");
		mDoubleEditPopWindow.show(true);
	//	mDoubleEditPopWindow.show();
	}
	
	private void delPerson()
	{
		mBoolean = mWhiteListAdapter.getSelectItem();
		int size = mWhiteListItemArrays.size();
		
		
		List<DeviceSetType.WhiteListSet> newList = new ArrayList<DeviceSetType.WhiteListSet>();
		
		
		for(int i = 0; i < size; i++)
		{
			if (mBoolean[i] == false)
			{
				newList.add(mWhiteListItemArrays.get(i));
			}
		}
		
		mWhiteListItemArrays = newList;
		clearBoolean(mBoolean);
		
		mWhiteListAdapter.refreshData(mWhiteListItemArrays, mBoolean);
	}
	

	
	private void savePerson()
	{
		if (!mApplication.isDeviceOnline()){			
			Utils.showToast(this, R.string.toask_device_not_line);
			return ;
		}
		
		
		DeviceSetType.WhiteListSetGroup group = new DeviceSetType.WhiteListSetGroup();
		group.mWhiteListSetList = mWhiteListItemArrays;
		
		mNetworkCenterEx.StartRequestToServer(DeviceSetType.DEVICE_WHITELIST_SET_MASID, group, this);
		
		showRequestDialog(true);
	}
	
	
	private void onAddPerson()
	{
		String name = mDoubleEditPopWindow.getEdit1String();
		String phone = mDoubleEditPopWindow.getEdit2String();
		
		if (name.length() == 0)
		{
			showToast(getResources().getString(R.string.toask_name_not_null));
			return ;
		}
		
		if (phone.length() == 0)
		{
			showToast(getResources().getString(R.string.toask_phone_not_null));
			return ;
		}
		
		
		if (VertifyUtil.isMobileNumber(phone) == false)
		{
			showToast(getResources().getString(R.string.keyset_vertify_phone_last));
			return ;
		}
		
		
		
		DeviceSetType.WhiteListSet set = new DeviceSetType.WhiteListSet();
		set.mName = name;
		set.mPhoneNumber = phone;
		
		mWhiteListItemArrays.add(set);
		
		
		mBoolean[mWhiteListItemArrays.size() - 1] = false;
		
		mWhiteListAdapter.refreshData(mWhiteListItemArrays, mBoolean);
	}
	
//	private void displayBoolean()
//	{
//		for(int i = 0; i < 5; i++)
//		{
//			log.e("i = " + i + ", flag = " + mBoolean[i]);
//		}
//	}
	

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
	
	
	
	private void showToast(String tip)
	{
		Toast.makeText(this, tip, Toast.LENGTH_SHORT).show();
	}
	
	private void showToast(int tip)
	{
		Toast.makeText(this, tip, Toast.LENGTH_SHORT).show();
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
		if (dataPacket == null)
		{
			Utils.showToast(this, R.string.set_data_fail);
			return false;
		}
		
		switch(requestAction)
		{
			case DeviceSetType.DEVICE_WHITELIST_SET_MASID:
			{
				
				if (dataPacket.rsp == 0)
				{
					String msg = dataPacket.msg;
					if (msg.length() > 0)
					{
						Utils.showToast(this, msg);
					}else{
						Utils.showToast(this, R.string.set_data_fail);
					}

					return false;
				}
				
				Utils.showToast(this, R.string.set_data_success);
				finish();
	
			}
			break;
		}
		
		
		return true;
		
	}
}
