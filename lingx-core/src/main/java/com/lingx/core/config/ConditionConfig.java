package com.lingx.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lingx.support.database.ICondition;
import com.lingx.support.database.impl.CascadeRuleCondition;
import com.lingx.support.database.impl.ExtendCondition;
import com.lingx.support.database.impl.OrderCondition;
import com.lingx.support.database.impl.PageCondition;
import com.lingx.support.database.impl.RuleCondition;
import com.lingx.support.database.impl.ScopeCondition;
import com.lingx.support.database.impl.SreachCondition;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年8月18日 下午4:52:08 
 * 类说明 
 */
@Configuration
public class ConditionConfig {
	/**
	 * 级联条件:支持二级级联、三级级联 
	 * @return
	 */
	@Bean(name="cascadeRuleCondition")
	public ICondition getCascadeRuleCondition(){
		return new CascadeRuleCondition();
	}
	/**
	 * 继承条件：由于有数据权限，取消继承条件，但IEntity的条件脚本依然有效
	 * @return
	 */
	@Bean(name="extendCondition")
	public ICondition getExtendCondition(){
		return new ExtendCondition();
	}
	/**
	 * 数据权限 条件：在开发界面中设置权限规则
	 * @return
	 */
	@Bean(name="ruleCondition")
	public ICondition getRuleCondition(){
		return new RuleCondition();
	}
	/**
	 * 约束排斥条件：对话框列表选择控件中，只能显示已关联或未关联的列表，需要extparam参数
	 * @return
	 */
	@Bean(name="scopeCondition")
	public ICondition getScopeCondition(){
		return new ScopeCondition();
	}
	/**
	 * 查询条件：查询的参数名以 下划线开头"_"
	 * @return
	 */
	@Bean(name="sreachCondition")
	public ICondition getSreachCondition(){
		return new SreachCondition();
	}
	/**
	 * 排序条件：由于排序条件的处理比较特殊，所以必须放在条件列表的最后一个 或 倒数第二个
	 * @return
	 */
	@Bean(name="orderCondition")
	public ICondition getOrderCondition(){
		return new OrderCondition();
	}
	/**
	 * 分页条件：由于分页条件的处理比较特殊，所以必须放在条件列表的最后一个
	 * @return
	 */
	@Bean(name="pageCondition")
	public ICondition getPageCondition(){
		return new PageCondition();
	}
}
