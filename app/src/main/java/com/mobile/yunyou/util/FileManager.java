package com.mobile.yunyou.util;


public class FileManager {

	public static String getSaveFilePath() {
		if (CommonUtil.hasSDCard()) {
			return CommonUtil.getRootFilePath() + "com.mobile.yunyou/files/";
		} else {
			return CommonUtil.getRootFilePath() + "com.mobile.yunyou/files/";
		}
	}
	
	public static String getSavePath(String url){
		String filename = String.valueOf(url.hashCode());
		return getSaveFilePath() + filename;
	}
}

