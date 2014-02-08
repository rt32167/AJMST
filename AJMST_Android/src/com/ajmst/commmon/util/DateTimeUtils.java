/**
 * @(#)DateTimeUtils.java
 *
 * Copyright(c) 2013-2020 cj���˳��� ��Ȩ����
 * LiuJingYu personnel program. All rights reserved.
 */

package com.ajmst.commmon.util;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * ����ʱ�䴦������
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
     * formatDate(date,"yyyy-MM-dd HH:mm:ss") ����"2012-09-06 14:16:37"
     * @param date ��Ҫ��ʽ��������
     * @param pattern year=yyyy month=MM day=dd hour=HH minute=mm second=ss
     * @return ����ָ����ʽ����������ת�����ַ�����ʽ
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
     * changeDateFormat("2012-12-01","yyyy-MM-dd","yyyy/MM/dd")����"2012/12/01",ע���������쳣Ĭ�Ϸ��ؿ��ַ���""
     * @param dateTime ��������
     * @param inputFormat �������ڵĸ�ʽ
     * @param outputFormat ������ڵĸ�ʽ
     * @return ��������ڸ�ʽת���������
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
     * ע�����24Сʱ��������0��
     * @param fromDate
     * @param toDate
     * @return ��������֮�������
     */
    public static int getDays(java.util.Date fromDate,java.util.Date toDate){
        long a = getMilliseconds(fromDate,toDate)/(24*60*60*1000);
        return (int)a;
    }
    

    /**
     * ע�����60����������0Сʱ
     * @param fromDate
     * @param toDate
     * @return ��������ʱ��֮���Сʱ��
     */
    public static int getHours(java.util.Date fromDate,java.util.Date toDate){
        long a = getMilliseconds(fromDate,toDate)/(60*60*1000);
        return (int)a;
    }
    
    
    /**
     * ע�����60����������0��
     * @param fromDate
     * @param toDate
     * @return ��������ʱ��֮��ķ�����
     */
    public static int getMinutes(java.util.Date fromDate,java.util.Date toDate){
        long a = getMilliseconds(fromDate,toDate)/(60*1000);
        return (int)a;
    }
    
    
    /**
     * ע�����1000������������0��
     * @param fromDate
     * @param toDate
     * @return ��������ʱ��֮�������
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
     * @return �������ڼ�ĺ�����
     */
    public static Long getMilliseconds(java.util.Date fromDate,java.util.Date toDate){
        long m1 = fromDate.getTime();
        long m2 = toDate.getTime();
        return m2 - m1;
    }
    

    /**
     * �Ƚ������ڴ�С,��ֻ�Ƚϵ���,���Ƚ�ʱ����
     * @param date1
     * @param date2
     * @return date1���ڴ���date2,�򷵻�ֵ1;date1����С��date2,�򷵻�ֵС��-1;��ȷ���0
     */
    public static int compareDay(java.util.Date date1,java.util.Date date2)
    {
        return compareDate(getDayStart(date1),getDayStart(date2));
    }
    
    
    /**
     * @param date1
     * @param date2
     * @return ǰ�ߴ��ں���,����1;ǰ��С�ں���,����-1;������ȷ���0
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
     * parseDate("2012-12-01","yyyy-MM-dd"),�������쳣(����������ַ�����ָ����ʱ���ʽ����),����null
     * @param text
     * @param pattern
     * @return ����ָ����ʽ���ַ���ת���������
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
     * ��������Ϊ2012-12-1 19:18:17, ��������Ϊ2012-12-1 00:00:00
     * @author caijun modified 2013-12-05 ����������Ϊ0,����ʱ��Ƚ�ʱ���ܲ����,c.set(Calendar.MILLISECOND, 0);
     * @param date
     * @return �����ڵĿ�ʼʱ��,�����յ�0ʱ0��0��0����
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
