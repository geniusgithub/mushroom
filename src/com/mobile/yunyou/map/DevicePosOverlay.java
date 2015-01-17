//package com.mobile.yunyou.map;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import android.R.xml;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Point;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.location.LocationManager;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ImageView;
//import android.widget.TextView;
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
//public class DevicePosOverlay extends ItemizedOverlay<OverlayItem>{
//
//	
//	private static final CommonLog log = LogFactory.createLog();
//	
//	private List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
//
//	private Drawable marker;		
//	private Context mContext;
//	private Bitmap headBitmap;
//	
//	private LocationEx mLastLocation;
//	private GeoPoint mLastGeoPoint;	
//	private PopViewManager mPopViewManager;
//	
//	
//	public DevicePosOverlay(Context  context, Drawable drawable,  PopViewManager popViewManager) {
//		super(boundCenterBottom(drawable));
//		// TODO Auto-generated constructor stub
//		
//		marker = drawable;
//		mContext = context;
//		mPopViewManager = popViewManager;
//		
//	//	headBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_head);
//		
//		populate();
//
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
//	public void setBitmap(Bitmap bitmap){
//		if (bitmap != null){
//			headBitmap = Bitmap.createScaledBitmap(bitmap, 90, 90, false);
//		}else{
//			headBitmap = null;
//		}
//		
//	}
//	
//	public void setLocation(LocationEx location)
//	{
//		if (location != null)
//		{
////			log.e("DevicePosOverlay   setLocation");
//			mLastLocation = location;
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
//			if (mPopViewManager != null)
//			{
//				Drawable drawable = getDrawable();
//				//log.e("boundCenterBottom.drawable = " + drawable);
//				//boundCenterBottom(drawable);
//				int x = drawable.getBounds().centerX();
//				int y = -drawable.getBounds().height();
//				GeoPoint geoPoint = mGeoList.get(0).getPoint();				
//				mPopViewManager.updatePopView(true, this, geoPoint, x, y, mLastLocation);
//			}
//		}
//	}
//	
//	private Drawable getDrawable(){
//		return marker;
//	}
//	
//	public void clear()
//	{
//		log.e("DevicePosOverlay clear...");
//		mGeoList.clear();
//		populate();
//		mLastLocation = null;
//		mLastGeoPoint = null;	
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
//	private void drawHeadBitmap(Canvas canvas, MapView mapView){
//		
//	
//		if (headBitmap != null){
//			GeoPoint geoPoint = getGeoPoint();
//			if (geoPoint != null){
//				Projection projection = mapView.getProjection(); 
//				Drawable drawable = getDrawable();
//				Point point = projection.toPixels(geoPoint, null); 
//				int x = point.x - headBitmap.getWidth() / 2;
//				int y = point.y - (drawable.getBounds().height() + headBitmap.getHeight())/2 - 12;
//				canvas.drawBitmap(headBitmap, x, y, null);
//			}		
//		}
//		
//		
//		
//	}
//
//	@Override
//	public void draw(Canvas canvas, MapView mapView, boolean flag) {
//		// TODO Auto-generated method stub		
//		
//        super.draw(canvas, mapView, flag);
//
//        drawHeadBitmap(canvas, mapView);
//	}
//
//	
//	@Override
//	protected boolean onTap(int pos) {
//		// TODO Auto-generated method stub
//				
//			if (mPopViewManager != null)
//			{
//				Drawable drawable = getDrawable();
//				int x = drawable.getBounds().centerX();
//				int y = -drawable.getBounds().height();
//				GeoPoint geoPoint = mGeoList.get(pos).getPoint();				
//				mPopViewManager.togglePopViewShow(true, this, geoPoint, x, y, mLastLocation);
//			}
//	
//		return true;
//		
//	}
//	
//	
//}
