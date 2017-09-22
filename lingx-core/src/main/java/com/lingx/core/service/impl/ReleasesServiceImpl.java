package com.lingx.core.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.alibaba.fastjson.JSON;
import com.lingx.core.service.IReleasesService;
import com.lingx.core.utils.HttpUtils;

/** 
 * @author www.lingx.com
 * @version 创建时间：2017年5月1日 上午10:36:00 
 * 类说明 
 */
public class ReleasesServiceImpl implements IReleasesService {
	private String uglifyJavaScriptUrl="http://tool.lu/js/ajax.html";
	private String sourcePath;
	private String targetPath;
	private List<String> list;
	public static void main(String args[]){
		ReleasesServiceImpl rsi=new ReleasesServiceImpl();
		rsi.uglifyJavaScript();
	}
	public ReleasesServiceImpl(){
		list=new ArrayList<String>();
		list.add("lingx/js/rootApi.js");
		list.add("lingx/js/rootApi_mobile.js");
		list.add("lingx/js/lingx.js");
		list.add("lingx/js/lingx-ext.js");
		list.add("lingx/js/lingx-editor.js");
		list.add("lingx/js/lingx-editor2.js");
		list.add("lingx/js/lingx-editor3.js");
		list.add("lingx/js/template/edit.js");
		list.add("lingx/js/template/grid.js");
		list.add("lingx/js/template/grid_cascade.js");
		list.add("lingx/js/template/grid_combo2.js");
		list.add("lingx/js/template/tree.js");
		list.add("lingx/js/template/tree_cascade.js");
		list.add("lingx/js/template/tree_combo2.js");
		list.add("lingx/js/template/delete.js");
		list.add("lingx/js/template/view.js");
		list.add("lingx/model/editor/editor.js");
		list.add("lingx/workflow/define/workflow.js");
		sourcePath="E:\\lingx-new\\workspace\\lingx\\lingx-web\\src\\main\\webapp";
		targetPath="E:\\lingx-new\\workspace\\releases\\WebContent";
	}
	@Override
	public void uglifyJavaScript() {
		String temp;
		for(String str:list){
			try {
				temp=this.uglify(FileUtils.readFileToString(new File(this.sourcePath+"/"+str), "UTF-8"),str);
				//System.out.println("混淆后："+temp);
				FileUtils.write(new File(this.targetPath+"/"+str), temp, "UTF-8");
				Thread.currentThread().sleep(5000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private String uglify(String text,String file){
		try {
			Map<String,Object> params=new HashMap<String,Object>();
			params.put("code", text);
			params.put("operate", "uglify");
			String ret=HttpUtils.post(uglifyJavaScriptUrl, params);
			Map<String,Object> json=JSON.parseObject(ret);
			return json.get("text").toString();
		} catch (Exception e) {
			System.out.println("不能加密:"+file);
			return text;
		}
	}

}
