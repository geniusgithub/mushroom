package com.mobile.yunyou.map;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;

import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.activity.PhoneStateManager;
import com.mobile.yunyou.map.util.CellIDInfo;
import com.mobile.yunyou.map.util.CellIDInfoManager;
import com.mobile.yunyou.map.util.WebManager;
import com.mobile.yunyou.model.DeviceSetType;
import com.mobile.yunyou.model.PublicType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.network.IRequestCallback;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.Utils;

public class UploadGPSManager  implements IRequestCallback, PhoneStateManager.IPhoneStrentthListener{

	
private final static int UPLOAD_GPS_INTERVAL = 60 * 1000;
	
	private static final CommonLog log = LogFactory.createLog();
	
    private NetworkCenterEx mNetworkCenter;
    
    private static UploadGPSManager mInstance;
	
	private Context mContext;
	
	private PhoneStateManager mPhoneStateManager;
	
	private double lat = -1;
	private double lon = -1;

	public synchronized static UploadGPSManager getInstance()
	{
		if (mInstance == null)
		{
			mInstance = new UploadGPSManager(YunyouApplication.getInstance());
		}
		
		return mInstance;
	}
	
	private UploadGPSManager(Context context)
	{
		mContext = context;

		mNetworkCenter = NetworkCenterEx.getInstance();
		
		mPhoneStateManager = new PhoneStateManager(context);
	}
	
	public void UploadGPSInfo(double lat, double lon)
	{
		this.lon = lon;
		this.lat = lat;
		mPhoneStateManager.syncGetPhoneStrength(this);	
	}

	@Override
	public boolean onComplete(int requestAction, ResponseDataPacket dataPacket) {
		// TODO Auto-generated method stub
		
		String jsString = "null";
		if (dataPacket != null)
		{
			jsString = dataPacket.toString();
		}
		
		log.e("requestAction = " + Utils.toHexString(requestAction) + "\nResponseDataPacket = \n" +jsString);
		if (dataPacket == null)
		{
			log.e("USER_UPLOAD_GPS_MASID fail...");
			return false;
		}
		switch(requestAction)
		{
			case PublicType.USER_UPLOAD_GPS_MASID:
			{
				if (dataPacket.rsp == 1)
				{
					log.e("USER_UPLOAD_GPS_MASID success...");
				}else{
					log.e("USER_UPLOAD_GPS_MASID fail...");
				}
				
			}
			break;
		}
		
		
		return true;
	}

	@Override
	public void onPhoneStrent(int rxlev) {
		// TODO Auto-generated method stub
		if (lat == -1 && lon == -1)
		{
			return ;
		}
		
		PublicType.UserUploadGps userUploadGps = new PublicType.UserUploadGps();
		
		userUploadGps.mLat = lat;
		userUploadGps.mLon = lon;
		PublicType.CellsGroup cellsGroup = new PublicType.CellsGroup();
		
		List<CellIDInfo> cellIDInfos = null;
		try {
			cellIDInfos = CellIDInfoManager.getCellIDInfo(mContext, rxlev);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (cellIDInfos != null)
		{
			cellsGroup.mList = cellIDInfos;
		}
		
		userUploadGps.mCellsGroup = cellsGroup;
		
		mNetworkCenter.StartRequestToServer(PublicType.USER_UPLOAD_GPS_MASID, userUploadGps, this);
		
		lon = -1;
		lat = -1;
	}
	
	
}
