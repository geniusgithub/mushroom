//package com.mobile.yunyou.map;
//
//import java.util.List;
//
//import com.amap.mapapi.map.Overlay;
//import com.mobile.yunyou.util.CommonLog;
//import com.mobile.yunyou.util.LogFactory;
//
//public class MapPositionManager {
//
//
//	
//	private List<Overlay> mOverlayList;
//	private PositionOverlay mSelfPositionOverlay;
//	private DevicePosOverlay mDevicePositionOverlay;
//	private DeviceAreaOverlay mDeviceAreanOverlay;
//	private DeviceHistoryOverlay mDeviceHistoryOverlay;
//	private int mCurShowState = -1;
//	
//	public static interface IViewConstant
//	{
//		int IVC_DEVICE_POS = 0;
//		int IVC_AREA_POS = 1;
//		int IVC_HISTORY_POS = 2;
//	}
//	
//	
//	public MapPositionManager(List<Overlay> list)
//	{
//		mOverlayList = list;
//	}
//	
//	public void setPosOverlay(PositionOverlay overlay)
//	{
//		mSelfPositionOverlay = overlay;
//	}
//	
//	public void setDevicePosOverlay(DevicePosOverlay overlay)
//	{
//		mDevicePositionOverlay = overlay;
//	}
//	
//	public void setDeviceAreaOverlay(DeviceAreaOverlay overlay)
//	{
//		mDeviceAreanOverlay = overlay;
//	}
//	
//	public void setDeviceHistoryOverlay(DeviceHistoryOverlay overlay)
//	{
//		mDeviceHistoryOverlay = overlay;
//	}
//	
//	public int getCurShowState(){
//		return mCurShowState;
//	}
//	
//	
//	private void showView(int viewID)
//	{
//		switch(viewID)
//		{
//			case IViewConstant.IVC_DEVICE_POS:
//			{
//				mOverlayList.clear();
//				mOverlayList.add(mSelfPositionOverlay);
//				mOverlayList.add(mDevicePositionOverlay);
//			}
//				break;
//			case IViewConstant.IVC_AREA_POS:
//			{
//				mOverlayList.clear();
//				mOverlayList.add(mDeviceAreanOverlay);
//			}
//				break;
//			case IViewConstant.IVC_HISTORY_POS:
//			{
//				mOverlayList.clear();
//				mOverlayList.add(mDeviceHistoryOverlay);
//			}
//				break;
//		}
//	}
//	
//	
//	public void showDevicePos()
//	{
//		showView(IViewConstant.IVC_DEVICE_POS);
//		mCurShowState = IViewConstant.IVC_DEVICE_POS;
//	}
//	
//	public void showAreaPos()
//	{
//		showView(IViewConstant.IVC_AREA_POS);
//		mCurShowState = IViewConstant.IVC_AREA_POS;
//	}
//	
//	
//	public void showHistoryPos()
//	{
//		showView(IViewConstant.IVC_HISTORY_POS);	
//		mCurShowState = IViewConstant.IVC_HISTORY_POS;
//	}
//	
//	
//}
