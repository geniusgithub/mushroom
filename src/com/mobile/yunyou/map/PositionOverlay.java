//package com.mobile.yunyou.map;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//
//import com.amap.mapapi.core.GeoPoint;
//import com.amap.mapapi.core.OverlayItem;
//import com.amap.mapapi.map.ItemizedOverlay;
//import com.amap.mapapi.map.MapView;
//import com.amap.mapapi.map.Projection;
//import com.mobile.yunyou.R;
//import com.mobile.yunyou.map.data.LocationEx;
//import com.mobile.yunyou.util.CommonLog;
//import com.mobile.yunyou.util.LogFactory;
//
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Point;
//import android.graphics.Paint.Style;
//import android.graphics.drawable.Drawable;
//import android.location.Location;
//import android.location.LocationManager;
//import android.provider.ContactsContract.Contacts.Data;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//
//
//public class PositionOverlay extends ItemizedOverlay<OverlayItem>{
//
//	private static final CommonLog log = LogFactory.createLog();
//	
//	private List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
//	private Drawable marker;		
//	private Context mContext;
//	
//	
//	private LocationEx mLastLocation;
//	private GeoPoint mLastGeoPoint;
//	private PopViewManager mPopViewManager;
////	private Paint mPaint;
////	private int mCircColor;			
//
//	
//	
//	public PositionOverlay(Context  context, Drawable drawable, PopViewManager popViewManager) {
//		super(boundCenterBottom(drawable));
//		// TODO Auto-generated constructor stub
//		
//		marker = drawable;
//		mContext = context;
//		mPopViewManager = popViewManager;
//		
////		mCircColor = Color.argb(50, 50, 50, 50);
////		
////		mPaint = new Paint();
////		mPaint.setAntiAlias(true);
////		mPaint.setColor(mCircColor);
////		mPaint.setStyle(Style.FILL);
//		
//		populate();
//	}
//
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
//	public void setLocation(LocationEx location)
//	{
//		if (location != null)
//		{
//		
//			mLastLocation = location;
//		//	log.e("getLatitude = " + mLastLocation.getLatitude() + ", getLongitude = " + mLastLocation.getLongitude());
//			
//			mGeoList.clear();
//			mLastGeoPoint = new GeoPoint((int) (mLastLocation.getOffsetLat() * 1E6),
//					(int) (mLastLocation.getOffsetLon() * 1E6));
//			
//			
//			mGeoList.add(new OverlayItem(mLastGeoPoint, "", ""));
//	
//			populate();	
//			
//			if (mPopViewManager != null && marker != null)
//			{
//				int x = marker.getBounds().centerX();
//				int y = -marker.getBounds().height();
//				GeoPoint geoPoint = mGeoList.get(0).getPoint();				
//				mPopViewManager.updatePopView(false, this, geoPoint, x, y, mLastLocation);
//			}
//		}
//	}
//
//	
//	public LocationEx getLastLocation()
//	{
//		return mLastLocation;
//	}
//	
//	public GeoPoint getGeoPoint()
//	{
//		return mLastGeoPoint;
//	}
//	
//	
////	public void setColor(int color)
////	{
////		mCircColor = color;
////		
////		mPaint.setColor(mCircColor);
////	}
//
//
//	@Override
//	public void draw(Canvas canvas, MapView mapView, boolean flag) {
//		// TODO Auto-generated method stub
////		
////        if (mLastLocation != null)
////		{			
////        	Projection projection = mapView.getProjection();
////        	Point screenPts = new Point();
////        	projection.toPixels(mLastGeoPoint, screenPts);
////			float radius = projection.metersToEquatorPixels(mLastLocation.getAccuracy());
////			canvas.drawCircle(screenPts.x, screenPts.y , radius, mPaint);
////		}			
//		
//        super.draw(canvas, mapView, flag);
//	}
//
//
//	
//	
//	@Override
//	protected boolean onTap(int pos) {
//		// TODO Auto-generated method stub
//				
//			if (mPopViewManager != null && marker != null)
//			{
//				int x = marker.getBounds().centerX();
//				int y = -marker.getBounds().height();
//				GeoPoint geoPoint = mGeoList.get(pos).getPoint();				
//				mPopViewManager.togglePopViewShow(false, this, geoPoint, x, y, mLastLocation);
//			}
//	
//		return true;
//		
//	}
//	
//}
