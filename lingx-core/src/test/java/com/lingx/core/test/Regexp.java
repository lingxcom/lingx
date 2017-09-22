package com.lingx.core.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年10月15日 下午11:07:45 
 * 类说明 
 */
public class Regexp {
	
	@Test
	public void test(){
		String temp="asdfaf${abc} 0${asf}fda";
		Pattern pattern = Pattern.compile("\\$\\{{1}\\}");
		Matcher matcher = pattern.matcher(temp);
		StringBuffer buffer = new StringBuffer();
		while(matcher.find()){              
		    buffer.append(matcher.group());        
		    buffer.append("/r/n");              
		System.out.println(buffer.toString());
		}
		        
	}
}
