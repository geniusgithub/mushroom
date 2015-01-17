package com.mobile.yunyou.device.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mobile.yunyou.R;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DeviceAdapter extends BaseAdapter{

	private static final CommonLog log = LogFactory.createLog();
	
	private List<DeviceItemObject> data = new ArrayList<DeviceItemObject>();
	
	private Context mContext;
	
	public DeviceAdapter(Context context, List<DeviceItemObject> data)
	{
		mContext = context;
		this.data = data;
	}
	
	public void refreshData(List<DeviceItemObject> data)
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
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		 ViewHolder holder = null; 


		if (view == null)
		{
			view = LayoutInflater.from(mContext).inflate(R.layout.device_list_item_layout, null);
			holder = new ViewHolder();
			holder.imDeviceIcon = (ImageView) view.findViewById(R.id.device_icon);
			holder.tvDeviceName = (TextView) view.findViewById(R.id.tvDeviceName);
			view.setTag(holder);
		}else{
			holder = (ViewHolder) view.getTag();
		}
		
		holder.imDeviceIcon.setImageDrawable(data.get(pos).mDrawable);	
		holder.tvDeviceName.setText(data.get(pos).mDeviceName);

		return view;
	}
	
	
    static class ViewHolder { 
        public ImageView imDeviceIcon;
        public TextView tvDeviceName;
    } 

}
