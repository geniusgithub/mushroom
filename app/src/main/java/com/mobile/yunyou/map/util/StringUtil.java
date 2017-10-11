package com.mobile.yunyou.map.util;

import com.mobile.yunyou.util.YunTimeUtils;

public class StringUtil {

	public static String  ConvertByDoubeString(double value){
		String string = String.valueOf(value);
		int pos = string.indexOf('.');
		if (pos == -1){
			return string;
		}
		
		int len = string.length();
		int max = Math.min(pos + 4, len);
		
		return string.substring(0, max);
	}
	
	public static String  ConvertByDoubeString(double value, int size){
		String string = String.valueOf(value);
		int pos = string.indexOf('.');
		if (pos == -1){
			return string;
		}
		
		int len = string.length();
		int max = Math.min(pos + size, len);
		
		return string.substring(0, max);
	}
	
	public static long getTimeMillson(String startTime, String endTime){
		try {
			long timeStart = YunTimeUtils.getTimeMillison(startTime);
			long timeEnd = YunTimeUtils.getTimeMillison(endTime);
			long mTimeInterval = (int) (timeEnd - timeStart);
			return mTimeInterval;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}


}
