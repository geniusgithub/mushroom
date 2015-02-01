package com.mobile.yunyou;

import android.content.Context;
import android.content.Intent;

public class BrocastFactory {

	public final static String BROCAST_UPDATE_NICKNAME = "BROCAST_UPDATE_NICKNAME";
	public final static String BROCAST_UPDATE_HEADICON = "BROCAST_UPDATE_HEADICON";
	
	public static void sendUserInfoUpdate(Context context){
		Intent intent = new Intent();
		intent.setAction(BROCAST_UPDATE_NICKNAME);
		context.sendBroadcast(intent);
	}
	
	public static void sendHeadUpdate(Context context){
		Intent intent = new Intent();
		intent.setAction(BROCAST_UPDATE_HEADICON);
		context.sendBroadcast(intent);
	}
}
