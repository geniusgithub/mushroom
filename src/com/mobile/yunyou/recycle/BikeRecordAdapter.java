package com.mobile.yunyou.recycle;
//package com.mobile.yunyou.bike;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.mobile.yunyou.R;
//import com.mobile.yunyou.model.DeviceSetType;
//import com.mobile.yunyou.model.BikeType.BikeRecordResult;
//import com.mobile.yunyou.model.DeviceSetType.DeviceHistoryResult;
//import com.mobile.yunyou.util.YunTimeUtils;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//public class BikeRecordAdapter  extends BaseAdapter{
//	
//	public List<BikeRecordResult> mBikeRecordResultList = new ArrayList<BikeRecordResult>();
//
//	private Context mContext;
//	
//	public BikeRecordAdapter(Context context, List<BikeRecordResult> data)
//	{
//		mContext = context;
//		mBikeRecordResultList = data;
//	}
//	
//	public void setData(List<BikeRecordResult> data)
//	{
//		mBikeRecordResultList = data;
//		notifyDataSetChanged();
//	}
//	@Override
//	public int getCount() {
//		// TODO Auto-generated method stub
//		return mBikeRecordResultList.size();
//	}
//
//	@Override
//	public Object getItem(int pos) {
//		// TODO Auto-generated method stub
//		return mBikeRecordResultList.get(pos);
//	}
//
//	@Override
//	public long getItemId(int pos) {
//		// TODO Auto-generated method stub
//		return pos;
//	}
//
//	@Override
//	public View getView(int pos, View view, ViewGroup parent) {
//
//		ViewHolder holder = null;
//
//		if (view == null)
//		{
//			view = LayoutInflater.from(mContext).inflate(R.layout.bikerecord_listitem_layout, null);
//			holder = new ViewHolder();
//			holder.tvTimeTop = (TextView) view.findViewById(R.id.tv_timeTop);
//			holder.tvTimeRight = (TextView) view.findViewById(R.id.tv_timeRight);
//			holder.tvDistance = (TextView) view.findViewById(R.id.tv_distance);
//			view.setTag(holder);
//		}else{
//			holder = (ViewHolder) view.getTag();
//		}
//		
//		BikeRecordResult object = (BikeRecordResult) getItem(pos);
//		holder.tvTimeTop.setText(object.mTimeTop);
//		holder.tvTimeRight.setText(object.mTimeRight);
//		holder.tvDistance.setText(getShowDistance(object.mDistance));
//		
//		return view;
//		
//	}
//
//	
//    static class ViewHolder { 
//    	public TextView tvTimeTop;
//    	public TextView tvTimeRight;
//    	public TextView tvDistance;
//    }
//    
//    public String getShowDistance(int distance){
//    	double value = distance * 1.0 / 1000;
//    	String string = Double.valueOf(value) + "公里";
//    	return string;
//    }
//    
//    
//  
//}
