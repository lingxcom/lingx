package com.lingx.core.event.impl;

import javax.annotation.Resource;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.lingx.core.event.TreeExecutorEvent;
import com.lingx.core.service.IReportService;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年9月17日 下午2:12:55 
 * 类说明 
 */
@Component
public  class TreeExecutorListener  implements ApplicationListener<TreeExecutorEvent>{
	@Resource
	private IReportService reportService;
	@Override
	public void onApplicationEvent(TreeExecutorEvent event) {
		this.reportService.putSqlToSession(event.getSql(), event.getContext().getEntity().getCode(), event.getContext().getSession());
	}
	public void setReportService(IReportService reportService) {
		this.reportService = reportService;
	}

}
