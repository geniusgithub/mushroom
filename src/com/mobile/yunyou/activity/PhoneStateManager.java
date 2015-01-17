package com.mobile.yunyou.activity;


import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;


public class PhoneStateManager {

	
	public interface IPhoneStrentthListener{
		public void onPhoneStrent(int rxlev);
	}
	
	private Context mContext;
	private TelephonyManager mTelephonyManager;	
	private PhoneStateListener mListener;
	private IPhoneStrentthListener mPhoneStrentthListener;
	
	public PhoneStateManager(Context context)
	{
		mContext = context;
		mTelephonyManager = ( TelephonyManager )context.getSystemService(Context.TELEPHONY_SERVICE);		
		mListener = new MyPhoneStateListener();
	}
	
	public void syncGetPhoneStrength(IPhoneStrentthListener listener)
	{
		registerListen(listener);
		
	}

	private void registerListen(IPhoneStrentthListener listener)
	{		
		if (listener != null)
		{
			mPhoneStrentthListener = listener;
			mTelephonyManager.listen(mListener ,PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		}	
	}
	
	private void unRegisterListen()
	{
		if (mPhoneStrentthListener != null)
		{
			mTelephonyManager.listen(mListener, PhoneStateListener.LISTEN_NONE);	
		}
		mPhoneStrentthListener = null;
	}
	
	private static final CommonLog log = LogFactory.createLog();
	
	
	private class MyPhoneStateListener extends PhoneStateListener{
		@Override		 
		public void onSignalStrengthsChanged(SignalStrength signalStrength){
			super.onSignalStrengthsChanged(signalStrength);
		
			
			int strength = signalStrength.getGsmSignalStrength();
			log.e("----------------------->						 signalStrength.getGsmSignalStrength() = " + strength);
			if (mPhoneStrentthListener != null)
			{
				mPhoneStrentthListener.onPhoneStrent(strength);
				unRegisterListen();
			}
		}
	};
}
