package com.lingx.plugin.aliyun.oss;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.lingx.core.service.IUploadService;
@Component(value="aliyunOssUploadService")
public class AliyunOssUploadService implements IUploadService {
	@Resource
	private IAliyunOssService aliyunOssService;
	
	private boolean enable;
	@Override
	public Map<String, Object> uploadFile(String name, long size,
			InputStream inputStream) {
		Map<String, Object> ret=new HashMap<String,Object>();
		
		if(!this.enable){
			ret.put("message","阿里云存储插件已禁用，不可上传");
			ret.put("code","-1");
			return ret;
		}
		try {
			String path=this.aliyunOssService.put(name, size, inputStream);
			ret.put("message","文件上传成功.文件大小: " + size/1024 + "KB");
			ret.put("code","1");
			ret.put("name", name);
			ret.put("path", path);
		} catch (Exception e) {
			e.printStackTrace();
			ret.put("message",e.getMessage());
			ret.put("code","-1");
		}
		return ret;
	}
	public void setAliyunOssService(IAliyunOssService aliyunOssService) {
		this.aliyunOssService = aliyunOssService;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}

}
