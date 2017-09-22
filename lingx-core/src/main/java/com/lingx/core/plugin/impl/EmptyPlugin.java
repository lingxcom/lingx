package com.lingx.core.plugin.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.lingx.core.plugin.IPlugin;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年7月21日 下午7:54:15 
 * 类说明 
 */
@Component
public class EmptyPlugin implements IPlugin {

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return "Empty";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "空置插件";
	}

	@Override
	public String getDetail() {
		// TODO Auto-generated method stub
		return "测试/DEMO";
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return "lingx";
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return "1.0";
	}

	@Override
	public boolean isEnable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setEnable(boolean enable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(Map<String, Object> params) {
		// TODO Auto-generated method stub
		
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
