package com.mobile.yunyou.bike;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.mobile.yunyou.R;
import com.mobile.yunyou.map.util.StringUtil;
import com.mobile.yunyou.model.BikeType;
import com.mobile.yunyou.model.DeviceSetType;
import com.mobile.yunyou.model.BikeType.BikeRecordResult;
import com.mobile.yunyou.model.DeviceSetType.DeviceHistoryResult;
import com.mobile.yunyou.util.YunTimeUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RunRecordAdapter  extends BaseAdapter{
	
	public List<BikeType.RunRecordGroup> mRunRecordList = new ArrayList<BikeType.RunRecordGroup>();

	private Context mContext;
	
	public RunRecordAdapter(Context context, List<BikeType.RunRecordGroup> data)
	{
		mContext = context;
		mRunRecordList = data;
	}
	
	public void setData(List<BikeType.RunRecordGroup> data)
	{
		mRunRecordList = data;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mRunRecordList.size();
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return mRunRecordList.get(pos);
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
			view = LayoutInflater.from(mContext).inflate(R.layout.bikerecord_listitem_layout, null);
			holder = new ViewHolder();
			holder.tvTimeTop = (TextView) view.findViewById(R.id.tv_timeTop);
			holder.tvTimeRight = (TextView) view.findViewById(R.id.tv_timeRight);
			holder.tvDistance = (TextView) view.findViewById(R.id.tv_distance);
			view.setTag(holder);
		}else{
			holder = (ViewHolder) view.getTag();
		}
		
		BikeType.RunRecordGroup object = (BikeType.RunRecordGroup) getItem(pos);

		long timeStart = 0;
		try {
			timeStart = YunTimeUtils.getTimeMillison(object.mStartTime);
			String mTimeTop = YunTimeUtils.getFormatTime1(timeStart);
			holder.tvTimeTop.setText(mTimeTop);
			
			long timeMillsion = StringUtil.getTimeMillson(object.mStartTime, object.mEndTime);
			String timeRight =  YunTimeUtils.getFormatTimeInterval(timeMillsion);
			holder.tvTimeRight.setText(timeRight);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		
		
		holder.tvDistance.setText(getShowDistance(object.mTotalDistance));
		
		return view;
		
	}


	
    static class ViewHolder { 
    	public TextView tvTimeTop;
    	public TextView tvTimeRight;
    	public TextView tvDistance;
    }
    
    public String getShowDistance(int distance){
    	double value = distance * 1.0 / 1000;
    	String string = Double.valueOf(value) + "公里";
    	return string;
    }
    
    
  
}
