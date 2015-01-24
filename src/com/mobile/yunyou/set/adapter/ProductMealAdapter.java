package com.mobile.yunyou.set.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobile.yunyou.R;
import com.mobile.yunyou.model.ProductType;
import com.mobile.yunyou.model.ProductType.GetPackage;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;

public class ProductMealAdapter extends BaseAdapter{

private static final CommonLog log = LogFactory.createLog();
	
	private List<ProductType.GetPackage> data = new ArrayList<ProductType.GetPackage>();
	
	private Context mContext;
	
	public ProductMealAdapter(Context context, List<ProductType.GetPackage> data)
	{
		mContext = context;
		this.data = data;
	}
	
	public void refreshData(List<ProductType.GetPackage> data)
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
	public View getView(int pos, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ViewHolder holder = null; 

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.meal_list_item_layout, null);
			
			holder = new ViewHolder();
			holder.subject = (TextView) convertView.findViewById(R.id.subject);
			holder.body = (TextView) convertView.findViewById(R.id.body);
			holder.price = (TextView) convertView.findViewById(R.id.price);
			convertView.setTag(holder);
		} else {
			holder =  (ViewHolder) convertView.getTag();
		}
		
		ProductType.GetPackage info = (GetPackage) getItem(pos);
		holder.subject.setText(info.mName);
		holder.body.setText(info.mDetail);
		holder.price.setText(String.valueOf(info.mPrice));

		return convertView;
	}
	
	
	static class ViewHolder {
		TextView subject;
		TextView body;
		TextView price;
	}

    
}
