//package com.mobile.yunyou.map;
//
//import com.amap.mapapi.core.GeoPoint;
//import com.amap.mapapi.core.OverlayItem;
//import com.amap.mapapi.map.ItemizedOverlay;
//import com.amap.mapapi.map.MapView;
//import com.amap.mapapi.poisearch.PoiSearch;
//import com.mobile.yunyou.R;
//import com.mobile.yunyou.YunyouApplication;
//import com.mobile.yunyou.map.data.LocationEx;
//import com.mobile.yunyou.util.CommonLog;
//import com.mobile.yunyou.util.LogFactory;
//import com.mobile.yunyou.util.YunTimeUtils;
//import com.mobile.yunyou.util.Utils;
//
//import android.R.integer;
//import android.content.Context;
//import android.location.LocationManager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//public class PopViewManager implements OnClickListener{
//
//	private static final CommonLog log = LogFactory.createLog();
//	
//	private MapView mMapView;
//	private View mPopView;										//地图气泡
//	private Context mContext;
//	private TextView mTitleTextView;
//	private TextView mContextTextView;
//	private ItemizedOverlay<OverlayItem> mItemizedOverlay;
//	
//	private int mScreenWidth = 0;
//	private MapPositionManager mPositionManager;
//	
//	public PopViewManager(Context context, MapView mapView, View popView)
//	{
//		
//		mContext = context;
//		mMapView = mapView;
//		mPopView = popView;
//		mTitleTextView = (TextView) popView.findViewById(R.id.tv_title);
////		mPopView = LayoutInflater.from(context).inflate(R.layout.map_poptip_layout, null);
////		mMapView.addView(mPopView,new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT, 
////															MapView.LayoutParams.WRAP_CONTENT,
////															null,
////															MapView.LayoutParams.BOTTOM_CENTER));  
////
////		mPopView.setVisibility(View.GONE);
//		
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
//	public void togglePopViewShow(boolean isDevice, ItemizedOverlay<OverlayItem> itemizedOverlay, GeoPoint geoPoint, int x, int y, LocationEx locationEx)
//	{
//		mItemizedOverlay = itemizedOverlay;
//		if (mPopView != null)
//		{
//			setContent(isDevice, locationEx);
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
//	public void setContent(Boolean isDevice, LocationEx locationEx)
//	{
//		if (locationEx == null)
//		{
//			mContextTextView.setText("");
//		}
//		
//		String content = getShowContent(isDevice, locationEx);
//		updateContentWidth(locationEx);
//		mContextTextView.setText(content);
//		mTitleTextView.setText("位置信息");
//	}
//	
//	private  void updateContentWidth(LocationEx locationEx)
//	{
//		if (locationEx  == null)
//		{
//			return ;
//		}
//		String lon_latString = "经纬度:";
//		String lat = String.valueOf(locationEx.getOffsetLat());
//		lat = lat.substring(0, Math.min(lat.indexOf(".") + 7, lat.length()));
//		String lon = String.valueOf(locationEx.getOffsetLon());
//		lon = lon.substring(0, Math.min(lon.indexOf(".") + 7, lon.length()));	
//		lon_latString += lon + "(E)," + lat + "(N)";
//		int width1 = Utils.getFitWidth(mContext, lon_latString, (int)mContextTextView.getTextSize());
//		
////		log.e("lon_latString = " + lon_latString + ", width = " + width1);
//		
//		String adressString = "位置:" + locationEx.getAdress();		
//		int width2 = Utils.getFitWidth(mContext, adressString, (int)mContextTextView.getTextSize());
//		
//	//	log.e("adressString = " + adressString + ", width = " + width2);
//		
//		int w = Math.max(width1, width2);
//		mContextTextView.setWidth(w);
//	}
//	
//	public void updatePopView(boolean isDevice, ItemizedOverlay<OverlayItem> itemizedOverlay,  GeoPoint geoPoint, int x, int y, LocationEx locationEx)
//	{
//		if (mPositionManager != null){
//			int statue = mPositionManager.getCurShowState();
//			if (statue != MapPositionManager.IViewConstant.IVC_DEVICE_POS){
//				return ;
//			}
//		}
//		if (mPopView.isShown() == false)
//		{
//			return ;
//		}
//		
//		if (mItemizedOverlay != itemizedOverlay)
//		{
//			return ;
//		}
//		
//		if (mPopView != null)
//		{
//			setContent(isDevice, locationEx);
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
//	public void clearForItemOverlay(ItemizedOverlay<OverlayItem> itemizedOverlay)
//	{
//		if (mPopView.isShown() == false)
//		{
//			return ;
//		}
//		
//		if (mItemizedOverlay != itemizedOverlay)
//		{
//			return ;
//		}
//		
//		showPopView(false);
//	}
//	
//	public static String getShowContent(boolean isDevice, LocationEx mLastLocation)
//	{
//		if (mLastLocation == null)
//		{
//			return "";
//		}
//		
//		try {
//			String lon_latString = "经纬度:";
//		//	String lat = String.valueOf(mLastLocation.getOffsetLat());
//			String lat = String.valueOf(mLastLocation.getLatitude());
//			lat = lat.substring(0, Math.min(lat.indexOf(".") + 7, lat.length()));
//		//	String lon = String.valueOf(mLastLocation.getOffsetLon());
//			String lon = String.valueOf(mLastLocation.getLongitude());
//			
//			lon = lon.substring(0, Math.min(lon.indexOf(".") + 7, lon.length()));	
//			lon_latString += lon + "(E)," + lat + "(N)";
//		
//			String adressString = "位置:" + mLastLocation.getAdress();		
//			String timeString = "时间:" + mLastLocation.getUpdateTimeString();
//
//			
//			String statusString = "状态:";
//			String provider = mLastLocation.getProvider();
//			
//			if (provider.equals(LocationManager.GPS_PROVIDER))
//			{
//				statusString += "GPS定位";
//			}else{
//				statusString += "基站定位";
//			}
//			
//			if (isDevice)
//			{
//				if (mLastLocation.getOnline() == 0)
//				{
//					statusString += "/离线";
//				}else
//				{
//					if (isMove(mLastLocation))
//					{
//						statusString += "/移动";
//					}else{
//						statusString += "/静止";
//						
//						int interval = getInterval(mLastLocation);		
//						String showString = YunTimeUtils.getShowTimeIntervalString(interval);
//						
//						statusString += showString;
//					}
//				}
//		
//				
//				
//			}
//			
//			
//			
//			StringBuffer sBuffer = new StringBuffer();
//			sBuffer.append(lon_latString + "\n" + 
//							adressString + "\n" + 
//							timeString + "\n" + 
//							statusString);
//			
//			
//			return sBuffer.toString();
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		
//		return "can't get the content";
//	}
//	
//	public static boolean isMove(LocationEx locationEx)
//	{
//		int interval = getInterval(locationEx);
//		if (interval > 60 || interval < 0)
//		{
//			return false;
//		}
//		
//		return true;
//	}
//	
//	public static int getInterval(LocationEx locationEx)
//	{
//		long secondTime1 = YunTimeUtils.getSecondsInCurrentYear(locationEx.getCreateTimeString());
//		long secondTime2 = YunTimeUtils.getSecondsInCurrentYear(locationEx.getUpdateTimeString());
//		log.e("secondTime1 = " + secondTime1 + ", secondTime2 = " + secondTime2);
//		
//		int interval = (int) (secondTime2 - secondTime1);
//		
//		return interval;
//	}
//	
//
//	@Override
//	public void onClick(View view) {
//		// TODO Auto-generated method stub
//		showPopView(false);
//	}
//	
//	
//}
