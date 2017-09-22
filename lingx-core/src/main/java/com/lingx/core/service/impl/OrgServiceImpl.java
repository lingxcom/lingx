package com.lingx.core.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.lingx.core.service.IOrgService;

/** 
 * @author www.lingx.com
 * @version 创建时间：2017年3月24日 上午9:38:08 
 * 类说明 
 */
@Component(value="lingxOrgService")
public class OrgServiceImpl implements IOrgService {
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Override
	public String[] getAllOrgIdsByAppid(String appid) {
		String orgid=this.jdbcTemplate.queryForObject("select org_root_id from tlingx_app where id=?", String.class,appid);
		StringBuilder sb=new StringBuilder();
		this.dg(orgid, sb);
		if(sb.length()>0)sb.deleteCharAt(sb.length()-1);
		return sb.toString().split(",");
	}
	/**
	 * 递归组织树
	 * @param orgid
	 * @param sb
	 */
	private void dg(String orgid,StringBuilder sb){
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select id from tlingx_org where fid=?",orgid);
		if(sb.indexOf(orgid)==-1){
			sb.append(orgid).append(",");
			for(Map<String,Object> map:list){
				dg(map.get("id").toString(),sb);
			}
		}
		
	}

}
