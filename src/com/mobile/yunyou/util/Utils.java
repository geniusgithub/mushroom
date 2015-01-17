package com.mobile.yunyou.util;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;



public class Utils {
	
	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}
	
	public static int getScreenHeight(Context context) {
		WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getHeight();
	}
	
	public static float getScreenDensity(Context context) {
    	try {
    		DisplayMetrics dm = new DisplayMetrics();
	    	WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
	    	manager.getDefaultDisplay().getMetrics(dm);
	    	return dm.density;
    	} catch(Exception ex) {
    	
    	}
    	return 1.0f;
    }
	
	public static String ToDBC(String input) 
	{   
       char[] c = input.toCharArray();   
         for (int i = 0; i < c.length; i++) {   
             if (c[i] == 12288) {   
                 c[i] = (char) 32;   
                 continue;   
             }   
             if (c[i] > 65280 && c[i] < 65375)   
                c[i] = (char) (c[i] - 65248);   
        }   
         
         
        return new String(c);   
	}  
	
	
	/*
	 * 根据字符串所占像素宽度得到合适的文本宽度设置（文本宽度和屏幕宽度取较小值）
	 */
	public static int getFitWidth(Context context, String txt, int txtSize)
	{
	
		int StringLength = getStringWidth(txt, txtSize);
		int screenWidth = (int) (getScreenWidth(context) * 0.9);
	
		return Math.min(StringLength, screenWidth);
	}
	
	public static int getStringWidth(String txt, int txtSize)
	{
		Paint pFont = new Paint();
		pFont.setTextSize(txtSize);
		int adressPxLen = (int) pFont.measureText(txt);
		
		return adressPxLen;
	}


	
	public static String toHexString(int num)
	{
		String string = "0x" + Integer.toHexString(num);
		return string;
	}
	
	
	public static String getPackageVersionName(Context context) throws Exception
	{
		
       PackageManager packageManager = context.getPackageManager();
       
       // getPackageName()是你当前类的包名，0代表是获取版本信息
       PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
       String version = packInfo.versionName;
       return version;
	}
	
	public static String getOSVersion()
	{
		return android.os.Build.VERSION.RELEASE;
	}
	
	public static String getDeviceManufacturer()
	{
		return android.os.Build.MANUFACTURER;
	}
	
	public static String getDeviceModel()
	{
		return android.os.Build.MODEL;
	}

	public static void showToast(Context context, int tip)
	{
		Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
	}
	public static void showToast(Context context, String tip)
	{
		Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
	}
	

}