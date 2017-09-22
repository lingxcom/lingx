package com.lingx.plugin.aliyun.oss;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;


import org.springframework.stereotype.Component;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.lingx.core.utils.Utils;

/**
 * 在OSS的资源要设置为公共读
 * @author lingx
 *
 */
@Component
public class AliyunOssServiceImpl implements IAliyunOssService {
	private String accessId;
	private String accessKey;
	private String ossEndPoint;//http://oss-cn-qingdao.aliyuncs.com/ 青岛站点
	private String bucketName;
	private OSSClient client ;
	private Date expiration;
	
	public AliyunOssServiceImpl(){
		this.expiration= new Date(new Date().getTime() + 3600 * 1000);
	}
	public void init(){
		client= new OSSClient(ossEndPoint,accessId, accessKey);
	}
	@Override
	public String put(String fileName,long length, InputStream inputStream)
			throws Exception {
        // 创建上传Object的Metadata
        ObjectMetadata meta = new ObjectMetadata();
        // 必须设置ContentLength
        meta.setContentLength(length);
        fileName=getOOSFileName(fileName);
        if(isImageFile(fileName)){
        	//Image image = ImageIO.read(input);
		      BufferedImage  bufImg = ImageIO.read(inputStream);//把图片读入到内存中
		//压缩代码

		//bufImg = Thumbnails.of(bufImg).size(bufImg.getWidth(), bufImg.getHeight()).outputQuality(0.9).asBufferedImage();
		 ByteArrayOutputStream bos = new ByteArrayOutputStream();//存储图片文件byte数组
		  ImageOutputStream ios = ImageIO.createImageOutputStream(bos); 
		  String extName=getExtName(fileName);
		  ImageIO.write(bufImg,extName , ios); //图片写入到 ImageOutputStream
		  
		  InputStream input = new ByteArrayInputStream(bos.toByteArray());
		  meta.setContentLength(input.available());
		  meta.setContentType("image/*"); 
          PutObjectResult result = client.putObject(bucketName, fileName, input, meta);
          //input.close();
        }else{
        	  // 上传Object.
            PutObjectResult result = client.putObject(bucketName, fileName, inputStream, meta);
        }
      
        // 打印ETag
        //System.out.println(result.getETag());

        //URL url = client.generatePresignedUrl(bucketName, fileName, expiration); 有时效限制的访问地址
       
		return Utils.formatString(URL_TEMPLATE, ossEndPoint,this.bucketName,fileName);
	}

	@Override
	public OutputStream get(String fileName) {
		return null;
	}

	public static final String URL_TEMPLATE="{}{}/{}";
	public static final String CHAR_TEMPLATE="abcdefghijklmnopqrstuvwxyz1234567890";
	public static final int CHAE_TEMPLATE_LEGNTH=CHAR_TEMPLATE.length();
	public static final int OOS_NAME_LENGTH=32;
	public static final Random random=new Random();
	/**
	 * 根据后缀判断是否是图片文件
	 * 只支持jpg，测试发现png处理后，图片会变大
	 * @param file
	 * @return
	 */
	public static boolean isImageFile(String fileName){
		fileName=fileName.toLowerCase();
		//return fileName.endsWith(".jpg")||fileName.endsWith(".png")||fileName.endsWith(".gif");
		return fileName.endsWith(".jpg");
	}
	public static String getExtName(String fileName){
		fileName=fileName.toLowerCase();
		
		return new String(fileName.substring(fileName.lastIndexOf(".")+1));
	}
	public static void main(String args[]){
		try {
			String fileName="D:/001/bll.jpg";
			String fileName2="D:/001/bll_min.jpg";
			if(isImageFile(fileName)){
				File file=new File(fileName);
				File file2=new File(fileName2);
				FileInputStream inputStream=new FileInputStream(file);
				//Image image = ImageIO.read(input);
			      BufferedImage  bufImg = ImageIO.read(inputStream);//把图片读入到内存中
			//压缩代码

			//bufImg = Thumbnails.of(bufImg).size(bufImg.getWidth(), bufImg.getHeight()).outputQuality(0.9).asBufferedImage();
			FileOutputStream outputStream=new FileOutputStream(file2);
			  ImageOutputStream ios = ImageIO.createImageOutputStream(outputStream); 
			  ImageIO.write(bufImg, getExtName(fileName), ios); //图片写入到 ImageOutputStream
			  outputStream.close();
			 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("完成");
	}
	/**
	 * 获取文件名后缀
	 * @param name
	 * @return
	 */
	public static String getFixName(String name){
		return name.lastIndexOf(".")==-1?"":name.substring(name.lastIndexOf("."));
	      
	}
	/**
	 * 生成文件名
	 * @return
	 */
	public static String getOOSFileName(String name){
		StringBuilder sb=new StringBuilder();
		sb.append("f-").append(Utils.getTime()).append("-");
		for(int i=0;i<OOS_NAME_LENGTH;i++){
			sb.append(CHAR_TEMPLATE.charAt(random.nextInt(CHAE_TEMPLATE_LEGNTH)));
		}
		sb.append(getFixName(name));
		return sb.toString();
	}
	public void setAccessId(String accessId) {
		this.accessId = accessId;
	}
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	public void setOssEndPoint(String ossEndPoint) {
		this.ossEndPoint = ossEndPoint;
	}
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	public void setClient(OSSClient client) {
		this.client = client;
	}
	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}
	@Override
	public String put(String file) {
		
		return this.put(new File(file));
	}
	@Override
	public String put(File file) {
		String path=null;
		try {
			FileInputStream inputStream=new FileInputStream(file);
			path=this.put(file.getName(), file.length(), inputStream);
			if(inputStream!=null){
				inputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return path;
	}

}
