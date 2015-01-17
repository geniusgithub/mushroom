package com.mobile.yunyou.device.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mobile.yunyou.R;
import com.mobile.yunyou.model.DeviceSetType;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class GuardTimeAdapter extends BaseAdapter implements OnCheckedChangeListener{

private List<DeviceSetType.GpsStillTime> data = new ArrayList<DeviceSetType.GpsStillTime>();
	
	private Context mContext;
	
    private boolean[] mSelectItems = null;
    
	public GuardTimeAdapter(Context context, List<DeviceSetType.GpsStillTime> data,  boolean[] flag)
	{
		mContext = context;
		if (data != null)
		{
			this.data = data;
			
		}
		
		if (flag != null)
		{
			mSelectItems = flag;
		}
	}
	
	public void refreshData(List<DeviceSetType.GpsStillTime> data,  boolean[] flag)
	{
	  	if (data != null)
    	{
	  		this.data = data;
	  		setSelectItem(flag);
    	}
		
	}
	
	 public void setSelectItem(boolean[] flag)
    {
    	if (flag != null)
    	{
    		mSelectItems = flag;
    		notifyDataSetChanged();
    	}
    
    }

    public boolean[] getSelectItem()
    {
    	return mSelectItems;
    }

    public void clear() {
    	 data.clear();
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
			view = LayoutInflater.from(mContext).inflate(R.layout.time_list_item_layout, null);
			holder = new ViewHolder();
			holder.tvTime = (TextView) view.findViewById(R.id.tvTime);
			holder.tvWeek = (TextView) view.findViewById(R.id.tvWeek);
			holder.checkBox = (CheckBox) view.findViewById(R.id.checkBox);
			holder.checkBox.setOnCheckedChangeListener(this);
			view.setTag(holder);
			
			TextView tvName = (TextView) view.findViewById(R.id.tvName);
			tvName.setVisibility(View.GONE);
		}else{
			holder = (ViewHolder) view.getTag();
		}
		
		holder.checkBox.setTag(pos);
		
		String timeString = data.get(pos).toStartTimeString() + "-" + data.get(pos).toEndTimeString();
		holder.tvTime.setText(timeString);	
		holder.tvWeek.setText(data.get(pos).toWeekString());
		holder.checkBox.setChecked(mSelectItems[pos]);
		return view;
	}
	
	
    static class ViewHolder { 
        public TextView tvTime;
        public TextView tvWeek;
        public CheckBox checkBox;
    }


	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		int pos = (Integer)buttonView.getTag();
		mSelectItems[pos] = isChecked;
	}
    
}
