package com.harmazing.spms.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.time.DateUtils;

import com.google.common.collect.Lists;

public class DateUtil {
	
	public static SimpleDateFormat norSdf = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat chinaSdf = new SimpleDateFormat("yyyy年MM月dd日");
	public static Date parseRssTtimeToDate(String dateStr) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
		return simpleDateFormat.parse(dateStr);
		
	}
	
	
	/**
	 * 将时间字符串转换成long型
	 * @param str
	 * @param format
	 * @throws ParseException
	 */
	public static Long getMills(String str, String format) throws ParseException{
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        Date date = sdf.parse(str);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if(str.indexOf("PM") > -1){
            calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR)+12);
        }
        //System.out.println(calendar.getTimeInMillis());
		return calendar.getTimeInMillis();
    }
	
	
	public static Date parseStringToDate(String dateStr, String pattern) throws ParseException {
	    String [] formatArr = dateStr.split(" ");
	    List <String> lists = Lists.newArrayList();
	    if(formatArr.length == 2) {
		lists.addAll(Arrays.asList(formatArr[0].split("-")));
		lists.addAll(Arrays.asList(formatArr[1].split(":")));
	    } else {
		lists.addAll(Arrays.asList(formatArr[0].split("-")));
	    }
	    
		   StringBuffer formatSB = new StringBuffer();
		    for(int i=0; i<lists.size(); i++) {
			if(i==0) {
			    StringBuffer year = new StringBuffer();
			    for(int j=0; j < lists.get(i).length(); j++) {
				year.append("y");
			    }
			    formatSB.append(year).append("-");
			}
			if(i==1) {
			    StringBuffer month = new StringBuffer();
			    for(int j=0; j < lists.get(i).length(); j++) {
				month.append("M");
			    }	
			    formatSB.append(month).append("-");
			}
			if(i==2) {
			    StringBuffer day = new StringBuffer();
			    for(int j=0; j < lists.get(i).length(); j++) {
				day.append("d");
			    }	
			    formatSB.append(day).append(" ");				    
			}
			if(i==3) {
			    StringBuffer hour = new StringBuffer();
			    for(int j=0; j < lists.get(i).length(); j++) {
				hour.append("h");
			    }	
			    formatSB.append(hour).append(":");
			}
			if(i==4) {
			    StringBuffer minute = new StringBuffer();
			    for(int j=0; j < lists.get(i).length(); j++) {
				minute.append("m");
			    }	
			    formatSB.append(minute).append(":");				    
			}
			if(i==5) {
			    StringBuffer s = new StringBuffer();
			    for(int j=0; j < lists.get(i).length(); j++) {
				s.append("s");
			    }	
			    formatSB.append(s);				    
			}
			
		    }
		    String format = formatSB.toString();
		    if(format.endsWith(":") || format.endsWith(" ")) {
			format = format.substring(0,format.length()-1);
		    }
		return DateUtils.parseDate(dateStr, format);
	}
	
	public static Date parseStringToDate(String dateStr) throws ParseException {
	
		return DateUtils.parseDate(dateStr, "yyyy-MM-dd");
	}
	
	public static String parseDateToString(Date date) throws ParseException {
		return parseDateToString(date,"yyyy-MM-dd hh:mm:ss");
	}
	
	public static String parseDateToString(Date date, String pattern) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(date);
	}
	
	public static String getDateString(Date date){
		return norSdf.format(date);
	}
	/**
	 * 获取当前时间
	 * @return
	 */
	public static String getNowDate(){
		return getDateString(new Date());
	}
	/**
	 * 获取当前年
	 * @return
	 */
	public static Integer getNowYear(){
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	/**
	 * 获取当前月
	 * @return
	 */
	public static Integer getNowMonth(){
		return Calendar.getInstance().get(Calendar.MONTH)+1;
	}
	/**
	 * 获取本月最后一天
	 * @return
	 */
	public static String getLastDayDate(SimpleDateFormat sdf){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
		return sdf.format(calendar.getTime());
	}
	/**
	 * 获取本月最后一天最后一刻。
	 * @return
	 */
	public static Date getLastDayDate(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
		calendar.set(Calendar.HOUR_OF_DAY,11);
		calendar.set(Calendar.MINUTE,59);
		calendar.set(Calendar.SECOND,59);
		return calendar.getTime();
	}
	public static Date getLastMonthLastDayDate(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
		calendar.set(Calendar.HOUR_OF_DAY,11);
		calendar.set(Calendar.MINUTE,59);
		calendar.set(Calendar.SECOND,59);
		return calendar.getTime();
	}
	/**
	 * 获取本月最早一天的凌晨
	 * @return
	 */
	public static Date getNewestDayDate(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		return calendar.getTime();
	}
	public static Date getLastMonthNewestDayDate(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH,-1);
		calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		return calendar.getTime();
	}
	/**
	 * 获取某日期的月中
	 * @return
	 */
	public static String getNewestDayDate(SimpleDateFormat sdf){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
		return sdf.format(calendar.getTime());
	}
	/**
	 * 输出本月的开始截止日期(x月x日-x月x日)
	 * @param sdf
	 * @return
	 */
	public static String getMonthBothEnds(SimpleDateFormat sdf, Date date){
		String monthStart = getNewestDayDate(sdf);
		monthStart = monthStart.substring(monthStart.indexOf('年')+1);
		String monthEnd = getLastDayDate(sdf);
		monthEnd = monthEnd.substring(monthEnd.indexOf('年')+1);
		return monthStart+"-"+monthEnd;
	}
	
	
	public static void main(String[] args) {
		System.out.println(getNowYear()+":"+getNowMonth());
		//System.out.println(getNowNewestDayDate(chinaSdf)+":"+getNowLastDayDate(chinaSdf));
		//System.out.println(getMonthBothEnds());
		//System.out.println(DateUtil.getMonthBothEnds(DateUtil.chinaSdf,new Date(new Date().getTime()-1000*3600*24*9)));
		System.out.println(getLastDayDate());
		System.out.println(getNewestDayDate());
		System.out.println(getLastDayDate().getTime()>getNewestDayDate().getTime());
	}
	
}
