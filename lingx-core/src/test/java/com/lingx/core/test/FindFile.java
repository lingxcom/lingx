package com.lingx.core.test;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.FileUtils;

/** 
 * @author www.lingx.me
 * @version 创建时间：2015年9月6日 下午5:27:19 
 * 类说明 
 */
public class FindFile {
	public static FileFilterImpl fileFilterImpl=new FileFilterImpl();
	public static void main(String[] args) {
		System.out.println("开始");
		String text="currentprice pricedisplay";
		String path="D:\\gy\\icreatek";
		try {
			test(text,new File(path));
			//System.out.println("index.php".endsWith(".php"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("结束");
	}

	public static void test(String search,File file)throws Exception{
		if(file.isDirectory()){
			File files[]=file.listFiles(fileFilterImpl);
			for(File f:files){
				//System.out.println(file.getAbsolutePath());
				test(search,f);
			}
		}else{//System.out.println(file.getAbsolutePath());
			String temp=FileUtils.readFileToString(file);
			//System.out.println(temp);
			if(temp.indexOf(search)!=-1){
				System.out.println(file.getAbsolutePath());
			}
		}
	}
	
	static class FileFilterImpl implements FileFilter{

		@Override
		public boolean accept(File arg0) {
			if(arg0.isDirectory())return true;
			String name=arg0.getName();
			//System.out.println(name);
			return name.endsWith("php");
		}
		
	}
}
