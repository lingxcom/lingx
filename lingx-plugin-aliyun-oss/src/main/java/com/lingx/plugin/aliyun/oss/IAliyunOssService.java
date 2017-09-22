package com.lingx.plugin.aliyun.oss;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public interface IAliyunOssService {
	public String put(String file);
	public String put(File file);
	/**
	 * 往阿里云磁盘写入文件
	 * @param fileName
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	public String put(String fileName,long length,InputStream inputStream)throws Exception;
	/**
	 * 从阿里动磁盘中提取文件
	 * @param fileName
	 * @return
	 */
	public OutputStream get(String fileName);
}
