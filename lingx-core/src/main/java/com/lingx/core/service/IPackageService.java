package com.lingx.core.service;

import java.io.OutputStream;

import com.lingx.core.model.bean.PackBean;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年9月26日 上午1:21:30 
 * 类说明 
 */
public interface IPackageService {

	public void packAndDownload(PackBean bean,String basePath,OutputStream outputStream);
	
	public String packAndUpload(PackBean bean,String basePath,String secret);
	
	public String uploadPackLingx(PackBean bean,String basePath,String secret);
}
