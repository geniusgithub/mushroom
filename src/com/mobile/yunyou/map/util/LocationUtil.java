package com.mobile.yunyou.map.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.util.Log;

public class LocationUtil {

//	public static void toggleGPS(Context context) {
//		Intent gpsIntent = new Intent();
//		gpsIntent.setClassName("com.android.settings",
//				"com.android.settings.widget.SettingsAppWidgetProvider");
//		gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
//		gpsIntent.setData(Uri.parse("custom:3"));
//		try {
//			PendingIntent.getBroadcast(context, 0, gpsIntent, 0).send();
//		} catch (CanceledException e) {
//			e.printStackTrace();
//			Log.e("", "toggleGPS CanceledException");
//		}
//	}

	public static boolean isGPSEnable(Context context) {
		/*
		 * 用Setting.System来读取也可以，只是这是更旧的用法 String str =
		 * Settings.System.getString(getContentResolver(),
		 * Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		 */
		String str = Settings.Secure.getString(context.getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		
		//GPS 状态
		Boolean IsOpen = false;
		
		LocationManager mlocationManager =( LocationManager)context.getSystemService(context.LOCATION_SERVICE);
		IsOpen =  mlocationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);

		
	    Log.e("", "isGPSEnable  str = " + str + ", IsOpen = " + IsOpen);
//		
//		if (str != null) {
//			return str.contains("gps");
//		} else {
//			return false;
//		}
	    return IsOpen;
	}
	
	/** 检查坐标更新的频率时间 */
	private static final int CHECK_INTERVAL = 30 * 1000;
	/**
	 * 判断最新位置信息是否符合最新位置的规则
	 * 
	 * @param location
	 *            最新坐标
	 * @param currentBestLocation
	 *            当前已有的最合适坐标
	 * @return true符合 false不符合
	 */
	public static boolean isBetterLocation(Location location,Location currentBestLocation) {
		if (true){
			return true;
		}
		if (currentBestLocation == null) {
			return true;
		}

		// 检查是新坐标还是旧的坐标
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > CHECK_INTERVAL;
		boolean isSignificantlyOlder = timeDelta < -CHECK_INTERVAL;
		boolean isNewer = timeDelta > 0;

		if (isSignificantlyNewer) {
			return true;
		} else if (isSignificantlyOlder) {
			return false;
		}

		// 检查新坐标的精确度
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
				.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// 检查是不是一样的Provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate
				&& isFromSameProvider) {
			return true;
		}
		return false;
	}

	/** 检查两个 providers 是否一样 */
	public static boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}
}
