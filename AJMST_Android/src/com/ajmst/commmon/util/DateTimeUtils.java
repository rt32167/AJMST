/**
 * @(#)DateTimeUtils.java
 *
 * Copyright(c) 2013-2020 cj个人程序 版权所有
 * LiuJingYu personnel program. All rights reserved.
 */

package com.ajmst.commmon.util;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * 日期时间处理函数集
 * @author cher
 * @version 1.0 2004-03-22 created
 * @author caijun
 * @version 1.1 2012-12-07 modified
 */
public class DateTimeUtils 
{	
    /**
     * Calendar c = Calendar.getInstance();
     * c.set(2012,8, 6, 14, 16,37);
     * Date date = c.getTime();
     * formatDate(date,"yyyy-MM-dd HH:mm:ss") 返回"2012-09-06 14:16:37"
     * @param date 需要格式化的日期
     * @param pattern year=yyyy month=MM day=dd hour=HH minute=mm second=ss
     * @return 根据指定格式将输入日期转换成字符串格式
     */
    public static String formatDate(java.util.Date date,String pattern){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(date.getTime());
        }catch(Exception e){
            return "";
        }
    }

    /**
     * changeDateFormat("2012-12-01","yyyy-MM-dd","yyyy/MM/dd")返回"2012/12/01",注意若发生异常默认返回空字符串""
     * @param dateTime 输入日期
     * @param inputFormat 输入日期的格式
     * @param outputFormat 输出日期的格式
     * @return 按输出日期格式转换后的日期
     */
    public static String changeDateFormat(String dateTime,String inputFormat,String outputFormat)
    {
        try{
            SimpleDateFormat iFormat = new  SimpleDateFormat(inputFormat);
            java.util.Date iDate = iFormat.parse(dateTime);
            SimpleDateFormat oFormat = new SimpleDateFormat(outputFormat);
            String sRet = oFormat.format(iDate);
            return sRet;
        }
        catch(Exception e)
        {
            return "";
        }
    }

    /**
     * 注意相差24小时以内则算0天
     * @param fromDate
     * @param toDate
     * @return 两个日期之间的天数
     */
    public static int getDays(java.util.Date fromDate,java.util.Date toDate){
        long a = getMilliseconds(fromDate,toDate)/(24*60*60*1000);
        return (int)a;
    }
    

    /**
     * 注意相差60分以内则算0小时
     * @param fromDate
     * @param toDate
     * @return 两个日期时间之间的小时数
     */
    public static int getHours(java.util.Date fromDate,java.util.Date toDate){
        long a = getMilliseconds(fromDate,toDate)/(60*60*1000);
        return (int)a;
    }
    
    
    /**
     * 注意相差60秒以内则算0分
     * @param fromDate
     * @param toDate
     * @return 两个日期时间之间的分钟数
     */
    public static int getMinutes(java.util.Date fromDate,java.util.Date toDate){
        long a = getMilliseconds(fromDate,toDate)/(60*1000);
        return (int)a;
    }
    
    
    /**
     * 注意相差1000毫秒以内则算0秒
     * @param fromDate
     * @param toDate
     * @return 两个日期时间之间的秒数
     */
    public static int getSeconds(java.util.Date fromDate,java.util.Date toDate){
        long m1 = fromDate.getTime();
        long m2 = toDate.getTime();
        long a = (m2-m1)/1000;
        return (int)a;
    }
    
    
    /**
     * 
     * @param fromDate
     * @param toDate
     * @return 两个日期间的毫秒数
     */
    public static Long getMilliseconds(java.util.Date fromDate,java.util.Date toDate){
        long m1 = fromDate.getTime();
        long m2 = toDate.getTime();
        return m2 - m1;
    }
    

    /**
     * 比较两日期大小,即只比较到日,不比较时分秒
     * @param date1
     * @param date2
     * @return date1日期大于date2,则返回值1;date1日期小于date2,则返回值小于-1;相等返回0
     */
    public static int compareDay(java.util.Date date1,java.util.Date date2)
    {
        return compareDate(getDayStart(date1),getDayStart(date2));
    }
    
    
    /**
     * @param date1
     * @param date2
     * @return 前者大于后者,返回1;前者小于后者,返回-1;两者相等返回0
     */
    public static int compareDate(java.util.Date date1,java.util.Date date2){
    	int result = date1.compareTo(date2);
    	if(result > 0){
    		result = 1;
    	}else if(result < 0){
    		result = -1;
    	}else{
    		result = 0;
    	}
    	return result;
    }
    

    /**
     * parseDate("2012-12-01","yyyy-MM-dd"),若发生异常(传入的日期字符串与指定的时间格式不符),返回null
     * @param text
     * @param pattern
     * @return 根据指定格式将字符床转换后的日期
     */
    public static java.util.Date parseDate(String text,String pattern)
    {
       try{
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(text);
       }catch(Exception e){
            return null;
       }
    }
  
    
    
    /**
     * 传入日期为2012-12-1 19:18:17, 返回日期为2012-12-1 00:00:00
     * @author caijun modified 2013-12-05 将毫秒设置为0,否则时间比较时可能不相等,c.set(Calendar.MILLISECOND, 0);
     * @param date
     * @return 该日期的开始时间,即该日的0时0分0秒0毫秒
     */
    public static java.util.Date getDayStart(java.util.Date date){
    	Calendar c = Calendar.getInstance();
    	c.setTime(date);
    	c.set(Calendar.HOUR_OF_DAY, 0);
    	c.set(Calendar.MINUTE, 0);
    	c.set(Calendar.SECOND, 0);
    	c.set(Calendar.MILLISECOND, 0);
    	return c.getTime();
    }
    
}
