package com.mobile.yunyou.map.data;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mobile.yunyou.network.api.HeadFileConfigure;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.FileManager;
import com.mobile.yunyou.util.LogFactory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

public class DevicePosManager {

	private static final CommonLog log = LogFactory.createLog();
	
	private List<LocationEx> mLocationExList = new ArrayList<LocationEx>();
	private HashMap<String,SoftReference<Bitmap>> mHashMap = new HashMap<String, SoftReference<Bitmap>>();
	
	private Handler mMainHandler;
	
	public DevicePosManager(Handler mainHandler)
	{
		mMainHandler = mainHandler;
	}
	
	public Bitmap getBitmap(String did){
		Bitmap bitmap = getBitmapFromCache(did);
		if (bitmap != null){
			return bitmap;
		}
		
		bitmap = getBitmapFromFile(did);
		return bitmap;
	}
	
	public Bitmap getBitmapFromCache(String did){
		SoftReference<Bitmap> bitmapReference = mHashMap.get(did);
		if (bitmapReference != null){
			Bitmap bitmap = bitmapReference.get();
			if (bitmap != null){
				return bitmap;
			}
		}
		return null;
	}
	
	public Bitmap getBitmapFromFile(String did){
	
		String requestUriString = HeadFileConfigure.getRequestUri(did);
		String savePath = FileManager.getSavePath(requestUriString);
		Bitmap bitmap = BitmapFactory.decodeFile(savePath);
		return bitmap;
	}
	
	public void addLocationEx(LocationEx locationEx)
	{
		String did = locationEx.getDID();
		LocationEx location = getLocationExExist(did);
		if (location == null)
		{
			
			mLocationExList.add(locationEx);
			log.e("mLocationExList.add(locationEx);");
			Bitmap bitmap = getBitmapFromCache(did);
			if (bitmap == null){
				bitmap = getBitmapFromFile(did);
				if (bitmap != null){
					mHashMap.put(did, new SoftReference<Bitmap>(bitmap));
				}		
			}
		}else{			
			location.SetObject(locationEx);
			log.e("location.SetObject(locationEx)");
			Bitmap bitmap = getBitmapFromCache(did);
			if (bitmap == null){
				bitmap = getBitmapFromFile(did);
				if (bitmap != null){
					mHashMap.put(did, new SoftReference<Bitmap>(bitmap));
				}		
			}
		}
	}
	
	public LocationEx getLocationExExist(String did)
	{
		int size = mLocationExList.size();
		for(int i = 0; i < size; i++)
		{
			LocationEx locationEx = mLocationExList.get(i);
			if (locationEx.getDID().equals(did))
			{
				return locationEx;
			}
		}
		
		return null;
	}
}
