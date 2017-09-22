package com.lingx.plugin.upload.ext;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.lingx.core.plugin.IPlugin;
import com.lingx.support.web.action.UploadAction;
@Component
public class UploadExtPlugin implements IPlugin {
	public static Logger logger = LogManager.getLogger(UploadExtPlugin.class);
	@Resource
	private IUploadExtService uploadExtService;
	@Resource
	private UploadAction uploadAction;
	private boolean enable=true;
	@Override
	public String getId() {
		return "upload-ext-plugin";
	}

	@Override
	public String getName() {
		return "文档外置上传插件";
	}

	@Override
	public String getDetail() {
		return "由于多容器署时，会导致文档上传混乱，且文档服务需要大容量硬盘；所以独立部署文档系统，通过使用本插件系统上传的文件，均转发到文档系统。\r\n需要参数：targetURL，流方式上传路径";
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
		return enable;
	}

	@Override
	public void setEnable(boolean enable) {
		this.enable=enable;
	}

	@Override
	public void init(Map<String, Object> params) {
		try {
			if(params.get("targetURL")==null){return;}
			String targetURL=params.get("targetURL").toString();
			this.uploadExtService.setTargetURL(targetURL);
			this.uploadAction.setType(2);//设置为从IUploadService
		} catch (Exception e) {
			logger.error("plugin init error:"+e.getMessage());
			//e.printStackTrace();
		}		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> execute(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

}
