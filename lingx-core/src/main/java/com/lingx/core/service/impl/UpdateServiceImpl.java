package com.lingx.core.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.core.model.IEntity;
import com.lingx.core.model.bean.ItemBean;
import com.lingx.core.model.bean.OptionBean;
import com.lingx.core.service.IConfigService;
import com.lingx.core.service.IModelService;
import com.lingx.core.service.IUpdateService;
import com.lingx.core.service.IUserService;
import com.lingx.core.utils.Utils;
import com.lingx.core.utils.ZipUtils;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年9月26日 上午12:20:08 
 * 类说明 
 */
@Component(value="lingxUpdateService")
public class UpdateServiceImpl implements IUpdateService {
	public static final Logger log=LogManager.getLogger(UpdateServiceImpl.class);
	private String basePath;
	private String updateURL="";
	private String unZipPath="/temp/update/";
	private String configFile="/config.json";
	public static final int BUFFER=1024*4;
	
	@Resource(name="jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Resource
	private IModelService modelService;
	@Resource
	private IUserService userService;
	@Resource
	private IConfigService configService;
	//@PostConstruct
	public void init(){
		log.info("IUpdateService IMPL init ...");
	}
	
	public static void main(String args[]){
		String json="[{\"appid\":\"335ec1fc-1011-11e5-b7ab-74d02b6b5f61\",\"code\":\"ZJLX\",\"items\":[{\"enabled\":\"1\",\"name\":\"自增\",\"orderindex\":\"1\",\"value\":\"1\"},{\"enabled\":\"1\",\"name\":\"UUID\",\"orderindex\":\"2\",\"value\":\"2\"}],\"name\":\"主键类型\"},{\"appid\":\"335ec1fc-1011-11e5-b7ab-74d02b6b5f61\",\"code\":\"SJKLX\",\"items\":[{\"enabled\":\"1\",\"name\":\"varchar\",\"orderindex\":\"1\",\"value\":\"varchar\"},{\"enabled\":\"1\",\"name\":\"int\",\"orderindex\":\"2\",\"value\":\"int\"},{\"enabled\":\"1\",\"name\":\"notdbfield\",\"orderindex\":\"3\",\"value\":\"notdbfield\"},{\"enabled\":\"1\",\"name\":\"float\",\"orderindex\":\"2\",\"value\":\"float\"}],\"name\":\"数据库类型\"},{\"appid\":\"335ec1fc-1011-11e5-b7ab-74d02b6b5f61\",\"code\":\"FHLX\",\"items\":[{\"enabled\":\"1\",\"name\":\"JSON\",\"orderindex\":\"2\",\"value\":\"JSON\"},{\"enabled\":\"1\",\"name\":\"JSP\",\"orderindex\":\"1\",\"value\":\"JSP\"},{\"enabled\":\"1\",\"name\":\"JAVASCRIPT\",\"orderindex\":\"4\",\"value\":\"JAVASCRIPT\"},{\"enabled\":\"1\",\"name\":\"URL\",\"orderindex\":\"3\",\"value\":\"URL\"}],\"name\":\"返回类型\"},{\"appid\":\"335ec1fc-1011-11e5-b7ab-74d02b6b5f61\",\"code\":\"YYLX\",\"items\":[{\"enabled\":\"1\",\"name\":\"空值\",\"orderindex\":\"3\",\"value\":\"\"},{\"enabled\":\"1\",\"name\":\"引用值\",\"orderindex\":\"1\",\"value\":\"ref-value\"},{\"enabled\":\"1\",\"name\":\"显示值\",\"orderindex\":\"2\",\"value\":\"ref-display\"}],\"name\":\"引用类型\"},{\"appid\":\"335ec1fc-1011-11e5-b7ab-74d02b6b5f61\",\"code\":\"CDXXLX\",\"items\":[{\"enabled\":\"1\",\"name\":\"一级菜单\",\"orderindex\":\"4\",\"value\":\"0\"},{\"enabled\":\"1\",\"name\":\"普通选项\",\"orderindex\":\"1\",\"value\":\"1\"},{\"enabled\":\"1\",\"name\":\"分隔线\",\"orderindex\":\"2\",\"value\":\"2\"},{\"enabled\":\"1\",\"name\":\"分组选项\",\"orderindex\":\"3\",\"value\":\"3\"}],\"name\":\"菜单选项类型\"},{\"appid\":\"335ec1fc-1011-11e5-b7ab-74d02b6b5f61\",\"code\":\"BJKKJ\",\"items\":[{\"enabled\":\"1\",\"name\":\"单选框\",\"orderindex\":\"13\",\"value\":\"radio\"},{\"enabled\":\"1\",\"name\":\"多选框\",\"orderindex\":\"14\",\"value\":\"checkbox\"},{\"enabled\":\"1\",\"name\":\"隐藏控件\",\"orderindex\":\"9\",\"value\":\"hidden\"},{\"enabled\":\"1\",\"name\":\"对话框列表选择[多选]\",\"orderindex\":\"2\",\"value\":\"dialogoption2\"},{\"enabled\":\"1\",\"name\":\"对话框树形选择[单选]\",\"orderindex\":\"15\",\"value\":\"dialogtree\"},{\"enabled\":\"1\",\"name\":\"对话框树形选择[多选]\",\"orderindex\":\"16\",\"value\":\"dialogtree2\"},{\"enabled\":\"0\",\"name\":\"系统选择项\",\"orderindex\":\"3\",\"value\":\"sysoption\"},{\"enabled\":\"1\",\"name\":\"文本输入框\",\"orderindex\":\"1\",\"value\":\"textfield\"},{\"enabled\":\"1\",\"name\":\"对话框列表选择[单选]\",\"orderindex\":\"2\",\"value\":\"dialogoption\"},{\"enabled\":\"1\",\"name\":\"密码输入框\",\"orderindex\":\"7\",\"value\":\"password\"},{\"enabled\":\"1\",\"name\":\"数字输入框\",\"orderindex\":\"4\",\"value\":\"numberfield\"},{\"enabled\":\"1\",\"name\":\"日期输入框\",\"orderindex\":\"5\",\"value\":\"datefield\"},{\"enabled\":\"1\",\"name\":\"日期时间输入框\",\"orderindex\":\"6\",\"value\":\"datetimefield\"},{\"enabled\":\"1\",\"name\":\"文本域输入框\",\"orderindex\":\"10\",\"value\":\"textarea\"},{\"enabled\":\"1\",\"name\":\"只读输入框\",\"orderindex\":\"11\",\"value\":\"displayfield\"},{\"enabled\":\"1\",\"name\":\"选择控件\",\"orderindex\":\"8\",\"value\":\"combobox\"},{\"enabled\":\"1\",\"name\":\"文件\",\"orderindex\":\"12\",\"value\":\"file\"}],\"name\":\"编辑框控件\"},{\"appid\":\"335ec1fc-1011-11e5-b7ab-74d02b6b5f61\",\"code\":\"GNLX\",\"items\":[{\"enabled\":\"1\",\"name\":\"公开\",\"orderindex\":\"1\",\"value\":\"1\"},{\"enabled\":\"1\",\"name\":\"私有\",\"orderindex\":\"2\",\"value\":\"2\"},{\"enabled\":\"1\",\"name\":\"禁用\",\"orderindex\":\"3\",\"value\":\"3\"}],\"name\":\"功能类型\"},{\"appid\":\"335ec1fc-1011-11e5-b7ab-74d02b6b5f61\",\"code\":\"DXLX\",\"items\":[{\"enabled\":\"1\",\"name\":\"中介对象\",\"orderindex\":\"3\",\"value\":\"3\"},{\"enabled\":\"1\",\"name\":\"实体对象\",\"orderindex\":\"1\",\"value\":\"1\"},{\"enabled\":\"1\",\"name\":\"查询对象\",\"orderindex\":\"2\",\"value\":\"2\"}],\"name\":\"对象类型\"},{\"appid\":\"335ec1fc-1011-11e5-b7ab-74d02b6b5f61\",\"code\":\"DXZT\",\"items\":[{\"enabled\":\"1\",\"name\":\"开发\",\"orderindex\":\"1\",\"value\":\"1\"},{\"enabled\":\"1\",\"name\":\"完成\",\"orderindex\":\"2\",\"value\":\"2\"}],\"name\":\"对象状态\"},{\"appid\":\"335ec1fc-1011-11e5-b7ab-74d02b6b5f61\",\"code\":\"YHZT\",\"items\":[{\"enabled\":\"1\",\"name\":\"禁用\",\"orderindex\":\"2\",\"value\":\"2\"},{\"enabled\":\"1\",\"name\":\"正常\",\"orderindex\":\"1\",\"value\":\"1\"}],\"name\":\"用户状态\"},{\"appid\":\"335ec1fc-1011-11e5-b7ab-74d02b6b5f61\",\"code\":\"SF\",\"items\":[{\"enabled\":\"1\",\"name\":\"是\",\"orderindex\":\"1\",\"value\":\"1\"},{\"enabled\":\"1\",\"name\":\"否\",\"orderindex\":\"2\",\"value\":\"2\"}],\"name\":\"是否\"},{\"appid\":\"335ec1fc-1011-11e5-b7ab-74d02b6b5f61\",\"code\":\"CDZT\",\"items\":[{\"enabled\":\"1\",\"name\":\"显示\",\"orderindex\":\"1\",\"value\":\"1\"},{\"enabled\":\"1\",\"name\":\"隐藏\",\"orderindex\":\"2\",\"value\":\"2\"}],\"name\":\"菜单状态\"},{\"appid\":\"335ec1fc-1011-11e5-b7ab-74d02b6b5f61\",\"code\":\"LJYSF\",\"items\":[{\"enabled\":\"1\",\"name\":\"匹配\",\"orderindex\":\"9\",\"value\":\"regexp\"},{\"enabled\":\"1\",\"name\":\"等于\",\"orderindex\":\"1\",\"value\":\"=\"},{\"enabled\":\"1\",\"name\":\"大于\",\"orderindex\":\"2\",\"value\":\">\"},{\"enabled\":\"1\",\"name\":\"小于\",\"orderindex\":\"3\",\"value\":\"<\"},{\"enabled\":\"1\",\"name\":\"大于等于\",\"orderindex\":\"4\",\"value\":\">=\"},{\"enabled\":\"1\",\"name\":\"小于等于\",\"orderindex\":\"5\",\"value\":\"<=\"},{\"enabled\":\"1\",\"name\":\"相似\",\"orderindex\":\"6\",\"value\":\"like\"},{\"enabled\":\"1\",\"name\":\"in\",\"orderindex\":\"7\",\"value\":\"in\"},{\"enabled\":\"1\",\"name\":\"not in\",\"orderindex\":\"8\",\"value\":\"not in\"}],\"name\":\"逻辑运算符\"},{\"appid\":\"335ec1fc-1011-11e5-b7ab-74d02b6b5f61\",\"code\":\"XSMS\",\"items\":[{\"enabled\":\"1\",\"name\":\"grid\",\"orderindex\":\"1\",\"value\":\"grid\"},{\"enabled\":\"1\",\"name\":\"tree\",\"orderindex\":\"2\",\"value\":\"tree\"}],\"name\":\"显示模式\"},{\"appid\":\"335ec1fc-1011-11e5-b7ab-74d02b6b5f61\",\"code\":\"SXJDKG\",\"items\":[{\"enabled\":\"1\",\"name\":\"展开\",\"orderindex\":\"1\",\"value\":\"open\"},{\"enabled\":\"1\",\"name\":\"关闭\",\"orderindex\":\"2\",\"value\":\"close\"}],\"name\":\"树形节点开关\"},{\"appid\":\"335ec1fc-1011-11e5-b7ab-74d02b6b5f61\",\"code\":\"QXLX\",\"items\":[{\"enabled\":\"1\",\"name\":\"功能\",\"orderindex\":\"1\",\"value\":\"1\"},{\"enabled\":\"1\",\"name\":\"组织\",\"orderindex\":\"2\",\"value\":\"2\"},{\"enabled\":\"1\",\"name\":\"角色\",\"orderindex\":\"3\",\"value\":\"3\"},{\"enabled\":\"1\",\"name\":\"菜单\",\"orderindex\":\"4\",\"value\":\"4\"}],\"name\":\"权限类型\"},{\"appid\":\"335ec1fc-1011-11e5-b7ab-74d02b6b5f61\",\"code\":\"CJKYX\",\"items\":[{\"enabled\":\"1\",\"name\":\"<span style=\\\"color:red\\\">禁用</span>\",\"orderindex\":\"2\",\"value\":\"false\"},{\"enabled\":\"1\",\"name\":\"<span style=\\\"color:green\\\">启用</span>\",\"orderindex\":\"1\",\"value\":\"true\"}],\"name\":\"插件可用性\"},{\"appid\":\"335ec1fc-1011-11e5-b7ab-74d02b6b5f61\",\"code\":\"GZLYHFZLX\",\"items\":[{\"enabled\":\"1\",\"name\":\"角色\",\"orderindex\":\"2\",\"value\":\"2\"},{\"enabled\":\"1\",\"name\":\"组织\",\"orderindex\":\"1\",\"value\":\"1\"}],\"name\":\"工作流用户分组类型\"},{\"appid\":\"335ec1fc-1011-11e5-b7ab-74d02b6b5f61\",\"code\":\"CSKYX\",\"items\":[{\"enabled\":\"1\",\"name\":\"<span style=\\\"color:green\\\">启用</span>\",\"orderindex\":\"1\",\"value\":\"1\"},{\"enabled\":\"1\",\"name\":\"<span style=\\\"color:red\\\">禁用</span>\",\"orderindex\":\"2\",\"value\":\"2\"}],\"name\":\"参数可用性\"},{\"appid\":\"335ec1fc-1011-11e5-b7ab-74d02b6b5f61\",\"code\":\"SEX\",\"items\":[{\"enabled\":\"1\",\"name\":\"男\",\"orderindex\":\"1\",\"value\":\"1\"},{\"enabled\":\"1\",\"name\":\"女\",\"orderindex\":\"2\",\"value\":\"2\"}],\"name\":\"性别\"}]";
		List<OptionBean> list=JSON.parseArray(json, OptionBean.class);
		System.out.println(list.size());
	}
	
	public boolean update(String id,String basePath){
		this.basePath=basePath;
		try {
			File tempFile=new File(this.basePath+this.unZipPath);
			if(!tempFile.exists())tempFile.mkdirs();
			FileUtils.cleanDirectory(tempFile);
			File zip=new File(this.basePath+this.unZipPath+"/update.zip");
			
			this.download(new URL(this.updateURL+"?id="+id),zip );
			this.unzip(zip);
			Map<String, Object> config=this.getConfig();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	public boolean update(URL url,String basePath,String ts){

		try {
			File file=new File(basePath+this.unZipPath+"/update_url.zip");
			FileUtils.copyURLToFile(url, file);
			
			return this.update(file, basePath,ts);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public boolean update(File file,String basePath,String ts){
		this.basePath=basePath;
		try {
			this.unzip(file);
			Map<String, Object> config=this.getConfig();
			this.runSQL(config.get("sql").toString());
			if(config.get("optionJSON")!=null)
			this.updateOptions(config.get("optionJSON").toString());
			if(config.get("entityJSON")!=null)
			this.updateEntity(config.get("entityJSON").toString());

			if(config.get("model")!=null)
			this.updateModel((List<String>)config.get("model"));
			if(config.get("files")!=null)
			this.copyFiles((List<String>)config.get("files"));


			if(config.get("menu")!=null)
			this.updateMenu((List<Map<String,Object>>)config.get("menu"));

			if(config.get("func")!=null)
			this.updateFunc((List<Map<String,Object>>)config.get("func"));
			FileUtils.cleanDirectory(new File(this.basePath+this.unZipPath));
			this.configService.saveValue("lingx.update.version.ts", ts);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	private void download(URL url,File file) throws Exception{
		FileUtils.copyURLToFile(url, file);
	}

	
	private void unzip(File file) throws Exception {
		File dir=new File(this.basePath+this.unZipPath);
		if(!dir.exists())dir.mkdirs();
		log.info("unzip:"+file.getAbsolutePath());
		ZipUtils.decompressZip(file.getAbsolutePath(), this.basePath+this.unZipPath);
	}

	
	private Map<String, Object> getConfig() throws Exception  {
		String temp=FileUtils.readFileToString(new File(this.basePath+this.unZipPath+this.configFile), "UTF-8");
		//System.out.println(temp);
		return (Map<String, Object>)JSON.parse(temp);
	}

	
	private void copyFiles(List<String> list)throws Exception  {
		for(String path:list){
			if(Utils.isNull(path))continue;
			path=path.replace("\\", "/");
			log.info(path);
			FileUtils.copyFile(new File(this.basePath+this.unZipPath+path), new File(this.basePath+path));
		}
	}

	
	private void runSQL(String sql) {
		if(Utils.isNull(sql))return;
		sql=sql.replaceAll("\r", "").replaceAll("\n", "");
		if(sql.indexOf(";")==-1){
			this.jdbcTemplate.update(sql);
		}else{
			String arr[]=sql.split(";");
			for(String s:arr){
				log.info(s);
				if(Utils.isNotNull(s)){
					try {
						this.jdbcTemplate.update(s);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	

	
	private void updateOptions(String json) {
		if(Utils.isNull(json))return;
		
		List<OptionBean> list=JSON.parseArray(json, OptionBean.class);
		for(OptionBean bean:list){
			log.info("update option:"+bean.getCode());
			try {
				if(this.jdbcTemplate.queryForInt("select count(*) from tlingx_option where code=?",bean.getCode())==0)
				this.jdbcTemplate.update("insert into tlingx_option(id,name,code,app_id) values(uuid(),?,?,?)",bean.getName(),bean.getCode(),bean.getAppid());
				
				String id=this.jdbcTemplate.queryForObject("select id from tlingx_option where code=?",String.class,bean.getCode());
				for(ItemBean bean2:bean.getItems()){
					if(Utils.isNull(bean2.getValue()))continue;
					if(this.jdbcTemplate.queryForInt("select count(*) from tlingx_optionitem where option_id=? and value=? and name=?",id,bean2.getValue(),bean2.getName())==0)
					this.jdbcTemplate.update("insert into tlingx_optionitem(id,name,value,option_id,orderindex,enabled) values(uuid(),?,?,?,?,?)",bean2.getName(),bean2.getValue(),id,bean2.getOrderindex(),bean2.getEnabled());
					else 
						this.jdbcTemplate.update("update tlingx_optionitem set name=?,orderindex=?,enabled=? where option_id=? and value=?",bean2.getName(),bean2.getOrderindex(),bean2.getEnabled(),id,bean2.getValue());
					//删除多余的选项
					removeSurplusItem(id,bean2.getValue());
				}
			} catch (DataAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void removeSurplusItem(String optionId,String value){
		while(this.jdbcTemplate.queryForInt("select count(*) from tlingx_optionitem where option_id=? and value=?",optionId,value)>1){
			String id=this.jdbcTemplate.queryForObject("select id from tlingx_optionitem where option_id=? and value=? limit 1", String.class,optionId,value);
			this.jdbcTemplate.update("delete from tlingx_optionitem where id=?",id);
		}
			
	}
	
	private void updateMenu(List<Map<String,Object>> list){
		String sql="insert into tlingx_menu(id,name,short_name,type,iconcls,fid,orderindex,status,state,func_id,remark) values(?,?,?,?,?,?,?,?,?,?,?)";
		for(Map<String,Object> map:list){
			log.info("update menu:"+map.get("name"));
			if(this.jdbcTemplate.queryForInt("select count(*) from tlingx_menu where id=?",map.get("id"))==0){
				this.jdbcTemplate.update(sql,map.get("id"),map.get("name"),map.get("short_name"),map.get("type"),map.get("iconcls"),map.get("fid"),
						map.get("orderindex"),map.get("status"),map.get("state"),map.get("func_id"),map.get("remark"));
			}
		}
		this.userService.superManagerAuthRefresh();
	}
	
	private void updateFunc(List<Map<String,Object>> list){
		String sql="insert into tlingx_func(id,name,module,func,type,fid)values(?,?,?,?,?,?)";
		for(Map<String,Object> map:list){
			log.info("update func:"+map.get("name"));
			if(this.jdbcTemplate.queryForInt("select count(*) from tlingx_func where id=?",map.get("id"))==0){
				this.jdbcTemplate.update(sql,map.get("id"),map.get("name"),map.get("module"),map.get("func"),map.get("type"),map.get("fid"));
			}
		}
		this.userService.superManagerAuthRefresh();
	}
	
	private void updateEntity(String json) {
		if(Utils.isNull(json))return;
		String sql="insert into tlingx_entity(id,name,code,type,status,app_id,create_time) values(uuid(),?,?,?,?,?,?)";
		List<Map<String,Object>> list=(List<Map<String,Object>>)JSON.parse(json);
		for(Map<String,Object> map:list){
			log.info("update entity:"+map.get("code"));
			if(this.jdbcTemplate.queryForInt("select count(*) from tlingx_entity where code=?",map.get("code"))==0){
				this.jdbcTemplate.update(sql,map.get("name"),map.get("code"),map.get("type"),map.get("status"),map.get("app_id"),Utils.getTime());
			}
		}
	}
	
	private void updateModel(List<String> list){
		for(String temp:list){
			if(Utils.isNull(temp))continue;
			log.info("update model:"+temp);
			IEntity e=readFile(this.basePath+this.unZipPath+temp+".lingx");
			this.modelService.save(e);
		}
	}

	public static IEntity readFile(String path) {
		IEntity entity=null;
		try {
			FileInputStream fos = new FileInputStream(path);
			ObjectInputStream oos = new ObjectInputStream(fos);
			entity=(IEntity)oos.readObject();
			oos.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;
	}
}
