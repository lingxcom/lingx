package com.lingx.core.service.impl;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.lingx.core.service.IFuncService;
import com.lingx.core.service.ILingxService;
import com.lingx.core.utils.Utils;

/** 
 * @author www.lingx.com
 * @version 创建时间：2016年2月22日 上午11:57:15 
 * 功能授权辅助
 */
@Component("lingxFuncService")
public class FuncServiceImpl implements IFuncService {

	private Cache<String,Boolean> cacheFunc ;
	private int maximumSize=5000;
	private int expireTime=10;
	@Resource
	private ILingxService lingxService;
	@Resource(name="jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	public void init(){
		this.cacheFunc=CacheBuilder.newBuilder().maximumSize(maximumSize).expireAfterAccess(expireTime, TimeUnit.MINUTES).build();
	}
	
	@Override
	public boolean getAuth(String userId,String module,String func) {
		Boolean ret=null;
		
		if("true".equals(this.lingxService.getConfigValue("lingx.auth.func.cache", "false"))){//是否启用缓存，默认不启用
			StringBuilder sb=new StringBuilder();
			sb.append(userId).append(module).append(func);
			
			ret=this.cacheFunc.getIfPresent(sb.toString());
			if(ret==null){
				//System.out.println("不在缓存:"+sb);
				ret=this.get(userId, module, func);
				this.cacheFunc.put(sb.toString(), ret);
			}else{
				//System.out.println("在缓存:"+sb);
			}
		}else{
			ret=this.get(userId, module, func);
		}
		
		return ret;
	}

	private boolean get(String userId,String entityCode,String methodCode){
		String sql = "select type from tlingx_func where module=? and func=?";
		boolean isAuth=false;
		if(this.jdbcTemplate.queryForInt("select count(1) from tlingx_func where module=? and func=?", entityCode, methodCode)==0)return false;
		try {
			int c = this.jdbcTemplate.queryForInt(sql, entityCode, methodCode );
			switch (c) {
			case 1:// 公开
				isAuth = true;
				break;
			case 2:// 私有
				sql = "select count(*) from tlingx_user t,tlingx_userrole a,tlingx_rolefunc b,tlingx_func c where t.id=a.user_id and a.role_id=b.role_id and b.func_id=c.id and c.module=? and c.func=? and t.id=?";
				c = this.jdbcTemplate.queryForInt(sql, entityCode, methodCode,userId );
				sql = "select count(*) from tlingx_userfunc t,tlingx_func a where t.user_id=? and t.func_id=a.id and a.module=? and a.func=? ";
				c=c+this.jdbcTemplate.queryForInt(sql, userId, entityCode, methodCode );
				isAuth=c >= 1;
				break;
			case 3:// 禁用
				isAuth = false;
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isAuth;
	}
	@Override
	public void refresh() {
		this.cacheFunc.invalidateAll();
	}

	@Override
	public void refresh(String userId) {
		this.cacheFunc.invalidate(userId);
	}

}
