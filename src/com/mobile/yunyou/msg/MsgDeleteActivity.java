package com.mobile.yunyou.msg;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.mobile.yunyou.R;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.activity.BaseActivity;
import com.mobile.yunyou.model.DeviceSetType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.PopWindowFactory;
import com.mobile.yunyou.util.Utils;

public class MsgDeleteActivity extends BaseActivity implements OnClickListener, ChatMsgViewAdapter.ItemSelectChangeListener,  IRequestCallback{

	
	private static final CommonLog log = LogFactory.createLog();

	private YunyouApplication mApplication;
	private NetworkCenterEx mNetworkCenterEx;
//	private MsgManager mMsgManager;
	private MsgRuestProxy msgRuestProxy;
	
	private View mRootView;
	private Button mBtnBack;
	private Button mBtnDel;
	private Button mBtnSelAll;
	private ListView mListView;
	private ChatMsgViewAdapter mAdapter;
	private LinkedList<ChatMsgEntity> mDataArrays = new LinkedList<ChatMsgEntity>();
	
	private boolean isSelectAllState = false;
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msg_delete_layout);
        
        
        initView();
        
        initData();
      
    }
	
	
	
	
	 @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	 }




	public void initView()
    {
		mRootView = findViewById(R.id.rootView);
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
		
		mBtnDel = (Button) findViewById(R.id.btn_del);
		mBtnDel.setOnClickListener(this);
		
		mBtnSelAll = (Button) findViewById(R.id.btn_selall);
		mBtnSelAll.setOnClickListener(this);
		
		mListView = (ListView) findViewById(R.id.listview);

    }
	     
    
    public void initData()
    {
    	mApplication = YunyouApplication.getInstance();
    	mNetworkCenterEx = NetworkCenterEx.getInstance();
    	
    	msgRuestProxy = MsgRuestProxy.getInstance(mNetworkCenterEx);
 //   	mMsgManager = MsgManager.getInstance();
//    	mMsgManager.resetMsgFromDatabase();	
 //   	List<DeviceSetType.DeviceMsgData> listData = mMsgManager.getAllData();
    	
    	List<DeviceSetType.DeviceMsgData> listData = msgRuestProxy.getDataArray();
    	for(int i = 0; i < listData.size(); i++)
    	{

    		ChatMsgEntity entity = new ChatMsgEntity(listData.get(i));
    	
    		mDataArrays.addLast(entity);
    	}

    	mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
    	mAdapter.setCheckbox(true);
    	mAdapter.setItemChangeListener(this);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(mAdapter);

    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_del:
			del();
			break;
		case R.id.btn_selall:
			if (isSelectAllState)
			{
				cancelSelAll();
			}else{
				selAll();
			}
			
			break;
		}
	}


	Set<Integer> idsSet = new HashSet<Integer>();
	private void del()
	{
		List<Integer> idsList = new ArrayList<Integer>();		
		idsSet.clear();
		
		int size = mDataArrays.size();
		for(int i = 0; i < size; i++)
		{
			ChatMsgEntity entity = mDataArrays.get(i);
			boolean state = entity.getSelectState();
			if (state)
			{
				idsSet.add(entity.getIDS());
			}
		}
		
		if (idsSet.size() == 0)
		{
			Utils.showToast(this, R.string.toask_tip_no_msg_del);
			return ;
		}
		
		
		DeviceSetType.DeviceDelMsg object = new DeviceSetType.DeviceDelMsg();		
		List<Integer> list = new ArrayList<Integer>();
		
		Iterator<Integer> iterator = idsSet.iterator();
		while(iterator.hasNext())
		{
			int id = iterator.next();
			list.add(id);
			log.e("id = " + id);
		}
		
		object.mList = list;
		mNetworkCenterEx.StartRequestToServer(DeviceSetType.DEVICE_DEL_MSG_MASID, object, this);
		
		showRequestDialog(true);
	}
	
	private void selAll()
	{
		mAdapter.selAll();
	}
	
	private void cancelSelAll()
	{
		mAdapter.cancelSelAll();
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
			case DeviceSetType.DEVICE_DEL_MSG_MASID:
				onDelMsgResult(dataPacket);
				break;
			default:
				break;
		}
		
		showRequestDialog(false);
		
		return true;
	}
	
	private void onDelMsgResult(ResponseDataPacket dataPacket)
	{

		if (dataPacket.rsp == 0)
		{
			String msg = dataPacket.msg;
			if (msg.length() > 0)
			{
				Utils.showToast(this, msg);
			}else{
				Utils.showToast(this, R.string.toask_tip_del_fail);
			}
			
			return ;
		}
		
		List<Integer> list = new ArrayList<Integer>();
		
		Iterator<Integer> iterator = idsSet.iterator();
		while(iterator.hasNext())
		{
			int id = iterator.next();
			list.add(id);
			log.e("del id = " + id);
		}
		
	//	mMsgManager.delMsg(list);
		
	//	mMsgManager.resetMsgFromDatabase();	
		
		msgRuestProxy.delMsgList(idsSet);
		mDataArrays.clear();
    	//List<DeviceSetType.DeviceMsgData> listData = mMsgManager.getAllData();
		List<DeviceSetType.DeviceMsgData> listData = msgRuestProxy.getDataArray();
		
    	for(int i = 0; i < listData.size(); i++)
    	{
    		ChatMsgEntity entity = new ChatMsgEntity(listData.get(i));
    	
    		mDataArrays.addLast(entity);
    	}

    	mAdapter.setData(mDataArrays);
		
    	setResult(RESULT_OK);
    	finish();
    	
    	Utils.showToast(this, R.string.toask_tip_msg_success);
	}
	
	
	
	private PopupWindow mPopupWindow = null;
//	private Dialog mDialog = null;
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


	private void setSelBtnState(boolean flag)
	{
		if (flag)
		{
			mBtnSelAll.setText(R.string.btn_cancel_select);
		}else{
			mBtnSelAll.setText(R.string.btn_selectall);
		}
	}

	@Override
	public void onSelectChange(boolean flag) {
		// TODO Auto-generated method stub
		isSelectAllState = flag;
		setSelBtnState(isSelectAllState);
	}

}
