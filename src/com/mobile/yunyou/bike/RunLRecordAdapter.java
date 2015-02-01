package com.mobile.yunyou.bike;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobile.yunyou.R;
import com.mobile.yunyou.map.util.StringUtil;
import com.mobile.yunyou.model.BikeType;
import com.mobile.yunyou.util.YunTimeUtils;

public class RunLRecordAdapter  extends BaseAdapter{
	
	public List<BikeType.BikeLRecordResult> mRunRecordList = new ArrayList<BikeType.BikeLRecordResult>();

	private Context mContext;
	
	public RunLRecordAdapter(Context context, List<BikeType.BikeLRecordResult> data)
	{
		mContext = context;
		mRunRecordList = data;
	}
	
	public void setData(List<BikeType.BikeLRecordResult> data)
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
			holder.tvType = (TextView) view.findViewById(R.id.tv_type);
			holder.tvTimeRight = (TextView) view.findViewById(R.id.tv_timeRight);
			holder.tvDistance = (TextView) view.findViewById(R.id.tv_distance);
			view.setTag(holder);
		}else{
			holder = (ViewHolder) view.getTag();
		}
		
		BikeType.BikeLRecordResult object = (BikeType.BikeLRecordResult) getItem(pos);

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

		if (object.isLocal){
			holder.tvType.setText("上传中");
		}else{
			holder.tvType.setText("已上传");
		}
		
		holder.tvDistance.setText(getShowDistance(object.mTotalDistance));
		
		return view;
		
	}


	
    static class ViewHolder { 
    	public TextView tvTimeTop;
     	public TextView tvType;
    	public TextView tvTimeRight;
    	public TextView tvDistance;
    }
    
    public String getShowDistance(double distance){
    	String string = Double.valueOf(distance) + "公里";
    	return string;
    }
    
    
  
}
