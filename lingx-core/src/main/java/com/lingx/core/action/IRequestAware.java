package com.lingx.core.action;

import javax.servlet.http.HttpServletRequest;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年6月11日 下午9:45:42 
 * 类说明 
 */
public interface IRequestAware {

	public void setRequest(HttpServletRequest request);
}
