package com.lingx.core.service;

import java.io.File;
import java.net.URL;
import java.util.Map;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年8月2日 下午11:08:36 
 * 类说明 
 */
public interface IUpdateService {
	
	public boolean update(File file,String basePath);
	public boolean update(URL url,String basePath);
}
