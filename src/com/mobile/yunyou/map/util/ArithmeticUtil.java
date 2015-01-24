package com.mobile.yunyou.map.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mobile.yunyou.model.DeviceSetType.DeviceHistoryResult;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.YunTimeUtils;

public class ArithmeticUtil {

	private static final CommonLog log = LogFactory.createLog();
	
	public static void filterLocationHistory(List<DeviceHistoryResult> list){
		
		for(int i = 0; i < list.size();){
			if (i == list.size() - 1){
				break;
			}
			DeviceHistoryResult object = list.get(i);
			DeviceHistoryResult object2 = list.get(i + 1);
			if (object.mCreateTime.equalsIgnoreCase(object2.mCreateTime)){
				list.remove(i);
				continue;
			}
			 i++;
		}
		
	}
	
	
	public static Set<Integer> getStaticPoint(List<DeviceHistoryResult> list){
		Set<Integer> set = new HashSet<Integer>();
		
		for(int i = 0; i < list.size(); i++){
			if (i == 0 || i == list.size() - 1){
				set.add(i);
				continue;
			}
			DeviceHistoryResult object = list.get(i);
			String createTiString = object.mCreateTime;
			String updateTiString = object.mUploadTime;
			if (!isMove(createTiString, updateTiString)){
				set.add(i);
			}
		}
		
		return set;
	}
	
	public static boolean isMove(String createTime, String updateTime){
		
		long time1 = YunTimeUtils.getSecondsInCurrentYear(createTime);
		long time2 = YunTimeUtils.getSecondsInCurrentYear(updateTime);
		
		if (time1 == 0 || time2 == 0){
			return true;
		}
		
		if (time2 - time1 > 60){
			return false;
		}
		
		return true;
	}
	
}
