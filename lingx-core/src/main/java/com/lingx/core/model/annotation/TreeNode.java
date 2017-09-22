package com.lingx.core.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月10日 下午3:18:46 
 * 类说明 
 */

@Target(ElementType.FIELD)  
@Retention(RetentionPolicy.RUNTIME) 
public @interface TreeNode {

}
