package com.lingx.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lingx.core.model.IInterpreter;
import com.lingx.core.model.impl.ExpressionInterpreter;
import com.lingx.support.model.interpreter.DateTime14Interpreter;
import com.lingx.support.model.interpreter.DateTimeToDateInterpreter;
import com.lingx.support.model.interpreter.Time4Interpreter;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年8月18日 下午4:52:43 
 * 类说明 
 */
@Configuration
public class InterpreterConfig {
	@Bean
	public IInterpreter getExpressionInterpreter(){
		ExpressionInterpreter ei=new ExpressionInterpreter();
		ei.setName("表达式解释");
		ei.setType("expression");
		return ei;
	}
	@Bean
	public IInterpreter getDateTime14Interpreter(){
		DateTime14Interpreter ei=new DateTime14Interpreter();
		ei.setName("日期时间14");
		ei.setType("datetime");
		return ei;
	}
	@Bean
	public IInterpreter getDateTimeToDateInterpreter(){
		DateTimeToDateInterpreter ei=new DateTimeToDateInterpreter();
		ei.setName("日期8");
		ei.setType("date");
		return ei;
	}
	@Bean
	public IInterpreter getTime4Interpreter(){
		Time4Interpreter ei=new Time4Interpreter();
		ei.setName("时间4");
		ei.setType("time4");
		return ei;
	}
	@Bean
	public IInterpreter getDT14BIInterpreter(){
		Time4Interpreter ei=new Time4Interpreter();
		ei.setName("智能输出DT14");
		ei.setType("DT14BI");
		return ei;
	}
}
