package com.lingx.core.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月25日 下午9:48:22 
 * 类说明 
 */
public class AnnoUtils {

	public static List<Object> getField(Class clazz,Class clazz2,Object object){
		List<Object> objects=new ArrayList<Object>();
		Field[] fields1=clazz.getDeclaredFields();
		Field[] fields2=clazz.getSuperclass().getDeclaredFields();
		Field[] fields=new Field[fields1.length+fields2.length];
		System.arraycopy(fields1, 0, fields, 0, fields1.length);
		System.arraycopy(fields2, 0, fields, fields1.length, fields2.length);
		for(Field field:fields){
			field.setAccessible(true); 
			if(field.isAnnotationPresent(clazz2)){
				try {
					objects.add(field.get(object));
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return objects;
	}
	
}
