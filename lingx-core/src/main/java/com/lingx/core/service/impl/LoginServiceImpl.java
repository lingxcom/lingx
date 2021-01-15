package com.lingx.core.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.lingx.core.engine.IContext;
import com.lingx.core.service.ILingxService;
import com.lingx.core.service.ILoginService;
import com.lingx.core.utils.Utils;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年6月11日 下午10:26:49 
 * 类说明 
 */
@Component(value="lingxLoginService")
public class LoginServiceImpl implements ILoginService {

	@Resource(name="jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Resource
	private ILingxService lingxService;
	@Override
	public boolean before(String userid, String password, IContext context) {
		
		return true;
	}

	@Override
	public boolean login(String userid, String password, IContext context) {
		boolean b=false;
		int count = -1;
		String sql = "select * from tlingx_user where account=?";// ,pass.replace(" ",
		count = jdbcTemplate.queryForInt("select count(*) from tlingx_user where account=?",userid);
		if (count == 0) {
			return b;
		}
		count = -1;
		Map<String, Object> map = jdbcTemplate.queryForMap(sql,userid.trim());
		if (map != null) {
			String p = map.get("password").toString();
			String r = p.substring(32);
			password = Utils.md5(password)+r;
			b=(password.equals(p));
			if(b){
				this.jdbcTemplate.update("update tlingx_user set login_count=login_count+1,last_login_time=?,last_login_ip=? where account=?",Utils.getTime(),context.getRequest().getAttribute(IContext.CLIENT_IP),userid);
			}
		}
		
		return b;
	}

	@Override
	public String after(String userid, String password, IContext context) {
		String array[]=new String[]{"000000","111111","222222","333333","444444","555555","666666","777777","888888","999999","123456","123123"};
		if("true".equals(this.lingxService.getConfigValue("lingx.login.password.zero6", "false"))){
			for(String p:array){
			String zeroPwd=this.lingxService.passwordEncode(p, userid);
		
			String r = zeroPwd.substring(32);
			String temp = Utils.md5(password)+r;
			if(temp.equals(zeroPwd)){
				return this.lingxService.getConfigValue("lingx.login.password.edit", "d?c=password");
			}
			}
		}
		return null;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
