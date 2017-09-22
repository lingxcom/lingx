package com.lingx.core.service.impl.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.core.support.AbstractLobStreamingResultSetExtractor;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;

import com.lingx.core.model.IEntity;
import com.lingx.core.utils.Utils;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月10日 上午11:05:54 
 * 类说明 
 */
public class ModelIO {
	public static final String sql_insert = "insert into tlingx_model(id,model,ts) values(?,?,?)";
	public static final String sql_update = "update tlingx_model set model=?,ts=? where id=?";
	public static final String sql_select = "select model from tlingx_model where id=?";
	public static final String sql_delete = "delete tlingx_model where id=?";
	public static final String sql_count = "select count(*) from tlingx_model where id=?";
	/**
	 * 模型克隆
	 * @param object
	 * @return
	 */
	public static Object clone(Object object) {
		Object o = null;
		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(object);
			ByteArrayInputStream byteIn = new ByteArrayInputStream(
					byteOut.toByteArray());
			ObjectInputStream in = new ObjectInputStream(byteIn);
			o = in.readObject();
			out.close();
			in.close();
			byteOut.close();
			byteIn.close();
			return o;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 根据代码读取实体模型
	 * @param code
	 * @param jdbcTemplate
	 * @return
	 * @throws Exception
	 */
	public static IEntity readEntity(String code,JdbcTemplate jdbcTemplate) throws Exception {
		IEntity entity = null;
		final LobHandler lobHandler = new DefaultLobHandler();

		final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		if(jdbcTemplate.queryForInt(sql_count,code)==0)return null;
		jdbcTemplate.query(sql_select,
				new AbstractLobStreamingResultSetExtractor() {
					protected void streamData(ResultSet rs)
							throws SQLException, IOException,
							DataAccessException {

						try {
							InputStream is =lobHandler.getBlobAsBinaryStream(rs, 1);
							byte buff[]=new byte[1024];
							int len=0;
							while((len=is.read(buff))!=-1){
								byteOut.write(buff, 0, len);
							}
							is.close();
							/*
							FileCopyUtils.copy(
									lobHandler.getBlobAsBinaryStream(rs, 1),
									out);*/
						} catch (Exception e) {
							e.printStackTrace();
						}
						// FileCopyUtils.copy(lobHandler.getBlobAsBinaryStream(rs,1),out);

					}
				},code);
		ByteArrayInputStream byteIn = new ByteArrayInputStream(
				byteOut.toByteArray());
		ObjectInputStream in = new ObjectInputStream(byteIn);
		entity = (IEntity) in.readObject();
		//if("be516eac-aa22-4e16-9d46-cb34dc5713e5".equals(code))entity=null;
		in.close();
		byteOut.close();
		byteIn.close();
		return entity;
	}
	/**
	 * 保存实体模型
	 * @param code
	 * @param entity
	 * @param jdbcTemplate
	 * @throws Exception
	 */
	public static void writeEntity(IEntity entity,JdbcTemplate jdbcTemplate)
			throws Exception {
		final LobHandler lobHandler = new DefaultLobHandler();

		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(byteOut);
		out.writeObject(entity);
		final String code=entity.getCode();
		final int length = byteOut.toByteArray().length;
		
		final ByteArrayInputStream byteIn = new ByteArrayInputStream(
				byteOut.toByteArray());
		//final ObjectInputStream in = new ObjectInputStream(byteIn);
		if(jdbcTemplate.queryForInt(sql_count,code)!=0){
			jdbcTemplate.execute(sql_update,
				new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
					protected void setValues(PreparedStatement pstmt,
							LobCreator lobCreator) {
						try {
							lobCreator.setBlobAsBinaryStream(pstmt, 1, byteIn,
									length);
							pstmt.setString(2, Utils.getTime());
							pstmt.setString(3, code);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				});
		}else{
			jdbcTemplate
					.execute(sql_insert,
							new AbstractLobCreatingPreparedStatementCallback(
									lobHandler) {
								protected void setValues(
										PreparedStatement pstmt,
										LobCreator lobCreator) {
									try {
										pstmt.setString(1, code);
										lobCreator.setBlobAsBinaryStream(pstmt,
												2, byteIn, length);
										pstmt.setString(3, Utils.getTime());
									} catch (SQLException e) {
										e.printStackTrace();
									}
								}
							});
		}
		out.close();
		//in.close();
		byteOut.close();
		byteIn.close();
	}
	
	
	
	
	
	
	

	public static IEntity readDisk(String path) {
		IEntity entity=null;
		try {
			FileInputStream fos = new FileInputStream(path);
			ObjectInputStream oos = new ObjectInputStream(fos);
			entity=(IEntity)oos.readObject();
			oos.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;
	}
	public static void writeDisk(String path,IEntity entity) {
		try {
			File file=new File(path);
        	long lastModifiedTime=file.lastModified();
        	long currentTime=System.currentTimeMillis();
        	if(!file.exists())file.createNewFile();
        	
			FileOutputStream fos = new FileOutputStream(path);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(entity);
			oos.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
