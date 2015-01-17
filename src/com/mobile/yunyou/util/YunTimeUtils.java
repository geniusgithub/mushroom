package com.mobile.yunyou.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.mobile.yunyou.model.DeviceSetType;

public class YunTimeUtils {

	private static final CommonLog log = LogFactory.createLog();
	
	// 2012-08-09 12:23:30
	public static String getFormatTime(long timeMillis)
	{
		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(timeMillis);

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;     
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int mins = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        
        String resultString = String.format("%04d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, mins, second);
   						
        return resultString;	
		
	}
	
	// 2012-08-09
	public static String getFormatTime1(long timeMillis)
	{
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(timeMillis);

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;     
        int day = c.get(Calendar.DAY_OF_MONTH);
        
        String resultString = String.format("%04d-%02d-%02d", year, month, day);
   						
        return resultString;	
		
	}
	
	// 12:23:30
	public static String getFormatTime2(long timeMillis)
	{
		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(timeMillis);

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int mins = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        
        String resultString = String.format("%02d:%02d:%02d", hour, mins, second);
   						
        return resultString;	
		
	}
	
	// 03:23:23
	public static String getFormatTimeInterval(long timeInterval)
	{
		int time = (int) (timeInterval / 1000);
		int timeSecond = time % 60;
		int timeMinute = (time / 60) % 60;
		int timeHour = time / 3600;
		
        String resultString = String.format("%02d:%02d:%02d", timeHour, timeMinute, timeSecond);
   						
        return resultString;	
		
	}
	
	public static String getShowTimeIntervalString(int seconds)
	{

		if (seconds < 60)
		{
			return "0分";
		}
		
		int mins = seconds / 60;
		int hours = mins / 60;
		mins = mins % 60;
		
		if (hours == 0)
		{
			return "(" + mins + "分)";
		}
		
		if (hours < 24){
			return "(" + hours + "小时" + mins + "分)";
		}
		 
		int day = hours / 24;
		hours = hours % 24;
		
		return "(" + day + "天" + hours + "小时" + mins + "分)";
		
	}
	
	// 2012-08-09 12:23:30
	public static long getSecondsInCurrentYear(String time)
	{
		if (time.equals(""))
		{
			return 0;
		}
		
		int  startPos = time.indexOf(" ");
		if (startPos == -1)
		{
			return 0;
		}
		
		try {
			String preTime = time.substring(0, startPos);
			String subTime = time.substring(startPos + 1);
			
			String resultPre[] = preTime.split("-");
			int  year = Integer.valueOf(resultPre[0]);
			int  month = Integer.valueOf(resultPre[1]);
			int  day = Integer.valueOf(resultPre[2]);
			
			String result[] = subTime.split(":");		
			int hour = Integer.valueOf(result[0]);
			int mins = Integer.valueOf(result[1]);
			int second = Integer.valueOf(result[2]);
			
		    int days = dayBeforeMonth(year, month) + day;
		
			return (days * 24 + hour) * 60 * 60 +  mins * 60 + second;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return 0;
		}
		
	}
	
	// 2012-08-09 12:23:30
	public static long getTimeMillison(String formatTime) throws ParseException{
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		int betweenDays = 0; 
		Date d1;
		d1 = format.parse(formatTime);
		Calendar c1 = Calendar.getInstance(); 
		c1.setTime(d1); 
		return c1.getTimeInMillis();
	}
	

	public static int getSecondsInYear()
	{

		Calendar c = Calendar.getInstance();

		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int mins = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        
        int days = dayBeforeMonth(year, month) + day;
        
    	return (days * 24 + hour) * 60 * 60 +  mins * 60 + second;
		
	}
	
	
	public static int YEARS[] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	public static int dayBeforeMonth(int year, int month)
	{
		int days = 0;
		for(int i = 1; i < month; i++)
		{
			days += YEARS[i];
		}
		
		if (isRunYear(year) && month > 2)
		{
			days += 1;
		}
		
		return days;
	}
	
	public static boolean isRunYear(int year)
	{
		if (year % 4 == 0 && year %100 != 0)
		{
			return true;
		}
		
		if (year % 400 == 0)
		{
			return true;
		}
		
		return false;
	}
	
	public static void filterGpsStillTime(List<DeviceSetType.GpsStillTime> list, int year, int month, int day)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, day);

		int week = getWeekByCalendar(calendar.get(Calendar.WEEK_OF_MONTH));
		filterGpsStillTime(list, week);
	}
	
	public static void filterGpsStillTime(List<DeviceSetType.GpsStillTime> list, int week)
	{
	
		String tmString = String.valueOf(week);
		log.e("filterGpsStillTime week = " + week); 
		for(int i = 0; i < list.size(); )
		{
			DeviceSetType.GpsStillTime object = list.get(i);
			String weekString = object.mWeekString;
			log.e("filterGpsStillTime --> i = " + i + ", weekString = " + weekString);
			int index = weekString.indexOf(tmString);
			if (index == -1)
			{
				list.remove(i);
				continue;
			}
			i++;
		}
	}
	
	public static int getWeekByCalendar(int value)
	{
		int result = 1;
		switch(value)
		{
		case Calendar.MONDAY:
			result = 1;
			break;
		case Calendar.TUESDAY:
			result = 2;
			break;
		case Calendar.WEDNESDAY:
			result = 3;
			break;
		case Calendar.THURSDAY:
			result = 4;
			break;
		case Calendar.FRIDAY:
			result = 5;
			break;
		case Calendar.SATURDAY:
			result = 6;
			break;
		case Calendar.SUNDAY:
			result = 7;
			break;
		}
		
		return result;
	}
    
}
