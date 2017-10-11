package com.mobile.yunyou.map.util;

public class CellIDInfo {

	public CellIDInfo() {
		
	}
	
	public int cellId;									//基站id

	public String mobileCountryCode;//mcc				//国家代号

	public String mobileNetworkCode;//mnc				//网络代号

	public int locationAreaCode;//lac					//区域代号
	
	public String radioType;							// gsm or cdma
	
	public int rxlv = 0;

	public int type;									// 网络制式	

}
