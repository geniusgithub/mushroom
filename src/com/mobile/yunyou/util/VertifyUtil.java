package com.mobile.yunyou.util;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mobile.yunyou.model.BaseType;
import com.mobile.yunyou.model.DeviceSetType;
import com.mobile.yunyou.model.BaseType.Birthday;

import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class VertifyUtil {

	
	private static final CommonLog log = LogFactory.createLog();
	
	
	  public static boolean isEMail(String str)
	  {
	    	 Pattern pattern = Pattern.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
	    	 Matcher matcher = pattern.matcher(str);
	         if(matcher.matches()){
	        	 return true;
	         }
	         return false;
	  }
	  
	  
	  public static boolean isMobileNumber(String mobileNumber)
	  {
			int[] nums = {133,142,144,146,148,149,153,180,181,189
		            ,130,131,132,141,143,145,155,156,185,186
		            ,134,135,136,137,138,139,140,147,150,151
		            ,152,157,158,159,182,183,187,188};//first 3 numbers of mobile number;
			if (11 != mobileNumber.length())
				return false;
			String first3Str = mobileNumber.substring(0, 3);
			int num = Integer.parseInt(first3Str);
			for(int a:nums){
				if(a == num)
					return true;
			}
			return false;
	  }
	  
	  public static boolean isAllNumber(String number)
	  {
			if (number.length() == 0)
			{
				return false;
			}
			
			try {
				Integer.valueOf(number);
				return true;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			
			return false;
	  }
	  
	  public static boolean isRunYear(int year)
	  {
		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
		{
			return true;
		}
		
		return false;
	  }
	  
	  private static int months[] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	  public static boolean isVaildBirthday(Birthday birthday)
	  {
	    int year = birthday.year;
	    int month = birthday.month;
	    int day = birthday.day;
	    
	    if (year < 1900 || month < 1 || month > 12 || day < 1 || day > 31)
	    {
	    	return false;
	    }
	    
	    
	    int tmp = months[month];
	    if (isRunYear(year))
	    {
	    	tmp++;
	    }
	    
	    if (day > tmp)
	    {
	    	return false;
	    }


	    return true;
	  }
	  
	  public static boolean isVaildDate(int year, int month, int day)
	  {
	    if (year < 1900 || month < 1 || month > 12 || day < 1 || day > 31)
	    {
	    	log.e("year = " + year + ", month = " + month + ", day = " + day);
	    	return false;
	    }
	    
	    
	    int tmp = months[month];
	    if (isRunYear(year))
	    {
	    	tmp++;
	    }
	    
	    if (day > tmp)
	    {
	    	return false;
	    }


	    return true;
	  }
	  
	  public static boolean isValidTime(int hour, int minute)
	  {
		  if (hour < 0 || hour > 23 || minute < 0 || minute > 59)
		  {
			  return false;
		  }
		  
		  return true;
	  }
	  
	  
	  
	  public static boolean isIntersect(BaseType.SimpleTime1 mStartTime1, BaseType.SimpleTime1 mEndTime1,
			  							BaseType.SimpleTime1 mStartTime2, BaseType.SimpleTime1 mEndTime2)
	  {
		  if (mStartTime2.compare(mStartTime1) > 0 && mStartTime2.compare(mEndTime1) < 0)
		  {		  
			  return true;
		  }
		  
		  if (mEndTime2.compare(mStartTime1) > 0 && mStartTime2.compare(mEndTime1) < 0)
		  {		  
			  return true;
		  }
		  
		  if (mStartTime2.compare(mStartTime1) <= 0 && mEndTime2.compare(mEndTime1) >= 0)
		  {
			  return true;
		  }
		  
		  return false;
	  }
	  
	 public static boolean isIntersect(BaseType.WeekTime1 mWeekTime1, BaseType.WeekTime1 mWeekTime2)
	 {
		 int size1 = mWeekTime1.weekList.size();
		 int size2 = mWeekTime2.weekList.size();
		 if (size1 == 0 || size2 == 0)
		 {
			 return false;
		 }
		 
		 HashSet<Integer> set = new HashSet<Integer>();
		 for(int i = 0; i < size1; i++)
		 {
			 set.add(mWeekTime1.weekList.get(i));
		 }
		 
		 
		
		 for(int i = 0; i < size2; i++)
		 {
			 boolean flag = set.add(mWeekTime2.weekList.get(i));
			 if (flag == false)
			 {
				 return true;
			 }
		 }
		
		return false;
	 }
	  
	  public static boolean isIntersect(DeviceSetType.GpsStillTime time1, DeviceSetType.GpsStillTime time2)
	  {
		 BaseType.SimpleTime1 mStartTime1 = new BaseType.SimpleTime1();
		 BaseType.SimpleTime1 mEndTime1 = new BaseType.SimpleTime1();
		 BaseType.WeekTime1 mWeekTime1 = new BaseType.WeekTime1();
		
		 BaseType.SimpleTime1 mStartTime2 = new BaseType.SimpleTime1();
		 BaseType.SimpleTime1 mEndTime2 = new BaseType.SimpleTime1();
		 BaseType.WeekTime1 mWeekTime2 = new BaseType.WeekTime1();
		 
		 try {
			 mStartTime1.parseString(time1.mStartTimeString);
			 mEndTime1.parseString(time1.mEndTimeString);
			 mWeekTime1.parseString(time1.mWeekString);
			 
			 mStartTime2.parseString(time2.mStartTimeString);
			 mEndTime2.parseString(time2.mEndTimeString);
			 mWeekTime2.parseString(time2.mWeekString);
			 
			 if (isIntersect(mStartTime1, mEndTime1, mStartTime2, mEndTime2) && isIntersect(mWeekTime1, mWeekTime2))
			 {
				 return true;
			 }
			 
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		  
		  
		  return false;
	  }
	  
	  
	  public static boolean isValidRangeTime(BaseType.RangeTime rangeTime)
	  {
		  
		  
		  
		  
		  return true;
	  } 
}
