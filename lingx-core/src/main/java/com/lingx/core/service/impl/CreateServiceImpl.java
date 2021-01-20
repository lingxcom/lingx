package com.lingx.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.core.model.IEntity;
import com.lingx.core.model.bean.UserBean;
import com.lingx.core.service.ICreateService;
import com.lingx.core.service.IDatabaseService;
import com.lingx.core.service.ILingxService;
import com.lingx.core.service.IModelService;
import com.lingx.core.utils.Utils;

/** 
 * @author www.lingx.com
 * @version 创建时间：2016年2月24日 上午9:29:01 
 * 类说明 
 */
@Component(value="lingxCreateService")
public class CreateServiceImpl implements ICreateService {
	@Resource(name="jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Resource
	private IModelService modelService;
	@Resource
	private IDatabaseService databaseService;
	@Resource
	private ILingxService lingxService;
	@Override
	public boolean create(String name, String options,String appid,UserBean userBean) {
		int appSN=this.jdbcTemplate.queryForInt("select sn from tlingx_app where id=?",appid);
		String tableName="ta_"+Utils.getRandomNumber(4);
		List<Map<String,String>> jsons=(List<Map<String,String>>)JSON.parse(options);
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		String pinyin,type="",key,val;
		int index=1;
		for(Map<String,String> map:jsons){
			key=map.get("name");
			val=map.get("type");
			pinyin="f"+index++;
			
			if("数字".equals(val)){
				type="int(11)";
			}else if("金额".equals(val)){
				type="decimal(10,2)";
			}else{
				type="varchar(500)";
			}
			switch(key){
			case "树型上级":
				pinyin="fid";
				type="int(11)";
				break;
			case "树型状态":
				pinyin="state";
				type="varchar(32)";
				break;
			case "树型图标":
				pinyin="icon_cls";
				type="varchar(32)";
				break;
			case "创建时间":
				pinyin="create_time";
				type="char(14)";
				break;
			case "修改时间":
				pinyin="modify_time";
				type="char(14)";
				break;
			case "隶属组织":
				pinyin="org_id";
				type="char(36)";
				break;
			case "隶属用户":
				pinyin="user_id";
				type="char(36)";
				break;
			case "顺序":
				pinyin="orderindex";
				type="int(11)";
				break;
			}
			Map<String,Object> m=new HashMap<String,Object>();
			m.put("name", pinyin);
			m.put("type", type);
			m.put("comment", key);
			list.add(m);
		}
		String sql=this.getCreateTableSql(tableName, list);
		this.jdbcTemplate.update(sql);
		///
		IEntity entity=modelService.createScriptEntity(tableName,name, userBean.getAccount() ,1,databaseService.getDatabaseName(), this.jdbcTemplate);
		modelService.save(entity);
		if(jdbcTemplate.queryForInt("select count(*) from tlingx_entity where code=?",tableName)==0){
			jdbcTemplate.update("insert into tlingx_entity(id,name,code,type,status,app_id,create_time)values(uuid(),?,?,1,1,?,?)",name,tableName,appid,Utils.getTime());
		}
		return true;
	}
	/**
	 * 
	 * @param name 表名
	 * @param list 属性  map->name,type,comment
	 * @return
	 */
	public String getCreateTableSql(String name,List<Map<String,Object>> list){
		StringBuilder sb =new StringBuilder();
		sb.append("CREATE TABLE `").append(name).append("` (");
		sb.append("`id` int(11) NOT NULL AUTO_INCREMENT,");
		for(Map<String,Object> map:list){
			sb.append("`").append(map.get("name")).append("` ").append(map.get("type")).append(" DEFAULT NULL COMMENT '").append(map.get("comment")).append("',");
		}
		sb.append("PRIMARY KEY (`id`)");
		sb.append(") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;");
		return sb.toString();
	}
	
}
