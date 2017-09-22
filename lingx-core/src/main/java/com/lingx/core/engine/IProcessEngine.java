package com.lingx.core.engine;

import com.lingx.core.exception.LingxCascadeException;
import com.lingx.core.exception.LingxChooseException;
import com.lingx.core.exception.LingxConvertException;
import com.lingx.core.exception.LingxProcessException;
import com.lingx.core.exception.LingxValidatorException;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月5日 下午3:11:49 
 * 类说明 
 */
public interface IProcessEngine {
	public static final String LINGX="http://www.lingx.com";
	
	public static final String KEY_ENTITY="e";
	public static final String KEY_METHOD="m";
	public static final String KEY_TYPE="t";
	
	public static final int TYPE_VIEW=1;
	public static final int TYPE_VIEW_CASCADE=2;
	public static final int TYPE_PROCESS=3;
	public String process(IContext context)throws LingxProcessException,LingxChooseException,LingxConvertException,LingxValidatorException,LingxCascadeException;
}
