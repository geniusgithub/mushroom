//package com.mobile.yunyou.map;
//
//import com.amap.mapapi.core.GeoPoint;
//import com.amap.mapapi.map.MapView;
//import com.mobile.yunyou.R;
//import com.mobile.yunyou.map.data.LocationEx;
//import com.mobile.yunyou.model.DeviceSetType;
//import com.mobile.yunyou.util.CommonLog;
//import com.mobile.yunyou.util.LogFactory;
//import com.mobile.yunyou.util.Utils;
//import com.mobile.yunyou.util.YunTimeUtils;
//
//import android.content.Context;
//import android.location.LocationManager;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//public class DeviceHistoryPopViewManager implements OnClickListener{
//
//	  private static final CommonLog log = LogFactory.createLog();
//		
//		private MapView mMapView;
//		private View mPopView;										//地图气泡
//		private Context mContext;
//		private TextView mContextTextView;
//		private TextView mTitleTextView;
//		
//		private int mScreenWidth = 0;
//		
//		public static interface PosType{
//			int IPT_HEAD = 0;
//			int IPT_MIDDLE = 1;
//			int IPT_END = 2;
//		}
//		
//		
//		public DeviceHistoryPopViewManager(Context context, MapView mapView, View popView)
//		{
//			
//			mContext = context;
//			mMapView = mapView;
//			mPopView = popView;
//			
//			mTitleTextView = (TextView) popView.findViewById(R.id.tv_title);
//			mContextTextView = (TextView) mPopView.findViewById(R.id.tv_content);
//		    ImageView btnCLose = (ImageView) mPopView.findViewById(R.id.iv_close);
//			btnCLose.setOnClickListener(this);		
//			
//			mScreenWidth = Utils.getScreenWidth(mContext);			
//		}
//		
//		public void togglePopViewShow(GeoPoint geoPoint, int x, int y,  DeviceSetType.DeviceHistoryResult object, int posType)
//		{
//			if (mPopView != null)
//			{
//				setContent(object, posType);
//				
//				MapView.LayoutParams geoLP = (MapView.LayoutParams)mPopView.getLayoutParams();
//				geoLP.x = x;
//				geoLP.y = y;
//				if (geoPoint != null)
//				{
//					geoLP.point = geoPoint;
//				}
//				mMapView.updateViewLayout(mPopView, geoLP);		
//				
//				if (mPopView.isShown())
//				{
//					showPopView(false);
//				}else{
//					showPopView(true);
//				}
//			}
//		}
//		
//		
//
//		public void showPopView(boolean bShow)
//		{
//			if (mPopView != null)
//			{
//				if (bShow)
//				{
//					mPopView.setVisibility(View.VISIBLE);
//				}else{
//					mPopView.setVisibility(View.GONE);
//			
//					
//				}
//				
//				if (mMapView != null)
//				{
//					mMapView.postInvalidate();
//				}
//			}
//		}
//		
//		public void setContent(DeviceSetType.DeviceHistoryResult object, int posType)
//		{
//			if (object == null)
//			{
//				mContextTextView.setText("");
//			}
//			
//			String content = getShowContent(object, posType);
//			mContextTextView.setText(content);
//			mTitleTextView.setText("轨迹信息");
//		}
//		
//		
//		public static String getShowContent(DeviceSetType.DeviceHistoryResult object, int posType)
//		{
//			if (object == null)
//			{
//				return "";
//			}
//			
//			try {
//				String lon_latString = "经纬度:";
//				String lat = String.valueOf(object.mLat);
//				log.e("lat = " + lat);
//				lat = lat.substring(0, Math.min(lat.indexOf(".") + 7, lat.length()));
//				String lon = String.valueOf(object.mLon);
//				log.e("lon = " + lon);
//				lon = lon.substring(0, Math.min(lon.indexOf(".") + 7, lon.length()));	
//				lon_latString += lon + "(E)," + lat + "(N)";
//			
//				
//				String time = "";
//				switch(posType){
//					case PosType.IPT_HEAD:
//						time = "起点:";
//						break;
//					case PosType.IPT_MIDDLE:
//						time = "时间:";
//						break;
//					case PosType.IPT_END:
//						time = "终点:";
//						break;
//				}
//				 time += object.mUploadTime;
//				
//				 String status = "状态：";
//				 if (object.mType == 0){
//					 status += "GPS定位";
//				 }else if (object.mType == 1){
//					 status += "基站定位";
//				 }else{
//					 status += "基站定位";
//				 }
//
//				 boolean moveFlag = isMove(object);
//				 if (moveFlag){
//					 status += "/移动";
//				 }else{
//					int interval = getInterval(object);
//					status += "/静止" + YunTimeUtils.getShowTimeIntervalString(interval);
//				 }
//
//			
//				
//				StringBuffer sBuffer = new StringBuffer();
//				sBuffer.append(time + "\n" + lon_latString + "\n" + status);	
//				return sBuffer.toString();
//			} catch (Exception e) {
//				e.printStackTrace(); 
//				
//			}
//			return "can't get the content";
//		}
//		
//		
//		public static boolean isMove(DeviceSetType.DeviceHistoryResult object)
//		{
//			int interval = getInterval(object);
//			if (interval > 60 || interval < 0)
//			{
//				return false;
//			}
//			
//			return true;
//		}
//		
//		
//		private static int getInterval(DeviceSetType.DeviceHistoryResult object){
//			long time1 = YunTimeUtils.getSecondsInCurrentYear(object.mCreateTime);
//			long time2 = YunTimeUtils.getSecondsInCurrentYear(object.mUploadTime);
//			
//			return (int) (time2 - time1);
//		}
//		
//		
//		@Override
//		public void onClick(View view) {
//			// TODO Auto-generated method stub
//			showPopView(false);
//		}
//}
