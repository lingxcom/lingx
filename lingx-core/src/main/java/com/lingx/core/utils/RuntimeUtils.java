package com.lingx.core.utils;
/** 
 * @author www.lingx.com

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
