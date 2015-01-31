package com.mobile.yunyou.datastore;

import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class YunyouSharePreference {

	private static final CommonLog log = LogFactory.createLog();
	
	private final static String SHAREPREFERENCE_NAME = "YunyouSharePreference";
	
	private final static String KEY_USERNAME = "username";
	
	private final static String KEY_PASSWORD = "password";
	
	private final static String KEY_REMEMBER_PWD = "remember_pwd";

	private final static String KEY_CUR_TIME = "curt_time";
	
	private final static String KEY_DISTANCE = "distance";
	
	public static boolean putUserName(Context context, String username)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREPREFERENCE_NAME, 0);
		Editor editor = sharedPreferences.edit();
		editor.putString(KEY_USERNAME, username);
		
		return editor.commit();
	}
	
	public static String getUserName(Context context)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREPREFERENCE_NAME, 0);
		return sharedPreferences.getString(KEY_USERNAME, "");
	}
	
	public static boolean putPwd(Context context, String pwd)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREPREFERENCE_NAME, 0);
		Editor editor = sharedPreferences.edit();
		editor.putString(KEY_PASSWORD, pwd);
		
		return editor.commit();
	}
	
	public static String getPwd(Context context)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREPREFERENCE_NAME, 0);
		return sharedPreferences.getString(KEY_PASSWORD, "");
	}
	
	public static boolean putRememberFlag(Context context, boolean flag)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREPREFERENCE_NAME, 0);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(KEY_REMEMBER_PWD, flag);
		
		return editor.commit();
	}
	
	public static boolean getRememberFlag(Context context)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREPREFERENCE_NAME, 0);
		return sharedPreferences.getBoolean(KEY_REMEMBER_PWD, false);
	}
	
	public static boolean putCurtime(Context context, String curTime)
	{
		log.e("putCurtime = "  + curTime);
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREPREFERENCE_NAME, 0);
		Editor editor = sharedPreferences.edit();
		editor.putString(KEY_CUR_TIME, curTime);
		
		return editor.commit();
	}
	
	public static String getCurtime(Context context)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREPREFERENCE_NAME, 0);
		return sharedPreferences.getString(KEY_CUR_TIME, "");
	}
	
	public static boolean putDistance(Context context, int distance)
	{
		log.e("putDistance = "  + distance);
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREPREFERENCE_NAME, 0);
		Editor editor = sharedPreferences.edit();
		editor.putFloat(KEY_DISTANCE, distance);
		
		return editor.commit();
	}
	
	public static int getDistance(Context context)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREPREFERENCE_NAME, 0);
		return sharedPreferences.getInt(KEY_DISTANCE, 0);
	}
	
}
