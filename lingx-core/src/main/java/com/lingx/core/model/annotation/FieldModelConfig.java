package com.lingx.core.model.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月5日 上午11:38:18 
 * 类说明 
 */
@Target(ElementType.FIELD)  
@Retention(RetentionPolicy.RUNTIME)  
@Documented  
@Inherited 
public @interface FieldModelConfig  {
	public String sort() default "z";
	public String name() default "";
	public String inputType() default "string";
	public String editor() default "text";
	public boolean isTreeNode() default false;


}
