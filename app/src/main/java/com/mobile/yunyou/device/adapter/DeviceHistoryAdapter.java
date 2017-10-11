package com.mobile.yunyou.device.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobile.yunyou.R;
import com.mobile.yunyou.model.DeviceSetType;

public class DeviceHistoryAdapter  extends BaseAdapter{

	private List<DeviceSetType.GpsStillTime> data = new ArrayList<DeviceSetType.GpsStillTime>();
	private Context mContext;
	
	public DeviceHistoryAdapter(Context context, List<DeviceSetType.GpsStillTime> data)
	{
		mContext = context;
		this.data = data;
	}
	
	public void setData(List<DeviceSetType.GpsStillTime> data)
	{
		this.data = data;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return data.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}

	@Override
	public View getView(int pos, View view, ViewGroup parent) {

		ViewHolder holder = null;

		if (view == null)
		{
			view = LayoutInflater.from(mContext).inflate(R.layout.dev_history_list_item_layout, null);
			holder = new ViewHolder();
			holder.tvTime = (TextView) view.findViewById(R.id.tvTime);
			view.setTag(holder);
		}else{
			holder = (ViewHolder) view.getTag();
		}
		
		DeviceSetType.GpsStillTime object = (DeviceSetType.GpsStillTime) getItem(pos);
		String showString = object.toStartTimeString() + " - " + object.toEndTimeString();
		holder.tvTime.setText(showString);
		return view;
		
	}

	
    static class ViewHolder { 
    	public TextView tvTime;
    }
}
