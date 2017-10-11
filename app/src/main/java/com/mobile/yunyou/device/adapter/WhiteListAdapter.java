package com.mobile.yunyou.device.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mobile.yunyou.R;
import com.mobile.yunyou.model.DeviceSetType;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;

public class WhiteListAdapter extends BaseAdapter implements OnItemClickListener{

private static final CommonLog log = LogFactory.createLog();
	
	private List<DeviceSetType.WhiteListSet> data = new ArrayList<DeviceSetType.WhiteListSet>();
	
	private Context mContext;
	
    private boolean[] mSelectItems = null;
	
	public WhiteListAdapter(Context context, List<DeviceSetType.WhiteListSet> data, boolean[] flag)
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
	
	public void refreshData(List<DeviceSetType.WhiteListSet> data, boolean[] flag)
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
			view = LayoutInflater.from(mContext).inflate(R.layout.whitelist_list_item_layout, null);
			holder = new ViewHolder();
			holder.tvName = (TextView) view.findViewById(R.id.tvName);
			holder.tvPhone = (TextView) view.findViewById(R.id.tvPhone);
			holder.checkBox = (CheckBox) view.findViewById(R.id.checkBox);
			view.setTag(holder);
			
			TextView tvPreName = (TextView) view.findViewById(R.id.tv_prename);
		    TextPaint paint1 = tvPreName.getPaint();
		    paint1.setFakeBoldText(true);
		    
			TextView tvPrePhone = (TextView) view.findViewById(R.id.tv_prephone);
		    TextPaint paint2 = tvPrePhone.getPaint();
		    paint2.setFakeBoldText(true);
			
		}else{
			holder = (ViewHolder) view.getTag();
		}
		
		holder.tvName.setText(data.get(pos).mName);	
		holder.tvPhone.setText(data.get(pos).mPhoneNumber);
		holder.checkBox.setChecked(mSelectItems[pos]);
		return view;
	}
	
	
    static class ViewHolder { 
        public TextView tvName;
        public TextView tvPhone;
        public CheckBox checkBox;
    }


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		mSelectItems[position] = !mSelectItems[position];
		notifyDataSetChanged();
//		displayBoolean();
	} 
    
	
//	private void displayBoolean()
//	{
//		for(int i = 0; i < 5; i++)
//		{
//			log.e("i = " + i + ", flag = " + mSelectItems[i]);
//		}
//	}
}
