package com.lingx.core.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

/** 
 * @author www.lingx.com
 * @version 创建时间：2016年8月22日 上午11:06:53 
 * 类说明 
 */
public class HttpUtils {

	public static String get(String httpUrl){
		StringBuilder sb=new StringBuilder();
		try{
			URL url = new URL(httpUrl);  
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
	        conn.setConnectTimeout(25000);
			conn.setReadTimeout(25000);
			HttpURLConnection.setFollowRedirects(true);
			// 请求方式
			conn.setRequestMethod("GET");

			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setRequestProperty("contentType", "UTF-8");
	       // conn.setRequestProperty("Connection", "Keep-Alive");  
			//conn.setDoOutput(true);
			//conn.setDoInput(true);
			//int len;
			//byte[] buff=new byte[4096];
	        InputStream in=conn.getInputStream();

	        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8")); 
	       /* while((len=in.read(buff))!=-1){//System.out.println(len);
	        	sb.append(new String(buff,0,len));
	        }*/
	        String line = "";  
	        while ((line = br.readLine()) != null){  
	        	sb.append(line);  
	        }  
	        br.close();
	        in.close();
	        conn.disconnect();
		}catch(Exception e){
			e.printStackTrace();
		}
		return sb.toString();
	}
	/**
	 * 未完善
	 * @param httpUrl
	 * @param params
	 * @return
	 */
	public static String post(String httpUrl,Map<String,Object> params){
		StringBuffer sb = new StringBuffer();  
		try{
		URL url = new URL(httpUrl);   
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();   
		  
		   
		// //设置连接属性   
		httpConn.setDoOutput(true);// 使用 URL 连接进行输出   
		httpConn.setDoInput(true);// 使用 URL 连接进行输入   
		httpConn.setUseCaches(false);// 忽略缓存   
		httpConn.setRequestMethod("POST");// 设置URL请求方法   
		//String requestString = "客服端要以以流方式发送到服务端的数据...";   
		  
		   
		// 设置请求属性   
		// 获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致   
		//byte[] requestStringBytes = requestString.getBytes(ENCODING_UTF_8);   
		//httpConn.setRequestProperty("Content-length", "" + requestStringBytes.length);   
		//httpConn.setRequestProperty("Content-Type", "application/octet-stream");   
		//httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接   
		httpConn.setRequestProperty("Charset", "UTF-8");   
		//   
		//String name = URLEncoder.encode("黄武艺", "utf-8");  
		/*for(String key:params.keySet()){
			httpConn.setRequestProperty(key,params.get(key).toString());   
		}
		  */
		   
		// 建立输出流，并写入数据   
		OutputStream outputStream = httpConn.getOutputStream();   
		//outputStream.write(requestStringBytes);  
		StringBuilder req=new StringBuilder();
		for(String key:params.keySet()){
			req.append(key).append("=").append(URLEncoder.encode(params.get(key).toString(), "UTF-8")).append("&");
		}
		if(req.length()>0){req=req.deleteCharAt(req.length()-1);}
		PrintWriter pw=new PrintWriter(outputStream);
		//System.out.println(req.toString());
		pw.print(req.toString());
		pw.flush();
		pw.close();
		outputStream.close();   
		// 获得响应状态   
		int responseCode = httpConn.getResponseCode();   
		  
		   
		if (HttpURLConnection.HTTP_OK == responseCode) {// 连接成功   
		// 当正确响应时处理数据    
		String readLine;   
		BufferedReader responseReader;   
		// 处理响应流，必须与服务器响应流输出的编码一致   
		 responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));   
		while ((readLine = responseReader.readLine()) != null) {   
		sb.append(readLine).append("\n");   
		}   
		responseReader.close();   
		//tv.setText(sb.toString());   
		}   
		} catch (Exception ex) {   
		ex.printStackTrace();   
		}   
		  return sb.toString();
	}
	
	public static void main(String args[]){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("func", "100025");
		map.put("userids", "02948982-6d62-4a4b-af3e-c59269994b1a");
		map.put("type", "1");
		map.put("title", "通知提醒");
		map.put("content", "有志者事竟成");
		map.put("handlerUrl", "app/notice/notice-detail.jsp?id="+1);
		
		map.put("version", "1.0.0");
		map.put("timestamp",System.currentTimeMillis());
		String content="";
		String url="http://cas.youngzit.com/api?param="+JSON.toJSONString(map)+"&appkey=4jZK7M0yuRMyJdwoWokD1kgBMtQpaetO";
		url="http://cas.youngzit.com/api?param={\"content\":\"我们都市\",\"func\":\"100025\",\"handlerUrl\":\"http://xt.youngzit.com:80/app/notice/notice-detail.jsp?id=61\",\"timestamp\":1481787044799,\"title\":\"测量\",\"type\":\"1\",\"userids\":\"0867038f-66a0-43d2-86cd-5c033e2f87ea,13b59764-6849-486f-82f5-cf7aba7618ff,1fb21ebe-22ea-4d52-8564-086bc118d13b,383a2b55-7867-44bb-a364-e73d964d3914,4f2b37c5-b374-46df-94e1-8886baa25020,54234d26-e814-4f0e-afca-2053ad62ec23,7d65f0f3-36fc-4ed5-8bd3-850d58c57117,997aa413-afc8-420c-8f08-f5d388f99f8f,a2202b5a-7869-4c28-a5ea-fd34a53ba75d,b2deeb91-7db5-49e0-aaea-1003bdcec9cb,c27f0c25-bf2d-4fe6-94ad-b02b1bc981aa,c6729a91-2753-4e87-8a90-cd934ad60757,df8c3791-ffc0-48a8-a25d-8f38361232eb,e76a9d5e-e3c0-4c99-9f74-765c74512912,f2b007b8-91bc-4233-afe8-55335c940270,\",\"version\":\"1.0.0\"}&appkey=4jZK7M0yuRMyJdwoWokD1kgBMtQpaetO";
		System.out.println(url);
		content=HttpUtils.get(url);
		System.out.println(content);
		

/**/		Map<String,Object> params=new HashMap<String,Object>();
		params.put("param", JSON.toJSONString(map));
		params.put("appkey", "4jZK7M0yuRMyJdwoWokD1kgBMtQpaetO");
		content=HttpUtils.post("http://tool.lu/js/ajax.html",params);
		System.out.println(content);
		
	}

}
