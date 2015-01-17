package com.mobile.yunyou.map.util;

import java.util.ArrayList;
import java.util.List;

import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;



import android.content.Context;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;




public class CellIDInfoManager {

	private static final CommonLog log = LogFactory.createLog();


	public CellIDInfoManager() {
		
	}
	
	/*
	 * 获取基站信息
	 */
	public static ArrayList<CellIDInfo> getCellIDInfo(Context context, int rxlv) throws Exception{
		
		TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		
		ArrayList<CellIDInfo> CellID = new ArrayList<CellIDInfo>();
		CellIDInfo currentCell = new CellIDInfo();

		int type = manager.getNetworkType();						// 获得网络类型

		int phoneType = manager.getPhoneType();

		log.e("type = " + type);
		
		if (type == TelephonyManager.NETWORK_TYPE_GPRS				// GSM网
				|| type == TelephonyManager.NETWORK_TYPE_EDGE
				|| type == TelephonyManager.NETWORK_TYPE_HSDPA
				|| type == TelephonyManager.NETWORK_TYPE_UMTS
				|| type == TelephonyManager.NETWORK_TYPE_HSPA)
		{
			GsmCellLocation gsm = ((GsmCellLocation) manager.getCellLocation());
			if (gsm == null)
			{
				log.e("GsmCellLocation is null!!!");
				return null;
			}
				

			int lac = gsm.getLac();
			String mcc = manager.getNetworkOperator().substring(0, 3);
			String mnc = manager.getNetworkOperator().substring(3, 5);
			int cid = gsm.getCid();
			
			currentCell.cellId = gsm.getCid();
			currentCell.mobileCountryCode = mcc;
			currentCell.mobileNetworkCode = mnc;
			currentCell.locationAreaCode = lac;
			
			currentCell.radioType = "gsm";
			currentCell.rxlv = rxlv;
			
			CellID.add(currentCell);
			
			// 获得邻近基站信息
			List<NeighboringCellInfo> list = manager.getNeighboringCellInfo();
			int size = list.size();
			for (int i = 0; i < size; i++) {

				CellIDInfo info = new CellIDInfo();
				info.cellId = list.get(i).getCid();
				info.mobileCountryCode = mcc;
				info.mobileNetworkCode = mnc;
				info.locationAreaCode = lac;
				info.type = type;
				info.rxlv = rxlv;
			
				CellID.add(info);
			}
			
		}else if (type == TelephonyManager.NETWORK_TYPE_CDMA		// 电信cdma网
				|| type == TelephonyManager.NETWORK_TYPE_1xRTT
				|| type == TelephonyManager.NETWORK_TYPE_EVDO_0
				|| type == TelephonyManager.NETWORK_TYPE_EVDO_A)
		{
			
			CdmaCellLocation cdma = (CdmaCellLocation) manager.getCellLocation();	
			if (cdma == null)
			{
				log.e("CdmaCellLocation is null!!!");
				return null;
			}
			
			int lac = cdma.getNetworkId();
			String mcc = manager.getNetworkOperator().substring(0, 3);
			String mnc = String.valueOf(cdma.getSystemId());
			int cid = cdma.getBaseStationId();
			
			currentCell.cellId = cid;
			currentCell.mobileCountryCode = mcc;
			currentCell.mobileNetworkCode = mnc;
			currentCell.locationAreaCode = lac;
	
			currentCell.radioType = "cdma";
			currentCell.rxlv = rxlv;
			
			CellID.add(currentCell);
			
			// 获得邻近基站信息
			List<NeighboringCellInfo> list = manager.getNeighboringCellInfo();
			int size = list.size();
			for (int i = 0; i < size; i++) {

				CellIDInfo info = new CellIDInfo();
				info.cellId = list.get(i).getCid();
				info.mobileCountryCode = mcc;
				info.mobileNetworkCode = mnc;
				info.locationAreaCode = lac;
				info.type = type;
				info.rxlv = rxlv;
			
				CellID.add(info);
			}
		}
		
		return CellID;
			
	}

}
