package com.lingx.core.service;

import java.awt.image.BufferedImage;

import com.lingx.core.engine.IContext;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年6月11日 下午10:30:12 
 * 类说明 
 */
public interface IVerifyCodeService {

	public BufferedImage getVerifyCodeImage(String code,int width,int height);
	
	public boolean verify(IContext context);
}
