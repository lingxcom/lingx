package com.lingx.core.utils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/** 
 * @author www.lingx.com
 * @version 创建时间：2016年3月15日 上午9:00:56 
 * 类说明 
 */
public class DateUtils {

	public static final java.text.SimpleDateFormat format1 = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
	public static final java.text.SimpleDateFormat format2 = new java.text.SimpleDateFormat("yyyy-MM-dd");
	public static final java.text.SimpleDateFormat format3 = new java.text.SimpleDateFormat("HH:mm");
	public static final java.text.SimpleDateFormat format4 = new java.text.SimpleDateFormat("MM-dd");
	public static String getTime(){
		return Utils.getTime();
	}
	/**
	 * 格式化20150315121212 -> 2015-03-15
	 * @param time
	 * @return
	 */
	public static String format14To8(String time){
		
		return new String(time.substring(0,4)+"-"+time.substring(4,6)+"-"+time.substring(6,8));
	}
	/**
	 * 智能解析日期
	 * 昨天 16:15
	   04-11 16:15
	 * 2015-04-16 16:15
	 * @param time
	 * @return
	 */
	public static String formatBi(String time){
		StringBuilder sb=new StringBuilder();
		String arr1[]=new String[]{"前天","昨天","今天","明天","后天"};
		int arr2[]=new int[]{-2,-1,0,1,2};
		java.util.GregorianCalendar gc=new java.util.GregorianCalendar();
		try {
			Date current = null;
			Date temp = format1.parse(time);
			for(int i=0;i<arr2.length;i++){
				int a=arr2[i];
				current = new Date();
				gc.setTime(current);
				gc.add(Calendar.DATE,a);
				current=gc.getTime();
				//System.out.println(format2.format(current)+" VS "+format2.format(temp));
				if(format2.format(current).equals(format2.format(temp))){
					sb.append(arr1[i]);
					break;
				}
			}

			if(sb.length()==0){
				current = new Date();
				if(temp.getYear()==current.getYear()){
					sb.append(format4.format(temp));
				}else{
					sb.append(format2.format(temp));
				}
			}
			
			sb.append(" ");
			sb.append(format3.format(temp));
		} catch (Exception e) {
			//e.printStackTrace();
			
			return time;
		}

		return sb.toString();
	}
	
	public static void main(String args[]){
		System.out.println(formatBi("20160411161542"));
	}
}
