package com.lingx.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lingx.core.model.IValidator;
import com.lingx.core.model.impl.ExpressionValidator;
import com.lingx.support.model.validator.EmailValidator;
import com.lingx.support.model.validator.NotNullValidator;
import com.lingx.support.model.validator.NumberBetweenValidator;
import com.lingx.support.model.validator.NumberMaxValidator;
import com.lingx.support.model.validator.NumberMinValidator;
import com.lingx.support.model.validator.NumberValidator;
import com.lingx.support.model.validator.RegexValidator;
import com.lingx.support.model.validator.StringBetweenSizeValidator;
import com.lingx.support.model.validator.StringMaxSizeValidator;
import com.lingx.support.model.validator.StringMinSizeValidator;
import com.lingx.support.model.validator.TreeFidValidator;
import com.lingx.support.model.validator.UniqueValidator;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年8月18日 下午4:49:55 
 * 类说明 
 */
@Configuration
public class ValidatorConfig {
	@Bean
	public IValidator getExpressionValidator(){
		ExpressionValidator ev=new ExpressionValidator();
		ev.setName("表达式验证");
		ev.setType("expression");
		return ev;
	}
	@Bean
	public IValidator getNotNullValidator(){
		NotNullValidator ev=new NotNullValidator();
		ev.setName("不为空验证");
		ev.setType("no_null");
		ev.setMessage("{0}不可为空");
		return ev;
	}
	@Bean
	public IValidator getRegexValidator(){
		RegexValidator ev=new RegexValidator();
		ev.setName("正则表达式");
		ev.setType("regex");
		return ev;
	}
	@Bean
	public IValidator getEmailValidator(){
		EmailValidator ev=new EmailValidator();
		ev.setName("电子邮箱验证");
		ev.setType("email");
		ev.setMessage("{0}不是有效的电子邮箱");
		return ev;
	}
	@Bean
	public IValidator getNumberBetweenValidator(){
		NumberBetweenValidator nbv=new NumberBetweenValidator();
		nbv.setName("数字在两数字之间");
		nbv.setType("NumberBetween");
		nbv.setMessage("{0}需不小于{1}不大于{2}");
		return nbv;
	}
	@Bean
	public IValidator getNumberMaxValidator(){
		NumberMaxValidator nmv=new NumberMaxValidator();
		nmv.setName("数字不大于{}");
		nmv.setType("NumberMax");
		nmv.setMessage("{0}不大于{1}");
		return nmv;
	}
	@Bean
	public IValidator getNumberMinValidator(){
		NumberMinValidator nmv=new NumberMinValidator();
		nmv.setName("数字不小于{}");
		nmv.setType("NumberMin");
		nmv.setMessage("{0}不小于{1}");
		return nmv;
	}
	@Bean
	public IValidator getNumberValidator(){
		NumberValidator nv=new NumberValidator();
		nv.setName("数字验证");
		nv.setType("Number");
		nv.setType("{0}不是有效数字");
		return nv;
	}
	@Bean
	public IValidator getStringBetweenSizeValidator(){
		StringBetweenSizeValidator v=new StringBetweenSizeValidator();
		v.setName("字符串长度在两数字之间");
		v.setType("StringBetweenSize");
		v.setMessage("{0}长度不小于{1}不大于{2}");
		return v;
	}
	@Bean
	public IValidator getStringMaxSizeValidator(){
		StringMaxSizeValidator v=new StringMaxSizeValidator();
		v.setName("字符串长度不大于{}");
		v.setType("StringMaxSize");
		v.setMessage("{0}长度不大于{1}");
		return v;
	}
	@Bean
	public IValidator getStringMinSizeValidator(){
		StringMinSizeValidator v=new StringMinSizeValidator();
		v.setName("字符串长度不小于{}");
		v.setType("StringMinSize");
		v.setMessage("{0}长度不小于{1}");
		return v;
	}
	@Bean
	public IValidator getUniqueValidator(){
		UniqueValidator v=new UniqueValidator();
		v.setName("唯一校验");
		v.setType("Unique");
		v.setMessage("{0}已存在，不可重复");
		return v;
	}
	@Bean
	public IValidator getTreeFidValidator(){
		TreeFidValidator v=new TreeFidValidator();
		v.setName("树型上级校验");
		v.setType("TreeFid");
		v.setMessage("上级无效");
		return v;
	}
}
