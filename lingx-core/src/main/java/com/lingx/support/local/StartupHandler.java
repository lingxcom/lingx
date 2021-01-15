package com.lingx.support.local;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/** 
* @author www.lingx.com
* @version 创建时间：2020年12月18日 上午10:26:07 
* 类说明 
*/
public class StartupHandler {

	public static void main(String[] args) {
		try {
			String localPath=args[0];
			String temp=FileUtils.readFileToString(new File(localPath+"/mysql/my-template.ini"));
			temp=temp.replace("${basePath}", localPath.replace('\\', '/'));
			FileUtils.write(new File(localPath+"/mysql/my.ini"), temp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
