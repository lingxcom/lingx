package com.lingx.plugin.aliyun.oss;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.lingx.core.SpringContext;
import com.lingx.core.plugin.IPlugin;
import com.lingx.core.service.ILingxService;
import com.lingx.support.web.action.UploadAction;
@Component
public class AliyunOssPlugin implements IPlugin {
	public static Logger logger = LogManager.getLogger(AliyunOssPlugin.class);
	@Resource
	private AliyunOssServiceImpl aliyunOssService;
	@Resource
	private AliyunOssUploadService aliyunOssUploadService;
	@Resource
	private UploadAction uploadAction;
	private boolean enable;
	@Override
	public String getId() {
		return "aliyun-oss-plugin";
	}

	@Override
	public String getName() {
		return "阿里云存储上传插件";
	}

	@Override
	public String getDetail() {
		return "阿里云存储上传插件，依赖aliyun-sdk-oss-2.0.3.jar，需要事先申请阿里云账号，申请OSS权限；需要参数:accessId,accessKey,bucketName,ossEndPoint";
	}

	@Override
	public String getAuthor() {
		return "www.lingx.com";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public boolean isEnable() {
		return this.enable;
	}

	@Override
	public void setEnable(boolean enable) {
		this.enable=enable;
		this.aliyunOssUploadService.setEnable(enable);
	}

	@Override
	public void init(Map<String, Object> params) {
		try {
			logger.info("lingx plugin ali-yun-oss init.");
			String accessId=params.get("accessId").toString();
			String accessKey=params.get("accessKey").toString();
			String bucketName=params.get("bucketName").toString();
			String ossEndPoint=params.get("ossEndPoint").toString();
			this.aliyunOssService.setAccessId(accessId);
			this.aliyunOssService.setAccessKey(accessKey);
			this.aliyunOssService.setBucketName(bucketName);
			this.aliyunOssService.setOssEndPoint(ossEndPoint);
			this.aliyunOssService.init();
			
			this.uploadAction.setType(2);//设置为从IUploadService
		} catch (Exception e) {
			logger.error("plugin init error:"+e.getMessage());
			//e.printStackTrace();
		}
	}

	@Override
	public void start() {

	}

	@Override
	public void stop() {

	}

	@Override
	public void destory() {

	}

	@Override
	public Map<String, Object> execute(Map<String, Object> params) {
		return null;
	}

}
