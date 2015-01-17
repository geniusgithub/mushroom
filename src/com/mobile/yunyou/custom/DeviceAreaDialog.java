package com.mobile.yunyou.custom;

import java.util.List;

import com.mobile.yunyou.device.adapter.DeviceAreaAdapter;
import com.mobile.yunyou.model.DeviceSetType;

import android.content.Context;

public class DeviceAreaDialog extends AbstractListDialog{

	
	private DeviceAreaAdapter mAdapter;
	private List<DeviceSetType.DeviceAreaResult> mData;
	
	public DeviceAreaDialog(Context context, List<DeviceSetType.DeviceAreaResult> data) {
		super(context);
		// TODO Auto-generated constructor stub
		mData = data;
		initData();
	}	

	protected void initData() {
		// TODO Auto-generated method stub
		mAdapter = new DeviceAreaAdapter(mContext, mData);
		mListView.setAdapter(mAdapter);
	
	}
	
	public void refreshData(List<DeviceSetType.DeviceAreaResult> data)
	{
		mAdapter.setData(data);
		int size = data.size();
		String title = "共有" + size + "条围栏记录，请选择显示";
		setTitle(title);
	}

}
