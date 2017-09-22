package com.lingx.core.utils;
/** 
 * @author www.lingx.me
 * @version 创建时间：2015年10月12日 下午2:27:10 
 * 类说明 
 */
public class RuntimeUtils {

	private static long stime=0;
	
	public static void reset(){
		stime=System.currentTimeMillis();
	}
	
	public static void printTime(){
		long t=System.currentTimeMillis()-stime;
		System.out.println((t)+"ms");
	}
}
