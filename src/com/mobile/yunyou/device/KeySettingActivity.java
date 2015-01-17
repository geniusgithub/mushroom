package com.mobile.yunyou.device;



import java.util.ArrayList;
import java.util.List;

import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.R;
import com.mobile.yunyou.custom.DoubleEditDialog;
import com.mobile.yunyou.custom.DoubleEditPopWindow;
import com.mobile.yunyou.custom.MultiChoicePopWindow;
import com.mobile.yunyou.model.DeviceSetType;
import com.mobile.yunyou.model.DeviceSetType.KeySet;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.PopWindowFactory;
import com.mobile.yunyou.util.Utils;
import com.mobile.yunyou.util.VertifyUtil;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class KeySettingActivity extends Activity implements OnClickListener, OnCheckedChangeListener, IRequestCallback{

	private static final CommonLog log = LogFactory.createLog();
	
	

	private NetworkCenterEx mNetworkCenterEx;
	
	private Context mContext;
	private Button mBtnBack;
	private Button mBtnSave;
	private View mRootView;
	
	
	private View mKeysetView1;
	private View mKeysetView2;
	private View mKeysetView3;
	private View mKeysetView4;
	
	private CheckBox mCheckBox1;
	private CheckBox mCheckBox2;
	private CheckBox mCheckBox3;
	private CheckBox mCheckBox4;
	
	private TextView mTvName1;
	private TextView mTvName2;
	private TextView mTvName3;
	private TextView mTvName4;
	
	private TextView mTvPhone1;
	private TextView mTvPhone2;
	private TextView mTvPhone3;
	private TextView mTvPhone4;
	

	private DoubleEditPopWindow mDoubleEditPopWindow;
//	private DoubleEditDialog mDoubleEditPopWindow;
	
	
	private final int COUNT = 4;
	private String[] mPhoneNumberArray = new String[COUNT];		//号码
	private String[] mPhoneNameArray = new String[COUNT];		//姓名
	private boolean[] mSwitch = new boolean[COUNT];			    //是否开启
	
	private List<DeviceSetType.KeySet> mKeySetsArray = new ArrayList<DeviceSetType.KeySet>();
	
	private YunyouApplication mApplication;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dev_keyset_layout);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra(DeviceIntentConstant.KEY_DATA_BUNDLE);
		if (bundle == null)
		{
			log.e("bundle == null");
			finish();
			return ;
		}
		
		mKeySetsArray = bundle.getParcelableArrayList(DeviceIntentConstant.KEY_LIST_DATA);
		if (mKeySetsArray.size() > COUNT)
		{
			log.e("mKeySetsArray.size() > COUNT");
			finish();
			return ;
		}
		
		initView();
		
		initData();
	}

	private void initView()
	{
		mRootView = findViewById(R.id.rootView);
		
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
		
		mBtnSave = (Button) findViewById(R.id.btn_save);
		mBtnSave.setOnClickListener(this);
		
		mCheckBox1 = (CheckBox) findViewById(R.id.checkBox1);
		mCheckBox2 = (CheckBox) findViewById(R.id.checkBox2);
		mCheckBox3 = (CheckBox) findViewById(R.id.checkBox3);
		mCheckBox4 = (CheckBox) findViewById(R.id.checkBox4);
		mCheckBox1.setOnCheckedChangeListener(this);
		mCheckBox2.setOnCheckedChangeListener(this);
		mCheckBox3.setOnCheckedChangeListener(this);
		mCheckBox4.setOnCheckedChangeListener(this);
		mCheckBox1.setTag(0);
		mCheckBox2.setTag(1);
		mCheckBox3.setTag(2);
		mCheckBox4.setTag(3);
		
		mTvPhone1 = (TextView) findViewById(R.id.tvPhone1);
		mTvPhone2 = (TextView) findViewById(R.id.tvPhone2);
		mTvPhone3 = (TextView) findViewById(R.id.tvPhone3);
		mTvPhone4 = (TextView) findViewById(R.id.tvPhone4);
		
		mTvName1 = (TextView) findViewById(R.id.tvName1);
		mTvName2 = (TextView) findViewById(R.id.tvName2);
		mTvName3 = (TextView) findViewById(R.id.tvName3);
		mTvName4 = (TextView) findViewById(R.id.tvName4);


		
		mKeysetView1 = findViewById(R.id.rl_keyset1);
		mKeysetView2 = findViewById(R.id.rl_keyset2);
		mKeysetView3 = findViewById(R.id.rl_keyset3);
		mKeysetView4 = findViewById(R.id.rl_keyset4);
		mKeysetView1.setOnClickListener(this);
		mKeysetView2.setOnClickListener(this);
		mKeysetView3.setOnClickListener(this);
		mKeysetView4.setOnClickListener(this);
	}
	
	private void initData()
	{
		mContext = this;
		mApplication = YunyouApplication.getInstance();
		
		mNetworkCenterEx = NetworkCenterEx.getInstance();
		
		mDoubleEditPopWindow = new DoubleEditPopWindow(this, mRootView);
//		mDoubleEditPopWindow = new DoubleEditDialog(this);
		mDoubleEditPopWindow.setTitle(getResources().getString(R.string.keyset_title_poptitle));
		mDoubleEditPopWindow.setOnOKButtonListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Integer index = (Integer) mDoubleEditPopWindow.getTag();
				onKeySet(index);
			}
		});
		
		
		refreshKeyData();
	}



	private void refreshKeyData()
	{
		for(int i  = 0; i < COUNT; i++)
		{
			mSwitch[i] = false;		
			mPhoneNameArray[i] = "";
			mPhoneNumberArray[i] = ""; 
		}
		
		for(int i = 0; i < mKeySetsArray.size(); i++)
		{
			KeySet keySet = mKeySetsArray.get(i);
			
			int keyIndex =  Integer.valueOf(keySet.mKey);
			keyIndex--;
			if (keyIndex >= 0 && keyIndex < COUNT)
			{
				mSwitch[keyIndex] = true;
				mPhoneNumberArray[keyIndex] = keySet.mPhoneNumber;
				mPhoneNameArray[keyIndex] = keySet.mName;
			}

		}
		
		
		mCheckBox1.setChecked(mSwitch[0]);
		mCheckBox2.setChecked(mSwitch[1]);
		mCheckBox3.setChecked(mSwitch[2]);
		mCheckBox4.setChecked(mSwitch[3]);

		mTvName1.setText(mPhoneNameArray[0]);
		mTvName2.setText(mPhoneNameArray[1]);
		mTvName3.setText(mPhoneNameArray[2]);
		mTvName4.setText(mPhoneNameArray[3]);
		
		mTvPhone1.setText(mPhoneNumberArray[0]);
		mTvPhone2.setText(mPhoneNumberArray[1]);
		mTvPhone3.setText(mPhoneNumberArray[2]);
		mTvPhone4.setText(mPhoneNumberArray[3]);
	
	 
	}
	
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_save:
			save();
			break;
		case R.id.rl_keyset1:
			showDoubleEditWindow(0);
			break;
		case R.id.rl_keyset2:
			showDoubleEditWindow(1);
			break;
		case R.id.rl_keyset3:
			showDoubleEditWindow(2);
			break;
		case R.id.rl_keyset4:
			showDoubleEditWindow(3);
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		int tag = (Integer) buttonView.getTag();
		mSwitch[tag] = isChecked;
	}
	
	
	private void save()
	{
		if (!mApplication.isDeviceOnline()){			
			Utils.showToast(this, R.string.toask_device_not_line);
			return ;
		}
		
		boolean vertify = true;
		for(int i = 0; i < COUNT; i++)
		{
			if (isVertifySuccess(i) == false)
			{
				vertify = false;
				break;
			}
		}
		
		if (vertify == false)
		{
			return ;
		}
		
		
		mKeySetsArray.clear();
		
		for(int i = 0; i < COUNT; i++)
		{
			updateListData(i);
		}
		
		DeviceSetType.KeySetGroup group = new DeviceSetType.KeySetGroup();
		group.mKeySetList = mKeySetsArray;
		
		mNetworkCenterEx.StartRequestToServer(DeviceSetType.DEVICE_KEYSET_MASID, group, this);
		
		showRequestDialog(true);
		
	}

	
	private void updateListData(int index)
	{
		if (index < 0 || index >= COUNT)
		{
			return ;
		}
		
		if (mSwitch[index] == false)
		{
			return ;
		}
		
		KeySet keySet = new KeySet();
		keySet.mKey = String.valueOf(index + 1);
		keySet.mPhoneNumber = mPhoneNumberArray[index];
		keySet.mName = mPhoneNameArray[index];
		
		mKeySetsArray.add(keySet);
		
	}
	
	private void showDoubleEditWindow(int index)
	{
		mDoubleEditPopWindow.setTag(index);
		mDoubleEditPopWindow.setEdit1String(mPhoneNameArray[index]);
		mDoubleEditPopWindow.setEdit2String(mPhoneNumberArray[index]);
		mDoubleEditPopWindow.show(true);
//		mDoubleEditPopWindow.show();
	}
	
	private void onKeySet(int index)
	{
	
		mPhoneNameArray[index] = mDoubleEditPopWindow.getEdit1String();
		mPhoneNumberArray[index] = mDoubleEditPopWindow.getEdit2String();
		
		switch(index)
		{
			case 0:
				mTvName1.setText(mPhoneNameArray[0]);
				mTvPhone1.setText(mPhoneNumberArray[0]);
				break;
			case 1:
				mTvName2.setText(mPhoneNameArray[1]);
				mTvPhone2.setText(mPhoneNumberArray[1]);
				break;
			case 2:
				mTvName3.setText(mPhoneNameArray[2]);
				mTvPhone3.setText(mPhoneNumberArray[2]);
				break;
			case 3:
				mTvName4.setText(mPhoneNameArray[3]);
				mTvPhone4.setText(mPhoneNumberArray[3]);
				break;
		}
	}
    
    
	private boolean isVertifySuccess(int index)
	{
		if (mSwitch[index] == false)
		{
			return true;
		}
		
		if (mPhoneNameArray[index].length() == 0)
		{
			Utils.showToast(this, getResources().getString(R.string.keyset_vertify_pre) + (index + 1) +
					getResources().getString(R.string.keyset_vertify_name_last));
			return false;
		}
		
		if (VertifyUtil.isMobileNumber(mPhoneNumberArray[index]) == false)
		{
			Utils.showToast(this, getResources().getString(R.string.keyset_vertify_pre) + (index + 1) +
					getResources().getString(R.string.keyset_vertify_phone_last));
			return false;
		}
		
		
		return true;
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
		
		showRequestDialog(false);
	
		
		
		String jsString = "null";
		if (jsString != null)
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
			case DeviceSetType.DEVICE_KEYSET_MASID:
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
