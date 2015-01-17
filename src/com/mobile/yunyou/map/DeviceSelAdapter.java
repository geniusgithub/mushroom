package com.mobile.yunyou.map;

import java.util.ArrayList;
import java.util.List;

import com.mobile.yunyou.R;
import com.mobile.yunyou.model.GloalType;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DeviceSelAdapter extends BaseAdapter implements OnItemClickListener{

	public static interface SelChangeListener
	{
		public void onItemChange(GloalType.DeviceInfoEx deviceInfoEx);
		
	}
	
	 private Context mContext;   
	 private List<GloalType.DeviceInfoEx> mObjects = new ArrayList<GloalType.DeviceInfoEx>();
	 private int mSelectItem = 0;
	 private SelChangeListener mChangeListener;
	    
	 private LayoutInflater mInflater;

	  
	    public DeviceSelAdapter(Context context) {
	        init(context);
	    }
	    
	    public DeviceSelAdapter(Context context,  List<GloalType.DeviceInfoEx> objects) {
	        init(context);
	        if (objects != null)
	    	{
	        	mObjects = objects;
	    	}
	    }

	    private void init(Context context) {
	        mContext = context;
	        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    }
	    
	    public void refreshData(List<GloalType.DeviceInfoEx> objects)
	    {
	    	if (objects != null)
	    	{
	    		mObjects = objects;
	    		setSelectItem(0);
	    	}
	    }
	    
	    public void setSelectItem(int selectItem)
	    {
	    	if (selectItem >= 0 && selectItem < mObjects.size())
	    	{
	    		mSelectItem = selectItem;
	    		notifyDataSetChanged();
	    	}
	    
	    }

	    public int getSelectItem()
	    {
	    	return mSelectItem;
	    }
	    
	    public void setListener(SelChangeListener listener)
	    {
	    	mChangeListener = listener;
	    }

	    public void clear() {
	         mObjects.clear();
	         mSelectItem = 0;
	         notifyDataSetChanged();
	    }

	    
	    public int getCount() {
	        return mObjects.size();
	    }

	    public GloalType.DeviceInfoEx getItem(int position) {
	        return mObjects.get(position);
	    }

	    public int getPosition(GloalType.DeviceInfoEx item) {
	        return mObjects.indexOf(item);
	    }


	    public long getItemId(int position) {
	        return position;
	    }


	    public View getView(int position, View convertView, ViewGroup parent) {   
		
	    	 ViewHolder viewHolder;
	    	 
		     if (convertView == null) {
		    	 convertView = mInflater.inflate(R.layout.choice_list_item_layout, null);
		         viewHolder = new ViewHolder();
		         viewHolder.mTextView = (TextView) convertView.findViewById(R.id.textView);
		         viewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.checkBox);
		         convertView.setTag(viewHolder);
		         
		         viewHolder.mCheckBox.setButtonDrawable(R.drawable.selector_checkbox3);

		     } else {
		         viewHolder = (ViewHolder) convertView.getTag();
		     }
		   
		     viewHolder.mCheckBox.setChecked(mSelectItem == position);	  
		     
		     GloalType.DeviceInfoEx item = getItem(position);
			 viewHolder.mTextView.setText(item.mAlias);

		     return convertView;
	    }

	    public static class ViewHolder
	    {
	    	public TextView mTextView;
	    	public CheckBox mCheckBox;
	    }

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			if (position != mSelectItem)
			{
				mSelectItem = position;
				notifyDataSetChanged();
				if (mChangeListener != null)
				{
					mChangeListener.onItemChange(mObjects.get(position));
				}
			}
		}  
		
}
