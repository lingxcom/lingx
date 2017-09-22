package com.lingx.core.test.spring;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.lingx.core.service.IScriptApisService;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月10日 下午1:59:04 
 * 类说明 
 */
public class TestLingxService extends BaseTest {
	private IScriptApisService apisService;
	@Test
	public void test() {
		for(String key:this.apisService.getScriptApis().keySet()){
			System.out.println(key);
		}
	}

	@Autowired
	public void setApisService(IScriptApisService apisService) {
		this.apisService = apisService;
	}
	

}
