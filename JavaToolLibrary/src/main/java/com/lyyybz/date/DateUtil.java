package com.lyyybz.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil
{
	/*
	* 获取某年某月的最大天数。
	*/
	public int getDayOfMonth( int year, int month )
	{
	         int days[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	         if( 2 == month && 0 == (year % 4) && ( 0 != (year % 100) || 0 == (year % 400) ) )
	         {
	             days[1] = 29;
	         }
	         return( days[month -1] );
	}
	/*
	* 获取年的最大天数，即闰年366，平年365；每月的最大天数同理。
	*/
	public int getDayOfYear()
	{
	     Calendar calendar = Calendar.getInstance();
	     calendar.set(Calendar.YEAR, 2000);          // 不设置则为当前时间的年份
	     int day = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
	     return day;
	}
	/*
	* 获取当前时间，中国时区，格式 yy-MM-dd HH:mm:ss:SSS
	*/
	public static String getDateTimeofNow()
	{
	     String date;
	     SimpleDateFormat myfmt = new SimpleDateFormat("[yy-MM-dd HH:mm:ss:SSS]");
	     TimeZone timeZoneChina = TimeZone.getTimeZone("Asia/Shanghai");
	     myfmt.setTimeZone(timeZoneChina);
	     date = myfmt.format(new Date());
	     return date;
	}
	/*
	* 获取当前时间，中国时区，格式 yyMMddHHmmss
	*/
	public static String getDateTimeofNow2()
	{
	     String date;
	     SimpleDateFormat myfmt = new SimpleDateFormat("yyMMddHHmmss");
	     TimeZone timeZoneChina = TimeZone.getTimeZone("Asia/Shanghai");
	     myfmt.setTimeZone(timeZoneChina);
	     date = myfmt.format(new Date());
	     return date;
	}
	/*
	* 计算当前时间curDate间隔interval之后的时间，格式为 yyMMddHHmmss
	*/    
	public static String getNextDateTime(String curDate, long interval) throws ParseException
	{
	     SimpleDateFormat myfmt = new SimpleDateFormat("yyMMddHHmmss");
	     TimeZone timeZoneChina = TimeZone.getTimeZone("Asia/Shanghai");
	     myfmt.setTimeZone(timeZoneChina);    
	     Date date = myfmt.parse(curDate);
	     long ms = date.getTime() + interval*1000;
	     return myfmt.format(new Date(ms));    
	}
	/*
	* 计算当前时间curDate间隔interval之前的时间，格式为 yyMMddHHmmss
	*/    
	public static String getPreviousDateTime(String curDate, long interval) throws ParseException
	{
	     SimpleDateFormat myfmt = new SimpleDateFormat("yyMMddHHmmss");
	     TimeZone timeZoneChina = TimeZone.getTimeZone("Asia/Shanghai");
	     myfmt.setTimeZone(timeZoneChina);
	     Date date = myfmt.parse(curDate);
	     long ms = date.getTime() - (long)interval*1000;    
	     return myfmt.format(new Date(ms));
	}
	    
	public static int getTimeDiff(String newDate, String oldDate) throws ParseException
	{
	     SimpleDateFormat myfmt = new SimpleDateFormat("yyMMddHHmmss");
	     TimeZone timeZoneChina = TimeZone.getTimeZone("Asia/Shanghai");
	     myfmt.setTimeZone(timeZoneChina);    
	     Date date1 = myfmt.parse(newDate);
	     Date date2 = myfmt.parse(oldDate);
	     long ms1 = date1.getTime();
	     long ms2 = date2.getTime();
	     long sdiff = (ms1-ms2)/1000;
	     return (int) Math.abs(sdiff);
	}
	    
	public static long compareTime(String strdate1, String strdate2) throws ParseException
	{
	     SimpleDateFormat myfmt = new SimpleDateFormat("yyMMddHHmmss");
	     TimeZone timeZoneChina = TimeZone.getTimeZone("Asia/Shanghai");
	     myfmt.setTimeZone(timeZoneChina);
	     Date date1 = myfmt.parse(strdate1);
	     Date date2 = myfmt.parse(strdate2);
	     long ms1 = date1.getTime();
	     long ms2 = date2.getTime();
	     long sdiff = (ms1-ms2)/1000;
	     return sdiff;
	}
	    
	public static boolean checkDateTime(String tmp) throws ParseException
	{
	     SimpleDateFormat myfmt = new SimpleDateFormat("yyMMddHHmmss");
	     TimeZone timeZoneChina = TimeZone.getTimeZone("Asia/Shanghai");
	     myfmt.setTimeZone(timeZoneChina);    
	     Date date = myfmt.parse(tmp);
	     if(myfmt.format(date).compareTo(tmp) == 0)
	     {
	          return true;
	     }
	     return false;
	}
	public static String getDateString(Date date)
	{
	     String tmp;
	     SimpleDateFormat myfmt = new SimpleDateFormat("yyMMddHHmmss");
	     TimeZone timeZoneChina = TimeZone.getTimeZone("Asia/Shanghai");
	     myfmt.setTimeZone(timeZoneChina);
	     tmp = myfmt.format(date).toString();
	     return tmp;
	}
	
	
	/**
	 * 获取随机日期
	 * @param beginDate
	 *            起始日期，格式为：yyyy-MM-dd
	 * @param endDate
	 *            结束日期，格式为：yyyy-MM-dd
	 * @return
	 */
	public static Date randomDate(String beginDate, String endDate) 
    {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date start = format.parse(beginDate);// 构造开始日期
			Date end = format.parse(endDate);// 构造结束日期
			// getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
			if (start.getTime() >= end.getTime()) 
            {
				return null;
			}
			long date = random(start.getTime(), end.getTime());
			return new Date(date);
		} catch (Exception e) 
        {
			e.printStackTrace();
		}
		return null;
	}
	private static long random(long begin, long end) 
    {
		long rtn = begin + (long) (Math.random() * (end - begin));
        // java.lang.Math.random() 返回一个正符号的double值，大于或等于0.0且小于1.0
		// 如果返回的是开始时间和结束时间，则递归调用本函数查找随机值
		if (rtn == begin || rtn == end) 
        {
			return random(begin, end);
		}
		return rtn;
	}
}
