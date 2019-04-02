package com.harmazing;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Test {
	public static void main(String[] args) {		
		Calendar cal = Calendar.getInstance();
		int today = cal.get(Calendar.DAY_OF_WEEK)-1;
		System.out.println(today);
		
		System.out.println(cal.get(Calendar.HOUR));
		System.out.println(cal.get(Calendar.MINUTE));
		
		String val = "2016-04-23 06:00:00";
		String val2 = val.substring(0, val.indexOf(" ")+1) + "00:00:00";
		long t1 = convertTimeStr2Long(val, "yyyy-MM-dd HH:mm:ss");
		long t2 = convertTimeStr2Long(val2, "yyyy-MM-dd HH:mm:ss");
		
		System.out.println("t2-t1 = " + (t2-t1));
		
				
		int cur_time = (cal.get(Calendar.HOUR)*60*60 + cal.get(Calendar.MINUTE)*60)*1000;
		
		System.out.println("cur_time = " + cur_time);
		System.out.println("t2-t1 = " +cal.get(Calendar.HOUR) + ",   " + cal.get(Calendar.MINUTE));
		
		
		
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str_sdf = sdf1.format(cal.getTime());
		System.out.println(str_sdf);
		
	}
	
	private static long convertTimeStr2Long(String t, String format){
		SimpleDateFormat sdf= new SimpleDateFormat(format);
		Date dt = null;
		try{
			dt = sdf.parse(t);
		}catch(Exception e){
			e.printStackTrace();
		}
		return dt.getTime();
	}
}
