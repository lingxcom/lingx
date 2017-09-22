package com.lingx.core.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年9月17日 上午10:27:30 
 * 类说明 
 */
public interface IReportService {
	/**
	 * 
	 * @param sql 查询的SQL
	 * @param entityCode  对应的实体类
	 * @return
	 */
	public XSSFWorkbook createExcelBySQL(String sql,String entityCode,HttpServletRequest request);
	/**
	 * 缓存最后一次查询的SQL
	 * @param sql
	 * @param entityCode
	 * @param session
	 */
	public void putSqlToSession(String sql,String entityCode,Map<String,Object> session);
	/**
	 * 获取最后一次查询的SQL
	 * @param entity
	 * @param session
	 */
	public String getSqlBySession(String entity,Map<String,Object> session);/**
	 * 获取最后一次查询的SQL
	 * @param entity
	 * @param session
	 */
	public String getSqlBySession(String entity,HttpSession session);
}
