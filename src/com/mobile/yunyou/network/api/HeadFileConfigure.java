package com.mobile.yunyou.network.api;

import com.mobile.yunyou.model.GloalType;
import com.mobile.yunyou.util.FileManager;

public class HeadFileConfigure {

	public static String getRequestUri(GloalType.DeviceInfoEx deviceInfoEx){
		
		StringBuffer requestUri = new StringBuffer("http://www.360lbs.net/avatar/");
		try {		
			String did = deviceInfoEx.mDid;
			requestUri.append(did.substring(did.length() - 3) + "/");
			requestUri.append(did + ".jpg");
		} catch (Exception e) {
		}
			
		//return "http://www.360lbs.net/avatar/test.jpg";
		return requestUri.toString();
	}
	
	public static String getRequestUri(String did){
		
		
		StringBuffer requestUri = new StringBuffer("http://www.360lbs.net/avatar/");
		try {		
			requestUri.append(did.substring(did.length() - 3) + "/");
			requestUri.append(did + ".jpg");
		} catch (Exception e) {
		}
			
		//return "http://www.360lbs.net/avatar/test.jpg";
		return requestUri.toString();
	}
	
	public static String getAccountUri(String cid){
		
		StringBuffer requestUri = new StringBuffer("http://www.360lbs.net/avatar/user/");
		try {		
			requestUri.append(cid.substring(cid.length() - 3) + "/");
			requestUri.append(cid + ".jpg");
		} catch (Exception e) {
		}
			
		//return "http://www.360lbs.net/avatar/test.jpg";
		return requestUri.toString();
	}
	
//	public static String getSaveUri(GloalType.DeviceInfoEx deviceInfoEx){
//		return FileManager.getSaveFilePath() + deviceInfoEx.mDid + ".jpg";
//	}
//	
//	public static String getSaveUri(String did){
//		return FileManager.getSaveFilePath() + did + ".jpg";
//	}
	
}
