package com.lingx.core.service.impl;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.lingx.core.engine.IContext;
import com.lingx.core.service.IDefaultValueService;
import com.lingx.core.service.IFormService;
import com.lingx.core.utils.Utils;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年10月13日 下午6:05:48 
 * 类说明 
 */
@Component(value="lingxFormService")
public class FormServiceImpl implements IFormService {
	@Resource
	private IDefaultValueService defaultValueService;
	@Resource(name="jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Override
	public String formWorkflowRendering(String content, String instanceId,IContext context) {
		if(Utils.isNotNull(instanceId)){
			List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select name,value from tlingx_wf_instance_value where instance_id=?",instanceId);
			for(Map<String,Object> map:list){
				content=content.replaceAll("\\$\\{"+map.get("name").toString()+"\\}", map.get("value").toString());
			}
		}
		content=this.defaultValueService.transforms(content, context);
		
		content=removeTags(content);
		
		return content;
	}
	public void setDefaultValueService(IDefaultValueService defaultValueService) {
		this.defaultValueService = defaultValueService;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public static final String removeTags(String temp){
		String regEx_script="[$][{][^}]*[}]";
		Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE); 
        Matcher m_script=p_script.matcher(temp); 
         temp=m_script.replaceAll("");
		return temp;
	}

	public static void main(String args[]){
		String content="asfasdf111${abc}111fdafsaf";
		//content=content.replaceAll("\\$\\{abc\\}", "有志者事竟成");
		System.out.println(removeTags(content));
	}
	
}
