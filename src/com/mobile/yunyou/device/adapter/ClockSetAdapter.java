package com.mobile.yunyou.device.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mobile.yunyou.R;
import com.mobile.yunyou.device.adapter.WhiteListAdapter.ViewHolder;
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

public class ClockSetAdapter extends BaseAdapter implements OnCheckedChangeListener{

	
	private List<DeviceSetType.ClockSet> data = new ArrayList<DeviceSetType.ClockSet>();
	
	private Context mContext;
	
	private boolean[] mSelectItems = null;
	   
	public ClockSetAdapter(Context context, List<DeviceSetType.ClockSet> data,   boolean[] flag)
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
	
	public void refreshData(List<DeviceSetType.ClockSet> data,   boolean[] flag)
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
			holder.tvName = (TextView) view.findViewById(R.id.tvName);
			holder.tvTime = (TextView) view.findViewById(R.id.tvTime);
			holder.tvWeek = (TextView) view.findViewById(R.id.tvWeek);
			holder.checkBox = (CheckBox) view.findViewById(R.id.checkBox);
			view.setTag(holder);
		}else{
			holder = (ViewHolder) view.getTag();
		}
		
		holder.tvName.setText(data.get(pos).mName);
		holder.tvTime.setText(data.get(pos).toTimeString());	
		holder.tvWeek.setText(data.get(pos).toWeekString());
		holder.checkBox.setChecked(mSelectItems[pos]);
		holder.checkBox.setTag(pos);
		holder.checkBox.setOnCheckedChangeListener(this);
		return view;
	}
	
	
    static class ViewHolder { 
    	public TextView tvName;
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
