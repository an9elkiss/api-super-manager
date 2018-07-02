package com.an9elkiss.api.manager.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;

public class DateTools {

	/**
	 * 根据年月周获取该周的第一天
	 * @param year
	 * @param month
	 * @param week
	 * @return
	 */
	public static Date getFirstDayOfWeek(int year, int month, int week) {  
	  
	    final Calendar c = Calendar.getInstance();  
	    c.set(Calendar.YEAR, year);  
	    c.set(Calendar.MONTH, month - 1);  
	    c.set(Calendar.DAY_OF_MONTH, 1); // 设为每个月的第一天(1号)  
	  
	    int dayOfWeek = c.get(Calendar.DAY_OF_WEEK); // 每个月的第一天为星期几  
	  
	    /* 
	     * 星期日:1,星期一:2,星期二:3,星期三:4,星期四:5,星期五:6,星期六:7 
	     * 转化为我们的使用习惯:星期一:1,星期二:2,星期三:3,星期四:4,星期五:5,星期六:6,星期日:7 
	     */  
	    if (dayOfWeek != Calendar.SUNDAY)  
	    {  
	        dayOfWeek = dayOfWeek - 1;  
	    }  
	    else  
	    {  
	        dayOfWeek = 7;  
	    }  
	    c.add(Calendar.DAY_OF_MONTH, 1 - dayOfWeek); // 使其为每个月第一天所在周的星期一  
	    c.add(Calendar.DAY_OF_MONTH, (week - 1) * 7); 
	  
	    return c.getTime();  
	} 
	
	/**
	 * 根据年月周获取该周的最后一天
	 * @param year
	 * @param month
	 * @param week
	 * @return
	 */
	public static Date getLastDayOfWeek(int year, int month, int week) {  
	    final Calendar c = Calendar.getInstance();  
	    c.set(Calendar.YEAR, year);  
	    c.set(Calendar.MONTH, month - 1);  
	    c.set(Calendar.DAY_OF_MONTH, 1); // 设为每个月的第一天(1号)  
	  
	    int dayOfWeek = c.get(Calendar.DAY_OF_WEEK); // 每个月的第一天为星期几  
	  
	    /* 
	     * 星期日:1,星期一:2,星期二:3,星期三:4,星期四:5,星期五:6,星期六:7 
	     * 转化为我们的使用习惯:星期一:1,星期二:2,星期三:3,星期四:4,星期五:5,星期六:6,星期日:7 
	     */  
	    if (dayOfWeek != Calendar.SUNDAY)  
	    {  
	        dayOfWeek = dayOfWeek - 1;  
	    }  
	    else  
	    {  
	        dayOfWeek = 7;  
	    }  
	    c.add(Calendar.DAY_OF_MONTH, 1 - dayOfWeek); // 使其为每个月第一天所在周的星期一  
	    c.add(Calendar.DAY_OF_MONTH, (week - 1) * 7 + 6); 
	  
	    return c.getTime();    
	} 
	
	/**
	 * 根据传入的日期返回改日期属于，哪一年、哪一月、哪一周
	 * @param date 日期(2018-05-05)
	 * @return
	 * @throws ParseException
	 */
	public static Map<String, Integer> getYearMonthWeek(Date date) throws ParseException {
		Map<String, Integer> map = new HashMap<>();
		// 找到本周7天
		Iterator<Calendar> iterator = DateUtils.iterator(date, DateUtils.RANGE_WEEK_MONDAY);
		Calendar calendar = iterator.next();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);   
		map.put("year", calendar.get(Calendar.YEAR));
		map.put("month", calendar.get(Calendar.MONTH) + 1);
		map.put("week", calendar.get(Calendar.WEEK_OF_MONTH));
		return map;
	}
	
	/**
	 * 根据年月获取本月共有几周
	 * @param year
	 * @param month
	 * @return
	 * @throws ParseException 
	 */
	public static Integer getWeekCount(String year, String month) throws ParseException{
	    Calendar c = Calendar.getInstance();  
        c.set(Calendar.YEAR, Integer.valueOf(year));  
        c.set(Calendar.MONTH, (Integer.valueOf(month) - 1));  
        c.setFirstDayOfWeek(Calendar.MONDAY);   
        return c.getActualMaximum(Calendar.WEEK_OF_MONTH);
	}
	
	/**
	 * 根据年月周获取上周的第一天
	 * @param year
	 * @param month
	 * @param week
	 * @return
	 * @throws ParseException 
	 */
	public static Date getFirstDayOfLastWeek(int year, int month, int week){
		Date date = getFirstDayOfWeek(year, month, week);
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        calendar.add(Calendar.DAY_OF_MONTH, -7);  
        return calendar.getTime();
	}
	
	/**
	 * 根据年月周获取上周的最后一天
	 * @param year
	 * @param month
	 * @param week
	 * @return
	 * @throws ParseException 
	 */
	public static Date getLastDayOfLastWeek(int year, int month, int week){
		Date date = getFirstDayOfWeek(year, month, week); 
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        calendar.add(Calendar.DAY_OF_MONTH, -1);  
        return calendar.getTime();
	}
	
	/**
	 * 根据年月周获取下周的第一天
	 * @param year
	 * @param month
	 * @param week
	 * @return
	 * @throws ParseException 
	 */
	public static Date getFirstDayOfNextWeek(int year, int month, int week){
		Date date = getFirstDayOfWeek(year, month, week);
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        calendar.add(Calendar.DAY_OF_MONTH, 7);  
        return calendar.getTime();
	}
	
	/**
	 * 根据年月周获取下周的最后一天
	 * @param year
	 * @param month
	 * @param week
	 * @return
	 * @throws ParseException 
	 */
	public static Date getLastDayOfNextWeek(int year, int month, int week){
		Date date = getFirstDayOfWeek(year, month, week); 
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        calendar.add(Calendar.DAY_OF_MONTH, 13);  
        return calendar.getTime();
	}
}
