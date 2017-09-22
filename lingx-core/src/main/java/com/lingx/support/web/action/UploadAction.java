package com.lingx.support.web.action;

import java.io.File;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.fastjson.JSON;
import com.lingx.core.action.IRequestAware;
import com.lingx.core.engine.IContext;
import com.lingx.core.service.ILingxService;
import com.lingx.core.service.IUploadService;
import com.lingx.core.utils.Utils;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年7月24日 下午4:17:56 
 * 类说明 
 */
public class UploadAction  extends AbstractJsonAction implements IRequestAware{

	private int maxSize=3;//最大3M //在配置文件中改
	private int type=1;//在配置文件中改
	private String allowed="png,jpg,jpeg,gif";//在配置文件中改
	private HttpServletRequest request;
	@Resource
	private ILingxService lingxService;
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Override
	public String execute(IContext context) {
		this.maxSize=this.lingxService.getConfigValue("lingx.upload.maxSize", 3*1024);
		this.allowed=this.lingxService.getConfigValue("lingx.upload.allowed", "png,jpg,jpeg,gif");
		Map<String,Object> ret=new HashMap<String,Object>();
		DiskFileItemFactory dfif = new DiskFileItemFactory();
		dfif.setSizeThreshold(4096);// 设置上传文件时用于临时存放文件的内存大小,这里是4K.多于的部分将临时存在硬盘
		File uploadDir=new File(request.getServletContext().getRealPath("upload"));
		if(!uploadDir.exists())uploadDir.mkdirs();
		dfif.setRepository(uploadDir);
		ServletFileUpload sfu = new ServletFileUpload(dfif);
		sfu.setHeaderEncoding("UTF-8");
		sfu.setSizeMax(maxSize  * 1024);// 设置最大上传尺寸
		try {
			List<FileItem> fileList = sfu.parseRequest(request);
			if (fileList == null || fileList.size() == 0) {
				ret.put("message","请选择上传文件");
				ret.put("code","-1");
				return JSON.toJSONString(ret);
			}
			/*
			if (fileList.size() >1) {
				ret.put("message","只支持单个文件上传");
				ret.put("code","-1");
				return JSON.toJSONString(ret);
			}*/
			FileItem fileItem=fileList.get(0);
			for(FileItem item:fileList){
				if("file".equalsIgnoreCase(item.getFieldName())){
					fileItem=item;
				}
			}
			//System.out.println("UploadAction fileItem.name:"+fileItem.getName());
			switch(type){
			case 1:
				this.save(fileItem, ret);
				break;
			case 2:
				IUploadService uploadService=null;
				if(lingxService.getSpringContext().getBeanNamesForType(IUploadService.class).length==0){
					ret.put("message","在Spring中找不到IUploadService的实现类");
					ret.put("code","-1");
				}else{
					uploadService=lingxService.getSpringContext().getBean(IUploadService.class);
					ret=uploadService.uploadFile(fileItem.getName(), fileItem.getSize(), fileItem.getInputStream());
				}
				break;
			}
			fileItem.delete();
			
			this.jdbcTemplate.update("insert into tlingx_file(name,path,suffix,length,user_id,upload_time) values(?,?,?,?,?,?)",
					fileItem.getName(),ret.get("path"),getSuffix(fileItem.getName()),ret.get("length"),context.getUserBean().getId(),Utils.getTime());
		} catch (Exception e) {// 处理文件尺寸过大异常
			e.printStackTrace();
			ret.put("message","文件尺寸超过规定大小:" + maxSize + "KB,或者是处理异常");
			ret.put("code","-1");
		}
		return JSON.toJSONString(ret);
	}

	@Override
	public void setRequest(HttpServletRequest request) {
		this.request=request;
	}
	
	private void save(FileItem fileItem,Map<String,Object> ret){
		Set<String> sets=new HashSet<String>();
		String array[]=allowed.split(",");
		for(String str:array){
			sets.add(str);
		}
		String path = fileItem.getName();
		// 得到去除路径的文件名
		String t_name = path.substring(path.lastIndexOf("\\") + 1);
		// 得到文件的扩展名(无扩展名时将得到全名)
		String t_ext = t_name.substring(t_name.lastIndexOf(".") + 1);
		if(sets.contains(t_ext)){
			long now = System.currentTimeMillis();
			// 根据系统时间生成上传后保存的文件名
			String prefix = String.valueOf(now);
			// 保存的最终文件完整路径,保存在web根目录下的upload目录下
			
			String u_name = request.getServletContext().getRealPath("/") + "upload/"+getDate()
					+ prefix + "." + t_ext;
			File dir=new File(request.getServletContext().getRealPath("/") + "upload/"+getDate());
			if(!dir.exists())dir.mkdirs();
			try {
				fileItem.write(new File(u_name));
				ret.put("message","文件上传成功. 已保存为: " + prefix + "." + t_ext
						+ " ；文件大小: " + fileItem.getSize()/1024 + "KB");
				ret.put("code","1");
				ret.put("length", fileItem.getSize()/1024);
				ret.put("path", "upload/"+getDate()
					+ prefix + "." + t_ext);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			ret.put("message",t_ext+"不可上传该类型文件");
			ret.put("code","-1");
		}
	}
	public static String getDate() {
		String dateTime = MessageFormat.format("{0,date,yyyy/MM/dd/}",
				new Object[] { new java.sql.Date(System.currentTimeMillis()) });
		return dateTime;

	}

	public String getSuffix(String name){
		String suffix="";
		if(name.indexOf(".")>0){
			suffix=name.substring(name.lastIndexOf(".")+1);
		}
		return suffix;
	}
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setAllowed(String allowed) {
		this.allowed = allowed;
	}

	public void setLingxService(ILingxService lingxService) {
		this.lingxService = lingxService;
	}
}
