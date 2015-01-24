package com.mobile.yunyou.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.mobile.yunyou.R;
import com.mobile.yunyou.model.DeviceSetType;
import com.mobile.yunyou.msg.MessageActivity;

public class notifactionUtils {

	private static final int NOTIFICATION_WARING_ID = 0x0001;
	
	public static boolean notificationWarning(Context context, DeviceSetType.DeviceUnReadMsgCount object){
	    NotificationManager   manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		String message = object.mAlert;
		
	   Notification	notification = new Notification(R.drawable.set_logo_icon, "告警消息", System.currentTimeMillis());
       Intent intent = new Intent(context, MessageActivity.class);
       PendingIntent contentIntent = PendingIntent.getActivity(context, 100, intent, 0);
       notification.setLatestEventInfo(context, "告警消息", object.mAlert, contentIntent);
       notification.flags = Notification.FLAG_AUTO_CANCEL;
       
       boolean isRing = object.mBikeAlertWay.mRing;
       boolean isVibe = object.mBikeAlertWay.mVibe;
       if (isRing){
    	   notification.defaults |= Notification.DEFAULT_SOUND;
       }
       
       if (isVibe){
    	   notification.defaults |= Notification.DEFAULT_VIBRATE;
       }

       manager.notify(NOTIFICATION_WARING_ID, notification);

		
		return true;
	}
	
	public static void cancelWarning(Context context){
	    NotificationManager   manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancel(NOTIFICATION_WARING_ID);
	}
	
	public static void resendWarning(Context context,  DeviceSetType.DeviceUnReadMsgCount object){
		 cancelWarning(context);
		 notificationWarning(context, object);
	}
}
