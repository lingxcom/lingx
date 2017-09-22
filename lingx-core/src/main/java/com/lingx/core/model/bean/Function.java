package com.lingx.core.model.bean;

import java.util.HashMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONAware;
/**
 * 
*    
* 项目名称：lingx-core   
* 类名称：Function   
* 类描述：   
* 创建人：lingx   
* 创建时间：2015年6月18日 上午10:02:37   
* 修改人：lingx   
* 修改时间：2015年6月18日 上午10:02:37   
* 修改备注：   
* @version    
*
 */
public class Function implements JSONAware {
	private String functionString;
	  public static void main(String[] args) { 

		  Function click = new Function("function(){alert(1);}"); 

		   

		  HashMap<String, Object> json = new HashMap<String, Object>(); 

		  json.put("name", "tony"); 

		  json.put("click", click); 

		  String jsonString = JSON.toJSONString(json); 

		  System.out.println(jsonString); 

		  } 
	public Function() {

	}

	public Function(String functionString) {

		this.functionString = functionString;

	}

	public String toJSONString() {

		return this.functionString;

	}

	public String getFunctionString() {

		return functionString;

	}

	public void setFunctionString(String functionString) {

		this.functionString = functionString;

	}
}
