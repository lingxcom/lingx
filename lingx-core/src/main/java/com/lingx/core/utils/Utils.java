package com.lingx.core.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.Random;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月5日 上午11:40:42 
 * 类说明 
 */
public class Utils {
	public static boolean isNotNull(String s) {
		return !isNull(s);
	}

	public static final boolean isNull(String s) {
		return s == null || "".equals(s.trim());
	}

	public static String md5(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes("UTF-8"));
			byte b[] = md.digest();

			int i;

			StringBuilder buf = new StringBuilder();
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return plainText;
		}
	}
	
	public static String md52(String temp){
		String rand=getRandomString(32).toLowerCase();
		String pass=(md5(temp));
		String t=pass+rand;
		return md5(t)+rand;
	}
	/**
	 * MD5加沙加密
	 * @param temp 明文
	 * @param rand 密文
	 * @return
	 */
	public static String md52(String temp,String rand){
		//String rand=getRandomString(32);
		String pass=(md5(temp));
		String t=pass+rand;
		return md5(t)+rand;
	}
	public static String getRandomString(int bit){
		Random random=new Random();
		StringBuilder sb=new StringBuilder();
		String temp="1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
		int max=temp.length();
		for(int i=0;i<bit;i++){
			int t=random.nextInt(max);
			sb.append(temp.charAt(t));
		}
		return sb.toString();
	}

	public static String getRandomNumber(int bit){
		Random random=new Random();
		StringBuilder sb=new StringBuilder();
		String temp="1234567890";
		int max=10;
		for(int i=0;i<bit;i++){
			int t=random.nextInt(max);
			sb.append(temp.charAt(t));
		}
		return sb.toString();
	}
	/**
	 * 时间 yyyyMMddHHmmss
	 * @return
	 */
	public static String getTime() {
		String dateTime = MessageFormat.format("{0,date,yyyyMMddHHmmss}",
				new Object[] { new java.sql.Date(System.currentTimeMillis()) });
		return dateTime;

	}
	public static String formatString(String template,Object... objects){
		for(Object obj : objects){
			if(obj==null)continue;
			if(obj.getClass().isArray()){
				Object array[]=(Object[])obj;
				for(Object o:array){
					if(o!=null)
					template=template.replaceFirst("\\{[^}]*\\}", o.toString());
				}
			}else{
				template=template.replaceFirst("\\{[^}]*\\}", obj.toString());
			}
		}
		return template;
	}
	/**
	 * LINGX加密方法
	 * @param password 密码明文
	 * @param userid 账号明文
	 * @return
	 */
	public static String lingxPasswordEncode(String password,String userid){
		userid=handlerUserid(userid);
		String pwdMd5=md5(password);
		String userIdMd5=md5(userid);
		
		return md5(md5(pwdMd5+userIdMd5))+userIdMd5;
	}
	
	private static String handlerUserid(String userid){
		StringBuilder sb=new StringBuilder();
		String temp="1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM_";
		for(int i=0;i<userid.length();i++){
			if(temp.indexOf(userid.charAt(i))>=0){
				sb.append(userid.charAt(i));
			}
			
		}
		return sb.toString();
	}
	public static void main(String args[]){
		System.out.println(lingxPasswordEncode("000000","lingx"));
		System.out.println(Utils.lingxPasswordEncode("963963", "川"));
	}
}
