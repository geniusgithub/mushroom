//package com.mobile.yunyou.map;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Point;
//import android.graphics.Paint.Style;
//import android.graphics.drawable.Drawable;
//
//import com.amap.mapapi.core.GeoPoint;
//import com.amap.mapapi.core.OverlayItem;
//import com.amap.mapapi.map.ItemizedOverlay;
//import com.amap.mapapi.map.MapView;
//import com.amap.mapapi.map.Projection;
//import com.mobile.yunyou.model.DeviceSetType;
//import com.mobile.yunyou.util.CommonLog;
//import com.mobile.yunyou.util.LogFactory;
//
//public class DeviceAreaOverlay extends ItemizedOverlay<OverlayItem>{
//
//	private static final CommonLog log = LogFactory.createLog();
//	
//	private List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
//	private Drawable marker;		
//	private Context mContext;
//	
//	
//	private DeviceSetType.DeviceAreaResult mObject;
//	private GeoPoint mLastGeoPoint;	
//	private DeviceAreaPopViewManager manager;
//	
//	public DeviceAreaOverlay(Context  context, Drawable drawable,DeviceAreaPopViewManager manager) {
//		super(boundCenterBottom(drawable));
//		// TODO Auto-generated constructor stub
//		
//		marker = drawable;
//		mContext = context;
//		this.manager = manager;
//		
//		initPaint();
//		populate();
//	}
//
//	private Paint circlePaint = new Paint();
//	private void initPaint()
//	{
//		circlePaint = new Paint();  
//		circlePaint.setAntiAlias(true);  
//		circlePaint.setColor(Color.BLUE);  
//		circlePaint.setAlpha(50);  
//		circlePaint.setStyle(Style.FILL);
//
//	}
//	@Override
//	protected OverlayItem createItem(int pos) {
//		// TODO Auto-generated method stub
//		return mGeoList.get(pos);
//	}
//
//	@Override
//	public int size() {
//		// TODO Auto-generated method stub
//		return mGeoList.size();
//	}
//	
//	
//	public void setArea(DeviceSetType.DeviceAreaResult object)
//	{
//		if (object != null)
//		{
//			log.e("DeviceAreaOverlay setArea...lat = " + object.mLat + ", lon = " + object.mLon);
//			mObject = object;
//			
//			mGeoList.clear();
//			mLastGeoPoint = new GeoPoint((int) (mObject.mOffsetLat * 1E6),
//					(int) (mObject.mOffsetLon * 1E6));
//			
//			
//			mGeoList.add(new OverlayItem(mLastGeoPoint, "", ""));
//		
//			populate();	
//			
//		
//		}
//	}
//	
//	public void clear()
//	{
//		log.e("DevicePosOverlay clear...");
//		mGeoList.clear();
//		populate();
//		mObject = null;
//		mLastGeoPoint = null;	
//	}
//	
//
//	public DeviceSetType.DeviceAreaResult getArea()
//	{
//		return mObject;
//	}
//	
//	public GeoPoint getGeoPoint()
//	{
//		return mLastGeoPoint;
//	}
//
//	@Override
//	public void draw(Canvas canvas, MapView mapView, boolean flag) {
//		// TODO Auto-generated method stub		
//        super.draw(canvas, mapView, flag);
//        
//    
//        if (mLastGeoPoint != null && mObject != null)
//        {
//
//            Projection projection = mapView.getProjection(); 
//            Point centerPoint = projection.toPixels(mLastGeoPoint, null);
//            int radius = (int) projection.metersToEquatorPixels((float) (mObject.mRadius * 1.0));
//            canvas.drawCircle(centerPoint.x, centerPoint.y,radius, circlePaint);
//        }
// 
//	}
//
//	public void showPopView()
//	{
//		log.e("showPopView");
//		if (manager != null && marker != null)
//		{
//			int x = marker.getBounds().centerX();
//			int y = -marker.getBounds().height();
//			
//			try {
//				manager.showPopView(mLastGeoPoint, x, y, mObject);
//			} catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//			}
//			
//		}
//	}
//	
//	@Override
//	protected boolean onTap(int pos) {
//		// TODO Auto-generated method stub
//				
//			if (manager != null && marker != null)
//			{
//				int x = marker.getBounds().centerX();
//				int y = -marker.getBounds().height();
//				GeoPoint geoPoint = mGeoList.get(pos).getPoint();				
//				manager.togglePopViewShow(geoPoint, x, y, mObject);
//			}
//	
//		return true;
//		
//	}
//	
//	
//}
