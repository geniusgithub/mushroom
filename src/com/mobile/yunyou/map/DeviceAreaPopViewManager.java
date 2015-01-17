//package com.mobile.yunyou.map;
//
//import java.util.List;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.amap.mapapi.core.GeoPoint;
//import com.amap.mapapi.map.MapView;
//import com.mobile.yunyou.R;
//import com.mobile.yunyou.model.BaseType;
//import com.mobile.yunyou.model.DeviceSetType;
//import com.mobile.yunyou.util.CommonLog;
//import com.mobile.yunyou.util.LogFactory;
//import com.mobile.yunyou.util.Utils;
//
//public class DeviceAreaPopViewManager implements OnClickListener{
//
//    private static final CommonLog log = LogFactory.createLog();
//	
//	private MapView mMapView;
//	private View mPopView;										//地图气泡
//	private Context mContext;
//	private TextView mContextTextView;
//	private TextView mTitleTextView;
//	
//	private int mScreenWidth = 0;
//	private MapPositionManager mPositionManager;
//	
//	public DeviceAreaPopViewManager(Context context, MapView mapView, View popView)
//	{
//		
//		mContext = context;
//		mMapView = mapView;
//		mPopView = popView;
//		
//		mTitleTextView = (TextView) popView.findViewById(R.id.tv_title);
//		mContextTextView = (TextView) mPopView.findViewById(R.id.tv_content);
//	    ImageView btnCLose = (ImageView) mPopView.findViewById(R.id.iv_close);
//		btnCLose.setOnClickListener(this);
//		
//		
//		mScreenWidth = Utils.getScreenWidth(mContext);
//		
//	}
//	
//	public void bindMapPosMng(MapPositionManager manager){
//		mPositionManager = manager;
//	}
//
//	
//	public void togglePopViewShow(GeoPoint geoPoint, int x, int y,  DeviceSetType.DeviceAreaResult object)
//	{
//		if (mPopView != null)
//		{
//			setContent(object);
//			
//			MapView.LayoutParams geoLP = (MapView.LayoutParams)mPopView.getLayoutParams();
//			geoLP.x = x;
//			geoLP.y = y;
//			if (geoPoint != null)
//			{
//				geoLP.point = geoPoint;
//			}
//			mMapView.updateViewLayout(mPopView, geoLP);		
//			
//			if (mPopView.isShown())
//			{
//				showPopView(false);
//			}else{
//				showPopView(true);
//			}
//		}
//	}
//	
//	
//
//	public void showPopView(boolean bShow)
//	{
//		if (mPopView != null)
//		{
//			if (bShow)
//			{
//				mPopView.setVisibility(View.VISIBLE);
//			}else{
//				mPopView.setVisibility(View.GONE);
//		
//				
//			}
//			
//			if (mMapView != null)
//			{
//				mMapView.postInvalidate();
//			}
//		}
//	}
//	
//	public void setContent(DeviceSetType.DeviceAreaResult object)
//	{
//		if (object == null)
//		{
//			mContextTextView.setText("");
//		}
//		
//		String content = getShowContent(object);
//		mContextTextView.setText(content);
//		mTitleTextView.setText("围栏信息");
//	}
//	
//	public void showPopView(GeoPoint geoPoint, int x, int y, DeviceSetType.DeviceAreaResult object)
//	{
//		mPopView.setVisibility(View.VISIBLE);
//		updatePopView(geoPoint, x, y, object);
//	}
//	
//	public void updatePopView(GeoPoint geoPoint, int x, int y, DeviceSetType.DeviceAreaResult object)
//	{
//
//		if (mPositionManager != null){
//			int statue = mPositionManager.getCurShowState();
//			if (statue != MapPositionManager.IViewConstant.IVC_AREA_POS){
//				return ;
//			}
//		}
//	
//		if (mPopView.isShown() == false)
//		{
//	
//			return ;
//		}
//		
//		if (mPopView != null)
//		{
//			setContent(object);
//			
//			MapView.LayoutParams geoLP = (MapView.LayoutParams)mPopView.getLayoutParams();
//			geoLP.x = x;
//			geoLP.y = y;
//			if (geoPoint != null)
//			{
//				geoLP.point = geoPoint;
//			}
//			
//			mMapView.updateViewLayout(mPopView, geoLP);		
//			log.e("updatePopView");
//		}
//		
//	}
//	
//	public static String getShowWeek(String time){
//		String result = "";
//		try {
//			BaseType.WeekTime1 weekTime1 = new BaseType.WeekTime1();
//			weekTime1.parseString(time);
//			result = weekTime1.toDisplayString1();
//			
//		} catch (Exception e) {
//			
//		}
//		return result;
//	}
//	
//	public static String getShowType(String type){
//		if (type == null){
//			return "";
//		}
//		String result = "";
//		if (type.equals("forbid")){
//			result = "禁区围栏";
//		}else if (type.equals("signin")){
//			result = "签到围栏";
//		}else if (type.equals("permit")){
//			result = "防护围栏";
//		}
//		return result;
//	}
//	
//	public static String getShowContent(DeviceSetType.DeviceAreaResult object)
//	{
//		if (object == null)
//		{
//			return "";
//		}
//		
//		try {
//			String lon_latString = "经纬度:";
//			String lat = String.valueOf(object.mLat);
//			lat = lat.substring(0, Math.min(lat.indexOf(".") + 7, lat.length()));
//			String lon = String.valueOf(object.mLon);
//			lon = lon.substring(0, Math.min(lon.indexOf(".") + 7, lon.length()));	
//			lon_latString += lon + "(E)," + lat + "(N)";
//			
//			String name = "名称:" + object.mName;
//			String type = "类型:" + getShowType(object.mType);
//			String radius =  "半径:" + String.valueOf(object.mRadius) + "米";
//			String time = "时间:" + object.mStartTime + " - " + object.mEndTime;
//			String wk = "每周:" + getShowWeek(object.mWK);
//						
//			try {
//				StringBuffer stringStartBuffer = new StringBuffer(object.mStartTime);
//				stringStartBuffer.insert(2, ":");
//				
//				StringBuffer stringEndBuffer = new StringBuffer( object.mEndTime);
//				stringEndBuffer.insert(2, ":");
//				
//				time = "时间:" +stringStartBuffer + " - " + stringEndBuffer;
//			} catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//			}
//			
//				
//			StringBuffer sBuffer = new StringBuffer();
//			sBuffer.append(lon_latString + "\n" + 
//					name + "\n" + 
//					type + "\n" + 
//					radius + "\n" + 
//					time + "\n" + 
//					wk);
//				
//				
//				return sBuffer.toString();
//		} catch (Exception e) {
//			//e.printStackTrace(); TODO: handle exception
//		}
//		return "can't get the content";
//	}
//	
//
//	@Override
//	public void onClick(View view) {
//		// TODO Auto-generated method stub
//		showPopView(false);
//	}
//	
//}
