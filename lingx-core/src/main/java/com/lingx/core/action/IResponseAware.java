package com.lingx.core.action;

import javax.servlet.http.HttpServletResponse;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年6月11日 下午9:46:01 
 * 类说明 
 */
public interface IResponseAware {

	public void setResponse(HttpServletResponse response);
}
