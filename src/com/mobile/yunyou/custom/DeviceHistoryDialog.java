package com.mobile.yunyou.custom;

import java.util.List;

import android.content.Context;
import com.mobile.yunyou.device.adapter.DeviceHistoryAdapter;
import com.mobile.yunyou.model.DeviceSetType;

public class DeviceHistoryDialog extends AbstractListDialog{

	private DeviceHistoryAdapter mAdapter;
	private List<DeviceSetType.GpsStillTime> mData;
	
	public DeviceHistoryDialog(Context context, List<DeviceSetType.GpsStillTime> data) {
		super(context);
		// TODO Auto-generated constructor stub
		mData = data;
		initData();
	}	

	protected void initData() {
		// TODO Auto-generated method stub
		mAdapter = new DeviceHistoryAdapter(mContext, mData);
		mListView.setAdapter(mAdapter);
	
	}
	
	public void refreshData(List<DeviceSetType.GpsStillTime> data)
	{
		mAdapter.setData(data);
		int size = data.size();
		String title = "共有" + size + "条轨迹记录，请选择显示";
		setTitle(title);
	}
	
}
