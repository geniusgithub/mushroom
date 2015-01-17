package com.mobile.yunyou.datastore;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class YunyouSharePreference {

	private final static String SHAREPREFERENCE_NAME = "YunyouSharePreference";
	
	private final static String KEY_USERNAME = "username";
	
	private final static String KEY_PASSWORD = "password";
	
	private final static String KEY_REMEMBER_PWD = "remember_pwd";

	
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
	
}
