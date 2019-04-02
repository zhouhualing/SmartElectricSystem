package com.harmazing.spms.base.initData;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuliepeng on 2014/10/7.
 */
public class GenerateSN {
    private static Map<String,String> map=new HashMap<String, String>();
    private  static String STATNUM="000001";
    public  String getYearAndMonth(){
        StringBuffer sb=new StringBuffer();
        Calendar cal = new GregorianCalendar();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH)+1;
        sb.append(year);
        if(month<10){
            sb.append("0"+month);
        }else{
            sb.append(month);
        }

        return sb.toString();
    }

    public String getLastSixNum(String s){
        String rs=s;
        int i=Integer.parseInt(rs);
        i+=1;
        rs=""+i;
        for (int j = rs.length(); j <6; j++) {
            rs="0"+rs;
        }
        return rs;
    }
    public synchronized  String getNum(String prefix){
        String yearAMon=getYearAndMonth();
        String last6Num=map.get(yearAMon);
        if(last6Num==null){
            map.put(yearAMon,STATNUM);
        }else{
            map.put(yearAMon,getLastSixNum(last6Num));
        }
        return prefix + yearAMon + map.get(yearAMon);
    }
    public static void main(String []args){
        GenerateSN t= new GenerateSN();
        for(int i=0;i<10000;i++){
            System.out.println(t.getNum("AC-"));
        }

    }}
