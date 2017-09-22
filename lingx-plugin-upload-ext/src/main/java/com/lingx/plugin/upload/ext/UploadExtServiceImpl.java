package com.lingx.plugin.upload.ext;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.core.service.IUploadService;
@Component(value="lingxUploadExtService")
public class UploadExtServiceImpl implements IUploadService, IUploadExtService {
	private String targetURL;
	@Resource
	private UploadExtPlugin plugin;
	@Override
	public void setTargetURL(String url) {
		this.targetURL=url;
	}

	@Override
	public Map<String, Object> uploadFile(String name, long size,
			InputStream inputStream) {
		Map<String, Object> ret=new HashMap<String,Object>();
		if(!plugin.isEnable()){
			ret.put("message","外部文档上传插件已禁用，不可上传");
			ret.put("code","-1");
			return ret;
		}
		String json=this.stream(inputStream,name, this.targetURL);
		System.out.println("uploadFile:"+json);
		return (Map<String, Object>)JSON.parse(json);
	}


	public static String stream(InputStream input,String filename,String httpUrl){
		StringBuilder sb=new StringBuilder();
		try{
			URL url = new URL(httpUrl);  
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
	        conn.setConnectTimeout(25000);
			conn.setReadTimeout(25000);
			HttpURLConnection.setFollowRedirects(true);
			// 请求方式
			conn.setRequestMethod("POST");
	        conn.setRequestProperty("Connection", "Keep-Alive");  
			conn.setDoOutput(true);
			conn.setDoInput(true);
			//System.out.println("stream:"+filename);
			conn.setRequestProperty("filename",URLEncoder.encode(filename,"UTF-8"));
			conn.setRequestProperty("Content-Length",String.valueOf(input.available()));
			conn.setRequestProperty("Content-Type", "application/octet-stream");
			OutputStream out=conn.getOutputStream();
	        byte[] buff=new byte[4096];
	        int len=-1;
	        while((len=input.read(buff))!=-1){
	        	out.write(buff,0,len);
	        }
	        
	        out.flush();
	        out.close();
	        input.close();
	        InputStream in=conn.getInputStream();
	        while((len=in.read(buff))!=-1){//System.out.println(len);
	        	sb.append(new String(buff,0,len));
	        }
	        in.close();
	        conn.disconnect();
		}catch(Exception e){
			e.printStackTrace();
		}
		return sb.toString();
	}
	
}
