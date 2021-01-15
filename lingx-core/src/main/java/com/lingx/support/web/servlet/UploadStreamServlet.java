package com.lingx.support.web.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.lingx.core.service.ILingxService;
import com.lingx.core.service.IUpdateService;
import com.lingx.core.utils.MD5Utils;
import com.lingx.core.utils.Utils;


public class UploadStreamServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
    public UploadStreamServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println("UploadStreamServlet:"+request.getHeader("Lingx-Secret"));
		/**/
		ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
		ILingxService lingx=applicationContext.getBean(ILingxService.class);
		IUpdateService updateService=applicationContext.getBean(IUpdateService.class);
		
String lingxUpdateEnabled=lingx.getConfigValue("lingx.update.enabled","false");
String lingxUpdateSecret=lingx.getConfigValue("lingx.update.secret",lingx.uuid());
		if(!"true".equals(lingxUpdateEnabled)){
			System.out.println("Enabled is false.");
			response.getWriter().print("Enabled is false.");
			return;
		}
		if(!lingxUpdateSecret.equals(request.getHeader("Lingx-Secret"))){
			System.out.println("Secret is Error.");
			response.getWriter().print("Secret is Error.");
			return;}
		JdbcTemplate jdbc=applicationContext.getBean("jdbcTemplate",JdbcTemplate.class);
		Map<String,Object> ret=new HashMap<String,Object>();
		String prefix=String.valueOf(System.currentTimeMillis() );
		String filename=request.getHeader("filename");
		String appid=request.getHeader("Lingx-Appid");
		String content=request.getHeader("Lingx-Content");
		content=URLDecoder.decode(content,"UTF-8");
		//System.out.println("Lingx-Content:"+content);
		//filename=URLDecoder.decode(filename,"UTF-8");
		//String contentLength=request.getHeader("Content-Length");
		String t_ext = filename.substring(filename.lastIndexOf(".") + 1);
		InputStream in=request.getInputStream();
		BufferedInputStream bis=new BufferedInputStream(in);
		File dir=new File(request.getServletContext().getRealPath("/") + "upload/"+getDate());
		if(!dir.exists()){
			dir.mkdirs();
		}
		String xdlj="upload/"+getDate()+ prefix + "." + t_ext;;
		String path= request.getServletContext().getRealPath("/") +xdlj;
		FileOutputStream fos=new FileOutputStream(path);
		
		BufferedOutputStream bos=new BufferedOutputStream(fos);
		int fileLength=0;
		byte buffer[]=new byte[4096];
		int len=0;
		while((len=bis.read(buffer))!=-1){
			bos.write(buffer, 0, len);
			fileLength+=len;
		}bis.close();
		in.close();
		bos.flush();
		bos.close();
		fos.close();
		//ret.put("message","文件上传成功. 已保存为: " + prefix + "." + t_ext
		//		+ " ；文件大小: " + fileLength/1024 + "KB");
		String id=t_ext+fileLength+MD5Utils.getFileMD5String(new File(path));
		String url=getBasePath(request)+ xdlj;
		//String downloadUrl=getBasePath(request)+"download?id="+id;
		ret.put("code","1");
		ret.put("message","SUCCESS");
		ret.put("length", fileLength);
		ret.put("name", filename);
		//ret.put("path",downloadUrl);
		updateService.update(new File(path), request.getServletContext().getRealPath("/"), Utils.getTime());
		saveToDatabase(id, url, filename, fileLength, jdbc, new File(path),xdlj,content,xdlj,appid);
		response.setContentType("text/html;charset=UTF-8");  
		response.setStatus(HttpServletResponse.SC_OK);
	    //System.out.println(JSON.toJSONString(ret));
		response.getWriter().print("SUCCESS");
	}
	public static void saveToDatabase(String id,String url,String name,long length,JdbcTemplate jdbc,File file,String xdlj,String content,String downloadUrl,String appid){
		
		jdbc.update("insert into tlingx_update(content,length,path,ts,appid)values(?,?,?,?,?)",content,length,downloadUrl,Utils.getTime(),appid);
	}
	public static String getDate() {
		String dateTime = MessageFormat.format("{0,date,yyyy/MM/dd/}",
				new Object[] { new java.sql.Date(System.currentTimeMillis()) });
		return dateTime;

	}
	
	public static String getBasePath(HttpServletRequest request){
		String basePath = request.getScheme() + "://"
				+ request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/";
		return basePath;
	}

}
