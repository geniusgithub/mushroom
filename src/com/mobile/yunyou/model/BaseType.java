package com.mobile.yunyou.model;

import java.util.ArrayList;
import java.util.List;

import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.VertifyUtil;
import com.mobile.yunyou.util.YunTimeUtils;

import android.R.integer;
import android.util.Log;

public class BaseType {

	private static final CommonLog log = LogFactory.createLog();
	
	
	public static class SimpleTime1 implements IParseString
	{
		public int hour = 0;
		public int minute = 0;
		
		
		@Override
		public String toString() {
			// TODO Auto-generated method stub

			return String.format("%02d%02d", hour, minute);
		
		}


		@Override
		public boolean parseString(String str) throws Exception {
			// TODO Auto-generated method stub

			hour = Integer.parseInt(str.substring(0, 2));
			minute = Integer.parseInt(str.substring(2));
			
			return true;

		}
		
		public String toDisplayString() {
			// TODO Auto-generated method stub

			return String.format("%02d:%02d", hour, minute);
		
		}
		
		public int compare(SimpleTime1 time)
		{
			if (hour > time.hour)
			{
				return 1;
			}
			
			if (hour < time.hour)
			{
				return -1;
			}
			
			if (minute == time.minute)
			{
				return 0;
			}
			
			return minute > time.minute ? 1 : -1;
		}
		
		
	}
	
	public static class WeekTime1 implements IParseString
	{
		public List<Integer> weekList = new ArrayList<Integer>();
	
		
		
		@Override
		public String toString() {
			// TODO Auto-generated method stub

			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("[");
			int size = weekList.size();
			for(int i = 0; i < size; i++)
			{
				stringBuffer.append("\"" + weekList.get(i) + "\"");
				if (i != size - 1)
				{
					stringBuffer.append(",");
				}
			
			}
			stringBuffer.append("]");
			return stringBuffer.toString();
		
		}
		
		public String toDisplayString() {
			// TODO Auto-generated method stub

			StringBuffer stringBuffer = new StringBuffer();
			int size = weekList.size();
			for(int i = 0; i < size; i++)
			{
				int week = weekList.get(i);
				String weekString = getDisplayString(week);
				stringBuffer.append(weekString);
				if (i != size - 1)
				{
					stringBuffer.append(",");
				}
			}
			
			return stringBuffer.toString();
		
		}
		
		public String toDisplayString1() {
			// TODO Auto-generated method stub

			StringBuffer stringBuffer = new StringBuffer();
			int size = weekList.size();
			for(int i = 0; i < size; i++)
			{
				int week = weekList.get(i);
				String weekString = getDisplayString1(week);
				stringBuffer.append(weekString);
				if (i != size - 1)
				{
					stringBuffer.append(",");
				}
			}
			
			return stringBuffer.toString();
		
		}


		@Override
		public boolean parseString(String str) throws Exception {
			// TODO Auto-generated method stub
			weekList.clear();
			
			str = str.substring(1, str.length() - 1);
		
			String strs[] = str.split(",");
			int size = strs.length;
			for(int i = 0; i < size; i++)
			{

				String tmp = strs[i];
				tmp = tmp.substring(1, tmp.length() - 1);
				weekList.add(Integer.parseInt(tmp));
			}
			
			return true;

		}
		
		public static String getDisplayString(int week)
		{
			String str = "";
			switch (week) {
				case 1:
					str = "周一";
					break;
				case 2:
					str = "周二";	
					break;
				case 3:
					str = "周三";
					break;
				case 4:
					str = "周四";
					break;
				case 5:
					str = "周五";
					break;
				case 6:
					str = "周六";
					break;
				case 7:
					str = "周日";
					break;	
				default:
					break;
			}
			
			return str;
			
		}
		
		public static String getDisplayString1(int week)
		{
			String str = "";
			switch (week) {
				case 1:
					str = "一";
					break;
				case 2:
					str = "二";	
					break;
				case 3:
					str = "三";
					break;
				case 4:
					str = "四";
					break;
				case 5:
					str = "五";
					break;
				case 6:
					str = "六";
					break;
				case 7:
					str = "七";
					break;	
				default:
					break;
			}
			
			return str;
		}
		
		
	}
	
	
	public static class RangeTime
	{
		public int year = 0;
		public int month = 0;
		public int day = 0;
		public int startHour = 0;
		public int startMinute = 0;
		public int endHour = 0;
		public int endMinute = 0;

		public String getStartTime()
		{
			return String.format("%04d-%02d-%02d %02d:%02d:00", year, month, day, startHour, startMinute);
		}
		
		public String getEndTime()
		{
			return String.format("%04d-%02d-%02d %02d:%02d:00", year, month, day, endHour, endMinute);
		}

		
		public boolean isValid()
		{
			if (!VertifyUtil.isVaildDate(year, month, day))
			{
				log.e("isVaildDate fail");
				return false;
			}
			
			if (!VertifyUtil.isValidTime(startHour, startMinute) || !VertifyUtil.isValidTime(endHour, endMinute))
			{
				log.e("isValidTime fail");
				return false;
			}
			
			if (startHour * 60 + startMinute >= endHour * 60 + endMinute)
			{
				log.e("startTime > endTime");
				return false;
			}
			
			return true;
		}
		
	}
	
	
	public static class Birthday implements IParseString
	{
		public int year = 0;
		public int month = 0;
		public int day = 0;
		
		
		@Override
		public String toString() {
			// TODO Auto-generated method stub

			return String.format("%04d-%02d-%02d", year, month, day);
		
		}


		@Override
		public boolean parseString(String str) throws Exception {
			// TODO Auto-generated method stub

			String ayyay[] = str.split("-");
			year = Integer.parseInt(ayyay[0]);
			month = Integer.parseInt(ayyay[1]);
			day = Integer.parseInt(ayyay[2]);
			
			return true;

		}	
		
	}
	
	public static class BaseLocation
	{
		public double lat = 0;
		public double lon = 0;
		
		
	}
}
