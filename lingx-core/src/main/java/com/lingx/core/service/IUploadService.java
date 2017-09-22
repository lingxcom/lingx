package com.lingx.core.service;

import java.io.InputStream;
import java.util.Map;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年7月24日 下午4:27:57 
 * 类说明 
 */
public interface IUploadService {
	/**
	 * 上传文件
	 * @param inputstream
	 * @return
	 */
	public Map<String,Object> uploadFile(String name,long size,InputStream inputStream);
}
