package com.lingx.core.service.impl.schedule;

/** 
* @author www.lingx.com
* @version 创建时间：2020年6月12日 下午5:25:37 
* 类说明  测试用的
*/
public class TestTask extends LingxScheduler {

	

	@Override
	public String execute() {
		System.out.println("================================TestTask");
		return null;
	}

}
