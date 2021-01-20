package com.lingx.core.service.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.lingx.core.Constants;
import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.engine.impl.DefaultPerformer;
import com.lingx.core.model.IEntity;
import com.lingx.core.model.IField;
import com.lingx.core.service.IContextService;
import com.lingx.core.service.IInterpretService;
import com.lingx.core.service.IModelService;
import com.lingx.core.service.IReportService;
import com.lingx.core.service.IScriptApisService;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年9月17日 上午10:30:48 
 * 类说明 
 */
@Component(value="lingxReportService")
public class ReportServiceImpl implements IReportService {
	@Resource(name="jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Resource
	private IModelService modelService;
	@Resource
	private IInterpretService interpretService;
	@Resource
	private IContextService contextService;

	@Resource
	private IScriptApisService scriptApisService;
	@Override
	public XSSFWorkbook createExcelBySQL(String sql, String entityCode,HttpServletRequest request) {
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList(sql);
		IEntity entity=this.modelService.getCacheEntity(entityCode);
		IContext context=contextService.getContext(request);
		IPerformer performer =new DefaultPerformer(scriptApisService.getScriptApis(),context.getRequest());
		
		this.interpretService.outputFormat(list, entity.getFields().getList(), entity, context, performer);
		
		XSSFWorkbook xlsx=new XSSFWorkbook();
		String title=entity.getName();
		List<String> keys=new ArrayList<String>();
		Map<String,String> map=new HashMap<String,String>();
		keys.add("id");
		map.put("id", "id");
		for(IField field:entity.getFields().getList()){
			if(field.getVisible()){
				keys.add(field.getCode());
				map.put(field.getCode(), field.getName());
			}
		}
		this.exportExcel(xlsx, title, keys, map, list, null);
		return xlsx ;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	public void setModelService(IModelService modelService) {
		this.modelService = modelService;
	}
	
	/**
	 * 根据参数生成excel报表
	 * @param workbook
	 * @param title
	 * @param keys
	 * @param map
	 * @param list
	 * @param styleMap
	 * @return
	 */
	private  XSSFWorkbook exportExcel(XSSFWorkbook workbook,String title,List<String> keys,Map<String,String> map,List<Map<String,Object>> list,Map<String,Object> styleMap){

        // 生成一个表格
        XSSFSheet sheet = workbook.createSheet(title);
        
        // 生成一个样式
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFCellStyle otherStyle=null;
        List<Map<String,Integer>> cells = new ArrayList<Map<String,Integer>>();
        // 设置这些样式
       style.setFillForegroundColor(new XSSFColor(Color.WHITE)); 
        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        XSSFFont font = workbook.createFont();
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 生成并设置另一个样式
        XSSFCellStyle style2 = workbook.createCellStyle();

        style2.setFillBackgroundColor(new XSSFColor(Color.decode("#FFFFFF")).getIndexed());
        style2.setFillForegroundColor(new XSSFColor(Color.decode("#000000")).getIndexed());
        style2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style2.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        XSSFFont font2 = workbook.createFont();
        font2.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);

        // 声明一个画图的顶级管理器
        //XSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        
        XSSFRow row = sheet.createRow(0);
        int index=0;
        for (String temp:keys) {
            XSSFCell cell = row.createCell(index++);
            cell.setCellStyle(style);
            XSSFRichTextString text = new XSSFRichTextString(map.get(temp));
            cell.setCellValue(text);
        }
        if(styleMap!=null){  
           otherStyle = workbook.createCellStyle();
        // 生成另一个字体
           XSSFFont otherFont = workbook.createFont();       
           otherFont.setColor((XSSFColor)styleMap.get("color")); //设置字体颜色
           otherStyle.setFont(otherFont);
           cells=(List<Map<String,Integer>>) styleMap.get("cells");
       }
        index=0;
        for(Map<String,Object> m:list){
        	if(m==null)continue;
        	row = sheet.createRow(++index);
        	int cellIndex=0;
        	for(String temp:keys){
        		 XSSFCell cell = row.createCell(cellIndex++);
        		 if(styleMap!=null){        			
             		for(Map<String,Integer> tmp:cells){
                        if(tmp.get("row")==index&&tmp.get("col")==cellIndex){  
                        	//System.out.println(index+":"+cellIndex);
                        	cell.setCellStyle(otherStyle);	
                        }
             		}
             		 
        		 }
        		 // cell.setCellStyle(style2);
        		  XSSFRichTextString richString;
        		  if(m.get(temp)!=null){
        			  Object o=m.get(temp);
        			  if(o instanceof List){
        				  List<Map<String,Object>> l=(List<Map<String,Object>>)o;
        				  richString = new XSSFRichTextString( l.get(0).get("text").toString().replaceAll("<[^>]+>", ""));
        			  }else{
        				  richString = new XSSFRichTextString(o.toString());
        			  }
        			   
        		  }else{
        			   richString = new XSSFRichTextString("");
        		  }
        		 
        		  cell.setCellValue(richString);
        	}
        }
        return workbook;
        
	}
	
	@Override
	public void putSqlToSession(String sql, String entityCode,
			Map<String, Object> session) {
		Map<String,String> cache=null;
		if(session.get(Constants.SESSION_LAST_QUERY_SQL)==null){
			cache=new HashMap<String,String>();
		}else{
			cache=(Map<String,String>)session.get(Constants.SESSION_LAST_QUERY_SQL);
		}
		cache.put(entityCode, sql);
		session.put(Constants.SESSION_LAST_QUERY_SQL,cache);
	}
	@Override
	public String getSqlBySession(String entityCode, Map<String, Object> session) {
		if(session.get(Constants.SESSION_LAST_QUERY_SQL)==null){
			return "";
		}else{
			Map<String,String>	cache=(Map<String,String>)session.get(Constants.SESSION_LAST_QUERY_SQL);
			return cache.get(entityCode);
		}
	}
	@Override
	public String getSqlBySession(String entityCode, HttpSession session) {
		if(session.getAttribute(Constants.SESSION_LAST_QUERY_SQL)==null){
			return "";
		}else{
			Map<String,String>	cache=(Map<String,String>)session.getAttribute(Constants.SESSION_LAST_QUERY_SQL);
			return cache.get(entityCode);
		}
	}

}
