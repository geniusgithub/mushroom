//package com.mobile.yunyou.map;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Point;
//import android.graphics.drawable.Drawable;
//
//import com.amap.mapapi.core.GeoPoint;
//import com.amap.mapapi.core.OverlayItem;
//import com.amap.mapapi.core.v;
//import com.amap.mapapi.map.ItemizedOverlay;
//import com.amap.mapapi.map.MapView;
//import com.amap.mapapi.map.Projection;
//import com.mobile.yunyou.R;
//import com.mobile.yunyou.model.DeviceSetType;
//import com.mobile.yunyou.util.CommonLog;
//import com.mobile.yunyou.util.LogFactory;
//import com.mobile.yunyou.util.Utils;
//
//public class DeviceHistoryOverlay extends ItemizedOverlay<OverlayItem>{
//
//	
//	private static final CommonLog log = LogFactory.createLog();
//	
//	private List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
//	private Set<Integer> mStaticPointSet = new HashSet<Integer>();
//	private Map<Integer, Integer> mHashMap = new HashMap<Integer, Integer>();
//	
//	private Drawable marker;		
//	private Context mContext;
//	
//	private DeviceHistoryPopViewManager manager;
//	
//	private List<DeviceSetType.DeviceHistoryResult> mList;
//	private List<GeoPoint> mGeoPointList = new ArrayList<GeoPoint>();
//	private GeoPoint mCurGeoPoint;
//	private int mCurIndex = 0;
//
//	private Paint paintLinePaint = new Paint();
//	
//	public DeviceHistoryOverlay(Context  context, Drawable drawable, DeviceHistoryPopViewManager manager) {
//		super(boundCenterBottom(drawable));
//		// TODO Auto-generated constructor stub
//		
//		marker = drawable;
//		mContext = context;
//		this.manager = manager;
//		
//		initPaint();
//		
//		populate();
//	}
//	
//	
//	public void initPaint()
//	{
//
//		paintLinePaint.setColor(0xFF333333);
//		paintLinePaint.setStrokeWidth(10);
//		paintLinePaint.setStyle(Paint.Style.STROKE);
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
//	public void setLocationList(List<DeviceSetType.DeviceHistoryResult> list, Set<Integer> set)
//	{
//		if (list != null)
//		{
//			mStaticPointSet = set;
//			log.e("setLocationList size = " + list.size() + ", set size = " + set.size());
//			mList = list;
//			mGeoPointList.clear();
//			mGeoList.clear();
//			mHashMap.clear();
//			mCurGeoPoint = null;
//			mCurIndex = 0;
//			int size = list.size();
//			
//			int index = 0;
//			for(int i = 0; i < size; i++)
//			{
//				DeviceSetType.DeviceHistoryResult result = list.get(i);	
//				GeoPoint point = new GeoPoint((int)(result.mOffsetLat * 1E6), (int)(result.mOffsetLon * 1E6));
//				
//				mGeoPointList.add(point);
//				
//				if (mStaticPointSet.contains(i)){
//					OverlayItem item = new OverlayItem(point, "", "");
//					if (i == 0){
//						Drawable drawable = mContext.getResources().getDrawable(R.drawable.point_start);
//					    //使用高德地图不写此属性的话无法显示标记点  
//					    drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());  
//					    //图片是显示在一个矩形区域，这句是将图片中心点作为其在屏幕的坐标  
//					    item.setMarker(boundCenterBottom(drawable));  
//					}else if (i == size - 1){
//						Drawable drawable = mContext.getResources().getDrawable(R.drawable.point_end);
//					    //使用高德地图不写此属性的话无法显示标记点  
//					    drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());  
//					    //图片是显示在一个矩形区域，这句是将图片中心点作为其在屏幕的坐标  
//					    item.setMarker(boundCenterBottom(drawable));  
//					}
//					mGeoList.add(item);
//					mHashMap.put(index, i);
//					index++;
//				}
//			
//			}
//			
//			populate();	
//			
//		}
//	}
//	
//	public void clear()
//	{
//		mGeoList.clear();
//		mGeoPointList.clear();
//		mCurGeoPoint = null;
//		mCurIndex = 0;
//		populate();	
//	}
//	
//	public List<GeoPoint>  getGeoPoint()
//	{
//		return mGeoPointList;
//	}
//	
//	public void reset(){
//		mCurIndex = 0;
//		mCurGeoPoint = null;
//		mGeoList.clear();
//		populate();	
//	}
//
//	public boolean showNext(){
//		if (mGeoPointList.size() < 2 || mCurIndex >= mGeoPointList.size() - 1){
//			return false;
//		}
//		
//		mCurIndex++;
//		mCurGeoPoint = mGeoPointList.get(mCurIndex);
//		
//		mGeoList.clear();
//		mGeoList.add(new OverlayItem(mCurGeoPoint, "", ""));
//		populate();	
//		
//		return true;
//	}
//	
//	@Override
//	public void draw(Canvas canvas, MapView mapView, boolean flag) {
//		// TODO Auto-generated method stub		
//      
//        
//        Projection projection = mapView.getProjection(); 
//        
//        int size = mGeoPointList.size();
//        
//        for(int i = 0; i < size; i++)
//        {
//        	if (i == size - 1)
//        	{
//        		break;
//        	}
//        	
//        	Point pointStart = projection.toPixels(mGeoPointList.get(i), null);
//        	Point pointEnd =  projection.toPixels(mGeoPointList.get(i + 1), null);
//    
//        	canvas.drawLine(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y, paintLinePaint);
//        }
//        
//        super.draw(canvas, mapView, flag);
//	}
//	
//	@Override
//	protected boolean onTap(int pos) {
//		// TODO Auto-generated method stub
//				
//			Integer value = mHashMap.get(pos);
//			if (manager != null && marker != null && value != null && value < mGeoPointList.size())
//			{
//				
//				int x = marker.getBounds().centerX();
//				int y = -marker.getBounds().height();
//				GeoPoint geoPoint = mGeoList.get(pos).getPoint();
//				try {
//					int posType = DeviceHistoryPopViewManager.PosType.IPT_MIDDLE;
//					if (value == 0){
//						posType = DeviceHistoryPopViewManager.PosType.IPT_HEAD;
//					}else if (value == mList.size() - 1){
//						posType = DeviceHistoryPopViewManager.PosType.IPT_END;
//					}
//						manager.togglePopViewShow(geoPoint, x, y, mList.get(value), posType);
//					
//				} catch (Exception e) {
//					// TODO: handle exception
//					e.printStackTrace();
//					Utils.showToast(mContext, "can't show popview!!!");
//				}
//				
//				
//			}
//			
//	
//		return true;
//		
//	}
//	
//	
//}
