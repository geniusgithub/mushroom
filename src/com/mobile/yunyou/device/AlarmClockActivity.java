package com.mobile.yunyou.device;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.mobile.yunyou.R;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.device.adapter.ClockSetAdapter;
import com.mobile.yunyou.device.adapter.WhiteListAdapter;
import com.mobile.yunyou.model.DeviceSetType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.PopWindowFactory;
import com.mobile.yunyou.util.Utils;

public class AlarmClockActivity extends Activity implements OnClickListener, OnItemClickListener, IRequestCallback, OnCreateContextMenuListener{

	private final int REQUEST_CODE_SET_CLOCK = 0x0001;
	
	private static final CommonLog log = LogFactory.createLog();
	
	private NetworkCenterEx mNetworkCenter;
	
	
	private View mRootView;
	private Button mBtnBack;
	private Button mBtnAddClock;
	private Button mBtnSave;
	
	
	private ListView mClockSetListView;
	private ClockSetAdapter mClockSetAdapter;
	private List<DeviceSetType.ClockSet> mClockSetItemArrays = new ArrayList<DeviceSetType.ClockSet>();
	private boolean mBoolean[];
	
	private int setClockPos = -1;
	
	private YunyouApplication mApplication;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dev_alarmclock_layout);
		
		
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra(DeviceIntentConstant.KEY_DATA_BUNDLE);
		if (bundle == null)
		{
			log.e("bundle == null");
			finish();
			return ;
		}
		
		mClockSetItemArrays = bundle.getParcelableArrayList(DeviceIntentConstant.KEY_LIST_DATA);
		
		
		initView();
		
		initData();
	}

	private void initView()
	{
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
		
		mBtnAddClock = (Button) findViewById(R.id.btn_addclock);
		mBtnAddClock.setOnClickListener(this);
		
		mBtnSave = (Button) findViewById(R.id.btn_save);
		mBtnSave.setOnClickListener(this);
		
		mClockSetListView = (ListView) findViewById(R.id.listview);
		mRootView = findViewById(R.id.rootView);
	}

	private final int MAX_COUNT = 100;
	private void initData()
	{
		mApplication = YunyouApplication.getInstance();
		mNetworkCenter = NetworkCenterEx.getInstance();
		
		mBoolean = new boolean[MAX_COUNT];
		
		clearBoolean(mBoolean);

		int size = mClockSetItemArrays.size();
		for(int i = 0; i < size; i++)
		{
			mBoolean[i] = mClockSetItemArrays.get(i).mSwitch == 1 ? true : false;
		}
		
		mClockSetAdapter = new ClockSetAdapter(this, mClockSetItemArrays, mBoolean);
		
		mClockSetListView.setAdapter(mClockSetAdapter);
		mClockSetListView.setOnItemClickListener(this);
		mClockSetListView.setOnCreateContextMenuListener(this);
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
		case R.id.btn_addclock:
			addClock();
			break;
		case R.id.btn_save:
			save();
			break;
		}
	}
	
	
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		
		
		menu.setHeaderTitle(R.string.clock_text_tip);   

		menu.add(0, 0, 0, R.string.btn_del);

	}
	
	
	

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
	
		AdapterContextMenuInfo  menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		int pos = menuInfo.position;

		mClockSetItemArrays.remove(pos);
		mClockSetAdapter.notifyDataSetChanged();
		
		return true;

	}

	private void addClock()
	{
		setClockPos = mClockSetItemArrays.size();
		
		Intent intent = new Intent();
		intent.setClass(this, SetClockActivity.class);
	
		DeviceSetType.ClockSet set = new DeviceSetType.ClockSet();
		set.mTimeString = "1200";
		set.mWeekString = "[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\"]";
		intent.putExtra(SetClockActivity.DATA_KEY,set);
		startActivityForResult(intent, REQUEST_CODE_SET_CLOCK);
	}

	private void save()
	{
		
		if (!mApplication.isDeviceOnline()){			
			Utils.showToast(this, R.string.toask_device_not_line);
			return ;
		}
		
		
		int size = mClockSetItemArrays.size();
		for(int i = 0; i < size; i++)
		{
			mClockSetItemArrays.get(i).mSwitch = (mBoolean[i] == true ? 1 : 0);
		}
		
		
		DeviceSetType.ClockSetGroup group = new DeviceSetType.ClockSetGroup();
		group.mClockSetList = mClockSetItemArrays;
		
		mNetworkCenter.StartRequestToServer(DeviceSetType.DEVICE_CLOCK_SET_MASID, group, this);
		
		showRequestDialog(true);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		goSetClockActivity(position);

	}
	
	private void goSetClockActivity(int index)
	{
		setClockPos = index;
		
		Intent intent = new Intent();
		intent.setClass(this, SetClockActivity.class);
		
		intent.putExtra(SetClockActivity.DATA_KEY, mClockSetItemArrays.get(index));
		startActivityForResult(intent, REQUEST_CODE_SET_CLOCK);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		switch(requestCode)
		{
		case REQUEST_CODE_SET_CLOCK:
			onSetClockResult(resultCode, data);
			break;
		}
	}
	
	
	private void onSetClockResult(int resultCode, Intent data)
	{
		if (resultCode == RESULT_OK)
		{
			DeviceSetType.ClockSet set = data.getParcelableExtra(SetClockActivity.DATA_KEY);
			if (set != null)
			{
				try {
					if (setClockPos < mClockSetItemArrays.size())
					{
						DeviceSetType.ClockSet clockSet = mClockSetItemArrays.get(setClockPos);
						clockSet.mName = set.mName;
						clockSet.mTimeString = set.mTimeString;
						clockSet.mWeekString = set.mWeekString;
					}else{
						mClockSetItemArrays.add(set);
						mBoolean[setClockPos] = true;
					}
				
					mClockSetAdapter.notifyDataSetChanged();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
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
			case DeviceSetType.DEVICE_CLOCK_SET_MASID:
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
