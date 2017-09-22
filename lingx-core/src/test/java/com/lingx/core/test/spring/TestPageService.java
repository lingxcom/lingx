package com.lingx.core.test.spring;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.lingx.core.service.IPageService;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月10日 下午1:59:04 
 * 类说明 
 */
public class TestPageService extends BaseTest {
	private IPageService pageService;
	@Test
	public void test() {
		System.out.println(this.pageService.getPage("no_permission"));
		System.out.println(this.pageService.getPage("timeout"));
	}
	@Autowired
	public void setPageService(IPageService pageService) {
		this.pageService = pageService;
	}

}
